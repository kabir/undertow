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

package tmp.texugo;

import java.io.IOException;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Logger;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;
import org.xnio.IoFuture;

/**
 * log messages start at 5000
 *
 * @author Stuart Douglas
 */
@MessageLogger(projectCode = "TEXUGO")
public interface TexugoLogger extends BasicLogger {

    TexugoLogger ROOT_LOGGER = Logger.getMessageLogger(TexugoLogger.class, TexugoLogger.class.getPackage().getName());

    TexugoLogger REQUEST_LOGGER = Logger.getMessageLogger(TexugoLogger.class, TexugoLogger.class.getPackage().getName()+".request");

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 5000, value = "HttpServerExchange.getRequestChannel() has been called without also calling " +
            "HttpServerExchange.getResponseChannel(), the request is going to be automatically closed which will " +
            "cancel any async reads taking place on the request Channel")
    void getRequestCalledWithoutGetResponse();

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 5001, value = "An exception occurred processing the request")
    void exceptionProcessingRequest(@Cause Throwable cause);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 5002, value = "An exception occurred closing the response channel")
    void errorClosingResponseChannel(@Cause Exception e);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 5003, value = "An exception occurred getting the session")
    void getSessionFailed(@Cause IOException exception);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 5004, value = "Unexpected state in session callback %s")
    void unexpectedStatusGettingSession(IoFuture.Status status);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 5005, value = "Could not send session cookie as response has already started")
    void couldNotSendSessionCookieAsResponseAlreadyStarted();

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 5006, value = "Could not invalidate session cookie as response has already started")
    void couldNotInvalidateSessionCookieAsResponseAlreadyStarted();

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 5007, value = "Could not find session cookie config in the request, session will not be persistent across requests")
    void couldNotFindSessionCookieConfig();
}