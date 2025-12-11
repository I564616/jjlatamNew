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
import java.util.Map;

import com.jnj.facades.data.JnjTemplateEntryData;
import com.jnj.facades.template.JnjTemplateFacade;
import com.jnj.core.dto.JnjGTOrderTemplateForm;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTTemplateDetailsData;
import com.jnj.facades.data.JnjGTOrderTemplateData;


/**
 * @author Accenture
 * @version 1.0
 */

public interface JnjGTOrderTemplateFacade extends JnjTemplateFacade
{

	/**
	 * Creates the template from session cart.
	 * 
	 * @param templateName
	 *           the template name
	 * @param shared
	 *           the shared
	 * @return true, if successful
	 */
	public boolean createTemplateFromSessionCart(String templateName, boolean shared);

	/**
	 * Gets the paged template for statuses.
	 * 
	 * @param pageableData
	 *           the pageable data
	 * @param form
	 *           the form
	 * @return the paged template for statuses
	 */
	public SearchPageData<JnjGTOrderTemplateData> getPagedTemplateForStatuses(PageableData pageableData,
			JnjGTOrderTemplateForm form);

	public Map<String, String> getDropDownList(String configId);

	/**
	 * Gets the selected order template detail.
	 * 
	 * @param jnjGTPageableData
	 *           the jnj na pageable data
	 * @return the selected order template detail
	 */
	List<JnjTemplateEntryData> getSelectedOrderTemplateDetail(JnjGTPageableData jnjGTPageableData);


	/**
	 * This method is used to update the data of the Order Template.
	 * 
	 * @param templateDetailsData
	 *           the template details data
	 * @return true, if successful
	 */
	public boolean updateTemplate(JnjGTTemplateDetailsData templateDetailsData);

	/**
	 * This method is used to find a method based on the template code provided.
	 * 
	 * @param templateCode
	 *           the template code
	 * @return the template for code
	 */
	JnjGTOrderTemplateData getTemplateForCode(String templateCode);

	/**
	 * Gets the drop down list for group by.
	 * 
	 * @param id
	 *           the id
	 * @return the drop down list for group by
	 */
	List<String> getDropDownListForGroupBy(String id);

	/**
	 * This method is used For addTocart From Template Via Home
	 * 
	 * @param responseMap
	 * @param templateCode
	 * @return Map<String, Object>
	 */
	public Map<String, Object> addToCartFromHomeTemplate(final Map<String, Object> responseMap, final String templateCode);

	/**
	 * Gets the order templates for home.
	 * 
	 * @return the order templates for home
	 */
	Map<String, String> getOrderTemplatesForHome();

	/**
	 * Creates the template from order.
	 * 
	 * @param orderId
	 *           the order id
	 * @param templateName
	 *           the template name
	 * @param shared
	 *           the shared
	 * @return true, if successful
	 */
	public boolean createTemplateFromOrder(final String orderId, final String templateName, final boolean shared);
	
	public String updateProductAndQuantityForTemplate(JnjGTTemplateDetailsData jnjGTTemplateDetailsForm);
	
	public boolean deleteTemplateProduct(final String templateCode, final String refVariant);

	public boolean createNewTemplate(final String tempProds, final String templateName, final boolean shared);
	
	public boolean editExistingTemplate(final String tempProdQtys, final String templateName, final boolean shared, final String templateCode);
	
	
}
