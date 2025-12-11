package com.jnj.gt.outbound.mapper;

import java.util.List;

import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;

public interface JnjGTInventoryReportMapper {
	/**
	 * Map Inventory request and response by getting value from the inventory report page and set it in
	 * JnjGTInventoryReportResponseData object.
	 * 
	 * @param jnjGTPageableData
	 * @return JnjGTInventoryReportResponseData
	 * @throws IntegrationException
	 * @throws SystemException
	 */
	public List<JnjGTInventoryReportResponseData> mapInventoryReportRequestResponse(final JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException;

}
