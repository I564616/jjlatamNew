package com.jnj.b2b.cartandcheckoutaddon.download.views;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import com.jnj.services.MessageService;
import com.jnj.facades.data.JnjGTAddressData;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import com.jnj.services.CMSSiteService;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

public class JnjGTPriceInquiryPdfView extends AbstractPdfView {
	private static final Logger LOG = Logger.getLogger(JnjGTPriceInquiryPdfView.class);
	
	final String PRICE_INQUIRY_INFORMATION = "gt.price.inquiry.pdf.information";
	final String PRICE_INQUIRY_CONFIRMATION = "gt.price.inquiry.pdf.price.inquiry";
	final String DOWNLOAD_DATE 		  = "gt.price.inquiry.pdf.download.date";	
	final String PRICE_INQUIRY_NUMBER = "gt.price.inquiry.pdf.price.inquiry.number";
	final String SHIP_NAME_ADDRESS    = "gt.price.inquiry.pdf.shipname.address";
	final String BILL_NAME_ADDRESS    = "gt.price.inquiry.pdf.billname.address";
	final String PRICE_INFORMATION    = "gt.price.inquiry.pdf.pricing.information";
	final String NOTE        		  = "gt.price.inquiry.pdf.note";
	final String NOTE_TWO             = "gt.price.inquiry.pdf.note_two";
	final String PRODUCT_CODE   	  = "gt.price.inquiry.pdf.product.code";	
	final String GTIN_SHIPPING_UNIT   = "gt.price.inquiry.pdf.gtin.shipping.unit";	
	final String DESCSRIPTION     	  = "gt.price.inquiry.pdf.description";
	final String QUANTITY      		  =	"gt.price.inquiry.pdf.quantity";
	final String UNIT_MEASURE      	  = "gt.price.inquiry.pdf.unit.measure";	
	final String UNIT_PRICE      	  =	"gt.price.inquiry.pdf.unit.price";
	final String TOTAL      		  =	"gt.price.inquiry.pdf.total";
	final String FOOTER_ONE           = "gt.price.inquiry.pdf.footer_one";
	final String FOOTER_TWO           = "gt.price.inquiry.pdf.footer_two";
	
	public static final String TEMPLATE_FOOTER_ONE  = "gt.pdf.copyright.footerone";
	public static final String TEMPLATE_FOOTER_TWO  = "gt.pdf.copyright.footer";
	
	@Autowired
	private CMSSiteService cmsSiteService;	
	
	//private String TAX_ID=null;
	
	@Autowired
	private MessageService messageService;
	
	/** I18NService to retrieve the current locale. */
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
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			document.setPageSize(PageSize.A4.rotate());
			writer = PdfWriter.getInstance(document, baos);
			//add the footer
			/*EMEAFooterPdfView event = new EMEAFooterPdfView(writer);
			writer.setPageEvent(event);*/
		
			document.open();
			final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
			byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
			response.setHeader("Content-Disposition", "attachment; filename=PriceInquiry.pdf");
			if (map.get("orderData") != null && map.get("orderData") instanceof OrderData){
				
				final OrderData order = (OrderData) map.get("orderData");
				 try
				 {
					// final String jnjConnectLogoURL = map.get("jnjConnectLogoURL") != null ?(String)map.get("jnjConnectLogoURL"):"";
					Image jnjConnectLogo = null;
					if ( jnjConnectLogoByteArray != null){
							//jnjConnectLogo = Image.getInstance(new URL(jnjConnectLogoURL));
							jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
							jnjConnectLogo.setAlignment(Element.ALIGN_LEFT);
							//jnjConnectLogo.setAbsolutePosition(100F, 475F);
							//jnjConnectLogo.scaleAbsolute(180, 74); 
							document.add(jnjConnectLogo);
					}
				 }catch(Exception ex){
					 LOG.error("Exception occurred while loading image in JnjEMEAPriceInquiryPdfView.buildPdfDocument() due to "+ex);
				 }
			
				//build the pdf document
				generatePdfDocument( order, document, map );
			}
			
			
			
			//close the document
			document.close();
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating EMEA Prince Inquiry PDF due to " + exception);
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
	public void generatePdfDocument(final OrderData order,final Document document, final Map<String, Object> map) throws DocumentException,
			MalformedURLException, IOException, BusinessException {
		
		//method responsible for rendering STANDARD ORDER CONFIRMATION
		createHeader(document,map);
		
		createFirstLevelData( document, order );
		
		createSecondLevelData(document);
		
		createTabularData( document, order );
		createThirdLevelData(document);
	}
	
	private void createHeader(final Document document, final Map<String, Object> map) throws DocumentException, BusinessException
	{
		Font underlinedFont = FontFactory.getFont("Arial", 11, Font.BOLD | Font.UNDERLINE);
		underlinedFont.setColor(new Color(0x356B71));
		//document.add(new Paragraph(messageService.getMessageForCode(PRICE_INQUIRY,i18nService.getCurrentLocale()),underlinedFont));
		if ("orderConfirmation".equals((String)map.get("confirmationZQT")))
		{
			document.add(new Paragraph(messageSource.getMessage(PRICE_INQUIRY_CONFIRMATION, null,i18nService.getCurrentLocale()),underlinedFont));
		}
		else
		{
			document.add(new Paragraph(messageSource.getMessage(PRICE_INQUIRY_INFORMATION, null,i18nService.getCurrentLocale()),underlinedFont));
		}
		
		
		document.add(new Paragraph("\n"));
		
		
		
		
	}
	
	private void createFirstLevelData(final Document document,
			final OrderData order) throws DocumentException,
			MalformedURLException, IOException, BusinessException
	{
		/*UAT #895*/
		String TAX_ID=null;
		/*UAT #895*/
		Font boldFont = FontFactory.getFont("Arial", 11, Font.BOLD);
		Font normalFont = FontFactory.getFont("Arial", 11, Font.NORMAL);
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern(jnjCommonUtil.getTimeStampFormat());
		Date date = new Date();
		//main table
		PdfPTable mainTable = new PdfPTable(3); // 4 columns.
		mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		mainTable.setHorizontalAlignment(Element.ALIGN_LEFT);
				
		if (null != order && order instanceof JnjGTOrderData)
		{	
			JnjGTOrderData orderData = (JnjGTOrderData)order;
			// nested table one
			PdfPTable nestedTableOne = new PdfPTable(2);
			nestedTableOne.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			//nestedTableOne.addCell(new Paragraph(messageService.getMessageForCode(DOWNLOAD_DATE,i18nService.getCurrentLocale()),boldFont));
			nestedTableOne.addCell(new Paragraph(messageSource.getMessage(DOWNLOAD_DATE, null,i18nService.getCurrentLocale()),boldFont));
			nestedTableOne.addCell(new Paragraph(dateFormat.format(date),normalFont));
			//nestedTableOne.addCell(new Paragraph(messageService.getMessageForCode(PRICE_INQUIRY_NUMBER,i18nService.getCurrentLocale()),boldFont));
			nestedTableOne.addCell(new Paragraph(messageSource.getMessage(PRICE_INQUIRY_NUMBER, null,i18nService.getCurrentLocale()),boldFont));
			nestedTableOne.addCell(new Paragraph(orderData.getSapOrderNumber() != null ?orderData.getSapOrderNumber():"",normalFont));
			
			// nested table two
			PdfPTable nestedTableTwo = new PdfPTable(1);
			nestedTableTwo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			//nestedTableTwo.addCell(new Paragraph(messageService.getMessageForCode(SHIP_NAME_ADDRESS,i18nService.getCurrentLocale()),boldFont));
			nestedTableTwo.addCell(new Paragraph(messageSource.getMessage(SHIP_NAME_ADDRESS, null,i18nService.getCurrentLocale()),boldFont));
			
			/*StringBuffer shipToName = new StringBuffer();*/
			
			/*if (null != orderData.getDeliveryAddress()){
				shipToName.append(orderData.getDeliveryAddress().getCompanyName() != null ?orderData.getDeliveryAddress().getCompanyName():"");
				
			}
			*/
			/*if (null != orderData.getDeliveryAddress()){
				shipToName.append(orderData.getDeliveryAddress().getLastName()!= null ?orderData.getDeliveryAddress().getLastName():"");
			}*/
						
			if (orderData.getDeliveryAddress() != null){
				if(orderData.getDeliveryAddress().getCompanyName() != null)
				{
					nestedTableTwo.addCell(new Paragraph(orderData.getDeliveryAddress().getCompanyName().toString(),normalFont));
				}
				if(orderData.getDeliveryAddress().getLine1() != null)
				{
					nestedTableTwo.addCell(new Paragraph(orderData.getDeliveryAddress().getLine1().toString(),normalFont));
				}
				if(orderData.getDeliveryAddress().getLine2() != null)
				{
					nestedTableTwo.addCell(new Paragraph(orderData.getDeliveryAddress().getLine2().toString(),normalFont));
				}
				if(orderData.getDeliveryAddress() instanceof JnjGTAddressData)
				{
					JnjGTAddressData jnjNAAddressData= (JnjGTAddressData)orderData.getDeliveryAddress();
				if(jnjNAAddressData.getLine3() != null)
				{
					nestedTableTwo.addCell(new Paragraph(jnjNAAddressData.getLine3(),normalFont));
				}
				if(jnjNAAddressData.getLine4() != null)
				{
					nestedTableTwo.addCell(new Paragraph(jnjNAAddressData.getLine4(),normalFont));
				}
				
				}
				if(orderData.getDeliveryAddress().getPostalCode() != null)
				{
					nestedTableTwo.addCell(new Paragraph(orderData.getDeliveryAddress().getPostalCode().toString(),normalFont));
				}
				if(orderData.getDeliveryAddress().getTown() != null)
				{
					nestedTableTwo.addCell(new Paragraph(orderData.getDeliveryAddress().getTown().toString(),normalFont));
				}
				if(orderData.getDeliveryAddress().getCountry().getName() != null)
				{
					nestedTableTwo.addCell(new Paragraph(orderData.getDeliveryAddress().getCountry().getName().toString(),normalFont));
				}
					
				}
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
			PdfPTable nestedTableThree = new PdfPTable(1);
			nestedTableThree.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			//nestedTableThree.addCell(new Paragraph(messageService.getMessageForCode(BILL_NAME_ADDRESS,i18nService.getCurrentLocale()),boldFont));
			nestedTableThree.addCell(new Paragraph(messageSource.getMessage(BILL_NAME_ADDRESS, null,i18nService.getCurrentLocale()),boldFont));
			
			/*StringBuffer billToName = new StringBuffer();
			
			if (null != orderData.getBillingAddress()){
				billToName.append(orderData.getBillingAddress().getFirstName() != null ?orderData.getBillingAddress().getFirstName():"");
				billToName.append(" ");
			}
			
			if (null !=  orderData.getBillingAddress()){
				billToName.append(orderData.getBillingAddress().getLastName() != null ?orderData.getBillingAddress().getLastName():"");
			}*/
			
			
			
			if (orderData.getBillingAddress() != null){
				if(orderData.getBillingAddress().getCompanyName() != null)
				{
					nestedTableThree.addCell(new Paragraph(orderData.getBillingAddress().getCompanyName().toString(),normalFont));
				}
				if(orderData.getBillingAddress().getLine1() != null)
				{
					nestedTableThree.addCell(new Paragraph(orderData.getBillingAddress().getLine1().toString(),normalFont));
				}
				if(orderData.getBillingAddress().getLine2() != null)
				{
					nestedTableThree.addCell(new Paragraph(orderData.getBillingAddress().getLine2().toString(),normalFont));
				}
				if(orderData.getBillingAddress() instanceof JnjGTAddressData)
				{
					JnjGTAddressData jnjNAAddressData= (JnjGTAddressData)orderData.getBillingAddress();
				if(jnjNAAddressData.getLine3() != null)
				{
					nestedTableThree.addCell(new Paragraph(jnjNAAddressData.getLine3(),normalFont));
				}
				if(jnjNAAddressData.getLine4() != null)
				{
					nestedTableThree.addCell(new Paragraph(jnjNAAddressData.getLine4(),normalFont));
				}
				if(jnjNAAddressData.getTaxid() != null)
				{
					TAX_ID=jnjNAAddressData.getTaxid();
				}	
					
				}
				if(orderData.getBillingAddress().getPostalCode() != null)
				{
					nestedTableThree.addCell(new Paragraph(orderData.getBillingAddress().getPostalCode().toString(),normalFont));
				}
				if(orderData.getBillingAddress().getTown() != null)
				{
					nestedTableThree.addCell(new Paragraph(orderData.getBillingAddress().getTown().toString(),normalFont));
				}
				if(orderData.getBillingAddress().getCountry().getName() != null)
				{
					nestedTableThree.addCell(new Paragraph(orderData.getBillingAddress().getCountry().getName().toString(),normalFont));
				}
				if(TAX_ID != null)
				{
					nestedTableThree.addCell(new Paragraph(TAX_ID,normalFont));
				}
				
				}
				
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
				
			mainTable.addCell(nestedTableOne);
			mainTable.addCell(nestedTableTwo);
			mainTable.addCell(nestedTableThree);
			//mainTable.addCell(nestedTableFour);
		}
		
		 document.add(mainTable);
		/* document.add(new Paragraph(NEXT_LINE));*/
	}
	
	private void createSecondLevelData(final Document document) throws DocumentException, BusinessException
	{
		final PdfPTable var4 = new PdfPTable(1);
		PdfPCell content=new PdfPCell();
		content.setBorder(Rectangle.NO_BORDER);
		//content.addElement(textOnePara);
		var4.addCell(content);
		document.add(var4);
		Font boldFont = FontFactory.getFont("Arial", 11, Font.BOLD);
		Paragraph review_price = new Paragraph(messageSource.getMessage(PRICE_INFORMATION,null,i18nService.getCurrentLocale()));
		review_price.setAlignment(Element.ALIGN_CENTER);
		Paragraph textOnePara = new Paragraph(messageSource.getMessage(NOTE,null,i18nService.getCurrentLocale()));
		textOnePara.setAlignment(Element.ALIGN_CENTER);
		Paragraph note2 =new Paragraph(messageSource.getMessage(NOTE_TWO,null,i18nService.getCurrentLocale()));
		note2.setAlignment(Element.ALIGN_CENTER);
		
	//	Paragraph para1 = new Paragraph(messageService.getMessageForCode(NOTE,i18nService.getCurrentLocale()),boldFont);
		final PdfPTable var = new PdfPTable(1);
		final PdfPTable var2 = new PdfPTable(1);
		final PdfPTable var3 = new PdfPTable(1);
		//var.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		
		//PdfPCell content=new PdfPCell();
		content.setBorder(Rectangle.NO_BORDER);
		content.addElement(review_price);
		//content.addElement(textOnePara);
		var.addCell(content);
		document.add(var);
		document.add(new Paragraph("\n"));
		PdfPCell content2=new PdfPCell();
		content2.setBorder(Rectangle.NO_BORDER);
	    content2.addElement(textOnePara);
		var2.addCell(content2);
		document.add(var2);
		document.add(new Paragraph("\n"));
		PdfPCell content3=new PdfPCell();
		content3.setBorder(Rectangle.NO_BORDER);
	    content3.addElement(note2);
		var3.addCell(content3);
		document.add(var3);
		document.add(new Paragraph("\n"));
		/*Paragraph textTwoPara = new Paragraph(TEXT_TWO,boldFont);
		textTwoPara.setAlignment(Element.ALIGN_CENTER);;*/
		
		/*document.add(textOnePara);
		document.add(new Paragraph(NEXT_LINE));
		document.add(textTwoPara);
		document.add(new Paragraph(NEXT_LINE));*/
	}

	private void createTabularData(final Document document,
			final OrderData order) throws DocumentException, BusinessException {
		Font boldFont = FontFactory.getFont("Arial", 11, Font.BOLD);
		Font normalFont = FontFactory.getFont("Arial", 11, Font.NORMAL);

		if ( order != null ) {

			final PdfPTable templateDetailTable = new PdfPTable(7);
			templateDetailTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			templateDetailTable.setWidthPercentage(100);

			// set header for the table
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(PRODUCT_CODE,null,i18nService.getCurrentLocale()), boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(GTIN_SHIPPING_UNIT,null,i18nService.getCurrentLocale()), boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(DESCSRIPTION,null,i18nService.getCurrentLocale()), boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(QUANTITY,null,i18nService.getCurrentLocale()),boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(UNIT_MEASURE,null,i18nService.getCurrentLocale()), boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(UNIT_PRICE,null,i18nService.getCurrentLocale()),	boldFont)));
			templateDetailTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(TOTAL,null,i18nService.getCurrentLocale()),
					boldFont)));

			// set data rows
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
					cell =  new PdfPCell(new Phrase((product.getDescription() != null) ? product.getName()+" "+product.getDescription() :product.getName(),normalFont));
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

			document.add(templateDetailTable);
		}
		   document.add(new Paragraph("\n"));

	}
	/**
	 * Method to set the copyright
	 * @param document
	 * @throws DocumentException
	 * @throws BusinessException
	 */
	private void createThirdLevelData(final Document document) throws DocumentException, BusinessException{
		//Font boldFont = FontFactory.getFont("Arial", 11);
		Font boldFont = FontFactory.getFont("Arial",9, Color.GRAY);
		Font boldFont1 = FontFactory.getFont("Arial", 10);
		Paragraph footer1 = new Paragraph(messageSource.getMessage(TEMPLATE_FOOTER_ONE,null,i18nService.getCurrentLocale()),boldFont1);
		footer1.setAlignment(Element.ALIGN_LEFT);
		String footerContent=TEMPLATE_FOOTER_TWO+"."+getCurrentCountry();
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
	public MessageSource getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
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
	public String getCurrentCountry(){
		String country= null;
		try{
			final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
			country =  cmsSiteModel.getUid();
		}catch(Exception e){
			e.printStackTrace();
		}		
		return country;
	}

}
