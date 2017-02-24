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

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * A factory for creating Configuration objects.
 */
public class ConfigurationFactory
{

	/**
	 * Factory method for creating a new {@link HttpConfiguration} from the given parameters. The
	 * default scheme for http is <code>http</code>.
	 *
	 * @param secureScheme
	 *            the secure scheme
	 * @param securePort
	 *            the secure port
	 * @param outputBufferSize
	 *            the output buffer size
	 * @return the new {@link HttpConfiguration}.
	 */
	public static HttpConfiguration newHttpConfiguration(final String secureScheme,
		final int securePort, final int outputBufferSize)
	{
		final HttpConfiguration httpConfiguration = new HttpConfiguration();
		httpConfiguration.setSecureScheme(secureScheme);
		httpConfiguration.setSecurePort(securePort);
		httpConfiguration.setOutputBufferSize(outputBufferSize);
		return httpConfiguration;
	}

	/**
	 * Factory method for creating a new {@link SslContextFactory} from the given parameters.
	 *
	 * @param keyStoreResource
	 *            the key store resource
	 * @param keyStorePassword
	 *            the key store password
	 * @param keyManagerPassword
	 *            the key manager password
	 * @return the new {@link SslContextFactory}.
	 */
	public static SslContextFactory newSslContextFactory(final Resource keyStoreResource,
		final String keyStorePassword, final String keyManagerPassword)
	{
		final SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStoreResource(keyStoreResource);
		sslContextFactory.setKeyStorePassword(keyStorePassword);
		sslContextFactory.setKeyManagerPassword(keyManagerPassword);
		return sslContextFactory;
	}

	/**
	 * Factory method for creating a new {@link SslContextFactory} from the given parameters.
	 *
	 * @param keyStorePath
	 *            the key store path
	 * @param keyStorePassword
	 *            the key store password
	 * @param keyManagerPassword
	 *            the key manager password
	 * @return the new {@link SslContextFactory}.
	 */
	public static SslContextFactory newSslContextFactory(final String keyStorePath,
		final String keyStorePassword, final String keyManagerPassword)
	{
		final SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(keyStorePath);
		sslContextFactory.setKeyStorePassword(keyStorePassword);
		sslContextFactory.setKeyManagerPassword(keyManagerPassword);
		return sslContextFactory;
	}

	/**
	 * Factory method for creating a new {@link ServerConnector} from the given parameters.
	 *
	 * @param server
	 *            the server
	 * @param httpConfiguration
	 *            the http configuration
	 * @param port
	 *            the port
	 * @param idleTimeout
	 *            the idle timeout
	 * @return the new {@link ServerConnector}.
	 */
	public static ServerConnector newServerConnector(final Server server,
		final HttpConfiguration httpConfiguration, final int port, final long idleTimeout)
	{
		final ServerConnector serverConnector = new ServerConnector(server,
			new HttpConnectionFactory(httpConfiguration));
		serverConnector.setPort(port);
		serverConnector.setIdleTimeout(idleTimeout);
		return serverConnector;
	}

	/**
	 * Factory method for creating a new {@link ServerConnector} from the given parameters.
	 *
	 * @param server
	 *            the server
	 * @param sslConnectionFactory
	 *            the ssl connection factory
	 * @param httpConfiguration
	 *            the http configuration
	 * @param port
	 *            the port
	 * @param idleTimeout
	 *            the idle timeout
	 * @return the new {@link ServerConnector}.
	 */
	public static ServerConnector newServerConnector(final Server server,
		final SslConnectionFactory sslConnectionFactory, final HttpConfiguration httpConfiguration,
		final int port, final long idleTimeout)
	{
		final ServerConnector serverConnector = new ServerConnector(server, sslConnectionFactory,
			new HttpConnectionFactory(httpConfiguration));
		serverConnector.setPort(port);
		serverConnector.setIdleTimeout(idleTimeout);
		return serverConnector;
	}

	/**
	 * Factory method for creating a new {@link SecureRequestCustomizer} from the given parameters.
	 *
	 * @param stsMaxAgeSeconds
	 *            the sts max age seconds
	 * @param stsIncludeSubDomains
	 *            the sts include sub domains
	 * @return the new {@link SecureRequestCustomizer}.
	 */
	public static SecureRequestCustomizer newSecureRequestCustomizer(final long stsMaxAgeSeconds,
		final boolean stsIncludeSubDomains)
	{
		final SecureRequestCustomizer src = new SecureRequestCustomizer();
		src.setStsMaxAge(stsMaxAgeSeconds);
		src.setStsIncludeSubDomains(stsIncludeSubDomains);
		return src;
	}

}
