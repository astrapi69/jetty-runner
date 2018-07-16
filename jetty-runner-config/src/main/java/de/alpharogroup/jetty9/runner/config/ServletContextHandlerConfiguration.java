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
package de.alpharogroup.jetty9.runner.config;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.HandlerContainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

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
@Builder(toBuilder = true)
public class ServletContextHandlerConfiguration
{

	/** The application class for wicket. */
	private Class<?> applicationClass;

	/** The context path. */
	private String contextPath;

	/** The filter holder configurations. */
	@Singular
	private List<FilterHolderConfiguration> filterHolderConfigurations;
	/** The filter path. */
	private String filterPath;

	/** The init parameters. */
	@Singular
	private Map<String, String> initParameters;

	/**
	 * Sets the timeout in seconds. Like in web.xml=>web-app=>session-config=>session-timeout
	 *
	 **/
	private int maxInactiveInterval;

	/** The parent. */
	private HandlerContainer parent;

	/** The servlet holder configurations. */
	@Singular
	private List<ServletHolderConfiguration> servletHolderConfigurations;

	/** The webapp. */
	private File webapp;
}
