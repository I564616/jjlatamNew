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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.core.model.JnJProductModel;
import com.jnj.gt.outbound.mapper.JnjGTConsInventoryReportMapper;
import com.jnj.gt.outbound.mapper.JnjGTConsignmentStockMapper;
import com.jnj.gt.outbound.services.JnjGTConsignmentStockService;
import com.jnj.core.constants.Jnjb2bCoreConstants;

/**
 * This class represents the mapper layer for the consignment stock flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTConsInventoryReportMapper implements JnjGTConsInventoryReportMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTConsInventoryReportMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	JnjGTConsignmentStockService jnjGTConsignmentStockService;

	@Resource(name = "productService")
	JnJGTProductService productService;

	public JnjGTConsignmentStockService getJnjGTConsignmentStockService() {
		return jnjGTConsignmentStockService;
	}

	public JnJGTProductService getProductService() {
		return productService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.outbound.mapper.JnjGTConsignmentStockMapper#mapConsignmentStockRequestResponse(com.jnj.core.dto.
	 * JnjGTPageableData)
	 */
	@Override
	public List<JnjGTConsInventoryData> mapConsInventoryReportRequestResponse(final JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException
	{
		List<JnjGTConsInventoryData> consInventoryReportList = buildScheduledLinesOutputList();
		return consInventoryReportList;
	}
	
	private List<JnjGTConsInventoryData> buildScheduledLinesOutputList()
	{
		BufferedReader br = null;
		String sCurrentLine = null;
		JnjGTConsInventoryData jnjGTConsIneventoryReportData = null;
		List<JnjGTConsInventoryData> jnjGTConsIneventoryReportDataList = new ArrayList<JnjGTConsInventoryData>();
		final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_CONS_INVENTORY_REPORT_PATH);
        LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
        try {
			br = new BufferedReader(new FileReader(hotFolderBaseLocation));
			  
				while ((sCurrentLine = br.readLine()) != null) {
					jnjGTConsIneventoryReportData = new JnjGTConsInventoryData();
					 String[] details = sCurrentLine.split("\\~");
					  if(details.length >1){
						  jnjGTConsIneventoryReportData.setStockLocationAcc(details[0]);
						  jnjGTConsIneventoryReportData.setStockLocationName(details[1]);
						  jnjGTConsIneventoryReportData.setFranchiseDescription(details[2]);
						  jnjGTConsIneventoryReportData.setProductCode(details[3]);
						  jnjGTConsIneventoryReportData.setProductDesc(details[4]);
						  jnjGTConsIneventoryReportData.setQtyInStock(details[5]);
						  jnjGTConsIneventoryReportData.setParLevelQty(details[6]);
						  jnjGTConsIneventoryReportData.setAvailableOrderQty(details[7]);
						  jnjGTConsIneventoryReportData.setUom(details[8]);
						  jnjGTConsIneventoryReportDataList.add(jnjGTConsIneventoryReportData);
					  }
				  }
				
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
        
        return jnjGTConsIneventoryReportDataList;
	}
		
	@Override
	public List<JnjGTConsInventoryData> fetchBatchDetails(final JnjGTPageableData jnjGTPageableData) throws IntegrationException, SystemException
	{
		LOGGER.info("invoked fetchBatchDetails @ ConsignmentInventory mapper");
		BufferedReader br = null;
		String sCurrentLine = null;
		String[] strings;
		String[] serials;
		Map<String, List<String>> batchSerialMap;
		JnjGTConsInventoryData jnjGTConsInventoryData = null;
		List<JnjGTConsInventoryData> jnjGTConsInventoryDataList = new ArrayList<JnjGTConsInventoryData>();
		final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_CONS_INVENTORY_REPORT_PATH);
        LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
        try {
			br = new BufferedReader(new FileReader(hotFolderBaseLocation));
			  
				while ((sCurrentLine = br.readLine()) != null) {
					jnjGTConsInventoryData = new JnjGTConsInventoryData();
					 String[] details = sCurrentLine.split("\\~");
					  if(details.length >1){
						  batchSerialMap = new HashMap<>();
						  jnjGTConsInventoryData.setProductCode(details[3]);
						  jnjGTConsInventoryData.setSerialManaged(details[9]);
						  jnjGTConsInventoryData.setBatchManaged(details[10]);
						  
						  if(details[9].equalsIgnoreCase("1")&&details[10].equalsIgnoreCase("1"))
						  {
							  strings=details[12].split(",");
							  serials=details[11].split(",");
							  for (int i = 0; i < strings.length; i++) {
								  if(i==0)
								  {
									  batchSerialMap.put(strings[i], Arrays.asList(serials[0],serials[1]));
								  }
								  else if(i==1)
								  {
									  batchSerialMap.put(strings[i], Arrays.asList(serials[2],serials[1]));
								  }
								  else
								  {
									  batchSerialMap.put(strings[i], Arrays.asList(serials[1],serials[2]));
								  }
								  
							}
							 jnjGTConsInventoryData.setBatchSerialMap(batchSerialMap);
						  }
						  
						  if(details[9].equalsIgnoreCase("1")&&!details[11].equalsIgnoreCase("")){
						  strings=details[11].split(",");
						  jnjGTConsInventoryData.setSerialNumbers(Arrays.asList(strings));
						  }
						  if(details[10].equalsIgnoreCase("1")&&!details[12].equalsIgnoreCase("")){
						  strings=details[12].split(",");
						  jnjGTConsInventoryData.setBatchNumbers(Arrays.asList(strings));
						  }
						  jnjGTConsInventoryDataList.add(jnjGTConsInventoryData);
					  }
				  }
		}catch(Exception e) {
			
			e.printStackTrace();
		}
        return jnjGTConsInventoryDataList;
	}
}
