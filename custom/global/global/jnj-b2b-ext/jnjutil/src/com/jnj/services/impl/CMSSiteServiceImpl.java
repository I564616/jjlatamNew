/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.services.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSSiteService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.dao.CMSSiteDao;
import com.jnj.services.CMSSiteService;


/**
 * The Class CMSSiteServiceImpl.
 * 
 * @author Accenture
 * @version 1.0
 */
public class CMSSiteServiceImpl extends DefaultCMSSiteService implements CMSSiteService
{

	/** Reference for CMSSiteDao. */
	@Autowired
	private CMSSiteDao frameworkCMSSiteDao;

	/** Reference for I18NService. */
	@Autowired
	private I18NService i18nService;

	public CMSSiteDao getFrameworkCMSSiteDao() {
		return frameworkCMSSiteDao;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.services.CMSSiteService#getSiteForCountry(java.lang. String,java.lang.String)
	 */
	@Override
	public CMSSiteModel getSiteForCountry(final String countryIsoCode) throws CMSItemNotFoundException
	{
		final CMSSiteModel cmsSite = frameworkCMSSiteDao.findCMSSiteByCountry(i18nService.getCountry(countryIsoCode));
		return cmsSite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.services.CMSSiteService#getSiteForCountry(de.hybris. platform.core.model.c2l.CountryModel,
	 * java.lang.String)
	 */
	@Override
	public CMSSiteModel getSiteForCountry(final CountryModel countryModel) throws CMSItemNotFoundException
	{
		final CMSSiteModel cmsSite = frameworkCMSSiteDao.findCMSSiteByCountry(countryModel);
		return cmsSite;
	}








}
