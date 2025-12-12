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
package com.jnj.facades.order.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjOrderHistoryDTO;
import com.jnj.core.dto.OrderHistoryDTO;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjLaOrderData;
import com.jnj.facades.data.JnjLatamUploadOrderData;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.data.JnjOrderHistoryData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.daos.impl.DefaultLaJnjOrderDao;
import com.jnj.la.core.model.JnjUploadOrderModel;
import com.jnj.la.core.model.JnjUploadOrderSHAModel;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.la.core.services.order.JnjLatamEdiOrderService;
import com.jnj.la.core.util.JnjlatamOrderUtil;
import com.jnj.outboundservice.services.order.mapper.JnjLatamSalesOrderMapper;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultCompanyB2BCommerceService;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;


public class JnjLatamOrderFacadeImpl implements JnjLatamOrderFacade
{
	private static final Logger LOGGER = Logger.getLogger(JnjLatamOrderFacadeImpl.class);
	
	private JnjLatamSalesOrderMapper jnjLatamSalesOrderMapper;

	/**
	 * @return the jnjLatamSalesOrderMapper
	 */
	public JnjLatamSalesOrderMapper getJnjLatamSalesOrderMapper()
	{
		return jnjLatamSalesOrderMapper;
	}

	/**
	 * @param jnjLatamSalesOrderMapper
	 *           the jnjLatamSalesOrderMapper to set
	 */
	public void setJnjLatamSalesOrderMapper(final JnjLatamSalesOrderMapper jnjLatamSalesOrderMapper)
	{
		this.jnjLatamSalesOrderMapper = jnjLatamSalesOrderMapper;
	}

	@Autowired
	private JnjLAOrderService jnjLaOrderService;

	@Autowired
	private UserService userService;

	private CustomerAccountService customerAccountService;

	private final Class currentClass = JnjLatamOrderFacadeImpl.class;

	@Autowired
	private JnjCartService jnjCartService;

	@Autowired
	private B2BOrderService b2bOrderService;

	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	private Converter<OrderModel, JnjLaOrderData> orderConverter;

	@Autowired
	private JnjLatamEdiOrderService jnjLatamEdiOrderService;

	@Autowired
	private DefaultLaJnjOrderDao jnjOrderDao;

    @Autowired
    private JnjLAOrderService jnjLAOrderService;

	public Converter<OrderModel, JnjLaOrderData> getOrderConverter()
	{
		return orderConverter;
	}

	/**
	 * @param orderConverter
	 *           the orderConverter to set
	 */
	public void setOrderConverter(final Converter<OrderModel, JnjLaOrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	@Autowired
	private Converter<OrderModel, JnjOrderHistoryData> orderHistoryConverter;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected DefaultCompanyB2BCommerceService companyB2BCommerceService;

	@Autowired
	protected ModelService modelService;

	protected Converter<OrderModel, JnjOrderHistoryData> getOrderHistoryConverter()
	{
		return orderHistoryConverter;
	}

	final private String ORDER_STATUS_PREFIX = "order.status.";
	final private String ORDER_STATUS_SUFIX = ".value";
	final private String ORDER_ALL_STATUS = "All Status";
	final private String ORDER_VALID_STATUS_HEADER = "all.header.status";
	final private String ORDER_VALID_STATUS_LINE = "all.line.status";

	@Override
	public SalesOrderCreationResponse invokeSalesOrderCreationWrapper(final OrderModel orderModel)
			throws IntegrationException, SystemException, ParseException
	{
		final String methodName = "invokeSalesOrderCreationWrapper()";
		JnjGTCoreUtil.logDebugMessage("Invoke sales order creation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		SalesOrderCreationResponse salesOrderCreationResponse = null;
		try
		{
			salesOrderCreationResponse = jnjLatamSalesOrderMapper.mapSalesOrderCreationWrapper(orderModel);
		}
		catch (final com.jnj.exceptions.SystemException e)
		{
			JnjGTCoreUtil.logErrorMessage("Latam OrderFacadeImplException", methodName, "Message.", e,
					JnjLatamOrderFacadeImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage("Invoke sales order creation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		return salesOrderCreationResponse;
	}

	@Override
	public JnjValidateOrderData invokeSalesOrderSimulateWrapper(final CartModel cartModel)
			throws IntegrationException, SystemException, TimeoutException
	{

		final String methodName = "invokeSalesOrderSimulateWrapper()";

		JnjValidateOrderData validateOrderData = null;

		JnjGTCoreUtil.logDebugMessage("Invoke sales order simulation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
		try
		{
			validateOrderData = jnjLatamSalesOrderMapper.mapSalesOrderSimulationWrapper(cartModel);
		}
		catch (final com.jnj.exceptions.SystemException e)
		{

			JnjGTCoreUtil.logErrorMessage("LatamOrderFacadeImplException", methodName, "Message.", e, JnjLatamOrderFacadeImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage("Invoke sales order simulation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
		return validateOrderData;
	}
	
	
	@Override
	public void modifyOrderStatus(final List<String> orderCodeList) {		
		for (final String orderCode : orderCodeList) {
			final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody() {
								
				@Override
				public OrderModel execute() {
					return b2bOrderService.getOrderForCode(orderCode);
				}
			}, userService.getAdminUser());
			if (StringUtils.isNotEmpty(orderModel.getSapOrderNumber())) {
				orderModel.setStatus(OrderStatus.CREATED);
				for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries()) {
					orderEntry.setStatus(OrderEntryStatus.CREATED.getCode());
					modelService.save(orderEntry);
                    updateOrderEntryStatusWithPermutationMatrix(orderEntry);
                }
			}
						
			modelService.save(orderModel);
            updateOrderHeaderStatusWithPermutationMatrix(orderModel);
           
		}

	}

    private void updateOrderHeaderStatusWithPermutationMatrix(OrderModel orderModel) {
	    try {
            jnjLAOrderService.updateOrderHeaderStatus(orderModel);
        } catch (Exception e) {
	        String error = "Could not update placed order using Permutation Matrix Order Header";
            JnjGTCoreUtil.logErrorMessage("LatamOrderFacadeImplException", "updateOrderHeaderStatusWithPermutationMatrix", error, e, JnjLatamOrderFacadeImpl.class);
        }
    }

    private void updateOrderEntryStatusWithPermutationMatrix(AbstractOrderEntryModel orderEntry) {
	    try {
            jnjLAOrderService.updateOrderEntryStatus(orderEntry);
        } catch (Exception e) {
	        String error = "Could not update placed order using Permutation Matrix Order Line";
            JnjGTCoreUtil.logErrorMessage("LatamOrderFacadeImplException", "updateOrderEntryStatusWithPermutationMatrix", error, e, JnjLatamOrderFacadeImpl.class);
        }
    }

    @Override
	public SalesOrderPricingResponse invokeSalesOrderPricingWrapper(final CartModel cartModel, final String orderEntryNumber)
			throws IntegrationException, TimeoutException
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public JnjOrderData getOrderData(final String purchaseOrderNumber, final String orderNumber)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchPageData<JnjOrderHistoryData> getPagedOrderHistoryForStatuses(final PageableData pageableData,
			final OrderHistoryDTO orderHistoryDTO, final OrderStatus statuses)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getProcuctCodes(final String orderId)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAllStatus(final List<JnjOrderHistoryData> orders)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveEdiFilesToFtp(final CommonsMultipartFile[] submitOrderFileArray, final Map<String, String> fileStatusMap)
	{
		final String methodName = "saveEdiFilesToFtp()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		String fileName = null;
		try
		{
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			for (final CommonsMultipartFile submitOrderFile : submitOrderFileArray)
			{
				fileName = submitOrderFile.getOriginalFilename();
				final String fileHash = JnjlatamOrderUtil.getFileSha512(submitOrderFile.getInputStream(), fileName);
				List<JnjUploadOrderSHAModel> shaModels = jnjOrderDao.getUploadOrderSHADetails(fileHash);
				if (shaModels == null || shaModels.isEmpty()) // No existing file with this hash
				{
					shaModels = new ArrayList<>();
				}
				else if (JnjlatamOrderUtil.isIgnoreFileRequired(fileStatusMap, shaModels, fileName, currentUser))
				{
					continue;
				}

				if (jnjLaOrderService.saveUploadEdiOrderFileToFTP(submitOrderFile))
				{
					jnjLatamEdiOrderService.createUploadOrderSHADetails(fileHash, fileName, currentUser);
					fileStatusMap.put(fileName, Jnjlab2bcoreConstants.UploadOrder.RECEIVED_STATUS);
				}
				else
				{
					fileStatusMap.put(fileName, Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS);
				}
			}
			// Created entries on file uploaded files
			jnjLaOrderService.createUploadOrderStatus(fileStatusMap);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"An exception occured while trying to save the submitted files.", exception, currentClass);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);
	}

	@Override
	public OrderData getLatestOrder(final int numberOfEntries)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderHistoryDTO getDefaultData()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public JnjOrderData getOrderByCode(final String code)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getAllHeaderStatus()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getAllLineStatus()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * @Override public boolean saveOrderOpsArchModel(final JnjOrderOpsArchModel jnjOrderOpsArchModel) { // YTODO
	 * Auto-generated method stub return false; }
	 */

	@Override
	public boolean saveOrderModel(final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	@Override
	public SearchPageData<JnjLatamUploadOrderData> getUploadOrder(final String sortflag, final String filterFlag,
			final PageableData pageableData)
	{
		final String methodName = "getUploadOrder()";

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamOrderFacadeImpl.class);
		SearchPageData<JnjUploadOrderModel> list = null;
		try
		{
			list = jnjLaOrderService.getUploadOrderData(sortflag, filterFlag, pageableData);
		}
		catch (final Exception exp)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName,
					"Error while calling service class to obtain the list object of JnjUploadOrderModel type", exp,
					JnjLatamOrderFacadeImpl.class);
		}
		Collection<JnjUploadOrderModel> sellOutList = Collections.EMPTY_LIST;
		if (null != list)
		{
			sellOutList = list.getResults();
		}

		final List<JnjLatamUploadOrderData> sellOutDataList = templateConverter(sellOutList);
		final SearchPageData<JnjLatamUploadOrderData> result = new SearchPageData<JnjLatamUploadOrderData>();
		result.setResults(sellOutDataList);

		if (null != list)
		{
			result.setPagination(list.getPagination());
		}

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD,
				JnjLatamOrderFacadeImpl.class);


		return result;
	}

	private List<JnjLatamUploadOrderData> templateConverter(final Collection<JnjUploadOrderModel> sellOutList)
	{
		final List<JnjLatamUploadOrderData> uploadOrderDataList = new ArrayList<JnjLatamUploadOrderData>();
		if (!sellOutList.isEmpty())
		{
			JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, "templateConverter",
					"Upload Order Model list size" + sellOutList.size(), JnjLatamOrderFacadeImpl.class);
		}
		for (final JnjUploadOrderModel uploadOrderModel : sellOutList)
		{
			final JnjLatamUploadOrderData uploadOrderData = createOrderTemplateData(uploadOrderModel);

			uploadOrderDataList.add(uploadOrderData);

		}
		return uploadOrderDataList;
	}

	private JnjLatamUploadOrderData createOrderTemplateData(final JnjUploadOrderModel jnjUploadOrderModel)
	{
		final String methodName = "createOrderTemplateData ()";

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamOrderFacadeImpl.class);

		final JnjLatamUploadOrderData jnjUploadOrderData = new JnjLatamUploadOrderData();
		jnjUploadOrderData.setDocName(jnjUploadOrderModel.getFileName());
		jnjUploadOrderData.setCustomer(jnjUploadOrderModel.getUploadByCustomer());
		jnjUploadOrderData.setUser(jnjUploadOrderModel.getUploadByUser());
		jnjUploadOrderData.setFileNameId(jnjUploadOrderModel.getFileNameId());

		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName,
				"File Name id for upload order model :" + jnjUploadOrderModel.getFileNameId(), JnjLatamOrderFacadeImpl.class);
		final SimpleDateFormat enFormatter = new SimpleDateFormat(Jnjlab2bcoreConstants.SellOutReports.EN_DATE_FORMAT);
		final SimpleDateFormat ptFormatter = new SimpleDateFormat(Jnjlab2bcoreConstants.SellOutReports.PT_DATE_FORMAT);
		String language = null;
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) companyB2BCommerceService.getCurrentUser();
		if (null != currentUser.getSessionLanguage())
		{
			language = currentUser.getSessionLanguage().getIsocode();
		}
		if (null != language && ("en").equalsIgnoreCase(language))
		{
			if (null != jnjUploadOrderModel.getDate())
			{
				jnjUploadOrderData.setDate(enFormatter.format(jnjUploadOrderModel.getDate()));
			}
			else
			{
				jnjUploadOrderData.setDate(enFormatter.format(new Date()));
			}
		}
		else
		{
			jnjUploadOrderData.setDate(ptFormatter.format(jnjUploadOrderModel.getDate()));
		}
		jnjUploadOrderData.setStatus(jnjUploadOrderModel.getUploadFileStatus());
		jnjUploadOrderData.setIsLinkEnable(Boolean.TRUE);
		if (Jnjlab2bcoreConstants.UploadOrder.SUCCESS_STATUS.equalsIgnoreCase(jnjUploadOrderModel.getUploadFileStatus()))
		{
			jnjUploadOrderData.setIsLinkEnable(Boolean.FALSE);
		}
		jnjUploadOrderData.setErrorMessageList(jnjUploadOrderModel.getErrorMessageList());
		jnjUploadOrderData.setTrackingID(jnjUploadOrderModel.getTrackingId());
		if (null != jnjUploadOrderModel.getTrackingId() && StringUtils.isNotEmpty(jnjUploadOrderModel.getTrackingId()))
		{
			final String[] poNumberArray = jnjUploadOrderModel.getTrackingId().split("_JJ");
			final List<String> poNumberList = new ArrayList<String>();
			for (final String poNumber : poNumberArray)
			{
				poNumberList.add(poNumber);
			}
			jnjUploadOrderData.setPoNumber(poNumberList.get(0));
		}

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD,
				JnjLatamOrderFacadeImpl.class);
		return jnjUploadOrderData;
	}

	@Override
	public List<OrderEntryStatus> removeInValidLineStatuses(final List<OrderEntryStatus> validStates)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderStatus> removeInValidHeaderStatuses(final List<OrderStatus> validStates)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean skipValidation()
	{
		// YTODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean skipCartException()
	{
		// YTODO Auto-generated method stub
		return false;
	}

	@Override
	public void showHideCheckOutButton(final Model model)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public CartModel getCartModelforCode(final String code)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchPageData<JnjOrderHistoryData> getPagedOrderHistory(final PageableData pageableData,
			final JnjOrderHistoryDTO jnjOrderHistoryDTO)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderData getOrderDetailsForCode(final String code)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public JnjLatamUploadOrderData getUploadOrderDetails(final String uploadedOrderId)
	{
		final String methodName = "getUploadOrderDetails()";

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamOrderFacadeImpl.class);

		JnjUploadOrderModel jnjUploadOrderModel = null;
		try
		{
			jnjUploadOrderModel = jnjLaOrderService.getUploadOrderDataDetails(uploadedOrderId);
		}
		catch (final Exception exp)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName,
					"Error while calling service class to obtain the list object of JnjUploadOrderModel type", exp,
					JnjLatamOrderFacadeImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD,
				JnjLatamOrderFacadeImpl.class);

		if (null != jnjUploadOrderModel)
		{
			return createOrderTemplateData(jnjUploadOrderModel);
		}
		else
		{
			return null;
		}
	}

	@Override
	public OrderData getOrderDetailsForCode(final String orderCode, final boolean ignoreRestriction)
	{
		final String methodName = "Latam getOrderDetailsForCode()";
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName, Logging.BEGIN_OF_METHOD,
				currentClass);
		final OrderModel orderModel = jnjLaOrderService.getOrderDetailsForCode(orderCode, ignoreRestriction);
		if (orderModel == null)
		{
			throw new UnknownIdentifierException(
					"Order with code " + orderCode + " not found for current user in current BaseStore");
		}
		final OrderData orderData = getOrderConverter().convert(orderModel);
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName, Logging.END_OF_METHOD,
				currentClass);
		return orderData;
	}
	
	@Override
	public String getCustomerFreightType(final String orderCode) {
		final OrderModel orderModel = jnjLaOrderService.getOrderDetailsForCode(orderCode, true);
		if (orderModel == null)
		{
			LOGGER.info("Order not found UnknownIdentifierException");
			return null;
		}
		return orderModel.getCustomerFreightType();
	}
	
}
