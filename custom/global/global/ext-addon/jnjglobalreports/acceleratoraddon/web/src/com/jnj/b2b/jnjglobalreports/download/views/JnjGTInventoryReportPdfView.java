/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.download.views;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTInventoryReportForm;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.servicelayer.i18n.I18NService;


/**
 * This class handles the PDF download for inventory report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTInventoryReportPdfView extends AbstractPdfView
{
	private static final Logger LOG = Logger.getLogger(JnjGTInventoryReportPdfView.class);
	private static final String INVENTORY_RESPONSE_DATA_LIST = "jnjGTInventoryReportResponseDataList";
	private static final String INVENTORY_FORM_NAME = "jnjGTInventoryReportForm";
	protected static final int MARGIN = 32;
	
	private static final String REP_UCN = "reports.inventory.rep.ucn";
	private static final String PRODUCT_CODE = "reports.inventory.product.code";
	private static final String LOT_NUMBER = "reports.inventory.product.lot.number";
	private static final String ZERO_STOCKS = "reports.inventory.zero.stocks";
	private static final String HEADER = "reports.inventory.header";
	private static final String UCN_NUMBER = "reports.inventory.table.ucn.number";
	private static final String DESC = "reports.inventory.table.desc";
	private static final String UNRESTRICTED = "reports.inventory.table.unrestricted";
	private static final String RESTRICTED = "reports.inventory.table.restricted";
	private static final String STOCK = "reports.inventory.table.quality.stock";
	private static final String TOTAL_QTY = "reports.inventory.table.total.quantity";
	private static final String UNIT = "reports.inventory.table.unit";

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
	protected void buildPdfDocument(final Map<String, Object> arg0, final Document arg1, final PdfWriter arg2,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
	{
		try
		{
			MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
		    arg2.setPageEvent(events);   
	        events.onOpenDocument(arg2, arg1); 
			arg4.setHeader("Content-Disposition", "attachment; filename=Inventory_Report.pdf");
			final List<JnjGTInventoryReportResponseData> jnjGTInventoryReportResponseDataList = (List<JnjGTInventoryReportResponseData>) arg0
					.get(INVENTORY_RESPONSE_DATA_LIST);

			final JnjGTInventoryReportForm searchCriteria = (JnjGTInventoryReportForm) arg0.get(INVENTORY_FORM_NAME);
			
			//image adding start 
	      	final InputStream jnjConnectLogoIS = (InputStream) arg0.get("jnjConnectLogoURL");
	      	final InputStream jnjConnectLogoIS2 = (InputStream) arg0.get("jnjConnectLogoURL2");
	      	byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
	      	byte[] jnjConnectLogoByteArray2 = IOUtils.toByteArray(jnjConnectLogoIS2);
	      	PdfPTable imageTable = new PdfPTable(2); // 3 columns.
	      	imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
	      	imageTable.setWidthPercentage(100);
	      	imageTable.setLockedWidth(false);
	      	imageTable.setSpacingAfter(30f);
	      	
	      	if ( jnjConnectLogoByteArray != null){
	      		imageTable.addCell(createImageCell(jnjConnectLogoByteArray,Element.ALIGN_LEFT));
	      	}
	      	if ( jnjConnectLogoByteArray2 != null){
	      		imageTable.addCell(createImageCell(jnjConnectLogoByteArray2,Element.ALIGN_RIGHT));
	      	}
	      	//image adding end
			final PdfPTable searchCriteriaTable = new PdfPTable(4);
			searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			searchCriteriaTable.setTotalWidth(822F);
			searchCriteriaTable.setLockedWidth(true);
			searchCriteriaTable.setSpacingAfter(736f);

			final PdfPCell headerCell = new PdfPCell(new Phrase("INVENTORY REPORT SEARCH CRITERIA"));
			headerCell.setColspan(4);
			searchCriteriaTable.addCell(headerCell);
			searchCriteriaTable.addCell(messageSource.getMessage(REP_UCN, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(LOT_NUMBER, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ZERO_STOCKS, null, i18nService.getCurrentLocale()));
			
			HashSet<String> ucn=new HashSet<String>(Arrays.asList(searchCriteria.getRepUCNs().split("\\s*(=>|,|\\s)\\s*")));
			 ucn.remove("");			
			searchCriteriaTable.addCell(StringUtils.join(ucn,','));
			searchCriteriaTable.addCell(searchCriteria.getProductCode());
			searchCriteriaTable.addCell(searchCriteria.getLotNumber());
			if (searchCriteria.isDisplayZeroStocks())
			{
				searchCriteriaTable.addCell("Yes");
			}
			else
			{
				searchCriteriaTable.addCell("No");
			}

			final PdfPTable inventoryTable = new PdfPTable(9);
			inventoryTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			inventoryTable.setTotalWidth(822F);
			inventoryTable.setLockedWidth(true);
			inventoryTable.setSpacingAfter(736f);
			inventoryTable.addCell(messageSource.getMessage(UCN_NUMBER, null, i18nService.getCurrentLocale()));
			inventoryTable.addCell(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			inventoryTable.addCell(messageSource.getMessage(DESC, null, i18nService.getCurrentLocale()));
			inventoryTable.addCell(messageSource.getMessage(LOT_NUMBER, null, i18nService.getCurrentLocale()));
			inventoryTable.addCell(messageSource.getMessage(UNRESTRICTED, null, i18nService.getCurrentLocale()));
			inventoryTable.addCell(messageSource.getMessage(RESTRICTED, null, i18nService.getCurrentLocale()));
			inventoryTable.addCell(messageSource.getMessage(STOCK, null, i18nService.getCurrentLocale()));
			inventoryTable.addCell(messageSource.getMessage(TOTAL_QTY, null, i18nService.getCurrentLocale()));
			inventoryTable.addCell(messageSource.getMessage(UNIT, null, i18nService.getCurrentLocale()));

			final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);

			if (null != jnjGTInventoryReportResponseDataList)
			{
				for (final JnjGTInventoryReportResponseData jnjGTInventoryReportResponseData : jnjGTInventoryReportResponseDataList)
				{
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getUcnNumber() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTInventoryReportResponseData.getUcnNumber())));
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getProductCode() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTInventoryReportResponseData.getProductCode())));
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getDescription() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTInventoryReportResponseData.getDescription())));
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getLotNumber() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTInventoryReportResponseData.getLotNumber())));
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getUnrestricted() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTInventoryReportResponseData.getUnrestricted())));
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getRestricted() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTInventoryReportResponseData.getRestricted())));
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getQualityStock() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTInventoryReportResponseData.getQualityStock())));
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getTotalQty() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTInventoryReportResponseData.getTotalQty())));
					inventoryTable.addCell(jnjGTInventoryReportResponseData.getUnit() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTInventoryReportResponseData.getUnit())));
				}
			}
			arg1.add(imageTable);
			arg1.add(searchCriteriaTable);
			arg1.add(inventoryTable);
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating PDF - " + exception.getMessage());
		}
	}
	//create cells
protected PdfPCell createLabelCell(String text){
      // font
      Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.GRAY);
      // create cell
      PdfPCell cell = new PdfPCell(new Phrase(text,font));//.toUpperCase()
      // set style
      labelCellStyle(cell);
      return cell;
  }

  // create cells
protected PdfPCell createValueCell(String text){
      // font
      Font font = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
      // create cell
      PdfPCell cell = new PdfPCell(new Phrase(text,font));
      // set style
      valueCellStyle(cell);
      return cell;
  }

 public void headerCellStyle(PdfPCell cell){
    // alignment
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    // padding
   cell.setPaddingTop(0f);
   cell.setPaddingBottom(7f);
   // background color
   cell.setBackgroundColor(new Color(0,121,182));
   // border
   cell.setBorder(0);
   cell.setBorderWidthBottom(2f);

}
public void labelCellStyle(PdfPCell cell){
    // alignment
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    // padding
    cell.setPaddingLeft(3f);
    cell.setPaddingTop(0f);
    // background color
    cell.setBackgroundColor(Color.LIGHT_GRAY);
    // border
    cell.setBorder(0);
    cell.setBorderWidthBottom(1);
    cell.setBorderColorBottom(Color.GRAY);
    // height
    cell.setMinimumHeight(18f);
}

public void valueCellStyle(PdfPCell cell){
   // alignment
   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
   cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
   // padding
   cell.setPaddingTop(0f);
   cell.setPaddingBottom(5f);
   // border
   cell.setBorder(0);
   cell.setBorderWidthBottom(0.5f);
   // height
   cell.setMinimumHeight(18f);
}

//~ Inner Classes ----------------------------------------------------------   

private static class MyPageEvents extends PdfPageEventHelper {   
	 	
	protected MessageSourceAccessor messageSourceAccessor;   

    // This is the PdfContentByte object of the writer   
	protected PdfContentByte cb;   

    // We will put the final number of pages in a template   
	protected PdfTemplate template;   

    // This is the BaseFont we are going to use for the header / footer   
	protected BaseFont bf = null;   
       
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
	  /**
	    * @param path
	    * @param align
	    * @return
	    * @throws Exception
	    */
	   public static PdfPCell createImageCell(byte[] path, int align) throws Exception {
	   		Image img = Image.getInstance(path);
	       PdfPCell cell = new PdfPCell(img, false);
	       labelImageCellStyle(cell,align);
	       return cell;
	   } 
	  
	  /**
	    * @param cell
	    * @param align
	    */
	   public static void labelImageCellStyle(PdfPCell cell, int align){
	     // alignment
	     	if(align ==2){
	     		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	     	}else{
	     		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	     	}
	         cell.setVerticalAlignment(Element.ALIGN_TOP);
	         cell.setBorder(Rectangle.NO_BORDER);
	     }
}
