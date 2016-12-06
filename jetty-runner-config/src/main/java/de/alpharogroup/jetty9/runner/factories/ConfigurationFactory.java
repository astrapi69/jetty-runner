package de.alpharogroup.jetty9.runner.factories;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * A factory for creating Configuration objects.
 */
public class ConfigurationFactory
{

	/**
	 * Factory method for creating a new {@link HttpConfiguration} from the given parameters. The default scheme for http is
	 * <code>http</code>.
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
}
