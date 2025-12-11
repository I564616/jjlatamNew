/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.cart.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.cart.JnjGTCartDao;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTProductLotModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTShippingMethodModel;
import com.jnj.core.util.JnJCommonUtil;



/**
 * TODO:<class level comments are missing>.
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTCartDao implements JnjGTCartDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCartDao.class);
	
	@Autowired
	FlexibleSearchService flexibleSearchService;

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	@Override
	public List<JnjGTShippingMethodModel> getShippingMethods()
	{
		final String query = "select {pk} from {JnjGTShippingMethod}";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final List<JnjGTShippingMethodModel> result = flexibleSearchService.<JnjGTShippingMethodModel> search(fQuery).getResult();
		return result;
	}

	@Override
	public boolean isValidLotNumber(final String invoiceNum, final String lotNumber)
	{
		boolean lotNumfound = false;
		final String query = "SELECT {i:pk} FROM {jnjGTinvoiceEntry as i LEFT JOIN jnjproduct AS p ON {i:product}={p:pk} LEFT JOIN "
				+ "JnjProduct2LotRelationship AS lotProdRel ON {lotProdRel:source}={p:pk} LEFT JOIN JnjGTProductLot as lot ON {lot:pk}={lotProdRel:target}}  "
				+ "WHERE {i:invoiceNum}=?invoiceNum AND {lot:lotNumber}=?lotNumber";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("lotNumber", lotNumber);
		params.put("invoiceNum", invoiceNum);
		fQuery.addQueryParameters(params);

		final List<JnjGTInvoiceEntryModel> result = flexibleSearchService.<JnjGTInvoiceEntryModel> search(fQuery).getResult();
		if (CollectionUtils.isNotEmpty(result))
		{
			lotNumfound = true;
		}

		return lotNumfound;
	}
	
	//FIX GTR-1768
	@Override
	public boolean isValidLotNumForProduct(final String lotNumber, final String pcode)
	{
		final boolean result = false;
		final Map queryParams = new HashMap();
		//ServicesUtil.validateParameterNotNull(jnjId, "Id must not be null");
		final String query = "select {pk} from {JnjGTProductLot} where {lotNumber}=?lotNumber and {productCode}=?pcode";
		queryParams.put("lotNumber", lotNumber);
		queryParams.put("pcode", pcode);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		LOGGER.debug("validate lot num.. " + fQuery);
		int recCount = getFlexibleSearchService().<JnjGTProductLotModel> search(fQuery).getTotalCount();
		LOGGER.debug("validate lot num.. result count.. " + recCount);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getAddressByIdandSourceSysId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recCount>0;
	}


	@Override
	public boolean validateInvoiceNumber(final JnJProductModel product, final String returnInvNumber)
	{
		boolean exists = false;

		final String query = "SELECT {pk} FROM {JnjGTInvoiceEntry} where {invoiceNum}=?invoiceNum and {product}=?prodPK";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("invoiceNum", returnInvNumber);
		params.put("prodPK", product.getPk());
		fQuery.addQueryParameters(params);
		List<JnjGTInvoiceEntryModel> invoiceRecords = null;
		invoiceRecords = flexibleSearchService.<JnjGTInvoiceEntryModel> search(fQuery).getResult();

		if (CollectionUtils.isNotEmpty(invoiceRecords))
		{
			exists = true;
		}
		return exists;
	}

	@Override
	public JnjGTInvoiceEntryModel findInvoiceEntryforProduct(final JnJProductModel product, final String invoiceNumber)
	{
		JnjGTInvoiceEntryModel invoiceEntryModel = null;
		//final String query = "SELECT {pk} FROM {JnjGTInvoiceEntry} where {invoiceNum}=?invoiceNum and {product}=?prodPK";
		final String query = "SELECT {pk} FROM {JnjGTInvoiceEntry} where {invoiceNum}=?invoiceNum AND "
				+ "({product}=?prodPK OR {product} in ( {{ select {pk} from {jnjproduct} where {materialBaseProduct}=?prodPK }}) )";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("invoiceNum", invoiceNumber);
		params.put("prodPK", product.getPk());
		fQuery.addQueryParameters(params);
		List<JnjGTInvoiceEntryModel> invoiceRecords = null;
		invoiceRecords = flexibleSearchService.<JnjGTInvoiceEntryModel> search(fQuery).getResult();
		if (CollectionUtils.isNotEmpty(invoiceRecords))
		{
			invoiceEntryModel = invoiceRecords.get(0);
		}
		return invoiceEntryModel;
	}

	@Override
	public boolean validateLotNumber(final JnJProductModel product, final String batchNumber)
	{
		boolean exists = false;
		//final String query = "SELECT {pk} FROM {JnjGTProductLot} where {lotNumber}=?lotNum and {product}=?prodPK";
		final String query = "SELECT {pk} FROM {JnjGTProductLot} where {lotNumber}=?lotNum and {productCode}=?productCode";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("lotNum", batchNumber);
		params.put("productCode", product.getCode());
		fQuery.addQueryParameters(params);
		List<JnjGTProductLotModel> lotRecords = null;
		lotRecords = flexibleSearchService.<JnjGTProductLotModel> search(fQuery).getResult();
		if (CollectionUtils.isNotEmpty(lotRecords))
		{
			exists = true;
		}
		return exists;
	}

	@Override
	public CartModel getCartForUserAndUnit(final BaseSiteModel site, final UserModel user, final B2BUnitModel b2BUnit)
	{
		final Map params = new HashMap();
		params.put("site", site);
		params.put("user", user);
		params.put("b2BUnit", b2BUnit);

		final String query = "SELECT {pk} FROM {Cart} WHERE {user} = ?user AND {site} = ?site AND {unit}=?b2BUnit ORDER BY {modifiedtime} DESC";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);

		final List<CartModel> carts = flexibleSearchService.<CartModel> search(fQuery).getResult();

		if ((carts != null) && (!(carts.isEmpty())))
		{
			return carts.get(0);
		}
		return null;
	}


	@Override
	public List<JnjGTShippingMethodModel> getFasterShippingMethods(final int rank, final List<String> routeList)
	{

		final Map params = new HashMap();
		params.put("rank", rank);
		params.put("requiredIds", routeList);
		final String query = "select {pk} from {JnjGTShippingMethod} where {rank} >=?rank  and {route} in (?requiredIds) order by {rank} asc";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);
		final List<JnjGTShippingMethodModel> result = flexibleSearchService.<JnjGTShippingMethodModel> search(fQuery).getResult();
		return result;
	}

	@Override
	public List<JnjGTShippingMethodModel> getRequiredShippingMethod(final List<String> requiredIds)
	{

		final Map params = new HashMap();
		params.put("requiredIds", requiredIds);
		final String query = "select {pk} from {JnjGTShippingMethod} where {route} in (?requiredIds) order by {route} order by {rank} asc";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);
		final List<JnjGTShippingMethodModel> result = flexibleSearchService.<JnjGTShippingMethodModel> search(fQuery).getResult();
		return result;


	}

}