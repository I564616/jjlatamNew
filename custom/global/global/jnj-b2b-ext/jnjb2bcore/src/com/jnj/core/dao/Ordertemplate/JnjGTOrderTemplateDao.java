/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.Ordertemplate;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;
import java.util.Map;

import com.jnj.core.dao.template.JnjTemplateDao;
import com.jnj.core.model.JnjOrderTemplateModel;


/**
 * TODO:<Balinder-Class level comments missing>
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTOrderTemplateDao extends JnjTemplateDao
{
	public List<JnjOrderTemplateModel> searchOrderTemplate(final String searchByCriteria, final String searchParameter);

	public SearchPageData<JnjOrderTemplateModel> getPagedOrderTemplates(final PageableData pageableData,
			final String searchByCriteria, final String searchParameter, String sortByCriteria, final String unit);

	/**
	 * @param templateCode
	 * @param unit
	 * @return
	 */
	JnjOrderTemplateModel getOrderTemplateDetails(String templateCode, String unit);

	/**
	 * @return
	 */
	public Map<String, String> getOrderTemplatesForHome();
}
