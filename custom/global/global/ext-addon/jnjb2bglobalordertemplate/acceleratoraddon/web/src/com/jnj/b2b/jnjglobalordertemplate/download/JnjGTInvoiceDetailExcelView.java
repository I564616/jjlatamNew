/**
 *
 */
package com.jnj.b2b.jnjglobalordertemplate.download;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;

import org.apache.commons.io.IOUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTInvoiceEntryData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTShippingTrckInfoData;
import com.jnj.facades.data.SurgeryInfoData;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTInvoiceOrderData;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * Class responsible to create Excel view for Invoice detail.
 *
 * @author Accenture
 *
 */
public class JnjGTInvoiceDetailExcelView extends AbstractXlsView
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
	
	private static final String INVOICE_DETAIL_PICTURE_RESIZING_FACTOR = "invoiceDetail.picture.resizeFactor";
	private static final String EMPTY = "";
	private static final String HYPHEN = "-";
	private static final String LINE_NUMBER_SUFFIX = "--00";
	private static final String ORDER_HEADER_INFO_VALUE_STYLE = "orderHeaderInfoValueStyle";
	private static final String ORDER_HEADER_INFO_KEY_STYLE = "orderHeaderInfoKeyStyle";
	private static final String STYLE4 = "style4";
	private static final String STYLE3 = "style3";
	private static final String CELL_STYLE_LEFT = "cellStyleLeft";
	private static final String CELL_STYLE_RIGHT = "cellStyleRight";
	private static final String CELL_STYLE = "cellStyle";
	private static final String STYLE = "style";
	private static final String DATE_FORMAT = "MM/dd/yyyy";
	private static final String SPACE = " ";
	private static final String SHEET_NAME = "Invoice Detail";
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	
	private static final String NOT_AVAILABLE = "orderdetail.invoice.notavailable";
	private static final String INVOICE_INFORMATION = "orderdetail.invoice.information";
	private static final String BILLING_DATE = "orderdetail.invoice.billingdate";
	private static final String SUMMARY_HEADER = "Invoice Details";
	private static final String LINE_HEADER = "orderdetail.invoice.lineheader";
	private static final String INVOICE_NUMBER = "orderdetail.invoice.number";
	private static final String SHIP_DATE = "orderdetail.order.shipDate";
	private static final String BILL_OF_LADING = "orderDetailPage.orderData.billOfLading";
	private static final String CARRIER_NAME = "orderdetail.invoice.carriername";
	private static final String PACKING_LIST = "invoiceDetailPage.packingList";
	private static final String SHIPPING_METHOD = "orderdetail.order.ShippingMethod";
	private static final String ITEM_PRICE = "orderdetail.order.itemprice";
	private static final String EXTENDED_PRICE = "orderdetail.order.ExtendedPrice";
	private static final String CONTRACT_NUMBER = "orderdetail.order.ContractNum";
	private static final String ORDERED_QUANTITY = "orderdetail.invoice.orderderedqty";
	private static final String INVOICED_QUANTITY = "orderdetail.invoice.invoicedqty";
	private static final String UPC = "orderdetail.invoice.upc";
	private static final String SHIP_TO_ADDRESS = "orderdetail.order.shipToaddress";
	private static final String TRACKING_COLON = "Tracking:";
	private static final String SHIPPING = "orderdetail.invoice.shipping";
	private static final String TRACKING = "orderdetail.invoice.tracking";
	private static final String INVOICE_TOTAL = "Invoice Total:";
	private static final String INVOICED_DISCLAIMER = "invoiced.disclaimer";
	private static final String INVOICED_DISCLAIMER_MSG = "invoiced.disclaimer.message";

	private static final String ORDER_NUMBER = "orderdetail.order.number";
	private static final String ACCOUNT_NUMBER = "orderdetail.order.accountnumber";
	private static final String ATTENTION = "orderdetail.order.attention";
	private static final String BILLING_NAME_ADDRESS = "orderhistory.soldto.address";
	private static final String DEALER_PO = "orderdetail.order.dealer.po";
	private static final String DROP_SHIP_ACC = "orderdetail.order.dropship.account";
	private static final String REQ_DELIVERY_DATE = "orderdetail.order.requesteddel.date";
	private static final String EXPECTED_DELIVERY_DATE = "orderdetail.order.expecteddel.date";
	private static final String ACTUAL_SHIP_DATE = "orderdetail.order.actualship.date";
	private static final String ACTUAL_DELIVERY_DATE = "orderdetail.order.actualdel.date";
	private static final String SHIPMENT_LOCATION = "orderdetail.order.Shipment.Location";
	private static final String LINE_STATUS = "orderdetail.order.linestatus";
	private static final String PRODUCT_NAME = "orderdetail.order.productname";
	private static final String PRODUCT_ID = "orderdetail.order.productId";
	private static final String CONS_PRODUCT_CODE = "orderdetail.order.productcode.upc";
	private static final String GTIN = "orderdetail.order.gtin";
	private static final String UNIT_OF_MEASURE = "orderdetail.order.unitmeasure";
	private static final String QUANTITY = "orderdetail.order.quantity";
	private static final String UNIT_PRICE = "orderdetail.order.UnitItemPrice";
	private static final String TOTAL_PRICE = "orderdetail.order.total";
	private static final String ORDER_CHANNEL = "order.channel.";
	private static final String SURGEON_NAME = "orderdetail.order.SurgeonName";
	private static final String DISTRIBUTOR_PO_NUMBER = "orderdetail.order.DistributorPONumber";
	private static final String SHIP_TO_ACCOUNT = "orderhistory.shipto.accountnumber";
	private static final String LOT_NUMBER = "orderdetail.order.LotNumber";
	private static final String LOT_COMMENT = "orderdetail.order.LotComment";
	private static final String SPECIAL_STOCK_PARTNER = "orderdetail.order.SpecialStockPartner";
	private static final String ESTIMATED_DELIVERY = "orderdetail.order.EstimatedDelDate";
	
	private static final String ORDER_INFORMATION = "orderdetail.order.OrderInformation";
	private static final String BILLING_INFORMATION = "orderdetail.order.BillingInformation";
	private static final String SHIPPING_INFORMATION = "orderdetail.order.ShippingInformation";
	private static final String JNJ_ORDER_NUMBER = "orderdetail.order.J&JOrderNumber";
	private static final String PO_NUMBER = "orderdetail.order.Purchaseorder";
	private static final String ORDER_DATE = "orderdetail.order.OrderDate";
	private static final String ORDER_STATUS = "orderdetail.order.Status :";
	private static final String ORDER_TYPE = "orderdetail.order.OrderType";
	private static final String ORDER_BY = "orderdetail.order.OrderedBy";
	private static final String NUMBER_OF_LINES = "orderdetail.order.numberoflines";
	private static final String SOLD_TO_ACC_NUMBER = "orderhistory.soldto.accountnumber";
	private static final String GLN = "orderdetail.order.gln";
	private static final String PAYMENT_METHOD = "orderdetail.order.Paymentmethod";
	private static final String CREDIT_CARD_TYPE = "orderdetail.order.CreditCardType";
	private static final String CREDIT_CARD_NUMBER = "orderdetail.order.CreditCardNumber";
	private static final String CREDIT_CARD_EXPIRY = "orderdetail.order.CreditCardExpirationDate";
	private static final String DELIVERY_INFORMATION = "orderdetail.order.DeliveryInformation";
	private static final String CARRIER = "orderdetail.order.Carrier";
	private static final String BILL_OF_LANDING = "orderdetail.order.BillofLading";
	private static final String LINE = "orderdetail.order.line";
	private static final String PRODUCT_CODE = "orderdetail.order.productcode.gtin";
	private static final String CONTRACT = "orderdetail.order.contract";
	private static final String QTY = "orderdetail.order.qty";
	private static final String UOM = "orderdetail.order.uom";
	private static final String STATUS = "orderdetail.order.status";
	private static final String TOTAL = "Total";
	private static final String SUB_TOTAL = "orderdetail.order.subtotal";
	private static final String FEES_FREIGHT = "orderdetail.order.feesfreight";
	private static final String TAXES = "orderdetail.order.taxes";
	private static final String ORDER_TOTAL = "orderdetail.order.ordertotal";
	private static final String SPINE_SALES_REP_UCN = "orderdetail.order.spinesales.ucn";
	private static final String SALES_TERRITORY = "orderdetail.order.sales.territory";
	private static final String SSP = "orderdetail.order.ssp";
	private static final String SHIPPING_DATE = "orderdetail.order.shipDate";
	private static final String DELIVERY_DATE = "orderdetail.order.deliveryDate";
	private static final String SURGERY_INFORMATION = "orderdetail.order.surgeryInfo";
	private static final String TOTAL_WEIGHT = "orderdetail.order.total.weight";
	private static final String TOTAL_VOLUME = "orderdetail.order.total.volume";

	private static final String SURGEON = "orderdetail.order.surgeon";
	private static final String CASE_DATE = "orderdetail.order.casedate";
	private static final String SURGERY_SPECIALITY = "orderdetail.order.surgery.speciality";
	private static final String LEVELS_INSTRUMENTED = "orderdetail.order.levels.instrumented";
	private static final String ORTHOBIOLOGICS = "orderdetail.order.orthobiologics";
	private static final String PATHOLOGY = "orderdetail.order.pathology";
	private static final String SURGICAL_APPROACH = "orderdetail.order.surgicalapproach";
	private static final String ZONE = "orderdetail.order.zone";
	private static final String INTERBODY = "orderdetail.order.interbody";
	private static final String FILE_CASE = "orderdetail.order.filecase";
	
	
	private static final Logger LOG = Logger.getLogger(JnjGTInvoiceDetailExcelView.class);
	
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{

		final JnjGTInvoiceOrderData invoiceData = (JnjGTInvoiceOrderData) arg0.get("orderInvoice");
		final JnjGTOrderData orderDetails = (JnjGTOrderData) arg0.get("orderData");
		final String currentSite = (String) arg0.get(Jnjb2bCoreConstants.SITE_NAME);
		final String headerImagePath = (String) arg0.get("siteLogoPath");
		final String filename = "Invoice " + invoiceData.getInvoiceNumber() + ".xls";
		arg3.setHeader("Content-Disposition", "attachment; filename=" + filename);
		final Sheet sheet = arg1.createSheet(SHEET_NAME);

		final Map<String, CellStyle> cellStylesMap = new HashMap<>();
		getCellStyles(arg1, cellStylesMap);


		//createDisclaimerRow(arg1, sheet, 8);
		final int startRowNumber = 8;

		if (orderDetails.getOrderType() == "ZOR")
		{
			createInvoiceDetailExcelForStandardOrder(arg1, sheet, cellStylesMap, orderDetails, invoiceData, startRowNumber);
		}
		else if (orderDetails.getOrderType() == "ZDEL")
		{
			createInvoiceDetailExcelForDeliveredOrder(arg1, sheet, cellStylesMap, orderDetails, invoiceData, startRowNumber);
		}
		else if (orderDetails.getOrderType() == "ZEX")
		{
			createInvoiceDetailExcelForInternationalOrder(arg1, sheet, cellStylesMap, orderDetails, invoiceData, startRowNumber);
		}
		else if (orderDetails.getOrderType() == "ZNC")
		{
			createInvoiceDetailExcelForNoChargeOrder(arg1, sheet, cellStylesMap, orderDetails, invoiceData, startRowNumber);
		}
		else if (orderDetails.getOrderType() == "ZKB")
		{
			createInvoiceDetailExcelForReplenishOrder(arg1, sheet, cellStylesMap, orderDetails, invoiceData, startRowNumber);
		}
		else if (orderDetails.getOrderType() == "ZRE")
		{
			createInvoiceDetailExcelForReturnOrder(arg1, sheet, cellStylesMap, orderDetails, invoiceData, startRowNumber);
		}
		else
		{
			createInvoiceDetailExcelForConsumerOrder(arg1, sheet, cellStylesMap, orderDetails, invoiceData, startRowNumber);
		}
		insertImage(arg1, sheet, headerImagePath);
	}
	
	protected void insertImage(final Workbook hssfWorkBook, final Sheet sheet, final String headerImagePath)
	{
		try
		{
			final InputStream inputStream = new FileInputStream(headerImagePath); // Get image file system path from the map (set in controller)
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			final int index = hssfWorkBook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();

			final CreationHelper helper = hssfWorkBook.getCreationHelper();
			final Drawing drawing = sheet.createDrawingPatriarch();

			final ClientAnchor anchor = helper.createClientAnchor();
			anchor.setCol1(2);
			anchor.setRow1(2);
			anchor.setCol2(11);
			anchor.setRow2(7);
			final HSSFPicture pict = (HSSFPicture) drawing.createPicture(anchor, index);
			pict.resize(Double.valueOf(Config.getParameter(INVOICE_DETAIL_PICTURE_RESIZING_FACTOR)));
		}
		catch (final FileNotFoundException fileNotFoundException)
		{
			LOG.error("File not found exception occured: " + Logging.HYPHEN + fileNotFoundException.getMessage());
		}
		catch (final IOException ioException)
		{
			LOG.error("Input output exception occured: " + Logging.HYPHEN + ioException.getMessage());
		}

	}

	protected void createInvoiceDetailExcelForStandardOrder(final Workbook hssfWorkbook, final Sheet sheet,
			final Map<String, CellStyle> cellStylesMap, final JnjGTOrderData orderDetails,
			final JnjGTInvoiceOrderData invoiceData, final int startRowNumber)
	{

		final int firstRow = startRowNumber;
		final String emptyField = new String();

		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		final CellStyle style = cellStylesMap.get(STYLE);
		final CellStyle cellStyle = cellStylesMap.get(CELL_STYLE);
		final CellStyle cellStyleRight = cellStylesMap.get(CELL_STYLE_RIGHT);
		final CellStyle cellStyleLeft = cellStylesMap.get(CELL_STYLE_LEFT);
		final CellStyle style3 = cellStylesMap.get(STYLE3);
		final CellStyle style4 = cellStylesMap.get(STYLE4);
		final CellStyle orderHeaderInfoKeyStyle = cellStylesMap.get(ORDER_HEADER_INFO_KEY_STYLE);
		final CellStyle orderHeaderInfoValueStyle = cellStylesMap.get(ORDER_HEADER_INFO_VALUE_STYLE);

		final Row orderInfoLabelRow = sheet.createRow(firstRow);
		orderInfoLabelRow.setHeight((short) 500);
		orderInfoLabelRow.createCell(2)
				.setCellValue(messageSource.getMessage(INVOICE_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(2).setCellStyle(style3);
		final CellRangeAddress reg1 = new CellRangeAddress(firstRow, firstRow, 2, 3);
		sheet.addMergedRegion(reg1);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg1, sheet);
		setBorder(reg1, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(4)
				.setCellValue(messageSource.getMessage(BILLING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(4).setCellStyle(style3);
		final CellRangeAddress reg2 = new CellRangeAddress(firstRow, firstRow, 4, 5);
		sheet.addMergedRegion(reg2);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg2, sheet);
		setBorder(reg2, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(6).setCellValue(
				messageSource.getMessage(SHIPPING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(6).setCellStyle(style3);
		final CellRangeAddress reg3 = new CellRangeAddress(firstRow, firstRow, 6, 7);
		sheet.addMergedRegion(reg3);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg3, sheet);
		setBorder(reg3, sheet, hssfWorkbook);

		//order info			
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 2,
				messageSource.getMessage(INVOICE_NUMBER,null, i18nService.getCurrentLocale()), invoiceData.getInvoiceNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 2,
				messageSource.getMessage(BILLING_DATE,null, i18nService.getCurrentLocale()),
				(invoiceData.getBillingdate() != null) ? sdf.format(invoiceData.getBillingdate())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 2,
				messageSource.getMessage(JNJ_ORDER_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 2,
				messageSource.getMessage(PO_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getPurchaseOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 2,
				messageSource.getMessage(ORDER_DATE,null, i18nService.getCurrentLocale()),
				(orderDetails.getCreated() != null) ? sdf.format(orderDetails.getCreated())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 6, 2,
				messageSource.getMessage(STATUS,null, i18nService.getCurrentLocale()), orderDetails.getStatusDisplay(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 7, 2,
				messageSource.getMessage(ORDER_TYPE,null, i18nService.getCurrentLocale()), "Standard", false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 8, 2,
				messageSource.getMessage(ORDER_BY,null, i18nService.getCurrentLocale()), orderDetails.getOrderedBy(), true);

		//order info

		//billing info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 4,
				messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getAccountNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 4,
				messageSource.getMessage(GLN,null, i18nService.getCurrentLocale()), orderDetails.getGln(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 4,
				messageSource.getMessage(BILLING_NAME_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getBillingAddress(), true), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 4,
				messageSource.getMessage(PAYMENT_METHOD,null, i18nService.getCurrentLocale()), "Purchase Order", true);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 11, 4,"Credit Card Type:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 12, 4,"Credit Card Number:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 13, 4,"Credit Card Expiration:", orderDetails.get);
		//billing info

		//shipping info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 6,
				messageSource.getMessage(PACKING_LIST,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getPackingListDetails().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 6,
				messageSource.getMessage(BILL_OF_LADING,null, i18nService.getCurrentLocale()), invoiceData.getBillOfLading(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 6,
				messageSource.getMessage(CARRIER_NAME,null, i18nService.getCurrentLocale()), invoiceData.getCarrier(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 6,
				messageSource.getMessage(TRACKING_COLON,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getTrackingIdList().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 6,
				messageSource.getMessage(SHIP_TO_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getDeliveryAddress(), false), false);

		//shipping info



		/// bordr
		final CellRangeAddress regNew = new CellRangeAddress(firstRow + 1, firstRow + 8, 2, 3);
		setBorder(regNew, sheet, hssfWorkbook);
		final CellRangeAddress regNew1 = new CellRangeAddress(firstRow + 1, firstRow + 8, 4, 5);
		setBorder(regNew1, sheet, hssfWorkbook);
		final CellRangeAddress regNew2 = new CellRangeAddress(firstRow + 1, firstRow + 8, 6, 7);
		setBorder(regNew2, sheet, hssfWorkbook);
		//border

		//Entry[START]
		final Row entryInfoHeaderRow = sheet.createRow(firstRow + 10);
		entryInfoHeaderRow.setHeight((short) 500);
		entryInfoHeaderRow.createCell(0).setCellValue(messageSource.getMessage(LINE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(0, 4000);
		entryInfoHeaderRow.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_NAME,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(1, 4000);
		entryInfoHeaderRow.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_CODE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(2, 5000);
		entryInfoHeaderRow.createCell(3).setCellValue(messageSource.getMessage(CONTRACT,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(3, 5000);
		entryInfoHeaderRow.createCell(4).setCellValue(messageSource.getMessage(QTY,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(4, 4000);
		entryInfoHeaderRow.createCell(5).setCellValue(messageSource.getMessage(UOM,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(5, 5000);
		entryInfoHeaderRow.createCell(6).setCellValue(messageSource.getMessage(SHIPPING,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(6, 5000);
		entryInfoHeaderRow.createCell(7).setCellValue(messageSource.getMessage(TRACKING,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(7, 5000);
		entryInfoHeaderRow.createCell(8).setCellValue(messageSource.getMessage(ITEM_PRICE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(8, 4000);
		entryInfoHeaderRow.createCell(9).setCellValue(messageSource.getMessage(TOTAL,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(9, 4000);
		entryInfoHeaderRow.getCell(0).setCellStyle(style);
		entryInfoHeaderRow.getCell(1).setCellStyle(style);
		entryInfoHeaderRow.getCell(2).setCellStyle(style);
		entryInfoHeaderRow.getCell(3).setCellStyle(style);
		entryInfoHeaderRow.getCell(4).setCellStyle(style);
		entryInfoHeaderRow.getCell(5).setCellStyle(style);
		entryInfoHeaderRow.getCell(6).setCellStyle(style);
		entryInfoHeaderRow.getCell(7).setCellStyle(style);
		entryInfoHeaderRow.getCell(8).setCellStyle(style);
		entryInfoHeaderRow.getCell(9).setCellStyle(style);
		entryInfoHeaderRow.getCell(0).setCellStyle(style);


		//style.setFont(null);
		int count = 1;
		int entryRowNum = firstRow + 11;
		for (final JnjGTInvoiceEntryData invoiceEntry : invoiceData.getInvoiceEntries())
		{

			final Row entryInfoValueRow = sheet.createRow(entryRowNum);
			entryInfoValueRow.createCell(0).setCellValue(String.valueOf(count) + LINE_NUMBER_SUFFIX);
			entryInfoValueRow.createCell(1).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getProduct().getName()) ? invoiceEntry.getProduct().getName() : emptyField);
			entryInfoValueRow.createCell(2).setCellValue(
					messageSource.getMessage(PRODUCT_ID,null, i18nService.getCurrentLocale())
							+ invoiceEntry.getProduct().getCode()
							+ "\n"
							+ messageSource.getMessage(GTIN,null, i18nService.getCurrentLocale())
							+ (StringUtils.isNotEmpty(invoiceEntry.getProduct().getGtin()) ? invoiceEntry.getProduct().getGtin()
									: emptyField));
			entryInfoValueRow.createCell(3).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getContractNum()) ? invoiceEntry.getContractNum() : emptyField);
			entryInfoValueRow.createCell(4).setCellValue(invoiceEntry.getQty());
			entryInfoValueRow.createCell(5).setCellValue(
					(StringUtils.isNotEmpty(invoiceEntry.getProduct().getDeliveryUnit())) ? invoiceEntry.getProduct()
							.getDeliveryUnit()
							+ " ("
							+ invoiceEntry.getProduct().getNumerator()
							+ SPACE
							+ invoiceEntry.getProduct().getSalesUnit() + ")" : emptyField);
			entryInfoValueRow.createCell(6).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getShippingMethod()) ? invoiceEntry.getShippingMethod() : emptyField);
			entryInfoValueRow.createCell(7).setCellValue(getTrackingInfoForInvoiceEntry(orderDetails, invoiceEntry.getLineNumber()));
			entryInfoValueRow.createCell(8).setCellValue(
					(invoiceEntry.getBasePrice() != null) ? invoiceEntry.getBasePrice().getFormattedValue() : emptyField);
			entryInfoValueRow.createCell(9).setCellValue(
					(invoiceEntry.getTotalPrice() != null) ? invoiceEntry.getTotalPrice().getFormattedValue() : emptyField);

			entryInfoValueRow.getCell(0).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(1).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(2).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(3).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(4).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(5).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(6).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(7).setCellStyle(cellStyle);

			//allign right			
			entryInfoValueRow.getCell(8).setCellStyle(cellStyleRight);
			entryInfoValueRow.getCell(9).setCellStyle(cellStyleRight);

			entryRowNum++;
			count++;
		}
		//Entry[END]
		createTotalForMDD(hssfWorkbook, sheet, invoiceData, emptyField, style4, entryRowNum, 0, 9);
	}

	protected void createInvoiceDetailExcelForDeliveredOrder(final Workbook hssfWorkbook, final Sheet sheet,
			final Map<String, CellStyle> cellStylesMap, final JnjGTOrderData orderDetails,
			final JnjGTInvoiceOrderData invoiceData, final int startRowNumber)
	{


		final int firstRow = startRowNumber;
		final String emptyField = new String();

		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		final CellStyle style = cellStylesMap.get(STYLE);
		final CellStyle cellStyle = cellStylesMap.get(CELL_STYLE);
		final CellStyle cellStyleRight = cellStylesMap.get(CELL_STYLE_RIGHT);
		final CellStyle cellStyleLeft = cellStylesMap.get(CELL_STYLE_LEFT);
		final CellStyle style3 = cellStylesMap.get(STYLE3);
		final CellStyle style4 = cellStylesMap.get(STYLE4);
		final CellStyle orderHeaderInfoKeyStyle = cellStylesMap.get(ORDER_HEADER_INFO_KEY_STYLE);
		final CellStyle orderHeaderInfoValueStyle = cellStylesMap.get(ORDER_HEADER_INFO_VALUE_STYLE);

		final Row orderInfoLabelRow = sheet.createRow(firstRow);
		orderInfoLabelRow.setHeight((short) 500);

		orderInfoLabelRow.createCell(1)
				.setCellValue(messageSource.getMessage(INVOICE_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(1).setCellStyle(style3);
		final CellRangeAddress reg1 = new CellRangeAddress(firstRow, firstRow, 1, 2);
		sheet.addMergedRegion(reg1);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg1, sheet);
		setBorder(reg1, sheet, hssfWorkbook);

		orderInfoLabelRow.createCell(3)
				.setCellValue(messageSource.getMessage(BILLING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(3).setCellStyle(style3);
		final CellRangeAddress reg2 = new CellRangeAddress(firstRow, firstRow, 3, 4);
		sheet.addMergedRegion(reg2);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg2, sheet);
		setBorder(reg2, sheet, hssfWorkbook);

		orderInfoLabelRow.createCell(5).setCellValue(
				messageSource.getMessage(SHIPPING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(5).setCellStyle(style3);
		final CellRangeAddress reg3 = new CellRangeAddress(firstRow, firstRow, 5, 6);
		sheet.addMergedRegion(reg3);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg3, sheet);
		setBorder(reg3, sheet, hssfWorkbook);

		orderInfoLabelRow.createCell(7)
				.setCellValue(messageSource.getMessage(SURGERY_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(7).setCellStyle(style3);
		final CellRangeAddress reg4 = new CellRangeAddress(firstRow, firstRow, 7, 8);
		sheet.addMergedRegion(reg4);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg4, sheet);
		setBorder(reg4, sheet, hssfWorkbook);


		//order info			
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 1,
				messageSource.getMessage(INVOICE_NUMBER,null, i18nService.getCurrentLocale()), invoiceData.getInvoiceNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 1,
				messageSource.getMessage(BILLING_DATE,null, i18nService.getCurrentLocale()),
				(invoiceData.getBillingdate() != null) ? sdf.format(invoiceData.getBillingdate())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 1,
				messageSource.getMessage(JNJ_ORDER_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 1,
				messageSource.getMessage(PO_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getPurchaseOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 1,
				messageSource.getMessage(ORDER_DATE,null, i18nService.getCurrentLocale()),
				(orderDetails.getCreated() != null) ? sdf.format(orderDetails.getCreated())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 6, 1,
				messageSource.getMessage(STATUS,null, i18nService.getCurrentLocale()), orderDetails.getStatusDisplay(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 7, 1,
				messageSource.getMessage(ORDER_TYPE,null, i18nService.getCurrentLocale()), "Delivered", false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 8, 1,
				messageSource.getMessage(ORDER_BY,null, i18nService.getCurrentLocale()), orderDetails.getOrderedBy(), true);
		//order info

		//billing info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 3,
				messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getAccountNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 3,
				messageSource.getMessage(GLN,null, i18nService.getCurrentLocale()), orderDetails.getGln(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 3,
				messageSource.getMessage(BILLING_NAME_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getBillingAddress(), true), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 3,
				messageSource.getMessage(PAYMENT_METHOD,null, i18nService.getCurrentLocale()), "Purchase Order", true);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 11, 4,"Credit Card Type:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 12, 4,"Credit Card Number:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 13, 4,"Credit Card Expiration:", orderDetails.get);
		//billing info

		//shipping info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 5,
				messageSource.getMessage(PACKING_LIST,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getPackingListDetails().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 5,
				messageSource.getMessage(BILL_OF_LADING,null, i18nService.getCurrentLocale()), invoiceData.getBillOfLading(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 5,
				messageSource.getMessage(CARRIER_NAME,null, i18nService.getCurrentLocale()), invoiceData.getCarrier(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 5,
				messageSource.getMessage(TRACKING_COLON,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getTrackingIdList().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 5,
				messageSource.getMessage(SHIP_TO_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getDeliveryAddress(), false), false);

		//surgery info
		final Map<String, String> surgeryDataMap = new HashMap<>();
		populateSurgeryData(orderDetails.getSurgeryInfo(), surgeryDataMap);

		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 7, "Surgeon:",
				orderDetails.getSurgeonName(), true);
		createCellForOrderHeaderInfo(
				sheet,
				orderHeaderInfoKeyStyle,
				orderHeaderInfoValueStyle,
				firstRow + 2,
				7,
				messageSource.getMessage(CASE_DATE,null, i18nService.getCurrentLocale()),
				((orderDetails.getSurgeryInfo() != null) ? ((orderDetails.getSurgeryInfo().getCaseDate() != null) ? sdf
						.format(orderDetails.getSurgeryInfo().getCaseDate())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale())) : emptyField), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 7,
				messageSource.getMessage(FILE_CASE,null, i18nService.getCurrentLocale()),
				((orderDetails.getSurgeryInfo() != null) ? ((orderDetails.getSurgeryInfo().getCaseNumber() != null) ? orderDetails
						.getSurgeryInfo().getCaseNumber() : messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()))
						: emptyField), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 7,
				messageSource.getMessage(SURGERY_SPECIALITY,null, i18nService.getCurrentLocale()),
				surgeryDataMap.get("surgeryInfoSurgerySpecialty"), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 7,
				messageSource.getMessage(LEVELS_INSTRUMENTED,null, i18nService.getCurrentLocale()),
				surgeryDataMap.get("surgeryInfoLevelsInstrumented"), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 6, 7,
				messageSource.getMessage(ORTHOBIOLOGICS,null, i18nService.getCurrentLocale()),
				surgeryDataMap.get("surgeryInfoOrthobiologics"), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 7, 7,
				messageSource.getMessage(PATHOLOGY,null, i18nService.getCurrentLocale()), surgeryDataMap.get("surgeryInfoPathology"),
				false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 8, 7,
				messageSource.getMessage(SURGICAL_APPROACH,null, i18nService.getCurrentLocale()),
				surgeryDataMap.get("surgeryInfoSurgicalApproach"), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 9, 7,
				messageSource.getMessage(ZONE,null, i18nService.getCurrentLocale()), surgeryDataMap.get("surgeryInfoZone"), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 10, 7,
				messageSource.getMessage(INTERBODY,null, i18nService.getCurrentLocale()), surgeryDataMap.get("surgeryInfoInterbody"),
				true);

		//surgery info

		/// border
		final CellRangeAddress regNew = new CellRangeAddress(firstRow + 1, firstRow + 10, 1, 2);
		setBorder(regNew, sheet, hssfWorkbook);
		final CellRangeAddress regNew1 = new CellRangeAddress(firstRow + 1, firstRow + 10, 3, 4);
		setBorder(regNew1, sheet, hssfWorkbook);
		final CellRangeAddress regNew2 = new CellRangeAddress(firstRow + 1, firstRow + 10, 5, 6);
		setBorder(regNew2, sheet, hssfWorkbook);
		final CellRangeAddress regNew3 = new CellRangeAddress(firstRow + 1, firstRow + 10, 7, 8);
		setBorder(regNew3, sheet, hssfWorkbook);
		//border

		//Entry[START]
		final Row entryInfoHeaderRow = sheet.createRow(firstRow + 12);
		entryInfoHeaderRow.setHeight((short) 500);
		entryInfoHeaderRow.createCell(0).setCellValue(messageSource.getMessage(LINE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(0, 4000);
		entryInfoHeaderRow.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_NAME,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(1, 5000);
		entryInfoHeaderRow.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_CODE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(2, 5000);
		entryInfoHeaderRow.createCell(3).setCellValue(messageSource.getMessage(CONTRACT,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(3, 5000);
		entryInfoHeaderRow.createCell(4).setCellValue(messageSource.getMessage(LOT_NUMBER,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(4, 5000);
		entryInfoHeaderRow.createCell(5).setCellValue(messageSource.getMessage(SSP,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(5, 5000);
		entryInfoHeaderRow.createCell(6).setCellValue(messageSource.getMessage(QTY,null, i18nService.getCurrentLocale()));

		entryInfoHeaderRow.createCell(7).setCellValue(messageSource.getMessage(UOM,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(7, 5000);

		entryInfoHeaderRow.createCell(8).setCellValue(messageSource.getMessage(ITEM_PRICE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(8, 5000);
		entryInfoHeaderRow.createCell(9).setCellValue(messageSource.getMessage(TOTAL,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(9, 4000);
		entryInfoHeaderRow.getCell(0).setCellStyle(style);
		entryInfoHeaderRow.getCell(1).setCellStyle(style);
		entryInfoHeaderRow.getCell(2).setCellStyle(style);
		entryInfoHeaderRow.getCell(3).setCellStyle(style);
		entryInfoHeaderRow.getCell(4).setCellStyle(style);
		entryInfoHeaderRow.getCell(5).setCellStyle(style);
		entryInfoHeaderRow.getCell(6).setCellStyle(style);
		entryInfoHeaderRow.getCell(7).setCellStyle(style);
		entryInfoHeaderRow.getCell(8).setCellStyle(style);
		entryInfoHeaderRow.getCell(9).setCellStyle(style);

		//style.setFont(null);
		int count = 1;
		int entryRowNum = firstRow + 13;
		for (final JnjGTInvoiceEntryData invoiceEntry : invoiceData.getInvoiceEntries())
		{

			final Row entryInfoValueRow = sheet.createRow(entryRowNum);
			entryInfoValueRow.createCell(0).setCellValue(String.valueOf(count) + LINE_NUMBER_SUFFIX);
			entryInfoValueRow.createCell(1).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getProduct().getName()) ? invoiceEntry.getProduct().getName() : emptyField);
			entryInfoValueRow.createCell(2).setCellValue(
					messageSource.getMessage(PRODUCT_ID,null, i18nService.getCurrentLocale())
							+ invoiceEntry.getProduct().getCode()
							+ "\n"
							+ messageSource.getMessage(GTIN,null, i18nService.getCurrentLocale())
							+ (StringUtils.isNotEmpty(invoiceEntry.getProduct().getGtin()) ? invoiceEntry.getProduct().getGtin()
									: emptyField));
			entryInfoValueRow.createCell(3).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getContractNum()) ? invoiceEntry.getContractNum() : emptyField);
			// Don't have lot number and ssp fields at invoice entry level
			entryInfoValueRow.createCell(4).setCellValue(emptyField);
			entryInfoValueRow.createCell(5).setCellValue(emptyField);

			entryInfoValueRow.createCell(6).setCellValue(invoiceEntry.getQty());
			entryInfoValueRow.createCell(7).setCellValue(
					(StringUtils.isNotEmpty(invoiceEntry.getProduct().getDeliveryUnit())) ? invoiceEntry.getProduct()
							.getDeliveryUnit()
							+ " ("
							+ invoiceEntry.getProduct().getNumerator()
							+ SPACE
							+ invoiceEntry.getProduct().getSalesUnit() + ")" : emptyField);

			entryInfoValueRow.createCell(8).setCellValue(
					(invoiceEntry.getBasePrice() != null) ? invoiceEntry.getBasePrice().getFormattedValue() : emptyField);
			entryInfoValueRow.createCell(9).setCellValue(
					(invoiceEntry.getTotalPrice() != null) ? invoiceEntry.getTotalPrice().getFormattedValue() : emptyField);

			entryInfoValueRow.getCell(0).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(1).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(2).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(3).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(4).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(5).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(6).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(7).setCellStyle(cellStyle);

			//allign right			
			entryInfoValueRow.getCell(8).setCellStyle(cellStyleRight);
			entryInfoValueRow.getCell(9).setCellStyle(cellStyleRight);

			entryRowNum++;
			count++;
		}
		//Entry[END]
		createTotalForMDD(hssfWorkbook, sheet, invoiceData, emptyField, style4, entryRowNum, 0, 9);
	}

	protected void createInvoiceDetailExcelForConsumerOrder(final Workbook hssfWorkbook, final Sheet sheet,
			final Map<String, CellStyle> cellStylesMap, final JnjGTOrderData orderDetails,
			final JnjGTInvoiceOrderData invoiceData, final int startRowNumber)
	{

		final int firstRow = startRowNumber;
		final String emptyField = new String();

		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final CellStyle style = cellStylesMap.get(STYLE);
		final CellStyle cellStyle = cellStylesMap.get(CELL_STYLE);
		final CellStyle cellStyleRight = cellStylesMap.get(CELL_STYLE_RIGHT);
		final CellStyle cellStyleLeft = cellStylesMap.get(CELL_STYLE_LEFT);
		final CellStyle style3 = cellStylesMap.get(STYLE3);
		final CellStyle style4 = cellStylesMap.get(STYLE4);
		final CellStyle orderHeaderInfoKeyStyle = cellStylesMap.get(ORDER_HEADER_INFO_KEY_STYLE);
		final CellStyle orderHeaderInfoValueStyle = cellStylesMap.get(ORDER_HEADER_INFO_VALUE_STYLE);

		final Row orderInfoLabelRow = sheet.createRow(firstRow);
		orderInfoLabelRow.setHeight((short) 500);

		orderInfoLabelRow.createCell(1).setCellValue(messageSource.getMessage(ORDER_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(1).setCellStyle(style3);
		final CellRangeAddress reg1 = new CellRangeAddress(firstRow, firstRow, 1, 2);
		sheet.addMergedRegion(reg1);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg1, sheet);
		setBorder(reg1, sheet, hssfWorkbook);

		orderInfoLabelRow.createCell(3)
				.setCellValue(messageSource.getMessage(BILLING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(3).setCellStyle(style3);
		final CellRangeAddress reg2 = new CellRangeAddress(firstRow, firstRow, 3, 4);
		sheet.addMergedRegion(reg2);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg2, sheet);
		setBorder(reg2, sheet, hssfWorkbook);

		orderInfoLabelRow.createCell(5).setCellValue(
				messageSource.getMessage(SHIPPING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(5).setCellStyle(style3);
		final CellRangeAddress reg3 = new CellRangeAddress(firstRow, firstRow, 5, 6);
		sheet.addMergedRegion(reg3);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg3, sheet);
		setBorder(reg3, sheet, hssfWorkbook);

		orderInfoLabelRow.createCell(7)
				.setCellValue(messageSource.getMessage(INVOICE_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(7).setCellStyle(style3);
		final CellRangeAddress reg4 = new CellRangeAddress(firstRow, firstRow, 7, 8);
		sheet.addMergedRegion(reg4);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg4, sheet);
		setBorder(reg4, sheet, hssfWorkbook);

		//order info			

		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 1,
				messageSource.getMessage(JNJ_ORDER_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 1,
				messageSource.getMessage(PO_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getPurchaseOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 1,
				messageSource.getMessage(ORDER_DATE,null, i18nService.getCurrentLocale()),
				(orderDetails.getCreated() != null) ? sdf.format(orderDetails.getCreated())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 1,
				messageSource.getMessage(STATUS,null, i18nService.getCurrentLocale()), orderDetails.getStatusDisplay(), false);

		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 1,
				messageSource.getMessage(ORDER_BY,null, i18nService.getCurrentLocale()), orderDetails.getOrderedBy(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 6, 1,
				"Sales Territory:", orderDetails.getSalesTerritory(), true);
		//order info

		//billing info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 3,
				messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getAccountNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 3,
				messageSource.getMessage(BILLING_NAME_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getBillingAddress(), true), false);
		//createCellForOrderHeaderInfo(sheet, OrderHeaderInfoStyle, firstRow + 3, 3, messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale())PAYMENT_METHOD, orderDetails.getPaymentType().getDisplayName(), true);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 11, 4,"Credit Card Type:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 12, 4,"Credit Card Number:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 13, 4,"Credit Card Expiration:", orderDetails.get);
		//billing info

		//shipping info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 5,
				messageSource.getMessage(PACKING_LIST,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getPackingListDetails().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 5,
				messageSource.getMessage(BILL_OF_LADING,null, i18nService.getCurrentLocale()), invoiceData.getBillOfLading(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 5,
				messageSource.getMessage(CARRIER_NAME,null, i18nService.getCurrentLocale()), invoiceData.getCarrier(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 5,
				messageSource.getMessage(TRACKING_COLON,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getTrackingIdList().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 5,
				messageSource.getMessage(SHIP_TO_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getDeliveryAddress(), false), false);

		//invoice info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 7,
				messageSource.getMessage(INVOICE_NUMBER,null, i18nService.getCurrentLocale()), invoiceData.getInvoiceNumber(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 7,
				messageSource.getMessage(BILLING_DATE,null, i18nService.getCurrentLocale()),
				(invoiceData.getBillingdate() != null) ? sdf.format(invoiceData.getBillingdate())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 7,
				messageSource.getMessage(TOTAL_WEIGHT,null, i18nService.getCurrentLocale()),
				String.valueOf(orderDetails.getOrderWeight())
						+ (StringUtils.isNotEmpty(orderDetails.getOrderWeightUOM()) ? orderDetails.getOrderWeightUOM() : " lbs"), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 7,
				messageSource.getMessage(TOTAL_VOLUME,null, i18nService.getCurrentLocale()),
				String.valueOf(orderDetails.getOrderVolume())
						+ (StringUtils.isNotEmpty(orderDetails.getOrderVolumeUOM()) ? orderDetails.getOrderVolumeUOM() : " ft\u00B3"),
				false);
		//invoice info

		/// bordr
		final CellRangeAddress regNew = new CellRangeAddress(firstRow + 1, firstRow + 7, 1, 2);
		setBorder(regNew, sheet, hssfWorkbook);
		final CellRangeAddress regNew1 = new CellRangeAddress(firstRow + 1, firstRow + 7, 3, 4);
		setBorder(regNew1, sheet, hssfWorkbook);
		final CellRangeAddress regNew2 = new CellRangeAddress(firstRow + 1, firstRow + 7, 5, 6);
		setBorder(regNew2, sheet, hssfWorkbook);
		final CellRangeAddress regNew3 = new CellRangeAddress(firstRow + 1, firstRow + 7, 7, 8);
		setBorder(regNew3, sheet, hssfWorkbook);
		//border

		//Entry[START]
		final Row entryInfoHeaderRow = sheet.createRow(firstRow + 9);
		entryInfoHeaderRow.setHeight((short) 500);
		entryInfoHeaderRow.createCell(1).setCellValue(messageSource.getMessage(LINE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(1, 5000);
		entryInfoHeaderRow.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_NAME,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(2, 5000);
		entryInfoHeaderRow.createCell(3).setCellValue("Product Code and UPC ");
		sheet.setColumnWidth(3, 5000);
		entryInfoHeaderRow.createCell(4).setCellValue(messageSource.getMessage(ORDERED_QUANTITY,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(4, 5000);

		entryInfoHeaderRow.createCell(5).setCellValue(messageSource.getMessage(INVOICED_QUANTITY,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(5, 5000);
		entryInfoHeaderRow.createCell(6).setCellValue(messageSource.getMessage(UOM,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(6, 5000);

		entryInfoHeaderRow.createCell(7).setCellValue(messageSource.getMessage(ITEM_PRICE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(7, 5000);
		entryInfoHeaderRow.createCell(8).setCellValue(messageSource.getMessage(TOTAL,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(8, 5000);

		entryInfoHeaderRow.getCell(1).setCellStyle(style);
		entryInfoHeaderRow.getCell(2).setCellStyle(style);
		entryInfoHeaderRow.getCell(3).setCellStyle(style);
		entryInfoHeaderRow.getCell(4).setCellStyle(style);
		entryInfoHeaderRow.getCell(5).setCellStyle(style);
		entryInfoHeaderRow.getCell(6).setCellStyle(style);
		entryInfoHeaderRow.getCell(7).setCellStyle(style);
		entryInfoHeaderRow.getCell(8).setCellStyle(style);
		//style.setFont(null);
		int count = 1;
		int entryRowNum = firstRow + 10;
		for (final JnjGTInvoiceEntryData invoiceEntry : invoiceData.getInvoiceEntries())
		{

			final Row entryInfoValueRow = sheet.createRow(entryRowNum);
			entryInfoValueRow.createCell(1).setCellValue(String.valueOf(count) + LINE_NUMBER_SUFFIX);
			String productName = new String();
			if (StringUtils.isNotEmpty(invoiceEntry.getProduct().getName()))
			{
				productName = invoiceEntry.getProduct().getName();
				if (StringUtils.containsIgnoreCase(invoiceEntry.getProduct().getName(), "<sup>"))
				{
					productName = productName.replaceAll("<sup>", "");
					if (StringUtils.containsIgnoreCase(invoiceEntry.getProduct().getName(), "</sup>"))
					{
						productName = productName.replaceAll("</sup>", "");
					}
				}

			}
			entryInfoValueRow.createCell(2).setCellValue(StringUtils.isNotEmpty(productName) ? productName : emptyField);
			entryInfoValueRow.createCell(3).setCellValue(
					messageSource.getMessage(PRODUCT_ID,null, i18nService.getCurrentLocale())
							+ invoiceEntry.getProduct().getCode()
							+ "\n"
							+ "UPC:"
							+ (StringUtils.isNotEmpty(invoiceEntry.getProduct().getUpc()) ? invoiceEntry.getProduct().getUpc()
									: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale())));
			entryInfoValueRow.createCell(4).setCellValue(null != invoiceEntry.getOrderedQty()?invoiceEntry.getOrderedQty(): 0.00);
			entryInfoValueRow.createCell(5).setCellValue(invoiceEntry.getQty());
			entryInfoValueRow.createCell(6).setCellValue(
					(StringUtils.isNotEmpty(invoiceEntry.getProduct().getDeliveryUnit())) ? invoiceEntry.getProduct()
							.getDeliveryUnit()
							+ " ("
							+ invoiceEntry.getProduct().getNumerator()
							+ SPACE
							+ invoiceEntry.getProduct().getSalesUnit() + ")" : emptyField);

			entryInfoValueRow.createCell(7).setCellValue(
					(invoiceEntry.getBasePrice() != null) ? invoiceEntry.getBasePrice().getFormattedValue() : emptyField);
			entryInfoValueRow.createCell(8).setCellValue(
					(invoiceEntry.getTotalPrice() != null) ? invoiceEntry.getTotalPrice().getFormattedValue() : emptyField);

			entryInfoValueRow.getCell(1).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(2).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(3).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(4).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(5).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(6).setCellStyle(cellStyle);
			//allign right			
			entryInfoValueRow.getCell(7).setCellStyle(cellStyleRight);
			entryInfoValueRow.getCell(8).setCellStyle(cellStyleRight);

			entryRowNum++;
			count++;
		}
		//Entry[END]

		//Totals[START]
		final Row subTotalRow = sheet.createRow(entryRowNum);
		final String subTotal = (invoiceData.getSubTotal() != null) ? invoiceData.getSubTotal().getFormattedValue() : emptyField;
		subTotalRow.createCell(1).setCellValue(messageSource.getMessage(SUB_TOTAL,null, i18nService.getCurrentLocale()) + subTotal);
		subTotalRow.getCell(1).setCellStyle(style4);
		final CellRangeAddress region = new CellRangeAddress(entryRowNum, entryRowNum, 1, 8);
		sheet.addMergedRegion(region);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);


		final Row adjRateAllowanceRow = sheet.createRow(entryRowNum + 1);
		final String adjRateAllowance = (invoiceData.getAdjustedRateAllowance() != null) ? invoiceData.getAdjustedRateAllowance()
				.getFormattedValue() : emptyField;
		adjRateAllowanceRow.createCell(1).setCellValue(" Adj Rate Allowance:: " + adjRateAllowance);
		adjRateAllowanceRow.getCell(1).setCellStyle(style4);
		final CellRangeAddress region1 = new CellRangeAddress(entryRowNum + 1, entryRowNum + 1, 1, 8);
		sheet.addMergedRegion(region1);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region1, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region1, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region1, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region1, sheet);

		final Row invoiceNetAmtRow = sheet.createRow(entryRowNum + 2);
		final String netValue = (invoiceData.getNetValue() != null) ? invoiceData.getNetValue().getFormattedValue() : emptyField;
		invoiceNetAmtRow.createCell(1).setCellValue("Invoice Net Amount:" + netValue);
		invoiceNetAmtRow.getCell(1).setCellStyle(style4);
		final CellRangeAddress region2 = new CellRangeAddress(entryRowNum + 2, entryRowNum + 2, 1, 8);
		sheet.addMergedRegion(region2);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region2, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region2, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region2, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region2, sheet);


		final Row taxesRow = sheet.createRow(entryRowNum + 3);
		final String taxes = (invoiceData.getTaxTotal() != null) ? invoiceData.getTaxTotal().getFormattedValue() : emptyField;
		taxesRow.createCell(1).setCellValue(messageSource.getMessage(TAXES,null, i18nService.getCurrentLocale()) + taxes);
		taxesRow.getCell(1).setCellStyle(style4);
		final CellRangeAddress region3 = new CellRangeAddress(entryRowNum + 3, entryRowNum + 3, 1, 8);
		sheet.addMergedRegion(region3);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region2, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region3, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region3, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region3, sheet);

		final Row invoiceTotalRow = sheet.createRow(entryRowNum + 4);
		final String invoiceTotal = (invoiceData.getInvoiceTotal() != null) ? invoiceData.getInvoiceTotal().getFormattedValue()
				: emptyField;
		invoiceTotalRow.createCell(1).setCellValue(
				messageSource.getMessage(INVOICE_TOTAL,null, i18nService.getCurrentLocale()) + invoiceTotal);
		final Font font = hssfWorkbook.createFont();
		font.setBold(true);
		style4.setFont(font);
		invoiceTotalRow.getCell(1).setCellStyle(style4);
		final CellRangeAddress region4 = new CellRangeAddress(entryRowNum + 4, entryRowNum + 4, 1, 8);
		sheet.addMergedRegion(region4);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region3, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region4, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region4, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region4, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region4, sheet);
		//Totals[END]

	}

	protected void createInvoiceDetailExcelForInternationalOrder(final Workbook hssfWorkbook, final Sheet sheet,
			final Map<String, CellStyle> cellStylesMap, final JnjGTOrderData orderDetails,
			final JnjGTInvoiceOrderData invoiceData, final int startRowNumber)
	{

		final int firstRow = startRowNumber;
		final String emptyField = new String();

		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		final CellStyle style = cellStylesMap.get(STYLE);
		final CellStyle cellStyle = cellStylesMap.get(CELL_STYLE);
		final CellStyle cellStyleRight = cellStylesMap.get(CELL_STYLE_RIGHT);
		final CellStyle cellStyleLeft = cellStylesMap.get(CELL_STYLE_LEFT);
		final CellStyle style3 = cellStylesMap.get(STYLE3);
		final CellStyle style4 = cellStylesMap.get(STYLE4);
		final CellStyle orderHeaderInfoKeyStyle = cellStylesMap.get(ORDER_HEADER_INFO_KEY_STYLE);
		final CellStyle orderHeaderInfoValueStyle = cellStylesMap.get(ORDER_HEADER_INFO_VALUE_STYLE);

		final Row orderInfoLabelRow = sheet.createRow(firstRow);
		orderInfoLabelRow.setHeight((short) 500);
		orderInfoLabelRow.createCell(2)
				.setCellValue(messageSource.getMessage(INVOICE_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(2).setCellStyle(style3);
		final CellRangeAddress reg1 = new CellRangeAddress(firstRow, firstRow, 2, 3);
		sheet.addMergedRegion(reg1);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg1, sheet);
		setBorder(reg1, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(4)
				.setCellValue(messageSource.getMessage(BILLING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(4).setCellStyle(style3);
		final CellRangeAddress reg2 = new CellRangeAddress(firstRow, firstRow, 4, 5);
		sheet.addMergedRegion(reg2);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg2, sheet);
		setBorder(reg2, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(6).setCellValue(
				messageSource.getMessage(SHIPPING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(6).setCellStyle(style3);
		final CellRangeAddress reg3 = new CellRangeAddress(firstRow, firstRow, 6, 7);
		sheet.addMergedRegion(reg3);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg3, sheet);
		setBorder(reg3, sheet, hssfWorkbook);

		//order info			
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 2,
				messageSource.getMessage(INVOICE_NUMBER,null, i18nService.getCurrentLocale()), invoiceData.getInvoiceNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 2,
				messageSource.getMessage(BILLING_DATE,null, i18nService.getCurrentLocale()),
				(invoiceData.getBillingdate() != null) ? sdf.format(invoiceData.getBillingdate())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 2,
				messageSource.getMessage(JNJ_ORDER_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 2,
				messageSource.getMessage(PO_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getPurchaseOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 2,
				messageSource.getMessage(ORDER_DATE,null, i18nService.getCurrentLocale()),
				(orderDetails.getCreated() != null) ? sdf.format(orderDetails.getCreated())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 6, 2,
				messageSource.getMessage(STATUS,null, i18nService.getCurrentLocale()), orderDetails.getStatusDisplay(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 7, 2,
				messageSource.getMessage(ORDER_TYPE,null, i18nService.getCurrentLocale()), "International", false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 8, 2,
				messageSource.getMessage(ORDER_BY,null, i18nService.getCurrentLocale()), orderDetails.getOrderedBy(), true);

		//order info

		//billing info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 4,
				messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getAccountNumber(), false);

		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 4,
				messageSource.getMessage(BILLING_NAME_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getBillingAddress(), true), false);
		//createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 4,messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale())PAYMENT_METHOD, orderDetails.getPaymentType().getDisplayName(), true);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 11, 4,"Credit Card Type:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 12, 4,"Credit Card Number:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 13, 4,"Credit Card Expiration:", orderDetails.get);
		//billing info

		//shipping info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 6,
				messageSource.getMessage(PACKING_LIST,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getPackingListDetails().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 6,
				messageSource.getMessage(BILL_OF_LADING,null, i18nService.getCurrentLocale()), invoiceData.getBillOfLading(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 6,
				messageSource.getMessage(CARRIER_NAME,null, i18nService.getCurrentLocale()), invoiceData.getCarrier(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 6,
				messageSource.getMessage(TRACKING_COLON,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getTrackingIdList().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 6,
				messageSource.getMessage(SHIP_TO_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getDeliveryAddress(), false), false);

		//shipping info



		/// bordr
		final CellRangeAddress regNew = new CellRangeAddress(firstRow + 1, firstRow + 8, 2, 3);
		setBorder(regNew, sheet, hssfWorkbook);
		final CellRangeAddress regNew1 = new CellRangeAddress(firstRow + 1, firstRow + 8, 4, 5);
		setBorder(regNew1, sheet, hssfWorkbook);
		final CellRangeAddress regNew2 = new CellRangeAddress(firstRow + 1, firstRow + 8, 6, 7);
		setBorder(regNew2, sheet, hssfWorkbook);
		//border

		//Entry[START]
		final Row entryInfoHeaderRow = sheet.createRow(firstRow + 10);
		entryInfoHeaderRow.setHeight((short) 500);
		entryInfoHeaderRow.createCell(0).setCellValue(messageSource.getMessage(LINE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(0, 4000);
		entryInfoHeaderRow.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_NAME,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(1, 4000);
		entryInfoHeaderRow.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_CODE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(2, 5000);
		entryInfoHeaderRow.createCell(3).setCellValue(messageSource.getMessage(CONTRACT,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(3, 5000);
		entryInfoHeaderRow.createCell(4).setCellValue(messageSource.getMessage(QTY,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(4, 4500);
		entryInfoHeaderRow.createCell(5).setCellValue(messageSource.getMessage(UOM,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(5, 5000);
		entryInfoHeaderRow.createCell(6).setCellValue(messageSource.getMessage(SHIPPING,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(6, 5000);
		entryInfoHeaderRow.createCell(7).setCellValue(messageSource.getMessage(TRACKING,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(7, 5000);
		entryInfoHeaderRow.createCell(8).setCellValue(messageSource.getMessage(ITEM_PRICE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(8, 4000);
		entryInfoHeaderRow.createCell(9).setCellValue(messageSource.getMessage(TOTAL,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(9, 4000);
		entryInfoHeaderRow.getCell(0).setCellStyle(style);
		entryInfoHeaderRow.getCell(1).setCellStyle(style);
		entryInfoHeaderRow.getCell(2).setCellStyle(style);
		entryInfoHeaderRow.getCell(3).setCellStyle(style);
		entryInfoHeaderRow.getCell(4).setCellStyle(style);
		entryInfoHeaderRow.getCell(5).setCellStyle(style);
		entryInfoHeaderRow.getCell(6).setCellStyle(style);
		entryInfoHeaderRow.getCell(7).setCellStyle(style);
		entryInfoHeaderRow.getCell(8).setCellStyle(style);
		entryInfoHeaderRow.getCell(9).setCellStyle(style);


		//style.setFont(null);
		int count = 1;
		int entryRowNum = firstRow + 11;
		for (final JnjGTInvoiceEntryData invoiceEntry : invoiceData.getInvoiceEntries())
		{

			final Row entryInfoValueRow = sheet.createRow(entryRowNum);
			entryInfoValueRow.createCell(0).setCellValue(String.valueOf(count) + LINE_NUMBER_SUFFIX);
			entryInfoValueRow.createCell(1).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getProduct().getName()) ? invoiceEntry.getProduct().getName() : emptyField);
			entryInfoValueRow.createCell(2).setCellValue(
					messageSource.getMessage(PRODUCT_ID,null, i18nService.getCurrentLocale())
							+ invoiceEntry.getProduct().getCode()
							+ "\n"
							+ messageSource.getMessage(GTIN,null, i18nService.getCurrentLocale())
							+ (StringUtils.isNotEmpty(invoiceEntry.getProduct().getGtin()) ? invoiceEntry.getProduct().getGtin()
									: emptyField));
			entryInfoValueRow.createCell(3).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getContractNum()) ? invoiceEntry.getContractNum() : emptyField);
			entryInfoValueRow.createCell(4).setCellValue(invoiceEntry.getQty());
			entryInfoValueRow.createCell(5).setCellValue(
					(StringUtils.isNotEmpty(invoiceEntry.getProduct().getDeliveryUnit())) ? invoiceEntry.getProduct()
							.getDeliveryUnit()
							+ " ("
							+ invoiceEntry.getProduct().getNumerator()
							+ SPACE
							+ invoiceEntry.getProduct().getSalesUnit() + ")" : emptyField);
			entryInfoValueRow.createCell(6).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getShippingMethod()) ? invoiceEntry.getShippingMethod() : emptyField);
			entryInfoValueRow.createCell(7).setCellValue(getTrackingInfoForInvoiceEntry(orderDetails, invoiceEntry.getLineNumber()));
			entryInfoValueRow.createCell(8).setCellValue(
					(invoiceEntry.getBasePrice() != null) ? invoiceEntry.getBasePrice().getFormattedValue() : emptyField);
			entryInfoValueRow.createCell(9).setCellValue(
					(invoiceEntry.getTotalPrice() != null) ? invoiceEntry.getTotalPrice().getFormattedValue() : emptyField);

			entryInfoValueRow.getCell(0).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(1).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(2).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(3).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(4).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(5).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(6).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(7).setCellStyle(cellStyle);

			//allign right			
			entryInfoValueRow.getCell(8).setCellStyle(cellStyleRight);
			entryInfoValueRow.getCell(9).setCellStyle(cellStyleRight);

			entryRowNum++;
			count++;
		}
		//Entry[END]

		createTotalForMDD(hssfWorkbook, sheet, invoiceData, emptyField, style4, entryRowNum, 0, 9);

	}

	protected void createInvoiceDetailExcelForNoChargeOrder(final Workbook hssfWorkbook, final Sheet sheet,
			final Map<String, CellStyle> cellStylesMap, final JnjGTOrderData orderDetails,
			final JnjGTInvoiceOrderData invoiceData, final int startRowNumber)
	{

		final int firstRow = startRowNumber;
		final String emptyField = new String();

		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		final CellStyle style = cellStylesMap.get(STYLE);
		final CellStyle cellStyle = cellStylesMap.get(CELL_STYLE);
		final CellStyle cellStyleRight = cellStylesMap.get(CELL_STYLE_RIGHT);
		final CellStyle cellStyleLeft = cellStylesMap.get(CELL_STYLE_LEFT);
		final CellStyle style3 = cellStylesMap.get(STYLE3);
		final CellStyle style4 = cellStylesMap.get(STYLE4);
		final CellStyle orderHeaderInfoKeyStyle = cellStylesMap.get(ORDER_HEADER_INFO_KEY_STYLE);
		final CellStyle orderHeaderInfoValueStyle = cellStylesMap.get(ORDER_HEADER_INFO_VALUE_STYLE);

		final Row orderInfoLabelRow = sheet.createRow(firstRow);
		orderInfoLabelRow.setHeight((short) 500);
		orderInfoLabelRow.createCell(1)
				.setCellValue(messageSource.getMessage(INVOICE_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(1).setCellStyle(style3);
		final CellRangeAddress reg1 = new CellRangeAddress(firstRow, firstRow, 1, 2);
		sheet.addMergedRegion(reg1);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg1, sheet);
		setBorder(reg1, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(3)
				.setCellValue(messageSource.getMessage(BILLING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(3).setCellStyle(style3);
		final CellRangeAddress reg2 = new CellRangeAddress(firstRow, firstRow, 3, 4);
		sheet.addMergedRegion(reg2);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg2, sheet);
		setBorder(reg2, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(5).setCellValue(
				messageSource.getMessage(SHIPPING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(5).setCellStyle(style3);
		final CellRangeAddress reg3 = new CellRangeAddress(firstRow, firstRow, 5, 6);
		sheet.addMergedRegion(reg3);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg3, sheet);
		setBorder(reg3, sheet, hssfWorkbook);

		//order info			
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 1,
				messageSource.getMessage(INVOICE_NUMBER,null, i18nService.getCurrentLocale()), invoiceData.getInvoiceNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 1,
				messageSource.getMessage(BILLING_DATE,null, i18nService.getCurrentLocale()),
				(invoiceData.getBillingdate() != null) ? sdf.format(invoiceData.getBillingdate())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 1,
				messageSource.getMessage(JNJ_ORDER_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 1,
				messageSource.getMessage(PO_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getPurchaseOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 1,
				messageSource.getMessage(ORDER_DATE,null, i18nService.getCurrentLocale()),
				(orderDetails.getCreated() != null) ? sdf.format(orderDetails.getCreated())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 6, 1,
				messageSource.getMessage(STATUS,null, i18nService.getCurrentLocale()), orderDetails.getStatusDisplay(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 7, 1,
				messageSource.getMessage(ORDER_TYPE,null, i18nService.getCurrentLocale()), "No Charge", false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 8, 1,
				messageSource.getMessage(ORDER_BY,null, i18nService.getCurrentLocale()), orderDetails.getOrderedBy(), true);

		//order info

		//billing info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 3,
				messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getAccountNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 3,
				messageSource.getMessage(GLN,null, i18nService.getCurrentLocale()), orderDetails.getGln(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 3,
				messageSource.getMessage(BILLING_NAME_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getBillingAddress(), true), false);
		//createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 4,messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale())PAYMENT_METHOD, orderDetails.getPaymentType().getDisplayName(), true);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 11, 4,"Credit Card Type:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 12, 4,"Credit Card Number:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 13, 4,"Credit Card Expiration:", orderDetails.get);
		//billing info

		//shipping info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 5,
				messageSource.getMessage(PACKING_LIST,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getPackingListDetails().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 5,
				messageSource.getMessage(BILL_OF_LADING,null, i18nService.getCurrentLocale()), invoiceData.getBillOfLading(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 5,
				messageSource.getMessage(CARRIER_NAME,null, i18nService.getCurrentLocale()), invoiceData.getCarrier(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 5,
				messageSource.getMessage(TRACKING_COLON,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getTrackingIdList().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 5,
				messageSource.getMessage(SHIP_TO_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getDeliveryAddress(), false), false);

		//shipping info



		/// bordr
		final CellRangeAddress regNew = new CellRangeAddress(firstRow + 1, firstRow + 8, 1, 2);
		setBorder(regNew, sheet, hssfWorkbook);
		final CellRangeAddress regNew1 = new CellRangeAddress(firstRow + 1, firstRow + 8, 3, 4);
		setBorder(regNew1, sheet, hssfWorkbook);
		final CellRangeAddress regNew2 = new CellRangeAddress(firstRow + 1, firstRow + 8, 5, 6);
		setBorder(regNew2, sheet, hssfWorkbook);
		//border

		//Entry[START]
		final Row entryInfoHeaderRow = sheet.createRow(firstRow + 10);
		entryInfoHeaderRow.setHeight((short) 500);
		entryInfoHeaderRow.createCell(0).setCellValue(messageSource.getMessage(LINE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(0, 4000);
		entryInfoHeaderRow.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_NAME,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(1, 5000);
		entryInfoHeaderRow.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_CODE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(2, 5000);
		entryInfoHeaderRow.createCell(3).setCellValue(messageSource.getMessage(QTY,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(3, 4500);
		entryInfoHeaderRow.createCell(4).setCellValue(messageSource.getMessage(UOM,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(4, 5000);
		entryInfoHeaderRow.createCell(5).setCellValue(messageSource.getMessage(SHIPPING,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(5, 5000);
		entryInfoHeaderRow.createCell(6).setCellValue(messageSource.getMessage(TRACKING,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(6, 5000);
		entryInfoHeaderRow.createCell(7).setCellValue(messageSource.getMessage(ITEM_PRICE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(7, 4500);
		entryInfoHeaderRow.createCell(8).setCellValue(messageSource.getMessage(TOTAL,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(8, 4500);
		entryInfoHeaderRow.getCell(0).setCellStyle(style);
		entryInfoHeaderRow.getCell(1).setCellStyle(style);
		entryInfoHeaderRow.getCell(2).setCellStyle(style);
		entryInfoHeaderRow.getCell(3).setCellStyle(style);
		entryInfoHeaderRow.getCell(4).setCellStyle(style);
		entryInfoHeaderRow.getCell(5).setCellStyle(style);
		entryInfoHeaderRow.getCell(6).setCellStyle(style);
		entryInfoHeaderRow.getCell(7).setCellStyle(style);
		entryInfoHeaderRow.getCell(8).setCellStyle(style);



		//style.setFont(null);
		int count = 1;
		int entryRowNum = firstRow + 11;
		for (final JnjGTInvoiceEntryData invoiceEntry : invoiceData.getInvoiceEntries())
		{

			final Row entryInfoValueRow = sheet.createRow(entryRowNum);
			entryInfoValueRow.createCell(0).setCellValue(String.valueOf(count) + LINE_NUMBER_SUFFIX);
			entryInfoValueRow.createCell(1).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getProduct().getName()) ? invoiceEntry.getProduct().getName() : emptyField);
			entryInfoValueRow.createCell(2).setCellValue(
					messageSource.getMessage(PRODUCT_ID,null, i18nService.getCurrentLocale())
							+ invoiceEntry.getProduct().getCode()
							+ "\n"
							+ messageSource.getMessage(GTIN,null, i18nService.getCurrentLocale())
							+ (StringUtils.isNotEmpty(invoiceEntry.getProduct().getGtin()) ? invoiceEntry.getProduct().getGtin()
									: emptyField));

			entryInfoValueRow.createCell(3).setCellValue(invoiceEntry.getQty());
			entryInfoValueRow.createCell(4).setCellValue(
					(StringUtils.isNotEmpty(invoiceEntry.getProduct().getDeliveryUnit())) ? invoiceEntry.getProduct()
							.getDeliveryUnit()
							+ " ("
							+ invoiceEntry.getProduct().getNumerator()
							+ SPACE
							+ invoiceEntry.getProduct().getSalesUnit() + ")" : emptyField);
			entryInfoValueRow.createCell(5).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getShippingMethod()) ? invoiceEntry.getShippingMethod() : emptyField);
			entryInfoValueRow.createCell(6).setCellValue(getTrackingInfoForInvoiceEntry(orderDetails, invoiceEntry.getLineNumber()));
			entryInfoValueRow.createCell(7).setCellValue(
					(invoiceEntry.getBasePrice() != null) ? invoiceEntry.getBasePrice().getFormattedValue() : emptyField);
			entryInfoValueRow.createCell(8).setCellValue(
					(invoiceEntry.getTotalPrice() != null) ? invoiceEntry.getTotalPrice().getFormattedValue() : emptyField);

			entryInfoValueRow.getCell(0).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(1).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(2).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(3).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(4).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(5).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(6).setCellStyle(cellStyle);

			//allign right			
			entryInfoValueRow.getCell(7).setCellStyle(cellStyleRight);
			entryInfoValueRow.getCell(8).setCellStyle(cellStyleRight);


			entryRowNum++;
			count++;
		}
		//Entry[END]
		createTotalForMDD(hssfWorkbook, sheet, invoiceData, emptyField, style4, entryRowNum, 0, 8);
	}

	protected void createInvoiceDetailExcelForReplenishOrder(final Workbook hssfWorkbook, final Sheet sheet,
			final Map<String, CellStyle> cellStylesMap, final JnjGTOrderData orderDetails,
			final JnjGTInvoiceOrderData invoiceData, final int startRowNumber)
	{

		final int firstRow = startRowNumber;
		final String emptyField = new String();

		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		final CellStyle style = cellStylesMap.get(STYLE);
		final CellStyle cellStyle = cellStylesMap.get(CELL_STYLE);
		final CellStyle cellStyleRight = cellStylesMap.get(CELL_STYLE_RIGHT);
		final CellStyle cellStyleLeft = cellStylesMap.get(CELL_STYLE_LEFT);
		final CellStyle style3 = cellStylesMap.get(STYLE3);
		final CellStyle style4 = cellStylesMap.get(STYLE4);
		final CellStyle orderHeaderInfoKeyStyle = cellStylesMap.get(ORDER_HEADER_INFO_KEY_STYLE);
		final CellStyle orderHeaderInfoValueStyle = cellStylesMap.get(ORDER_HEADER_INFO_VALUE_STYLE);

		final Row orderInfoLabelRow = sheet.createRow(firstRow);
		orderInfoLabelRow.setHeight((short) 500);
		orderInfoLabelRow.createCell(1)
				.setCellValue(messageSource.getMessage(INVOICE_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(1).setCellStyle(style3);
		final CellRangeAddress reg1 = new CellRangeAddress(firstRow, firstRow, 1, 2);
		sheet.addMergedRegion(reg1);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg1, sheet);
		setBorder(reg1, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(3)
				.setCellValue(messageSource.getMessage(BILLING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(3).setCellStyle(style3);
		final CellRangeAddress reg2 = new CellRangeAddress(firstRow, firstRow, 3, 4);
		sheet.addMergedRegion(reg2);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg2, sheet);
		setBorder(reg2, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(5).setCellValue(
				messageSource.getMessage(SHIPPING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(5).setCellStyle(style3);
		final CellRangeAddress reg3 = new CellRangeAddress(firstRow, firstRow, 5, 6);
		sheet.addMergedRegion(reg3);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg3, sheet);
		setBorder(reg3, sheet, hssfWorkbook);


		//order info			
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 1,
				messageSource.getMessage(INVOICE_NUMBER,null, i18nService.getCurrentLocale()), invoiceData.getInvoiceNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 1,
				messageSource.getMessage(BILLING_DATE,null, i18nService.getCurrentLocale()),
				(invoiceData.getBillingdate() != null) ? sdf.format(invoiceData.getBillingdate())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 1,
				messageSource.getMessage(JNJ_ORDER_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 1,
				messageSource.getMessage(PO_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getPurchaseOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 1,
				messageSource.getMessage(ORDER_DATE,null, i18nService.getCurrentLocale()),
				(orderDetails.getCreated() != null) ? sdf.format(orderDetails.getCreated())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 6, 1,
				messageSource.getMessage(STATUS,null, i18nService.getCurrentLocale()), orderDetails.getStatusDisplay(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 7, 1,
				messageSource.getMessage(ORDER_BY,null, i18nService.getCurrentLocale()), orderDetails.getOrderedBy(), true);

		//order info

		//billing info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 3,
				messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getAccountNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 3,
				messageSource.getMessage(BILLING_NAME_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getBillingAddress(), true), false);
		//createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 4,messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale())PAYMENT_METHOD, orderDetails.getPaymentType().getDisplayName(), true);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 11, 4,"Credit Card Type:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 12, 4,"Credit Card Number:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 13, 4,"Credit Card Expiration:", orderDetails.get);
		//billing info

		//shipping info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 5,
				messageSource.getMessage(PACKING_LIST,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getPackingListDetails().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 5,
				messageSource.getMessage(BILL_OF_LADING,null, i18nService.getCurrentLocale()), invoiceData.getBillOfLading(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 5,
				messageSource.getMessage(CARRIER_NAME,null, i18nService.getCurrentLocale()), invoiceData.getCarrier(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 5,
				messageSource.getMessage(TRACKING_COLON,null, i18nService.getCurrentLocale()), getFormattedList(orderDetails
						.getTrackingIdList().keySet()), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 5,
				messageSource.getMessage(SHIP_TO_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getDeliveryAddress(), false), false);

		//shipping info



		/// bordr
		final CellRangeAddress regNew = new CellRangeAddress(firstRow + 1, firstRow + 8, 1, 2);
		setBorder(regNew, sheet, hssfWorkbook);
		final CellRangeAddress regNew1 = new CellRangeAddress(firstRow + 1, firstRow + 8, 3, 4);
		setBorder(regNew1, sheet, hssfWorkbook);
		final CellRangeAddress regNew2 = new CellRangeAddress(firstRow + 1, firstRow + 8, 5, 6);
		setBorder(regNew2, sheet, hssfWorkbook);
		//border

		//Entry[START]
		final Row entryInfoHeaderRow = sheet.createRow(firstRow + 10);
		entryInfoHeaderRow.setHeight((short) 500);
		entryInfoHeaderRow.createCell(0).setCellValue(messageSource.getMessage(LINE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(0, 4000);
		entryInfoHeaderRow.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_NAME,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(1, 5000);
		entryInfoHeaderRow.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_CODE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(2, 5000);
		entryInfoHeaderRow.createCell(3).setCellValue(messageSource.getMessage(QTY,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(3, 4500);
		entryInfoHeaderRow.createCell(4).setCellValue(messageSource.getMessage(UOM,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(4, 5000);
		entryInfoHeaderRow.createCell(5).setCellValue(messageSource.getMessage(SHIPPING,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(5, 5000);
		entryInfoHeaderRow.createCell(6).setCellValue(messageSource.getMessage(TRACKING,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(6, 5000);
		entryInfoHeaderRow.createCell(7).setCellValue(messageSource.getMessage(ITEM_PRICE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(7, 4500);
		entryInfoHeaderRow.createCell(8).setCellValue(messageSource.getMessage(TOTAL,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(8, 4500);
		entryInfoHeaderRow.getCell(0).setCellStyle(style);
		entryInfoHeaderRow.getCell(1).setCellStyle(style);
		entryInfoHeaderRow.getCell(2).setCellStyle(style);
		entryInfoHeaderRow.getCell(3).setCellStyle(style);
		entryInfoHeaderRow.getCell(4).setCellStyle(style);
		entryInfoHeaderRow.getCell(5).setCellStyle(style);
		entryInfoHeaderRow.getCell(6).setCellStyle(style);
		entryInfoHeaderRow.getCell(7).setCellStyle(style);
		entryInfoHeaderRow.getCell(8).setCellStyle(style);



		//style.setFont(null);
		int count = 1;
		int entryRowNum = firstRow + 11;
		for (final JnjGTInvoiceEntryData invoiceEntry : invoiceData.getInvoiceEntries())
		{

			final Row entryInfoValueRow = sheet.createRow(entryRowNum);
			entryInfoValueRow.createCell(0).setCellValue(String.valueOf(count) + LINE_NUMBER_SUFFIX);
			entryInfoValueRow.createCell(1).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getProduct().getName()) ? invoiceEntry.getProduct().getName() : emptyField);
			entryInfoValueRow.createCell(2).setCellValue(
					messageSource.getMessage(PRODUCT_ID,null, i18nService.getCurrentLocale())
							+ invoiceEntry.getProduct().getCode()
							+ "\n"
							+ messageSource.getMessage(GTIN,null, i18nService.getCurrentLocale())
							+ (StringUtils.isNotEmpty(invoiceEntry.getProduct().getGtin()) ? invoiceEntry.getProduct().getGtin()
									: emptyField));

			entryInfoValueRow.createCell(3).setCellValue(invoiceEntry.getQty());
			entryInfoValueRow.createCell(4).setCellValue(
					(StringUtils.isNotEmpty(invoiceEntry.getProduct().getDeliveryUnit())) ? invoiceEntry.getProduct()
							.getDeliveryUnit()
							+ " ("
							+ invoiceEntry.getProduct().getNumerator()
							+ SPACE
							+ invoiceEntry.getProduct().getSalesUnit() + ")" : emptyField);
			entryInfoValueRow.createCell(5).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getShippingMethod()) ? invoiceEntry.getShippingMethod() : emptyField);
			entryInfoValueRow.createCell(6).setCellValue(getTrackingInfoForInvoiceEntry(orderDetails, invoiceEntry.getLineNumber()));
			entryInfoValueRow.createCell(7).setCellValue(
					(invoiceEntry.getBasePrice() != null) ? invoiceEntry.getBasePrice().getFormattedValue() : emptyField);
			entryInfoValueRow.createCell(8).setCellValue(
					(invoiceEntry.getTotalPrice() != null) ? invoiceEntry.getTotalPrice().getFormattedValue() : emptyField);

			entryInfoValueRow.getCell(0).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(1).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(2).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(3).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(4).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(5).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(6).setCellStyle(cellStyle);

			//allign right			
			entryInfoValueRow.getCell(7).setCellStyle(cellStyleRight);
			entryInfoValueRow.getCell(8).setCellStyle(cellStyleRight);

			entryRowNum++;
			count++;
		}
		//Entry[END]
		createTotalForMDD(hssfWorkbook, sheet, invoiceData, emptyField, style4, entryRowNum, 0, 8);
	}

	protected void createInvoiceDetailExcelForReturnOrder(final Workbook hssfWorkbook, final Sheet sheet,
			final Map<String, CellStyle> cellStylesMap, final JnjGTOrderData orderDetails,
			final JnjGTInvoiceOrderData invoiceData, final int startRowNumber)
	{

		final int firstRow = startRowNumber;
		final String emptyField = new String();

		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final CellStyle style = cellStylesMap.get(STYLE);
		final CellStyle cellStyle = cellStylesMap.get(CELL_STYLE);
		final CellStyle cellStyleRight = cellStylesMap.get(CELL_STYLE_RIGHT);
		final CellStyle cellStyleLeft = cellStylesMap.get(CELL_STYLE_LEFT);
		final CellStyle style3 = cellStylesMap.get(STYLE3);
		final CellStyle style4 = cellStylesMap.get(STYLE4);
		final CellStyle orderHeaderInfoKeyStyle = cellStylesMap.get(ORDER_HEADER_INFO_KEY_STYLE);
		final CellStyle orderHeaderInfoValueStyle = cellStylesMap.get(ORDER_HEADER_INFO_VALUE_STYLE);

		final Row orderInfoLabelRow = sheet.createRow(firstRow);
		orderInfoLabelRow.setHeight((short) 500);
		orderInfoLabelRow.createCell(1)
				.setCellValue(messageSource.getMessage(INVOICE_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(1).setCellStyle(style3);
		final CellRangeAddress reg1 = new CellRangeAddress(firstRow, firstRow, 1, 2);
		sheet.addMergedRegion(reg1);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg1, sheet);
		setBorder(reg1, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(3)
				.setCellValue(messageSource.getMessage(BILLING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(3).setCellStyle(style3);
		final CellRangeAddress reg2 = new CellRangeAddress(firstRow, firstRow, 3, 4);
		sheet.addMergedRegion(reg2);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg2, sheet);
		setBorder(reg2, sheet, hssfWorkbook);
		orderInfoLabelRow.createCell(5).setCellValue(
				messageSource.getMessage(SHIPPING_INFORMATION,null, i18nService.getCurrentLocale()));
		orderInfoLabelRow.getCell(5).setCellStyle(style3);
		final CellRangeAddress reg3 = new CellRangeAddress(firstRow, firstRow, 5, 6);
		sheet.addMergedRegion(reg3);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg3, sheet);
		setBorder(reg3, sheet, hssfWorkbook);

		//order info			
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 1,
				messageSource.getMessage(INVOICE_NUMBER,null, i18nService.getCurrentLocale()), invoiceData.getInvoiceNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 1,
				messageSource.getMessage(BILLING_DATE,null, i18nService.getCurrentLocale()),
				(invoiceData.getBillingdate() != null) ? sdf.format(invoiceData.getBillingdate())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 1,
				messageSource.getMessage(JNJ_ORDER_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 4, 1,
				messageSource.getMessage(PO_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getPurchaseOrderNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 5, 1,
				messageSource.getMessage(ORDER_DATE,null, i18nService.getCurrentLocale()),
				(orderDetails.getCreated() != null) ? sdf.format(orderDetails.getCreated())
						: messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 6, 1,
				messageSource.getMessage(STATUS,null, i18nService.getCurrentLocale()), orderDetails.getStatusDisplay(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 7, 1,
				messageSource.getMessage(ORDER_TYPE,null, i18nService.getCurrentLocale()), "Return", true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 8, 1,
				messageSource.getMessage(ORDER_BY,null, i18nService.getCurrentLocale()), orderDetails.getOrderedBy(), true);

		//order info

		//billing info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 3,
				messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()), orderDetails.getAccountNumber(), false);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 2, 3,
				messageSource.getMessage(GLN,null, i18nService.getCurrentLocale()), orderDetails.getGln(), true);
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 3,
				messageSource.getMessage(BILLING_NAME_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getBillingAddress(), true), false);
		//createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 3, 4,messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale())PAYMENT_METHOD, orderDetails.getPaymentType().getDisplayName(), true);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 11, 4,"Credit Card Type:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 12, 4,"Credit Card Number:", orderDetails.get);
		//createCellForOrderHeaderInfo(sheet, cellStyle,font, 13, 4,"Credit Card Expiration:", orderDetails.get);
		//billing info

		//shipping info
		createCellForOrderHeaderInfo(sheet, orderHeaderInfoKeyStyle, orderHeaderInfoValueStyle, firstRow + 1, 5,
				messageSource.getMessage(SHIP_TO_ADDRESS,null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getDeliveryAddress(), false), false);

		//shipping info



		/// bordr
		final CellRangeAddress regNew = new CellRangeAddress(firstRow + 1, firstRow + 8, 1, 2);
		setBorder(regNew, sheet, hssfWorkbook);
		final CellRangeAddress regNew1 = new CellRangeAddress(firstRow + 1, firstRow + 8, 3, 4);
		setBorder(regNew1, sheet, hssfWorkbook);
		final CellRangeAddress regNew2 = new CellRangeAddress(firstRow + 1, firstRow + 8, 5, 6);
		setBorder(regNew2, sheet, hssfWorkbook);
		//border

		//Entry[START]
		final Row entryInfoHeaderRow = sheet.createRow(firstRow + 10);
		entryInfoHeaderRow.setHeight((short) 500);
		entryInfoHeaderRow.createCell(1).setCellValue(messageSource.getMessage(LINE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(1, 5000);
		entryInfoHeaderRow.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_NAME,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(2, 5000);
		entryInfoHeaderRow.createCell(3).setCellValue(messageSource.getMessage(PRODUCT_CODE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(3, 5000);
		entryInfoHeaderRow.createCell(4).setCellValue(messageSource.getMessage(QTY,null, i18nService.getCurrentLocale()));
		entryInfoHeaderRow.createCell(5).setCellValue(messageSource.getMessage(UOM,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(5, 5000);
		entryInfoHeaderRow.createCell(6).setCellValue(messageSource.getMessage(ITEM_PRICE,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(6, 5000);
		entryInfoHeaderRow.createCell(7).setCellValue(messageSource.getMessage(TOTAL,null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(7, 5000);
		entryInfoHeaderRow.getCell(1).setCellStyle(style);
		entryInfoHeaderRow.getCell(2).setCellStyle(style);
		entryInfoHeaderRow.getCell(3).setCellStyle(style);
		entryInfoHeaderRow.getCell(4).setCellStyle(style);
		entryInfoHeaderRow.getCell(5).setCellStyle(style);
		entryInfoHeaderRow.getCell(6).setCellStyle(style);
		entryInfoHeaderRow.getCell(7).setCellStyle(style);


		//style.setFont(null);
		int count = 1;
		int entryRowNum = firstRow + 11;
		for (final JnjGTInvoiceEntryData invoiceEntry : invoiceData.getInvoiceEntries())
		{

			final Row entryInfoValueRow = sheet.createRow(entryRowNum);
			entryInfoValueRow.createCell(1).setCellValue(String.valueOf(count) + LINE_NUMBER_SUFFIX);
			entryInfoValueRow.createCell(2).setCellValue(
					StringUtils.isNotEmpty(invoiceEntry.getProduct().getName()) ? invoiceEntry.getProduct().getName() : emptyField);
			entryInfoValueRow.createCell(3).setCellValue(
					messageSource.getMessage(PRODUCT_ID,null, i18nService.getCurrentLocale())
							+ invoiceEntry.getProduct().getCode()
							+ "\n"
							+ messageSource.getMessage(GTIN,null, i18nService.getCurrentLocale())
							+ (StringUtils.isNotEmpty(invoiceEntry.getProduct().getGtin()) ? invoiceEntry.getProduct().getGtin()
									: emptyField));

			entryInfoValueRow.createCell(4).setCellValue(invoiceEntry.getQty());
			entryInfoValueRow.createCell(5).setCellValue(
					(StringUtils.isNotEmpty(invoiceEntry.getProduct().getDeliveryUnit())) ? invoiceEntry.getProduct()
							.getDeliveryUnit()
							+ " ("
							+ invoiceEntry.getProduct().getNumerator()
							+ SPACE
							+ invoiceEntry.getProduct().getSalesUnit() + ")" : emptyField);

			entryInfoValueRow.createCell(6).setCellValue(
					(invoiceEntry.getBasePrice() != null) ? invoiceEntry.getBasePrice().getFormattedValue() : emptyField);
			entryInfoValueRow.createCell(7).setCellValue(
					(invoiceEntry.getTotalPrice() != null) ? invoiceEntry.getTotalPrice().getFormattedValue() : emptyField);

			entryInfoValueRow.getCell(1).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(2).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(3).setCellStyle(cellStyleLeft);
			entryInfoValueRow.getCell(4).setCellStyle(cellStyle);
			entryInfoValueRow.getCell(5).setCellStyle(cellStyle);
			//allign right		
			entryInfoValueRow.getCell(6).setCellStyle(cellStyleRight);
			entryInfoValueRow.getCell(7).setCellStyle(cellStyleRight);

			entryRowNum++;
			count++;
		}
		//Entry[END]

		createTotalForMDD(hssfWorkbook, sheet, invoiceData, emptyField, style4, entryRowNum, 1, 7);

	}

	protected void createTotalForMDD(final Workbook hssfWorkbook, final Sheet sheet,
			final JnjGTInvoiceOrderData invoiceData, final String emptyField, final CellStyle style4, final int entryRowNum,
			final int columnNumStart, final int columnNumEnd)
	{
		//Totals[START]
		final Row subTotalRow = sheet.createRow(entryRowNum);
		final String subTotal = (invoiceData.getSubTotal() != null) ? invoiceData.getSubTotal().getFormattedValue() : emptyField;
		subTotalRow.createCell(columnNumStart).setCellValue(
				messageSource.getMessage(SUB_TOTAL,null, i18nService.getCurrentLocale()) + subTotal);
		subTotalRow.getCell(columnNumStart).setCellStyle(style4);
		final CellRangeAddress region = new CellRangeAddress(entryRowNum, entryRowNum, columnNumStart, columnNumEnd);
		sheet.addMergedRegion(region);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);


		final Row feesAndFreightRow = sheet.createRow(entryRowNum + 1);
		final String feesAndFreight = (invoiceData.getTempFeeAndPromotion() != null) ? invoiceData.getTempFeeAndPromotion()
				.getFormattedValue() : emptyField;
		feesAndFreightRow.createCell(columnNumStart).setCellValue(
				messageSource.getMessage(FEES_FREIGHT,null, i18nService.getCurrentLocale()) + feesAndFreight);
		feesAndFreightRow.getCell(columnNumStart).setCellStyle(style4);
		final CellRangeAddress region1 = new CellRangeAddress(entryRowNum + 1, entryRowNum + 1, columnNumStart, columnNumEnd);
		sheet.addMergedRegion(region1);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region1, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region1, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region1, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region1, sheet);

		final Row taxesRow = sheet.createRow(entryRowNum + 2);
		final String taxes = (invoiceData.getTaxAmt() != null) ? invoiceData.getTaxAmt().getFormattedValue() : emptyField;
		taxesRow.createCell(columnNumStart).setCellValue(messageSource.getMessage(TAXES,null, i18nService.getCurrentLocale()) + taxes);
		taxesRow.getCell(columnNumStart).setCellStyle(style4);
		final CellRangeAddress region2 = new CellRangeAddress(entryRowNum + 2, entryRowNum + 2, columnNumStart, columnNumEnd);
		sheet.addMergedRegion(region2);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region2, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region2, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region2, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region2, sheet);

		final Row invoiceTotalRow = sheet.createRow(entryRowNum + 3);
		final String invoiceTotal = (invoiceData.getNetValue() != null) ? invoiceData.getNetValue().getFormattedValue()
				: emptyField;
		invoiceTotalRow.createCell(columnNumStart).setCellValue(
				messageSource.getMessage(INVOICE_TOTAL,null, i18nService.getCurrentLocale()) + invoiceTotal);
		final Font font = hssfWorkbook.createFont();
		font.setBold(true);
		style4.setFont(font);
		invoiceTotalRow.getCell(columnNumStart).setCellStyle(style4);
		final CellRangeAddress region3 = new CellRangeAddress(entryRowNum + 3, entryRowNum + 3, columnNumStart, columnNumEnd);
		sheet.addMergedRegion(region3);
		//RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region3, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region3, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region3, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region3, sheet);
		//Totals[END]
	}

	protected void populateSurgeryData(final SurgeryInfoData surgeryInfoData, final Map<String, String> surgeryDataMap)
	{
		if (surgeryInfoData != null)
		{

			if (!StringUtils.isEmpty(surgeryInfoData.getZone()))
			{
				surgeryDataMap.put("surgeryInfoZone",
						getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.zone." + surgeryInfoData.getZone(), null, EMPTY));
			}

			if (!StringUtils.isEmpty(surgeryInfoData.getPathology()))
			{
				surgeryDataMap.put(
						"surgeryInfoPathology",
						getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.pathology." + surgeryInfoData.getPathology(),
								null, EMPTY));
			}

			if (!StringUtils.isEmpty(surgeryInfoData.getLevelsInstrumented()))
			{
				surgeryDataMap.put(
						"surgeryInfoLevelsInstrumented",
						getJnjCommonFacadeUtil().getMessageForCode(
								"surgeryInfoPopup.levelsInstrumented." + surgeryInfoData.getLevelsInstrumented(), null, EMPTY));
			}

			if (!StringUtils.isEmpty(surgeryInfoData.getInterbodyFusion()))
			{
				surgeryDataMap.put(
						"surgeryInfoInterbodyFusion",
						getJnjCommonFacadeUtil().getMessageForCode(
								"surgeryInfoPopup.interbodyFusion." + surgeryInfoData.getInterbodyFusion(), null, EMPTY));
			}

			if (!StringUtils.isEmpty(surgeryInfoData.getInterbody()))
			{
				surgeryDataMap.put(
						"surgeryInfoInterbody",
						getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.interbody." + surgeryInfoData.getInterbody(),
								null, EMPTY));
			}

			if (!StringUtils.isEmpty(surgeryInfoData.getSurgicalApproach()))
			{
				surgeryDataMap.put(
						"surgeryInfoSurgicalApproach",
						getJnjCommonFacadeUtil().getMessageForCode(
								"surgeryInfoPopup.surgicalApproach." + surgeryInfoData.getSurgicalApproach(), null, EMPTY));
			}

			if (!StringUtils.isEmpty(surgeryInfoData.getOrthobiologics()))
			{
				surgeryDataMap.put(
						"surgeryInfoOrthobiologics",
						getJnjCommonFacadeUtil().getMessageForCode(
								"surgeryInfoPopup.orthobiologics." + surgeryInfoData.getOrthobiologics(), null, EMPTY));
			}

			if (!StringUtils.isEmpty(surgeryInfoData.getSurgerySpecialty()))
			{
				surgeryDataMap.put(
						"surgeryInfoSurgerySpecialty",
						getJnjCommonFacadeUtil().getMessageForCode(
								"surgeryInfoPopup.surgerySpeciality." + surgeryInfoData.getSurgerySpecialty(), null, EMPTY));
			}
		}
		else
		{
			surgeryDataMap.put("surgeryInfoZone", messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()));
			surgeryDataMap.put("surgeryInfoPathology", messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()));
			surgeryDataMap.put("surgeryInfoLevelsInstrumented",
					messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()));
			surgeryDataMap.put("surgeryInfoInterbodyFusion",
					messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()));
			surgeryDataMap.put("surgeryInfoInterbody", messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()));
			surgeryDataMap.put("surgeryInfoSurgicalApproach",
					messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()));
			surgeryDataMap
					.put("surgeryInfoOrthobiologics", messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()));
			surgeryDataMap.put("surgeryInfoSurgerySpecialty",
					messageSource.getMessage(NOT_AVAILABLE,null, i18nService.getCurrentLocale()));
		}
	}

	protected static void createCellForOrderHeaderInfo(final Sheet sheet, final CellStyle styleForKey,
			final CellStyle styleForValue, final int rowIndex, final int cellNum, final String key, String value,
			final boolean showNotAvailable)
	{
		final String emptyField = new String();

		Row row = sheet.getRow(rowIndex);
		if (null == row)
		{
			row = sheet.createRow(rowIndex);
		}
		row.createCell(cellNum).setCellValue(key);
		row.getCell(cellNum).setCellStyle(styleForKey);

		if (StringUtils.isEmpty(value))
		{
			if (showNotAvailable)
			{
				value = ControllerConstants.JnjGTExcelPdfViewLabels.InvoiceDetails.NOT_AVAILABLE;
			}
			else
			{
				value = emptyField;
			}
		}

		row.createCell(cellNum + 1).setCellValue(value);
		row.getCell(cellNum + 1).setCellStyle(styleForValue);

		sheet.autoSizeColumn(cellNum);
		sheet.autoSizeColumn(cellNum + 1);
	}

	public static void setBorder(final CellRangeAddress region, final Sheet sheet, final Workbook hssfWorkbook)
	{
		//RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
	}

	protected static String getFormattedAddress(final AddressData addressData, final boolean isBillingAddress)
	{
		String addressField = new String();
		if (null != addressData)
		{
			if (isBillingAddress)
			{
				if (StringUtils.isNotEmpty(addressData.getFirstName()))
				{
					addressField = addressField.concat(addressData.getFirstName()
							+ (StringUtils.isEmpty(addressData.getLastName()) ? EMPTY : SPACE + addressData.getLastName()) + ", \n");
				}

				if (StringUtils.isNotEmpty(addressData.getLine1()))
				{
					addressField = addressField.concat(addressData.getLine1() + ", \n");
				}

				if (StringUtils.isNotEmpty(addressData.getLine2()))
				{
					addressField = addressField.concat(addressData.getLine2() + ", \n");
				}

				if (StringUtils.isNotEmpty(addressData.getTown()))
				{
					addressField = addressField.concat(addressData.getTown() + ",");
				}

				if (addressData.getRegion() != null)
				{
					if (StringUtils.isNotEmpty(addressData.getRegion().getName()))
					{
						addressField = addressField.concat(addressData.getRegion().getName() + ",");
					}
				}

			}
			else
			{

				if (StringUtils.isNotEmpty(addressData.getFirstName()))
				{
					addressField = addressField.concat(addressData.getFirstName()
							+ (StringUtils.isEmpty(addressData.getLastName()) ? EMPTY : SPACE + addressData.getLastName()) + ", \n");
				}

				if (StringUtils.isNotEmpty(addressData.getLine1()))
				{
					addressField = addressField.concat(addressData.getLine1() + " \n");
				}

				if (StringUtils.isNotEmpty(addressData.getLine2()))
				{
					addressField = addressField.concat(addressData.getLine2() + " \n");
				}

				if (StringUtils.isNotEmpty(addressData.getTown()))
				{
					addressField = addressField.concat(addressData.getTown() + SPACE);
				}

				if (addressData.getRegion() != null)
				{
					if (StringUtils.isNotEmpty(addressData.getRegion().getName()))
					{
						addressField = addressField.concat(addressData.getRegion().getName() + SPACE);
					}
				}
			}

			if (StringUtils.isNotEmpty(addressData.getPostalCode()))
			{
				if (addressData.getPostalCode().length() == 9)
				{

					addressField = addressField.concat(StringUtils.substring(addressData.getPostalCode(), 0, 5) + HYPHEN
							+ StringUtils.substring(addressData.getPostalCode(), 5, 9));

				}
				else
				{
					addressField = addressField.concat(addressData.getPostalCode());

				}

			}
		}


		return addressField;
	}

	protected static String getFormattedList(final Collection<String> collection)
	{
		final List<String> list = new ArrayList<>(collection);
		String listField = new String();

		if (CollectionUtils.isNotEmpty(list))
		{
			int count = 1;
			for (final String item : list)
			{
				if (StringUtils.isNotEmpty(item))
				{
					if (count > 1)
					{
						listField = listField.concat("\n" + item);
					}
					else
					{
						listField = listField.concat(item);
					}
					count++;
				}
			}
		}

		return listField;
	}

	protected static String getTrackingInfo(final JnjGTInvoiceOrderData invoiceOrderData, final JnjGTOrderData jnjGTOrderData)
	{

		String trackingInfo = new String();
		if (!CollectionUtils.isEmpty(invoiceOrderData.getInvoiceEntries()))
		{
			for (final JnjGTInvoiceEntryData invoiceEntry : invoiceOrderData.getInvoiceEntries())
			{

				trackingInfo = getTrackingInfoForInvoiceEntry(jnjGTOrderData, invoiceEntry.getLineNumber());
			}
		}

		return trackingInfo;
	}

	protected static String getTrackingInfoForInvoiceEntry(final JnjGTOrderData jnjGTOrderData, final String invoiceEntryNumber)
	{

		String trackingInfo = new String();

		if (!jnjGTOrderData.getShippingTrackingInfo().isEmpty())
		{
			int count = 1;
			for (final JnjGTShippingTrckInfoData shippingTrckInfoData : jnjGTOrderData.getShippingTrackingInfo().get(
					invoiceEntryNumber))
			{
				if (count > 1)
				{
					trackingInfo = trackingInfo.concat("\n" + shippingTrckInfoData.getTrackingId());
				}
				else
				{
					trackingInfo = trackingInfo.concat(shippingTrckInfoData.getTrackingId());
				}

				count++;
			}
		}
		return trackingInfo;
	}

	protected static void getCellStyles(final Workbook hssfWorkbook, final Map<String, CellStyle> cellStylesMap)
	{
		final Font font = hssfWorkbook.createFont();
		font.setBold(true);

		final CellStyle style = hssfWorkbook.createCellStyle();
		style.setWrapText(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		//style.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
		style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);


		final CellStyle cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);

		final CellStyle cellStyleRight = hssfWorkbook.createCellStyle();
		cellStyleRight.setWrapText(true);
		cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
		cellStyleRight.setBorderBottom(BorderStyle.THIN);
		cellStyleRight.setBorderLeft(BorderStyle.THIN);
		cellStyleRight.setBorderRight(BorderStyle.THIN);
		cellStyleRight.setBorderTop(BorderStyle.THIN);

		final CellStyle cellStyleLeft = hssfWorkbook.createCellStyle();
		cellStyleLeft.setWrapText(true);
		cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
		cellStyleLeft.setBorderBottom(BorderStyle.THIN);
		cellStyleLeft.setBorderLeft(BorderStyle.THIN);
		cellStyleLeft.setBorderRight(BorderStyle.THIN);
		cellStyleLeft.setBorderTop(BorderStyle.THIN);

		final CellStyle style3 = hssfWorkbook.createCellStyle();
		style3.setWrapText(true);
		style3.setAlignment(HorizontalAlignment.LEFT);
		style3.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style3.setFont(font);

		final CellStyle style4 = hssfWorkbook.createCellStyle();
		style4.setWrapText(true);
		style4.setAlignment(HorizontalAlignment.RIGHT);
		style4.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);


		final CellStyle orderHeaderInfoValueStyle = hssfWorkbook.createCellStyle();
		orderHeaderInfoValueStyle.setWrapText(true);
		final CellStyle orderHeaderInfoKeyStyle = hssfWorkbook.createCellStyle();
		orderHeaderInfoKeyStyle.setWrapText(true);
		orderHeaderInfoKeyStyle.setFont(font);

		cellStylesMap.put(STYLE, style);
		cellStylesMap.put(CELL_STYLE, cellStyle);

		cellStylesMap.put(CELL_STYLE_RIGHT, cellStyleRight);
		cellStylesMap.put(CELL_STYLE_LEFT, cellStyleLeft);

		cellStylesMap.put(STYLE3, style3);
		cellStylesMap.put(STYLE4, style4);

		cellStylesMap.put(ORDER_HEADER_INFO_KEY_STYLE, orderHeaderInfoKeyStyle);
		cellStylesMap.put(ORDER_HEADER_INFO_VALUE_STYLE, orderHeaderInfoValueStyle);
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
