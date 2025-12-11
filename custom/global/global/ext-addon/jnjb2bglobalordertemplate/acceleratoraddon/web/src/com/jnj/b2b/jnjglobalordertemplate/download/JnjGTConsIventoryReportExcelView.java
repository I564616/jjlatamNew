/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalordertemplate.download;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTBackorderReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTConsignmentInventoryReportForm;

import de.hybris.platform.servicelayer.i18n.I18NService;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * This class handles the excel download for back-order report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTConsIventoryReportExcelView extends AbstractXlsView
{
	protected static final Logger LOG = Logger.getLogger(JnjGTConsIventoryReportExcelView.class);
	protected static final String CONSIGNMENT_INVENTORY_RESPONSE_DATA_LIST = "consInventoryReportList";
	protected static final String CONSIGNMENT_INV_FORM_NAME = "jnjGlobalConsignmentInventoryReportForm";
	private static final String HEADER = "conInv.reports.header";
	private static final String DOWNLOADDATE = "backorder.reports.download.date";
	private static final String ACC_NUMBER = "backorder.reports.account.number";
	private static final String ACC_NAME = "backorder.reports.accountname";
	private static final String STOCK_LOC_ACC = "cart.common.stock.user";
	private static final String STOCK_LOC_NAME = "cart.common.stock.user.name";
	private static final String FRANCHISE_DESC = "reports.financial.table.product.franchiseDescription";
	private static final String PRODUCT_CODE = "cutReport.product.code.label";
	private static final String PRODUCT_DESC = "reports.financial.table.product.desc";
	private static final String CURRENT_QTY = "consignment.quantity.in.Stock";
	private static final String AGREED_QTY = "consignment.quantity.agreed.level";
	private static final String AVALIABLE_QTY = "consignment.quantity.available";
	private static final String UOM = "reports.order.analysis.UOM";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	
	@Autowired
	private I18NService i18nService;
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

	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		try
		{
			 String emptyField = new String();
		     emptyField="";
			arg3.setHeader("Content-Disposition", "attachment; filename=Consignment Inventory Report.xls");
			
			final JnjGTConsignmentInventoryReportForm formData = (JnjGTConsignmentInventoryReportForm) arg0
					.get(CONSIGNMENT_INV_FORM_NAME);
			final List<JnjGTConsInventoryData> jnjGTConsIneventoryReportDataList = (List<JnjGTConsInventoryData>) arg0
					.get(CONSIGNMENT_INVENTORY_RESPONSE_DATA_LIST);
			final String accountsSelectedName = (String) arg0.get("accountreportname");
			//final HSSFSheet searchSheet = HSSFWorkbook.createSheet((messageSource.getMessage(SEARCH_CRITERIA, null, i18nService.getCurrentLocale())).toUpperCase());
			final Sheet sheet = arg1.createSheet("Consignment Inventory Report");
			
			final Row searchHeader = sheet.createRow(6);//TODO - 6 row??
			setHeaderImage(arg1, sheet, (String) arg0.get("siteLogoPath"));
			final Row header0 = sheet.createRow(6);
			final Row header3 = sheet.createRow(9);
			final Row header4 = sheet.createRow(10);
			final Row header5 = sheet.createRow(11);
			
			final Font font = arg1.createFont();
			font.setBold(true);
			
			final CellStyle style = arg1.createCellStyle();
			style.setFont(font);
			
			final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			final Date date = new Date();
			
			header0.createCell(0).setCellValue(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale()));
			header0.getCell(0).setCellStyle(createLabelCell(arg1));
			
			header3.createCell(0).setCellValue(messageSource.getMessage(DOWNLOADDATE, null, i18nService.getCurrentLocale()));
			header3.getCell(0).setCellStyle(createLabelCell(arg1));
			header3.createCell(1).setCellValue(dateFormat.format(date));
			header4.createCell(0).setCellValue(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()));
			header4.getCell(0).setCellStyle(createLabelCell(arg1));
			header5.createCell(0).setCellValue(messageSource.getMessage(ACC_NAME, null, i18nService.getCurrentLocale()));
			header5.getCell(0).setCellStyle(createLabelCell(arg1));
			
			header4.createCell(1).setCellValue((formData.getAccountIds() != null) ? formData.getAccountIds() : emptyField);
			header4.getCell(1).setCellStyle(style);
			
			
			header5.createCell(1).setCellValue((accountsSelectedName != null) ? accountsSelectedName : emptyField);
			
			final Row header = sheet.createRow(17);
			header.createCell(0).setCellValue(messageSource.getMessage(STOCK_LOC_ACC, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(arg1));
			header.createCell(1).setCellValue(messageSource.getMessage(STOCK_LOC_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(arg1));
			header.createCell(2).setCellValue(messageSource.getMessage(FRANCHISE_DESC, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(arg1));
			header.createCell(3).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(arg1));
			header.createCell(4).setCellValue(messageSource.getMessage(PRODUCT_DESC, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(arg1));
			header.createCell(5).setCellValue(messageSource.getMessage(CURRENT_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(arg1));
			header.createCell(6).setCellValue(messageSource.getMessage(AGREED_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(arg1));
			header.createCell(7).setCellValue(messageSource.getMessage(AVALIABLE_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(arg1));
			header.createCell(8).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(arg1));
			
			int rowNum = 18;
			if (null != jnjGTConsIneventoryReportDataList)
			{
				double totals = 0;
				for (final JnjGTConsInventoryData jnjGTConsIneventoryReportData : jnjGTConsIneventoryReportDataList)
				{
					final Row row = sheet.createRow(rowNum++);
					sheet.autoSizeColumn(rowNum);
					row.createCell(0).setCellValue(jnjGTConsIneventoryReportData.getStockLocationAcc());
					row.createCell(1).setCellValue(jnjGTConsIneventoryReportData.getStockLocationName());
					row.createCell(3).setCellValue(jnjGTConsIneventoryReportData.getFranchiseDescription());
					row.createCell(2).setCellValue(jnjGTConsIneventoryReportData.getProductCode());					
					row.createCell(4).setCellValue(jnjGTConsIneventoryReportData.getProductDesc());
					row.createCell(5).setCellValue(jnjGTConsIneventoryReportData.getQtyInStock()) ;
					row.createCell(6).setCellValue(jnjGTConsIneventoryReportData.getParLevelQty());
					row.createCell(7).setCellValue(jnjGTConsIneventoryReportData.getAvailableOrderQty());
					row.createCell(8).setCellValue(jnjGTConsIneventoryReportData.getUom());
					
			}
		}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating excel - " + exception.getMessage());
		}
		
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
}
