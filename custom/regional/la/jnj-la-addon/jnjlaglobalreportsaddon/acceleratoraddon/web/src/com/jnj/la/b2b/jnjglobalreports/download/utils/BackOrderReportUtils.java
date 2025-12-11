/*
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.b2b.jnjglobalreports.download.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.springframework.context.MessageSource;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.la.browseandsearch.constants.JnjlabrowseandsearchaddonConstants;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;


import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;

public class BackOrderReportUtils {
	
	private static final String DATE_FORMAT="vm.backorder.date.format";
	private static final String SHEET_NAME = "Open Item Report";
	
	private static final String DOWNLOAD_DATE =  "backorder.reports.download.date";
	private static final String ACCOUNT =  "backorder.excel.download.account";
	private static final String ORDER_NUM =  "backorder.excel.download.orderNumber";
	private static final String CUSTOMER_PO =  "backorder.excel.download.cpo";
	private static final String ORDER_DATE =  "backorder.excel.download.orderDate";
	private static final String PRODUCT_CODE =  "backorder.excel.download.prodCode";
	private static final String PRODUCT_NAME =  "backorder.excel.download.prodName";
	private static final String QTY =  "backorder.excel.download.qty";
	private static final String UOM =  "backorder.excel.download.uom";
	private static final String UNIT_PRICE =  "backorder.excel.download.unitPrice";
	private static final String TOTAL_PRICE =  "backorder.excel.download.totalPrice";
	private static final String LINE_STATUS =  "backorder.excel.download.orderLineStatus";
	private static final String EDD =  "backorder.excel.download.edd";
	private static final String MULTIPLE =  "backorder.excel.multiple";
	private static final String SEARCH_CRITERIA =  "backorder.excel.search.criteria";
	private static final String START =  "backorder.reports.date.start";
	private static final String END =  "backorder.reports.date.end";
	private static final String DOC_DETAILS =  "backorder.report.document.details";
	private static final String DOC_NAME =  "backorder.report.document.name";
	private static final String DATE1 = "date";
	private static final String SITELOGOPATH1 = "siteLogoPath";
	private static final String SEARCHCRITERIA1 = "searchCriteria";
	private static final String BACKORDEREXCELORPDF = "backOrderExcelorPdf";
	private static final String DOCNAME = "docName";
	private static final String DOCDETAILS = "docDetails";
	private static final String SHEETNAME1 = "sheetName1";
	private static final String ACCOUNT1 = "account";
	private static final String DOWNLOADDATE = "downloadDate";
	private static final String SELECTEDACCOUNTS = "selectedAccounts";
	private static final String SELECTEDACCOUNTNAME = "selectedAccountName";
	private static final String CURRENTACCOUNTNAME1 = "currentAccountName";
	private static final String CURRENTACCOUNTID1 = "currentAccountId";
	private static final String JNJGTBACKORDERREPORTRESPONSEDATA = "jnjGTBackorderReportResponseData";
	private static final String SRART1 = "start";
	private static final String END1 = "end";
	private static final String SEARCHCRITERIA = "searchcriteria";
	private static final String ORDERNUM = "orderNum";
	private static final String CUSTOMERPO = "customerPO";
	private static final String ORDERDATE = "orderDate";
	private static final String PRODUCTCODE = "productCode";
	private static final String PRODUCTNAME = "productName";
	private static final String QTY1 = "qty";
	private static final String UOM1 = "uom";
	private static final String UNITPRICE = "unitPrice";
	private static final String TOTALPRICE = "totalPrice";
	private static final String LINESTATUS = "lineStatus";
	private static final String ESTDELDATE = "estDelDate";
	
	private MessageSource messageSource;
	protected I18NService i18nService;
	protected ConfigurationService configurationService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	private BaseSiteService baseSiteService;

	/**
	 * @param searchCriteria
	 * @param jnjGTBackorderReportResponseDataList
	 * @param siteLogoPath
	 * @param accountsSelectedName
	 * @param currentAccountName
	 * @param currentAccountId
	 * @param backOrderExcel
	 * @return
	 */
	public VelocityContext populateBackorderReportData(final JnjGTBackorderReportForm searchCriteria,
			final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList,final String siteLogoPath,
			final String accountsSelectedName,final String currentAccountName,final String currentAccountId,final String backOrderExcel) {
		final VelocityContext context = new VelocityContext();
		
		Date date = new Date();
		final SimpleDateFormat dateFormat= new SimpleDateFormat(getConfigurationService().getConfiguration().getString(DATE_FORMAT,"dd.MM.yyyy, HH:mm:ss z"));
		
		final String currentDate=dateFormat.format(date);
		context.put(DATE1, currentDate);
		context.put(SITELOGOPATH1, siteLogoPath);
		context.put(SEARCHCRITERIA1, searchCriteria);
		context.put(BACKORDEREXCELORPDF, backOrderExcel);
		context.put(DOCNAME, messageSource.getMessage(DOC_NAME, null, i18nService.getCurrentLocale()));
		context.put(DOCDETAILS, messageSource.getMessage(DOC_DETAILS, null, i18nService.getCurrentLocale()));
		context.put(SHEETNAME1, SHEET_NAME);
		context.put(ACCOUNT1, messageSource.getMessage(ACCOUNT, null, i18nService.getCurrentLocale()));
		context.put(DOWNLOADDATE, messageSource.getMessage(DOWNLOAD_DATE, null, i18nService.getCurrentLocale()));
		final String[] accounts=searchCriteria.getAccountIds().split(",");
		if(accounts.length > 1) {
			context.put(SELECTEDACCOUNTS, messageSource.getMessage(MULTIPLE, null, i18nService.getCurrentLocale()));
		}else {
			context.put(SELECTEDACCOUNTS, searchCriteria.getAccountIds());
			context.put(SELECTEDACCOUNTNAME, accountsSelectedName);
		}
		context.put(CURRENTACCOUNTNAME1, currentAccountName);
		context.put(CURRENTACCOUNTID1, currentAccountId);
		context.put(JNJGTBACKORDERREPORTRESPONSEDATA, jnjGTBackorderReportResponseDataList);
		context.put(SRART1, messageSource.getMessage(START, null, i18nService.getCurrentLocale()));
		context.put(END1, messageSource.getMessage(END, null, i18nService.getCurrentLocale()));
		context.put(SEARCHCRITERIA, messageSource.getMessage(SEARCH_CRITERIA, null, i18nService.getCurrentLocale()));
		context.put(ORDERNUM, messageSource.getMessage(ORDER_NUM, null, i18nService.getCurrentLocale()));
		context.put(CUSTOMERPO, messageSource.getMessage(CUSTOMER_PO, null, i18nService.getCurrentLocale()));
		context.put(ORDERDATE, messageSource.getMessage(ORDER_DATE, null, i18nService.getCurrentLocale()));
		context.put(PRODUCTCODE, messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
		context.put(PRODUCTNAME, messageSource.getMessage(PRODUCT_NAME, null, i18nService.getCurrentLocale()));
		context.put(QTY1, messageSource.getMessage(QTY, null, i18nService.getCurrentLocale()));
		context.put(UOM1, messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
		context.put(UNITPRICE, messageSource.getMessage(UNIT_PRICE, null, i18nService.getCurrentLocale()));
		context.put(TOTALPRICE, messageSource.getMessage(TOTAL_PRICE, null, i18nService.getCurrentLocale()));
		context.put(LINESTATUS, messageSource.getMessage(LINE_STATUS, null, i18nService.getCurrentLocale()));
		context.put(ESTDELDATE, messageSource.getMessage(EDD, null, i18nService.getCurrentLocale()));
		context.put(JnjlabrowseandsearchaddonConstants.PDP.MEDIA_BASE_URL_PROPERTY, siteBaseUrlResolutionService.getMediaUrlForSite(baseSiteService.getCurrentBaseSite(), true));
		return context;
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

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{

		return siteBaseUrlResolutionService;
	}

	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	protected  BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

}
