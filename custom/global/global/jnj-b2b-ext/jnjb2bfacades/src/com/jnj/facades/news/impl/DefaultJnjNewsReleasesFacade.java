/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.news.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjNewsBannerComponentModel;
import com.jnj.core.services.news.JnjNewsReleaseService;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjNewsReleasesData;
import com.jnj.facades.news.JnjNewsReleasesFacade;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjNewsReleasesFacade implements JnjNewsReleasesFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjNewsReleasesFacade.class);
	private static final String METHOD_GET_NEWS_COMPONENTS = "getNewsComponents";

	@Autowired
	private JnjNewsReleaseService jnjNewsReleaseService;


	private Converter<JnjNewsBannerComponentModel, JnjNewsReleasesData> jnjNewsReleasesDataConverter;


	@Override
	public List<JnjNewsReleasesData> getNewsComponents(final JnjNewsBannerComponentModel jnjNewsBannerComponentModel)
			throws BusinessException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		final List<JnjNewsReleasesData> jnjNewsReleasesDataList = new ArrayList<JnjNewsReleasesData>();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ "Getting News Banner Components for BusinessCenter - " + jnjNewsBannerComponentModel.getBusinessCenter());
		}

		//Getting all JnjNewsBannerComponentModel for the Business Center Associated with the inputed JnjNewsBannerComponentModel
		List<JnjNewsBannerComponentModel> newReleasesComponents = new ArrayList<JnjNewsBannerComponentModel>();
		if (null != jnjNewsBannerComponentModel && null != jnjNewsBannerComponentModel.getBusinessCenter())
		{
			newReleasesComponents = jnjNewsReleaseService
					.getJnjNewsBannerComponents(jnjNewsBannerComponentModel.getBusinessCenter());
		}
		else
		{
			LOGGER.error(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ "JnjNewsBannerComponentModel  or BusinessCenter associated with the same is null. Throwing Exception.");

			throw new BusinessException("JnjNewsBannerComponentModel  or BusinessCenter associated with the same is null.");
		}
		// Removing inputed JnjNewsBannerComponentModel from the List retrieved. 
		if (null != newReleasesComponents && !(newReleasesComponents.isEmpty()))
		{
			for (final JnjNewsBannerComponentModel retrivedJnjNewsBannerComponentModel : newReleasesComponents)
			{
				if (!(retrivedJnjNewsBannerComponentModel.getUid().equals(jnjNewsBannerComponentModel.getUid())))
				{
					final JnjNewsReleasesData jnjNewsReleasesData = new JnjNewsReleasesData();
					getJnjNewsReleasesDataConverter().convert(retrivedJnjNewsBannerComponentModel, jnjNewsReleasesData);
					jnjNewsReleasesDataList.add(jnjNewsReleasesData);
				}
			}
		}
		else
		{
			LOGGER.error(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ "No News Components Fetched for BusinessCenter - "
					+ jnjNewsBannerComponentModel.getBusinessCenter().getCode().toString() + " .Throwing Exception.");

			throw new BusinessException("No News Components Fetched for BusinessCenter - "
					+ jnjNewsBannerComponentModel.getBusinessCenter().getCode().toString());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		return jnjNewsReleasesDataList;
	}

	/**
	 * @return the jnjNewsReleasesDataConverter
	 */
	public Converter<JnjNewsBannerComponentModel, JnjNewsReleasesData> getJnjNewsReleasesDataConverter()
	{
		return jnjNewsReleasesDataConverter;
	}


	/**
	 * @param jnjNewsReleasesDataConverter
	 *           the jnjNewsReleasesDataConverter to set
	 */
	public void setJnjNewsReleasesDataConverter(
			final Converter<JnjNewsBannerComponentModel, JnjNewsReleasesData> jnjNewsReleasesDataConverter)
	{
		this.jnjNewsReleasesDataConverter = jnjNewsReleasesDataConverter;
	}


}
