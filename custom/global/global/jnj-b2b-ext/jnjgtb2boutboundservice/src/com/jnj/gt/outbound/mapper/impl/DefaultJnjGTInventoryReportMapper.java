package com.jnj.gt.outbound.mapper.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.gt.outbound.mapper.JnjGTInventoryReportMapper;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

public class DefaultJnjGTInventoryReportMapper implements JnjGTInventoryReportMapper{

	public List<JnjGTInventoryReportResponseData> mapInventoryReportRequestResponse(
			JnjGTPageableData jnjGTPageableData) throws IntegrationException,
			SystemException {
		List<JnjGTInventoryReportResponseData> invoiceClearingReportList = buildScheduledLinesOutputList();
		return invoiceClearingReportList;
	}
	
	private List<JnjGTInventoryReportResponseData> buildScheduledLinesOutputList() {

		final String METHOD_NAME = "buildScheduledLinesOutputList()";
		BufferedReader br = null;
		String sCurrentLine = null;
		JnjGTInventoryReportResponseData jnjGTInventoryReportData = null;
		List<JnjGTInventoryReportResponseData> jnjGTinvoiceClearingReportDataList = new ArrayList<JnjGTInventoryReportResponseData>();
		final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_INENTORY_REPORT_PATH);
        //LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
        try {
			br = new BufferedReader(new FileReader(hotFolderBaseLocation));
			  
				while ((sCurrentLine = br.readLine()) != null) {
					jnjGTInventoryReportData = new JnjGTInventoryReportResponseData();
					 String[] details = sCurrentLine.split("\\~");
					  if(details.length >1){
						 
						  jnjGTInventoryReportData.setUcnNumber(details[0]);
						  jnjGTInventoryReportData.setProductCode(details[1]);
						  jnjGTInventoryReportData.setDescription(details[2]);
						  jnjGTInventoryReportData.setLotNumber(details[3]);
						  jnjGTInventoryReportData.setUnrestricted(details[4]);
						  jnjGTInventoryReportData.setRestricted(details[5]);
						  jnjGTInventoryReportData.setQualityStock(details[6]);
						  jnjGTInventoryReportData.setTotalQty(details[7]);
						  jnjGTInventoryReportData.setUnit(details[8]);
						 
						  jnjGTinvoiceClearingReportDataList.add(jnjGTInventoryReportData);
					  }
				  }
		}catch (Exception e) {
			// TODO Auto-generated catch block
			/*LOG.error(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ " There are some problem on fetching data");*/
		}
        
        return jnjGTinvoiceClearingReportDataList;
	

	}

}
