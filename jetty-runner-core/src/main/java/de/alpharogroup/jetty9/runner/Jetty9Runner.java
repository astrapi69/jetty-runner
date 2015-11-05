package de.alpharogroup.jetty9.runner;


import java.io.File;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

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

import de.alpharogroup.file.search.PathFinder;
import de.alpharogroup.jetty9.runner.config.Jetty9RunConfiguration;

/**
 * The Class {@link Jetty9Runner}.
 */
public class Jetty9Runner
{

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
		final HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
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
				System.out.println(
					"SSL access to the examples has been enabled on port " + config.getHttpsPort());
				System.out.println("You can access the application using SSL on https://localhost:"
					+ config.getHttpsPort());
				System.out.println();
			}
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
		http_config.setSecureScheme("https");
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
				System.out.println(
					"SSL access to the examples has been enabled on port " + config.getHttpsPort());
				System.out.println("You can access the application using SSL on https://localhost:"
					+ config.getHttpsPort());
				System.out.println();
			}
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
