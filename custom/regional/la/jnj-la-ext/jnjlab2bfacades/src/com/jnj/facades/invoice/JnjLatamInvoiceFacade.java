/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.invoice;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.facades.data.JnJLaInvoiceHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.data.JnJInvoiceOrderData;

public interface JnjLatamInvoiceFacade {

	List<JnJInvoiceOrderData> getInvoices(String salesOrder) throws BusinessException;

	/**
	 * The getInvoiceDocFile method calls the mapper and then calls the utility to read the xml or pdf file from the sftp
	 * location.
	 *
	 * @param fileType
	 *           the file type
	 * @param invoiceId
	 *           the invoice id
	 * @return the invoice doc file
	 * @throws BusinessException
	 *            the business exception
	 * @throws IntegrationException
	 *            the integration exception
	 */
	File getInvoiceDocFile(final String fileType, final String invoiceId) throws BusinessException, IntegrationException;


	Map<String, List<String>> uploadInvoiceFile(CommonsMultipartFile[] submitOrderFileArray,
			Map<String, String> fileStatusMap, String salesOrder, String poNum, List<JnJInvoiceOrderData> invoiceList)
			throws BusinessException;

	/**
	 * @param salesOrder
	 * @param fileName
	 * @return
	 * @throws BusinessException
	 */
	Map<File, String> downloadInvoiceFile(String salesOrder, String fileName) throws BusinessException;

	SearchPageData<JnJLaInvoiceHistoryData> getInvoiceOrderData(final String searchBy, final String searchText, final String fromDate, final String toDate,
																final Integer pageSize, final Integer currentPage, final String sortColumn, final String sortDirection);

	SearchPageData<JnJLaInvoiceHistoryData> getInvoiceOrderData(final String searchBy, final String searchText, final String fromDate, final String toDate);

    /**
	  * call the service method to get Invoices by orders
	  * @param order the order is the list of orders
	  * @return List<JnJInvoiceOrderModel>
	  */
	List<JnJInvoiceOrderModel> getInvoicesByOrders(final List<OrderModel> order);
}
