/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.jnj.constants.JnjutilConstants.Logging;





/**
 * The class provides static utility methods for common operations like date formatting, reading properties files,
 * checking empty array etc.
 * 
 * @author Accenture
 * @version 1.0
 */
public final class CommonUtil
{

	/**
	 * Logger instance will be used for logging informations, warnings and errors.
	 */
	private static final Logger LOG = Logger.getLogger(CommonUtil.class);

	/**
	 * Private Default Constructor for Utility Class.
	 */
	private CommonUtil()
	{
		super();
	}

	/**
	 * Format date to string.
	 * 
	 * @param date
	 *           the date
	 * @param format
	 *           the format
	 * @return the string
	 */
	public static String formatDateToString(final Date date, final String format)
	{

		final SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * Format date to string.
	 * 
	 * @param dateStr
	 *           the date str
	 * @param format
	 *           the format
	 * @return the string
	 */
	public static Date parseStringToDate(final String dateStr, final String format)
	{

		final SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		Date date = null;
		try
		{
			date = formatter.parse(dateStr);
		}
		catch (final ParseException e)
		{
			LOG.debug("ParseException while parsing date ", e);
		}
		return date;
	}

	/**
	 * Parses the xml date to date.
	 * 
	 * @param calander
	 *           the calander
	 * @return the date
	 */
	public static Date parseXMLDateToDate(final XMLGregorianCalendar calander)
	{
		return calander.toGregorianCalendar().getTime();
	}

	/**
	 * Method to get the String from byte array.
	 * 
	 * @param byteArray
	 *           byte array to be converted in String
	 * @return converted String
	 */
	public static String getStringFromByteArray(final byte[] byteArray)
	{
		String string = null;
		if (null != byteArray && byteArray.length > 0)
		{
			final StringBuffer strBuffer = new StringBuffer(byteArray.length);
			for (int count = 0; count < byteArray.length; count++)
			{
				strBuffer.append((char) byteArray[count]);
			}
			string = strBuffer.toString();
		}
		return string;
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	public static void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit,
			final Logger log)
	{
		if (log.isDebugEnabled())
		{
			log.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	public static void logDebugMessage(final String functionalityName, final String methodName, final String message,
			final Logger log)
	{
		if (log.isDebugEnabled())
		{
			log.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
	
	/**
	 * Utility method to replace all occurrence of a character with a different character
	 *
	 * @param characterToSearchFor
	 * @param characterToReplaceWith
	 * @param inputText
	 */
	public static String replaceInString(final String characterToSearchFor, final String characterToReplaceWith,
			final String inputText)
	{
		if (inputText != null)
		{
			return inputText.replace(characterToSearchFor, characterToReplaceWith);
		}
		else
		{
			return null;
		}
	}

}
