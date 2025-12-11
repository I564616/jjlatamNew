/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingResponse;


/**
 * The JnjInvoiceDocMapper interface map the input parameter with request object.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjInvoiceDocMapper
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
