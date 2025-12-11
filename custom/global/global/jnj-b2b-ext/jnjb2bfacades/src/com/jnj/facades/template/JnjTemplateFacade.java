/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.template;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;

import com.jnj.facades.data.JnjOrderTemplateData;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjTemplateFacade
{

	/**
	 * Creates the template.
	 * 
	 * @return true, if successful
	 */
	public boolean createTemplate();

	/**
	 * Creates the order template.
	 * 
	 * @param orderId
	 *           the order id
	 * @return true, if successful
	 */
	public boolean createOrderTemplate(String orderId);


	public List<JnjOrderTemplateData> getTemplatesForB2BUnit();

	/**
	 * @param searchByCriteria
	 * @param searchParameter
	 */
	public List<JnjOrderTemplateData> searchOrderTemplate(String searchByCriteria, String searchParameter);

	/**
	 * @param templateCode
	 */
	public boolean deleteTemplate(String templateCode);

	/**
	 * @param templateCode
	 * @param entryCount
	 *           YTODO
	 * @return
	 */
	public JnjOrderTemplateData getTemplateForCode(String templateCode, int entryCount);

	/**
	 * @param templateCode
	 * @return
	 */
	public boolean addTemplateToCart(String templateCode);


	/**
	 * @param pageableData
	 * @param searchByCriteria
	 * @param searchParameter
	 * @param string
	 * @return
	 */
	public SearchPageData<JnjOrderTemplateData> getPagedOrderTemplates(PageableData pageableData, String searchByCriteria,
			String searchParameter, String sortByCriteria);

	/**
	 * @return
	 */
	public List<JnjOrderTemplateData> getRecenlyUsedTemplates();





}
