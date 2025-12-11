package com.jnj.gt.outbound.mapper.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTFinancialPaymentSummaryReportData;
import com.jnj.gt.outbound.mapper.JnjGTPaymentSummaryReportMapper;
import com.jnj.core.dto.JnjGTPageableData;

import de.hybris.platform.util.Config;

public class DefaultJnjGTPaymentSummaryReportMapper implements JnjGTPaymentSummaryReportMapper{

	@SuppressWarnings("resource")
	@Override
	public List<JnjGTFinancialPaymentSummaryReportData> mapPaymentSummaryReportRequestResponse(JnjGTPageableData jnjGTPageableData) throws IntegrationException,
			SystemException {
		BufferedReader br = null;
		String sCurrentLine = null;
		JnjGTFinancialPaymentSummaryReportData jnjGTFinancialPaymentSummaryReportData = null;
		List<JnjGTFinancialPaymentSummaryReportData> paymentSummaryReportDatasList = new ArrayList<JnjGTFinancialPaymentSummaryReportData>();
		final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_CONS_PAYMENT_SUMMARY_REPORT_PATH);
		
        //LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
        try {
			br = new BufferedReader(new FileReader(hotFolderBaseLocation));
			  
				while ((sCurrentLine = br.readLine()) != null) {
					jnjGTFinancialPaymentSummaryReportData = new JnjGTFinancialPaymentSummaryReportData();
					 String[] details = sCurrentLine.split("\\~");
					  if(details.length >1){
						  jnjGTFinancialPaymentSummaryReportData.setAmountInvoicedMTD(details[0]); 
						  jnjGTFinancialPaymentSummaryReportData.setNetAmountPaidMTD(details[1]);
						  jnjGTFinancialPaymentSummaryReportData.setAmountInvoicedPriorMonth(details[2]);
						  jnjGTFinancialPaymentSummaryReportData.setNetAmountPaidPriorMonth(details[3]); 
						  jnjGTFinancialPaymentSummaryReportData.setAmountInvoiceThisYear(details[4]);
						  jnjGTFinancialPaymentSummaryReportData.setNetAmountPaidThisYear(details[5]);
						  jnjGTFinancialPaymentSummaryReportData.setAmountInvoicedPriorYear(details[6]); 
						  jnjGTFinancialPaymentSummaryReportData.setNetAmountPaidPrioryear(details[7]);
						  jnjGTFinancialPaymentSummaryReportData.setLastPaymentAmount(details[8]);
					  }
					  
				  }
				paymentSummaryReportDatasList.add(jnjGTFinancialPaymentSummaryReportData);
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
        
        return paymentSummaryReportDatasList;
	}

}
