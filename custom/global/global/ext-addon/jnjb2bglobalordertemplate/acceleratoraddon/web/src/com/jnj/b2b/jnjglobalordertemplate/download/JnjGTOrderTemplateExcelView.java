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
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTOrderTemplateData;

import de.hybris.platform.servicelayer.i18n.I18NService;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;


import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * This class handles the excel download for order template list
 *
 * @author balinder.singh
 * @version 1.0
 */
public class JnjGTOrderTemplateExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTOrderTemplateExcelView.class);
	protected static final String ORDER_TEMPLATE_DATA_LIST = "orderTemplate";
	protected static final String EMPTY_CELL = " ";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	private static final String ORDER_LIST = "Template.order.list";
	private static final String TEMPLATE_NAME = "template.name";
	private static final String TEMPLATE_NUMBER = "template.number";
	private static final String TEMPLATE_AUTHOR = "template.author.text";
	private static final String TEMPLATE_CREATED = "template.Created.text";
	private static final String SHARESTATUS = "template.sharestatus.text";
	private static final String LINES = "template.order.lines";
	
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

	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook, final HttpServletRequest arg2,
			final HttpServletResponse arg3) throws Exception
	{
		try
		{
			arg3.setHeader("Content-Disposition", "attachment; filename=" + Jnjb2bCoreConstants.TemplateSearch.EXCEL_FILE_NAME
					+ ".xls");
			final List<JnjGTOrderTemplateData> jnjGTOrderTemplateDataList = (List<JnjGTOrderTemplateData>) map.get(ORDER_TEMPLATE_DATA_LIST);
			final Sheet sheet = hssfWorkbook.createSheet(messageSource.getMessage(ORDER_LIST, null, i18nService.getCurrentLocale()));
			final Row header = sheet.createRow(6);
			setHeaderImage(hssfWorkbook, sheet, (String) map.get("siteLogoPath"));
			header.createCell(0).setCellValue(messageSource.getMessage(TEMPLATE_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(1).setCellValue(messageSource.getMessage(TEMPLATE_NUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(2).setCellValue(messageSource.getMessage(TEMPLATE_AUTHOR, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(3).setCellValue(messageSource.getMessage(TEMPLATE_CREATED, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(4).setCellValue(messageSource.getMessage(SHARESTATUS, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(5).setCellValue(messageSource.getMessage(LINES, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));
			final SimpleDateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			CellStyle css = createValueCell(hssfWorkbook);

			int rowNum = 7;
			if (null != jnjGTOrderTemplateDataList)
			{
				for (final JnjGTOrderTemplateData jnjGTOrderTemplateData : jnjGTOrderTemplateDataList)
				{
					final Row row = sheet.createRow(rowNum++);

					Cell cell0 = row.createCell(0);
					cell0.setCellValue(new HSSFRichTextString(jnjGTOrderTemplateData.getTemplateName()));
					cell0.setCellStyle(css);

					
					Cell cell1 = row.createCell(1);
					cell1.setCellValue(new HSSFRichTextString(jnjGTOrderTemplateData.getTemplateNumber()));
					cell1.setCellStyle(css);
					
					Cell cell2 = row.createCell(2);
					cell2.setCellValue(new HSSFRichTextString(jnjGTOrderTemplateData.getAuthor()));
					cell2.setCellStyle(css);
					   Cell cell3 = row.createCell(3);
					if (jnjGTOrderTemplateData.getCreatedOn() != null)
					{ 
					    cell3.setCellValue(new HSSFRichTextString(dateFormatter.format(jnjGTOrderTemplateData.getCreatedOn())));
					}
					else
					{
					    cell3.setCellValue(new HSSFRichTextString(EMPTY_CELL));
					}
					 cell3.setCellStyle(css);
					
                        		    Cell cell4 = row.createCell(4);
                        		    cell4.setCellValue(new HSSFRichTextString(jnjGTOrderTemplateData.getShareStatus()));
                        		    cell4.setCellStyle(css);
                        		    Cell cell5 = row.createCell(5);
                        		    cell5.setCellValue(new HSSFRichTextString(jnjGTOrderTemplateData.getLines().floatValue() + ""));
                        		    cell5.setCellStyle(css);
					int count = 1;
					count++;
				}
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				sheet.autoSizeColumn(4);
				sheet.autoSizeColumn(5);

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
