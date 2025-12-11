package com.jnj.gt.outbound.mapper.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTFinancialBalanceSummaryReportData;
import com.jnj.gt.outbound.mapper.JnjGTBalanceSummaryReportMapper;
import com.jnj.core.dto.JnjGTPageableData;

import de.hybris.platform.util.Config;

public class DefaultJnjGTBalanceSummaryReportMapper implements JnjGTBalanceSummaryReportMapper{

	@SuppressWarnings("resource")
	@Override
	public List<JnjGTFinancialBalanceSummaryReportData> mapBalanceSummaryReportRequestResponse(JnjGTPageableData jnjGTPageableData) throws IntegrationException,
			SystemException {
		BufferedReader br = null;
		String sCurrentLine = null;
		JnjGTFinancialBalanceSummaryReportData jnjGTFinancialBalanceSummaryReportData = null;
		List<JnjGTFinancialBalanceSummaryReportData> balanceSummaryReportDatasList = new ArrayList<JnjGTFinancialBalanceSummaryReportData>();
		final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_CONS_BALANCE_SUMMARY_REPORT_PATH);
	
        //LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
        try {
			br = new BufferedReader(new FileReader(hotFolderBaseLocation));
			  
				while ((sCurrentLine = br.readLine()) != null) {
					jnjGTFinancialBalanceSummaryReportData = new JnjGTFinancialBalanceSummaryReportData();
					 String[] details = sCurrentLine.split("\\~");
					  if(details.length >1){
						  jnjGTFinancialBalanceSummaryReportData.setDueDate(details[0]); 
						  jnjGTFinancialBalanceSummaryReportData.setAmountPaid(details[1]);
						  jnjGTFinancialBalanceSummaryReportData.setTotal(details[2]);
					  }
					  
				  }
				balanceSummaryReportDatasList.add(jnjGTFinancialBalanceSummaryReportData);
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
        
        return balanceSummaryReportDatasList;
	}
}
