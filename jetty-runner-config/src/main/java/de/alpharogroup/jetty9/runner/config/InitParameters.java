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

/**
 * The Interface InitParameters holds constants for the keys in the web.xml.
 */
public interface InitParameters
{

	/** The application factory class name. */
	String APPLICATION_FACTORY_CLASS_NAME = "applicationFactoryClassName";

	/** The wicket spring web application factory class. */
	String WICKET_SPRING_WEB_APPLICATION_FACTORY_CLASS = "org.apache.wicket.spring.SpringWebApplicationFactory";

}
