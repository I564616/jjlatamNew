/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.download.views;

import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.noggit.JSONWriter.Writable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * This class handles the excel download for back-order report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTBackorderReportExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTBackorderReportExcelView.class);
	private static final String BACKORDER_RESPONSE_DATA_LIST = "jnjGTBackorderReportResponseDataList";
	private static final String BACKORDER_FORM_NAME = "jnjGTBackorderReportForm";
	private static final String sheetName = "BACKORDER REPORT RESULTS";
	private static final String TEXT_ONE = "Please note: Prices are excluding VAT. Availability and Prices might be subject to change.";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	private static final String HEADER = "backorder.reports.header";
	private static final String DOWNLOADDATE = "backorder.reports.download.date";
	private static final String RESULTS = "backorder.reports.results";
	private static final String ACC_NUMBER = "backorder.reports.account.number";
	private static final String ACC_NAME = "backorder.reports.accountname";
	private static final String ORDERDATE = "backorder.reports.orderdate";
	private static final String ACCOUNTS =  "backorder.reports.accounts";
	private static final String CUST_PO = "backorder.reports.customer.po";
	private static final String SHIPTO_NAME = "backorder.reports.shipto.name";
	private static final String ORDERNUMBER = "backorder.reports.ordernumber";
	private static final String PRODUCTCODE = "backorder.reports.productcode";
	private static final String GTIN = "backorder.reports.gtin";
	private static final String PRODUCTNAME = "backorder.reports.productname";
	private static final String ORDERQTY =  "backorder.reports.orderquantity";
	private static final String UNIT = "backorder.reports.unit";
	private static final String EST_AVAILABILITY = "backorder.reports.estimatedavailability";
	private static final String ITEMPRICE = "backorder.reports.itemprice";
	private static final String ORDERTOTAL = "backorder.reports.orderTotal";
	private static final String FRANCISE =  "backorder.reports.franchise";
	private static final String SHIPTO_ACCOUNT =  "backorder.reports.shiptoaccount";
	private static final String FROM =  "backorder.reports.from";
	private static final String TO =  "backorder.reports.to";
	private static final String SEARCHBY =  "backorder.reports.searchBy";
	private static final String STATUS =  "product.detail.specification.status";

	
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
	
	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{        
		     String emptyField = new String();
		     emptyField="";
		 
			arg3.setHeader("Content-Disposition", "attachment; filename=Backorder_Report.xls");
			
			final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = (List<JnjGTBackorderReportResponseData>) arg0
					.get(BACKORDER_RESPONSE_DATA_LIST);
			final JnjGTBackorderReportForm searchCriteria = (JnjGTBackorderReportForm) arg0.get(BACKORDER_FORM_NAME);

			final String accountsSelectedName = (String) arg0.get("accountreportname");
			final Sheet searchSheet = arg1.createSheet(sheetName);
			setHeaderImage(arg1, searchSheet, (String) arg0.get("siteLogoPath"));
			final Row header0 = searchSheet.createRow(6);
			final Row header3 = searchSheet.createRow(9);
			final Row header4 = searchSheet.createRow(10);
			final Row header5 = searchSheet.createRow(11);
		 
			final Row header9 = searchSheet.createRow(15);
			final Row header = searchSheet.createRow(17);
			
			final Font font = arg1.createFont();
			font.setBold(true);
			
			final CellStyle style = arg1.createCellStyle();
			style.setFont(font);
			
			final CellStyle styleCell = arg1.createCellStyle();
			styleCell.setWrapText(true);
			
			final SimpleDateFormat dateFormat= new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			
			/*final SimpleDateFormat dateFormat = new SimpleDateFormat();
			dateFormat.applyPattern("dd/MM/yyyy; HH:mm zzz");*/
			final Date date = new Date();

			header0.createCell(0).setCellValue(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale()));
			header0.getCell(0).setCellStyle(createLabelCell(arg1));

			header3.createCell(0).setCellValue(messageSource.getMessage(DOWNLOADDATE, null, i18nService.getCurrentLocale()));
			header3.getCell(0).setCellStyle(createLabelCell(arg1));

			header3.createCell(1).setCellValue(dateFormat.format(date));

			header3.createCell(2).setCellValue(messageSource.getMessage(RESULTS, null, i18nService.getCurrentLocale()));
			header3.getCell(2).setCellStyle(createLabelCell(arg1));

			header4.createCell(0).setCellValue(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()));
			header4.getCell(0).setCellStyle(createLabelCell(arg1));

			header4.createCell(1).setCellValue((searchCriteria.getAccountIds() != null) ? searchCriteria.getAccountIds() : emptyField);
			header4.getCell(1).setCellStyle(style);
			//Modified by Archana for AAOL-5513
			
			/*final SimpleDateFormat dateFormat2 = new SimpleDateFormat();
			dateFormat2.applyPattern("dd.MM.yyyy");*/
			if (searchCriteria.getFromDate() != null && searchCriteria.getFromDate() != "")
			{
				final String fromDate = new String().concat(messageSource.getMessage(FROM, null,i18nService.getCurrentLocale())+":")
						.concat(dateFormat.format(searchCriteria.getFromDate()));
				header4.createCell(2).setCellValue((fromDate != null) ? fromDate : emptyField);
			}
			if (searchCriteria.getToDate() != null && searchCriteria.getToDate() != "")
			{
				final String toDate = new String().concat(messageSource.getMessage(TO, null,i18nService.getCurrentLocale())+": ")
						.concat(dateFormat.format(searchCriteria.getToDate()));
				header4.createCell(3).setCellValue((toDate != null) ? toDate : "");
			}
			header5.createCell(0).setCellValue(messageSource.getMessage(ACC_NAME, null, i18nService.getCurrentLocale()));
			header5.getCell(0).setCellStyle(createLabelCell(arg1));
			header5.createCell(1).setCellValue((accountsSelectedName != null) ? accountsSelectedName : emptyField);

			final String searchBy = new String().concat(messageSource.getMessage(SEARCHBY, null,i18nService.getCurrentLocale()) + ":")
					.concat(searchCriteria.getProductCode());
			header5.createCell(2).setCellValue((searchBy != null) ? searchBy : "");
			header5.getCell(2).setCellStyle(createLabelCell(arg1));
		 
//			header9.createCell(0).setCellValue(TEXT_ONE);
//			header9.getCell(0).setCellStyle(createLabelCell(arg1));
			
			
			header.createCell(0).setCellValue(messageSource.getMessage(ACCOUNTS, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(arg1));
			header.createCell(1).setCellValue(messageSource.getMessage(CUST_PO, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(arg1));
			header.createCell(2).setCellValue(messageSource.getMessage(ORDERNUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(arg1));
         	
			
			header.createCell(3).setCellValue(messageSource.getMessage(SHIPTO_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(arg1));
			header.createCell(4).setCellValue(messageSource.getMessage(FRANCISE, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(arg1));
			header.createCell(5).setCellValue(messageSource.getMessage(SHIPTO_ACCOUNT, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(arg1));
			
			header.createCell(6).setCellValue(messageSource.getMessage(ORDERDATE, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(arg1));
			header.createCell(7).setCellValue(messageSource.getMessage(PRODUCTNAME, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(arg1));
			header.createCell(8).setCellValue(messageSource.getMessage(PRODUCTCODE, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(arg1));
			header.createCell(9).setCellValue(messageSource.getMessage(ORDERQTY, null, i18nService.getCurrentLocale()));
			header.getCell(9).setCellStyle(createLabelCell(arg1));
			header.createCell(10).setCellValue(messageSource.getMessage(UNIT, null, i18nService.getCurrentLocale()));
			header.getCell(10).setCellStyle(createLabelCell(arg1));
			
			header.createCell(11).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
			header.getCell(11).setCellStyle(createLabelCell(arg1));
			header.createCell(12).setCellValue(messageSource.getMessage(EST_AVAILABILITY, null, i18nService.getCurrentLocale()));
			header.getCell(12).setCellStyle(createLabelCell(arg1));
			
			
			

			/*header.createCell(11).setCellValue("Item Unit Price");
			header.getCell(11).setCellStyle(style);

			header.createCell(12).setCellValue("Order Total");
			header.getCell(12).setCellStyle(style);
			
			header.createCell(13).setCellValue("Franchise");
			header.getCell(13).setCellStyle(style);*/
			CellStyle css = createValueCell(arg1);
			int rowNum = 18;
			if (null != jnjGTBackorderReportResponseDataList)
			{
				double totals = 0;
				for (final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData : jnjGTBackorderReportResponseDataList)
				{
					searchSheet.autoSizeColumn(rowNum);
					final Row row = searchSheet.createRow(rowNum++);
					Cell cell0 = row.createCell(0);
					cell0.setCellValue(jnjGTBackorderReportResponseData.getAccountNumber());
					cell0.setCellStyle(css);
					Cell cell1 = row.createCell(1);
					cell1.setCellValue(jnjGTBackorderReportResponseData.getCustomerPO());
					cell1.setCellStyle(css);
//					row.createCell(3).setCellValue(jnjGTBackorderReportResponseData.getShipToName());
					Cell cell2 = row.createCell(2);
					cell2.setCellValue(null != jnjGTBackorderReportResponseData.getOrderNumber()
							? jnjGTBackorderReportResponseData.getOrderNumber().split("\\|")[1] : "");
					cell2.setCellStyle(css);
					Cell cell3 = row.createCell(3);
					cell3.setCellValue(jnjGTBackorderReportResponseData.getShipToName());
					cell3.setCellStyle(css);
					Cell cell4 = row.createCell(4);
					cell4.setCellValue(jnjGTBackorderReportResponseData.getOperatingCompany());
					cell4.setCellStyle(css);
					Cell cell5 = row.createCell(5);
					cell5.setCellValue(jnjGTBackorderReportResponseData.getShipToAccount());
					cell5.setCellStyle(css);
					Cell cell6 = row.createCell(6);
					cell6.setCellValue(jnjGTBackorderReportResponseData.getOrderDate());
					cell6.setCellStyle(css);
					Cell cell7 = row.createCell(7);
					cell7.setCellValue(jnjGTBackorderReportResponseData.getProductName());
					cell7.setCellStyle(css);
					Cell cell8 = row.createCell(8);
					cell8.setCellValue(jnjGTBackorderReportResponseData.getProductCode());
					cell8.setCellStyle(css);
				
//					row.createCell(6).setCellValue(jnjGTBackorderReportResponseData.getProductGTIN());
					if(jnjGTBackorderReportResponseData.getQty()!="null"){
						Cell cell9 = row.createCell(9);
						cell9.setCellValue(jnjGTBackorderReportResponseData.getQty());	
						cell9.setCellStyle(css);
					}
					
					if(!jnjGTBackorderReportResponseData.getQty().equals("null") ){
						Cell cell9 = row.createCell(9);
						cell9.setCellValue(jnjGTBackorderReportResponseData.getQty());	
						cell9.setCellStyle(css);
						
					}else{
						Cell cell9 = row.createCell(9);
						cell9.setCellValue(emptyField);
						cell9.setCellStyle(css);
					}
					
					Cell cell10 = row.createCell(10);
					cell10.setCellValue(jnjGTBackorderReportResponseData.getUnit());
					cell10.setCellStyle(css);
					Cell cell11 = row.createCell(11);
					cell11.setCellValue(jnjGTBackorderReportResponseData.getStatus());
					cell11.setCellStyle(css);
					Cell cell12 = row.createCell(12);
					cell12.setCellValue(jnjGTBackorderReportResponseData.getEstimatedAvailability());
					cell12.setCellStyle(css);
/*					row.createCell(11).setCellValue(jnjGTBackorderReportResponseData.getItemPrice() + jnjGTBackorderReportResponseData.getCurrency());
					row.createCell(12).setCellValue(jnjGTBackorderReportResponseData.getExtendedPrice() + jnjGTBackorderReportResponseData.getCurrency());
					//Franchise data
					row.createCell(13).setCellValue(jnjGTBackorderReportResponseData.getOperatingCompany());*/
					if (StringUtils.isNotEmpty(jnjGTBackorderReportResponseData.getExtendedPrice()))
					{
						totals += Double.parseDouble(jnjGTBackorderReportResponseData.getExtendedPrice());
					}
				}
				final Row row = searchSheet.createRow(rowNum++);
				row.createCell(0).setCellValue("");
				row.createCell(1).setCellValue("");
				row.createCell(2).setCellValue("");
				row.createCell(3).setCellValue("");
				row.createCell(4).setCellValue("");
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue("");
				row.createCell(7).setCellValue("");
				row.createCell(8).setCellValue("");
				row.createCell(9).setCellValue("");
				/*row.createCell(10).setCellValue("");
				row.createCell(11).setCellValue("");
				row.createCell(12).setCellValue("");
				row.createCell(13).setCellValue("");*/
				searchSheet.setAutoFilter(CellRangeAddress.valueOf("A18:N"+18+jnjGTBackorderReportResponseDataList.size()+""));
				
				searchSheet.addMergedRegion(new CellRangeAddress(15,15,0,6));
				searchSheet.addMergedRegion(new CellRangeAddress(9,9,2,4));
				for(int i=0;i<=13;i++){
					if(i==1){
						searchSheet.setColumnWidth(i,4500);
					}else{
						searchSheet.autoSizeColumn(i);
					}
				
				}
			}
			
		}
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final String logoPath)
	{
		final CellStyle styleImgCell = hssfWorkbook.createCellStyle();
		styleImgCell.setAlignment(HorizontalAlignment.CENTER);
		//final Row header0 = sheet.createRow(0);
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
		sheet.addMergedRegion(new CellRangeAddress(0,4,0,9));
		//header0.getCell(0).setCellStyle(styleImgCell);
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
	