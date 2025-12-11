/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.download.views;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;

import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTMultiPurchaseAnalysisReportForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTMultiPurchaseOrderReportResponseData;
import com.jnj.services.MessageService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;

import java.io.InputStream;
import java.awt.Color;
import java.io.IOException;









/**
 * This class handles the PDF download for multiple purchase analysis report
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTMultiPurchaseAnalysisReportPdfView extends AbstractPdfView
{
	private MessageService messageService;
	/** I18NService to retrieve the current locale. */

	private static final Logger LOG = Logger.getLogger(JnjGTMultiPurchaseAnalysisReportPdfView.class);
	private static final String MULTI_PURCHASE_ANALYSIS_RESPONSE_DATA_LIST = "jnjGTMultiPurchaseOrderReportResponseDataList";
	private static final String MULTI_PURCHASE_ANALYSIS_FORM_NAME = "jnjGlobalMultiPurchaseAnalysisReportForm";
	private static final String TOTAL_SPENDING_MAP = "totalSpendingMap";
	private static final String SEARCH_TOTAL = "searchTotal";
	private static final int MARGIN = 32;
	private static final String ACCOUNT = "multireports.account";
	private static final String TERRITORY = "multireports.territory";
	private static final String STARTDATE = "multireports.startdate";
	private static final String ENDDATE = "multireports.enddate";
	private static final String BRAND = "multireports.brand";
	private static final String SUBBRAND = "multireports.subbrand";
	private static final String ORDEREDFROM =  "multireports.orderedFrom";
	private static final String PRODUCTSDISPLAY = "multireports.ProductsToDisplay";
	private static final String ANALYSISVARIABLE = "multireports.Analysisvariable";
	private static final String SEARCHHEADER = "multireports.search.header";
	private static final String SEARCHRESULTS = "multireports.search.results";
	private static final String ACCOUNTNUMBER = "multireports.account.number";
	private static final String PRODUCTNAME = "multireports.product.name";
	private static final String PRODUCTCODE = "multireports.product.code";
	private static final String AMOUNT =  "multireports.product.amount";
	private static final String SPENDING = "multireports.spending";
	private static final String QUANTITY = "multireports.product.orderQuantity";
	private static final String FREQUENCY = "multireports.product.orderFrequency";
	private static final String WEIGHT = "multireports.product.order.weight";
	private static final String SPENT =  "multireports.product.total.spent";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	@Autowired
	protected I18NService i18nService;
	private MessageSource messageSource;
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	/**
	 * @return the jnjCommonUtil
	 */
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	/**
	 * @param jnjCommonUtil the jnjCommonUtil to set
	 */
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
	
	
	public MessageService getMessageService()
	{
		return messageService;
	}

	public void setMessageService(final MessageService messageService)
	{
		this.messageService = messageService;
	}
	
	

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
			arg4.setHeader("Content-Disposition", "attachment; filename=Multi_Purchase_Analysis_Report.pdf");
			final JnjGTMultiPurchaseAnalysisReportForm formData = (JnjGTMultiPurchaseAnalysisReportForm) map
					.get(MULTI_PURCHASE_ANALYSIS_FORM_NAME);
			final Map<String, Double> totalSpendingMap = (Map<String, Double>) map.get(TOTAL_SPENDING_MAP);
			final PriceData totalAmountSpend = (PriceData) map.get(SEARCH_TOTAL);
			final List<JnjGTMultiPurchaseOrderReportResponseData> dataList = (List<JnjGTMultiPurchaseOrderReportResponseData>) map
					.get(MULTI_PURCHASE_ANALYSIS_RESPONSE_DATA_LIST);
			final SimpleDateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());//Modified by Archana for AAOL-5513
			final PdfPTable searchCriteriaTable = new PdfPTable(9);
			searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			searchCriteriaTable.setTotalWidth(822F);
			searchCriteriaTable.setLockedWidth(true);
			searchCriteriaTable.setSpacingAfter(30f);

			final PdfPCell headerCell = new PdfPCell();
			headerCell.setPhrase(new Phrase(messageSource.getMessage(SEARCHHEADER, null, i18nService.getCurrentLocale()),new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			headerCell.setColspan(9);
			searchCriteriaTable.addCell(headerCell);
			
			searchCriteriaTable.addCell(messageSource.getMessage(ACCOUNT, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(TERRITORY, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(STARTDATE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ENDDATE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(BRAND, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(SUBBRAND, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ORDEREDFROM, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(PRODUCTSDISPLAY, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ANALYSISVARIABLE, null, i18nService.getCurrentLocale()));
			
			
			/*final PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			titleTable.setTotalWidth(822F);
			titleTable.setLockedWidth(true);
			titleTable.setSpacingAfter(30f);
			
			final PdfPCell headerCell = new PdfPCell(new Phrase("MULTI PURCHASE ANALYSIS SEARCH"));
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
				searchCriteriaTable.addCell(formData.getTerritory());
				searchCriteriaTable.addCell(formData.getStartDate());//Modified by Archana for AAOL-5513
				searchCriteriaTable.addCell(formData.getEndDate());//Modified by Archana for AAOL-5513
				searchCriteriaTable.addCell(getMessageService().getMessageForCode(formData.getOperatingCompany(),
				        getI18nService().getCurrentLocale()));
				searchCriteriaTable.addCell(formData.getFranchiseDivCode());
				searchCriteriaTable.addCell(getMessageService().getMessageForCode(formData.getOrderedFrom(),
				        getI18nService().getCurrentLocale()));
				searchCriteriaTable.addCell(getMessageService().getMessageForCode(formData.getProductsToDisplay(),
				        getI18nService().getCurrentLocale()));
				searchCriteriaTable.addCell(getMessageService().getMessageForCode(formData.getAnalysisVariable(),
				        getI18nService().getCurrentLocale()));
			}

			final PdfPTable multiPurchaseAnalysisTable = new PdfPTable(8);
			multiPurchaseAnalysisTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			multiPurchaseAnalysisTable.setTotalWidth(822F);
			multiPurchaseAnalysisTable.setLockedWidth(true);
			multiPurchaseAnalysisTable.setSpacingAfter(736f);
			final PdfPCell resultsCell = new PdfPCell();
			resultsCell.setPhrase(new Phrase(messageSource.getMessage(SEARCHRESULTS, null, i18nService.getCurrentLocale()),new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			resultsCell.setColspan(9);
			multiPurchaseAnalysisTable.addCell(resultsCell);
			multiPurchaseAnalysisTable.addCell(messageSource.getMessage(ACCOUNTNUMBER, null, i18nService.getCurrentLocale()));
			multiPurchaseAnalysisTable.addCell(messageSource.getMessage(PRODUCTNAME, null, i18nService.getCurrentLocale()));
			multiPurchaseAnalysisTable.addCell(messageSource.getMessage(PRODUCTCODE, null, i18nService.getCurrentLocale()));
			multiPurchaseAnalysisTable.addCell(messageSource.getMessage(AMOUNT, null, i18nService.getCurrentLocale()));
			multiPurchaseAnalysisTable.addCell(messageSource.getMessage(SPENDING, null, i18nService.getCurrentLocale()));
			multiPurchaseAnalysisTable.addCell(messageSource.getMessage(QUANTITY, null, i18nService.getCurrentLocale()));
			multiPurchaseAnalysisTable.addCell(messageSource.getMessage(FREQUENCY, null, i18nService.getCurrentLocale()));
			multiPurchaseAnalysisTable.addCell(messageSource.getMessage(WEIGHT, null, i18nService.getCurrentLocale()));
			final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);

			if (null != dataList)
			{
				for (final JnjGTMultiPurchaseOrderReportResponseData data : dataList)
				{
					final DecimalFormat decimalFormat = new DecimalFormat("#.##");
					final double percentSpending = (data.getAmount().getValue().doubleValue() / totalSpendingMap.get(
							data.getAccountNumber()).doubleValue()) * 100;
					if (!Double.isNaN(percentSpending))
					{
						data.setPercentageSpending(Double.valueOf(decimalFormat.format(percentSpending)).doubleValue());
					}
					else
					{
						data.setPercentageSpending(Double.valueOf(0).doubleValue());
					}
					multiPurchaseAnalysisTable.addCell(data.getAccountNumber() == null ? pdfCell : new PdfPCell(new Phrase(data
							.getAccountNumber())));
					multiPurchaseAnalysisTable.addCell(data.getProductName() == null ? pdfCell : new PdfPCell(new Phrase(data
							.getProductName())));
					/*multiPurchaseAnalysisTable.addCell((data.getProductCode() == null && data.getProductGTIN() == null && data
							.getProductUPC() == null) ? pdfCell : new PdfPCell(new Phrase(data.getProductCode()
							+ Jnjb2bCoreConstants.SPACE + data.getProductGTIN() + Jnjb2bCoreConstants.SPACE + data.getProductUPC())));*/
					
					String productCode= new String();
					if(data.getProductCode()!= null){
						productCode=data.getProductCode();	
					}
					if(data.getProductGTIN() != null){
						productCode=productCode+Jnjb2bCoreConstants.SPACE+data.getProductGTIN();
					}
					if(data.getProductUPC() != null){
						productCode=productCode+Jnjb2bCoreConstants.SPACE+data.getProductUPC();	
					}
					
					
					multiPurchaseAnalysisTable.addCell((data.getProductCode() == null && data.getProductGTIN() == null && data.getProductUPC() == null) ? pdfCell: new PdfPCell(new Phrase(productCode)));
					
					
					multiPurchaseAnalysisTable.addCell((data.getAmount().getFormattedValue()) == null ? pdfCell : new PdfPCell(
							new Phrase(data.getAmount().getFormattedValue())));
					multiPurchaseAnalysisTable.addCell(Double.valueOf(data.getPercentageSpending()) == null ? pdfCell : new PdfPCell(
							new Phrase(Double.valueOf(data.getPercentageSpending()).toString())));
					multiPurchaseAnalysisTable.addCell(Long.valueOf(data.getUnitQuantity()) == null ? pdfCell : new PdfPCell(
							new Phrase(Long.valueOf(data.getUnitQuantity()).toString() + data.getUom())));
					multiPurchaseAnalysisTable.addCell(Integer.valueOf(data.getOrderFrequency()) == null ? pdfCell : new PdfPCell(
							new Phrase(Integer.valueOf(data.getOrderFrequency()).toString())));

					if (data.getOrderWeight() != 0D)
					{
						multiPurchaseAnalysisTable.addCell(Double.valueOf(data.getOrderWeight()) == null ? pdfCell : new PdfPCell(
								new Phrase(Double.valueOf(data.getOrderWeight()).toString())));
					}
					else
					{
						multiPurchaseAnalysisTable.addCell(pdfCell);
					}
				}
				multiPurchaseAnalysisTable.addCell(pdfCell);
				multiPurchaseAnalysisTable.addCell(pdfCell);
				multiPurchaseAnalysisTable.addCell(pdfCell);
				multiPurchaseAnalysisTable.addCell(pdfCell);
				multiPurchaseAnalysisTable.addCell(pdfCell);
				multiPurchaseAnalysisTable.addCell(pdfCell);
				multiPurchaseAnalysisTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(SPENT, null, i18nService.getCurrentLocale()))));
				multiPurchaseAnalysisTable.addCell(totalAmountSpend.getFormattedValue() == null ? pdfCell : new PdfPCell(new Phrase(
						totalAmountSpend.getFormattedValue())));
			}
			
			
			document.add(imageTable);
			//document.add(titleTable);
			document.add(searchCriteriaTable);
			document.add(multiPurchaseAnalysisTable);
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
