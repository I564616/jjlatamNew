/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalordertemplate.download;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTBackorderReportForm;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * This class handles the excel download for back-order report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTBackorderReportExcelView extends AbstractXlsView
{
	protected static final Logger LOG = Logger.getLogger(JnjGTBackorderReportExcelView.class);
	protected static final String BACKORDER_RESPONSE_DATA_LIST = "jnjGTBackorderReportResponseDataList";
	protected static final String BACKORDER_FORM_NAME = "jnjGTBackorderReportForm";

	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		try
		{
			arg3.setHeader("Content-Disposition", "attachment; filename=Backorder_Report.xls");
			final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = (List<JnjGTBackorderReportResponseData>) arg0
					.get(BACKORDER_RESPONSE_DATA_LIST);
			final JnjGTBackorderReportForm searchCriteria = (JnjGTBackorderReportForm) arg0.get(BACKORDER_FORM_NAME);

			final Sheet searchSheet = arg1.createSheet("Backorder Report Search Criteria");
			final Row searchHeader = searchSheet.createRow(0);
			searchHeader.createCell(0).setCellValue("Accounts");
			searchHeader.createCell(1).setCellValue("From Date");
			searchHeader.createCell(2).setCellValue("To Date");
			final Row searchRow = searchSheet.createRow(1);
			searchRow.createCell(0).setCellValue(searchCriteria.getAccountIds());
			searchRow.createCell(1).setCellValue(searchCriteria.getFromDate());
			searchRow.createCell(2).setCellValue(searchCriteria.getToDate());

			final Sheet sheet = arg1.createSheet("Backorder Report");
			final Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("Account Number");
			header.createCell(1).setCellValue("Product Name");
			header.createCell(2).setCellValue("Product Code / GTIN");
			header.createCell(3).setCellValue("Operating Company");
			header.createCell(4).setCellValue("Customer PO");
			header.createCell(5).setCellValue("Order Number");
			header.createCell(6).setCellValue("Order Date");
			header.createCell(7).setCellValue("Estimated Availability");
			header.createCell(8).setCellValue("Qty");
			header.createCell(9).setCellValue("Unit");
			header.createCell(10).setCellValue("Item Price");
			header.createCell(11).setCellValue("Extended Price");

			int rowNum = 1;
			if (null != jnjGTBackorderReportResponseDataList)
			{
				double totals = 0;
				for (final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData : jnjGTBackorderReportResponseDataList)
				{
					final Row row = sheet.createRow(rowNum++);

					row.createCell(0).setCellValue(jnjGTBackorderReportResponseData.getAccountNumber());
					row.createCell(1).setCellValue(jnjGTBackorderReportResponseData.getProductName());
					row.createCell(2).setCellValue(
							jnjGTBackorderReportResponseData.getProductCode() + Jnjb2bCoreConstants.CONST_COMMA
									+ jnjGTBackorderReportResponseData.getProductGTIN());
					row.createCell(3).setCellValue(jnjGTBackorderReportResponseData.getOperatingCompany());
					row.createCell(4).setCellValue(jnjGTBackorderReportResponseData.getCustomerPO());
					row.createCell(5).setCellValue(
							null != jnjGTBackorderReportResponseData.getOrderNumber() ? jnjGTBackorderReportResponseData
									.getOrderNumber().split("\\|")[0] : "");
					row.createCell(6).setCellValue(jnjGTBackorderReportResponseData.getOrderDate());
					row.createCell(7).setCellValue(jnjGTBackorderReportResponseData.getEstimatedAvailability());
					row.createCell(8).setCellValue(jnjGTBackorderReportResponseData.getQty());
					row.createCell(9).setCellValue(jnjGTBackorderReportResponseData.getUnit());
					row.createCell(10).setCellValue(jnjGTBackorderReportResponseData.getItemPrice());
					row.createCell(11).setCellValue(jnjGTBackorderReportResponseData.getExtendedPrice());

					if (StringUtils.isNotEmpty(jnjGTBackorderReportResponseData.getExtendedPrice()))
					{
						totals += Double.parseDouble(jnjGTBackorderReportResponseData.getExtendedPrice());
					}
				}
				final Row row = sheet.createRow(rowNum++);
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
				row.createCell(10).setCellValue("Totals");
				row.createCell(11).setCellValue(totals);
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating excel - " + exception.getMessage());
		}
	}
}
