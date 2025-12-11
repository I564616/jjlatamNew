package com.jnj.gt.outbound.mapper;

import java.util.List;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTFinancialCreditSummaryReportData;

public interface JnjGTCreditSummaryReportMapper {
	
	/**
	 * Map Account aging request and response by getting value from the Financial Summary report page and set it in
	 * JnjGTFinancialCreditSummaryReportData object.
	 * 
	 * @param jnjGTPageableData
	 * @return JnjGTFinancialCreditSummaryReportData
	 * @throws IntegrationException
	 * @throws SystemException
	 */

	public List<JnjGTFinancialCreditSummaryReportData> mapCreditSummaryReportRequestResponse(JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException;

}
