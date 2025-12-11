/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.converters.populator.template;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.facades.data.JnjGTOrderTemplateData;
import com.jnj.facades.data.JnjGTOrderTemplateEntryData;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.core.model.JnjOrderTemplateModel;


/**
 * This is used to fetch the data from solr server as per JNJ NA req
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOrderTemplatePopulator implements Populator<JnjOrderTemplateModel, JnjGTOrderTemplateData>
{
	private static final Logger LOG = Logger.getLogger(JnjGTOrderTemplatePopulator.class);
	@Autowired
	SessionService sessionService;

	@Autowired
	JnjGTOrderTemplateEntryPopulator templateEntryPopulator;	

	@Override
	public void populate(final JnjOrderTemplateModel source, final JnjGTOrderTemplateData target)
	{
		final List<JnjGTOrderTemplateEntryData> entryDataList = new ArrayList<JnjGTOrderTemplateEntryData>();
		for (final JnjTemplateEntryModel model : source.getEntryList())
		{
			if (model instanceof JnjTemplateEntryModel)
			{
				final JnjGTOrderTemplateEntryData entryData = new JnjGTOrderTemplateEntryData();
				templateEntryPopulator.populate(model, entryData);
				entryDataList.add(entryData);
			}
		}
		target.setTemplateEntry(entryDataList);
		target.setAuthor(source.getAuthor().getName());
		target.setAuthorId(source.getAuthor().getUid());
		target.setB2bUnit(source.getUnit().getName());
		if (source.getRecTimeStamp() != null)
		{
			target.setCreatedOn(source.getRecTimeStamp());
		}
		if (source.getShareStatus() != null)
		{
			target.setShareStatus(source.getShareStatus().getCode());
		}
		target.setTemplateName(source.getName());
		target.setTemplateNumber(source.getTemplateNumber());
		target.setCode(source.getCode());
		target.setCreatedOn(source.getCreationtime());
		target.setLines(Integer.valueOf(source.getEntryList().size()));
	}
}
