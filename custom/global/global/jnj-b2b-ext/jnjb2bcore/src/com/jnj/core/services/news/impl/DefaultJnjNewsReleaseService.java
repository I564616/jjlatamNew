/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.news.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.news.JnjNewsReleasesDao;
import com.jnj.core.enums.BusinessCenter;
import com.jnj.core.model.JnjNewsBannerComponentModel;
import com.jnj.core.services.news.JnjNewsReleaseService;
import com.jnj.exceptions.BusinessException;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjNewsReleaseService implements JnjNewsReleaseService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjNewsReleaseService.class);
	protected static final String METHOD_GET_NEWS_BANNER_COMPONENTS = "getJnjNewsBannerComponents()";


	@Autowired
	private JnjNewsReleasesDao jnjNewsReleasesDao;
	

	public JnjNewsReleasesDao getJnjNewsReleasesDao() {
		return jnjNewsReleasesDao;
	}


	@Override
	public List<JnjNewsBannerComponentModel> getJnjNewsBannerComponents(final BusinessCenter businessCenter)
			throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_BANNER_COMPONENTS + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final List<JnjNewsBannerComponentModel> jnjNewsBannerComponentList = jnjNewsReleasesDao.getNewsComponents(businessCenter);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_BANNER_COMPONENTS + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		return jnjNewsBannerComponentList;
	}
}
