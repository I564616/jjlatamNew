/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.util;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.services.CMSSiteService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import com.granule.json.JSONObject;
import java.nio.charset.Charset;

/**
 * CommonUtil for service layer
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnJCommonUtil
{


	protected static Random rnd = new Random();
	protected static final Logger LOGGER = Logger.getLogger(JnJCommonUtil.class);
	final static String VALID_COUNTRIES = Config.getParameter(Jnjb2bCoreConstants.KEY_Valid_COUNTRIES);
	protected static final String FILE_PURGE_DELETE_SUB_FOLDER = "file.purge.delete.subfolders";

	@Autowired
	MessageFacadeUtill messageFacade;
	@Autowired
	protected CMSSiteService cMSSiteService;
	@Autowired
	MediaService mediaService;
	@Autowired
	protected I18NService i18nService;

	@Autowired
	protected ConfigurationService configurationService;
	
	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
	
	private UserService userService;

	
	public MessageFacadeUtill getMessageFacade() {
		return messageFacade;
	}

	public CMSSiteService getcMSSiteService() {
		return cMSSiteService;
	}

	public MediaService getMediaService() {
		return mediaService;
	}

	/**
	 * 
	 * Validate country value for upsert customer
	 * 
	 * @param countryIsoCode
	 * @return boolean
	 */
	public static boolean checkValidCountry(final String countryIsoCode)
	{
		boolean valid = false;
		final String[] validCountries = VALID_COUNTRIES.split(",");

		for (final String isoCode : validCountries)
		{
			if (isoCode.equalsIgnoreCase(countryIsoCode))
			{
				valid = true;
				break;
			}
		}
		return valid;
	}

	/**
	 * 
	 * getCurrent Time in string
	 * 
	 * @return String
	 */
	public static String getCurrentDateTime()
	{
		return new Date().toString();
	}

	/**
	 * 
	 * This is used to get configuration from properties file for given key and delimination
	 * 
	 * @param key
	 * @param deliminator
	 * @return List<String>
	 */
	public static List<String> getValues(final String key, final String deliminator)
	{

		List<String> values = null;

		if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(deliminator))
		{

			final String configValue = Config.getParameter(key);
			if (StringUtils.isNotEmpty(configValue))
			{
				values = new ArrayList<String>();
				values = Arrays.asList(configValue.split(deliminator));
			}
		}

		return values;

	}


	/**
	 * 
	 * This is used to get configuration from properties file for given key
	 * 
	 * @param key
	 * 
	 * @return String
	 */
	public static String getValue(final String key)
	{
		if (StringUtils.isNotEmpty(key))
		{

			return Config.getParameter(key);
		}
		return null;
	}


	public String getMessageFromImpex(String messageCode)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getMessageFromImpex()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		String messageText = null;
		if (messageCode == null)
		{
			messageCode = "NONE";
		}

		/** MessageFacade to retrieve the message text from message code. */
		try
		{
			messageText = messageFacade.getMessageTextForCode(messageCode);
		}
		catch (final Exception exception)
		{
			LOGGER.error("Unable to render message text for message code : " + messageCode, exception);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getMessageFromImpex()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return messageText;
	}

	/**
	 * Format date into given String Format. If date param is passed as null it will convert current date into given
	 * format
	 * 
	 * @param date
	 *           the date
	 * @param requiredFormat
	 *           the required format
	 * @return the string
	 */
	public static String formatDate(Date date, final String requiredFormat)
	{
		final DateFormat formatter = new SimpleDateFormat(requiredFormat);
		if (date == null)
		{
			date = Calendar.getInstance().getTime();
		}
		return formatter.format(date);
	}


	/**
	 * This method creates a random password with character set "Jnjb2bCoreConstants.UserCreation.CHAR_SET" and of length
	 * Jnjb2bCoreConstants.UserCreation.LENGTH_OF_PASSWORD
	 * 
	 * @author balinder.singh
	 * @param len
	 * @return
	 */
	public static String createInitialPassword()
	{
		final int passLength = Jnjb2bCoreConstants.UserCreation.LENGTH_OF_PASSWORD;
		final String charSetForPassword = Jnjb2bCoreConstants.UserCreation.ALPHA_CHAR_SET;
		final String numberSetForPassword = Jnjb2bCoreConstants.UserCreation.NUMBER_SET;
		final StringBuilder finalPassword = new StringBuilder(passLength);
		finalPassword.append(charSetForPassword.charAt(rnd.nextInt(charSetForPassword.length())));
		for (int i = 1; i < passLength; i++)
		{
			finalPassword.append(numberSetForPassword.charAt(rnd.nextInt(numberSetForPassword.length())));
		}
		return finalPassword.toString();
	}

	/**
	 * 
	 * @param earlyDate
	 * @param nextDate
	 * @return
	 */
	public static int daysBetween(final Date earlyDate, final Date nextDate)
	{
		return (int) ((nextDate.getTime() - earlyDate.getTime()) / (1000 * 60 * 60 * 24));
	}


	public String createMediaLogoURL()
	{
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
				.getActiveCatalogVersion();
		return mediaService.getMedia(currentCatalog, "siteLogoImage").getURL();
	}

	/**
	 * Utility method used for logging entry into / exit from any method in debug mode.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
	 */
	public void logMethodStartOrEnd(final String functionalityName,
			final String methodName, final String entryOrExit) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(functionalityName + Logging.HYPHEN + methodName
					+ Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}


	/**
	 * This method removes all the files in specified directory (param dir). but will skip all the sub directories and
	 * its files.
	 * 
	 * @author balinder.singh
	 * @param dir
	 * @param filePattern
	 */
	protected void purgeDirectoryButKeepSubDirectories(final File dir, final String filePattern) throws IOException

	{
		final String METHOD_NAME = "purgeDirectoryButKeepSubDirectories ()";
		logMethodStartOrEnd(Logging.FILE_PURGING_SAP, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final int minDays = Config.getInt(Jnjb2bCoreConstants.FEED_FILE_PURGE_DAYS, 10);
		if (dir.isDirectory())
		{
			for (final File file : dir.listFiles())
			{
				if (!(filePattern != null && file.getName().indexOf(filePattern) != 0) && !file.isDirectory())
				{ // this will skip all the sub directories and its files and those files whose name doesn't start with provided pattern(filePattern) 
					if (JnJCommonUtil.daysBetween(new Date(file.lastModified()), new Date()) > minDays)
					{
						logMethodStartOrEnd("Deleting file " + file.getName(), METHOD_NAME, "");
						file.delete();
					}
				}
			}
		}
		else
		{
			LOGGER.error("JnJCommonUtil purgeDirectoryButKeepSubDirectories() " + Logging.HYPHEN + dir + Logging.HYPHEN
					+ "No such directory found" + Logging.HYPHEN + System.currentTimeMillis());
		}
		logMethodStartOrEnd(Logging.FILE_PURGING_SAP, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method removes all the files in specified directory (param dir) including all the sub directories and its
	 * files by making a recursive call.
	 * 
	 * @author balinder.singh
	 * @param dir
	 * @param filePattern
	 */
	protected void purgeDirectoryIncludingSubDirectory(final File dir, final String filePattern) throws IOException
	{
		final String METHOD_NAME = "purgeDirectoryIncludingSubDirectory ()";
		logMethodStartOrEnd(Logging.FILE_PURGING_SAP, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final int minDays = Config.getInt(Jnjb2bCoreConstants.FEED_FILE_PURGE_DAYS, 10);
		if (dir.isDirectory())
		{
			for (final File file : dir.listFiles())
			{
				if (!(filePattern != null && file.getName().indexOf(filePattern) != 0))
				{
					if (file.isDirectory())
					{
						purgeDirectory(file, filePattern);
					}
					logMethodStartOrEnd("Deleting file/folder " + file.getName(), METHOD_NAME, "");
					if (JnJCommonUtil.daysBetween(new Date(file.lastModified()), new Date()) > minDays)
					{
						file.delete();
					}
				}
			}
		}
		else
		{
			LOGGER.error("JnJCommonUtil purgeDirectory() " + Logging.HYPHEN + dir + Logging.HYPHEN + "No such directory found"
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		logMethodStartOrEnd(Logging.FILE_PURGING_SAP, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method calls the protected methods to delete the files and sub folders depending upon the value passed in the
	 * param deleteSubFolder. If deleteSubFolder = true, it will delete the subFolders. Else it will skip the sub
	 * folders, but will delete the files inside the folder.
	 * 
	 * @author balinder.singh
	 * @param dir
	 * @param filePattern
	 */
	public void purgeDirectory(final File dir, final String filePattern) throws IOException
	{
		final String METHOD_NAME = "purgeDirectory ()";
		logMethodStartOrEnd(Logging.FILE_PURGING_SAP, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final String deleteSubFolder = getValue(FILE_PURGE_DELETE_SUB_FOLDER);
		if (deleteSubFolder != null && deleteSubFolder.equalsIgnoreCase(Boolean.TRUE.toString()))
		{
			purgeDirectoryIncludingSubDirectory(dir, filePattern);
		}
		else
		{
			purgeDirectoryButKeepSubDirectories(dir, filePattern);
		}

		logMethodStartOrEnd(Logging.FILE_PURGING_SAP, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	/**
	 * Get Site Url based on Language
	 */
	public static String getSecureSiteUrlByLanguage(String langISOCode){		
		String baseUrl = Config.getParameter(Jnjb2bCoreConstants.SECURE_BASESITE_URL);
		return baseUrl+langISOCode+"/";
	}
	
	public static String getBaseSiteUrl(){		
		String baseUrl = Config.getParameter(Jnjb2bCoreConstants.BASESITE_URL);
		return baseUrl;
	}
	
	public static String getSecureSiteUrl(){		
		String baseUrl = Config.getParameter(Jnjb2bCoreConstants.SECURE_BASESITE_URL);
		return baseUrl;
	}
	
	/**
	 * AAOL - 6138
	 * @return
	 */
	public String getDateFormat(){
		
		
		String dateformat = Config.getString((Jnjb2bCoreConstants.DATE_FORMAT) + "." + getLocale().toLowerCase(),Jnjb2bCoreConstants.GENERIC_DATE_FORMAT);
		return dateformat;
	}
	/**
	 * AAOL-6138
	 * @return
	 */
	public String getTimeStampFormat(){
		
		String timeStampformat = Config.getString((Jnjb2bCoreConstants.TIME_STAMP_FORMAT) + "." + getLocale().toLowerCase(), Jnjb2bCoreConstants.GENERIC_TIME_STAMP_FORMAT);
		return timeStampformat;
	}
	
	/**
	 * AAOL-6138
	 * @return
	 */
	public String getDBDateFormat(){
		
		String dbDateformat = Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT;
		return dbDateformat;
	}
	public String getLocale(){
		
		//Changes for Cron Job Issue
		//Return default language as base site is not available for cron job.
		CountryModel siteCountry = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
		String locale = null;
		if(siteCountry!=null){
			 locale = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
		}else{
			 locale = i18nService.getCurrentLocale().toString();
		}
		
		return locale;
	}
	public boolean checkCaptcha(final String response){
		final String secretKey = Config.getParameter(Jnjb2bCoreConstants.RECAPTCHA_PRIVATE_KEY+"." + getLocale());
		 try {
		        String url = "https://www.google.com/recaptcha/api/siteverify?"
		                + "secret=" + secretKey
		                + "&response=" + response;
		        InputStream res = URI.create(url).toURL().openStream();
		        BufferedReader rd = new BufferedReader(new InputStreamReader(res, Charset.forName("UTF-8")));
		        StringBuilder sb = new StringBuilder();
		        int cp;
		        while ((cp = rd.read()) != -1) {
		            sb.append((char) cp);
		        }
		        String jsonText = sb.toString();
		        res.close();
		        JSONObject json = new JSONObject(jsonText);
		        boolean result = json.getBoolean("success");
		        
		        if(!result){
		        	LOGGER.debug("Error while validating Captcha "+json.getBoolean("error-codes"));
		        	LOGGER.info("Error while validating Captcha "+json.getBoolean("error-codes"));
		        }
		        return result;
		    } catch (final Exception e) {
		    	LOGGER.error("Exception occurred...",e);
		        return false;
		    }
	}
	
	/**
	 * This returns the current account of the user. 
	 * @return
	 */
	public String getCurrentUserAccountNumber()
	{
		String accountNumber = StringUtils.EMPTY;
		try
		{
			final CustomerModel user = (CustomerModel) userService.getCurrentUser();
			if(user instanceof JnJB2bCustomerModel)
			{
				final JnJB2BUnitModel currentB2bUnit = ((JnJB2bCustomerModel) user).getCurrentB2BUnit();
				if(null != currentB2bUnit)
				{
					accountNumber = currentB2bUnit.getUid();
				}
				LOGGER.debug("accountNumber : "+accountNumber);
			}
		}	
		catch(final NullPointerException exception)
		{
			LOGGER.error("getCurrentUserAccountNumber() typo occured" + exception);
		}
		return accountNumber;
	}

	/**
	 * Returns the value of a property as int from configuration service
	 * @param key - name of the property key
	 * @param defaultValue - default value
	 * @return key value as int
	 */
	public int getInt(final String key, int defaultValue) {
		return configurationService.getConfiguration().getInt(key, defaultValue);
	}
	
	public UserService getUserService() {
		return userService;
	}
    
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
}