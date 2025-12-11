/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.component.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjLinkComponentModel;
import com.jnj.core.services.component.JnjCmsComponentService;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.component.JnjCmsComponentFacade;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCmsComponentFacade implements JnjCmsComponentFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCmsComponentFacade.class);
	private static final String METHOD_JNJ_LINK_COMPONENT = "getJnjLinkComponent()";

	@Autowired
	private JnjCmsComponentService jnjCmsComponentService;

	@Override
	public JnjLinkComponentModel getJnjLinkComponent(final String componentId) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_CMS_COMPONET_DAO + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		JnjLinkComponentModel jnjLinkComponentModel = null;
		if (StringUtils.isNotEmpty(componentId))
		{
			jnjLinkComponentModel = jnjCmsComponentService.getJnjLinkComponentForId(componentId);
		}
		else
		{
			LOGGER.error(Logging.JNJ_CMS_COMPONET_DAO + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ "Component Id is null. Throwing Exception.");
			throw new BusinessException("Component Id is null");

		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_CMS_COMPONET_DAO + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return jnjLinkComponentModel;
	}




}
