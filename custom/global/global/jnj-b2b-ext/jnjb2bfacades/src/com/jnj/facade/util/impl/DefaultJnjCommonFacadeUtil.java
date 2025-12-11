/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facade.util.impl;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Set;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.jnj.core.services.JnjConfigService;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.services.CMSSiteService;
import com.jnj.core.constants.Jnjb2bCoreConstants;

import de.hybris.platform.util.Config;

/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCommonFacadeUtil implements JnjCommonFacadeUtil
{
	@Autowired
	private UserService userService;

	@Autowired
	MessageFacadeUtill messageFacade;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private CMSSiteService cMSSiteService;

	@Autowired
	private JnjConfigService jnjConfigService;
	
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCommonFacadeUtil.class);

	@Override
	public String getMessageFromImpex(String messageCode)
	{
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
		return messageText;
	}
	
	@Override
	public String getMessageFromImpex(String messageCode,final Locale locale)
	{
		String messageText = null;
		if (messageCode == null)
		{
			messageCode = "NONE";
		}

		/** MessageFacade to retrieve the message text from message code. */
		try
		{
			messageText = messageFacade.getMessageTextForCode(messageCode,locale);
		}
		catch (final Exception exception)
		{
			LOGGER.error("Unable to render message text for message code : " + messageCode, exception);
		}
		return messageText;
	}
	
	
	@Override
	public String getMessageFromPropertiesFile(String messageCode)
	{
		String messageText = null;
		if (messageCode == null)
		{
			messageCode = "NONE";
		}

		/** MessageFacade to retrieve the message text from message code. */
		try
		{
			messageText = Config.getParameter(messageCode);
		}
		catch (final Exception exception)
		{
			LOGGER.error("Unable to render message text for message code : " + messageCode, exception);
		}
		return messageText;
	}

	/*
	 * (non-Javadoc) This method fetch the name in form of firstName in first index and last name in last index
	 * 
	 * @see com.jnj.facade.util.JnjCommonFacadeUtil#getName(java.lang.String)
	 */
	@Override
	public List<String> getName(String name)
	{
		final List<String> returnName = new ArrayList<String>();
		if (name != null && name.trim().length() > 0)
		{
			name = name.trim();
			final String stringName[] = name.split(" ");
			returnName.add(stringName[0].trim());
			if (stringName.length > 1)
			{
				returnName.add((name.substring(stringName[0].length())).trim());
			}
		}
		return returnName;
	}


	@Override
	public String createMediaLogoURL()
	{
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
				.getActiveCatalogVersion();
		return mediaService.getMedia(currentCatalog, "siteLogoImage").getURL();
	}

	/**
	 * This method fetches the drop down data from config impex aginst id "configId"
	 */
	@Override
	public Map<String, String> getDropDownFromConfig(final String configId)
	{
		return jnjConfigService.getDropdownValuesInMap(configId);
	}

	/**
	 * Gets the message for code.
	 * 
	 * @param messageCode
	 *           the message code
	 * @param locale
	 *           the locale, can be null, if null method considers the current locale
	 * @param params
	 *           Variable length array of Strings, that contain the values to be replaced for the placeholder
	 * @return the message for code
	 */
	@Override
	public String getMessageForCode(final String messageCode, final Locale locale, final String... params)
	{

		return messageFacade.getMessageForCode(messageCode, locale, params);
	}
	
	@Override
	public String getMediaURL(final String catalogId, final String catalogVersion, final String mediaCode)
	{
		MediaModel mediaModel = modelService.create(MediaModel.class);
		mediaModel = modelService.create(MediaModel.class); //Creating a new Media Model
		mediaModel.setCatalogVersion(catalogVersionService.getCatalogVersion(catalogId, catalogVersion));
		mediaModel.setCode(mediaCode);
		try
		{
			final MediaModel existingMedia = flexibleSearchService.getModelByExample(mediaModel);
			if (existingMedia != null)
			{
				return existingMedia.getURL();
			}
		}
		catch (final Exception exception)
		{
			LOGGER.info("Media Not Found");
		}
		return null;
	}
	
	@Override
	public String getMediaAbsoluteURL(String relativeURL, StringBuffer requestURL) {
		String output = null;
		String temp1 = null;
		String separators[] = requestURL.toString().split("/");
		for(int i=0; i<separators.length; i++) {
			output = separators[0]+"/"+separators[1]+"/"+separators[2];					
		}
		output = output+relativeURL;
		StringBuffer sb = new StringBuffer(output);
		output = output.replace("https", "http");
		output = output.replace("9002", "9001");
		System.out.println("jnjConnectLogo absolute URL :: "+output);
		return output;
	}
	
	@Override
	public List<String> getPermissions() {
		final UserModel currentUser = userService.getCurrentUser();
		final Set<PrincipalGroupModel> groups = currentUser.getAllGroups();
		return mapAllGroupAttributes(groups);
	}

	private List<String> mapAllGroupAttributes(
			final Set<PrincipalGroupModel> groups) {
		final List<String> groupsToBeAdded = new ArrayList<>();
        
		if (CollectionUtils.isNotEmpty(groups)) {
			for (final PrincipalGroupModel group : groups) {
				final String groupUid = group.getUid();
				if (Jnjb2bCoreConstants.GROUP_PLACE_ORDER.equals(groupUid)
						|| Jnjb2bCoreConstants.GROUP_ORDER_HISTORY
								.equals(groupUid)
						|| Jnjb2bCoreConstants.GROUP_CATALOG.equals(groupUid)
						|| Jnjb2bCoreConstants.GROUP_PLACE_ORDER_RES_COMM_USER
								.equals(groupUid)
						|| Jnjb2bCoreConstants.GROUP_PHARMA_COMMERCIAL_USER
								.equals(groupUid)
						|| Jnjb2bCoreConstants.GROUP_MDD_COMMERCIAL_USER
								.equals(groupUid)) {
					groupsToBeAdded.add(group.getUid());
				}
			}
		}
		return groupsToBeAdded;
	}

	public boolean getPermissionsFlagsForCommercialUserGroup(final String group) {
		final List<String> permissions = getPermissions();
		
		if (CollectionUtils.isNotEmpty(permissions)) {
			if (permissions
					.contains(Jnjb2bCoreConstants.GROUP_PLACE_ORDER_RES_COMM_USER)
					|| permissions
							.contains(Jnjb2bCoreConstants.GROUP_PHARMA_COMMERCIAL_USER)
					|| permissions
							.contains(Jnjb2bCoreConstants.GROUP_MDD_COMMERCIAL_USER)) {
				return true;
			}
		}
		return false;

	}

}
