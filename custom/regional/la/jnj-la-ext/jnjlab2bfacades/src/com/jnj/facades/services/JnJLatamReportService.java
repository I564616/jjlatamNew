/*
 * Copyright:  Copyright 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.la.core.data.JnjLaOpenOrdersReportTemplateData;

import de.hybris.platform.core.model.order.OrderEntryModel;
import com.jnj.la.core.model.JnjLaOpenOrdersReportTemplateModel;

import com.jnj.la.core.model.JnjOrderTypeModel;

import java.util.List;

/**
 * This interface defines methods for Open Orders Report data.
 * @author RChauh21
 *
 */
public interface JnJLatamReportService {

	/**
	 * This method will be used to fetch the open orders report data.
	 * @param jnjGTPageableData will be passed to fetch the data of open orders reports data.
	 * @return will return List of OrderEntryModel
	 */
	public List<OrderEntryModel> fetchOpenOrdersReport(final JnjGTPageableData jnjGTPageableData);
	
	/**
	 * This method will fetch all the order types.
	 * @return Will return the list of JnjOrderTypesEnum
	 */
	public List<JnjOrderTypeModel> getOrderTypes();
	
	/**
	 * This method will be use to save the open orders report template.
	 * @param openOrdersReportData reports data.
	 * @param templateName name of the template to be saved.
	 */
	public void saveOpenOrdersReportTemplate(final JnjLaOpenOrdersReportTemplateData openOrdersReportData, final String templateName);
	
	/**
	 * This method will fetch the open orders report template from database.
	 * @return will return the List of JnjLaOpenOrdersReportTemplateModel.
	 */
	  public List<JnjLaOpenOrdersReportTemplateModel> getOpenOrdersReportTemplate();
	  
		  
	  /**
	   * This method will delete the open orders report template.
	   * @param templateName will be pass as parameter.
	   */
	  public void deleteOpenOrdersReportTemplate(final String templateName);
}