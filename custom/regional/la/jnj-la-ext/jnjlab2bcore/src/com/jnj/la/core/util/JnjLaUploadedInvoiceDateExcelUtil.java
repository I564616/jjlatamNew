/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.util;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.data.JnJLaInvoiceRowError;
import com.jnj.la.core.model.JnJUploadedInvoiceDateModel;
import com.jnj.la.core.services.impl.JnJUpdateInvoiceServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.ACTUAL_DELIVERY_DATE;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.ACTUAL_DELIVERY_DATE_INDEX;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.ERROR_MESSAGE;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.ERROR_MESSAGE_INDEX;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.ESTIMATED_DELIVERY_DATE;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.ESTIMATED_DELIVERY_DATE_INDEX;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.HEADER_ROW_INDEX;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.INVOICE_NUMBER;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.INVOICE_NUMBER_INDEX;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.XSL_EXTENSION;

public final class JnjLaUploadedInvoiceDateExcelUtil {

    private JnjLaUploadedInvoiceDateExcelUtil(){}

    public static Iterator<Row> readRowsFromFile(final FileInputStream fis) throws IOException {
        final HSSFWorkbook workbook = new HSSFWorkbook(fis);
        final HSSFSheet sheet = workbook.getSheetAt(HEADER_ROW_INDEX);
        return sheet.rowIterator();
    }

    public static void generateErrorFile(final Path errorPath, final JnJUploadedInvoiceDateModel model, final List<JnJLaInvoiceRowError> errors) {
        final File errorFile = errorPath.resolve(getErrorFileName(model.getFilename())).toFile();

        try (final FileOutputStream fos = new FileOutputStream(errorFile)) {

            final HSSFWorkbook workbook = new HSSFWorkbook();
            final HSSFSheet sheet = workbook.createSheet();

            createHeader(sheet);

            int rowNum = 1;
            for (final JnJLaInvoiceRowError error : errors) {
                createErrorRow(sheet, rowNum, error);
                rowNum++;
            }

            workbook.write(fos);
        } catch (final IOException e) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, "generateErrorFile()", "Unable to create error file", e, JnJUpdateInvoiceServiceImpl.class);
        }
    }

    public static String getErrorFileName(final String filename) {
        return FilenameUtils.getBaseName(filename) + XSL_EXTENSION;
    }

    private static void createErrorRow(final HSSFSheet sheet, int rowNum, final JnJLaInvoiceRowError error) {
        final HSSFRow row = sheet.createRow(rowNum);
        row.createCell(INVOICE_NUMBER_INDEX).setCellValue(error.getInvoice());
        row.createCell(ESTIMATED_DELIVERY_DATE_INDEX).setCellValue(error.getEstDeliveryDate());
        row.createCell(ACTUAL_DELIVERY_DATE_INDEX).setCellValue(error.getActualDeliveryDate());
        row.createCell(ERROR_MESSAGE_INDEX).setCellValue(error.getErrorMessage());
    }

    public static JnJLaInvoiceRowError createRowError(final String invoiceNumber, final String ceddStr, final String caddStr, final String message) {
        final JnJLaInvoiceRowError error = new JnJLaInvoiceRowError();
        error.setInvoice(invoiceNumber);
        error.setEstDeliveryDate(ceddStr);
        error.setActualDeliveryDate(caddStr);
        error.setErrorMessage(message);
        return error;
    }

    private static void createHeader(final HSSFSheet sheet) {
        final HSSFRow header = sheet.createRow(HEADER_ROW_INDEX);
        header.createCell(INVOICE_NUMBER_INDEX).setCellValue(INVOICE_NUMBER);
        header.createCell(ESTIMATED_DELIVERY_DATE_INDEX).setCellValue(ESTIMATED_DELIVERY_DATE);
        header.createCell(ACTUAL_DELIVERY_DATE_INDEX).setCellValue(ACTUAL_DELIVERY_DATE);
        header.createCell(ERROR_MESSAGE_INDEX).setCellValue(ERROR_MESSAGE);
    }


}
