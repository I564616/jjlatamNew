/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.la.jnjlaservicepageaddon.controllers.pages;

import com.jnj.b2b.jnjlaordertemplate.constants.ContentType;
import com.jnj.b2b.jnjlaordertemplate.controllers.pages.AbstractJnjLaBasePageController;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.invoice.JnJUploadedInvoiceDateFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
@Scope("tenant")
@RequireHardLogIn
@RequestMapping(value = "/cls")
public class JnjLaClsPageController extends AbstractJnjLaBasePageController {

    private static final String CLS_PAGE = "addon:/jnjlaservicepageaddon/pages/cls/clsPage";
    private static final String CMS_PAGE = "jnjCLSPage";
    private static final String PAGE_HEADER = "pageHeader";
    private static final String FILE_LIST = "fileList";

    private static final Logger LOGGER = Logger.getLogger(JnjLaClsPageController.class);

    private static final String ERROR_DOWNLOAD_ORIGINAL_FILE = "clsPage.uploadDeliveryDates.download.originalFile.error";
    private static final String ERROR_DOWNLOAD_ERROR_FILE = "clsPage.uploadDeliveryDates.download.errorFile.error";
    private static final String ERROR_UPLOAD_FILE = "clsPage.uploadDeliveryDates.upload.error";
    private static final String SUCCESS_UPLOAD_FILE = "clsPage.uploadDeliveryDates.upload.ok";
    private static final String CLS_PATH = "/cls";

    @Autowired
    private JnJUploadedInvoiceDateFacade jnjUploadedInvoiceDateFacade;

    @GetMapping
    public String loadPage(final Model model) {
        preparePage(model, CMS_PAGE, Jnjlab2bcoreConstants.CLS_PAGE_HEADING);
        model.addAttribute(PAGE_HEADER, jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.CLS_UPLOAD_DELIVERY_DATES_HEADING));
        model.addAttribute(FILE_LIST, jnjUploadedInvoiceDateFacade.getUploadedInvoiceDates(getCurrentUser()));
        return CLS_PAGE;
    }

    @PostMapping("/delivery-dates-files")
    public String uploadFile(final RedirectAttributes model, @RequestParam(value = "deliveryDatesFile") final MultipartFile deliveryDatesFile) {
        try {
            jnjUploadedInvoiceDateFacade.uploadFile(deliveryDatesFile, getCurrentUser());
            addConfFlashMessage(model, SUCCESS_UPLOAD_FILE);
        } catch (BusinessException e) {
            handleExceptionWithFlashMessage(model, ERROR_UPLOAD_FILE, e);
        }
        return redirect(CLS_PATH);
    }

    @GetMapping("/delivery-dates-files/{id}")
    public void downloadOriginalFile(final Model model, @PathVariable("id") final String id, final HttpServletResponse response) {
        try {
            File file = jnjUploadedInvoiceDateFacade.getUploadedFile(id);
            downloadExcelFile(file, response);
        } catch (BusinessException e) {
            handleExceptionWithMessage(model, ERROR_DOWNLOAD_ORIGINAL_FILE, e);
        }
    }

    @GetMapping("/delivery-dates-files/{id}/errors")
    public void downloadErrorFile(final Model model, @PathVariable("id") final String id, final HttpServletResponse response) {
        try {
            File file = jnjUploadedInvoiceDateFacade.getErrorFile(id);
            downloadExcelFile(file, response);
        } catch (BusinessException e) {
            handleExceptionWithMessage(model, ERROR_DOWNLOAD_ERROR_FILE, e);
        }
    }

    private void downloadExcelFile(File file, HttpServletResponse response) throws BusinessException {
        try {
            downloadFile(file, ContentType.EXCEL, response);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BusinessException("IO error");
        }
    }

}
