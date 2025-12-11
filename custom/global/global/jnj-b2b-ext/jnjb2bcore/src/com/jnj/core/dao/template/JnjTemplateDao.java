/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.template;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;

import com.jnj.core.model.JnjOrderTemplateModel;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjTemplateDao
{

	/**
	 * @param searchByCriteria
	 * @param searchParameter
	 * @return
	 */
	public List<JnjOrderTemplateModel> searchOrderTemplate(String searchByCriteria, String searchParameter);

	public JnjOrderTemplateModel getJnjTemplateByCode(final String searchByCriteria);

	/**
	 * @param pageableData
	 * @param searchByCriteria
	 * @param searchParameter
	 * @param sortByCriteria
	 * @return
	 */
	public SearchPageData<JnjOrderTemplateModel> getPagedOrderTemplates(PageableData pageableData, String searchByCriteria,
			String searchParameter, String sortByCriteria);

	/**
	 * @return
	 */
	public List<JnjOrderTemplateModel> getRecenlyUsedTemplates();
}
