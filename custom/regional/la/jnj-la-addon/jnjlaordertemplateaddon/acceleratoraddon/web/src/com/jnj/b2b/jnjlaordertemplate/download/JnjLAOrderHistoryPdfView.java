/**
 *
 */
package com.jnj.b2b.jnjlaordertemplate.download;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjLaOrderHistoryData;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.facades.data.JnjLaOrderData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjLaProductData;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
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

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.apache.commons.io.IOUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.apache.commons.collections4.CollectionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.jnj.core.util.JnjGTCoreUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.servicelayer.config.ConfigurationService;

public class JnjLAOrderHistoryPdfView extends AbstractPdfView {
	
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;
	private JnjLatamOrderFacade jnjlatamCustomOrderFacade;
	private static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";
	private static final int MARGIN = 32;

	
	@Autowired
	private ConfigurationService configurationService;

	@Override
	protected Document newDocument() {
		return new Document(PageSize.A4.rotate());
	}

	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception {
		// for page number added
		final MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());
		pdfWriter.setPageEvent(events);
		events.onOpenDocument(pdfWriter, document);

		arg4.setHeader("Content-Disposition", "attachment; filename=OrderHistorySearchResult.pdf");
		final SearchPageData<JnjLaOrderHistoryData> searchPageData = (SearchPageData<JnjLaOrderHistoryData>) map
				.get("searchPageData");

		final List<JnjLaOrderHistoryData> results = searchPageData.getResults();
		final String currentSite = (String) map.get(Jnjb2bCoreConstants.SITE_NAME);
		final boolean isMddSite = (currentSite != null && Jnjb2bCoreConstants.MDD.equals(currentSite)) ? true : false;
		final Boolean resultLimitExceeded = (Boolean) map.get("resultLimitExceeded");
		// image adding start
		final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
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

		// image adding end

		final PdfPTable headerTable = new PdfPTable(1);
		headerTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		headerTable.setTotalWidth(822F);
		headerTable.setLockedWidth(true);
		headerTable.setSpacingAfter(50f);

		if (resultLimitExceeded.booleanValue()) {
			document.add(new Paragraph(RESULTS_EXCEEDED_MESSAGE));
			document.add(new Paragraph("\n"));
		}

		final PdfPCell headerCell = new PdfPCell(
				new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.orderHistory")));
		headerCell.setColspan(11);
		headerTable.addCell(headerCell);

		final PdfPTable orderTable = new PdfPTable(6);
		orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		orderTable.setTotalWidth(822F);
		orderTable.setLockedWidth(true);
		orderTable.setSpacingAfter(736f);

		orderTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.clientNumber"));
		orderTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.jnJOrderHistoryNumber"));
		orderTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.contrctNumber"));
		orderTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.estimatedDeliveryDate"));
		orderTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.totalOrder"));
		orderTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("order.history.orderStatus"));

		final String emptyField = new String();
		final DateFormat dateFormat = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.DATE_FORMAT);
		if (results != null && !results.isEmpty()) {
			for (final JnjLaOrderHistoryData data : results) {
				orderTable.addCell(data.getPurchaseOrderNumber());
				orderTable.addCell(data.getSapOrderNumber());
				orderTable.addCell(data.getContractNumber());
				
				JnjLaOrderData orderData = (JnjLaOrderData) getJnjlatamCustomOrderFacade().getOrderDetailsForCode(data.getCode(), false);
				List<OrderEntryData> orderEntryDataList = orderData.getEntries();
				StringBuilder eddAndQty = new StringBuilder();				
				for (final OrderEntryData orderEntryData : orderEntryDataList) {
					final JnjLaOrderEntryData jnjOrderEntryData = (JnjLaOrderEntryData) orderEntryData; 
					final JnjLaProductData product = (JnjLaProductData) orderEntryData.getProduct();

					List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList = jnjOrderEntryData.getScheduleLines();

					populateDeliveryScheduleData(dateFormat, eddAndQty,
							product, jnjDeliveryScheduleDataList);
				}
				
				orderTable.addCell(StringUtils.isNotEmpty(eddAndQty.toString()) ? eddAndQty.toString() : jnjCommonFacadeUtil
						.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));				
				orderTable.addCell((data.getTotal() != null) ? data.getTotal().getFormattedValue() : emptyField);
				orderTable.addCell((data.getStatus() != null) ? data.getStatusDisplay() : emptyField);
			}
		}
		document.add(table);
		document.add(headerTable);
		document.add(Chunk.NEWLINE);
		document.add(orderTable);
	}

	private void populateDeliveryScheduleData(DateFormat dateFormat,
			StringBuilder eddAndQty, JnjLaProductData product,
			List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList) {
		if (CollectionUtils.isNotEmpty(jnjDeliveryScheduleDataList)) {
			for (JnjDeliveryScheduleData pacHiveSchedule : jnjDeliveryScheduleDataList) {
				try {
					populateDeliveryScheduleData(dateFormat, eddAndQty,
							product, pacHiveSchedule);
				} catch (ParseException pe) {
					JnjGTCoreUtil.logErrorMessage("EXCEL SHEET",
							"buildPdfDocument()", "Error while writing Data.",
							pe, JnjLAOrderHistoryPdfView.class);
				}
			}
		}
	}

	private void populateDeliveryScheduleData(DateFormat dateFormat, StringBuilder eddAndQty, JnjLaProductData product, JnjDeliveryScheduleData pacHiveSchedule) throws ParseException {
		String date1 = configurationService.getConfiguration()
				.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DATE);
		String date2 = configurationService.getConfiguration()
				.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DT);

		Date defaultDate1 = dateFormat.parse(date1);
		Date defaultDate2 = dateFormat.parse(date2);
		if (null != pacHiveSchedule
						.getDeliveryDate()) {
			populateDeliveryScheduleData(dateFormat, eddAndQty, product, pacHiveSchedule, defaultDate1, defaultDate2);
		} else {
			eddAndQty
					.append(jnjCommonFacadeUtil
							.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
			if (null != pacHiveSchedule.getQuantity()) {
				eddAndQty.append(" - "+ pacHiveSchedule.getQuantity()+ " " + product.getDeliveryUnitCode());
				}
			eddAndQty.append("\n");
			}
	}

	private void populateDeliveryScheduleData(DateFormat dateFormat, StringBuilder eddAndQty, JnjLaProductData product, JnjDeliveryScheduleData pacHiveSchedule, Date defaultDate1, Date defaultDate2) throws ParseException {
		Date deliveryDate = dateFormat.parse(dateFormat
				.format(pacHiveSchedule
						.getDeliveryDate()));
		if (defaultDate1.compareTo(deliveryDate) != 0 && defaultDate2.compareTo(deliveryDate) != 0) {
			if (null != pacHiveSchedule.getQuantity()) {
			eddAndQty.append(dateFormat
					.format(pacHiveSchedule
							.getDeliveryDate()) + " - "
					+ pacHiveSchedule.getQuantity()
					+ " "
					+ product.getDeliveryUnitCode()
					+ "\n");
			} else {
				eddAndQty.append(dateFormat
						.format(pacHiveSchedule
								.getDeliveryDate()) + "\n");
			}
		} else {
			eddAndQty
			.append(jnjCommonFacadeUtil
					.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
			if (null != pacHiveSchedule.getQuantity()) {
				eddAndQty.append(" - "+ pacHiveSchedule.getQuantity()+ " " + product.getDeliveryUnitCode());
				}
			eddAndQty.append("\n");

}
	}


	private static class MyPageEvents extends PdfPageEventHelper {

		private final MessageSourceAccessor messageSourceAccessor;

		private PdfContentByte cb;

		private PdfTemplate template;

		private BaseFont bf = null;

		public MyPageEvents(final MessageSourceAccessor messageSourceAccessor) {
			this.messageSourceAccessor = messageSourceAccessor;
		}

		public void onOpenDocument(final PdfWriter writer, final Document document) {
			try {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb = writer.getDirectContent();
				template = cb.createTemplate(50, 50);
			} catch (final DocumentException de) {
			} catch (final IOException ioe) {
			}
		}

		public void onEndPage(final PdfWriter writer, final Document document) {
			final int pageN = writer.getPageNumber();
			final String text = messageSourceAccessor.getMessage("page", "page") + " " + pageN + " "
					+ messageSourceAccessor.getMessage("on", "on") + " ";
			final float len = bf.getWidthPoint(text, 8);
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

		public void onCloseDocument(final PdfWriter writer, final Document document) {
			template.beginText();
			template.setFontAndSize(bf, 8);
			template.showText(String.valueOf(writer.getPageNumber() - 1));
			template.endText();
		}
	}
	
	public JnjLatamOrderFacade getJnjlatamCustomOrderFacade() {
		return jnjlatamCustomOrderFacade;
	}

	public void setJnjlatamCustomOrderFacade(final JnjLatamOrderFacade jnjlatamCustomOrderFacade) {
		this.jnjlatamCustomOrderFacade = jnjlatamCustomOrderFacade;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}

}
