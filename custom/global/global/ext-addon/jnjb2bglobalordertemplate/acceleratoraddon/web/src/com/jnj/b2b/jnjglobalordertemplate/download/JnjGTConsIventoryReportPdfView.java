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
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTBackorderReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTConsignmentInventoryReportForm;
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
public class JnjGTConsIventoryReportPdfView extends AbstractPdfView
{
	protected static final Logger LOG = Logger.getLogger(JnjGTConsIventoryReportPdfView.class);
	protected static final String CONSIGNMENT_INVENTORY_DATA_LIST = "consInventoryReportList";
	protected static final String CONSIGNMENT_INVENTORY_REPORT_FORM_NAME = "jnjConsignmentInventoryReportForm";

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
			arg4.setHeader("Content-Disposition", "attachment; filename=Consignment_Report.pdf");
			final List<JnjGTConsInventoryData> jnjGTConsIneventoryReportDataList = (List<JnjGTConsInventoryData>) arg0
					.get(CONSIGNMENT_INVENTORY_DATA_LIST);
			//final JnjGTConsignmentInventoryReportForm searchCriteria = (JnjGTConsignmentInventoryReportForm) arg0.get(CONSIGNMENT_INVENTORY_REPORT_FORM_NAME);

			
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
			final PdfPCell headerResultsCell = new PdfPCell(new Phrase("CONSIGNMENT INVENTORY REPORT RESULTS"));
			headerResultsCell.setColspan(12);
			backorderTable.addCell(headerResultsCell);
			backorderTable.addCell("Stock Location Account");
			backorderTable.addCell("Stock Location Name");
			backorderTable.addCell("Franchise Description");
			backorderTable.addCell("Product Code");
			backorderTable.addCell("Product Description");
			backorderTable.addCell("Current Stock Qty");
			backorderTable.addCell("Par Level Qty");
			backorderTable.addCell("Available Order Qty");
			backorderTable.addCell("UOM");
			
			final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);

			if (null != jnjGTConsIneventoryReportDataList)
			{
				double totals = 0;
				for (final JnjGTConsInventoryData jnjGTConsIneventoryReportData : jnjGTConsIneventoryReportDataList)
				{
					backorderTable.addCell(jnjGTConsIneventoryReportData.getStockLocationAcc() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTConsIneventoryReportData.getStockLocationAcc())));
					backorderTable.addCell(jnjGTConsIneventoryReportData.getStockLocationName() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTConsIneventoryReportData.getStockLocationName())));
					backorderTable
							.addCell((jnjGTConsIneventoryReportData.getFranchiseDescription() == null && jnjGTConsIneventoryReportData
									.getFranchiseDescription() == null) ? pdfCell : new PdfPCell(new Phrase(jnjGTConsIneventoryReportData
									.getFranchiseDescription())));
									
					backorderTable.addCell(jnjGTConsIneventoryReportData.getProductCode() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTConsIneventoryReportData.getProductCode())));
					backorderTable.addCell(jnjGTConsIneventoryReportData.getProductDesc() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTConsIneventoryReportData.getProductDesc())));
					backorderTable.addCell(jnjGTConsIneventoryReportData.getQtyInStock() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTConsIneventoryReportData.getQtyInStock())));
					backorderTable.addCell(jnjGTConsIneventoryReportData.getParLevelQty() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTConsIneventoryReportData.getParLevelQty())));
					backorderTable.addCell(jnjGTConsIneventoryReportData.getAvailableOrderQty() == null ? pdfCell
							: new PdfPCell(new Phrase(jnjGTConsIneventoryReportData.getAvailableOrderQty())));
					backorderTable.addCell(jnjGTConsIneventoryReportData.getUom() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTConsIneventoryReportData.getUom())));
					

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
				
			}
			
			arg1.add(backorderTable);
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating PDF - " + exception.getMessage());
		}
	}
}
