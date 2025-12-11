/**
 * Copyright: Copyright © 2023
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.b2b.jnjglobalreports.download.views;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjLaOpenOrdersReportReponseData;
import com.jnj.la.b2b.jnjglobalreports.forms.JnjLaOpenOrdersReportForm;
import com.jnj.la.b2b.jnjglobalreports.download.constants.JnjlaglobalreportsaddonXLSPDFConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.servicelayer.i18n.I18NService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will be used to generate the open orders report excel. *
 */
public class JnjLAOpenOrderReportExcelView extends AbstractXlsView {

    private static final Logger LOG = LoggerFactory.getLogger(JnjLAOpenOrderReportExcelView.class);
    private static final String SHEET_NAME = "Open Orders Report";
    private static final String SOLD_TO_COL = "reports.openItem.columns.SoldTo";
    private static final String SOLD_TO_NAME_COL = "reports.openItem.columns.SoldToName";
    private static final String SHIP_TO_COL = "reports.openItem.columns.ShipTo";
    private static final String SHIP_TO_NAME_COL = "reports.openItem.columns.ShipToName";
    private static final String JNJ_ORDER_NUMBER_COL = "reports.openItem.columns.JnJOrderNumber";
    private static final String ORDER_TYPE_COL = "reports.openItem.columns.OrderType";
    private static final String LINE_NUMBER_COL = "reports.openItem.columns.LineNumber";
    private static final String SCHEDULE_LINE_COL = "reports.openItem.columns.scheduleLine";
    private static final String PRODUCT_CODE_COL = "reports.openItem.columns.ProductCode";
    private static final String PRODUCT_DESCRIPTION_COL = "reports.openItem.columns.ProductDescription";
    private static final String ESTI_DELIVERY_DATE = "reports.openItem.columns.EstimatedDeliveryDate";
    private static final String ORDER_REFERENCE_COL = "reports.openItem.columns.OrderReference";
    private static final String EAN_1 = "reports.openItem.columns.EAN";
    private static final String SUB_FRANCHISE_COL = "reports.openItem.columns.SubFranchise";
    private static final String TOTAL_UNITS_QUANTITY_COL = "reports.openItem.columns.TotalUnitsQuantity";
    private static final String UNIT_OF_MEASURE_COL = "reports.openItem.columns.UnitOfMeasure";
    private static final String ORDER_QTY_SALES_UNITS_COL = "reports.openItem.columns.OrderQuantitySalesUnits";
    private static final String OPEN_ORDER_COL = "reports.openItem.columns.OpenOrder";
    private static final String ORDER_CREATION_DATE_COL = "reports.openItem.columns.OrderCreationDate";
    private static final String DATE_REQ_DATE_COL = "reports.openItem.columns.DateRequestedDate";
    private static final String SHOW_COLUMNS_VAL = "Y";
    private static final String SOLD_TO_NAME_COL_KEY = "soldToName_col_show_key";
    private static final String ORDER_REFERENCE_COL_KEY = "orderReference_col_show_key";
    private static final String EAN_COL_KEY = "ean_col_show_key";
    private static final String SUB_FRANCHISE_COL_KEY = "subFranchise_col_show_key";
    private static final String TOTAL_UNITS_QUANTITY_COL_KEY = "totalUnitsQuantity_col_show_key";
    private static final String UNIT_OF_MEASURE_COL_KEY = "unitOfMeasure_col_show_key";
    private static final String ORDER_QTY_SALES_UNITS_COL_KEY = "orderQtySalesUnits_col_show_key";
    private static final String OPEN_ORDER_COL_KEY = "openOrder_col_show_key";
    private static final String ORDER_CREATION_DATE_COL_KEY = "orderCreationDate_col_show_key";
    private static final String REG_DELIVERY_DATE_COL_KEY = "requestedDeliveryDate_col_show_key";
    private MessageSource messageSource;
    private I18NService i18nService;

    /**
     * @param dataMap
     * @param hssfWorkbook
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @Override
    protected void buildExcelDocument(final Map<String, Object> dataMap, final Workbook hssfWorkbook,
                                      final HttpServletRequest httpServletRequest,
                                      final HttpServletResponse httpServletResponse) throws Exception {
        final String METHOD_NAME = "buildExcelDocument()";
        final List<JnjLaOpenOrdersReportReponseData> jnjLaOpenOrdersReportResponseDataList =
                (List<JnjLaOpenOrdersReportReponseData>) dataMap
                        .get(JnjlaglobalreportsaddonXLSPDFConstants.OPEN_ORDERS_RESPONSE_DATA_LIST);
        final JnjLaOpenOrdersReportForm searchCriteria = (JnjLaOpenOrdersReportForm) dataMap
                .get(JnjlaglobalreportsaddonXLSPDFConstants.OPEN_ORDERS_REPORT_FORM_NAME);
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + JnjlaglobalreportsaddonXLSPDFConstants.EXCEL_FILE_NAME);
        Map<String, String> mapShowColumns = new HashMap<>();
        Map<String, String> mapShowColumnsReceive = new HashMap<>();
        try {
            String[] selectedReportcolumns = searchCriteria.getReportColumns().split(",");
            if (null != selectedReportcolumns && ArrayUtils.isNotEmpty(selectedReportcolumns) && selectedReportcolumns.length > 0) {
                for (String reportColumn : selectedReportcolumns) {
                    mapShowColumnsReceive = getShowColumnsDetails(reportColumn, mapShowColumns);
                }
            }
            final Sheet sheet = hssfWorkbook.createSheet(SHEET_NAME);
            setHeaderImage(hssfWorkbook, sheet, (InputStream) dataMap.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY));
            int rowNumber = 6;
            final Row header = sheet.createRow(rowNumber);
            final Font font = hssfWorkbook.createFont();
            font.setBold(true);
            final CellStyle style = hssfWorkbook.createCellStyle();
            style.setFont(font);
            setHeaderTitleInfo(header, mapShowColumnsReceive);
            setHeaderTitleInfoNext(header, mapShowColumnsReceive);
            setDataToExcelSheetExecute(jnjLaOpenOrdersReportResponseDataList, mapShowColumnsReceive, sheet, rowNumber);
        } catch (final Exception exception) {
            LOG.error(METHOD_NAME + Logging.HYPHEN + "Exception in creating OPEN ORDER EXCEL :: ", exception);
        }
    }

    /**
     * Set Data To Excel Sheet Execution
     * @param jnjLaOpenOrdersReportResponseDataList
     * @param mapShowColumnsReceive
     * @param sheet
     * @param rowNumber
     */
    private void setDataToExcelSheetExecute(final List<JnjLaOpenOrdersReportReponseData> jnjLaOpenOrdersReportResponseDataList, final Map<String, String> mapShowColumnsReceive, Sheet sheet, int entryRowNumber) {
        if (null != jnjLaOpenOrdersReportResponseDataList && CollectionUtils.isNotEmpty(jnjLaOpenOrdersReportResponseDataList)) {
            for (final JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata : jnjLaOpenOrdersReportResponseDataList) {
                entryRowNumber = setDataToExcelSheet(sheet, openOrdersReportResponsedata, mapShowColumnsReceive, entryRowNumber);
            }

        }
    }

    /**
     * Setting data to Excel Sheet
     *
     * @param sheet
     * @param openOrdersReportResponsedata
     * @param mapShowColumnsReceive
     * @param rowNum
     * @return
     */
    private int setDataToExcelSheet(final Sheet sheet, final JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata, final Map<String, String> mapShowColumnsReceive, int rowNum) {
        if (null != openOrdersReportResponsedata.getScheduleLines() && CollectionUtils.isNotEmpty(openOrdersReportResponsedata.getScheduleLines())) {
            for (JnjDeliveryScheduleData deliveryScheduleData : openOrdersReportResponsedata.getScheduleLines()){
                rowNum = rowNum + 1;
                final Row row = sheet.createRow(rowNum);
                setDataToExcelSheetInfo(row, mapShowColumnsReceive, openOrdersReportResponsedata);
                setDataToExcelSheetInfoNext(row, mapShowColumnsReceive, openOrdersReportResponsedata, deliveryScheduleData);
            }
        } else if (null != openOrdersReportResponsedata.getScheduleLines() && CollectionUtils.isEmpty(openOrdersReportResponsedata.getScheduleLines())) {
            rowNum = rowNum + 1;
            final Row row = sheet.createRow(rowNum);
            setDataToExcelSheetInfo(row, mapShowColumnsReceive, openOrdersReportResponsedata);
            setDataToExcelSheetInfoScheduleLineEmpty(row, mapShowColumnsReceive, openOrdersReportResponsedata);
        }
        return rowNum;
    }

    /**
     * @param hssfWorkbook
     * @param sheet
     * @param inputStream  Setting Header Image
     */
    public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final InputStream inputStream) {
        int index = 0;
        Drawing drawing;
        try {
            final byte[] bytes = IOUtils.toByteArray(inputStream);
            index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
            inputStream.close();
        } catch (final IOException ioException) {
            LOG.error("Exception occured during input output operation in the method setHeaderImage()", ioException);
        }
        final CreationHelper helper = hssfWorkbook.getCreationHelper();
        drawing = sheet.createDrawingPatriarch();
        final ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setCol2(10);
        anchor.setRow1(1);
        anchor.setRow2(5);
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
        drawing.createPicture(anchor, index);
    }

    /**
     * Get Show Columns Details
     *
     * @param reportColumn
     * @param mapShowColumns
     * @return
     */
    private Map<String, String> getShowColumnsDetails(final String reportColumn, Map<String, String> mapShowColumns) {
        switch (reportColumn) {
            case JnjlaglobalreportsaddonXLSPDFConstants.SOLDTONAME_COL_VAL:
                mapShowColumns.put(SOLD_TO_NAME_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            case JnjlaglobalreportsaddonXLSPDFConstants.ORDERREFERENCE_COL_VAL:
                mapShowColumns.put(ORDER_REFERENCE_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            case JnjlaglobalreportsaddonXLSPDFConstants.EAN_COL_VAL:
                mapShowColumns.put(EAN_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            case JnjlaglobalreportsaddonXLSPDFConstants.SUBFRANCHISE_COL_VAL:
                mapShowColumns.put(SUB_FRANCHISE_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            case JnjlaglobalreportsaddonXLSPDFConstants.TOTALUNITSQUANTITY_COL_VAL:
                mapShowColumns.put(TOTAL_UNITS_QUANTITY_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            case JnjlaglobalreportsaddonXLSPDFConstants.UNITOFMEASURE_COL_VAL:
                mapShowColumns.put(UNIT_OF_MEASURE_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            case JnjlaglobalreportsaddonXLSPDFConstants.ORDERQTYSALESUNITS_COL_VAL:
                mapShowColumns.put(ORDER_QTY_SALES_UNITS_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            default:
        }
        getShowColumnsDetailsNext(reportColumn, mapShowColumns);
        return mapShowColumns;
    }

    /**
     * Get Show Columns Details Second Part
     *
     * @param reportColumn
     * @param mapShowColumns
     */
    private void getShowColumnsDetailsNext(final String reportColumn, Map<String, String> mapShowColumns) {
        switch (reportColumn) {
            case JnjlaglobalreportsaddonXLSPDFConstants.OPENORDER_COL_VAL:
                mapShowColumns.put(OPEN_ORDER_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            case JnjlaglobalreportsaddonXLSPDFConstants.ORDERCREATIONDATE_COL_VAL:
                mapShowColumns.put(ORDER_CREATION_DATE_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            case JnjlaglobalreportsaddonXLSPDFConstants.DATEREQDATE_COL_VAL:
                mapShowColumns.put(REG_DELIVERY_DATE_COL_KEY, SHOW_COLUMNS_VAL);
                break;
            default:
        }
    }

    /**
     * Header title for Open Orders Reports First part
     *
     * @param header
     * @param mapShowColumnsReceive
     */
    public void setHeaderTitleInfo(Row header, final Map<String, String> mapShowColumnsReceive) {
        header.createCell(0).setCellValue(messageSource.getMessage(SOLD_TO_COL, null, i18nService.getCurrentLocale()));
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
            header.createCell(1).setCellValue(messageSource.getMessage(SOLD_TO_NAME_COL, null, i18nService.getCurrentLocale()));
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
            header.createCell(2).setCellValue(messageSource.getMessage(SHIP_TO_COL, null, i18nService.getCurrentLocale()));
            header.createCell(3).setCellValue(messageSource.getMessage(SHIP_TO_NAME_COL, null, i18nService.getCurrentLocale()));
        } else {
            header.createCell(1).setCellValue(messageSource.getMessage(SHIP_TO_COL, null, i18nService.getCurrentLocale()));
            header.createCell(2).setCellValue(messageSource.getMessage(SHIP_TO_NAME_COL, null, i18nService.getCurrentLocale()));
        }
        setHeaderTitleInfoColumnAdjustedHeaderForOrderReference(header, mapShowColumnsReceive);
    }

    /**
     * Setting Header Title Info Column Adjusted Header For Order Reference
     * @param header
     * @param mapShowColumnsReceive
     */
    private void setHeaderTitleInfoColumnAdjustedHeaderForOrderReference(Row header, Map<String, String> mapShowColumnsReceive) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_REFERENCE_COL_KEY))) {
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                header.createCell(4).setCellValue(messageSource.getMessage(ORDER_REFERENCE_COL, null, i18nService.getCurrentLocale()));
            } else {
                header.createCell(3).setCellValue(messageSource.getMessage(ORDER_REFERENCE_COL, null, i18nService.getCurrentLocale()));
            }
        }
    }

    /**
     * Header title for Open Orders Reports Next part
     *
     * @param header
     * @param mapShowColumnsReceive
     */
    public void setHeaderTitleInfoNext(Row header, final Map<String, String> mapShowColumnsReceive) {
        int i = 4;
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_REFERENCE_COL_KEY))) {
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                i=5;
            }
        }else if (!SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
            i = 3;
        }
        setHeaderTitleInfoNextColumnAdjustedDynamically(header,i);
        setHeaderTitleInfoNextColumnAdjustedForEan(header, mapShowColumnsReceive);
        setHeaderTitleInfoNextColumnAdjustedForSubFranchise(header, mapShowColumnsReceive);
        setHeaderTitleInfoNextColumnAdjustedForTotalUnitQty(header, mapShowColumnsReceive);
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(UNIT_OF_MEASURE_COL_KEY))) {
            header.createCell(15).setCellValue(messageSource.getMessage(UNIT_OF_MEASURE_COL, null, i18nService.getCurrentLocale()));
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_QTY_SALES_UNITS_COL_KEY))) {
            header.createCell(16).setCellValue(messageSource.getMessage(ORDER_QTY_SALES_UNITS_COL, null, i18nService.getCurrentLocale()));
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(OPEN_ORDER_COL_KEY))) {
            header.createCell(17).setCellValue(messageSource.getMessage(OPEN_ORDER_COL, null, i18nService.getCurrentLocale()));
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_CREATION_DATE_COL_KEY))) {
            header.createCell(18).setCellValue(messageSource.getMessage(ORDER_CREATION_DATE_COL, null, i18nService.getCurrentLocale()));
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(REG_DELIVERY_DATE_COL_KEY))) {
            header.createCell(19).setCellValue(messageSource.getMessage(DATE_REQ_DATE_COL, null, i18nService.getCurrentLocale()));
        }
    }
    /**
     * Setting Header Title Info Next Column Adjusted Dynamically
     * @param header
     * @param i
     */
    private void setHeaderTitleInfoNextColumnAdjustedDynamically(Row header, int i) {
        header.createCell(i).setCellValue(messageSource.getMessage(JNJ_ORDER_NUMBER_COL, null, i18nService.getCurrentLocale()));
        header.createCell(i + 1).setCellValue(messageSource.getMessage(ORDER_TYPE_COL, null, i18nService.getCurrentLocale()));
        header.createCell(i + 2).setCellValue(messageSource.getMessage(PRODUCT_CODE_COL, null, i18nService.getCurrentLocale()));
        header.createCell(i + 3).setCellValue(messageSource.getMessage(PRODUCT_DESCRIPTION_COL, null, i18nService.getCurrentLocale()));
        header.createCell(i + 4).setCellValue(messageSource.getMessage(LINE_NUMBER_COL, null, i18nService.getCurrentLocale()));
        header.createCell(i + 5).setCellValue(messageSource.getMessage(SCHEDULE_LINE_COL, null, i18nService.getCurrentLocale()));
        header.createCell(i + 6).setCellValue(messageSource.getMessage(ESTI_DELIVERY_DATE, null, i18nService.getCurrentLocale()));
    }
    /**
     * Setting Header Title Info Next Column Adjusted For Total Unit Qty
     * @param header
     * @param mapShowColumnsReceive
     */
    private void setHeaderTitleInfoNextColumnAdjustedForTotalUnitQty(Row header, Map<String, String> mapShowColumnsReceive) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(TOTAL_UNITS_QUANTITY_COL_KEY))) {
            header.createCell(14).setCellValue(messageSource.getMessage(TOTAL_UNITS_QUANTITY_COL, null, i18nService.getCurrentLocale()));
        }
    }

    /**
     * Setting Header Title Info Next Column Adjusted For Sub Franchise
     * @param header
     * @param mapShowColumnsReceive
     */
    private void setHeaderTitleInfoNextColumnAdjustedForSubFranchise(Row header, Map<String, String> mapShowColumnsReceive) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SUB_FRANCHISE_COL_KEY))) {
            header.createCell(13).setCellValue(messageSource.getMessage(SUB_FRANCHISE_COL, null, i18nService.getCurrentLocale()));
        }
    }

    /**
     * Setting Header Title Info Next Column Adjusted For Ean
     * @param header
     * @param mapShowColumnsReceive
     */
    private void setHeaderTitleInfoNextColumnAdjustedForEan(Row header, Map<String, String> mapShowColumnsReceive) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(EAN_COL_KEY))) {
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_REFERENCE_COL_KEY))) {
                if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                    header.createCell(12).setCellValue(messageSource.getMessage(EAN_1, null, i18nService.getCurrentLocale()));
                }else{
                    header.createCell(11).setCellValue(messageSource.getMessage(EAN_1, null, i18nService.getCurrentLocale()));
                }
            }else{
                if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                    header.createCell(11).setCellValue(messageSource.getMessage(EAN_1, null, i18nService.getCurrentLocale()));
                }else{
                    header.createCell(10).setCellValue(messageSource.getMessage(EAN_1, null, i18nService.getCurrentLocale()));
                }
            }
        }
    }
    /**
     * Set Data To Excel Sheet For Open Orders Report First part
     *
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     */
    public void setDataToExcelSheetInfo(Row row, final Map<String, String> mapShowColumnsReceive, final JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        row.createCell(0).setCellValue(openOrdersReportResponsedata.getAccountNumber());
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
            row.createCell(1).setCellValue(openOrdersReportResponsedata.getAccountName());
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
            row.createCell(2).setCellValue(openOrdersReportResponsedata.getShipToAccount());
            row.createCell(3).setCellValue(openOrdersReportResponsedata.getShipToName());
        } else {
            row.createCell(1).setCellValue(openOrdersReportResponsedata.getShipToAccount());
            row.createCell(2).setCellValue(openOrdersReportResponsedata.getShipToName());
        }
        setDataToExcelSheetInfoColumnAdjustedForOrderReference(row, mapShowColumnsReceive, openOrdersReportResponsedata);
    }

    /**
     * Seting tData To Excel Sheet Info Row Adjusted For Order Reference
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoColumnAdjustedForOrderReference(Row row, Map<String, String> mapShowColumnsReceive, JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_REFERENCE_COL_KEY))) {
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                row.createCell(4).setCellValue(openOrdersReportResponsedata.getCustomerPO());
            } else {
                row.createCell(3).setCellValue(openOrdersReportResponsedata.getCustomerPO());
            }
        }
    }

    /**
     * Set Data To Excel Sheet For Open Orders Report Second part
     *
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     * @param deliveryScheduleData
     */
    public void setDataToExcelSheetInfoNext(Row row, final Map<String, String> mapShowColumnsReceive, final JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata, final JnjDeliveryScheduleData deliveryScheduleData) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_REFERENCE_COL_KEY))) {
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                setDataToExcelSheetInfoNextAdjustedRowsFromFive(row, openOrdersReportResponsedata, deliveryScheduleData);
            }else{
                setDataToExcelSheetInfoNextAdjustedRowsFromFour(row, openOrdersReportResponsedata, deliveryScheduleData);
            }
        }else{
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                setDataToExcelSheetInfoNextAdjustedRowsFromFour(row, openOrdersReportResponsedata, deliveryScheduleData);
            }else{
                setDataToExcelSheetInfoNextAdjustedRowsFromThree(row, openOrdersReportResponsedata, deliveryScheduleData);
            }
        }
        setDataToExcelSheetInfoNextAdjustedRowsForEAN(row, mapShowColumnsReceive, openOrdersReportResponsedata);
        setDataToExcelSheetInfoNextAdjustedRowsForSubFranchise(row, mapShowColumnsReceive, deliveryScheduleData);
        setDataToExcelSheetInfoNextAdjustedRowsForTotalUnitQty(row, mapShowColumnsReceive, deliveryScheduleData);
        setDataToExcelSheetInfoNextAdjustedRowsForUnitOfMeasure(row, mapShowColumnsReceive, openOrdersReportResponsedata);
        setDataToExcelSheetInfoNextAdjustedRowsForOrderQtySalesUnits(row, mapShowColumnsReceive, deliveryScheduleData);
        setDataToExcelSheetInfoNextAdjustedRowsForQuantityPendingStock(row, mapShowColumnsReceive, deliveryScheduleData);
        setDataToExcelSheetInfoNextAdjustedRowsForOrderCreationDate(row, mapShowColumnsReceive, openOrdersReportResponsedata);
        setDataToExcelSheetInfoNextAdjustedRowsForRequestedDeliveryDate(row, mapShowColumnsReceive, openOrdersReportResponsedata);
    }

    /**
     * Set Data To Excel Sheet Info Next Adjusted Rows For Requested Delivery Date
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsForRequestedDeliveryDate(Row row, Map<String, String> mapShowColumnsReceive, JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(REG_DELIVERY_DATE_COL_KEY))) {
            row.createCell(19).setCellValue(openOrdersReportResponsedata.getRequestedDeliveryDate());
        }
    }

    /**
     * Setting Data To Excel Sheet Info Next Adjusted Rows For Order Creation Date
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsForOrderCreationDate(Row row, Map<String, String> mapShowColumnsReceive, JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_CREATION_DATE_COL_KEY))) {
            row.createCell(18).setCellValue(openOrdersReportResponsedata.getOrderDate());
        }
    }

    /**
     * Setting Data To Excel Sheet Info Next Adjusted Rows For Quantity Pending Stock
     * @param row
     * @param mapShowColumnsReceive
     * @param deliveryScheduleData
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsForQuantityPendingStock(Row row, Map<String, String> mapShowColumnsReceive, JnjDeliveryScheduleData deliveryScheduleData) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(OPEN_ORDER_COL_KEY))) {
            row.createCell(17).setCellValue(null != deliveryScheduleData.getQuantityPendingStock() ? deliveryScheduleData.getQuantityPendingStock():StringUtils.EMPTY);
        }
    }

    /**
     * Setting Data To Excel Sheet Info Next Adjusted Rows For OrderQty Sales Units
     * @param row
     * @param mapShowColumnsReceive
     * @param deliveryScheduleData
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsForOrderQtySalesUnits(Row row, Map<String, String> mapShowColumnsReceive, JnjDeliveryScheduleData deliveryScheduleData) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_QTY_SALES_UNITS_COL_KEY))) {
            row.createCell(16).setCellValue(null!= deliveryScheduleData.getAmountPendingDelivery()? deliveryScheduleData.getAmountPendingDelivery():StringUtils.EMPTY);
        }
    }

    /**
     * Setting Data To Excel Sheet Info Next Adjusted Rows For Unit Of Measure
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsForUnitOfMeasure(Row row, Map<String, String> mapShowColumnsReceive, JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(UNIT_OF_MEASURE_COL_KEY))) {
            row.createCell(15).setCellValue(openOrdersReportResponsedata.getUnit());
        }
    }

    /**
     * Set Data To Excel Sheet Info Next Adjusted Rows For Total Unit Qty
     * @param row
     * @param mapShowColumnsReceive
     * @param deliveryScheduleData
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsForTotalUnitQty(Row row, Map<String, String> mapShowColumnsReceive, JnjDeliveryScheduleData deliveryScheduleData) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(TOTAL_UNITS_QUANTITY_COL_KEY))) {
            if (null != deliveryScheduleData.getRequestedUnitsTotalQuantity()){
                row.createCell(14).setCellValue(deliveryScheduleData.getRequestedUnitsTotalQuantity());
            } else {
                row.createCell(14).setCellValue(StringUtils.EMPTY);
            }
        }
    }

    /**
     * Setting Data To Excel Sheet Info Next Adjusted Rows For Sub Franchise
     * @param row
     * @param mapShowColumnsReceive
     * @param deliveryScheduleData
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsForSubFranchise(Row row, Map<String, String> mapShowColumnsReceive, JnjDeliveryScheduleData deliveryScheduleData) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SUB_FRANCHISE_COL_KEY))) {
            row.createCell(13).setCellValue(null!= deliveryScheduleData.getSubFranchise()? deliveryScheduleData.getSubFranchise(): StringUtils.EMPTY);
        }
    }

    /**
     * Setting Data To Excel Sheet Info Next Adjusted Rows For EAN
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsForEAN(Row row,
                        Map<String, String> mapShowColumnsReceive,
                        JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(EAN_COL_KEY))) {
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_REFERENCE_COL_KEY))) {
                if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                    row.createCell(12).setCellValue(openOrdersReportResponsedata.getProductGTIN());
                } else {
                    row.createCell(11).setCellValue(openOrdersReportResponsedata.getProductGTIN());
                }
            }else{
                if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                    row.createCell(11).setCellValue(openOrdersReportResponsedata.getProductGTIN());
                } else {
                    row.createCell(10).setCellValue(openOrdersReportResponsedata.getProductGTIN());
                }
            }
        }
    }

    /**
     * Setting Data To Excel Sheet Info Next Adjusted Rows From Three
     * @param row
     * @param openOrdersReportResponsedata
     * @param deliveryScheduleData
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsFromThree(Row row, JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata, JnjDeliveryScheduleData deliveryScheduleData) {
        row.createCell(3).setCellValue(openOrdersReportResponsedata.getSapOrderNum());
        row.createCell(4).setCellValue(openOrdersReportResponsedata.getOrderType());
        row.createCell(5).setCellValue(openOrdersReportResponsedata.getProductCode());
        row.createCell(6).setCellValue(openOrdersReportResponsedata.getProductName());
        row.createCell(7).setCellValue(openOrdersReportResponsedata.getLineNumber());
        row.createCell(8).setCellValue(deliveryScheduleData.getLineNumber());
        row.createCell(9).setCellValue(deliveryScheduleData.getFormattedDeliveryDate());
    }

    /**
     * Setting Data To Excel Sheet Info Next Adjusted Rows From Four
     * @param row
     * @param openOrdersReportResponsedata
     * @param deliveryScheduleData
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsFromFour(Row row, JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata, JnjDeliveryScheduleData deliveryScheduleData) {
        row.createCell(4).setCellValue(openOrdersReportResponsedata.getSapOrderNum());
        row.createCell(5).setCellValue(openOrdersReportResponsedata.getOrderType());
        row.createCell(6).setCellValue(openOrdersReportResponsedata.getProductCode());
        row.createCell(7).setCellValue(openOrdersReportResponsedata.getProductName());
        row.createCell(8).setCellValue(openOrdersReportResponsedata.getLineNumber());
        row.createCell(9).setCellValue(deliveryScheduleData.getLineNumber());
        row.createCell(10).setCellValue(deliveryScheduleData.getFormattedDeliveryDate());
    }

    /**
     * Set Data To Excel Sheet Info Next Adjusted Rows From Five
     * @param row
     * @param openOrdersReportResponsedata
     * @param deliveryScheduleData
     */
    private static void setDataToExcelSheetInfoNextAdjustedRowsFromFive(Row row, JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata, JnjDeliveryScheduleData deliveryScheduleData) {
        row.createCell(5).setCellValue(openOrdersReportResponsedata.getSapOrderNum());
        row.createCell(6).setCellValue(openOrdersReportResponsedata.getOrderType());
        row.createCell(7).setCellValue(openOrdersReportResponsedata.getProductCode());
        row.createCell(8).setCellValue(openOrdersReportResponsedata.getProductName());
        row.createCell(9).setCellValue(openOrdersReportResponsedata.getLineNumber());
        row.createCell(10).setCellValue(deliveryScheduleData.getLineNumber());
        row.createCell(11).setCellValue(deliveryScheduleData.getFormattedDeliveryDate());
    }

    /**
     * Set Data To Excel Sheet for Open Orders Report ScheduleLine Empty Second part
     *
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     */
    public static void setDataToExcelSheetInfoScheduleLineEmpty(Row row,
                       final Map<String, String> mapShowColumnsReceive,
                       final JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_REFERENCE_COL_KEY))) {
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsFromFive(row, openOrdersReportResponsedata);
            }else{
                setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsFromFour(row, openOrdersReportResponsedata);
            }
        }else{
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsFromFour(row, openOrdersReportResponsedata);
            }else{
                setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsFromThree(row, openOrdersReportResponsedata);
            }
        }
        setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsForEAN(row, mapShowColumnsReceive, openOrdersReportResponsedata);
        setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsForSubFranchise(row, mapShowColumnsReceive);
        setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsForTotalUnitsQty(row, mapShowColumnsReceive);
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(UNIT_OF_MEASURE_COL_KEY))) {
            row.createCell(15).setCellValue(openOrdersReportResponsedata.getUnit());
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_QTY_SALES_UNITS_COL_KEY))) {
            row.createCell(16).setCellValue(StringUtils.EMPTY);
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(OPEN_ORDER_COL_KEY))) {
            row.createCell(17).setCellValue(StringUtils.EMPTY);
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_CREATION_DATE_COL_KEY))) {
            row.createCell(18).setCellValue(openOrdersReportResponsedata.getOrderDate());
        }
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(REG_DELIVERY_DATE_COL_KEY))) {
            row.createCell(19).setCellValue(openOrdersReportResponsedata.getRequestedDeliveryDate());
        }
    }

    /**
     * Setting Data To Excel Sheet Info Schedule Line Empty Adjusted Rows For Total Units Qty
     * @param row
     * @param mapShowColumnsReceive
     */
    private static void setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsForTotalUnitsQty(Row row, Map<String, String> mapShowColumnsReceive) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(TOTAL_UNITS_QUANTITY_COL_KEY))) {
            row.createCell(14).setCellValue(StringUtils.EMPTY);
        }
    }

    /**
     * Setting Data To Excel Sheet Info ScheduleLine Empty Adjusted Rows For Sub Franchise
     * @param row
     * @param mapShowColumnsReceive
     */
    private static void setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsForSubFranchise(Row row, Map<String, String> mapShowColumnsReceive) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SUB_FRANCHISE_COL_KEY))) {
            row.createCell(13).setCellValue(StringUtils.EMPTY);
        }
    }

    /**
     * Setting Data To Excel Sheet Info Schedule Line Empty Adjusted Rows For EAN
     * @param row
     * @param mapShowColumnsReceive
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsForEAN(Row row, Map<String,
            String> mapShowColumnsReceive, JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(EAN_COL_KEY))) {
            if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(ORDER_REFERENCE_COL_KEY))) {
                if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                    row.createCell(12).setCellValue(openOrdersReportResponsedata.getProductGTIN());
                } else {
                    row.createCell(11).setCellValue(openOrdersReportResponsedata.getProductGTIN());
                }
            } else {
                if (SHOW_COLUMNS_VAL.equalsIgnoreCase(mapShowColumnsReceive.get(SOLD_TO_NAME_COL_KEY))) {
                    row.createCell(11).setCellValue(openOrdersReportResponsedata.getProductGTIN());
                } else {
                    row.createCell(10).setCellValue(openOrdersReportResponsedata.getProductGTIN());
                }
            }
        }
    }

    /**
     * Setting Data To Excel Sheet Info Schedule Line Empty Adjusted Rows From Three
     * @param row
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsFromThree(Row row,
                                JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        row.createCell(3).setCellValue(openOrdersReportResponsedata.getSapOrderNum());
        row.createCell(4).setCellValue(openOrdersReportResponsedata.getOrderType());
        row.createCell(5).setCellValue(openOrdersReportResponsedata.getProductCode());
        row.createCell(6).setCellValue(openOrdersReportResponsedata.getProductName());
        row.createCell(7).setCellValue(openOrdersReportResponsedata.getLineNumber());
        row.createCell(8).setCellValue(StringUtils.EMPTY);
        row.createCell(9).setCellValue(openOrdersReportResponsedata.getEstimatedDeliveryDate());
    }

    /**
     * Setting Data To Excel Sheet Info Schedule Line Empty Adjusted Rows From Four
     * @param row
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsFromFour(Row row,
                        JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        row.createCell(4).setCellValue(openOrdersReportResponsedata.getSapOrderNum());
        row.createCell(5).setCellValue(openOrdersReportResponsedata.getOrderType());
        row.createCell(6).setCellValue(openOrdersReportResponsedata.getProductCode());
        row.createCell(7).setCellValue(openOrdersReportResponsedata.getProductName());
        row.createCell(8).setCellValue(openOrdersReportResponsedata.getLineNumber());
        row.createCell(9).setCellValue(StringUtils.EMPTY);
        row.createCell(10).setCellValue(openOrdersReportResponsedata.getEstimatedDeliveryDate());
    }

    /**
     * Setting Data To Excel Sheet Info Schedule Line Empty Adjusted Rows From Five
     * @param row
     * @param openOrdersReportResponsedata
     */
    private static void setDataToExcelSheetInfoScheduleLineEmptyAdjustedRowsFromFive(Row row,
                        JnjLaOpenOrdersReportReponseData openOrdersReportResponsedata) {
        row.createCell(5).setCellValue(openOrdersReportResponsedata.getSapOrderNum());
        row.createCell(6).setCellValue(openOrdersReportResponsedata.getOrderType());
        row.createCell(7).setCellValue(openOrdersReportResponsedata.getProductCode());
        row.createCell(8).setCellValue(openOrdersReportResponsedata.getProductName());
        row.createCell(9).setCellValue(openOrdersReportResponsedata.getLineNumber());
        row.createCell(10).setCellValue(StringUtils.EMPTY);
        row.createCell(11).setCellValue(openOrdersReportResponsedata.getEstimatedDeliveryDate());
    }

    public void setI18nService(final I18NService i18nService) {
        this.i18nService = i18nService;
    }

    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}
