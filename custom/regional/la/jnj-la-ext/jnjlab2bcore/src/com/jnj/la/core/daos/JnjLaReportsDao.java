/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.daos;

import com.jnj.core.dao.reports.JnjGTReportsDao;
import com.jnj.core.dto.JnjGTPageableData;

import java.util.List;

import de.hybris.platform.core.model.order.OrderEntryModel;

import com.jnj.la.core.model.JnjLaOpenOrdersReportTemplateModel;
import com.jnj.la.core.model.JnjOrderTypeModel;


public interface JnjLaReportsDao extends JnjGTReportsDao {
	
	/**
	 * This method will be used to fetch the open orders report
	 * @param jnjGTPageableData will pass as parameter
	 * @return will return the list of OrderEntryModel
	 */
	public List<OrderEntryModel> fetchOpenOrdersReport(final JnjGTPageableData jnjGTPageableData);
	
	/**
	 * This method will fetch all the order types.
	 * @return Will return the list of JnjOrderType
	 */
	public List<JnjOrderTypeModel> getOrderTypes();
	
	/**
	 * This method will fetch customer open orders report template.
	 * @param userId 
	 * @return will return List JnjLaOpenOrdersReportTemplateModel
	 */
	public List<JnjLaOpenOrdersReportTemplateModel> getOpenOrdersReportTemplate(final String userId);
	
	/**
	 * This method will fetch the report template by user id and name
	 * @param userId
	 * @param templateName
	 * @return will return the open orders report template.
	 */
	public JnjLaOpenOrdersReportTemplateModel getOpenOrdersReportTemplate(final String userId, final String templateName);

}