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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a servlet deployment.
 *
 * @author Stuart Douglas
 */
public class DeploymentInfo {

    private final String deploymentName;
    private final String contextName;
    private final ClassLoader classLoader;
    private final URL webContextRoot;
    private final List<ServletInfo> servlets;

    DeploymentInfo(final String deploymentName, final String contextName, final ClassLoader classLoader, final URL webContextRoot, final List<ServletInfo> servlets) {
        this.deploymentName = deploymentName;
        this.contextName = contextName;
        this.classLoader = classLoader;
        this.webContextRoot = webContextRoot;
        this.servlets = Collections.unmodifiableList(new ArrayList<ServletInfo>(servlets));
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public String getContextName() {
        return contextName;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public URL getWebContextRoot() {
        return webContextRoot;
    }

    public List<ServletInfo> getServlets() {
        return servlets;
    }

    public static DeploymentInfoBuilder builder() {
        return new DeploymentInfoBuilder();
    }

    public static class DeploymentInfoBuilder {

        private String deploymentName;
        private String contextName;
        private ClassLoader classLoader;
        private URL webContextRoot;
        private final List<ServletInfo> servlets = new ArrayList<ServletInfo>();

        DeploymentInfoBuilder() {

        }

        public String getDeploymentName() {
            return deploymentName;
        }

        public DeploymentInfoBuilder setDeploymentName(final String deploymentName) {
            this.deploymentName = deploymentName;
            return this;
        }

        public String getContextName() {
            return contextName;
        }

        public DeploymentInfoBuilder setContextName(final String contextName) {
            this.contextName = contextName;
            return this;
        }

        public ClassLoader getClassLoader() {
            return classLoader;
        }

        public DeploymentInfoBuilder setClassLoader(final ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public URL getWebContextRoot() {
            return webContextRoot;
        }

        public DeploymentInfoBuilder setWebContextRoot(final URL webContextRoot) {
            this.webContextRoot = webContextRoot;
            return this;
        }

        public DeploymentInfoBuilder addServlet(final ServletInfo servlet) {
            servlets.add(servlet);
            return this;
        }
    }

}
