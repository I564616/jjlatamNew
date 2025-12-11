/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.news.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjNewsBannerComponentModel;
import com.jnj.core.util.JnJCmsUtil;
import com.jnj.facades.data.JnjNewsReleasesData;


/**
 * Populator class to populate JnjNewsReleasesData from JnjNewsBannerComponentModel
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjNewsReleasesDataPopulator implements Populator<JnjNewsBannerComponentModel, JnjNewsReleasesData>
{

	/**
	 * Method to populate JnjNewsReleasesData from JnjNewsBannerComponentModel
	 */

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjNewsReleasesDataPopulator.class);

	@Autowired
	private JnJCmsUtil jnjCmsUtil;
	@Autowired
	private EnumerationService enumerationService;


	@Override
	public void populate(final JnjNewsBannerComponentModel source, final JnjNewsReleasesData target) throws ConversionException
	{

		logMethodStartOrEnd(Logging.INVOICE_ENTRY_DATA_POPULATOR, Logging.METHOD_POPULATE, Logging.BEGIN_OF_METHOD);

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setHeadLine(source.getHeadline());
		target.setTitle(source.getHeadline());
		target.setUrl(source.getUrlLink());
		target.setSummaryDesc(jnjCmsUtil.trimContent(source.getContent(), Jnjb2bCoreConstants.NewsReleases.BLANK_DELIMITER,
				Config.getInt(Jnjb2bCoreConstants.NewsReleases.NUMBER_OF_CHARACTERS_KEY, 200)));
		target.setNewsPublishDate(source.getNewsPublishDate());
		target.setBusinessCenter(enumerationService.getEnumerationName(source.getBusinessCenter()));
		logMethodStartOrEnd(Logging.INVOICE_ENTRY_DATA_POPULATOR, Logging.METHOD_POPULATE, Logging.END_OF_METHOD);

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
	private void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
	}

}
