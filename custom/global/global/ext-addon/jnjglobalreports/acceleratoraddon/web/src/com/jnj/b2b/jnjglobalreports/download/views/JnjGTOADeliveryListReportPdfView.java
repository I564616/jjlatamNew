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

import org.springframework.context.MessageSource;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.springframework.context.support.MessageSourceAccessor;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTOADeliveryListReportForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTOADeliveryListReportResponseData;
import com.jnj.services.MessageService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
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
import java.io.IOException;









/**
 * AAOL-4603: This class handles the PDF download for Order Analysis for Delivery List report
 *
 * @author Cognizant
 * @version 1.0
 */
public class JnjGTOADeliveryListReportPdfView extends AbstractPdfView
{

	private static final Logger LOG = Logger.getLogger(JnjGTOADeliveryListReportPdfView.class);
	private static final String OA_DELIVERY_LIST_RESPONSE_DATA_LIST = "jnjGTOADeliveryListReportResponseDataList";
	private static final String OA_DELIVERY_LIST_FORM_NAME = "jnjGlobalOADeliveryListReportForm";
	//private static final String TOTAL_SPENDING_MAP = "totalSpendingMap";//TODO - REMOVE
	//private static final String SEARCH_TOTAL = "searchTotal";//TODO - REMOVE
	private static final int MARGIN = 32;//TODO - REMOVE??

	private static final String DL_HEADER="reports.oa.pdf.header.deliverylist";
	private static final String SEARCH_CRITERIA = "reports.common.searchCriteria";
	private static final String START_DATE = "reports.backorder.excel.from.start"; 
	private static final String END_DATE = "reports.backorder.excel.to.start"; 
	private static final String ORDERTYPE = "cart.common.orderType"; 
	private static final String SALES_DOC_NUM = "reports.financial.table.product.salesDocumentNumber"; 
	private static final String LINE_ITEM = "reports.financial.table.product.lineItem"; 
	private static final String CUST_PO_NUM = "reports.financial.table.product.customerPoNo"; 
	private static final String DELIVERY_NUM = "reports.order.analysis.delivery.No"; 
	private static final String DELIVERY_ITEM_NUM = "reports.order.analysis.delivery.itemno"; 
	private static final String ACTUAL_SHIP_DATE = "reports.order.analysis.actualShipDate"; 
	private static final String FRANCHISE_DESC	 = "reports.financial.table.product.franchiseDescription"; 
	private static final String PROD_CODE = "reports.financial.table.product.code"; 
	private static final String PROD_DESC = "reports.financial.table.product.desc"; 
	private static final String UOM = "reports.order.analysis.UOM"; 
	private static final String DELIVERY_QTY = "reports.order.analysis.delivery.qty"; 
	private static final String ORDER_QTY = "reports.order.analysis.order.quantity"; 
	private static final String BATCH_NUM = "cart.validate.batchNo"; 
	private static final String BATCH_EXPIRY_DATE = "reports.oa.batchExpDate"; 
	private static final String SERIAL_NUM = "cart.validate.serialNo"; 
	private static final String POD_DATE = "reports.oa.podDateTime"; 
	private static final String SHIP_TO_ACC = "cart.deliver.shipToAcc"; 
	private static final String SHIP_TO_NAME = "reports.backorder.table.shipTo"; 
	private static final String CONNOTE = "reports.oa.connote"; 
	  
	
	private MessageService messageService;
	/** I18NService to retrieve the current locale. */
	private I18NService i18nService;
	private MessageSource messageSource;
	
	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
	public MessageService getMessageService()
	{
		return messageService;
	}

	public void setMessageService(final MessageService messageService)
	{
		this.messageService = messageService;
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
			arg4.setHeader("Content-Disposition", "attachment; filename=OA_Delivery_List_Report.pdf");
			final JnjGTOADeliveryListReportForm formData = (JnjGTOADeliveryListReportForm) map
					.get(OA_DELIVERY_LIST_FORM_NAME);
			//final Map<String, Double> totalSpendingMap = (Map<String, Double>) map.get(TOTAL_SPENDING_MAP);//TODO - REMOVE
			//final PriceData totalAmountSpend = (PriceData) map.get(SEARCH_TOTAL);//TODO - REMOVE
			final List<JnjGTOADeliveryListReportResponseData> dataList = (List<JnjGTOADeliveryListReportResponseData>) map
					.get(OA_DELIVERY_LIST_RESPONSE_DATA_LIST);

			final PdfPTable searchCriteriaTable = new PdfPTable(8);
			searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			searchCriteriaTable.setTotalWidth(822F);
			searchCriteriaTable.setLockedWidth(true);
			searchCriteriaTable.setSpacingAfter(30f);

			searchCriteriaTable.addCell(messageSource.getMessage(START_DATE, null, i18nService.getCurrentLocale()).toUpperCase());
			searchCriteriaTable.addCell(messageSource.getMessage(END_DATE, null, i18nService.getCurrentLocale()).toUpperCase());
			searchCriteriaTable.addCell(messageSource.getMessage(CUST_PO_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			searchCriteriaTable.addCell(messageSource.getMessage(SALES_DOC_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			searchCriteriaTable.addCell(messageSource.getMessage(PROD_CODE, null, i18nService.getCurrentLocale()).toUpperCase());
			searchCriteriaTable.addCell(messageSource.getMessage(FRANCHISE_DESC, null, i18nService.getCurrentLocale()).toUpperCase());
			searchCriteriaTable.addCell(messageSource.getMessage(ORDERTYPE, null, i18nService.getCurrentLocale()).toUpperCase());
			searchCriteriaTable.addCell(messageSource.getMessage(DELIVERY_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			
			
			final PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			titleTable.setTotalWidth(822F);
			titleTable.setLockedWidth(true);
			titleTable.setSpacingAfter(30f);
			
			final PdfPCell headerCell = new PdfPCell(new Phrase(messageSource.getMessage(DL_HEADER, null, i18nService.getCurrentLocale())));
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

			if (null != formData)
			{
				
				searchCriteriaTable.addCell(formData.getStartDate());
				searchCriteriaTable.addCell(formData.getEndDate());				
				searchCriteriaTable.addCell(formData.getCustPONum());
				searchCriteriaTable.addCell(formData.getSalesDocNum());	
				searchCriteriaTable.addCell(formData.getProductCode());
				searchCriteriaTable.addCell(formData.getFranchiseDesc());
				searchCriteriaTable.addCell(formData.getOrderType());
				searchCriteriaTable.addCell(formData.getDeliveryNum());
				
				
			}

			final PdfPTable oaDeliveryListTable = new PdfPTable(20);
			oaDeliveryListTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			oaDeliveryListTable.setTotalWidth(822F);
			oaDeliveryListTable.setLockedWidth(true);
			oaDeliveryListTable.setSpacingAfter(736f);
			oaDeliveryListTable.addCell(messageSource.getMessage(ORDERTYPE, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(SALES_DOC_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(LINE_ITEM, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(CUST_PO_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(DELIVERY_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(DELIVERY_ITEM_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(ACTUAL_SHIP_DATE, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(FRANCHISE_DESC, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(PROD_CODE, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(PROD_DESC, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(DELIVERY_QTY, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(ORDER_QTY, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(BATCH_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(BATCH_EXPIRY_DATE, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(SERIAL_NUM, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(POD_DATE, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(SHIP_TO_ACC, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(SHIP_TO_NAME, null, i18nService.getCurrentLocale()).toUpperCase());
			oaDeliveryListTable.addCell(messageSource.getMessage(CONNOTE, null, i18nService.getCurrentLocale()).toUpperCase());
			
			
			final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);

			if (null != dataList)
			{
				for (final JnjGTOADeliveryListReportResponseData data : dataList)
				{					
					oaDeliveryListTable.addCell(data.getOrderType() == null ? pdfCell : new PdfPCell(new Phrase(data
							.getOrderType())));
					oaDeliveryListTable.addCell(data.getSalesDocNum() == null ? pdfCell : new PdfPCell(new Phrase(data
							.getSalesDocNum())));
					oaDeliveryListTable.addCell(data.getLineItem() == null ? pdfCell : new PdfPCell(new Phrase(data
							.getLineItem())));
					oaDeliveryListTable.addCell(data.getCustPONum() == null ? pdfCell : new PdfPCell(new Phrase(data
							.getCustPONum())));
					oaDeliveryListTable.addCell(data.getDeliveryNum() == null ? pdfCell : new PdfPCell(new Phrase(data.getDeliveryNum())));
					oaDeliveryListTable.addCell(data.getDeliveryItemNum() == null ? pdfCell : new PdfPCell(new Phrase(data.getDeliveryItemNum())));
					oaDeliveryListTable.addCell(data.getActualShipmentDate() == null ? pdfCell : new PdfPCell(new Phrase(data.getActualShipmentDate())));
					oaDeliveryListTable.addCell(data.getFranchiseDesc() == null ? pdfCell : new PdfPCell(new Phrase(data.getFranchiseDesc())));
					oaDeliveryListTable.addCell(data.getProductCode() == null ? pdfCell : new PdfPCell(new Phrase(data.getProductCode())));
					oaDeliveryListTable.addCell(data.getProductDesc() == null ? pdfCell : new PdfPCell(new Phrase(data.getProductDesc())));
					oaDeliveryListTable.addCell(data.getUom() == null ? pdfCell : new PdfPCell(new Phrase(data.getUom())));
					oaDeliveryListTable.addCell(data.getDeliveryQuantity() == 0 ? pdfCell : new PdfPCell(new Phrase(data.getDeliveryQuantity())));
					
					oaDeliveryListTable.addCell(data.getOrderQuantity() == null ? pdfCell : new PdfPCell(new Phrase(data.getOrderQuantity())));
					oaDeliveryListTable.addCell(data.getBatchNum() == null ? pdfCell : new PdfPCell(new Phrase(data.getBatchNum())));
					oaDeliveryListTable.addCell(data.getBatchExpiryDate() == null ? pdfCell : new PdfPCell(new Phrase(data.getBatchExpiryDate())));
					oaDeliveryListTable.addCell(data.getSerialNum() == null ? pdfCell : new PdfPCell(new Phrase(data.getSerialNum())));
					oaDeliveryListTable.addCell(data.getPodDate() == null ? pdfCell : new PdfPCell(new Phrase(data.getPodDate())));
					oaDeliveryListTable.addCell(data.getShipToAccount() == null ? pdfCell : new PdfPCell(new Phrase(data.getShipToAccount())));
					oaDeliveryListTable.addCell(data.getShipToName() == null ? pdfCell : new PdfPCell(new Phrase(data.getShipToName())));
					oaDeliveryListTable.addCell(data.getConnote() == null ? pdfCell : new PdfPCell(new Phrase(data.getConnote())));
				}
				/*oaDeliveryListTable.addCell(pdfCell);
				oaDeliveryListTable.addCell(pdfCell);
				oaDeliveryListTable.addCell(pdfCell);
				oaDeliveryListTable.addCell(pdfCell);
				oaDeliveryListTable.addCell(pdfCell);
				oaDeliveryListTable.addCell(pdfCell);//TODO FOR WHAT??*/
				
			}
			
			
			document.add(imageTable);
			document.add(titleTable);
			document.add(searchCriteriaTable);
			document.add(oaDeliveryListTable);
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
