/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.invoice;

import java.io.File;
import java.util.List;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.data.JnJInvoiceOrderData;




/**
 * The interface for InvoiceFacadeImpl The interface for operations related to invoices.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjInvoiceFacade
{

	public List<JnJInvoiceOrderData> getInvoices(String salesOrder) throws BusinessException;

	/**
	 * The getInvoiceDocFile method calls the mapper and then calls the utility to read the xml or pdf file from the sftp
	 * location.
	 * 
	 * @param fileType
	 *           the file type
	 * @param invoiceId
	 *           the invoice id
	 * @return the invoice doc file
	 * @throws BusinessException
	 *            the business exception
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public File getInvoiceDocFile(final String fileType, final String invoiceId) throws BusinessException, IntegrationException;

}