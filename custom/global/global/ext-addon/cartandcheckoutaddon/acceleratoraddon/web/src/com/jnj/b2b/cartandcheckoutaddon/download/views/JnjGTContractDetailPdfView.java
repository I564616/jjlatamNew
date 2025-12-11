package com.jnj.b2b.cartandcheckoutaddon.download.views;


import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.io.InputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonWebConstants;
import com.jnj.facades.data.JnjContractData;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjContractPriceData;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.servicelayer.i18n.I18NService;

import com.lowagie.text.Chunk;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Font;


/**
 * 
 * 
 */
public class JnjGTContractDetailPdfView extends AbstractPdfView {

	
	

	protected static final Logger LOG = Logger.getLogger(JnjGTContractDetailPdfView.class);
	protected static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";
	protected static final int MARGIN = 32;
	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	protected Document newDocument()	{
		return new Document(PageSize.A4.rotate());
	}
	
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
	
	private static final String CONTRACT_MYCONTRACTS = "contract.mycontracts";
	private static final String CONTRACT_CONTRACTNUMBER = "contract.number.header";
	private static final String CONTRACT_TENDERNUMBER = "contract.tendernumber.header";
	private static final String CONTRACT_INDIRECTCUSTOMER = "contract.indirectcustomer.number";
	private static final String CONTRACT_INDIRECTCUSTOMER_NAME = "contract.indirectcustomer.name";
	private static final String CONTRACT_EXPIRATIONDATE = "contract.expiration.date";
	private static final String CONTRACT_CONTRACTTYPE = "contract.type";
		
	private static final String CONTRACT_STATUS = "contract.status";
	private static final String CONTRACT_CREATION_DATE = "contract.creation.date";
	private static final String CONTRACT_LAST_TIME_UPDATED = "contract.last.updated";
	private static final String CONTRACT_PRODUCT_NAME = "contract.product.name";
	private static final String CONTRACT_UNIT_PRICE = "contract.unit.price";
		
	private static final String CONTRACT_BAL = "contract.balance";
	private static final String CONTRACT_CONSUMED = "contract.consumed";
	private static final String CONTRACT_QTY = "contract.quantity";
	private static final String CONTRACT_UNIT_MEASURE = "contract.unit.measurement";
		
	private static final String CONTRACT_TOTAL_CONTRACT_VALUE = "contract.total.value";
	private static final String CONTRACT_CONSUMED_VALUE = "contract.consumed.value";
	private static final String CONTRACT_BALANCE = "contract.balance";
	private static final String CONTRACT_ZCQ = "ZCQ";
	private static final String CONTRACT_ZCV = "ZCV";
	public static final String CONTRACT_LIST_TITLE = "MyContract List";
	public static final String CONTRACT_DETAIL_FILENAME = "contract.detail.name";
	public static final String CONTRACT_DETAIL_TITLE = "contract.details.header";
	//Modified by Archana for AAOL-5513
	private static final String DATE_FORMAT = "date.dateformat";
	
	protected static final String sheetName = "MyContract Details";
	
	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception	{
		try {
		//Modified by Archana for AAOL-5513	
      	final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
      	final SimpleDateFormat dateFormatHHMMSS = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());
      	response.setHeader("Content-Disposition", "attachment; filename=MyContractDetail.pdf");
      	
      	final JnjContractData contractData = (JnjContractData) map.get("contractData");
   		final List<JnjContractEntryData> jnjContractEntryDataList = (List<JnjContractEntryData>) map.get("contractEntryList");
   		final Map<String, JnjContractPriceData> cntrctEntryMap = (Map<String, JnjContractPriceData>) map.get("cntrctEntryMap");
   		final JnjContractPriceData  cntrctPriceData = (JnjContractPriceData) map.get("cntrctPriceData");
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
      	/*Image jnjConnectLogo = null;
      	Image jnjConnectLogo2 = null;
      	PdfPCell cell1 = null;
      	PdfPCell cell2 = null;*/
      	PdfPTable imageTable = new PdfPTable(2); // 3 columns.
      	imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
      	imageTable.setWidthPercentage(100);
      	imageTable.setLockedWidth(false);
      	imageTable.setSpacingAfter(30f);
      	
      	if ( jnjConnectLogoByteArray != null){
      		/*jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
      		cell1 = new PdfPCell(jnjConnectLogo, false);
      		cell1.setBorder(Rectangle.NO_BORDER);*/
      		imageTable.addCell(createImageCell(jnjConnectLogoByteArray,Element.ALIGN_LEFT));
      	}
      	
      	if ( jnjConnectLogoByteArray2 != null){
      		/*jnjConnectLogo2 = Image.getInstance(jnjConnectLogoByteArray2);
      		cell2 = new PdfPCell(jnjConnectLogo2, false);
      		cell2.setBorder(Rectangle.NO_BORDER);*/
      		imageTable.addCell(createImageCell(jnjConnectLogoByteArray2,Element.ALIGN_RIGHT));
      	}
      	/*imageTable.addCell(cell1);
      	imageTable.addCell(cell2);*/
      	//image adding end
      
      	final String currentSite = (String) map.get(Jnjb2bCoreConstants.SITE_NAME);
      	final boolean isMddSite = (currentSite != null && Jnjb2bCoreConstants.MDD.equals(currentSite)) ? true : false;
      	 //title adding start
         final PdfPTable titleTable = new PdfPTable(1);
      	titleTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
      	titleTable.setWidthPercentage(100);
       	titleTable.setLockedWidth(false);
       	titleTable.setSpacingAfter(10f);
       	
       	final PdfPCell titleCell = new PdfPCell();
       	titleCell.setColspan(6);
       	titleCell.setBorderWidthBottom(10f);
       	titleCell.setBorderWidthTop(5f);
       	titleCell.setBackgroundColor(Color.WHITE); 
       	titleCell.setBorder(Rectangle.NO_BORDER);
       	titleCell.setPhrase(new Phrase(messageSource.getMessage(CONTRACT_DETAIL_TITLE, null, i18nService.getCurrentLocale()), 
       			new Font(Font.HELVETICA, 14, Font.BOLD,Color.CYAN)));
         titleTable.addCell(titleCell);
    	
       //title adding end
    
         String[] headers_CONTRACT_ZCQ = new String[] {
        		 
        		 messageSource.getMessage(CONTRACT_PRODUCT_NAME, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_UNIT_PRICE, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_UNIT_MEASURE, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_QTY, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_CONSUMED, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_BAL, null, i18nService.getCurrentLocale()),
				 
          		 };
          
          String[] headers_CONTRACT_ZCV = new String[] {
        		  
        		messageSource.getMessage(CONTRACT_PRODUCT_NAME, null, i18nService.getCurrentLocale()),
          		messageSource.getMessage(CONTRACT_UNIT_PRICE, null, i18nService.getCurrentLocale()),
          		messageSource.getMessage(CONTRACT_TOTAL_CONTRACT_VALUE, null, i18nService.getCurrentLocale()),
          		messageSource.getMessage(CONTRACT_CONSUMED_VALUE, null, i18nService.getCurrentLocale()),
          		messageSource.getMessage(CONTRACT_BALANCE, null, i18nService.getCurrentLocale()),
          		
          	 };
       // create 6 column table
       PdfPTable orderTable = null;
      	if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCQ, null, i18nService.getCurrentLocale()))){
      		orderTable = new PdfPTable(headers_CONTRACT_ZCQ.length);
      		orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
      		orderTable.setWidthPercentage(100);
           	orderTable.setLockedWidth(false);
           	orderTable.setSpacingAfter(736f);
           	for (int i = 0; i < headers_CONTRACT_ZCQ.length; i++) {
                String header = headers_CONTRACT_ZCQ[i];
                orderTable.addCell(createLabelCell(header.toUpperCase(),0,0));
            }
      	}
   		
   		if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCV, null, i18nService.getCurrentLocale()))){
   			orderTable = new PdfPTable(headers_CONTRACT_ZCV.length);
   			orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
   			orderTable.setWidthPercentage(100);
           	orderTable.setLockedWidth(false);
           	orderTable.setSpacingAfter(736f);
           	for (int i = 0; i < headers_CONTRACT_ZCV.length; i++) {
                String header = headers_CONTRACT_ZCV[i];
                orderTable.addCell(createLabelCell(header.toUpperCase(),0,0));
            }
   		}
   		orderTable.completeRow();
   		
      	final String emptyField = new String();
      	if (jnjContractEntryDataList != null && !jnjContractEntryDataList.isEmpty()) {
      		for (final JnjContractEntryData jnjContractEntryData : jnjContractEntryDataList) {
      			orderTable.addCell(createValueCell(jnjContractEntryData.getProductCode() +"\n"+jnjContractEntryData.getProductName(),0,0));
			    orderTable.addCell(createValueCell(jnjContractEntryData.getContractProductPrice(),0,0));
   				if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCQ, null, i18nService.getCurrentLocale()))){
   					orderTable.addCell(createValueCell(jnjContractEntryData.getProdSalesUnit() != null ? jnjContractEntryData.getProdSalesUnit().toString() : StringUtils.EMPTY,0,0));
   			        orderTable.addCell(createValueCell(jnjContractEntryData.getContractQty() != null ? jnjContractEntryData.getContractQty().toString() : StringUtils.EMPTY,0,0));
   			        orderTable.addCell(createValueCell(jnjContractEntryData.getConsumedQty() != null ? jnjContractEntryData.getConsumedQty().toString() : StringUtils.EMPTY,0,0));
   			        orderTable.addCell(createValueCell(jnjContractEntryData.getContractBalanceQty() != null ? jnjContractEntryData.getContractBalanceQty().toString() : StringUtils.EMPTY,0,0));
   				}
   				
   				if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCV, null, i18nService.getCurrentLocale()))){
   					String prodCode = jnjContractEntryData.getProductCode();
   					JnjContractPriceData jnjContractPriceData =  cntrctEntryMap.get(prodCode);
   					 if(jnjContractPriceData != null){
   			            orderTable.addCell(createValueCell(jnjContractPriceData.getTotalAmount().getValue() != null ? jnjContractPriceData.getTotalAmount().getValue().toString() : StringUtils.EMPTY,0,0));
   			            orderTable.addCell(createValueCell(jnjContractPriceData.getConsumedAmount().getValue() != null ? jnjContractPriceData.getConsumedAmount().getValue().toString() : StringUtils.EMPTY,0,0));
   			            orderTable.addCell(createValueCell(jnjContractPriceData.getBalanceAmount().getValue() != null ? jnjContractPriceData.getBalanceAmount().getValue().toString() : StringUtils.EMPTY,0,0));
   					 } 
   				}
      		}
      	}
        final PdfPTable contractInfo = new PdfPTable(6);
        contractInfo.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        contractInfo.setWidthPercentage(100);
        contractInfo.setLockedWidth(false);
        contractInfo.setSpacingAfter(30f);
        //first row
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_CONTRACTNUMBER, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getEccContractNum(),5,0));
        ///second row
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_INDIRECTCUSTOMER, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getIndirectCustomer() != null ? contractData.getIndirectCustomer() : StringUtils.EMPTY,0,0));
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_STATUS, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getStatus() != null ? contractData.getStatus() : StringUtils.EMPTY,0,0));
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_CREATION_DATE, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getStartDate() != null ? dateFormat.format(contractData.getStartDate()).toString() :  StringUtils.EMPTY,0,0)); 
        ///third row
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_INDIRECTCUSTOMER_NAME, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getIndirectCustomerName() != null ? contractData.getIndirectCustomerName() : StringUtils.EMPTY,0,0)); 
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_TENDERNUMBER, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getTenderNum() != null ? contractData.getTenderNum() : StringUtils.EMPTY,0,0)); 
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_EXPIRATIONDATE, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getEndDate() != null ? dateFormat.format(contractData.getEndDate()).toString() :  StringUtils.EMPTY,0,0));
        //fourth row
        contractInfo.addCell(createValueCell(emptyField,2,0));
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_CONTRACTTYPE, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getContractOrderReason() != null ? contractData.getContractOrderReason() : StringUtils.EMPTY,0,0));
        contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_LAST_TIME_UPDATED, null, i18nService.getCurrentLocale()),0,0));
        contractInfo.addCell(createValueCell(contractData.getLastUpdatedTime() != null ? dateFormatHHMMSS.format(contractData.getLastUpdatedTime()).toString() :  StringUtils.EMPTY,0,0));
        
         if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCV, null, i18nService.getCurrentLocale()))){
         	if (cntrctPriceData != null) {
         		//fifth row
         		contractInfo.addCell(createValueCell(emptyField,4,0));
                contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_TOTAL_CONTRACT_VALUE, null, i18nService.getCurrentLocale()),0,0));
                contractInfo.addCell(createValueCell(cntrctPriceData.getTotalAmount().getValue() != null ? cntrctPriceData.getTotalAmount().getValue().toString() : StringUtils.EMPTY,0,0));
               //sixth row
                contractInfo.addCell(createValueCell(emptyField,4,0));
                contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_CONSUMED_VALUE, null, i18nService.getCurrentLocale()),0,0));
                contractInfo.addCell(createValueCell(cntrctPriceData.getConsumedAmount().getValue() != null ? cntrctPriceData.getConsumedAmount().getValue().toString() : StringUtils.EMPTY,0,0));
               //seventh row
                contractInfo.addCell(createValueCell(emptyField,4,0));
                contractInfo.addCell(createLabelCell(messageSource.getMessage(CONTRACT_BALANCE, null, i18nService.getCurrentLocale()),0,0));
                contractInfo.addCell(createValueCell(cntrctPriceData.getBalanceAmount().getValue() != null ? cntrctPriceData.getBalanceAmount().getValue().toString() : StringUtils.EMPTY,0,0));
         		}
         	}
         //complete row
         contractInfo.completeRow();
         
         
        document.add(imageTable); 
      	document.add(titleTable);
      	document.add(Chunk.NEWLINE);
      	document.add(contractInfo);
      	document.add(Chunk.NEWLINE);
      	document.add(orderTable);
      }
      catch (final Exception e) {
      	e.printStackTrace();
      	LOG.error("error while setting table - " + e.getMessage());
      }
	}
	

	

	//create cells
	protected  PdfPCell createLabelCell(String text,int colspan, int rospan){
	      // font
	      Font font = new Font(Font.HELVETICA, 8, Font.BOLD, Color.BLACK);

	      // create cell
	      PdfPCell cell = new PdfPCell(new Phrase(text,font));
	      cell.setColspan(colspan);
	      cell.setBackgroundColor(Color.GRAY);
	      // set style
	      labelCellStyle(cell);
	      return cell;
	  }

	  // create cells
	protected  PdfPCell createValueCell(String text, int colspan, int rospan){
	      // font
	      Font font = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
	      // create cell
	      PdfPCell cell = new PdfPCell(new Phrase(text,font));
	      cell.setColspan(colspan);
	      cell.setBackgroundColor(Color.WHITE);
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
	    protected static class MyPageEvents extends PdfPageEventHelper {   
  	 	
	    	protected MessageSourceAccessor messageSourceAccessor;   
		  
		     
	    	protected PdfContentByte cb;   
		   
	    	protected PdfTemplate template;   
		  
		     
	    	protected BaseFont bf = null;   
		          
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
