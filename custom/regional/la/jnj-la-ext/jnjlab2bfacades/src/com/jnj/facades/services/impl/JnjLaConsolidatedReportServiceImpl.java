/*
 * Copyright: Copyright © 2023
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services.impl;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.services.JnjLaConsolidatedReportService;
import com.jnj.facades.util.JnjLaEmailPopulatorUtils;
import com.jnj.la.core.data.order.JnjLAConsolidatedReportRowData;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.jnj.core.util.JnjGTCoreUtil;

import org.springframework.beans.factory.annotation.Autowired;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;



public class JnjLaConsolidatedReportServiceImpl implements JnjLaConsolidatedReportService {

    private static final Logger LOG = Logger.getLogger(JnjLaConsolidatedReportServiceImpl.class);

    private static final String HEADER_LABEL = "jnj.la.consolidated.report.header.";

    private static final int SAP_ORDER_NUMBER = 0;
    private static final int SALES_ORDER_NUMBER = 1;
    private static final int ORDER_TYPE = 2;
    private static final int ISSUER_CODE = 3;
    private static final int ISSUER_NAME = 4;
    private static final int DELIVERY_ADDRESS = 5;
    private static final int CREATED_DATE = 6;
    private static final int SALES_ORDER_TYPE = 7;
    private static final int FIRST_HIERARCHY = 8;
    private static final int SAP_MATERIAL_NUMBER = 9;
    private static final int MATERIAL_NAME = 10;
    private static final int MATERIAL_EAN = 11;
    private static final int CUSTOMER_MATERIAL_CODE = 12;
    private static final int LINE_QUANTITY = 13;
    private static final int INVOICED_QUANTITY = 14;
    private static final int OPENED_QUANTITY = 15;
    private static final int UNIT_OF_MEASURE = 16;
    private static final int UNIT_PRICE = 17;
    private static final int SAP_LINE_NUMBER = 18;
    private static final int LINE_STATUS = 19;
    private static final int ESTIMATED_DELIVERY_DATE = 20;
    private static final int ORDER_STATUS = 21;
    private static final int CONTRACT_REFERENCE = 22;
    private static final int INDIRECT_CUSTOMER = 23;
    private static final int INVOICE_NUMBER = 24;
    private static final int CREATED_BY = 25;
    private static final int REQUESTED_DELIVERY_DATE = 26;

    private static final String TAX_WARNING = "jnj.la.consolidated.report.TAX_WARNING";
    private static final String SHEET_NAME = "jnj.la.consolidated.report.SHEET_NAME";

    private static final String EXCEL_MIME = "application/vnd.ms-excel";
    private static final String FILE_NAME_FORMAT = "opened_orders_%s_%s.xls";
    private static final String DATE_FORMAT = "m/d/yy"; // this must the format, even in pt_BR, just to detect Date built-in type
    private static final String NUMBER_FORMAT = "#,##0;\\-#,##0";
    private static final String CURRENCY_FORMAT = "#,##0.00;\\-#,##0.00";
    private static final String SPACE = " ";
    private static final String DATE1 = "date1";
    private static final String DATE2 = "date2";
    private static final String MESSAGE = "message";
    
    private EmailService emailService;
    private JnjCommonFacadeUtil jnjCommonFacadeUtil;
    
    @Autowired
	private ConfigurationService configurationService;   	
    
    @Override
    public EmailAttachmentModel createConsolidatedReport(final List<JnjLAConsolidatedReportRowData> reportRows,
                                                         final JnJB2BUnitModel unit, final Locale locale) {

        final File tempFile = createTempFile();

        if (tempFile == null) {
            return null;
        }

        return generateConsolidatedReport(reportRows, unit, locale, tempFile);
    }

    private EmailAttachmentModel generateConsolidatedReport(List<JnjLAConsolidatedReportRowData> reportRows,
                                                            JnJB2BUnitModel unit, Locale locale, File tempFile) {
        try (final FileOutputStream fos = new FileOutputStream(tempFile)) {
            final HSSFWorkbook workbook = new HSSFWorkbook();
            final HSSFSheet sheet = workbook.createSheet(jnjCommonFacadeUtil.getMessageFromImpex(SHEET_NAME, locale));
            String message = jnjCommonFacadeUtil.getMessageFromImpex(
                    Jnjlab2bcoreConstants.Order.ORDER_DELIVERY_UNAVAILABLE, locale);
            String date1 = configurationService.getConfiguration()
					.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DATE);
			String date2 = configurationService.getConfiguration()
					.getString(Jnjlab2bcoreConstants.Order.DEFAULT_DELIVERY_DT);
			
            createHeader(sheet, createHeaderStyle(workbook), locale);
            createRows(sheet, workbook, reportRows, message, date1, date2);

            workbook.write(fos);

            return emailService.createEmailAttachment(new DataInputStream(new FileInputStream(tempFile)),
                    getFileName(unit), EXCEL_MIME);
        } catch (final IOException e) {
            LOG.error("Not able to generate Excel Report for " + unit.getUid(), e);
            return null;
        }
    }

    private static CellStyle createHeaderStyle(final HSSFWorkbook workbook) {
        return createBoldStyle(workbook);
    }

    private static CellStyle createBoldStyle(final HSSFWorkbook workbook) {
        final CellStyle style = workbook.createCellStyle();
        style.setFont(createBoldFont(workbook));
        return style;
    }

    private static CellStyle createDateStyle(final HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(createDataFormat(workbook));
        return style;
    }

    private static CellStyle createNumberStyle(final HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(createNumberFormat(workbook));
        return style;
    }
    
    private static CellStyle createCurrencyStyle(final HSSFWorkbook workbook,List<JnjLAConsolidatedReportRowData> reportRows) {
    	
    	String currency = StringUtils.EMPTY;
    	for (final JnjLAConsolidatedReportRowData reportRow : reportRows) {
    		currency =reportRow.getCurrency();
    		if(!StringUtils.isEmpty(currency))
    		{
    			LOG.info("Currency symbol*********************** "+currency);
    			break;
    		}
    	}
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setDataFormat(createCurrencyFormat(workbook,currency));
        return style;
    }
    
    private static CellStyle createBoldAndDateStyle(final HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFont(createBoldFont(workbook));
        style.setDataFormat(createDataFormat(workbook));
        return style;
    }

    private static HSSFFont createBoldFont(final HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        return font;
    }

    private static short createDataFormat(final HSSFWorkbook workbook) {
        return workbook.getCreationHelper().createDataFormat().getFormat(DATE_FORMAT);
    }

    private static short createNumberFormat(final HSSFWorkbook workbook) {
        return workbook.getCreationHelper().createDataFormat().getFormat(NUMBER_FORMAT);
    }
    
    private static short createCurrencyFormat(final HSSFWorkbook workbook, final String currencySymbol) {
    	return workbook.getCreationHelper().createDataFormat().getFormat(currencySymbol + SPACE + CURRENCY_FORMAT);
    }
    
    private static File createTempFile() {
        try {
            return File.createTempFile("temp", "xls");
        } catch (IOException e) {
            LOG.error("Unable to create temporary file", e);
            return null;
        }
    }

    private static String getFileName(final JnJB2BUnitModel unit) {
        return String.format(FILE_NAME_FORMAT, unit.getUid(), System.currentTimeMillis());
    }

    private void createHeader(final HSSFSheet sheet, final CellStyle headerStyle, final Locale locale) {
        createTaxWarning(sheet, headerStyle, locale);

        final HSSFRow header = sheet.createRow(2);
        createHeaderCell(header, headerStyle, SAP_ORDER_NUMBER, HEADER_LABEL + "SAP_ORDER_NUMBER", locale);
        createHeaderCell(header, headerStyle, SALES_ORDER_NUMBER, HEADER_LABEL + "SALES_ORDER_NUMBER", locale);
        createHeaderCell(header, headerStyle, ORDER_TYPE, HEADER_LABEL + "ORDER_TYPE", locale);
        createHeaderCell(header, headerStyle, ISSUER_CODE, HEADER_LABEL + "ISSUER_CODE", locale);
        createHeaderCell(header, headerStyle, ISSUER_NAME, HEADER_LABEL + "ISSUER_NAME", locale);
        createHeaderCell(header, headerStyle, DELIVERY_ADDRESS, HEADER_LABEL + "DELIVERY_ADDRESS", locale);
        createHeaderCell(header, headerStyle, CREATED_DATE, HEADER_LABEL + "CREATED_DATE", locale);
        createHeaderCell(header, headerStyle, SALES_ORDER_TYPE, HEADER_LABEL + "SALES_ORDER_TYPE", locale);
        createHeaderCell(header, headerStyle, FIRST_HIERARCHY, HEADER_LABEL + "FIRST_HIERARCHY", locale);
        createHeaderCell(header, headerStyle, SAP_MATERIAL_NUMBER, HEADER_LABEL + "SAP_MATERIAL_NUMBER", locale);
        createHeaderCell(header, headerStyle, MATERIAL_NAME, HEADER_LABEL + "MATERIAL_NAME", locale);
        createHeaderCell(header, headerStyle, MATERIAL_EAN, HEADER_LABEL + "MATERIAL_EAN", locale);
        createHeaderCell(header, headerStyle, CUSTOMER_MATERIAL_CODE, HEADER_LABEL + "CUSTOMER_MATERIAL_CODE", locale);
        createHeaderCell(header, headerStyle, LINE_QUANTITY, HEADER_LABEL + "LINE_QUANTITY", locale);
        createHeaderCell(header, headerStyle, INVOICED_QUANTITY, HEADER_LABEL + "INVOICED_QUANTITY", locale);
        createHeaderCell(header, headerStyle, OPENED_QUANTITY, HEADER_LABEL + "OPENED_QUANTITY", locale);
        createHeaderCell(header, headerStyle, UNIT_OF_MEASURE, HEADER_LABEL + "UNIT_OF_MEASURE", locale);
        createHeaderCell(header, headerStyle, UNIT_PRICE, HEADER_LABEL + "UNIT_PRICE", locale);
        createHeaderCell(header, headerStyle, SAP_LINE_NUMBER, HEADER_LABEL + "SAP_LINE_NUMBER", locale);
        createHeaderCell(header, headerStyle, LINE_STATUS, HEADER_LABEL + "LINE_STATUS", locale);
        createHeaderCell(header, headerStyle, ESTIMATED_DELIVERY_DATE, HEADER_LABEL + "ESTIMATED_DELIVERY_DATE", locale);
        createHeaderCell(header, headerStyle, ORDER_STATUS, HEADER_LABEL + "ORDER_STATUS", locale);
        createHeaderCell(header, headerStyle, CONTRACT_REFERENCE, HEADER_LABEL + "CONTRACT_REFERENCE", locale);
        createHeaderCell(header, headerStyle, INDIRECT_CUSTOMER, HEADER_LABEL + "INDIRECT_CUSTOMER", locale);
        createHeaderCell(header, headerStyle, INVOICE_NUMBER, HEADER_LABEL + "INVOICE_NUMBER", locale);
        createHeaderCell(header, headerStyle, CREATED_BY, HEADER_LABEL + "CREATED_BY", locale);
        createHeaderCell(header, headerStyle, REQUESTED_DELIVERY_DATE, HEADER_LABEL + "REQUESTED_DELIVERY_DATE", locale);

        for (int i = 0; i <= header.getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createTaxWarning(HSSFSheet sheet, CellStyle headerStyle, Locale locale) {
        final HSSFRow messageRow = sheet.createRow(0);
        final HSSFCell cell = messageRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(jnjCommonFacadeUtil.getMessageFromImpex(TAX_WARNING, locale));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
    }

    private void createHeaderCell(final HSSFRow header, CellStyle headerStyle, final int column, final String key,
                                  final Locale locale) {
        final HSSFCell cell = header.createCell(column);
        cell.setCellValue(jnjCommonFacadeUtil.getMessageFromImpex(key, locale));
        cell.setCellStyle(headerStyle);
    }

    private static void createRows(final HSSFSheet sheet, final HSSFWorkbook workbook,
                                   final List<JnjLAConsolidatedReportRowData> reportRows, String message, String date1,
                                   String date2) {
        final CellStyle boldStyle = createBoldStyle(workbook);
        final CellStyle dateStyle = createDateStyle(workbook);
        final CellStyle numberStyle = createNumberStyle(workbook);
        final CellStyle currencyStyle = createCurrencyStyle(workbook,reportRows);
        final CellStyle boldAndDateStyle = createBoldAndDateStyle(workbook);

        int firstRow = 3;

        for (final JnjLAConsolidatedReportRowData reportRow : reportRows) {
            final HSSFRow row = sheet.createRow(firstRow);
        	  
            row.createCell(SAP_ORDER_NUMBER).setCellValue(reportRow.getSapOrderNumber());
            row.createCell(SALES_ORDER_NUMBER).setCellValue(reportRow.getCustomerOrderNumber());
            row.createCell(ORDER_TYPE).setCellValue(reportRow.getOrderType());
            row.createCell(ISSUER_CODE).setCellValue(reportRow.getIssuerCode());
            row.createCell(ISSUER_NAME).setCellValue(reportRow.getIssuerName());
            row.createCell(DELIVERY_ADDRESS).setCellValue(reportRow.getDeliveryAddress());
            createDateCell(CREATED_DATE, row, dateStyle, reportRow.getCreationDate());
            row.createCell(SALES_ORDER_TYPE).setCellValue(reportRow.getSalesOrderChannel());
            row.createCell(FIRST_HIERARCHY).setCellValue(reportRow.getFirstHierarchy());
            row.createCell(SAP_MATERIAL_NUMBER).setCellValue(reportRow.getProductCode());
            row.createCell(MATERIAL_NAME).setCellValue(reportRow.getProductName());
            row.createCell(MATERIAL_EAN).setCellValue(reportRow.getEanNumber());
            row.createCell(CUSTOMER_MATERIAL_CODE).setCellValue(reportRow.getCustomerMaterialCode());
            createNumberCell(LINE_QUANTITY,row,numberStyle,reportRow.getQuantity());
            createNumberCell(INVOICED_QUANTITY,row,numberStyle,reportRow.getInvoicedQuantity());
            createNumberCell(OPENED_QUANTITY,row,numberStyle,reportRow.getOpenedQuantity());
            row.createCell(UNIT_OF_MEASURE).setCellValue(reportRow.getSalesUnit());
            createCurrencyCell(UNIT_PRICE,row,currencyStyle,reportRow.getUnitPrice());
            row.createCell(SAP_LINE_NUMBER).setCellValue(reportRow.getSapLineNumber());
            row.createCell(LINE_STATUS).setCellValue(reportRow.getStatus());
            
            Map<String, String> dataMap = new HashMap();
            dataMap.put(DATE1, date1);
            dataMap.put(DATE2, date2);
            dataMap.put(MESSAGE, message);
            
            setEstimateDeliveryDate(row, reportRow, dataMap);
            
            row.createCell(ORDER_STATUS).setCellValue(reportRow.getOrderStatus());
            row.createCell(CONTRACT_REFERENCE).setCellValue(reportRow.getContractReference());
            row.createCell(INDIRECT_CUSTOMER).setCellValue(reportRow.getIndirectCustomer());
            row.createCell(INVOICE_NUMBER).setCellValue(reportRow.getInvoiceNumber());
            row.createCell(CREATED_BY).setCellValue(reportRow.getCreatedBy());
            createDateCell(REQUESTED_DELIVERY_DATE, row, dateStyle, reportRow.getRequestedDeliveryDate());

            if (reportRow.getHasChanged() != null && reportRow.getHasChanged()) {
                setStyleOnRow(boldStyle, row);
                setStyleOnCell(boldAndDateStyle, row, CREATED_DATE);
                setStyleOnCell(boldAndDateStyle, row, ESTIMATED_DELIVERY_DATE);
                setStyleOnCell(boldAndDateStyle, row, REQUESTED_DELIVERY_DATE);
            }

            firstRow++;
        }
    }
    
	private static void setEstimateDeliveryDate(final HSSFRow row,			 
			final JnjLAConsolidatedReportRowData reportRow, Map<String, String> dataMap) {

		try {
			final DateFormat dateFormat = new SimpleDateFormat(
					Jnjlab2bcoreConstants.Order.DATE_FORMAT);

			Date defaultDate1 = dateFormat.parse(dataMap.get(DATE1));
			Date defaultDate2 = dateFormat.parse(dataMap.get(DATE2));
			Date date = reportRow.getEstimatedDeliveryDate();
            String quantityValue = "";
            if(null != reportRow.getQuantity()){
                quantityValue = reportRow.getQuantity();
            }

			if (null != date) {
				Date deliveryDate = dateFormat.parse(dateFormat.format(date));
				if (defaultDate1.compareTo(deliveryDate) != 0
						&& defaultDate2.compareTo(deliveryDate) != 0) {
                    row.createCell(ESTIMATED_DELIVERY_DATE).setCellValue(JnjLaEmailPopulatorUtils.formatDate(date)
                            + " - " + quantityValue + " " + reportRow.getSalesUnit());

				} else {
					row.createCell(ESTIMATED_DELIVERY_DATE).setCellValue(
							dataMap.get(MESSAGE));
				}
			} else {
				row.createCell(ESTIMATED_DELIVERY_DATE).setCellValue(
						dataMap.get(MESSAGE));
			}
		} catch (ParseException pe) {
			JnjGTCoreUtil.logErrorMessage("excel", "createRow()",
					"Error while writing Data.", pe,
					JnjLaConsolidatedReportServiceImpl.class);
		}
	}

    private static void createDateCell(final int column, final HSSFRow row, final CellStyle style, final Date value) {
        HSSFCell cell = row.createCell(column);
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(style);
    }
    
    private static void createNumberCell(final int column, final HSSFRow row, final CellStyle style, final String value) {
        HSSFCell cell = row.createCell(column);
        if (value != null) {
            cell.setCellValue(Integer.parseInt(value));
        }
        cell.setCellStyle(style);
    }
    
    private static void createCurrencyCell(final int column, final HSSFRow row, final CellStyle style, final Double value) {
    	 HSSFCell cell = row.createCell(column);
         if (value != null) {
             cell.setCellValue(value);
         }
         cell.setCellStyle(style);
    }

    private static void setStyleOnRow(final CellStyle style, final HSSFRow row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            row.getCell(i).setCellStyle(style);
        }
    }

    private static void setStyleOnCell(final CellStyle style, HSSFRow row, int cell) {
        row.getCell(cell).setCellStyle(style);
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void setJnjCommonFacadeUtil(JnjCommonFacadeUtil jnjCommonFacadeUtil) {
        this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }
}