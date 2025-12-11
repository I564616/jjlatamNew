/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.invoice.impl;

import com.jnj.commons.Severity;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.invoice.JnJUploadedInvoiceDateFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.invoice.JnJUploadedInvoiceDateDao;
import com.jnj.la.core.data.JnJUploadedInvoiceDateData;
import com.jnj.la.core.model.JnJUploadedInvoiceDateModel;
import com.jnj.la.core.services.JnJUpdateInvoiceService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JnJUploadedInvoiceDateFacadeImpl implements JnJUploadedInvoiceDateFacade {

    private static final String ERROR_DUPLICATED_FILE_MESSAGE = "Duplicated filename";
    private static final String ERROR_DUPLICATED_FILE_CODE = "clsPage.uploadDeliveryDates.exception.duplicated.file";
    private static final String ERROR_FILE_NOT_FOUND_CODE = "clsPage.uploadDeliveryDates.exception.file.not.found";
    private JnJUpdateInvoiceService updateInvoiceService;

    private Converter<JnJUploadedInvoiceDateModel, JnJUploadedInvoiceDateData> uploadedInvoiceDateConverter;

    private JnJUploadedInvoiceDateDao uploadedInvoiceDateDao;

    @Override
    public void uploadFile(final MultipartFile file, final JnJB2bCustomerModel user) throws BusinessException {
        if (uploadedInvoiceDateDao.getFileByUserAndFilename(user, file.getOriginalFilename()) != null) {
            throw createException(ERROR_DUPLICATED_FILE_MESSAGE, ERROR_DUPLICATED_FILE_CODE);
        }

        try (final InputStream is = file.getInputStream()) {
            updateInvoiceService.uploadFile(is, file.getOriginalFilename(), user);
        } catch (final IOException e) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, "uploadFile()", "Not able to save the uploaded file", e, JnJUploadedInvoiceDateFacadeImpl.class);
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public List<JnJUploadedInvoiceDateData> getUploadedInvoiceDates(final JnJB2bCustomerModel user) {
        return uploadedInvoiceDateConverter.convertAll(uploadedInvoiceDateDao.getUploadedByUser(user));
    }

    @Override
    public File getUploadedFile(final String id) throws BusinessException {
        try {
            return updateInvoiceService.getUploadedFile(getModelById(id));
        } catch (final FileNotFoundException e) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, "getUploadedFile()", "File not found", e, JnJUploadedInvoiceDateFacadeImpl.class);
            throw createException(e.getMessage(), ERROR_FILE_NOT_FOUND_CODE);
        }
    }

    @Override
    public File getErrorFile(final String id) throws BusinessException {
        try {
            return updateInvoiceService.getErrorFile(getModelById(id));
        } catch (final FileNotFoundException e) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, "getErrorFile()", "File not found", e, JnJUploadedInvoiceDateFacadeImpl.class);
            throw createException(e.getMessage(), ERROR_FILE_NOT_FOUND_CODE);
        }
    }

    private JnJUploadedInvoiceDateModel getModelById(String id) throws BusinessException {
        final JnJUploadedInvoiceDateModel model = uploadedInvoiceDateDao.getById(id);

        if (model == null) {
            throw new BusinessException("No uploaded file found for the given ID");
        }

        return model;
    }

    public void setUpdateInvoiceService(final JnJUpdateInvoiceService updateInvoiceService) {
        this.updateInvoiceService = updateInvoiceService;
    }

    public void setUploadedInvoiceDateConverter(Converter<JnJUploadedInvoiceDateModel, JnJUploadedInvoiceDateData> uploadedInvoiceDateConverter) {
        this.uploadedInvoiceDateConverter = uploadedInvoiceDateConverter;
    }

    public void setUploadedInvoiceDateDao(JnJUploadedInvoiceDateDao uploadedInvoiceDateDao) {
        this.uploadedInvoiceDateDao = uploadedInvoiceDateDao;
    }

    private static BusinessException createException(final String message, final String exceptionCode) {
        return new BusinessException(message, exceptionCode, Severity.BUSINESS_EXCEPTION);
    }
}
