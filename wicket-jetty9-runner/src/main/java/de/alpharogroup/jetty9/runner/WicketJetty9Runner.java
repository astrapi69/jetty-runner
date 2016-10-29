/**
 * Copyright (C) 2010 Asterios Raptis
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
package de.alpharogroup.jetty9.runner;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.jetty9.runner.config.FilterHolderConfiguration;
import de.alpharogroup.jetty9.runner.config.Jetty9RunConfiguration;
import de.alpharogroup.jetty9.runner.config.ServletContextHandlerConfiguration;
import de.alpharogroup.jetty9.runner.config.ServletHolderConfiguration;
import de.alpharogroup.jetty9.runner.config.StartConfig;
import de.alpharogroup.jetty9.runner.factories.DeploymentManagerFactory;
import de.alpharogroup.jetty9.runner.factories.ServletContextHandlerFactory;
import de.alpharogroup.jetty9.runner.factories.WicketServletContextHandlerFactory;
import de.alpharogroup.log.LoggerExtensions;

/**
 * The Class {@link WicketJetty9Runner}.
 */
public class WicketJetty9Runner
{

	/**
	 * The Constant WICKET_CONFIGURATION_KEY is the key for the system properties that defines the
	 * runtime configuration.
	 */
	public static final String WICKET_CONFIGURATION_KEY = "wicket.configuration";


	/**
	 * Run a jetty server with the given parameters.
	 *
	 * @param applicationClass
	 *            the application class
	 * @param webapp
	 *            the webapp
	 */
	public static void run(final Class<? extends Application> applicationClass, final File webapp)
	{
		run(applicationClass, webapp, 8080, 8443, "wicket");
	}

	/**
	 * Run a jetty server with the given parameters.
	 *
	 * @param applicationClass
	 *            the application class
	 * @param webapp
	 *            the webapp
	 * @param httpPort
	 *            the http port
	 * @param httpsPort
	 *            the https port
	 * @param keyStorePassword
	 *            the key store password
	 */
	public static void run(final Class<? extends Application> applicationClass, final File webapp,
		final int httpPort, final int httpsPort, final String keyStorePassword)
	{
		Jetty9Runner.runWithNewServer(
			WicketServletContextHandlerFactory.newServletContextHandler(applicationClass, webapp),
			httpPort, httpsPort);
	}


	/**
	 * Run a jetty server with the given {@link StartConfig} object.
	 *
	 * @param startConfig
	 *            the start config
	 */
	public static void run(final StartConfig startConfig)
	{
		WicketJetty9Runner.run(startConfig, new Server());
	}

	/**
	 * Run a jetty server with the given {@link StartConfig} object on the given {@link Server}
	 * object.
	 *
	 * @param startConfig
	 *            the start config
	 * @param server
	 *            the server
	 */
	public static void run(final StartConfig startConfig, final Server server)
	{

		System.setProperty(WICKET_CONFIGURATION_KEY, startConfig.getRuntimeConfigurationType());

		if (startConfig.getLogFile().exists()) {
			try {
				DeleteFileExtensions.delete(startConfig.getLogFile());
			} catch (final IOException e) {
				Logger.getRootLogger().error("logfile could not deleted.", e);
			}
		}
		// Add a file appender to the logger programatically
		LoggerExtensions.addFileAppender(Logger.getRootLogger(),
			LoggerExtensions.newFileAppender(startConfig.getAbsolutePathFromLogfile()));

		final ContextHandlerCollection contexts = new ContextHandlerCollection();

		final ServletContextHandler servletContextHandler = ServletContextHandlerFactory
			.getNewServletContextHandler(ServletContextHandlerConfiguration.builder()
				.parent(contexts)
				.filterHolderConfiguration(FilterHolderConfiguration.builder()
					.filterClass(WicketFilter.class).filterPath(startConfig.getFilterPath())
					.initParameter(WicketFilter.FILTER_MAPPING_PARAM, startConfig.getFilterPath())
					.initParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM,
						startConfig.getApplicationName())
					.build())
				.servletHolderConfiguration(
					ServletHolderConfiguration.builder().servletClass(DefaultServlet.class)
					.pathSpec(startConfig.getFilterPath()).build())
				.contextPath(startConfig.getContextPath()).webapp(startConfig.getWebapp())
				.maxInactiveInterval(startConfig.getSessionTimeout())
				.filterPath(startConfig.getFilterPath()).build());

		final DeploymentManager deployer = DeploymentManagerFactory.newDeploymentManager(contexts,
			startConfig.getWebapp().getAbsolutePath(), null);

		final Jetty9RunConfiguration configuration = Jetty9Runner
			.newJetty9RunConfiguration(servletContextHandler, contexts, deployer, startConfig);
		Jetty9Runner.runServletContextHandler(server, configuration);
	}

}
