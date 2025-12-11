/*
 * Copyright: Copyright  2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services.impl;


import com.jnj.core.dto.JnjGTPageableData;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.core.model.user.UserModel;
import com.jnj.core.model.JnJB2bCustomerModel;

import com.jnj.la.core.model.JnjOrderTypeModel;
import com.jnj.la.core.daos.JnjLaReportsDao;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facades.services.JnJLatamReportService;

import org.apache.log4j.Logger;

import java.util.List;

import com.jnj.la.core.model.JnjLaOpenOrdersReportTemplateModel;
import com.jnj.la.core.data.JnjLaOpenOrdersReportTemplateData;

public class JnjLatamReportServiceImpl implements JnJLatamReportService {

    private static final Logger LOG = Logger.getLogger(JnjLatamReportServiceImpl.class);
   
    @Autowired
    private JnjLaReportsDao jnjLaReportsDao;
    
    @Autowired
	private ModelService modelService;
    
    @Autowired
    private UserService userService;
    
    @Override
    public List<OrderEntryModel> fetchOpenOrdersReport(final JnjGTPageableData jnjGTPageableData) {
    	return jnjLaReportsDao.fetchOpenOrdersReport(jnjGTPageableData);
    }
    
    @Override
    public List<JnjOrderTypeModel> getOrderTypes() {
    	return jnjLaReportsDao.getOrderTypes();
    }
    
    @Override
    public void saveOpenOrdersReportTemplate(final JnjLaOpenOrdersReportTemplateData template, final String templateName) {    	
    	JnjLaOpenOrdersReportTemplateModel openOrdersTemplate = modelService.create(JnjLaOpenOrdersReportTemplateModel.class);
    	final UserModel currentUserModel = userService.getCurrentUser();
    	if (currentUserModel instanceof JnJB2bCustomerModel) {    		
    		openOrdersTemplate.setUser((JnJB2bCustomerModel)currentUserModel);    	
    	} 
    	openOrdersTemplate.setTemplateName(templateName);
    	openOrdersTemplate.setSelectedAccountIds(template.getAccountIds()); 
    	openOrdersTemplate.setQuickSelection(template.getQuickSelection());
        openOrdersTemplate.setFromDate(template.getFromDate());
        openOrdersTemplate.setToDate(template.getToDate());
        openOrdersTemplate.setOrderType(template.getOrderType());
        openOrdersTemplate.setOrderNumber(template.getOrderNumber());
        openOrdersTemplate.setProductCode(template.getProductCode());
        openOrdersTemplate.setShipTo(template.getShipTo());
        openOrdersTemplate.setReportColumns(template.getReportColumns());
    	modelService.save(openOrdersTemplate);
        LOG.info("Template saved succesfully");
    	}
    
    @Override
    public List<JnjLaOpenOrdersReportTemplateModel> getOpenOrdersReportTemplate() {    	
    	final UserModel currentUserModel = userService.getCurrentUser();
    	List<JnjLaOpenOrdersReportTemplateModel> openOrdersTemplate = null;
    	if (currentUserModel instanceof JnJB2bCustomerModel) {
    		openOrdersTemplate = jnjLaReportsDao.getOpenOrdersReportTemplate(currentUserModel.getUid());
    		
    	}    	
    	return openOrdersTemplate;
    	
    }
    
        
    @Override
    public void deleteOpenOrdersReportTemplate(final String templateName) {
    	final UserModel currentUserModel = userService.getCurrentUser();
    	if (currentUserModel instanceof JnJB2bCustomerModel) {    		
    	    JnjLaOpenOrdersReportTemplateModel template = jnjLaReportsDao.getOpenOrdersReportTemplate(currentUserModel.getUid(), templateName);
    	    if (null != template) {    	    	
    	        modelService.remove(template);
    	    }
    	}
    }
    
   
}