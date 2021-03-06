/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.undertow.websockets.handler;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.undertow.UndertowLogger;
import io.undertow.server.HttpCompletionHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import io.undertow.websockets.WebSocketChannel;
import io.undertow.websockets.WebSocketHandshakeException;
import io.undertow.websockets.WebSocketLogger;
import io.undertow.websockets.protocol.Handshake;
import io.undertow.websockets.protocol.version00.Hybi00Handshake;
import io.undertow.websockets.protocol.version07.Hybi07Handshake;
import io.undertow.websockets.protocol.version08.Hybi08Handshake;
import io.undertow.websockets.protocol.version13.Hybi13Handshake;
import org.xnio.IoFuture;

/**
 * {@link HttpHandler} which will process the {@link HttpServerExchange} and do the actual handshake/upgrade
 * to WebSocket.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public class WebSocketProtocolHandshakeHandler implements HttpHandler {
    private final String websocketPath;
    private final List<Handshake> handshakes;

    private final WebSocketConnectionCallback callback;

    /**
     * Create a new {@link WebSocketProtocolHandshakeHandler}
     *
     * @param websocketPath The path which is used to serve the WebSocket requests
     * @param callback
     */
    public WebSocketProtocolHandshakeHandler(String websocketPath, final WebSocketConnectionCallback callback) {
        this.websocketPath = websocketPath;
        this.callback = callback;
        List<Handshake> handshakes = new ArrayList<Handshake>();
        handshakes.add(new Hybi13Handshake());
        handshakes.add(new Hybi08Handshake());
        handshakes.add(new Hybi07Handshake());
        handshakes.add(new Hybi00Handshake());
        this.handshakes = handshakes;
    }


    /**
     * Create a new {@link WebSocketProtocolHandshakeHandler}
     *
     * @param websocketPath The path which is used to serve the WebSocket requests
     * @param handshakes    The supported handshake methods
     * @param callback
     */
    public WebSocketProtocolHandshakeHandler(String websocketPath, List<Handshake> handshakes, final WebSocketConnectionCallback callback) {
        this.websocketPath = websocketPath;
        this.callback = callback;
        this.handshakes = new ArrayList<Handshake>(handshakes);
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange, final HttpCompletionHandler completionHandler) {
        if (!exchange.getRequestMethod().equals(Methods.GET)) {
            // Only GET is supported to start the handshake
            exchange.setResponseCode(403);
            completionHandler.handleComplete();
            return;
        }
        Handshake handshaker = null;
        for (Handshake method : handshakes) {
            if (method.matches(exchange)) {
                handshaker = method;
                break;
            }
        }

        if (handshaker == null) {
            UndertowLogger.REQUEST_LOGGER.debug("Could not find hand shaker for web socket request");
            exchange.setResponseCode(403);
            completionHandler.handleComplete();
            return;
        }


        try {
            IoFuture<WebSocketChannel> future = handshaker.handshake(exchange);
            future.addNotifier(new IoFuture.Notifier<WebSocketChannel, Object>() {
                @Override
                public void notify(final IoFuture<? extends WebSocketChannel> ioFuture, final Object attachment) {
                    try {
                        callback.onConnect(exchange, ioFuture.get());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        completionHandler.handleComplete();
                    }
                }
            }, null);
            // After the handshake was complete we are now have the connection upgraded to WebSocket and no futher HTTP processing will take place.
        } catch (WebSocketHandshakeException e) {
            exchange.setResponseCode(500);
            completionHandler.handleComplete();
            WebSocketLogger.REQUEST_LOGGER.webSocketHandshakeFailed(e);
        }

    }

    /**
     * Get the proper WebSocket location
     *
     * @param exchange The {@link HttpServerExchange} which is used to do the upgrade.
     * @param path     The path which is used for serve WebSockets
     * @return location        The complete location for WebSockets
     */
    private static String getWebSocketLocation(HttpServerExchange exchange, String path) {
        String protocol = "ws";
        if (exchange.getRequestScheme().equalsIgnoreCase("https")) {
            // SSL in use so use Secure WebSockets
            protocol = "wss";
        }
        // TODO: Store the header names somewhere global and use these static fields to lookup.
        return protocol + "://" + exchange.getRequestHeaders().getLast(Headers.HOST) + path;
    }

}
