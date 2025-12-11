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
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.core.data.JnjGTInventoryReportResponseData;

import de.hybris.platform.servicelayer.i18n.I18NService;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTInventoryReportForm;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * This class handles the excel download for inventory report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTInventoryReportExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTInventoryReportExcelView.class);
	private static final String INVENTORY_RESPONSE_DATA_LIST = "jnjGTInventoryReportResponseDataList";
	private static final String INVENTORY_FORM_NAME = "jnjGTInventoryReportForm";
	
	private static final String REP_UCN = "reports.inventory.rep.ucn";
	private static final String PRODUCT_CODE = "reports.inventory.product.code";
	private static final String LOT_NUMBER = "reports.inventory.product.lot.number";
	private static final String ZERO_STOCKS = "reports.inventory.zero.stocks";
	private static final String HEADER = "reports.inventory.header";
	private static final String UCN_NUMBER = "reports.inventory.table.ucn.number";
	private static final String DESC = "reports.inventory.table.desc";
	private static final String UNRESTRICTED = "reports.inventory.table.unrestricted";
	private static final String RESTRICTED = "reports.inventory.table.restricted";
	private static final String STOCK = "reports.inventory.table.quality.stock";
	private static final String TOTAL_QTY = "reports.inventory.table.total.quantity";
	private static final String UNIT = "reports.inventory.table.unit";

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
	
	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		try
		{
			arg3.setHeader("Content-Disposition", "attachment; filename=Inventory_Report.xls");
			final List<JnjGTInventoryReportResponseData> jnjGTInventoryReportResponseDataList = (List<JnjGTInventoryReportResponseData>) arg0
					.get(INVENTORY_RESPONSE_DATA_LIST);

			final JnjGTInventoryReportForm searchCriteria = (JnjGTInventoryReportForm) arg0.get(INVENTORY_FORM_NAME);
			final Sheet searchSheet = arg1.createSheet("Inventory Report Search Criteria");
			final Row searchHeader = searchSheet.createRow(6);
			setHeaderImage(arg1, searchSheet, (String) arg0.get("siteLogoPath"));
			searchHeader.createCell(0).setCellValue(messageSource.getMessage(REP_UCN, null, i18nService.getCurrentLocale()));
			searchHeader.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			searchHeader.createCell(2).setCellValue(messageSource.getMessage(LOT_NUMBER, null, i18nService.getCurrentLocale()));
			searchHeader.createCell(3).setCellValue(messageSource.getMessage(ZERO_STOCKS, null, i18nService.getCurrentLocale()));
			final Row searchRow = searchSheet.createRow(7);
			HashSet<String> ucn=new HashSet<String>(Arrays.asList(searchCriteria.getRepUCNs().split("\\s*(=>|,|\\s)\\s*")));
			 ucn.remove("");			
			searchRow.createCell(0).setCellValue(StringUtils.join(ucn,','));
			searchRow.createCell(1).setCellValue(searchCriteria.getProductCode());
			searchRow.createCell(2).setCellValue(searchCriteria.getLotNumber());
			if (searchCriteria.isDisplayZeroStocks())
			{
				searchRow.createCell(3).setCellValue("Yes");
			}
			else
			{
				searchRow.createCell(3).setCellValue("No");
			}

			final Sheet sheet = arg1.createSheet(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale()));
			final Row header = sheet.createRow(0);
			header.createCell(0).setCellValue(messageSource.getMessage(UCN_NUMBER, null, i18nService.getCurrentLocale()));
			header.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			header.createCell(2).setCellValue(messageSource.getMessage(DESC, null, i18nService.getCurrentLocale()));
			header.createCell(3).setCellValue(messageSource.getMessage(LOT_NUMBER, null, i18nService.getCurrentLocale()));
			header.createCell(4).setCellValue(messageSource.getMessage(UNRESTRICTED, null, i18nService.getCurrentLocale()));
			header.createCell(5).setCellValue(messageSource.getMessage(RESTRICTED, null, i18nService.getCurrentLocale()));
			header.createCell(6).setCellValue(messageSource.getMessage(STOCK, null, i18nService.getCurrentLocale()));
			header.createCell(7).setCellValue(messageSource.getMessage(TOTAL_QTY, null, i18nService.getCurrentLocale()));
			header.createCell(8).setCellValue(messageSource.getMessage(UNIT, null, i18nService.getCurrentLocale()));

			int rowNum = 1;
			if (null != jnjGTInventoryReportResponseDataList)
			{
				for (final JnjGTInventoryReportResponseData jnjGTInventoryReportResponseData : jnjGTInventoryReportResponseDataList)
				{
					final Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(jnjGTInventoryReportResponseData.getUcnNumber());
					row.createCell(1).setCellValue(jnjGTInventoryReportResponseData.getProductCode());
					row.createCell(2).setCellValue(jnjGTInventoryReportResponseData.getDescription());
					row.createCell(3).setCellValue(jnjGTInventoryReportResponseData.getLotNumber());
					row.createCell(4).setCellValue(jnjGTInventoryReportResponseData.getUnrestricted());
					row.createCell(5).setCellValue(jnjGTInventoryReportResponseData.getRestricted());
					row.createCell(6).setCellValue(jnjGTInventoryReportResponseData.getQualityStock());
					row.createCell(7).setCellValue(jnjGTInventoryReportResponseData.getTotalQty());
					row.createCell(8).setCellValue(jnjGTInventoryReportResponseData.getUnit());
				}
			}
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
		anchor.setCol2(10);
		anchor.setRow1(1);
		anchor.setRow2(5);
		anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
		final Picture pict = drawing.createPicture(anchor, index);
			

	}
	
	protected static CellStyle createLabelCell(HSSFWorkbook workbook){

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
	protected static CellStyle createValueCell(HSSFWorkbook workbook){

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
