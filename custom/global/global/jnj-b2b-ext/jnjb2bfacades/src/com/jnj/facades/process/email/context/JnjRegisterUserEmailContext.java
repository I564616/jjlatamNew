/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjRegistrationProcessModel;
import com.jnj.core.services.JnjConfigService;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjRegisterUserEmailContext extends CustomerEmailContext
{
	private Map<String, Object> jnjCustomerFormMap = new HashMap<String, Object>();


	@Autowired
	private JnjConfigService jnjConfigService;

	private static final String COUNTRYISOCODE = "countryIsoCode";


	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);

		if (storeFrontCustomerProcessModel instanceof JnjRegistrationProcessModel)
		{
			setJnjCustomerFormMap(((JnjRegistrationProcessModel) storeFrontCustomerProcessModel).getJnjCustomerFormMap());
		}
		final Map<String, Object> emailContextMap = getJnjCustomerFormMap();
		put(EMAIL, emailContextMap.get("email"));
		put(FROM_EMAIL, jnjConfigService.getConfigValueById(Jnjb2bCoreConstants.Register.REGISTER_EMAIL_FROM));
		put(FROM_DISPLAY_NAME, jnjConfigService.getConfigValueById(Jnjb2bCoreConstants.Register.REGISTER_EMAIL_DISPLAY_NAME));

	}

	/**
	 * @return the jnjCustomerFormMap
	 */
	public Map<String, Object> getJnjCustomerFormMap()
	{
		return jnjCustomerFormMap;
	}

	/**
	 * @param jnjCustomerFormMap
	 *           the jnjCustomerFormMap to set
	 */
	public void setJnjCustomerFormMap(final Map<String, Object> jnjCustomerFormMap)
	{
		this.jnjCustomerFormMap = jnjCustomerFormMap;
	}


}
