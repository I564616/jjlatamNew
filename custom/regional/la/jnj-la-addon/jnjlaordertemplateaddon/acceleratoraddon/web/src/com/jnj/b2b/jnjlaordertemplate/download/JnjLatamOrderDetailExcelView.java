/**
 *
 */
package com.jnj.b2b.jnjlaordertemplate.download;

import com.jnj.b2b.jnjglobalordertemplate.download.JnjGTOrderDetailExcelView;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.data.JnJInvoiceEntryData;
import com.jnj.facades.data.JnJInvoiceOrderData;
import com.jnj.facades.data.JnJLaInvoiceEntryData;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjLaOrderData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.invoice.JnjLatamInvoiceFacade;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.facades.util.impl.JnjLatamCommonFacadeUtilImpl;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import java.util.Date;
import java.text.ParseException;

import com.jnj.facade.util.JnjCommonFacadeUtil;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.servicelayer.config.ConfigurationService;



/**
 * Class responsible to create Excel view for Order detail.
 *
 * @author Accenture
 *
 */
public class JnjLatamOrderDetailExcelView extends JnjGTOrderDetailExcelView {
	private static final String SHEET_NAME = "Order Detail";
	private static final String CONSTANT_ZERO = "0";
	private static final String EDD_EXCLUDED_STATUS = "edd.excluded.status";
	private static final String ORDER_STATUS = "order.status.";
	private static final String VALUE = ".value";
	private static final String INVOICE_LOT_NUMBER_MAP = "invoiceLotNumberMap";
	private static final String INVOICE_PRODUCT_MAP = "invoiceProductMap";
	

	private JnjLatamCommonFacadeUtilImpl jnjLatamCommonFacadeUtil;
	private JnjLatamOrderFacade jnjlatamCustomOrderFacade;
	private JnjLatamInvoiceFacade jnjLatamInvoiceFacade;
	
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

	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1,
			final HttpServletRequest arg2, final HttpServletResponse arg3) throws Exception {
		arg3.setHeader("Content-Disposition", "attachment; filename=OrderDetails.xls");
		try {
			final JnjGTOrderData orderDetails = (JnjGTOrderData) arg0.get("orderData");
			final Sheet sheet = arg1.createSheet(SHEET_NAME);
			setHeaderImage(arg1, sheet, (InputStream) arg0.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY), 0);
			final Font font = arg1.createFont();
			font.setBold(true);
			final CellStyle style = arg1.createCellStyle();
			style.setFont(font);

			int rowNum = 6;

			final Row orderDetailHeader = sheet.createRow(rowNum++);

			orderDetailHeader.createCell(0).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.sapno"));
			orderDetailHeader.createCell(1).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.pono"));
			orderDetailHeader.createCell(2).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.doctype"));
			orderDetailHeader.createCell(3).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.soldtoaccno"));
			orderDetailHeader.createCell(4).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.soldtoaccname"));
			orderDetailHeader.createCell(5).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.shiptoaccno"));
			orderDetailHeader.createCell(6).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.payfrmaccno"));
			orderDetailHeader.createCell(7).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.socreationdate"));
			orderDetailHeader.createCell(8).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.potype"));
			orderDetailHeader.createCell(9).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.sapmatno"));
			orderDetailHeader.createCell(10).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.matno"));
			orderDetailHeader.createCell(11).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.custmatno"));
			orderDetailHeader.createCell(12).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.salesuom"));
			orderDetailHeader.createCell(13).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.qty"));
			orderDetailHeader.createCell(14).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.unitprice"));
			orderDetailHeader.createCell(15).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.saporderlineno"));
			orderDetailHeader.createCell(16).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.schlineno"));
			orderDetailHeader.createCell(17).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.overallstatus"));
			orderDetailHeader.createCell(18).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.confqty"));
			orderDetailHeader.createCell(19).setCellValue(getJnjLatamCommonFacadeUtil()
					.getMessageFromImpex("order.detail.download.header.estimateddelieverydate"));
			orderDetailHeader.createCell(20).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.orderstatus"));
			orderDetailHeader.createCell(21).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.contractref"));
			orderDetailHeader.createCell(22).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.indirectcustacc"));
			orderDetailHeader.createCell(23).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.lotno"));
			orderDetailHeader.createCell(24).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.invoiceno"));
			orderDetailHeader.createCell(25).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.createdby"));
			orderDetailHeader.createCell(26).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.reqdeliverydate"));
			orderDetailHeader.createCell(27).setCellValue(
					getJnjLatamCommonFacadeUtil().getMessageFromImpex("order.detail.download.header.orderconfdate"));

			// code for values of Order Line details
			if (null != orderDetails) {
				rowNum = rowNum++;
				List<OrderEntryData> orderEntryDataList = null;
				List<JnJInvoiceOrderData> orderInvoices = null;
				
				final JnjLaOrderData orderLaDetails = (JnjLaOrderData) orderDetails;
				orderEntryDataList = orderLaDetails.getEntries();
				extractedOrderEntryData(sheet, rowNum, orderEntryDataList, orderInvoices, orderLaDetails);
			}

		} catch (final Exception exception) {
			JnjGTCoreUtil.logErrorMessage("EXCEL SHEET", "buildExcelDocument()", "Error while writing Data.", exception,
					JnjLatamOrderDetailExcelView.class);
		}
	}

	private void extractedOrderEntryData(Sheet sheet, int rowNum, List<OrderEntryData> orderEntryDataList, List<JnJInvoiceOrderData> orderInvoices, JnjLaOrderData orderLaDetails) throws BusinessException {
		List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList;
		if (null != orderEntryDataList) {
			final String sapOrderNumber = orderLaDetails.getSapOrderNumber();

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
					rowNum = setDataToExcelSheet(rowNum, sheet, orderLaDetails, jnjDeliveryScheduleDataList,
							jnjOrderEntryData, invoiceData);
				} else {
					rowNum = setDataToExcelSheet(rowNum, sheet, orderLaDetails, jnjOrderEntryData,
							invoiceData);
				}
			}

		}
	}

	private Map<String, String> getInvoiceMaterialMap(final List<JnJInvoiceOrderData> invoiceModelList) {

		final Map<String, String> invoiceMaterialMap = new HashMap<>();
		if (null != invoiceModelList) {
			for (final JnJInvoiceOrderData jnJInvoiceOrderModel : invoiceModelList) {
				final List<JnJInvoiceEntryData> jnJInvoiceEntryModelList = jnJInvoiceOrderModel.getEntries();
				final String invoiceNumber = jnJInvoiceOrderModel.getInvDocNo();
				if (null != jnJInvoiceEntryModelList) {
					for (final JnJInvoiceEntryData jnJInvoiceEntrydata : jnJInvoiceEntryModelList) {
						addEntryToInvoiceMap(invoiceMaterialMap, jnJInvoiceEntrydata, invoiceNumber);
					}
				}
			}
		}
		return invoiceMaterialMap;
	}
	
	private void addEntryToInvoiceMap(final Map<String, String> invoiceMaterialMap, JnJInvoiceEntryData key, String value){
		if(key instanceof JnJLaInvoiceEntryData){
			JnjLaProductData jnjLaProductData = ((JnJLaInvoiceEntryData)key).getLamaterial();
			invoiceMaterialMap.put(jnjLaProductData.getCode(), value);
		}
	}

	private Map<String, String> getInvoiceLotNumberMap(final List<JnJInvoiceOrderData> invoiceModelList) {
		final Map<String, String> invoiceMaterialMap = new HashMap<>();
		if (null != invoiceModelList) {
			for (final JnJInvoiceOrderData jnjInvoiceOrderData : invoiceModelList) {
				final List<JnJInvoiceEntryData> jnJInvoiceEntryDataList = jnjInvoiceOrderData.getEntries();
				if (null != jnJInvoiceEntryDataList) {
					for (final JnJInvoiceEntryData jnJInvoiceEntryData : jnJInvoiceEntryDataList) {
						addEntryToInvoiceMap(invoiceMaterialMap, jnJInvoiceEntryData, jnJInvoiceEntryData.getLotNo());
					}
				}
			}
		}

		return invoiceMaterialMap;
	}
	
	private SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(Jnjlab2bcoreConstants.LA_DATE_FORMAT);
	}
	
	private int setDataToExcelSheet(int rowNum, final Sheet sheet, final JnjLaOrderData jnjOrderData,
			final List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList,
			final JnjLaOrderEntryData jnjOrderEntryData, final Map<String, Map<String, String>> invoiceData) {
		Map<String, String> invoiceProductMap = invoiceData.get(INVOICE_PRODUCT_MAP);
		Map<String, String> invoiceLotNumberMap = invoiceData.get(INVOICE_LOT_NUMBER_MAP);

		final SimpleDateFormat simpleDateFormat = getDateFormat();
		for (final JnjDeliveryScheduleData jnjDeliveryScheduleData : jnjDeliveryScheduleDataList) {
			String freeItemQuantity = StringUtils.EMPTY;
			if (jnjOrderEntryData instanceof JnjLaOrderEntryData) {
				freeItemQuantity = jnjOrderEntryData.getFreeItemsQuanity();
			}
			final Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(StringUtils.stripStart(jnjOrderData.getSapOrderNumber(), CONSTANT_ZERO));
			extractedOrderCode(jnjOrderData, row);
			row.createCell(2).setCellValue(getJnjLatamCommonFacadeUtil().getDocType(jnjOrderData.getOrderType()));

			row.createCell(3).setCellValue(jnjOrderData.getSoldToAccount());
			row.createCell(4).setCellValue(jnjOrderData.getB2bUnitName());

			extractedDeliveryAddress(jnjOrderData, row);
			row.createCell(6).setCellValue(jnjOrderData.getShipToNumber());

			row.createCell(7).setCellValue(jnjOrderData.getCreated());

			row.createCell(8).setCellValue(getJnjLatamCommonFacadeUtil().getPoType(jnjOrderData.getChannel()));

			row.createCell(9).setCellValue(jnjOrderEntryData.getProduct().getCode());
			row.createCell(10).setCellValue(jnjOrderEntryData.getProduct().getName());
			row.createCell(11).setCellValue(jnjOrderEntryData.getMaterialNumber());
			row.createCell(12).setCellValue(((JnjLaProductData) jnjOrderEntryData.getProduct()).getSalesUnit());
			row.createCell(13).setCellValue(jnjDeliveryScheduleData.getRoundedQuantity());
			row.createCell(14).setCellValue(jnjOrderEntryData.getBasePrice().getFormattedValue());
			row.createCell(15)
					.setCellValue(StringUtils.stripStart(jnjOrderEntryData.getSapOrderlineNumber(), CONSTANT_ZERO));
			row.createCell(16)
					.setCellValue(StringUtils.stripStart(jnjDeliveryScheduleData.getLineNumber(), CONSTANT_ZERO));
			row.createCell(17).setCellValue(getJnjLatamCommonFacadeUtil()
					.getMessageFromImpex(ORDER_STATUS + jnjOrderEntryData.getStatus() + VALUE));
			extractedQuantity(jnjDeliveryScheduleData, row);
			final Map<OrderEntryData, Boolean> showATPFlagMap= (Map<OrderEntryData, Boolean>)sessionService.getAttribute("showATPFlagMap");
			if(jnjOrderData.getPartialDelivFlag() && !jnjOrderData.getHoldCreditCardFlag() && MapUtils.getBooleanValue(showATPFlagMap, jnjOrderEntryData)
					&& (JnJCommonUtil.getValues(EDD_EXCLUDED_STATUS, Jnjb2bFacadesConstants.COMMA_IN_STRING).stream().noneMatch(jnjOrderEntryData.getStatus()::equalsIgnoreCase)))
			{
				 try {
					 populateDeliveryScheduleData(jnjDeliveryScheduleData, row);
				 } catch(ParseException pe) {
							JnjGTCoreUtil.logErrorMessage("EXCEL SHEET", "setDataToExcelSheet()", "Error while writing Data.", pe,
									JnjLatamOrderDetailExcelView.class);
						}

			 }
			else
			{
				row.createCell(19).setCellValue(jnjCommonFacadeUtil
						.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
			}
			row.createCell(20).setCellValue(jnjOrderData.getStatusDisplay());
			row.createCell(21).setCellValue(jnjOrderData.getContractNumber());
			row.createCell(22).setCellValue(jnjOrderEntryData.getIndirectCustomer());
			extractedInvoiceNumberAndProductMap(jnjOrderEntryData, invoiceProductMap, invoiceLotNumberMap, row);
			row.createCell(25).setCellValue(jnjOrderData.getB2bCustomerData().getName());
			row.createCell(26).setCellValue(jnjOrderData.getRequestedDeliveryDate() != null
					? simpleDateFormat.format(jnjOrderData.getRequestedDeliveryDate()) : " ");// Requested

			row.createCell(27).setCellValue(
					jnjOrderData.getCreated() != null ? simpleDateFormat.format(jnjOrderData.getCreated()) : " ");

			// Changes to Display Free Items Line in Excel.
						
			Map<String, String> rowQTYMap = new HashMap<>();
			rowQTYMap.put("rowNum", String.valueOf(rowNum));
			rowQTYMap.put("freeItemQuantity", freeItemQuantity);
			
			rowNum = getRowNumToFreeItemsDisplay(rowQTYMap, sheet, jnjOrderData, jnjOrderEntryData, invoiceData, jnjDeliveryScheduleData, row);
		} // End of scheduleLineLoop
		return rowNum;
	}

	private static void extractedOrderCode(JnjLaOrderData jnjOrderData, Row row) {
		row.createCell(1)
				.setCellValue((jnjOrderData.getPurchaseOrderNumber() == null
						|| StringUtils.isEmpty(jnjOrderData.getPurchaseOrderNumber())) ? jnjOrderData.getCode()
								: jnjOrderData.getPurchaseOrderNumber());
	}

	private int getRowNumToFreeItemsDisplay(Map<String, String> rowQTYMap, Sheet sheet, JnjLaOrderData jnjOrderData, JnjLaOrderEntryData jnjOrderEntryData,final Map<String, Map<String, String>> invoiceData, JnjDeliveryScheduleData jnjDeliveryScheduleData, Row row) {
		int rowNum = Integer.parseInt(rowQTYMap.get("rowNum"));
		String freeItemQuantity = rowQTYMap.get("freeItemQuantity");
		
		if (freeItemQuantity != null) {
			Map<String, String> invoiceProductMap = invoiceData.get(INVOICE_PRODUCT_MAP);
			Map<String, String> invoiceLotNumberMap = invoiceData.get(INVOICE_LOT_NUMBER_MAP);
			
			final SimpleDateFormat simpleDateFormat = getDateFormat();
			
			final Row rowFreeItem = sheet.createRow(rowNum++);
			rowFreeItem.createCell(0)
					.setCellValue(StringUtils.stripStart(jnjOrderData.getSapOrderNumber(), CONSTANT_ZERO));
			rowFreeItem.createCell(1)
					.setCellValue((jnjOrderData.getPurchaseOrderNumber() == null
							|| StringUtils.isEmpty(jnjOrderData.getPurchaseOrderNumber())) ? jnjOrderData.getCode()
									: jnjOrderData.getPurchaseOrderNumber());
			rowFreeItem.createCell(2)
					.setCellValue(getJnjLatamCommonFacadeUtil().getDocType(jnjOrderData.getOrderType()));

			rowFreeItem.createCell(3).setCellValue(jnjOrderData.getShipToNumber());
			row.createCell(4).setCellValue(jnjOrderData.getShipToName());

			if (null != getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress())) {
				rowFreeItem.createCell(5)
						.setCellValue(getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress()));
			} else {
				rowFreeItem.createCell(5).setCellValue(StringUtils.EMPTY);
			}

			rowFreeItem.createCell(6).setCellValue(jnjOrderData.getShipToNumber());
			rowFreeItem.createCell(7).setCellValue(jnjOrderData.getCreated());
			rowFreeItem.createCell(8)
					.setCellValue(getJnjLatamCommonFacadeUtil().getPoType(jnjOrderData.getChannel()));

			rowFreeItem.createCell(9).setCellValue(jnjOrderEntryData.getProduct().getCode());
			rowFreeItem.createCell(10).setCellValue(jnjOrderEntryData.getProduct().getName());
			rowFreeItem.createCell(11).setCellValue(jnjOrderEntryData.getMaterialNumber());
			rowFreeItem.createCell(12)
					.setCellValue(((JnjLaProductData) jnjOrderEntryData.getProduct()).getSalesUnit());
			rowFreeItem.createCell(13).setCellValue(StringUtils.EMPTY);
			rowFreeItem.createCell(14).setCellValue("Free Item");
			rowFreeItem.createCell(15)
					.setCellValue(StringUtils.stripStart(jnjOrderEntryData.getSapOrderlineNumber(), CONSTANT_ZERO));
			rowFreeItem.createCell(16)
					.setCellValue(StringUtils.stripStart(jnjDeliveryScheduleData.getLineNumber(), CONSTANT_ZERO));
			rowFreeItem.createCell(17).setCellValue(StringUtils.EMPTY);
			rowFreeItem.createCell(18).setCellValue(String.valueOf(jnjOrderEntryData.getFreeItemsQuanity()));

			extractedDeliveryDate(jnjDeliveryScheduleData, rowFreeItem);
			rowFreeItem.createCell(20).setCellValue(StringUtils.EMPTY);
			rowFreeItem.createCell(21).setCellValue(jnjOrderData.getContractNumber());
			rowFreeItem.createCell(22).setCellValue(jnjOrderEntryData.getIndirectCustomer());
			extractedLotNumberMap(jnjOrderEntryData, invoiceLotNumberMap, rowFreeItem);
			if (null != invoiceProductMap && !invoiceProductMap.isEmpty()) {
				rowFreeItem.createCell(24)
						.setCellValue(invoiceProductMap.get(jnjOrderEntryData.getProduct().getCode()));
			}
			rowFreeItem.createCell(25).setCellValue(jnjOrderData.getB2bCustomerData().getName());
			rowFreeItem.createCell(26).setCellValue(jnjOrderData.getRequestedDeliveryDate() != null
					? simpleDateFormat.format(jnjOrderData.getRequestedDeliveryDate()) : " ");
			rowFreeItem.createCell(27).setCellValue(
					jnjOrderData.getCreated() != null ? simpleDateFormat.format(jnjOrderData.getCreated()) : " ");

		}
		return rowNum;
	}

	private static void extractedLotNumberMap(JnjLaOrderEntryData jnjOrderEntryData, Map<String, String> invoiceLotNumberMap, Row rowFreeItem) {
		if (null != invoiceLotNumberMap && !invoiceLotNumberMap.isEmpty()) {
			rowFreeItem.createCell(23)
					.setCellValue(invoiceLotNumberMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
	}

	private void extractedDeliveryDate(JnjDeliveryScheduleData jnjDeliveryScheduleData, Row rowFreeItem) {
		if (null != jnjDeliveryScheduleData.getDeliveryDate()) {
			rowFreeItem.createCell(19).setCellValue(jnjDeliveryScheduleData.getDeliveryDate().toString());
		} else {
			rowFreeItem.createCell(19).setCellValue(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
		}
	}

	private static void extractedInvoiceNumberAndProductMap(JnjLaOrderEntryData jnjOrderEntryData, Map<String, String> invoiceProductMap, Map<String, String> invoiceLotNumberMap, Row row) {
		if (null != invoiceLotNumberMap && !invoiceLotNumberMap.isEmpty()) {
			row.createCell(23).setCellValue(invoiceLotNumberMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
		if (null != invoiceProductMap && !invoiceProductMap.isEmpty()) {
			row.createCell(24).setCellValue(invoiceProductMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
	}

	private static void extractedQuantity(JnjDeliveryScheduleData jnjDeliveryScheduleData, Row row) {
		if(null!= jnjDeliveryScheduleData.getQuantity())
		{
		row.createCell(18).setCellValue(String.valueOf(jnjDeliveryScheduleData.getQuantity()));
		}
		else
		{
			row.createCell(18).setCellValue(StringUtils.EMPTY);
		}
	}

	private void extractedDeliveryAddress(JnjLaOrderData jnjOrderData, Row row) {
		if (null != getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress())) {
			row.createCell(5)
					.setCellValue(getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress()));
		} else {
			row.createCell(5).setCellValue(StringUtils.EMPTY);
		}
	}

	private void populateDeliveryScheduleData(JnjDeliveryScheduleData jnjDeliveryScheduleData, Row row) throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat(Jnjlab2bcoreConstants.LA_DATE_FORMAT);

		String date1 = configurationService.getConfiguration()
				.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DATE);
		String date2 = configurationService.getConfiguration()
				.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DT);

		Date defaultDate1 = dateFormat.parse(date1);
		Date defaultDate2 = dateFormat.parse(date2);
		if (null != jnjDeliveryScheduleData.getDeliveryDate()) {
			populateDeliveryScheduleData(jnjDeliveryScheduleData, row, dateFormat, defaultDate1, defaultDate2);
		} else {
			row.createCell(19)
					.setCellValue(
							jnjCommonFacadeUtil
									.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
		}
	}

	private void populateDeliveryScheduleData(JnjDeliveryScheduleData jnjDeliveryScheduleData, Row row, DateFormat dateFormat, Date defaultDate1, Date defaultDate2) throws ParseException {
		Date deliveryDate = dateFormat.parse(dateFormat.format(jnjDeliveryScheduleData.getDeliveryDate()));
		if (defaultDate1.compareTo(deliveryDate) != 0 && defaultDate2.compareTo(deliveryDate) != 0) {
			row.createCell(19).setCellValue(dateFormat.format(jnjDeliveryScheduleData.getDeliveryDate()));
		} else {
			row.createCell(19)
			.setCellValue(
					jnjCommonFacadeUtil
							.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
			}
	}

	private int setDataToExcelSheet(int rowNum, final Sheet sheet, final JnjLaOrderData jnjOrderData,
			final JnjLaOrderEntryData jnjOrderEntryData, final Map<String, Map<String, String>> invoiceData) {
		Map<String, String> invoiceProductMap = invoiceData.get(INVOICE_PRODUCT_MAP);
		Map<String, String> invoiceLotNumberMap = invoiceData.get(INVOICE_LOT_NUMBER_MAP);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String freeItemQuantity = StringUtils.EMPTY;
		if (jnjOrderEntryData instanceof JnjLaOrderEntryData) {
			freeItemQuantity = jnjOrderEntryData.getFreeItemsQuanity();
		}

		final Row row = sheet.createRow(rowNum++);
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
			row.createCell(5).setCellValue(StringUtils.EMPTY);
		}
		row.createCell(6).setCellValue(jnjOrderData.getShipToNumber());

		row.createCell(7).setCellValue(
				jnjOrderData.getCreated() != null ? simpleDateFormat.format(jnjOrderData.getCreated()) : " ");
		row.createCell(8).setCellValue(getJnjLatamCommonFacadeUtil().getPoType(jnjOrderData.getChannel()));

		row.createCell(9).setCellValue(jnjOrderEntryData.getProduct().getCode());
		row.createCell(10).setCellValue(jnjOrderEntryData.getProduct().getName());
		row.createCell(11).setCellValue(jnjOrderEntryData.getMaterialNumber());
		row.createCell(12).setCellValue(((JnjLaProductData) jnjOrderEntryData.getProduct()).getSalesUnit());
		row.createCell(13).setCellValue(" ");
		row.createCell(14).setCellValue(jnjOrderEntryData.getBasePrice().getFormattedValue());
		row.createCell(15)
				.setCellValue(StringUtils.stripStart(jnjOrderEntryData.getSapOrderlineNumber(), CONSTANT_ZERO));

		row.createCell(16).setCellValue(" ");

		row.createCell(17).setCellValue(getJnjLatamCommonFacadeUtil()
				.getMessageFromImpex(ORDER_STATUS + jnjOrderEntryData.getStatus() + VALUE));

		row.createCell(18).setCellValue(" ");
		row.createCell(19).setCellValue(jnjCommonFacadeUtil
				.getMessageFromImpex(Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE));
		row.createCell(20).setCellValue(jnjOrderData.getStatusDisplay());
		row.createCell(21).setCellValue(jnjOrderData.getContractNumber());
		row.createCell(22).setCellValue(jnjOrderEntryData.getIndirectCustomer());
		if (null != invoiceLotNumberMap && !invoiceLotNumberMap.isEmpty()) {
			row.createCell(23).setCellValue(invoiceLotNumberMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
		if (null != invoiceProductMap && !invoiceProductMap.isEmpty()) {
			row.createCell(24).setCellValue(invoiceProductMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
		row.createCell(25).setCellValue(jnjOrderData.getB2bCustomerData().getName());
		row.createCell(26).setCellValue(jnjOrderData.getRequestedDeliveryDate() != null
				? simpleDateFormat.format(jnjOrderData.getRequestedDeliveryDate()) : " ");
		row.createCell(27).setCellValue(
				jnjOrderData.getCreated() != null ? simpleDateFormat.format(jnjOrderData.getCreated()) : " ");

		// Changes to Display Free Items Line in Excel.
		rowNum = getRowNumToDisplayFreeItems(rowNum, sheet, jnjOrderData, jnjOrderEntryData, invoiceData, freeItemQuantity, row);
		return rowNum;
	}

	private int getRowNumToDisplayFreeItems(int rowNum, Sheet sheet, JnjLaOrderData jnjOrderData, JnjLaOrderEntryData jnjOrderEntryData, Map<String, Map<String, String>> invoiceData, String freeItemQuantity, Row row) {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Map<String, String>	invoiceProductMap = invoiceData.get(INVOICE_PRODUCT_MAP);
		Map<String, String> invoiceLotNumberMap = invoiceData.get(INVOICE_LOT_NUMBER_MAP);
		
		if (freeItemQuantity != null) {
			final Row rowFreeItem = sheet.createRow(rowNum++);
			rowFreeItem.createCell(0)
					.setCellValue(StringUtils.stripStart(jnjOrderData.getSapOrderNumber(), CONSTANT_ZERO));
			rowFreeItem.createCell(1)
					.setCellValue((jnjOrderData.getPurchaseOrderNumber() == null
							|| StringUtils.isEmpty(jnjOrderData.getPurchaseOrderNumber())) ? jnjOrderData.getCode()
									: jnjOrderData.getPurchaseOrderNumber());
			rowFreeItem.createCell(2)
					.setCellValue(getJnjLatamCommonFacadeUtil().getDocType(jnjOrderData.getOrderType()));

			rowFreeItem.createCell(3).setCellValue(jnjOrderData.getShipToNumber());
			row.createCell(4).setCellValue(jnjOrderData.getShipToName());

			if (null != getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress())) {
				rowFreeItem.createCell(5)
						.setCellValue(getJnjLatamCommonFacadeUtil().getAddress(jnjOrderData.getDeliveryAddress()));
			} else {
				rowFreeItem.createCell(5).setCellValue(StringUtils.EMPTY);
			}
			rowFreeItem.createCell(6).setCellValue(jnjOrderData.getShipToNumber());

			rowFreeItem.createCell(7).setCellValue(
					jnjOrderData.getCreated() != null ? simpleDateFormat.format(jnjOrderData.getCreated()) : " ");

			rowFreeItem.createCell(8).setCellValue(getJnjLatamCommonFacadeUtil().getPoType(jnjOrderData.getChannel()));

			rowFreeItem.createCell(9).setCellValue(jnjOrderEntryData.getProduct().getCode());
			rowFreeItem.createCell(10).setCellValue(jnjOrderEntryData.getProduct().getName());
			rowFreeItem.createCell(11).setCellValue(jnjOrderEntryData.getMaterialNumber());
			rowFreeItem.createCell(12).setCellValue(((JnjLaProductData) jnjOrderEntryData.getProduct()).getSalesUnit());
			rowFreeItem.createCell(13).setCellValue(StringUtils.EMPTY);
			rowFreeItem.createCell(14).setCellValue("Free Item");

			rowFreeItem.createCell(15)
					.setCellValue(StringUtils.stripStart(jnjOrderEntryData.getSapOrderlineNumber(), CONSTANT_ZERO));

			rowFreeItem.createCell(16).setCellValue(StringUtils.EMPTY);

			rowFreeItem.createCell(17).setCellValue(StringUtils.EMPTY);
			rowFreeItem.createCell(18).setCellValue(StringUtils.EMPTY);
			rowFreeItem.createCell(19).setCellValue(StringUtils.EMPTY);
			rowFreeItem.createCell(20).setCellValue(StringUtils.EMPTY);
			rowFreeItem.createCell(21).setCellValue(jnjOrderData.getContractNumber());
			rowFreeItem.createCell(22).setCellValue(jnjOrderEntryData.getIndirectCustomer());
			extractedInvoiceNumberMap(jnjOrderEntryData, invoiceProductMap, invoiceLotNumberMap, rowFreeItem);
			rowFreeItem.createCell(25).setCellValue(jnjOrderData.getB2bCustomerData().getName());
			rowFreeItem.createCell(26).setCellValue(jnjOrderData.getRequestedDeliveryDate() != null
					? simpleDateFormat.format(jnjOrderData.getRequestedDeliveryDate()) : " ");
			rowFreeItem.createCell(27).setCellValue(
					jnjOrderData.getCreated() != null ? simpleDateFormat.format(jnjOrderData.getCreated()) : " ");

		}
		return rowNum;
	}

	private static void extractedInvoiceNumberMap(JnjLaOrderEntryData jnjOrderEntryData, Map<String, String> invoiceProductMap, Map<String, String> invoiceLotNumberMap, Row rowFreeItem) {
		if (null != invoiceLotNumberMap && !invoiceLotNumberMap.isEmpty()) {
			rowFreeItem.createCell(23)
					.setCellValue(invoiceLotNumberMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
		if (null != invoiceProductMap && !invoiceProductMap.isEmpty()) {
			rowFreeItem.createCell(24)
					.setCellValue(invoiceProductMap.get(jnjOrderEntryData.getProduct().getCode()));
		}
	}

	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final InputStream inputStream, final int colLen) {
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
}
