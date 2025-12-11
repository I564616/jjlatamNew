/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.invoice;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.data.JnJUploadedInvoiceDateData;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface JnJUploadedInvoiceDateFacade {

    void uploadFile(final MultipartFile file, final JnJB2bCustomerModel user) throws BusinessException;

    List<JnJUploadedInvoiceDateData> getUploadedInvoiceDates(final JnJB2bCustomerModel user);

    File getUploadedFile(final String id) throws BusinessException;

    File getErrorFile(final String id) throws BusinessException;

}
