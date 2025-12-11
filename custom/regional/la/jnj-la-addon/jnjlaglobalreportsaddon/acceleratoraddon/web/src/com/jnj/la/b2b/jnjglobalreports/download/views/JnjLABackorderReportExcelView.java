/*
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.b2b.jnjglobalreports.download.views;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.la.b2b.jnjglobalreports.download.utils.BackOrderReportUtils;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
/**
 * Class responsible to create Excel view for Back Order Report.
 * 
 */
public class JnjLABackorderReportExcelView extends AbstractXlsView {
	private static final String BACK_ORDER_REPORT_VM_PATH = "la.backorder.report.vm.path";
	private static final String BACKORDER_RESPONSE_DATA_LIST = "jnjGTBackorderReportResponseDataList";
	private static final String BACKORDER_FORM_NAME = "jnjGTBackorderReportForm";
	private static final String ORDER_DETAIL_EXCEL_VM = "backOrderReport_la.vm";
	
	
	private static final Logger LOG = Logger.getLogger(JnjLABackorderReportExcelView.class);
	protected I18NService i18nService;
	private BackOrderReportUtils backOrderReportUtils;
	protected ConfigurationService configurationService;
	String finalFileName = "Open Item Report.xls";
	


	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		final String METHOD_NAME = "buildExcelDocument()";
		@SuppressWarnings("unchecked")
		final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = (List<JnjGTBackorderReportResponseData>) arg0
				.get(BACKORDER_RESPONSE_DATA_LIST);
		final JnjGTBackorderReportForm searchCriteria = (JnjGTBackorderReportForm) arg0.get(BACKORDER_FORM_NAME);
		final String accountsSelectedName = (String) arg0.get("reportAccountName");
		final String siteLogoPath1 = (String) arg0.get("siteLogoPathURL");
		final String currentAccountName = (String) arg0.get("currentAccountName");
		final String currentAccountId = (String) arg0.get("currentAccountId");
		final String backOrderExcel = "OpenItemExcel";
		final String htmlStringForExcel = generateVMForOrderHistory(searchCriteria,jnjGTBackorderReportResponseDataList, siteLogoPath1, accountsSelectedName,currentAccountName,currentAccountId,backOrderExcel);
		try
		{
			ServletOutputStream outputStream = arg3.getOutputStream();
			arg3.setContentType("application/vnd.ms-excel");
			arg3.setCharacterEncoding("UTF-8");
			arg3.setHeader("Content-Disposition", "attachment; filename="+finalFileName);
			outputStream.write(htmlStringForExcel.getBytes()); 
			arg3.getOutputStream().flush();
		}
		catch (final Exception exception)
		{
			LOG.error(METHOD_NAME + Logging.HYPHEN + "Exception in creating PDF :: " + exception);
		}
		
	}

	protected String generateVMForOrderHistory(final JnjGTBackorderReportForm searchCriteria,final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList,
			final String siteLogoPath, final String accountsSelectedName,
			final String currentAccountName, final String currentAccountId,
			final String backOrderExcel) {
		final String METHOD_NAME = "generateVMForOrderHistory";

		try {
			final VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine
					.setProperty("file.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.FileResourceLoader");

			LOG.info("Getting the vm file..");

			final String path = getClass()
					.getResource(
							getConfigurationService()
									.getConfiguration()
									.getString(BACK_ORDER_REPORT_VM_PATH,
											"/jnjlaglobalreportsaddon/vm/backOrderReport_la.vm"))
					.getPath();
			LOG.info("Found Backorder report Path as:  " + path);

			velocityEngine.setProperty("file.resource.loader.path",
					path.substring(0, path.lastIndexOf('/')));
			LOG.info(Logging.HYPHEN + METHOD_NAME
					+ "file.resource.loader.path :  "
					+ path.substring(0, path.lastIndexOf('/')));
			velocityEngine.setProperty("file.resource.loader.cache",
					Boolean.TRUE);
			velocityEngine.setProperty(
					"file.resource.loader.modificationCheckInterval", "2");

			LOG.info(Logging.HYPHEN + METHOD_NAME
					+ "Initializing Velocity Engine");
			velocityEngine.init();

			LOG.info(Logging.HYPHEN + METHOD_NAME
					+ "Retrieving template from Velocity Engine");
			final Template template = velocityEngine
					.getTemplate(ORDER_DETAIL_EXCEL_VM);
			LOG.info("path" + template);
			final VelocityContext context = backOrderReportUtils
					.populateBackorderReportData(searchCriteria,
							jnjGTBackorderReportResponseDataList, siteLogoPath,
							accountsSelectedName, currentAccountName,
							currentAccountId, backOrderExcel);

			final StringWriter writer = new StringWriter();
			template.merge(context, writer);
			return String.valueOf(writer);
		} catch (final Exception exception) {
			LOG.error(METHOD_NAME + Logging.HYPHEN
					+ "Exception in retreiving VM file :: " + exception);			
		}
		return null;
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
	public BackOrderReportUtils getBackOrderReportUtils() {
		return backOrderReportUtils;
	}

	public void setBackOrderReportUtils(final BackOrderReportUtils backOrderReportUtils) {
		this.backOrderReportUtils = backOrderReportUtils;
	}
	   
}
