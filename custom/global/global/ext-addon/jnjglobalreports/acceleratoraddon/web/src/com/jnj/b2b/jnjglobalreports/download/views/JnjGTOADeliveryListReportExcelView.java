
/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTOADeliveryListReportResponseData;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTOADeliveryListReportForm;

import de.hybris.platform.servicelayer.i18n.I18NService;
import com.jnj.services.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * AAOL-4603: This class handles the Excel download for multiple purchase analysis report
 * 
 * @author Cognizant
 * @version 1.0
 */
public class JnjGTOADeliveryListReportExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTOADeliveryListReportExcelView.class);
	private static final String OA_DELIVERY_LIST_RESPONSE_DATA_LIST = "jnjGTOADeliveryListReportResponseDataList";
	private static final String OA_DELIVERY_LIST_FORM_NAME = "jnjGlobalOADeliveryListReportForm";
	//private static final String TOTAL_SPENDING_MAP = "totalSpendingMap";//TODO REMOVE
	//private static final String SEARCH_TOTAL = "searchTotal";//TODO REMOVE
	
	private static final String DL_HEADER="reports.oa.pdf.header.deliverylist";
	private static final String SEARCH_CRITERIA = "DELIVERY LIST REPORT RESULTS";
	private static final String START_DATE = "reports.backorder.excel.from.start"; 
	private static final String END_DATE = "reports.backorder.excel.to.start"; 
	private static final String ORDERTYPE = "cart.common.orderType"; 
	private static final String SALES_DOC_NUM = "reports.financial.table.product.salesDocumentNumber"; 
	private static final String LINE_ITEM = "reports.financial.table.product.lineItem"; 
	private static final String CUST_PO_NUM = "reports.financial.table.product.customerPoNo"; 
	private static final String DELIVERY_NUM = "reports.order.analysis.delivery.No"; 
	private static final String DELIVERY_ITEM_NUM = "reports.order.analysis.delivery.itemno"; 
	private static final String ACTUAL_SHIP_DATE = "reports.order.analysis.actualShipDate"; 
	private static final String FRANCHISE_DESC	 = "reports.financial.table.product.franchiseDescription"; 
	private static final String PROD_CODE = "reports.financial.xls.product.code"; 
	private static final String PROD_DESC = "reports.financial.table.product.desc"; 
	private static final String UOM = "reports.order.analysis.UOM"; 
	private static final String DELIVERY_QTY = "reports.order.analysis.delivery.qty"; 
	private static final String ORDER_QTY = "reports.order.analysis.order.quantity"; 
	private static final String BATCH_NUM = "delivery.xls.batchNo"; 
	private static final String BATCH_EXPIRY_DATE = "reports.oa.batchExpDate"; 
	private static final String SERIAL_NUM = "delivery.xls.serialNo"; 
	private static final String POD_DATE = "reports.oa.podDate";
	private static final String POD_TIME = "reports.oa.podTime";
	private static final String SHIP_TO_ACC = "cart.deliver.shipToAcc"; 
	private static final String SHIP_TO_NAME = "reports.backorder.table.shipTo"; 
	private static final String CONNOTE = "reports.oa.connote";
	private static final String HEADER = "delivery.reports.header";
	private static final String DOWNLOADDATE = "backorder.reports.download.date";
	private static final String ACC_NUMBER = "backorder.reports.account.number";
	private static final String ACC_NAME = "backorder.reports.accountname";
	private static final String FROM =  "backorder.reports.from";
	private static final String TO =  "backorder.reports.to";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	
	private MessageService messageService;
	/** I18NService to retrieve the current locale. */
	private I18NService i18nService;
	private MessageSource messageSource;
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	
	/**
	 * @return the jnjCommonUtil
	 */
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	/**
	 * @param jnjCommonUtil the jnjCommonUtil to set
	 */
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
	public MessageService getMessageService()
	{
		return messageService;
	}

	public void setMessageService(final MessageService messageService)
	{
		this.messageService = messageService;
	}
	
	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		try
		{
			 String emptyField = new String();
		     emptyField="";
			arg3.setHeader("Content-Disposition", "attachment; filename=Delivery List.xls");
			final JnjGTOADeliveryListReportForm formData = (JnjGTOADeliveryListReportForm) map
					.get(OA_DELIVERY_LIST_FORM_NAME);
			//TODO REMOVE final Map<String, Double> totalSpendingMap = (Map<String, Double>) map.get(TOTAL_SPENDING_MAP);
			//TODO REMOVE final PriceData totalAmountSpend = (PriceData) map.get(SEARCH_TOTAL);
			final List<JnjGTOADeliveryListReportResponseData> dataList = (List<JnjGTOADeliveryListReportResponseData>) map
					.get(OA_DELIVERY_LIST_RESPONSE_DATA_LIST);
			final String accountsSelectedName = (String) map.get("accountreportname");
			
			final Sheet searchSheet = hssfWorkbook.createSheet((messageSource.getMessage(SEARCH_CRITERIA, null, i18nService.getCurrentLocale())).toUpperCase());
			final Row searchHeader = searchSheet.createRow(6);//TODO - 6 row??
			setHeaderImage(hssfWorkbook, searchSheet, (String) map.get("siteLogoPath"));
			final Row header0 = searchSheet.createRow(6);
			final Row header3 = searchSheet.createRow(9);
			final Row header4 = searchSheet.createRow(10);
			final Row header5 = searchSheet.createRow(11);
			
			final Font font = hssfWorkbook.createFont();
			font.setBold(true);
			
			final CellStyle style = hssfWorkbook.createCellStyle();
			style.setFont(font);
			
			final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			final Date date = new Date();
			
			header0.createCell(0).setCellValue(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale()));
			header0.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			
			header3.createCell(0).setCellValue(messageSource.getMessage(DOWNLOADDATE, null, i18nService.getCurrentLocale()));
			header3.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			header3.createCell(1).setCellValue(dateFormat.format(date));
			header4.createCell(0).setCellValue(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()));
			header4.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			header5.createCell(0).setCellValue(messageSource.getMessage(ACC_NAME, null, i18nService.getCurrentLocale()));
			header5.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			
			header4.createCell(1).setCellValue((formData.getAccountIds() != null) ? formData.getAccountIds() : emptyField);
			header4.getCell(1).setCellStyle(style);
			
			
			header5.createCell(1).setCellValue((accountsSelectedName != null) ? accountsSelectedName : emptyField);
			/*searchSheet.autoSizeColumn(0);
			searchSheet.autoSizeColumn(1);
			searchSheet.autoSizeColumn(2);
			searchSheet.autoSizeColumn(3);
			searchSheet.autoSizeColumn(4);
			searchSheet.autoSizeColumn(5);
			searchSheet.autoSizeColumn(6);
			searchSheet.autoSizeColumn(7);*/
			
			/*final HSSFSheet sheet = hssfWorkbook.createSheet(messageSource.getMessage(DL_HEADER, null, i18nService.getCurrentLocale()).toUpperCase());
			setHeaderImage(hssfWorkbook, sheet, (String) map.get("siteLogoPath"));*/
			final Row header = searchSheet.createRow(17);
			
			header.createCell(0).setCellValue(messageSource.getMessage(ORDERTYPE, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(1).setCellValue(messageSource.getMessage(SALES_DOC_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(2).setCellValue(messageSource.getMessage(LINE_ITEM, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(3).setCellValue(messageSource.getMessage(CUST_PO_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(4).setCellValue(messageSource.getMessage(DELIVERY_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(5).setCellValue(messageSource.getMessage(DELIVERY_ITEM_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(6).setCellValue(messageSource.getMessage(ACTUAL_SHIP_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(7).setCellValue(messageSource.getMessage(FRANCHISE_DESC, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(8).setCellValue(messageSource.getMessage(PROD_CODE, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(9).setCellValue(messageSource.getMessage(PROD_DESC, null, i18nService.getCurrentLocale()));
			header.getCell(9).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(10).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
			header.getCell(10).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(11).setCellValue(messageSource.getMessage(DELIVERY_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(11).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(12).setCellValue(messageSource.getMessage(ORDER_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(12).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(13).setCellValue(messageSource.getMessage(BATCH_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(13).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(14).setCellValue(messageSource.getMessage(BATCH_EXPIRY_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(14).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(15).setCellValue(messageSource.getMessage(SERIAL_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(15).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(16).setCellValue(messageSource.getMessage(POD_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(16).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(17).setCellValue(messageSource.getMessage(POD_TIME, null, i18nService.getCurrentLocale()));
			header.getCell(17).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(18).setCellValue(messageSource.getMessage(SHIP_TO_ACC, null, i18nService.getCurrentLocale()));
			header.getCell(18).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(19).setCellValue(messageSource.getMessage(SHIP_TO_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(19).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(20).setCellValue(messageSource.getMessage(CONNOTE, null, i18nService.getCurrentLocale()));
			header.getCell(20).setCellStyle(createLabelCell(hssfWorkbook));

			
			CellStyle css = createValueCell(hssfWorkbook);
			int rowNum = 18;
			if (null != dataList)
			{
				for (final JnjGTOADeliveryListReportResponseData data : dataList)
				{
					searchSheet.autoSizeColumn(rowNum);
					LOG.error("set size for delivery search results before cell added for "+rowNum);
					final Row row = searchSheet.createRow(rowNum++);
					Cell cell = row.createCell(0);
					cell.setCellValue(new HSSFRichTextString(data.getOrderType()));
					cell.setCellStyle(css);
					cell = row.createCell(1);
					cell.setCellValue(new HSSFRichTextString(data.getSalesDocNum()));
					cell.setCellStyle(css);
					cell = row.createCell(2);
					cell.setCellValue(new HSSFRichTextString(data.getLineItem()));
					cell.setCellStyle(css);
					cell = row.createCell(3);
					cell.setCellValue(new HSSFRichTextString(data.getCustPONum()));
					cell.setCellStyle(css);
					cell = row.createCell(4);
					cell.setCellValue(new HSSFRichTextString(data.getDeliveryNum()));
					cell.setCellStyle(css);
					cell = row.createCell(5);
					cell.setCellValue(new HSSFRichTextString(data.getDeliveryItemNum()));
					cell.setCellStyle(css);
					cell = row.createCell(6);
					cell.setCellValue(new HSSFRichTextString(data.getActualShipmentDate()));
					cell.setCellStyle(css);
					cell = row.createCell(7);
					cell.setCellValue(new HSSFRichTextString(data.getFranchiseDesc()));
					cell.setCellStyle(css);
					cell = row.createCell(8);
					cell.setCellValue(new HSSFRichTextString(data.getProductCode()));
					cell.setCellStyle(css);
					cell = row.createCell(9);
					cell.setCellValue(new HSSFRichTextString(data.getProductDesc()));
					cell.setCellStyle(css);
					cell = row.createCell(10);
					cell.setCellValue(new HSSFRichTextString(data.getUom()));
					cell.setCellStyle(css);					
					cell = row.createCell(11);
					cell.setCellValue(new HSSFRichTextString(data.getDeliveryQuantity()+""));
					cell.setCellStyle(css);
					cell = row.createCell(12);
					cell.setCellValue(new HSSFRichTextString(data.getOrderQuantity()));
					cell.setCellStyle(css);
					cell = row.createCell(13);
					cell.setCellValue(new HSSFRichTextString(data.getBatchNum()));
					cell.setCellStyle(css);
					cell = row.createCell(14);
					cell.setCellValue(new HSSFRichTextString(data.getBatchExpiryDate()));
					cell.setCellStyle(css);
					cell = row.createCell(15);
					cell.setCellValue(new HSSFRichTextString(data.getSerialNum()));
					cell.setCellStyle(css);					
					cell = row.createCell(16);
					cell.setCellValue(new HSSFRichTextString(data.getPodDate()));
					cell.setCellStyle(css);
					cell = row.createCell(17);
					cell.setCellValue(new HSSFRichTextString(data.getPodDate()));
					cell.setCellStyle(css);
					cell = row.createCell(18);
					cell.setCellValue(new HSSFRichTextString(data.getShipToAccount()));
					cell.setCellStyle(css);
					cell = row.createCell(19);
					cell.setCellValue(new HSSFRichTextString(data.getShipToName()));
					cell.setCellStyle(css);
					cell = row.createCell(20);
					cell.setCellValue(new HSSFRichTextString(data.getConnote()));
					cell.setCellStyle(css);				
					
				}
			}
			searchSheet.autoSizeColumn(0);
			searchSheet.autoSizeColumn(1);
			searchSheet.autoSizeColumn(2);
			searchSheet.autoSizeColumn(3);
			searchSheet.autoSizeColumn(4);
			searchSheet.autoSizeColumn(5);
			searchSheet.autoSizeColumn(6);
			searchSheet.autoSizeColumn(7);
			searchSheet.autoSizeColumn(8);
			searchSheet.autoSizeColumn(9);
			searchSheet.autoSizeColumn(10);
			searchSheet.autoSizeColumn(11);
			searchSheet.autoSizeColumn(12);
			searchSheet.autoSizeColumn(13);
			searchSheet.autoSizeColumn(14);
			searchSheet.autoSizeColumn(15);
			searchSheet.autoSizeColumn(16);
			searchSheet.autoSizeColumn(17);
			searchSheet.autoSizeColumn(18);
			searchSheet.autoSizeColumn(19);
			searchSheet.autoSizeColumn(20);
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating excel - " + exception.getMessage());
		}
	}
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final String logoPath)
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
		anchor.setCol1(0);
		anchor.setCol2(10);//TODO is it for logo??
		anchor.setRow1(1);
		anchor.setRow2(5);
		anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
		final Picture pict = drawing.createPicture(anchor, index);
			

	}
	
	protected static CellStyle createLabelCell(Workbook workbook){

	    CellStyle cellStyle = workbook.createCellStyle();
	    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());

	    cellStyle.setBorderBottom(BorderStyle.THIN);

	    cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

	    cellStyle.setBorderLeft(BorderStyle.THIN);

	    cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

	    cellStyle.setBorderRight(BorderStyle.THIN);

	    cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

	    cellStyle.setBorderTop(BorderStyle.THIN);

	    cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

	    return cellStyle;

	    }
	protected static CellStyle createValueCell(Workbook workbook){

	    CellStyle cellStyle = workbook.createCellStyle();

	    cellStyle.setBorderBottom(BorderStyle.THIN);

	    cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

	    cellStyle.setBorderLeft(BorderStyle.THIN);

	    cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

	    cellStyle.setBorderRight(BorderStyle.THIN);

	    cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

	    cellStyle.setBorderTop(BorderStyle.THIN);

	    cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

	    return cellStyle;

	    }
}
