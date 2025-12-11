/**
 *
 */
package com.jnj.b2b.jnjglobalordertemplate.download;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTShippingTrckInfoData;
import com.jnj.facades.data.SurgeryInfoData;
import com.jnj.facades.product.converters.populator.JnjGTProductBasicPopulator.SITE_NAME;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * Class responsible to create Excel view for Order detail.
 *
 * @author Accenture
 *
 */
public class JnjGTOrderDetailExcelView extends AbstractXlsView
{
	private static final String NEW_LINE_CAHARACTER = "\n";
	private static final String ORDER_TYPE_CONSTANT = "cart.common.orderType.";
	private static final String sheetName = "Order Detail";
	private static final String NOT_AVAILABLE = "Not Available";
	private static final String EMPTY_FIELD = "Not Available";
   private JnjCommonFacadeUtil jnjCommonFacadeUtil;
   
   @Autowired
   protected SessionService sessionService;
   
   @Autowired
   protected JnJCommonUtil jnjCommonUtil;

	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	
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
  
   
	private static final String ORDER_NUMBER = "orderdetail.order.number";
	private static final String ACCOUNT_NUMBER = "orderdetail.order.accountnumber";
	private static final String SHIP_TO_ADDRESS = "orderdetail.order.shipToaddress";
	private static final String ATTENTION = "orderdetail.order.attention";
	private static final String BILLING_NAME_ADDRESS = "checkout.summary.paymentMethod.billingAddress.header";//AAOL-5692
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
	private static final String CONTRACT_NUMBER = "orderdetail.order.ContractNum";
	private static final String SPECIAL_STOCK_PARTNER = "orderdetail.order.SpecialStockPartner";
	private static final String EXTENDED_PRICE = "orderdetail.order.ExtendedPrice";
	private static final String ESTIMATED_DELIVERY = "orderdetail.order.EstimatedDelDate";
	private static final String SHIPPING_METHOD = "orderdetail.order.ShippingMethod";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	private static final String ORDER_INFORMATION = "orderdetail.order.OrderInformation";
	private static final String BILLING_INFORMATION = "orderdetail.order.BillingInformation";
	private static final String SHIPPING_INFORMATION = "orderdetail.order.ShippingInformation";
	private static final String JNJ_ORDER_NUMBER = "orderdetail.order.J&JOrderNumber";
	private static final String PO_NUMBER = "orderdetail.order.Purchaseorder";
	private static final String ORDER_DATE = "orderdetail.order.OrderDate";
	private static final String ORDER_STATUS = "orderdetail.order.Status";//AAOL-5693
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
	private static final String PACKING_LIST = "orderdetail.order.Packinglist";
	private static final String CARRIER = "orderdetail.order.Carrier";
	private static final String BILL_OF_LANDING = "orderdetail.order.BillofLading";
	private static final String LINE = "orderdetail.order.line";
	private static final String PRODUCT_CODE = "orderdetail.order.productcode.gtin";
	private static final String CONTRACT = "orderdetail.order.contract";
	private static final String QTY = "orderdetail.order.qty";
	private static final String UOM = "orderdetail.order.uom";
	private static final String STATUS = "orderdetail.order.status";
	private static final String ITEM_PRICE = "orderdetail.order.itemprice";
	private static final String TOTAL = "orderdetail.order.total";
	private static final String SUB_TOTAL = "orderdetail.order.subtotal";
	private static final String FEES_FREIGHT = "orderdetail.order.feesfreight";
	private static final String TAXES = "orderdetail.order.taxes";
	private static final String ORDER_TOTAL = "orderdetail.order.ordertotal";
	private static final String TRACKING = "orderdetail.order.tracking";
	private static final String REASON_CODE = "orderdetail.order.reasoncode";
	private static final String CORDIS_HOUSE_ACCOUNT = "orderdetail.order.cordishouseacc";
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

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workBook,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		final JnjGTOrderData orderDetails = (JnjGTOrderData) map.get("orderData");
		final String fileName = orderDetails.getOrderNumber()!=null?"Order_"+orderDetails.getOrderNumber() + ".xls" : "Order.xls";
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		final String siteName = (String) map.get(Jnjb2bCoreConstants.SITE_NAME);
		final Sheet sheet = workBook.createSheet(sheetName);
		final String emptyField = new String("Not Available");
		final CellStyle headerStyle = getStyleForTableHeader(workBook);//Modified by Archana for AAOL-5513
		final DateFormat df2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		//Setting the header for first table
		//Siddhant 
		setHeaderImage(workBook, sheet, (String) map.get("siteLogoPath"),5);
		int row1Num = 6;
		int row2Num = 6;
		int column1Num = 1;
		int column2Num = 3;
		final Row row7 = sheet.createRow(row1Num);
		row7.setHeight((short) 500);
		row7.createCell(column1Num).setCellValue(messageSource.getMessage(ORDER_INFORMATION, null, i18nService.getCurrentLocale()));
		//row7.getCell(1).setCellStyle(headerStyle);
		row7.getCell(column1Num).setCellStyle(headerStyle);
		//createMergedRegion(sheet, hssfWorkbook, 6, 6, 1, 3);
		createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 3;
		column2Num = column2Num + 4;
		row7.createCell(column1Num).setCellValue(messageSource.getMessage(BILLING_INFORMATION, null, i18nService.getCurrentLocale()));
		row7.getCell(column1Num).setCellStyle(headerStyle);
		//createMergedRegion(sheet, hssfWorkbook, 6, 6, 4, 7);
		createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
		if (orderDetails.getOrderType().equalsIgnoreCase("ZDEL"))
		{
			column1Num = column1Num + 4;
			column2Num = column2Num + 2;
			row7.createCell(column1Num).setCellValue(messageSource.getMessage(SHIPPING_INFORMATION, null, i18nService.getCurrentLocale()));
			row7.getCell(column1Num).setCellStyle(headerStyle);
			createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
			column1Num = column1Num + 2;
			column2Num = column2Num + 2;
			row7.createCell(column1Num).setCellValue(messageSource.getMessage(SURGERY_INFORMATION, null, i18nService.getCurrentLocale()));
			row7.getCell(column1Num).setCellStyle(headerStyle);
			createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);

		}
		else
		{
			column1Num = column1Num + 4;
			column2Num = column2Num + 4;
			row7.createCell(column1Num).setCellValue(messageSource.getMessage(SHIPPING_INFORMATION, null, i18nService.getCurrentLocale()));
			row7.getCell(column1Num).setCellStyle(headerStyle);
			createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
		}


		int cellNum = 1;
		row1Num = row1Num + 1;
		/*
		 * createCellForOrderHeaderInfo(sheet, hssfWorkbook, 7, 1,
		 * messageSource.getMessage(BILLING_INFORMATION, null, i18nService.getCurrentLocale())JNJ_ORDER_NUMBER, orderDetails.getOrderNumber(), false,
		 * 1, false);
		 */
		createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
				messageSource.getMessage(JNJ_ORDER_NUMBER, null, i18nService.getCurrentLocale()),
				((orderDetails.getOrderNumber() != null) ? orderDetails.getOrderNumber() : NOT_AVAILABLE), 1, false);
		row1Num = row1Num + 1;
		
		//JJEPIC-629 GTR-1682
		
		createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
				messageSource.getMessage(PO_NUMBER, null, i18nService.getCurrentLocale()),
				((orderDetails.getPurchaseOrderNumber() != null) ? orderDetails.getPurchaseOrderNumber() : NOT_AVAILABLE), 1, false);
		row1Num = row1Num + 1;
		
		createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
				messageSource.getMessage(ORDER_DATE, null, i18nService.getCurrentLocale()),
				((orderDetails.getCreated() != null) ? df2.format(orderDetails.getCreated()) : NOT_AVAILABLE), 1, false);
		row1Num = row1Num + 1;
		createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
				messageSource.getMessage(ORDER_STATUS, null, i18nService.getCurrentLocale()),
				((orderDetails.getStatusDisplay() != null) ? orderDetails.getStatusDisplay() : NOT_AVAILABLE), 1, false);
		row1Num = row1Num + 1;
		if (!(orderDetails.getOrderType().equalsIgnoreCase("ZKB") || orderDetails.getOrderType().equalsIgnoreCase("ZHOR")
				|| orderDetails.getOrderType().equalsIgnoreCase("ZTOR") || orderDetails.getOrderType().equalsIgnoreCase("ZIO2")))
		{
			createCellForOrderHeaderInfo(
					sheet,
					workBook,
					row1Num,
					cellNum,
					messageSource.getMessage(ORDER_TYPE, null, i18nService.getCurrentLocale()),
					((orderDetails.getOrderType() != null) ? getJnjCommonFacadeUtil().getMessageForCode(
							ORDER_TYPE_CONSTANT + orderDetails.getOrderType(), null, "") : ""), 1, false);
			row1Num = row1Num + 1;
		}
		createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
				messageSource.getMessage(ORDER_BY, null, i18nService.getCurrentLocale()),
				((orderDetails.getOrderedBy() != null) ? orderDetails.getOrderedBy() : NOT_AVAILABLE), 1, false);
		row1Num = row1Num + 1;
		createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
				messageSource.getMessage(NUMBER_OF_LINES, null, i18nService.getCurrentLocale()),
				((orderDetails.getTotalNumberOfEntries() != null) ? orderDetails.getTotalNumberOfEntries() : NOT_AVAILABLE), 1, false);

		if (orderDetails.getOrderType().equalsIgnoreCase("ZHOR") || orderDetails.getOrderType().equalsIgnoreCase("ZNC")
				|| orderDetails.getOrderType().equalsIgnoreCase("ZRE"))
		{
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(
					sheet,
					workBook,
					row1Num,
					cellNum,
					messageSource.getMessage(REASON_CODE, null, i18nService.getCurrentLocale()),
					((orderDetails.getReasonCode() != null) ? orderDetails.getReasonCode() : NOT_AVAILABLE)
							+ ":"
							+ ((orderDetails.getReasonCode() != null) ? getJnjCommonFacadeUtil().getMessageForCode(
									orderDetails.getReasonCode(), null, "") : NOT_AVAILABLE), 1, false);
		}

		row1Num = populateOrderSpecificInformation(sheet, workBook, orderDetails, row1Num);
		populateBorders(sheet, workBook, row1Num, cellNum, 3, false);
		row1Num = 7;
		cellNum = cellNum + 3;
		createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
				messageSource.getMessage(SOLD_TO_ACC_NUMBER, null, i18nService.getCurrentLocale()), orderDetails.getSoldToAccount(), 2, false);
		if (orderDetails.getOrderType().equalsIgnoreCase("ZOR") || orderDetails.getOrderType().equalsIgnoreCase("ZNC")
				|| orderDetails.getOrderType().equalsIgnoreCase("ZRE") || orderDetails.getOrderType().equalsIgnoreCase("ZEX")
				|| orderDetails.getOrderType().equalsIgnoreCase("ZDEL"))
		{
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(GLN, null, i18nService.getCurrentLocale()),
					((orderDetails.getGln() != null) ? orderDetails.getGln() : NOT_AVAILABLE), 2, false);
		}
		row1Num = row1Num + 1;
		createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
				messageSource.getMessage(BILLING_NAME_ADDRESS, null, i18nService.getCurrentLocale()),
				getFormattedAddress(orderDetails.getBillingAddress(), true, orderDetails, siteName), 2, false);
		if (orderDetails.getOrderType().equalsIgnoreCase("ZOR") || orderDetails.getOrderType().equalsIgnoreCase("ZDEL") || orderDetails.getOrderType().equalsIgnoreCase("ZORD"))
		{
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(PAYMENT_METHOD, null, i18nService.getCurrentLocale()),
					(orderDetails.getPaymentType() != null) ? orderDetails.getPaymentType().getDisplayName() : NOT_AVAILABLE, 2, false);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(CREDIT_CARD_NUMBER, null, i18nService.getCurrentLocale()),
					(orderDetails.getPaymentInfo() != null) ? ((orderDetails.getPaymentInfo().getCardNumber() != null) ? orderDetails
							.getPaymentInfo().getCardNumber() : NOT_AVAILABLE) : NOT_AVAILABLE, 2, false);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(CREDIT_CARD_TYPE, null, i18nService.getCurrentLocale()),
					(orderDetails.getPaymentInfo() != null) ? ((orderDetails.getPaymentInfo().getCardType() != null) ? orderDetails
							.getPaymentInfo().getCardType() : NOT_AVAILABLE) : NOT_AVAILABLE, 2, false);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(CREDIT_CARD_EXPIRY, null, i18nService.getCurrentLocale()),
					(orderDetails.getPaymentInfo() != null) ? orderDetails.getPaymentInfo().getExpiryMonth() + "-"
							+ orderDetails.getPaymentInfo().getExpiryYear() : NOT_AVAILABLE, 2, false);
		}
		
		populateBorders(sheet, workBook, row1Num, cellNum, 4, false);

		row1Num = 7;
		cellNum = cellNum + 4;
		int cellSpan = 2;
		int cellSpanBorder = 4;
		boolean border = true;
		if (orderDetails.getOrderType().equalsIgnoreCase("ZDEL"))
		{
			cellSpan = 0;
			cellSpanBorder = 2;
			border = false;
		}
		if (!orderDetails.getOrderType().equalsIgnoreCase("ZRE") && !orderDetails.getOrderType().equalsIgnoreCase("ZHOR")
				&& !orderDetails.getOrderType().equalsIgnoreCase("ZTOR") && !orderDetails.getOrderType().equalsIgnoreCase("ZIO2"))
		{
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(DROP_SHIP_ACC, null, i18nService.getCurrentLocale()) + "#",
					(orderDetails.getDropShipAccount() != null) ? orderDetails.getDropShipAccount() : NOT_AVAILABLE, cellSpan, border);
			row1Num = row1Num + 1;
		}
		createCellForOrderHeaderInfo(
				sheet,
				workBook,
				row1Num,
				cellNum,
				messageSource.getMessage(SHIP_TO_ADDRESS, null, i18nService.getCurrentLocale()),
				(orderDetails.getDeliveryAddress() != null) ? getFormattedAddress(orderDetails.getDeliveryAddress(), false,
						orderDetails, siteName) : EMPTY_FIELD, cellSpan, border);
		if (!orderDetails.getOrderType().equalsIgnoreCase("ZRE") && !orderDetails.getOrderType().equalsIgnoreCase("ZHOR")
				&& !orderDetails.getOrderType().equalsIgnoreCase("ZTOR") && !orderDetails.getOrderType().equalsIgnoreCase("ZIO2"))
		{
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(ATTENTION, null, i18nService.getCurrentLocale()),
					((orderDetails.getAttention() != null) ? orderDetails.getAttention() : NOT_AVAILABLE), cellSpan, border);

		}
		if (orderDetails.getOrderType().equalsIgnoreCase("ZHOR") || orderDetails.getOrderType().equalsIgnoreCase("ZTOR")
				|| orderDetails.getOrderType().equalsIgnoreCase("ZIO2"))
		{
			row1Num = row1Num + 1;

			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(TOTAL_WEIGHT, null, i18nService.getCurrentLocale()), orderDetails.getOrderWeight() + "lbs",
					cellSpan, border);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(TOTAL_VOLUME, null, i18nService.getCurrentLocale()), orderDetails.getOrderVolume() + "ft3 ",
					cellSpan, border);

		}
		populateBorders(sheet, workBook, row1Num, cellNum, cellSpanBorder, border);
		//Setting surgery information for delivered order
		if (orderDetails.getOrderType().equalsIgnoreCase("ZDEL"))
		{
			row1Num = 7;
			cellNum = cellNum + 2;
			final Map<String, String> surgeryDataMap = new HashMap<>();
			populateSurgeryData(orderDetails.getSurgeryInfo(), surgeryDataMap);

			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(SURGEON, null, i18nService.getCurrentLocale()),
					(orderDetails.getSurgeonName() != null) ? orderDetails.getSurgeonName() : NOT_AVAILABLE, 0, true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(
					sheet,
					workBook,
					row1Num,
					cellNum,
					messageSource.getMessage(CASE_DATE, null, i18nService.getCurrentLocale()),
					(orderDetails.getSurgeryInfo() != null && orderDetails.getSurgeryInfo().getCaseDate() != null) ? df2
							.format(orderDetails.getSurgeryInfo().getCaseDate()) : NOT_AVAILABLE, 0, true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(SURGERY_SPECIALITY, null, i18nService.getCurrentLocale()),
					surgeryDataMap.get("surgeryInfoSurgerySpecialty"), 0, true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(LEVELS_INSTRUMENTED, null, i18nService.getCurrentLocale()),
					surgeryDataMap.get("surgeryInfoLevelsInstrumented"), 0, true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(ORTHOBIOLOGICS, null, i18nService.getCurrentLocale()),
					surgeryDataMap.get("surgeryInfoOrthobiologics"), 0, true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(PATHOLOGY, null, i18nService.getCurrentLocale()), surgeryDataMap.get("surgeryInfoPathology"), 0,
					true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(SURGICAL_APPROACH, null, i18nService.getCurrentLocale()),
					surgeryDataMap.get("surgeryInfoSurgicalApproach"), 0, true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(ZONE, null, i18nService.getCurrentLocale()), surgeryDataMap.get("surgeryInfoZone"), 0, true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(INTERBODY, null, i18nService.getCurrentLocale()), surgeryDataMap.get("surgeryInfoInterbody"), 0,
					true);
			row1Num = row1Num + 1;
			createCellForOrderHeaderInfo(sheet, workBook, row1Num, cellNum,
					messageSource.getMessage(FILE_CASE, null, i18nService.getCurrentLocale()),
					(orderDetails.getSurgeryInfo() != null && orderDetails.getSurgeryInfo().getCaseNumber() != null) ? orderDetails
							.getSurgeryInfo().getCaseNumber() : NOT_AVAILABLE, 0, true);
			populateBorders(sheet, workBook, row1Num, cellNum, 2, true);
		}
		row1Num = 21;
		row2Num = 21;
		if (!orderDetails.getOrderType().equalsIgnoreCase("ZDEL") && !orderDetails.getOrderType().equalsIgnoreCase("ZRE"))
		{
			final List trackingKeyList = new ArrayList<>();
			final List<Date> shipDateList = getShipDate(orderDetails);
			final List<Date> deliveryDateList = getDeliveryDate(orderDetails);
			if (!orderDetails.getTrackingIdList().isEmpty())
			{
				trackingKeyList.addAll(orderDetails.getTrackingIdList().keySet());
			}

			column1Num = 1;
			column2Num = 11;
			// Setting Delivery Information Table
			sheet.createRow(row1Num).createCell(column1Num)
					.setCellValue(messageSource.getMessage(DELIVERY_INFORMATION, null, i18nService.getCurrentLocale()));
			//headerStyle.setAlignment();
			final CellStyle newStyle = workBook.createCellStyle();
			newStyle.cloneStyleFrom(headerStyle);
			final CellStyle style1 = workBook.createCellStyle();
			style1.setWrapText(true);
			newStyle.setAlignment(HorizontalAlignment.LEFT);
			sheet.getRow(row1Num).getCell(column1Num).setCellStyle(newStyle);
			createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
			final int sizeOfDeliveryInfo = getSizeOfDeliveryInformation(orderDetails);
			row1Num = row1Num + 1;
			row2Num = row2Num + 1;
			column1Num = 1;
			column2Num = 3;
			if (orderDetails.getOrderType().equalsIgnoreCase("ZNC"))
			{
				column1Num = 1;
				column2Num = 2;
				sheet.createRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(SHIPPING_DATE, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 2;
				column2Num = column2Num + 1;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(DELIVERY_DATE, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 1;
				column2Num = column2Num + 2;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(PACKING_LIST, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 2;
				column2Num = column2Num + 2;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(CARRIER, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 2;
				column2Num = column2Num + 2;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(BILL_OF_LANDING, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 2;
				column2Num = column2Num + 2;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(TRACKING, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);

				for (int index = 1; index <= sizeOfDeliveryInfo; index++)
				{
					row1Num = row1Num + 1;
					row2Num = row2Num + 1;
					column1Num = 1;
					column2Num = 2;

					sheet.createRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									((shipDateList.size() >= index && null != shipDateList.get((index - 1))) ? df2.format(shipDateList
											.get((index - 1))) : ""));
					sheet.getRow(row1Num).setHeight((short) 650);
					sheet.getRow(row1Num).setHeight((short) 60);
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 2;
					column2Num = column2Num + 1;

					sheet.getRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									(deliveryDateList.size() >= index && null != deliveryDateList.get((index - 1))) ? df2
											.format(deliveryDateList.get((index - 1))) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 1;
					column2Num = column2Num + 2;

					sheet.getRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									(orderDetails.getPackingListDetails().size() >= index) ? orderDetails.getPackingListDetails().get(
											(index - 1)) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 2;
					column2Num = column2Num + 2;

					sheet.getRow(row1Num).createCell(column1Num)
							.setCellValue((orderDetails.getCarrier().size() >= index) ? orderDetails.getCarrier().get((index - 1)) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					sheet.getRow(row1Num).setHeight((short) (600));
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 2;
					column2Num = column2Num + 2;

					sheet.getRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									(orderDetails.getBillOfLading().size() >= index) ? orderDetails.getBillOfLading().get((index - 1))
											: "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 2;
					column2Num = column2Num + 2;

					sheet.getRow(row1Num).createCell(column1Num)
							.setCellValue((trackingKeyList.size() >= index) ? String.valueOf(trackingKeyList.get((index - 1))) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				}
			}
			else if (orderDetails.getOrderType().equalsIgnoreCase("ZHOR") || orderDetails.getOrderType().equalsIgnoreCase("ZTOR")
					|| orderDetails.getOrderType().equalsIgnoreCase("ZIO2"))
			{
				column1Num = 1;
				column2Num = 1;
				sheet.createRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(REQ_DELIVERY_DATE, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).setHeight((short) 650);
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				sheet.setColumnWidth(column1Num, 4000);
				column1Num = column1Num + 1;
				column2Num = column2Num + 1;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(ACTUAL_SHIP_DATE, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				column1Num = column1Num + 1;
				column2Num = column2Num + 1;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(ESTIMATED_DELIVERY, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				column1Num = column1Num + 1;
				column2Num = column2Num + 1;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(ACTUAL_DELIVERY_DATE, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				sheet.setColumnWidth(column1Num, 4050);
				column1Num = column1Num + 1;
				column2Num = column2Num + 1;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(SHIPMENT_LOCATION, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				sheet.setColumnWidth(column1Num, 4000);
				column1Num = column1Num + 1;
				column2Num = column2Num + 2;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(CARRIER, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 2;
				column2Num = column2Num + 2;

				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(BILL_OF_LANDING, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 2;
				column2Num = column2Num + 2;

				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(TRACKING, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				for (int index = 1; index <= sizeOfDeliveryInfo; index++)
				{
					row1Num = row1Num + 1;
					row2Num = row2Num + 1;
					column1Num = 1;
					column2Num = 1;
					sheet.createRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									(orderDetails.getRequestedDeliveryDate() != null) ? df2.format(orderDetails.getRequestedDeliveryDate())
											: "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 1;
					column2Num = column2Num + 1;
					sheet.getRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									((shipDateList.size() >= index && null != shipDateList.get((index - 1))) ? df2.format(shipDateList
											.get((index - 1))) : ""));
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 1;
					column2Num = column2Num + 1;
					sheet.getRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									(orderDetails.getExpectedDeliveryDate() != null) ? df2.format(orderDetails.getExpectedDeliveryDate())
											: "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 1;
					column2Num = column2Num + 1;

					sheet.getRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									((deliveryDateList.size() >= index && null != deliveryDateList.get((index - 1))) ? df2
											.format(deliveryDateList.get((index - 1))) : ""));
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 1;
					column2Num = column2Num + 1;

					sheet.getRow(row1Num).createCell(column1Num)
							.setCellValue((orderDetails.getShipmentLocation() != null) ? orderDetails.getShipmentLocation() : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);

					column1Num = column1Num + 1;
					column2Num = column2Num + 2;
					sheet.getRow(row1Num).createCell(column1Num)
							.setCellValue((orderDetails.getCarrier().size() >= index) ? orderDetails.getCarrier().get((index - 1)) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 2;
					column2Num = column2Num + 2;

					sheet.getRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									(orderDetails.getBillOfLading().size() >= index) ? orderDetails.getBillOfLading().get((index - 1))
											: "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 2;
					column2Num = column2Num + 2;

					sheet.getRow(row1Num).createCell(column1Num)
							.setCellValue((trackingKeyList.size() >= index) ? String.valueOf(trackingKeyList.get((index - 1))) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				}

			}
			else
			{
				column1Num = 1;
				column2Num = 3;
				sheet.createRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(PACKING_LIST, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).setHeight((short) 650);
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 3;
				column2Num = column2Num + 3;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(CARRIER, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 3;
				column2Num = column2Num + 3;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(BILL_OF_LANDING, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				column1Num = column1Num + 3;
				column2Num = column2Num + 2;
				sheet.getRow(row1Num).createCell(column1Num)
						.setCellValue(messageSource.getMessage(TRACKING, null, i18nService.getCurrentLocale()));
				sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
				createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);

				for (int index = 1; index <= sizeOfDeliveryInfo; index++)
				{
					row1Num = row1Num + 1;
					row2Num = row2Num + 1;
					column1Num = 1;
					column2Num = 3;

					sheet.createRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									(orderDetails.getPackingListDetails().size() >= index) ? orderDetails.getPackingListDetails().get(
											(index - 1)) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 3;
					column2Num = column2Num + 3;

					sheet.getRow(row1Num).createCell(column1Num)
							.setCellValue((orderDetails.getCarrier().size() >= index) ? orderDetails.getCarrier().get((index - 1)) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 3;
					column2Num = column2Num + 3;

					sheet.getRow(row1Num)
							.createCell(column1Num)
							.setCellValue(
									(orderDetails.getBillOfLading().size() >= index) ? orderDetails.getBillOfLading().get((index - 1))
											: "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
					column1Num = column1Num + 3;
					column2Num = column2Num + 2;

					sheet.getRow(row1Num).createCell(column1Num)
							.setCellValue((trackingKeyList.size() >= index) ? String.valueOf(trackingKeyList.get((index - 1))) : "");
					sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
					createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
				}
			}
		}
		// Setting Order Entry Table
		row1Num = row1Num + 2;
		row1Num = createOrderEntryTable(sheet, workBook, row1Num, headerStyle, orderDetails, siteName);
		final CellStyle style1 = getStyleForBorder(workBook);
		style1.setAlignment(HorizontalAlignment.RIGHT);
		row1Num = row1Num + 1;
		row2Num = row1Num;
		column1Num = 1;
		column2Num = 11;
		sheet.createRow(row1Num)
				.createCell(column1Num)
				.setCellValue(
						messageSource.getMessage(SUB_TOTAL, null, i18nService.getCurrentLocale()) + ""
								+ orderDetails.getTotalNetValue().getFormattedValue());
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
		createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
		if (!orderDetails.getOrderType().equalsIgnoreCase("ZHOR") && !orderDetails.getOrderType().equalsIgnoreCase("ZTOR")
				&& !orderDetails.getOrderType().equalsIgnoreCase("ZIO2"))
		{
			row1Num = row1Num + 1;
			row2Num = row2Num + 1;
			sheet.createRow(row1Num)
					.createCell(column1Num)
					.setCellValue(
							messageSource.getMessage(FEES_FREIGHT, null, i18nService.getCurrentLocale()) + ""
									+ orderDetails.getTotalFees().getFormattedValue());
			sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
			createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);
		}

		row1Num = row1Num + 1;
		row2Num = row2Num + 1;
		sheet.createRow(row1Num)
				.createCell(column1Num)
				.setCellValue(
						messageSource.getMessage(TAXES, null, i18nService.getCurrentLocale()) + ""
								+ orderDetails.getTotalTax().getFormattedValue());
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(style1);
		createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);

		row1Num = row1Num + 1;
		row2Num = row2Num + 1;
		sheet.createRow(row1Num)
				.createCell(column1Num)
				.setCellValue(
						messageSource.getMessage(ORDER_TOTAL, null, i18nService.getCurrentLocale()) + ""
								+ orderDetails.getTotalPrice().getFormattedValue());
		final Font hssfFont = workBook.createFont();
		hssfFont.setBold(true);
		final CellStyle newStyle = workBook.createCellStyle();
		newStyle.cloneStyleFrom(style1);
		newStyle.setFont(hssfFont);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(newStyle);
		createMergedRegion(sheet, workBook, row1Num, row2Num, column1Num, column2Num);


	}

	public int createOrderEntryTable(final Sheet sheet, final Workbook hssfWorkbook, final int rowNum,
			final CellStyle headerStyle, final JnjGTOrderData orderDetails, final String siteName)
	{
		final String emptyField = new String(" ");
		final Row row1 = sheet.createRow(rowNum);
		row1.setHeight((short) 500);
		row1.createCell(1).setCellValue(messageSource.getMessage(LINE, null, i18nService.getCurrentLocale()));
		row1.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_NAME, null, i18nService.getCurrentLocale()));
		row1.createCell(3).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
		if (orderDetails.getOrderType().equalsIgnoreCase("ZOR") || orderDetails.getOrderType().equalsIgnoreCase("ZEX"))
		{
			row1.createCell(4).setCellValue(messageSource.getMessage(CONTRACT, null, i18nService.getCurrentLocale()));
			row1.getCell(4).setCellStyle(headerStyle);
			createMergedRegion(sheet, hssfWorkbook, rowNum, rowNum, 4, 5);
			row1.createCell(6).setCellValue(messageSource.getMessage(QTY, null, i18nService.getCurrentLocale()));
			row1.createCell(7).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
			row1.createCell(8).setCellValue(messageSource.getMessage(SHIPPING_INFORMATION, null, i18nService.getCurrentLocale()));
			row1.getCell(6).setCellStyle(headerStyle);
			row1.getCell(7).setCellStyle(headerStyle);
			row1.getCell(8).setCellStyle(headerStyle);
			row1.createCell(9).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
			row1.getCell(9).setCellStyle(headerStyle);
		}
		else if (orderDetails.getOrderType().equalsIgnoreCase("ZNC") || orderDetails.getOrderType().equalsIgnoreCase("ZKB"))
		{
			createMergedRegion(sheet, hssfWorkbook, rowNum, rowNum, 3, 4);
			row1.createCell(5).setCellValue(messageSource.getMessage(QTY, null, i18nService.getCurrentLocale()));
			row1.getCell(5).setCellStyle(headerStyle);
			createMergedRegion(sheet, hssfWorkbook, rowNum, rowNum, 5, 6);
			row1.createCell(7).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
			row1.getCell(7).setCellStyle(headerStyle);
			row1.createCell(8).setCellValue(messageSource.getMessage(SHIPPING_INFORMATION, null, i18nService.getCurrentLocale()));
			row1.getCell(8).setCellStyle(headerStyle);
			row1.createCell(9).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
			row1.getCell(9).setCellStyle(headerStyle);
		}
		else if (orderDetails.getOrderType().equalsIgnoreCase("ZRE"))
		{
			row1.createCell(4).setCellValue(messageSource.getMessage(LOT_NUMBER, null, i18nService.getCurrentLocale()));
			row1.getCell(4).setCellStyle(headerStyle);
			createMergedRegion(sheet, hssfWorkbook, rowNum, rowNum, 4, 5);
			row1.createCell(6).setCellValue(messageSource.getMessage(QTY, null, i18nService.getCurrentLocale()));
			row1.getCell(6).setCellStyle(headerStyle);
			row1.createCell(7).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
			row1.getCell(7).setCellStyle(headerStyle);
			createMergedRegion(sheet, hssfWorkbook, rowNum, rowNum, 7, 8);
			row1.createCell(9).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
			row1.getCell(9).setCellStyle(headerStyle);
		}
		else if (orderDetails.getOrderType().equalsIgnoreCase("ZDEL"))
		{
			row1.createCell(4).setCellValue(messageSource.getMessage(CONTRACT, null, i18nService.getCurrentLocale()));
			row1.createCell(5).setCellValue(messageSource.getMessage(LOT_NUMBER, null, i18nService.getCurrentLocale()));
			row1.createCell(6).setCellValue(messageSource.getMessage(SSP, null, i18nService.getCurrentLocale()));
			row1.createCell(7).setCellValue(messageSource.getMessage(QTY, null, i18nService.getCurrentLocale()));
			row1.createCell(8).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
			row1.getCell(4).setCellStyle(headerStyle);
			row1.getCell(5).setCellStyle(headerStyle);
			row1.getCell(6).setCellStyle(headerStyle);
			row1.getCell(7).setCellStyle(headerStyle);
			row1.getCell(8).setCellStyle(headerStyle);
			row1.createCell(9).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
			row1.getCell(9).setCellStyle(headerStyle);

		}
		else
		{
			createMergedRegion(sheet, hssfWorkbook, rowNum, rowNum, 2, 3);
			if (SITE_NAME.CONS.toString().equals(siteName))
			{
				row1.createCell(4).setCellValue(messageSource.getMessage(CONS_PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			}
			else
			{
				row1.createCell(4).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			}
			row1.getCell(4).setCellStyle(headerStyle);
			createMergedRegion(sheet, hssfWorkbook, rowNum, rowNum, 4, 5);
			row1.createCell(6).setCellValue(messageSource.getMessage(QTY, null, i18nService.getCurrentLocale()));
			row1.getCell(6).setCellStyle(headerStyle);
			row1.createCell(7).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
			sheet.setColumnWidth(7, 2500);
			row1.getCell(7).setCellStyle(headerStyle);
			row1.createCell(8).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
			row1.getCell(8).setCellStyle(headerStyle);
			createMergedRegion(sheet, hssfWorkbook, rowNum, rowNum, 8, 9);
		}
		row1.createCell(10).setCellValue(messageSource.getMessage(ITEM_PRICE, null, i18nService.getCurrentLocale()));
		sheet.setColumnWidth(10, 4000);
		row1.createCell(11).setCellValue(messageSource.getMessage(TOTAL, null, i18nService.getCurrentLocale()));
		row1.getCell(1).setCellStyle(headerStyle);
		row1.getCell(2).setCellStyle(headerStyle);
		row1.getCell(3).setCellStyle(headerStyle);
		row1.getCell(10).setCellStyle(headerStyle);
		row1.getCell(11).setCellStyle(headerStyle);
		final CellStyle style = getStyleForBorder(hssfWorkbook);
		int entryRowNum = rowNum;
		for (final OrderEntryData entryData : orderDetails.getEntries())
		{
			final JnjGTOrderEntryData entry = (JnjGTOrderEntryData) entryData;
			final JnjGTProductData product = (JnjGTProductData) entry.getProduct();
			entryRowNum++;
			final Row row2 = sheet.createRow(entryRowNum);
			row2.setHeight((short) 500);
			row2.createCell(1).setCellValue(
					StringUtils.isNotEmpty(entry.getSapOrderlineNumber()) ? entry.getSapOrderlineNumber() : emptyField);
			row2.createCell(2).setCellValue(
					product != null ? StringUtils.isNotEmpty(product.getName()) ? product.getName().replaceAll("\\<.*?>", "")
							: emptyField : emptyField);
			if (SITE_NAME.CONS.toString().equals(siteName))
			{
				row2.createCell(3).setCellValue(
						"ID:" + product.getBaseMaterialNumber().replaceAll("^0+", StringUtils.EMPTY) + "\n" + "UPC"
								+ (StringUtils.isNotEmpty(product.getUpc()) ? product.getUpc() : NOT_AVAILABLE));
			}
			else
			{
				row2.createCell(3).setCellValue(
						"ID:" + product.getCode() + "\n" + "GTIN"
								+ (StringUtils.isNotEmpty(product.getGtin()) ? product.getGtin() : NOT_AVAILABLE));
			}
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(2, 5000);
			row2.getCell(1).setCellStyle(style);
			row2.getCell(2).setCellStyle(style);

			if (orderDetails.getOrderType().equalsIgnoreCase("ZOR") || orderDetails.getOrderType().equalsIgnoreCase("ZEX"))
			{
				row2.getCell(3).setCellStyle(style);
				row2.createCell(4).setCellValue(
						StringUtils.isNotEmpty(entry.getContractNumber()) ? entry.getContractNumber() : emptyField);
				row2.getCell(4).setCellStyle(style);
				createMergedRegion(sheet, hssfWorkbook, entryRowNum, entryRowNum, 4, 5);
				row2.createCell(6).setCellValue(entry.getQuantity().doubleValue());
				row2.createCell(7).setCellValue(
						(StringUtils.isNotEmpty(product.getDeliveryUnit())) ? product.getDeliveryUnit() + " (" + product.getNumerator()
								+ product.getSalesUnit() + ")" : emptyField);
				row2.createCell(8).setCellValue(getShippingInfo(entry, orderDetails));
				sheet.setColumnWidth(8, 5000);
				sheet.autoSizeColumn(8);
				row2.setHeight((short) 1200);
				row2.getCell(6).setCellStyle(style);
				row2.getCell(7).setCellStyle(style);
				final CellStyle cellStyle = hssfWorkbook.createCellStyle();
				cellStyle.cloneStyleFrom(style);
				cellStyle.setAlignment(HorizontalAlignment.LEFT);
				row2.getCell(8).setCellStyle(cellStyle);
				row2.createCell(9).setCellValue(StringUtils.isNotEmpty(entry.getStatus()) ? entry.getStatus() : emptyField);
				row2.getCell(9).setCellStyle(style);
			}
			else if (orderDetails.getOrderType().equalsIgnoreCase("ZNC") || orderDetails.getOrderType().equalsIgnoreCase("ZKB"))
			{
				createMergedRegion(sheet, hssfWorkbook, entryRowNum, entryRowNum, 3, 4);
				row2.createCell(5).setCellValue(entry.getQuantity().doubleValue());
				row2.getCell(5).setCellStyle(style);
				createMergedRegion(sheet, hssfWorkbook, entryRowNum, entryRowNum, 5, 6);
				row2.createCell(7).setCellValue(
						(StringUtils.isNotEmpty(product.getDeliveryUnit())) ? product.getDeliveryUnit() + " (" + product.getNumerator()
								+ product.getSalesUnit() + ")" : emptyField);
				row2.getCell(7).setCellStyle(style);
				row2.createCell(8).setCellValue(getShippingInfo(entry, orderDetails));
				sheet.setColumnWidth(8, 5000);
				row2.setHeight((short) 1200);
				final CellStyle cellStyle = hssfWorkbook.createCellStyle();
				cellStyle.cloneStyleFrom(style);
				cellStyle.setAlignment(HorizontalAlignment.LEFT);
				row2.getCell(8).setCellStyle(cellStyle);
				row2.createCell(9).setCellValue(StringUtils.isNotEmpty(entry.getStatus()) ? entry.getStatus() : emptyField);
				row2.getCell(9).setCellStyle(style);
			}
			else if (orderDetails.getOrderType().equalsIgnoreCase("ZRE"))
			{
				row2.getCell(3).setCellStyle(style);
				row2.createCell(4).setCellValue(entry.getLotNumber());
				row2.getCell(4).setCellStyle(style);
				createMergedRegion(sheet, hssfWorkbook, entryRowNum, entryRowNum, 4, 5);
				row2.createCell(6).setCellValue(entry.getQuantity().doubleValue());
				row2.getCell(6).setCellStyle(style);
				row2.createCell(7).setCellValue(
						(StringUtils.isNotEmpty(product.getDeliveryUnit())) ? product.getDeliveryUnit() + " (" + product.getNumerator()
								+ product.getSalesUnit() + ")" : emptyField);
				row2.getCell(7).setCellStyle(style);
				createMergedRegion(sheet, hssfWorkbook, entryRowNum, entryRowNum, 7, 8);
				row2.createCell(9).setCellValue(StringUtils.isNotEmpty(entry.getStatus()) ? entry.getStatus() : emptyField);
				row2.getCell(9).setCellStyle(style);
			}
			else if (orderDetails.getOrderType().equalsIgnoreCase("ZDEL"))
			{
				row2.getCell(3).setCellStyle(style);
				row2.createCell(4).setCellValue(
						StringUtils.isNotEmpty(entry.getContractNumber()) ? entry.getContractNumber() : emptyField);
				row2.createCell(5).setCellValue(entry.getLotNumber());
				row2.createCell(6).setCellValue(entry.getSpecialStockPartner());
				row2.createCell(7).setCellValue(entry.getQuantity().doubleValue());
				row2.createCell(8).setCellValue(
						(StringUtils.isNotEmpty(product.getDeliveryUnit())) ? product.getDeliveryUnit() + " (" + product.getNumerator()
								+ product.getSalesUnit() + ")" : emptyField);
				sheet.setColumnWidth(8, 4000);
				row2.getCell(4).setCellStyle(style);
				row2.getCell(5).setCellStyle(style);
				row2.getCell(6).setCellStyle(style);
				row2.getCell(7).setCellStyle(style);
				row2.getCell(8).setCellStyle(style);
				row2.createCell(9).setCellValue(StringUtils.isNotEmpty(entry.getStatus()) ? entry.getStatus() : emptyField);
				row2.getCell(9).setCellStyle(style);

			}
			else
			{
				createMergedRegion(sheet, hssfWorkbook, entryRowNum, entryRowNum, 2, 3);
				if (SITE_NAME.CONS.toString().equals(siteName))
				{
					row2.createCell(4).setCellValue(
							"ID:" + product.getCode().replaceAll("^0+", StringUtils.EMPTY) + "\n" + "UPC:"
									+ (StringUtils.isNotEmpty(product.getUpc()) ? product.getUpc() : emptyField));
				}
				else
				{
					row2.createCell(4).setCellValue(
							"ID:" + product.getCode() + "\n" + "GTIN:"
									+ (StringUtils.isNotEmpty(product.getGtin()) ? product.getGtin() : emptyField));
				}
				createMergedRegion(sheet, hssfWorkbook, entryRowNum, entryRowNum, 4, 5);
				row2.createCell(6).setCellValue(entry.getQuantity().doubleValue());
				row2.getCell(6).setCellStyle(style);
				row2.createCell(7).setCellValue(
						(StringUtils.isNotEmpty(product.getDeliveryUnit())) ? product.getDeliveryUnit() + " (" + product.getNumerator()
								+ product.getSalesUnit() + ")" : emptyField);
				row2.getCell(7).setCellStyle(style);
				sheet.setColumnWidth(7, 4000);
				row2.createCell(8).setCellValue(StringUtils.isNotEmpty(entry.getStatus()) ? entry.getStatus() : emptyField);
				row2.getCell(8).setCellStyle(style);
				createMergedRegion(sheet, hssfWorkbook, entryRowNum, entryRowNum, 8, 9);
			}
			row2.createCell(10).setCellValue((entry.getBasePrice() != null) ? entry.getBasePrice().getFormattedValue() : emptyField);
			row2.createCell(11).setCellValue(
					(entry.getTotalPrice() != null) ? entry.getTotalPrice().getFormattedValue() : emptyField);
			row2.getCell(10).setCellStyle(style);
			row2.getCell(11).setCellStyle(style);
		}
		return entryRowNum;
	}


	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final String logoPath,int colLen)
	{
		InputStream inputStream = null;
		int index = 0;
		try
		{
			inputStream = new FileInputStream(logoPath);
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();
		}
		catch (final IOException ioException)
		{
			logger.error("Exception occured during input output operation in the method setHeaderImage()");
		}
		final CreationHelper helper = hssfWorkbook.getCreationHelper();
		final Drawing drawing = sheet.createDrawingPatriarch();
		final ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(1);
		anchor.setCol2(colLen);
		anchor.setRow1(1);
		anchor.setRow2(5);
		anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
		final Picture pict = drawing.createPicture(anchor, index);
		//pict.resize(0.85);
	}

	public CellStyle getStyleForTableHeader(final Workbook hssfWorkbook)
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
		style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return style;
	}

	public CellStyle getStyleForBorder(final Workbook hssfWorkbook)
	{
		final CellStyle style = hssfWorkbook.createCellStyle();
		style.setWrapText(true);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		return style;
	}

	public static void setBorder(final CellRangeAddress region, final Sheet sheet, final Workbook hssfWorkbook)
	{
		//RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet, hssfWorkbook);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);

	}

	public void populateBorders(final Sheet sheet, final Workbook hssfWorkbook, int row1Num, final int cellNum,
			final int cellspan, final boolean rightBorder)
	{
		row1Num = row1Num + 1;
		final CellRangeAddress reg = new CellRangeAddress(row1Num, 18, cellNum, (cellNum + (cellspan - 1)));
		sheet.addMergedRegion(reg);
		RegionUtil.setBorderLeft(BorderStyle.THIN, reg, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, reg, sheet);
		if (rightBorder)
		{
			RegionUtil.setBorderRight(BorderStyle.THIN, reg, sheet);
		}
	}

	public void createMergedRegion(final Sheet sheet, final Workbook hssfWorkbook, final int row1, final int row2,
			final int col1, final int col2)
	{
		final CellRangeAddress reg = new CellRangeAddress(row1, row2, col1, col2);
		sheet.addMergedRegion(reg);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg, sheet);
		setBorder(reg, sheet, hssfWorkbook);
	}

	protected static void createCellForOrderHeaderInfo(final Sheet sheet, final Workbook hssfWorkbook, final int rowIndex,
			final int cellNum, final String key, final String value, final int margingCell, final boolean rightBorder)
	{
		final Font font = hssfWorkbook.createFont();
		font.setBold(true);
		final CellStyle style = hssfWorkbook.createCellStyle();
		style.setWrapText(true);
		final CellStyle style1 = hssfWorkbook.createCellStyle();
		style1.setWrapText(true);
		Row row = sheet.getRow(rowIndex);
		if (null == row)
		{
			row = sheet.createRow(rowIndex);
		}
		row.createCell(cellNum + margingCell + 1).setCellValue(value);
		final CellRangeAddress reg3 = new CellRangeAddress(rowIndex, rowIndex, cellNum, cellNum + margingCell);
		sheet.addMergedRegion(reg3);
		row.createCell(cellNum).setCellValue(key);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);
		style.setBorderLeft(BorderStyle.THIN);
		row.getCell(cellNum).setCellStyle(style);
		if (rightBorder)
		{
			style1.setBorderRight(BorderStyle.THIN);
		}
		row.getCell(cellNum + margingCell + 1).setCellStyle(style1);
		sheet.autoSizeColumn(cellNum);
		sheet.autoSizeColumn(cellNum + margingCell + 1);

	}

	protected int populateOrderSpecificInformation(final Sheet sheet, final Workbook hssfWorkbook,
			final JnjGTOrderData orderDetails, int rownum)
	{
		if (orderDetails.getOrderType().equalsIgnoreCase("ZNC"))
		{
			rownum = rownum + 1;
			createCellForOrderHeaderInfo(sheet, hssfWorkbook, rownum, 1,
					messageSource.getMessage(CORDIS_HOUSE_ACCOUNT, null, i18nService.getCurrentLocale()),
					(orderDetails.getCordisHouseAccount() != null) ? orderDetails.getCordisHouseAccount() : NOT_AVAILABLE, 1, false);
		}
		if (orderDetails.getOrderType().equalsIgnoreCase("ZDEL"))
		{
			rownum = rownum + 1;
			createCellForOrderHeaderInfo(sheet, hssfWorkbook, rownum, 1,
					messageSource.getMessage(SPINE_SALES_REP_UCN, null, i18nService.getCurrentLocale()),
					(orderDetails.getSpineSalesRepUCN() != null) ? orderDetails.getSpineSalesRepUCN() : NOT_AVAILABLE, 1, false);
		}
		if (orderDetails.getOrderType().equalsIgnoreCase("ZIO2") || orderDetails.getOrderType().equalsIgnoreCase("ZTOR"))
		{
			rownum = rownum + 1;
			createCellForOrderHeaderInfo(sheet, hssfWorkbook, rownum, 1,
					messageSource.getMessage(SALES_TERRITORY, null, i18nService.getCurrentLocale()),
					(orderDetails.getSpineSalesRepUCN() != null) ? orderDetails.getSpineSalesRepUCN() : NOT_AVAILABLE, 1, false);
		}
		return rownum;

	}
	
	
	

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
	{
		return jnjCommonFacadeUtil;
	}

	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
	{
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}
    

	protected void populateSurgeryData(final SurgeryInfoData surgeryInfoData, final Map<String, String> surgeryDataMap)
	{

		if (surgeryInfoData != null && !StringUtils.isEmpty(surgeryInfoData.getZone()))
		{
			surgeryDataMap.put("surgeryInfoZone",
					getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.zone." + surgeryInfoData.getZone(), null, ""));
		}
		else
		{
			surgeryDataMap.put("surgeryInfoZone", NOT_AVAILABLE);
		}

		if (surgeryInfoData != null && !StringUtils.isEmpty(surgeryInfoData.getPathology()))
		{
			surgeryDataMap.put(
					"surgeryInfoPathology",
					getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.pathology." + surgeryInfoData.getPathology(), null,
							""));
		}
		else
		{
			surgeryDataMap.put("surgeryInfoPathology", NOT_AVAILABLE);
		}

		if (surgeryInfoData != null && !StringUtils.isEmpty(surgeryInfoData.getLevelsInstrumented()))
		{
			surgeryDataMap.put(
					"surgeryInfoLevelsInstrumented",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.levelsInstrumented." + surgeryInfoData.getLevelsInstrumented(), null, ""));
		}
		else
		{
			surgeryDataMap.put("surgeryInfoLevelsInstrumented", NOT_AVAILABLE);
		}

		if (surgeryInfoData != null && !StringUtils.isEmpty(surgeryInfoData.getInterbodyFusion()))
		{
			surgeryDataMap.put(
					"surgeryInfoInterbodyFusion",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.interbodyFusion." + surgeryInfoData.getInterbodyFusion(), null, ""));
		}
		else
		{
			surgeryDataMap.put("surgeryInfoInterbodyFusion", NOT_AVAILABLE);
		}

		if (surgeryInfoData != null && !StringUtils.isEmpty(surgeryInfoData.getInterbody()))
		{
			surgeryDataMap.put(
					"surgeryInfoInterbody",
					getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.interbody." + surgeryInfoData.getInterbody(), null,
							""));
		}
		else
		{
			surgeryDataMap.put("surgeryInfoInterbody", NOT_AVAILABLE);
		}

		if (surgeryInfoData != null && !StringUtils.isEmpty(surgeryInfoData.getSurgicalApproach()))
		{
			surgeryDataMap.put(
					"surgeryInfoSurgicalApproach",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.surgicalApproach." + surgeryInfoData.getSurgicalApproach(), null, ""));
		}
		else
		{
			surgeryDataMap.put("surgeryInfoSurgicalApproach", NOT_AVAILABLE);
		}

		if (surgeryInfoData != null && !StringUtils.isEmpty(surgeryInfoData.getOrthobiologics()))
		{
			surgeryDataMap.put(
					"surgeryInfoOrthobiologics",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.orthobiologics." + surgeryInfoData.getOrthobiologics(), null, ""));
		}
		else
		{
			surgeryDataMap.put("surgeryInfoOrthobiologics", NOT_AVAILABLE);
		}

		if (surgeryInfoData != null && !StringUtils.isEmpty(surgeryInfoData.getSurgerySpecialty()))
		{
			surgeryDataMap.put(
					"surgeryInfoSurgerySpecialty",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.surgerySpeciality." + surgeryInfoData.getSurgerySpecialty(), null, ""));
		}
		else
		{
			surgeryDataMap.put("surgeryInfoSurgerySpecialty", NOT_AVAILABLE);
		}

	}

	protected static String getFormattedAddress(final AddressData addressData, final boolean isBillingAddress,
			final JnjGTOrderData jnjGTOrderData, final String siteName)
	{
		String addressField = new String();
		if (null != addressData)
		{
			if (isBillingAddress)
			{

				if (SITE_NAME.CONS.toString().equals(siteName))
				{
					if (StringUtils.isNotEmpty(addressData.getFirstName()))
					{
						addressField = addressField.concat(addressData.getFirstName()
								+ (StringUtils.isEmpty(addressData.getLastName()) ? "" : " " + addressData.getLastName()) + " \n");
					}
				}
				else
				{
					if (StringUtils.isNotEmpty(jnjGTOrderData.getB2bUnitName()))
					{
						addressField = addressField.concat(jnjGTOrderData.getB2bUnitName() + " \n");
					}
					else
					{
						if (StringUtils.isNotEmpty(addressData.getFirstName()))
						{
							addressField = addressField.concat(addressData.getFirstName()
									+ (StringUtils.isEmpty(addressData.getLastName()) ? "" : " " + addressData.getLastName()) + " \n");
						}
					}
				}
				//AAOL-5691
				if (StringUtils.isNotEmpty(addressData.getLine1()))
				{
					addressField = addressField.concat(addressData.getLine1() + ", ");
				}
				if (StringUtils.isNotEmpty(addressData.getLine2()))
				{
					addressField = addressField.concat(addressData.getLine2() + " \n");
				}
				if (StringUtils.isNotEmpty(addressData.getTown()))
				{
					addressField = addressField.concat(addressData.getTown() + " , ");
				}
				if (addressData.getRegion() != null)
				{
					if (StringUtils.isNotEmpty(addressData.getRegion().getName()))
					{
						addressField = addressField.concat(addressData.getRegion().getName() + " ");
					}
				}
			}
			else
			{
				if (SITE_NAME.CONS.toString().equals(siteName))
				{
					if (StringUtils.isNotEmpty(addressData.getCompanyName()))
					{
						addressField = addressField.concat(addressData.getCompanyName() + " \n");
					}
				}
				else
				{
					if (StringUtils.isNotEmpty(addressData.getFirstName()))
					{
						addressField = addressField.concat(addressData.getFirstName()
								+ (StringUtils.isEmpty(addressData.getLastName()) ? "" : " " + addressData.getLastName()) + " \n");
					}
				}
				//AAOL-5691
				if (StringUtils.isNotEmpty(addressData.getLine1()))
				{
					addressField = addressField.concat(addressData.getLine1() + ", ");
				}
				if (StringUtils.isNotEmpty(addressData.getLine2()))
				{
					addressField = addressField.concat(addressData.getLine2() + " \n");
				}
				if (StringUtils.isNotEmpty(addressData.getTown()))
				{
					addressField = addressField.concat(addressData.getTown() + " , ");
				}
				if (addressData.getRegion() != null)
				{
					if (StringUtils.isNotEmpty(addressData.getRegion().getName()))
					{
						addressField = addressField.concat(addressData.getRegion().getName() + " ");
					}
				}
			}
			if (StringUtils.isNotEmpty(addressData.getPostalCode()))
			{
				if (addressData.getPostalCode().length() == 9)
				{
					addressField = addressField.concat(StringUtils.substring(addressData.getPostalCode(), 0, 5) + "-"
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

	protected int getSizeOfDeliveryInformation(final JnjGTOrderData jnjGTOrderData)
	{
		int size = 1;
		if (jnjGTOrderData.getPackingListDetails() != null && jnjGTOrderData.getPackingListDetails().size() > 1
				&& size < jnjGTOrderData.getPackingListDetails().size())
		{
			size = jnjGTOrderData.getPackingListDetails().size();
		}
		if (jnjGTOrderData.getCarrier() != null && jnjGTOrderData.getCarrier().size() > 1
				&& size < jnjGTOrderData.getCarrier().size())
		{
			size = jnjGTOrderData.getCarrier().size();
		}
		if (jnjGTOrderData.getBillOfLading() != null && jnjGTOrderData.getBillOfLading().size() > 1
				&& size < jnjGTOrderData.getBillOfLading().size())
		{
			size = jnjGTOrderData.getBillOfLading().size();
		}
		if (jnjGTOrderData.getTrackingIdList() != null && jnjGTOrderData.getTrackingIdList().size() > 1
				&& size < jnjGTOrderData.getTrackingIdList().size())
		{
			size = jnjGTOrderData.getTrackingIdList().size();
		}
		return size;

	}

	protected String getShippingInfo(final JnjGTOrderEntryData orderEntryData, final JnjGTOrderData jnjGTOrderData)
	{//Modified by Archana for AAOL-5513
		final DateFormat df2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		String shippingInfo = "Shipping :";
		if (orderEntryData.getShippingMethod() != null)
		{
			shippingInfo = shippingInfo + orderEntryData.getShippingMethod();
		}
		else
		{
			shippingInfo = shippingInfo + " ";
		}
		shippingInfo = shippingInfo + "\n" + "Tracking: ";
		if (jnjGTOrderData.getShippingTrackingInfo() != null)
		{
			final Set<JnjGTShippingTrckInfoData> shippingTrckInfoDatas = jnjGTOrderData.getShippingTrackingInfo().get(
					orderEntryData.getSapOrderlineNumber());
			if (shippingTrckInfoDatas != null)
			{
				for (final JnjGTShippingTrckInfoData data : shippingTrckInfoDatas)
				{
					if (data.getTrackingId() != null)
					{
						shippingInfo = shippingInfo + "\n" + data.getTrackingId();
					}
				}
			}
		}
		else
		{
			shippingInfo = shippingInfo + " ";
		}
		shippingInfo = shippingInfo + "\n" + "Ship Date: ";
		if (orderEntryData.getExpectedShipDate() != null)
		{
			shippingInfo = shippingInfo + df2.format(orderEntryData.getExpectedShipDate());
		}
		else
		{
			shippingInfo = shippingInfo + " ";
		}
		shippingInfo = shippingInfo + "\n" + "Delivery Date: ";
		if (orderEntryData.getExpectedDeliveryDate() != null)
		{
			shippingInfo = shippingInfo + df2.format(orderEntryData.getExpectedDeliveryDate());
		}
		else
		{
			shippingInfo = shippingInfo + " ";
		}
		return shippingInfo;
	}

	protected List<Date> getShipDate(final JnjGTOrderData jnjGTOrderData)
	{
		final List<Date> shipDateList = new ArrayList<Date>();
		if (!jnjGTOrderData.getShippingTrackingInfo().isEmpty())
		{
			final Collection<Set<JnjGTShippingTrckInfoData>> shippingTrckInfoList = jnjGTOrderData.getShippingTrackingInfo()
					.values();
			for (final Set<JnjGTShippingTrckInfoData> shippingTrckInfoSet : shippingTrckInfoList)
			{
				for (final JnjGTShippingTrckInfoData shippingTrckInfo : shippingTrckInfoSet)
				{

					shipDateList.add(shippingTrckInfo.getShipDate());
				}
			}

		}
		return shipDateList;
	}
 
	protected List<Date> getDeliveryDate(final JnjGTOrderData jnjGTOrderData)
	{
		final List<Date> deliveryDateList = new ArrayList<Date>();
		if (!jnjGTOrderData.getShippingTrackingInfo().isEmpty())
		{
			final Collection<Set<JnjGTShippingTrckInfoData>> shippingTrckInfoList = jnjGTOrderData.getShippingTrackingInfo()
					.values();
			for (final Set<JnjGTShippingTrckInfoData> shippingTrckInfoSet : shippingTrckInfoList)
			{
				for (final JnjGTShippingTrckInfoData shippingTrckInfo : shippingTrckInfoSet)
				{
					deliveryDateList.add(shippingTrckInfo.getDeliveryDate());
				}
			}

		}
		return deliveryDateList;
	}
	
}
