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
package de.alpharogroup.jetty9.runner.log;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import lombok.experimental.UtilityClass;

/**
 * Logger extensions for appenders.
 *
 * @deprecated use instead {@code de.alpharogroup.log.LoggerExtensions} <br>
 *             <br>
 *             Note: will be removed in the next minor release
 */
@Deprecated
@UtilityClass
public class LoggerExtensions
{

	/**
	 * Adds the file appender to the given logger.
	 *
	 * @param logger
	 *            the logger
	 * @param fileAppender
	 *            the file appender
	 */
	public static void addFileAppender(final Logger logger, final FileAppender fileAppender)
	{
		logger.addAppender(fileAppender);
	}

	/**
	 * New file appender.
	 *
	 * @param logFilePath
	 *            the log file path
	 * @return the file appender
	 */
	public static FileAppender newFileAppender(final String logFilePath)
	{
		final FileAppender appender = new FileAppender();
		appender.setName("MyFileAppender");
		appender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		appender.setFile(logFilePath);
		appender.setAppend(true);
		appender.setThreshold(Level.DEBUG);
		appender.activateOptions();
		return appender;
	}
}