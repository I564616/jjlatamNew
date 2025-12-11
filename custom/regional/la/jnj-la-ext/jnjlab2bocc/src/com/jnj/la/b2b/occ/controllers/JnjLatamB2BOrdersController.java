/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.la.b2b.occ.controllers;

import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjLatamOrdersListData;
import com.jnj.la.b2b.webservicescommons.dto.JnjLatamCartWsDTO;
import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrdersListWsDTO;
import com.jnj.la.b2b.webservicescommons.dto.JnJLatamOrderWsDTO;
import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrderRequestWsDTO;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.annotation.SiteChannelRestriction;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import jakarta.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.SystemException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

import com.jnj.facades.data.JnjLaCartData;
import com.jnj.facades.vtex.order.JnjLatamVtexOrderFacade;
import com.jnj.la.b2b.occ.helper.JnjLatamOrderHelper;
import com.jnj.facades.data.JnjLatamOrderRequestData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/orders")
@ApiVersion("v2")
@Tag(name = "B2B Orders")
public class JnjLatamB2BOrdersController extends JnjLatamB2BBaseController
{

	private static final Logger LOG = LoggerFactory.getLogger(JnjLatamB2BOrdersController.class);

	private static final String ORDER_REQUEST = "orderRequest";

	@Resource(name = "jnjLatamVtexOrderFacade")
	private JnjLatamVtexOrderFacade jnjLatamVtexOrderFacade;

	@Resource(name = "jnjLatamCreateOrderRequestValidator")
	private Validator jnjLatamCreateOrderRequestValidator;

	@Resource(name = "jnjLatamOrderHelper")
	private JnjLatamOrderHelper jnjLatamOrderHelper;

	@Resource(name = "jnjLatamOrderStatusRequestValidator")
	private Validator jnjLatamOrderStatusRequestValidator;

	@Secured(
			{"ROLE_CUSTOMERGROUP", "ROLE_CLIENT"})
	@PostMapping(value = "/simulate", consumes =
			{MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@Operation(operationId = "validateCart", summary = "Validate list of products and account details before placing an order", description = "Returns Cart details if success else error messages")
	@ApiBaseSiteIdAndUserIdParam
	public JnjLatamCartWsDTO validateCart(
			@Parameter(description = "Cart identifier: cart code for logged in user, cart guid for anonymous user, 'current' for the last modified cart", required = true)
			@RequestBody final JnjLatamOrderRequestWsDTO orderRequestWsDto, @ApiFieldsParam
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) throws BusinessException, SystemException, IntegrationException, TimeoutException
	{
		validate(orderRequestWsDto, ORDER_REQUEST, jnjLatamCreateOrderRequestValidator);
		final JnjLatamOrderRequestData orderRequestData = getDataMapper().map(orderRequestWsDto, JnjLatamOrderRequestData.class,
				fields);
		jnjLatamVtexOrderFacade.createVtexCart(orderRequestData);
		jnjLatamVtexOrderFacade.updateCartEntrySalesOrg();
		final JnjLaCartData latamCartData = jnjLatamVtexOrderFacade.validateCart();
		return getDataMapper().map(latamCartData, JnjLatamCartWsDTO.class, fields);
	}

	@Secured(
			{"ROLE_CUSTOMERGROUP", "ROLE_CLIENT"})
	@PostMapping(value = "/placeOrder", consumes =
			{MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@Operation(operationId = "placeOrder", summary = "Places a B2B Order", description = "Places a B2B Order")
	@ApiBaseSiteIdAndUserIdParam
	public JnJLatamOrderWsDTO placeOrder(
			@Parameter(description = "Cart identifier: cart code for logged in user, cart guid for anonymous user, 'current' for the last modified cart", required = true)
			@RequestBody final JnjLatamOrderRequestWsDTO orderRequestWsDto, @ApiFieldsParam
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) throws BusinessException, InvalidCartException, SystemException, IntegrationException, ParseException
	{
		validate(orderRequestWsDto, ORDER_REQUEST, jnjLatamCreateOrderRequestValidator);
		final JnjLatamOrderRequestData orderRequestData = getDataMapper().map(orderRequestWsDto, JnjLatamOrderRequestData.class,
				fields);
		jnjLatamVtexOrderFacade.setVtexCart(orderRequestData);
		final String orderCode = jnjLatamVtexOrderFacade.placeOrderInHybris();
		jnjLatamVtexOrderFacade.createOrderInSAP(orderCode);
		final JnjGTOrderData order = (JnjGTOrderData) jnjLatamVtexOrderFacade.getOrderPlaced(orderCode);
		return getDataMapper().map(order, JnJLatamOrderWsDTO.class, fields);
	}

	@Secured(
			{"ROLE_CUSTOMERGROUP", "ROLE_CLIENT"})
	@PostMapping(value = "/status", consumes =
			{MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@Operation(operationId = "getOrderStatus", summary = "Get Order History By Using SAP Order Number", description = "Get Order History By Using SAP Order Number")
	@ApiBaseSiteIdAndUserIdParam
	public OrderHistoryListWsDTO getOrderStatus(
			@RequestBody final JnjLatamOrdersListWsDTO ordersListWsDTO, @ApiFieldsParam
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		validate(ordersListWsDTO, ORDER_REQUEST, jnjLatamOrderStatusRequestValidator);
		final JnjLatamOrdersListData ordersListData = getDataMapper().map(ordersListWsDTO, JnjLatamOrdersListData.class,
				fields);
		final OrderHistoriesData orderData = jnjLatamVtexOrderFacade.getOrderHistoriesData(ordersListData);
		return getDataMapper().map(orderData, OrderHistoryListWsDTO.class, fields);
	}

}
