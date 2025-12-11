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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.lowagie.text.Chunk;
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
 * This class handles the PDF download for back-order report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTBackorderReportPdfView extends AbstractPdfView
{
	private static final Logger LOG = Logger.getLogger(JnjGTBackorderReportPdfView.class);
	private static final String BACKORDER_RESPONSE_DATA_LIST = "jnjGTBackorderReportResponseDataList";
	protected static final int MARGIN = 32;
	private static final String BACKORDER_FORM_NAME = "jnjGTBackorderReportForm";
	private static final String sheetName = "BACK ORDER REPORT RESULT";
	private static final String sheetName1 = "BACKORDER REPORT SEARCH CRITERIA";
	private static final String TEXT_ONE = "Please note: Prices are excluding VAT. Availability and Prices might be subject to change.";
	
	private static final String HEADER = "backorder.reports.header";
	private static final String DOWNLOADDATE = "backorder.reports.download.date";
	private static final String RESULTS = "backorder.reports.results";
	private static final String ACC_NUMBER = "backorder.reports.account.number";
	private static final String ACC_NAME = "backorder.reports.accountname";
	private static final String ORDERDATE = "backorder.reports.orderdate";
	private static final String ACCOUNTS =  "backorder.reports.accounts";
	private static final String CUST_PO = "backorder.reports.customer.po";
	private static final String SHIPTO_NAME = "backorder.reports.shipto.name";
	private static final String ORDERNUMBER = "backorder.reports.ordernumber";
	private static final String PRODUCTCODE = "backorder.reports.productcode";
	private static final String GTIN = "backorder.reports.gtin";
	private static final String PRODUCTNAME = "backorder.reports.productname";
	private static final String ORDERQTY =  "backorder.reports.orderquantity";
	private static final String UNIT = "backorder.reports.unit";
	private static final String EST_AVAILABILITY = "backorder.reports.estimatedavailability";
	private static final String ITEMPRICE = "backorder.reports.itemprice";
	private static final String ORDERTOTAL = "backorder.reports.orderTotal";
	private static final String FRANCISE =  "backorder.reports.franchise";
	private static final String EXTENDEDPRICE =  "backorder.reports.extendedprice";
	private static final String PRODUCTGTIN =  "backorder.reports.productGtin";
	private static final String STARTDATE =  "backorder.reports.startdate";
	private static final String ENDDATE =  "backorder.reports.enddate";
	private static final String SEARCHBY =  "backorder.reports.searchBy";
	private static final String SHIPTO_ACCOUNT =  "backorder.reports.shiptoaccount";
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
			//for page number added
			 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
			 arg2.setPageEvent(events);   
	       events.onOpenDocument(arg2, arg1);  
	       
			arg4.setHeader("Content-Disposition", "attachment; filename=Backorder_Report.pdf");
			final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = (List<JnjGTBackorderReportResponseData>) arg0
					.get(BACKORDER_RESPONSE_DATA_LIST);
			final JnjGTBackorderReportForm searchCriteria = (JnjGTBackorderReportForm) arg0.get(BACKORDER_FORM_NAME);
			
			String emptyField = new String();
			final String accountsSelectedName = (String) arg0.get("accountreportname");
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
	      	
			final PdfPTable searchCriteriaTable = new PdfPTable(6);
			searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			searchCriteriaTable.setTotalWidth(822F);
			searchCriteriaTable.setLockedWidth(true);
			searchCriteriaTable.setSpacingAfter(30f);
			
			final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			final Date date = new Date();
			
			final PdfPCell headerCell = new PdfPCell();
			headerCell.setColspan(6);
		 	/*headerCell.setBorderWidthBottom(10f);
	       	headerCell.setBorderWidthTop(5f);
	       	headerCell.setBackgroundColor(Color.WHITE); 
	       	headerCell.setBorder(Rectangle.NO_BORDER);*/
	       	headerCell.setPhrase(new Phrase(sheetName1,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa"))));
			searchCriteriaTable.addCell(headerCell);
			
			searchCriteriaTable.addCell(messageSource.getMessage(DOWNLOADDATE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ACC_NAME, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ACCOUNTS, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(STARTDATE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(ENDDATE, null, i18nService.getCurrentLocale()));
			searchCriteriaTable.addCell(messageSource.getMessage(SEARCHBY, null, i18nService.getCurrentLocale()));
			
			searchCriteriaTable.addCell(dateFormat.format(date));
			searchCriteriaTable.addCell(accountsSelectedName != null ? accountsSelectedName : emptyField);
			searchCriteriaTable.addCell(searchCriteria.getAccountIds());
			searchCriteriaTable.addCell(searchCriteria.getFromDate());
			searchCriteriaTable.addCell(searchCriteria.getToDate());
			searchCriteriaTable.addCell(searchCriteria.getProductCode() != null? searchCriteria.getProductCode() : emptyField);
			 
			final PdfPTable displayTextTable = new PdfPTable(13);
			displayTextTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			displayTextTable.setTotalWidth(822F);
			displayTextTable.setLockedWidth(true);
			displayTextTable.setSpacingAfter(30f);
			
			final PdfPCell txtCellDisplayCell = new PdfPCell();
			/*txtCellDisplayCell.setColspan(10);
			txtCellDisplayCell.setBackgroundColor(Color.WHITE); 
			txtCellDisplayCell.setBorder(Rectangle.NO_BORDER);*/
			txtCellDisplayCell.setPhrase(new Phrase(TEXT_ONE,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			displayTextTable.addCell(txtCellDisplayCell);
			
			final PdfPTable backorderTable = new PdfPTable(13);
			backorderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			backorderTable.setTotalWidth(new float[]{ 63,63,63,63,63,63,75,63,82,55,34,63,69 });
			backorderTable.setLockedWidth(true);
			backorderTable.setSpacingAfter(30f);
			
			final PdfPCell headerResultsCell = new PdfPCell();
			headerResultsCell.setColspan(13);
			/*headerResultsCell.setBorderWidthBottom(10f);
		 	headerResultsCell.setBorderWidthTop(5f);
	       	headerResultsCell.setBackgroundColor(Color.WHITE); 
	       	headerResultsCell.setBorder(Rectangle.NO_BORDER);*/
	    	headerResultsCell.setPhrase(new Phrase(sheetName,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa"))));
	    	
			backorderTable.addCell(headerResultsCell);
			backorderTable.addCell(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()));
			backorderTable.addCell(messageSource.getMessage(CUST_PO, null, i18nService.getCurrentLocale()));
			backorderTable.addCell(messageSource.getMessage(ORDERNUMBER, null, i18nService.getCurrentLocale()));
			
			//START for Adding ship to account - 4601
			backorderTable.addCell(messageSource.getMessage(SHIPTO_NAME, null, i18nService.getCurrentLocale()));
			backorderTable.addCell(messageSource.getMessage(FRANCISE, null, i18nService.getCurrentLocale()));
			backorderTable.addCell(messageSource.getMessage(SHIPTO_ACCOUNT, null, i18nService.getCurrentLocale()));
			//END for Adding ship to account - 4601
			
			backorderTable.addCell(messageSource.getMessage(ORDERDATE, null, i18nService.getCurrentLocale()));
			backorderTable.addCell(messageSource.getMessage(PRODUCTNAME, null, i18nService.getCurrentLocale()));
			backorderTable.addCell(messageSource.getMessage(PRODUCTGTIN, null, i18nService.getCurrentLocale()));
			backorderTable.addCell(messageSource.getMessage(ORDERQTY, null, i18nService.getCurrentLocale()));
			backorderTable.addCell(messageSource.getMessage(UNIT, null, i18nService.getCurrentLocale()));
			backorderTable.addCell("Status");
	        backorderTable.addCell(messageSource.getMessage(EST_AVAILABILITY, null, i18nService.getCurrentLocale()));

	

			
	

			/*backorderTable.addCell(createLabelCell("Ship To Name"));
			backorderTable.addCell(createLabelCell("Franchise"));

			backorderTable.addCell(createLabelCell("Item price"));
			backorderTable.addCell(createLabelCell("Extended Price"));*/

			if (null != jnjGTBackorderReportResponseDataList)
			{
				double totals = 0;
				for (final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData : jnjGTBackorderReportResponseDataList)
				{
					emptyField="";
					backorderTable.addCell(jnjGTBackorderReportResponseData.getAccountNumber() != null ? jnjGTBackorderReportResponseData.getAccountNumber() :emptyField);
					backorderTable.addCell(jnjGTBackorderReportResponseData.getCustomerPO() != null ? jnjGTBackorderReportResponseData.getCustomerPO() :emptyField);
					backorderTable.addCell(jnjGTBackorderReportResponseData.getOrderNumber() != null ? jnjGTBackorderReportResponseData.getOrderNumber().split("\\|")[1]: emptyField);
					
					//START for Adding ship to account - 4601
					backorderTable.addCell(jnjGTBackorderReportResponseData.getShipToName() != null ? jnjGTBackorderReportResponseData.getShipToName() :emptyField);
					backorderTable.addCell(jnjGTBackorderReportResponseData.getOperatingCompany() != null ? jnjGTBackorderReportResponseData.getOperatingCompany() : emptyField);
					backorderTable.addCell(jnjGTBackorderReportResponseData.getShipToAccount()!= null ? jnjGTBackorderReportResponseData.getShipToAccount() : emptyField);
					//END for Adding ship to account - 4601
					
					backorderTable.addCell(jnjGTBackorderReportResponseData.getOrderDate() != null ? jnjGTBackorderReportResponseData.getOrderDate(): emptyField);
					backorderTable.addCell(jnjGTBackorderReportResponseData.getProductName() != null ? jnjGTBackorderReportResponseData.getProductName() :emptyField);
			
					//backorderTable.addCell(createValueCell(jnjGTBackorderReportResponseData.getOperatingCompany() != null ? jnjGTBackorderReportResponseData.getOperatingCompany(): emptyField));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getProductCode()!=null?jnjGTBackorderReportResponseData.getProductCode():emptyField +
							jnjGTBackorderReportResponseData.getProductGTIN()!=null?jnjGTBackorderReportResponseData.getProductGTIN():emptyField);
					
					if(!jnjGTBackorderReportResponseData.getQty().equals("null") ){
						backorderTable.addCell(jnjGTBackorderReportResponseData.getQty());
					}else{
						backorderTable.addCell(emptyField);	
					}
					
					
					backorderTable.addCell(jnjGTBackorderReportResponseData.getUnit() != null ? jnjGTBackorderReportResponseData.getUnit() : emptyField);
					backorderTable.addCell(jnjGTBackorderReportResponseData.getStatus() != null ? jnjGTBackorderReportResponseData.getStatus() : emptyField);

					backorderTable.addCell(jnjGTBackorderReportResponseData.getEstimatedAvailability() != null ? jnjGTBackorderReportResponseData.getEstimatedAvailability()
							: emptyField);
					
					
					/*backorderTable.addCell(createValueCell(jnjGTBackorderReportResponseData.getItemPrice() != null ? jnjGTBackorderReportResponseData.getItemPrice() +jnjGTBackorderReportResponseData.getCurrency(): emptyField));
					
					backorderTable.addCell(createValueCell(jnjGTBackorderReportResponseData.getExtendedPrice() != null ? jnjGTBackorderReportResponseData.getExtendedPrice() +jnjGTBackorderReportResponseData.getCurrency() :emptyField));
*/
					/*if (StringUtils.isNotEmpty(jnjGTBackorderReportResponseData.getExtendedPrice()))
					{
						totals += Double.parseDouble(jnjGTBackorderReportResponseData.getExtendedPrice());
					}*/
				}
			/*	backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));
				backorderTable.addCell(createValueCell(emptyField));*/
				/*backorderTable.addCell(createValueCell(emptyField));*/
				/*backorderTable.addCell(createValueCell("Totals"));
				backorderTable.addCell(createValueCell(String.valueOf(totals + jnjGTBackorderReportResponseDataList.get(0).getCurrency())));*/
			}
			arg1.add(imageTable);
			arg1.add(searchCriteriaTable);
			arg1.add(Chunk.NEWLINE);
			arg1.add(displayTextTable);
			arg1.add(Chunk.NEWLINE);
			arg1.add(backorderTable);
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
