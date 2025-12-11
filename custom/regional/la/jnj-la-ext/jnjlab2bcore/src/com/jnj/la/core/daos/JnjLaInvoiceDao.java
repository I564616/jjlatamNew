/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.la.core.daos;

import com.jnj.core.dao.JnjInvoiceDao;
import com.jnj.core.dto.JnJInvoiceDTO;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.model.JnJInvoiceOrderModel;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

public interface JnjLaInvoiceDao extends JnjInvoiceDao {

    JnJInvoiceDTO getInvoiceRecordsFormRSA(final String invoiceQuery);

    List<JnJInvoiceOrderModel> getInvoiceOrderData(final String invDocNo);

    SearchPageData<JnJInvoiceOrderModel> getInvoiceOrderData(JnjPageableData pageableData);

    List<JnJInvoiceOrderModel> getInvoicesByOrders(List<OrderModel> order);
}
