/*
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 * @author prodri92
 */

package com.jnj.b2b.jnjlaordertemplate.download;

import com.jnj.b2b.jnjlaordertemplate.controllers.utils.JnjLatamInvoiceControllerUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnJLaInvoiceHistoryData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnJLALanguageDateFormatUtil;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLatamAbstractExportViewUtil;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JnjLatamInvoiceHistoryExcelView extends JnjLatamAbstractExcelView {

    private static final String SHEET_NAME = "Invoice History";
    private static final int INVOICE_NUMBER_COLUMN = 0;
    private static final int ORDER_NUMBER_COLUMN = 1;
    private static final int INVOICE_DATE_COLUMN = 2;
    private static final int CARRIER_ESTIMATED_DELIVERY_DATE = 3;
    private static final int CARRIER_ACTUAL_DELIVERY_DATE = 4;
    private static final int INVOICE_TOTAL_COLUMN = 5;
    private static final Class<JnjLatamInvoiceHistoryExcelView> currentClass = JnjLatamInvoiceHistoryExcelView.class;

    private CommonI18NService commonI18NService;
    private JnjCommonFacadeUtil jnjCommonFacadeUtil;
    private JnjLatamInvoiceControllerUtil invoiceUtil;

    @Override
    protected void buildExcelDocument(Map<String, Object> sessionMap, Workbook hssfWorkbook,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws Exception {
        Integer rowNumber = INITIAL_ROW;
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=InvoiceHistorySearchResult.xls");
        try {
            final SearchPageData<JnJLaInvoiceHistoryData> searchPageData =
                (SearchPageData<JnJLaInvoiceHistoryData>) JnjLaCommonUtil.getMapValue("searchPageData", sessionMap);
            final List<JnJLaInvoiceHistoryData> results = searchPageData != null ? searchPageData.getResults() : null;
            final Sheet sheet = hssfWorkbook.createSheet(SHEET_NAME);
            CellStyle currencyStyle = createCurrencyStyle(hssfWorkbook);
            createHeader(hssfWorkbook, sheet, sessionMap, rowNumber);
            rowNumber++;

            if (null != results) {
                final SimpleDateFormat dateFormatBaseOnUserLanguage = JnJLALanguageDateFormatUtil.getDateFormatBaseOnUserLanguage(commonI18NService);
                for (final JnJLaInvoiceHistoryData data : results) {
                    rowNumber++;
                    createEntryRow(data, sheet, dateFormatBaseOnUserLanguage, rowNumber, currencyStyle);
                }
            }

            if (invoiceUtil.hasAnyEmptyOrder(results)) {
                createOrderNotLoadedMessage(rowNumber, sheet);
            }

        } catch (final Exception exception) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.INVOICE_EXPORT_TO_EXCEL,
                "buildExcelDocument()", "Error while writing Data.", exception, currentClass);
        }
    }

    private void createOrderNotLoadedMessage(Integer rowNumber, Sheet sheet) {
        final Row messageRow = sheet.createRow(rowNumber + 1);
        final String orderNotLoadedMessage = jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_ORDER_NOT_LOADED_MESSAGE);
        messageRow.createCell(0).setCellValue(orderNotLoadedMessage);
    }

    private CellStyle createCurrencyStyle(Workbook hssfWorkbook) {
        CellStyle currencyStyle = hssfWorkbook.createCellStyle();
        currencyStyle.setAlignment(HorizontalAlignment.CENTER);
        return currencyStyle;
    }

    private void createEntryRow(JnJLaInvoiceHistoryData entry, Sheet sheet, SimpleDateFormat dateFormatBaseOnUserLanguage, Integer rowNumber, CellStyle currencyStyle) {
        final Row row = sheet.createRow(rowNumber);

        row.createCell(INVOICE_NUMBER_COLUMN).setCellValue(entry.getInvoiceNumber());
        row.createCell(ORDER_NUMBER_COLUMN).setCellValue(entry.getSapOrderNumber());
        row.createCell(INVOICE_DATE_COLUMN).setCellValue(createDate(entry, dateFormatBaseOnUserLanguage, entry.getCreationDate()));
        row.createCell(CARRIER_ESTIMATED_DELIVERY_DATE).setCellValue(createDate(entry, dateFormatBaseOnUserLanguage, entry.getCarrierEstimateDeliveryDate()));
        row.createCell(CARRIER_ACTUAL_DELIVERY_DATE).setCellValue(createDate(entry, dateFormatBaseOnUserLanguage, entry.getCarrierConfirmedDeliveryDate()));
        Cell totalCell = row.createCell(INVOICE_TOTAL_COLUMN);
        totalCell.setCellValue(createInvoiceTotal(entry));
        totalCell.setCellStyle(currencyStyle);
    }

    private String createInvoiceTotal(JnJLaInvoiceHistoryData entry) {
        if (entry.getNetValue() != null) {
            return entry.getNetValue().getFormattedValue();
        }
        return null;
    }

    private String createDate(final JnJLaInvoiceHistoryData entry, final SimpleDateFormat dateFormatBaseOnUserLanguage, final Date entryDate ) {
        if (entryDate != null) {
            return dateFormatBaseOnUserLanguage.format(entryDate);
        }
        return null;
    }

    private void createHeader(Workbook hssfWorkbook, Sheet sheet, Map<String, Object> sessionMap, Integer rowNumber) {
        setHeaderImage(hssfWorkbook, sheet, (InputStream) sessionMap.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY));

        verifyResultExceedLimit(sheet, sessionMap, rowNumber);

        final Row header = sheet.createRow(rowNumber);

        final Font font = hssfWorkbook.createFont();
        font.setBold(true);

        final CellStyle style = hssfWorkbook.createCellStyle();
        style.setFont(font);

        populateHeaderText(header);
    }

    private void populateHeaderText(Row header) {
        header.createCell(INVOICE_NUMBER_COLUMN).setCellValue(jnjCommonFacadeUtil
            .getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_INVOICE_NUMBER));
        header.createCell(ORDER_NUMBER_COLUMN).setCellValue(jnjCommonFacadeUtil
            .getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_INVOICE_ORDER_NUMBER));
        header.createCell(INVOICE_DATE_COLUMN).setCellValue(jnjCommonFacadeUtil
            .getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_INVOICE_DATE));
        header.createCell(CARRIER_ESTIMATED_DELIVERY_DATE).setCellValue(jnjCommonFacadeUtil
            .getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_ESTIMATED_DELIVERY_DATE));
        header.createCell(CARRIER_ACTUAL_DELIVERY_DATE).setCellValue(jnjCommonFacadeUtil
            .getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_ACTUAL_DELIVERY_DATE));
        header.createCell(INVOICE_TOTAL_COLUMN).setCellValue(jnjCommonFacadeUtil
            .getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_INVOICE_TOTAL));
    }

    private void verifyResultExceedLimit(Sheet sheet, Map<String, Object> sessionMap, Integer rowNumber) {
        final Boolean resultLimitExceeded =
            JnjLaCommonUtil.getMapValue("resultLimitExceeded", sessionMap) != null;
        if (resultLimitExceeded) {
            rowNumber++;
            final Row note = sheet.createRow(rowNumber);
            note.createCell(INVOICE_NUMBER_COLUMN).setCellValue(JnjLatamAbstractExportViewUtil.RESULTS_EXCEEDED_MESSAGE);
        }
    }

    private void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final InputStream inputStream) {
        final String methodName = "setHeaderImage()";
        int index = 0;
        try {
            final byte[] bytes = IOUtils.toByteArray(inputStream);
            index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
            inputStream.close();
        } catch (final IOException ioException) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.INVOICE_EXPORT_TO_EXCEL, methodName,
                "Exception occurred during input/output operation in the method setHeaderImage()", ioException, currentClass);
        }
        final CreationHelper helper = hssfWorkbook.getCreationHelper();
        final Drawing drawing = sheet.createDrawingPatriarch();
        final ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setCol2(10);
        anchor.setRow1(1);
        anchor.setRow2(5);
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
        drawing.createPicture(anchor, index);
    }

    public void setCommonI18NService(final CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }

    public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
        this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }

    public void setInvoiceUtil(JnjLatamInvoiceControllerUtil invoiceUtil) {
        this.invoiceUtil = invoiceUtil;
    }
}