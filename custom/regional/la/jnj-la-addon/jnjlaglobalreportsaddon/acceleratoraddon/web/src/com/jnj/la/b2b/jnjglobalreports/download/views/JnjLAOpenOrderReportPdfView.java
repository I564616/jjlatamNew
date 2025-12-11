/*
 * Copyright: Copyright © 2023
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.b2b.jnjglobalreports.download.views;

import com.lowagie.text.Chunk;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import com.jnj.la.b2b.jnjglobalreports.download.constants.JnjlaglobalreportsaddonXLSPDFConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.html.simpleparser.HTMLWorker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import com.jnj.la.b2b.jnjglobalreports.forms.JnjLaOpenOrdersReportForm;
import com.jnj.facades.data.JnjLaOpenOrdersReportReponseData;
import com.jnj.la.b2b.jnjglobalreports.download.utils.OpenOrderReportUtil;

import java.util.Date;

/**
 * This class will be used to populate the OpenOrderReport PDF.
 * 
 */
public class JnjLAOpenOrderReportPdfView extends AbstractPdfView {

	private static final Logger LOG = LoggerFactory.getLogger(JnjLAOpenOrderReportPdfView.class);

	private static final int MARGIN = 40;
	protected ConfigurationService configurationService;
	protected I18NService i18nService;
	private OpenOrderReportUtil openOrderReportUtil;

	@Override
	protected Document newDocument() {
		return new Document(PageSize.A3.rotate());
	}

	@Override
	protected void buildPdfDocument(final Map<String, Object> dataMap,
			final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse)
			throws Exception {
		final String METHOD_NAME = "buildPdfDocument()";
		final List<JnjLaOpenOrdersReportReponseData> jnjLaOpenOrdersReportResponseDataList =
				(List<JnjLaOpenOrdersReportReponseData>) dataMap.get(
						JnjlaglobalreportsaddonXLSPDFConstants.OPEN_ORDERS_RESPONSE_DATA_LIST);
		final JnjLaOpenOrdersReportForm searchCriteria = (JnjLaOpenOrdersReportForm) dataMap
				.get(JnjlaglobalreportsaddonXLSPDFConstants.OPEN_ORDERS_REPORT_FORM_NAME);
		
		String siteLogoPath1 = (String) dataMap.get("siteLogoPath");
		String openOrderReportPdf = "OpenOrdersPdf";

		final String htmlStringForPDF = generateVMForOpenOrderReport(
				searchCriteria, jnjLaOpenOrdersReportResponseDataList,
				siteLogoPath1, openOrderReportPdf);
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename="
				+ JnjlaglobalreportsaddonXLSPDFConstants.FILE_NAME);

		MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());
		pdfWriter.setPageEvent(events);
		events.onOpenDocument(pdfWriter, document);

		try {
			final File file = File.createTempFile("certificate", ".pdf");
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			document.add(new Chunk(""));
			final HTMLWorker htmlWorker = new HTMLWorker(document);
			htmlWorker.parse(new StringReader(htmlStringForPDF));
			document.close();
		} catch (final Exception exception) {
			LOG.error(METHOD_NAME + Logging.HYPHEN + "Exception in creating PDF :: ", exception);
		}
	}

	private String generateVMForOpenOrderReport(final JnjLaOpenOrdersReportForm searchCriteria,
			final List<JnjLaOpenOrdersReportReponseData> jnjLaOpenOrdersReportResponseDataList,
			final String siteLogoPath, String openOrderReportPdf) {

		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");

		LOG.info("Getting the vm file..");
		final String path = getClass().getResource(getConfigurationService().getConfiguration().getString(
										JnjlaglobalreportsaddonXLSPDFConstants.OPEN_ORDER_REPORT_VM_PATH,
										"/jnjlaglobalreportsaddon/vm/openOrderReport_la.vm")).getPath();
		
		velocityEngine.setProperty("file.resource.loader.path", path.substring(0, path.lastIndexOf('/')));
		velocityEngine.setProperty("file.resource.loader.cache", Boolean.TRUE);
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");
		
		velocityEngine.init();		
		final Template template = velocityEngine.getTemplate(
				JnjlaglobalreportsaddonXLSPDFConstants.OPEN_ORDER_REPORT_EXCEL_VM);

		final VelocityContext context = openOrderReportUtil.populateOpenOrdersReportData(searchCriteria,
						jnjLaOpenOrdersReportResponseDataList, siteLogoPath, openOrderReportPdf);

		final StringWriter writer = new StringWriter();
		template.merge(context, writer);

		return String.valueOf(writer);
	}

	public static class MyPageEvents extends PdfPageEventHelper {
		private MessageSourceAccessor messageSourceAccessor;
		private PdfContentByte cb;
		private PdfTemplate template;
		private BaseFont bf = null;

		public MyPageEvents(MessageSourceAccessor messageSourceAccessor) {
			this.messageSourceAccessor = messageSourceAccessor;
		}

		@Override
		public void onOpenDocument(PdfWriter writer, Document document) {
			try {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb = writer.getDirectContent();
				template = cb.createTemplate(50, 50);
			} catch (DocumentException de) {
				LOG.error("Error in onOpenDocument method DocumentException ", de);
			} catch (IOException ioe) {
				LOG.error("Error in onOpenDocument method DocumentException ", ioe);
			}
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			int pageN = writer.getPageNumber() - 1;
			String text = messageSourceAccessor.getMessage("Page", "Page")
					+ " " + pageN + " "
					+ messageSourceAccessor.getMessage("of", "of") + " ";
			float len = bf.getWidthPoint(text, 8);
			cb.beginText();
			cb.setFontAndSize(bf, 8);
			cb.setTextMatrix(MARGIN, 16);
			cb.showText(text);
			cb.endText();
			cb.addTemplate(template, MARGIN + len, 16);
			cb.beginText();
			cb.setFontAndSize(bf, 8);
			cb.endText();

			String finalFileName = JnjlaglobalreportsaddonXLSPDFConstants.SHEET_REPORT_NAME;
			String fileName = messageSourceAccessor.getMessage("Page",
					finalFileName);
			cb.beginText();
			cb.setTextMatrix(575, 16);
			cb.showText(fileName);
			cb.endText();
			if (pageN > 1) {
				Date date = new Date();
				final SimpleDateFormat dateFormat = new SimpleDateFormat(
						Config.getParameter(JnjlaglobalreportsaddonXLSPDFConstants.DATE_FORMAT));
				String currentDate = dateFormat.format(date);
				String finalCurrentDate = messageSourceAccessor.getMessage(
						"Page", currentDate);
				cb.beginText();
				cb.setTextMatrix(1065, 16);
				cb.showText(finalCurrentDate);
				cb.endText();
			}
		}

		@Override
		public void onCloseDocument(PdfWriter writer, Document document) {
			template.beginText();
			template.setFontAndSize(bf, 8);
			template.showText(String.valueOf(writer.getPageNumber() - 2));
			template.endText();
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

	public void setOpenOrderReportUtil(final OpenOrderReportUtil openOrderReportUtil) {
		this.openOrderReportUtil = openOrderReportUtil;
	}

}
