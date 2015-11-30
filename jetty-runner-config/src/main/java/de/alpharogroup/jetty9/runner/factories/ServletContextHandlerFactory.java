package de.alpharogroup.jetty9.runner.factories;

import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.DispatcherType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import de.alpharogroup.jetty9.runner.config.FilterHolderConfiguration;
import de.alpharogroup.jetty9.runner.config.ServletContextHandlerConfiguration;
import de.alpharogroup.jetty9.runner.config.ServletHolderConfiguration;

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
}
