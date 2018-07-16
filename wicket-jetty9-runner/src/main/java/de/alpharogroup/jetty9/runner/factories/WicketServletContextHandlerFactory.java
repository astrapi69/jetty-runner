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
 * The factory class {@link WicketServletContextHandlerFactory} for creating
 * {@link ServletContextHandler} objects for wicket applications.
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
		return newWicketServletContextHandler(applicationClass, "/", PathFinder.getSrcMainJavaDir(),
			300, "/*");
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
		return newWicketServletContextHandler(applicationClass, "/", webapp, 300, "/*");
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
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release
	 */
	@Deprecated
	public static ServletContextHandler newServletContextHandler(
		final Class<? extends Application> applicationClass, final String contextPath,
		final File webapp, final int maxInactiveInterval, final String filterPath)
	{
		return newWicketServletContextHandler(applicationClass, contextPath, webapp,
			maxInactiveInterval, filterPath);
	}

	/**
	 * New servlet context handler.
	 *
	 * @param configuration
	 *            the configuration
	 * @return the servlet context handler
	 * @deprecated use instead
	 *             {@link WicketServletContextHandlerFactory#newWicketServletContextHandler(ServletContextHandlerConfiguration)}
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release
	 */
	@Deprecated
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
		return newWicketServletContextHandler(
			ServletContextHandlerConfiguration.builder().applicationClass(applicationClass)
				.contextPath(contextPath).webapp(webapp).maxInactiveInterval(maxInactiveInterval)
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
		filter.setInitParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM,
			configuration.getApplicationClass().getName());
		for (final Entry<String, String> initParameter : configuration.getInitParameters()
			.entrySet())
		{
			filter.setInitParameter(initParameter.getKey(), initParameter.getValue());
		}
		context.addFilter(filter, configuration.getFilterPath(),
			EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));
		context.addServlet(DefaultServlet.class, configuration.getFilterPath());

		context.getSessionHandler().setMaxInactiveInterval(configuration.getMaxInactiveInterval());
		return context;
	}
}
