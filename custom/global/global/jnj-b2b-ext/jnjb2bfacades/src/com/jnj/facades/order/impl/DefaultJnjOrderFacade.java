/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order.impl;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BOrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.OrderHistoryDTO;
import com.jnj.core.services.JnjOrderService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.data.JnjOrderHistoryData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.order.JnjOrderFacade;
import com.jnj.facades.order.converters.populator.JnjGTMockOrderDataPopulator;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BOrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
/**
 * The JnjOrderFacadeImpl class is used to invoke the respective method of the JnjSalesOrderMapper and JnjSalesOrder
 * class and pass the request object to corresponding method.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjOrderFacade extends DefaultB2BOrderFacade implements JnjOrderFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjOrderFacade.class);

	private JnjOrderService jnjOrderService;
	private BaseStoreService baseStoreService;
	private CustomerAccountService customerAccountService;

	@Autowired
	private JnjCartService jnjCartService;

	@Autowired
	B2BOrderService b2bOrderService;

	@Autowired
	JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	protected SessionService sessionService;

	private Converter<OrderModel, JnjOrderData> orderConverter;
	@Autowired
	private Converter<OrderModel, JnjOrderHistoryData> orderHistoryConverter;

	
	@Autowired
	private JnjGTMockOrderDataPopulator jnjGTMockOrderPopulator;
	

	

	public JnjGTMockOrderDataPopulator getJnjGTMockOrderPopulator() {
		return jnjGTMockOrderPopulator;
	}


	public void setJnjGTMockOrderPopulator(
			JnjGTMockOrderDataPopulator jnjGTMockOrderPopulator) {
		this.jnjGTMockOrderPopulator = jnjGTMockOrderPopulator;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @throws IntegrationException
	 * @throws SystemException
	 * 
	 */
	@Override
	public JnjValidateOrderData invokeSalesOrderSimulateWrapper(final CartModel cartModel) throws IntegrationException,
			SystemException
	{

		final JnjValidateOrderData validateOrderData = null;

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "invokeSalesOrderSimulateWrapper()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		//validateOrderData = jnjSalesOrderMapper.mapSalesOrderSimulationWrapper(cartModel);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "invokeSalesOrderSimulateWrapper()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return validateOrderData;

	}
	
	@Override
	public OrderData getOrderDetailsForCode(final String code) {
		final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public OrderModel execute()
			{
				return b2bOrderService.getOrderForCode(code);
			}
		}, getUserService().getAdminUser());
		if (orderModel == null) {
			throw new UnknownIdentifierException("Order with code " + code + " not found for current user in current BaseStore");
		}
		OrderData orderData = new OrderData();
		orderData =  getOrderConverter().convert(orderModel);	
		/*if(Config.getBoolean("jnj.site.order.mddSite.mockkey", false)){
			 getJnjGTMockOrderPopulator().populate(orderModel,orderData);
			
		}*/
		return orderData;
	}

	/**
	 * @param jnjOrderService
	 *           the jnjOrderService to set
	 */
	public void setJnjOrderService(final JnjOrderService jnjOrderService)
	{
		this.jnjOrderService = jnjOrderService;
	}

	private List<JnjOrderHistoryData> convertOrders(final List<OrderModel> orderModelList)
	{
		final List<JnjOrderHistoryData> orders = new ArrayList<JnjOrderHistoryData>();
		for (final OrderModel orderModel : orderModelList) 
		{
			final JnjOrderHistoryData jnjOrderHistoryData = new JnjOrderHistoryData();
			getOrderHistoryConverter().convert(orderModel, jnjOrderHistoryData);
			orders.add(jnjOrderHistoryData);
		}
		return orders;
	}

	private List<JnjOrderData> convertOrderData(final List<OrderModel> orderModelList)
	{
		final List<JnjOrderData> orders = new ArrayList<JnjOrderData>();
		for (final OrderModel orderModel : orderModelList)
		{
			final JnjOrderData JnjOrderData = new JnjOrderData();
			getOrderConverter().convert(orderModel, JnjOrderData);
			orders.add(JnjOrderData);
		}
		return orders;
	}



	/*
	 * This method is used to get all order details from Order Model
	 * 
	 * @see com.jnj.facades.order.JnjOrderFacade#getAllOrder()
	 */
	@Override
	public List<JnjOrderHistoryData> getAllOrder(final String purchaseOrderNumber, final String orderNumber)
	{
		return convertOrders(jnjOrderService.getAllOrderDetails(purchaseOrderNumber, orderNumber));
	}


	/*
	 * This method is used to get all order details from Order Model
	 * 
	 * @see com.jnj.facades.order.JnjOrderFacade#getAllOrder()
	 */
	@Override
	public List<JnjOrderData> getOrderData(final String purchaseOrderNumber, final String orderNumber)
	{
		return convertOrderData(jnjOrderService.getAllOrderDetails(purchaseOrderNumber, orderNumber));
	}

	@Override
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Override
	protected <T extends CustomerAccountService> T getCustomerAccountService()
	{
		return (T) customerAccountService;
	}

	@Override
	protected <S, T> SearchPageData<T> convertPageData(final SearchPageData<S> source, final Converter<S, T> converter)
	{
		final SearchPageData<T> result = new SearchPageData<T>();
		result.setPagination(source.getPagination());
		result.setSorts(source.getSorts());
		result.setResults(Converters.convertAll(source.getResults(), converter));
		return result;
	}

	/**
	 * this method is used to get product code on the basis of order ID.
	 */
	@Override
	public List<String> getProcuctCodes(final String orderId)
	{

		return jnjOrderService.getProductCodes(orderId);
	}

	@Override
	public Set<String> getAllStatus(final List<JnjOrderHistoryData> orders)
	{
		final Set<String> statusSet = new HashSet<String>();
		statusSet.add("All Status");
		for (final JnjOrderHistoryData historyData : orders)
		{
			statusSet.add(historyData.getStatus().getCode());
		}
		return statusSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Map<String, List<String>> createOrderFromInterface(final CommonsMultipartFile[] submitOrderFileArray)

	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "createOrderFromInterface()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		InputStream inputStream = null;
		boolean created = false;
		String dataRow = null;
		List<String> errorDetails = null;
		String fileName = null;
		final List<File> fileListForSAP = new ArrayList<File>();
		final Map<String, List<String>> fileNameWithErrorDetailsMap = new HashMap<String, List<String>>();
		try
		{
			for (final CommonsMultipartFile submitOrderFile : submitOrderFileArray)
			{
				inputStream = submitOrderFile.getInputStream();
				fileName = submitOrderFile.getOriginalFilename();
				final String[] stringArray = fileName.split(Jnjb2bFacadesConstants.Order.FILE_EXTENSION_SPILTTER);

				// check the string array as null and its length should be greater than zero.
				if (null != stringArray && stringArray.length > Jnjb2bFacadesConstants.Order.ZERO_AS_INT
						&& StringUtils.isNotEmpty(fileName))
				{
					// Get the first element of the file so that we check in this class if the the file is empty or not.
					final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
					dataRow = bufferReader.readLine();
					final String fileExtension = stringArray[stringArray.length - Jnjb2bFacadesConstants.Order.ONE_AS_INT];
					// if the file extension is csv then enter in the if block
					if (StringUtils.isNotEmpty(fileExtension)
							&& fileExtension.equalsIgnoreCase(Jnjb2bFacadesConstants.Order.TXT_STRING)
							&& StringUtils.isNotEmpty(dataRow))
					{
						bufferReader.close();
						inputStream = submitOrderFile.getInputStream();
						errorDetails = new ArrayList<String>();
						//created = jnjOrderService.createOrderFromSaoLuiz(inputStream, fileListForSAP, errorDetails);
						// check the if the errorDetails contains any row with wrong data or input.
						if (!errorDetails.isEmpty())
						{
							fileNameWithErrorDetailsMap.put(fileName, errorDetails);
						}
					}
					// if the file extension is ean then enter in the else if block
					else if (StringUtils.isNotEmpty(fileExtension)
							&& fileExtension.equalsIgnoreCase(Jnjb2bFacadesConstants.Order.EAN_STRING)
							&& StringUtils.isNotEmpty(dataRow))
					{
						bufferReader.close();
						inputStream = submitOrderFile.getInputStream();
						errorDetails = new ArrayList<String>();
						//created = jnjOrderService.createOrderFromEanLayOut(inputStream, fileListForSAP, errorDetails);
						// check the if the errorDetails contains any row with wrong data or input.
						if (!errorDetails.isEmpty())
						{
							fileNameWithErrorDetailsMap.put(fileName, errorDetails);
						}
					}
					// else file is the xml file
					else if (StringUtils.isNotEmpty(fileExtension)
							&& fileExtension.equalsIgnoreCase(Jnjb2bFacadesConstants.Order.XML_STRING))
					{
						dataRow = bufferReader.readLine();// Get the second line as the second line of the file contains the differentiating tag.
						// Enter in the if block for the Albert Einstein File
						if (StringUtils.isNotEmpty(dataRow) && dataRow.contains(Jnjb2bFacadesConstants.Order.PEDIDO))
						{
							bufferReader.close();
							inputStream = submitOrderFile.getInputStream();
							errorDetails = new ArrayList<String>();
							//created = jnjOrderService.createOrderFromAlbertFile(inputStream, fileListForSAP, errorDetails);
							// check the if the errorDetails contains any row with wrong data or input.
							if (!errorDetails.isEmpty())
							{
								fileNameWithErrorDetailsMap.put(fileName, errorDetails);
							}
						}
						//Enter in the else block for the Alianca File
						else if (StringUtils.isNotEmpty(dataRow)
								&& dataRow.contains(Jnjb2bFacadesConstants.Order.PROCESS_PURCHASE_ORDER))
						{
							bufferReader.close();
							inputStream = submitOrderFile.getInputStream();
							errorDetails = new ArrayList<String>();
							//created = jnjOrderService.createOrderFromAliancaFile(inputStream, fileListForSAP, errorDetails);
							// check the if the errorDetails contains any row with wrong data or input.
							if (!errorDetails.isEmpty())
							{
								fileNameWithErrorDetailsMap.put(fileName, errorDetails);
							}
						}
						else
						{
							bufferReader.close();
							if (LOGGER.isDebugEnabled())
							{
								LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "createOrderFromInterface()" + Logging.HYPHEN
										+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + "File is Empty");
							}
							errorDetails = new ArrayList<String>();
							errorDetails.add(jnjCommonFacadeUtil.getMessageFromImpex("order.home.fileFormatInvalid"));
							fileNameWithErrorDetailsMap.put(fileName, errorDetails);
						}
					}
					else
					{
						bufferReader.close();
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "createOrderFromInterface()" + Logging.HYPHEN
									+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN
									+ "This File format is not submitted by Submit Order EDI Functionality");
						}
						errorDetails = new ArrayList<String>();
						errorDetails.add(jnjCommonFacadeUtil.getMessageFromImpex("order.home.fileFormatInvalid"));
						fileNameWithErrorDetailsMap.put(fileName, errorDetails);
					}
				}
			}
		}
		catch (final IOException inputOutputException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "createOrderInSAP()" + Logging.HYPHEN
					+ "Input Output exception occured " + inputOutputException.getMessage(), inputOutputException);
			populateErrorDetailsList(errorDetails);
			fileNameWithErrorDetailsMap.put(fileName, errorDetails);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "createOrderInSAP()" + Logging.HYPHEN
					+ "Illegal Argument exception occured " + illegalArgumentException.getMessage(), illegalArgumentException);
			populateErrorDetailsList(errorDetails);
			fileNameWithErrorDetailsMap.put(fileName, errorDetails);
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "createOrderInSAP()" + Logging.HYPHEN
					+ "Throwable exception occured " + throwable.getMessage(), throwable);
			populateErrorDetailsList(errorDetails);
			fileNameWithErrorDetailsMap.put(fileName, errorDetails);
		}

		// if the fileListForSAP list is not empty then move the corresponding file in archive folder or error folder.
		if (!fileListForSAP.isEmpty())
		{
			created = jnjOrderService.sftpCallAndMoveFileToZipFolder(fileListForSAP);
			LOGGER.info(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "createOrderFromInterface()" + Logging.HYPHEN

			+ "Created Flag value after getting response from the sftpCallAndMoveFileToZipFolder method is " + created);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "createOrderFromInterface()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return fileNameWithErrorDetailsMap;
	}


	/**
	 * Populate error details list with general error message in case of any exception.
	 * 
	 * @param errorDetails
	 *           the error details
	 */
	private void populateErrorDetailsList(List<String> errorDetails)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "populateErrorDetailsList()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// if the error details is null then create the list else add the general error message in the list.
		if (null == errorDetails)
		{
			errorDetails = new ArrayList<String>();
		}
		errorDetails.add(jnjCommonFacadeUtil.getMessageFromImpex("order.home.generalErrorMessage"));
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "populateErrorDetailsList()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	@Override
	public OrderData getLatestOrder(final int numberOfEntries)
	{
		JnjOrderData jnjOrderData = null;
		final OrderModel orderModel = jnjOrderService.getLatestOrder();
		if (orderModel != null)
		{
			jnjOrderData = new JnjOrderData();
			if (orderModel.getEntries().size() >= numberOfEntries)
			{
				orderModel.setEntries(orderModel.getEntries().subList(0, numberOfEntries));
			}
			getOrderConverter().convert(orderModel, jnjOrderData);
		}
		return jnjOrderData;
	}

	@Override
	public OrderHistoryDTO getDefaultData()
	{
		final OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();
		orderHistoryDTO.setSortbynumber("Date - newest to oldest");
		orderHistoryDTO.setFieldName(Jnjb2bCoreConstants.OrderHistory.ORDER_NUMBER);
		orderHistoryDTO.setCode("");
		orderHistoryDTO.setStatus("All Status");
		return orderHistoryDTO;
	}

	

}
