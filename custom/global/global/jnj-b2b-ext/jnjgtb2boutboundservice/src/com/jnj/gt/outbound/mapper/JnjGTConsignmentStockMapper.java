/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import java.util.List;

import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.data.JnjGTInventoryReportResponseData;


/**
 * @author ujjwal.negi
 * 
 */
public interface JnjGTConsignmentStockMapper
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

	public List<JnjGTInventoryReportResponseData> mapConsignmentStockRequestResponse(final JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException;
}
