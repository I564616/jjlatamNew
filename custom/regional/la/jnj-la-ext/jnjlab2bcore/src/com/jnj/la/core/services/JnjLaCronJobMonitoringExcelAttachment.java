/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.services;

import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;


/**
 * To create file for job monitoring email report attachment
 */

public class JnjLaCronJobMonitoringExcelAttachment
{
    private static final Logger LOGGER = Logger.getLogger(JnjLaCronJobMonitoringExcelAttachment.class);
    private static final String JOB_MONITORING_REPORT_DATA_LIST = "jobMonitoringData";
    private static final String SHEET_NAME = "Job Monitoring Report";
    private static final String FILE_EXTENSION = ".xls";
    private static final String HEADER_COUNTRY = "Country";
    private static final String HEADER_JOBNAME = "Job Name";
    private static final String HEADER_FREQUENCY = "Frequency";
    private static final String HEADER_SLT_STARTTIME = "Service Level Target Start Time (UTC)";
    private static final String HEADER_SLT_ENDTIME = "Service Level Target End Time (UTC)";
    private static final String HEADER_LASTSTATUS = "Last Status";
    private static final String HEADER_CURRENTSTATUS = "Current Status";
    private static final String HEADER_ACTUAL_STARTTIME = "Actual Start Time (UTC)";
    private static final String HEADER_ACTUAL_ENDTIME = "Actual End Time (UTC)";
    private static final String HEADER_REMARKS = "Remarks";
    private static final String STATUS_RUNNING = "RUNNING";
    private static final String STATUS_FAILED = "FAILED";
    private static final String STATUS_NOT_STARTED = "NOT STARTED";
    private static final String REMARKS_RUNNING_DELAY = "running delay";

    private static final int COLUMN_COUNT = 10;
    private static final int COLUMN_INDEX0 = 0;
    private static final int COLUMN_INDEX1 = 1;
    private static final int COLUMN_INDEX2 = 2;

    /**
     * Create excel file
     */
    public void buildJobMonitoringReportExcel(final Map<String, Object> outputMap, final HSSFWorkbook excelFile, String fileName)
    {
        generateFileJobReport(outputMap, excelFile);
        final String filepath = Config.getParameter(Jnjlab2bcoreConstants.JOB_MONITORING_EMAIL_ATTACHMENT_PATH);
        fileName = fileName + FILE_EXTENSION;
        try
        {
            writeExcel(excelFile, filepath + File.separator + fileName);
            excelFile.close();
        }
        catch (final IOException exception)
        {
            LOGGER.error("There was an error while trying to preform input output operations on the file", exception);
        }
    }

    private static void generateFileJobReport(final Map<String, Object> outputMap, final HSSFWorkbook excelFile)
    {
        final List<JnjLaCronJobMonitoringEmailDto> dtoList = (List<JnjLaCronJobMonitoringEmailDto>) outputMap
                .get(JOB_MONITORING_REPORT_DATA_LIST);

        final HSSFSheet searchSheet = excelFile.createSheet(SHEET_NAME);
        final HSSFRow header = searchSheet.createRow(0);

        final HSSFFont font = excelFile.createFont();
        font.setBold(true);

        final HSSFColor hssfColor = excelFile.getCustomPalette().findSimilarColor(255, 255, 255);
        font.setColor(hssfColor.getIndex());

        final HSSFCellStyle style = excelFile.createCellStyle();
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.JUSTIFY);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        final HSSFCellStyle styleCell = excelFile.createCellStyle();
        styleCell.setWrapText(true);

        createColumns(header, style);

        if (null != dtoList)
        {
            populateColumns(dtoList, searchSheet, styleCell, excelFile);

        }

    }


    private static void createColumns(final HSSFRow header, final HSSFCellStyle style)
    {


        header.createCell(0).setCellValue(HEADER_COUNTRY);
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue(HEADER_JOBNAME);
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue(HEADER_FREQUENCY);
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue(HEADER_SLT_STARTTIME);
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue(HEADER_SLT_ENDTIME);
        header.getCell(4).setCellStyle(style);

        header.createCell(5).setCellValue(HEADER_LASTSTATUS);
        header.getCell(5).setCellStyle(style);

        header.createCell(6).setCellValue(HEADER_CURRENTSTATUS);
        header.getCell(6).setCellStyle(style);

        header.createCell(7).setCellValue(HEADER_ACTUAL_STARTTIME);
        header.getCell(7).setCellStyle(style);

        header.createCell(8).setCellValue(HEADER_ACTUAL_ENDTIME);
        header.getCell(8).setCellStyle(style);

        header.createCell(9).setCellValue(HEADER_REMARKS);
        header.getCell(9).setCellStyle(style);
    }

    private static void populateColumns(final List<JnjLaCronJobMonitoringEmailDto> dtoList, final HSSFSheet searchSheet,
                                 final HSSFCellStyle styleCell, final HSSFWorkbook excelFile)
    {

        int rowNum = 1;
        for (final JnjLaCronJobMonitoringEmailDto dto : dtoList)
        {
            searchSheet.autoSizeColumn(rowNum);
            final HSSFRow row = searchSheet.createRow(rowNum++);
            row.setRowStyle(styleCell);
            row.createCell(0).setCellValue(dto.getCountry());
            row.createCell(1).setCellValue(dto.getJobCode());
            row.createCell(2).setCellValue(dto.getFrequency());
            row.createCell(3).setCellValue(dto.getServiceLevelStartTime());
            row.createCell(4).setCellValue(dto.getServiceLevelEndTime());
            row.createCell(5).setCellValue(dto.getLastStatus());
            final HSSFCell cell = row.createCell(6);
            cell.setCellValue(dto.getCurrentStatus());
            setStatusStyle(cell, excelFile, dto);
            row.createCell(7).setCellValue(dto.getActualStartTime());
            row.createCell(8).setCellValue(dto.getActualEndTime());
            row.createCell(9).setCellValue(dto.getRemarks());

        }
        setColumnWidth(searchSheet);
    }

    private static void setStatusStyle(final HSSFCell cell, final HSSFWorkbook excelFile, final JnjLaCronJobMonitoringEmailDto dto)
    {
        final HSSFCellStyle style = excelFile.createCellStyle();
        style.setWrapText(true);

        final HSSFFont font = excelFile.createFont();
        font.setBold(true);

        if (StringUtils.isNotEmpty(dto.getCurrentStatus()))
        {
            if (STATUS_RUNNING.equals(dto.getCurrentStatus()) && StringUtils.isNotEmpty(dto.getRemarks())
                    && dto.getRemarks().contains(REMARKS_RUNNING_DELAY))
            {
                style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setFont(font);
            }
            else if (STATUS_RUNNING.equals(dto.getCurrentStatus()))
            {
                style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setFont(font);
            }
            else if (STATUS_FAILED.equals(dto.getCurrentStatus()) || STATUS_NOT_STARTED.equals(dto.getCurrentStatus()))
            {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setFont(font);
            }
        }
        cell.setCellStyle(style);
    }


    /**
     * @param searchSheet
     *           the searchSheet to be adjusted
     */
    private static void setColumnWidth(final HSSFSheet searchSheet)
    {
        searchSheet.setColumnWidth(COLUMN_INDEX0, 2500);
        searchSheet.setColumnWidth(COLUMN_INDEX1, 10000);
        for (int i = COLUMN_INDEX2; i <= COLUMN_COUNT; i++)
        {
            searchSheet.setColumnWidth(i, 4500);
        }
    }

    private static void writeExcel(final HSSFWorkbook workbook, final String filePath)
    {
        final File file = new File(filePath);

        try (FileOutputStream out = new FileOutputStream(file))
        {
            workbook.write(out);
            out.flush();
        }
        catch (final IOException exception)
        {
            LOGGER.error("There was an error while trying to preform input output operations on the file", exception);
        }
    }
}
