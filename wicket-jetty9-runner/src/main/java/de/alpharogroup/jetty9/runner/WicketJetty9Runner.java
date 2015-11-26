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

import org.apache.wicket.Application;

import de.alpharogroup.jetty9.runner.factories.WicketServletContextHandlerFactory;

/**
 * The Class {@link WicketJetty9Runner}.
 */
public class WicketJetty9Runner
{

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

}
