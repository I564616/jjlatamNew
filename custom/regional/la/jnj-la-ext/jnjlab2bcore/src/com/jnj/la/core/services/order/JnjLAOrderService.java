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
package com.jnj.la.core.services.order;

import com.jnj.core.dataload.mapper.PurchaseOrder;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.model.JnjOrderChannelModel;
import com.jnj.la.core.model.JnjOrderTypeModel;
import com.jnj.la.core.model.JnjUploadOrderModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


public interface JnjLAOrderService extends JnjGTOrderService {

	OrderModel getExistingOrderByHybrisOrderNumber(String orderNumber);

	JnjOrderTypeModel getOrderType(String orderTypeCode) throws BusinessException;

	CurrencyModel getCurrentCurrency(String salesOrgOrder);

	JnjOrderChannelModel getOrderChannel(String orderChannel) throws BusinessException;

	void sendOrderShipEmailNotification(final String invoiceNumber);

	boolean saveUploadEdiOrderFileToFTP(CommonsMultipartFile submitOrderFile);

	void createUploadOrderStatus(Map<String, String> fileStatusMap);

	List<File> getUploadOrderFiles();

	void processEdiFiles(List<File> uploadeFilesList);

	boolean updateUploadOrderStatus(Map<String, List<String>> fileNameDetailsMap, Map<String, String> fileStatusMap, Map<String, List<PurchaseOrder>> purchaseOrderMap);

	SearchPageData<JnjUploadOrderModel> getUploadOrderData(String sortflag, String filterFlag, PageableData pageableData);

	JnjUploadOrderModel getUploadOrderDataDetails(final String uploadedOrderId);

	List<JnjDropShipmentDetailsModel> getShippingDetails(List<String> materialIds);

	Date processOrderEntryStatus(Date lastUpdatedDate);

	Date processOrdersHeaderStatus(Date lastUpdatedDate);

	OrderStatus calculateOrderHeaderStatus(OrderModel order);

	void updateOrderEntryStatus(AbstractOrderEntryModel entry);

    void updateOrderHeaderStatus(OrderModel order);
    /**
	* To fetch recipients for mail
	* @param periodicity the periodicity is the periodicity selected by customer
	* @param b2bUnit the b2bUnit is the b2b Unit model
	* @return List<String>
	 */
	List<String> getAllRecipientsForImmediateOrDaily(final String periodicity,final B2BUnitModel b2bUnit);
	
	 /**
		* To fetch the user preference for mail
		* @param uid the uid is the id to be used
		* @param b2bUnit the b2bUnit is the b2b Unit model
		* @return boolean
		 */
	boolean getUserPreference(final String uid, final B2BUnitModel b2bUnit);
	
	/**
     * To send sap failed order report email.
     *
     */
    boolean sendSAPFailedOrdersReportEmail();

}
