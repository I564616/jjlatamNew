/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.invoice;

import java.util.List;

import com.jnj.core.model.JnjGTInvoiceEntryLotModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTInvoicePriceModel;


/**
 * TODO:<Class level comments missing>
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTInvoiceDao
{

	List<JnjGTInvoiceModel> getInvoiceDetailsByCode(String orderCode);

	/**
	 * YTODO <<Replace this line with the method description and describe each parameter below>>
	 * 
	 * @param invDocNo
	 * @return
	 */
	JnjGTInvoiceModel getInvoiceByInvoiceNum(String invDocNo);

	/**
	 * YTODO <<Replace this line with the method description and describe each parameter below>>
	 * 
	 * @param invoiceNum
	 * @param lineNum
	 * @return
	 */
	JnjGTInvoiceEntryModel getInvoiceEntryByInvoiceNumAdLineItemNum(String invoiceNum, String lineNum);

	/**
	 * YTODO <<Replace this line with the method description and describe each parameter below>>
	 * 
	 * @param invoiceDocNum
	 * @param invoiceEntryNum
	 * @param lotNum
	 * @return
	 */
	JnjGTInvoiceEntryLotModel getInvoiceEntryLotByInvNumLineItemAndLotNum(String invoiceDocNum, String invoiceEntryNum,
			String lotNum);

	/**
	 * YTODO <<Replace this line with the method description and describe each parameter below>>
	 * 
	 * @param invoiceDocNum
	 * @return
	 */
	List<JnjGTInvoicePriceModel> getInvoicePricesByInvoiceNum(String invoiceDocNum);


}
