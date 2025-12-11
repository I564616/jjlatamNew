/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjOrderStatusNotificationProcessModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.services.CMSSiteService;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;


/**
 * TODO:<Akash-class level comments are missing>.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderStatusNotificationEmailContext extends CustomerEmailContext
{
	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjOrderStatusNotificationEmailContext.class);

	private static final String ORDER_DETAIL_PAGE_REL_PATH = "/order-history/order/";
	public static final String EMAIL_ATTACHMENT_PATH = "attachmentPath";
	public static final String EMAIL_ATTACHMENT_FILE_NAME = "attachmentFileName";
	public static final String USE_DIRECT_PATH = "useDirectPath";
	private static final String NONE = "None";
	
	
	
	private static final String FONT = "fonts/FreeSans.ttf";
	String GOOrderNumber = null;

	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;

	@Autowired
	private CMSSiteService cmsSiteService;	

	@Autowired
	private MessageService messageService;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private CatalogVersionService catalogVersionService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	private I18NService i18nService;

	/**
	 * Order status before processing.
	 */
	private String previousStatus;

	/**
	 * Order status post processing.
	 */
	private String currentStatus;

	/**
	 * JnJ Order Number.
	 */
	private String jnjOrderNumber;

	/**
	 * SAP based/originated Order Number.
	 */
	private String sapOrderNumber;

	/**
	 * Client Order/PO Number.
	 */
	private String clientOrderNumber;

	/**
	 * Base URL for the BR/MX Site.
	 */
	private String baseUrl;


	/**
	 * Media URL for the Site.
	 */
	private String mediaUrl;

	/**
	 * The Order Data
	 */
	private JnjGTOrderData orderData;
	
	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl()
	{
		return mediaUrl;
	}

	/**
	 * @param mediaUrl
	 *           the mediaUrl to set
	 */
	public void setMediaUrl(final String mediaUrl)
	{
		this.mediaUrl = mediaUrl;
	}

	/**
	 * Indicates if the email trigger is from Synchronize Order or other flow.
	 */
	private boolean syncOrderNotification;
	
	

	/* (non-Javadoc)
	 * @see com.jnj.facades.process.email.context.CustomerEmailContext#init(de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel, de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel)
	 */
	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel) {
		final String METHOD_INIT = "JnjOrderStatusNotificationEmailContext - init()";
		JnJB2bCustomerModel currentUser = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_INIT + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof JnjOrderStatusNotificationProcessModel) {
			final JnjOrderStatusNotificationProcessModel orderStatusNotificationProcessModel = (JnjOrderStatusNotificationProcessModel) businessProcessModel;
			String locale;
			if(null != i18nService.getCurrentLocale()){
				locale = i18nService.getCurrentLocale().toString();
			}else{
				locale = Locale.ENGLISH.toString();
			}
			if (null != orderStatusNotificationProcessModel.getOrder()) {
				final OrderModel order = orderStatusNotificationProcessModel.getOrder();
				put("userFullName", order.getUser().getName());
				final OrderData populatedOrderData = orderConverter.convert(order, new JnjGTOrderData());
				if (populatedOrderData instanceof JnjGTOrderData) {
					final JnjGTOrderData orderData = (JnjGTOrderData) populatedOrderData;
					if (JnjOrderTypesEnum.ZOR.equals(order.getOrderType()) || JnjOrderTypesEnum.KA.equals(order.getOrderType())||JnjOrderTypesEnum.KB.equals(order.getOrderType())||JnjOrderTypesEnum.KE.equals(order.getOrderType())) {
						String fileName = Config.getParameter(Jnjb2bCoreConstants.OrderPdf.PDFFILENAME+"."+locale);
						try {
							generatePDF(orderData, new Document(), new HashMap<String, Object>(),fileName,locale);
						} catch (DocumentException documentException) {
							documentException.getMessage();
						} catch (Exception exception) {
							exception.getMessage();
						}
					}
					setOrderData(orderData);
					setClientOrderNumber(orderData.getCode());
					CommonUtil.logDebugMessage("Order Status Notification", "init()","Order Data Has been set in the JnjOrderStatusNotificationEmailContext!", LOGGER);
				}
			}
			else {
				setPreviousStatus(orderStatusNotificationProcessModel.getPreviousStatus() == null ? NONE : orderStatusNotificationProcessModel.getPreviousStatus().toString());
				setCurrentStatus(orderStatusNotificationProcessModel.getCurrentStatus() == null ? NONE : orderStatusNotificationProcessModel.getCurrentStatus().toString());
				setSapOrderNumber(orderStatusNotificationProcessModel.getSapOrderNumber());
				setJnjOrderNumber(orderStatusNotificationProcessModel.getJnjOrderNumber());
				CommonUtil.logDebugMessage("Order Status Notification", "init()", "Order not found. Order Data Has been set from the process in the JnjOrderStatusNotificationEmailContext!", LOGGER);
			}
			setMediaUrl(orderStatusNotificationProcessModel.getMediaUrl());
			put(FROM_EMAIL, Config.getParameter("user.registration.from.address"));
			CommonUtil.logDebugMessage("Order Status Notification - Email", "init()", Config.getParameter("user.registration.from.address"), LOGGER);
			put(FROM_DISPLAY_NAME, "Johnson & Johnson"); // TODO
			
			/** Setting the To Email address AAOL 4911**/
			final String toEmailID = orderStatusNotificationProcessModel.getToEmail();
			put(EMAIL, toEmailID);
			
			if (null != orderStatusNotificationProcessModel.getSyncOrderNotification()) {
				syncOrderNotification = orderStatusNotificationProcessModel.getSyncOrderNotification().booleanValue();
			}
			if (!syncOrderNotification) {
				setBaseUrl(orderStatusNotificationProcessModel.getBaseUrl());
			}
		}else{
			CommonUtil.logDebugMessage("Order Status Notification", "init()", "Order Data Has not been set in the JnjOrderStatusNotificationEmailContext!", LOGGER);	
		}
	}
	
	/**
	 * This is method where we can start creating the pdf file.
	 * 
	 * @param order
	 * @param document
	 * @param map
	 * @param fileName
	 * @param locale 
	 * @throws Exception
	 */
	private void generatePDF(JnjGTOrderData orderData,final Document document,final Map<String, Object> map,final String fileName, String locale) throws Exception {
		ByteArrayOutputStream baos = null;
		OutputStream fileOS = null;
		try	{
			if(JnjOrderTypesEnum.ZOR.getCode().equals(orderData.getOrderType()))
				document.setPageSize(PageSize.A4.rotate());
			else
				document.setPageSize(PageSize.A4);
			baos = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			System.out.println("*****************" + writer +"*****************************");
			document.open();
			
			try {
				List<ContentCatalogModel> catologLst =cmsSiteService.getCurrentSite().getContentCatalogs();
	            byte[] jnjConnectLogoByteArray = null;
	            byte[] jnjConnectLogoByteArray2 = null;
	    		if (catologLst != null && catologLst.size() > 0) {
	    			MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE),Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
	    			MediaModel mediaModel = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE),Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
	    			InputStream inputStream2 = null;
	    			if (mediaModel2 != null) {
	    				try {
	    					inputStream2 = mediaService.getStreamFromMedia(mediaModel2);
	    					if(inputStream2!=null) {
	    						jnjConnectLogoByteArray2 = IOUtils.toByteArray(inputStream2);
	    					}
	    				}
	    				catch( Exception e) {
	    					LOGGER.error("exception occured pdf"+e.getMessage());
	    				}
	    				finally	{
	    					if(inputStream2!=null) {
	    						inputStream2.close();
	    					}
	    				}
	    			}
	    			InputStream inputStream1 = null;
	    			if (mediaModel != null) {
	    				try {
	    					inputStream1 = mediaService.getStreamFromMedia(mediaModel);
	    					if(inputStream1!=null) {
	    						jnjConnectLogoByteArray = IOUtils.toByteArray(inputStream1);
	    					}
	    				}
	    				catch( Exception e) {
	    					LOGGER.error("exception occured pdf"+e.getMessage());
	    				}
	    				finally	{
	    					if(inputStream1!=null) {
	    						inputStream1.close();
	    					}
	    				}
	    			}
	    			
	    		}
	    		PdfPTable table;
	    		if(JnjOrderTypesEnum.ZOR.getCode().equals(orderData.getOrderType())){	
	    			table = new PdfPTable(3); // 3 columns.
	    		}
	    		else{	    			
	    			table = new PdfPTable(2); // 2 columns
	    		}
				table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		      	table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		      	if(JnjOrderTypesEnum.ZOR.getCode().equals(orderData.getOrderType())){
		      		table.setTotalWidth(822F);
		      		table.setSpacingAfter(15f);
		      	}
				else{
					table.setTotalWidth(512F);
					table.setSpacingAfter(5f);
				}
		      	table.setLockedWidth(true);
		      	
		      	 if ( jnjConnectLogoByteArray != null){
			      		table.addCell(createImageCell(jnjConnectLogoByteArray,Element.ALIGN_LEFT));
			      	}
		     	if(JnjOrderTypesEnum.ZOR.getCode().equals(orderData.getOrderType())){
				 table.addCell(new Paragraph());
		     	}else{}
			      	if ( jnjConnectLogoByteArray2 != null){
			      		table.addCell(createImageCell(jnjConnectLogoByteArray2,Element.ALIGN_RIGHT));
			      	}
			      	Font underlinedFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,11, Font.BOLD | Font.UNDERLINE);
	    			underlinedFont.setColor(new Color(0x356B71));
			      	if(JnjOrderTypesEnum.ZOR.getCode().equals(orderData.getOrderType())){			      		
			      		table.addCell(new Paragraph());
			      		Paragraph para=new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.ORDERCONFIRMATION+"."+locale),underlinedFont);
			      		para.setAlignment(Element.ALIGN_CENTER);
			      		table.addCell(para);
			      		table.addCell(new Paragraph());
			      	}
			      	document.add(table);
	    		
				
			}
			catch (Exception ex) {
				LOGGER.error("Exception occurred in JnjOrderStatusNotificationEmailContext.getShippingMethod() due to "
						+ ex);
			}
			if(JnjOrderTypesEnum.ZOR.getCode().equals(orderData.getOrderType()))
				generatePdfDocument(orderData, document, map,locale);
			else
				generatedConsignmentPdfDocument(orderData, document, map,locale);
			document.close();
			if (baos.size() >0) {
				final File file = File.createTempFile(fileName, ".pdf");
				fileOS = new FileOutputStream(file);
				fileOS.write(baos.toByteArray());
				LOGGER.debug("**********Attachment file name***********" + file.getName());
			
				LOGGER.debug("**********Attachment file name***********" + file.getPath());
				put(EMAIL_ATTACHMENT_FILE_NAME, file.getName());
				put("orderNumber", fileName);
				put(EMAIL_ATTACHMENT_PATH, file.getPath().replace(file.getName(), ""));
				LOGGER.debug("**********Attachment file name***********" + file.getName());
				put(USE_DIRECT_PATH, Boolean.TRUE);
			}
		}
		catch(Exception e) {
			LOGGER.info("Exception occured while attaching excel in email " + e.getMessage());
		}

		finally{
			try {				
				if (baos != null) {
					baos.close();
				}				
				if (null != fileOS ) {
					fileOS.close();
				}
			} catch (IOException excep) {
				LOGGER.error("Exception occurred in JnjOrderStatusNotificationEmailContext.generatePDF() due to "+excep);
			}
		}
	}

	/**
	 * Generates PDF document on 
	 * @param orderData
	 * @param document
	 * @param map
	 * @param locale 
	 * @throws DocumentException
	 * @throws BusinessException
	 */
	private void generatedConsignmentPdfDocument(JnjGTOrderData orderData, Document document,
			Map<String, Object> map, String locale) throws DocumentException, BusinessException {
		final List<OrderEntryData> orderEntryList = orderData.getEntries();
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

 		final PdfPCell orderTypeField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.ORDERTYPE+"."+locale),boldFont));
 		final PdfPCell pdfDownloadDate = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.DOWNLOAD_DATE+"."+locale),boldFont));
 		final PdfPCell customerPOField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.CUSTOMER_PO_NUMBER+"."+locale),boldFont));
 		final PdfPCell orderNumberField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.JJORDERNUMBER+"."+locale),boldFont));
 		final PdfPCell poDateField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.PO_DATE+"."+locale),boldFont));
 		final PdfPCell requestedDeliveryDateField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.REQUIRED_DELIVERY_DATE+"."+locale),boldFont));
 		final PdfPCell stockLocationAccountField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.STOCK_LOCATION_ACCOUNT+"."+locale),boldFont));
 		final PdfPCell endUserField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.END_USER+"."+locale),boldFont));
 		final PdfPCell shippingInstructionsField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.SHIPPING_INSTRUCTIONS+"."+locale),boldFont));
 		final PdfPCell overallStatusField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.OVERALL_STATUS+"."+locale),boldFont));
 		final PdfPCell shipToAddressField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.SHIP_TO_ADDRESS+"."+locale),boldFont));
 		final PdfPCell billToAddressField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.BILL_TO_ADDRESS+"."+locale),boldFont));
 		final PdfPCell returnCreatedDateField = new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.RETURN_CREATED_DATE+"."+locale),boldFont));
 		
 		

 		//Field name and its value
 		orderTable.addCell(fieldCellStyle(orderTypeField));
 		orderTable.addCell(": "+((orderData.getOrderType() != null) ? Config.getParameter(Jnjb2bCoreConstants.Cart.ORDER_TYPE_CONSTANT + "."+ ((JnjGTOrderData) orderData).getOrderType().toString()+"."+locale) : emptyField)+"\n");
 		
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
 		
 		//if(orderData.getOrderType() != null && orderData.getOrderType().equalsIgnoreCase(messageSource.getMessage(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_FILLUP_ORDER, null, i18nService.getCurrentLocale()))){				
 		if(orderData.getOrderType() != null && orderData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KB.getCode())){	
 			orderTable.addCell(fieldCellStyle(requestedDeliveryDateField));
 			orderTable.addCell(": "+((orderData.getCreated()!= null) ? simpleDateFormat.format(orderData.getCreated()) : emptyField)+"\n");
 		}else if(orderData.getOrderType() != null && orderData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KA.getCode())){
 			orderTable.addCell(fieldCellStyle(returnCreatedDateField));
 			orderTable.addCell(": "+((orderData.getCreated()!= null) ? simpleDateFormat.format(orderData.getCreated()) : emptyField)+"\n");
 		}else{}

 		orderTable.addCell(fieldCellStyle(stockLocationAccountField));
 		orderTable.addCell(": "+((orderData.getStockUser()!= null) ? orderData.getStockUser() : emptyField)+"\n");
 		
 		orderTable.addCell(fieldCellStyle(endUserField));
 		orderTable.addCell(": "+((orderData.getEndUser()!= null) ? orderData.getEndUser() : emptyField)+"\n");
 		
 		orderTable.addCell(fieldCellStyle(shippingInstructionsField));
 		if(orderData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KA.getCode()) && (null == orderData.getShippingInstructions()  || (orderData.getShippingInstructions().trim()).equalsIgnoreCase(emptyField))){
 			orderTable.addCell(": "+Config.getParameter(Jnjb2bCoreConstants.OrderPdf.NOT_AVAILABLE+"."+locale)+"\n");
 		}else{				
 			orderTable.addCell(": "+((orderData.getShippingInstructions()!= null) ? orderData.getShippingInstructions() : emptyField)+"\n");
 		}
 		
 		orderTable.addCell(fieldCellStyle(overallStatusField));
 		orderTable.addCell(": "+((orderData.getStatusDisplay()!= null) ? orderData.getStatusDisplay() : emptyField)+"\n");
		orderTable.addCell(fieldCellStyle(shipToAddressField));
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
 		if(orderData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KE.getCode())){
 			createSecondLevelConsignmentData(document,locale);
 		}
 		
 		//Table for Order entry details			
 		final PdfPCell lineNumberColumn = new PdfPCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.LINE_NUMBER+"."+locale),boldFont));
 		final PdfPCell productNameColumn = new PdfPCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.PRODUCT+"."+locale),boldFont));
 		final PdfPCell productQuantityColumn = new PdfPCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.QUANTITY+"."+locale),boldFont));
 		final PdfPCell unitColumn = new PdfPCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.UNIT+"."+locale),boldFont));
 		final PdfPCell batchNumColumn = new PdfPCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.BATCH_NUM+"."+locale),boldFont));
 		final PdfPCell serialNumColumn = new PdfPCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.SERIAL_NUM+"."+locale),boldFont));
 		final PdfPCell unitPriceColumn = new PdfPCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.UNIT_PRICE+"."+locale),boldFont));
 		final PdfPCell totalPriceColumn = new PdfPCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.TOTAL_PRICE+"."+locale
 				 ),boldFont));
 		
 		PdfPTable productsTable = null;
 		if(orderData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KE.getCode())){
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
 		if(orderData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KE.getCode())){
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
 			if(orderData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KE.getCode())){
 				if(orderEntryData instanceof JnjGTOrderEntryData){
 					productsTable.addCell(((JnjGTOrderEntryData) orderEntryData).getBatchNumber());
 					productsTable.addCell(((JnjGTOrderEntryData) orderEntryData).getSerialNumber());
 					productsTable.addCell(orderEntryData.getBasePrice().getFormattedValue());
 					productsTable.addCell(orderEntryData.getTotalPrice().getFormattedValue());
 				}
 				
 			}
 		}

 		if (LOGGER.isDebugEnabled())
 		{
 			LOGGER.debug("Setting Table in Document");
 		}


 		document.add(productsTable);
		document.close();
	}

	private void createSecondLevelConsignmentData(Document document, String locale) throws DocumentException, BusinessException {

		Font boldFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,10, Font.BOLD);
		Paragraph textOnePara = new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_NOTE_1+"."+locale),boldFont);
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
		
		Paragraph textTwoPara = new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.CONSIGNMENT_NOTE_2+"."+locale),boldFont);
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

	/**
	 * Method responsible for building the Pdf Document.
	 * 
	 * @param order
	 * @param document
	 * @param locale 
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void generatePdfDocument(final OrderData order,final Document document,final Map<String, Object> map, String locale) throws DocumentException,
			MalformedURLException, IOException, BusinessException {
		
		//method responsible for rendering STANDARD ORDER CONFIRMATION
		String OrderTypes = null;
		if(order instanceof JnjGTOrderData) {
			OrderTypes = ((JnjGTOrderData) order).getOrderType();
		}
		createHeader(document,OrderTypes,locale);
		
		createFirstLevelData( document, order, locale);
		
		createSecondLevelData(document,locale);
					
		createTabularData(document, order,map,locale);
		
		createThirdLevelData(document,locale);
	}
	
	private void createHeader(final Document document, final String OrderTypes, String locale) throws DocumentException, BusinessException {
		Font underlinedFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,11, Font.BOLD | Font.UNDERLINE);
		underlinedFont.setColor(new Color(0x356B71));
		if("SX".equals(OrderTypes))
		{
		document.add(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.ORDERCONFIRMATIONSX+"."+locale),underlinedFont));
		}
		else
		{
	        
		}
	}
	
	private void createFirstLevelData(final Document document, final OrderData order, String locale) throws DocumentException,
			MalformedURLException, IOException, BusinessException {
		String TAX_ID=null;
		Font boldFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,11, Font.BOLD);
		Font normalFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,11, Font.NORMAL);
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.DOWNLOADDATEFORMAT+"."+locale));
		Date date = new Date();
		//main table
		PdfPTable mainTable = new PdfPTable(4); // 4 columns.
		mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		mainTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainTable.setWidthPercentage(100);		
		if (null != order && order instanceof JnjGTOrderData)
		{	
			JnjGTOrderData orderData = (JnjGTOrderData)order;
			SimpleDateFormat deliveryDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
//			deliveryDateFormat.applyPattern(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.DATEFORMAT+"."+locale));
			Date deliveryDate =orderData.getRequestedDeliveryDate();

			
			
			// nested table one Keys
			//PdfPTable nestedTableOne_1 = new PdfPTable(1);
			//mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			mainTable.addCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.DOWNLOADDATE+"."+locale),boldFont));
			mainTable.addCell(new Paragraph(dateFormat.format(date),normalFont));
			mainTable.addCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.SHIPADD+"."+locale),boldFont));
			mainTable.addCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.BILLNAMEADD+"."+locale),boldFont));
			mainTable.addCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.CUSTOMERPOREF+"."+locale),boldFont));
			mainTable.addCell(new Paragraph(orderData.getPurchaseOrderNumber() != null ?orderData.getPurchaseOrderNumber():"",normalFont));
			if (orderData.getDeliveryAddress() != null){
				StringBuffer shipToName = new StringBuffer();
				
				if (null != orderData.getDeliveryAddress().getFirstName()){
					shipToName.append(orderData.getDeliveryAddress().getFirstName() != null ?orderData.getDeliveryAddress().getFirstName():"");
					shipToName.append(" ");
				}
				if (null != orderData.getDeliveryAddress().getLastName()){
					shipToName.append(orderData.getDeliveryAddress().getLastName()!= null ?orderData.getDeliveryAddress().getLastName():"");
				}
				if(shipToName != null)
				{
					mainTable.addCell(new Paragraph(shipToName.toString(),normalFont));
				}
				StringBuffer billToName = new StringBuffer();
				
				if (null != orderData.getBillingAddress()){
					billToName.append(orderData.getBillingAddress().getFirstName() != null ?orderData.getBillingAddress().getFirstName():"");
					billToName.append(" ");
				}
				
				if (null !=  orderData.getBillingAddress()){
					billToName.append(orderData.getBillingAddress().getLastName() != null ?orderData.getBillingAddress().getLastName():"");
				}
				
				if(billToName != null)
				{
					mainTable.addCell(new Paragraph(billToName.toString(),normalFont));
				}
			}
			mainTable.addCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.ORDERNO+"."+locale),boldFont));
			mainTable.addCell(new Paragraph(orderData.getOrderNumber() != null ?orderData.getOrderNumber() :"",normalFont));
			if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress().getLine1() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getLine1().toString(),normalFont));
				}
			}
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress().getLine1() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getLine1().toString(),normalFont));
				}
			}
			mainTable.addCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.SHIPMETHOD+"."+locale),boldFont));
			mainTable.addCell(new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.STANDARDORDERDELIVERY+"."+locale),normalFont)); 
			if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress().getLine2() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getLine2().toString(),normalFont));
				}
			}
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress().getLine2() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getLine2().toString(),normalFont));
				}
			}
			
			mainTable.addCell(new Paragraph());
			mainTable.addCell(new Paragraph());
			if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress().getPostalCode() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getPostalCode().toString(),normalFont));
				}
			}
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress().getPostalCode() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getPostalCode().toString(),normalFont));
				}
			}
			mainTable.addCell(new Paragraph());
			mainTable.addCell(new Paragraph());
			if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress().getTown() != null)
				{
					
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getTown().toString(),normalFont));
				}
			}
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress().getTown() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getTown().toString(),normalFont));
				}
			}
			mainTable.addCell(new Paragraph());
			mainTable.addCell(new Paragraph());
			if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress().getCountry().getName() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getCountry().getName().toString(),normalFont));
				}
			}
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress().getCountry().getName() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getCountry().getName().toString(),normalFont));
				}
			}
		}
			 document.add(mainTable);
	}
	
	private void createSecondLevelData(final Document document, String locale) throws DocumentException, BusinessException {
		Font boldFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,10, Font.BOLD);
		Paragraph textOnePara = new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.NOTE+"."+locale),boldFont);
		textOnePara.setAlignment(Element.ALIGN_CENTER);;
		final PdfPTable var = new PdfPTable(1);
		PdfPCell content=new PdfPCell();
		content.setBorder(Rectangle.NO_BORDER);
		content.addElement(textOnePara);
		var.addCell(content);
		document.add(var);
		document.add(new Paragraph("\n"));
	}

	
	/**
	 * Method to Fill in the tabular data
	 * @param document
	 * @param order
	 * @param map
	 * @param locale 
	 * @throws DocumentException
	 * @throws BusinessException
	 */
	private void createTabularData(final Document document,
			final OrderData order,final Map<String, Object> map, String locale) throws DocumentException, BusinessException {
	    Font boldFont = FontFactory.getFont(FONT, 11, Font.BOLD);
		Font normalFont = FontFactory.getFont(FONT, 11, Font.NORMAL);
		final PdfPTable templateDetailTable = new PdfPTable(7);
		templateDetailTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		templateDetailTable.setWidthPercentage(100);
		templateDetailTable.addCell(new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.PRODUCTCODE+"."+locale),boldFont)));
		templateDetailTable.addCell(new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.GTINSHIPUNIT+"."+locale), boldFont)));
		templateDetailTable.addCell(new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.DESCRIPTION+"."+locale), boldFont)));
		templateDetailTable.addCell(new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.QUANTITY+"."+locale),boldFont)));
		templateDetailTable.addCell(new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.UNITMEASURE+"."+locale), boldFont)));
		templateDetailTable.addCell(new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.UNITPRICE+"."+locale),boldFont)));
		templateDetailTable.addCell(new PdfPCell(new Phrase(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.TOTAL+"."+locale),boldFont)));
		List<OrderEntryData> orderEntryList = order.getEntries();
		for (final OrderEntryData entry : orderEntryList) {
			if (entry instanceof JnjGTOrderEntryData)
			{
				final JnjGTOrderEntryData entryData = (JnjGTOrderEntryData) entry;
				final JnjGTProductData product = (JnjGTProductData) entryData.getProduct();
				PdfPCell cell = new PdfPCell(new Phrase(product.getCode()!= null ?product.getCode() :"",normalFont));
				
				templateDetailTable.addCell(cell);
				cell =  new PdfPCell(new Phrase(product.getGtin()!= null ?product.getGtin() :"",normalFont));
				templateDetailTable.addCell(cell);
				cell =  new PdfPCell(new Phrase((product.getDescription() != null) ? product.getDescription() :"",normalFont));
				templateDetailTable.addCell(cell);
				cell =  new PdfPCell(new Phrase(entryData.getQuantity()!= null ?entryData.getQuantity().toString() :"",normalFont));
				templateDetailTable.addCell(cell);
				
				StringBuffer unitName = new StringBuffer();
				unitName.append(product.getDeliveryUnit() != null ?product.getDeliveryUnit():"");
				unitName.append(" (");
				unitName.append( product.getNumerator() != null ?product.getNumerator() :"");
				unitName.append(product.getSalesUnit() != null ?product.getSalesUnit() :"");
				unitName.append(")");
				
				templateDetailTable.addCell(new PdfPCell(new Phrase(unitName.toString(),normalFont)));
				cell =  new PdfPCell(new Phrase(entryData.getBasePrice()!= null ?entryData.getBasePrice().getFormattedValue().replaceAll("[^\\d,.]", "").concat(" ").concat(entryData.getBasePrice().getCurrencyIso()) :"",normalFont));
				
				templateDetailTable.addCell(cell);
				cell =  new PdfPCell(new Phrase(entryData.getTotalPrice()!= null ?entryData.getTotalPrice().getFormattedValue().replaceAll("[^\\d,.]", "").concat(" ").concat(entryData.getBasePrice().getCurrencyIso()) :"",normalFont));
				templateDetailTable.addCell(cell);
			}
			

		}
		document.add(new Paragraph("\n"));
		document.add(templateDetailTable);
	    
}
	/**
	 * Method to set the Copyright
	 * @param document
	 * @param locale 
	 * @throws DocumentException
	 * @throws BusinessException
	 */
	private void createThirdLevelData(final Document document, String locale) throws DocumentException, BusinessException{
		Font boldFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,9, Font.BOLD , Color.GRAY);
		Font boldFont1 = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,10, Font.BOLD , Color.GRAY);
		//boldFont.setColor(new Color(Gray));
		Paragraph footer1 = new Paragraph(Config.getParameter(Jnjb2bCoreConstants.OrderPdf.TEMPLATE_FOOTER_ONE+"."+locale),boldFont1);
		footer1.setAlignment(Element.ALIGN_LEFT);
		
		String footerContent=Config.getParameter(Jnjb2bCoreConstants.OrderPdf.TEMPLATE_FOOTER_TWO+"."+locale);
		Paragraph footer2 = new Paragraph(footerContent,boldFont);
		footer2.setAlignment(Element.ALIGN_RIGHT);
		final PdfPTable var4  = new PdfPTable(1);
		final PdfPTable var5 = new PdfPTable(1);
		var4.setWidthPercentage(100);
		var5.setWidthPercentage(100);
		PdfPCell content4=new PdfPCell();
		content4.setBorder(Rectangle.NO_BORDER);
		content4.addElement(footer1);
		var4.addCell(content4);
		document.add(var4);
		document.add(new Paragraph("\n"));
		PdfPCell content5=new PdfPCell();
		content5.setBorder(Rectangle.NO_BORDER);
	    content5.addElement(footer2);
		var5.addCell(content5);
		document.add(var5);
	}
	
	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	public void setI18nService(I18NService i18nService) {
		this.i18nService = i18nService;
	}
	
	/**
	 * Method to get the current user login country
	 * @return String
	 */	
	public String getCountry(){
		String country= null;
		try{
			final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
			country =  cmsSiteModel.getUid();
			LOGGER.debug("Country in PDF : " + country);
		}catch(Exception e){
			e.printStackTrace();
		}		
		return country;
	}
	/**
	 *
	 * @return
	 */
	public String getOrderDetailUrl()
	{
		return baseUrl == null ? null : baseUrl.trim() + ORDER_DETAIL_PAGE_REL_PATH + getClientOrderNumber();
	}

	/**
	 * @return the previousStatus
	 */
	public String getPreviousStatus()
	{
		return previousStatus;
	}

	/**
	 * @param previousStatus
	 *           the previousStatus to set
	 */
	public void setPreviousStatus(final String previousStatus)
	{
		this.previousStatus = previousStatus;
	}

	/**
	 * @return the currentStatus
	 */
	public String getCurrentStatus()
	{
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 *           the currentStatus to set
	 */
	public void setCurrentStatus(final String currentStatus)
	{
		this.currentStatus = currentStatus;
	}

	/**
	 * @return the jnjOrderNumber
	 */
	public String getJnjOrderNumber()
	{
		return jnjOrderNumber;
	}

	/**
	 * @param jnjOrderNumber
	 *           the jnjOrderNumber to set
	 */
	public void setJnjOrderNumber(final String jnjOrderNumber)
	{
		this.jnjOrderNumber = jnjOrderNumber;
	}

	/**
	 * @return the sapOrderNumber
	 */
	public String getSapOrderNumber()
	{
		return sapOrderNumber;
	}

	/**
	 * @param sapOrderNumber
	 *           the sapOrderNumber to set
	 */
	public void setSapOrderNumber(final String sapOrderNumber)
	{
		this.sapOrderNumber = sapOrderNumber;
	}

	/**
	 * @return the clientOrderNumber
	 */
	public String getClientOrderNumber()
	{
		return clientOrderNumber;
	}

	/**
	 * @param clientOrderNumber
	 *           the clientOrderNumber to set
	 */
	public void setClientOrderNumber(final String clientOrderNumber)
	{
		this.clientOrderNumber = clientOrderNumber;
	}

	/**
	 * @return the syncOrderNotification
	 */
	public boolean isSyncOrderNotification()
	{
		return syncOrderNotification;
	}

	/**
	 * @param syncOrderNotification
	 *           the syncOrderNotification to set
	 */
	public void setSyncOrderNotification(final boolean syncOrderNotification)
	{
		this.syncOrderNotification = syncOrderNotification;
	}

	/**
	 * @return the baseUrl
	 */
	@Override
	public String getBaseUrl()
	{
		return baseUrl;
	}

	/**
	 * @param baseUrl
	 *           the baseUrl to set
	 */
	public void setBaseUrl(final String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the orderData
	 */
	public JnjGTOrderData getOrderData()
	{
		return orderData;
	}

	/**
	 * @param orderData
	 *           the orderData to set
	 */
	public void setOrderData(final JnjGTOrderData orderData)
	{
		this.orderData = orderData;
	}
}
