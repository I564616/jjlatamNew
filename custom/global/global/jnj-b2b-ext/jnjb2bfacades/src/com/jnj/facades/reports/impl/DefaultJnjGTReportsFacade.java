/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013 
 * All rights reserved.
 */
package com.jnj.facades.reports.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import com.jnj.facades.data.JnjGTInvoiceClearingReportResponseData;

import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants.Reports;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.reports.JnjGTReportsService;
import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.facades.converters.populator.address.JnjGTAddressPopulator;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.facades.data.JnjGTCutReportOrderData;
import com.jnj.facades.data.JnjGTCutReportOrderEntryData;
import com.jnj.facades.data.JnjGTFinancePurchaseOrderReportResponseData;
import com.jnj.facades.data.JnjGTFinancialAccountAgingReportData;
import com.jnj.facades.data.JnjGTFinancialBalanceSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialCreditSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialPaymentSummaryReportData;
import com.jnj.facades.data.JnjGTMultiPurchaseOrderReportResponseData;
import com.jnj.facades.data.JnjGTOADeliveryListReportResponseData;
import com.jnj.facades.data.JnjGTSinglePurchaseAnalysisEntriesData;
import com.jnj.facades.data.JnjGTSinglePurchaseOrderReportResponseData;
import com.jnj.facades.data.JnjGTSalesReportResponseOrderEntryData;
import com.jnj.facades.data.JnjGTSalesReportResponseData;
import com.jnj.facades.reports.JnjGTReportsFacade;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTOrderChannelModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTShippingDetailsModel;
import com.jnj.core.model.JnjGTShippingLineDetailsModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.gt.outbound.mapper.JnjGTAccountAgingReportMapper;
import com.jnj.gt.outbound.mapper.JnjGTBalanceSummaryReportMapper;
import com.jnj.gt.outbound.mapper.JnjGTConsInventoryReportMapper;
import com.jnj.gt.outbound.mapper.JnjGTConsignmentStockMapper;
import com.jnj.gt.outbound.mapper.JnjGTCreditSummaryReportMapper;
import com.jnj.gt.outbound.mapper.JnjGTInventoryReportMapper;
import com.jnj.gt.outbound.mapper.JnjGTPaymentSummaryReportMapper;
import com.jnj.gt.outbound.mapper.JnjGTInventoryReportMapper;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.territory.JnjGTTerritoryService;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
import com.jnj.gt.outbound.mapper.JnjGTInvoiceClearingReportMapper;

import org.apache.commons.lang3.StringUtils;

import com.jnj.facades.data.JnjGTSalesReportResponseOrderEntryData;

/**
 * This class represents the facade layer for the reports flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTReportsFacade implements JnjGTReportsFacade
{
	
	protected static final String REASON_OF_REJECTION_ID_PREFIX = "order.entry.reasonOfRejection.";

	/**
	 *
	 */
	protected static final String CANCELLED = "CANCELLED";

	/**
	 *
	 */
	protected static final String BACKORDERED = "BACKORDERED";

	protected static final String SEARCH_TOTAL = "searchTotal";
	

	protected static final Logger LOG = Logger.getLogger(DefaultJnjGTReportsFacade.class);

	protected static final String DATE_FORMAT_FOR_SORT = "dd/MM/yyyy";
	protected static final String INVALID_PRODUCT = "invalidProduct";
	protected static final String ELECTRONIC = "ELECTRONIC";
	protected static final String WEEK = "Week";
	protected static final String DATE_FORMAT = "dd/MM/yyyy";

	protected static final String REPORT_TYPE_URL= ".reportType.url";
	protected static final String CATEGORY_URL= "category.url.";
	/** This is the reports service **/
	protected JnjGTReportsService jnjGTReportsService;
	/** The project properties service. */
	@Autowired
	protected JnjConfigService jnjConfigService;
	/** The message service. */
	@Autowired
	protected MessageService messageService;

	/** I18NService to retrieve the current locale. */
	@Autowired
	protected I18NService i18nService;
	
	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;

	@Autowired
	protected JnjGTOrderService jnjGTOrderService;

	@Autowired
	protected JnjGTConsignmentStockMapper jnjGTConsignmentStockMapper;
	
	@Autowired
	protected JnjGTInventoryReportMapper jnjGTInventoryMapper;
	
	@Autowired
	protected JnjGTTerritoryService jnjGtTerritoryService;

	@Autowired
	protected CategoryService categoryService;

	@Autowired
	protected CatalogVersionService catalogVersionService;

	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;

	@Autowired
	protected PriceDataFactory priceDataFactory;

	@Autowired
	protected JnjGTAddressPopulator jnjGTAddressPopulator;

	@Autowired
	SessionService sessionService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	@Autowired
	protected JnjGTConsInventoryReportMapper jnjGTConsInventoryReportMapper;
	
	@Autowired
	protected JnjGTAccountAgingReportMapper jnjGTAccountAgingReportMapper;
	
	@Autowired
	protected JnjGTBalanceSummaryReportMapper jnjGTBalanceSummaryReportMapper;
	
	@Autowired
	protected JnjGTPaymentSummaryReportMapper jnjGTPaymentSummaryReportMapper;
	
	@Autowired
	protected JnjGTCreditSummaryReportMapper jnjGTCreditSummaryReportMapper;
	
	//AAOL-2426 changes
	@Autowired
	protected JnjGTInvoiceClearingReportMapper jnjGTInvoiceClearingReportMapper;
	//AAOL-2426 changes

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;
	
	public EnumerationService getEnumerationService() {
		return enumerationService;
	}
	
	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	public JnjGTOrderService getJnjGTOrderService() {
		return jnjGTOrderService;
	}

	public JnjGTConsignmentStockMapper getJnjGTConsignmentStockMapper() {
		return jnjGTConsignmentStockMapper;
	}

	public JnjGTTerritoryService getJnjGtTerritoryService() {
		return jnjGtTerritoryService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public JnJGTProductService getJnJGTProductService() {
		return jnJGTProductService;
	}

	public PriceDataFactory getPriceDataFactory() {
		return priceDataFactory;
	}

	public JnjGTAddressPopulator getJnjGTAddressPopulator() {
		return jnjGTAddressPopulator;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * This method fetches back-order report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTBackorderReportResponseData>
	 */
	@Override
	public List<JnjGTBackorderReportResponseData> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchBackOrderReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		
		/** Calling service layer to fetch back-order report data based on search criteria **/
		if (null != jnjGTPageableData.getSearchBy() && jnjGTPageableData.getSearchBy()!="")
			{
				convertBackorderProductCodeToPK(jnjGTPageableData, false);
			}
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch backorder report ");
		final List<OrderEntryModel> backorderLines = getJnjGTReportsService().fetchBackOrderReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "backorder lines obtained :: " + backorderLines);

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return populateBackorderReportResponseData(backorderLines);
		
	}

	/**
	 * This method is used to fetch the product PK for the product code entered in jnjGTPageableData's search by field
	 * 
	 * @param jnjGTPageableData
	 * @return product Name + GTIN
	 */
	@Override
	public String convertProductCodeToPK(final JnjGTPageableData jnjGTPageableData, final boolean isReturnProductInfo)
	{
		final String METHOD_NAME = "fetchSinglePurchaseAnalysisReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/**
		 * Calling the service layer to return product info and converting the product code to the appropriate primary key
		 **/
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getJnjGTReportsService().convertProductCodeToPK(jnjGTPageableData, isReturnProductInfo);
	}
	/*
	rajesh*/
	public String convertBackorderProductCodeToPK(final JnjGTPageableData jnjGTPageableData, final boolean isReturnProductInfo)
	{
		final String METHOD_NAME = "fetchBackOrderReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/**
		 * Calling the service layer to return product info and converting the product code to the appropriate primary key
		 **/
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getJnjGTReportsService().convertBackorderProductCodeToPK(jnjGTPageableData,isReturnProductInfo);
	}

	/**
	 * This method fetches financial Analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>
	 */
	/*AAOL #2419*/
	@Override
	public TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData> fetchFinancialAnalysisReport(
			final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchFinancialAnalysisReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Converting the product code to the product PK by querying the database **/
		TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData> financialAnalysisResponse = new TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData>();
			/*Calling DAO layer to fetch Financial Analysis report data based on search criteria*/
			LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports DAO to fetch Single Purchase Analysis report ");
			final List<JnjGTInvoiceModel> financialAnalysisLines = getJnjGTReportsService().fetchFinancialAnalysisReport(
					jnjGTPageableData);
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Financial Analysis lines obtained :: "
					+ financialAnalysisLines);
			
			if(null != financialAnalysisLines){
				 /*Populating the response */
				financialAnalysisResponse = populateFinancialAnalysisResponseData(financialAnalysisLines, jnjGTPageableData);
			}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		
		return financialAnalysisResponse;
	}
	
	
	/**
	 * This method populates the response data list for the financial analysis reports
	 * 
	 * @param singlePurchaseAnalysisLines
	 * @return TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>
	 */
	/*AAOL #2419*/
	protected TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData> populateFinancialAnalysisResponseData(
			final List<JnjGTInvoiceModel> financialAnalysisLines, final JnjGTPageableData jnjGTPageableData)	{
		
		final String METHOD_NAME = "populateFinancialAnalysisResponseData()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData> responseMapfinancial = new TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData>();
			String uniqueKey = null;
			/** Iterating over the Invoice entry lines **/
			for (final JnjGTInvoiceModel jnjGTInvoiceModel  : financialAnalysisLines) {
				final JnjGTFinancePurchaseOrderReportResponseData responseLine = new JnjGTFinancePurchaseOrderReportResponseData();
				final String accountId = jnjGTInvoiceModel.getOrder().getUnit().getUid();;
				
					Collection<JnjGTInvoiceEntryModel> invoiceenteries = jnjGTInvoiceModel.getEntries();
					if(!invoiceenteries.isEmpty()) {
						for (final JnjGTInvoiceEntryModel jnjGTInvoiceModelEntry  : invoiceenteries) {
							JnjOrderTypesEnum jnjOrderTypesEnum = jnjGTInvoiceModel.getOrder().getOrderType();
							responseLine.setOrderNumber(jnjGTInvoiceModel.getOrder().getCode());
							if (null != jnjOrderTypesEnum) {
								String orderType = enumerationService.getEnumerationName(JnjOrderTypesEnum.valueOf(jnjOrderTypesEnum.getCode()));
								responseLine.setOrderType(orderType);
							}
							responseLine.setInvoiceNumber(jnjGTInvoiceModel.getInvoiceNum());
							responseLine.setLineItem(jnjGTInvoiceModelEntry.getLineNum());
							responseLine.setCustomerPONumber(jnjGTInvoiceModel.getOrder().getPurchaseOrderNumber());
							responseLine.setProductCode(jnjGTInvoiceModelEntry.getProduct().getName());
							responseLine.setProductDescription(jnjGTInvoiceModelEntry.getProduct().getDescription());
							responseLine.setInvoiceQty(jnjGTInvoiceModelEntry.getQty());
							responseLine.setBillingDate(jnjGTInvoiceModel.getBillingType());
							responseLine.setSalesDocNo(jnjGTInvoiceModel.getOrder().getOrderNumber());
							responseLine.setReimbursementCode("");
							responseLine.setReceiptNumber(jnjGTInvoiceModel.getReceiptNumber());
							responseLine.setShiptoAccount(jnjGTInvoiceModel.getOrder().getShipToAccount());
							responseLine.setShipToName(jnjGTInvoiceModel.getOrder().getShipToAccount());
							responseLine.setEndUserAccount(jnjGTInvoiceModel.getOrder().getEndUser());
							responseLine.setEndUserName(jnjGTInvoiceModel.getOrder().getEndUser());
							responseLine.setSoldToName(jnjGTInvoiceModel.getSoldToAccNum().getName());
							responseLine.setSoldToAccount(jnjGTInvoiceModel.getSoldToAccNum().getId());
							responseLine.setShiptoAccount(jnjGTInvoiceModel.getOrder().getShipToAccount());
							responseLine.setShipToName(jnjGTInvoiceModel.getOrder().getShipToAccount());
							responseLine.setEndUserName(jnjGTInvoiceModel.getOrder().getEndUser());
							responseLine.setEndUserAccount(jnjGTInvoiceModel.getOrder().getEndUser());
							responseLine.setPaidAmount(jnjGTInvoiceModel.getPaidAmount());
							responseLine.setOpenAmount(jnjGTInvoiceModel.getOpenAmount());
							responseLine.setTotalPrice(jnjGTInvoiceModel.getInvoiceTotalAmount());
							responseLine.setFranchiseDescription("");
							Collection<AbstractOrderEntryModel> orderModelEnteries = jnjGTInvoiceModel.getOrder().getEntries();
							if(!orderModelEnteries.isEmpty()) {
								for(AbstractOrderEntryModel abstractOrderEntryModel : orderModelEnteries){
									responseLine.setStatus(abstractOrderEntryModel.getStatus());
									//responseLine.setTotalPrice(abstractOrderEntryModel.getTotalPrice());
									responseLine.setUom(abstractOrderEntryModel.getUnit().getName());
									responseLine.setCurrency(abstractOrderEntryModel.getOrder().getCurrency().getIsocode());
									responseLine.setUnitPrice(abstractOrderEntryModel.getBasePrice().toString());
								}
							}
							uniqueKey = accountId + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + jnjGTInvoiceModel.getCreationtime() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + jnjGTInvoiceModel.getInvoiceNum();
							
							/** Adding the line to the map against key "accountId_periodStartDate_invoicenumber" **/
							responseMapfinancial.put(uniqueKey, responseLine);
						}
					}
			}
		//}
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Going to populate the financial analysis response map ... ");
		/*Iterating over the order entry lines*/
		
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"response map for financial analysis report is ready with number of entries :: " + responseMapfinancial.size());

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return responseMapfinancial;
	}
	
	/**
	 * This method fetches InvoicePastDue based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return TreeMap<String, fetchInvoicePastDueReport>
	 */
	/*@Override
	public List<JnjGTInvoicePastDueReportResponseData> fetchInvoicePastDueReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchInvoicePastDueReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		
		//** Calling service layer to fetch back-order report data based on search criteria **//*
		if (null != jnjGTPageableData.getSearchBy() && jnjGTPageableData.getSearchBy()!="")
			{
				convertBackorderProductCodeToPK(jnjGTPageableData, false);
			}
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch backorder report ");
		final List<JnjGTInvoicePastDueReportResponseData> invoicepastdueLines = getJnjGTReportsService().fetchInvoicePastDueReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "backorder lines obtained :: " + invoicepastdueLines);

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return invoicepastdueLines;
		
	}*/
	
	/**
	 * This method fetches Single Purchase Analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>
	 */
	@Override
	public List<JnjGTInvoicePastDueReportResponseData> fetchInvoicePastDueReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchInvoicePastDueReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch backorder report ");
		final List<JnjGTInvoiceModel> invoicepastdueLines = getJnjGTReportsService().fetchInvoicePastDueReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "backorder lines obtained :: " + invoicepastdueLines);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return populateInvoiceDueReportResponseData(invoicepastdueLines);
	}
	
	protected List<JnjGTInvoicePastDueReportResponseData> populateInvoiceDueReportResponseData(final List<JnjGTInvoiceModel> invoicepastdueLines) {
		final String METHOD_NAME = "populateInvoiceDueReportResponseData()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME,
				METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final SimpleDateFormat simpleDateFormatForDisplay = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final TreeMap<String, List<JnjGTInvoicePastDueReportResponseData>> responseMap = new TreeMap<String, List<JnjGTInvoicePastDueReportResponseData>>();
		List<JnjGTInvoicePastDueReportResponseData> responseList = new ArrayList<JnjGTInvoicePastDueReportResponseData>();
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Going to populate the sales Report response map ... ");
		/** Iterating over the order entry lines **/
		JnjGTInvoicePastDueReportResponseData responseLine = null;
		if (null != invoicepastdueLines && CollectionUtils.isNotEmpty(invoicepastdueLines)) {
			for (final JnjGTInvoiceModel line : invoicepastdueLines) {
				responseLine = new JnjGTInvoicePastDueReportResponseData();
				responseLine.setInvoiceNum(line.getInvoiceNum());
				responseLine.setBillingDate((null !=line.getBillingDate())?simpleDateFormat.format(line.getBillingDate()):"");
				responseLine.setSoldToAccNum(line.getSoldToAccNum().getUid());
				responseLine.setSoldToAccName(line.getSoldToAccNum().getName());
				responseLine.setReceiptNumber(line.getReceiptNumber());
				responseLine.setStatus(line.getStatus());
				responseLine.setInvoiceDueDate(simpleDateFormat.format(line.getInvoiceDueDate()));
				responseLine.setInvoiceTotalAmount(line.getInvoiceTotalAmount());
				responseLine.setOpenAmount(line.getOpenAmount());
				responseLine.setCurrency(line.getCurrency().getIsocode());
				responseLine.setOrderNum(line.getOrder().getOrderNumber());
				responseLine.setCustomerPoNum(line.getOrder().getPurchaseOrderNumber());
				
				/*Collection<AbstractOrderEntryModel> orderModelEnteries = line.getOrder().getEntries();
				if(!orderModelEnteries.isEmpty()) {
					for(AbstractOrderEntryModel abstractOrderEntryModel : orderModelEnteries){
						responseLine.setStatus(abstractOrderEntryModel.getStatus());
					}
				}*/
				responseList.add(responseLine);
			}
		}
		responseMap.put("responseData", responseList);
		logDebugMessage(
				Jnjb2bCoreConstants.Logging.REPORTS_NAME,
				METHOD_NAME,
				"response map for invoice past due analysis report is ready with number of entries :: "
						+ responseMap.size());

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME,
				METHOD_NAME, Logging.END_OF_METHOD);
		return responseList;
	}
	
	
	//AAOL-2426 changes
	
	@Override
	public List<JnjGTInvoiceClearingReportResponseData> fetchInvoiceClearingReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchInvoiceClearingReport()";
		 List<JnjGTInvoiceClearingReportResponseData> invoicepastdueLines = null;
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch backorder report ");
		try
		{
		 invoicepastdueLines = jnjGTInvoiceClearingReportMapper.mapInvoiceClearingReportRequestResponse(jnjGTPageableData);
		}
		catch (SystemException e) {
		
			LOG.error(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ " There are some problem on fetching data");
		} catch (IntegrationException e) {
			
			LOG.error(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ " There are some problem on fetching data");
		}
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "backorder lines obtained :: " + invoicepastdueLines);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return invoicepastdueLines;
	}
	

	/**
	 * This method fetches Single Purchase Analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>
	 */
	@Override
	public TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData> fetchSinglePurchaseAnalysisReport(
			final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchSinglePurchaseAnalysisReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Converting the product code to the product PK by querying the database **/
		convertProductCodeToPK(jnjGTPageableData, false);
		TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData> purchaseAnalysisResponse = new TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>();
		if (null != jnjGTPageableData.getSearchBy())
		{
			/** Calling DAO layer to fetch Single Purchase Analysis report data based on search criteria **/
			LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports DAO to fetch Single Purchase Analysis report ");
			final List<OrderEntryModel> singlePurchaseAnalysisLines = getJnjGTReportsService().fetchSinglePurchaseAnalysisReport(
					jnjGTPageableData);
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Single Purchase Analysis lines obtained :: "
					+ singlePurchaseAnalysisLines);
			/** Populating the response **/
			purchaseAnalysisResponse = populateSinglePurchaseAnalysisResponseData(singlePurchaseAnalysisLines, jnjGTPageableData);
		}
		else
		{
			/** Setting an invalid product key **/
			purchaseAnalysisResponse.put(INVALID_PRODUCT, null);
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Product code entered is invalid");
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return purchaseAnalysisResponse;
	}
	
	
	/**
	 * AAOL-2410: This method fetches Single Purchase Analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>
	 */
	@Override
	public TreeMap<String, List<JnjGTSalesReportResponseData>> fetchSalesReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchSinglePurchaseAnalysisReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Converting the product code to the product PK by querying the database **/
		//convertProductCodeToPK(jnjGTPageableData, false);
		TreeMap<String, List<JnjGTSalesReportResponseData>> salesReportResponse = new TreeMap<String, List<JnjGTSalesReportResponseData>>();
		if (null != jnjGTPageableData)
		{
			/** Calling DAO layer to fetch Single Purchase Analysis report data based on search criteria **/
			LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports DAO to fetch Single Purchase Analysis report ");
			final List<OrderModel> salesReportLines = getJnjGTReportsService().fetchSalesReport(jnjGTPageableData);
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Single Purchase Analysis lines obtained :: "
					+ salesReportLines);
			/** Populating the response **/
			salesReportResponse = populateSalesReportResponseData(salesReportLines, jnjGTPageableData);
		}
		else
		{
			/** Setting an invalid product key **/
			salesReportResponse.put(INVALID_PRODUCT, null);
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Product code entered is invalid");
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return salesReportResponse;
	}
	
	
	
	/**
	 * AAOL-2410: This method populates the response data list for the back-order reports
	 * 
	 * @param SalesReportAnalysisLines
	 * @return TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>
	 */
	protected TreeMap<String, List<JnjGTSalesReportResponseData>> populateSalesReportResponseData(final List<OrderModel> salesReportLines, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "populateSalesReportResponseData()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final SimpleDateFormat simpleDateFormatForDisplay = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final TreeMap<String, List<JnjGTSalesReportResponseData>> responseMap = new TreeMap<String, List<JnjGTSalesReportResponseData>>();
		List<JnjGTSalesReportResponseData> responseList = new ArrayList<JnjGTSalesReportResponseData>();
		List<JnjGTSalesReportResponseOrderEntryData> orderEntryList = new ArrayList<JnjGTSalesReportResponseOrderEntryData>();
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Going to populate the sales Report response map ... ");
		/** Iterating over the order entry lines **/
		 JnjGTSalesReportResponseData  responseLine = null;
		 JnjGTSalesReportResponseOrderEntryData orderEntry = null;
		if (null != salesReportLines && CollectionUtils.isNotEmpty(salesReportLines)) {

			for (final OrderModel line : salesReportLines) {
				List<String> invoiceQuantitiesList = new ArrayList<String>();
				responseLine = new JnjGTSalesReportResponseData();
				//responseLine.setOrderType(line.getOrderType().getCode());
				responseLine.setOrderType(enumerationService.getEnumerationName(JnjOrderTypesEnum.valueOf(line.getOrderType().getCode())));
				responseLine.setSalesDocNo(line.getCode());
				
				if (line.getDate() != null) {
					responseLine.setOrderDate(simpleDateFormat.format(line.getPoDate()));
				}
				responseLine.setStatus(line.getStatus().getCode());
				
				responseLine.setCustomerPONo(line.getPurchaseOrderNumber());
				responseLine.setCreditBlock(line.getCreditStatus());
				responseLine.setDeliveryBlock(line.getDeliveyBlock());
				responseLine.setFranchiseDescription("");
				responseLine.setBackOrderQty("");
				responseLine.setBillingQty("");
				responseLine.setReimbursementCode("");
				Set<JnjGTShippingDetailsModel> shippingDetails = line.getShippingDetails();
				for(JnjGTShippingDetailsModel shippingInfo:shippingDetails){
					responseLine.setDeliveryNumber(shippingInfo.getDeliveryNum());
					responseLine.setEstDeliveryDate(simpleDateFormat.format(shippingInfo.getActualDeliveryDate()));
					
					for(JnjGTShippingLineDetailsModel shippingLineDetails:shippingInfo.getShippingLineDetails()){
						responseLine.setDeliveryQty(shippingLineDetails.getDeliveryQty());
					}
				}
				
				Collection<JnjGTInvoiceModel> invoices = line.getInvoices();
				for(JnjGTInvoiceModel invoice:invoices){
					responseLine.setSoldToAccount(invoice.getSoldToAccNum().getId());
					responseLine.setSoldToName(invoice.getSoldToAccNum().getName());
					responseLine.setInvoiceNumber(invoice.getInvoiceNum());
					for(JnjGTInvoiceEntryModel invoiceEntries:invoice.getEntries()){
						invoiceQuantitiesList.add(invoiceEntries.getQty().toString());
					}
				}
				responseLine.setInvoiceQuantity(invoiceQuantitiesList);
				responseLine.setTotalPrice("");
				responseLine.setCurr(line.getCurrency().getName());
				responseLine.setDeliveryBlock(line.getDeliveyBlock());
				responseLine.setBillingBlock("");
				responseLine.setShipToName(line.getShipToAccount());
				responseLine.setShiptoAccount(line.getShipToAccount());
				responseLine.setEndUserAccount(line.getEndUser());
				responseLine.setEndUserName(line.getEndUser());
				responseLine.setStockLocationAccount("");
				responseLine.setStockLocationName("");
				if (line.getEntries() != null) {
					
					for (AbstractOrderEntryModel data : line.getEntries()) {
						orderEntry = new JnjGTSalesReportResponseOrderEntryData();
						orderEntry.setProductCode(data.getProduct().getCode());
						orderEntry.setDescription(data.getProduct().getDescription());
						orderEntry.setLineItem(data.getCustLineNumber());
						orderEntry.setQuantity(data.getQuantity());
						orderEntry.setUOM(data.getUnit().getCode().toString());
						orderEntry.setUnitPrice(data.getBasePrice().toString());
						orderEntryList.add(orderEntry);
					}
					
					responseLine.setOrderEntryList(orderEntryList);
				}
				responseList.add(responseLine);
			}

		}
		
		
		responseMap.put("responseData", responseList);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"response map for single purchase analysis report is ready with number of entries :: " + responseMap.size());

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		
	
		return responseMap;
	}	
	
	
	
	
	

	/**
	 * This method populates the entries for the specified line in the purchase analysis report
	 * 
	 * @param orderEntries
	 * @return List<JnjGTSinglePurchaseAnalysisEntriesData>
	 */
	protected List<JnjGTSinglePurchaseAnalysisEntriesData> populateSinglePurchaseAnalysisEntriesData(
			final List<OrderEntryModel> orderEntries)
	{
		final String METHOD_NAME = "populateSinglePurchaseAnalysisEntriesData()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		List<JnjGTSinglePurchaseAnalysisEntriesData> entriesData = null;

		/** Checking if the entries are not null or empty **/
		if (null != orderEntries && CollectionUtils.isNotEmpty(orderEntries))
		{
			entriesData = new ArrayList<JnjGTSinglePurchaseAnalysisEntriesData>();
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"Going to create Single Purchase Analysis entries...");
			/** Iterating over the order entries **/
			for (final OrderEntryModel entry : orderEntries)
			{
				Double totalPrice = entry.getTotalPrice();
				final JnjOrderTypesEnum orderType = entry.getOrder().getOrderType();
				//•	For non ZRE orders exclude the scheduled line status of RJ, UC, CN, CB. Essentially, exclude the cancelled and backordered lines from the total
				if ((StringUtils.equalsIgnoreCase(BACKORDERED, entry.getStatus()) || StringUtils.equalsIgnoreCase(CANCELLED,
						entry.getStatus()))
						&& !orderType.equals(JnjOrderTypesEnum.ZRE))
				{
					continue;//we skip this line item as this is not required to be displayed.
				}
				boolean isDataModified = false;
				long nonZREQty = 0L;// For Non ZRE quantity calculations, this quantity will be deducted from the original order line
				final Double basePrice = entry.getBasePrice();// For Non ZRE price calculations, this value will be deducted from the original order line
				for (final JnjDeliveryScheduleModel scheduleLine : entry.getDeliverySchedules())
				{
					if (orderType.equals(JnjOrderTypesEnum.ZRE))
					{
						//•	For ZRE orders, only use the confirmed lines, CQ.
						//•	If the order type is ZRE, subtract the value from the total.
						if (StringUtils.equals(scheduleLine.getLineStatus(), "CQ"))
						{
							// Change the value of the price to a negative one in order to subtract the value from the total.
							totalPrice = Double.valueOf(totalPrice.doubleValue() * -1);
							entry.setTotalPrice(totalPrice);
							final long qty = entry.getQuantity().longValue() * -1;
							entry.setQuantity(Long.valueOf(qty));
							break;
						}
					}
					else
					{
						//•	For non ZRE orders exclude the scheduled line status of RJ, UC, CN, CB. Essentially, exclude the cancelled and backordered lines from the total

						if (Reports.NON_ZRE_EXCLUDED_SCH_LINE_STATUS.contains(scheduleLine.getLineStatus()))
						{
							isDataModified = true;
							nonZREQty = +scheduleLine.getQty().longValue();
						}
					}
				}
				if (isDataModified)
				{
					final double tempPrice = nonZREQty * basePrice.doubleValue();
					totalPrice = Double.valueOf(totalPrice.doubleValue() - tempPrice);
					entry.setTotalPrice(totalPrice);
					final Long quantity = Long.valueOf(entry.getQuantity().longValue() - nonZREQty);
					entry.setQuantity(quantity);
				}
				final JnjGTSinglePurchaseAnalysisEntriesData entryData = new JnjGTSinglePurchaseAnalysisEntriesData();
				final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

				/** Populating the order entry data **/
				entryData.setOrderNumber(entry.getOrder().getOrderNumber() + Jnjb2bCoreConstants.SYMBOl_PIPE
						+ entry.getOrder().getCode());

				entryData.setOrderDate(simpleDateFormat.format(entry.getOrder().getDate()));
				entryData.setAmount(createPrice(entry.getOrder().getCurrency(), entry.getTotalPrice()));
				entryData.setQuantity(String.valueOf(entry.getQuantity()));
				entryData.setStatus(entry.getStatus());
				entryData.setUnit(entry.getUnit().getName());

				/** Adding to the order entry data list **/
				entriesData.add(entryData);
			}
		}
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Single Purchase Analysis entries data created :: " + entriesData);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return entriesData;
	}

	/**
	 * This method populates the response data list for the back-order reports
	 * 
	 * @param singlePurchaseAnalysisLines
	 * @return TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>
	 */
	protected TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData> populateSinglePurchaseAnalysisResponseData(
			final List<OrderEntryModel> singlePurchaseAnalysisLines, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "populateSinglePurchaseAnalysisResponseData()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final SimpleDateFormat simpleDateFormatForDisplay = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData> responseMap = new TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>();
		final Map<String, JnjGTOrderChannelModel> orderChannelsMap = new HashMap<String, JnjGTOrderChannelModel>();

		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Going to populate the single purchase analysis response map ... ");
		/** Iterating over the order entry lines **/
		for (final OrderEntryModel line : singlePurchaseAnalysisLines)
		{
			final String accountId = line.getOrder().getUnit().getUid();
			final String poType = line.getOrder().getPoType();
			final Date calculatedStartDate = calculatePeriodStartDate(jnjGTPageableData.getAdditionalSearchText(),
					line.getOrder().getDate(), jnjGTPageableData.getFromDate()).getTime();
			final String orderPeriodStartDate = simpleDateFormat.format(calculatedStartDate);
			final String uniqueKey = accountId + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + orderPeriodStartDate;
			final String orderChannel = getJnjGTReportsService().fetchOrderChannel(orderChannelsMap, poType);

			/** If an entry already exists in the map for the account id and the period start day unique key **/
			if (responseMap.containsKey(uniqueKey))
			{
				final JnjGTSinglePurchaseOrderReportResponseData responseLine = responseMap.get(uniqueKey);

				/**
				 * Incrementing / recalculating the values in the existing response line against key
				 * "accountId_periodStartDate"
				 **/
				if (ELECTRONIC.equalsIgnoreCase(orderChannel))
				{
					/** IN CASE OF ELECTRONIC CHANNEL **/
					responseLine.setAmountElectronic(createPrice(
							line.getOrder().getCurrency(),
							Double.valueOf((null != responseLine.getAmountElectronic() ? responseLine.getAmountElectronic().getValue()
									.doubleValue() : 0D)
									+ (null != line.getTotalPrice() ? line.getTotalPrice().doubleValue() : 0D))));
					responseLine.setFrequencyElectronic(responseLine.getFrequencyElectronic() + 1);
					responseLine.setQuantityElectronic(Long.valueOf((null != responseLine.getQuantityElectronic() ? responseLine
							.getQuantityElectronic().longValue() : 0L)
							+ (null != line.getQuantity() ? line.getQuantity().longValue() : 0L)));
				}
				else
				{
					/** IN CASE OF OTHER CHANNEL **/
					responseLine.setAmountOther(createPrice(
							line.getOrder().getCurrency(),
							Double.valueOf((null != responseLine.getAmountElectronic() ? responseLine.getAmountElectronic().getValue()
									.doubleValue() : 0D)
									+ (null != line.getTotalPrice() ? line.getTotalPrice().doubleValue() : 0D))));
					responseLine.setFrequencyOther(responseLine.getFrequencyOther() + 1);
					responseLine.setQuantityOther(Long.valueOf((null != responseLine.getQuantityOther() ? responseLine
							.getQuantityOther().longValue() : 0L) + (null != line.getQuantity() ? line.getQuantity().longValue() : 0L)));

				}
				responseMap.put(uniqueKey, responseLine);
			}
			/** If an entry does not exist in the map for the account id and the period start day unique key **/
			else
			{
				final String orderPeriodEndDate = simpleDateFormatForDisplay.format(calculatePeriodEndDate(
						jnjGTPageableData.getAdditionalSearchText(), line.getOrder().getDate(), jnjGTPageableData.getToDate())
						.getTime());
				final JnjGTSinglePurchaseOrderReportResponseData responseLine = new JnjGTSinglePurchaseOrderReportResponseData();

				/** populating the response line **/
				responseLine.setAccountNumber(accountId);
				responseLine.setPeriodFrom(simpleDateFormatForDisplay.format(calculatedStartDate));
				responseLine.setPeriodTo(orderPeriodEndDate);
				responseLine.setQuantityUnit(line.getUnit().getName());
				if (ELECTRONIC.equalsIgnoreCase(orderChannel))
				{
					/** IN CASE OF ELECTRONIC CHANNEL **/
					responseLine.setAmountElectronic(createPrice(line.getOrder().getCurrency(), line.getTotalPrice()));
					responseLine.setAmountOther(createPrice(line.getOrder().getCurrency(), Double.valueOf(0)));
					responseLine.setFrequencyElectronic(1);
					responseLine.setFrequencyOther(0);
					responseLine.setQuantityElectronic(line.getQuantity());
					responseLine.setQuantityOther(Long.valueOf(0));
				}
				else
				{
					/** IN CASE OF OTHER CHANNEL **/
					responseLine.setAmountElectronic(createPrice(line.getOrder().getCurrency(), Double.valueOf(0)));
					responseLine.setAmountOther(createPrice(line.getOrder().getCurrency(), line.getTotalPrice()));
					responseLine.setFrequencyElectronic(0);
					responseLine.setFrequencyOther(1);
					responseLine.setQuantityElectronic(Long.valueOf(0));
					responseLine.setQuantityOther(line.getQuantity());
				}
				/** Adding the line to the map against key "accountId_periodStartDate" **/
				responseMap.put(uniqueKey, responseLine);
			}

		}
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"response map for single purchase analysis report is ready with number of entries :: " + responseMap.size());

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return responseMap;
	}

	/**
	 * This method fetches the entries for the particular line in the purchase analysis table
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTSinglePurchaseAnalysisEntriesData>
	 */
	@Override
	public List<JnjGTSinglePurchaseAnalysisEntriesData> fetchSinglePurchaseAnalysisOrderEntries(
			final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchSinglePurchaseAnalysisOrderEntries()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling service layer to fetch single purchase analysis report entries for the line **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports DAO to fetch Single Purchase Analysis entries ");
		final List<OrderEntryModel> orderEntries = getJnjGTReportsService().fetchSinglePurchaseAnalysisReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Single Purchase Analysis entries obtained :: "
				+ orderEntries);

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return populateSinglePurchaseAnalysisEntriesData(orderEntries);
	}

	/**
	 * This method calculates the period start date
	 * 
	 * @param breakdown
	 * @param orderDate
	 * @param userSelectedStartDate
	 * @return calendar
	 */
	protected Calendar calculatePeriodStartDate(final String breakdown, final Date orderDate, final String userSelectedStartDate)
	{
		final String METHOD_NAME = "calculatePeriodStartDate()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(orderDate);
		/** If period breakdown was selected as week **/
		if (WEEK.equalsIgnoreCase(breakdown))
		{
			/** Setting Sunday as the first day of the week **/
			calendar.setFirstDayOfWeek(Calendar.SUNDAY);
			/** Calculating the week start date by subtracting current day of the week index **/
			calendar.add(Calendar.DATE, -(calendar.get(Calendar.DAY_OF_WEEK) - 1));
		}
		/** If period breakdown was selected as month **/
		else
		{
			/** Setting the Calendar day as the 1st of the month **/
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		}

		final SimpleDateFormat formatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		try
		{
			final Date userstartDate = formatter.parse(userSelectedStartDate);
			/**
			 * If user selected start date is after the calculated pocket start date, then the pocket start date should be
			 * set as the user start date
			 **/
			if (userstartDate.after(calendar.getTime()))
			{
				calendar.setTime(userstartDate);
			}
		}
		catch (final ParseException parseException)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ " error parsing date!");
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return calendar;
	}

	/**
	 * This method calculates the period end date
	 * 
	 * @param breakdown
	 * @param orderDate
	 * @param userSelectedEndDate
	 * @return calendar
	 */
	protected Calendar calculatePeriodEndDate(final String breakdown, final Date orderDate, final String userSelectedEndDate)
	{
		final String METHOD_NAME = "calculatePeriodStartDate()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(orderDate);
		/** If period breakdown was selected as week **/
		if (WEEK.equalsIgnoreCase(breakdown))
		{
			/** Setting Sunday as the first day of the week **/
			calendar.setFirstDayOfWeek(Calendar.SUNDAY);
			/** Calculating the week start date by subtracting current day of the week index **/
			calendar.add(Calendar.DATE, 7 - calendar.get(Calendar.DAY_OF_WEEK));
		}
		/** If period breakdown was selected as month **/
		else
		{
			/** Setting the last day of the month **/
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		}

		final SimpleDateFormat formatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		try
		{
			final Date userEndDate = formatter.parse(userSelectedEndDate);
			/**
			 * If user selected end date is before the calculated pocket end date, then the pocket end date should be set
			 * as the user end date
			 **/
			if (userEndDate.before(calendar.getTime()))
			{
				calendar.setTime(userEndDate);
			}
		}
		catch (final ParseException parseException)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ " error parsing date!");
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return calendar;
	}

	/**
	 * This method fetches multi purchase analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTMultiPurchaseOrderReportResponseData>
	 */
	@Override
	public Map<String, Object> fetchMultiPurchaseAnalysisReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchMultiPurchaseAnalysisReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling service layer to fetch single purchase analysis report data based on search criteria **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch single purchase analysis report");

		final List<OrderEntryModel> orderEntries = getJnjGTReportsService().fetchMultiPurchaseAnalysisReport(jnjGTPageableData);
		/** Creating objects to return the response data **/
		List<JnjGTMultiPurchaseOrderReportResponseData> multiPurchaseOrderReportResponseList = null;
		final Map<String, JnjGTMultiPurchaseOrderReportResponseData> filteredMap = new HashMap<String, JnjGTMultiPurchaseOrderReportResponseData>();
		final Map<String, Double> accountTotalSpendingMap = new HashMap<String, Double>();
		List<JnjGTMultiPurchaseOrderReportResponseData> finalResponseList = null;
		CurrencyModel currency = null;
		int productCount = 0;
		double searchTotal = 0;
		if (CollectionUtils.isNotEmpty(orderEntries))
		{
			/* Separate the order entries on the basis of the Accounts provided by the user. */
			for (final OrderEntryModel orderEntryModel : orderEntries)
			{
				//Setting the currency to fetch the current value of the currency.
				currency = orderEntryModel.getOrder().getCurrency();
				/** populating the map with the key as the accountNumber and the total spending of that account **/
				final String accountMapKey = orderEntryModel.getOrder().getUnit().getUid();
				Double totalPrice = orderEntryModel.getTotalPrice();
				/** check if the value exists then we update the value else we just add a new value **/
				if (accountTotalSpendingMap.containsKey(accountMapKey))
				{
					final Double totalSpending = accountTotalSpendingMap.get(accountMapKey);
					final double newTotalSpending = totalSpending.doubleValue() + totalPrice.doubleValue();
					totalPrice = Double.valueOf(newTotalSpending);
				}
				accountTotalSpendingMap.put(accountMapKey, totalPrice);

				searchTotal += orderEntryModel.getTotalPrice().doubleValue();
				/**
				 * populating the map with the key as the combination of accountNumber and product code and the UOM code of
				 * the variant
				 **/
				String currentKey = null;
				if (null != orderEntryModel.getReferencedVariant())
				{
					if((orderEntryModel.getOrder().getUnit()!=null)&& (orderEntryModel.getProduct()!=null)){
						currentKey = orderEntryModel.getOrder().getUnit().getUid() + Jnjb2bCoreConstants.SYMBOl_PIPE
								+ orderEntryModel.getProduct().getCode() + Jnjb2bCoreConstants.SYMBOl_PIPE
								+ orderEntryModel.getReferencedVariant().getUnit().getCode();
					}
					
				}
				else
				{
					//Changes for Null Pointer issue for old date range
					if((orderEntryModel.getOrder().getUnit()!=null)&& (orderEntryModel.getProduct()!=null)){
						currentKey = orderEntryModel.getOrder().getUnit().getUid() + Jnjb2bCoreConstants.SYMBOl_PIPE
								+ orderEntryModel.getProduct().getCode() + Jnjb2bCoreConstants.SYMBOl_PIPE;
					}
					
				}
				if(currentKey!=null){
					if (filteredMap.containsKey(currentKey))
					{
						final JnjGTMultiPurchaseOrderReportResponseData responseData = filteredMap.get(currentKey);
						/** update the amount of the current entry **/
						final double newAmount = responseData.getAmount().getValue().doubleValue()
								+ orderEntryModel.getTotalPrice().doubleValue();
						final PriceData newPrice = createPrice(orderEntryModel.getOrder().getCurrency(), Double.valueOf(newAmount));
						responseData.setAmount(newPrice);
						responseData.setAmountValue(newPrice.getValue().doubleValue());
						/** update the unit quantity of the current entry **/
						final long newQuantity = responseData.getUnitQuantity() + orderEntryModel.getQuantity().longValue();
						responseData.setUnitQuantity(newQuantity);
						/** update the order count of the current entry **/
						int currrentOrder = responseData.getOrderFrequency();
						responseData.setOrderFrequency(++currrentOrder);
						final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
						/** Calculating the weight **/
						if (currentSite.equals(Jnjb2bCoreConstants.CONS) && null != orderEntryModel.getReferencedVariant()
								&& null != orderEntryModel.getReferencedVariant().getWeightQty())
						{
							responseData.setOrderWeight(responseData.getOrderWeight()
									+ (orderEntryModel.getReferencedVariant().getWeightQty().doubleValue() * orderEntryModel.getQuantity()
											.longValue()));
							CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Weight calculated :: "
									+ responseData.getOrderWeight(), LOG);
						}

						filteredMap.put(currentKey, responseData);
					}
					else
					{
						//Changes for Null Pointer issue for old date range
						final JnjGTMultiPurchaseOrderReportResponseData responseData = getMultiPurchaseOrderresponseData(orderEntryModel);						
						filteredMap.put(currentKey, responseData);
					}
				}
				else{
					//Changes for Null Pointer issue for old date range
					final JnjGTMultiPurchaseOrderReportResponseData responseData = getMultiPurchaseOrderresponseData(orderEntryModel);						
					filteredMap.put(currentKey, responseData);
				}
				
			}
			multiPurchaseOrderReportResponseList = new ArrayList<JnjGTMultiPurchaseOrderReportResponseData>(filteredMap.values());
			JnjObjectComparator jnjObjectComparator = null;
			// sorting the JnjGTMultiPurchaseOrderReportResponseData object on the basis of the selected analysis variable in descending order.
			switch (jnjGTPageableData.getSort())
			{
				case "quantity":
					jnjObjectComparator = new JnjObjectComparator(JnjGTMultiPurchaseOrderReportResponseData.class, "getUnitQuantity",
							false, false);
					break;
				case "frequency":
					jnjObjectComparator = new JnjObjectComparator(JnjGTMultiPurchaseOrderReportResponseData.class,
							"getOrderFrequency", false, false);
					break;
				default:
					jnjObjectComparator = new JnjObjectComparator(JnjGTMultiPurchaseOrderReportResponseData.class, "getAmountValue",
							false, false);
					break;
			}

			Collections.sort(multiPurchaseOrderReportResponseList, jnjObjectComparator);

			productCount = jnjGTPageableData.getPageSize() == 0
					|| jnjGTPageableData.getPageSize() > multiPurchaseOrderReportResponseList.size() ? multiPurchaseOrderReportResponseList
					.size() : jnjGTPageableData.getPageSize();
			finalResponseList = multiPurchaseOrderReportResponseList.subList(0, productCount);

		}
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "multi purchase analysis lines obtained");
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		/**
		 * create map to put the response data and the total spending map and pass to the frontend to calculate the total
		 * spending
		 **/
		final Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put("responseData", finalResponseList);
		outputMap.put("totalSpendingMap", accountTotalSpendingMap);
		/**
		 * populating the total spending map with the total amount spend in the particular search , with the key :
		 * searchTotal
		 **/
		double finalTotal = 0;
		if (CollectionUtils.isNotEmpty(finalResponseList))
		{
			if (jnjGTPageableData.getPageSize() != 0)
			{
				for (final JnjGTMultiPurchaseOrderReportResponseData jnjGTMultiPurchaseOrderReportResponseData : finalResponseList)
				{
					finalTotal += jnjGTMultiPurchaseOrderReportResponseData.getAmount().getValue().doubleValue();
				}
				outputMap.put(SEARCH_TOTAL, createPrice(currency, Double.valueOf(finalTotal)));
			}
			else
			{
				outputMap.put(SEARCH_TOTAL, createPrice(currency, Double.valueOf(searchTotal)));
			}
		}
		return outputMap;
	}
	
	/**
	 * This method is used to get the response data from Order Entry for multi-purchase
	 * @param orderEntryModel
	 * @return
	 */
	private JnjGTMultiPurchaseOrderReportResponseData getMultiPurchaseOrderresponseData(OrderEntryModel orderEntryModel) {
		
		final String METHOD_NAME = "getMultiPurchaseOrderresponseData()";
		final JnjGTMultiPurchaseOrderReportResponseData responseData = new JnjGTMultiPurchaseOrderReportResponseData();
		if(orderEntryModel.getProduct()!=null && orderEntryModel.getProduct().getCode()!=null){
			responseData.setProductCode(orderEntryModel.getProduct().getCode());
			responseData.setProductName(jnJGTProductService.getProductName((JnJProductModel) orderEntryModel.getProduct()));
		}
		
		// From session get the current site i.e. MDD/CONS
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (null != orderEntryModel.getReferencedVariant())
		{
			final String productGTIN = currentSite.equals(Jnjb2bCoreConstants.MDD) ? orderEntryModel
					.getReferencedVariant().getEan() : orderEntryModel.getReferencedVariant().getUpc();
			responseData.setProductGTIN(productGTIN);
		}
		//Changing for the defect JJEpic-480
		
		responseData.setAccountNumber(orderEntryModel.getOrder().getUnit().getUid());
		final PriceData price = createPrice(orderEntryModel.getOrder().getCurrency(), orderEntryModel.getTotalPrice());
		responseData.setAmount(price);
		responseData.setAmountValue(price.getValue().doubleValue());
		responseData.setOrderFrequency(1);
		responseData.setUnitQuantity(orderEntryModel.getQuantity().longValue());
		responseData.setUom(orderEntryModel.getUnit().getName());

		/** Calculating the weight **/
		if (currentSite.equals(Jnjb2bCoreConstants.CONS) && null != orderEntryModel.getReferencedVariant()
				&& null != orderEntryModel.getReferencedVariant().getWeightQty())
		{
			responseData.setOrderWeight(orderEntryModel.getReferencedVariant().getWeightQty().doubleValue()
					* orderEntryModel.getQuantity().longValue());
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Weight calculated :: "
					+ responseData.getOrderWeight(), LOG);
		}
		return responseData;
	}

	/**
	 * AAOL-4603: This method fetches order analysis for delivery list report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTOADeliveryListReportResponseData>
	 */
	@Override	public Map<String, Object> fetchOADeliveryListReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchOADeliveryListReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling service layer to fetch order analysis for delivery list report data based on search criteria **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch order analysis for delivery list report");

		final SearchResult<List<Object>> sresult = getJnjGTReportsService().fetchOADeliveryListReport(jnjGTPageableData);
		//final List<OrderEntryModel> orderEntries = getJnjGTReportsService().fetchMultiPurchaseAnalysisReport(jnjGTPageableData);
		/** Creating objects to return the response data **/
		List<JnjGTOADeliveryListReportResponseData> oadeliveryListReportResponseList = null;
			oadeliveryListReportResponseList = new ArrayList<JnjGTOADeliveryListReportResponseData>();//TODO
			//Separate the order entries on the basis of the Accounts provided by the user.
		//	for (final OrderEntryModel orderEntryModel : orderEntries)
		//	{


			for (List<Object> row : sresult.getResult()) {
				try{
			     //String productCode = row.get(0).toString();
			     LOG.info("1st col-code"+row.get(0));
			     //String locName = row.get(1).toString();
			     LOG.info("2nd col-line"+row.get(1));
			 
				final JnjGTOADeliveryListReportResponseData responseData = new JnjGTOADeliveryListReportResponseData();
				
				/*
				 {otenum.code},{o.sapOrderNumber},{oe.sapOrderlineNumber},{o.purchaseOrderNumber},"
				+ "{sd.deliveryNum},{sld.deliveryLineNum},{sd.actualShipDate},"
				+ "{p.franchiseName},{p.code},{p.name},"
				+ "{sld.deliveryQty},{oe.quantity},"
				+" {sld.batchNum}, {sld.batchExpiryDate}, {sld.serialNum}, {sld.podDate}, {o.shipToAccount}, {sld.trackingNum}
				 */
				/*responseData.setOrderType(row.get(0)==null?"":enumerationService
						.getEnumerationName(row.get(0)));*/
				try{
					/* set date*/					
					SimpleDateFormat format1 = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());//"2017-04-05 15:47:00.000000"
					LOG.error("format1.."+"yyyy-MM-dd HH:mm:ss.SSS");
					SimpleDateFormat format2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
					LOG.error("format2.. dd/MM/yyyy");
					String sDate = row.get(6)==null?"":row.get(6).toString();					
					LOG.error("given date is "+sDate);
					Date date = format1.parse(sDate);				
					responseData.setActualShipmentDate(row.get(6)==null?"":format2.format(date));
					LOG.error("fmtd date for actual ship date is "+responseData.getActualShipmentDate());
					sDate = row.get(13)==null?"":row.get(13).toString();
					responseData.setBatchExpiryDate(row.get(13)==null?"":format2.format(format1.parse(sDate)));
					sDate = row.get(15)==null?"":row.get(15).toString();
					responseData.setPodDate(row.get(15)==null?"":format2.format(format1.parse(sDate)));
				}catch(Exception e){
					LOG.error("Delivery List Report : Error while date conversion" + e);
					/*responseData.setActualShipmentDate(row.get(6)==null?"":row.get(6).toString());//Error while date conversion - 2017-05-02 00:00:00
					responseData.setBatchExpiryDate(row.get(13)==null?"":row.get(13).toString());//Error while date conversion
					responseData.setPodDate(row.get(15)==null?"":row.get(15).toString());*///Error while date conversion
				}
				
				responseData.setOrderType(enumerationService.getEnumerationName(JnjOrderTypesEnum.valueOf(row.get(0)==null?"":row.get(0).toString())));//TODO ORDER TYPE ENUM VALUE
				responseData.setSalesDocNum(row.get(1)==null?"":row.get(1).toString());			
				responseData.setLineItem(row.get(2)==null?"":row.get(2).toString());
				responseData.setCustPONum(row.get(3)==null?"":row.get(3).toString());
				responseData.setDeliveryNum(row.get(4)==null?"":row.get(4).toString());
				responseData.setDeliveryItemNum(row.get(5)==null?"":row.get(5).toString());
				/*responseData.setActualShipmentDate(row.get(6)==null?"":row.get(6).toString());*/
				responseData.setFranchiseDesc(row.get(7)==null?"":row.get(7).toString());
				responseData.setProductCode(row.get(8)==null?"":row.get(8).toString());
				responseData.setProductDesc(row.get(9)==null?"":row.get(9).toString());
				responseData.setDeliveryQuantity(row.get(10)==null?0:Integer.parseInt(row.get(10).toString()));				
				responseData.setOrderQuantity(row.get(11)==null?"":row.get(11).toString());
				responseData.setBatchNum(row.get(12)==null?"":row.get(12).toString());				
				/*responseData.setBatchExpiryDate(row.get(13)==null?"":row.get(13).toString());//TODO date conversion
*/				responseData.setSerialNum(row.get(14)==null?"":row.get(14).toString());				
				/*responseData.setPodDate(row.get(15)==null?"":row.get(15).toString());//TODO date conversion
*/				responseData.setShipToAccount(row.get(16)==null?"":row.get(16).toString());				
				responseData.setConnote(row.get(17)==null?"":row.get(17).toString());
				responseData.setShipToName(row.get(18)==null?"":row.get(18).toString());
				responseData.setUom(row.get(19)==null?"":row.get(19).toString());				
			
				oadeliveryListReportResponseList.add(responseData);
				}catch(Exception e){
					LOG.error(e.getMessage());
				}
			}

		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "order analysis for delivery list obtained");
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		/**
		 * create map to put the response data and the total spending map and pass to the frontend to calculate the total
		 * spending
		 **/
		final Map<String, Object> outputMap = new HashMap<String, Object>();
		//outputMap.put("responseData", finalResponseList);//TODO remove duplicate list
		outputMap.put("responseData", oadeliveryListReportResponseList);
		
		return outputMap;
	}

	
	/**
	 * This method populates the response data list for the back-order reports
	 * 
	 * @param backorderLines
	 * @return jnjGTBackorderReportResponseDataList
	 */
	protected List<JnjGTBackorderReportResponseData> populateBackorderReportResponseData(final List<OrderEntryModel> backorderLines)
	{
		final String METHOD_NAME = "populateBackorderReportResponseData()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = null;

		/** Checking if back-order lines are not null or empty **/
		if (null != backorderLines && CollectionUtils.isNotEmpty(backorderLines))
		{
			jnjGTBackorderReportResponseDataList = new ArrayList<JnjGTBackorderReportResponseData>();

			/** Iterating over the back-order lines **/
			for (final OrderEntryModel orderEntryModel : backorderLines)
			{
				/** Iterating over the schedule lines **/
				for (final JnjDeliveryScheduleModel jnjDeliveryScheduleModel : orderEntryModel.getDeliverySchedules())
				{
					/** Checking if the line status is Unconfirmed - UC **/
					if ("UC".equalsIgnoreCase(jnjDeliveryScheduleModel.getLineStatus()) || "BACKORDERED".equalsIgnoreCase(jnjDeliveryScheduleModel.getLineStatus()))
					{
						logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Unconfirmed line found!");
						final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData = new JnjGTBackorderReportResponseData();
						/** Setting order level info **/
						setBackorderOrderLevelInfo(orderEntryModel, jnjGTBackorderReportResponseData);
						/** Setting product level info **/
						setBackorderProductLevelInfo(orderEntryModel, jnjGTBackorderReportResponseData);
						/** Setting unit name **/
						final String productCode = orderEntryModel.getProduct().getCode();
						 final JnJProductModel product = (JnJProductModel) orderEntryModel.getProduct();
							/** Setting unit name **/
							if (null != orderEntryModel.getUnit())
							{
								logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
										"Setting unit info for backorder in jnjGTBackorderReportResponseData.");
								jnjGTBackorderReportResponseData.setUnit(orderEntryModel.getUnit().getName());
								logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
										"Unit info set for backorder in jnjGTBackorderReportResponseData!");
							}
						/** Setting essential data **/
						logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
								"Setting essential data for backorder.");
						jnjGTBackorderReportResponseData.setCurrency(orderEntryModel.getOrder().getCurrency().getSymbol());
						jnjGTBackorderReportResponseData.setItemPrice(String.valueOf(orderEntryModel.getBasePrice()));
						jnjGTBackorderReportResponseData.setQty(String.valueOf(jnjDeliveryScheduleModel.getQty()));
						if (null != product.getBackOrderedDate())
						{
							String EstdDeliveryDate = String.valueOf(jnjDeliveryScheduleModel.getDeliveryDate());
							try{
								SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
								SimpleDateFormat format2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
								Date date = format1.parse(EstdDeliveryDate);
								jnjGTBackorderReportResponseData.setEstimatedAvailability(format2.format(date));
							}catch(ParseException e){
							}
						}
						jnjGTBackorderReportResponseData.setExtendedPrice(calculateExtendedPrice(orderEntryModel,
								jnjDeliveryScheduleModel));
						logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
								"Essential data set for backorder in jnjGTBackorderReportResponseData!");
						/** Adding to the list **/
						jnjGTBackorderReportResponseDataList.add(jnjGTBackorderReportResponseData);
						logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
								"Data added to the jnjGTBackorderReportResponseDataList!");
					}
				}
			}
		}
		else
		{
			/** Back-order lines are null or empty **/
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "backorder lines null or empty!");
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTBackorderReportResponseDataList;
	}

	/**
	 * This method sets the product level info in the jnjGTBackorderReportResponseData
	 * 
	 * @param orderEntryModel
	 * @param jnjGTBackorderReportResponseData
	 */
	protected void setBackorderProductLevelInfo(final OrderEntryModel orderEntryModel,
			final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData)
	{
		final String METHOD_NAME = "setBackorderProductLevelInfo()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** Setting product level info **/
		if (null != orderEntryModel.getProduct())
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Setting product info for backorder.");
			/** Fetching the product URL **/
			jnjGTBackorderReportResponseData.setProductUrl(jnJGTProductService.getProductUrl(orderEntryModel.getProduct()));
			jnjGTBackorderReportResponseData.setProductCode(orderEntryModel.getProduct().getCode());
			jnjGTBackorderReportResponseData.setProductGTIN(orderEntryModel.getProduct().getEan());
			//Changing for the defect JJEpic-480
			jnjGTBackorderReportResponseData.setProductName(jnJGTProductService.getProductName((JnJProductModel) orderEntryModel
					.getProduct()));
			//Below Field Used For franchise
			jnjGTBackorderReportResponseData.setOperatingCompany(retrieveSuperCategory(orderEntryModel));
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"Product info set for backorder in jnjGTBackorderReportResponseData!");
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method sets the order level info in the jnjGTBackorderReportResponseData
	 * 
	 * @param orderEntryModel
	 * @param jnjGTBackorderReportResponseData
	 */
	protected void setBackorderOrderLevelInfo(final OrderEntryModel orderEntryModel,
			final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData)
	{
		final String METHOD_NAME = "setBackorderOrderLevelInfo()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** Setting order level info **/
		if (null != orderEntryModel.getOrder())
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"Setting order info for backorder in jnjGTBackorderReportResponseData.");
			jnjGTBackorderReportResponseData.setCustomerPO(orderEntryModel.getOrder().getPurchaseOrderNumber());
			jnjGTBackorderReportResponseData.setAccountNumber(orderEntryModel.getOrder().getUnit().getUid());
			
			//Changes for AAOL - 3679
			jnjGTBackorderReportResponseData.setStatus(orderEntryModel.getOrder().getStatusDisplay());
			//jnjGTBackorderReportResponseData.setOrderDate(String.valueOf(orderEntryModel.getOrder().getDate()));
			String orderDate=String.valueOf(orderEntryModel.getOrder().getDate());
			try{
			SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			SimpleDateFormat format2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			Date date = format1.parse(orderDate);
			jnjGTBackorderReportResponseData.setOrderDate(format2.format(date));
			LOG.debug("jnjGTBackorderReportResponseData.getOrderDate() "+jnjGTBackorderReportResponseData.getOrderDate());
			}catch(ParseException e){
			}
			jnjGTBackorderReportResponseData.setOrderNumber(orderEntryModel.getOrder().getOrderNumber()
					+ Jnjb2bCoreConstants.SYMBOl_PIPE + orderEntryModel.getOrder().getCode());
			//START for Adding ship to account - 4601
			jnjGTBackorderReportResponseData.setShipToAccount(orderEntryModel.getOrder().getShipToAccount());
			//END for Adding ship to account - 4601
			//jnjGTBackorderReportResponseData.setStatus(orderEntryModel.getStatus().toString());
			if (orderEntryModel.getOrder().getDeliveryAddress() != null){
				if(orderEntryModel.getOrder().getDeliveryAddress().getCompany() != null)
				{
					LOG.debug("Company name="+ orderEntryModel.getOrder().getDeliveryAddress().getCompany());
					jnjGTBackorderReportResponseData.setShipToName(orderEntryModel.getOrder().getDeliveryAddress().getCompany());
				}
			}
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"Order info set for backorder in jnjGTBackorderReportResponseData!");
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method calculates the extended price for the items in the schedule line
	 * 
	 * @param orderEntryModel
	 * @param jnjDeliveryScheduleModel
	 * @return extendedPrice
	 */
	protected String calculateExtendedPrice(final OrderEntryModel orderEntryModel,
			final JnjDeliveryScheduleModel jnjDeliveryScheduleModel)
	{
		final String METHOD_NAME = "calculateExtendedPrice()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		String extendedPrice = null;
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Going to calculate Extended price");

		/** check if base price and quantity not null **/
		if (null != orderEntryModel.getBasePrice() && null != jnjDeliveryScheduleModel.getQty())
		{
			/** multiplying the base price with quantity of the item in the schedule line to get the extended price **/
			extendedPrice = String.valueOf(orderEntryModel.getBasePrice().doubleValue()
					* jnjDeliveryScheduleModel.getQty().longValue());
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Extended price calculated :: " + extendedPrice);
		}
		else
		{
			/** base price or quantity found null **/
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"Extended price not claculated as price or quantity found null!");
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return extendedPrice;
	}

	/**
	 * This method is used to retrieve the super category for setting the operating company
	 * 
	 * @param orderEntryModel
	 * @return operating company
	 */
	protected String retrieveSuperCategory(final OrderEntryModel orderEntryModel)
	{
		final String METHOD_NAME = "retrieveSuperCategory()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		String categoryName = null;
		/** iterating over the second level of super categories **/
		for (final CategoryModel secondLevelSuperCategory : orderEntryModel.getProduct().getSupercategories())
		{
			/** iterating over the first level of super categories **/
			for (final CategoryModel firstLevelSuperCategory : secondLevelSuperCategory.getSupercategories())
			{
				/** operating company name fetched **/
				categoryName = firstLevelSuperCategory.getName();
				logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Category name fetched :: " + categoryName);

				/** operating company name fetched hence breaking the loop **/
				break;
			}
			if (null != categoryName)
			{
				/** operating company name fetched hence breaking the loop **/
				break;
			}
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return categoryName;
	}

	/**
	 * This method fetches Inventory report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTInventoryReportResponseData>
	 */
	@Override
	public List<JnjGTInventoryReportResponseData> fetchInventoryReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchInventoryReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling service layer to fetch Inventory report data based on search criteria **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch inventory report ");

		List<JnjGTInventoryReportResponseData> inventoryReportResponseDataList = null;
		try
		{
			inventoryReportResponseDataList = jnjGTConsignmentStockMapper.mapConsignmentStockRequestResponse(jnjGTPageableData);
		}
		catch (final IntegrationException integrationException)
		{
			LOG.error("Unable to fetch Inventory data due to the following exception : " + integrationException.getMessage(),
					integrationException);
		}
		catch (final SystemException systemException)
		{
			LOG.error("Unable to fetch Inventory data due to the following exception : " + systemException.getMessage(),
					systemException);
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return inventoryReportResponseDataList;
	}
	
	
	/**
	 * This method fetches Inventory report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTInventoryReportResponseData>
	 */
	@Override
	public List<JnjGTInventoryReportResponseData> fetchInventoryDataReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchInventoryReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling service layer to fetch Inventory report data based on search criteria **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch inventory report ");

		List<JnjGTInventoryReportResponseData> inventoryReportResponseDataList = null;
		try
		{
			inventoryReportResponseDataList = jnjGTInventoryMapper.mapInventoryReportRequestResponse(jnjGTPageableData);
		}
		catch (final IntegrationException integrationException)
		{
			LOG.error("Unable to fetch Inventory data due to the following exception : " + integrationException.getMessage(),
					integrationException);
		}
		catch (final SystemException systemException)
		{
			LOG.error("Unable to fetch Inventory data due to the following exception : " + systemException.getMessage(),
					systemException);
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return inventoryReportResponseDataList;
	}

	/**
	 * This method fetches Inventory report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTInventoryReportResponseData>
	 */
	/*@Override
	public List<JnjGTInventoryReportResponseData> fetchInventoryDataReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchInventoryReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		*//** Calling service layer to fetch Inventory report data based on search criteria **//*
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch inventory report ");

		List<JnjGTInventoryReportResponseData> inventoryReportResponseDataList = null;
		try
		{
			inventoryReportResponseDataList = jnjGTInventoryMapper.mapInventoryReportRequestResponse(jnjGTPageableData);
		}
		catch (final IntegrationException integrationException)
		{
			LOG.error("Unable to fetch Inventory data due to the following exception : " + integrationException.getMessage(),
					integrationException);
		}
		catch (final SystemException systemException)
		{
			LOG.error("Unable to fetch Inventory data due to the following exception : " + systemException.getMessage(),
					systemException);
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return inventoryReportResponseDataList;
	}
*/
	/**
	 * @return the jnjGTReportsService
	 */
	public JnjGTReportsService getJnjGTReportsService()
	{
		return jnjGTReportsService;
	}

	/**
	 * @param jnjGTReportsService
	 *           the jnjGTReportsService to set
	 */
	public void setJnjGTReportsService(final JnjGTReportsService jnjGTReportsService)
	{
		this.jnjGTReportsService = jnjGTReportsService;
	}

	/**
	 * 
	 * This method generates the price data
	 * 
	 * @param currency
	 * @param val
	 * @return PriceData
	 */
	protected PriceData createPrice(final CurrencyModel currency, final Double val)
	{
		return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(val.doubleValue()), currency);
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.reports.JnjGTReportsFacade#getShipTOAddressesForAccount(java.lang.String[])
	 */
	@Override
	public List<JnjGTAddressData> getShipTOAddressesForAccount(final String accountNumbers)
	{
		final List<JnjGTAddressData> addressList = new ArrayList<JnjGTAddressData>();
		final Set<JnJB2BUnitModel> unitSet = jnjGTCustomerService.getB2bUnitsForAccountNumbers(accountNumbers);
		for (final JnJB2BUnitModel JnJB2BUnitModel : unitSet)
		{
			for (final AddressModel addressModel : JnJB2BUnitModel.getShippingAddresses())
			{
				final JnjGTAddressData addressData = new JnjGTAddressData();
				jnjGTAddressPopulator.populate(addressModel, addressData);
				addressList.add(addressData);
			}
		}
		return addressList;
	}


	/**
	 * This method fetches back-order report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 */
	@Override
	public List<JnjGTCutReportOrderData> fetchCutReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchCutReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final List<JnjGTCutReportOrderData> reportOrderDatas = new ArrayList<JnjGTCutReportOrderData>();
		/** Calling service layer to fetch back-order report data based on search criteria **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Calling reports service to fetch cut report ");
		final List<OrderModel> cutOrders = getJnjGTReportsService().fetchCutReport(jnjGTPageableData);
		if (CollectionUtils.isNotEmpty(cutOrders))
		{
			for (final OrderModel orderModel : cutOrders)
			{ 
				List<JnjGTCutReportOrderEntryData> orderEntryData = new ArrayList<JnjGTCutReportOrderEntryData>();
				final JnjGTCutReportOrderData jnjGTCutReportOrderData = new JnjGTCutReportOrderData();
				jnjGTCutReportOrderData.setAccountNumber(orderModel.getUnit().getUid());
				jnjGTCutReportOrderData.setOrderDate(orderModel.getDate());
				jnjGTCutReportOrderData.setOrderNumber(orderModel.getOrderNumber());
				jnjGTCutReportOrderData.setHybrisOrderNumber(orderModel.getCode());
				jnjGTCutReportOrderData.setPONumber(orderModel.getPurchaseOrderNumber());
				jnjGTCutReportOrderData.setShipToName((StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getCompany()) ? orderModel
						.getDeliveryAddress().getCompany() + ", " : StringUtils.EMPTY)+orderModel.getDeliveryAddress().getLine1() + ", "
			+ orderModel.getDeliveryAddress().getPostalcode());
				//Logic for setting the operating company
				final StringBuilder operatingCompanies = new StringBuilder();
				for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
				{
					operatingCompanies.append(retrieveSuperCategory((OrderEntryModel) orderEntry)).append(",");
					
					if (orderEntry.getRejected() != null && orderEntry.getRejected().booleanValue())
					{
						
						JnjGTCutReportOrderEntryData reportOrderEntryData = new JnjGTCutReportOrderEntryData();
						reportOrderEntryData = createOrderEntryData(orderEntry);
						orderEntryData.add(reportOrderEntryData);
					}

				}
				if (operatingCompanies.deleteCharAt(operatingCompanies.lastIndexOf(",")) != null)
				{
					jnjGTCutReportOrderData.setOperatingCompany(operatingCompanies.toString());
				}
				
			
			
				jnjGTCutReportOrderData.setCutReportEntries(orderEntryData);
		
				
				reportOrderDatas.add(jnjGTCutReportOrderData);
			}
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return reportOrderDatas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.reports.JnjGTReportsFacade#fetchCutReportOrderEntryData(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<JnjGTCutReportOrderEntryData> fetchCutReportOrderEntryData(final String orderNumber)
	{
		final List<OrderEntryModel> orderEntryList = jnjGTReportsService.fetchEntryDataForCutReport(orderNumber);
		final List<JnjGTCutReportOrderEntryData> orderEntryData = new ArrayList<JnjGTCutReportOrderEntryData>();
		for (final AbstractOrderEntryModel orderEntry : orderEntryList)
		{
			if (orderEntry.getRejected() != null && orderEntry.getRejected().booleanValue())
			{
				JnjGTCutReportOrderEntryData reportOrderEntryData = new JnjGTCutReportOrderEntryData();
				reportOrderEntryData = createOrderEntryData(orderEntry);
				orderEntryData.add(reportOrderEntryData);
			}
		}
		return orderEntryData;
	}

	/**
	 * @param orderEntry
	 */
	protected JnjGTCutReportOrderEntryData createOrderEntryData(final AbstractOrderEntryModel orderEntry)
	{
		final JnjGTCutReportOrderEntryData reportOrderEntryData = new JnjGTCutReportOrderEntryData();
		reportOrderEntryData.setOrderLine(orderEntry.getSapOrderlineNumber());
		reportOrderEntryData.setCutQuantity(orderEntry.getQuantity().toString());
		reportOrderEntryData.setCutReason(orderEntry.getReasonForRejection());
		reportOrderEntryData.setUnitOfMeasure(orderEntry.getUnit().getCode().toString());
		reportOrderEntryData.setOrderQuantity(orderEntry.getQuantity().toString());
		
		//reportOrderEntryData.setItemPrice(orderEntry.getBasePrice());
		reportOrderEntryData.setItemPrice(createPrice(orderEntry.getOrder().getCurrency(), orderEntry.getBasePrice()));
		//reportOrderEntryData.setExtendedPrice(orderEntry.getTotalPrice());
		reportOrderEntryData.setExtendedPrice(createPrice(orderEntry.getOrder().getCurrency(), orderEntry.getTotalPrice()));
		reportOrderEntryData.setProductCode(orderEntry.getProduct().getCode());
		//Changing for the defect JJEpic-480
		reportOrderEntryData.setProductName(jnJGTProductService.getProductName((JnJProductModel) orderEntry.getProduct()));
		reportOrderEntryData.setUpc(orderEntry.getReferencedVariant().getUpc());
		reportOrderEntryData.setGtin(orderEntry.getReferencedVariant().getEan());
		if(orderEntry.getDeliverySchedules()!=null && orderEntry.getDeliverySchedules().size()>0){
			reportOrderEntryData.setAvailabilityDate(orderEntry.getDeliverySchedules().get(0).getMaterialAvailabilityDate());
		}
	
		/** Setting the product URL **/
		reportOrderEntryData.setProductUrl(jnJGTProductService.getProductUrl(orderEntry.getProduct()));
		return reportOrderEntryData;
	}

	/**
	 * This method is used to fetch the static dropdown data from the config table in a map.
	 */
	@Override
	public Map<String, String> getDropdownValuesInMap(final String id)
	{
		final Map<String, String> outputMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> configModels = jnjConfigService.getConfigModelsByIdAndKey(id, null);
		for (final JnjConfigModel jnjConfigModel : configModels)
		{
			try
			{
				outputMap.put(jnjConfigModel.getKey(),
						messageService.getMessageForCode(jnjConfigModel.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{

				LOG.error("Unable to render message text for message code : " + jnjConfigModel.getKey(), exception);
			}
		}
		return outputMap;
	}

	/**
	 * This method is used to fetch the list of static dropdown data from the config table.
	 */
	@Override
	public List<String> getDropdownValuesInList(final String id)
	{
		final List<String> outputList = new ArrayList<String>();
		final List<JnjConfigModel> sortValues = jnjConfigService.getConfigModelsByIdAndKey(id, null);
		for (final JnjConfigModel sortValue : sortValues)
		{
			try
			{
				outputList.add(messageService.getMessageForCode(sortValue.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + sortValue.getKey(), exception);
			}
		}
		return outputList;
	}

	/**
	 * Fetches operating company
	 */
	@Override
	public Map<String, String> getOperatingCompanyDropdown(final String currentSite)
	{
		CatalogVersionModel currentCatalogVersion = null;
		if (currentSite.equalsIgnoreCase("MDD"))
		{
			currentCatalogVersion = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID,
					Jnjb2bCoreConstants.ONLINE);
		}
		else
		{
			currentCatalogVersion = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.CONSUMER_CATALOG_ID,
					Jnjb2bCoreConstants.ONLINE);
			
		}

		final Map<String, String> operatingCompanyMap = new LinkedHashMap<String, String>();		
				
		try
		{
			final CategoryModel mddRootCategory = categoryService.getCategoryForCode(currentCatalogVersion, "Categories");

			for (final CategoryModel category : mddRootCategory.getCategories())
			{
				operatingCompanyMap.put(category.getCode(),category.getName());						        
			}		

		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			//	throw new BusinessException(NO_CATALOG_FOUND_ERROR_MESSAGE + Jnjnab2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
		}

		return operatingCompanyMap;
	}	
		
	/**
	 * 
	 * This method is used to fetch the values for the territory dropdown.
	 */
	@Override
	public List<String> getTerritoryForSalesRep(final String currentSite)
	{

		final List<String> territoryList = new ArrayList<String>();
		final List<JnjGTTerritoryDivisonModel> jnjGTTerritoryModelList = jnjGtTerritoryService
				.getAllJnjGTTerritoryDivModel(currentSite);
		if (!jnjGTTerritoryModelList.isEmpty())
		{
			for (final JnjGTTerritoryDivisonModel jnjGTTerritoryModel : jnjGTTerritoryModelList)
			{
				territoryList.add(jnjGTTerritoryModel.getUid());
			}
		}
		return territoryList;
	}

	/**
	 * 
	 * Fetches franchise or division code
	 */
	@Override
	public Map<String, String> getFranchiseOrDivCode(final String operatingCompany)
	{
		final Map<String, String> franchiseDivMap = new LinkedHashMap<String, String>();
		try
		{
			final CategoryModel mddRootCategory = categoryService.getCategoryForCode(operatingCompany);

			for (final CategoryModel category : mddRootCategory.getAllSubcategories())
			{
				franchiseDivMap.put(category.getCode(), category.getName());
			}

		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			//	throw new BusinessException(NO_CATALOG_FOUND_ERROR_MESSAGE + JnjGTb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
		}
		return franchiseDivMap;
	}

	/**
	 * 
	 * AAOL-4603: Fetches franchise or division code
	 */
	@Override
	public Map<String, String> getFranchiseOrDivCode()
	{
		final Map<String, String> franchiseDivMap = new LinkedHashMap<String, String>();
		try
		{
			List<String> franchiseDescList = getJnjGTReportsService().getFranchiseDesc();
			if(franchiseDescList==null){
				LOG.info("from facade, franchiseDescList.."+franchiseDescList);
			}else{
				LOG.info("from facade, franchiseDescList.."+franchiseDescList.size());
			}
			for (final String franchiseDesc:franchiseDescList)
			{
				franchiseDivMap.put(franchiseDesc, franchiseDesc);
			}
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			exception.getMessage();
			//throw new BusinessException(NO_CATALOG_FOUND_ERROR_MESSAGE + JnjGTb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
		}
		return franchiseDivMap;
	}

	public Map<String, String> getFranchiseOrDivCode_old()
	{
		final Map<String, String> franchiseDivMap = new LinkedHashMap<String, String>();
		try
		{
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			CatalogVersionModel currentCatalogVersion = null;
			if (currentSite.equalsIgnoreCase("MDD"))
			{
				currentCatalogVersion = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID,
						Jnjb2bCoreConstants.ONLINE);
			}
			else
			{
				currentCatalogVersion = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.CONSUMER_CATALOG_ID,
						Jnjb2bCoreConstants.ONLINE);
				
			}
			
			final CategoryModel mddRootCategory = categoryService.getCategoryForCode(currentCatalogVersion, "Categories");

			for (final CategoryModel category : mddRootCategory.getAllSubcategories())
			{
				franchiseDivMap.put(category.getCode(), category.getName());
			}

		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			exception.getMessage();
			//throw new BusinessException(NO_CATALOG_FOUND_ERROR_MESSAGE + JnjGTb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
		}
		return franchiseDivMap;
	}
	
	/**
	 * 
	 * This method returns the ordered from codes
	 * 
	 * @param key
	 * @return codes
	 */
	@Override
	public String fetchValuesFromConfig(final String id, final String key)
	{
		final String METHOD_NAME = "fetchOrderedFromCodes()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final List<JnjConfigModel> configModel = jnjConfigService.getConfigModelsByIdAndKey(id, key);
		String codes = null;
		for (final JnjConfigModel configData : configModel)
		{
			codes = configData.getValue();
			break;
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return codes;
	}

	/** 4871 added by surabhi**/
	@Override
	public Map<String,String> getCartegoryDropdownValuesInMap() {
		
		Map<String,String> categoriesUrlMap = new LinkedHashMap<>();
		final List<String> categories = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Reports.CATEGORIES, Jnjb2bCoreConstants.SYMBOl_COMMA);
		for(String category : categories)
		{
			String categoryUrl = Config.getParameter(CATEGORY_URL+category);
			categoriesUrlMap.put(category, categoryUrl);
		}
		
		return categoriesUrlMap;
	}
	
	@Override
	public Map<String,String> getFinancialReportsTypeDropdownVaulesInMap() {
		
		Map<String, String> financialAnanlysisReporUrlMap = new LinkedHashMap<>();
		List<String> financialAnanlysisReportsTypes = JnJCommonUtil
				.getValues(Jnjb2bCoreConstants.Reports.FINANCIAL_REPORT_TYPE, Jnjb2bCoreConstants.SYMBOl_COMMA);
		for (String financialAnanlysisReportsType : financialAnanlysisReportsTypes) {
			String financialAnanlysisReportsTypeUrl = Config
					.getParameter(financialAnanlysisReportsType + REPORT_TYPE_URL);
			financialAnanlysisReporUrlMap.put(financialAnanlysisReportsType, financialAnanlysisReportsTypeUrl);
		}
		return financialAnanlysisReporUrlMap;
		
	}

	@Override
	public Map<String,String> getInventoryReportsTypeDropdownVaulesInMap() {
		
		Map<String, String> inventoryAnanlysisReporUrlMap = new LinkedHashMap<>();
		
		List<String> inventoryAnanlysisReportsTypes = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Reports.INVENTORY_REPORT_TYPE, Jnjb2bCoreConstants.SYMBOl_COMMA);
		for (String inventoryAnanlysisReportsType : inventoryAnanlysisReportsTypes) {
			String inventoryAnanlysisReportsTypeUrl = Config.getParameter(inventoryAnanlysisReportsType +REPORT_TYPE_URL);
			inventoryAnanlysisReporUrlMap.put(inventoryAnanlysisReportsType, inventoryAnanlysisReportsTypeUrl);
		}
		return inventoryAnanlysisReporUrlMap;
	}

	@Override
	public Map<String,String> getOrderReportsTypeDropdownVaulesInMap() {
		
		Map<String, String> orderAnanlysisReporUrlMap = new LinkedHashMap<>();
		
		List<String> orderAnalysisReportsTypes = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Reports.ORDER_REPORT_TYPE, Jnjb2bCoreConstants.SYMBOl_COMMA);
		for (String orderAnalysisReportsType : orderAnalysisReportsTypes) {
			String orderAnanlysisReportsTypeUrl = Config.getParameter(orderAnalysisReportsType + REPORT_TYPE_URL);
			orderAnanlysisReporUrlMap.put(orderAnalysisReportsType, orderAnanlysisReportsTypeUrl);
		}
		return orderAnanlysisReporUrlMap;
	}
	/** End 4871 **/
	
		@Override
	public List<JnjGTFinancialAccountAgingReportData> simulateAccountAgingReport(JnjGTPageableData jnjGTPageableData) {
		List<JnjGTFinancialAccountAgingReportData> accountAgingReportList = null;
		
		try {
			
			accountAgingReportList = jnjGTAccountAgingReportMapper.mapAccountAgingReportRequestResponse(jnjGTPageableData);
		} catch (SystemException | IntegrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountAgingReportList;
	}
		
		
	@Override
	public List<JnjGTFinancialBalanceSummaryReportData> simulateBalanceSummaryReport(JnjGTPageableData jnjGTPageableData) {
		List<JnjGTFinancialBalanceSummaryReportData> balanceSummaryReportList = null;
		
		try {
			
			balanceSummaryReportList = jnjGTBalanceSummaryReportMapper.mapBalanceSummaryReportRequestResponse(jnjGTPageableData);
		} catch (SystemException | IntegrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return balanceSummaryReportList;
	}
	
	@Override
	public List<JnjGTFinancialPaymentSummaryReportData> simulatePaymentSummaryReport(JnjGTPageableData jnjGTPageableData) {
		List<JnjGTFinancialPaymentSummaryReportData> paymentSummaryReportList = null;
		
		try {
			
			paymentSummaryReportList = jnjGTPaymentSummaryReportMapper.mapPaymentSummaryReportRequestResponse(jnjGTPageableData);
		} catch (SystemException | IntegrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return paymentSummaryReportList;
	}
	
	@Override
	public List<JnjGTFinancialCreditSummaryReportData> simulateCreditSummaryReport(JnjGTPageableData jnjGTPageableData) {
		List<JnjGTFinancialCreditSummaryReportData> consCreditSummaryReportList = null;
		
		try {
			
			consCreditSummaryReportList = jnjGTCreditSummaryReportMapper.mapCreditSummaryReportRequestResponse(jnjGTPageableData);
		} catch (SystemException | IntegrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return consCreditSummaryReportList;
	}
	
	@Override
	public List<JnjGTConsInventoryData> simulateConsInventoryReport(JnjGTPageableData jnjGTPageableData) {
		List<JnjGTConsInventoryData> consInventoryReportList = null;
		
		try {
			
		consInventoryReportList = jnjGTConsInventoryReportMapper.mapConsInventoryReportRequestResponse(jnjGTPageableData);
		} catch (SystemException | IntegrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return consInventoryReportList;
	}
	
	@Override	
	public List<String> getDropdownFranchiseCode(String productCode)
	{
		
		return jnjGTReportsService.fetchFranchiseCode(productCode);
		
	}
	
	
	/*public List<JnjGTFinancialSummaryReportData> fetchFinancialSummaryReport(final JnjGTPageableData jnjGTPageableData){
		
		return null;
	}*/
	
	public List<String> getPayerIdValuesBasedPayFromAddress(String accountid){
		
		final List<String> payerIdList = jnjGTReportsService.fetchPayerId(accountid);
		
		return payerIdList;
	}
	
	
	@Override	
	public List<String> fetchStockLocationAccount(String accountid)
	{
		
		return jnjGTReportsService.fetchStockLocationAccount(accountid);
		
	}
}
