/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.cart.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.JnJ.common.logging.MessageLoggerHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.JnJCustomerDataService;
import com.jnj.core.services.JnJProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.facades.cart.JnjCartFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.data.JnjOrderEntryData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.order.JnjCheckoutFacade;
import com.jnj.facades.order.JnjOrderFacade;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCartFacade;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class DefaultJnjCartFacade extends DefaultCartFacade implements JnjCartFacade
{
	/** The Constant CLASS_NAME. */
	private static final String CLASS_NAME = DefaultJnjCartFacade.class.getName();

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCartFacade.class);

	/** The jnj cart service. */
	@Autowired
	JnjCartService jnjCartService;
	@Autowired
	@Qualifier(value = "jnjCustomOrderFacade")
	private JnjOrderFacade orderFacade;
	@Autowired
	B2BOrderService b2bOrderService;
	@Autowired
	JnjConfigService jnjConfigService;
	@Autowired
	private JnJProductService jnjProductService;
	//	@Autowired
	//	JnjOrderEntryPopulator jnjOrderEntryPopulator;
	@Autowired
	JnjCommonFacadeUtil jnjCommonFacadeUtil;
	@Autowired
	ModelService modelService;
	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;
	@Autowired
	private MessageFacadeUtill messageFacade;
	@Autowired
	private JnJCustomerDataService jnjCustomerDataService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	private ProductFacade b2bProductFacade;
	
	private  DefaultB2BCheckoutFacade checkoutFacade;
	private Populator<AbstractOrderModel, List<CartModificationData>> groupCartModificationListPopulator;
	
	//private JnjCheckoutFacade jnjCheckoutFacade;


	// Komal : Need to remove the  the boolean removeContract and related logic from the method  at the time of the implementation

	@Override
	public CartModificationData addToCart(final String code, final long quantity) throws CommerceCartModificationException
	{
		CartModificationData cartModificationData = null;
		try
		{
			// Getting Product Model By product Code
			final JnJProductModel product = jnjProductService.getProductCodeOrEAN(code);
			// Checking Weather Product is Valid Or Not Discontinue
			if (null != product && BooleanUtils.isNotTrue(product.getDisContinue()))
			{
				final CartModel cartModel = getCartService().getSessionCart();
				//Add To Cart
				final CommerceCartModification cartModification = jnjCartService.addToCart(cartModel, product, quantity,
						product.getUnit(), false);
				//End
				cartModificationData = getCartModificationConverter().convert(cartModification); // Calling CartModification Converter 
			}
			else
			{// Given product code is not valid
				cartModificationData = new CartModificationData();
				final OrderEntryData entryData = new OrderEntryData();
				final ProductData productData = new ProductData();
				productData.setCode(code);
				entryData.setProduct(productData);
				cartModificationData.setEntry(entryData);
				cartModificationData.setStatusCode(Jnjb2bCoreConstants.ADDTO_CART_ERROR);
				LOGGER.error("Add to Cart - Product Code-" + code + "not Found for current catalog");
			}
		}
		catch (final ModelLoadingException itemNotFoundExp)
		{
			LOGGER.error("Add to Cart - Product Code: " + code + "not Valid for add to session cart");

		}
		return cartModificationData;
	}


	// Komal : Need to remove the  the boolean removeContract and related logic from the method  at the time of the implementation
	@Override
	public JnjCartModificationData addToCart(final List<String> productCodeList) throws CommerceCartModificationException
	{
		final JnjCartModificationData jnjCartModificationData = new JnjCartModificationData();
		//if (removeContract  jnjContractFacade.isProductsExistsInContract(productCodeList))
		//{
		final List<CartModificationData> cartModificationList = new ArrayList<CartModificationData>();
		CartModificationData cartModificationData = null;
		for (final String code : productCodeList)
		{
			cartModificationData = addToCart(code.trim(), Jnjb2bCoreConstants.DEFAULT_ADD_TO_CART_QTY);
			cartModificationList.add(cartModificationData);
			if (Jnjb2bCoreConstants.MAX_QTY_LIMIT_EXCEED.equals(cartModificationData.getStatusCode()))
			{
				jnjCartModificationData.getProductsWithMaxQty().add(code);
			}
		}
		jnjCartModificationData.setCartModifications(cartModificationList);
		return jnjCartModificationData;
	}



	@Override
	public CartData getMiniCart()
	{
		final CartData cartData = super.getMiniCart();
		cartData.setTotalUnitCount(Integer.valueOf(getCartService().getSessionCart().getEntries().size()));
		return cartData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequestCompleteOrder()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CHECK_OUT_FUNCTIONALITY + Logging.HYPHEN + "updateChangePrice()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean isRequestCompleteOrder = false;
		final CartModel cartModel = getCartService().getSessionCart();
		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			for (final AbstractOrderEntryModel abstOrderEntryModel : cartModel.getEntries())
			{
				final JnJProductModel jnjProductModel = (JnJProductModel) abstOrderEntryModel.getProduct();
				if (StringUtils.isNotEmpty(jnjProductModel.getSector())
						&& !StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.PHR_SECTOR, jnjProductModel.getSector()))
				{
					isRequestCompleteOrder = true;
					break;
				}
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CHECK_OUT_FUNCTIONALITY + Logging.HYPHEN + "updateChangePrice()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return isRequestCompleteOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateDeliveryDate(final String expDeliveryDate, final int cartEntryNumber)
	{

		final String METHOD_NAME = Jnjb2bFacadesConstants.Validation.UPDATE_DELIVERY_DATE;
		Date expDeliveryDateInDateFormat = null;
//		final DateFormat dateFormat = new SimpleDateFormat(Jnjb2bFacadesConstants.Validation.DATE_FORMAT);
		final DateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		boolean saved = false;

		try
		{
			// get the current cart model object by using Cart Service
			final CartModel cartModel = getCartService().getSessionCart();
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("After getting Cart Model");
			}
			//Setting expected delivery date for one selected entry
			expDeliveryDateInDateFormat = dateFormat.parse(expDeliveryDate);
			if (cartEntryNumber >= 0)
			{
				final AbstractOrderEntryModel abstOrdEntModel = jnjCartService.getEntryModelForNumber(cartModel, cartEntryNumber);

				abstOrdEntModel.setExpectedDeliveryDate(expDeliveryDateInDateFormat);
				saved = jnjCartService.saveAbstOrderEntry(abstOrdEntModel);
			}
			//Setting expected delivery date for whole cart
			else if (null != cartModel && null != cartModel.getEntries() && !cartModel.getEntries().isEmpty())
			{
				// iterating each entry to set the expected delivery date.
				for (final AbstractOrderEntryModel abstOrdEntModel : cartModel.getEntries())
				{
					abstOrdEntModel.setExpectedDeliveryDate(expDeliveryDateInDateFormat);

				}
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("Before calling the jnj cart service");
				}
				jnjCartService.saveAbstOrderEntriesModels(cartModel.getEntries());
				saved = true;
			}
		}
		catch (final ParseException exception)
		{
			LOGGER.error(MessageLoggerHelper
					.buildErrorMessage("userId", "transactionId", "code", "message", CLASS_NAME, METHOD_NAME) + exception);
		}

		return saved;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * 
	 */
	@Override
	public Map<String, String> getPrice(final String entryNumber, final String deliveryAddressId) throws IntegrationException
	{

		final Map<String, String> entryPriceMap = new HashMap<String, String>();

		// Call The Get Price SAP method from here.

		return entryPriceMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnjOrderData placeOrderInSap(final String orderCode) throws SystemException, IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "placeOrderInSap()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//	final boolean created = false;
		JnjOrderData orderData = null;
		final OrderModel orderModel = b2bOrderService.getOrderForCode(orderCode);
		modelService.save(orderModel);
		//To Call the create method of SAP from here.
		/*
		 * final SalesOrderCreationResponse salesOrderCreationResponse =
		 * orderFacade.invokeSalesOrderCreationWrapper(orderModel); if (null != salesOrderCreationResponse) {
		 */
		orderData = new JnjOrderData();
		orderConverter.convert(orderModel, orderData);
		/* } */
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "placeOrderInSap()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return orderData;
	}

	@Override
	public boolean updateShippingAdress(final String addressId)
	{
		return jnjCartService.updateShippingAdress(addressId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	//	Change the return type of the Method for CP-002
	@Override
	public JnjValidateOrderData validateOrder() throws IntegrationException, SystemException, BusinessException
	{
		JnjValidateOrderData validateOrderData;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "validateOrder()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// Cart Model object of the logged user is retrieved by using getSessionCart method of Cart Service.
		final CartModel cartModel = getCartService().getSessionCart();

		// Invoking the invokeSalesOrderSimulateWrapper method of the Order Facade.
		validateOrderData = orderFacade.invokeSalesOrderSimulateWrapper(cartModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "validateOrder()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return validateOrderData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String updateChangePrice(final String changePrice, final String reason, final int cartEntryId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CHECK_OUT_FUNCTIONALITY + Logging.HYPHEN + "updateChangePrice()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean saved = false;
		String updatedChangedPrice = null;
		String updChangedPriceWithoutComma = null;
		// get the current cart model object by using Cart Service
		final CartModel cartModel = getCartService().getSessionCart();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("After getting Cart Model ");
		}
		//Setting expected delivery date for one selected entry
		if (cartEntryId >= 0)
		{
			final AbstractOrderEntryModel abstOrdEntModel = jnjCartService.getEntryModelForNumber(cartModel, cartEntryId);
			if (StringUtils.isNotEmpty(changePrice) && changePrice.contains(Jnjb2bFacadesConstants.COMMA_IN_STRING))
			{
				updChangedPriceWithoutComma = changePrice.replaceAll(Jnjb2bFacadesConstants.COMMA_IN_STRING,
						Jnjb2bFacadesConstants.EMPTY_STRING);
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(Logging.CHECK_OUT_FUNCTIONALITY + Logging.HYPHEN + "updateChangePrice()" + Logging.HYPHEN
							+ "Changed Price Value without comma - " + updChangedPriceWithoutComma);
				}
				abstOrdEntModel.setPriceOverride(updChangedPriceWithoutComma);
			}
			else
			{
				abstOrdEntModel.setPriceOverride(changePrice);
			}
			abstOrdEntModel.setPriceOverrideReason(reason);

			saved = jnjCartService.saveAbstOrderEntry(abstOrdEntModel);
			if (saved)
			{
				final CurrencyModel currencyModel = abstOrdEntModel.getOrder().getCurrency();

				updatedChangedPrice = currencyModel.getSymbol().concat(changePrice);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CHECK_OUT_FUNCTIONALITY + Logging.HYPHEN + "updateChangePrice()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return updatedChangedPrice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getChangeReasonData()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CHECK_OUT_FUNCTIONALITY + Logging.HYPHEN + "getChangeReasonData()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<String> changeReasonCodes = jnjConfigService.getConfigValuesById(Jnjb2bFacadesConstants.Order.CHANGE_REASON,
				Jnjb2bFacadesConstants.Order.TILT_SEPARATOR);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CHECK_OUT_FUNCTIONALITY + Logging.HYPHEN + "updateChangePrice()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return changeReasonCodes;
	}


	/**
	 * This method is used to get calculate cart model which will further call calculateCartModel(CartModel)
	 */
	@Override
	public void createCartFromOrder(final String orderId)
	{
		jnjCartService.createCartFromOrder(orderId);
		jnjCartService.calculateSessionCart();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderEntryData getOrderEntryData(final int entryNumber)
	{
		JnjGTOrderEntryData jnjOrderEntryData = null;
		if (entryNumber >= 0)
		{
			// get the current cart model object by using Cart Service
			final CartModel cartModel = getCartService().getSessionCart();
			final AbstractOrderEntryModel abstOrdEntModel = jnjCartService.getEntryModelForNumber(cartModel, entryNumber);
			jnjOrderEntryData = new JnjGTOrderEntryData();
			//	jnjOrderEntryPopulator.populate(abstOrdEntModel, jnjOrderEntryData);
		}
		return jnjOrderEntryData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.cart.JnjCartFacade#addToCart(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public JnjCartModificationData addToCartQuick(final String productCode, final String productQuantity,List<CartModificationData> cartModificationList)
			throws CommerceCartModificationException
	{
		final JnjCartModificationData jnjCartModificationData = new JnjCartModificationData();
		final long productQunatityToBeAdded = Long.parseLong(productQuantity);
//		final List<CartModificationData> cartModificationList = new ArrayList<CartModificationData>();
		CartModificationData cartModificationData = null;
		// Add To Cart 
		cartModificationData = addToCart(productCode, productQunatityToBeAdded);
		// End 
		cartModificationList.add(cartModificationData);
		jnjCartModificationData.setCartModifications(cartModificationList);
		return jnjCartModificationData;
	}
	
	
	//Quick add to cart End
	
	
	@Override
	public JnjCartModificationData addToCart(final String productCode, final String productQuantity)
			throws CommerceCartModificationException
	{
		final JnjCartModificationData jnjCartModificationData = new JnjCartModificationData();
		final long productQunatityToBeAdded = Long.parseLong(productQuantity);
		final List<CartModificationData> cartModificationList = new ArrayList<CartModificationData>();
		CartModificationData cartModificationData = null;
		// Add To Cart 
		cartModificationData = addToCart(productCode, productQunatityToBeAdded);
		// End 
		cartModificationList.add(cartModificationData);
		jnjCartModificationData.setCartModifications(cartModificationList);
		return jnjCartModificationData;
	}
	
	@Override
	public boolean isProductCodeValid(final String productId)
	{

		boolean isProductCodeValid = false;
		final ProductModel product = jnjProductService.getProductCodeOrEAN(productId);
		if (product != null)
		{
			isProductCodeValid = true;
		}
		return isProductCodeValid;
	}

	/**
	 * This method is used to get calculate cart model which will further call calculateCartModel(CartModel
	 */
	@Override
	public void calculateSessionCart()
	{
		jnjCartService.calculateSessionCart();
	}


	/**
	 * @return the b2bProductFacade
	 */
	public ProductFacade getB2bProductFacade()
	{
		return b2bProductFacade;
	}


	/**
	 * @param b2bProductFacade
	 *           the b2bProductFacade to set
	 */
	public void setB2bProductFacade(final ProductFacade b2bProductFacade)
	{
		this.b2bProductFacade = b2bProductFacade;
	}

	protected Populator<AbstractOrderModel, List<CartModificationData>> getGroupCartModificationListPopulator()
	{
		return groupCartModificationListPopulator;
	}

	public void setGroupCartModificationListPopulator(
			final Populator<AbstractOrderModel, List<CartModificationData>> groupCartModificationListPopulator)
	{
		this.groupCartModificationListPopulator = groupCartModificationListPopulator;
	}


	public DefaultB2BCheckoutFacade getCheckoutFacade() {
		return checkoutFacade;
	}


	public void setCheckoutFacade(DefaultB2BCheckoutFacade checkoutFacade) {
		this.checkoutFacade = checkoutFacade;
	}

	
	@Override
	public boolean updateCartModel(JnjGTCartData cart) {
		
		final CartModel currentCart = getCartService().getSessionCart();
		
		currentCart.setPurchaseOrderNumber(cart.getPurchaseOrderNumber());
		currentCart.setPoDate(cart.getPoDate());
		currentCart.setRequestedDeliveryDate(cart.getExpectedShipDate());
		currentCart.setEndUser(cart.getEndUser());
		currentCart.setStockUser(cart.getStockUser());
		currentCart.setShippingInstruction(cart.getShippingInstructions());
		return jnjCartService.saveCartModel(currentCart, true);
	}

	/*public JnjCheckoutFacade getCheckoutFacade() {
		return jnjCheckoutFacade;
	}


	public void setCheckoutFacade(JnjCheckoutFacade jnjCheckoutFacade) {
		this.jnjCheckoutFacade = jnjCheckoutFacade;
	}*/



}
