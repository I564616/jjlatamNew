/*
 * Copyright: Copyright © 2023
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.b2b.jnjglobalreports.download.utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Locale;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.springframework.context.MessageSource;

import com.jnj.la.b2b.jnjglobalreports.forms.JnjLaOpenOrdersReportForm;
import com.jnj.facades.data.JnjLaOpenOrdersReportReponseData;
import com.jnj.la.browseandsearch.constants.JnjlabrowseandsearchaddonConstants;
import com.jnj.la.b2b.jnjglobalreports.download.constants.JnjlaglobalreportsaddonXLSPDFConstants;

import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;

/**
 * This class will be use to populate open orders report excel and pdf data.
 *
 */
public class OpenOrderReportUtil {
	private static final Logger LOG = LoggerFactory.getLogger(OpenOrderReportUtil.class);
	
	private String soldToColShow = StringUtils.EMPTY;
	private String soldToColNameShow = StringUtils.EMPTY;
	private String orderReferenceColShow = StringUtils.EMPTY;
	private String eanColShow = StringUtils.EMPTY;
	private String subFranchiseColShow = StringUtils.EMPTY;
	private String totalUnitsQuantityColShow = StringUtils.EMPTY;
	private String uomColShow = StringUtils.EMPTY;
	private String totalQtySalesUnitColShow = StringUtils.EMPTY;
	private String openOrderColShow = StringUtils.EMPTY;
	private String orderCreationDateColShow = StringUtils.EMPTY;
	private String dateReqDateColShow = StringUtils.EMPTY;
	private static final String ACCOUNT = "openorders.report.excel.download.account";
	private static final String ORDER_TYPE = "reports.openItem.columns.OrderType";
	private static final String SHIP_TO = "reports.openItem.columns.ShipTo";
	private static final String SHIP_TO_NAME = "reports.openItem.columns.ShipToName";
	private static final String JNJ_ORDER_NUMBER = "reports.openItem.columns.JnJOrderNumber";
	private static final String LINE_NUMBER = "reports.openItem.columns.LineNumber";
	private static final String SCHEDULE_LINE = "reports.openItem.columns.scheduleLine";
	private static final String PRODUCT_CODE = "reports.openItem.columns.ProductCode";
	private static final String PRODUCT_DESCRIPTION = "reports.openItem.columns.ProductDescription";
	private static final String ESTIMATED_DELIVERY_DATE = "reports.openItem.columns.EstimatedDeliveryDate";
	private static final String SOLD_TO = "reports.openItem.columns.SoldTo";
	private static final String SOLD_TO_NAME = "reports.openItem.columns.SoldToName";
	private static final String ORDER_REFERENCE = "reports.openItem.columns.OrderReference";
	private static final String EAN_1 = "reports.openItem.columns.EAN";
	private static final String SUB_FRANCHISE = "reports.openItem.columns.SubFranchise";
	private static final String TOTAL_UNITS_QUANTITY = "reports.openItem.columns.TotalUnitsQuantity";
	private static final String UNIT_OF_MEASURE = "reports.openItem.columns.UnitOfMeasure";
	private static final String ORDER_QTY_SALES_UNITS = "reports.openItem.columns.OrderQuantitySalesUnits";
	private static final String OPEN_ORDER = "reports.openItem.columns.OpenOrder";
	private static final String ORDER_CREATION_DATE = "reports.openItem.columns.OrderCreationDate";
	private static final String DATE_REQ_DATE = "reports.openItem.columns.DateRequestedDate";
	private static final String DOWNLOAD_DATE = "openorders.reports.download.date";
	private static final String MULTIPLE = "openorders.excel.multiple";
	private static final String SEARCH_CRITERIA = "openorders.excel.search.criteria";
	private static final String START = "openorders.reports.date.start";
	private static final String END = "openorders.reports.date.end";
	private static final String DOC_DETAILS = "openorders.report.document.details";
	private static final String DOC_NAME = "openorders.report.document.name";
	private static final String SHEET_NAME = "report.type.open.orders";

	private MessageSource messageSource;
	protected I18NService i18nService;
	protected ConfigurationService configurationService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	private BaseSiteService baseSiteService;

	public VelocityContext populateOpenOrdersReportData(final JnjLaOpenOrdersReportForm searchCriteria,
			final List<JnjLaOpenOrdersReportReponseData> jnjLaOpenOrdersReportResponseDataList,
			final String siteLogoPath, final String openOrdersReportExcelOrPdf) {

		final VelocityContext context = new VelocityContext();
		Locale currentLocale = i18nService.getCurrentLocale();

		Date date = new Date();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(getConfigurationService().getConfiguration()
				.getString(JnjlaglobalreportsaddonXLSPDFConstants.DATE_FORMAT,"dd.MM.yyyy, HH:mm:ss z"));

		final String currentDate = dateFormat.format(date);

		try {
			final SimpleDateFormat dtFormat = new SimpleDateFormat(JnjlaglobalreportsaddonXLSPDFConstants.DT_FORMAT);
			Date startDate = dtFormat.parse(searchCriteria.getFromDate());
			Date endDate = dtFormat.parse(searchCriteria.getToDate());
			searchCriteria.setFromDate(dtFormat.format(startDate));
			searchCriteria.setToDate(dtFormat.format(endDate));
		} catch (ParseException exe) {
			LOG.error("Error while parsing date", exe);
		}

		context.put(JnjlaglobalreportsaddonXLSPDFConstants.DATE1, currentDate);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SITE_LOGO_PATH_1, siteLogoPath);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SEARCH_CRITERIA_1, searchCriteria);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.OPEN_ORDERS_EXCEL_PDF, openOrdersReportExcelOrPdf);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.DOC_NAME,
				messageSource.getMessage(DOC_NAME, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.DOC_DETAILS,
				messageSource.getMessage(DOC_DETAILS, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SHEET_NAME_1,
				messageSource.getMessage(SHEET_NAME, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.DOWNLOAD_DATE,
				messageSource.getMessage(DOWNLOAD_DATE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.MULTIPLE_ACCOUNTS,
				messageSource.getMessage(MULTIPLE, null, currentLocale));

		List<String> accountIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(searchCriteria.getAccountIds())) {
			populateReportData(searchCriteria, context, accountIds);
		}
		String[] selectedReportColumns = searchCriteria.getReportColumns()
				.split(",");
		for (String reportColumn : selectedReportColumns) {
			populateShowData(reportColumn);
		}
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SOLDTO_COL_VM, soldToColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SOLDTONAME_COL_VM, soldToColNameShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ORDERREFERENCE_COL_VM, orderReferenceColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.EAN_COL_VM, eanColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SUBFRANCHISE_COL_VM, subFranchiseColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.TOTALUNITSQUANTITY_COL_VM, totalUnitsQuantityColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.UNITOFMEASURE_COL_VM, uomColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ORDERQTYSALESUNITS_COL_VM, totalQtySalesUnitColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.OPENORDER_COL_VM, openOrderColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ORDERCREATIONDATE_COL_VM, orderCreationDateColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.DATEREQDATE_COL_VM, dateReqDateColShow);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.JNJ_OPEN_ORDER_REPORT_RESPONSE_DATA,
				jnjLaOpenOrdersReportResponseDataList);
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.START1,
				messageSource.getMessage(START, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.END1,
				messageSource.getMessage(END, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SEARCH_CRITERIA,
				messageSource.getMessage(SEARCH_CRITERIA, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ACCOUNT1,
				messageSource.getMessage(ACCOUNT, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ORDER_TYPE,
				messageSource.getMessage(ORDER_TYPE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SHIP_TO,
				messageSource.getMessage(SHIP_TO, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SHIP_TO_NAME,
				messageSource.getMessage(SHIP_TO_NAME, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.JNJ_ORDER_NUMBER,
				messageSource.getMessage(JNJ_ORDER_NUMBER, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.LINE_NUMBER,
				messageSource.getMessage(LINE_NUMBER, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SCHEDULE_LINE,
				messageSource.getMessage(SCHEDULE_LINE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.PRODUCT_CODE,
				messageSource.getMessage(PRODUCT_CODE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.PRODUCT_DESCRIPTION,
				messageSource.getMessage(PRODUCT_DESCRIPTION, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ESTIMATED_DELIVERY_DATE,
				messageSource.getMessage(ESTIMATED_DELIVERY_DATE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SOLD_TO,
				messageSource.getMessage(SOLD_TO, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SOLD_TO_NAME,
				messageSource.getMessage(SOLD_TO_NAME, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ORDER_REFERENCE,
				messageSource.getMessage(ORDER_REFERENCE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.EAN,
				messageSource.getMessage(EAN_1, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.SUB_FRANCHISE,
				messageSource.getMessage(SUB_FRANCHISE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.TOTAL_UNITS_QUANTITY,
				messageSource.getMessage(TOTAL_UNITS_QUANTITY, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.UNIT_OF_MEASURE,
				messageSource.getMessage(UNIT_OF_MEASURE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ORDER_QTY_SALES_UNITS,
				messageSource.getMessage(ORDER_QTY_SALES_UNITS, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.OPEN_ORDER,
				messageSource.getMessage(OPEN_ORDER, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.ORDER_CREATION_DATE,
				messageSource.getMessage(ORDER_CREATION_DATE, null, currentLocale));
		context.put(JnjlaglobalreportsaddonXLSPDFConstants.DATE_REQ_DATE,
				messageSource.getMessage(DATE_REQ_DATE, null, currentLocale));
		context.put(JnjlabrowseandsearchaddonConstants.PDP.MEDIA_BASE_URL_PROPERTY,
				siteBaseUrlResolutionService.getMediaUrlForSite(baseSiteService.getCurrentBaseSite(), true));

		return context;
	}

	private void populateShowData(final String reportColumn) {

		populateSoldToShowData(reportColumn);

		if (JnjlaglobalreportsaddonXLSPDFConstants.ORDERREFERENCE_COL_VAL.equalsIgnoreCase(reportColumn)) {
			orderReferenceColShow = "Y";
		} 
		if (JnjlaglobalreportsaddonXLSPDFConstants.EAN_COL_VAL.equalsIgnoreCase(reportColumn)) {
			eanColShow = "Y";
		} 
		if (JnjlaglobalreportsaddonXLSPDFConstants.SUBFRANCHISE_COL_VAL.equalsIgnoreCase(reportColumn)) {
			subFranchiseColShow = "Y";
		} 
		if (JnjlaglobalreportsaddonXLSPDFConstants.TOTALUNITSQUANTITY_COL_VAL.equalsIgnoreCase(reportColumn)) {
			totalUnitsQuantityColShow = "Y";
		} 
		if (JnjlaglobalreportsaddonXLSPDFConstants.UNITOFMEASURE_COL_VAL.equalsIgnoreCase(reportColumn)) {
			uomColShow = "Y";
		} 
		if (JnjlaglobalreportsaddonXLSPDFConstants.ORDERQTYSALESUNITS_COL_VAL.equalsIgnoreCase(reportColumn)) {
			totalQtySalesUnitColShow = "Y";
		} 
		if (JnjlaglobalreportsaddonXLSPDFConstants.OPENORDER_COL_VAL.equalsIgnoreCase(reportColumn)) {
			openOrderColShow = "Y";
		} 
		if (JnjlaglobalreportsaddonXLSPDFConstants.ORDERCREATIONDATE_COL_VAL.equalsIgnoreCase(reportColumn)) {
			orderCreationDateColShow = "Y";
		} 
		if (JnjlaglobalreportsaddonXLSPDFConstants.DATEREQDATE_COL_VAL.equalsIgnoreCase(reportColumn)) {
			dateReqDateColShow = "Y";
		}
	}

	private void populateSoldToShowData(final String reportColumn) {
		if (JnjlaglobalreportsaddonXLSPDFConstants.SOLD_TO_COL_VAL.equalsIgnoreCase(reportColumn)) {
			soldToColShow = "Y";
		}
		if (JnjlaglobalreportsaddonXLSPDFConstants.SOLDTONAME_COL_VAL.equalsIgnoreCase(reportColumn)) {
			soldToColNameShow = "Y";
		}
	}

	private void populateReportData(final JnjLaOpenOrdersReportForm searchCriteria, VelocityContext context,
									List<String> accountIds) {
		if (searchCriteria.getAccountIds().size() > 1) {
			for (String accountIdSelected : searchCriteria.getAccountIds()) {
				accountIds.add(accountIdSelected);
			}

			context.put(JnjlaglobalreportsaddonXLSPDFConstants.SELECTED_ACCOUNTS, accountIds);
		} else if (searchCriteria.getAccountIds().size() == 1) {
			for (String accountIdSelected : searchCriteria.getAccountIds()) {
				accountIds.add(accountIdSelected);
				context.put(JnjlaglobalreportsaddonXLSPDFConstants.SELECTED_ACCOUNTS, accountIds);
			}
		}
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public void setI18nService(final I18NService i18nService) {
		this.i18nService = i18nService;
	}

	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService) {
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}
	
	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}
}
