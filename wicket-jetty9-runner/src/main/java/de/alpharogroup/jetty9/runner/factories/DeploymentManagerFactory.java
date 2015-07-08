package de.alpharogroup.jetty9.runner.factories;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.PropertiesConfigurationManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class DeploymentManagerFactory
{
	// see:http://git.eclipse.org/c/jetty/org.eclipse.jetty.project.git/tree/examples/embedded/src/main/java/org/eclipse/jetty/embedded/LikeJettyXml.java
	public static DeploymentManager newDeploymentManager(ContextHandlerCollection contexts,
		String monitoredDirName, String defaultsDescriptor)
	{
		DeploymentManager deployer = new DeploymentManager();
		deployer.setContexts(contexts);
		deployer.setContextAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
			".*/servlet-api-[^/]*\\.jar$");
		WebAppProvider webAppProvider = new WebAppProvider();
		webAppProvider.setMonitoredDirName(monitoredDirName);
		if (defaultsDescriptor != null)
		{
			webAppProvider.setDefaultsDescriptor(defaultsDescriptor);
		}
		webAppProvider.setScanInterval(1);
		webAppProvider.setExtractWars(true);
		webAppProvider.setConfigurationManager(new PropertiesConfigurationManager());

		deployer.addAppProvider(webAppProvider);
		return deployer;
	}
}
