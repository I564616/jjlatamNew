package com.jnj.gt.outbound.mapper.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTFinancialCreditSummaryReportData;
import com.jnj.gt.outbound.mapper.JnjGTCreditSummaryReportMapper;
import com.jnj.core.dto.JnjGTPageableData;

import de.hybris.platform.util.Config;

public class DefaultJnjGTCreditSummaryReportMapper implements JnjGTCreditSummaryReportMapper{

	@SuppressWarnings("resource")
	@Override
	public List<JnjGTFinancialCreditSummaryReportData> mapCreditSummaryReportRequestResponse(JnjGTPageableData jnjGTPageableData) throws IntegrationException,
			SystemException {
		BufferedReader br = null;
		String sCurrentLine = null;
		JnjGTFinancialCreditSummaryReportData jnjGTFinancialCreditSummaryReportData = null;
		List<JnjGTFinancialCreditSummaryReportData> creditSummaryReportDatasList = new ArrayList<JnjGTFinancialCreditSummaryReportData>();
		final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_CONS_CREDIT_SUMMARY_REPORT_PATH);
        //LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
        try {
			br = new BufferedReader(new FileReader(hotFolderBaseLocation));
			  
				while ((sCurrentLine = br.readLine()) != null) {
					jnjGTFinancialCreditSummaryReportData = new JnjGTFinancialCreditSummaryReportData();
					 String[] details = sCurrentLine.split("\\~");
					  if(details.length >1){
						  jnjGTFinancialCreditSummaryReportData.setOpenOrderValue(details[0]); 
						  jnjGTFinancialCreditSummaryReportData.setOpenDeliveryValue(details[1]);
						  jnjGTFinancialCreditSummaryReportData.setAmountDue(details[2]);
						  jnjGTFinancialCreditSummaryReportData.setCreditUsed(details[3]); 
						  jnjGTFinancialCreditSummaryReportData.setCreditLimit(details[4]);
						  jnjGTFinancialCreditSummaryReportData.setOverUnderValue(details[5]);
						  jnjGTFinancialCreditSummaryReportData.setCreditLimitUsed(details[6]);
					  }
					  
				  }
				creditSummaryReportDatasList.add(jnjGTFinancialCreditSummaryReportData);
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
        
        return creditSummaryReportDatasList;
	}

}
