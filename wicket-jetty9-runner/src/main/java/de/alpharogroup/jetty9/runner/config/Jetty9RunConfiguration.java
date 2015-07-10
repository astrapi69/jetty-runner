package de.alpharogroup.jetty9.runner.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Run Configuration for jetty 9.
 * <p>This class is a holder of the run configuration for jetty 9. </p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jetty9RunConfiguration
{
	/** The servlet context handler. */
	private ServletContextHandler servletContextHandler;
	
	/** The http port. */
	private int httpPort;
	
	/** The https port. */
	private int httpsPort;
	
	/** The key store path resource. */
	private String keyStorePathResource;
	
	/** The key store password. */
	private String keyStorePassword;
	
	/** The handlers. */
	private HandlerCollection handlers;
	
	/** The contexts. */
	private ContextHandlerCollection contexts;
	
	/** The deployer. */
	private DeploymentManager deployer;
	
	/** The http configuration. */
	private HttpConfiguration httpConfiguration;
}
