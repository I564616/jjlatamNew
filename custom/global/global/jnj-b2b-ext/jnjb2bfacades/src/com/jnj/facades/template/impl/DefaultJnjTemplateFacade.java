/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.template.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjOrderTemplateModel;
import com.jnj.core.services.template.JnjTemplateService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.data.JnjOrderTemplateData;
import com.jnj.facades.template.JnjTemplateFacade;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjTemplateFacade implements JnjTemplateFacade
{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjTemplateFacade.class);

	@Autowired
	@Qualifier("defaultjnjTemplateService")
	JnjTemplateService jnjTemplateService;


	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;


	@Autowired
	private Converter<JnjOrderTemplateModel, JnjOrderTemplateData> jnjOrderTemplateDataConverter;

	@Override
	public boolean createTemplate()
	{
		return jnjTemplateService.createTempateFromSessionCart();
	}

	@Override
	public boolean createOrderTemplate(final String orderId)
	{
		return jnjTemplateService.createTempateFromOrder(orderId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.template.JnjTemplateFacade#getTemplatesForB2BUnit()
	 */
	@Override
	public List<JnjOrderTemplateData> getTemplatesForB2BUnit()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - "
					+ Jnjb2bCoreConstants.Logging.GET_TEMPLATE_FOR_B2BUNIT + "-" + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final JnJB2BUnitModel currentB2bUnit = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		final Collection<JnjOrderTemplateModel> orderTemplateList = currentB2bUnit.getOrderTemplate();
		final List<JnjOrderTemplateData> orderTemplateDataList = templateConverter(orderTemplateList);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - "
					+ Jnjb2bCoreConstants.Logging.GET_TEMPLATE_FOR_B2BUNIT + "-" + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return orderTemplateDataList;

	}


	/**
	 * @param orderTemplateList
	 * @return
	 */
	private List<JnjOrderTemplateData> templateConverter(final Collection<JnjOrderTemplateModel> orderTemplateList)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.TEMPLATE_CONVERTER
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjOrderTemplateData> orderTemplateDataList = new ArrayList<JnjOrderTemplateData>();
		for (final JnjOrderTemplateModel orderTemplate : orderTemplateList)
		{
			JnjOrderTemplateData templateData = new JnjOrderTemplateData();
			templateData = jnjOrderTemplateDataConverter.convert(orderTemplate, templateData);
			orderTemplateDataList.add(templateData);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.TEMPLATE_CONVERTER
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return orderTemplateDataList;
	}



	@Override
	public List<JnjOrderTemplateData> searchOrderTemplate(final String searchByCriteria, final String searchParameter)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.SEARCH_ORDER_TEMPLATE
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		List<JnjOrderTemplateData> orderTemplateDataList = null;
		final List<JnjOrderTemplateModel> templateModelList = jnjTemplateService.searchOrderTemplate(searchByCriteria,
				searchParameter);
		if (templateModelList != null)
		{
			orderTemplateDataList = templateConverter(templateModelList);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.SEARCH_ORDER_TEMPLATE
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return orderTemplateDataList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.template.JnjTemplateFacade#deleteTemplate(java.lang.String)
	 */
	@Override
	public boolean deleteTemplate(final String templateCode)
	{
		return jnjTemplateService.deleteTemplate(templateCode);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.template.JnjTemplateFacade#deleteTemplate(java.lang.String)
	 */
	@Override
	public JnjOrderTemplateData getTemplateForCode(final String templateCode, final int entryCount)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.SEARCH_ORDER_TEMPLATE
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjOrderTemplateModel orderTemplateModel = jnjTemplateService.getTemplateForCode(templateCode);
		JnjOrderTemplateData orderTemplateData = new JnjOrderTemplateData();
		orderTemplateData.setEntryCount(entryCount);
		orderTemplateData = jnjOrderTemplateDataConverter.convert(orderTemplateModel, orderTemplateData);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.SEARCH_ORDER_TEMPLATE
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return orderTemplateData;
	}








	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.template.JnjTemplateFacade#addTemplateToCart(java.lang.String)
	 */
	@Override
	public boolean addTemplateToCart(final String templateCode)
	{
		return jnjTemplateService.addTemplateToCart(templateCode);

	}




	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.facades.template.JnjTemplateFacade#getPagedOrderTemplates(de.hybris.platform.commerceservices.search.pagedata
	 * .PageableData, java.lang.String, java.lang.String)
	 */
	@Override
	public SearchPageData<JnjOrderTemplateData> getPagedOrderTemplates(final PageableData pageableData,
			final String searchByCriteria, final String searchParameter, final String sortByCriteria)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.GET_PAGED_TEMPLATES
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final SearchPageData<JnjOrderTemplateModel> orderTemplateResults = jnjTemplateService.getPagedOrderTemplates(pageableData,
				searchByCriteria, searchParameter, sortByCriteria);
		final Collection<JnjOrderTemplateModel> orderTemplateList = orderTemplateResults.getResults();
		final List<JnjOrderTemplateData> orderTemplateDataList = templateConverter(orderTemplateList);
		final SearchPageData<JnjOrderTemplateData> result = new SearchPageData<JnjOrderTemplateData>();
		result.setResults(orderTemplateDataList);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.GET_PAGED_TEMPLATES
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}


	public List<JnjOrderTemplateData> getRecenlyUsedTemplates()
	{
		List<JnjOrderTemplateData> orderTemplateDataList = null;
		final List<JnjOrderTemplateModel> templateModelList = jnjTemplateService.getRecenlyUsedTemplates();
		if (templateModelList != null)
		{
			orderTemplateDataList = templateConverter(templateModelList);
		}
		return orderTemplateDataList;
	}
}
