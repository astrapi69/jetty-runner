# jetty-runner

The jetty-runner project provides method that can start programmatically the jetty server.

# Build Status 
[![Build Status](https://travis-ci.org/astrapi69/jetty-runner.svg?branch=master)](https://travis-ci.org/astrapi69/jetty-runner)


## Maven Central

Maven dependency is now on sonatype. 

Check out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~de.alpharogroup~jetty-runner~~~) for latest snapshots and releases.


Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of jcommons-lang:

Than you can add the dependency to your dependencies:

		<!-- JCOMMONS-LANG version -->
		<jetty-runner.version>3.6.0</jetty-runner.version>

		<dependencies>
			...
			<dependency>
				<groupId>de.alpharogroup</groupId>
				<artifactId>jetty-runner</artifactId>
				<version>${jetty-runner.version}</version>
			</dependency>
			...
		</dependencies>



Add the following maven dependency to your project `pom.xml` if you want to import wicket-jetty9-runner:

```xml

		<dependency>
			<groupId>de.alpharogroup</groupId>
			<artifactId>wicket-jetty9-runner</artifactId>
			<version>${jetty-runner.version}</version>
		</dependency>

		<dependency>
			<groupId>de.alpharogroup</groupId>
			<artifactId>jetty-runner-config</artifactId>
			<version>${jetty-runner.version}</version>
		</dependency>
```

## License

The source code comes under the liberal Apache License V2.0, making mvn-parent-projects great for all types of  applications.

# Donate

If you like this library, please consider a donation through 
<a href="http://flattr.com/thing/4180737/astrapi69jetty-runner-on-GitHub" target="_blank"><img src="http://api.flattr.com/button/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0" /></a>
