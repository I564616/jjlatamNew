/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.facades.vtex.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.service.vtex.order.JnjLatamVtexOrderService;
import com.jnj.core.services.JnJAddressService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjLaCartData;
import com.jnj.facades.data.JnjLatamOrderRequestData;
import com.jnj.facades.data.JnjLatamOrdersListData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.data.SapErrorMessageData;
import com.jnj.facades.data.SapErrorMessagesData;
import com.jnj.facades.order.JnjLatamCheckoutFacade;
import com.jnj.facades.vtex.order.JnjLatamVtexOrderFacade;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnjOrderChannelModel;
import com.jnj.la.core.services.order.JnjLAOrderService;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;


/**
 * The implementation class DefaultJnjLatamVtexOrderFacade.
 *
 */
public class DefaultJnjLatamVtexOrderFacade implements JnjLatamVtexOrderFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultJnjLatamVtexOrderFacade.class);
	
	private static final String PARAMETER_ACCOUNTID_CANNOT_BE_NULL = "Parameter accountId cannot be null";
	private static final String SESSION_CART_PARAMETER_NAME = "cart";
	private static final String PRODUCTS_NOT_ADDED = "One or more products are not added into the Cart";
	private static final String SUCCESS = "success";
	private static final String ADD_TO_CART = "Add to Cart";

	private static final String CART_CLEANUP_REQUIRED = "isCartCleanUprequired";
	private static final String COLON = ":";

	private SessionService sessionService;
    private CartService cartService;
    private JnjLatamVtexOrderService jnjLatamVtexOrderService;
    private JnjLatamCartFacade jnJLatamCartFacade;
	private JnJAddressService jnjAddressService;
	private Converter<CartModel, JnjLaCartData> defaultCartConverter;
	private B2BOrderService b2bOrderService;
	private JnjLatamCheckoutFacade jnjLatamCheckoutFacade;
	private Converter<OrderModel, JnjGTOrderData> jnjOrderConverter;
	private Converter<OrderModel, OrderHistoryData> orderHistoryConverter;
	private UserService userService;
	private ModelService modelService;
	private JnjLAOrderService jnjLAOrderService;
	private JnjConfigService jnjConfigService;
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Sets the current B 2 B unit.
	 *
	 * @param accountId the new current B 2 B unit
	 */
	private void setCurrentB2BUnit(final String accountId)
	{
		validateParameterNotNull(accountId, PARAMETER_ACCOUNTID_CANNOT_BE_NULL);
        final JnJB2BUnitModel b2bUnit = getB2bUnitForAccountId(accountId);
        if (b2bUnit == null)
		{
            throw new UnknownIdentifierException("Account is not found for given ID: " + accountId);
        }
        getSessionService().setAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT, b2bUnit);
	}
	
	/**
	 * Creates the vtex cart.
	 *
	 * @param orderRequestData the order request data
	 */
    @Override
	public void createVtexCart(final JnjLatamOrderRequestData orderRequestData) throws BusinessException
	{
		final Optional<AbstractOrderModel> vtexCart = this.getVtexCart(orderRequestData.getCartReferenceNumber());
		if(vtexCart.isPresent() && vtexCart.get() instanceof CartModel)
		{
			jnjLatamVtexOrderService.removeVtexCart(vtexCart.get());
		}
		this.setCurrentB2BUnit(orderRequestData.getSoldToAccount());
		this.createCart(orderRequestData);
	}

	/**
	 * sets the vtex cart.
	 *
	 * @param orderRequestData the order request data
	 */
	@Override
	public void setVtexCart(final JnjLatamOrderRequestData orderRequestData) throws BusinessException
	{
		this.setCurrentB2BUnit(orderRequestData.getSoldToAccount());
		final Optional<AbstractOrderModel> vtexCart = this.getVtexCart(orderRequestData.getCartReferenceNumber());
		if(vtexCart.isPresent())
		{
			getCartService().setSessionCart((CartModel) vtexCart.get());
			if(StringUtils.isNotBlank(orderRequestData.getPaymentToken()))
			{
				jnJLatamCartFacade.updateComplementaryInfo(orderRequestData.getPaymentToken());
			}
		}	
		else
		{
			throw new BusinessException("Cart not available for placing vtex order");
		}
	}

	private void createCart(final JnjLatamOrderRequestData orderRequestData) throws BusinessException
	{
		getSessionService().removeAttribute(SESSION_CART_PARAMETER_NAME);
		final CartModel cartModel = getCartService().getSessionCart();
		addCartModelAttributes(orderRequestData, cartModel);
		getCartService().setSessionCart(cartModel);
		createCartEntries(orderRequestData);
	}

	/**
	 * validates cart
	 * @return latamCartData
	 */
	@Override
	public JnjLaCartData validateCart() throws BusinessException, SystemException, IntegrationException, TimeoutException
	{
		 final JnjGTSapWsData wsData = new JnjGTSapWsData();
		 JnjValidateOrderData validateOrderData = null;
	     validateOrderData = jnJLatamCartFacade.validateOrder(wsData);
	     final CartModel cartModel = getCartService().getSessionCart();
		 final JnjLaCartData latamCartData = defaultCartConverter.convert(cartModel);
         if(Objects.nonNull(validateOrderData) && BooleanUtils.isTrue(validateOrderData.getSapErrorResponse()) &&
        		 CollectionUtils.isNotEmpty(validateOrderData.getSapErrorMessages()))
		 {
        	 latamCartData.setHasSapErrors(true);
        	 
        	 latamCartData.setSapErrorMessages(this.populateSapErrorMeesages(validateOrderData.getSapErrorMessages()));
		 }
         return latamCartData;
	}
	
	
	private SapErrorMessagesData populateSapErrorMeesages(final List<String> sapErrorMessages)
	{
		List<SapErrorMessageData> sapErrors = new ArrayList<>();
		for(String errorMsg : sapErrorMessages) {
			SapErrorMessageData errorMessageData = new SapErrorMessageData();
			errorMessageData.setMessage(errorMsg);
			sapErrors.add(errorMessageData);
		}
		SapErrorMessagesData errorMessagesData = new SapErrorMessagesData();
		errorMessagesData.setErrorMessages(sapErrors);
		return errorMessagesData;
	}

	/**
	 * Place order in hybris.
	 *
	 * @return OrderCode the string
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Override
	public String placeOrderInHybris() throws InvalidCartException
	{
		String orderCode= null;
		final boolean isCartCleanupRequired = Config.getParameter(CART_CLEANUP_REQUIRED).isEmpty() ? Boolean.TRUE
				: Boolean.valueOf(Config.getParameter(CART_CLEANUP_REQUIRED));
		try {
			orderCode = jnjLatamCheckoutFacade.placeOrderInHybris(isCartCleanupRequired);
		} catch (InvalidCartException e) {
			LOG.error("Exception occured", e);
		}
		return orderCode;
	}

	/**
	 * creates order in sap
	 * @param orderCode
	 * @return SalesOrderCreationResponse
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws IntegrationException
	 * @throws ParseException
	 */
	public SalesOrderCreationResponse createOrderInSAP(final String orderCode) throws BusinessException, SystemException, IntegrationException, ParseException
	{
		return getJnjLatamCheckoutFacade().createOrderInSAP(orderCode, null);
	}

	/**
	 * Gets order place
	 * @param orderCode
	 * @return orderData
	 */
	@Override
	public OrderData getOrderPlaced(final String orderCode)
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderCode);
		JnjGTOrderData orderData = getJnjOrderConverter().convert(order);
		orderData.setCartReferenceNumber(order.getExternalOrderRefNumber());
		return orderData;
	}

	/**
	 * Gets Order Histories Data
	 * @param ordersListData the order List Data
	 * @return ordersData
	 */
	@Override
	public OrderHistoriesData getOrderHistoriesData(final JnjLatamOrdersListData ordersListData)
	{
		OrderHistoriesData ordersData = new OrderHistoriesData();
		ordersData.setOrders(new ArrayList<>());
		Set<String> orderNumbersSet = this.getOrderNumbers(ordersListData);
		List<OrderModel> ordersList = jnjLatamVtexOrderService.getOrdersList(orderNumbersSet);
		if(CollectionUtils.isNotEmpty(ordersList))
		{
			ordersList.stream().forEach(orderModel -> ordersData.getOrders().add(this.getOrderHistoryData(orderModel)));
		}
		return ordersData;
	}

	/**
	 * Gets Order History Data
	 * @param order to be used
	 * @return orderData
	 */
	private OrderHistoryData getOrderHistoryData(final OrderModel order)
	{
		return getOrderHistoryConverter().convert(order);
	}

	/**
	 * Get Order Numbers
	 * @param ordersListData to be used
	 * @return orderNumbersSet
	 */
	private Set<String> getOrderNumbers(final JnjLatamOrdersListData ordersListData)
	{
		Set<String> orderNumbersSet = new HashSet<>();
		ordersListData.getOrdersList().stream().forEach(order->orderNumbersSet.add(order.getCode()));
		return orderNumbersSet;
	}

	/**
	 * Gets the vtex cart.
	 *
	 * @param cartReferenceNumber the cart reference number
	 * @return the vtex cart
	 */
	private Optional<AbstractOrderModel> getVtexCart(final String cartReferenceNumber)
	{
		return jnjLatamVtexOrderService.getCartForVtexReferenceNumber(cartReferenceNumber);
	}
	
	 /**
     * @param orderRequest the orderRequest to be used
     */
    private void addCartModelAttributes(final JnjLatamOrderRequestData  orderRequest,
            final CartModel cart)
	{
        cart.setOrderType(JnjOrderTypesEnum.valueOf(orderRequest.getOrderType()));
        cart.setExternalOrderRefNumber(orderRequest.getCartReferenceNumber());
        cart.setPurchaseOrderNumber(orderRequest.getPurchaseOrderNumber());
        cart.setShipToAccount(orderRequest.getShipToAccount());
        cart.setOrderChannel(this.getOrderChannel(orderRequest.getOrderChannel()));
        final AddressModel deliveryAddress = jnjAddressService.getAddressById(orderRequest.getShipToAccount());
        if(Objects.nonNull(deliveryAddress)) {
        	cart.setDeliveryAddress(deliveryAddress);
        }
        getModelService().save(cart);
        getModelService().refresh(cart);
    }
    
    
    private JnjOrderChannelModel getOrderChannel(String requestOrderChannel) 
    {
       JnjOrderChannelModel orderChannel = null;
       if(StringUtils.isBlank(requestOrderChannel)) {
    	   requestOrderChannel = getJnjConfigService().getConfigValueById(Jnjlab2bcoreConstants.Order.ORDER_CHANNEL);
       }
       try {
     		 orderChannel = getJnjLAOrderService().getOrderChannel(requestOrderChannel);
     	   }	   
       catch(Exception e) {
     	LOG.error("Exception occurred while getting order channel for request order channel :: "+requestOrderChannel, e);
       }
       return orderChannel;
    }
	/**
	 * Gets the b 2 b unit for account id.
	 *
	 * @param accountId the account id
	 * @return the b 2 b unit for account id
	 */
    @Override
	public JnJB2BUnitModel getB2bUnitForAccountId(final String accountId)
	{
	        try
			{
	            return getSessionService().executeInLocalView(new SessionExecutionBody()
				{
	                @Override
	                public JnJB2BUnitModel execute()
					{
	                    return getUserService().getUserGroupForUID(accountId, JnJB2BUnitModel.class);
	                }
	            }, getUserService().getAdminUser());
	        } catch (final UnknownIdentifierException | ClassMismatchException e)
			{
	            LOG.error("Failed to get unit: " + accountId, e);
	            return null;
	        }
	       
	  }

	
	 /**
     * @param orderRequest the orderRequest to be used
     * @throws BusinessException
     */
    private void createCartEntries(final JnjLatamOrderRequestData orderRequest)
            throws BusinessException
	{
    	final String methodName = "createCartEntries()";
    	final Map<String, String> productCodes = new LinkedHashMap<>();
		getUpdatedProductCodesMap(productCodes, orderRequest);
        JnjCartModificationData cartModificationData = null;
        try
		{
        cartModificationData = getJnJLatamCartFacade().addToCartLatam(productCodes,false, false,false,0);
        }catch(Exception e)
		{
        	JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName, e.getMessage(), e, DefaultJnjLatamVtexOrderFacade.class);
        }
        if (Objects.nonNull(cartModificationData) && CollectionUtils.isNotEmpty(cartModificationData.getCartModifications()))
		{
            for (final CartModificationData cartModifiedEntries : cartModificationData
                    .getCartModifications())
			{
                if (!(SUCCESS).equalsIgnoreCase(cartModifiedEntries.getStatusCode()))
				{
                    throw new BusinessException(PRODUCTS_NOT_ADDED);
                }
            }
        }
        addAdditionalAttributesinCartEntries();
    }
    
    
    /**
     * Updates the Quantity if present in the productCodes Map
     *
     * @param productCodes the productCodes to be used
     * @param orderRequest the orderRequest to be used
     */
    private static void getUpdatedProductCodesMap(final Map<String, String> productCodes,
												  final JnjLatamOrderRequestData orderRequest)
	{
		orderRequest.getEntries().stream().forEach(requestEntry ->{
			String indirectCustomerAccount = StringUtils.isNotBlank(requestEntry.getIndirectCustomerAccount()) ? requestEntry.getIndirectCustomerAccount() : StringUtils.EMPTY;
			productCodes.put(requestEntry.getMaterialNumber(),
				requestEntry.getQuantity().toString().concat(COLON).concat(indirectCustomerAccount));
		});
    }


    /**
     * Add the additional attributes
     */
    private void addAdditionalAttributesinCartEntries()
	{
        final CartModel cartModel = getCartService().getSessionCart();
        for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
		{
            populateEntryAttributes(cartEntry);
        }
        getModelService().saveAll(cartModel.getEntries());
        getModelService().refresh(cartModel);
    }
    
    /**
     * @param cartEntry the cartEntry to be used
     */
    private void populateEntryAttributes(final AbstractOrderEntryModel cartEntry)
	{
       cartEntry.setMaterialEntered(cartEntry.getProduct().getCode());
       cartEntry.setMaterialNumber(cartEntry.getProduct().getCode());
    }
    
    @Override
    public void updateCartEntrySalesOrg() {
      final CartModel cartModel = getCartService().getSessionCart();
      final Map<String,String> sectorSalesOrgMap = this.getSectorSalesOrgMapForCurrentUnit(cartModel);
      if(CollectionUtils.isNotEmpty(cartModel.getEntries()) && MapUtils.isNotEmpty(sectorSalesOrgMap)) {
    	  cartModel.getEntries().stream().forEach(cartEntry-> this.updateSalesOrg(cartEntry,sectorSalesOrgMap));
    	  getModelService().saveAll(cartModel.getEntries());
          getModelService().refresh(cartModel);
      }
    }
    
    private void updateSalesOrg(final AbstractOrderEntryModel cartEntry, final Map<String,String> sectorSalesOrgMap) {
    	JnJProductModel productModel = (JnJProductModel) cartEntry.getProduct();
    	final String productSector = StringUtils.isBlank(productModel.getSector()) ? StringUtils.EMPTY: productModel.getSector().toUpperCase();
		final String salesOrg = sectorSalesOrgMap.get(productSector);
		if(StringUtils.isNotBlank(salesOrg)) {
			cartEntry.setSalesOrg(salesOrg);
		}
    }
    
    private Map<String,String> getSectorSalesOrgMapForCurrentUnit(final CartModel cartModel)
	{
        Map<String,String> sectorSalesOrgMap = new HashMap<>();
    	final JnJB2BUnitModel unit = (JnJB2BUnitModel) cartModel.getUnit();
		final JnJSalesOrgCustomerModel jnjSalesOrgCustomerModel = new JnJSalesOrgCustomerModel();
		jnjSalesOrgCustomerModel.setCustomerId(unit);
		final List<JnJSalesOrgCustomerModel> salesOrgCustomerList = flexibleSearchService.getModelsByExample(jnjSalesOrgCustomerModel);
		if(CollectionUtils.isNotEmpty(salesOrgCustomerList)) {
			sectorSalesOrgMap = salesOrgCustomerList.stream().filter(salesOrgCustomer-> StringUtils.isNotBlank(salesOrgCustomer.getSector()))
			.collect(Collectors.toMap(salesOrgCustomer->salesOrgCustomer.getSector().toUpperCase(), JnJSalesOrgCustomerModel::getSalesOrg));
		}
		return sectorSalesOrgMap;
	}


	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected JnjLatamVtexOrderService getJnjLatamVtexOrderService()
	{
		return jnjLatamVtexOrderService;
	}

	public void setJnjLatamVtexOrderService(final JnjLatamVtexOrderService jnjLatamVtexOrderService)
	{
		this.jnjLatamVtexOrderService = jnjLatamVtexOrderService;
	}

	protected JnjLatamCartFacade getJnJLatamCartFacade()
	{
		return jnJLatamCartFacade;
	}

	public void setJnJLatamCartFacade(final JnjLatamCartFacade jnJLatamCartFacade)
	{
		this.jnJLatamCartFacade = jnJLatamCartFacade;
	}

	protected JnJAddressService getJnjAddressService()
	{
		return jnjAddressService;
	}

	public void setJnjAddressService(final JnJAddressService jnjAddressService)
	{
		this.jnjAddressService = jnjAddressService;
	}

	protected Converter<CartModel, JnjLaCartData> getDefaultCartConverter()
	{
		return defaultCartConverter;
	}

	public void setDefaultCartConverter(final Converter<CartModel, JnjLaCartData> defaultCartConverter)
	{
		this.defaultCartConverter = defaultCartConverter;
	}

	protected JnjLatamCheckoutFacade getJnjLatamCheckoutFacade()
	{
		return jnjLatamCheckoutFacade;
	}

	public void setJnjLatamCheckoutFacade(final JnjLatamCheckoutFacade jnjLatamCheckoutFacade)
	{
		this.jnjLatamCheckoutFacade = jnjLatamCheckoutFacade;
	}
	protected B2BOrderService getB2bOrderService()
	{
		return b2bOrderService;
	}
	public void setB2bOrderService(final B2BOrderService b2bOrderService)
	{
		this.b2bOrderService = b2bOrderService;
	}

	protected Converter<OrderModel, JnjGTOrderData> getJnjOrderConverter()
	{
		return jnjOrderConverter;
	}

	public void setJnjOrderConverter(final Converter<OrderModel, JnjGTOrderData> jnjOrderConverter)
	{
		this.jnjOrderConverter = jnjOrderConverter;
	}
	public Converter<OrderModel, OrderHistoryData> getOrderHistoryConverter()
	{
		return orderHistoryConverter;
	}
	public void setOrderHistoryConverter(final Converter<OrderModel, OrderHistoryData> orderHistoryConverter)
	{
		this.orderHistoryConverter = orderHistoryConverter;
	}

	protected UserService getUserService()
	{
		
		return userService;
	}

	public void setUserService(final UserService userService) 
	{
		this.userService = userService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService) 
	{
		this.modelService = modelService;
	}

	protected JnjLAOrderService getJnjLAOrderService() 
	{
		return jnjLAOrderService;
	}

	public void setJnjLAOrderService(final JnjLAOrderService jnjLAOrderService)
	{
		this.jnjLAOrderService = jnjLAOrderService;
	}

	protected JnjConfigService getJnjConfigService() 
	{
		return jnjConfigService;
	}

	public void setJnjConfigService(final JnjConfigService jnjConfigService) 
	{
		this.jnjConfigService = jnjConfigService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) 
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	
}
