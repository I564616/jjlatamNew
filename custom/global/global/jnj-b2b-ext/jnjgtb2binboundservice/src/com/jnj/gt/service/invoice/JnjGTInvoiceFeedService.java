/**
 * 
 */
package com.jnj.gt.service.invoice;

import java.util.List;

import com.jnj.gt.model.JnjGTIntInvoiceEntryLotModel;
import com.jnj.gt.model.JnjGTIntInvoiceEntryModel;
import com.jnj.gt.model.JnjGTIntInvoicePriceModel;


/**
 * @author abhishek.b.arora
 * 
 */
public interface JnjGTInvoiceFeedService
{

	/**
	 * @param invoiceDocNum
	 * @return
	 */
	List<JnjGTIntInvoiceEntryModel> getInvoiceEntry(String invoiceDocNum);

	List<JnjGTIntInvoiceEntryLotModel> getInvoiceEntryLot(String invoiceDocNum, String invoiceEntryNum);

	List<JnjGTIntInvoicePriceModel> getInvoicePrice(String invoiceDocNum);

	/**
	 * @param invoiceDocNum
	 * @param itemCategory
	 * @return
	 */
	List<JnjGTIntInvoiceEntryModel> getInvoiceEntry(String invoiceDocNum, List<String> itemCategory);
}
