/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import java.util.List;

import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.facades.data.JnjGTInvoiceClearingReportResponseData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.data.JnjGTInventoryReportResponseData;


/**
 * @author ujjwal.negi
 * 
 */
public interface JnjGTInvoiceClearingReportMapper
{
	/**
	 * Map consignment stock request and response by getting value from the inventory report page and set it in
	 * JnjGTInventoryReportResponseData object.
	 * 
	 * @param jnjGTPageableData
	 * @return JnjGTInventoryReportResponseData
	 * @throws IntegrationException
	 * @throws SystemException
	 */

	public List<JnjGTInvoiceClearingReportResponseData> mapInvoiceClearingReportRequestResponse(final JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException;
}
