package com.jnj.b2b.cartandcheckoutaddon.download.views;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.GenericSearchConstants.LOG;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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


import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;

/**
 * 
 * 
 */
public class JnjGTOrderConfirmationPdfView extends AbstractPdfView
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

	protected static final Logger LOG = Logger.getLogger(JnjGTOrderConfirmationPdfView.class);
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	protected static final int MARGIN = 32;
	
	private static final String PO_NUMBER = "order.confirmation.ponumber"; 
	private static final String JJORDERNUMBER = "order.confirmation.jjordernumber"; 
	private static final String ORDERTYPE = "order.confirmation.ordertype"; 
	private static final String ADDRESS = "order.confirmation.address"; 
	private static final String SALES_FORBIDDEN = "order.confirmation.salesforbidden"; 
	private static final String ORDER = "order.confirmation.order"; 
	private static final String TOTAL_NET = "order.confirmation.total.net";
	private static final String TOTAL_FEES = "order.confirmation.total.fees"; 
	private static final String FREIGHT_FEES = "order.confirmation.total.freightfees"; 
	private static final String EXPEDITED_FEES = "order.confirmation.total.expeditedfees"; 
	private static final String HSA_PROMOTION = "order.confirmation.hsa.promotion"; 
	private static final String DROPSHIP_FEES = "order.confirmation.dropship.fees"; 
	private static final String MINIMUM_FEES = "order.confirmation.minimum.fees"; 
	private static final String TAXES = "order.confirmation.taxes"; 
	private static final String DISCOUNTS = "order.confirmation.discounts"; 
	private static final String TOTALGROSS = "order.confirmation.totalgross.price"; 
	private static final String ENTRY_NUMBER = "order.confirmation.entry.number"; 
	private static final String PRODUCT_NAME = "order.confirmation.product.name"; 
	private static final String PRODUCT_DESC = "order.confirmation.Product.Description"; 
	private static final String PRODUCT_QUANTITY = "order.confirmation.Product.Quantity"; 
	private static final String DELIVERY_DATE = "order.confirmation.Estimated.DeliveryDate"; 
	private static final String INDIRECT_CUST = "order.confirmation.indirectCustomer"; 
	private static final String ITEMPRICE = "order.confirmation.itemPrice"; 
	private static final String TOTAL = "order.confirmation.total"; 
	
	
	
	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */

	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter writer,	final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{

		try
		{
			document.open();
			response.setHeader("Content-Disposition", "attachment; filename=OrderConfirmation.pdf");
			
			
			final String emptyField = new String();
			final JnjGTOrderData orderData = (JnjGTOrderData) map.get("orderData");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			
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
	      	imageTable.setTotalWidth(500F);
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
	      	imageTable.addCell(cell1);
	      	imageTable.addCell(cell2);
	      	//image adding end
			
			
			
			
			
			
			
			final PdfPTable orderTable = new PdfPTable(3);
			final PdfPTable orderTableOne = new PdfPTable(3);
			final PdfPTable orderTableTwo = new PdfPTable(4);
			final PdfPTable orderTableThree = new PdfPTable(4);
			final PdfPTable orderTableFour = new PdfPTable(2);

			orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			orderTable.setTotalWidth(512f);
			orderTable.setLockedWidth(true);
			orderTable.setSpacingAfter(10f);

			orderTableOne.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			orderTableOne.setTotalWidth(512f);
			orderTableOne.setLockedWidth(true);
			orderTableOne.setSpacingAfter(10f);

			orderTableTwo.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			orderTableTwo.setTotalWidth(512f);
			orderTableTwo.setLockedWidth(true);
			orderTableTwo.setSpacingAfter(10f);

			orderTableThree.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			orderTableThree.setTotalWidth(512f);
			orderTableThree.setLockedWidth(true);
			orderTableThree.setSpacingAfter(10f);

			orderTableFour.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			orderTableFour.setTotalWidth(512f);
			orderTableFour.setLockedWidth(true);
			orderTableFour.setSpacingAfter(10f);

			final PdfPCell cell1_order = new PdfPCell(new Paragraph(messageSource.getMessage(PO_NUMBER, null, i18nService.getCurrentLocale())));
			final PdfPCell cell2_order = new PdfPCell(new Paragraph(messageSource.getMessage(JJORDERNUMBER, null, i18nService.getCurrentLocale())));
			final PdfPCell cell3_order = new PdfPCell(new Paragraph(messageSource.getMessage(ORDERTYPE, null, i18nService.getCurrentLocale())));
			final PdfPCell cell4_order = new PdfPCell(new Paragraph(messageSource.getMessage(ADDRESS, null, i18nService.getCurrentLocale())));
			final PdfPCell cell5_order = new PdfPCell(new Paragraph(messageSource.getMessage(SALES_FORBIDDEN, null, i18nService.getCurrentLocale())));
			final PdfPCell cell6_order = new PdfPCell(new Paragraph(messageSource.getMessage(ORDER, null, i18nService.getCurrentLocale())));
			final PdfPCell cell7_order = new PdfPCell(new Paragraph(messageSource.getMessage(TOTAL_NET, null, i18nService.getCurrentLocale())));
			final PdfPCell cell8_order = new PdfPCell(new Paragraph(messageSource.getMessage(TOTAL_FEES, null, i18nService.getCurrentLocale())));
			final PdfPCell cell9_order = new PdfPCell(new Paragraph(messageSource.getMessage(FREIGHT_FEES, null, i18nService.getCurrentLocale())));
			final PdfPCell cell10_order = new PdfPCell(new Paragraph(messageSource.getMessage(EXPEDITED_FEES, null, i18nService.getCurrentLocale())));
			final PdfPCell cell11_order = new PdfPCell(new Paragraph(messageSource.getMessage(HSA_PROMOTION, null, i18nService.getCurrentLocale())));
			final PdfPCell cell12_order = new PdfPCell(new Paragraph(messageSource.getMessage(DROPSHIP_FEES, null, i18nService.getCurrentLocale())));
			final PdfPCell cell13_order = new PdfPCell(new Paragraph(messageSource.getMessage(MINIMUM_FEES, null, i18nService.getCurrentLocale())));
			final PdfPCell cell14_order = new PdfPCell(new Paragraph(messageSource.getMessage(TAXES, null, i18nService.getCurrentLocale())));
			final PdfPCell cell15_order = new PdfPCell(new Paragraph(messageSource.getMessage(DISCOUNTS, null, i18nService.getCurrentLocale())));
			final PdfPCell cell16_order = new PdfPCell(new Paragraph(messageSource.getMessage(TOTALGROSS, null, i18nService.getCurrentLocale())));
			


			//			Header added 
			orderTable.addCell(cell1_order);
			orderTable.addCell(cell2_order);
			orderTable.addCell(cell3_order);

			orderTableOne.addCell(cell4_order);
			orderTableOne.addCell(cell5_order);
			orderTableOne.addCell(cell6_order);

			orderTableTwo.addCell(cell7_order);
			orderTableTwo.addCell(cell8_order);
			orderTableTwo.addCell(cell9_order);
			orderTableTwo.addCell(cell10_order);

			orderTableThree.addCell(cell11_order);
			orderTableThree.addCell(cell12_order);
			orderTableThree.addCell(cell13_order);
			orderTableThree.addCell(cell14_order);

			orderTableFour.addCell(cell15_order);
			orderTableFour.addCell(cell16_order);
			
			//Data Table 
			orderTable.addCell((orderData.getPurchaseOrderNumber() != null) ? orderData.getPurchaseOrderNumber() : emptyField);

			if (null != orderData.getSapOrderNumber()){
				orderTable.addCell((orderData.getSapOrderNumber()!= null) ? orderData.getSapOrderNumber() : emptyField);
			}else{
				orderTable.addCell((orderData.getOrderNumber()!= null) ? orderData.getOrderNumber() : emptyField);
			}
		

			orderTable.addCell((orderData.getOrderType() != null) ? orderData.getOrderType() : emptyField);

			orderTableOne.addCell((orderData.getDeliveryAddress().getFormattedAddress() != null) ? orderData.getDeliveryAddress()
					.getFormattedAddress() : emptyField);

			orderTableOne.addCell((orderData.getSalesForbidden() != null) ? orderData.getSalesForbidden() : emptyField);

			orderTableOne.addCell((orderData.getCompleteOrder() != null) ? orderData.getCompleteOrder() : emptyField);

			orderTableTwo.addCell((orderData.getTotalPrice() != null) ? orderData.getTotalPrice().getFormattedValue() : emptyField);

			orderTableTwo.addCell((orderData.getTotalNetValue() != null) ? orderData.getTotalNetValue().getFormattedValue()
					: emptyField);

			orderTableTwo.addCell((orderData.getTotalFreightFees() != null) ? orderData.getTotalFreightFees().getFormattedValue()
					: emptyField);

			orderTableTwo.addCell((orderData.getExpeditedFee() != null) ? orderData.getExpeditedFee().getFormattedValue()
					: emptyField);

			orderTableThree.addCell((orderData.getHsaPromotion() != null) ? orderData.getHsaPromotion().getFormattedValue()
					: emptyField);

			orderTableThree.addCell((orderData.getTotalDropShipFee() != null) ? orderData.getTotalDropShipFee().getFormattedValue()
					: emptyField);

			orderTableThree.addCell((orderData.getTotalminimumOrderFee() != null) ? orderData.getTotalminimumOrderFee()
					.getFormattedValue() : emptyField);

			orderTableThree.addCell((orderData.getTotalTax() != null) ? orderData.getTotalTax().getFormattedValue() : emptyField);

			orderTableFour.addCell((orderData.getDiscountTotal() != null) ? orderData.getDiscountTotal().getFormattedValue()
					: emptyField);

			orderTableFour.addCell((orderData.getTotalGrossPrice() != null) ? orderData.getTotalGrossPrice().getFormattedValue()
					: emptyField);
			document.add(imageTable);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Order Confirmation Details"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(orderTable);
			document.add(orderTableOne);
			document.add(orderTableTwo);
			document.add(orderTableThree);
			document.add(orderTableFour);

			document.add(new Paragraph("Order Enrtry  Details"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			final PdfPTable productsTable = new PdfPTable(4);
			final PdfPTable productsTableOne = new PdfPTable(4);

			productsTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			productsTable.setTotalWidth(512f);
			productsTable.setLockedWidth(true);
			productsTable.setSpacingAfter(10f);

			productsTableOne.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			productsTableOne.setTotalWidth(512f);
			productsTableOne.setLockedWidth(true);
			productsTableOne.setSpacingAfter(10f);
			
			final PdfPCell cell1_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(ENTRY_NUMBER, null, i18nService.getCurrentLocale())));
			final PdfPCell cell2_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(PRODUCT_NAME, null, i18nService.getCurrentLocale())));
			final PdfPCell cell3_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(PRODUCT_DESC, null, i18nService.getCurrentLocale())));
			final PdfPCell cell4_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(PRODUCT_QUANTITY, null, i18nService.getCurrentLocale())));
			final PdfPCell cell5_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(DELIVERY_DATE, null, i18nService.getCurrentLocale())));
			final PdfPCell cell6_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(INDIRECT_CUST, null, i18nService.getCurrentLocale())));
			final PdfPCell cell7_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(ITEMPRICE, null, i18nService.getCurrentLocale())));
			final PdfPCell cell8_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(TOTAL, null, i18nService.getCurrentLocale())));
			
			

			productsTable.addCell(cell1_orderDetail);
			productsTable.addCell(cell2_orderDetail);
			productsTable.addCell(cell3_orderDetail);
			productsTable.addCell(cell4_orderDetail);
			productsTableOne.addCell(cell5_orderDetail);
			productsTableOne.addCell(cell6_orderDetail);
			productsTableOne.addCell(cell7_orderDetail);
			productsTableOne.addCell(cell8_orderDetail);

			final List<OrderEntryData> orderEntryList = (List<OrderEntryData>) map.get("orderEntryList");


			for (final OrderEntryData orderEntryData : orderEntryList)
			{
				productsTable.addCell(String.valueOf(orderEntryData.getEntryNumber()+1));
				productsTable.addCell((orderEntryData.getProduct().getName() != null) ? orderEntryData.getProduct().getName()
						: emptyField);
				productsTable.addCell((orderEntryData.getProduct().getDescription() != null) ? orderEntryData.getProduct()
						.getDescription() : emptyField);
				productsTable.addCell(String.valueOf(orderEntryData.getQuantity()));
				if (orderEntryData instanceof JnjGTOrderEntryData)
				{
					if (((JnjGTOrderEntryData) orderEntryData).getExpectedDeliveryDate() != null)
					{
						productsTableOne.addCell(simpleDateFormat.format(((JnjGTOrderEntryData) orderEntryData).getExpectedDeliveryDate()).toString());
					}
					else
					{
						productsTableOne.addCell(StringUtils.EMPTY);
					}
					if (null != ((JnjGTOrderEntryData) orderEntryData).getIndirectCustomer())
					{
						productsTableOne.addCell(((JnjGTOrderEntryData) orderEntryData).getIndirectCustomer());
					}
					else
					{
						productsTableOne.addCell(StringUtils.EMPTY);
					}

					if (null != ((JnjGTOrderEntryData) orderEntryData).getBasePrice())
					{
						productsTableOne.addCell(((JnjGTOrderEntryData) orderEntryData).getBasePrice().getFormattedValue());
					}
				}
				else
				{
					productsTableOne.addCell(StringUtils.EMPTY);
					productsTableOne.addCell(StringUtils.EMPTY);
					if (null != orderEntryData.getBasePrice())
					{
						productsTableOne.addCell(orderEntryData.getBasePrice().getFormattedValue());
					}
				}
				if (orderEntryData.getTotalPrice() != null)
				{
					productsTableOne.addCell(orderEntryData.getTotalPrice().getFormattedValue());
				}
			}

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Setting Table in Document");
			}


			document.add(productsTable);
			document.add(productsTableOne);
			document.close();
		}

		catch (final Exception exp)
		{
			exp.printStackTrace();
			LOG.error("error while setting table - " + exp.getMessage());
		}


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
