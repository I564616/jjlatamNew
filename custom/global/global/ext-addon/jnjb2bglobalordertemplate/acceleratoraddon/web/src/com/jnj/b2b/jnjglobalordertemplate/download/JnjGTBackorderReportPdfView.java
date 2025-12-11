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
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTBackorderReportForm;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This class handles the PDF download for back-order report
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTBackorderReportPdfView extends AbstractPdfView
{
	protected static final Logger LOG = Logger.getLogger(JnjGTBackorderReportPdfView.class);
	protected static final String BACKORDER_RESPONSE_DATA_LIST = "jnjGTBackorderReportResponseDataList";
	protected static final String BACKORDER_FORM_NAME = "jnjGTBackorderReportForm";

	@Override
	protected Document newDocument()
	{
		return new Document(PageSize.A4.rotate());
	}

	/**
	 * This method generates the PDF doc
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(final Map<String, Object> arg0, final Document arg1, final PdfWriter arg2,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
	{
		try
		{
			arg4.setHeader("Content-Disposition", "attachment; filename=Backorder_Report.pdf");
			final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = (List<JnjGTBackorderReportResponseData>) arg0
					.get(BACKORDER_RESPONSE_DATA_LIST);
			final JnjGTBackorderReportForm searchCriteria = (JnjGTBackorderReportForm) arg0.get(BACKORDER_FORM_NAME);

			final PdfPTable searchCriteriaTable = new PdfPTable(3);
			searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			searchCriteriaTable.setTotalWidth(822F);
			searchCriteriaTable.setLockedWidth(true);
			searchCriteriaTable.setSpacingAfter(736f);

			final PdfPCell headerCell = new PdfPCell(new Phrase("BACKORDER REPORT SEARCH CRITERIA"));
			headerCell.setColspan(3);
			searchCriteriaTable.addCell(headerCell);
			searchCriteriaTable.addCell("Accounts");
			searchCriteriaTable.addCell("Start Date");
			searchCriteriaTable.addCell("End Date");
			// Comment to be removed after SAP Connection
			/*searchCriteriaTable.addCell(searchCriteria.getAccountIds());
			searchCriteriaTable.addCell(searchCriteria.getFromDate());
			searchCriteriaTable.addCell(searchCriteria.getToDate());
*/
			final PdfPTable backorderTable = new PdfPTable(12);
			backorderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			backorderTable.setTotalWidth(822F);
			backorderTable.setLockedWidth(true);
			backorderTable.setSpacingAfter(736f);
			final PdfPCell headerResultsCell = new PdfPCell(new Phrase("BACKORDER REPORT RESULTS"));
			headerResultsCell.setColspan(12);
			backorderTable.addCell(headerResultsCell);
			backorderTable.addCell("Account Number");
			backorderTable.addCell("Product Name");
			backorderTable.addCell("Product Code / GTIN");
			backorderTable.addCell("Operating Company");
			backorderTable.addCell("Customer PO");
			backorderTable.addCell("Order Number");
			backorderTable.addCell("Order Date");
			backorderTable.addCell("Estimated Availability");
			backorderTable.addCell("Qty");
			backorderTable.addCell("Unit");
			backorderTable.addCell("Item Price");
			backorderTable.addCell("Extended Price");

			final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);

			if (null != jnjGTBackorderReportResponseDataList)
			{
				double totals = 0;
				for (final JnjGTBackorderReportResponseData jnjGTBackorderReportResponseData : jnjGTBackorderReportResponseDataList)
				{
					backorderTable.addCell(jnjGTBackorderReportResponseData.getAccountNumber() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTBackorderReportResponseData.getAccountNumber())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getProductName() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTBackorderReportResponseData.getProductName())));
					backorderTable
							.addCell((jnjGTBackorderReportResponseData.getProductCode() == null && jnjGTBackorderReportResponseData
									.getProductGTIN() == null) ? pdfCell : new PdfPCell(new Phrase(jnjGTBackorderReportResponseData
									.getProductCode()
									+ Jnjb2bCoreConstants.CONST_COMMA
									+ jnjGTBackorderReportResponseData.getProductGTIN())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getOperatingCompany() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTBackorderReportResponseData.getOperatingCompany())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getCustomerPO() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTBackorderReportResponseData.getCustomerPO())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getOrderNumber() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTBackorderReportResponseData.getOrderNumber().split("\\|")[0])));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getOrderDate() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTBackorderReportResponseData.getOrderDate())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getEstimatedAvailability() == null ? pdfCell
							: new PdfPCell(new Phrase(jnjGTBackorderReportResponseData.getEstimatedAvailability())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getQty() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTBackorderReportResponseData.getQty())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getUnit() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTBackorderReportResponseData.getUnit())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getItemPrice() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTBackorderReportResponseData.getItemPrice())));
					backorderTable.addCell(jnjGTBackorderReportResponseData.getExtendedPrice() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTBackorderReportResponseData.getExtendedPrice())));

					if (StringUtils.isNotEmpty(jnjGTBackorderReportResponseData.getExtendedPrice()))
					{
						totals += Double.parseDouble(jnjGTBackorderReportResponseData.getExtendedPrice());
					}
				}
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(pdfCell);
				backorderTable.addCell(new Phrase("Totals"));
				backorderTable.addCell(new Phrase(String.valueOf(totals)));
			}
			arg1.add(searchCriteriaTable);
			arg1.add(backorderTable);
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating PDF - " + exception.getMessage());
		}
	}
}
