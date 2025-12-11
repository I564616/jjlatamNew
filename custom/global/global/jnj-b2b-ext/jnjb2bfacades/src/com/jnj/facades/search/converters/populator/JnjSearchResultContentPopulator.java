/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.search.converters.populator;

import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.Config;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCmsUtil;
import com.jnj.facades.data.JnjContentData;


/**
 * This is used to convert solr result data to JnjContentData.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSearchResultContentPopulator implements Populator<SearchResultValueData, JnjContentData>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */


	/** The jnj cms util. */
	@Autowired
	private JnJCmsUtil jnjCmsUtil;



	@Override
	public void populate(final SearchResultValueData source, final JnjContentData target) throws ConversionException
	{
		target.setUrlLink(this.<String> getValue(source, "urlLink"));
		target.setHeadline((this.<String> getValue(source, "headline")));
		final String originalContent = (this.<String> getValue(source, "content"));
		target.setContent(jnjCmsUtil.trimContent(originalContent, Jnjb2bCoreConstants.NewsReleases.BLANK_DELIMITER,
				Config.getInt(Jnjb2bCoreConstants.NewsReleases.NUMBER_OF_CHARACTERS_KEY, 200)));
		target.setThumbnailUrl((this.<String> getValue(source, "thumbnailUrl")));
		target.setNewsPublishDate((this.<String> getValue(source, "newsPublishDate")));
		target.setBusinessCenter((this.<String> getValue(source, "businessCenter")));
	}

	/**
	 * Gets the value.
	 * 
	 * @param <T>
	 *           the generic type
	 * @param source
	 *           the source
	 * @param propertyName
	 *           the property name
	 * @return the value
	 */
	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}

		// DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
		return (T) source.getValues().get(propertyName);
	}
}
