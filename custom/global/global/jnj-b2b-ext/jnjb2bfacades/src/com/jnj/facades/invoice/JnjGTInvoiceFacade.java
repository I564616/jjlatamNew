/**
 * 
 */
package com.jnj.facades.invoice;

import java.util.List;

import com.jnj.facades.data.JnjGTInvoiceOrderData;


/**
 * @author t.e.sharma
 * 
 */
public interface JnjGTInvoiceFacade
{
	List<JnjGTInvoiceOrderData> getInvoiceDetailsByCode(String orderCode);

	JnjGTInvoiceOrderData getInvoiceDetailsByInvoiceNumber(String invoiceNumber);
}
