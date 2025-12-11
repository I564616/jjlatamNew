/*
 * Copyright: Copyright © 2020
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

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.apache.log4j.Logger;
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

import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.la.b2b.jnjglobalreports.download.utils.BackOrderReportUtils;

import java.util.Date;

/**
 * Class responsible to create Pdf view for Back Order Report.
 * 
 */
public class JnjLABackorderReportPdfView extends AbstractPdfView {

	private static final String BACK_ORDER_REPORT_VM_PATH = "la.backorder.report.vm.path";
	private static final String BACKORDER_RESPONSE_DATA_LIST = "jnjGTBackorderReportResponseDataList";
	private static final String BACKORDER_FORM_NAME = "jnjGTBackorderReportForm";
	private static final String DATE_FORMAT="vm.backorder.date.format";
	private static final String ORDER_DETAIL_PDF_VM = "backOrderReport_la.vm";
	private static final String SHEET_NAME = "Open Item Report";
	
	private static final String FILE_NAME = "Open_Item_Report.pdf";
	private static final Logger LOG = Logger.getLogger(JnjLABackorderReportPdfView.class);
	protected ConfigurationService configurationService;
	
	private static final int MARGIN = 40;
	
	protected I18NService i18nService;
	
	private BackOrderReportUtils backOrderReportUtils;

	@Override
	protected Document newDocument()
	{
		return new Document(PageSize.A3.rotate());
	} 

	
	@Override
	protected void buildPdfDocument(final Map<String, Object> arg0, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception {

		final String METHOD_NAME = "buildPdfDocument()";
		final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = (List<JnjGTBackorderReportResponseData>) arg0
				.get(BACKORDER_RESPONSE_DATA_LIST);
		final JnjGTBackorderReportForm searchCriteria = (JnjGTBackorderReportForm) arg0.get(BACKORDER_FORM_NAME);
		final String accountsSelectedName = (String) arg0.get("reportAccountName");
		String siteLogoPath1 = (String) arg0.get("siteLogoPath");
		String currentAccountName = (String) arg0.get("currentAccountName");
		String currentAccountId = (String) arg0.get("currentAccountId");
		String backOrderPdf = "OpenItemPdf";
		
		final String htmlStringForPDF = generateVMForOrderHistory(searchCriteria,jnjGTBackorderReportResponseDataList, siteLogoPath1, accountsSelectedName,currentAccountName,currentAccountId,backOrderPdf);
		arg4.setHeader("Content-Disposition", "attachment; filename="+FILE_NAME);
		// page number add start here
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
		}
		catch (final Exception exception) {
			LOG.error(METHOD_NAME + Logging.HYPHEN + "Exception in creating PDF :: " + exception);
		}
		
	}

	private String generateVMForOrderHistory(final JnjGTBackorderReportForm searchCriteria,final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList,
			final String siteLogoPath,final String accountsSelectedName,final String currentAccountName,final String currentAccountId,String backOrderPdf) { 
		final String METHOD_NAME = "generateVMForOrderHistory";

		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");

		LOG.info("Getting the vm file..");
		final String path = getClass().getResource(getConfigurationService().getConfiguration().getString(BACK_ORDER_REPORT_VM_PATH,"/jnjlaglobalreportsaddon/vm/backOrderReport_la.vm")).getPath();
		LOG.info("Found Backorder report PDF Path as:  " + path);
		velocityEngine.setProperty("file.resource.loader.path", path.substring(0, path.lastIndexOf('/')));
		LOG.info(Logging.HYPHEN + METHOD_NAME + "file.resource.loader.path :  " + path.substring(0, path.lastIndexOf('/')));
		velocityEngine.setProperty("file.resource.loader.cache", Boolean.TRUE);
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");

		LOG.info(Logging.HYPHEN + METHOD_NAME + "Initializing Velocity Engine");
		velocityEngine.init();

		LOG.info(Logging.HYPHEN + METHOD_NAME + "Retrieving template from Velocity Engine");
		final Template template = velocityEngine.getTemplate(ORDER_DETAIL_PDF_VM);

		final VelocityContext context = backOrderReportUtils.populateBackorderReportData(searchCriteria,
				jnjGTBackorderReportResponseDataList, siteLogoPath, accountsSelectedName, currentAccountName,
				currentAccountId, backOrderPdf);
		
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
	
	     
	    public void onOpenDocument(PdfWriter writer, Document document) {   
	        try {   
	            bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );   
	            cb = writer.getDirectContent();   
	            template = cb.createTemplate(50, 50);   
	        } catch (DocumentException de) { 
	        	LOG.error("Error in onOpenDocument method DocumentException ",de);
	        } catch (IOException ioe) {
	        	LOG.error("Error in onOpenDocument method DocumentException ",ioe);
	        }    
	    } 
	    
	     
	    public void onEndPage(PdfWriter writer, Document document) {  
	    	
	        int pageN = writer.getPageNumber()-1;   
	        String text = messageSourceAccessor.getMessage("Page", "Page") + " " + pageN + " " +   
	            messageSourceAccessor.getMessage("of", "of") + " ";   
	        float  len = bf.getWidthPoint( text, 8 );   
	       
	        cb.beginText();   
	        cb.setFontAndSize(bf, 8);   
	        cb.setTextMatrix(MARGIN, 16);   
	        cb.showText(text);   
	        cb.endText();   
	
	        cb.addTemplate(template, MARGIN + len, 16);   
	        cb.beginText();   
	        cb.setFontAndSize(bf, 8);   
	        cb.endText(); 
	        
	        String finalFileName = SHEET_NAME;
	        String fileName = messageSourceAccessor.getMessage("Page", finalFileName) ;
	        cb.beginText();
	        cb.setTextMatrix(575, 16);   
	        cb.showText(fileName);   
	        cb.endText(); 
	        if(pageN > 1) {
	        	Date date = new Date();
		        final SimpleDateFormat dateFormat= new SimpleDateFormat(Config.getParameter(DATE_FORMAT));
				String currentDate=dateFormat.format(date);
				String finalCurrentDate = messageSourceAccessor.getMessage("Page", currentDate) ;
		        cb.beginText();
		        cb.setTextMatrix(1065, 16);   
		        cb.showText(finalCurrentDate);   
		        cb.endText(); 
	        }
	    }  
	    
	
	   
	    public void onCloseDocument(PdfWriter writer, Document document) {   
	        template.beginText();   
	        template.setFontAndSize(bf, 8);   
	        template.showText(String.valueOf( writer.getPageNumber() - 2 ));   
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
	public BackOrderReportUtils getBackOrderReportUtils() {
		return backOrderReportUtils;
	}
	public void setBackOrderReportUtils(final BackOrderReportUtils backOrderReportUtils) {
		this.backOrderReportUtils = backOrderReportUtils;
	}

}
