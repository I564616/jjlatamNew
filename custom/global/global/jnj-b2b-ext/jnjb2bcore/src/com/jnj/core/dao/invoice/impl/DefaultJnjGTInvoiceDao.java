/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.invoice.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.invoice.JnjGTInvoiceDao;
import com.jnj.core.model.JnjGTInvoiceEntryLotModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTInvoicePriceModel;


/**
 * TODO:<Class level comments missing>
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTInvoiceDao implements JnjGTInvoiceDao
{
	protected static Logger LOGGER = Logger.getLogger(DefaultJnjGTInvoiceDao.class);
	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	protected ModelService modelService;

	
	public ModelService getModelService() {
		return modelService;
	}

	@Override
	public List<JnjGTInvoiceModel> getInvoiceDetailsByCode(final String orderCode)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTInvoiceModel._TYPECODE)
				.append(" AS INVOICE JOIN ").append(OrderModel._TYPECODE).append(" AS ORD ON {INVOICE:ORDER} = {ORD:PK}}")
				.append(" WHERE {ORD:CODE} = ?code");

		queryParams.put(OrderModel.CODE, orderCode);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjGTInvoiceModel> result = getFlexibleSearchService().<JnjGTInvoiceModel> search(flexibleSearchQuery)
				.getResult();
		return result;
	}

	@Override
	public JnjGTInvoiceModel getInvoiceByInvoiceNum(final String invDocNo)
	{
		final JnjGTInvoiceModel tempInvoiceOrderModel = new JnjGTInvoiceModel();
		tempInvoiceOrderModel.setInvoiceDocNum(invDocNo);
		tempInvoiceOrderModel.setInvoiceNum(invDocNo);

		JnjGTInvoiceModel jnjGTInvoiceModel = null;
		try
		{
			jnjGTInvoiceModel = flexibleSearchService.getModelByExample(tempInvoiceOrderModel);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{

			LOGGER.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + "getInvoiceByInvoiceNum" + Logging.HYPHEN
					+ "Invoiec Model with invoiceDocNo: " + invDocNo + "Not Found. ModelNotFoundException occured. Returning Null."
					+ modelNotFoundException.getMessage());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + "getInvoiceByInvoiceNum" + Logging.HYPHEN
					+ "IllegalArgumentException Occured. Returning Null." + illegalArgumentException.getMessage());
		}
		return jnjGTInvoiceModel;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public JnjGTInvoiceEntryModel getInvoiceEntryByInvoiceNumAdLineItemNum(final String invoiceNum, final String lineNum)
	{
		JnjGTInvoiceEntryModel jnjGTInvoiceEntryModel = null;
		final JnjGTInvoiceEntryModel tempInvoiceEntryModel = new JnjGTInvoiceEntryModel();
		tempInvoiceEntryModel.setInvoiceNum(invoiceNum);
		tempInvoiceEntryModel.setLineNum(lineNum);

		try
		{
			jnjGTInvoiceEntryModel = flexibleSearchService.getModelByExample(tempInvoiceEntryModel);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{

			LOGGER.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + "getInvoiceEntryByInvoiceNumAdLineItemNum" + Logging.HYPHEN
					+ "Invoiec Model with invoiceDocNo: " + invoiceNum + " And lineItemNum : " + lineNum
					+ " Not Found. ModelNotFoundException occured. Returning Null." + modelNotFoundException.getMessage());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + "getInvoiceEntryByInvoiceNumAdLineItemNum" + Logging.HYPHEN
					+ "IllegalArgumentException Occured. Returning Null." + illegalArgumentException.getMessage());
		}
		return jnjGTInvoiceEntryModel;
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public JnjGTInvoiceEntryLotModel getInvoiceEntryLotByInvNumLineItemAndLotNum(final String invoiceDocNum,
			final String invoiceEntryNum, final String lotNum)
	{
		JnjGTInvoiceEntryLotModel jnjGTInvoiceEntryLotModel = null;
		final JnjGTInvoiceEntryLotModel tempJnJInvoiceLotEntryModel = modelService.create(JnjGTInvoiceEntryLotModel.class);
		tempJnJInvoiceLotEntryModel.setInvoiceNum(invoiceDocNum);
		tempJnJInvoiceLotEntryModel.setInvoiceLineNum(invoiceEntryNum);
		tempJnJInvoiceLotEntryModel.setLotNum(lotNum);

		try
		{
			jnjGTInvoiceEntryLotModel = flexibleSearchService.getModelByExample(tempJnJInvoiceLotEntryModel);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{

			LOGGER.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + "getInvoiceEntryLotByInvNumLineItemAndLotNum" + Logging.HYPHEN
					+ "Invoiec Model with invoiceDocNo: " + invoiceDocNum + " And lineItemNum : " + invoiceEntryNum + " And LotNum : "
					+ lotNum + " Not Found. ModelNotFoundException occured. Returning Null." + modelNotFoundException.getMessage());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + "getInvoiceEntryLotByInvNumLineItemAndLotNum" + Logging.HYPHEN
					+ "IllegalArgumentException Occured. Returning Null." + illegalArgumentException.getMessage());
		}
		return jnjGTInvoiceEntryLotModel;
	}


	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public List<JnjGTInvoicePriceModel> getInvoicePricesByInvoiceNum(final String invoiceDocNum)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTInvoicePriceModel._TYPECODE)
				.append("} WHERE {").append(JnjGTInvoicePriceModel.INVOICENUM).append("} = ?code");

		queryParams.put("code", invoiceDocNum);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjGTInvoicePriceModel> result = flexibleSearchService.<JnjGTInvoicePriceModel> search(flexibleSearchQuery)
				.getResult();
		return result;

	}

}
