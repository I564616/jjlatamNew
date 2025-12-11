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
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTSinglePurchaseOrderReportResponseData;

import de.hybris.platform.servicelayer.i18n.I18NService;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTSinglePurchaseAnalysisReportForm;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * This class handles the Excel download for single purchase analysis report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTSinglePurchaseAnalysisReportExcelView extends AbstractXlsView
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
	
	
	private static final Logger LOG = Logger.getLogger(JnjGTSinglePurchaseAnalysisReportExcelView.class);
	private static final String SINGLE_PURCHASE_ANALYSIS_RESPONSE_DATA_MAP = "jnjGTSinglePurchaseOrderReportResponseDataMap";
	private static final String SINGLE_PURCHASE_ANALYSIS_FORM_NAME = "jnjGTSinglePurchaseAnalysisReportForm";
	
	private static final String ACCOUNT = "reports.purchase.analysis.account"; 
	private static final String PRODUCT_CODE = "reports.purchase.analysis.productcode"; 
	private static final String BREAKDOWN = "reports.purchase.analysis.period.breakdown"; 
	private static final String STARTDATE = "reports.purchase.analysis.startdate"; 
	private static final String ENDDATE = "reports.purchase.analysis.enddate"; 
	private static final String ORDERFROM = "reports.purchase.analysis.orderdFrom"; 
	private static final String LOTNUM = "reports.purchase.analysis.lotNum";
	private static final String ACCOUNTNUMBER = "reports.purchase.analysis.account.number"; 
	private static final String PERIOD = "reports.purchase.analysis.period"; 
	private static final String AMOUNTELECTRONIC = "reports.purchase.analysis.amountElectronic"; 
	private static final String AMOUNTOTHER = "reports.purchase.analysis.amountOther"; 
	private static final String QTYELECTRONIC = "reports.purchase.analysis.QuantityElectronic"; 
	private static final String QTYOTHER = "reports.purchase.analysis.QuantityOther"; 
	private static final String QTYUNIT = "reports.purchase.analysis.QuantityUnit";
	private static final String FREQUENCYELCTRONIC = "reports.purchase.analysis.FrequencyElectronic"; 
	private static final String FREQUENCYOTHER = "reports.purchase.analysis.FrequencyOther";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		try
		{
			arg3.setHeader("Content-Disposition", "attachment; filename=Single_Purchase_Analysis_Report.xls");
			final JnjGTSinglePurchaseAnalysisReportForm formData = (JnjGTSinglePurchaseAnalysisReportForm) map
					.get(SINGLE_PURCHASE_ANALYSIS_FORM_NAME);
			final TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData> dataMap = (TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>) map.get(SINGLE_PURCHASE_ANALYSIS_RESPONSE_DATA_MAP);
			final SimpleDateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());//Modified by Archana for AAOL-5513
			final Sheet searchSheet = hssfWorkbook.createSheet("Search Criteria");
			final Row searchHeader = searchSheet.createRow(6);
			setHeaderImage(hssfWorkbook, searchSheet, (String) map.get("siteLogoPath"));
			searchHeader.createCell(0).setCellValue(messageSource.getMessage(ACCOUNT, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(2).setCellValue(messageSource.getMessage(BREAKDOWN, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(3).setCellValue(messageSource.getMessage(STARTDATE, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(4).setCellValue(messageSource.getMessage(ENDDATE, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(5).setCellValue(messageSource.getMessage(ORDERFROM, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(6).setCellValue(messageSource.getMessage(LOTNUM, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(6).setCellStyle(createLabelCell(hssfWorkbook));
			CellStyle css = createValueCell(hssfWorkbook);

			if (null != formData)
			{
				final Row searchRow = searchSheet.createRow(7);
				Cell cell0 = searchRow.createCell(0);
				cell0.setCellValue(new HSSFRichTextString(formData.getAccountIds()));
				cell0.setCellStyle(css);
				Cell cell1 = searchRow.createCell(1);
				cell1.setCellValue(new HSSFRichTextString(formData.getProductCode()));
				cell1.setCellStyle(css);
				Cell cell2 = searchRow.createCell(2);
				cell2.setCellValue(new HSSFRichTextString(formData.getPeriodBreakdown()));
				cell2.setCellStyle(css);
				Cell cell3 = searchRow.createCell(3);
				cell3.setCellValue(new HSSFRichTextString(formData.getStartDate()));//Modified by Archana for AAOL-5513
				cell3.setCellStyle(css);
				Cell cell4 = searchRow.createCell(4);
				cell4.setCellValue(new HSSFRichTextString(formData.getEndDate()));//Modified by Archana for AAOL-5513
				cell4.setCellStyle(css);
				Cell cell5 = searchRow.createCell(5);
				cell5.setCellValue(new HSSFRichTextString(formData.getOrderedFrom()));
				cell5.setCellStyle(css);
				Cell cell6 = searchRow.createCell(6);
				cell6.setCellValue(new HSSFRichTextString(formData.getLotNumber()));
				cell6.setCellStyle(css);
			}

			final Sheet sheet = hssfWorkbook.createSheet("Single Purchase Analysis Report");
			
			final Row header = sheet.createRow(7);
			setHeaderImage(hssfWorkbook, sheet, (String) map.get("siteLogoPath"));
			header.createCell(0).setCellValue(messageSource.getMessage(ACCOUNTNUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(1).setCellValue(messageSource.getMessage(PERIOD, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(2).setCellValue(messageSource.getMessage(AMOUNTELECTRONIC, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(3).setCellValue(messageSource.getMessage(AMOUNTOTHER, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(4).setCellValue(messageSource.getMessage(QTYELECTRONIC, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(5).setCellValue(messageSource.getMessage(QTYOTHER, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(6).setCellValue(messageSource.getMessage(QTYUNIT, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(7).setCellValue(messageSource.getMessage(FREQUENCYELCTRONIC, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(8).setCellValue(messageSource.getMessage(FREQUENCYOTHER, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(hssfWorkbook));
			int rowNum = 8;
			if (null != dataMap)
			{
				for (final String key : dataMap.keySet())
				{
					final Row row = sheet.createRow(rowNum++);
					
					Cell cell0 = row.createCell(0);
					cell0.setCellValue(new HSSFRichTextString(dataMap.get(key).getAccountNumber()));
					cell0.setCellStyle(css);
					Cell cell1 = row.createCell(1);
					cell1.setCellValue(new HSSFRichTextString(dataMap.get(key).getPeriodFrom() + Jnjb2bCoreConstants.SPACE + dataMap.get(key).getPeriodTo()));
					cell1.setCellStyle(css);
					Cell cell2 = row.createCell(2);
					cell2.setCellValue(new HSSFRichTextString(dataMap.get(key).getAmountElectronic().getFormattedValue()));
					cell2.setCellStyle(css);
					Cell cell3 = row.createCell(3);
					cell3.setCellValue(new HSSFRichTextString(dataMap.get(key).getAmountOther().getFormattedValue()));
					cell3.setCellStyle(css);
					Cell cell4 = row.createCell(4);
					cell4.setCellValue(new HSSFRichTextString(String.valueOf(dataMap.get(key).getQuantityElectronic())));
					cell4.setCellStyle(css);
					Cell cell5 = row.createCell(5);
					cell5.setCellValue(new HSSFRichTextString(String.valueOf(dataMap.get(key).getQuantityOther())));
					cell5.setCellStyle(css);
					Cell cell6 = row.createCell(6);
					cell6.setCellValue(new HSSFRichTextString(dataMap.get(key).getQuantityUnit()));
					cell6.setCellStyle(css);
					Cell cell7 = row.createCell(7);
					cell7.setCellValue(new HSSFRichTextString(dataMap.get(key).getFrequencyElectronic()+""));
					cell7.setCellStyle(css);
					Cell cell8 = row.createCell(8);
					cell8.setCellValue(new HSSFRichTextString(dataMap.get(key).getFrequencyOther()+""));
					cell8.setCellStyle(css);
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
