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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class represents a start config for a jetty instance.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartConfig
{

	/** The project name. */
	private String projectName;

	/** The application name. */
	private String applicationName;

	/** The runtime configuration type. */
	private String runtimeConfigurationType;

	/** The session timeout. */
	private int sessionTimeout;

	/** The project directory. */
	private File projectDirectory;

	/** The log file. */
	private File logFile;

	/** The absolute path from logfile. */
	private String absolutePathFromLogfile;

	/** The webapp. */
	private File webapp;

	/** The filter path. */
	private String filterPath;

	/** The key store password. */
	private String keyStorePassword;

	/** The key store path resource. */
	private String keyStorePathResource;

	/** The context path. */
	private String contextPath;

	/** The http port. */
	private int httpPort;

	/** The https port. */
	private int httpsPort;

}
