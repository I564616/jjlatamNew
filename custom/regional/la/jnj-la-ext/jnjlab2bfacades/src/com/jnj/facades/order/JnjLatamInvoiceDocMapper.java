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
package com.jnj.facades.order;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.outboundservice.invoice.ElectronicBillingResponse;


/**
 *
 */
public interface JnjLatamInvoiceDocMapper
{

	/**
	 * The getInvoiceDocMapper method set the request parameter and passes them to Jnj Invoice Impl.
	 *
	 * @param fileType
	 *           the file type
	 * @param invoiceId
	 *           the invoice id
	 * @return the invoice doc mapper
	 * @throws IntegrationException
	 *            the integration exception
	 */

	public ElectronicBillingResponse getInvoiceDocMapper(final String fileType, final String invoiceId)
			throws IntegrationException, BusinessException;


}
