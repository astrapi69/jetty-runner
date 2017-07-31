# jetty-runner

The jetty-runner project provides method that can start programmatically the jetty server.

## License

The source code is released under the liberal Apache License V2.0, making jetty-runner great for all types of java web applications.

# Build Status 
[![Build Status](https://travis-ci.org/astrapi69/jetty-runner.svg?branch=master)](https://travis-ci.org/astrapi69/jetty-runner)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/jetty-runner/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/jetty-runner)

## Maven Central

Maven dependency is now on sonatype. 

Check out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~de.alpharogroup~jetty-runner~~~) for latest snapshots and releases.

Add the following maven dependencies to your project `pom.xml` if you want to import the core functionality:

You can first define the version properties:

	<properties>
			...
		<!-- JETTY-RUNNER version -->
		<jetty-runner.version>3.14.0</jetty-runner.version>
			...
	</properties>


Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of jetty-runner-core:

		<dependencies>
			...
			<dependency>
				<groupId>de.alpharogroup</groupId>
				<artifactId>jetty-runner-core</artifactId>
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

## Want to Help and improve it? ###

The source code for jetty-runner are on GitHub. Please feel free to fork and send pull requests!

Create your own fork of [astrapi69/jetty-runner/fork](https://github.com/astrapi69/jetty-runner/fork)

To share your changes, [submit a pull request](https://github.com/astrapi69/jetty-runner/pull/new/develop).

Don't forget to add new units tests on your changes.

## Contacting the Developer

Do not hesitate to contact the jetty-runner developers with your questions, concerns, comments, bug reports, or feature requests.
- Feature requests, questions and bug reports can be reported at the [issues page](https://github.com/astrapi69/jetty-runner/issues).

## Note

No animals were harmed in the making of this library.

# Donate

If you like this library, please consider a donation through 
<a href="http://flattr.com/thing/4180737/astrapi69jetty-runner-on-GitHub" target="_blank"><img src="http://api.flattr.com/button/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0" /></a>
