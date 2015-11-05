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
