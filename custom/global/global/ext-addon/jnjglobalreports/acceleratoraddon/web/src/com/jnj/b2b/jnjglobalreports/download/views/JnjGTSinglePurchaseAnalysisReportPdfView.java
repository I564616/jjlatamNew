/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.download.views;

import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTSinglePurchaseOrderReportResponseData;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTSinglePurchaseAnalysisReportForm;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.servicelayer.i18n.I18NService;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.awt.Color;
import java.io.IOException;

import org.apache.commons.io.IOUtils;


/**
 * This class handles the PDF download for single purchase analysis report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTSinglePurchaseAnalysisReportPdfView extends AbstractPdfView
{
	
	@Autowired
	protected I18NService i18nService;
	private MessageSource messageSource;
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	
	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
	
	private static final Logger LOG = Logger.getLogger(JnjGTSinglePurchaseAnalysisReportPdfView.class);
	private static final String SINGLE_PURCHASE_ANALYSIS_RESPONSE_DATA_MAP = "jnjGTSinglePurchaseOrderReportResponseDataMap";
	private static final String SINGLE_PURCHASE_ANALYSIS_FORM_NAME = "jnjGTSinglePurchaseAnalysisReportForm";
	private static final String sheetName = "SINGLE PURCHASE ANALYSIS REPORT SEARCH CRITERIA";
	private static final String sheetName1 = "SINGLE PURCHASE ANALYSIS REPORT RESULTS";
	private static final String sheetName2 = "SINGLE PURCHASE ANALYSIS SEARCH";

	private static final int MARGIN = 32;
	
	private static final String ACCOUNT = "reports.purchase.analysis.account"; 
	private static final String PRODUCT_CODE = "reports.purchase.analysis.productcode"; 
	private static final String BREAKDOWN = "reports.purchase.analysis.period.breakdown"; 
	private static final String STARTDATE = "reports.purchase.analysis.startdate"; 
	private static final String ENDDATE = "reports.purchase.analysis.enddate"; 
	private static final String ORDERFROM = "reports.purchase.analysis.orderdFrom"; 
	private static final String LOTNUM = "reports.purchase.analysis.lotNum";
	private static final String ACCOUNTNUMBER = "reports.purchase.analysis.account.number"; 
	private static final String PERIOD = "reports.purchase.analysis.period"; 
	private static final String AMOUNTELECTRONIC = "reports.purchase.analysis.amountElectronic"; 
	private static final String AMOUNTOTHER = "reports.purchase.analysis.amountOther"; 
	private static final String QTYELECTRONIC = "reports.purchase.analysis.QuantityElectronic"; 
	private static final String QTYOTHER = "reports.purchase.analysis.QuantityOther"; 
	private static final String QTYUNIT = "reports.purchase.analysis.QuantityUnit";
	private static final String FREQUENCYELCTRONIC = "reports.purchase.analysis.FrequencyElectronic"; 
	private static final String FREQUENCYOTHER = "reports.purchase.analysis.FrequencyOther";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	
	
	
	@Override
	protected Document newDocument()
	{
		return new Document(PageSize.A4.rotate());
	}

	/**
	 * This method generates the PDF doc
	 */
	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
	{
		try
		{
			arg4.setHeader("Content-Disposition", "attachment; filename=Single_Purchase_Analysis_Report.pdf");
			final JnjGTSinglePurchaseAnalysisReportForm formData = (JnjGTSinglePurchaseAnalysisReportForm) map
					.get(SINGLE_PURCHASE_ANALYSIS_FORM_NAME);
			final TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData> dataMap = (TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>) map
					.get(SINGLE_PURCHASE_ANALYSIS_RESPONSE_DATA_MAP);
			final SimpleDateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());//Modified by Archana for AAOL-5513
			final PdfPTable searchCriteriaTable = new PdfPTable(7);
			searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			searchCriteriaTable.setTotalWidth(822F);
			searchCriteriaTable.setLockedWidth(true);
			searchCriteriaTable.setSpacingAfter(30f);

			final PdfPCell headerResultsCell = new PdfPCell();
			headerResultsCell.setPhrase(new Phrase(sheetName,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			headerResultsCell.setColspan(7);
			searchCriteriaTable.addCell(headerResultsCell);
			
			
			searchCriteriaTable.addCell(messageSource.getMessage(ACCOUNT, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(BREAKDOWN, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(STARTDATE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ENDDATE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ORDERFROM, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(LOTNUM, null, i18nService.getCurrentLocale()));
			
			/*final PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			titleTable.setTotalWidth(822F);
			titleTable.setLockedWidth(true);
			titleTable.setSpacingAfter(30f);
			
			final PdfPCell headerCell = new PdfPCell();
			headerResultsCell.setPhrase(new Phrase(sheetName2,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));

			headerCell.setColspan(11);
			titleTable.addCell(headerCell);*/
			
			// page number add start here
			 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
			 pdfWriter.setPageEvent(events);   
	               events.onOpenDocument(pdfWriter, document);
		   
		   //page number add end here

			//image adding start 
		      	final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
		      	final InputStream jnjConnectLogoIS2 = (InputStream) map.get("jnjConnectLogoURL2");
		      	byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
		      	byte[] jnjConnectLogoByteArray2 = IOUtils.toByteArray(jnjConnectLogoIS2);
		      	Image jnjConnectLogo = null;
		      	Image jnjConnectLogo2 = null;
		      	PdfPCell cell1 = null;
		      	PdfPCell cell2 = null;
		      	PdfPTable imageTable = new PdfPTable(2); // 3 columns.
		      	imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		      	imageTable.setTotalWidth(822F);
		      	imageTable.setLockedWidth(true);
		      	imageTable.setSpacingAfter(30f);
		      	
		      	if ( jnjConnectLogoByteArray != null){
		      		jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
		      		cell1 = new PdfPCell(jnjConnectLogo, false);
		      		cell1.setBorder(Rectangle.NO_BORDER);
		      	}
		      	
		      	if ( jnjConnectLogoByteArray2 != null){
		      		jnjConnectLogo2 = Image.getInstance(jnjConnectLogoByteArray2);
		      		cell2 = new PdfPCell(jnjConnectLogo2, false);
		      		cell2.setBorder(Rectangle.NO_BORDER);
		      	}
		      	imageTable.addCell(cell1);
		      	imageTable.addCell(cell2);
		      	//image adding end

			if (null != formData)
			{
				searchCriteriaTable.addCell(formData.getAccountIds());
				searchCriteriaTable.addCell(formData.getProductCode());
				searchCriteriaTable.addCell(formData.getPeriodBreakdown());
				searchCriteriaTable.addCell(formData.getStartDate());
				searchCriteriaTable.addCell(formData.getEndDate());
				searchCriteriaTable.addCell(formData.getOrderedFrom());
				searchCriteriaTable.addCell(formData.getLotNumber());
			}

			final PdfPTable sinlgePurchaseAnalysisTable = new PdfPTable(9);
			sinlgePurchaseAnalysisTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			sinlgePurchaseAnalysisTable.setTotalWidth(822F);
			sinlgePurchaseAnalysisTable.setLockedWidth(true);
			sinlgePurchaseAnalysisTable.setSpacingAfter(736f);
			final PdfPCell resultsCell = new PdfPCell();
			resultsCell.setPhrase(new Phrase(sheetName1,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			resultsCell.setColspan(9);
			sinlgePurchaseAnalysisTable.addCell(resultsCell);
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(ACCOUNTNUMBER, null, i18nService.getCurrentLocale()));
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(PERIOD, null, i18nService.getCurrentLocale()));
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(AMOUNTELECTRONIC, null, i18nService.getCurrentLocale()));
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(AMOUNTOTHER, null, i18nService.getCurrentLocale()));
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(QTYELECTRONIC, null, i18nService.getCurrentLocale()));
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(QTYOTHER, null, i18nService.getCurrentLocale()));
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(QTYUNIT, null, i18nService.getCurrentLocale()));
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(FREQUENCYELCTRONIC, null, i18nService.getCurrentLocale()));
			sinlgePurchaseAnalysisTable.addCell(messageSource.getMessage(FREQUENCYOTHER, null, i18nService.getCurrentLocale()));
			
			

			final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);

			if (null != dataMap)
			{
				for (final String key : dataMap.keySet())
				{
					sinlgePurchaseAnalysisTable.addCell(dataMap.get(key).getAccountNumber() == null ? pdfCell : new PdfPCell(
							new Phrase(dataMap.get(key).getAccountNumber())));
					sinlgePurchaseAnalysisTable
							.addCell((dataMap.get(key).getPeriodFrom() == null && dataMap.get(key).getPeriodTo() == null) ? pdfCell
									: new PdfPCell(new Phrase(dataMap.get(key).getPeriodFrom() + Jnjb2bCoreConstants.SPACE
											+ dataMap.get(key).getPeriodTo())));
					sinlgePurchaseAnalysisTable.addCell(dataMap.get(key).getAmountElectronic() == null ? pdfCell : new PdfPCell(
							new Phrase(dataMap.get(key).getAmountElectronic().getFormattedValue())));
					sinlgePurchaseAnalysisTable.addCell(dataMap.get(key).getAmountOther() == null ? pdfCell : new PdfPCell(new Phrase(
							dataMap.get(key).getAmountOther().getFormattedValue())));
					sinlgePurchaseAnalysisTable.addCell(dataMap.get(key).getQuantityElectronic() == null ? pdfCell : new PdfPCell(
							new Phrase(String.valueOf(dataMap.get(key).getQuantityElectronic()))));
					sinlgePurchaseAnalysisTable.addCell(dataMap.get(key).getQuantityOther() == null ? pdfCell : new PdfPCell(
							new Phrase(String.valueOf(dataMap.get(key).getQuantityOther()))));
					sinlgePurchaseAnalysisTable.addCell(dataMap.get(key).getQuantityUnit() == null ? pdfCell : new PdfPCell(
							new Phrase(dataMap.get(key).getQuantityUnit())));
					sinlgePurchaseAnalysisTable.addCell(new PdfPCell(new Phrase(String.valueOf(dataMap.get(key)
							.getFrequencyElectronic()))));
					sinlgePurchaseAnalysisTable
							.addCell(new PdfPCell(new Phrase(String.valueOf(dataMap.get(key).getFrequencyOther()))));
				}
			}
			document.add(imageTable);
//			document.add(titleTable);
			document.add(searchCriteriaTable);
			document.add(sinlgePurchaseAnalysisTable);
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating PDF - " + exception.getMessage());
		}
	}
	  //~ Inner Classes ----------------------------------------------------------   
	   
	   private static class MyPageEvents extends PdfPageEventHelper {   
	  	 	
	       private MessageSourceAccessor messageSourceAccessor;   
	  
	       // This is the PdfContentByte object of the writer   
	       private PdfContentByte cb;   
	  
	       // We will put the final number of pages in a template   
	       private PdfTemplate template;   
	  
	       // This is the BaseFont we are going to use for the header / footer   
	       private BaseFont bf = null;   
	          
	       public MyPageEvents(MessageSourceAccessor messageSourceAccessor) {   
	           this.messageSourceAccessor = messageSourceAccessor;   
	       }
	  
	       // we override the onOpenDocument method   
	       public void onOpenDocument(PdfWriter writer, Document document) {   
	           try {   
	               bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );   
	               cb = writer.getDirectContent();   
	               template = cb.createTemplate(50, 50);   
	           } catch (DocumentException de) {   
	           } catch (IOException ioe) {}   
	       } 
	  
	       // we override the onEndPage method   
	       public void onEndPage(PdfWriter writer, Document document) {   
	           int pageN = writer.getPageNumber();   
	           String text = messageSourceAccessor.getMessage("page", "page") + " " + pageN + " " +   
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
	       }  
	  
	       // we override the onCloseDocument method   
	       public void onCloseDocument(PdfWriter writer, Document document) {   
	           template.beginText();   
	           template.setFontAndSize(bf, 8);   
	           template.showText(String.valueOf( writer.getPageNumber() - 1 ));   
	           template.endText();   
	       }  
	   }  
}
