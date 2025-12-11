/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.download.views;

import de.hybris.platform.commercefacades.product.data.PriceData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

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
import com.jnj.facades.data.JnjGTMultiPurchaseOrderReportResponseData;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTMultiPurchaseAnalysisReportForm;
import de.hybris.platform.servicelayer.i18n.I18NService;
import com.jnj.services.MessageService;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * This class handles the Excel download for multiple purchase analysis report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTMultiPurchaseAnalysisReportExcelView extends AbstractXlsView
{
	private MessageService messageService;
	/** I18NService to retrieve the current locale. */

	private static final Logger LOG = Logger.getLogger(JnjGTMultiPurchaseAnalysisReportExcelView.class);
	private static final String MULTI_PURCHASE_ANALYSIS_RESPONSE_DATA_LIST = "jnjGTMultiPurchaseOrderReportResponseDataList";
	private static final String MULTI_PURCHASE_ANALYSIS_FORM_NAME = "jnjGlobalMultiPurchaseAnalysisReportForm";
	private static final String TOTAL_SPENDING_MAP = "totalSpendingMap";
	private static final String SEARCH_TOTAL = "searchTotal";
	private static final String ACCOUNT = "multireports.account";
	private static final String TERRITORY = "multireports.territory";
	private static final String STARTDATE = "multireports.startdate";
	private static final String ENDDATE = "multireports.enddate";
	private static final String BRAND = "multireports.brand";
	private static final String SUBBRAND = "multireports.subbrand";
	private static final String ORDEREDFROM =  "multireports.orderedFrom";
	private static final String PRODUCTSDISPLAY = "multireports.ProductsToDisplay";
	private static final String ANALYSISVARIABLE = "multireports.Analysisvariable";
	private static final String SEARCHHEADER = "multireports.search.header";
	private static final String ACCOUNTNUMBER = "multireports.account.number";
	private static final String PRODUCTNAME = "multireports.product.name";
	private static final String PRODUCTCODE = "multireports.product.code";
	private static final String AMOUNT =  "multireports.product.amount";
	private static final String SPENDING = "multireports.spending";
	private static final String QUANTITY = "multireports.product.orderQuantity";
	private static final String FREQUENCY = "multireports.product.orderFrequency";
	private static final String WEIGHT = "multireports.product.order.weight";
	private static final String SPENT =  "multireports.product.total.spent";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	
	
	@Autowired
	protected I18NService i18nService;
	
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
	
	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
	
	
	public MessageService getMessageService()
	{
		return messageService;
	}

	public void setMessageService(final MessageService messageService)
	{
		this.messageService = messageService;
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
			arg3.setHeader("Content-Disposition", "attachment; filename=Multi_Product_Purchase_Analysis_Report.xls");
			final JnjGTMultiPurchaseAnalysisReportForm formData = (JnjGTMultiPurchaseAnalysisReportForm) map
					.get(MULTI_PURCHASE_ANALYSIS_FORM_NAME);
			final Map<String, Double> totalSpendingMap = (Map<String, Double>) map.get(TOTAL_SPENDING_MAP);
			final PriceData totalAmountSpend = (PriceData) map.get(SEARCH_TOTAL);
			final List<JnjGTMultiPurchaseOrderReportResponseData> dataList = (List<JnjGTMultiPurchaseOrderReportResponseData>) map
					.get(MULTI_PURCHASE_ANALYSIS_RESPONSE_DATA_LIST);
			final SimpleDateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());//Modified by Archana for AAOL-5513
			final Sheet searchSheet = hssfWorkbook.createSheet("Search Criteria");
			final Row searchHeader = searchSheet.createRow(6);
			setHeaderImage(hssfWorkbook, searchSheet, (String) map.get("siteLogoPath"));
			searchHeader.createCell(0).setCellValue(messageSource.getMessage(ACCOUNT, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(1).setCellValue(messageSource.getMessage(TERRITORY, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(2).setCellValue(messageSource.getMessage(STARTDATE, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(3).setCellValue(messageSource.getMessage(ENDDATE, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(4).setCellValue(messageSource.getMessage(BRAND, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(5).setCellValue(messageSource.getMessage(SUBBRAND, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(6).setCellValue(messageSource.getMessage(ORDEREDFROM, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(6).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(7).setCellValue(messageSource.getMessage(PRODUCTSDISPLAY, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(7).setCellStyle(createLabelCell(hssfWorkbook));
			searchHeader.createCell(8).setCellValue(messageSource.getMessage(ANALYSISVARIABLE, null, i18nService.getCurrentLocale()));
			searchHeader.getCell(8).setCellStyle(createLabelCell(hssfWorkbook));
			CellStyle css = createValueCell(hssfWorkbook);

			if (null != formData)
			{
				final Row searchRow = searchSheet.createRow(7);
				
				Cell cell0 = searchRow.createCell(0);
				cell0.setCellValue(new HSSFRichTextString(formData.getAccountIds()));
				cell0.setCellStyle(css);
				Cell cell1 = searchRow.createCell(1);
				cell1.setCellValue(new HSSFRichTextString(formData.getTerritory()));
				cell1.setCellStyle(css);
				Cell cell2 = searchRow.createCell(2);
				cell2.setCellValue(new HSSFRichTextString(formData.getStartDate()));//Modified by Archana for AAOL-5513
				cell2.setCellStyle(css);
				Cell cell3 = searchRow.createCell(3);
				cell3.setCellValue(new HSSFRichTextString(formData.getEndDate()));//Modified by Archana for AAOL-5513
				cell3.setCellStyle(css);
				Cell cell4 = searchRow.createCell(4);
				cell4.setCellValue(new HSSFRichTextString(getMessageService().getMessageForCode(formData.getOperatingCompany(),
				        getI18nService().getCurrentLocale())));
				cell4.setCellStyle(css);
				Cell cell5 = searchRow.createCell(5);
				cell5.setCellValue(new HSSFRichTextString(formData.getFranchiseDivCode()));
				cell5.setCellStyle(css);
				Cell cell6 = searchRow.createCell(6);
				cell6.setCellValue(new HSSFRichTextString(getMessageService().getMessageForCode(formData.getOrderedFrom(),
				        getI18nService().getCurrentLocale())));
				cell6.setCellStyle(css);
				Cell cell7 = searchRow.createCell(7);
				cell7.setCellValue(new HSSFRichTextString(getMessageService().getMessageForCode(formData.getProductsToDisplay(),
				        getI18nService().getCurrentLocale())));
				cell7.setCellStyle(css);
				
				searchRow.createCell(8).setCellValue(getMessageService().getMessageForCode(formData.getAnalysisVariable(),
				        getI18nService().getCurrentLocale()));
				
			}


			final Sheet sheet = hssfWorkbook.createSheet("Multi-Product Purchase Analysis Report");
			setHeaderImage(hssfWorkbook, sheet, (String) map.get("siteLogoPath"));
			final Row header = sheet.createRow(6);
			
			header.createCell(0).setCellValue(messageSource.getMessage(ACCOUNTNUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(1).setCellValue(messageSource.getMessage(PRODUCTNAME, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(2).setCellValue(messageSource.getMessage(PRODUCTCODE, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(3).setCellValue(messageSource.getMessage(AMOUNT, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(4).setCellValue(messageSource.getMessage(SPENDING, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(5).setCellValue(messageSource.getMessage(QUANTITY, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(6).setCellValue(messageSource.getMessage(FREQUENCY, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(7).setCellValue(messageSource.getMessage(WEIGHT, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(hssfWorkbook));
			/*CellStyle css = createValueCell(hssfWorkbook);*/
			int rowNum = 7;
			if (null != dataList)
			{
				for (final JnjGTMultiPurchaseOrderReportResponseData data : dataList)
				{
					final DecimalFormat decimalFormat = new DecimalFormat("#.##");
					final double percentSpending = (data.getAmount().getValue().doubleValue() / totalSpendingMap.get(
							data.getAccountNumber()).doubleValue()) * 100;
					if (!Double.isNaN(percentSpending))
					{
						data.setPercentageSpending(Double.valueOf(decimalFormat.format(percentSpending)).doubleValue());
					}
					final Row row = sheet.createRow(rowNum++);
					
					Cell cell0 = row.createCell(0);
					cell0.setCellValue(new HSSFRichTextString(data.getAccountNumber()));
					cell0.setCellStyle(css);
					Cell cell1 = row.createCell(1);
					cell1.setCellValue(new HSSFRichTextString(data.getProductName()));
					cell1.setCellStyle(css);
					Cell cell2 = row.createCell(2);
					String productCode= new String();
					if(data.getProductCode()!= null){
						productCode=data.getProductCode();	
					}
					if(data.getProductGTIN() != null){
						productCode=productCode+Jnjb2bCoreConstants.SPACE+data.getProductGTIN();
					}
					if(data.getProductUPC() != null){
						productCode=productCode+Jnjb2bCoreConstants.SPACE+data.getProductUPC();	
					}
					cell2.setCellValue(new HSSFRichTextString(productCode));
					cell2.setCellStyle(css);
					Cell cell3 = row.createCell(3);
					cell3.setCellValue(new HSSFRichTextString(data.getAmount().getFormattedValue()));
					cell3.setCellStyle(css);
					Cell cell4 = row.createCell(4);
					cell4.setCellValue(new HSSFRichTextString(data.getPercentageSpending()+""));
					cell4.setCellStyle(css);
					Cell cell5 = row.createCell(5);
					cell5.setCellValue(new HSSFRichTextString(data.getUnitQuantity() + data.getUom()));
					cell5.setCellStyle(css);
					Cell cell6 = row.createCell(6);
					cell6.setCellValue(new HSSFRichTextString(data.getOrderFrequency()+""));
					cell6.setCellStyle(css);
					
					Cell cell7 = row.createCell(7);
					if (data.getOrderWeight() != 0D)
					{cell7.setCellValue(new HSSFRichTextString(data.getOrderWeight()+""));
						
					}
					else
					{cell7.setCellValue(new HSSFRichTextString(""));
						
					}
					
					cell7.setCellStyle(css);
				}
				
				
				
				final Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue("Total Amount Spent:");
				row.createCell(1).setCellValue(totalAmountSpend.getFormattedValue());
				
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				sheet.autoSizeColumn(4);
				sheet.autoSizeColumn(5);
				sheet.autoSizeColumn(6);
				sheet.autoSizeColumn(7);
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
