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

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServletContextHandlerConfiguration
{
	private Class<?> applicationClass;
	private File webapp;
	private String contextPath;
	/** 
	 * Sets the timeout in seconds.
	 * Like in web.xml=>web-app=>session-config=>session-timeout
	 * 
	 **/
	private int maxInactiveInterval;
	private String filterPath;
	@Singular
	private List<FilterHolderConfiguration> filterHolderConfigurations;
	@Singular
	private List<ServletHolderConfiguration> servletHolderConfigurations;
	@Singular
	private Map<String, String> initParameters;
}
