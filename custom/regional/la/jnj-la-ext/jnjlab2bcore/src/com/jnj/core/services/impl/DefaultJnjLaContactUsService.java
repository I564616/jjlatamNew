/**
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 **/
package com.jnj.core.services.impl;

import java.util.HashMap;

import org.apache.commons.collections4.MapUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import com.jnj.services.CMSSiteService;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;


public class DefaultJnjLaContactUsService extends DefaultJnjGTContactUsService
{
	private static final Logger LOG = Logger.getLogger(DefaultJnjLaContactUsService.class);
	
	protected final String DOT = ".";
	private CMSSiteService cmsSiteService;
	
	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public void setCmsSiteService(final CMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}
	
	
	@Override
	public String getEmailForContractUs(final String subjectID)
	{
		String returnValue = null;
		final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		String emailList = EMAIL_LIST;
		
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		if(Jnjlab2bcoreConstants.COUNTRY_ISO_PUERTORICO.equalsIgnoreCase(cmsSiteModel.getDefaultCountry().getIsocode()))
		{
			
			emailList=EMAIL_LIST+DOT+cmsSiteModel.getDefaultCountry().getIsocode()+DOT+cmsSiteModel.getDefaultLanguage().getIsocode();
		}
		else
		{
			emailList=EMAIL_LIST+DOT+cmsSiteModel.getDefaultCountry().getIsocode();
		}
		
		LOG.info("To email id for contact us LATAM: "+emailList);
		
		final HashMap<String, String> emailHashMap = getHashMap(emailList);
		if (MapUtils.isNotEmpty(emailHashMap))
		{
			returnValue = emailHashMap.get(subjectID);
		}
		return returnValue;
	}
	
}
