/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.daos;

import com.jnj.core.dao.JnjOrderDao;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnjLaConsolidatedEmailProcessModel;
import com.jnj.la.core.model.JnjUploadOrderModel;
import com.jnj.la.core.model.JnjUploadOrderSHAModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JnjLaOrderDao extends JnjOrderDao
{

	JnJB2BUnitModel fetchAllSoldToNumberForFile(String validSoldToNumber);

	List<JnjUploadOrderSHAModel> getUploadOrderSHADetails(final String fileHash);

	AddressModel foundConversion(final String shipToNumber, final JnJB2BUnitModel jnJB2BUnitModel);

	List<AddressModel> fetchShipToAddress(final JnJB2BUnitModel jnJB2BUnitModel);

	List<JnJLaB2BUnitModel> fetchB2BUnitForCnpj(String cnpjValue);

	SearchPageData<JnjUploadOrderModel> getUploadOrderData(final String sortflag, final String filterFlag,
			final PageableData pageableData);

	JnjUploadOrderModel getUploadOrderDataDetails(final String uploadedOrderId);

	List<JnjDropShipmentDetailsModel> getShippingDetails(final List<String> materialIds);

    List<OrderEntryModel> getOrderEntryModelByModifiedTime(final Date lastUpdatedTime);

	List<OrderModel> getOrderModelByModifiedTimeInEntries(final Date lastUpdatedTime);

	OrderEntryStatus getOrderEntryStatusUsingPermutationMatrix(final AbstractOrderEntryModel orderLine);

	List<OrderModel> getOrdersByPeriodicity(final JnJEmailPeriodicity periodicity);

	List<OrderEntryModel> getPendingFromOrderEntries(final JnJEmailPeriodicity periodicity);

    List<OrderModel> getOrdersByPeriodicityAndStore(JnJEmailPeriodicity periodicity, String site);

	List<OrderModel> getOrdersForConsolidatedEmails(final List<String> countries, final List<String> excludeStatuses,final List<String> orderTypes);

	OrderModel getOrderBySapOrderNumber(final String sapOrderNumber);

	List<OrderEntryModel> getPendingFromOrderEntriesByStore(final JnJEmailPeriodicity periodicity,final String site,final List<OrderModel> orders);

    OrderStatus calculateOrderHeaderStatus(OrderModel order);

	Map<B2BUnitModel, List<JnJB2bCustomerModel>> getUsersForConsolidatedEmail();

	Map<B2BUnitModel, List<String>> getDefaultRecipientsForConsolidatedEmail();
	
	Map<B2BUnitModel, List<String>> getDefaultRecipientsForImmediateEmail(final B2BUnitModel b2bUnit);
	
	Map<B2BUnitModel, List<String>> getDefaultRecipientsForDailyEmail(final B2BUnitModel b2bUnit);

	List<OrderModel> getUpdatedOrders(final Date modifiedtime);

	List<OrderModel> getUpdatedInvoiceOrders(final Date modifiedtime);

	List<OrderModel> getOrdersForImmediateEmails(final JnJEmailPeriodicity periodicity, final List<String> excludeStatuses);

	Map<B2BUnitModel, List<JnJB2bCustomerModel>> getUsersForDailyEmail();

	Map<B2BUnitModel, List<JnJB2bCustomerModel>> getUsersForImmediateEmail();

	List<OrderModel> getOrdersForDailyEmails(final JnJEmailPeriodicity periodicity, final List<String> countries,
			final List<String> excludeStatuses);
	
	boolean getUserPreference(final String uid, final B2BUnitModel b2bUnit);

	List<JnJB2bCustomerModel> getRecipientsForDailyAndImmediateEmail(final B2BUnitModel b2bUnit,
			final String periodicity);

	public List<JnjLaConsolidatedEmailProcessModel> getJnjLaConsolidatedEmailProcessModels();
	
	public List<OrderModel> getFailedErpOrdersList(final List<String> orderCodes,final List<JnjOrderTypesEnum> orderTypes, final Date startDate, final Date endDate);
	
	List<OrderModel> findSAPFailedOrders(final Integer maxRetryAttempt,final String[] sapFailedOrderStatus);
}
