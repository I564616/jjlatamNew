/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.services.impl;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.invoice.JnJUploadedInvoiceDateDao;
import com.jnj.la.core.data.JnJLaInvoiceRowError;
import com.jnj.la.core.enums.JnJFileError;
import com.jnj.la.core.enums.JnJUploadedFileStatus;
import com.jnj.la.core.model.JnJUploadedInvoiceDateModel;
import com.jnj.la.core.services.JnJUpdateInvoiceService;
import com.jnj.la.core.services.JnjLaInvoiceService;
import com.jnj.la.core.util.JnjLaUploadedInvoiceDateExcelUtil;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.ACTUAL_DELIVERY_DATE_INDEX;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.CLEANUP_DAYS_DEFAULT;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.DATES_IN_WRONG_FORMAT;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.ESTIMATED_DELIVERY_DATE_INDEX;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.FIRST_ROW;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.INVOICE_NUMBER_INDEX;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.UploadedInvoiceFile.INVOICE_NUMBER_NOT_FOUND;

public class JnJUpdateInvoiceServiceImpl implements JnJUpdateInvoiceService {

    private static final Logger LOG = Logger.getLogger(JnJUpdateInvoiceServiceImpl.class);

    private static final String PROCESS_FILE = "processFile()";

    private JnjLaInvoiceService invoiceService;

    private ModelService modelService;

    private JnJUploadedInvoiceDateDao uploadedInvoiceDateDao;

    private ConfigurationService configurationService;

    @Override
    public void processUploadedFiles(final JnjIntegrationRSACronJobModel model) {
        handlePending();
        handleOldFiles();

        updateJobDate(model);
    }

    @Override
    public void uploadFile(final InputStream is, final String filename, final JnJB2bCustomerModel user) throws IOException {
        Files.copy(is, getPathByStatus(JnJUploadedFileStatus.PENDING, user.getPk().toString()).resolve(filename));

        final JnJUploadedInvoiceDateModel model = new JnJUploadedInvoiceDateModel();
        model.setErased(false);
        model.setCurrentStatus(JnJUploadedFileStatus.PENDING);
        model.setFilename(filename);
        model.setUser(user);

        modelService.save(model);
    }

    @Override
    public File getUploadedFile(final JnJUploadedInvoiceDateModel model) throws FileNotFoundException {
        final File file = getPathByStatus(model.getCurrentStatus(), model.getUser().getPk().toString()).resolve(model.getFilename()).toFile();

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        return file;
    }

    @Override
    public File getErrorFile(final JnJUploadedInvoiceDateModel model) throws FileNotFoundException {
        final String errorFilename = JnjLaUploadedInvoiceDateExcelUtil.getErrorFileName(model.getFilename());
        final File file = getPathByStatus(model.getCurrentStatus(), model.getUser().getPk().toString()).resolve(errorFilename).toFile();

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        return file;
    }

    private void handlePending() {
        final List<JnJUploadedInvoiceDateModel> uploadedFiles = uploadedInvoiceDateDao.getPendingFiles();
        LOG.info("Found " + uploadedFiles.size() + " uploaded pending files");

        uploadedFiles.forEach(this::processUploadedFile);
    }

    private void processUploadedFile(final JnJUploadedInvoiceDateModel model) {
        startProcess(model);

        final Path pendingPath = getPathByStatus(JnJUploadedFileStatus.PENDING, model.getUser().getPk().toString());

        final Path filePath = pendingPath.resolve(model.getFilename());
        if (filePath.toFile().exists()) {
            model.setCurrentStatus(JnJUploadedFileStatus.SUCCESS);

            final List<JnJLaInvoiceRowError> errors = processFile(model, filePath.toFile());

            if (CollectionUtils.isNotEmpty(errors)) {
                final Path errorFile = getPathByStatus(JnJUploadedFileStatus.ERROR, model.getUser().getPk().toString());
                JnjLaUploadedInvoiceDateExcelUtil.generateErrorFile(errorFile, model, errors);
                model.setCurrentStatus(JnJUploadedFileStatus.ERROR);
                model.setErrorMessage(JnJFileError.PARTIALLY_PROCESSED);
            }

            moveFileToStatusFolder(filePath, model);

        } else {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, PROCESS_FILE, filePath.toString() + " does not exist", JnJUpdateInvoiceServiceImpl.class);
            model.setCurrentStatus(JnJUploadedFileStatus.ERROR);
            model.setErrorMessage(JnJFileError.NOT_FOUND);
        }

        modelService.save(model);
    }

    private void moveFileToStatusFolder(final Path source, final JnJUploadedInvoiceDateModel model) {
        try {
            final Path destination = getPathByStatus(model.getCurrentStatus(), model.getUser().getPk().toString()).resolve(model.getFilename());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, PROCESS_FILE, "Unable to move file to status folder", e, JnJUpdateInvoiceServiceImpl.class);
        }
    }

    private void startProcess(final JnJUploadedInvoiceDateModel model) {
        model.setCurrentStatus(JnJUploadedFileStatus.RUNNING);
        modelService.save(model);
    }

    private List<JnJLaInvoiceRowError> processFile(final JnJUploadedInvoiceDateModel uploaded, final File file) {
        final List<JnJLaInvoiceRowError> errors = new ArrayList<>();

        try (final FileInputStream fis = new FileInputStream(file)) {
            final Iterator<Row> rows = JnjLaUploadedInvoiceDateExcelUtil.readRowsFromFile(fis);
            rows.forEachRemaining(r -> processRow(errors, r, uploaded));
        } catch (final IOException e) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, PROCESS_FILE, "Not able to read: " + uploaded.getFilename(), e, JnJUpdateInvoiceServiceImpl.class);
            uploaded.setCurrentStatus(JnJUploadedFileStatus.ERROR);
            uploaded.setErrorMessage(JnJFileError.UNABLE_TO_READ);
        }

        return errors;
    }

    private void processRow(final List<JnJLaInvoiceRowError> errors, final Row row, final JnJUploadedInvoiceDateModel uploaded) {
        if (row.getRowNum() >= FIRST_ROW && !isCellBlank(row, INVOICE_NUMBER_INDEX)) {
            try {
                final String invoiceNumber = getCellAsString(row, INVOICE_NUMBER_INDEX);
                final String ceddStr = getCellAsString(row, ESTIMATED_DELIVERY_DATE_INDEX);
                final String caddStr = getCellAsString(row, ACTUAL_DELIVERY_DATE_INDEX);

                final JnJInvoiceOrderModel invoice = invoiceService.getInvoicebyCode(invoiceNumber);
                if (invoice == null) {
                    errors.add(JnjLaUploadedInvoiceDateExcelUtil.createRowError(invoiceNumber, ceddStr, caddStr, INVOICE_NUMBER_NOT_FOUND));
                } else {
                    populateInvoice(uploaded, row, invoice, ceddStr, caddStr, errors);
                }
            } catch (Exception e) {
                JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, PROCESS_FILE, "Not able to process row data: " + row.toString(), e, JnJUpdateInvoiceServiceImpl.class);
            }
        }
    }

    private static String getCellAsString(Row row, int cell) {
        if (isCellBlank(row, cell)) {
            return null;
        }

        String result = row.getCell(cell).toString();
        if (result == null) {
            return null;
        }
        return result.trim();
    }

    private void populateInvoice(final JnJUploadedInvoiceDateModel model, final Row row, final JnJInvoiceOrderModel invoice,
                                 final String ceddStr, final String caddStr, final List<JnJLaInvoiceRowError> errors) {
        try {

            final Date estimatedDeliveryDate = getDateAt(row, ESTIMATED_DELIVERY_DATE_INDEX);
            final Date actualDeliveryDate = getDateAt(row, ACTUAL_DELIVERY_DATE_INDEX);

            invoice.setCarrierEstimateDeliveryDate(estimatedDeliveryDate);
            invoice.setCarrierConfirmedDeliveryDate(actualDeliveryDate);
            invoice.setUpdatedByFile(model);
            invoice.setUpdatedByFileDate(model.getCreationtime());

            modelService.save(invoice);

        } catch (final ParseException e) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, PROCESS_FILE, DATES_IN_WRONG_FORMAT, e, JnJUpdateInvoiceServiceImpl.class);
            errors.add(JnjLaUploadedInvoiceDateExcelUtil.createRowError(invoice.getInvDocNo(), ceddStr, caddStr, DATES_IN_WRONG_FORMAT));
        }
    }

    private Date getDateAt(final Row row, int cell) throws ParseException {
        if (isCellBlank(row, cell)) {
            return null;
        }

        if (CellType.NUMERIC == row.getCell(cell).getCellType()) {
            return row.getCell(cell).getDateCellValue();
        } else {
            return getValidatedDate(row, cell);
        }
    }

    private static boolean isCellBlank(Row row, int cell) {
        return row.getCell(cell) == null ||
            CellType.BLANK == row.getCell(cell).getCellType() ||
            StringUtils.isBlank(row.getCell(cell).toString());
    }

    private Date getValidatedDate(final Row row, int cell) throws ParseException {
        final String dateStr = row.getCell(cell).getStringCellValue();
        final String format = getConfiguration().getString(Jnjlab2bcoreConstants.UploadedInvoiceFile.DATE_FORMAT, "dd/MM/yyyy");
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        return sdf.parse(dateStr);
    }

    private void handleOldFiles() {
        final long ageInDays = getConfiguration().getLong(Jnjlab2bcoreConstants.UploadedInvoiceFile.CLEANUP_DAYS, CLEANUP_DAYS_DEFAULT);
        uploadedInvoiceDateDao.getUploadedOlderThan(ageInDays).forEach(this::deleteFiles);
    }

    private void deleteFiles(final JnJUploadedInvoiceDateModel model) {
        final Path path = getPathByStatus(model.getCurrentStatus(), model.getUser().getPk().toString());
        deleteFile(path.resolve(model.getFilename()));

        if (JnJUploadedFileStatus.ERROR.equals(model.getCurrentStatus()) && JnJFileError.PARTIALLY_PROCESSED.equals(model.getErrorMessage())) {
            deleteFile(path.resolve(JnjLaUploadedInvoiceDateExcelUtil.getErrorFileName(model.getFilename())));
        }

        model.setErased(Boolean.TRUE);
        modelService.save(model);
    }

    private static void deleteFile(Path file) {
        try {
            Files.delete(file);
        } catch (final IOException e) {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, PROCESS_FILE, "Unable to delete file", e, JnJUpdateInvoiceServiceImpl.class);
        }
    }

    private void updateJobDate(final JnjIntegrationRSACronJobModel model) {
        model.setLastSuccessFulRecordProcessTime(Timestamp.valueOf(LocalDateTime.now()));
        modelService.save(model);
    }

    private Path getPathByStatus(final JnJUploadedFileStatus status, final String... subFolders) {
        final String fileRoot = getConfiguration().getString(Jnjlab2bcoreConstants.FEED_FILEPATH_ROOT);
        final String uploadedFilePath = getConfiguration().getString(Jnjlab2bcoreConstants.UploadedInvoiceFile.UPLOADED_FILE_FOLDER);
        Path path = Path.of(fileRoot, uploadedFilePath, status.toString().toLowerCase());

        for (final String folder: subFolders) {
            path = path.resolve(folder);
        }

        path.toFile().mkdirs();
        return path;
    }

    private Configuration getConfiguration() {
        return configurationService.getConfiguration();
    }

    public void setInvoiceService(JnjLaInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public void setUploadedInvoiceDateDao(JnJUploadedInvoiceDateDao uploadedInvoiceDateDao) {
        this.uploadedInvoiceDateDao = uploadedInvoiceDateDao;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
