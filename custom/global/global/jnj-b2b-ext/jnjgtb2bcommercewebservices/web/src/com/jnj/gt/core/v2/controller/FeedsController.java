/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.jnj.gt.core.v2.controller;

import de.hybris.platform.commercewebservicescommons.dto.queues.OrderStatusUpdateElementListWsDTO;
import com.jnj.gt.core.formatters.WsDateFormatter;
import com.jnj.gt.core.queues.data.OrderStatusUpdateElementData;
import com.jnj.gt.core.queues.data.OrderStatusUpdateElementDataList;
import com.jnj.gt.core.queues.impl.OrderStatusUpdateQueue;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;


@Controller
@RequestMapping(value = "/{baseSiteId}/feeds")
@Tag(name = "Feeds")
public class FeedsController extends BaseController
{
	@Resource(name = "wsDateFormatter")
	private WsDateFormatter wsDateFormatter;
	@Resource(name = "orderStatusUpdateQueue")
	private OrderStatusUpdateQueue orderStatusUpdateQueue;


	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/orders/statusfeed", method = RequestMethod.GET)
	@ResponseBody
	@Operation(summary = "Get a list of orders with status updates", description = "Returns the orders that have changed status. Returns only the elements from the "
			+ "current baseSite that have been updated after the provided timestamp.")
	public OrderStatusUpdateElementListWsDTO orderStatusFeed(
			@Parameter(description = "Only items newer than the given parameter are retrieved. This parameter should be in ISO-8601 format (for example, 2018-01-09T16:28:45+0000).", required = true) @RequestParam final String timestamp,
			@Parameter(description = "Base site identifier", required = true) @PathVariable final String baseSiteId,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final Date timestampDate = wsDateFormatter.toDate(timestamp);
		final List<OrderStatusUpdateElementData> orderStatusUpdateElements = orderStatusUpdateQueue.getItems(timestampDate);
		filterOrderStatusQueue(orderStatusUpdateElements, baseSiteId);
		final OrderStatusUpdateElementDataList dataList = new OrderStatusUpdateElementDataList();
		dataList.setOrderStatusUpdateElements(orderStatusUpdateElements);
		return getDataMapper().map(dataList, OrderStatusUpdateElementListWsDTO.class, fields);
	}

	protected void filterOrderStatusQueue(final List<OrderStatusUpdateElementData> orders, final String baseSiteId)
	{
		final Iterator<OrderStatusUpdateElementData> dataIterator = orders.iterator();
		while (dataIterator.hasNext())
		{
			final OrderStatusUpdateElementData orderStatusUpdateData = dataIterator.next();
			if (!baseSiteId.equals(orderStatusUpdateData.getBaseSiteId()))
			{
				dataIterator.remove();
			}
		}
	}
}
