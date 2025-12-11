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
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JnjLatamInvoiceHistoryPdfView extends AbstractPdfView {

    private static final Class<JnjLatamInvoiceHistoryPdfView> currentClass = JnjLatamInvoiceHistoryPdfView.class;
    private JnjCommonFacadeUtil jnjCommonFacadeUtil;
    private CommonI18NService commonI18NService;
    private JnjLatamInvoiceControllerUtil invoiceUtil;

    @Override
    protected Document newDocument() {
        return new Document(PageSize.A4.rotate());
    }

    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter,
                                    HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        final String methodName = "buildPdfDocument()";

        try{
            final JnjLatamAbstractExportViewUtil events =
                new JnjLatamAbstractExportViewUtil (getMessageSourceAccessor());
            JnjLatamAbstractExportViewUtil.addPdfPageNumber(document, pdfWriter, events);

            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=InvoiceHistorySearchResult.pdf");

            final SearchPageData<JnJLaInvoiceHistoryData> searchPageData =
                (SearchPageData<JnJLaInvoiceHistoryData>) JnjLaCommonUtil.getMapValue("searchPageData",map);

            final List<JnJLaInvoiceHistoryData> results = searchPageData != null ? searchPageData.getResults() : null;
            final Boolean resultLimitExceeded = JnjLaCommonUtil.getMapValue("resultLimitExceeded", map) != null;

            final PdfPTable table = JnjLatamAbstractExportViewUtil.setPdfImageHeader(map,822F,Boolean.TRUE,
                30f, Element.ALIGN_JUSTIFIED);
            final PdfPTable headerTable = new PdfPTable(1);
            JnjLatamAbstractExportViewUtil.setPdfTableProperties(822F, Boolean.TRUE,50f, Element.ALIGN_JUSTIFIED, headerTable);

            if (resultLimitExceeded.booleanValue()) {
                document.add(new Paragraph(JnjLatamAbstractExportViewUtil.RESULTS_EXCEEDED_MESSAGE));
                document.add(new Paragraph("\n"));
            }

            final PdfPCell headerCell = new PdfPCell(
                new Phrase(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_HEADING)));
            headerCell.setColspan(11);
            headerTable.addCell(headerCell);

            final PdfPTable invoiceTable = populatePdfHeader();
            populateFile(results, invoiceTable);

            if (invoiceUtil.hasAnyEmptyOrder(results)) {
                invoiceTable.addCell(getOrderNotLoadedMessage());
            }

            buildDocument(document, table, headerTable, invoiceTable);

        }catch (Exception exception){
            JnjGTCoreUtil.logErrorMessage("Build PDF Document", methodName, "Error while creating PDF file - ",
                exception, currentClass);
        }
    }

    private PdfPTable populatePdfHeader() {
        final PdfPTable invoiceTable = new PdfPTable(6);
        JnjLatamAbstractExportViewUtil
            .setPdfTableProperties(822F, Boolean.TRUE, 736f, Element.ALIGN_JUSTIFIED, invoiceTable);

        invoiceTable.addCell(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_INVOICE_NUMBER));
        invoiceTable.addCell(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_INVOICE_ORDER_NUMBER));
        invoiceTable.addCell(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_INVOICE_DATE));
        invoiceTable.addCell(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_ESTIMATED_DELIVERY_DATE));
        invoiceTable.addCell(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_ACTUAL_DELIVERY_DATE));
        invoiceTable.addCell(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_INVOICE_TOTAL));

        return invoiceTable;
    }

    private void buildDocument(final Document document, final PdfPTable table, final PdfPTable headerTable, final PdfPTable orderTable)
        throws DocumentException {
        final String methodName = "buildDocument()";
        try{
            document.add(table);
            document.add(headerTable);
            document.add(Chunk.NEWLINE);
            document.add(orderTable);
        }catch (DocumentException documentException){
            JnjGTCoreUtil.logErrorMessage("Building PDF Document", methodName,
                "Error while merging PDF components", documentException, currentClass);
        }
    }

    private PdfPCell getOrderNotLoadedMessage() {
        final String orderNotLoadedMessage = jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_ORDER_NOT_LOADED_MESSAGE);
        final PdfPCell pdfPCell = new PdfPCell(new Phrase(orderNotLoadedMessage));
        pdfPCell.setColspan(4);
        return pdfPCell;
    }

    private void populateFile(final List<JnJLaInvoiceHistoryData> results, PdfPTable invoiceTable){
        if (!CollectionUtils.isEmpty(results)) {
            for (final JnJLaInvoiceHistoryData data : results) {
                invoiceTable.addCell(StringUtils.defaultIfBlank(data.getInvoiceNumber(), StringUtils.EMPTY));
                invoiceTable.addCell(StringUtils.defaultIfBlank(data.getSapOrderNumber(), StringUtils.EMPTY));
                invoiceTable.addCell(getDate(data.getCreationDate()));
                invoiceTable.addCell(getDate(data.getCarrierEstimateDeliveryDate()));
                invoiceTable.addCell(getDate(data.getCarrierConfirmedDeliveryDate()));
                invoiceTable.addCell((data.getNetValue() != null) ? data.getNetValue().getFormattedValue() : StringUtils.EMPTY);
            }
        }
    }

    private String getDate(final Date date) {
        final SimpleDateFormat dateFormatBaseOnUserLanguage = JnJLALanguageDateFormatUtil.getDateFormatBaseOnUserLanguage(commonI18NService);
        return (date != null) ? dateFormatBaseOnUserLanguage.format(date) : StringUtils.EMPTY;
    }

    public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
        this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }

    public void setCommonI18NService(final CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }

    public void setInvoiceUtil(final JnjLatamInvoiceControllerUtil invoiceUtil) {
        this.invoiceUtil = invoiceUtil;
    }
}