/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalordertemplate.download;

import java.util.List;
import java.util.Map;

import java.io.InputStream;
import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTOrderTemplateEntryData;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.servicelayer.i18n.I18NService;

import org.apache.commons.io.IOUtils;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;


/**
 * YTODO <<Replace this line with the class description>>
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOrderTemplateDetailPdfView extends AbstractPdfView
{
	private static final Logger LOG = Logger.getLogger(JnjGTOrderTemplateDetailPdfView.class);
	private static final String DATA_LIST = "orderTemplateDetail";
	private static final int MARGIN = 32;
	
	private static final String PRODUCT_NAME = "template.detail.productname";
	private static final String PRODUCT_CODE = "template.detail.productcode";
	private static final String DESCRIPTION = "template.detail.description";
	private static final String PRODUCT_VOLUME = "template.detail.volume";
	private static final String PRODUCT_WEIGHT = "template.detail.weight";
	private static final String PRODUCT_QUANTITY = "template.detail.quantity";
	private static final String HEADER =  "template.order.detail.header";
	
	@Autowired
	protected I18NService i18nService;
	private MessageSource messageSource;
	
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
			arg4.setHeader("Content-Disposition", "attachment; filename=Template_Details.pdf");
			final List<JnjGTOrderTemplateEntryData> orderTemplateDatas = (List<JnjGTOrderTemplateEntryData>) map.get(DATA_LIST);

			final PdfPTable templateDetailTable = new PdfPTable(6);
			templateDetailTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			templateDetailTable.setTotalWidth(822F);
			templateDetailTable.setLockedWidth(true);
			templateDetailTable.setSpacingAfter(736f);
			templateDetailTable.addCell(messageSource.getMessage(PRODUCT_NAME, null, i18nService.getCurrentLocale()));
			templateDetailTable.addCell(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			templateDetailTable.addCell(messageSource.getMessage(DESCRIPTION, null, i18nService.getCurrentLocale()));
			templateDetailTable.addCell(messageSource.getMessage(PRODUCT_VOLUME, null, i18nService.getCurrentLocale()));
			templateDetailTable.addCell(messageSource.getMessage(PRODUCT_WEIGHT, null, i18nService.getCurrentLocale()));
			templateDetailTable.addCell(messageSource.getMessage(PRODUCT_QUANTITY, null, i18nService.getCurrentLocale()));
			
			
			final PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			titleTable.setTotalWidth(822F);
			titleTable.setLockedWidth(true);
			titleTable.setSpacingAfter(30f);
			
			final PdfPCell headerCell = new PdfPCell(new Phrase(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale())));
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

			if (null != orderTemplateDatas)
			{
				for (final JnjGTOrderTemplateEntryData orderTemplateData : orderTemplateDatas)
				{
					final String deliveryUnit = orderTemplateData.getRefVariant().getDeliveryUnit() == null ? Jnjb2bCoreConstants.SPACE
							: orderTemplateData.getRefVariant().getDeliveryUnit();
					final String numerator = orderTemplateData.getRefVariant().getNumerator() + Jnjb2bCoreConstants.SPACE
							+ orderTemplateData.getRefVariant().getSalesUnit() == null ? Jnjb2bCoreConstants.SPACE : orderTemplateData
							.getRefVariant().getNumerator()
							+ Jnjb2bCoreConstants.SPACE
							+ orderTemplateData.getRefVariant().getSalesUnit();

					templateDetailTable.addCell(orderTemplateData.getRefVariant().getName() == null ? pdfCell : new PdfPCell(
							new Phrase(orderTemplateData.getRefVariant().getName())));
					templateDetailTable.addCell(orderTemplateData.getRefVariant().getCode() == null ? pdfCell : new PdfPCell(
							new Phrase(orderTemplateData.getRefVariant().getCode())));
					templateDetailTable.addCell(orderTemplateData.getRefVariant().getDescription() == null ? pdfCell : new PdfPCell(
							new Phrase(orderTemplateData.getRefVariant().getDescription())));
					templateDetailTable.addCell(orderTemplateData.getRefVariant().getProductVolume() == null ? pdfCell : new PdfPCell(
							new Phrase(orderTemplateData.getRefVariant().getProductVolume())));
					templateDetailTable.addCell(orderTemplateData.getRefVariant().getProductWeight() == null ? pdfCell : new PdfPCell(
							new Phrase(orderTemplateData.getRefVariant().getProductWeight())));
					if (StringUtils.isEmpty(numerator))
					{
						templateDetailTable.addCell(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE
								+ orderTemplateData.getRefVariant().getDeliveryUnit() == null ? pdfCell : new PdfPCell(new Phrase(
								orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE
										+ orderTemplateData.getRefVariant().getDeliveryUnit())));
					}
					else
					{
						templateDetailTable
								.addCell(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE + deliveryUnit == null ? pdfCell
										: new PdfPCell(new Phrase(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE + deliveryUnit
												+ "(" + numerator + ")")));
					}

				}
			}
			document.add(imageTable);
			document.add(titleTable);
			
			document.add(templateDetailTable);
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
