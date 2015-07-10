package de.alpharogroup.jetty9.runner.config;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.Filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

/**
 * FilterHolder Configuration.
 * <p>This class is a holder of FilterHolder configuration. </p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterHolderConfiguration implements Serializable
{

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** The filter class. */
	private Class<? extends Filter> filterClass;
	
	/** The init parameters. */
	@Singular
	private Map<String, String> initParameters;

	/** The filter path. */
	private String filterPath;

	/** The name. */
	private String name;


}
