/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import java.util.List;

import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.exceptions.BusinessException;


/**
 * This is the interface for JnjInvoiceServiceImpl class containing the abstract methods for creating and saving the
 * Invoice Order and Entry Models into the database.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjInvoiceService
{

	/**
	 * Used for saving the JnJInvoiceOrderModel object into the database.
	 * 
	 * @param invoiceOrder
	 *           the invoice order
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean saveInvoiceOrder(final JnJInvoiceOrderModel invoiceOrder) throws BusinessException;

	/**
	 * Used for saving the JnJInvoiceEntryModel object into the database.
	 * 
	 * @param invoiceEntry
	 *           the invoice entry
	 * @return true, if successful
	 */
	public boolean saveInvoiceEntry(JnJInvoiceEntryModel invoiceEntry);

	/**
	 * Gets the invoiceby code.
	 * 
	 * @param invDocNo
	 *           the inv doc no
	 * @return the invoiceby code
	 */
	public JnJInvoiceOrderModel getInvoicebyCode(final String invDocNo);

	/**
	 * This method returns a list of invoices for the given SAP Order No.
	 * 
	 * @param salesOrder
	 * @return List<JnJInvoiceOrder>
	 */
	public List<JnJInvoiceOrderModel> getInvoicebyOrderCode(String salesOrder);

	/**
	 * Gets the invoice order data.
	 * 
	 * @return the invoice order data
	 */
	public List<JnJInvoiceOrderModel> getInvoiceOrderData();
}
