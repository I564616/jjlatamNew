/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.component.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.component.JnjCmsComponentDao;
import com.jnj.core.model.JnjLinkComponentModel;
import com.jnj.core.services.component.JnjCmsComponentService;
import com.jnj.exceptions.BusinessException;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCmsComponentService implements JnjCmsComponentService
{

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCmsComponentService.class);
	protected static final String METHOD_JNJ_LINK_COMPONENT = "getJnjLinkComponentForId()";

	@Autowired
	private JnjCmsComponentDao jnjCmsComponentDao;

	public JnjCmsComponentDao getJnjCmsComponentDao() {
		return jnjCmsComponentDao;
	}

	@Override
	public JnjLinkComponentModel getJnjLinkComponentForId(final String componentId) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_CMS_COMPONET_SERVICE + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final JnjLinkComponentModel jnjLinkComponentModel;
		jnjLinkComponentModel = jnjCmsComponentDao.getJnjLinkComponentForId(componentId);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_CMS_COMPONET_SERVICE + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return jnjLinkComponentModel;
	}


}
