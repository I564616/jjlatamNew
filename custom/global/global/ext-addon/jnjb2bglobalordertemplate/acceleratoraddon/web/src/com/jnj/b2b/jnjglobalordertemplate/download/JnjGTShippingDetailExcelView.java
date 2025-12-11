package com.jnj.b2b.jnjglobalordertemplate.download;


import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateWebConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.product.converters.populator.JnjGTProductBasicPopulator.SITE_NAME;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTShippingDetailExcelView extends AbstractXlsView
{
	protected static final Logger LOG = Logger.getLogger(JnjGTShippingDetailExcelView.class);
	protected static final String sheetName = "Delivery_Packing List";
	private static final String EMPTY_FIELD = "Not Available";

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workbook,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{

		final JnjGTOrderData orderDetails = (JnjGTOrderData) map
				.get("orderData");
		
		final String siteName = (String) map.get(Jnjb2bCoreConstants.SITE_NAME);
		//final JnjGTOrderData orderDetails = (JnjGTOrderData) map.get("orderData");
		final String fileName = Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.PACKING_LIST_FILENAME + ".xls";
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);
	
		final Sheet sheet = workbook.createSheet(sheetName);
		final CellStyle headerStyle = getStyleForTableHeader(workbook);
		int rowNumber = 1;

		final Row row = sheet.createRow(rowNumber++);

		final Font font = workbook.createFont();
		font.setBold(true);
		
		final CellStyle styleCell = workbook.createCellStyle();
		styleCell.setWrapText(true);
		
		final CellStyle style = workbook.createCellStyle();
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
	 
		int row1Num=3;
		int column1Num=1;
		final Row row7 = sheet.createRow(row1Num);
		row7.createCell(column1Num).setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.HEADER_DATA);
		row7.getCell(column1Num).setCellStyle(headerStyle);
		int row2Num=3;
		int column2Num=13;
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		
		row1Num = row1Num + 1;
		row2Num = row2Num + 2;
		column1Num = 1;
		column2Num = 1;
		sheet.createRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.SOLD_TO_ACCOUNT);
		//sheet.getRow(row1Num).setHeight((short) 650);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.SHIP_TO_NAME);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.SHIP_TO_ADDRESS);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.DELIVERY_NUMBER);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.DELIVERY_DATE);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.ORDER_NUMBER);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.CUSTOMER_PO);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.NO_CARTONS);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.TOTAL_WEIGHT);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.SHIPPING_INSTRUCTIONS);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.OPERTAION_DATE);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.PATIENT_ID);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.SURGEON);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		
		int rowNum = 6;
		int cellNum = 3;
		int cellSpan = 2;
		boolean border = true;
		if (null != orderDetails)
		{
				sheet.autoSizeColumn(rowNum);
				final Row row1 = sheet.createRow(rowNum++);
				
				row1.setRowStyle(styleCell);
				row1.createCell(1).setCellValue(orderDetails.getSoldToAccount());
				row1.createCell(2).setCellValue(orderDetails.getDeliveryAddress().getFirstName() + orderDetails.getDeliveryAddress().getLastName() );
				//row1.createCell(2).setCellValue(orderDetails.getDeliveryAddress());
				/*row1.createCell(3).setCellValue(*/
				/*createCellForOrderHeaderInfo(
						sheet,
						workbook,
						rowNum,
						cellNum,
						ControllerConstants.JnjGTExcelPdfViewLabels.OrderDetail.SHIP_TO_ADDRESS1,
						(orderDetails.getDeliveryAddress() != null) ? getFormattedAddress(orderDetails.getDeliveryAddress(), false,
								orderDetails, siteName) : EMPTY_FIELD, cellSpan, border);*/
				row1.createCell(3).setCellValue(orderDetails.getDeliveryAddress().getFirstName() + orderDetails.getDeliveryAddress().getLastName() + orderDetails.getDeliveryAddress().getLine1() + orderDetails.getDeliveryAddress().getLine2()
						  + orderDetails.getDeliveryAddress().getTown() + orderDetails.getDeliveryAddress().getPostalCode());
				//row1.createCell(4).setCellValue(orderDetails.getShipmentLocation());
				//row1.createCell(5).setCellValue(orderDetails.getShipmentLocation());
				row1.createCell(6).setCellValue(orderDetails.getOrderNumber());
				row1.createCell(7).setCellValue(orderDetails.getCustomerPoNumber());
				row1.createCell(10).setCellValue(orderDetails.getShippingInstructions());
			}
		sheet.autoSizeColumn(3);
		
	
		

		row1Num=10;
		column1Num=1;
		final Row row8 = sheet.createRow(row1Num);
		row8.createCell(column1Num).setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.ITEM_DATA);
		row8.getCell(column1Num).setCellStyle(headerStyle);
		row2Num=10;
		column2Num=9;
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);	
		row1Num = row1Num + 1;
		row2Num = row2Num + 2;
		column1Num = 1;
		column2Num = 1;
		sheet.createRow(row1Num).createCell(column1Num)
		.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.LINE_NO);
		//sheet.getRow(row1Num).setHeight((short) 650);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.PRODUCT_CODE);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.PRODUCT_DESCRIPTION);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.BATCH_SPLIT_QTY);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.UOM);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.UOM_CONVERSION);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.BATCH_SERIAL);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.EXPIRY_DATE);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		column1Num = column1Num + 1;
		column2Num = column2Num + 1;
		sheet.getRow(row1Num).createCell(column1Num)
				.setCellValue(Jnjb2bglobalordertemplateWebConstants.JnjGTExcelPdfViewLabels.ShippingDetailList.TOTAL_QTY);
		sheet.getRow(row1Num).getCell(column1Num).setCellStyle(headerStyle);
		createMergedRegion(sheet, workbook, row1Num, row2Num, column1Num, column2Num);
		
		rowNum = 13;
		if (null != orderDetails){
			List<OrderEntryData> LOE = orderDetails.getEntries();
			JnjGTOrderEntryData oe=null;
			//JnjGTProductData productData=null;
			for (int i=0;i<LOE.size();i++){
				oe=(JnjGTOrderEntryData) LOE.get(i);
				sheet.autoSizeColumn(rowNum);
				
				final Row row1 = sheet.createRow(rowNum++);
				row1.setRowStyle(styleCell);
				row1.createCell(1).setCellValue(oe.getSapOrderlineNumber());
				row1.createCell(2).setCellValue(oe.getProduct().getCode());
				row1.createCell(3).setCellValue(oe.getProduct().getDescription());
				row1.createCell(5).setCellValue(oe.getVolumeUOM());
				row1.createCell(7).setCellValue(oe.getBatchNumber() + oe.getSerialNumber());
				sheet.autoSizeColumn(i+1);	
		}
		}
		
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
	public void createMergedRegion(final Sheet sheet, final Workbook hssfWorkbook, final int row1, final int row2,
			final int col1, final int col2)
	{
		final CellRangeAddress reg = new CellRangeAddress(row1, row2, col1, col2);
		sheet.addMergedRegion(reg);
		RegionUtil.setBorderTop(BorderStyle.THIN, reg, sheet);
		setBorder(reg, sheet, hssfWorkbook);
	}
	public static void setBorder(final CellRangeAddress region, final Sheet sheet, final Workbook hssfWorkbook)

	{

	//HSSFRegionUtil.setBorderTop(BorderStyle.THIN, region, sheet, hssfWorkbook);

	RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);

	RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);

	RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);


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
					addressField = addressField.concat(addressData.getTown() + " ");
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
					addressField = addressField.concat(addressData.getTown() + " ");
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

	protected static void createCellForOrderHeaderInfo(final HSSFSheet sheet, final HSSFWorkbook hssfWorkbook, final int rowIndex,
			final int cellNum, final String key, final String value, final int margingCell, final boolean rightBorder)
	{
		final HSSFFont font = hssfWorkbook.createFont();
		font.setBold(true);
		final HSSFCellStyle style = hssfWorkbook.createCellStyle();
		style.setWrapText(true);
		final HSSFCellStyle style1 = hssfWorkbook.createCellStyle();
		style1.setWrapText(true);
		HSSFRow row = sheet.getRow(rowIndex);
		if (null == row)
		{
			row = sheet.createRow(rowIndex);
		}
		row.createCell(cellNum).setCellValue(value);
		final CellRangeAddress reg3 = new CellRangeAddress(rowIndex, rowIndex, cellNum, cellNum);
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
		row.getCell(cellNum).setCellStyle(style1);
		sheet.autoSizeColumn(cellNum);
		sheet.autoSizeColumn(cellNum);

	}
	
	
}