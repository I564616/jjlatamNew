/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.reports.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.gt.pac.aera.PacHiveException;
import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.reports.JnjLatamReportsFacade;
import com.jnj.facades.util.impl.JnjLatamCommonFacadeUtilImpl;
import com.pac.aera.job.service.JnjGTPacHiveConfigurationService;
import com.jnj.facades.services.JnJLatamReportService;
import com.jnj.la.core.model.JnjLaOpenOrdersReportTemplateModel;
import com.jnj.la.core.data.JnjLaOpenOrdersReportTemplateData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.jnj.facades.data.JnjLaOpenOrdersReportReponseData;
import com.jnj.la.core.model.JnjOrderTypeModel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.jnj.core.util.JnJCommonUtil;

import java.util.List;

import com.jnj.facade.util.JnjCommonFacadeUtil;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;


/**
 *
 */
public class JnjLatamReportsFacadeImpl extends DefaultJnjGTReportsFacade implements JnjLatamReportsFacade
{
	private JnjLatamCommonFacadeUtilImpl jnjLatamCommonFacadeUtil;
	private static final Logger LOG = Logger.getLogger(JnjLatamReportsFacadeImpl.class);
	protected static final String BACKORDER_REPORT_COUNTRY = "backorder.report.disabled.country";
	protected static final String BACKORDER_REPORT_ACCOUNTS = "backorder.report.active.accounts";
	protected static final String INVENTORY_REPORT_WITHOUT_BO = "reports.inventory.without.backorder";
	protected static final String SYMBOL_COMMA = ",";	
	protected static final String DB_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
	final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
	private JnjGTPacHiveConfigurationService jnjGTPacHiveConfigurationService;
	
	private CatalogVersionService catalogVersionService;

	
	private CategoryService categoryService;

	
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	
	protected JnjGTB2BUnitService jnjGTB2BUnitService;
	
	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	private ConfigurationService configurationService;
	
    private JnJLatamReportService jnjLatamReportService;
	
	private Converter<JnjLaOpenOrdersReportTemplateModel, JnjLaOpenOrdersReportTemplateData> jnjLaOpenOrdersReportTemplateConverter;
   
	private Converter<OrderEntryModel, JnjLaOpenOrdersReportReponseData> jnjLaOpenOrdersReportResponseConverter;
	
   
	@Override
	public Map<String, String> getOperatingCompanyDropdown(final String currentSite)
	{
		final String methodName = "getOperatingCompanyDropdown()";
		CatalogVersionModel currentCatalogVersion = null;
		final String loggedInSite = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();


		if (currentSite.equalsIgnoreCase(Jnjb2bCoreConstants.MDD))
		{
			try
			{
				currentCatalogVersion = catalogVersionService.getCatalogVersion(loggedInSite.toLowerCase() + "ProductCatalog",
						Jnjb2bCoreConstants.ONLINE);
			}
			catch (final UnknownIdentifierException exception)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, methodName,
						"CatalogVersion not found : " + loggedInSite.toLowerCase() + "ProductCatalog", exception,
						JnjLatamReportsFacadeImpl.class);
			}
		}
		else
		{
			currentCatalogVersion = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.CONSUMER_CATALOG_ID,
					Jnjb2bCoreConstants.ONLINE);

		}

		final Map<String, String> operatingCompanyMap = new LinkedHashMap<>();

		try
		{
			final CategoryModel mddRootCategory = categoryService.getCategoryForCode(currentCatalogVersion, "Categories");

			for (final CategoryModel category : mddRootCategory.getCategories())
			{
				operatingCompanyMap.put(category.getCode(), category.getName());
			}

		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, methodName,
					"CatalogVersion not found : " + currentCatalogVersion, exception, JnjLatamReportsFacadeImpl.class);
		}

		return operatingCompanyMap;
	}

	@Override
	public Map<String, String> getInventoryReportsTypeDropdownVaulesInMap() {
		
		Map<String, String> inventoryAnalysisReporUrlMap = new LinkedHashMap<>();
		List<String> inventoryAnalysisReportsTypes;
		if(isBackOrderAccessAuthorized()){
			inventoryAnalysisReportsTypes = JnJCommonUtil.getValues(Jnjb2bCoreConstants.Reports.INVENTORY_REPORT_TYPE, SYMBOL_COMMA);
		}else{
			inventoryAnalysisReportsTypes = JnJCommonUtil.getValues(INVENTORY_REPORT_WITHOUT_BO, SYMBOL_COMMA);
		}
		for (String inventoryAnalysisReportsType : inventoryAnalysisReportsTypes) {
			String inventoryAnalysisReportsTypeUrl = JnJCommonUtil.getValue(inventoryAnalysisReportsType +REPORT_TYPE_URL);
			inventoryAnalysisReporUrlMap.put(inventoryAnalysisReportsType, inventoryAnalysisReportsTypeUrl);
		}
		return inventoryAnalysisReporUrlMap;
	}
	
	@Override
	public List<JnjGTBackorderReportResponseData> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchBackOrderReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
        if (isBackOrderAccessAuthorized()) {
            if (null != jnjGTPageableData.getSearchBy() && !"".equals(jnjGTPageableData.getSearchBy())) {
                convertBackorderProductCodeToPK(jnjGTPageableData, false);
            }
            final List<OrderEntryModel> backorderLines = getJnjGTReportsService().fetchBackOrderReport(jnjGTPageableData);

            return populateBackorderReportResponseData(backorderLines);
        } else {
            LOG.error("Back order report access is denied for the user!");
            return new ArrayList<>();
        }
	}

    /**
     * Identifies if the backorder report access is authorized for the current country and the current
     * logged in unit.
     * 
     * @return true if backorder report access is authorized, false otherwise.
     */
    private boolean isBackOrderAccessAuthorized() {
        final String loggedCountry = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
        final List<String> countries = JnJCommonUtil.getValues(BACKORDER_REPORT_COUNTRY, SYMBOL_COMMA);
        final List<String> accounts = JnJCommonUtil.getValues(BACKORDER_REPORT_ACCOUNTS, SYMBOL_COMMA);
        final String currentUnit = jnjGTB2BUnitService.getCurrentB2BUnit().getUid();
        // If the backorder report access is disabled for the current country, then only accounts that are
        // whitelisted from that country can access backorder reports.
        return (!(CollectionUtils.isNotEmpty(countries) && countries.contains(loggedCountry)))
                || (CollectionUtils.isNotEmpty(accounts) && accounts.contains(currentUnit));
    }

	@Override
	protected List<JnjGTBackorderReportResponseData> populateBackorderReportResponseData(final List<OrderEntryModel> backorderLines)
	{
		List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = null;
		if(CollectionUtils.isNotEmpty(backorderLines)) {
			jnjGTBackorderReportResponseDataList = new ArrayList<>();
			for (final OrderEntryModel orderEntryModel : backorderLines)
			{
					final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData = new JnjGTBackorderReportResponseData();
					setCommonBackorderData(orderEntryModel,jnjGTBackorderReportResponseData);
					jnjGTBackorderReportResponseData.setExtendedPrice(decimalFormat.format(orderEntryModel.getTotalPrice()));
					jnjGTBackorderReportResponseData.setEstimatedDeliveryDate((null !=orderEntryModel.getExpectedDeliveryDate())? convertDateFormat(orderEntryModel.getExpectedDeliveryDate()):jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE, i18nService.getCurrentLocale()));
					jnjGTBackorderReportResponseData.setScheduleLines(setScheduleLines(orderEntryModel));
					jnjGTBackorderReportResponseDataList.add(jnjGTBackorderReportResponseData);
				
			}
		}
		
		return jnjGTBackorderReportResponseDataList;
	}
	

	/**
	 * Sets Schedule Line data
	 * @param orderEntryModel
	 * @return
	 */
	private List<JnjDeliveryScheduleData> setScheduleLines(OrderEntryModel orderEntryModel) {
		List<JnjDeliveryScheduleData> deliverySchedules = new ArrayList<>();
		if (CollectionUtils.isEmpty(orderEntryModel.getDeliverySchedules()))
		{
			try {
				if(jnjGTPacHiveConfigurationService.isPacHiveEnabledForOrderEntry(orderEntryModel)) {
					deliverySchedules = populateEDDInDummyScheduleLines(orderEntryModel);
				}
			} catch (PacHiveException e) {
				LOG.error("Exception occurred while checking pac enabled " + e.getMessage(),e);
			}
			
		}
		else {
			populateScheduleLines(orderEntryModel, deliverySchedules);
		}
		if(CollectionUtils.isEmpty(deliverySchedules)) {
			JnjDeliveryScheduleData deliveryScheduledata = new JnjDeliveryScheduleData();
			deliveryScheduledata.setFormattedDeliveryDate(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE, i18nService.getCurrentLocale()));
			deliveryScheduledata.setRequestedUnitsTotalQuantity(orderEntryModel.getQuantity().toString());
			deliverySchedules.add(deliveryScheduledata);
		}
		return deliverySchedules;
	}

	private void populateScheduleLines(OrderEntryModel orderEntryModel, List<JnjDeliveryScheduleData> deliverySchedules) {
		for(JnjDeliveryScheduleModel deliverySchedule: orderEntryModel.getDeliverySchedules())
		{
			JnjDeliveryScheduleData deliveryScheduledata = new JnjDeliveryScheduleData();
			deliveryScheduledata.setLineNumber(deliverySchedule.getLineNumber());
			deliveryScheduledata.setQuantity(deliverySchedule.getQty());
			deliveryScheduledata.setRequestedUnitsTotalQuantity(orderEntryModel.getQuantity().toString());
			deliveryScheduledata.setDeliveryDate(!ObjectUtils.isEmpty(deliverySchedule.getProofOfDeliveryDate())? deliverySchedule.getProofOfDeliveryDate(): deliverySchedule.getDeliveryDate());
			if (!ObjectUtils.isEmpty(deliverySchedule.getProofOfDeliveryDate())){
				deliveryScheduledata.setProofOfDeliveryDate(deliverySchedule.getProofOfDeliveryDate().toString());
			}
			setEstimateDeliveryDate(deliverySchedule, deliveryScheduledata);

			deliveryScheduledata.setScheduledLineNumber(deliverySchedule.getScheduledLineNumber());
			deliverySchedules.add(deliveryScheduledata);
		}
		try {
			if(jnjGTPacHiveConfigurationService.isPacHiveEnabledForOrderEntry(orderEntryModel)) {
				updateHiveDetails(deliverySchedules, orderEntryModel);
			}
		} catch (PacHiveException e) {
			LOG.error("Exception occurred while checking pac enabled " + e.getMessage(),e);
		}
	}

	private void setEstimateDeliveryDate(
			JnjDeliveryScheduleModel deliverySchedule,
			JnjDeliveryScheduleData deliveryScheduledata) {
		try {
			final DateFormat dateFormat = new SimpleDateFormat(
					Jnjlab2bcoreConstants.Order.DATE_FORMAT);
			deliveryScheduledata.setQuantity(deliverySchedule.getQty());
			String date1 = configurationService.getConfiguration().getString(
					Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DATE);
			String date2 = configurationService.getConfiguration().getString(
					Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DT);

			Date defaultDate1 = dateFormat.parse(date1);
			Date defaultDate2 = dateFormat.parse(date2);

			if (null != deliveryScheduledata.getDeliveryDate()) {
				Date deliveryDate = dateFormat.parse(dateFormat
						.format(deliveryScheduledata.getDeliveryDate()));
				if (defaultDate1.compareTo(deliveryDate) != 0
						&& defaultDate2.compareTo(deliveryDate) != 0) {
					deliveryScheduledata
							.setFormattedDeliveryDate(convertDateFormat(deliveryScheduledata
									.getDeliveryDate()));

				} else {
					deliveryScheduledata
							.setFormattedDeliveryDate(jnjCommonFacadeUtil
									.getMessageFromImpex(
											Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE,
											i18nService.getCurrentLocale()));
				}
			} else {
				deliveryScheduledata
						.setFormattedDeliveryDate(jnjCommonFacadeUtil
								.getMessageFromImpex(
										Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE,
										i18nService.getCurrentLocale()));
			}
		} catch (ParseException pe) {
			JnjGTCoreUtil.logErrorMessage("Doc", "setDeliveryScheduleDate()",
					"Error while writing Data.", pe,
					JnjLatamReportsFacadeImpl.class);
		}

	}

	/**
	 * Updates PAC HIVE Data
	 * @param deliverySchedules
	 * @param orderEntryModel
	 */
	private void updateHiveDetails(List<JnjDeliveryScheduleData> deliverySchedules, OrderEntryModel orderEntryModel) {
		for(JnjPacHiveEntryModel entry : orderEntryModel.getJnjPacHiveEntries()) {
			for(JnjDeliveryScheduleData scheduleData: deliverySchedules) {
				//mapped with LineNo feild as per Data availability in PROD 
				final String scheduledLineNumber = stripLeadingZeros(scheduleData.getLineNumber());
				
				final String pacHiveSchedLineNumber = stripLeadingZeros(entry.getSchedLineNumber());
				if(StringUtils.equals(scheduledLineNumber, pacHiveSchedLineNumber) && StringUtils.isEmpty(scheduleData.getProofOfDeliveryDate())) {
					populatePacHiveScheduleLines(orderEntryModel, entry, scheduleData, pacHiveSchedLineNumber);
					break;
				}

			}
		}
	}

	private void populatePacHiveScheduleLines(OrderEntryModel orderEntryModel, JnjPacHiveEntryModel entry, JnjDeliveryScheduleData scheduleData, String pacHiveSchedLineNumber) {
		scheduleData.setDeliveryDate(entry.getConvertedRecommendedDeliveryDate());
		scheduleData.setFormattedDeliveryDate(getFormattedDeliveryDate(entry));
		scheduleData.setLineNumber(pacHiveSchedLineNumber);
		if (null != entry.getConfirmedQuantity()){
			scheduleData.setQuantity(entry.getConfirmedQuantity().longValue());
		}
		scheduleData.setSubFranchise(entry.getSubFranchise());
		scheduleData.setRequestedUnitsTotalQuantity((null == entry.getRequestedUnitsTotalQuantity() || entry.getRequestedUnitsTotalQuantity().longValue()==0)? orderEntryModel.getQuantity().toString() : entry.getRequestedUnitsTotalQuantity().toString());
		scheduleData.setAmountPendingDelivery((null == entry.getAmountPendingDelivery() || Double.compare(entry.getAmountPendingDelivery().doubleValue(), 0.0)==0)? StringUtils.EMPTY : entry.getAmountPendingDelivery().toString());
		scheduleData.setQuantityPendingStock((null == entry.getQuantityPendingStock() || Double.compare(entry.getQuantityPendingStock().doubleValue(), 0.0)== 0)? StringUtils.EMPTY : entry.getQuantityPendingStock().toString());

	}

	private String getFormattedDeliveryDate(JnjPacHiveEntryModel entry) {
		if (null != entry.getConvertedRecommendedDeliveryDate()) {
			return convertDateFormat(entry.getConvertedRecommendedDeliveryDate());
			} else {
				return jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE, i18nService.getCurrentLocale());
			}
	}
	/**
	 * @param sourceEntryModel
	 * @param targetEntryData
	 * @return
	 */
	private List<JnjDeliveryScheduleData> populateEDDInDummyScheduleLines(
			final AbstractOrderEntryModel sourceEntryModel) {
		final List<JnjDeliveryScheduleData> dummyScheduleDataList =new ArrayList<>();
		for (JnjPacHiveEntryModel sourceJnjPacHiveEntry : sourceEntryModel.getJnjPacHiveEntries())
		{
			final JnjDeliveryScheduleData target = new JnjDeliveryScheduleData();
			target.setLineNumber(this.stripLeadingZeros(sourceJnjPacHiveEntry.getSchedLineNumber()));
			if (null!=sourceJnjPacHiveEntry.getConfirmedQuantity()) {
				target.setQuantity(sourceJnjPacHiveEntry.getConfirmedQuantity().longValue());
			}			
			target.setSubFranchise(sourceJnjPacHiveEntry.getSubFranchise());			
		    target.setRequestedUnitsTotalQuantity((null == sourceJnjPacHiveEntry.getRequestedUnitsTotalQuantity() || sourceJnjPacHiveEntry.getRequestedUnitsTotalQuantity()==0)? sourceEntryModel.getQuantity().toString() : sourceJnjPacHiveEntry.getRequestedUnitsTotalQuantity().toString());			
			target.setAmountPendingDelivery((null == sourceJnjPacHiveEntry.getAmountPendingDelivery() || Double.compare(sourceJnjPacHiveEntry.getAmountPendingDelivery().doubleValue(),0.0)==0)? StringUtils.EMPTY : sourceJnjPacHiveEntry.getAmountPendingDelivery().toString());
			target.setQuantityPendingStock((null== sourceJnjPacHiveEntry.getQuantityPendingStock() || Double.compare(sourceJnjPacHiveEntry.getQuantityPendingStock().doubleValue(), 0.0) == 0)? StringUtils.EMPTY : sourceJnjPacHiveEntry.getQuantityPendingStock().toString());
	
			target.setDeliveryDate(sourceJnjPacHiveEntry.getConvertedRecommendedDeliveryDate());
			if (null != target.getDeliveryDate()) {
			target.setFormattedDeliveryDate(convertDateFormat(target.getDeliveryDate()));
			} else {
				target.setFormattedDeliveryDate(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE, i18nService.getCurrentLocale()));
			}
			dummyScheduleDataList.add(target);
		}
		return dummyScheduleDataList;
	}
	
	/**
	 * to remove leading zeroes
	 * @param sapOrderlineNumber
	 * @return
	 */
	protected String stripLeadingZeros(final String sapOrderlineNumber)
	{
		return StringUtils.stripStart(sapOrderlineNumber, "0");
	}

	/**
	 * Sets backorder data
	 * @param orderEntryModel
	 * @param jnjGTBackorderReportResponseData
	 */
	private void setCommonBackorderData(final OrderEntryModel orderEntryModel,final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData) {
		setBackorderOrderLevelInfo(orderEntryModel, jnjGTBackorderReportResponseData);
		setBackorderProductLevelInfo(orderEntryModel, jnjGTBackorderReportResponseData);
		if (null != orderEntryModel.getUnit())
		{
			jnjGTBackorderReportResponseData.setUnit(orderEntryModel.getUnit().getName());
		}
		jnjGTBackorderReportResponseData.setCurrency(orderEntryModel.getOrder().getCurrency().getSymbol());
		jnjGTBackorderReportResponseData.setItemPrice(decimalFormat.format(orderEntryModel.getBasePrice()));
		jnjGTBackorderReportResponseData.setQty(String.valueOf(orderEntryModel.getQuantity()));
	}
	@Override
	protected void setBackorderProductLevelInfo(OrderEntryModel orderEntryModel,
			JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData) {
		jnjGTBackorderReportResponseData.setProductUrl(jnJGTProductService.getProductUrl(orderEntryModel.getProduct()));
		jnjGTBackorderReportResponseData.setProductCode(orderEntryModel.getProduct().getCode());
		jnjGTBackorderReportResponseData.setProductGTIN(orderEntryModel.getProduct().getEan());
		jnjGTBackorderReportResponseData.setUnit(orderEntryModel.getProduct().getUnit() != null ? orderEntryModel.getProduct().getUnit().getCode() : StringUtils.EMPTY);
		jnjGTBackorderReportResponseData.setProductName(jnJGTProductService.getProductName((JnJProductModel) orderEntryModel
				.getProduct()));
		
	}
	@Override
	protected void setBackorderOrderLevelInfo(OrderEntryModel orderEntryModel,
			JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData) {
		jnjGTBackorderReportResponseData.setCustomerPO(orderEntryModel.getOrder().getPurchaseOrderNumber());
		jnjGTBackorderReportResponseData.setAccountNumber(orderEntryModel.getOrder().getUnit().getUid());
		
		jnjGTBackorderReportResponseData.setStatus(getJnjLatamCommonFacadeUtil()
				.getMessageFromImpex("order.status." + orderEntryModel.getStatus() + ".value"));
		jnjGTBackorderReportResponseData.setOrderDate(convertDateFormat(orderEntryModel.getOrder().getDate()));
		jnjGTBackorderReportResponseData.setOrderNumber(orderEntryModel.getOrder().getSapOrderNumber()
				+ Jnjb2bCoreConstants.SYMBOl_PIPE + orderEntryModel.getOrder().getCode());
		jnjGTBackorderReportResponseData.setSapOrderNum(orderEntryModel.getOrder().getSapOrderNumber());
		if (null!=  orderEntryModel.getOrder().getShipToAccount()) {			
			jnjGTBackorderReportResponseData.setShipToAccount(orderEntryModel.getOrder().getShipToAccount());
		} else {
			jnjGTBackorderReportResponseData.setShipToAccount(null != orderEntryModel.getOrder().getDeliveryAddress() ? orderEntryModel.getOrder().getDeliveryAddress().getJnJAddressId() : StringUtils.EMPTY);
		}
		if (null != orderEntryModel.getOrder().getDeliveryAddress() && null != orderEntryModel.getOrder().getDeliveryAddress().getCompany()){
			jnjGTBackorderReportResponseData.setShipToName(orderEntryModel.getOrder().getDeliveryAddress().getCompany());
		}
		
	}

	@Override
	public List<JnjLaOpenOrdersReportReponseData> fetchOpenOrdersReport(
			final JnjGTPageableData jnjGTPageableData) {

		final String METHOD_NAME = "fetchOpenOrdersReport()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME,
				METHOD_NAME, Logging.BEGIN_OF_METHOD);

		if (null != jnjGTPageableData.getSearchBy()
				&& !"".equals(jnjGTPageableData.getSearchBy())) {
			convertBackorderProductCodeToPK(jnjGTPageableData, false);
		}
		final List<OrderEntryModel> openOrdersReportLines = jnjLatamReportService
				.fetchOpenOrdersReport(jnjGTPageableData);		

		return populateOpenOrdersReportResponseData(openOrdersReportLines);
		

	}
	
	private List<JnjLaOpenOrdersReportReponseData> populateOpenOrdersReportResponseData(final List<OrderEntryModel> openOrdersReportLines)
	{	
		List<JnjLaOpenOrdersReportReponseData> openOrdersReportReponseDataList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(openOrdersReportLines)) {
			
			for (final OrderEntryModel orderEntryModel : openOrdersReportLines)
			{
					final JnjLaOpenOrdersReportReponseData jnjLaOpenOrdersReportReponseData = jnjLaOpenOrdersReportResponseConverter.convert(orderEntryModel);
					setCommonBackorderData(orderEntryModel, jnjLaOpenOrdersReportReponseData);					
					jnjLaOpenOrdersReportReponseData.setScheduleLines(setScheduleLines(orderEntryModel));					
					openOrdersReportReponseDataList.add(jnjLaOpenOrdersReportReponseData);
				
			}
		}
		
		return openOrdersReportReponseDataList;
	}
	
	@Override
	public Map<String, String> getOrderTypes() {
		List<JnjOrderTypeModel> orderTypeList = jnjLatamReportService.getOrderTypes();
		Map<String, String> orderTypeMap = new HashMap<>();
		for (JnjOrderTypeModel orderType : orderTypeList) {
			orderTypeMap.put(orderType.getCode(), orderType.getName());	
		}
		 return orderTypeMap;
	}
	
	@Override
	public void saveOpenOrdersReportTemplate(final JnjLaOpenOrdersReportTemplateData openOrdersReportData, final String templateName) {
		jnjLatamReportService.saveOpenOrdersReportTemplate(openOrdersReportData, templateName);
	}
	
	
	@Override
	public List<JnjLaOpenOrdersReportTemplateData> getOpenOrdersReportTemplate() {
		List<JnjLaOpenOrdersReportTemplateModel> openOrdersReportTemplate = jnjLatamReportService.getOpenOrdersReportTemplate();
		return jnjLaOpenOrdersReportTemplateConverter.convertAll(openOrdersReportTemplate);

    }
	
	
	@Override
	public void deleteOpenOrdersReportTemplate(final String templateName) {
		jnjLatamReportService.deleteOpenOrdersReportTemplate(templateName);
		
	}
	
	public JnJLatamReportService getJnjLatamReportService()
	{
		return jnjLatamReportService;
	}

	public void setJnjLatamReportService(final JnJLatamReportService jnjLatamReportService)
	{
		this.jnjLatamReportService = jnjLatamReportService;
	}
	
	public Converter<JnjLaOpenOrdersReportTemplateModel, JnjLaOpenOrdersReportTemplateData> getJnjLaOpenOrdersReportTemplateConverter()
	{
		return jnjLaOpenOrdersReportTemplateConverter;
	}

	public void setJnjLaOpenOrdersReportTemplateConverter(final Converter<JnjLaOpenOrdersReportTemplateModel, JnjLaOpenOrdersReportTemplateData> jnjLaOpenOrdersReportTemplateConverter)
	{
		this.jnjLaOpenOrdersReportTemplateConverter = jnjLaOpenOrdersReportTemplateConverter;
	}

	public Converter<OrderEntryModel, JnjLaOpenOrdersReportReponseData> getJnjLaOpenOrdersReportResponseConverter()
	{
		return jnjLaOpenOrdersReportResponseConverter;
	}

	
	public void setJnjLaOpenOrdersReportResponseConverter(final Converter<OrderEntryModel, JnjLaOpenOrdersReportReponseData> jnjLaOpenOrdersReportResponseConverter)
	{
		this.jnjLaOpenOrdersReportResponseConverter = jnjLaOpenOrdersReportResponseConverter;
	}




	private String convertDateFormat(final Date orderDate) {
		if(orderDate == null){
			return StringUtils.EMPTY;
		}
		SimpleDateFormat format2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		return format2.format(orderDate);

	}

	@Override
	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService) {
		this.catalogVersionService = catalogVersionService;
	}
	@Override
	public CategoryService getCategoryService() {
		return categoryService;
	}
	public void setCategoryService(final CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}
	public final void setJnjGetCurrentDefaultB2BUnitUtil(final JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil) {
		this.jnjGetCurrentDefaultB2BUnitUtil = jnjGetCurrentDefaultB2BUnitUtil;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}
	public void setJnjGTB2BUnitService(final JnjGTB2BUnitService jnjGTB2BUnitService) {
		this.jnjGTB2BUnitService = jnjGTB2BUnitService;
	}
	public JnjLatamCommonFacadeUtilImpl getJnjLatamCommonFacadeUtil() {
		return jnjLatamCommonFacadeUtil;
	}
	public void setJnjLatamCommonFacadeUtil(final JnjLatamCommonFacadeUtilImpl jnjLatamCommonFacadeUtil) {
		this.jnjLatamCommonFacadeUtil = jnjLatamCommonFacadeUtil;
	}
	
	public JnjGTPacHiveConfigurationService getJnjGTPacHiveConfigurationService()
	{
		return jnjGTPacHiveConfigurationService;
	}

	public void setJnjGTPacHiveConfigurationService(final JnjGTPacHiveConfigurationService jnjGTPacHiveConfigurationService)
	{
		this.jnjGTPacHiveConfigurationService = jnjGTPacHiveConfigurationService;
	}
}
