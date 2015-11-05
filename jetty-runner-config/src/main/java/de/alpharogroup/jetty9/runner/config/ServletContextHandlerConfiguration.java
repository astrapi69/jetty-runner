package de.alpharogroup.jetty9.runner.config;

import java.io.File;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import org.eclipse.jetty.server.HandlerContainer;

/**
 * ServletContextHandler configuration.
 * <p>
 * This class is a holder of the ServletContextHandler configuration.
 * </p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class ServletContextHandlerConfiguration
{

	/** The application class for wicket. */
	private Class<?> applicationClass;

	/** The webapp. */
	private File webapp;

	/** The context path. */
	private String contextPath;
	/**
	 * Sets the timeout in seconds. Like in web.xml=>web-app=>session-config=>session-timeout
	 * 
	 **/
	private int maxInactiveInterval;

	/** The filter path. */
	private String filterPath;

	/** The filter holder configurations. */
	@Singular
	private List<FilterHolderConfiguration> filterHolderConfigurations;

	/** The servlet holder configurations. */
	@Singular
	private List<ServletHolderConfiguration> servletHolderConfigurations;

	/** The init parameters. */
	@Singular
	private Map<String, String> initParameters;

	/** The parent. */
	private HandlerContainer parent;
}
