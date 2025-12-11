package com.jnj.gt.outbound.mapper;

import java.util.List;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTFinancialAccountAgingReportData;

public interface JnjGTAccountAgingReportMapper {
	
	/**
	 * Map Account aging request and response by getting value from the Financial Summary report page and set it in
	 * JnjGTFinancialAccountAgingReportData object.
	 * 
	 * @param jnjGTPageableData
	 * @return JnjGTFinancialAccountAgingReportData
	 * @throws IntegrationException
	 * @throws SystemException
	 */

	public List<JnjGTFinancialAccountAgingReportData> mapAccountAgingReportRequestResponse(JnjGTPageableData jnjGTPageableData) throws IntegrationException,
	SystemException;

}
