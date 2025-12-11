/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.pac.aera.job.dao;

import com.gt.pac.aera.JnJPacAeraResponse;
import com.gt.pac.aera.PacHiveException;
import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;


public class DefaultJnjPacAeraDao implements JnjPacAeraDao
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultJnjPacAeraDao.class);

	protected FlexibleSearchService flexibleSearchService;

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	public JnjPacHiveEntryModel findJnjPacHiveEntryModel(
			@Nonnull final JnJPacAeraResponse jnJPacAeraResponse,
			@Nonnull final AbstractOrderEntryModel orderEntryModel
	) throws PacHiveException
	{
		Validate.notNull(jnJPacAeraResponse, "Parameter 'jnJPacAeraResponse' can not be null.");
		Validate.notNull(orderEntryModel, "Parameter 'orderEntryModel' can not be null.");

		if (CollectionUtils.isEmpty(orderEntryModel.getJnjPacHiveEntries()))
		{
			throw new PacHiveException(String.format(
					"No corresponding JnjPacHiveEntryModel was found for jnJPacAeraResponse with orderNumber: '%s'" +
					" lineNumber: '%s' schedLineNumber: '%s' because the order entry with PK: '%s' does not have any " +
					"JnjPacHiveEntryModels.",
					jnJPacAeraResponse.getOrderNumber(),
					jnJPacAeraResponse.getLineNumber(),
					jnJPacAeraResponse.getSchedLineNumber(),
					orderEntryModel.getPk()
			));
		}

		final List<JnjPacHiveEntryModel> matchingModels = orderEntryModel.getJnjPacHiveEntries().stream().filter(
				it -> StringUtils.equals(it.getSchedLineNumber(), jnJPacAeraResponse.getSchedLineNumber())
		).collect(Collectors.toList());

		if (matchingModels.isEmpty())
		{
			throw new PacHiveException(String.format(
					"No corresponding JnjPacHiveEntryModel was found for JnJPacAeraResponse" +
					" with schedLineNumber: '%s' inside order entry with PK: '%s'." +
					" JnJPacAeraResponse.orderNumber: '%s' JnJPacAeraResponse.lineNumber: '%s'",
					jnJPacAeraResponse.getSchedLineNumber(),
					orderEntryModel.getPk(),
					jnJPacAeraResponse.getOrderNumber(),
					jnJPacAeraResponse.getLineNumber()
			));
		}

		if (matchingModels.size() > 1)
		{
			final JnjPacHiveEntryModel result = matchingModels.get(0);
			LOG.error(
					"[PAC HIVE] More than one corresponding JnjPacHiveEntryModel was found for JnJPacAeraResponse" +
					" with sapOrderNumber: '{}' inside order entry with PK: '{}' and it is not clear which to use." +
					" The first one with PK: '{}' will be used. " +
					" JnJPacAeraResponse.orderNumber: '{}' JnJPacAeraResponse.lineNumber: '{}'",
					jnJPacAeraResponse.getSchedLineNumber(),
					orderEntryModel.getPk(),
					result.getPk(),
					jnJPacAeraResponse.getOrderNumber(),
					jnJPacAeraResponse.getLineNumber()
			);
			return result;
		}

		final JnjPacHiveEntryModel result = matchingModels.get(0);
		LOG.debug(
				"[PAC HIVE] One corresponding JnjPacHiveEntryModel with pk: '{}' was found for JnJPacAeraResponse" +
				" with schedLineNumber: '{}' inside order entry with PK: '{}'." +
				" JnJPacAeraResponse.orderNumber: '{}' JnJPacAeraResponse.lineNumber: '{}'",
				result.getPk(),
				jnJPacAeraResponse.getSchedLineNumber(),
				orderEntryModel.getPk(),
				jnJPacAeraResponse.getOrderNumber(),
				jnJPacAeraResponse.getLineNumber()
		);

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	public AbstractOrderEntryModel findCorrespondingOrderEntryModel(@Nonnull final JnJPacAeraResponse jnJPacAeraResponse)
	throws PacHiveException
	{
		Validate.notNull(jnJPacAeraResponse, "Parameter 'jnJPacAeraResponse' can not be null.");

		final OrderModel orderModel = this.findCorrespondingOrderModel(jnJPacAeraResponse);
		if (orderModel == null)
		{
			throw new PacHiveException(String.format(
					"Can not find corresponding OrderEntry for JnJPacAeraResponse with sapOrderNumber '%s'" +
					" catalogCode: '%s' lineNumber: '%s'. Because such order was not found.",
					jnJPacAeraResponse.getOrderNumber(),
					jnJPacAeraResponse.getCatalogCode(),
					jnJPacAeraResponse.getLineNumber()
			));
		}

		if (CollectionUtils.isEmpty(orderModel.getEntries()))
		{
			throw new PacHiveException(String.format(
					"Can not find corresponding OrderEntry for JnJPacAeraResponse inside Order with" +
					" sapOrderNumber '%s' and PK '%s' because the order does not have any order entries.",
					jnJPacAeraResponse.getOrderNumber(), orderModel.getPk()
			));
		}

		final List<AbstractOrderEntryModel> matchingOrderEntryModels = orderModel.getEntries().stream().filter(
				it -> this.isOrderEntryMatchesPacHiveReponse(it, jnJPacAeraResponse)
		).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(matchingOrderEntryModels))
		{
			throw new PacHiveException(String.format(
					"Can not find corresponding OrderEntry for JnJPacAeraResponse inside Order with" +
					" sapOrderNumber '%s' and PK '%s' because none of its order entries match PAC HIVE AERA" +
					" response.", jnJPacAeraResponse.getOrderNumber(), orderModel.getPk()
			));
		}

		if (matchingOrderEntryModels.size() > 1)
		{
			final AbstractOrderEntryModel result = matchingOrderEntryModels.get(0);
			LOG.error(
					"[PAC HIVE] More than one Order Entry was found matching JnJPacAeraResponse with" +
					" LineNumber: '{}' CatalogCode: '{}' OrderNumber '{}' and it is not clear which to use." +
					" Matching Order Entries: '{}'. The first one with PK: '{}' will be used.",
					jnJPacAeraResponse.getLineNumber(),
					jnJPacAeraResponse.getCatalogCode(),
					jnJPacAeraResponse.getOrderNumber(),
					matchingOrderEntryModels.stream().map(
							it -> null == it.getPk() ? null : it.getPk().toString()
					).collect(Collectors.joining(", ")),
					result.getPk()
			);
			return result;
		}

		final AbstractOrderEntryModel result = matchingOrderEntryModels.get(0);
		LOG.debug(
				"[PAC HIVE] One corresponding OrderEntry with PK '{}' was found for JnJPacAeraResponse with" +
				" LineNumber: '{}' CatalogCode: '{}' OrderNumber '{}' inside Order with PK '{}'.",
				result.getPk(),
				jnJPacAeraResponse.getLineNumber(),
				jnJPacAeraResponse.getCatalogCode(),
				jnJPacAeraResponse.getOrderNumber(),
				orderModel.getPk()
		);

		return result;
	}

	/**
	 * See https://jira.jnj.com/browse/AALH-2552?focusedCommentId=4075770&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-4075770
	 */
	protected boolean isOrderEntryMatchesPacHiveReponse(
			@Nonnull final AbstractOrderEntryModel orderEntryModel,
			@Nonnull final JnJPacAeraResponse jnJPacAeraResponse
	)
	{
		Validate.notNull(orderEntryModel, "Parameter 'orderEntryModel' can not be null.");
		Validate.notNull(jnJPacAeraResponse, "Parameter 'jnJPacAeraResponse' can not be null.");

		if (StringUtils.isBlank(orderEntryModel.getSapOrderlineNumber()))
		{
			if (orderEntryModel.getProduct() == null || StringUtils.isBlank(orderEntryModel.getProduct().getCode()))
			{
				LOG.error("[PAC HIVE] Invalid data. Order entry with PK '{}' has empty product. SapOrderNumber: '{}'.",
				          orderEntryModel.getPk(), jnJPacAeraResponse.getOrderNumber()
				);
				return false;
			}

			return StringUtils.equals(orderEntryModel.getProduct().getCode(), jnJPacAeraResponse.getCatalogCode());
		}
		else
		{
			final String entrySapOrderlineNumber = this.stripLeadingZeros(orderEntryModel.getSapOrderlineNumber());
			final String pacHiveLineNumber = this.stripLeadingZeros(jnJPacAeraResponse.getLineNumber());

			return StringUtils.equals(entrySapOrderlineNumber, pacHiveLineNumber);
		}
	}

	@Nonnull
	protected OrderModel findCorrespondingOrderModel(@Nonnull final JnJPacAeraResponse jnJPacAeraResponse)
	throws PacHiveException
	{
		Validate.notNull(jnJPacAeraResponse, "Parameter 'jnJPacAeraResponse' can not be null.");

		OrderModel orderModel = null;
		try
		{
			orderModel = this.strictSearchForExistingOrderBySAPOrderNumber(jnJPacAeraResponse.getOrderNumber());
		} catch (Exception e)
		{
			LOG.debug("[PAC HIVE] Failed to find Order with sapOrderNumber: '{}' using strict search.",
			          jnJPacAeraResponse.getOrderNumber(), e
			);
		}

		if (null == orderModel)
		{
			try
			{
				orderModel = this.fuzzySearchForExistingOrderBySAPOrderNumber(jnJPacAeraResponse.getOrderNumber());
			} catch (Exception e)
			{
				LOG.debug("[PAC HIVE] Failed to find Order with sapOrderNumber: '{}' using fuzzy search.",
				          jnJPacAeraResponse.getOrderNumber(), e
				);
			}
		}

		if (null == orderModel)
		{
			throw new PacHiveException(String.format(
					"Failed to find Order with sapOrderNumber: '%s' using both strict and fuzzy search.",
					jnJPacAeraResponse.getOrderNumber()
			));
		}

		return orderModel;
	}

	@Nonnull
	protected OrderModel strictSearchForExistingOrderBySAPOrderNumber(@Nonnull final String sapOrderNumber)
	throws PacHiveException
	{
		Validate.notBlank(sapOrderNumber, "OrderModel." + OrderModel.SAPORDERNUMBER + " can not be empty.");

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {");
		searchQuery.append(OrderModel.PK);
		searchQuery.append("} FROM {");
		searchQuery.append(OrderModel._TYPECODE);
		searchQuery.append("} WHERE CAST(nullif({");
		searchQuery.append(OrderModel.SAPORDERNUMBER);
		searchQuery.append("}, '') AS INT) = ?" + OrderModel.SAPORDERNUMBER);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameter(OrderModel.SAPORDERNUMBER, Integer.parseInt(sapOrderNumber));

		final SearchResult<OrderModel> searchResult = this.getFlexibleSearchService().search(flexibleSearchQuery);
		final List<OrderModel> matchingOrders = searchResult.getResult();

		if (CollectionUtils.isEmpty(matchingOrders))
		{
			throw new PacHiveException(String.format(
					"No orders were found with sapOrderNumber '%s' using strict search.", sapOrderNumber
			));
		}
		if (matchingOrders.size() > 1)
		{
			final OrderModel result = matchingOrders.get(0);
			LOG.error(
					"[PAC HIVE] More than one order model was found using strict search with sapOrderNumber '{}' and" +
					" it is not clear which to use. Matching orders: '{}'. The first one with PK: '{}' will be used.",
					sapOrderNumber,
					matchingOrders.stream().map(OrderModel::getSapOrderNumber).collect(Collectors.joining(", ")),
					result.getPk()
			);
			return result;
		}

		final OrderModel result = matchingOrders.get(0);
		LOG.debug("[PAC HIVE] One order model was found using strict search with sapOrderNumber '{}'." +
		          " OrderModel.PK '{}'.", sapOrderNumber, result.getPk()
		);

		return result;
	}

	@Nonnull
	protected OrderModel fuzzySearchForExistingOrderBySAPOrderNumber(@Nonnull final String sapOrderNumber)
	throws PacHiveException
	{
		Validate.notBlank(sapOrderNumber, "OrderModel." + OrderModel.SAPORDERNUMBER + " can not be empty.");

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {");
		searchQuery.append(OrderModel.PK);
		searchQuery.append("} FROM {");
		searchQuery.append(OrderModel._TYPECODE);
		searchQuery.append("} WHERE {");
		searchQuery.append(OrderModel.SAPORDERNUMBER);
		searchQuery.append("} LIKE ?" + OrderModel.SAPORDERNUMBER);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameter(OrderModel.SAPORDERNUMBER, '%' + sapOrderNumber);

		final SearchResult<OrderModel> searchResult = this.getFlexibleSearchService().search(flexibleSearchQuery);
		final List<OrderModel> orders = searchResult.getResult();

		if (CollectionUtils.isEmpty(orders))
		{
			throw new PacHiveException(String.format(
					"No orders were found with sapOrderNumber like '%s' using fuzzy search.", sapOrderNumber
			));
		}

		LOG.debug(
				"[PAC HIVE] Search result for Orders with sapOrderNumber like '{}': '{}'.",
				sapOrderNumber,
				orders.stream().map(OrderModel::getSapOrderNumber).collect(Collectors.joining(", "))
		);

		return this.filterOutFuzzyResults(sapOrderNumber, orders);
	}

	@Nonnull
	protected OrderModel filterOutFuzzyResults(
			@Nonnull final String sapOrderNumber,
			@Nullable final List<OrderModel> orders
	) throws PacHiveException
	{
		if (CollectionUtils.isEmpty(orders))
		{
			throw new PacHiveException(String.format(
					"No orders were found with sapOrderNumber like '%s' using fuzzy search.", sapOrderNumber
			));
		}

		Validate.notBlank(sapOrderNumber, "Parameter 'sapOrderNumber' can not be blank.");

		final String requestedSapOrderNumber = this.stripLeadingZeros(sapOrderNumber);
		final List<OrderModel> matchingOrders = orders.stream().filter(
				it ->
				{
					final String foundSapOrderNumber = this.stripLeadingZeros(it.getSapOrderNumber());
					return StringUtils.equals(foundSapOrderNumber, requestedSapOrderNumber);
				}
		).collect(Collectors.toList());

		if (matchingOrders.isEmpty())
		{
			throw new PacHiveException(String.format(
					"No orders were found with sapOrderNumber like '%s' using fuzzy search after filtering.",
					sapOrderNumber
			));
		}

		if (matchingOrders.size() > 1)
		{
			final OrderModel result = matchingOrders.get(0);
			LOG.error(
					"[PAC HIVE] More than one order model was found using fuzzy search with sapOrderNumber like '{}'" +
					" and it is not clear which to use." +
					" Matching orders: '{}'. The first one with PK: '{}' will be used.",
					sapOrderNumber,
					matchingOrders.stream().map(OrderModel::getSapOrderNumber).collect(Collectors.joining(", ")),
					result.getPk()
			);
			return result;
		}

		final OrderModel result = matchingOrders.get(0);
		LOG.debug("[PAC HIVE] One order model was found using fuzzy search with sapOrderNumber like '{}'." +
		          " OrderModel.PK '{}' sapOrderNumber: '{}'.",
		          sapOrderNumber, result.getPk(), result.getSapOrderNumber()
		);

		return result;
	}

	@Nullable
	protected String stripLeadingZeros(@Nullable final String sapOrderlineNumber)
	{
		return StringUtils.stripStart(sapOrderlineNumber, "0");
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
