/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.alpharogroup.jetty9.runner.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Run Configuration for jetty 9.
 * <p>
 * This class is a holder of the run configuration for jetty 9.
 * </p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jetty9RunConfiguration
{
	/** The servlet context handler. */
	private ServletContextHandler servletContextHandler;

	/** The http port. */
	private int httpPort;

	/** The https port. */
	private int httpsPort;

	/** The key store path resource. */
	private String keyStorePathResource;

	/** The key store password. */
	private String keyStorePassword;

	/** The handlers. */
	private HandlerCollection handlers;

	/** The contexts. */
	private ContextHandlerCollection contexts;

	/** The deployer. */
	private DeploymentManager deployer;

	/** The http configuration. */
	private HttpConfiguration httpConfiguration;
}
