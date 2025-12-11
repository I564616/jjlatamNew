/**
 * 
 */
package com.jnj.gt.dao.invoice;

import java.util.List;

import com.jnj.gt.model.JnjGTIntInvoiceEntryLotModel;
import com.jnj.gt.model.JnjGTIntInvoiceEntryModel;
import com.jnj.gt.model.JnjGTIntInvoicePriceModel;


/**
 * @author abhishek.b.arora
 * 
 */
public interface JnjGTInvoiceFeedDao
{

	/**
	 * @param invoiceDocNum
	 * @return
	 */
	List<JnjGTIntInvoiceEntryModel> getInvoiceEntry(String invoiceDocNum);

	/**
	 * @param invoiceDocNum
	 * @param invoiceEntryNum
	 * @return
	 */
	List<JnjGTIntInvoicePriceModel> getInvoicePrice(String invoiceDocNum);

	List<JnjGTIntInvoiceEntryModel> getInvoiceEntry(String invoiceDocNum, List<String> itemCategory);

	/**
	 * @param invoiceDocNum
	 * @param invoiceEntryNum
	 * @return
	 */
	List<JnjGTIntInvoiceEntryLotModel> getInvoiceEntryLot(String invoiceDocNum, String invoiceEntryNum);

}
