/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.order.util;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjGTSplitOrderInfo;
import com.jnj.core.dto.SplitOrderData;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.impl.DefaultMessageFacade;


/**
 * The JnjOrderUtil class is the utility which contains all the methods related to Orders.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */


public class JnjOrderUtil
{
	private static final Logger LOGGER = Logger.getLogger(JnjOrderUtil.class);
	private static final String CHECKOUT_TRUE_FLAG = "X";
	
	@Autowired
	protected DefaultMessageFacade messageFacade;
	
	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	/**
	 * The getSalesOrgFromAbstOrdEntModel method is used to form the map which contains the key as combination of Sales
	 * Org and Special Product Type and value as the list of Abstract Order Entry Model.
	 * 
	 * @param abstOrderModel
	 * @return salesOrgAndSplProdTypeMap
	 */
	public static Map<SplitOrderData, List<AbstractOrderEntryModel>> getSalesOrgFromAbstOrdEntModel(
			final AbstractOrderModel abstOrderModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgFromAbstOrdEntModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Map<SplitOrderData, List<AbstractOrderEntryModel>> salesOrgAndSplProdTypeMap = new HashMap<SplitOrderData, List<AbstractOrderEntryModel>>();

		if (null != abstOrderModel && null != abstOrderModel.getEntries() && !abstOrderModel.getEntries().isEmpty())
		{
			for (final AbstractOrderEntryModel abstOrderEntModel : abstOrderModel.getEntries())
			{
				 String salesOrg = abstOrderEntModel.getSalesOrg() == null ? StringUtils.EMPTY : abstOrderEntModel.getSalesOrg();
				LOGGER.debug("salesOrg=" + salesOrg);
				 String sapOrderType = abstOrderEntModel.getSapOrderType();
				LOGGER.debug("sapOrderType=" + sapOrderType);

				//workaround remove this
				if (sapOrderType == null || sapOrderType.trim().equals("")){
					abstOrderEntModel.setSapOrderType("ZOR");
					sapOrderType="ZOR";
				}
				
				if (salesOrg == null || salesOrg.trim().equals("")){
					abstOrderEntModel.setSalesOrg("CA02");
					salesOrg="CA02";
				}				
				
				if (StringUtils.isNotEmpty(sapOrderType))
				{
					final SplitOrderData splitOrderData = new SplitOrderData();
					splitOrderData.setSalesOrg(salesOrg);
					splitOrderData.setOrderType(sapOrderType);
					// if the salesOrgAndSplProdTypeMap map contains the key then just add the AbstractOrderEntryModel object in the list of 
					// AbstractOrderEntryModel Object.
					if (salesOrgAndSplProdTypeMap.containsKey(splitOrderData))
					{
						salesOrgAndSplProdTypeMap.get(splitOrderData).add(abstOrderEntModel);
					}
					// else create a new list of AbstractOrderEntryModel object and add the current AbstractOrderEntryModel object to that list and put that
					// to the salesOrgAndSplProdTypeMap map object.
					else
					{
						final List<AbstractOrderEntryModel> abstOrdEntryModelList = new ArrayList<AbstractOrderEntryModel>();
						abstOrdEntryModelList.add(abstOrderEntModel);
						salesOrgAndSplProdTypeMap.put(splitOrderData, abstOrdEntryModelList);
					}
				}
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgFromAbstOrdEntModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return salesOrgAndSplProdTypeMap;
	}
	
	/**
	 * This method is used to create a map of Split Order.
	 * @param cartEntriesMap
	 * @param dropShipmentDetails
	 * @param customerCountry
	 * @return
	 */
	public Map<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> getSplitOrderMap(
			Map<String, AbstractOrderEntryModel> cartEntriesMap, List<JnjDropShipmentDetailsModel> dropShipmentDetails,String customerCountry) {
		
		final Map<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>>();
		
		for (final JnjDropShipmentDetailsModel tableentry : dropShipmentDetails)
		{
			final JnjGTSplitOrderInfo splitOrderInfo = new JnjGTSplitOrderInfo();
			
			splitOrderInfo.setDocorderType(tableentry.getDocumentType());
			splitOrderInfo.setSalesOrganizantion(tableentry.getSalesOrganization());
			splitOrderInfo.setShipTo(tableentry.getShipTo());
			splitOrderInfo.setSoldTo(tableentry.getSoldTo());
			if (tableentry.getDestinationCountry() != null
					&& tableentry.getDestinationCountry().equalsIgnoreCase(customerCountry))
			{
				splitOrderInfo.setForbiddenFlag(CHECKOUT_TRUE_FLAG);
			}
			else
			{
				splitOrderInfo.setForbiddenFlag(StringUtils.EMPTY);
			}
			
			final AbstractOrderEntryModel abstractOrderEntryModel = cartEntriesMap.get(tableentry.getMaterialId());

			// Setting OrderType and SalesOrg for SAP XML request
			abstractOrderEntryModel.setSapOrderType(tableentry.getDocumentType());
			abstractOrderEntryModel.setSalesOrg(tableentry.getSalesOrganization());

			if (splitOrderMap.containsKey(splitOrderInfo))
			{
				if(!splitOrderMap.get(splitOrderInfo).contains(abstractOrderEntryModel))
				{
					splitOrderMap.get(splitOrderInfo).add(abstractOrderEntryModel);
				}
			}
			else
			{
				final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
				list.add(abstractOrderEntryModel);
				splitOrderMap.put(splitOrderInfo, list);
			}
			
		}
		
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSplitOrderMap()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return splitOrderMap;
	}
	
	/**
	 * This method validates the availability of product in the dropshipment table.
	 * @param splitOrderMap
	 * @param cartModel
	 * @return
	 */
	public Map<String, String> validateSplitOrderMap(
			Map<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap, CartModel cartModel) {

		String lineMessage = null;

		try
		{
			lineMessage = messageFacade.getMessageTextForCode(Jnjb2bCoreConstants.Dropshipment.NOT_FOUND_ERROR);
		}
		catch (final Exception e)
		{
			lineMessage = "Order Type not defined for this material. Please contact support.";
		}

		final Map<String, String> codesNotFound = new HashMap<String, String>();

		for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
		{

			boolean found = false;
			if(splitOrderMap!=null)
			{
				for (final Entry<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> mapEntry : splitOrderMap.entrySet())
				{
					
					if (!found)
					{
						for (final AbstractOrderEntryModel mapProduct : mapEntry.getValue())
						{
							
							if (mapProduct.getProduct().getCode().equalsIgnoreCase(cartEntry.getProduct().getCode()))
							{
								found = true;
							}
						}
					}
				}
			}
			if (!found)
			{
				codesNotFound.put(cartEntry.getProduct().getCode(), lineMessage);
			}
		}
		return codesNotFound;
	}
	
	
	/**
	 * This method is used to determine if the region wants Order Split or not.
	 * @return
	 */
	public boolean isOrderSplit(){

		String noSplit = Config.getParameter(Jnjb2bCoreConstants.Dropshipment.NO_SPLIT_LIST);
		final String noSplitList[] = noSplit.split(",");
		final String loggedInSite = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
		LOGGER.info("JnjOrderUtil class isOrderSplit method loggedInSite value is : "+loggedInSite);
		boolean split = true;
		for(String noSplitCountry : noSplitList)
		{
			LOGGER.info("JnjOrderUtil class isOrderSplit method noSplitCountry value is : "+noSplitCountry);
			if(noSplitCountry.equals(loggedInSite)){
				split=false;
			}
		}
		LOGGER.info("JnjOrderUtil class isOrderSplit method split value is : "+split);
		return split;
	}
	
	/**
	 * This method is used to verify if force new entry in cart is enabled.
	 * @return
	 */
	public boolean isForceNewEntryEnabled(){
		String forceNewEntry = Config.getParameter(Jnjb2bCoreConstants.FORCE_NEW_ENTRY);
		return Boolean.valueOf(forceNewEntry);
	}
	
	/**
	 * This method is used to verify if region has enabled zero pricing check
	 * @return
	 */
	public boolean checkZeroPricing(){
		String isZeroPriceCheck = Config.getParameter(Jnjb2bCoreConstants.CHECK_ZERO_PRICING);
		return Boolean.valueOf(isZeroPriceCheck);
	}
}
