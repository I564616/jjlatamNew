/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.mapper.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.facades.data.JnjGTReturnMessageData;
import com.jnj.core.data.JnjGTSimulateDelOrderResponseData;
import com.jnj.core.data.JnjGTSimulateOrderResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.utils.CommonUtil;
import com.jnj.core.util.JnjGTOrderTypeComparator;
import com.jnj.exceptions.IntegrationException;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.TestBackOrderItemFromGatewayInput;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.TestBackOrderItemFromGatewayOutput;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.OrderLinesInput;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.ManualSubLinesOutput;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.ObjectFactory;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.OrderLinesOutput;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.ReturnMessage;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.ScheduledLinesOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.outbound.mapper.JnjGTOutOrderLineMapper;
import com.jnj.gt.outbound.mapper.JnjGTProposedOrderItemMapper;
import com.jnj.gt.outbound.services.JnjGTSimulateOrderService;
import com.jnj.facades.data.JnjGTCommonFormIOData;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.variants.model.VariantProductModel;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.services.cart.JnjGTCartService;

/**
 * The DefaultJnjGTProposedOrderItemMapper class contains the definition of all the method of the jnjGTSimulateOrderMapper
 * interface.
 * 
 * @author  
 * @version 1.0
 */ 
public class DefaultJnjGTProposedOrderItemMapper implements JnjGTProposedOrderItemMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTSimulateOrderMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Resource(name = "commerceCartService")
	protected JnjGTCartService jnjGTCartService;
	
	@Resource(name = "productService")
	JnJGTProductService jnJGTProductService;
	
	@Autowired 
	protected JnjGTOutOrderLineMapper jnjGTOutOrderLineMapper;
	
	@Autowired
	protected JnjConfigService jnjConfigService;
	
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	@Autowired
	protected JnjCartService jnjCartService;
	
	@Autowired
	protected JnjGTOrderService jnjGTOrderService;
	
	@Autowired
	protected ModelService modelService;
	
	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	@Resource(name = "cartFacade")
	protected CartFacade cartFacade;
	
	//@Resource(name="commerceCartService")
	private Converter<CommerceCartModification, CartModificationData> cartModificationConverter;
	
	@Autowired
	protected CartService cartService;
	
	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}
	
	/**
	 * @return the jnJGTProductService
	 */
	public JnJGTProductService getJnJGTProductService()
	{
		return jnJGTProductService;
	}

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}
 
	
	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}
	
	public JnjGTOrderService getJnjGTOrderService() {
		return jnjGTOrderService;
	}

	public ModelService getModelService() {
		return modelService;
	}
	
	protected Converter<CommerceCartModification, CartModificationData> getCartModificationConverter()
	{
		return cartModificationConverter;
	}
	
	public CartService getCartService() {
		return cartService;
	}
	
	/**
	 * This method used to identify is first sap call or not if tru then it will process only proposed item list from the manual substitution to show in the popup 
	 * else it will calculate price and value and schedule line level also operation for status
	 * @throws IntegrationException
	 * @throws SystemException
	 * @throws BusinessException
	 */
	 @Override
		public JnjGTProposedOrderResponseData mapConsignmentSimulateOrderRequestResponse(CartModel cartModel, JnjGTSapWsData wsData) {
			 if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapConsignmentSimulateOrderRequestResponse()" + Logging.HYPHEN
							+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
				}
			 HashMap<String, List <AbstractOrderEntryModel>> abstractOrderEntryModelMap = new HashMap<String, List <AbstractOrderEntryModel>>();
			 CatalogVersionModel catalogVersionModel = null;
			 JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = null;
	 		 catalogVersionModel = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID, Jnjb2bCoreConstants.ONLINE);
	 		LOGGER.info("wsData.getIsfirstSAPCall() : "+wsData.getIsfirstSAPCall() +" wsData.getIsSecondSAPCall() : " + wsData.getIsSecondSAPCall());
			 if(wsData.getIsfirstSAPCall()){
				 jnjGTProposedOrderResponseData = mapProposedOrderRequestResponse(cartModel, catalogVersionModel, abstractOrderEntryModelMap,wsData.getIsfirstSAPCall());
			 }else{
				 //need to pas the input to sap when doing second call
				 jnjGTProposedOrderResponseData = mapConsignmentSimulateOutOrderList(cartModel, catalogVersionModel, abstractOrderEntryModelMap);
			 }
			return jnjGTProposedOrderResponseData;
		}
	 
	/**
	 *  
	 * This method used to identify is first sap call or not if tru then it will process only proposed item list from the manual substitution to show in the popup 
	 * @throws IntegrationException
	 * @throws SystemException
	 * @throws BusinessException
	 */
 
	private JnjGTProposedOrderResponseData mapProposedOrderRequestResponse(CartModel cartModel, CatalogVersionModel catalogVersionModel, 
			HashMap<String, List <AbstractOrderEntryModel>> abstractOrderEntryModelMap,boolean isfirstSAPCall ) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapSimulateOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//HashMap<String, List <AbstractOrderEntryModel>> abstractOrderEntryModelMap = new HashMap<String, List <AbstractOrderEntryModel>>();
		final JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = new JnjGTProposedOrderResponseData();
		TestBackOrderItemFromGatewayOutput response = null;
		
		getAbstractOrderEntryModelMap(cartModel, abstractOrderEntryModelMap);
		response = constructResponseFromGatewayOutput(cartModel, abstractOrderEntryModelMap, isfirstSAPCall);
		
		//preparing the data to show in the popup through ProposedOutOrderItem
		List<JnjGTCommonFormIOData>	proposedOutOrderItemList = constructProposedOutOrderList(catalogVersionModel, response );
		java.util.Collections.sort(proposedOutOrderItemList,jnjGTHybrisLineItemComparator);
		jnjGTProposedOrderResponseData.setJnjGTCommonFormIODataList(proposedOutOrderItemList);
		LOGGER.info("proposedOutOrderItemList ************* size : "+proposedOutOrderItemList.size());
		return jnjGTProposedOrderResponseData;
	}
	 
	/**
	 *  
	 * This method it will calculate price and value and schedule line level also operation for status
	 * @throws IntegrationException
	 * @throws SystemException
	 * @throws BusinessException
	 */
	
	private JnjGTProposedOrderResponseData mapConsignmentSimulateOutOrderList(CartModel cartModel, CatalogVersionModel catalogVersionModel,
			HashMap<String, List <AbstractOrderEntryModel>> abstractOrderEntryModelMap ) {
		    TestBackOrderItemFromGatewayOutput response = null;
		    JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = new JnjGTProposedOrderResponseData();
		    //collecting for amp from cartmodel
		    getAbstractOrderEntryModelMap(cartModel, abstractOrderEntryModelMap);
		    //expecting in this method as SAP call response. So we are creating SAP response for temp
		    response = constructResponseFromGatewayOutput(cartModel, abstractOrderEntryModelMap,false);
			mapCartModelFromOutOrderLine(cartModel, response.getOrderLinesOutput());
			jnjGTProposedOrderResponseData.setJnjGTCommonFormIODataList(mapSapErrorResponse(abstractOrderEntryModelMap));
			jnjCartService.calculateValidatedCart(cartModel);
			return jnjGTProposedOrderResponseData;
	} 
	
	//Soumitra - AAOL-4357
		private List<JnjGTCommonFormIOData> mapSapErrorResponse(HashMap<String, List <AbstractOrderEntryModel>> abstractOrderEntryModelMap)
		{
			List<JnjGTCommonFormIOData> commonFormIODatas = new ArrayList<>();
			JnjGTReturnMessageData returnMessage=null;
			String[] thresholdValues=Config.getParameter(Jnjgtb2boutboundserviceConstants.SapErrorResponse.SAP_ERROR_MOCK_THRESHOLD).split(",");
			for(Map.Entry<String, List <AbstractOrderEntryModel>> entry : abstractOrderEntryModelMap.entrySet() ){
				List<JnjGTReturnMessageData> returnMessages = new ArrayList<JnjGTReturnMessageData>();
				AbstractOrderEntryModel abstractOrderEntry=entry.getValue().get(0);
				JnjGTCommonFormIOData jnjGTCommonFormIOData = new JnjGTCommonFormIOData();
				jnjGTCommonFormIOData.setProductId(abstractOrderEntry.getProduct().getCode());
				if(abstractOrderEntry.getQuantity()>Integer.parseInt(thresholdValues[0]))
				{
					fakeSapErrorMessage(returnMessages,Jnjgtb2boutboundserviceConstants.SapErrorResponse.NOQTY.split(",")); 
				}
				if(abstractOrderEntry.getBatchNumber()!=null && abstractOrderEntry.getBatchNumber().equalsIgnoreCase(thresholdValues[1]))
				{
					fakeSapErrorMessage(returnMessages,Jnjgtb2boutboundserviceConstants.SapErrorResponse.BATCH_NOT_FOUND.split(","));
				}
				if(abstractOrderEntry.getSerialNumber()!=null && abstractOrderEntry.getSerialNumber().equalsIgnoreCase(thresholdValues[2]))
				{
					fakeSapErrorMessage(returnMessages,Jnjgtb2boutboundserviceConstants.SapErrorResponse.SERIAL_NOT_FOUND.split(","));
				}
				if(abstractOrderEntry.getTotalPrice()==null)
				{
					fakeSapErrorMessage(returnMessages,Jnjgtb2boutboundserviceConstants.SapErrorResponse.PRICING_INCOMPLETE.split(","));
				}
				if(abstractOrderEntry.getQuantity()>Integer.parseInt(thresholdValues[3]))
				{
					fakeSapErrorMessage(returnMessages,Jnjgtb2boutboundserviceConstants.SapErrorResponse.CBC_QTY_EXCEED.split(","));
					String exceededByQty=String.valueOf(abstractOrderEntry.getQuantity()-Integer.parseInt(thresholdValues[3]));
					returnMessages.get(returnMessages.size()-1).setMessagev2(exceededByQty);
				}
				jnjGTCommonFormIOData.setJnjGTReturnMessageDataList(returnMessages);
				commonFormIODatas.add(jnjGTCommonFormIOData);
			}
			return commonFormIODatas;
		}

		private void fakeSapErrorMessage(List<JnjGTReturnMessageData> returnMessages, String[] splitString) {
			JnjGTReturnMessageData returnMessage;
			returnMessage=new JnjGTReturnMessageData();
			returnMessage.setId(splitString[0]);
			returnMessage.setNumber(splitString[1]);
			returnMessage.setMessage(splitString[2]);
			returnMessage.setLogno(splitString[3]);
			returnMessages.add(returnMessage);
		}
		//Soumitra - AAOL-4357
/*
 *  This method is temp to construct manual substitution
 * @see com.jnj.gt.outbound.mapper.JnjGTProposedOrderItemMapper#buildManualSubstitutionList(de.hybris.platform.core.model.order.CartModel, de.hybris.platform.catalog.model.CatalogVersionModel, java.util.HashMap)
 */
	private List<ManualSubLinesOutput> buildManualSubstitutionList(String orderedItemCode,
			 HashMap<String, List<AbstractOrderEntryModel>> cartEntryMap) {
		List<ManualSubLinesOutput> manualSubLinesOutputList = new ArrayList<ManualSubLinesOutput>(); 
		BufferedReader br = null;
		try {
			String sCurrentLine;
			String proposedItemCodesList = null;
			String proposedItemCode = null;
		    String sapOrderedItemCode = null;
		   // String hyLineItemCode = null;
			final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_PROPOSED_FILE_PATH);
	        LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
	        br = new BufferedReader(new FileReader(hotFolderBaseLocation));//file name with path
	        while ((sCurrentLine = br.readLine()) != null) {
	        	LOGGER.info(" sCurrentLine : " +sCurrentLine);
	               String[] details = sCurrentLine.split("\\~");
	               LOGGER.info(" details.length ~: " +details.length);
	               if(details.length >1){
	               
                  	//hyLineItemCode = details[0];
	                sapOrderedItemCode = details[0]; // original product code
	                proposedItemCodesList = details[1]; // proposed product List
                  	
					//AAOL-6367
					if(orderedItemCode.contains(sapOrderedItemCode) && !cartEntryMap.get(orderedItemCode).get(0).getIsProposed() ){
						 ManualSubLinesOutput manualSubLinesOutput = new ManualSubLinesOutput();
						 LOGGER.info(" cartEntryMap.get(orderedItemCode).get(0).getEntryNumber() : " + cartEntryMap.get(orderedItemCode).get(0).getEntryNumber());
	                  		manualSubLinesOutput.setITMNUMBER(cartEntryMap.get(orderedItemCode).get(0).getEntryNumber()); // seq line item
		              		manualSubLinesOutput.setMATERIAL(orderedItemCode);
		              		manualSubLinesOutput.setTARGETQTY(cartEntryMap.get(orderedItemCode).get(0).getQuantity());//Integer.parseInt(details[2]));
		              		//AAOL-6368
		              		StringTokenizer proposedItemCodes=new StringTokenizer(proposedItemCodesList, ",");
		              		List<String> replacementItems = new ArrayList<String>();
			              		while(proposedItemCodes.hasMoreTokens()){
								proposedItemCode=proposedItemCodes.nextToken();
								LOGGER.info("  sapOrderedItemCode : "+ sapOrderedItemCode + " , ProposedItemCd : "+proposedItemCode);
								//AAOL-6377 && AAOL-6368
								replacementItems.add(proposedItemCode);								
								//manualSubLinesOutput.setTARGETQTY(cartEntryMap.get(orderedItemCode).get(0).getQuantity());//Integer.parseInt(details[2]));
							}
							manualSubLinesOutput.setSUBMATNO(replacementItems); // proposed prod code
							//manualSubLinesOutput.setProposedItemQty(Integer.parseInt(details[4]));
							manualSubLinesOutputList.add(manualSubLinesOutput);  // need to include if product is available in hybris catalog else no need to show int he popup
                  	}else{
                  		LOGGER.info("There is no manual substitution products available from SAP for this product");
                  	}
	                 
              	}
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	return manualSubLinesOutputList;
	}

	private List<ScheduledLinesOutput> buildScheduledLinesOutputList(String orderedItemCode,
			 HashMap<String, List<AbstractOrderEntryModel>> cartEntryMap) throws CommerceCartModificationException {
		List<ScheduledLinesOutput> scheduledLinesOutputList = new ArrayList<ScheduledLinesOutput>(); 
		BufferedReader br = null;
		try {
			String sCurrentLine;
			String conversionType = null;
		    String sapOrderedItemCode = null;
		    String schLineStatus=null;
		    String proposedProduct=null;
		    long confirmQty =0l;
		    int idx = 0;
		   // String hyLineItemCode = null;
			final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_PROPOSED_FILE_PATH);
	        LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
	        br = new BufferedReader(new FileReader(hotFolderBaseLocation));//file name with path
	        while ((sCurrentLine = br.readLine()) != null) {
	               String[] details = sCurrentLine.split("\\^");
	               if(details.length >1){
	            	   ScheduledLinesOutput scheduledLinesOutput = new ScheduledLinesOutput();
	                sapOrderedItemCode = details[0]; // original product code
	                conversionType = details[6]; // Substitution Type
                  	
                  	LOGGER.info(" OrderItemCd : " +orderedItemCode+" , sapOrderedItemCode : "+ sapOrderedItemCode + " and conversionType "+conversionType);
                  	
              		scheduledLinesOutput.setREQQTY(cartEntryMap.get(orderedItemCode).get(0).getQuantity());   
              		Date date1 =  convertStringToDateFormat(details[2]);
              		Date date2 =  convertStringToDateFormat(details[3]);
              		confirmQty = Long.parseLong(details[4]);
              		idx = Integer.parseInt(details[5]);
              		schLineStatus = buildscheduleLineStatus(date1, date2, confirmQty);
              		scheduledLinesOutput.setCONFSTAT(schLineStatus);
              		XMLGregorianCalendar availableDate =  toXMLGregorianCalendar(date1);
              		LOGGER.info("availableDate : "+availableDate);
              		scheduledLinesOutput.setMSDATE(availableDate);
              		XMLGregorianCalendar reqDeliveryDate =  toXMLGregorianCalendar(date2);
              		LOGGER.info("reqDeliveryDate : "+reqDeliveryDate);
              		scheduledLinesOutput.setREQDATE(reqDeliveryDate);
              		scheduledLinesOutput.setCONFIRQTY(confirmQty);
              		scheduledLinesOutput.setSCHEDLINE(idx);
              		
                  	if(orderedItemCode.equalsIgnoreCase(sapOrderedItemCode) && conversionType.equalsIgnoreCase("M") ){
                  		LOGGER.info("idx : "+idx);
                  		scheduledLinesOutput.setITMNUMBER(Integer.parseInt(sapOrderedItemCode)); // seq line item //
	              		scheduledLinesOutputList.add(scheduledLinesOutput);  // need to include if product is available in hybris catalog else no need to show int he popup
                  	}else if(orderedItemCode.equalsIgnoreCase(sapOrderedItemCode) && StringUtils.equalsIgnoreCase("A", conversionType) ){
                  		LOGGER.info("There is a ScheduledLines  available for Automatic Substitution");
                  		proposedProduct = details[7]; // proposed product code
                  			try {
    							CartModificationData cartModification = cartFacade.updateCartEntry(cartEntryMap.get(orderedItemCode).get(0).getEntryNumber(), 0);
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
                  			addPartialProduct(proposedProduct, confirmQty);
                			scheduledLinesOutput.setITMNUMBER(Integer.parseInt(proposedProduct)); // seq line item //
                			scheduledLinesOutput.setSCHEDLINE(idx);
                			scheduledLinesOutputList.add(scheduledLinesOutput);
                  	}else if(orderedItemCode.equalsIgnoreCase(sapOrderedItemCode) && StringUtils.equalsIgnoreCase("P", conversionType)){
                  		LOGGER.info("There is a ScheduledLines  available for Partial Substitution");
                  		LOGGER.info("idx : "+idx);
                  		proposedProduct = details[7]; // proposed product code
                  		if(orderedItemCode.equalsIgnoreCase(proposedProduct) ){
                  			scheduledLinesOutput.setITMNUMBER(Integer.parseInt(orderedItemCode)); // seq line item //
                  			scheduledLinesOutput.setSCHEDLINE(idx);
                  			try {
    							CartModificationData cartModification = cartFacade.updateCartEntry(cartEntryMap.get(orderedItemCode).get(0).getEntryNumber(), confirmQty);
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
                  		}else{
                  			addPartialProduct(proposedProduct, confirmQty);
                			scheduledLinesOutput.setITMNUMBER(Integer.parseInt(proposedProduct)); // seq line item //
                			scheduledLinesOutput.setSCHEDLINE(idx);
                  		}
	              		scheduledLinesOutputList.add(scheduledLinesOutput);  // need to include if product is available in hybris catalog else no need to show int he popup
                  	}else {
                  		LOGGER.info("There is no ScheduledLines  available from SAP");
                  	}
                  	
              	}
	          
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		return scheduledLinesOutputList;
	}
	
	private String buildscheduleLineStatus(Date availableDate, Date reqDeliveryDate, long confirmQty){
		String status = null;
		LOGGER.info("availableDate : "+ availableDate + " reqDeliveryDate : "+ reqDeliveryDate);
		if(confirmQty > 0 && (availableDate != null && reqDeliveryDate != null ) &&  (availableDate.compareTo(reqDeliveryDate) == 0) ){
			status = "CQ";
		}else if(confirmQty > 0){
			status = "UC";
		}
		LOGGER.info("buildscheduleLineStatus status :  " + status);
		return status;
	}

	/*
	 * Fetching the availabel product from hybris based on the catalog to show int he popup
	 */
	private JnJProductModel getProductDetails(String productCode, CatalogVersionModel catalogVersionModel){
		final JnJProductModel jnjProductModel = jnJGTProductService.getProductModelByCode(productCode, catalogVersionModel);
		return jnjProductModel;
	}
	
	private void getAbstractOrderEntryModelMap(CartModel cartModel, HashMap<String, List <AbstractOrderEntryModel>> abstractOrderEntryModelMap ){
		List<AbstractOrderEntryModel>  entries = cartModel.getEntries();
		if ((entries != null) && (!(entries.isEmpty()))) {
			for ( AbstractOrderEntryModel abstractOrderEntryModel : entries)  {
					final	String materialId   = abstractOrderEntryModel.getProduct().getCode();
					List<AbstractOrderEntryModel> abstractOrderEntryModelList = abstractOrderEntryModelMap.get(materialId);
					if (CollectionUtils.isEmpty(abstractOrderEntryModelList)) {
						abstractOrderEntryModelList = new ArrayList<AbstractOrderEntryModel>();
					}
					abstractOrderEntryModelList.add(abstractOrderEntryModel);
					abstractOrderEntryModelMap.put(materialId, abstractOrderEntryModelList);
			}
		}
	}
	
	/*
	 * This method using to construct the SAP response for the line items level and return the list of OrderLinesOutput
	 * This method using temp purpose without SAP call in GT. if region then SAP will response  
	 */
	private TestBackOrderItemFromGatewayOutput constructResponseFromGatewayOutput(CartModel cartModel, 
			 HashMap<String, List <AbstractOrderEntryModel>> cartEntryMap, boolean isfirstSAPCall ){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "constructResponseFromGatewayOutput()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		TestBackOrderItemFromGatewayOutput testBackOrderItemFromGatewayOutput = new TestBackOrderItemFromGatewayOutput();
		final List<OrderLinesOutput> orderLinesOutputList = new ArrayList<OrderLinesOutput>();
		// Iterating the Abstract Order Entries models one by one.
		testBackOrderItemFromGatewayOutput.setStrOrderType(cartModel.getOrderType().getCode());
		testBackOrderItemFromGatewayOutput.setStrAccountCountry(cartModel.getUnit().getCountry().getIsocode());
		//testBackOrderItemFromGatewayOutput.setStrShipTo(value);
		Integer entryNo=null;
		String orderedItemCode=null;
		double dropShipFee = 10.0;
		double totalFreightFees = 10.0;
		double minimumOrderFee = 10.0;
		
		for (AbstractOrderEntryModel abstOrderEntryModel :   cartModel.getEntries()) {
			Double netValue = 0.0;
			OrderLinesOutput orderLinesOutput = new OrderLinesOutput();
			
			entryNo = abstOrderEntryModel.getEntryNumber();
			orderLinesOutput.setITMNUMBER(entryNo);   //SAP Order Line Number
			orderedItemCode = abstOrderEntryModel.getProduct().getCode();
			LOGGER.info("entryNo : "+ entryNo +" , orderedItemCode : "+ orderedItemCode);
			orderLinesOutput.setMATERIAL(orderedItemCode);  // Material
			orderLinesOutput.setMATENTRD(orderedItemCode);  //Original Material
			orderLinesOutput.setSHORTTEXT("short text for sales order item");  //Short text for sales order item 
			orderLinesOutput.setREQQTY(abstOrderEntryModel.getQuantity());  // Quantity in Sales Units
			orderLinesOutput.setITMCATEGRY(Jnjb2bCoreConstants.BonusItem.CHRGD_ITEM_CATEGORY);
			
			if(orderLinesOutput.getREQQTY() >0 && abstOrderEntryModel.getBasePrice()!=null){
				netValue=  orderLinesOutput.getREQQTY() * abstOrderEntryModel.getBasePrice();
				LOGGER.info("netValue = outOrderLine.getMaterialQuantity() * abstOrderEntModel.getBasePrice() : "+netValue);
			}
			 
			orderLinesOutput.setNETVALUE(netValue.toString());//Net value of the order item in document currency
			
			//orderLinesOutput.setTAXAMOUNT(abstOrderEntryModel.);// Tax amount in document currency 
			orderLinesOutput.setROUTE(abstOrderEntryModel.getSelectedRoute());//  ROUTE
			orderLinesOutput.setSHIPPOINT(abstOrderEntryModel.getShippingPoint());// SHIP_POINT
			orderLinesOutput.setBATCH(abstOrderEntryModel.getBatchNum());  // BATCH
			if(isfirstSAPCall){
				List<ManualSubLinesOutput> manualSubLinesOutputList = buildManualSubstitutionList(orderedItemCode, cartEntryMap);
				LOGGER.info("manualSubLinesOutputList size :"+manualSubLinesOutputList.size());
				orderLinesOutput.getManualSubLinesOutput().addAll(manualSubLinesOutputList);
				orderLinesOutput.setMATENTRD(orderedItemCode);  //Original Material setting only in case of manual substitution
			}
			if(!isfirstSAPCall){
				//Original Material being set in case of manual substitution through the persisting value in orderEntry
				orderLinesOutput.setMATENTRD(abstOrderEntryModel.getOriginalOrderItem()!=null?abstOrderEntryModel.getOriginalOrderItem():orderedItemCode);
				List<ScheduledLinesOutput> scheduledLinesOutputList = null;
				try {
					scheduledLinesOutputList = buildScheduledLinesOutputList(orderedItemCode, cartEntryMap);
				} catch (CommerceCartModificationException e) {
					e.printStackTrace();
				}
				LOGGER.info("scheduledLinesOutputList size :"+scheduledLinesOutputList.size());
				if(CollectionUtils.isEmpty(scheduledLinesOutputList)){ //assuming ther is no BO product 
					scheduledLinesOutputList.add(constructNonBOProduct(orderedItemCode,cartEntryMap));
				}
				else
				{
					String scheduleLinesToRemove=null;
					int index=-1;
					
					// adding schedule lines to appropriate products - Soumitra
					for (Iterator iterator = scheduledLinesOutputList.iterator(); iterator.hasNext();) {
						ScheduledLinesOutput scheduledLinesOutput = (ScheduledLinesOutput) iterator.next();
						index=index+1;
						if(!orderedItemCode.equalsIgnoreCase(String.valueOf(scheduledLinesOutput.getITMNUMBER())))
						{
							scheduleLinesToRemove=index+",";
							Double partialProductnetValue = 0.0;
							OrderLinesOutput partialProductOrderLinesOutput = new OrderLinesOutput();
							partialProductOrderLinesOutput.setITMNUMBER(entryNo+1);   //SAP Order Line Number
							partialProductOrderLinesOutput.setMATERIAL(String.valueOf(scheduledLinesOutput.getITMNUMBER()));  // Material
							partialProductOrderLinesOutput.setMATENTRD(orderedItemCode);  //Original Material
							partialProductOrderLinesOutput.setSHORTTEXT("short text for sales order item");  //Short text for sales order item 
							partialProductOrderLinesOutput.setREQQTY(scheduledLinesOutput.getCONFIRQTY());  // Quantity in Sales Units
							partialProductOrderLinesOutput.setITMCATEGRY(Jnjb2bCoreConstants.BonusItem.CHRGD_ITEM_CATEGORY);
							partialProductOrderLinesOutput.getScheduledLinesOutput().add(scheduledLinesOutput);
							orderLinesOutputList.add(partialProductOrderLinesOutput);
						}
					}
					LOGGER.info("completed mapping schedule lines to appropriate partially available product");
					LOGGER.info("starting removal of processed sheduleLines> indices> "+scheduleLinesToRemove);
					//starting removal of processed sheduleLines - Soumitra
					if(scheduleLinesToRemove!=null)
					{
						LOGGER.info("starting removal of processed sheduleLines> indices");
						String[] scheduleLinesToRemoveArray = scheduleLinesToRemove.split(",");
						for (int i = 0; i < scheduleLinesToRemoveArray.length; i++) {
							if(scheduleLinesToRemoveArray[i]!=null && !scheduleLinesToRemoveArray[i].equalsIgnoreCase(""))
							scheduledLinesOutputList.remove(Integer.parseInt(scheduleLinesToRemoveArray[i]));
						}
						LOGGER.info("completed removal of processed sheduleLines> indices");
					}
				}
				orderLinesOutput.getScheduledLinesOutput().addAll(scheduledLinesOutputList);
			}
			orderLinesOutputList.add(orderLinesOutput);
		}
		testBackOrderItemFromGatewayOutput.getOrderLinesOutput().addAll(orderLinesOutputList);
		return testBackOrderItemFromGatewayOutput;
	}
	
	//set for dummy for no back ordered  product, So creating schedule line to display in current date
	private ScheduledLinesOutput constructNonBOProduct(String orderedItemCode, HashMap<String, List<AbstractOrderEntryModel>> cartEntryMap){
		ScheduledLinesOutput scheduledLinesOutput = new ScheduledLinesOutput();
  		//scheduledLinesOutput.setDOCNUMBER(details[0]);
  		scheduledLinesOutput.setITMNUMBER(Integer.parseInt(orderedItemCode)); // seq line item //
  		//LOGGER.info("idx : "+idx);
  		  //Integer.parseInt(details[1])
  		scheduledLinesOutput.setREQQTY(cartEntryMap.get(orderedItemCode).get(0).getQuantity());  //Long.parseLong(details[2])
  		Date date1 = new Date(); 
  		Date date2 =  new Date(); 
  		long confirmQty  = cartEntryMap.get(orderedItemCode).get(0).getQuantity();
  		//idx = Integer.parseInt(details[6]);
  		String schLineStatus = buildscheduleLineStatus(date1, date2, confirmQty);
  		scheduledLinesOutput.setCONFSTAT(schLineStatus);
  		
  		XMLGregorianCalendar availableDate =  toXMLGregorianCalendar(date1);
  		LOGGER.info("availableDate : "+availableDate);
  		scheduledLinesOutput.setMSDATE(availableDate);
  		
  		XMLGregorianCalendar reqDeliveryDate =  toXMLGregorianCalendar(date2);
  		LOGGER.info("reqDeliveryDate : "+reqDeliveryDate);
  		scheduledLinesOutput.setREQDATE(reqDeliveryDate);
  		
  		scheduledLinesOutput.setCONFIRQTY(confirmQty);
  		scheduledLinesOutput.setSCHEDLINE(1);
  		return scheduledLinesOutput;
	}
	
	/*
	 * This method used to separate the fields from response
	 */
	 
	private List<JnjGTCommonFormIOData> constructProposedOutOrderList(CatalogVersionModel catalogVersionModel, 
			 TestBackOrderItemFromGatewayOutput response) {
		List<JnjGTCommonFormIOData> proposedOutOrderItemList = new ArrayList<JnjGTCommonFormIOData>(); 
		List<OrderLinesOutput> orderLinesOutputList = response.getOrderLinesOutput();
		int outLineEntryNo = 0;
		if(CollectionUtils.isNotEmpty(orderLinesOutputList)){
			for (OrderLinesOutput orderLinesOutput : orderLinesOutputList) {
				outLineEntryNo = orderLinesOutput.getITMNUMBER();
				LOGGER.info("orderLinesOutput.getITMNUMBER() : " + outLineEntryNo);
				if(CollectionUtils.isNotEmpty(orderLinesOutput.getManualSubLinesOutput())){
					for (ManualSubLinesOutput manualSubLinesOutput : orderLinesOutput.getManualSubLinesOutput()) {
						JnjGTCommonFormIOData proposedOutOrderItem = new JnjGTCommonFormIOData();
						JnJProductModel origProductModel = getProductDetails(manualSubLinesOutput.getMATERIAL(), catalogVersionModel);
						LOGGER.info("manualSubLinesOutput.getITMNUMBER() : " + manualSubLinesOutput.getITMNUMBER());
						proposedOutOrderItem.setHybrisLineItemNo(String.valueOf(manualSubLinesOutput.getITMNUMBER())); // seq line item
		          		proposedOutOrderItem.setOrderItemCd(manualSubLinesOutput.getMATERIAL());
		          		proposedOutOrderItem.setOrderItemDesc(origProductModel != null ?origProductModel.getDescription():null); //get description from hybris 
		          		proposedOutOrderItem.setOrderItemQty(manualSubLinesOutput.getTARGETQTY());
		          		//AAOL-6377 and 6368 start
		          		List<String> proposedMateDesc = new ArrayList<String>();
						for(String proposedMaterial:manualSubLinesOutput.getSUBMATNO()){//getting List of Proposed Item Description
							JnJProductModel proposedProdModel = getProductDetails(proposedMaterial, catalogVersionModel);
							if(proposedProdModel != null){
								proposedMateDesc.add(proposedProdModel.getDescription() != null ?proposedProdModel.getDescription(): null);  // proposed prod Description from hybris
			          		}
							
						}
						proposedOutOrderItem.setProposedItemDesc(proposedMateDesc);//Setting the Proposed Item description List
		          		//AAOL-6378 and AAOL-6368 end
						proposedOutOrderItem.setProposedItemCd(manualSubLinesOutput.getSUBMATNO()); // proposed prod code
		          		proposedOutOrderItem.setProposedItemQty(manualSubLinesOutput.getTARGETQTY());
		          		proposedOutOrderItemList.add(proposedOutOrderItem);  // need to include if product is available in hybris catalog else no need to show int he popup
						
		          		
					}
				}else{
					LOGGER.info("There is no manual substitution available for this");
				}
			}
		}
		return proposedOutOrderItemList;
	}


	/*
	 * Making sort order to arrange in the line item h=based on the hybris line item
	 */
	private Comparator<JnjGTCommonFormIOData> jnjGTHybrisLineItemComparator = new Comparator<JnjGTCommonFormIOData>() {
		@Override
		public int compare(JnjGTCommonFormIOData p1, JnjGTCommonFormIOData p2) {
			Integer accounts1 = Integer.parseInt(p1.getHybrisLineItemNo());
			Integer accounts2 = Integer.parseInt(p2.getHybrisLineItemNo());
			// ascending order
			return accounts1.compareTo(accounts2);
		}
	};
	
/*
 * Convert String to date
 */
 public static Date convertStringToDateFormat(final String date) {
		Date parsedDate = null;
		if (date != null) {
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
//			final SimpleDateFormat formatter = new SimpleDateFormat(jnjcommonUtil.);
			try {
				parsedDate = formatter.parse(date);
			} catch (final ParseException exception) {
				exception.printStackTrace();
			}
		}
		return parsedDate;
	}
	 
 /*
  * Converts java.util.Date to javax.xml.datatype.XMLGregorianCalendar
  */
 public static XMLGregorianCalendar toXMLGregorianCalendar(Date date){
     GregorianCalendar gCalendar = new GregorianCalendar();
     gCalendar.setTime(date);
     XMLGregorianCalendar xmlCalendar = null;
     try {
         xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
     } catch (DatatypeConfigurationException ex) {
        // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE, null, ex);
     }
     return xmlCalendar;
 }

 /*
  * Converts XMLGregorianCalendar to java.util.Date in Java
  */
 public static Date toDate(XMLGregorianCalendar calendar){
     if(calendar == null) {
         return null;
     }
     return calendar.toGregorianCalendar().getTime();
 }
 
 
 protected void mapCartModelFromOutOrderLine(final CartModel cartModel, List<OrderLinesOutput> orderLinesOutputList)  {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		double totalDiscounts = 0.0;
		double totalNetValue = 0.0;
		double dropShipFee = 10.0;
		double totalFreightFees = 10.0;
		double minimumOrderFee = 10.0;
		double grossPrice = 0.0;
		double hsaPromotion = 0.0;
		
		final CurrencyModel currencyModel = cartModel.getCurrency();
		final DecimalFormat decimalFormat = new DecimalFormat("#.##");
		CatalogVersionModel catalogVersionModel = null;
		String productCode = null;
		final Map<String, AbstractOrderEntryModel> mapMaterialNoWithOrdLinesOutput = new HashMap<String, AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel abstOrdEntryModel : cartModel.getEntries())
		{
			// Null check for the OrderLinesOutput object and material number as the material number is of JAXB element type.
			if (null != abstOrdEntryModel && null != abstOrdEntryModel.getProduct())
			{
				final	String materialId   = abstOrdEntryModel.getProduct().getCode();
				productCode = new String(materialId);
				mapMaterialNoWithOrdLinesOutput.put(abstOrdEntryModel.getProduct().getCode(), abstOrdEntryModel);
			}
		}
	 
		final List<String> lineNumberExcluded = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.LINE_NUMBER_EXCLUDED,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		// To fetch active Catalog Version Model.
		if (null != cartModel.getSite() && CollectionUtils.isNotEmpty(cartModel.getSite().getStores())) {
			if (CollectionUtils.isNotEmpty(cartModel.getSite().getStores().get(0).getCatalogs())) {
				catalogVersionModel = cartModel.getSite().getStores().get(0).getCatalogs().get(0).getActiveCatalogVersion();
			}
		}
		
		if( CollectionUtils.isNotEmpty(orderLinesOutputList)){
		// Iterates the Cart Entries one by one and populates its fields value by getting them from the response object.
		for (OrderLinesOutput orderLinesOutput : orderLinesOutputList) {
			// In case of excluded customer, enter inside if block.
			if (null != orderLinesOutput && null != orderLinesOutput.getITMCATEGRY() && !lineNumberExcluded.contains(orderLinesOutput.getITMCATEGRY())) {
				AbstractOrderEntryModel abstOrdEntryModel = null;
				// In case of excluded products, enter inside if block.
					try {
						JnJProductModel product = null;
						JnJProductModel productEntered = null;
						ProductModel baseProduct = null;
						ProductModel baseProductEntered = null;
						//orderLinesOutput.setMaterialNumber(productCode);
						for(String prodCode:mapMaterialNoWithOrdLinesOutput.keySet()){
							productCode=prodCode;
							//doing a bad fix to stop unwanted iteration to fix issue in total calculation. Code needs to be revamped - Soumitra
						if (StringUtils.equals(productCode, orderLinesOutput.getMATERIAL())) {
						if (StringUtils.equals(productCode, orderLinesOutput.getMATERIAL())) {
								product = jnJGTProductService.getProductModelByCode(orderLinesOutput.getMATERIAL(), catalogVersionModel);
								if (null != product) {
									baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
									baseProductEntered = baseProduct;
								}
						} else {
							product = jnJGTProductService.getProductModelByCode(orderLinesOutput.getMATERIAL(), catalogVersionModel);
							productEntered = jnJGTProductService.getProductModelByCode(orderLinesOutput.getMATENTRD(), catalogVersionModel);
							if (null != product) {
								baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
							}
							if (null != productEntered) {
								baseProductEntered = productEntered.getMaterialBaseProduct() == null ? productEntered : productEntered.getMaterialBaseProduct();
							}
						}
						// Check if the base product entered is not null.
						if (null != baseProductEntered && null != baseProduct) {
							if (!mapMaterialNoWithOrdLinesOutput.containsKey(baseProductEntered.getCode())) {
								continue;
							}
							abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(baseProductEntered.getCode());
							abstOrdEntryModel.setProduct(baseProduct);
							//change to show the original item label for all substituted products in all scenarios - Soumitra
							abstOrdEntryModel.setOriginalOrderItem(orderLinesOutput.getMATENTRD());
							// check for the null value and set it in Sap Order Line Number.
							if (null != orderLinesOutput.getITMCATEGRY()) {
								abstOrdEntryModel.setSapOrderlineNumber(orderLinesOutput.getITMCATEGRY());
							}

							if (null != orderLinesOutput.getBASEUOM()) {
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(orderLinesOutput.getBASEUOM());
								abstOrdEntryModel.setBaseUOM(unitModel);
							}

							if (null != orderLinesOutput.getSALESUOM()) {
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(orderLinesOutput.getSALESUOM());
								abstOrdEntryModel.setUnit(unitModel);
								if (CollectionUtils.isNotEmpty(baseProduct.getVariants())) {
									// Get the Variant Product Models of the base product.
									final List<VariantProductModel> variantProductModels = (List) baseProduct.getVariants();
									// Iterate them one by one.
									for (final VariantProductModel variantProductModel : variantProductModels) {
										// Check it for not null value for the models and equate the unit model code of the Variant model with the incoming response sales uom.
										if (null != variantProductModel && null != variantProductModel.getUnit()
												&& variantProductModel.getUnit().getCode().equalsIgnoreCase(orderLinesOutput.getSALESUOM())) {
											// set the variant product model in reference variant model.
											abstOrdEntryModel.setReferencedVariant((JnjGTVariantProductModel) variantProductModel);
											break;
										}
									}
								}
							}
							 
							if ( orderLinesOutput.getHGLVITEM() >0) {
								abstOrdEntryModel.setHigherLevelItemNo(String.valueOf(orderLinesOutput.getHGLVITEM()));
							}

							abstOrdEntryModel.setItemCategory("");
							if (null != orderLinesOutput.getROUTE())
							{
								abstOrdEntryModel.setRoute(orderLinesOutput.getROUTE());
							}
							if (null != orderLinesOutput.getSHIPPOINT())
							{
								abstOrdEntryModel.setShippingPoint(orderLinesOutput.getSHIPPOINT());
							}
							if (null != orderLinesOutput.getPRICETYPE())
							{
								abstOrdEntryModel.setPriceType(orderLinesOutput.getPRICETYPE());
							}
							 
							
							//if (null != orderLinesOutput.getFreightFees() && !Jnjgtb2boutboundserviceConstants.EMPTY_STRING.equals(orderLinesOutput.getFreightFees() )) {
								abstOrdEntryModel.setFreightFees(Double.valueOf(Double.valueOf(totalFreightFees).toString()));
								totalFreightFees = totalFreightFees + abstOrdEntryModel.getFreightFees().doubleValue();
							//}
							//if (null != orderLinesOutput.getDropshipFee() && StringUtils.isNotEmpty(orderLinesOutput.getDropshipFee())) {
								abstOrdEntryModel.setDropshipFee(Double.valueOf(Double.valueOf(dropShipFee).toString()));
								dropShipFee = dropShipFee + abstOrdEntryModel.getDropshipFee().doubleValue();
							//}
							// (null != orderLinesOutput.getMinimumOrderFee() && StringUtils.isNotEmpty(orderLinesOutput.getMinimumOrderFee())) {
								abstOrdEntryModel.setMinimumOrderFee(Double.valueOf(Double.valueOf(minimumOrderFee).toString()));
								minimumOrderFee = minimumOrderFee + abstOrdEntryModel.getMinimumOrderFee().doubleValue();
							//}
							if (null != orderLinesOutput.getNETVALUE() && StringUtils.isNotEmpty(orderLinesOutput.getNETVALUE())) {
								abstOrdEntryModel.setNetPrice(Double.valueOf(orderLinesOutput.getNETVALUE()));
								totalNetValue = totalNetValue + abstOrdEntryModel.getNetPrice().doubleValue();
							}
							if (null != orderLinesOutput.getPLANT()) {
								abstOrdEntryModel.setPlant(orderLinesOutput.getPLANT());
							} 
							if (null != orderLinesOutput.getUNLOADPOINT()) {
								abstOrdEntryModel.setUnloadingPoint(orderLinesOutput.getUNLOADPOINT());
							}
							 
							 if (StringUtils.isNotEmpty(orderLinesOutput.getBATCH() )) {
								abstOrdEntryModel.setBatchNum(orderLinesOutput.getBATCH());
							 } 
							// To Set the Schedule Lines information in CartModel
							if (null != orderLinesOutput.getScheduledLinesOutput()) {
								long quantity = 0;
								if (CollectionUtils.isNotEmpty(abstOrdEntryModel.getDeliverySchedules())) {
									modelService.removeAll(abstOrdEntryModel.getDeliverySchedules());
								}
								final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<JnjDeliveryScheduleModel>();
								final List<String> exceptedDateFormatList = JnJCommonUtil.getValues( Jnjgtb2boutboundserviceConstants.EXCEPTED_DATE_FORMAT, Jnjb2bCoreConstants.SYMBOl_COMMA);
								LOGGER.info(" orderLinesOutput scheduledLinesOutputList size :"+orderLinesOutput.getScheduledLinesOutput().size());
								for (final ScheduledLinesOutput scheduledLines : orderLinesOutput.getScheduledLinesOutput()) {
									// Check for the not null object
									if (null != scheduledLines) {
										final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
										jnjDelSchModel.setOwnerEntry(abstOrdEntryModel);
										if ( scheduledLines.getITMNUMBER() > 0) {
											jnjDelSchModel.setLineNumber(String.valueOf(scheduledLines.getITMNUMBER()));
										}
										if (scheduledLines.getSCHEDLINE() > 0) {
											jnjDelSchModel.setScheduledLineNumber(String.valueOf(scheduledLines.getSCHEDLINE()));
										}
										if (null != scheduledLines.getCONFSTAT()) {
											jnjDelSchModel.setLineStatus(scheduledLines.getCONFSTAT());
										}
										
										if ( scheduledLines.getCONFIRQTY() > 0 && !Jnjgtb2boutboundserviceConstants.EMPTY_STRING.equals(String.valueOf(scheduledLines.getCONFIRQTY()))) {
											jnjDelSchModel.setQty(scheduledLines.getCONFIRQTY());
											quantity = quantity + scheduledLines.getCONFIRQTY();
										}
										
										if (null != scheduledLines.getREQDATE() && !exceptedDateFormatList.contains(scheduledLines.getREQDATE())) {
											jnjDelSchModel.setDeliveryDate(CommonUtil.parseXMLDateToDate(scheduledLines.getREQDATE()));
										}
										// if block loop
										if (null != scheduledLines.getMSDATE() && !exceptedDateFormatList.contains(scheduledLines.getMSDATE())) {
											jnjDelSchModel.setMaterialAvailabilityDate(CommonUtil.parseXMLDateToDate(scheduledLines.getMSDATE()));
										}
										
										// if block loop
										if (null != scheduledLines.getGIDATE() && !exceptedDateFormatList.contains(scheduledLines.getGIDATE())) {
											jnjDelSchModel.setShipDate(CommonUtil.parseXMLDateToDate(scheduledLines.getGIDATE()));
										}
										
										if (null != scheduledLines.getCONFSTAT()) {
											jnjDelSchModel.setLineStatus(scheduledLines.getCONFSTAT());
										}
										// if block loop
										jnjDelSchModelList.add(jnjDelSchModel);
									}
								}
								LOGGER.info(" orderLinesOutput jnjDelSchModelList size :"+jnjDelSchModelList.size());
								for (JnjDeliveryScheduleModel jnjDeliveryScheduleModel : jnjDelSchModelList) {
									LOGGER.info(" jnjDeliveryScheduleModel getLineNumber :"+jnjDeliveryScheduleModel.getLineNumber());
									LOGGER.info(" jnjDeliveryScheduleModel getScheduledLineNumber :"+jnjDeliveryScheduleModel.getScheduledLineNumber());
									LOGGER.info(" jnjDeliveryScheduleModel getMaterialAvailabilityDate :"+jnjDeliveryScheduleModel.getMaterialAvailabilityDate());
									LOGGER.info(" jnjDeliveryScheduleModel getQty :"+jnjDeliveryScheduleModel.getQty());
								}
								abstOrdEntryModel.setDeliverySchedules(jnjDelSchModelList);
//									abstOrdEntryModel.setQuantity(Long.valueOf(quantity));
							}
							// Changes for Price Override CR.
							if (StringUtils.isEmpty(abstOrdEntryModel.getPriceOverride())) {
								grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
							}
							jnjGTOrderService.populateMddOrderEntryStatus(abstOrdEntryModel);
						}
					}
				}
			}
				catch (final ModelNotFoundException exception) {
					LOGGER.error( Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
									+ "Model Not Found Exception Occurred for sales UOM or base UOM" + exception.getMessage(), exception);
				}
			}
		}
	}
		cartModel.setTotalPrice(Double.valueOf(totalNetValue));
		cartModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
		cartModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
		cartModel.setDeliveryCost(Double.valueOf(totalFreightFees - minimumOrderFee));
		cartModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
		cartModel.setSubtotal(Double.valueOf(grossPrice));
		cartModel.setTotalHsaPromotion(Double.valueOf(hsaPromotion));
		cartModel.setTotalOtherCharge(Double.valueOf(totalNetValue - grossPrice - minimumOrderFee - dropShipFee + hsaPromotion));
		cartModel.setTotalFees(Double.valueOf(minimumOrderFee + dropShipFee));
		cartModel.setSapValidated(Boolean.TRUE);
		cartModel.setTotalFreightFees(Double.valueOf(totalFreightFees));
		cartModel.setTotalGrossPrice(Double.valueOf(grossPrice));
		cartModel.setGlobalDiscountValues(createDiscountValues(Double.valueOf(totalDiscounts), currencyModel));//CP022 Changes
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}
 
 	protected List<DiscountValue> createDiscountValues(final Double discountValue, final CurrencyModel currencyModel) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final DiscountValue discount = new DiscountValue(Jnjgtb2boutboundserviceConstants.DISCOUNT_VALUE, 0.0D, false,
				discountValue.doubleValue(), (currencyModel == null) ? null : currencyModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<DiscountValue>();
		discountValues.add(discount);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return discountValues;
	}

 	@Override
	public JnjGTProposedOrderResponseData checkReplacemenItemForProduct(String productId, JnjGTSapWsData wsData) {
 		CatalogVersionModel catalogVersionModel = null;
		 JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = null;
		 catalogVersionModel = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID, Jnjb2bCoreConstants.ONLINE);
 		return mapReplacemenItemRequestResponse(productId, catalogVersionModel);
	}
 	
	/**
	 *  
	 * This method used to identify is first sap call or not if tru then it will process only proposed item list from the manual substitution to show in the popup 
	 * @throws IntegrationException
	 * @throws SystemException
	 * @throws BusinessException
	 */
 
	private JnjGTProposedOrderResponseData mapReplacemenItemRequestResponse(String productId, CatalogVersionModel catalogVersionModel ) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapReplacemenItemRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//HashMap<String, List <AbstractOrderEntryModel>> abstractOrderEntryModelMap = new HashMap<String, List <AbstractOrderEntryModel>>();
		final JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = new JnjGTProposedOrderResponseData();
		TestBackOrderItemFromGatewayOutput response = null;
		
		response = constructReplacementItemFromGatewayOutput(productId);
		//preparing the data to show in the popup through ProposedOutOrderItem
		List<JnjGTCommonFormIOData>	proposedOutOrderItemList = constructProposedOutOrderList(catalogVersionModel, response );
		java.util.Collections.sort(proposedOutOrderItemList,jnjGTHybrisLineItemComparator);
		jnjGTProposedOrderResponseData.setJnjGTCommonFormIODataList(proposedOutOrderItemList);
		LOGGER.info("proposedOutOrderItemList ************* size : "+proposedOutOrderItemList.size());
		return jnjGTProposedOrderResponseData;
	}
	
	/*
	 * This method using to construct the SAP response for the line items level and return the list of OrderLinesOutput
	 * This method using temp purpose without SAP call in GT. if region then SAP will response 
	 *  To identify the replacement item for the given product id
	 * 
	 */
	private TestBackOrderItemFromGatewayOutput constructReplacementItemFromGatewayOutput(String productId ){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "constructReplacementItemFromGatewayOutput()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		TestBackOrderItemFromGatewayOutput testBackOrderItemFromGatewayOutput = new TestBackOrderItemFromGatewayOutput();
		final List<OrderLinesOutput> orderLinesOutputList = new ArrayList<OrderLinesOutput>();
		Integer entryNo=null;
		String orderedItemCode=null;
			Double netValue = 0.0;
			OrderLinesOutput orderLinesOutput = new OrderLinesOutput();
			LOGGER.info("entryNo : "+ entryNo +" , orderedItemCode : "+ orderedItemCode);
			List<ManualSubLinesOutput> manualSubLinesOutputList = buildReplacementList(productId);
			orderLinesOutput.getManualSubLinesOutput().addAll(manualSubLinesOutputList);
			orderLinesOutputList.add(orderLinesOutput);
		testBackOrderItemFromGatewayOutput.getOrderLinesOutput().addAll(orderLinesOutputList);
		return testBackOrderItemFromGatewayOutput;
	}
	
	/*
	 *  This method is temp to construct manual substitution
	 * @see com.jnj.gt.outbound.mapper.JnjGTProposedOrderItemMapper#buildManualSubstitutionList(de.hybris.platform.core.model.order.CartModel, de.hybris.platform.catalog.model.CatalogVersionModel, java.util.HashMap)
	 */
		private List<ManualSubLinesOutput> buildReplacementList(String productId) {
			List<ManualSubLinesOutput> manualSubLinesOutputList = new ArrayList<ManualSubLinesOutput>(); 
			BufferedReader br = null;
			try {
				String sCurrentLine;
				String replaceItemCodeList = null;
			    String sapOrderedItemCode = null;
			    String replaceItemCode=null;
			   // String hyLineItemCode = null;
				final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.TEMP_READ_PROPOSED_FILE_PATH);
		        LOGGER.info("hotFolderBaseLocation : "+ hotFolderBaseLocation);
		        br = new BufferedReader(new FileReader(hotFolderBaseLocation));//file name with path
		        int idx =1;
		        while ((sCurrentLine = br.readLine()) != null) {
		        	LOGGER.info(" sCurrentLine : " +sCurrentLine);
		               String[] details = sCurrentLine.split("\\@");
		              // LOGGER.info(" details.length ~: " +details.length);
		               if(details.length >1){
	                  	//hyLineItemCode = details[0];
		                sapOrderedItemCode = details[0]; // original product code
	                  	replaceItemCodeList = details[1]; // getting proposed products list separated by comma
	                  	//AAOL-6377
	              		if(productId.contains(sapOrderedItemCode)){
	              			ManualSubLinesOutput manualSubLinesOutput = new ManualSubLinesOutput();
	              			manualSubLinesOutput.setITMNUMBER(idx); // seq line item
	              			manualSubLinesOutput.setMATERIAL(sapOrderedItemCode);
	              			StringTokenizer replaceItemCodes=new StringTokenizer(replaceItemCodeList, ",");//getting praposed items.
	              			List<String> replacementItems = new ArrayList<String>();
	              			while(replaceItemCodes.hasMoreTokens()){
	              				replaceItemCode=replaceItemCodes.nextToken();
	              				LOGGER.info("  sapOrderedItemCode : "+ sapOrderedItemCode + " , ProposedItemCd : "+replaceItemCode);
	              				replacementItems.add(replaceItemCode);								
			              		//manualSubLinesOutput.setTARGETQTY(cartEntryMap.get(orderedItemCode).get(0).getQuantity());//Integer.parseInt(details[2]));
	              			}
	                  			manualSubLinesOutput.setSUBMATNO(replacementItems); // proposed prod code list
			              		//manualSubLinesOutput.setProposedItemQty(Integer.parseInt(details[4]));
			              		manualSubLinesOutputList.add(manualSubLinesOutput);  // need to include if product is available in hybris catalog else no need to show int he popup
			              		idx=idx+1;
		                  }else{
		                  	LOGGER.info("There is no Replacemen productst items are available from SAP for this product");
		                  }
	              	}
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (br != null)br.close();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
		return manualSubLinesOutputList;
		}
		
		private void addPartialProduct(final String code, final long quantity) {
			try {
				final Map<ProductModel, Long> productQtyMap= new HashMap<>();
				final Date startTime = new Date();
				LOGGER.info("Start: Loading Product from Database>" + code);
				ProductModel product = jnJGTProductService.getProductByCodeOrEAN(code);
				LOGGER.info("End>>: Loading Product from Database");
				if (null != product) {
					 LOGGER.info("Start: Perform Business Validating " + code);
					final String validationError = validateProductCode(product);
					LOGGER.info("End>>: Perform Business Validating");

					if (null == validationError) {
						LOGGER.info("adding product to hashmap");
						productQtyMap.put(product, quantity);
						LOGGER.info("added product to hashmap");
						LOGGER.info("getting cart from sessioncart");
						final CartModel cartModel = getCartService().getSessionCart();
						LOGGER.info("got cart from sessioncart> " +cartModel.getOrderType().getCode());
						final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
						LOGGER.info("Start: Adding product to cart via jnjGTCartService");
						//final CommerceCartModification cartModification = jnjCartService.addToCart(cartModel, product, quantity, product.getUnit(), true);
						CommerceCartModification cartModification = jnjGTCartService.addToCartGT(cartModel,productQtyMap);
						LOGGER.info("End>>: Adding product to cart via cart service");
					}else{
						// Given product code is not valid
						LOGGER.error("Add to Cart - Product Code-" + code + " Found. But, product validation was failed. Error message: " + validationError);
						LOGGER.error("Will be logged into the database");
						
					}
				}else
				{
					LOGGER.info("product is null> " + code);
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		private String validateProductCode(final ProductModel productModel)
		{
			ProductModel productModelToValidate = productModel;
			final Date startTime = new Date();
			if (productModelToValidate != null)
			{
				// From session get the current site i.e. MDD/CONS
				final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

				// If site is MDD, variant needs to be searched upon GTIN and Base Material Number
				if (productModelToValidate instanceof JnjGTVariantProductModel)
				{
					productModelToValidate = ((JnjGTVariantProductModel) productModelToValidate).getBaseProduct();
				}
				final Boolean salesRep = sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS);

				// If product is not saleable return the corresponding error message
				final String status = jnJGTProductService.isProductSaleable((JnJProductModel) productModelToValidate, currentSite);
				if (null != status)
				{
					return status;
				}
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("----->JnjGTCartFacadeImpl - validateProduct(" + productModelToValidate.getCode()
						+ ") Time taken for Validating ProductModel: " + (new Date().getTime() - startTime.getTime()));
			}
			return null;
		}
}
