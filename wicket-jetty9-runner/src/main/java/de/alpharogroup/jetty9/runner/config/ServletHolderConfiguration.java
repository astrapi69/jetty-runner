package de.alpharogroup.jetty9.runner.config;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.Servlet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

/**
 * ServletHolder configuration.
 * <p>This class is a holder of the ServletHolder configuration. </p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServletHolderConfiguration implements Serializable
{
	/** The serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The servlet class. */
	private Class<? extends Servlet> servletClass;
	
	/** The init parameters. */
	@Singular
	private Map<String, String> initParameters;

	/** The path spec. */
	private String pathSpec;

	/** The name. */
	private String name;

}
