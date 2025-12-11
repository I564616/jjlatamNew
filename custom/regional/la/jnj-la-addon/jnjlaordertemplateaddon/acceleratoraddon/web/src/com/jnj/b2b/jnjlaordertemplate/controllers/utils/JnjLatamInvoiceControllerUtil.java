package com.jnj.b2b.jnjlaordertemplate.controllers.utils;

import com.jnj.b2b.jnjlaordertemplate.controllers.pages.JnjLatamOrderHistoryPageController;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnJLaInvoiceHistoryData;
import com.jnj.facades.invoice.JnjLatamInvoiceFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.facade.nfe.JnjNfeFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class JnjLatamInvoiceControllerUtil {

    private static final String INVOICE_DOWNLOAD_ERROR_CODE = "label.myaccount.invoicedetail.errorForPDFOrXMLDownload";
    private static final String UNABLE_TO_GET_NFE_FILE = "Unable to get NF-e file: ";
    private static final String UNABLE_TO_COMPOSE_FILE_TO_USER = "Unable to compose file to user: ";
    private static final String INVOICE_ERROR_MESSAGE_ATTRIBUTE = "invoiceErrorMessage";
    private static final String ERROR_RESOLVING_INVOICE_FILE = "Error resolving invoice file";
    private static final String INVOICE_DOC_FILE_NOT_FOUND = "Invoice Doc file not found or returned null";
    private static final String XML = "XML";

    @Autowired
    private JnjLatamInvoiceFacade invoiceFacade;

    @Autowired
    private JnjNfeFacade nfeFacade;

    @Autowired
    protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

    public void prepareNfeFile(String invoiceNumber, HttpServletResponse httpServletResponse, RedirectAttributes redirectAttributes) {
        final String methodName = "nfeFile()";

        try {
            final File file = nfeFacade.getNfeFile(invoiceNumber);
            if (null == file || !file.exists()) {
                logDebugForInvoice(methodName, UNABLE_TO_GET_NFE_FILE);
                populateInvoiceErrorAsAttribute(redirectAttributes, jnjCommonFacadeUtil.getMessageFromImpex(INVOICE_DOWNLOAD_ERROR_CODE));
            } else {
                downloadInvoiceFile(XML, invoiceNumber, httpServletResponse, redirectAttributes, methodName, file);
            }
        } catch (final IntegrationException | JAXBException | BusinessException exception) {
            logErrorForInvoice(methodName, exception, UNABLE_TO_GET_NFE_FILE);
            populateInvoiceErrorAsAttribute(redirectAttributes, jnjCommonFacadeUtil.getMessageFromImpex(INVOICE_DOWNLOAD_ERROR_CODE));
        }
    }

    public Boolean prepareInvoiceDocFile(String downloadType, String invoiceNumber, HttpServletResponse httpServletResponse, RedirectAttributes redirectAttributes) {
        Boolean hasError = Boolean.FALSE;
        final String methodName = "downloadInvoiceDoc()";

        try {
            final File file = invoiceFacade.getInvoiceDocFile(downloadType, invoiceNumber);
            if (null == file || !file.exists()) {
                logDebugForInvoice(methodName, INVOICE_DOC_FILE_NOT_FOUND);
                populateInvoiceErrorAsAttribute(redirectAttributes, jnjCommonFacadeUtil.getMessageFromImpex(INVOICE_DOWNLOAD_ERROR_CODE));
            } else {
                downloadInvoiceFile(downloadType, invoiceNumber, httpServletResponse, redirectAttributes, methodName, file);
            }
        } catch (final BusinessException | IntegrationException exception) {
            hasError = Boolean.TRUE;
            logErrorForInvoice(methodName, exception, ERROR_RESOLVING_INVOICE_FILE);
            populateInvoiceErrorAsAttribute(redirectAttributes, jnjCommonFacadeUtil.getMessageFromImpex(INVOICE_DOWNLOAD_ERROR_CODE));
        }

        return hasError;
    }

    public boolean hasAnyEmptyOrder(final List<JnJLaInvoiceHistoryData> results) {
        return results != null && results.stream().filter(i -> !i.getOrderLoaded()).findAny().isPresent();
    }

    private void downloadInvoiceFile(final String downloadType, final String invoiceNumber, final HttpServletResponse httpServletResponse,
                                     final RedirectAttributes redirectAttributes, final String methodName, final File file) {
        try (final FileInputStream fileInputStream = new FileInputStream(file);
             final ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType(downloadType);
            httpServletResponse.setContentLength((int) file.length());
            if (StringUtils.isNotEmpty(downloadType) && WebConstants.InvoiceDetails.FILE_TYPE.contains(downloadType)) {
                httpServletResponse.setHeader(WebConstants.InvoiceDetails.HEADER_PARAM,WebConstants.InvoiceDetails.HEADER_PARAM_VALUE + invoiceNumber + WebConstants.InvoiceDetails.FILE_TYPE);
            } else {
                httpServletResponse.setHeader(WebConstants.InvoiceDetails.HEADER_PARAM, WebConstants.InvoiceDetails.HEADER_PARAM_VALUE + invoiceNumber + WebConstants.InvoiceDetails.PDF_FILE_TYPE);
            }
            StreamUtils.copy(fileInputStream, servletOutputStream);
        } catch (final IOException exception) {
            logErrorForInvoice(methodName, exception, UNABLE_TO_COMPOSE_FILE_TO_USER);
            populateInvoiceErrorAsAttribute(redirectAttributes, jnjCommonFacadeUtil.getMessageFromImpex(INVOICE_DOWNLOAD_ERROR_CODE));
        } finally {
            file.delete();
        }
    }

    private void logDebugForInvoice(final String methodName, final String message) {
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.GET_INVOICE_DOCUMENT, methodName,
                message, JnjLatamOrderHistoryPageController.class);
    }

    private void logErrorForInvoice(final String methodName, final Exception exception, final String message) {
        JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.GET_INVOICE_DOCUMENT, methodName,
                message, exception, JnjLatamOrderHistoryPageController.class);
    }

    private void populateInvoiceErrorAsAttribute(final RedirectAttributes redirectAttributes, final String errorMessage) {
        redirectAttributes.addFlashAttribute(INVOICE_ERROR_MESSAGE_ATTRIBUTE, errorMessage);
    }

}