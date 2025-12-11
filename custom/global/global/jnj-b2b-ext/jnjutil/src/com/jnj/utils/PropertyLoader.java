/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.utils;

import java.io.IOException;
import java.util.Properties;


/**
 * Singleton based class to load properties of a file which is in classpath.
 * 
 * @author Accenture
 * @version 1.0
 */
public abstract class PropertyLoader
{

	/**
	 * PropertyLoader Instance which Integration.properties.
	 */
	protected static PropertyLoader propertyLoader = null;
	/**
	 * 
	 * PropertyLoader Instance which Integrationdao.properties.
	 */
	protected static PropertyLoader daoPropertyLoader = null;
	/**
	 * Properties Instance.
	 */
	protected Properties props = null;

	/**
	 * Parameterized Constructor.
	 * 
	 * @param propertyFileName
	 *           Property File Name Passed.
	 * @throws IOException
	 *            if I/O error occurred.
	 */
	protected PropertyLoader(final String propertyFileName) throws IOException
	{
		props = new Properties();
		loadPropertyFile(propertyFileName);
	}

	/**
	 * 
	 * Method to load the passed property file.
	 * 
	 * @param propertyFileName
	 *           Property File Name Passed.
	 * @throws IOException
	 *            if I/O error occurred.
	 */
	protected abstract void loadPropertyFile(final String propertyFileName) throws IOException;

	/**
	 * To Get the property value from the given key.
	 * 
	 * @param key
	 *           of the property that is configured in the property file
	 * @return value of the property based on key
	 */
	public String getProperty(final String key)
	{
		return props.getProperty(key);
	}

}
