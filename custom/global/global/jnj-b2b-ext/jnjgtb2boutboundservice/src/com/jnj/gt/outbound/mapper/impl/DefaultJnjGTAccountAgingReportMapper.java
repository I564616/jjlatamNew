package com.jnj.gt.outbound.mapper.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTFinancialAccountAgingReportData;
import com.jnj.gt.outbound.mapper.JnjGTAccountAgingReportMapper;

import de.hybris.platform.util.Config;

public class DefaultJnjGTAccountAgingReportMapper implements JnjGTAccountAgingReportMapper{

	@SuppressWarnings("resource")
	@Override
	public List<JnjGTFinancialAccountAgingReportData> mapAccountAgingReportRequestResponse(JnjGTPageableData jnjGTPageableData) throws IntegrationException,
			SystemException {
		
		BufferedReader br = null; 
		String sCurrentLine = null;
		JnjGTFinancialAccountAgingReportData jnjGTFinancialAccountAgingReportData = null;
		List<JnjGTFinancialAccountAgingReportData> accountAgingReportDatasList = new ArrayList<JnjGTFinancialAccountAgingReportData>();
		final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_CONS_ACCOUNT_AGING_REPORT_PATH);
		
        //LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
        try {
			br = new BufferedReader(new FileReader(hotFolderBaseLocation));
			  
				while ((sCurrentLine = br.readLine()) != null) {
					jnjGTFinancialAccountAgingReportData = new JnjGTFinancialAccountAgingReportData();
					 String[] details = sCurrentLine.split("\\~");
					  if(details.length >1){
						  jnjGTFinancialAccountAgingReportData.setDaysinArrears(details[0]); 
						  jnjGTFinancialAccountAgingReportData.setDueItem(details[1]);
						  jnjGTFinancialAccountAgingReportData.setNotDue(details[2]);
					  }
					  
				  }
				accountAgingReportDatasList.add(jnjGTFinancialAccountAgingReportData);
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
        
        return accountAgingReportDatasList;
	}
}
