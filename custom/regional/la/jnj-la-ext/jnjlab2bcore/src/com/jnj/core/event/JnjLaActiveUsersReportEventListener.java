/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.core.event;

import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.la.core.model.JnjLaActiveUserReportEmailProcessModel;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

public class JnjLaActiveUsersReportEventListener extends
		AbstractSiteEventListener<JnjLaActiveUsersReportEvent> {
	private static final Logger LOGGER = Logger
			.getLogger(JnjLaActiveUsersReportEventListener.class);
	
	private static final String HYPHEN = "-";
	
	protected BusinessProcessService businessProcessService;
	protected ModelService modelService;

	@Override
	protected void onSiteEvent(JnjLaActiveUsersReportEvent event) {
		try {
			final String METHOD_NAME = "onSiteEvent()";
			logMethodStartOrEnd("", METHOD_NAME, "");
			final JnjLaActiveUserReportEmailProcessModel jnjLaActiveUserReportEmailProcessModel =  getBusinessProcessService()
					.createProcess(
							"JnjLaActiveUsersReportEmailProcess" + "-"
									+ System.currentTimeMillis(),
							"JnjLaActiveUsersReportEmailProcess");
			final Map<String, String> activeUserReportData = event
					.getActiveUserReportData();
			if (null != activeUserReportData) {
				jnjLaActiveUserReportEmailProcessModel
						.setActiveUserReportData(activeUserReportData);
				populateProcessModel(event,
						jnjLaActiveUserReportEmailProcessModel);
			}
			logMethodStartOrEnd("", METHOD_NAME, "");
		} catch (Exception ex) {
			LOGGER.error("Error while generating the active users report email "
					+ ex);
		}

	}

	private void populateProcessModel(
			JnjLaActiveUsersReportEvent event,
			JnjLaActiveUserReportEmailProcessModel jnjLaActiveUserReportEmailProcessModel) {
		try {
			final String METHOD_NAME = "populateProcessModel()";
			logMethodStartOrEnd("", METHOD_NAME, "");
			jnjLaActiveUserReportEmailProcessModel.setCustomer(event
					.getCustomer());
			jnjLaActiveUserReportEmailProcessModel.setSite(event.getSite());
			jnjLaActiveUserReportEmailProcessModel.setLanguage(event.getLanguage());
			jnjLaActiveUserReportEmailProcessModel.setCurrency(event
					.getCurrency());
			getModelService().save(jnjLaActiveUserReportEmailProcessModel);
			getBusinessProcessService().startProcess(
					jnjLaActiveUserReportEmailProcessModel);
			logMethodStartOrEnd("", METHOD_NAME, "");
		} catch (Exception ex) {
			LOGGER.error("Error while populating process model " + ex);
		}
	}

	private static void logMethodStartOrEnd(final String functionalityName,
			final String methodName, final String entryOrExit) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(functionalityName + HYPHEN + methodName + HYPHEN
					+ entryOrExit + HYPHEN + System.currentTimeMillis());
		}
	}

	@Override
	protected boolean shouldHandleEvent(final JnjLaActiveUsersReportEvent event) {
		return true;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(
			final BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

}
