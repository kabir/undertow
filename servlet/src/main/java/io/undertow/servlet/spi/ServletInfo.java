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

package io.undertow.servlet.spi;

/**
 * @author Stuart Douglas
 */
public class ServletInfo {

    private final String servletClass;
    private final String name;

    ServletInfo(final String servletClass, final String name) {
        this.servletClass = servletClass;
        this.name = name;
    }

    public String getServletClass() {
        return servletClass;
    }

    public String getName() {
        return name;
    }

    public ServletInfoBuilder builder() {
        return new ServletInfoBuilder();
    }

    public static class ServletInfoBuilder {
        private String servletClass;
        private String name;

        ServletInfoBuilder() {

        }

        public ServletInfo build() {
            return new ServletInfo(servletClass, name);
        }

        public String getName() {
            return name;
        }

        public ServletInfoBuilder setName(final String name) {
            this.name = name;
            return this;
        }

        public String getServletClass() {
            return servletClass;
        }

        public ServletInfoBuilder setServletClass(final String servletClass) {
            this.servletClass = servletClass;
            return this;
        }
    }
}
