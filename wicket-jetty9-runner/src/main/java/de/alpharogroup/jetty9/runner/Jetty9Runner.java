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
import java.lang.management.ManagementFactory;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.MBeanServer;
import javax.servlet.DispatcherType;

import de.alpharogroup.file.search.PathFinder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.util.lang.Generics;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.PropertiesConfigurationManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
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
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import de.alpharogroup.jetty9.runner.config.FilterHolderConfiguration;
import de.alpharogroup.jetty9.runner.config.Jetty9RunConfiguration;
import de.alpharogroup.jetty9.runner.config.ServletContextHandlerConfiguration;
import de.alpharogroup.jetty9.runner.config.ServletHolderConfiguration;

public class Jetty9Runner
{

	public static void run(Class<? extends Application> applicationClass, File webapp)
	{
		run(applicationClass, webapp, 8080, 8443, "wicket");
	}

	public static void run(Class<? extends Application> applicationClass, File webapp,
		int httpPort, int httpsPort, String keyStorePassword)
	{
		runWithNewServer(getServletContextHandler(applicationClass, webapp), httpPort, httpsPort);
	}

	public static void runWithNewServer(ServletContextHandler servletContextHandler, int httpPort,
		int httpsPort)
	{
		runWithNewServer(servletContextHandler, httpPort, httpsPort, "wicket");
	}

	public static void run(Jetty9RunConfiguration config)
	{
		Server server = new Server();
		run(server, config);
	}

	public static void runWithNewServer(ServletContextHandler servletContextHandler, int httpPort,
		int httpsPort, String keyStorePassword)
	{
		Server server = new Server();
		run(server, servletContextHandler, httpPort, httpsPort, keyStorePassword, "/keystore");
	}

	public static void run(ServletContextHandler servletContextHandler, int httpPort,
		int httpsPort, String keyStorePassword)
	{
		Server server = new Server();
		run(server, servletContextHandler, httpPort, httpsPort, keyStorePassword, "/keystore");
	}

	public static void run(Server server, Jetty9RunConfiguration config)
	{
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(config.getHttpsPort());
		http_config.setOutputBufferSize(32768);

		ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
		http.setPort(config.getHttpPort());
		http.setIdleTimeout(1000 * 60 * 60);

		server.addConnector(http);

		Resource keystore = Resource.newClassPathResource(config.getKeyStorePathResource());
		if (keystore != null && keystore.exists())
		{
			// if a keystore for a SSL certificate is available, start a SSL
			// connector on port 'httpsPort'.
			// By default, the quickstart comes with a Apache Wicket Quickstart
			// Certificate that expires about half way september 2021. Do not
			// use this certificate anywhere important as the passwords are
			// available in the source.

			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStoreResource(keystore);
			sslContextFactory.setKeyStorePassword(config.getKeyStorePassword());
			sslContextFactory.setKeyManagerPassword(config.getKeyStorePassword());

			HttpConfiguration https_config = new HttpConfiguration(http_config);
			https_config.addCustomizer(new SecureRequestCustomizer());

			ServerConnector https = new ServerConnector(server, new SslConnectionFactory(
				sslContextFactory, "http/1.1"), new HttpConnectionFactory(https_config))
			{

			};
			https.setPort(config.getHttpsPort());
			https.setIdleTimeout(500000);

			server.addConnector(https);
			System.out.println("SSL access to the examples has been enabled on port "
				+ config.getHttpsPort());
			System.out.println("You can access the application using SSL on https://localhost:"
				+ config.getHttpsPort());
			System.out.println();
		}

		if (config.getHandlers() == null)
		{
			config.setHandlers(new HandlerCollection());
		}
		if (config.getContexts() == null)
		{
			config.setContexts(new ContextHandlerCollection());
		}
		config.getHandlers().setHandlers(
			new Handler[] { config.getServletContextHandler(), config.getContexts(),
					new DefaultHandler() });

		server.setHandler(config.getHandlers());

		if (config.getDeployer() != null)
		{
			server.addBean(config.getDeployer());
		}

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		server.addEventListener(mBeanContainer);
		server.addBean(mBeanContainer);

		try
		{
			server.start();
			server.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(100);
		}
	}

	public static void run(Server server, ServletContextHandler servletContextHandler,
		int httpPort, int httpsPort, String keyStorePassword, String keyStorePathResource)
	{
		run(server, Jetty9RunConfiguration.builder().servletContextHandler(servletContextHandler)
			.httpPort(httpPort).httpsPort(httpsPort).keyStorePassword(keyStorePassword)
			.keyStorePathResource(keyStorePathResource).build());
	}

	public static ServletContextHandler getServletContextHandler(
		Class<? extends Application> applicationClass, String contextPath, File webapp,
		int maxInactiveInterval, String filterPath)
	{
		Map<String, String> initParameters = Generics.newHashMap();
		initParameters.put(WicketFilter.FILTER_MAPPING_PARAM, filterPath);
		return getServletContextHandler(ServletContextHandlerConfiguration.builder()
			.applicationClass(applicationClass).contextPath(contextPath).webapp(webapp)
			.maxInactiveInterval(maxInactiveInterval)
			.initParameter(WicketFilter.FILTER_MAPPING_PARAM, filterPath).filterPath(filterPath)
			.build());
	}

	public static ServletContextHandler getServletContextHandler(
		ServletContextHandlerConfiguration configuration)
	{
		final ServletContextHandler context = new ServletContextHandler(
			ServletContextHandler.SESSIONS);
		context.setContextPath(configuration.getContextPath());

		context.setResourceBase(configuration.getWebapp().getAbsolutePath());

		final FilterHolder filter = new FilterHolder(WicketFilter.class);
		filter.setInitParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM, configuration
			.getApplicationClass().getName());
		for (Entry<String, String> initParameter : configuration.getInitParameters().entrySet())
		{
			filter.setInitParameter(initParameter.getKey(), initParameter.getValue());
		}
		context.addFilter(filter, configuration.getFilterPath(),
			EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));
		context.addServlet(DefaultServlet.class, configuration.getFilterPath());

		context.getSessionHandler().getSessionManager()
			.setMaxInactiveInterval(configuration.getMaxInactiveInterval());
		return context;
	}

	public static ServletContextHandler getNewServletContextHandler(
		ServletContextHandlerConfiguration configuration)
	{
		final ServletContextHandler context = new ServletContextHandler(
			ServletContextHandler.SESSIONS);
		context.setContextPath(configuration.getContextPath());

		context.setResourceBase(configuration.getWebapp().getAbsolutePath());

		context.getSessionHandler().getSessionManager()
			.setMaxInactiveInterval(configuration.getMaxInactiveInterval());

		initializeFilterHolder(configuration, context);

		initializeServletHolder(configuration, context);

		for (Entry<String, String> initParameter : configuration.getInitParameters().entrySet())
		{
			context.setInitParameter(initParameter.getKey(), initParameter.getValue());
		}
		return context;
	}

	private static void initializeFilterHolder(ServletContextHandlerConfiguration configuration,
		final ServletContextHandler context)
	{
		List<FilterHolderConfiguration> filterHolderConfigurations = configuration
			.getFilterHolderConfigurations();
		if (CollectionUtils.isNotEmpty(filterHolderConfigurations))
		{
			for (FilterHolderConfiguration filterHolderConfiguration : filterHolderConfigurations)
			{
				final FilterHolder filter = new FilterHolder(
					filterHolderConfiguration.getFilterClass());
				if (StringUtils.isNotEmpty(filterHolderConfiguration.getName()))
				{
					filter.setName(filterHolderConfiguration.getName());
				}
				if (MapUtils.isNotEmpty(filterHolderConfiguration.getInitParameters()))
				{
					for (Entry<String, String> initParameter : filterHolderConfiguration
						.getInitParameters().entrySet())
					{
						filter.setInitParameter(initParameter.getKey(), initParameter.getValue());
					}
				}
				if (StringUtils.isNotEmpty(filterHolderConfiguration.getFilterPath()))
				{
					context.addFilter(filter, filterHolderConfiguration.getFilterPath(),
						EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));
				}
			}
		}
	}

	private static void initializeServletHolder(ServletContextHandlerConfiguration configuration,
		final ServletContextHandler context)
	{
		List<ServletHolderConfiguration> servletHolderConfigurations = configuration
			.getServletHolderConfigurations();
		if (CollectionUtils.isNotEmpty(servletHolderConfigurations))
		{
			for (ServletHolderConfiguration servletHolderConfiguration : servletHolderConfigurations)
			{
				ServletHolder servletHolder = new ServletHolder(
					servletHolderConfiguration.getServletClass());
				String servletName = servletHolderConfiguration.getName();
				if (StringUtils.isNotEmpty(servletName))
				{
					servletHolder.setName(servletHolderConfiguration.getName());
				}
				if (MapUtils.isNotEmpty(servletHolderConfiguration.getInitParameters()))
				{
					for (Entry<String, String> initParameter : servletHolderConfiguration
						.getInitParameters().entrySet())
					{
						servletHolder.setInitParameter(initParameter.getKey(),
							initParameter.getValue());
					}
				}
				if (StringUtils.isNotEmpty(servletHolderConfiguration.getPathSpec()))
				{
					context.addServlet(servletHolder, servletHolderConfiguration.getPathSpec());
				}
			}
		}
	}

	public static ServletContextHandler getServletContextHandler(
		Class<? extends Application> applicationClass, File webapp)
	{
		return getServletContextHandler(applicationClass, "/", webapp, 300, "/*");
	}

	public static ServletContextHandler getServletContextHandler(
		Class<? extends Application> applicationClass)
	{
		return getServletContextHandler(applicationClass, "/", PathFinder.getSrcMainJavaDir(), 300,
			"/*");
	}

	public static WebAppContext getWebAppContext(Server server, String projectname)
	{
		File webapp = PathFinder.getProjectDirectory();
		File wa = PathFinder.getRelativePath(webapp, projectname, "src", "main", "webapp");
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setServer(server);
		webAppContext.setContextPath("/");
		webAppContext.setWar(wa.getAbsolutePath());
		return webAppContext;
	}


	// see:http://git.eclipse.org/c/jetty/org.eclipse.jetty.project.git/tree/examples/embedded/src/main/java/org/eclipse/jetty/embedded/LikeJettyXml.java
	public static DeploymentManager getDeploymentManager(ContextHandlerCollection contexts,
		String monitoredDirName, String defaultsDescriptor)
	{
		DeploymentManager deployer = new DeploymentManager();
		deployer.setContexts(contexts);
		deployer.setContextAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
			".*/servlet-api-[^/]*\\.jar$");
		WebAppProvider webAppProvider = new WebAppProvider();
		webAppProvider.setMonitoredDirName(monitoredDirName);
		if(defaultsDescriptor != null) {
			webAppProvider.setDefaultsDescriptor(defaultsDescriptor);
		}
		webAppProvider.setScanInterval(1);
		webAppProvider.setExtractWars(true);
		webAppProvider.setConfigurationManager(new PropertiesConfigurationManager());

		deployer.addAppProvider(webAppProvider);
		return deployer;
	}
}
