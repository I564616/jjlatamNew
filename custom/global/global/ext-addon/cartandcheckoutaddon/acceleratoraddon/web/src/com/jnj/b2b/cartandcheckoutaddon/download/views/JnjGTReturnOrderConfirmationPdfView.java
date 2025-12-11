package com.jnj.b2b.cartandcheckoutaddon.download.views;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


/**
 * 
 * 
 */
public class JnjGTReturnOrderConfirmationPdfView extends AbstractPdfView
{
	
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
	
	private static final String PO_NUMBER = "cart.return.entries.poNumber";
	private static final String JNJ_ORDERNUMBER = "return.confirmation.table.jnjorderNumber";
	private static final String ORDER_TYPE = "return.confirmation.order.type";
	private static final String ADDRESS = "return.confirmation.address";
	private static final String REASON_FOR_RETURN = "return.confirmation.reasonfor.return";
	private static final String CUST_PO_REFERENCE = "cart.return.returnInfo.customerReferencePO";
	private static final String ENTRYNUMBER = "return.confirmation.entry.number";
	private static final String PRODUCTNAME = "return.confirmation.productName";
	private static final String PRODUCT_DESC = "return.confirmation.productDescription";
	private static final String JNJID = "return.confirmation.jnjid";
	private static final String QUANTITY = "return.confirmation.product.qty";
	private static final String GTIN = "return.confirmation.gtin";
	private static final String LOT = "return.confirmation.lot";
	private static final String INVOICE_NUMBER = "cart.return.entries.invoiceNumber";


	protected static final Logger LOG = Logger.getLogger(JnjGTOrderConfirmationPdfView.class);


	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */

	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter writer,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{

		try
		{
			document.open();
			response.setHeader("Content-Disposition", "attachment; filename=OrderConfirmation.pdf");
			final String emptyField = new String();
			final JnjGTOrderData orderData = (JnjGTOrderData) map.get("orderData");
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
			final PdfPCell cell2_order = new PdfPCell(new Paragraph(messageSource.getMessage(JNJ_ORDERNUMBER, null, i18nService.getCurrentLocale())));
			final PdfPCell cell3_order = new PdfPCell(new Paragraph(messageSource.getMessage(ORDER_TYPE, null, i18nService.getCurrentLocale())));
			final PdfPCell cell4_order = new PdfPCell(new Paragraph(messageSource.getMessage(ADDRESS, null, i18nService.getCurrentLocale())));
			final PdfPCell cell5_order = new PdfPCell(new Paragraph(messageSource.getMessage(REASON_FOR_RETURN, null, i18nService.getCurrentLocale())));
			final PdfPCell cell6_order = new PdfPCell(new Paragraph(messageSource.getMessage(CUST_PO_REFERENCE, null, i18nService.getCurrentLocale())));


			//			Header added 
			orderTable.addCell(cell1_order);
			orderTable.addCell(cell2_order);
			orderTable.addCell(cell3_order);

			orderTableOne.addCell(cell4_order);
			orderTableOne.addCell(cell5_order);
			orderTableOne.addCell(cell6_order);


			//Data Table 
			orderTable.addCell((orderData.getPurchaseOrderNumber() != null) ? orderData.getPurchaseOrderNumber() : emptyField);

			orderTable.addCell((orderData.getSapOrderNumber() != null) ? orderData.getSapOrderNumber() : emptyField);

			orderTable.addCell((orderData.getOrderType() != null) ? orderData.getOrderType() : emptyField);

			orderTableOne.addCell((orderData.getDeliveryAddress().getFormattedAddress() != null) ? orderData.getDeliveryAddress()
					.getFormattedAddress() : emptyField);

			orderTableOne.addCell((orderData.getReasonCodeReturn() != null) ? orderData.getReasonCodeReturn() : emptyField);

			orderTableOne.addCell((orderData.getCustomerPoNumber() != null) ? orderData.getCustomerPoNumber() : emptyField);

			document.add(imageTable);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Order Confirmation Details"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(orderTable);
			document.add(orderTableOne);
			/*document.add(orderTableTwo);
			document.add(orderTableThree);
			document.add(orderTableFour);*/
			document.add(new Paragraph("Order Enrtry  Details"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			final PdfPTable productsTable = new PdfPTable(4);
			final PdfPTable productsTableOne = new PdfPTable(5);

			productsTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			productsTable.setTotalWidth(512f);
			productsTable.setLockedWidth(true);
			productsTable.setSpacingAfter(10f);

			productsTableOne.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			productsTableOne.setTotalWidth(512f);
			productsTableOne.setLockedWidth(true);
			productsTableOne.setSpacingAfter(10f);

			final PdfPCell cell1_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(ENTRYNUMBER, null, i18nService.getCurrentLocale())));
			final PdfPCell cell2_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(PRODUCTNAME, null, i18nService.getCurrentLocale())));
			final PdfPCell cell3_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(PRODUCT_DESC, null, i18nService.getCurrentLocale())));
			final PdfPCell cell4_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(JNJID, null, i18nService.getCurrentLocale())));
			final PdfPCell cell5_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(QUANTITY, null, i18nService.getCurrentLocale())));
			final PdfPCell cell6_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(GTIN, null, i18nService.getCurrentLocale())));
			final PdfPCell cell7_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(LOT, null, i18nService.getCurrentLocale())));
			final PdfPCell cell8_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(PO_NUMBER, null, i18nService.getCurrentLocale())));
			final PdfPCell cell9_orderDetail = new PdfPCell(new Paragraph(messageSource.getMessage(INVOICE_NUMBER, null, i18nService.getCurrentLocale())));

			productsTable.addCell(cell1_orderDetail);
			productsTable.addCell(cell2_orderDetail);
			productsTable.addCell(cell3_orderDetail);
			productsTable.addCell(cell4_orderDetail);
			productsTableOne.addCell(cell5_orderDetail);
			productsTableOne.addCell(cell6_orderDetail);
			productsTableOne.addCell(cell7_orderDetail);
			productsTableOne.addCell(cell8_orderDetail);
			productsTableOne.addCell(cell9_orderDetail);

			final List<OrderEntryData> orderEntryList = (List<OrderEntryData>) map.get("orderEntryList");


			for (final OrderEntryData orderEntryData : orderEntryList)
			{
				final JnjGTProductData productData = (JnjGTProductData) orderEntryData.getProduct();
				productsTable.addCell(String.valueOf(orderEntryData.getEntryNumber()));
				productsTable.addCell((productData.getName() != null) ? productData.getName() : emptyField);
				productsTable.addCell((productData.getDescription() != null) ? productData.getDescription() : emptyField);
				productsTable.addCell((productData.getCode() != null) ? productData.getCode() : emptyField);
				productsTableOne.addCell(String.valueOf(orderEntryData.getQuantity()));
				productsTableOne.addCell((productData.getGtin() != null) ? productData.getGtin() : emptyField);
				if (orderEntryData instanceof JnjGTOrderEntryData)
				{
					if (((JnjGTOrderEntryData) orderEntryData).getLotNumber() != null)
					{
						productsTableOne.addCell(((JnjGTOrderEntryData) orderEntryData).getLotNumber());
					}
					else
					{
						productsTableOne.addCell(StringUtils.EMPTY);
					}
					if (null != ((JnjGTOrderEntryData) orderEntryData).getPoNumber())
					{
						productsTableOne.addCell(((JnjGTOrderEntryData) orderEntryData).getPoNumber());
					}
					else
					{
						productsTableOne.addCell(StringUtils.EMPTY);
					}

					if (null != ((JnjGTOrderEntryData) orderEntryData).getReturnInvNumber())
					{
						productsTableOne.addCell(((JnjGTOrderEntryData) orderEntryData).getReturnInvNumber());
					}
					else
					{
						productsTableOne.addCell(StringUtils.EMPTY);
					}
				}
				else
				{
					productsTableOne.addCell(StringUtils.EMPTY);
					productsTableOne.addCell(StringUtils.EMPTY);
					productsTableOne.addCell(StringUtils.EMPTY);
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
}
