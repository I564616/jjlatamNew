package com.jnj.b2b.cartandcheckoutaddon.download.views;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.servicelayer.i18n.I18NService;

/**
 * Generates pdf view on order confirmation page for Consignment Fill up and Consignment Charge
 * 
 */
public class JnjGTConsignmentOrderConfirmationPdfView extends AbstractPdfView
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

	protected static final Logger LOG = Logger.getLogger(JnjGTConsignmentOrderConfirmationPdfView.class);
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	protected static final int MARGIN = 32;
	
	
	private static final String FONT = "fonts/FreeSans.ttf";
	
	
	
	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */

	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter writer,	final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{

		try
		{
			 
			response.setHeader("Content-Disposition", "attachment; filename=OrderConfirmation.pdf");
			
			
			
			final JnjGTOrderData orderData = (JnjGTOrderData) map.get("orderData");
			final List<OrderEntryData> orderEntryList = (List<OrderEntryData>) map.get("orderEntryList");
			 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
			 writer.setPageEvent(events);   
	       events.onOpenDocument(writer, document);
			
			
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
	      	imageTable.setTotalWidth(512F);
	      	imageTable.setLockedWidth(true);
	      	imageTable.setSpacingAfter(5f);
	      	
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
	      	imageTable.addCell(createImageCell(cell1,Element.ALIGN_LEFT));
	      	imageTable.addCell(createImageCell(cell2,Element.ALIGN_RIGHT));
	      	//image adding end
	      	document.add(imageTable);
	      	 //insertOrderDetailsintoPDF(orderData, document,orderEntryList);

	 		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
	 		String date = simpleDateFormat.format(new Date());
	 		final String emptyField = new String();
	 		Font boldFont = FontFactory.getFont(FONT, 11, Font.BOLD);
	       	
	 		//Table for order details
	 		final PdfPTable orderTable = new PdfPTable(2);

			orderTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			orderTable.setTotalWidth(512f);
			orderTable.setLockedWidth(true);
			orderTable.setSpacingAfter(10f);
			orderTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			final PdfPCell orderTypeField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.ORDERTYPE, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell pdfDownloadDate = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.DOWNLOAD_DATE, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell customerPOField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CUSTOMER_PO_NUMBER, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell orderNumberField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.JJORDERNUMBER, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell poDateField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.PO_DATE, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell requestedDeliveryDateField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.REQUIRED_DELIVERY_DATE, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell stockLocationAccountField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.STOCK_LOCATION_ACCOUNT, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell endUserField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.END_USER, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell shippingInstructionsField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.SHIPPING_INSTRUCTIONS, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell overallStatusField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.OVERALL_STATUS, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell shipToAddressField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.SHIP_TO_ADDRESS, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell billToAddressField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.BILL_TO_ADDRESS, null, i18nService.getCurrentLocale()),boldFont));
			final PdfPCell returnCreatedDateField = new PdfPCell(new Phrase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.RETURN_CREATED_DATE, null, i18nService.getCurrentLocale()),boldFont));
			
			

			//Field name and its value
			orderTable.addCell(fieldCellStyle(orderTypeField));
			orderTable.addCell(": "+((orderData.getOrderType() != null) ? orderData.getOrderType() : emptyField)+"\n");
			
			orderTable.addCell(fieldCellStyle(pdfDownloadDate));
			orderTable.addCell(": "+date+"\n");
			
			orderTable.addCell(fieldCellStyle(customerPOField));
			orderTable.addCell(": "+((orderData.getPurchaseOrderNumber() != null) ? orderData.getPurchaseOrderNumber() : emptyField)+"\n");
			
			orderTable.addCell(fieldCellStyle(orderNumberField));
			if (null != orderData.getSapOrderNumber()){
				orderTable.addCell(": "+((orderData.getSapOrderNumber()!= null) ? orderData.getSapOrderNumber() : emptyField)+"\n");
			}else{
				orderTable.addCell(": "+((orderData.getOrderNumber()!= null) ? orderData.getOrderNumber() : emptyField)+"\n");
			}
			
			orderTable.addCell(fieldCellStyle(poDateField));
			orderTable.addCell(": "+((orderData.getPoDate()!= null) ? simpleDateFormat.format(orderData.getPoDate()) : emptyField)+"\n");
			
			if(orderData.getOrderType() != null && orderData.getOrderType().equalsIgnoreCase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_FILLUP_ORDER, null, i18nService.getCurrentLocale()))){				
				orderTable.addCell(fieldCellStyle(requestedDeliveryDateField));
				orderTable.addCell(": "+((orderData.getRequestedDeliveryDate()!= null) ? simpleDateFormat.format(orderData.getRequestedDeliveryDate()) : emptyField)+"\n");
			}else if(orderData.getOrderType() != null && orderData.getOrderType().equalsIgnoreCase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_RETURN_ORDER, null, i18nService.getCurrentLocale()))){
				orderTable.addCell(fieldCellStyle(returnCreatedDateField));
				orderTable.addCell(": "+((orderData.getCreated()!= null) ? simpleDateFormat.format(orderData.getCreatedOn()) : emptyField)+"\n");
			}else{}

			orderTable.addCell(fieldCellStyle(stockLocationAccountField));
			orderTable.addCell(": "+((orderData.getStockUser()!= null) ? orderData.getStockUser() : emptyField)+"\n");
			
			orderTable.addCell(fieldCellStyle(endUserField));
			orderTable.addCell(": "+((orderData.getEndUser()!= null) ? orderData.getEndUser() : emptyField)+"\n");
			
			orderTable.addCell(fieldCellStyle(shippingInstructionsField));
			if(orderData.getOrderType().equalsIgnoreCase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_RETURN_ORDER, null, i18nService.getCurrentLocale())) && (null == orderData.getShippingInstructions()  || orderData.getShippingInstructions().equalsIgnoreCase(emptyField))){
				orderTable.addCell(": "+messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.NOT_AVAILABLE, null, i18nService.getCurrentLocale())+"\n");
			}else{				
				orderTable.addCell(": "+((orderData.getShippingInstructions()!= null) ? orderData.getShippingInstructions() : emptyField)+"\n");
			}
			
			orderTable.addCell(fieldCellStyle(overallStatusField));
			orderTable.addCell(": "+((orderData.getStatusDisplay()!= null) ? orderData.getStatusDisplay() : emptyField)+"\n");

	 		orderTable.addCell(fieldCellStyle(shipToAddressField));
	 		Phrase shipPhrase = new Phrase();
	 		shipPhrase.add(": ");
	 		if(orderData.getDeliveryAddress()!= null){
	 			if(orderData.getDeliveryAddress().getFirstName() != null){
	 				shipPhrase.add(orderData.getDeliveryAddress().getFirstName());
	 			}
	 			if(orderData.getDeliveryAddress().getLastName() != null){
	 				shipPhrase.add(orderData.getDeliveryAddress().getLastName());
	 			}
	 			if(orderData.getDeliveryAddress().getFormattedAddress() != null){
	 				shipPhrase.add(orderData.getDeliveryAddress().getFormattedAddress());
	 			}
	 		}
	 		StringBuffer deliveryAddress = new StringBuffer();
	 		if(null != orderData.getDeliveryAddress()){
	 			if(orderData.getDeliveryAddress().getFirstName() != null)
	 				deliveryAddress.append(orderData.getDeliveryAddress().getFirstName()+" ");
	 			if(orderData.getDeliveryAddress().getLastName() != null)
	 				deliveryAddress.append(orderData.getDeliveryAddress().getLastName()+" ");
	 			if(orderData.getDeliveryAddress().getFormattedAddress() != null)
	 				deliveryAddress.append(orderData.getDeliveryAddress().getFormattedAddress());
	 		}else{
	 			deliveryAddress.append(emptyField);
	 		}
	 		orderTable.addCell(": "+deliveryAddress+"\n");
	 		
	 		orderTable.addCell(fieldCellStyle(billToAddressField));
	 		StringBuffer billingAddress = new StringBuffer();
	 		if(null != orderData.getBillingAddress()){
	 			if(orderData.getBillingAddress().getFirstName() != null)
	 				billingAddress.append(orderData.getBillingAddress().getFirstName()+" ");
	 			if(orderData.getBillingAddress().getLastName() != null)
	 				billingAddress.append(orderData.getBillingAddress().getLastName()+" ");
	 			if(orderData.getBillingAddress().getFormattedAddress() != null)
	 				billingAddress.append(orderData.getBillingAddress().getFormattedAddress());
	 		}else{
	 			billingAddress.append(emptyField);
	 		}
	 		orderTable.addCell(": "+billingAddress+"\n");
	 		
	 		
	 		document.add(new Paragraph("\n"));
	 		document.add(new Paragraph("\n"));
	 		document.add(orderTable);
	 		document.add(new Paragraph("\n"));
	 		if(orderData.getOrderType().equalsIgnoreCase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_CHARGE_ORDER, null, i18nService.getCurrentLocale()))){
	 			createSecondLevelData(document);
	 		}
	 		
	 		//Table for Order entry details			
	 		final PdfPCell lineNumberColumn = new PdfPCell(new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.LINE_NUMBER, null, i18nService.getCurrentLocale()),boldFont));
	 		final PdfPCell productNameColumn = new PdfPCell(new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.PRODUCT, null, i18nService.getCurrentLocale()),boldFont));
	 		final PdfPCell productQuantityColumn = new PdfPCell(new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.QUANTITY, null, i18nService.getCurrentLocale()),boldFont));
	 		final PdfPCell unitColumn = new PdfPCell(new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.UNIT, null, i18nService.getCurrentLocale()),boldFont));
	 		final PdfPCell batchNumColumn = new PdfPCell(new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.BATCH_NUM,
	 				null, i18nService.getCurrentLocale()),boldFont));
	 		final PdfPCell serialNumColumn = new PdfPCell(new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.SERIAL_NUM,
	 				null, i18nService.getCurrentLocale()),boldFont));
	 		final PdfPCell unitPriceColumn = new PdfPCell(new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.UNIT_PRICE,
	 				null, i18nService.getCurrentLocale()),boldFont));
	 		final PdfPCell totalPriceColumn = new PdfPCell(new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.TOTAL_PRICE,
	 				null, i18nService.getCurrentLocale()),boldFont));
	 		
	 		PdfPTable productsTable = null;
	 		if(orderData.getOrderType().equalsIgnoreCase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_CHARGE_ORDER, null, i18nService.getCurrentLocale()))){
	 			productsTable = new PdfPTable(8);
	 		}
	 		else{
	 			productsTable = new PdfPTable(4);
	 		}

	 		productsTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
	 		productsTable.setTotalWidth(512f);
	 		productsTable.setLockedWidth(true);
	 		productsTable.setSpacingAfter(10f);
	 		
	 		//Column headers for Order entry details
	 		productsTable.addCell(headerCellStyle(lineNumberColumn));
	 		productsTable.addCell(headerCellStyle(productNameColumn));
	 		productsTable.addCell(headerCellStyle(productQuantityColumn));
	 		productsTable.addCell(headerCellStyle(unitColumn));
	 		if(orderData.getOrderType().equalsIgnoreCase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_CHARGE_ORDER, null, i18nService.getCurrentLocale()))){
	 			productsTable.addCell(headerCellStyle(batchNumColumn));
	 			productsTable.addCell(headerCellStyle(serialNumColumn));
	 			productsTable.addCell(headerCellStyle(unitPriceColumn));
	 			productsTable.addCell(headerCellStyle(totalPriceColumn));
	 		}
	 		
	 		
	 		

			//Row values for Order entry details
			for (final OrderEntryData orderEntryData : orderEntryList)
			{
				final JnjGTProductData product = (JnjGTProductData) orderEntryData.getProduct();
				productsTable.addCell(String.valueOf(orderEntryData.getEntryNumber()+1));
				StringBuffer productName = new StringBuffer();
				if(orderEntryData.getProduct() != null){
					productName.append(orderEntryData.getProduct().getName()+"\n");
					productName.append("J&J ID# :"+(orderEntryData.getProduct().getCode()));
					
				}
				productsTable.addCell(productName.toString());
				productsTable.addCell(String.valueOf(orderEntryData.getQuantity()));
				StringBuffer unitName = new StringBuffer();
				unitName.append(product.getDeliveryUnit() != null ?product.getDeliveryUnit():"");
				unitName.append(" (");
				unitName.append( product.getNumerator() != null ?product.getNumerator() :"");
				unitName.append(product.getSalesUnit() != null ?product.getSalesUnit() :"");
				unitName.append(")");
				productsTable.addCell(unitName.toString());
				if(orderData.getOrderType().equalsIgnoreCase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_CHARGE_ORDER, null, i18nService.getCurrentLocale()))){
					if(orderEntryData instanceof JnjGTOrderEntryData){
						productsTable.addCell(((JnjGTOrderEntryData) orderEntryData).getBatchNumber());
						productsTable.addCell(((JnjGTOrderEntryData) orderEntryData).getSerialNumber());
						productsTable.addCell(orderEntryData.getBasePrice().getFormattedValue());
						productsTable.addCell(orderEntryData.getTotalPrice().getFormattedValue());
					}
					
				}
			}

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Setting Table in Document");
			}


			document.add(productsTable);
			document.close();
		}
		
		

		catch (final Exception exp)
		{
			exp.printStackTrace();
			LOG.error("error while setting table - " + exp.getMessage());
		}


	}
	
	/**
	    * @param path
	    * @param align
	    * @return
	    * @throws Exception
	    */
	   public static PdfPCell createImageCell(PdfPCell cell, int align) throws Exception {
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

	public void insertOrderDetailsintoPDF(JnjGTOrderData orderData, Document document, List<OrderEntryData> orderEntryList) throws DocumentException, BusinessException {}

	private void createSecondLevelData(final Document document) throws DocumentException, BusinessException
	{
		Font boldFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,10, Font.BOLD);
		Paragraph textOnePara = new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_NOTE_1,
				null,i18nService.getCurrentLocale()),boldFont);
		textOnePara.setAlignment(Element.ALIGN_CENTER);;
		
	//	Paragraph para1 = new Paragraph(messageSource.getMessage(NOTE,i18nService.getCurrentLocale()),boldFont);
		final PdfPTable message1 = new PdfPTable(1);
		//var.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		
		PdfPCell content=new PdfPCell();
		content.setBorder(Rectangle.NO_BORDER);
		content.addElement(textOnePara);
		message1.addCell(content);
		document.add(message1);
		document.add(new Paragraph("\n"));
		
		Paragraph textTwoPara = new Paragraph(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_NOTE_2,
				null,i18nService.getCurrentLocale()),boldFont);
		textTwoPara.setAlignment(Element.ALIGN_CENTER);;
		
		final PdfPTable message2 = new PdfPTable(1);
		//var.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		
		PdfPCell content2=new PdfPCell();
		content2.setBorder(Rectangle.NO_BORDER);
		content2.addElement(textTwoPara);
		message2.addCell(content2);
		document.add(message2);
		document.add(new Paragraph("\n"));
		
	}


	private PdfPCell headerCellStyle(PdfPCell cell) {
				// alignment
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// padding
				cell.setPaddingTop(0f);
				cell.setPaddingBottom(7f);
				// background color
				cell.setBackgroundColor(new java.awt.Color(220,240,247));
				// border
				cell.setBorder(Rectangle.BOX);
			return cell;
		}
		private PdfPCell fieldCellStyle(PdfPCell cell) {
			// alignment
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			// border
			cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}



		private static class MyPageEvents extends PdfPageEventHelper {   
	 	
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
 * @return the jnjCommonFacadeUtil
 */
public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
{
	return jnjCommonFacadeUtil;
}

/**
 * @param jnjCommonFacadeUtil
 *           the jnjCommonFacadeUtil to set
 */
public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
{
	this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
}
 
}
