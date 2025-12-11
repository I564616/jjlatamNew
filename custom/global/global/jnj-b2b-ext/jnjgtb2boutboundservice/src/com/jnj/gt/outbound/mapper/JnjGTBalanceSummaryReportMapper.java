package com.jnj.gt.outbound.mapper;

import java.util.List;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTFinancialBalanceSummaryReportData;

public interface JnjGTBalanceSummaryReportMapper {
	/**
	 * Map Account aging request and response by getting value from the Financial Summary report page and set it in
	 * JnjGTFinancialBalanceSummaryReportData object.
	 * 
	 * @param jnjGTPageableData
	 * @return JnjGTFinancialBalanceSummaryReportData
	 * @throws IntegrationException
	 * @throws SystemException
	 */

	public List<JnjGTFinancialBalanceSummaryReportData> mapBalanceSummaryReportRequestResponse(JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException;
}
