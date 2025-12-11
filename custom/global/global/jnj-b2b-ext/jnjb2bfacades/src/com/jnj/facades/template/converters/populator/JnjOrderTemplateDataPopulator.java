/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.template.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import com.jnj.core.model.JnjOrderTemplateModel;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.facades.data.JnjOrderTemplateData;
import com.jnj.facades.data.JnjTemplateEntryData;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderTemplateDataPopulator implements Populator<JnjOrderTemplateModel, JnjOrderTemplateData>
{


	/** The jnj invoice entry data converter. */
	private Converter<JnjTemplateEntryModel, JnjTemplateEntryData> jnjTemplateEntryDataConverter;

	/**
	 * @return the jnjTemplateEntryDataConverter
	 */

	public Converter<JnjTemplateEntryModel, JnjTemplateEntryData> getJnjTemplateEntryDataConverter()
	{
		return jnjTemplateEntryDataConverter;
	}



	/**
	 * @param jnjTemplateEntryDataConverter
	 *           the jnjTemplateEntryDataConverter to set
	 */
	public void setJnjTemplateEntryDataConverter(
			final Converter<JnjTemplateEntryModel, JnjTemplateEntryData> jnjTemplateEntryDataConverter)
	{
		this.jnjTemplateEntryDataConverter = jnjTemplateEntryDataConverter;
	}



	@Override
	public void populate(final JnjOrderTemplateModel source, final JnjOrderTemplateData target) throws ConversionException
	{
		int entryCount = target.getEntryCount() == null ? 0 : target.getEntryCount().intValue();
		long totalQuantity = 0;
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getCode());
		target.setCreationTime(source.getCreationtime());
		target.setB2bUnit(source.getUnit().getUid());
		target.setTotalItem(String.valueOf(source.getEntryList().size()));

		final List<JnjTemplateEntryModel> jnJTemplateEntryModelList = source.getEntryList();
		if (CollectionUtils.isNotEmpty(jnJTemplateEntryModelList))
		{
			final List<JnjTemplateEntryData> jnJTemplateEntryDataList = new ArrayList<JnjTemplateEntryData>(
					jnJTemplateEntryModelList.size());
			if (entryCount == 0 || entryCount > jnJTemplateEntryModelList.size())
			{
				entryCount = jnJTemplateEntryModelList.size();
			}
			for (int i = 0; i < entryCount; i++)
			{
				jnJTemplateEntryDataList.add(getJnjTemplateEntryDataConverter().convert(jnJTemplateEntryModelList.get(i),
						new JnjTemplateEntryData()));
				totalQuantity = totalQuantity + jnJTemplateEntryModelList.get(i).getQty();
			}
			target.setOrderEntry(jnJTemplateEntryDataList);
			target.setTotalQuantity(totalQuantity);
		}
		else
		{
			target.setOrderEntry(null);
		}

	}







}
