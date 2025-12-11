/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 * @author prodri92
 */

package com.jnj.b2b.jnjlaordertemplate.controllers.pages;

import com.jnj.b2b.jnjlaordertemplate.controllers.utils.JnjLatamInvoiceControllerUtil;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.facades.data.JnJLaInvoiceHistoryData;
import com.jnj.facades.invoice.JnjLatamInvoiceFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SearchOption;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SearchOption.NONE;

@Controller
@Scope("tenant")
@RequireHardLogIn
@RequestMapping(value = "/invoice-history")
public class JnjLatamInvoiceHistoryPageController extends AbstractJnjLaBasePageController {

    private static final String INVOICE_HISTORY_CMS_PAGE = "jnjInvoiceHistoryPage";
    private static final String INVOICE_HISTORY_PAGE = "addon:/jnjlaordertemplateaddon/pages/invoice/invoiceHistoryPage";
    private static final String INVOICE_HISTORY_TABLE_PAGE = "addon:/jnjlaordertemplateaddon/pages/invoice/invoiceHistoryTablePage";
    private static final String INVOICE_HISTORY_EXCEL_VIEW = "jnjLatamInvoiceHistoryExcelView";
    private static final String INVOICE_HISTORY_PDF_VIEW = "jnjLatamInvoiceHistoryPdfView";
    private static final String INVOICE_HISTORY_REL_PATH = "/invoice-history";

    @Autowired
    private JnjLatamInvoiceFacade invoiceFacade;

    @Autowired
    private JnjLatamInvoiceControllerUtil invoiceUtil;

    @GetMapping
    public String getInvoiceHistory(final Model model) {
        preparePage(model, INVOICE_HISTORY_CMS_PAGE, Jnjlab2bcoreConstants.INVOICE_HISTORY_PAGE_HEADING);
        populateDropDownOptions(model);

        return INVOICE_HISTORY_PAGE;
    }

    @GetMapping("/search")
    public String search(
        @RequestParam(required = false, defaultValue = NONE) final String searchBy,
        @RequestParam(required = false) final String searchText,
        @RequestParam(required = false) final String fromDate,
        @RequestParam(required = false) final String toDate,
        @RequestParam(required = false, defaultValue = "10") final int pageSize,
        @RequestParam(required = false, defaultValue = "1") final int currentPage,
        @RequestParam(required = false) final String sortColumn,
        @RequestParam(required = false) final String sortDirection,
        final Model model) {

        final SearchPageData<JnJLaInvoiceHistoryData> invoiceOrderData = invoiceFacade.getInvoiceOrderData(searchBy, searchText, fromDate, toDate,
                pageSize, currentPage, sortColumn, sortDirection);
        if (CollectionUtils.isNotEmpty(invoiceOrderData.getResults())) {
            populateModel(model, invoiceOrderData, ShowMode.Page);
            handleSessionLanguage(model);
            populatePdfXmlDownloadFlag(model);
            if (invoiceUtil.hasAnyEmptyOrder(invoiceOrderData.getResults())) {
                addNotLoadedOrderMessage(model);
            }
        }

        return INVOICE_HISTORY_TABLE_PAGE;
    }

    @GetMapping("/download")
    public String download(
        @RequestParam(required = false, defaultValue = NONE) final String searchBy,
        @RequestParam(required = false) final String searchText,
        @RequestParam(required = false) final String fromDate,
        @RequestParam(required = false) final String toDate,
        @RequestParam(required = false) final DownloadType downloadType,
        final Model model) {

        final SearchPageData<JnJLaInvoiceHistoryData> invoiceOrderData = invoiceFacade.getInvoiceOrderData(searchBy, searchText, fromDate, toDate);
        if (CollectionUtils.isNotEmpty(invoiceOrderData.getResults())) {
            populateModel(model, invoiceOrderData, ShowMode.Page);
        }

        populateSiteLogoForDownload(model, downloadType);

        return DownloadType.PDF.equals(downloadType) ? INVOICE_HISTORY_PDF_VIEW : INVOICE_HISTORY_EXCEL_VIEW;
    }

    @GetMapping("/nfeFile")
    public String nfeFile(
        @RequestParam final String invoiceNumber,
        final HttpServletResponse httpServletResponse,
        final RedirectAttributes redirectAttributes) {

        invoiceUtil.prepareNfeFile(invoiceNumber, httpServletResponse, redirectAttributes);

        return REDIRECT_PREFIX + INVOICE_HISTORY_REL_PATH;
    }

    @GetMapping("/invoiceDocFile")
    public String invoiceDocFile(
        @RequestParam final String downloadType,
        @RequestParam final String invoiceNumber,
        final HttpServletResponse httpServletResponse,
        final RedirectAttributes redirectAttributes) {

        Boolean hasError = invoiceUtil.prepareInvoiceDocFile(downloadType, invoiceNumber, httpServletResponse, redirectAttributes);

        return hasError ? (REDIRECT_PREFIX + INVOICE_HISTORY_REL_PATH) : null; // redirect only if we had some error
    }

    private void populateDropDownOptions(final Model model) {
        addSearchOptions(model, SearchOption.INVOICE_NUMBER, SearchOption.ORDER_NUMBER,
            SearchOption.PO_NUMBER/*, SearchOption.PRODUCT_NUMBER, SearchOption.LOT_NUMBER*/);
    }

}