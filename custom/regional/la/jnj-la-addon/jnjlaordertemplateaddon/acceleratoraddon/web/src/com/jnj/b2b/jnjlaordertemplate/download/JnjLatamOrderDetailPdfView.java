package com.jnj.b2b.jnjlaordertemplate.download;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.util.Config;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.DateFormat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.b2b.jnjglobalordertemplate.download.JnjGTOrderDetailPdfView.MyPageEvents;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTShippingTrckInfoData;
import com.jnj.facades.data.SurgeryInfoData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facades.data.JnjLaOrderData;
import com.jnj.b2b.jnjlaordertemplate.constants.JnjlaordertemplateaddonConstants;
import com.jnj.b2b.jnjlaordertemplate.controllers.JnjlaordertemplateaddonControllerConstants;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.jnj.facades.data.JnjLaOrderData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.la.core.util.JnJLALanguageDateFormatUtil;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import org.apache.commons.collections4.MapUtils;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.text.ParseException;

import com.jnj.core.util.JnjGTCoreUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Class responsible to create PDF view for Order detail.
 *
 * @author Accenture
 *
 */
public class JnjLatamOrderDetailPdfView extends AbstractPdfView {
	private static final String NEW_LINE_CAHARACTER = "\n";
	
	
	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	private ConfigurationService configurationService;

    @Autowired
    private CommonI18NService commonI18NService;
    
	protected SessionService sessionService;
	
    public SessionService getSessionService() {
		return sessionService;
	}
    public void setSessionService(final SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
     * @return the commonI18NService
     */
    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }

    /**
     * @param commonI18NService
     *            the commonI18NService to set
     */
    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }

    @Override
    protected Document newDocument()
    {
        return new Document(PageSize.A3.rotate());
    }

	@Override
	protected void buildPdfDocument(final Map<String, Object> arg0, final Document arg1, final PdfWriter arg2,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception {
		arg4.setHeader("Content-Disposition", "attachment; filename=OrderDetailResult.pdf");
		String paymentCode = " ";
		final StringBuilder address = new StringBuilder(" ");
		final JnjLaOrderData orderDetails = (JnjLaOrderData) arg0.get("orderData");
		// image adding start
		final InputStream jnjConnectLogoIS = (InputStream) arg0.get("jnjConnectLogoURL");
		final byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
		Image jnjConnectLogo = null;
		PdfPCell cell1 = null;
		final PdfPTable table = new PdfPTable(1); // 3 columns.
		table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		table.setTotalWidth(822F);
		table.setLockedWidth(true);
		table.setSpacingAfter(30f);

		if (jnjConnectLogoByteArray != null) {
			jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
			cell1 = new PdfPCell(jnjConnectLogo, false);
			cell1.setBorder(Rectangle.NO_BORDER);
		}

        table.addCell(cell1);
        arg1.add(table);
        // image adding end
        if (null != orderDetails.getPaymentType() && orderDetails.getPaymentType().getCode() != null
                && orderDetails.getPaymentType().getCode().equalsIgnoreCase("ACCOUNT")) {
            paymentCode = orderDetails.getPaymentType().getCode();
        }
        if (null != orderDetails.getDeliveryAddress()) {
            if (orderDetails.getDeliveryAddress().getTitle() != null) {
                address.append(orderDetails.getDeliveryAddress().getTitle() + " ");
            }
            if (orderDetails.getDeliveryAddress().getFirstName() != null) {
                address.append(orderDetails.getDeliveryAddress().getFirstName() + " ");
            }
            if (orderDetails.getDeliveryAddress().getLastName() != null) {
                address.append(orderDetails.getDeliveryAddress().getLastName());
            }
            address.append("\t");
            if (orderDetails.getDeliveryAddress().getLine1() != null) {
                address.append(orderDetails.getDeliveryAddress().getLine1() + "\t ");
            }
            if (orderDetails.getDeliveryAddress().getLine2() != null) {
                address.append(orderDetails.getDeliveryAddress().getLine2() + "\t ");
            }
            if (orderDetails.getDeliveryAddress().getTown() != null) {
                address.append(orderDetails.getDeliveryAddress().getTown() + "\t ");
            }
            if (orderDetails.getDeliveryAddress().getPostalCode() != null) {
                address.append(orderDetails.getDeliveryAddress().getPostalCode() + "\t ");
            }
            if (orderDetails.getDeliveryAddress().getCountry() != null) {
                address.append(orderDetails.getDeliveryAddress().getCountry().getName() + "\t ");
            }
        }

        // ORDER DETAILS
        String language = "en";

        if (null != commonI18NService.getCurrentLanguage()) {
            language = commonI18NService.getCurrentLanguage().getIsocode().toLowerCase();
        }
        final String emptyField = new String();
        final Font boldFont = FontFactory.getFont("Arial", 12, Font.BOLD);
        final Paragraph header = new Paragraph(
                getJnjCommonFacadeUtil().getMessageFromImpex("order.history.orderDetail"), boldFont);
        arg1.add(new Paragraph(header));
        arg1.add(new Paragraph("\n"));
        arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.clientOrderNumber")
                + orderDetails.getPurchaseOrderNumber()));
        arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.overAllStatus") + "\t"
                + orderDetails.getStatusDisplay()));
        arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.orderDetailsContractNumber")
                + (orderDetails.getContractNumber() != null ? orderDetails.getContractNumber() : " ")));
        arg1.add(new Paragraph(
                getJnjCommonFacadeUtil().getMessageFromImpex("order.history.orderType") + (getJnjCommonFacadeUtil()
                        .getMessageFromImpex("cart.common.orderType." + orderDetails.getOrderType()))));
        arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.customerName")
                + ((orderDetails.getStatusDisplay() != null) ? orderDetails.getOrderedBy() : emptyField)));

		arg1.add(new Paragraph(
				getJnjCommonFacadeUtil().getMessageFromImpex("order.history.deliveryDetail") + address.toString()));

		arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.jnJOrderNumber")
				+ (orderDetails.getSapOrderNumber() != null ? orderDetails.getSapOrderNumber() : " ")));

        arg1.add(
                new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.placedOn") + ":"
                        + ((orderDetails.getCreated() != null) ? JnJLALanguageDateFormatUtil
                                .getLanguageSpecificDateOrderDetail(language, orderDetails.getCreated())
                                : emptyField)));

		// ORDER LINE DETAILS
		final PdfPTable orderLineTable = new PdfPTable(6);
		orderLineTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		orderLineTable.setTotalWidth(1100F);
		orderLineTable.setLockedWidth(true);

		orderLineTable.addCell(new PdfPCell(
				new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.items"), boldFont)));
		orderLineTable.addCell(new PdfPCell(new Phrase(
				getJnjCommonFacadeUtil().getMessageFromImpex("order.history.estimatedDeliveryDate"), boldFont)));

		orderLineTable.addCell(new PdfPCell(
				new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.quantity"), boldFont)));

		orderLineTable.addCell(new PdfPCell(
				new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.itemPrice"), boldFont)));

		orderLineTable.addCell(new PdfPCell(
				new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.total"), boldFont)));
		orderLineTable.addCell(new PdfPCell(new Phrase(
				getJnjCommonFacadeUtil().getMessageFromImpex("order.history.orderDetailsStatus"), boldFont)));

		for (final OrderEntryData entry : orderDetails.getEntries()) {
			if (entry instanceof JnjLaOrderEntryData) {
				final JnjLaOrderEntryData entryData = (JnjLaOrderEntryData) entry;
				String quantity = " ";
				final StringBuilder jnjProdDesc = new StringBuilder(" ");
				final StringBuilder jnjProdQty = new StringBuilder(" ");
				final JnjLaProductData product = (JnjLaProductData) entryData.getProduct();
				quantity = entryData.getQuantity() != null ? entryData.getQuantity().toString() : " ";

				if (product.getDeliveryUnit() != null) {
					jnjProdDesc.append(product.getDeliveryUnit().toUpperCase());
				}

				if (product.getSalesUnit() != null) {
					jnjProdDesc.append("(");
					jnjProdDesc.append(quantity);
					jnjProdDesc.append("  ");
					jnjProdDesc.append(product.getSalesUnit());
					jnjProdDesc.append(")");

					jnjProdQty.append(product.getSalesUnit());
					jnjProdQty.append("\n");
					jnjProdQty.append("( ");
					jnjProdQty.append(getJnjCommonFacadeUtil().getMessageFromImpex("product.multiple"));
					jnjProdQty.append(" ");
					jnjProdQty.append(quantity);
					jnjProdQty.append(")");
				}

				orderLineTable.addCell(new PdfPCell(new Phrase((product.getCode() + "\n" + product.getName()))));

				final Map<OrderEntryData, Boolean> showATPFlagMap= (Map<OrderEntryData, Boolean>)sessionService.getAttribute("showATPFlagMap");			
				
				if( orderDetails.getPartialDelivFlag() && !orderDetails.getHoldCreditCardFlag() && MapUtils.getBooleanValue(showATPFlagMap, entryData)) 
				{
					final DateFormat dateFormat = new SimpleDateFormat(Jnjlab2bcoreConstants.LA_DATE_FORMAT);
					StringBuilder eddAndQty = new StringBuilder();

					populateDeliveryScheduleData(orderLineTable, entryData,
							product, dateFormat, eddAndQty);
					
				 }
				else
				{
					orderLineTable.addCell(new PdfPCell(
	                        new Phrase(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE))));
				}
				
                final String quantity_prod = entryData.getQuantity() != null ? entryData.getQuantity().toString() : "0";
                orderLineTable.addCell(new PdfPCell(new Phrase(quantity_prod + "\t" + jnjProdQty)));

				orderLineTable.addCell(new PdfPCell(new Phrase((entryData.getBasePrice() != null)
						? entryData.getBasePrice().getFormattedValue() : emptyField)));

				orderLineTable.addCell(new PdfPCell(new Phrase((entryData.getTotalPrice() != null)
						? entryData.getTotalPrice().getFormattedValue() : emptyField)));

				orderLineTable.addCell(new PdfPCell(new Phrase(getJnjCommonFacadeUtil()
						.getMessageFromImpex("order.status." + entryData.getStatus() + ".value"))));

			}
		}

		// adding data table
		boolean showTotalPrice = false, showTotalFee = false, showTotalTax = false, showDiscountTotal = false,
				showTotalGrossPrice = false;
		if (orderDetails.getSubTotal() != null
				&& orderDetails.getSubTotal().getValue().doubleValue() > Double.valueOf(0).doubleValue()) {
			showTotalPrice = true;
		}
		if (orderDetails.getTotalFees() != null
				&& orderDetails.getTotalFees().getValue().doubleValue() > Double.valueOf(0).doubleValue()) {
			showTotalFee = true;
		}
		if (orderDetails.getTotalTax() != null
				&& orderDetails.getTotalTax().getValue().doubleValue() > Double.valueOf(0).doubleValue()) {
			showTotalTax = true;
		}
		if (orderDetails.getTotalDiscounts() != null
				&& orderDetails.getTotalDiscounts().getValue().doubleValue() > Double.valueOf(0).doubleValue()) {
			showDiscountTotal = true;
		}
		if (orderDetails.getTotalGrossPrice() != null
				&& orderDetails.getTotalGrossPrice().getValue().doubleValue() > Double.valueOf(0).doubleValue()) {
			showTotalGrossPrice = true;
		}

		final Paragraph lineDetails = new Paragraph(
				getJnjCommonFacadeUtil().getMessageFromImpex("checkOut.orderConfirmation.orderDetails"), boldFont);

		arg1.add(lineDetails);
		arg1.add(new Paragraph("\n"));
		arg1.add(orderLineTable);
		arg1.add(new Paragraph("\n"));
		final Paragraph lineDetails2 = new Paragraph(
				getJnjCommonFacadeUtil().getMessageFromImpex("text.account.order.orderTotals"), boldFont);
		arg1.add(lineDetails2);

		if (showTotalPrice) {
			arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.totalNetPrice")
					+ orderDetails.getSubTotal().getFormattedValue()));

		}
		if (showTotalFee) {
			arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.totalFees")
					+ orderDetails.getTotalFees().getFormattedValue()));
		}

		if (showTotalTax) {
			arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.taxes")
					+ orderDetails.getTotalTax().getFormattedValue()));
		}
		if (showDiscountTotal) {
			arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.promotions")
					+ orderDetails.getTotalDiscounts().getFormattedValue()));
		}
		if (showTotalGrossPrice) {
			arg1.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.totalGrossPrice")
					+ orderDetails.getTotalGrossPrice().getFormattedValue()));

		}

	}

	private void populateDeliveryScheduleData(PdfPTable orderLineTable,
			JnjLaOrderEntryData entryData, JnjLaProductData product,
			DateFormat dateFormat, StringBuilder eddAndQty) {
		if (null != entryData.getScheduleLines()) {
			List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList = entryData
					.getScheduleLines();

			for (JnjDeliveryScheduleData pacHiveSchedule : jnjDeliveryScheduleDataList) {
				try {
					populateDeliveryScheduleData(product, dateFormat,
							eddAndQty, pacHiveSchedule);
				} catch (ParseException pe) {
					JnjGTCoreUtil.logErrorMessage("PDF Doc",
							"buildPdfDocument()", "Error while writing Data.",
							pe, JnjLatamOrderDetailPdfView.class);
				}

			}
			if (StringUtils.isNotEmpty(eddAndQty.toString())) {
				orderLineTable.addCell(new PdfPCell(new Phrase(eddAndQty
						.toString())));
			} else {
				orderLineTable
						.addCell(new PdfPCell(
								new Phrase(
										jnjCommonFacadeUtil
												.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE))));
			}
		} else {
			orderLineTable
					.addCell(new PdfPCell(
							new Phrase(
									jnjCommonFacadeUtil
											.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE))));
		}
	}

	private void populateDeliveryScheduleData(JnjLaProductData product, DateFormat dateFormat, StringBuilder eddAndQty, JnjDeliveryScheduleData pacHiveSchedule) throws ParseException {
		String date1 = configurationService.getConfiguration()
				 .getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DATE);
		String date2 = configurationService.getConfiguration()
				.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DT);

		Date defaultDate1 = dateFormat.parse(date1);
		Date defaultDate2 = dateFormat.parse(date2);

		if (null != pacHiveSchedule.getDeliveryDate()){
		Date deliveryDate = dateFormat.parse(dateFormat.format(pacHiveSchedule.getDeliveryDate()));
		if (defaultDate1.compareTo(deliveryDate) != 0 && defaultDate2.compareTo(deliveryDate) != 0) {
			 if (null!= pacHiveSchedule.getQuantity()) {
			   eddAndQty.append(dateFormat.format(pacHiveSchedule.getDeliveryDate()) + " - " + pacHiveSchedule.getQuantity() + " " + product.getDeliveryUnitCode() +"\n");
			 } else {
				 eddAndQty.append(dateFormat.format(pacHiveSchedule.getDeliveryDate()) +"\n");
			 }
		} else {
			eddAndQty.append(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
			if (null != pacHiveSchedule.getQuantity()) {
			eddAndQty.append(" - "+ pacHiveSchedule.getQuantity()+ " " + product.getDeliveryUnitCode());
			}
			eddAndQty.append("\n");
		}
		}else {
			eddAndQty.append(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
			if (null != pacHiveSchedule.getQuantity()) {
				eddAndQty.append(" - "+ pacHiveSchedule.getQuantity()+ " " + product.getDeliveryUnitCode());
				}
			eddAndQty.append("\n");
		}
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}

}
