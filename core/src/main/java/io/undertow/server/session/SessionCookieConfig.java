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

package io.undertow.server.session;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.AttachmentKey;

/**
 * Encapsulation of session cookie configuration. This removes the need for the session manager to
 * know about cookie configuration.
 *
 * @author Stuart Douglas
 */
public class SessionCookieConfig {

    public static final AttachmentKey<SessionCookieConfig> ATTACHMENT_KEY = AttachmentKey.create(SessionCookieConfig.class);

    public static final String DEFAULT_SESSION_ID = "JSESSIONID";

    private final String cookieName;
    private final String path;
    private final String domain;
    private final boolean discard;
    private final boolean secure;


    public SessionCookieConfig(final String cookieName, final String path, final String domain, final boolean discard, final boolean secure) {
        this.cookieName = cookieName;
        this.path = path;
        this.domain = domain;
        this.discard = discard;
        this.secure = secure;
    }


    public void setSessionCookie(final HttpServerExchange exchange, final Session session) {
        Cookie cookie = new CookieImpl(cookieName, session.getId())
                .setPath(path)
                .setDomain(domain)
                .setDiscard(discard)
                .setSecure(secure);
        CookieImpl.addResponseCookie(exchange, cookie);

    }

    public void clearCookie(final HttpServerExchange exchange, final Session session) {
        Cookie cookie = new CookieImpl(cookieName, session.getId())
                .setPath(path)
                .setDomain(domain)
                .setDiscard(discard)
                .setSecure(secure)
                .setMaxAge(0);
        CookieImpl.addResponseCookie(exchange, cookie);
    }
}
