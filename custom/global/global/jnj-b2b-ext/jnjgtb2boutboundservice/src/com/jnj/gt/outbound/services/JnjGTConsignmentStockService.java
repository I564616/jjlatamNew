/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ViewConsignmentStockInput;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ViewConsignmentStockOutput;


/**
 * @author ujjwal.negi
 * 
 */
public interface JnjGTConsignmentStockService
{
	/**
	 * The viewConsignmentStocks method accepts the viewConsignmentStockInput object as its input parameters and passes
	 * the same to SAP service to fetch the stock details and receive the ViewConsignmentStockOutput object.
	 * 
	 * @param viewConsignmentStockInput
	 *           the consignment stock input
	 * @return the consignment stock data from SAP
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public ViewConsignmentStockOutput viewConsignmentStocks(final ViewConsignmentStockInput viewConsignmentStockInput)
			throws IntegrationException;
}
