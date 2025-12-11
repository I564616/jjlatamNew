/**
 * 
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.facades.data.JnjGTInvoiceClearingReportResponseData;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.BATCHIN;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.CUSTOMERIN;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ConsignmentIN;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.MATERIALOUT;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.MATNRIN;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ViewConsignmentStockInput;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ViewConsignmentStockOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.core.model.JnJProductModel;
import com.jnj.gt.outbound.mapper.JnjGTConsInventoryReportMapper;
import com.jnj.gt.outbound.mapper.JnjGTConsignmentStockMapper;
import com.jnj.gt.outbound.mapper.JnjGTInvoiceClearingReportMapper;
import com.jnj.gt.outbound.services.JnjGTConsignmentStockService;
import com.jnj.core.constants.Jnjb2bCoreConstants;

/**
 * This class represents the mapper layer for the consignment stock flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTInvoiceClearingReportMapper implements JnjGTInvoiceClearingReportMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTInvoiceClearingReportMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	JnjGTConsignmentStockService jnjGTConsignmentStockService;

	protected static final Logger LOG = Logger.getLogger(DefaultJnjGTInvoiceClearingReportMapper.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.outbound.mapper.JnjGTConsignmentStockMapper#mapConsignmentStockRequestResponse(com.jnj.core.dto.
	 * JnjGTPageableData)
	 */
	@Override
	public List<JnjGTInvoiceClearingReportResponseData> mapInvoiceClearingReportRequestResponse(final JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException
	{
		List<JnjGTInvoiceClearingReportResponseData> invoiceClearingReportList = buildScheduledLinesOutputList();
		return invoiceClearingReportList;
	}
	
	private List<JnjGTInvoiceClearingReportResponseData> buildScheduledLinesOutputList()
	{
		final String METHOD_NAME = "buildScheduledLinesOutputList()";
		BufferedReader br = null;
		String sCurrentLine = null;
		JnjGTInvoiceClearingReportResponseData invoiceClearingReportResponseData = null;
		List<JnjGTInvoiceClearingReportResponseData> jnjGTinvoiceClearingReportDataList = new ArrayList<JnjGTInvoiceClearingReportResponseData>();
		final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_INVOICE_CLEARING_REPORT_PATH);
        LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
        try {
			br = new BufferedReader(new FileReader(hotFolderBaseLocation));
			  
				while ((sCurrentLine = br.readLine()) != null) {
					 invoiceClearingReportResponseData = new JnjGTInvoiceClearingReportResponseData();
					 String[] details = sCurrentLine.split("\\~");
					  if(details.length >1){
						 
						  invoiceClearingReportResponseData.setInvoiceNum(details[0]);
						  invoiceClearingReportResponseData.setBillingDate(details[1]);
						  invoiceClearingReportResponseData.setSoldToAccNum(details[2]);
						  invoiceClearingReportResponseData.setSoldToAccName(details[3]);
						  invoiceClearingReportResponseData.setSalesDocNum(details[4]);
						  invoiceClearingReportResponseData.setCustomerPoNum(details[5]);
						  invoiceClearingReportResponseData.setReceiptNumber(details[6]);
						  invoiceClearingReportResponseData.setPaymentDate(details[7]);
						  invoiceClearingReportResponseData.setStatus(details[8]);
						  invoiceClearingReportResponseData.setInvoiceTotalAmount(Double.parseDouble(details[9]));
						  invoiceClearingReportResponseData.setOpenAmount(details[10]);
						  invoiceClearingReportResponseData.setCurrency(details[11]);
						  jnjGTinvoiceClearingReportDataList.add(invoiceClearingReportResponseData);
					  }
				  }
		}catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ " There are some problem on fetching data");
		}
        
        return jnjGTinvoiceClearingReportDataList;
	}
		
	
}
