/**
 * 
 */
package com.jnj.core.services;

import java.util.List;

import com.jnj.core.model.JnjGTInvoiceEntryLotModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTInvoicePriceModel;


/**
 * @author t.e.sharma
 * 
 */
public interface JnjGTInvoiceService
{

	/**
	 * 
	 * @param orderCode
	 * @return
	 */
	List<JnjGTInvoiceModel> getInvoiceDetailsByCode(String orderCode);

	JnjGTInvoiceModel getInvoiceByInvoiceNum(String invDocNo);

	/**
	 * YTODO <<Replace this line with the method description and describe each parameter below>>
	 * 
	 * @param invoiceNum
	 * @param lineNum
	 * @return
	 */
	JnjGTInvoiceEntryModel getInvoiceEntryByInvoiceNumAndLineItemNum(String invoiceNum, String lineNum);

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
