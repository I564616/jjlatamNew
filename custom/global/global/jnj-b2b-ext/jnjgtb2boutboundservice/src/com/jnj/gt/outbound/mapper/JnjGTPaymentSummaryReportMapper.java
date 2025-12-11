package com.jnj.gt.outbound.mapper;

import java.util.List;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTFinancialPaymentSummaryReportData;

public interface JnjGTPaymentSummaryReportMapper {
	/**
	 * Map Account aging request and response by getting value from the Financial Summary report page and set it in
	 * JnjGTFinancialPaymentSummaryReportData object.
	 * 
	 * @param jnjGTPageableData
	 * @return JnjGTFinancialPaymentSummaryReportData
	 * @throws IntegrationException
	 * @throws SystemException
	 */

	public List<JnjGTFinancialPaymentSummaryReportData> mapPaymentSummaryReportRequestResponse(JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException;
}
