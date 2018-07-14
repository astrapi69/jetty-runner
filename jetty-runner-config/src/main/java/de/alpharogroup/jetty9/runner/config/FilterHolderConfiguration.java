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
 * <p>
 * This class is a holder of FilterHolder configuration.
 * </p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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
