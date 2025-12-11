
package com.jnj.b2b.jnjlaordertemplate.download;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.data.JnJInvoiceEntryData;
import com.jnj.facades.data.JnJInvoiceOrderData;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjLaOrderData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjLaOrderHistoryData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.invoice.JnjLatamInvoiceFacade;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.facades.util.impl.JnjLatamCommonFacadeUtilImpl;
import com.jnj.facade.util.JnjCommonFacadeUtil;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.text.ParseException;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @author plahiri1
 *
 */
public class JnjLAOrderHistoryExcelView extends AbstractXlsView {

	private JnjLatamCommonFacadeUtilImpl jnjLatamCommonFacadeUtil;

	private static final String CONSTANT_ZERO = "0";
	private static final String SHEET_NAME = "Order History";
	private static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";
	private JnjLatamOrderFacade jnjlatamCustomOrderFacade;
	private JnjLatamInvoiceFacade jnjLatamInvoiceFacade;
	private CommonI18NService commonI18NService;
	public static final String LANGUAGE_ENGLISH_ISO = "en";
	private static final String EDD_EXCLUDED_STATUS = "edd.excluded.status";
	private static final String INVOICE_LOT_NUMBER_MAP = "invoiceLotNumberMap";
	private static final String INVOICE_PRODUCT_MAP = "invoiceProductMap";
	
	
	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	private ConfigurationService configurationService;

	public JnjLatamCommonFacadeUtilImpl getJnjLatamCommonFacadeUtil() {
		return jnjLatamCommonFacadeUtil;
	}

	public void setJnjLatamCommonFacadeUtil(final JnjLatamCommonFacadeUtilImpl jnjLatamCommonFacadeUtil) {
		this.jnjLatamCommonFacadeUtil = jnjLatamCommonFacadeUtil;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1,
			final HttpServletRequest arg2, final HttpServletResponse arg3) throws Exception {
		arg3.setHeader("Content-Disposition", "attachment; filename=OrderHistorySearchResult.xls");
		try {
			final boolean ignoreRestriction = false;
			final SearchPageData<JnjLaOrderHistoryData> searchPageData = (SearchPageData<JnjLaOrderHistoryData>) arg0
					.get("searchPageData");
			final List<JnjLaOrderHistoryData> results = searchPageData.getResults();

			final Boolean resultLimitExceeded = (Boolean) arg0.get("resultLimitExceeded");

			final Sheet sheet = arg1.createSheet(SHEET_NAME);
			setHeaderImage(arg1, sheet, (InputStream) arg0.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY));
			int rowNumber = 6;
			if (resultLimitExceeded.booleanValue()) {
				final Row note = sheet.createRow(rowNumber++);

				note.createCell(0).setCellValue(RESULTS_EXCEEDED_MESSAGE);
			}

			final Row header = sheet.createRow(rowNumber++);

			final Font font = arg1.createFont();
			font.setBold(true);

			final CellStyle style = arg1.createCellStyle();
			style.setFont(font);

			header.createCell(0).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.sapno"));
			header.createCell(1).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.pono"));
			header.createCell(2).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.doctype"));
			header.createCell(3).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.soldtoaccno"));
			header.createCell(4).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.soldtoaccname"));
			header.createCell(5).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.shiptoaccno"));
			header.createCell(6).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.payfrmaccno"));
			header.createCell(7).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.socreationdate"));
			header.createCell(8).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.potype"));
			header.createCell(9).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.sapmatno"));
			header.createCell(10).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.matno"));
			header.createCell(11).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.custmatno"));
			header.createCell(12).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.salesuom"));
			header.createCell(13).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.unitprice"));
			header.createCell(14).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.saporderlineno"));
			header.createCell(15).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.schlineno"));
			header.createCell(16).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.overallstatus"));
			header.createCell(17).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.confqty"));
			header.createCell(18).setCellValue(getJnjLatamCommonFacadeUtil()
					.getMessageFromImpex("order.detail.download.header.estimateddelieverydate"));
			header.createCell(19).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.orderstatus"));
			header.createCell(20).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.contractref"));
			header.createCell(21).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.indirectcustacc"));
			header.createCell(22).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.lotno"));
			header.createCell(23).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.invoiceno"));
			header.createCell(24).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.createdby"));
			header.createCell(25).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.reqdeliverydate"));
			int entryRowNumber = rowNumber++;
			extractedOrderHistoryData(ignoreRestriction, results, sheet, entryRowNumber);
		} catch (final Exception exception) {
			JnjGTCoreUtil.logErrorMessage("EXCEL SHEET", "buildExcelDocument()", "Error while writing Data.", exception,
					JnjLAOrderHistoryExcelView.class);

		}
	}

	private void extractedOrderHistoryData(boolean ignoreRestriction, List<JnjLaOrderHistoryData> results, Sheet sheet, int entryRowNumber) throws BusinessException {
		if (results != null && !results.isEmpty()) {
			for (final JnjLaOrderHistoryData data : results) {
				JnjLaOrderData orderData = null;
				List<OrderEntryData> orderEntryDataList = null;
				List<JnJInvoiceOrderData> orderInvoices = null;
				
				orderData = (JnjLaOrderData) getJnjlatamCustomOrderFacade().getOrderDetailsForCode(data.getCode(),
						ignoreRestriction);

				orderEntryDataList = orderData.getEntries();
				entryRowNumber = getEntryRowNumberForOrderEntryData(sheet, entryRowNumber, data, orderData, orderEntryDataList, orderInvoices);
			}
		}
	}

	private int getEntryRowNumberForOrderEntryData(Sheet sheet, int entryRowNumber, JnjLaOrderHistoryData data, JnjLaOrderData orderData, List<OrderEntryData> orderEntryDataList, List<JnJInvoiceOrderData> orderInvoices) throws BusinessException {
		List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList;
		
		if (null != orderEntryDataList) {
			final String sapOrderNumber = orderData.getSapOrderNumber();

			if (sapOrderNumber != null && !sapOrderNumber.isEmpty()) {
				orderInvoices = getJnjLatamInvoiceFacade().getInvoices(sapOrderNumber);
			}
			
			Map<String, Map<String, String>> invoiceData = new HashMap<>();
			
			if (null != orderInvoices) {				
				invoiceData.put(INVOICE_PRODUCT_MAP, getInvoiceMaterialMap(orderInvoices));
				invoiceData.put(INVOICE_LOT_NUMBER_MAP, getInvoiceLotNumberMap(orderInvoices));
			}

			for (final OrderEntryData orderEntryData : orderEntryDataList) {
				final JnjLaOrderEntryData jnjOrderEntryData = (JnjLaOrderEntryData) orderEntryData;

				jnjDeliveryScheduleDataList = jnjOrderEntryData.getScheduleLines();

				if (null != jnjDeliveryScheduleDataList && !jnjDeliveryScheduleDataList.isEmpty()) {
					entryRowNumber = setDataToExcelSheet(entryRowNumber, sheet, orderData, data,
							jnjDeliveryScheduleDataList, jnjOrderEntryData, invoiceData);
				} else {
					entryRowNumber = setDataToExcelSheet(entryRowNumber, sheet, orderData, data,
							jnjOrderEntryData, invoiceData);
				}

			}
		}
		return entryRowNumber;
	}

	private Map<String, String> getInvoiceMaterialMap(final List<JnJInvoiceOrderData> invoiceModelList) {

		final Map<String, String> invoiceMaterialMap = new HashMap<>();
		if (null != invoiceModelList) {
			for (final JnJInvoiceOrderData jnjInvoiceOrderData : invoiceModelList) {
				final List<JnJInvoiceEntryData> jnjInvoiceEntryDataList = jnjInvoiceOrderData.getEntries();
				if (null != jnjInvoiceEntryDataList) {
					addInvoiceEntriesToMap(jnjInvoiceEntryDataList, invoiceMaterialMap, jnjInvoiceOrderData.getInvDocNo());
				}
			}
		}

		return invoiceMaterialMap;
	}
	
	private Map<String, String> getInvoiceLotNumberMap(final List<JnJInvoiceOrderData> invoiceModelList) {
		final Map<String, String> invoiceMaterialMap = new HashMap<>();
		if (null != invoiceModelList) {
			for (final JnJInvoiceOrderData jnjInvoiceOrderData : invoiceModelList) {
				final List<JnJInvoiceEntryData> jnjInvoiceEntryDataList = jnjInvoiceOrderData.getEntries();
				if (null != jnjInvoiceEntryDataList) {
					addInvoiceEntriesToMap(jnjInvoiceEntryDataList, invoiceMaterialMap, null);
				}
			}
		}

		return invoiceMaterialMap;
	}
	
	private void addInvoiceEntriesToMap(final List<JnJInvoiceEntryData> jnJInvoiceEntryModelList, final Map<String, String> invoiceMaterialMap, final String invoiceNumber){
		for (final JnJInvoiceEntryData jnjInvoiceEntryData : jnJInvoiceEntryModelList) {
			if (jnjInvoiceEntryData.getMaterial() != null) {
				if(invoiceNumber != null){
					invoiceMaterialMap.put(jnjInvoiceEntryData.getMaterial().getCode(), invoiceNumber);
				}else{
					invoiceMaterialMap.put(jnjInvoiceEntryData.getMaterial().getCode(),  jnjInvoiceEntryData.getLotNo());
				}
			}
		}
	}
	
	private int setDataToExcelSheet(int rowNum, final Sheet sheet, final JnjLaOrderData jnjOrderData,
			final JnjLaOrderHistoryData orderHistoryData,
			final List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList,
			final JnjLaOrderEntryData jnjOrderEntryData, final Map<String, Map<String, String>> invoiceData) {
	    	Map<String, String> invoiceProductMap = invoiceData.get(INVOICE_PRODUCT_MAP);
		    Map<String, String> invoiceLotNumberMap = invoiceData.get(INVOICE_LOT_NUMBER_MAP);
		    
			for (final JnjDeliveryScheduleData jnjDeliveryScheduleData : jnjDeliveryScheduleDataList) {
			SimpleDateFormat simpleDateFormat = null;
			if (null != getCommonI18NService().getCurrentLanguage() && StringUtils
					.equals(getCommonI18NService().getCurrentLanguage().getIsocode(), LANGUAGE_ENGLISH_ISO)) {
				simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				
			} else {
				simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
			}
			rowNum = rowNum + 1;
			final Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(StringUtils.stripStart(jnjOrderData.getSapOrderNumber(), CONSTANT_ZERO));
			row.createCell(1)
					.setCellValue((jnjOrderData.getPurchaseOrderNumber() == null
							|| StringUtils.isEmpty(jnjOrderData.getPurchaseOrderNumber())) ? jnjOrderData.getCode()
									: jnjOrderData.getPurchaseOrderNumber());
			row.createCell(2).setCellValue(getJnjLatamCommonFacadeUtil().getDocType(jnjOrderData.getOrderType()));
			row.createCell(3).setCellValue(jnjOrderData.getSoldToAccount());
			row.createCell(4).setCellValue(jnjOrderData.getB2bUnitName());
			if (null != getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress())) {
				row.createCell(5)
						.setCellValue(getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress()));

			} else {
				row.createCell(5).setCellValue(StringUtils.EMPTY);
			}
			row.createCell(6).setCellValue(orderHistoryData.getShipToNumber());

			row.createCell(7).setCellValue(
					orderHistoryData.getPlaced() != null ? simpleDateFormat.format(orderHistoryData.getPlaced()) : " ");

			row.createCell(8).setCellValue(getJnjLatamCommonFacadeUtil().getPoType(orderHistoryData.getChannel()));

			row.createCell(9).setCellValue(jnjOrderEntryData.getProduct().getCode());
			row.createCell(10).setCellValue(jnjOrderEntryData.getProduct().getName());
			row.createCell(11).setCellValue(jnjOrderEntryData.getMaterialNumber());
			row.createCell(12).setCellValue(((JnjLaProductData) jnjOrderEntryData.getProduct()).getSalesUnit());
			row.createCell(13).setCellValue(jnjOrderEntryData.getBasePrice().getFormattedValue());
			row.createCell(14)
					.setCellValue(StringUtils.stripStart(jnjOrderEntryData.getSapOrderlineNumber(), CONSTANT_ZERO));
			row.createCell(15)
					.setCellValue(StringUtils.stripStart(jnjDeliveryScheduleData.getLineNumber(), CONSTANT_ZERO));
			row.createCell(16).setCellValue(orderHistoryData.getStatusDisplay());

			extractedQuantityAndStatus(jnjOrderEntryData, jnjDeliveryScheduleData, row);
			row.createCell(19).setCellValue(getJnjLatamCommonFacadeUtil()
					.getMessageFromImpex("order.status." + jnjOrderEntryData.getStatus() + ".value"));
			row.createCell(20).setCellValue(jnjOrderData.getContractNumber());
			row.createCell(21).setCellValue(jnjOrderEntryData.getIndirectCustomer());
			extractedInvoiceMap(jnjOrderEntryData, invoiceProductMap, invoiceLotNumberMap, row);
			row.createCell(24).setCellValue(jnjOrderData.getB2bCustomerData().getName());
			row.createCell(25).setCellValue(jnjOrderData.getRequestedDeliveryDate() != null
					? simpleDateFormat.format(jnjOrderData.getRequestedDeliveryDate()) : " ");
		} // End of scheduleLineLoop
		return rowNum;
	}

	private static void extractedInvoiceMap(JnjLaOrderEntryData jnjOrderEntryData, Map<String, String> invoiceProductMap, Map<String, String> invoiceLotNumberMap, Row row) {
		if (null != invoiceLotNumberMap && !invoiceLotNumberMap.isEmpty()) {
			row.createCell(22).setCellValue(invoiceLotNumberMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
		if (null != invoiceProductMap && !invoiceProductMap.isEmpty()) {
			row.createCell(23).setCellValue(invoiceProductMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
	}

	private void extractedQuantityAndStatus(JnjLaOrderEntryData jnjOrderEntryData, JnjDeliveryScheduleData jnjDeliveryScheduleData, Row row) {
		if(null!= jnjDeliveryScheduleData.getQuantity())
		{
			row.createCell(17).setCellValue(String.valueOf(jnjDeliveryScheduleData.getQuantity()));
		}
		else
		{
			row.createCell(17).setCellValue(StringUtils.EMPTY);
		}
		if (JnJCommonUtil
				.getValues(EDD_EXCLUDED_STATUS,
						Jnjb2bFacadesConstants.COMMA_IN_STRING).stream()
				.noneMatch(jnjOrderEntryData.getStatus()::equalsIgnoreCase)) {
			populateDeliveryScheduleData(jnjDeliveryScheduleData, row);
		} else {
			row.createCell(18).setCellValue(jnjCommonFacadeUtil
					.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
		}
	}

	private void populateDeliveryScheduleData(JnjDeliveryScheduleData jnjDeliveryScheduleData, Row row) {
		final DateFormat dateFormat = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.DATE_FORMAT);
		try {
			populateDeliveryScheduleData(jnjDeliveryScheduleData, row, dateFormat);
		} catch (ParseException pe) {
			JnjGTCoreUtil.logErrorMessage("EXCEL SHEET",
					"setDataToExcelSheet()",
					"Error while writing Data.", pe,
					JnjLAOrderHistoryExcelView.class);
		}
	}

	private void populateDeliveryScheduleData(JnjDeliveryScheduleData jnjDeliveryScheduleData, Row row, DateFormat dateFormat) throws ParseException {
		String date1 = configurationService.getConfiguration()
				.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DATE);
		String date2 = configurationService.getConfiguration()
				.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DT);

		Date defaultDate1 = dateFormat.parse(date1);
		Date defaultDate2 = dateFormat.parse(date2);
		if (null != jnjDeliveryScheduleData
								.getDeliveryDate()) {
			populateDeliveryScheduleData(jnjDeliveryScheduleData, row, dateFormat, defaultDate1, defaultDate2);
		} else {
			row.createCell(18)
					.setCellValue(
							jnjCommonFacadeUtil
									.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
		}
	}

	private void populateDeliveryScheduleData(JnjDeliveryScheduleData jnjDeliveryScheduleData, Row row, DateFormat dateFormat, Date defaultDate1, Date defaultDate2) throws ParseException {
		Date deliveryDate = dateFormat
				.parse(dateFormat
						.format(jnjDeliveryScheduleData
								.getDeliveryDate()));
		if (defaultDate1.compareTo(deliveryDate) != 0 && defaultDate2.compareTo(deliveryDate) != 0) {
			row.createCell(18).setCellValue(dateFormat
					.format(jnjDeliveryScheduleData
							.getDeliveryDate()));

		} else {
			row.createCell(18)
			.setCellValue(
					jnjCommonFacadeUtil
							.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
		}
	}

	private int setDataToExcelSheet(int rowNum, final Sheet sheet, final JnjLaOrderData jnjOrderData,
			final JnjLaOrderHistoryData orderHistoryData, final JnjLaOrderEntryData jnjOrderEntryData,
			final Map<String, Map<String, String>> invoiceData) {
		Map<String, String> invoiceProductMap = invoiceData.get(INVOICE_PRODUCT_MAP);
		Map<String, String> invoiceLotNumberMap = invoiceData.get(INVOICE_LOT_NUMBER_MAP);
		SimpleDateFormat simpleDateFormat;
		if (null != getCommonI18NService().getCurrentLanguage()
				&& StringUtils.equals(getCommonI18NService().getCurrentLanguage().getIsocode(), LANGUAGE_ENGLISH_ISO)) {
			simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		} else {
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		}
		rowNum = rowNum + 1;
		final Row row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue(StringUtils.stripStart(jnjOrderData.getSapOrderNumber(), CONSTANT_ZERO));
		row.createCell(1)
				.setCellValue((jnjOrderData.getPurchaseOrderNumber() == null
						|| StringUtils.isEmpty(jnjOrderData.getPurchaseOrderNumber())) ? jnjOrderData.getCode()
								: jnjOrderData.getPurchaseOrderNumber());
		row.createCell(2).setCellValue(getJnjLatamCommonFacadeUtil().getDocType(jnjOrderData.getOrderType()));
		row.createCell(3).setCellValue(jnjOrderData.getSoldToAccount());
		row.createCell(4).setCellValue(jnjOrderData.getB2bUnitName());
		if (null != getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress())) {
			row.createCell(5).setCellValue(getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress()));
		} else {
			row.createCell(5).setCellValue(StringUtils.EMPTY);
		}
		row.createCell(6).setCellValue(orderHistoryData.getShipToNumber());
		row.createCell(7).setCellValue(
				orderHistoryData.getPlaced() != null ? simpleDateFormat.format(orderHistoryData.getPlaced()) : " ");

		row.createCell(8).setCellValue(getJnjLatamCommonFacadeUtil().getPoType(orderHistoryData.getChannel()));
		row.createCell(9).setCellValue(jnjOrderEntryData.getProduct().getCode());
		row.createCell(10).setCellValue(jnjOrderEntryData.getProduct().getName());
		row.createCell(11).setCellValue(jnjOrderEntryData.getMaterialNumber());
		row.createCell(12).setCellValue(((JnjLaProductData) jnjOrderEntryData.getProduct()).getSalesUnit());
		row.createCell(13).setCellValue(jnjOrderEntryData.getBasePrice().getFormattedValue());
		row.createCell(14)
				.setCellValue(StringUtils.stripStart(jnjOrderEntryData.getSapOrderlineNumber(), CONSTANT_ZERO));
		row.createCell(15).setCellValue(" ");
		row.createCell(16).setCellValue(orderHistoryData.getStatusDisplay());
		row.createCell(17).setCellValue(" ");
		row.createCell(18).setCellValue(jnjCommonFacadeUtil
				.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
		row.createCell(19).setCellValue(getJnjLatamCommonFacadeUtil()
				.getMessageFromImpex("order.status." + jnjOrderEntryData.getStatus() + ".value"));
		row.createCell(20).setCellValue(jnjOrderData.getContractNumber());
		row.createCell(21).setCellValue(jnjOrderEntryData.getIndirectCustomer());
		if (null != invoiceLotNumberMap && !invoiceLotNumberMap.isEmpty()) {
			row.createCell(22).setCellValue(invoiceLotNumberMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
		if (null != invoiceProductMap && !invoiceProductMap.isEmpty()) {
			row.createCell(23).setCellValue(invoiceProductMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
		row.createCell(24).setCellValue(jnjOrderData.getB2bCustomerData().getName());
		row.createCell(25).setCellValue(jnjOrderData.getRequestedDeliveryDate() != null
				? simpleDateFormat.format(jnjOrderData.getRequestedDeliveryDate()) : " ");
		return rowNum;
	}

	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final InputStream inputStream) {
		int index = 0;
		try {
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();
		} catch (final IOException ioException) {
			logger.error("Exception occured during input output operation in the method setHeaderImage()");
		}
		final CreationHelper helper = hssfWorkbook.getCreationHelper();
		final Drawing drawing = sheet.createDrawingPatriarch();
		final ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(0);
		anchor.setCol2(10);
		anchor.setRow1(1);
		anchor.setRow2(5);
		anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
		drawing.createPicture(anchor, index);

	}

	public JnjLatamOrderFacade getJnjlatamCustomOrderFacade() {
		return jnjlatamCustomOrderFacade;
	}

	public void setJnjlatamCustomOrderFacade(final JnjLatamOrderFacade jnjlatamCustomOrderFacade) {
		this.jnjlatamCustomOrderFacade = jnjlatamCustomOrderFacade;
	}

	public JnjLatamInvoiceFacade getJnjLatamInvoiceFacade() {
		return jnjLatamInvoiceFacade;
	}

	public void setJnjLatamInvoiceFacade(final JnjLatamInvoiceFacade jnjLatamInvoiceFacade) {
		this.jnjLatamInvoiceFacade = jnjLatamInvoiceFacade;
	}

}
