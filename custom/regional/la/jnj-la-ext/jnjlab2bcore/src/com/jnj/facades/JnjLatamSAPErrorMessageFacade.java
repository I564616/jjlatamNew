/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.List;
import java.util.Map;

import com.jnj.facades.data.JnjSAPErrorMessageData;
import com.jnj.facades.data.JnjValidateOrderData;


/**
 *
 */
public interface JnjLatamSAPErrorMessageFacade
{


	String getErrorDetails(JnjSAPErrorMessageData errorMessageData, List<AbstractOrderEntryModel> orderEntry);

	/**
	 * This method is used to populate SAP error Messaged in Header List and Line Level Map.
	 * 
	 * @param headerErrorMessage
	 *           the header error message
	 * @param lineErroMessageMap
	 *           the line erro message map
	 * @param validateOrderData
	 *           the validate order data
	 */
	void populateSapErrorDetails(List<String> headerErrorMessage, Map<String, List<String>> lineErroMessageMap,
			JnjValidateOrderData validateOrderData);
}
