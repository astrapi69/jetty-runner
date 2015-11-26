package de.alpharogroup.jetty9.runner.factories;

import java.io.File;
import java.util.EnumSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.DispatcherType;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.util.lang.Generics;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import de.alpharogroup.file.search.PathFinder;
import de.alpharogroup.jetty9.runner.config.ServletContextHandlerConfiguration;

/**
 * A factory for creating ServletContextHandler objects.
 */
public class WicketServletContextHandlerFactory
{


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
	 * @deprecated use instead
	 *             {@link WicketServletContextHandlerFactory#newWicketServletContextHandler(Class, String, File, int, String)}
	 */
	public static ServletContextHandler newServletContextHandler(
		final Class<? extends Application> applicationClass, final String contextPath,
		final File webapp, final int maxInactiveInterval, final String filterPath)
	{
		return newWicketServletContextHandler(applicationClass, contextPath, webapp,
			maxInactiveInterval,
			filterPath);
	}

	/**
	 * New servlet context handler.
	 *
	 * @param configuration
	 *            the configuration
	 * @return the servlet context handler
	 * @deprecated use instead
	 *             {@link WicketServletContextHandlerFactory#newWicketServletContextHandler(ServletContextHandlerConfiguration)}
	 */
	public static ServletContextHandler newServletContextHandler(
		final ServletContextHandlerConfiguration configuration)
	{
		return newWicketServletContextHandler(configuration);
	}

	/**
	 * New wicket filter context handler.
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
	public static ServletContextHandler newWicketServletContextHandler(
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
	 * New wicket servlet context handler.
	 *
	 * @param configuration
	 *            the configuration
	 * @return the servlet context handler
	 */
	public static ServletContextHandler newWicketServletContextHandler(
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
