package com.jnj.b2b.cartandcheckoutaddon.download.views;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
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

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.services.CMSSiteService;
import com.jnj.services.MessageService;
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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.servicelayer.i18n.I18NService;

/**
 * 
 * 
 */
public class JnjGTStandardOrderConfirmationPdfView extends AbstractPdfView
{

private static final Logger LOG = Logger.getLogger(JnjGTOrderConfirmationPdfView.class);
	//Modified for AAOL-6007
	final String orderconfirmation   = "order.confirmation.pdf.order.confirmation";
	final String orderconfirmationSX   = "order.confirmation.pdf.order.confirmationSX";
	final String downloaddate 		 = "order.confirmation.pdf.download.date";	
	final String customerporef 		 = "order.confirmation.pdf.customer.poreference";	
	final String orderno   		 	 = "order.confirmation.pdf.order.number";
	final String shipmethod          = "order.confirmation.pdf.shipping.method";
	final String shipnameadd         = "order.confirmation.pdf.shipname.address";
	final String billnameadd         = "order.confirmation.pdf.billname.address";
	final String note        		 = "order.confirmation.pdf.note";
	final String productcode   		 = "order.confirmation.pdf.product.code";	
	final String gtinshipunit 		 = "order.confirmation.pdf.gtin.shipping.unit";	
	final String description     	 = "order.confirmation.pdf.description";
	final String quantity      		 =	"order.confirmation.pdf.quantity";
	final String unitmeasure       	 = "order.confirmation.pdf.uom";	
	final String unitprice      	 =	"order.confirmation.pdf.unit.price";
	final String total      		 = 	"order.confirmation.pdf.total";
	final String requestedDeliveryDate    =  "odr.cfm.pdf.dDt";
	final String FOOTER_ONE           = "price.inquiry.pdf.footer_one";
	final String FOOTER_TWO           = "price.inquiry.pdf.footer_two";
	final String standardOrderDelivery= "order.shipping.method";
	
	public static final String TEMPLATE_FOOTER_ONE  = "pdf.copyright.footerone";
	public static final String TEMPLATE_FOOTER_TWO  = "pdf.copyright.footer";
	
	private static final String FONT = "fonts/FreeSans.ttf";
	String GOOrderNumber = null;
	protected static final int MARGIN = 32;
	
	@Autowired
	private CMSSiteService cmsSiteService;	


	//private String TAX_ID=null;
	/*private static final String STANDARD_ORDER_CONFIRMATION = "STANDARD ORDER CONFIRMATION           ";
	private static final String NEXT_LINE = "\n";
	private static final String TEXT_ONE ="Please note: Prices are excluding VAT. Availability and Prices are subject to change. Check confirmed details under Order Information later.";
	private static final String TEXT_TWO ="Fees, if applicable to your order, will only be add at invoice creation. Please review your final invoice.";*/

	@Autowired
	private MessageService messageService;
	
	private MessageSource messageSource;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/** I18NService to retrieve the current locale. */
	@Autowired
	protected I18NService i18nService;
	
	
	@Override
	protected Document newDocument()
	{
		return new Document();
	}

	/**
	 * This method generates the PDF doc
	 */
	@Override
	protected void buildPdfDocument(final Map<String, Object> map, Document document, PdfWriter writer,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		try
		{
			response.setCharacterEncoding("UTF-8");
			
			 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
			 writer.setPageEvent(events);   
	       events.onOpenDocument(writer, document); 
			OutputStream os = response.getOutputStream();
			
			document.setPageSize(PageSize.A4.rotate());
			writer = PdfWriter.getInstance(document, os);
			//add the footer
			/*GTFooterPdfView event = new GTFooterPdfView(writer);
			writer.setPageEvent(event);*/
			if(null != map.get("jdegoOrderNum"))
			{
				GOOrderNumber = map.get("jdegoOrderNum").toString();
			}
			document.open();
			Font underlinedFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,11, Font.BOLD | Font.UNDERLINE);
			underlinedFont.setColor(new Color(0x356B71));
			final InputStream jnjConnectLogoIS2 = (InputStream) map.get("jnjConnectLogoURL2");
			final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
			byte[] jnjConnectLogoByteArray2 = IOUtils.toByteArray(jnjConnectLogoIS2);
			byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
			PdfPTable table = new PdfPTable(3); // 3 columns.
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	      	table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
	      	table.setTotalWidth(822F);
	      	table.setLockedWidth(true);
	      	table.setSpacingAfter(15f);
			response.setHeader("Content-Disposition", "attachment; filename=OrderConfirmation.pdf");
			if (map.get("orderData") != null && map.get("orderData") instanceof OrderData){
				
				final JnjGTOrderData order = (JnjGTOrderData) map.get("orderData");
				 try
				 {
					// final String jnjConnectLogoURL = map.get("jnjConnectLogoURL") != null ?(String)map.get("jnjConnectLogoURL"):"";
					/* Image jnjConnectLogo = null;
						if ( jnjConnectLogoByteArray2 != null){
								//jnjConnectLogo = Image.getInstance(new URL(jnjConnectLogoURL));
								jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray2);
								jnjConnectLogo.scaleToFit(50F, 100F);
								jnjConnectLogo.setAbsolutePosition(630F, 510F);
								jnjConnectLogo.scaleAbsolute(180,50);
								document.add(jnjConnectLogo);
							
						}
					jnjConnectLogo = null;
					if ( jnjConnectLogoByteArray != null){
							//jnjConnectLogo = Image.getInstance(new URL(jnjConnectLogoURL));
							jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
							jnjConnectLogo.setAbsolutePosition(625F, 485F);
							jnjConnectLogo.scaleAbsolute(180, 70);
							document.add(jnjConnectLogo);
					}*/
					 if ( jnjConnectLogoByteArray != null){
				      		table.addCell(createImageCell(jnjConnectLogoByteArray,Element.ALIGN_LEFT));
				      	}
					 table.addCell(new Paragraph());
				      	if ( jnjConnectLogoByteArray2 != null){
				      		table.addCell(createImageCell(jnjConnectLogoByteArray2,Element.ALIGN_RIGHT));
				      	}
				      	table.addCell(new Paragraph());
				      	Paragraph para=new Paragraph(messageSource.getMessage(orderconfirmation,null,i18nService.getCurrentLocale()),underlinedFont);
				      	para.setAlignment(Element.ALIGN_CENTER);
				      	table.addCell(para);
				      	table.addCell(new Paragraph());
				      	document.add(table);
				 }catch(Exception ex){
					 LOG.error("Exception occurred while loading image in JnjGTOrderConfirmationPdfView.buildPdfDocument() due to "+ex);
				 }
			
				//build the pdf document
				generatePdfDocument( order, document,map);
			}
			
			
			
			//close the document
			document.close();
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating GT order confirmation PDF due to " + exception);
		}
	}
	
	/**
	 * Method responsible for building the Pdf Document.
	 * 
	 * @param order
	 * @param document
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void generatePdfDocument(final OrderData order,final Document document,final Map<String, Object> map) throws DocumentException,
			MalformedURLException, IOException, BusinessException {
		
		//method responsible for rendering STANDARD ORDER CONFIRMATION
		String OrderTypes = null;
		if(order instanceof JnjGTOrderData)
		{
			OrderTypes = ((JnjGTOrderData) order).getOrderType();
		}
		createHeader(document,OrderTypes);
		
		createFirstLevelData( document, order );
		
		createSecondLevelData(document);
					
		createTabularData(document, order,map);
		
		createThirdLevelData(document);
	}
	
	private void createHeader(final Document document, final String OrderTypes) throws DocumentException, BusinessException
	{
		Font underlinedFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,11, Font.BOLD | Font.UNDERLINE);
		underlinedFont.setColor(new Color(0x356B71));
		if("SX".equals(OrderTypes))
		{
		document.add(new Paragraph(messageSource.getMessage(orderconfirmationSX,null,i18nService.getCurrentLocale()),underlinedFont));
		}
		else
		{
			/*PdfPTable table = new PdfPTable(2);
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(new Paragraph(""));
			table.addCell(new Paragraph(messageSource.getMessage(orderconfirmation,null,i18nService.getCurrentLocale()),underlinedFont));
	        document.add(table);*/
	        
		}
		/*document.add(new Paragraph(NEXT_LINE));*/
	}
	
	private void createFirstLevelData(final Document document,
			final OrderData order) throws DocumentException,
			MalformedURLException, IOException, BusinessException
	{
		String TAX_ID=null;
		Font boldFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,11, Font.BOLD);
		Font normalFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,11, Font.NORMAL);
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern(jnjCommonUtil.getTimeStampFormat());
		Date date = new Date();
		//main table
		PdfPTable mainTable = new PdfPTable(4); // 4 columns.
		mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		mainTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainTable.setWidthPercentage(100);		
		if (null != order && order instanceof JnjGTOrderData)
		{	
			JnjGTOrderData orderData = (JnjGTOrderData)order;
			SimpleDateFormat deliveryDateFormat = new SimpleDateFormat();
			deliveryDateFormat.applyPattern(jnjCommonUtil.getDateFormat());
			Date deliveryDate =orderData.getRequestedDeliveryDate();

			
			
			// nested table one Keys
			//PdfPTable nestedTableOne_1 = new PdfPTable(1);
			//mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			mainTable.addCell(new Paragraph(messageSource.getMessage(downloaddate,null,i18nService.getCurrentLocale()),boldFont));
			mainTable.addCell(new Paragraph(dateFormat.format(date),normalFont));
			mainTable.addCell(new Paragraph(messageSource.getMessage(shipnameadd,null,i18nService.getCurrentLocale()),boldFont));
			mainTable.addCell(new Paragraph(messageSource.getMessage(billnameadd,null,i18nService.getCurrentLocale()),boldFont));
			mainTable.addCell(new Paragraph(messageSource.getMessage(customerporef,null,i18nService.getCurrentLocale()),boldFont));
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
			mainTable.addCell(new Paragraph(messageSource.getMessage(orderno,null,i18nService.getCurrentLocale()),boldFont));
			mainTable.addCell(new Paragraph(orderData.getOrderNumber() != null ?orderData.getOrderNumber() :"",normalFont));
			/*if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress().getCompanyName() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getCompanyName().toString(),normalFont));
				}
			}
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress().getCompanyName() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getCompanyName().toString(),normalFont));
				}
			}*/
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
			mainTable.addCell(new Paragraph(messageSource.getMessage(shipmethod,null,i18nService.getCurrentLocale()),boldFont));
			mainTable.addCell(new Paragraph(messageSource.getMessage(standardOrderDelivery,null,i18nService.getCurrentLocale()),normalFont)); 
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
			
			/*mainTable.addCell(new Paragraph());
			mainTable.addCell(new Paragraph());
			if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress() instanceof JnjGTAddressData)
				{
					JnjGTAddressData jnjGTAddressData= (JnjGTAddressData)orderData.getDeliveryAddress();
					if(jnjGTAddressData.getLine3() != null)
					{
						System.out.println("jnjGTAddressData.getLine3()>>"+jnjGTAddressData.getLine3());
						mainTable.addCell(new Paragraph(jnjGTAddressData.getLine3(),normalFont));
					}
				}
			}
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress() instanceof JnjGTAddressData)
				{
					JnjGTAddressData jnjGTAddressData= (JnjGTAddressData)orderData.getBillingAddress();
				if(jnjGTAddressData.getLine3() != null)
				{
					mainTable.addCell(new Paragraph(jnjGTAddressData.getLine3(),normalFont));
				}
				}
			}*/
			/*mainTable.addCell(new Paragraph());
			mainTable.addCell(new Paragraph());
			if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress() instanceof JnjGTAddressData)
				{
					JnjGTAddressData jnjGTAddressData= (JnjGTAddressData)orderData.getDeliveryAddress();
				if(jnjGTAddressData.getLine4() != null)
				{
					System.out.println("jnjGTAddressData.getLine4()>>"+jnjGTAddressData.getLine4());
					mainTable.addCell(new Paragraph(jnjGTAddressData.getLine4(),normalFont));
				}
				}
			}
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress() instanceof JnjGTAddressData)
				{
					JnjGTAddressData jnjGTAddressData= (JnjGTAddressData)orderData.getBillingAddress();
					if(jnjGTAddressData.getLine4() != null)
					{
						mainTable.addCell(new Paragraph(jnjGTAddressData.getLine4(),normalFont));
					}
				}	
			}*/
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
			/*if(deliveryDate !=null){
				nestedTableOne_1.addCell(new Paragraph(messageSource.getMessage(requestedDeliveryDate,null,i18nService.getCurrentLocale()),boldFont));
				nestedTableOne_1.addCell(new Paragraph(deliveryDateFormat.format(deliveryDate),normalFont));
			}*/
			//Values
			//PdfPTable nestedTableOne_2 = new PdfPTable(1);
			//mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			/*if(null != GOOrderNumber)
			{
				nestedTableOne_2.addCell(new Paragraph(messageSource.getMessage("",null,i18nService.getCurrentLocale()),boldFont));
				nestedTableOne_2.addCell(new Paragraph(GOOrderNumber,normalFont));
				GOOrderNumber = null;
			}*/
			
			// nested table two
			//PdfPTable nestedTableTwo = new PdfPTable(1);
			//mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			
			
			/*if (orderData.getDeliveryAddress() != null){*/
				/*StringBuffer shipToName = new StringBuffer();
				
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
				}*/
				
				
				
				/*if(orderData.getDeliveryAddress() instanceof JnjGTAddressData)
				{
					JnjGTAddressData jnjGTAddressData= (JnjGTAddressData)orderData.getDeliveryAddress();
				if(jnjGTAddressData.getLine3() != null)
				{
					mainTable.addCell(new Paragraph(jnjGTAddressData.getLine3(),normalFont));
				}
				if(jnjGTAddressData.getLine4() != null)
				{
					mainTable.addCell(new Paragraph(jnjGTAddressData.getLine4(),normalFont));
				}
				
				}
				if(orderData.getDeliveryAddress().getPostalCode() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getPostalCode().toString(),normalFont));
				}
				if(orderData.getDeliveryAddress().getTown() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getTown().toString(),normalFont));
				}
				if(orderData.getDeliveryAddress().getCountry().getName() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getDeliveryAddress().getCountry().getName().toString(),normalFont));
				}
					
				}*/
				/*StringTokenizer token = new StringTokenizer(orderData.getDeliveryAddress().getFormattedAddress(),",");
				while (token.hasMoreElements()){
					if (orderData.getDeliveryAddress().getLastName()!= null){
						nestedTableTwo.addCell(" ");
					}
					nestedTableTwo.addCell(new Paragraph(token.nextToken(),normalFont));
					if (orderData.getDeliveryAddress().getLastName() == null && token.hasMoreTokens()){
						nestedTableTwo.addCell(" ");
					}
				}*/
			
						
			//nested table three
			//PdfPTable nestedTableThree = new PdfPTable(1);
			//nestedTableThree.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			
			
			
			
			/*if (orderData.getBillingAddress() != null){
				
				
				
				if(orderData.getBillingAddress() instanceof JnjGTAddressData)
				{
					JnjGTAddressData jnjGTAddressData= (JnjGTAddressData)orderData.getBillingAddress();
				if(jnjGTAddressData.getLine3() != null)
				{
					mainTable.addCell(new Paragraph(jnjGTAddressData.getLine3(),normalFont));
				}
				if(jnjGTAddressData.getLine4() != null)
				{
					mainTable.addCell(new Paragraph(jnjGTAddressData.getLine4(),normalFont));
				}
				if(jnjGTAddressData.getTaxid() != null)
				{
					TAX_ID=jnjGTAddressData.getTaxid();
				}	
					
				}
				if(orderData.getBillingAddress().getPostalCode() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getPostalCode().toString(),normalFont));
				}
				if(orderData.getBillingAddress().getTown() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getTown().toString(),normalFont));
				}
				if(orderData.getBillingAddress().getCountry().getName() != null)
				{
					mainTable.addCell(new Paragraph(orderData.getBillingAddress().getCountry().getName().toString(),normalFont));
				}
				if(TAX_ID != null)
				{
					mainTable.addCell(new Paragraph(TAX_ID,normalFont));
				}
				
				}
				*/
				/*StringTokenizer token = new StringTokenizer(orderData.getBillingAddress().getFormattedAddress(),",");
				while (token.hasMoreElements()){
					if (orderData.getBillingAddress().getLastName() != null){
						nestedTableThree.addCell(" ");
					}
					nestedTableThree.addCell(new Paragraph(token.nextToken(),normalFont));
					if (orderData.getBillingAddress().getLastName() == null && token.hasMoreTokens()){
						nestedTableThree.addCell(" ");
					}
				}*/
			
												
			//nested table Five
			/*PdfPTable nestedTableFour = new PdfPTable(1);
			nestedTableThree.getDefaultCell().setBorder(Rectangle.NO_BORDER);*/
			/*mainTable.addCell(nestedTableOne_1);	
			mainTable.addCell(nestedTableOne_2);
			mainTable.addCell(nestedTableTwo);
			mainTable.addCell(nestedTableThree);*/
			//mainTable.addCell(nestedTableFour);
		/*}*/
		
		
		/* document.add(new Paragraph(NEXT_LINE));*/
	}
	
	private void createSecondLevelData(final Document document) throws DocumentException, BusinessException
	{
		Font boldFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,10, Font.BOLD);
		Paragraph textOnePara = new Paragraph(messageSource.getMessage(note,null,i18nService.getCurrentLocale()),boldFont);
		textOnePara.setAlignment(Element.ALIGN_CENTER);;
		
	//	Paragraph para1 = new Paragraph(messageSource.getMessage(NOTE,i18nService.getCurrentLocale()),boldFont);
		final PdfPTable var = new PdfPTable(1);
		//var.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		
		PdfPCell content=new PdfPCell();
		content.setBorder(Rectangle.NO_BORDER);
		content.addElement(textOnePara);
		var.addCell(content);
		document.add(var);
		document.add(new Paragraph("\n"));
		/*Paragraph textTwoPara = new Paragraph(TEXT_TWO,boldFont);
		textTwoPara.setAlignment(Element.ALIGN_CENTER);;*/
		
		/*document.add(textOnePara);
		document.add(new Paragraph(NEXT_LINE));
		document.add(textTwoPara);
		document.add(new Paragraph(NEXT_LINE));*/
	}

	
	/**
	 * Method to Fill in the tabular data
	 * @param document
	 * @param order
	 * @param map
	 * @throws DocumentException
	 * @throws BusinessException
	 */
	private void createTabularData(final Document document,
			final OrderData order,final Map<String, Object> map) throws DocumentException, BusinessException {
		    Font boldFont = FontFactory.getFont(FONT, 11, Font.BOLD);
			Font normalFont = FontFactory.getFont(FONT, 11, Font.NORMAL);
			final PdfPTable templateDetailTable = new PdfPTable(7);
			templateDetailTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			templateDetailTable.setWidthPercentage(100);
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(productcode,null,i18nService.getCurrentLocale()),boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(gtinshipunit,null,i18nService.getCurrentLocale()), boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(description,null,i18nService.getCurrentLocale()), boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(quantity,null,i18nService.getCurrentLocale()),boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(unitmeasure,null,i18nService.getCurrentLocale()), boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(unitprice,null,i18nService.getCurrentLocale()),boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(total,null,i18nService.getCurrentLocale()),boldFont)));
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
	 * @throws DocumentException
	 * @throws BusinessException
	 */
	private void createThirdLevelData(final Document document) throws DocumentException, BusinessException{
		Font boldFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,9, Font.BOLD , Color.GRAY);
		Font boldFont1 = FontFactory.getFont(FONT, BaseFont.IDENTITY_H , true ,10, Font.BOLD , Color.GRAY);
		//boldFont.setColor(new Color(Gray));
		Paragraph footer1 = new Paragraph(messageSource.getMessage(TEMPLATE_FOOTER_ONE,null,i18nService.getCurrentLocale()),boldFont1);
		footer1.setAlignment(Element.ALIGN_LEFT);
		
		String footerContent=TEMPLATE_FOOTER_TWO;
		Paragraph footer2 = new Paragraph(messageSource.getMessage(footerContent,null,i18nService.getCurrentLocale()),boldFont);
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
	
	
	/*private void createFooter(final Document document)throws DocumentException
	{
		
	}*/
	
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
	
	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public void setCmsSiteService(CMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
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
			LOG.debug("Country in PDF : " + country);
		}catch(Exception e){
			e.printStackTrace();
		}		
		return country;
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
}
