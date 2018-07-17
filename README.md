# jetty-runner

<div align="center">

[![license apache2](https://img.shields.io/badge/license-apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/astrapi69/jetty-runner.svg?branch=master)](https://travis-ci.org/astrapi69/jetty-runner)
[![Open Issues](https://img.shields.io/github/issues/lightblueseas/jetty-runner.svg?style=flat)](https://github.com/lightblueseas/jetty-runner/issues) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/jetty-runner/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/jetty-runner)

</div>

The jetty-runner project provides method that can start programmatically the jetty server.

## License

The source code is released under the liberal Apache License V2.0, making jetty-runner great for all types of java web applications.

## Maven dependency

Maven dependency is now on sonatype. 

Check out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~de.alpharogroup~jetty-runner~~~) for latest snapshots and releases.

Add the following maven dependencies to your project `pom.xml` if you want to import the core functionality:

You can first define the version properties:

	<properties>
			...
		<!-- JETTY-RUNNER version -->
		<jetty-runner.version>3.16</jetty-runner.version>
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

# Donations

If you like this library, please consider a donation through paypal: <a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=MJ7V43GU2H386" target="_blank">
<img src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_donateCC_LG.gif" alt="PayPal this" title="PayPal – The safer, easier way to pay online!" border="0" />
</a>

or over bitcoin or bitcoin-cash with:

1Jzso5h7U82QCNmgxxSCya1yUK7UVcSXsW

or over ether with:

0xaB6EaE10F352268B0CA672Dd6e999C86344D49D8

or over flattr:
  
<a href="http://flattr.com/thing/4180911/astrapi69resourcebundle-inspector-on-GitHub" target="_blank">
<img src="http://api.flattr.com/button/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0" />
</a>

