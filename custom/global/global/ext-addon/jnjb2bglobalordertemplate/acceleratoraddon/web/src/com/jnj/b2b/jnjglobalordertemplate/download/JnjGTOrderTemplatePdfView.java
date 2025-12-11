/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalordertemplate.download;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTOrderTemplateData;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import java.io.IOException;

/**
 * This class handles the PDF download for Order Template list
 *
 * @author balinder.singh
 * @version 1.0
 */
public class JnjGTOrderTemplatePdfView extends AbstractPdfView
{
	private static final Logger LOG = Logger.getLogger(JnjGTOrderTemplatePdfView.class);
	protected static final String ORDER_TEMPLATE_DATA_LIST = "orderTemplate";
	private static final int MARGIN = 32;
	
	private static final String ORDER_LIST = "Template.order.list";
	private static final String TEMPLATE_NAME = "template.name";
	private static final String TEMPLATE_NUMBER = "template.number";
	private static final String TEMPLATE_AUTHOR = "template.author.text";
	private static final String TEMPLATE_CREATED = "template.Created.text";
	private static final String SHARESTATUS = "template.sharestatus.text";
	private static final String LINES = "template.order.lines";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	
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
			arg4.setHeader("Content-Disposition", "attachment; filename=" + Jnjb2bCoreConstants.TemplateSearch.PDF_FILE_NAME
					+ ".pdf");
			final List<JnjGTOrderTemplateData> jnjGTOrderTemplateDataList = (List<JnjGTOrderTemplateData>) map
					.get(ORDER_TEMPLATE_DATA_LIST);

			final PdfPTable orderTemplateTable = new PdfPTable(6);
			orderTemplateTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			orderTemplateTable.setTotalWidth(822F);
			orderTemplateTable.setLockedWidth(true);
			orderTemplateTable.setSpacingAfter(736f);
			orderTemplateTable.addCell(messageSource.getMessage(TEMPLATE_NAME, null, i18nService.getCurrentLocale()));
			orderTemplateTable.addCell(messageSource.getMessage(TEMPLATE_NUMBER, null, i18nService.getCurrentLocale()));
			orderTemplateTable.addCell(messageSource.getMessage(TEMPLATE_AUTHOR, null, i18nService.getCurrentLocale()));
			orderTemplateTable.addCell(messageSource.getMessage(TEMPLATE_CREATED, null, i18nService.getCurrentLocale()));
			orderTemplateTable.addCell(messageSource.getMessage(SHARESTATUS, null, i18nService.getCurrentLocale()));
			orderTemplateTable.addCell(messageSource.getMessage(LINES, null, i18nService.getCurrentLocale()));
			
			
			final PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			titleTable.setTotalWidth(822F);
			titleTable.setLockedWidth(true);
			titleTable.setSpacingAfter(30f);
			
			final PdfPCell headerCell = new PdfPCell(new Phrase("TEMPLATE  SEARCHED RESULTS"));
			headerCell.setColspan(11);
			titleTable.addCell(headerCell);
			
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

			final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);
			final SimpleDateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

			if (null != jnjGTOrderTemplateDataList)
			{
				for (final JnjGTOrderTemplateData jnjGTOrderTemplateData : jnjGTOrderTemplateDataList)
				{
					orderTemplateTable.addCell(jnjGTOrderTemplateData.getTemplateName() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTOrderTemplateData.getTemplateName())));
					orderTemplateTable.addCell(jnjGTOrderTemplateData.getTemplateNumber() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTOrderTemplateData.getTemplateNumber())));
					orderTemplateTable.addCell(jnjGTOrderTemplateData.getAuthor() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTOrderTemplateData.getAuthor())));
					orderTemplateTable.addCell(jnjGTOrderTemplateData.getCreatedOn() == null ? pdfCell : new PdfPCell(new Phrase(
							dateFormatter.format(jnjGTOrderTemplateData.getCreatedOn()))));
					orderTemplateTable.addCell(jnjGTOrderTemplateData.getShareStatus() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTOrderTemplateData.getShareStatus())));
					orderTemplateTable.addCell(jnjGTOrderTemplateData.getLines() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTOrderTemplateData.getLines().toString())));
				}
				
			}
			document.add(imageTable);
			document.add(titleTable);
			
			document.add(orderTemplateTable);
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating PDF - " + exception.getMessage());
		}
	}
	
	   
	   private static class MyPageEvents extends PdfPageEventHelper {   
	  	 	
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
	           } catch (IOException ioe) {}   
	       } 
	  
	      
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
	  
	    
	       public void onCloseDocument(PdfWriter writer, Document document) {   
	           template.beginText();   
	           template.setFontAndSize(bf, 8);   
	           template.showText(String.valueOf( writer.getPageNumber() - 1 ));   
	           template.endText();   
	       }  
	   }  
}
