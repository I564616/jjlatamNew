/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.services;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.la.core.model.JnJUploadedInvoiceDateModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface JnJUpdateInvoiceService {

    void processUploadedFiles(final JnjIntegrationRSACronJobModel model);

    void uploadFile(final InputStream is, final String filename, final JnJB2bCustomerModel user) throws IOException;

    File getUploadedFile(final JnJUploadedInvoiceDateModel data) throws FileNotFoundException;

    File getErrorFile(final JnJUploadedInvoiceDateModel data) throws FileNotFoundException;
}