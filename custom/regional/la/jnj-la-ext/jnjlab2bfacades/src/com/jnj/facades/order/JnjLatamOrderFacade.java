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
package com.jnj.facades.order;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.core.dto.JnjOrderHistoryDTO;
import com.jnj.core.dto.OrderHistoryDTO;
/*import com.jnj.core.model.JnjOrderChannelModel;
 import com.jnj.core.model.JnjOrderOpsArchModel;
 import com.jnj.core.model.JnjOrderTypeModel;*/
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.data.JnjLatamUploadOrderData;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.data.JnjOrderHistoryData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;


/**
 *
 */
public interface JnjLatamOrderFacade
{



	/**
	 * This method will be invoke when the user click on the place order button and it will call the JnJSalesOrderMapper
	 * and JnJSalesOrder class.
	 *
	 * @param orderModel
	 *           the order model
	 * @return salesOrderCreationResponse
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws ParseException
	 *            the parse exception
	 */
	SalesOrderCreationResponse invokeSalesOrderCreationWrapper(final OrderModel orderModel)
			throws SystemException, IntegrationException, ParseException;

	/**
	 * This method will be invoke when the user click on the validation button and it will call the JnJSalesOrderMapper
	 * and JnJSalesOrder class.
	 *
	 * @param cartModel
	 *           the cart model
	 * @return boolean
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 * @throws TimeoutException
	 *            the timeout exception
	 * @throws ParseException
	 */
	JnjValidateOrderData invokeSalesOrderSimulateWrapper(final CartModel cartModel)
			throws IntegrationException, SystemException, TimeoutException;

	/**
	 * This method will be invoke when the user click on the Get Price icon and it will call the JnJSalesOrderMapper and
	 * JnJSalesOrder class.
	 *
	 * @param cartModel
	 *           the cart model
	 * @param orderEntryNumber
	 *           the order entry number
	 * @return salesOrderPricingResponse
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws TimeoutException
	 *            the timeout exception
	 */
	SalesOrderPricingResponse invokeSalesOrderPricingWrapper(final CartModel cartModel, final String orderEntryNumber)
			throws IntegrationException, TimeoutException;

	/**
	 * This method will be invoke all the order details at the time of page load..
	 *
	 * @param purchaseOrderNumber
	 *           the purchase order number
	 * @param orderNumber
	 *           the order number
	 * @return JnjOrderData
	 */
	JnjOrderData getOrderData(String purchaseOrderNumber, String orderNumber);


	/**
	 * Returns the order history of the current user for given statuses.
	 *
	 * @param pageableData
	 *           paging information
	 * @param orderHistoryDTO
	 *           the order history dto
	 * @param statuses
	 *           array of order statuses to filter the results
	 * @return The order history of the current user.
	 */
	SearchPageData<JnjOrderHistoryData> getPagedOrderHistoryForStatuses(PageableData pageableData, OrderHistoryDTO orderHistoryDTO,
			OrderStatus statuses);

	/**
	 * This method is used to get product codes for given order id, this method is used to load more product on order
	 * details page.
	 *
	 * @param orderId
	 *           the order id
	 * @return List<String>
	 */
	List<String> getProcuctCodes(String orderId);

	/**
	 * This method is used to get the status information on order history page.
	 *
	 * @param orders
	 *           List<JnjOrderHistoryData>
	 * @return Set of status
	 */
	Set<String> getAllStatus(final List<JnjOrderHistoryData> orders);

	/**
	 * Gets the latest order.
	 *
	 * @param numberOfEntries
	 *           the number of entries
	 * @return the latest order
	 */
	public OrderData getLatestOrder(final int numberOfEntries);

	/**
	 * Gets the default data.
	 *
	 * @return the default data
	 */
	OrderHistoryDTO getDefaultData();

	/**
	 * Gets the order by code.
	 *
	 * @param code
	 *           the code
	 * @return the order by code
	 */
	public JnjOrderData getOrderByCode(final String code);

	/**
	 * This method returns all the available header statuses used.
	 *
	 * @return the all header status
	 */
	Map<String, String> getAllHeaderStatus();

	/**
	 * This method returns all the available line level statuses used.
	 *
	 * @return the all line status
	 */
	Map<String, String> getAllLineStatus();

	/**
	 * This method is used to save the JnjOrdersOpsArchModel.
	 *
	 * @param jnjOrderOpsArchModel
	 *           the jnj order ops arch model
	 * @return true, if successful
	 */
	//boolean saveOrderOpsArchModel(JnjOrderOpsArchModel jnjOrderOpsArchModel);

	/**
	 * This method is used to save the OrderModel for status error.
	 *
	 * @param orderModel
	 *           the OrderModel
	 * @return true, if successful
	 */
	boolean saveOrderModel(OrderModel orderModel);

	/**
	 * This method is used to fetch all records from JnjUploadOrder model.
	 *
	 * @param sortflag
	 *            the sortflag
	 * @param pageableData
	 *            the pageable data
	 * @return SearchPageData<JnjUploadOrderData>
	 */
	SearchPageData<JnjLatamUploadOrderData> getUploadOrder(String sortflag, String filterFlag,
			PageableData pageableData);

	/**
	 * This method removes all the invalid order statuses(line level).
	 *
	 * @param validStates
	 *           the valid states
	 * @return the list
	 * @author balinder.singh
	 */
	public List<OrderEntryStatus> removeInValidLineStatuses(List<OrderEntryStatus> validStates);

	/**
	 * This method removes all the invalid order statuses(over all).
	 *
	 * @param validStates
	 *           the valid states
	 * @return the list
	 * @author balinder.singh
	 */
	public List<OrderStatus> removeInValidHeaderStatuses(List<OrderStatus> validStates);

	/**
	 * Skip validation.
	 *
	 * @return true, if successful
	 */
	public boolean skipValidation();

	/**
	 * Skip cart exception.
	 *
	 * @return true, if successful
	 */
	public boolean skipCartException();

	/**
	 * Show hide check out button.
	 *
	 * @param model
	 *           the model
	 */
	public void showHideCheckOutButton(final Model model);


	/**
	 * This method is used to fetch cart model for given code , This method is used to display client number on
	 * replenishment details page.
	 *
	 * @param code
	 *           the code
	 * @return CartModel
	 */
	CartModel getCartModelforCode(String code);

	/**
	 * This method to returns order from Hybris based on Searching And Sorting Options.
	 *
	 * @param pageableData
	 *           the pageable data
	 * @param jnjOrderHistoryDTO
	 *           the jnj order history dto
	 * @return the paged order history
	 */
	SearchPageData<JnjOrderHistoryData> getPagedOrderHistory(PageableData pageableData, JnjOrderHistoryDTO jnjOrderHistoryDTO);

	/**
	 * This method returns the JnjOrderChannel for the Given Code.
	 *
	 * @param orderChannelCode
	 *           the order channel code
	 * @return the order channel
	 * @throws BusinessException
	 *            the business exception
	 */
	//JnjOrderChannelModel getOrderChannel(String orderChannelCode) throws BusinessException;

	/**
	 * This method returns the JnjOrderType for the Given Code.
	 *
	 * @param orderTypeCode
	 *           the order type code
	 * @return the order type
	 * @throws BusinessException
	 *            the business exception
	 */
	//JnjOrderTypeModel getOrderType(String orderTypeCode) throws BusinessException;

	/**
	 * Returns the detail of an Order.
	 *
	 * @param code
	 *           The code of the Order for which to retrieve the detail.
	 * @return The detail of the order with matching code
	 */
	OrderData getOrderDetailsForCode(String code);

	/**
	 * Returns the detail of an uploaded Order.
	 *
	 * @param uploadedOrderId
	 *           The uploadOrderId of the uploaded Order for which to retrieve the detail.
	 * @return The detail of the uploaded order with matching uploadedOrderId
	 */
	public JnjLatamUploadOrderData getUploadOrderDetails(final String uploadedOrderId);

	public void modifyOrderStatus(final List<String> orderCodeList);

	public OrderData getOrderDetailsForCode(String orderCode, boolean ignoreRestriction);

	public void saveEdiFilesToFtp(CommonsMultipartFile[] submitOrderFileArray, Map<String, String> fileStatusMap);
	
	public String getCustomerFreightType(final String orderCode);
	
}
