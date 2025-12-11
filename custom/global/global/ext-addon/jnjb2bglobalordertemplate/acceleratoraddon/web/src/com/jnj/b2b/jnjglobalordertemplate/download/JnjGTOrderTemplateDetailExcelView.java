/*
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
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.jnj.facades.data.JnjGTOrderTemplateEntryData;

import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * This class is used to create the excel for the Order template Details
 * 
 * @author Accenture
 * @version 1.0
 */
	public class JnjGTOrderTemplateDetailExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTOrderTemplateDetailExcelView.class);
	private static final String DATA_LIST = "orderTemplateDetail";
	
	private static final String PRODUCT_NAME = "template.detail.productname";
	private static final String PRODUCT_CODE = "template.detail.productcode";
	private static final String DESCRIPTION = "template.detail.description";
	private static final String PRODUCT_VOLUME = "template.detail.volume";
	private static final String PRODUCT_WEIGHT = "template.detail.weight";
	private static final String PRODUCT_QUANTITY = "template.detail.quantity";
	private static final String HEADER =  "template.order.detail.header";
	
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
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		try
		{
			arg3.setHeader("Content-Disposition", "attachment; filename=Template_Detail.xls");
			final List<JnjGTOrderTemplateEntryData> orderTemplateDatas = (List<JnjGTOrderTemplateEntryData>) map.get(DATA_LIST);

			final Sheet sheet = hssfWorkbook.createSheet(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale()));
			final Row header = sheet.createRow(6);
			setHeaderImage(hssfWorkbook, sheet, (String) map.get("siteLogoPath"));
			header.createCell(0).setCellValue(messageSource.getMessage(PRODUCT_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(2).setCellValue(messageSource.getMessage(DESCRIPTION, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(3).setCellValue(messageSource.getMessage(PRODUCT_VOLUME, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(4).setCellValue(messageSource.getMessage(PRODUCT_WEIGHT, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(5).setCellValue(messageSource.getMessage(PRODUCT_QUANTITY, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));
			CellStyle css = createValueCell(hssfWorkbook);
			int rowNum = 7;
			if (null != orderTemplateDatas)
			{
				for (final JnjGTOrderTemplateEntryData orderTemplateData : orderTemplateDatas)
				{
					final String deliveryUnit = orderTemplateData.getRefVariant().getDeliveryUnit() == null ? Jnjb2bCoreConstants.SPACE
							: orderTemplateData.getRefVariant().getDeliveryUnit();
					final String numerator = orderTemplateData.getRefVariant().getNumerator() + Jnjb2bCoreConstants.SPACE
							+ orderTemplateData.getRefVariant().getSalesUnit() == null ? Jnjb2bCoreConstants.SPACE : orderTemplateData
							.getRefVariant().getNumerator()
							+ Jnjb2bCoreConstants.SPACE
							+ orderTemplateData.getRefVariant().getSalesUnit();


					final Row row = sheet.createRow(rowNum++);
					
					Cell cell0 = row.createCell(0);
					cell0.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getName()));
					cell0.setCellStyle(css);
					
					Cell cell1 = row.createCell(1);
					cell1.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getCode()));
					cell1.setCellStyle(css);
					Cell cell2 = row.createCell(2);
					cell2.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getDescription()));
					cell2.setCellStyle(css);
					Cell cell3 = row.createCell(3);
					cell3.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getProductVolume()));
					cell3.setCellStyle(css);
					
					System.out.println(orderTemplateData.getRefVariant().getProductVolume()+"sdffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
					
					Cell cell4= row.createCell(4);
					cell4.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getProductWeight()));
					cell4.setCellStyle(css);
					System.out.println(orderTemplateData.getRefVariant().getProductWeight()+"sdffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
					Cell cell5 = row.createCell(5);
					if (StringUtils.isEmpty(numerator))
					{
					        cell5.setCellValue(new HSSFRichTextString(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE + deliveryUnit));
					}
					else
					{
					    cell5.setCellValue(new HSSFRichTextString(
							orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE
							+ orderTemplateData.getRefVariant().getDeliveryUnit() + "("
							+ orderTemplateData.getRefVariant().getNumerator() + Jnjb2bCoreConstants.SPACE
							+ orderTemplateData.getRefVariant().getSalesUnit() + ")"));
					}
					
					  cell5.setCellValue(new HSSFRichTextString(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE + orderTemplateData.getRefVariant().getDeliveryUnit()
							+ "(" + numerator + ")"));
					cell5.setCellStyle(css);
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
