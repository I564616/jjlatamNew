/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.cart.impl;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.B2BCartService;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractEntryPriceModel;
import com.jnj.core.services.JnJAddressService;
import com.jnj.core.services.JnjSalesOrgCustService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.services.customerEligibility.JnjCustomerEligibilityService;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCartService extends DefaultCommerceCartService implements JnjCartService
{


	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjCartService.class);

	/** The Constant CLASS_NAME. */
	protected static final String CLASS_NAME = DefaultJnjCartService.class.getName();

	/** The Constant LOG. */
	protected static final Logger LOG = Logger.getLogger(DefaultJnjCartService.class);

	protected static final String CHECKOUT_TRUE_FLAG = "Y";
	final Double DEFAULT_VALUE = Double.valueOf(0.0);

	protected static final String DEFAULT_CATEGORY_CODE = "DefaultCategory";

	@Resource(name = "commerceCartService")
	protected
	JnjGTCartService jnjGTCartService;

	@Autowired
	@Qualifier(value = "jnjGTAddressService")
	protected JnJAddressService jnjAddressService;

	@Autowired
	protected B2BCartService b2bCartService;

	@Autowired
	protected B2BOrderService b2bOrderService;

	@Autowired
	protected JnjSalesOrgCustService jnjSalesOrgCustService;

	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	protected Populator<ProductModel, ProductData> productPricePopulator;


	@Autowired
	protected SessionService sessionService;


	@Autowired
	UserService userService;

	@Autowired
	protected JnjCustomerEligibilityService customerEligibilityService;


	public JnJAddressService getJnjAddressService() {
		return jnjAddressService;
	}

	public B2BCartService getB2bCartService() {
		return b2bCartService;
	}

	public B2BOrderService getB2bOrderService() {
		return b2bOrderService;
	}

	public JnjSalesOrgCustService getJnjSalesOrgCustService() {
		return jnjSalesOrgCustService;
	}

	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}

	public Populator<ProductModel, ProductData> getProductPricePopulator() {
		return productPricePopulator;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public UserService getUserService() {
		return userService;
	}

	public JnjCustomerEligibilityService getCustomerEligibilityService() {
		return customerEligibilityService;
	}
	

	@Override
	public boolean calculateCart(final CartModel cartModel)
	{
		boolean calculated = false;
		try
		{
			if (BooleanUtils.isTrue(cartModel.getSapValidated()))
			{
				resetEntriesToDefaultPrice(cartModel);
				cartModel.setSapValidated(Boolean.FALSE);
			}
			else
			{
				calculateEntries(cartModel);
			}
			calculateTotals(cartModel);
			calculated = saveCartModel(cartModel, true);
		}
		catch (final CalculationException calculateExp)
		{
			LOGGER.error("cart calculaton not done");
		}
		return calculated;
	}

	protected void calculateEntries(final AbstractOrderModel order) throws CalculationException
	{
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			entry.setTotalPrice(entry.getBasePrice() * entry.getQuantity());
		}
	}

	protected void resetEntriesToDefaultPrice(final AbstractOrderModel order) throws CalculationException
	{
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			updatePriceForEntry(entry);
			entry.setTotalPrice(entry.getBasePrice() * entry.getQuantity());
		}
		order.setTotalFreightFees(DEFAULT_VALUE);
		order.setTotalTax(DEFAULT_VALUE);
		order.setTotalExpeditedFees(DEFAULT_VALUE);
		order.setTotalGrossPrice(DEFAULT_VALUE);
		order.setTotalInsurance(DEFAULT_VALUE);
		order.setTotalHandlingFee(DEFAULT_VALUE);
		order.setTotalDropShipFee(DEFAULT_VALUE);
		order.setTotalminimumOrderFee(DEFAULT_VALUE);
	}



	protected void calculateTotals(final AbstractOrderModel order)
	{
		double subtotal = 0.0;
		/*for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			subtotal += entry.getTotalPrice().doubleValue();
		}*/
		if(order != null) {
		subtotal += order.getTotalGrossPrice()+order.getTotalFees();
		LOGGER.info("calculateTotals subtotal : "+order.getTotalGrossPrice() + " + "+ order.getTotalFees());
		order.setTotalPrice(Double.valueOf(subtotal));
		}
	}


	/**
	 * Update price for entry with product Price
	 * 
	 * @param cartEntryModel
	 *           the cart entry model
	 */
	protected void updatePriceForEntry(final AbstractOrderEntryModel cartEntryModel)
	{

		final ProductData productData = new ProductData();
		productPricePopulator.populate(cartEntryModel.getProduct(), productData);
		if (null != productData.getPrice() && null != productData.getPrice().getValue())
		{
			cartEntryModel.setBasePrice(Double.valueOf(productData.getPrice().getValue().doubleValue()));
		}
		else
		{
			cartEntryModel.setBasePrice(DEFAULT_VALUE);
		}
		cartEntryModel.setDefaultPrice(DEFAULT_VALUE);
	}


	@Override
	public boolean saveAbstOrderEntry(final AbstractOrderEntryModel entryModel)
	{
		boolean success = Boolean.FALSE.booleanValue();
		try
		{
			getModelService().save(entryModel);
			success = true;
		}
		catch (final ModelSavingException modelSavingExp)
		{
			LOG.error("Not able save OrderEntry");
		}
		return success;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveAbstOrderEntriesModels(final List<AbstractOrderEntryModel> cartEntryModelList)
	{
		boolean saved = false;
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Before persisting the cart entries");
			}
			getModelService().saveAll(cartEntryModelList);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("After persisting the cart entries successfully");
			}
			saved = true;
		}
		catch (final ModelSavingException exception)
		{
			exception.printStackTrace();
			LOGGER.error(
					"saveAbstOrderEntriesModels()" + Logging.HYPHEN + "Abstract order entry list is not saved"
							+ exception.getMessage(), exception);
		}
		return saved;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveCartModel(final AbstractOrderModel cartModel, final boolean saveEntires)
	{
		boolean saved = false;
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Before persisting the cart model");
			}
			if (saveEntires)
			{
				saveAbstOrderEntriesModels(cartModel.getEntries());
			}
			getModelService().save(cartModel);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("CartModel saved successfully");
			}
			saved = true;
		}
		catch (final ModelSavingException exception)
		{
			LOGGER.error("saveCartModel()" + Logging.HYPHEN + "Cart Model Not saved" + exception.getMessage(), exception);
		}
		return saved;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractOrderEntryModel getEntryModelForNumber(final CartModel cartModel, final int number)
	{
		return getEntryForNumber(cartModel, number);
	}
	
	protected AbstractOrderEntryModel getEntryForNumber(
			AbstractOrderModel order, int number) {
		List<AbstractOrderEntryModel>  entries = order.getEntries();
		if ((entries != null) && (!(entries.isEmpty()))) {
			Integer requestedEntryNumber = Integer.valueOf(number);
			for (AbstractOrderEntryModel entry :  entries) {
				if ((entry != null)
						&& (requestedEntryNumber.equals(entry.getEntryNumber()))) {
					return entry;
				}
			}
		}
		return null;
	}

	@Override
	public boolean updateShippingAdress(final String pkString)
	{
		final AddressModel addressModel = jnjAddressService.getAddressByPK(pkString);
		final CartModel cartModel = b2bCartService.getSessionCart();
		cartModel.setDeliveryAddress(addressModel);
		return saveCartModel(cartModel, false);
	}



	@Override
	public void createCartFromOrder(final String orderId)
	{

		final CartModel sessionCart = b2bCartService.getSessionCart();
		// Iterating over Entries to set the Order Entries to Session Cart
		final OrderModel order = b2bOrderService.getOrderForCode(orderId);
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		for (final AbstractOrderEntryModel entry : entries)
		{
			try
			{
				addToCart(sessionCart, entry.getProduct(), entry.getQuantity().longValue(), entry.getProduct().getUnit(), false);
			}
			catch (final CommerceCartModificationException modificaitonExp)
			{
				LOGGER.error("Creating cart from Order---  Product Code: " + entry.getProduct().getCode() + " from Order:" + orderId
						+ " can not be added to cart");
			}

		}
		sessionCart.setUnit(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit());
		saveCartModel(sessionCart, true); //Save Cart Model
	}

	/**
	 * This method is used to calculate cart value
	 */
	@Override
	public void calculateSessionCart()
	{
		final CartModel sessionCart = b2bCartService.getSessionCart();
		calculateCart(sessionCart);
	}

	/**
	 * This method is used to calculate cart value after validation
	 */
	@Override
	public boolean calculateValidatedCart(final AbstractOrderModel cartModel)
	{
		boolean calculated = false;
		calculateTotals(cartModel);
		calculated = saveCartModel(cartModel, true);
		return calculated;
	}
	@Override
	public void calculateValidateEntries(final AbstractOrderModel order){
		Map<String, List<JnjContractEntryModel>> contractProductMap = new HashMap<String, List<JnjContractEntryModel>>();
		boolean basePriceFlag = false;
		Map<String, Set<String>> cartMap = new HashMap<String, Set<String>>();
		for (final AbstractOrderEntryModel entry : order.getEntries()) {
			String entryProductCode = entry.getProduct().getCode();
			if(!basePriceFlag){
				// if true then the productCode is avail in contract model else its not contract product
			basePriceFlag = jnjGTCartService.checkProductInContract(entryProductCode, basePriceFlag, contractProductMap,cartMap);
			}
			// basePriceflag = true then set baseprice of the product
			/*if(basePriceFlag){
			entry.setTotalPrice(entry.getBasePrice() * entry.getQuantity());
			}*/
		//}
		// basePriceflag = false then set contract price of the product
   		if(!basePriceFlag){
   			//for (final AbstractOrderEntryModel entry : order.getEntries()) {
   				String contractNumInEntry = entry.getContractNum();
   				String productCode = entry.getProduct().getCode();
   				Map<String, List<JnjContractEntryModel>> sessionContractProductMap = sessionService
   						.getAttribute("contractProductMap");
   				if (sessionContractProductMap.containsKey(contractNumInEntry)) {
   					List<JnjContractEntryModel> contractEntryModels = sessionContractProductMap.get(contractNumInEntry);
   					for (JnjContractEntryModel jnjContractEntryModel : contractEntryModels) {
   						if (productCode.equals(jnjContractEntryModel.getProduct().getCode())) {
   							JnjContractEntryPriceModel jnjContractEntryPriceModel = jnjContractEntryModel
   									.getContractPrice();
   							entry.setBasePrice(jnjContractEntryPriceModel.getPrice()); // setting base price as contract price
   							entry.setTotalPrice(entry.getBasePrice() * entry.getQuantity());
   							entry.setNetPrice(entry.getBasePrice() * entry.getQuantity());
   						}
   
   					}
   				}
   				saveAbstOrderEntry(entry);
   			//}
   		}
		}//for loop for the order entries
	}


	@Override
	public CommerceCartModification updateQuantityForCartEntry(final CartModel cartModel, final long entryNumber,
			final long newQuantity) throws CommerceCartModificationException
	{
		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");
		return null;

		/*
		 * final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) entryNumber);
		 * validateEntryBeforeModification(newQuantity, entryToUpdate); final Integer maxOrderQuantity =
		 * entryToUpdate.getProduct().getMaxOrderQuantity(); final long quantityToAdd = newQuantity -
		 * entryToUpdate.getQuantity().longValue();
		 * 
		 * final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel,
		 * entryToUpdate.getProduct(), quantityToAdd, null); //Set the price to default price from Hybris DB.
		 * updatePriceForEntry(entryToUpdate); return modifyEntry(cartModel, entryToUpdate, actualAllowedQuantityChange,
		 * newQuantity, maxOrderQuantity);
		 */
	}




	/**
	 * 
	 * Checks and returns true if any of super categories of the product is/are restricted as per Customer Eligibility.
	 * 
	 * @param product
	 * @return boolean
	 */
	protected boolean isProductAllignedToRestrictedCategory(final ProductModel product)
	{
		final Collection<CategoryModel> productSuperCategories = product.getSupercategories();
		final Set<CategoryModel> categories = new HashSet<CategoryModel>();
		Collection<CategoryModel> superCategories = null;

		for (final CategoryModel category : productSuperCategories)
		{
			categories.add(category);
			superCategories = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public Collection<CategoryModel> execute()
				{
					return category.getAllSupercategories();
				}
			}, userService.getAdminUser());

			if (superCategories != null && !superCategories.isEmpty())
			{
				categories.addAll(superCategories);
			}
		}
		final B2BCustomerModel currentCustomer = (B2BCustomerModel) userService.getCurrentUser();
		final JnJB2BUnitModel unit = (JnJB2BUnitModel) currentCustomer.getDefaultB2BUnit();
		final Set<String> restrictedCategoryCodes = customerEligibilityService.getRestrictedCategory(unit.getUid());
		restrictedCategoryCodes.add(DEFAULT_CATEGORY_CODE);

		if (categories.isEmpty())
		{
			return true;
		}
		else
		{

			for (final CategoryModel category : categories)
			{
				if (restrictedCategoryCodes.contains(category.getCode()))
				{
					return true;
				}
			}

		}
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	//Added method for JJEPIC-874
	@Override
	public AbstractOrderEntryModel getEntryModelForNumber(final AbstractOrderModel cartModel, final int number)
	{
		return getEntryForNumber(cartModel, number);
	}

	@Override
	public boolean calculateGTValidatedCart(AbstractOrderModel cartModel) {
	    boolean calculated = false;
		calculateGTTotals(cartModel);
		calculated = saveCartModel(cartModel, true);
		return calculated;
	}

	
	protected void calculateGTTotals(final AbstractOrderModel order)
	{
		double subtotal = 0.0;
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			subtotal += entry.getTotalPrice().doubleValue();
		}
		if(order != null) {
		subtotal += order.getTotalTax()+order.getTotalFees();
		order.setTotalPrice(Double.valueOf(subtotal));
		}
	}
}
