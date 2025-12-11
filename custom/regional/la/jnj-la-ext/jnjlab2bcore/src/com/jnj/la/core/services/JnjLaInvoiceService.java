/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.services;

import com.jnj.core.dto.JnJInvoiceDTO;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.services.JnjInvoiceService;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;


/**
 * This is the interface for JnjInvoiceServiceImpl class containing the abstract methods for creating and saving the
 * Invoice Order and Entry Models into the database.
 *
 * @author mpanda3
 * @version 1.0
 */
public interface JnjLaInvoiceService extends JnjInvoiceService
{

	JnJInvoiceDTO getInvoiceRecordsFormRSA(String invoiceQuery);

	List<JnJInvoiceOrderModel> getInvoiceOrderData(final String invDocNo);

	SearchPageData<JnJInvoiceOrderModel> getInvoiceOrderData(final String searchBy, final String searchText, final String fromDate, final String toDate,
															final Integer pageSize, final Integer currentPage, final String sortColumn, final String sortDirection);

	SearchPageData<JnJInvoiceOrderModel> getInvoiceOrderData(final String searchBy, final String searchText, final String fromDate, final String toDate);

    /**
	  * To fetch invoice by order
	  * @param order the order is the list of order models
	  * @return List<JnJInvoiceOrderModel>
	  */
    List<JnJInvoiceOrderModel> getInvoicesByOrders(final List<OrderModel> order);
    
    /**
     * This method will be used to send the invoice notification email.
     * @param invoiceNumber pass the parameter to publish invoice notification email.
     */
    public void publishInvoiceNotificationEmail(final String invoiceNumber);    

}
