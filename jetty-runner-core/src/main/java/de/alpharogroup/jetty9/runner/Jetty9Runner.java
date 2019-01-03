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
package de.alpharogroup.jetty9.runner;


import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;

import javax.management.MBeanServer;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.search.PathFinder;
import de.alpharogroup.jetty9.runner.config.Jetty9RunConfiguration;
import de.alpharogroup.jetty9.runner.config.StartConfig;
import de.alpharogroup.jetty9.runner.factories.ConfigurationFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

/**
 * The class {@link Jetty9Runner}
 */
@UtilityClass
@Log
public class Jetty9Runner
{
	public static final String HTTPS = "https";

	/**
	 * Gets the log file.
	 *
	 * @param projectDirectory
	 *            the project directory
	 * @param logFileName
	 *            the log file name
	 * @return the log file
	 */
	public static File getLogFile(final File projectDirectory, final String logFileName)
	{
		final File logfile = new File(projectDirectory, logFileName);
		if (logfile.exists())
		{
			try
			{
				DeleteFileExtensions.delete(logfile);
			}
			catch (final IOException e)
			{
				log.log(Level.SEVERE, "logfile could not deleted.", e);
			}
		}
		return logfile;
	}

	/**
	 * Gets the web app context.
	 *
	 * @param server
	 *            the server
	 * @param projectname
	 *            the projectname
	 * @return the web app context
	 */
	public static WebAppContext getWebAppContext(final Server server, final String projectname)
	{
		final File webapp = PathFinder.getProjectDirectory();
		final File wa = PathFinder.getRelativePath(webapp, projectname, "src", "main", "webapp");
		final WebAppContext webAppContext = new WebAppContext();
		webAppContext.setServer(server);
		webAppContext.setContextPath("/");
		webAppContext.setWar(wa.getAbsolutePath());
		return webAppContext;
	}

	/**
	 * Gets the webapp directory.
	 *
	 * @param projectDirectory
	 *            the project directory
	 * @param projectName
	 *            the project name
	 * @return the webapp directory
	 */
	public static File getWebappDirectory(final File projectDirectory, final String projectName)
	{
		final File webapp;
		if (projectDirectory.getAbsolutePath().endsWith(projectName))
		{
			webapp = PathFinder.getRelativePath(projectDirectory, "src", "main", "webapp");
		}
		else
		{
			webapp = PathFinder.getRelativePath(projectDirectory, projectName, "src", "main",
				"webapp");
		}
		return webapp;
	}

	/**
	 * Factory method for create the {@link Jetty9RunConfiguration} and set the ports from the
	 * config file or take the default if in config file is not set.
	 *
	 * @param servletContextHandler
	 *            the servlet context handler
	 * @param contexts
	 *            the contexts
	 * @param deployer
	 *            the deployer
	 * @param startConfig
	 *            the start config
	 * @return the new {@link Jetty9RunConfiguration}.
	 */
	public static Jetty9RunConfiguration newJetty9RunConfiguration(
		final ServletContextHandler servletContextHandler, final ContextHandlerCollection contexts,
		final DeploymentManager deployer, final StartConfig startConfig)
	{
		final Jetty9RunConfiguration configuration = Jetty9RunConfiguration.builder()
			.servletContextHandler(servletContextHandler).contexts(contexts).deployer(deployer)
			.httpPort(startConfig.getHttpPort()).httpsPort(startConfig.getHttpsPort())
			.keyStorePassword(startConfig.getKeyStorePassword())
			.keyStorePathResource(startConfig.getKeyStorePathResource()).build();
		return configuration;
	}

	/**
	 * Run a jetty server with the given parameters.
	 *
	 * @param config
	 *            the config
	 */
	public static void run(final Jetty9RunConfiguration config)
	{
		final Server server = new Server();
		run(server, config);
	}

	/**
	 * Run a jetty server with the given parameters.
	 *
	 * @param server
	 *            the server
	 * @param config
	 *            the config
	 */
	public static void run(final Server server, final Jetty9RunConfiguration config)
	{
		final HttpConfiguration httpsConfiguration = ConfigurationFactory
			.newHttpConfiguration(HTTPS, config.getHttpsPort(), 32768);

		final ServerConnector http = ConfigurationFactory.newServerConnector(server,
			httpsConfiguration, config.getHttpPort(), (1000 * 60 * 60));

		server.addConnector(http);
		if ((config.getKeyStorePathResource() != null)
			&& !config.getKeyStorePathResource().isEmpty())
		{
			final Resource keystore = Resource
				.newClassPathResource(config.getKeyStorePathResource());
			if ((keystore != null) && keystore.exists())
			{
				// if a keystore for a SSL certificate is available, start a SSL
				// connector on port 'httpsPort'.
				// By default, the quickstart comes with a Apache Wicket Quickstart
				// Certificate that expires about half way september 2021. Do not
				// use this certificate anywhere important as the passwords are
				// available in the source.

				final SslContextFactory sslContextFactory = ConfigurationFactory
					.newSslContextFactory(keystore, config.getKeyStorePassword(),
						config.getKeyStorePassword());


				final HttpConfiguration httpsConfig = new HttpConfiguration(httpsConfiguration);
				httpsConfig.addCustomizer(new SecureRequestCustomizer());

				final ServerConnector https = ConfigurationFactory.newServerConnector(server,
					new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
					httpsConfig, config.getHttpsPort(), 500000);

				server.addConnector(https);

				log.info(
					"***************************************************************************");
				log.info("**  SSL access to the application has been enabled on port "
					+ config.getHttpsPort() + ".         **");
				log.info("**  You can access the application using SSL on https://localhost:"
					+ config.getHttpsPort() + ".  **");
				log.info(
					"***************************************************************************");

			}
			else
			{
				log.severe("*****************************************************");
				log.severe("**  Keystore is null. Provide a keystore for ssh.  **");
				log.severe("*****************************************************");
			}
		}
		else
		{
			log.info("***************************************************");
			log.info("**  Keystore path is null. You can not use ssh.  **");
			log.info("***************************************************");
		}

		if (config.getHandlers() == null)
		{
			config.setHandlers(new HandlerCollection());
		}
		if (config.getContexts() == null)
		{
			config.setContexts(new ContextHandlerCollection());
		}
		config.getHandlers()
			.setHandlers(new Handler[] { config.getContexts(), new DefaultHandler() });

		server.setHandler(config.getHandlers());

		if (config.getDeployer() != null)
		{
			server.addBean(config.getDeployer());
		}

		final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		final MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		server.addEventListener(mBeanContainer);
		server.addBean(mBeanContainer);

		try
		{
			server.start();
			server.join();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			System.exit(100);
		}
	}

	/**
	 * Run a jetty server with the given parameters.
	 *
	 * @param server
	 *            the server
	 * @param servletContextHandler
	 *            the servlet context handler
	 * @param httpPort
	 *            the http port
	 * @param httpsPort
	 *            the https port
	 * @param keyStorePassword
	 *            the key store password
	 * @param keyStorePathResource
	 *            the key store path resource
	 */
	public static void run(final Server server, final ServletContextHandler servletContextHandler,
		final int httpPort, final int httpsPort, final String keyStorePassword,
		final String keyStorePathResource)
	{
		run(server,
			Jetty9RunConfiguration.builder().servletContextHandler(servletContextHandler)
				.httpPort(httpPort).httpsPort(httpsPort).keyStorePassword(keyStorePassword)
				.keyStorePathResource(keyStorePathResource).build());
	}

	/**
	 * Run a jetty server with the given parameters.
	 *
	 * @param servletContextHandler
	 *            the servlet context handler
	 * @param httpPort
	 *            the http port
	 * @param httpsPort
	 *            the https port
	 * @param keyStorePassword
	 *            the key store password
	 */
	public static void run(final ServletContextHandler servletContextHandler, final int httpPort,
		final int httpsPort, final String keyStorePassword)
	{
		final Server server = new Server();
		run(server, servletContextHandler, httpPort, httpsPort, keyStorePassword, "/keystore");
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
		if (startConfig.getLogFile().exists())
		{
			try
			{
				DeleteFileExtensions.delete(startConfig.getLogFile());
			}
			catch (final IOException e)
			{
				log.log(Level.SEVERE, "logfile could not deleted.", e);
			}
		}
	}

	/**
	 * Run a jetty server with the given parameters.
	 *
	 * @param server
	 *            the server
	 * @param config
	 *            the config
	 */
	public static void runServletContextHandler(final Server server,
		final Jetty9RunConfiguration config)
	{
		final HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme(HTTPS);
		http_config.setSecurePort(config.getHttpsPort());
		http_config.setOutputBufferSize(32768);

		final ServerConnector http = new ServerConnector(server,
			new HttpConnectionFactory(http_config));
		http.setPort(config.getHttpPort());
		http.setIdleTimeout(1000 * 60 * 60);

		server.addConnector(http);
		if ((config.getKeyStorePathResource() != null)
			&& !config.getKeyStorePathResource().isEmpty())
		{
			final Resource keystore = Resource
				.newClassPathResource(config.getKeyStorePathResource());
			if ((keystore != null) && keystore.exists())
			{
				// if a keystore for a SSL certificate is available, start a SSL
				// connector on port 'httpsPort'.
				// By default, the quickstart comes with a Apache Wicket Quickstart
				// Certificate that expires about half way september 2021. Do not
				// use this certificate anywhere important as the passwords are
				// available in the source.

				final SslContextFactory sslContextFactory = new SslContextFactory();
				sslContextFactory.setKeyStoreResource(keystore);
				sslContextFactory.setKeyStorePassword(config.getKeyStorePassword());
				sslContextFactory.setKeyManagerPassword(config.getKeyStorePassword());

				final HttpConfiguration https_config = new HttpConfiguration(http_config);
				https_config.addCustomizer(new SecureRequestCustomizer());

				final ServerConnector https = new ServerConnector(server,
					new SslConnectionFactory(sslContextFactory, "http/1.1"),
					new HttpConnectionFactory(https_config))
				{

				};
				https.setPort(config.getHttpsPort());
				https.setIdleTimeout(500000);

				server.addConnector(https);

				log.info(
					"***************************************************************************");
				log.info("**  SSL access to the application has been enabled on port "
					+ config.getHttpsPort() + ".         **");
				log.info("**  You can access the application using SSL on https://localhost:"
					+ config.getHttpsPort() + ".  **");
				log.info(
					"***************************************************************************");

			}
			else
			{
				log.severe("*****************************************************");
				log.severe("**  Keystore is null. Provide a keystore for ssh.  **");
				log.severe("*****************************************************");
			}
		}
		else
		{
			log.info("***************************************************");
			log.info("**  Keystore path is null. You can not use ssh.  **");
			log.info("***************************************************");
		}


		server.setHandler(config.getServletContextHandler());

		final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		final MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		server.addEventListener(mBeanContainer);
		server.addBean(mBeanContainer);

		try
		{
			server.start();
			server.join();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			System.exit(100);
		}
	}

	/**
	 * Run with new server.
	 *
	 * @param servletContextHandler
	 *            the servlet context handler
	 * @param httpPort
	 *            the http port
	 * @param httpsPort
	 *            the https port
	 */
	public static void runWithNewServer(final ServletContextHandler servletContextHandler,
		final int httpPort, final int httpsPort)
	{
		runWithNewServer(servletContextHandler, httpPort, httpsPort, "wicket");
	}


	/**
	 * Run with new server.
	 *
	 * @param servletContextHandler
	 *            the servlet context handler
	 * @param httpPort
	 *            the http port
	 * @param httpsPort
	 *            the https port
	 * @param keyStorePassword
	 *            the key store password
	 */
	public static void runWithNewServer(final ServletContextHandler servletContextHandler,
		final int httpPort, final int httpsPort, final String keyStorePassword)
	{
		final Server server = new Server();
		run(server, servletContextHandler, httpPort, httpsPort, keyStorePassword, "/keystore");
	}
}
