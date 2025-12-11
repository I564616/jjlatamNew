/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.dao.invoice;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.core.model.JnJUploadedInvoiceDateModel;

import java.util.List;

public interface JnJUploadedInvoiceDateDao {
    List<JnJUploadedInvoiceDateModel> getPendingFiles();

    List<JnJUploadedInvoiceDateModel> getUploadedOlderThan(Long ageInDays);

    List<JnJUploadedInvoiceDateModel> getUploadedByUser(JnJB2bCustomerModel user);

    JnJUploadedInvoiceDateModel getFileByUserAndFilename(JnJB2bCustomerModel user, String filename);

    JnJUploadedInvoiceDateModel getById(String id);
}
