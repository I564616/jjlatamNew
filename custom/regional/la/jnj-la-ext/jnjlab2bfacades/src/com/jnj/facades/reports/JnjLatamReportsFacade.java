/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.reports;

import java.util.List;
import java.util.Map;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.data.JnjLaOpenOrdersReportReponseData;
import com.jnj.la.core.data.JnjLaOpenOrdersReportTemplateData;


/**
 *
 */
public interface JnjLatamReportsFacade extends JnjGTReportsFacade
{

	/**
	 *
	 * To fetch the operating company for the purchase analysis report
	 *
	 * @param currentSite
	 *           - the value of the source system id of the current b2b unit of the current user
	 * @return Map<String, String>
	 */
	@Override
	Map<String, String> getOperatingCompanyDropdown(String currentSite);
	
	/**
	 * This method fetches back-order report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTBackorderReportResponseData>
	 */
	@Override
	public List<JnjGTBackorderReportResponseData> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData);
	@Override
	public Map<String,String> getInventoryReportsTypeDropdownVaulesInMap();
	
	/**
	 * This method will be used to fetch the open orders report data
	 * @param jnjGTPageableData will be passed.
	 * @return Will return List of JnjLaOpenOrdersData
	 */
	public List<JnjLaOpenOrdersReportReponseData> fetchOpenOrdersReport(final JnjGTPageableData jnjGTPageableData);
	
	/**
	 * This method will fetch all the order types.
	 * @return Will return the list of JnjOrderTypesEnum
	 */
	public Map<String, String> getOrderTypes();
	
	/**
	 * This method will be used to save the report template.
	 * @param openOrdersReportData reports data.
	 * @param templateName This is the name of the template to be saved.
	 */
	public void saveOpenOrdersReportTemplate(final JnjLaOpenOrdersReportTemplateData openOrdersReportData, final String templateName);
	
	/**
	 * This method will fetch the open orders report template from database.
	 * @return will return the List of JnjLaOpenOrdersReportTemplateData
	 */
	public List<JnjLaOpenOrdersReportTemplateData> getOpenOrdersReportTemplate();
	
	/**
	 * This method will be use to remove the open orders report template.
	 * @param templateName Name of the template will pass as parameter.
	 */
	public void deleteOpenOrdersReportTemplate(final String templateName);



}
