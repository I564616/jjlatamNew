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
package com.jnj.outboundservice.services.invoice;

import com.jnj.exceptions.IntegrationException;
import com.jnj.outboundservice.invoice.ElectronicBillingRequest;
import com.jnj.outboundservice.invoice.ElectronicBillingResponse;


/**
 *
 */
public interface JnjLatamInvoiceDoc
{

	public ElectronicBillingResponse receiveElectronicBillingFromHybrisWrapper(
			final ElectronicBillingRequest electronicBillingRequest) throws IntegrationException;

}
