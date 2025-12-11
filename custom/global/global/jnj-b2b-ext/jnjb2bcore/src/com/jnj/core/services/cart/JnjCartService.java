/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.cart;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;

import java.util.List;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjCartService extends CommerceCartService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.order.CommerceCartService#addToCart(de.hybris.platform.core.model.order.CartModel
	 * , de.hybris.platform.core.model.product.ProductModel, long, de.hybris.platform.core.model.product.UnitModel,
	 * boolean)
	 */
	@Override
	public abstract CommerceCartModification addToCart(CartModel cartmodel, ProductModel productmodel, long quantityToAdd,
			UnitModel unitmodel, boolean flag) throws CommerceCartModificationException;







	/**
	 * The saveCartModel method persists the cart model in the hybris data base.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @param saveEntires
	 * @return true, if successful
	 */
	public boolean saveCartModel(AbstractOrderModel cartModel, boolean saveEntires);

	/**
	 * The saveAbstOrderEntriesModel method persists the all the abstract order entry model in the hybris data base.
	 * 
	 * @param cartEntryModelList
	 *           the cart entry model list
	 * @return true, if successful
	 */
	public boolean saveAbstOrderEntriesModels(final List<AbstractOrderEntryModel> cartEntryModelList);



	/**
	 * Gets the CartEntryModel for given entryNumber.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @param cartEntryNumber
	 *           the cart entry number
	 * @return CartEntryModel the CartEntry w.r.t. entryNumber from given Cart.
	 */
	public AbstractOrderEntryModel getEntryModelForNumber(CartModel cartModel, int cartEntryNumber);



	/**
	 * @param abstOrdEntModel
	 */
	public boolean saveAbstOrderEntry(AbstractOrderEntryModel abstOrdEntModel);

	/**
	 * Update shipping adress.
	 * 
	 * @param addressId
	 *           the address id
	 * @return true, if successful
	 */
	public boolean updateShippingAdress(final String addressId);



	/**
	 * Creates the cart from order.
	 * 
	 * @param orderId
	 *           the order id
	 */
	public void createCartFromOrder(final String orderId);

	public void calculateSessionCart();

	/**
	 * Calculate validated cart.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @return true, if successful
	 */
	public boolean calculateValidatedCart(final AbstractOrderModel cartModel);
	
	public boolean calculateGTValidatedCart(final AbstractOrderModel cartModel);
	
	
	/**
	 * Gets the CartEntryModel for given entryNumber.
	 *
	 * @param cartModel
	 *           the AbstractOrder model
	 * @param cartEntryNumber
	 *           the cart entry number
	 * @return CartEntryModel the CartEntry w.r.t. entryNumber from given Cart.
	 */
	public AbstractOrderEntryModel getEntryModelForNumber(AbstractOrderModel cartModel, int cartEntryNumber);
	
	/**
	 * @param order
	 */
	public void calculateValidateEntries(final AbstractOrderModel order);
	
	
}
