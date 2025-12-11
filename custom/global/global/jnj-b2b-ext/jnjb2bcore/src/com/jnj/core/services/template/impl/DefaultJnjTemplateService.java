/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.template.impl;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.template.JnjTemplateDao;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjOrderTemplateModel;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.services.template.JnjTemplateService;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjTemplateService implements JnjTemplateService
{
	/** The Constant LOG. */
	protected static final Logger LOG = Logger.getLogger(DefaultJnjTemplateService.class);

	@Autowired
	protected CartService cartService;

	@Autowired
	protected ModelService modelService;

	@Autowired
	UserService userService;

	@Autowired
	protected JnjCartService jnjCartService;


	@Resource(name = "defaultjnjTemplateDao")
	protected JnjTemplateDao jnjTemplateDao;


	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	protected B2BOrderService b2bOrderService;
	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;



	public CartService getCartService() {
		return cartService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public UserService getUserService() {
		return userService;
	}

	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}

	public JnjTemplateDao getJnjTemplateDao() {
		return jnjTemplateDao;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public B2BOrderService getB2bOrderService() {
		return b2bOrderService;
	}

	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}

	@Override
	public boolean createTempateFromSessionCart()
	{
		final CartModel cart = cartService.getSessionCart();
		return createOrderTemplate(cart);
	}

	@Override
	public boolean createTempateFromOrder(final String orderId)
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderId);
		return createOrderTemplate(order);
	}

	/**
	 * Creates the order template.
	 * 
	 * @param cart
	 *           the cart
	 * @return true, if successful
	 */
	protected boolean createOrderTemplate(final AbstractOrderModel cart)
	{
		final List<AbstractOrderEntryModel> cartEntryList = cart.getEntries();
		final String code = cart.getCode();
		final List<JnjTemplateEntryModel> templateEntryModelList = new ArrayList<JnjTemplateEntryModel>();
		JnjOrderTemplateModel jnjOrderTemplateModel = modelService.create(JnjOrderTemplateModel.class);
		jnjOrderTemplateModel.setCode(code);
		try
		{

			//Trying to fetch existing one
			final JnjOrderTemplateModel savedTemplateModel = flexibleSearchService.getModelByExample(jnjOrderTemplateModel);
			//If exist set the latest with old one.
			jnjOrderTemplateModel = savedTemplateModel;
		}
		catch (final ModelNotFoundException mnfe)
		{
			LOG.info("Template not found for code:" + code + "Creating new Template");
		}
		for (final AbstractOrderEntryModel cartEntry : cartEntryList)
		{
			final JnjTemplateEntryModel templateEntryModel = modelService.create(JnjTemplateEntryModel.class);
			templateEntryModel.setProduct((JnJProductModel) cartEntry.getProduct());
			templateEntryModel.setQty(cartEntry.getQuantity());
			templateEntryModel.setUnitPrice(cartEntry.getBasePrice());
			templateEntryModel.setTotalPrice(cartEntry.getTotalPrice());
			templateEntryModel.setOrderTemplate(jnjOrderTemplateModel);
			templateEntryModelList.add(templateEntryModel);
		}
		jnjOrderTemplateModel.setEntryList(templateEntryModelList);
		jnjOrderTemplateModel.setUnit(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit());
		return saveTemplate(jnjOrderTemplateModel);
	}

	@Override
	public boolean saveTemplate(final JnjOrderTemplateModel jnjOrderTemplateModel)
	{
		boolean success = false;
		try
		{
			modelService.save(jnjOrderTemplateModel);
			success = true;
		}
		catch (final ModelSavingException modelSavingExp)
		{
			LOG.error("Not able to create Order Template" + modelSavingExp);
		}
		return success;
	}


	@Override
	public List<JnjOrderTemplateModel> searchOrderTemplate(final String searchByCriteria, final String searchParameter)
	{
		return jnjTemplateDao.searchOrderTemplate(searchByCriteria, searchParameter);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.template.JnjTemplateService#deleteTemplate(java.lang.String)
	 */
	@Override
	public boolean deleteTemplate(final String templateCode)
	{

		boolean confirmDelete = false;
		try
		{
			final JnjOrderTemplateModel templateModel = jnjTemplateDao.getJnjTemplateByCode(templateCode);
			modelService.removeAll(templateModel.getEntryList());
			modelService.remove(templateModel);
			confirmDelete = true;
		}
		catch (final ModelRemovalException e)
		{
			confirmDelete = false;
		}

		return confirmDelete;
	}

	public JnjOrderTemplateModel getTemplateForCode(final String templateCode)
	{

		return jnjTemplateDao.getJnjTemplateByCode(templateCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.template.JnjTemplateService#addTemplateToCart(java.lang.String)
	 */
	@Override
	public boolean addTemplateToCart(final String templateCode)
	{
		boolean success = Boolean.FALSE.booleanValue();
		final JnjOrderTemplateModel templateModel = jnjTemplateDao.getJnjTemplateByCode(templateCode);
		final List<JnjTemplateEntryModel> templateEntryList = templateModel.getEntryList();

		try
		{
			//Iterate over the contract entries and adding these to the session cart
			if (CollectionUtils.isNotEmpty(templateEntryList))
			{
				for (final JnjTemplateEntryModel templateEntry : templateEntryList)
				{
					final JnJProductModel product = templateEntry.getProduct();
					final CartModel cartModel = cartService.getSessionCart();
					final long qty = templateEntry.getQty();
					jnjCartService.addToCart(cartModel, product, qty, product.getUnit(), false);
					success = true;
				}
			}
		}
		catch (final CommerceCartModificationException cartModificatonExp)
		{
			LOG.debug("Not able to load Template for id :" + templateCode);
		}

		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.services.template.JnjTemplateService#getPagedOrderTemplates(de.hybris.platform.commerceservices.search
	 * .pagedata.PageableData, java.lang.String, java.lang.String)
	 */
	@Override
	public SearchPageData<JnjOrderTemplateModel> getPagedOrderTemplates(final PageableData pageableData,
			final String searchByCriteria, final String searchParameter, final String sortByCriteria)
	{
		return jnjTemplateDao.getPagedOrderTemplates(pageableData, searchByCriteria, searchParameter, sortByCriteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.template.JnjTemplateService#getRecenlyUsedContracts()
	 */
	@Override
	public List<JnjOrderTemplateModel> getRecenlyUsedTemplates()
	{

		return jnjTemplateDao.getRecenlyUsedTemplates();
	}


}