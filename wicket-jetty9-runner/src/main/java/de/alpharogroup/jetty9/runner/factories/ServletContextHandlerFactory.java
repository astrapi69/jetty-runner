package de.alpharogroup.jetty9.runner.factories;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.DispatcherType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.util.lang.Generics;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import de.alpharogroup.file.search.PathFinder;
import de.alpharogroup.jetty9.runner.config.FilterHolderConfiguration;
import de.alpharogroup.jetty9.runner.config.ServletContextHandlerConfiguration;
import de.alpharogroup.jetty9.runner.config.ServletHolderConfiguration;

/**
 * A factory for creating ServletContextHandler objects.
 */
public class ServletContextHandlerFactory
{

	/**
	 * Gets the new servlet context handler.
	 *
	 * @param configuration
	 *            the configuration
	 * @return the new servlet context handler
	 */
	public static ServletContextHandler getNewServletContextHandler(
		final ServletContextHandlerConfiguration configuration)
	{
		final ServletContextHandler context = new ServletContextHandler(
			ServletContextHandler.SESSIONS);
		context.setContextPath(configuration.getContextPath());

		context.setResourceBase(configuration.getWebapp().getAbsolutePath());

		context.getSessionHandler().getSessionManager()
			.setMaxInactiveInterval(configuration.getMaxInactiveInterval());

		initializeFilterHolder(configuration, context);

		initializeServletHolder(configuration, context);

		for (final Entry<String, String> initParameter : configuration.getInitParameters()
			.entrySet())
		{
			context.setInitParameter(initParameter.getKey(), initParameter.getValue());
		}
		return context;
	}

	/**
	 * Initialize filter holder.
	 *
	 * @param configuration
	 *            the configuration
	 * @param context
	 *            the context
	 */
	private static void initializeFilterHolder(
		final ServletContextHandlerConfiguration configuration, final ServletContextHandler context)
	{
		final List<FilterHolderConfiguration> filterHolderConfigurations = configuration
			.getFilterHolderConfigurations();
		if (CollectionUtils.isNotEmpty(filterHolderConfigurations))
		{
			for (final FilterHolderConfiguration filterHolderConfiguration : filterHolderConfigurations)
			{
				final FilterHolder filter = new FilterHolder(
					filterHolderConfiguration.getFilterClass());
				if (StringUtils.isNotEmpty(filterHolderConfiguration.getName()))
				{
					filter.setName(filterHolderConfiguration.getName());
				}
				if (MapUtils.isNotEmpty(filterHolderConfiguration.getInitParameters()))
				{
					for (final Entry<String, String> initParameter : filterHolderConfiguration
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

	/**
	 * Initialize servlet holder.
	 *
	 * @param configuration
	 *            the configuration
	 * @param context
	 *            the context
	 */
	private static void initializeServletHolder(
		final ServletContextHandlerConfiguration configuration, final ServletContextHandler context)
	{
		final List<ServletHolderConfiguration> servletHolderConfigurations = configuration
			.getServletHolderConfigurations();
		if (CollectionUtils.isNotEmpty(servletHolderConfigurations))
		{
			for (final ServletHolderConfiguration servletHolderConfiguration : servletHolderConfigurations)
			{
				final ServletHolder servletHolder = new ServletHolder(
					servletHolderConfiguration.getServletClass());
				final String servletName = servletHolderConfiguration.getName();
				if (StringUtils.isNotEmpty(servletName))
				{
					servletHolder.setName(servletHolderConfiguration.getName());
				}
				if (MapUtils.isNotEmpty(servletHolderConfiguration.getInitParameters()))
				{
					for (final Entry<String, String> initParameter : servletHolderConfiguration
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

	/**
	 * New servlet context handler.
	 *
	 * @param applicationClass
	 *            the application class
	 * @return the servlet context handler
	 */
	public static ServletContextHandler newServletContextHandler(
		final Class<? extends Application> applicationClass)
	{
		return newServletContextHandler(applicationClass, "/", PathFinder.getSrcMainJavaDir(), 300,
			"/*");
	}

	/**
	 * New servlet context handler.
	 *
	 * @param applicationClass
	 *            the application class
	 * @param webapp
	 *            the webapp
	 * @return the servlet context handler
	 */
	public static ServletContextHandler newServletContextHandler(
		final Class<? extends Application> applicationClass, final File webapp)
	{
		return newServletContextHandler(applicationClass, "/", webapp, 300, "/*");
	}


	/**
	 * New servlet context handler.
	 *
	 * @param applicationClass
	 *            the application class
	 * @param contextPath
	 *            the context path
	 * @param webapp
	 *            the webapp
	 * @param maxInactiveInterval
	 *            the max inactive interval
	 * @param filterPath
	 *            the filter path
	 * @return the servlet context handler
	 */
	public static ServletContextHandler newServletContextHandler(
		final Class<? extends Application> applicationClass, final String contextPath,
		final File webapp, final int maxInactiveInterval, final String filterPath)
	{
		final Map<String, String> initParameters = Generics.newHashMap();
		initParameters.put(WicketFilter.FILTER_MAPPING_PARAM, filterPath);
		return newServletContextHandler(ServletContextHandlerConfiguration.builder()
			.applicationClass(applicationClass).contextPath(contextPath).webapp(webapp)
			.maxInactiveInterval(maxInactiveInterval)
			.initParameter(WicketFilter.FILTER_MAPPING_PARAM, filterPath).filterPath(filterPath)
			.build());
	}

	/**
	 * New servlet context handler.
	 *
	 * @param configuration
	 *            the configuration
	 * @return the servlet context handler
	 */
	public static ServletContextHandler newServletContextHandler(
		final ServletContextHandlerConfiguration configuration)
	{
		final ServletContextHandler context;
		if (configuration.getParent() != null)
		{
			context = new ServletContextHandler(configuration.getParent(),
				configuration.getContextPath());
		}
		else
		{
			context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		}
		context.setContextPath(configuration.getContextPath());

		context.setResourceBase(configuration.getWebapp().getAbsolutePath());

		final FilterHolder filter = new FilterHolder(WicketFilter.class);
		filter.setInitParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM, configuration
			.getApplicationClass().getName());
		for (final Entry<String, String> initParameter : configuration.getInitParameters()
			.entrySet())
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
}
