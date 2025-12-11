/*
 * [y] hybris Platform
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.cart.impl;

import com.jnj.core.constants.CartViewMapping;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.services.operations.JnjGTOperationsService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.converters.populator.address.JnjGTAddressPopulator;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractFormData;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjLaCartData;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.data.JnjOutOrderLine;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.data.ReplacementProductData;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.model.JnjIndirectPayerModel;
import com.jnj.la.core.services.JnJLaCustomerDataService;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.cart.JnjLACartService;
import com.jnj.la.core.services.contract.JnjContractService;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.la.core.util.JnJLALanguageDateFormatUtil;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.facades.contract.JnjContractFacade;
import com.jnj.outboundservice.services.order.mapper.JnjLatamSalesOrderMapper;
import com.jnj.services.MessageService;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

import com.jnj.la.core.model.JnJLaB2BUnitModel;

import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.BooleanUtils;

import static com.jnj.core.constants.Jnjb2bCoreConstants.Cart.INVALID_PRODUCTCODE;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.MDD_SECTOR;

import org.apache.log4j.Logger;
import org.apache.commons.collections4.MapUtils;

public class JnjLatamCartFacadeImpl extends DefaultCartFacade implements JnjLatamCartFacade
{

	private static final Logger LOG = Logger.getLogger(JnjLatamCartFacadeImpl.class);
	private static final String ROWNO_CELLNO="Row no , cell no-";
	@Autowired
	protected CartService cartService;

	@Resource(name = "commerceCartService")
	protected JnjGTCartService jnjGTCartService;

	@Autowired
	protected JnJLaProductService jnjLaProductService;

	@Autowired
	private JnjContractFacade jnjContractFacade;

	@Autowired
	private JnjLatamSalesOrderMapper jnjLaSalesOrderMapper;

	@Autowired
	private JnjPriceDataFactory jnjPriceDataFactory;

	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;

	@Autowired
	protected MessageService messageService;

	@Autowired
	private JnjLACartService jnjLaCartService;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected Converter<CartModel, CartData> cartConverter;

	@Autowired
	protected Converter<CommerceCartModification, CartModificationData> cartModificationConverter;

	@Autowired
	protected Converter<CommerceCartRestoration, CartRestorationData> cartRestorationConverter;

	@Autowired
	protected JnjLatamOrderFacade jnjlatamCustomOrderFacade;

	@Autowired
	protected B2BOrderService b2bOrderService;

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Autowired
    private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;
	
	@Autowired
	protected ModelService modelService;

	@Autowired
	protected ThreadContextService threadContextService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected JnjGTOperationsService jnjNAOperationsService;

	@Autowired
	private JnJLaCustomerDataService jnjCustomerDataService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private JnjLAOrderService jnjLaOrderService;

	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	protected JnjGTB2BUnitService jnjGTB2BUnitService;
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private JnjContractService jnjContractService;

	@Resource(name = "b2bProductFacade")
	private ProductFacade productFacade;

	@Autowired
	protected JnjGTAddressPopulator jnjGTAddressPopulator;

	private static final String UPLOAD_TEMPLATE_START_FROM = "home.upload.template.startFrom";

	private static final String ADD_TO_CART = "Add to Cart";

	protected static final String PRODUCT = "product";

	private static final long DEFAULT_PRODUCT_QUANTITY = 0;
	protected static final String RESTRICTED_CATEGORY_PRODUCT_ERROR = "contract.restricted.product";

	private static final String CART_DATA = "cartData";

	private static final Class CURRENT_CLASS = JnjLatamCartFacadeImpl.class;

	private static final Integer SAME_CONTRACT = 0;

	private static final Integer DIFF_CONTRACT = 2;
	
    private static final String CART_SUBSTITUTE_ENABLED = "cart.substitutes.enabled";

	/**
	 * This method is used for updating the Indirect Customer.
	 *
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param cartEntryNumber
	 *           the cart entry number
	 * @return true, if successful
	 */
	@Override
	public boolean updateIndirectCustomer(final String indirectCustomer, final String indirectCustName, final int cartEntryNumber)
	{
		return jnjLaCartService.addIndirectCustomer(indirectCustomer, indirectCustName, cartEntryNumber);
	}


	@Override
	public JnjOrderData placeOrderInSap(final String orderCode) throws IntegrationException, ParseException
	{
		final String methodName = "placeOrderInSap()";

		JnjGTCoreUtil.logDebugMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName, Logging.BEGIN_OF_METHOD,
			CURRENT_CLASS);

		JnjOrderData orderData = null;
		final OrderModel orderModel = b2bOrderService.getOrderForCode(orderCode);
		JnjGTCoreUtil.logDebugMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
			"Preparing to send Hybris order to SAP. [ Hybris Order nr: [" + orderModel.getCode() + "] | Order status: ["
				+ orderModel.getStatus() + "]",
			CURRENT_CLASS);
		modelService.save(orderModel);
		try
		{
			final SalesOrderCreationResponse salesOrderCreationResponse = jnjlatamCustomOrderFacade
				.invokeSalesOrderCreationWrapper(orderModel);
			if (null != salesOrderCreationResponse)
			{
				orderData = new JnjOrderData();
				orderConverter.convert(orderModel, orderData);
			}
		}
		catch (final IntegrationException integrationException)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
				"Integration Exception occured: " + integrationException.getMessage(), integrationException, CURRENT_CLASS);
			throw integrationException;
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
				"Exception occured other then integration exception: " + exception.getMessage(), exception, CURRENT_CLASS);
			throw exception;
		}

		JnjGTCoreUtil.logDebugMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName, Logging.END_OF_METHOD,
			CURRENT_CLASS);
		return orderData;
	}


	@Override
	public JnjValidateOrderData validateOrder(final JnjGTSapWsData wsData)
		throws IntegrationException, SystemException, TimeoutException, BusinessException
	{

		final String methodName = "validateOrder()";

		JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
			JnjLatamCartFacadeImpl.class);

		// Cart Model object of the logged user is retrieved by using getSessionCart method of Cart Service.
		final CartModel cartModel = getCartService().getSessionCart();
		final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
		JnjLaCoreUtil.updateQuantityByFreeGoodLogic(cartModel, freeGoodsMap);
		
		final String contractNum = cartModel.getContractNumber();
		if(StringUtils.isNotBlank(contractNum))
		{
			saveCartTypeBasedOnContractNo(contractNum);
		}
				
		if (cartModel != null)
		{
			userService.getCurrentUser().getSessionCurrency().getSymbol();
			cartModel.setCurrency(userService.getCurrentUser().getSessionCurrency());
			modelService.save(cartModel);
		}
		JnjValidateOrderData validateOrderData = null;

		JnjGTOutboundStatusData jnjNAOutboundStatusData = null;

		// Cart Model object of the logged user is retrieved by using getSessionCart method of Cart Service.
		final String noSplit = Config.getParameter(Jnjb2bCoreConstants.Dropshipment.NO_SPLIT_LIST);
		final String noSplitList[] = noSplit.split(",");
		final String loggedInSite = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "loggedInSite :: " + loggedInSite, JnjLatamCartFacadeImpl.class);

		sessionService.setAttribute("loggedInSite", loggedInSite);

		boolean split = true;
		for (final String noSplitCountry : noSplitList)
		{
			if (noSplitCountry.equals(loggedInSite))
			{
				split = false;
			}
		}

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "split cart :: " + split, JnjLatamCartFacadeImpl.class);
		String vtexOrderChannel = cartModel.getOrderChannel() != null ? cartModel.getOrderChannel().getCode() :StringUtils.EMPTY;
		sessionService.setAttribute(Jnjlab2bcoreConstants.VTEX_ORDER_CHANNEL, vtexOrderChannel);

		if (!split)
		{
			sessionService.setAttribute("populateSpliCart", Boolean.FALSE);
			validateOrderData = jnjlatamCustomOrderFacade.invokeSalesOrderSimulateWrapper(cartModel);
		}
		else
		{
			final boolean isDummyOn = Config.getBoolean(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL, false);
			sessionService.setAttribute("populateSpliCart", Boolean.TRUE);

			jnjNAOutboundStatusData = jnjLaSalesOrderMapper.mapSalesOrderSimulationWrapper(cartModel, isDummyOn);

			if (!isDummyOn)
			{
				final List<JnjValidateOrderData> validateOrderDataList = validateSplitcart();
				final JnjValidateOrderData data = mergeValidatedOrderData(validateOrderDataList);

				if (data.getValidateOrderResponse() != null && !data.getValidateOrderResponse().booleanValue())
				{
					return data;
				}
			}
		}

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "jnjNAOutboundStatusData :: " + jnjNAOutboundStatusData,
			JnjLatamCartFacadeImpl.class);

		if (null != jnjNAOutboundStatusData)
		{
			JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "jnjNAOutboundStatusData :: " + jnjNAOutboundStatusData,
				JnjLatamCartFacadeImpl.class);

			validateOrderData = new JnjValidateOrderData();
			validateOrderData.setValidateOrderResponse(Boolean.valueOf(true));
			// To handle the hard stop error, it will enter inside if block.
			jnjNAOutboundStatusData.setHardStopErrorOcurred(true);
			extractedValidateOrderData(validateOrderData, jnjNAOutboundStatusData);
		}
		JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD,
			JnjLatamCartFacadeImpl.class);
		return validateOrderData;
	}

	private static void extractedValidateOrderData(JnjValidateOrderData validateOrderData, JnjGTOutboundStatusData jnjNAOutboundStatusData) {
		if (jnjNAOutboundStatusData.isHardStopErrorOcurred())
		{
			validateOrderData.setHardStopError(jnjNAOutboundStatusData.isHardStopErrorOcurred());
		}
		else if (null != jnjNAOutboundStatusData.getRemovedProductCodes()
			&& !jnjNAOutboundStatusData.getRemovedProductCodes().isEmpty())
		{
			validateOrderData.setRemovedProductCodes(jnjNAOutboundStatusData.getRemovedProductCodes());
		}
	}

	private List<JnjValidateOrderData> validateSplitcart() throws IntegrationException, TimeoutException
	{
		final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = sessionService.getAttribute("splitMap");
		sessionService.removeAttribute("splitMap");

		final CartModel cartModel = new CartModel();

		final CartModel cartModelTemp = getCart();
		cartModel.setUnit(cartModelTemp.getUnit());
		cartModel.setUser(cartModelTemp.getUser());
		cartModel.setEntries(cartModelTemp.getEntries());
		cartModel.setDeliveryAddress(cartModelTemp.getDeliveryAddress());
		cartModel.setCode(cartModelTemp.getCode());
		cartModel.setPurchaseOrderNumber(cartModelTemp.getPurchaseOrderNumber());
		cartModel.setCompleteDelivery(cartModelTemp.getCompleteDelivery());
		cartModel.setForbiddenSales(cartModelTemp.getForbiddenSales());
		cartModel.setCurrency(cartModelTemp.getCurrency());
		cartModel.setContractNumber(cartModelTemp.getContractNumber());

		int counter = -1;

		final List<JnjValidateOrderData> validateList = new ArrayList<>();
		if (splitOrderMap != null && !splitOrderMap.isEmpty() && !splitOrderMap.containsValue(null))
		{
			for (final Map.Entry<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> entry : splitOrderMap.entrySet())
			{
				counter += 1;
				sessionService.setAttribute("isProductFromSameCart", Integer.valueOf(counter));
				cartModel.setEntries(entry.getValue());
				cartModel.setOrderType(JnjOrderTypesEnum.valueOf(entry.getKey().getDocorderType()));
				LOG.info("Inside validateSplitcart() in JnjLatamCartFacadeImpl.java. Cart orderType is: " + cartModel.getOrderType());
				cartModel.setForbiddenSales(entry.getKey().getForbiddenFlag());
				validateList.add(jnjlatamCustomOrderFacade.invokeSalesOrderSimulateWrapper(cartModel));
			}
		}
		return validateList;
	}

	protected CartModel getCart()
	{
		return getCartService().getSessionCart();
	}


	/**
	 * Creates the price.
	 *
	 * @param currency
	 *           the currency
	 * @param val
	 *           the val
	 * @return the price data
	 */
	protected PriceData createPrice(final CurrencyModel currency, final Double val)
	{
		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;
		return jnjPriceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}


	/**
	 * Method to get the Cart Model. Added as part of Panama Enhancement
	 */
	@Override
	public CartModel getCartModel()
	{
		final CartModel cartModel = getCartService().getSessionCart();
		return cartModel;
	}

	/**
	 * Method to save the Cart Model. Added as part of Panama Enhancement
	 */
	@Override
	public void saveCart(final CartModel cartModel)
	{
		jnjLaCartService.calculateValidatedCart(cartModel);
	}

	@Override
	public Long updateQuantity(final OrderEntryData entry, final Long entryQuantity)
	{
		return entryQuantity;
	}

	@Override
	public CartData getSessionCart()
	{
		final CartData cartData;
		if (hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			cartData = getCartConverter().convert(cart);
		}
		else
		{
			cartData = createEmptyCart();
		}
		return cartData;
	}


	@Override
	public boolean hasSessionCart()
	{
		return getCartService().hasSessionCart();
	}

	@Override
	public CartModificationData addToCart(final String catalogId, final long quantity) throws CommerceCartModificationException
	{
		final String methodName = "addToCart()";
		JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "Start: Loading Product from Database", CURRENT_CLASS);

		ProductModel product = null;
		try
		{
			product = jnjLaProductService.getProductForCatalogId(catalogId);
			JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "End: Loading Product from Database", CURRENT_CLASS);
		}
		catch (final BusinessException e)
		{
			JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "Product with catalogId: " + catalogId, CURRENT_CLASS);
		}

		return saveProductToCartData(product, catalogId, quantity);
	}

	@Override
	public CartRestorationData restoreSavedCart(final String code) throws CommerceCartRestorationException
	{
		if (!hasEntries())
		{
			getCartService().setSessionCart(null);
		}

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		final CartModel cartForGuidAndSiteAndUser = getCommerceCartService().getCartForGuidAndSiteAndUser(code,
			getBaseSiteService().getCurrentBaseSite(), getUserService().getCurrentUser());
		parameter.setCart(cartForGuidAndSiteAndUser);

		return getCartRestorationConverter().convert(getCommerceCartService().restoreCart(parameter));
	}


	@Override
	public JnjCartModificationData addToCartQuick(final String productCode, final String productQuantity,
												  final List<CartModificationData> cartModificationList) throws CommerceCartModificationException
	{
		final JnjCartModificationData jnjCartModificationData = new JnjCartModificationData();
		final long productQunatityToBeAdded = Long.parseLong(productQuantity);
		CartModificationData cartModificationData;
		cartModificationData = addToCart(productCode, productQunatityToBeAdded);
		cartModificationList.add(cartModificationData);
		jnjCartModificationData.setCartModifications(cartModificationList);
		return jnjCartModificationData;
	}


	@Override
	public JnjCartModificationData addToCartFromContract(final String catalogId, final String productQuantity)
		throws CommerceCartModificationException
	{
		final String methodName = "addToCartFromContract()";

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
			JnjLatamCartFacadeImpl.class);

		final JnjCartModificationData jnjCartModificationData = new JnjCartModificationData();
		final long productQunatityToBeAdded = Long.parseLong(productQuantity);
		final List<CartModificationData> cartModificationList = new ArrayList<>();
		CartModificationData cartModificationData;
		cartModificationData = addToCartFromContractWithoutSearchRestriction(catalogId, productQunatityToBeAdded);
		cartModificationList.add(cartModificationData);
		jnjCartModificationData.setCartModifications(cartModificationList);

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD,
			JnjLatamCartFacadeImpl.class);

		return jnjCartModificationData;
	}

	private CartModificationData addToCartFromContractWithoutSearchRestriction(final String catalogId, final long quantity)
		throws CommerceCartModificationException
	{

		final String methodName = "addToCartFromContractWithoutSearchRestriction()";

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "Start: Loading Contract Product from Database", CURRENT_CLASS);

		final ProductModel product = jnjLaProductService.getProductByCatalogIdWithoutSearchRestriction(catalogId);

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "End: Loading Contract Product from Database", CURRENT_CLASS);

		return saveProductToCartData(product, catalogId, quantity);
	}

	private CartModificationData saveProductToCartData(final ProductModel product, final String catalogId, final long quantity)
	{
		final String methodName = "saveProductToCartData()";
		CartModificationData cartModificationData = null;
		try
		{
			if (null != product)
			{
				final CommerceCartModification cartModification;
				final CartModel cartModel = getCartService().getSessionCart();

				JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "Start: Adding product to cart via cart service",
					CURRENT_CLASS);
				cartModification = jnjLaCartService.addToCart(cartModel, product, quantity, product.getUnit(), false, true, catalogId,
					null);
				JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "End>>: Adding product to cart via cart service",
					CURRENT_CLASS);
				modelService.save(cartModel);

				JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "Start: CartModifiaction Conversion", CURRENT_CLASS);
				cartModificationData = getCartModificationConverter().convert(cartModification);
				if (Jnjlab2bcoreConstants.ADDTO_CART_CATEGORY_ERROR.equals(cartModification.getStatusCode()))
				{
					final String categoryError = messageService.getMessageForCode(RESTRICTED_CATEGORY_PRODUCT_ERROR,
						commonI18NService.getLocaleForLanguage(commonI18NService.getCurrentLanguage()));
					final String productCode = cartModificationData.getEntry().getProduct().getCode();

					cartModificationData.setError(true);
					cartModificationData.setStatusCode(categoryError + " " + productCode);
				}
				JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "End>>: CartModifiaction Conversion", CURRENT_CLASS);
				JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName,
					"cartModel.getIsContractCart() :: " + cartModel.getIsContractCart(), CURRENT_CLASS);
			}
			else
			{
				cartModificationData = new CartModificationData();
				updateCartModificationData(cartModificationData, catalogId,
					messageService.getMessageForCode(INVALID_PRODUCTCODE, null, catalogId));

				JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName,
					"ModelLoadingException: Add to Cart - Product Code: " + catalogId + " not Found for current catalog ",
					CURRENT_CLASS);
			}
		}
		catch (final ModelLoadingException itemNotFoundExp)
		{
			JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName,
				"ModelLoadingException: Add to Cart - Product Code: " + catalogId + "not Valid for add to session cart ",
				itemNotFoundExp, CURRENT_CLASS);
		}
		catch (final BusinessException | CommerceCartModificationException e)
		{
			JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName,
				"BusinessException Add to Cart - Product catalog id: " + catalogId + "not Valid for add to session cart", e,
				CURRENT_CLASS);

			cartModificationData = new CartModificationData();
			updateCartModificationData(cartModificationData, catalogId,
				messageService.getMessageForCode(INVALID_PRODUCTCODE, null, catalogId));

		}

		JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return cartModificationData;
	}

	protected void updateCartModificationData(final CartModificationData cartModificationData, final String code,
											  final String addToCartStatus)
	{
		final String methodName = "updateCartModificationData()";

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
			JnjLatamCartFacadeImpl.class);

		final OrderEntryData entryData = new OrderEntryData();
		final ProductData productData = new JnjGTProductData();
		productData.setCode(code);
		entryData.setProduct(productData);
		cartModificationData.setEntry(entryData);
		cartModificationData.setError(true);
		cartModificationData.setStatusCode(addToCartStatus);

		JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
			JnjLatamCartFacadeImpl.class);

	}


	@Override
	public List<String> getIndirectCustomer(final String country)
	{
		return jnjCustomerDataService.getIndirectCustomer(country);
	}

	@Override
	public List<String> getIndirectPayer(final String country)
	{
		return jnjCustomerDataService.getIndirectPayer(country);
	}

	@Override
	public List<JnjIndirectCustomerModel> getIndirectCustomers(final String country)
	{
		return jnjCustomerDataService.getJnjInidrectCustomer(country);
	}

	@Override
	public List<JnjIndirectPayerModel> getIndirectPayers(final String country)
	{
		return jnjCustomerDataService.getJnjIndirectPayers(country);
	}

	@Override
	public boolean updateIndirectPayer(final String indirectPayer, final String indirectPayerName, final int cartEntryNumber)
	{
		return jnjLaCartService.addIndirectPayer(indirectPayer, indirectPayerName, cartEntryNumber);
	}


	@Override
	public ProductData createCartWithOrder(final String orderId)
	{
		final JnjGTProductData productData = new JnjGTProductData();
		ProductData lastAddedProductData = null;
		createCartFromOrder(orderId, productData);
		if (StringUtils.isNotEmpty(productData.getLastAddedProduct()))
		{
			lastAddedProductData = productFacade.getProductForCodeAndOptions(productData.getLastAddedProduct(),
				Arrays.asList(ProductOption.BASIC));
		}
		if (lastAddedProductData != null && lastAddedProductData instanceof JnjGTProductData)
		{
			((JnjGTProductData) lastAddedProductData).setInvalidProductCodes(productData.getInvalidProductCodes());
			((JnjGTProductData) lastAddedProductData).setLastAddedProductQuantity(productData.getLastAddedProductQuantity());
			return lastAddedProductData;
		}
		return productData;
	}


	@Override
	public void createCartFromOrder(final String orderId, final JnjGTProductData productData)
	{
		jnjLaCartService.createCartFromOrder(orderId, productData);
		jnjLaCartService.calculateSessionCart();
	}

	@Override
	public JnjCartModificationData addToCartLatam(final Map<String, String> catalogIds, final boolean ignoreUPC,
												  final boolean isHomePageFileUpload, boolean forceNewEntry, int currentEntryNumber) throws CommerceCartModificationException
	{
		final String methodName = "addToCartLatam()";

		final JnjCartModificationData jnjCartModificationData = new JnjCartModificationData();
		final Map<String, String> revisedErrorMap = new LinkedHashMap<>();
		final List<CartModificationData> cartModificationList = new ArrayList<>(1);
		CartModificationData cartModificationData = null;

		final Map<ProductModel, Long> productQtyMap = new LinkedHashMap<>(catalogIds.size());
		boolean isProductsValid = true;

		final Map<String, Boolean> productCheckMap = new LinkedHashMap<>();
		final Map<String, String> indirectCustomerMap = new LinkedHashMap<>();
		final Map<String, String> indirectPayerMap = new LinkedHashMap<>();
		final ArrayList<String> catalogIdArray = new ArrayList<>(catalogIds.keySet());

		productCheckMap.putAll(jnjLaProductService.checkProductCodes(catalogIdArray));

		for (final Map.Entry<String, String> entry : catalogIds.entrySet())
		{
			final String catalogId = entry.getKey();
			final JnJProductModel product;
			try
			{
				product = jnjLaProductService.getProductForCatalogId(catalogId);
				JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName,
					"isProductsValid addToCartLatam...............:" + isProductsValid, JnjLatamCartFacadeImpl.class);

				if (null != product)
				{
					if (!productCheckMap.containsKey(catalogId.toUpperCase()))
					{
						final String validationError = messageService.getMessageForCode(INVALID_PRODUCTCODE, null, catalogId);
						catalogIds.put(catalogId, validationError);
						isProductsValid = false;
						if (isHomePageFileUpload)
						{
							revisedErrorMap.put(catalogId, validationError);
						}
						JnjGTCoreUtil.logErrorMessage(ADD_TO_CART,
							methodName, "Add to Cart - Product Code-" + catalogId
								+ " Found. But, product validation was failed. Error message: " + validationError,
							null, JnjLatamCartFacadeImpl.class);
					}
					else
					{
						final String composedProductQty = catalogIds.get(catalogId);
						String finalQty = "0";
						if (composedProductQty.contains(":"))
						{
							final String[] indirectQty = composedProductQty.split(":");
							if (indirectQty.length > 0)
							{
								finalQty = indirectQty[0];
							}
							if (indirectQty.length > 1 && product.getSector().equals(MDD_SECTOR))
							{
								indirectCustomerMap.put(catalogId, indirectQty[1]);
								if (indirectQty.length > 2)
								{
									indirectPayerMap.put(catalogId, indirectQty[2]);
								}
							}
						}
						else
						{
							finalQty = composedProductQty;
						}
						productQtyMap.put(product, Long.valueOf(finalQty));

					}
				}
				else
				{
					cartModificationData = new CartModificationData();
					final String errorMsg = messageService.getMessageForCode(INVALID_PRODUCTCODE, null, catalogId);
					isProductsValid = false;
					updateCartModificationData(cartModificationData, catalogId, errorMsg);
					catalogIds.put(catalogId, errorMsg);
					if (isHomePageFileUpload)
					{
						revisedErrorMap.put(catalogId, errorMsg);
					}
					JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName,
						"Add to Cart - Product Code-" + catalogId + "not Found for current catalog", null,
						JnjLatamCartFacadeImpl.class);
				}
			}
			catch (final BusinessException e)
			{
				JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, "addToCartLatam", INVALID_PRODUCTCODE + catalogId, e, CURRENT_CLASS);
				cartModificationData = new CartModificationData();
				final String errorMsg = messageService.getMessageForCode(INVALID_PRODUCTCODE, null, catalogId);
				isProductsValid = false;
				updateCartModificationData(cartModificationData, catalogId, errorMsg);
				catalogIds.put(catalogId, errorMsg);
				if (isHomePageFileUpload)
				{
					revisedErrorMap.put(catalogId, errorMsg);
				}
				JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName,
					"Add to Cart - Product catalog id: " + catalogId + "not Valid for add to session cart", null,
					JnjLatamCartFacadeImpl.class);
			}
		}

		if (isHomePageFileUpload && revisedErrorMap.size() > 0)
		{
			if (productQtyMap.size() > 0)
			{
				revisedErrorMap.put("someProductsAdded", "someProductsAdded");
			}
			catalogIds.clear();
			catalogIds.putAll(revisedErrorMap);
		}

		JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, "isProductsValid atlast " + isProductsValid,
			JnjLatamCartFacadeImpl.class);
		if (!isProductsValid) {
			JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, "products invalid" + isProductsValid, JnjLatamCartFacadeImpl.class);
			sessionService.setAttribute("isProductsValid", Boolean.FALSE);
		} else {
			JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, "products valid" + isProductsValid, JnjLatamCartFacadeImpl.class);
			sessionService.setAttribute("isProductsValid", Boolean.FALSE);
		}

		if (!productQtyMap.isEmpty()) {
			final CartModel cartModel = getCartService().getSessionCart();
			final Set<Entry<ProductModel, Long>> entrySet = productQtyMap.entrySet();
			long enteredQty;
			boolean removeContract = false;
			JnJLaProductModel variantProduct;
			for (final Entry entry : entrySet) {
				variantProduct = (JnJLaProductModel) entry.getKey();
				enteredQty = ((Long) entry.getValue()).longValue();
				cartModificationData = addToCart(variantProduct.getCatalogId(), enteredQty);

				final String cartContractNumber = cartModel.getContractNumber();

				removeContract = !isContractProduct(variantProduct.getCatalogId(), cartContractNumber);

				if (cartModificationData.isError()) {
					catalogIds.put(variantProduct.getCatalogId(), cartModificationData.getStatusCode());
				} else {
					catalogIds.put(variantProduct.getCatalogId(), Jnjb2bCoreConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS);
				}

				cartModificationList.add(cartModificationData);
			}

			if (removeContract) {
				saveCartRemoveContractNo();
			}
			final String externalOrderRefNum = cartModel.getExternalOrderRefNumber();
			if(StringUtils.isNotBlank(externalOrderRefNum))
			{
				indirectCustomerMap.entrySet().stream().forEach(indirectCustomer -> jnjLaCartService.addIndirectCustomer(indirectCustomer.getValue(), StringUtils.EMPTY, indirectCustomer.getKey()));
			}
			else
			{
				for (final Map.Entry<String, String> indirectCustomer : indirectCustomerMap.entrySet())
				{
					updateIndirectCustomer(indirectCustomer.getValue(), indirectCustomer.getKey());
				}
			}

			for (final Map.Entry<String, String> indirectPayer : indirectPayerMap.entrySet()) {
				updateIndirectPayer(indirectPayer.getValue(), indirectPayer.getKey());
			}

			jnjCartModificationData.setTotalUnitCount(calcTotalUnitCount(cartModel));
		}

		if (cartModificationData != null && (StringUtils.equals(cartModificationData.getStatusCode(),
			Jnjb2bCoreConstants.Cart.BASKET_PAGE_MESSAGE_MIN_QTY_ADDED)
			|| StringUtils.equals(cartModificationData.getStatusCode(),
			Jnjb2bCoreConstants.Cart.BASKET_PAGE_MESSAGE_QTY_ADJUSTED))) {
			jnjCartModificationData.setShowQtyAdjustment(true);
		}
		jnjCartModificationData.setCartModifications(cartModificationList);
		return jnjCartModificationData;
	}


	protected int calcTotalUnitCount(final AbstractOrderModel source)
	{
		int totalUnitCount = 0;
		for (final AbstractOrderEntryModel orderEntryModel : source.getEntries())
		{
			totalUnitCount += orderEntryModel.getQuantity().intValue();
		}
		return totalUnitCount;
	}

	@Override
	public void saveCartWithContractNo(final String contractNo)
	{
		final CartModel cartModel = getCartService().getSessionCart();

		if (contractNo != null)
		{
			cartModel.setContractNumber(contractNo);
			cartModel.setIsContractCart(Boolean.TRUE);
		}
		sessionService.removeAttribute("contractEntryList");
		saveCart(cartModel);
	}

	/**
	 * @param contractNo
	 */
	@Override
	public void saveCartTypeBasedOnContractNo(final String contractNo) {
		final CartModel cartModel = getCartService().getSessionCart();
		if (StringUtils.isNotBlank(contractNo))
		{
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel)cartModel.getUser();			
	        final Set<PrincipalGroupModel> groups = currentUser.getAllGroups();
			for (PrincipalGroupModel group : groups) {
				String uid = group.getUid();
				if (BooleanUtils.isTrue(jnjLatamCommonFacadeUtil.isCommercialUserEnabledForCurrentSite())
						&& (Jnjlab2bcoreConstants.GROUP_PHARMA_COMMERCIAL_USER.equalsIgnoreCase(uid)
						|| Jnjlab2bcoreConstants.GROUP_MDD_COMMERCIAL_USER.equalsIgnoreCase(uid))) {
					final JnjContractModel jnjContractModel = getJnjContractService().getContractDetailsById(contractNo);
					if (!ObjectUtils.isEmpty(jnjContractModel)) {
						LOG.debug("Inside saveCartTypeBasedOnContractNo(). Calculating order type for commercial user");
						JnjOrderTypesEnum orderType = jnjLaCartService.calculateOrderTypeForCommercialUser(jnjContractModel, cartModel);
						LOG.info("Order type for Commercial user##########:" + orderType.getCode());
						cartModel.setOrderType(orderType);
						List<AbstractOrderEntryModel> cartEntries = new ArrayList<>();
						updateCartEntries(cartModel,orderType,cartEntries);
						cartModel.setEntries(cartEntries);
					}
				}
			}
		}

		saveCart(cartModel);
	}

	private void updateCartEntries(CartModel cartModel, JnjOrderTypesEnum orderType,List<AbstractOrderEntryModel> cartEntries)
	{
		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			for (final AbstractOrderEntryModel abstractEntryModel : cartModel.getEntries())
			{
				abstractEntryModel.setSapOrderType(orderType.getCode());
				cartEntries.add(abstractEntryModel);
			}
			 
		}
	}
	
	
	@Override
	public void saveCartRemoveContractNo()
	{
		final CartModel cartModel = getCartService().getSessionCart();

		cartModel.setContractNumber(StringUtils.EMPTY);
		cartModel.setIsContractCart(Boolean.FALSE);

		saveCart(cartModel);
	}

	public boolean isContractProduct(final String catalogId, final String currentContractNumber)
	{
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
		final JnJB2BUnitModel currentB2BUnit = currentUser.getCurrentB2BUnit();
		final Set<JnjContractModel> contractList = currentB2BUnit.getJnjContracts();
		boolean contractProduct = false;

		if (CollectionUtils.isNotEmpty(contractList))
		{
			for (final JnjContractModel contract : contractList)
			{
				final String contractNumber = contract.getECCContractNum();
				if (currentContractNumber != null && currentContractNumber.equalsIgnoreCase(contractNumber))
				{
					final List<JnjContractEntryModel> entries = contract.getJnjContractEntries();
					if (CollectionUtils.isNotEmpty(entries))
					{
						for (final JnjContractEntryModel entry : entries)
						{
							if (contractEntryBelongsToCatalog(entry, catalogId)) {
								contractProduct = true;
								break;
							}
						}
					}
					break;
				}
			}
		}
		return contractProduct;
	}

	private boolean contractEntryBelongsToCatalog(final JnjContractEntryModel entry, final String catalogId) {
		return entry != null && entry.getProduct() != null && entry.getProduct().getCatalogId() != null && entry.getProduct().getCatalogId().equalsIgnoreCase(catalogId);
	}

	public boolean isContractProduct(final String catalogId)
	{
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
		final JnJB2BUnitModel currentB2BUnit = currentUser.getCurrentB2BUnit();
		final Set<JnjContractModel> contractList = currentB2BUnit.getJnjContracts();
		boolean contractProduct = false;
		final CartModel cartModel = getCartService().getSessionCart();
		final String currentContractNumber = cartModel.getContractNumber();

		if (CollectionUtils.isNotEmpty(contractList))
		{
			for (final JnjContractModel contract : contractList)
			{
				final String contractNumber = contract.getECCContractNum();
				if (currentContractNumber != null && currentContractNumber.equalsIgnoreCase(contractNumber))
				{
					final List<JnjContractEntryModel> entries = contract.getJnjContractEntries();
					if (CollectionUtils.isNotEmpty(entries))
					{
						for (final JnjContractEntryModel entry : entries)
						{
							if (entry.getProduct().getCatalogId().equalsIgnoreCase(catalogId))
							{
								contractProduct = true;
								break;
							}
						}
					}
					break;
				}
			}
		}
		return contractProduct;
	}

	@Override
	public Map<String, String> fileConverter(final MultipartFile homePageFile, final boolean indirectCustomer,
											 final boolean indirectPayer)
	{
		final String methodName = "fileConverter()";

		final Map<String, String> productQuantityMap = new LinkedHashMap<>();
		if (null != homePageFile)
		{

			try
			{
				final FileInputStream fileInputStream = (FileInputStream) homePageFile.getInputStream();

				if (StringUtils.endsWithIgnoreCase(FilenameUtils.getExtension(homePageFile.getOriginalFilename()),
					Jnjlab2bcoreConstants.FILE_EXTENSION_XLSX))
				{
					final XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
					final XSSFSheet sheet = workbook.getSheetAt(0);
					final String sheetType = Jnjlab2bcoreConstants.FILE_EXTENSION_XLSX;
					processSheet(productQuantityMap, sheet, indirectCustomer, indirectPayer, sheetType);
				}
				else
				{
					final HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
					final HSSFSheet sheet = workbook.getSheetAt(0);
					final String sheetType = Jnjlab2bcoreConstants.FILE_EXTENSION_XLS;
					processSheet(productQuantityMap, sheet, indirectCustomer, indirectPayer, sheetType);
				}

				fileInputStream.close();
			}
			catch (final FileNotFoundException exception)
			{
				JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName, "File not found", exception, JnjLatamCartFacadeImpl.class);
				return new HashMap<>();
			}
			catch (final IOException exception)
			{
				JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName, "I/O Exception", exception, JnjLatamCartFacadeImpl.class);
				return new HashMap<>();
			}
			catch (final ClassCastException exception)
			{
				JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName, "Class Cast Exception", exception,
					JnjLatamCartFacadeImpl.class);
				return new HashMap<>();
			}

		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName, "File is null: " + homePageFile, null,
				JnjLatamCartFacadeImpl.class);
			return new HashMap<>();
		}
		return productQuantityMap;
	}

	private void processSheet(final Map<String, String> productQuantityMap, final Object sheet, final boolean indirectCustomer,
							  final boolean indirectPayer, final String sheetType)
	{
		final String methodName = "processSheet()";

		Iterator<Row> rowIterator;
		//Iterate through each rows from first sheet
		if (sheetType.equals(Jnjlab2bcoreConstants.FILE_EXTENSION_XLS))
		{
			rowIterator = ((HSSFSheet) sheet).iterator();
		}
		else
		{
			rowIterator = ((XSSFSheet) sheet).iterator();
		}
		final int startFrom = Config.getInt(UPLOAD_TEMPLATE_START_FROM, 2);
		int rowCounter = 0;
		int blankCellsCounter = 0; //stops looping if more than 10 empty cells are found

		Iterator<Cell> cellIterator;
		int cellCounter;
		String key;
		String indirectCustomerCode;
		String indirectPayerCode;
		int value;

		while (rowIterator.hasNext() && blankCellsCounter < 10)
		{

			final Row row = rowIterator.next();

			if (rowCounter >= startFrom)
			{
				//For each row, iterate through each columns
				cellIterator = row.cellIterator();
				cellCounter = 0;
				key = "";
				indirectCustomerCode = "";
				indirectPayerCode = "";
				value = 0;

				if (isCellEmpty(row.getCell(0)))
				{
					blankCellsCounter++;
				}

				while (cellIterator.hasNext())
				{
					final Cell cell = cellIterator.next();

					switch (cell.getCellType())
					{
						case BOOLEAN:
							JnjGTCoreUtil.logWarnMessage(ADD_TO_CART, methodName,
								"Product code and quantity both should not be in cell with format  : " + cell.getBooleanCellValue(),
								JnjLatamCartFacadeImpl.class);
							break;
						case NUMERIC:
							if (cellCounter == 1)
							{
								value = (int) cell.getNumericCellValue();
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName,
										ROWNO_CELLNO + rowCounter + "," + cellCounter + " Quantity is :" + value,
									JnjLatamCartFacadeImpl.class);
							}
							else if (cellCounter == 0)
							{
								key = Long.toString((long) cell.getNumericCellValue());
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName,
										ROWNO_CELLNO + rowCounter + "," + cellCounter + " product Code : " + key,
									JnjLatamCartFacadeImpl.class);
							}
							else if (cellCounter == 2 && indirectCustomer)
							{
								indirectCustomerCode = Long.toString((long) cell.getNumericCellValue());
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, ROWNO_CELLNO + rowCounter + ","
									+ cellCounter + " indirect customer : " + indirectCustomerCode, JnjLatamCartFacadeImpl.class);
							}
							else if (cellCounter == 3 && indirectPayer)
							{
								indirectPayerCode = Long.toString((long) cell.getNumericCellValue());
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName,
										ROWNO_CELLNO + rowCounter + "," + cellCounter + " indirect payer : " + indirectPayerCode,
									JnjLatamCartFacadeImpl.class);
							}
							else
							{
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName,
										ROWNO_CELLNO + rowCounter + "," + cellCounter
										+ " Product Code should be in cell with format " + CellType.NUMERIC,
									JnjLatamCartFacadeImpl.class);
							}

							break;
						case STRING:
							if (cellCounter == 0)
							{
								key = cell.getStringCellValue();
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName,
										ROWNO_CELLNO + rowCounter + "," + cellCounter + " product Code : " + key,
									JnjLatamCartFacadeImpl.class);
							}
							else if (cellCounter == 2 && indirectCustomer)
							{
								indirectCustomerCode = cell.getStringCellValue();
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, ROWNO_CELLNO + rowCounter + ","
									+ cellCounter + " indirect customer : " + indirectCustomerCode, JnjLatamCartFacadeImpl.class);
							}
							else if (cellCounter == 3 && indirectPayer)
							{
								indirectPayerCode = cell.getStringCellValue();
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName,
										ROWNO_CELLNO + rowCounter + "," + cellCounter + " indirect payer : " + indirectPayerCode,
									JnjLatamCartFacadeImpl.class);
							}
							else
							{
								JnjGTCoreUtil.logDebugMessage(ADD_TO_CART,
									methodName, ROWNO_CELLNO + rowCounter + "," + cellCounter
										+ " Quantity should be in cell with format " + CellType.STRING,
									JnjLatamCartFacadeImpl.class);
							}
							break;
						default:
							JnjGTCoreUtil.logInfoMessage(ADD_TO_CART, methodName, "Invalid cell type : ", JnjLatamCartFacadeImpl.class);
							break;
					}
					cellCounter++;
				}
				if (StringUtils.isNotEmpty(key))
				{
					if (value != 0)
					{
						if (productQuantityMap.containsKey(key))
						{
							final int receivedQty = Integer.parseInt(productQuantityMap.get(key).split(":")[0]);
							final int newQty = receivedQty + value;

							String stringQty = String.valueOf(newQty);
							stringQty = stringQty.concat(":");

							if (indirectCustomer && !("").equals(indirectCustomerCode))
							{
								stringQty = stringQty.concat(indirectCustomerCode);
								stringQty = stringQty.concat(":");
								if (indirectPayer && !("").equals(indirectPayerCode))
								{
									stringQty = stringQty.concat(indirectPayerCode);
								}
							}
							productQuantityMap.put(key, stringQty);
						}
						else if (StringUtils.isNotBlank(key))
						{
							String stringQty = String.valueOf(value);
							stringQty = stringQty.concat(":");
							if (indirectCustomer && !("").equals(indirectCustomerCode))
							{
								stringQty = stringQty.concat(indirectCustomerCode);
								stringQty = stringQty.concat(":");
								if (indirectPayer && !("").equals(indirectPayerCode))
								{
									stringQty = stringQty.concat(indirectPayerCode);
								}
							}
							productQuantityMap.put(key, stringQty);
						}
					}
					else
					{
						JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName,
							"Quantity is not available hence adding the product with default quantity.",
							JnjLatamCartFacadeImpl.class);
						productQuantityMap.put(key, "0");
					}
				}
			}
			rowCounter++;
		}
	}

	@Override
	public boolean updateIndirectPayer(final String indirectPayer, final String productCode)
	{
		final CountryModel countryModel = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
		String indirectPayerName;
		final String receivedIndirectPayer = indirectPayer == null ? "" : indirectPayer;

		JnjIndirectPayerModel jnjIndirectPayerModel = null;
		if (null != countryModel)
		{
			jnjIndirectPayerModel = jnjCustomerDataService.getIndirectPayerModel(countryModel.getIsocode().toString(),
				indirectPayer);
		}

		if (jnjIndirectPayerModel != null)
		{
			indirectPayerName = jnjIndirectPayerModel.getIndirectPayerName();
			return jnjLaCartService.addIndirectPayer(receivedIndirectPayer, indirectPayerName, productCode);
		}

		return false;
	}

	@Override
	public boolean updateIndirectCustomer(final String indirectCustomer, final String productCode)
	{
		final CountryModel countryModel = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
		String indirectCustomerName;
		final String receivedIndirectCustomer = indirectCustomer == null ? "" : indirectCustomer;

		JnjIndirectCustomerModel jnjIndirectCustomerModel = null;
		if (null != countryModel)
		{
			jnjIndirectCustomerModel = jnjCustomerDataService.getIndirectCustomerModel(countryModel.getIsocode().toString(),
				indirectCustomer);
		}

		if (jnjIndirectCustomerModel != null)
		{
			indirectCustomerName = jnjIndirectCustomerModel.getIndirectCustomerName();
			return jnjLaCartService.addIndirectCustomer(receivedIndirectCustomer, indirectCustomerName, productCode);
		}
		return false;
	}

	public static boolean isCellEmpty(final Cell cell)
	{
		boolean cellFlag = false;
		if ((cell == null || cell.getCellType() == CellType.BLANK) || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().isEmpty()))
		{
			cellFlag = true;
		}
		return cellFlag;
	}



	public boolean updateNamedExpectedDeliveryDate(final String expShippingDate, final Integer entryNumber,
												   final String sessionLanguage)
	{

		String dateLanguage;

		if (sessionLanguage != null)
		{
			dateLanguage = sessionLanguage;
		}
		else
		{
			dateLanguage = getCurrentUserLanguage();
		}

		final Date expShippingDateInDateFormat = JnJLALanguageDateFormatUtil.getLanguageSpecificDate(dateLanguage, expShippingDate);

		final CartModel cartModel = getCartService().getSessionCart();
		final List<AbstractOrderEntryModel> abstractOrderEntryModelList = cartModel.getEntries();
		for (final AbstractOrderEntryModel abstractOrderEntryModel : abstractOrderEntryModelList)
		{
			if (abstractOrderEntryModel.getEntryNumber().equals(entryNumber))
			{
				abstractOrderEntryModel.setExpectedDeliveryDate(expShippingDateInDateFormat);
			}
		}
		cartModel.setEntries(abstractOrderEntryModelList);
		return jnjLaCartService.saveCartModel(cartModel, true); // change to true instead of false
	}


	public String getLanguageSpecificDatePattern(final LanguageData sessionLanguage)
	{
		final String dateLanguage = sessionLanguage != null ? sessionLanguage.getIsocode() : getCurrentUserLanguage();
		return JnJLALanguageDateFormatUtil.getLanguageSecificDatePattern(dateLanguage);
	}

	@Override
	public String getCurrentUserLanguage()
	{
		String language = "en";

		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();

		if (null != currentUser.getSessionLanguage())
		{
			language = currentUser.getSessionLanguage().getIsocode();
		}

		return language;
	}

	@Override
	public JnjCartModificationData addMultipleProds(final Map<String, String> productCodes, final Model model,
													final boolean isHomePageFileUpload, final boolean forceNewEntry, int currentEntryNumber)
	{
		final String methodName = "addMultipleProducts()";
		JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartFacadeImpl.class);

		JnjCartModificationData cartModificationData = null;
		try {
			cartModificationData = addToCartLatam(productCodes, false, isHomePageFileUpload, forceNewEntry,currentEntryNumber);
		} catch (final Exception exception) {
			JnjGTCoreUtil.logErrorMessage(ADD_TO_CART, methodName, exception.getMessage(), exception, JnjLatamCartFacadeImpl.class);
		}

		if (cartModificationData != null && !CollectionUtils.isEmpty(cartModificationData.getCartModifications())
			&& null != cartModificationData.getCartModifications().get(0).getEntry()) {
			model.addAttribute(PRODUCT, cartModificationData.getCartModifications().get(0).getEntry().getProduct());
		}

		model.addAttribute(CART_DATA, cartModificationData);

		JnjGTCoreUtil.logDebugMessage(ADD_TO_CART, methodName, Logging.END_OF_METHOD,  JnjLatamCartFacadeImpl.class);

		return cartModificationData;
	}


	@Override
	public JnjIndirectCustomerModel fetchIndirectCustomerName(final String indirectCustomer)
	{
		return jnjLaCartService.fetchIndirectCustomerName(indirectCustomer);
	}


	@Override
	public List<JnjIndirectCustomerModel> fetchIndirectCustomers(final String indirectCustomer, final CountryModel countryModel)
	{
		return jnjLaCartService.fetchIndirectCustomers(indirectCustomer, countryModel);
	}

	@Override
	public JnjIndirectCustomerModel fetchIndirectCustomerName(final String indirectCustomer, final CountryModel countryModel)
	{
		return jnjLaCartService.fetchIndirectCustomerName(indirectCustomer, countryModel);
	}

	@Override
	public JnjContractFormData validateIsNonContract(final String[] selectedProductCatalogIds, final String contractNumber)
	{
		final String methodName = "LATAM - validateIsNonContract()";
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, methodName,
			Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		final JnjContractFormData jnjContractFormData = new JnjContractFormData();
		final String currentContractNumber = StringUtils.EMPTY;
		final String receivedContractNumber = contractNumber == null ? StringUtils.EMPTY : contractNumber;

		CartData sessionCart = getSessionCart();
		if (sessionCart instanceof JnjLaCartData) {
			retrieveEntriesFromCart(selectedProductCatalogIds, jnjContractFormData, currentContractNumber, receivedContractNumber, sessionCart);
		}

		return jnjContractFormData;
	}

	private void retrieveEntriesFromCart(String[] selectedProductCatalogIds, JnjContractFormData jnjContractFormData, String currentContractNumber, String receivedContractNumber, CartData sessionCart) {
		final JnjLaCartData cartData = (JnjLaCartData) sessionCart;
		if (null != cartData)
		{
			String cartContractNumber = cartData.getContractId();
			cartContractNumber = cartContractNumber == null ? StringUtils.EMPTY : cartContractNumber;
			List<OrderEntryData> cartEntries = null;

			final boolean isContractCart = cartData.isIsContractCart();

			boolean isNonContractProductInSelectedList = false;
			boolean isNonContractProductInCart = false;

			/* Retrieve data from Cart */
			cartEntries = cartData.getEntries();

			isNonContractProductInSelectedList = isItNonContractProductInSelectedList(selectedProductCatalogIds, currentContractNumber, cartContractNumber, isContractCart, isNonContractProductInSelectedList);

			isNonContractProductInCart = isNonContractProductIsInCart(receivedContractNumber, cartEntries, isNonContractProductInSelectedList, isNonContractProductInCart);

			jnjContractFormData.setIsNonContractProductInSelectedList(isNonContractProductInSelectedList);
			jnjContractFormData.setIsNonContractProductInCart(isNonContractProductInCart);

			/* Multiple Entries */
			if (!receivedContractNumber.isEmpty() && !cartContractNumber.isEmpty()
				&& !receivedContractNumber.equalsIgnoreCase(cartContractNumber))
			{
				jnjContractFormData.setMultiContractCount(DIFF_CONTRACT);
				jnjContractFormData.setMultiProductCount(DIFF_CONTRACT);
			}
			else
			{
				jnjContractFormData.setMultiContractCount(SAME_CONTRACT);
				jnjContractFormData.setMultiProductCount(SAME_CONTRACT);
			}
		}
	}

	private boolean isNonContractProductIsInCart(String receivedContractNumber, List<OrderEntryData> cartEntries, boolean isNonContractProductInSelectedList, boolean isNonContractProductInCart) {
		if (!receivedContractNumber.isEmpty())
		{
			/* If Cart contains products that are not in the FUTURE contract */
			for (final OrderEntryData cartEntry : cartEntries)
			{
				if (!isContractProduct(cartEntry.getProduct().getCode(), receivedContractNumber))
				{
					isNonContractProductInCart = true;
				}
			}
			JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, "retrieveEntriesFromCart()",
				"isNonContractProductInSelectedList: " + isNonContractProductInSelectedList, CURRENT_CLASS);
		}
		return isNonContractProductInCart;
	}

	private boolean isItNonContractProductInSelectedList(String[] selectedProductCatalogIds, String currentContractNumber, String cartContractNumber, boolean isContractCart, boolean isNonContractProductInSelectedList) {
		if (isContractCart)
		{
			/* If List passed contains product that are NOT in the CURRENT cart contract */
			if (selectedProductCatalogIds != null && selectedProductCatalogIds.length > 0)
			{
				for (final String productCode : selectedProductCatalogIds)
				{
					if (!isContractProduct(productCode, cartContractNumber))
					{
						isNonContractProductInSelectedList = true;
					}
				}
			}
			JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, "retrieveEntriesFromCart()",
				"currentContractNumber: " + currentContractNumber, CURRENT_CLASS);
		}
		return isNonContractProductInSelectedList;
	}

	@Override
	public String getPathForView(final String page, String orderType)
	{
		String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (orderType == null)
		{
			orderType = getOrderType();
		}
		if (currentSite == null)
		{
			currentSite = Jnjb2bCoreConstants.MDD;
		}
		final String siteDir = CartViewMapping.cmsSiteCartDirMap.get(currentSite);
		final String dirForOrderType = CartViewMapping.orderTypeCartDirMap.get(orderType);
		return new StringBuilder(CartViewMapping.CartPageBaseDirPath).append(siteDir).append(dirForOrderType).append(page)
			.toString();

	}

	public String getOrderType()
	{
		return jnjGTCartService.getOrderType();
	}

	/**
	 * Method to get the Shipment Details.
	 *
	 * @return
	 */
	public Map<String, String> getShippingDetails()
	{
		List<String> materialIds;
		Map<String, String> productShipmentMap = null;
		final CartModel cartModel = getCartService().getSessionCart();

		if (cartModel != null && cartModel.getEntries() != null && cartModel.getEntries().size() > 0)
		{
			materialIds = new ArrayList<>();
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				materialIds.add(entry.getMaterialEntered());
			}
			final List<JnjDropShipmentDetailsModel> resultList = jnjLaOrderService.getShippingDetails(materialIds);

			if (resultList != null && !resultList.isEmpty())
			{
				productShipmentMap = new HashMap<>();

				for (final JnjDropShipmentDetailsModel jnjDropShipmentDetailsModel : resultList)
				{
					productShipmentMap.put(jnjDropShipmentDetailsModel.getMaterialId(), jnjDropShipmentDetailsModel.getShipper());
				}

			}
		}
		return productShipmentMap;
	}

	/**
	 * This method is used to remove the non contract product from the cart
	 *
	 * @param contractNum
	 * @return boolean
	 */
	public boolean removeNonContractProduct(final String contractNum)
	{
		return jnjLaCartService.removeNonContractProduct(contractNum);
	}

	private JnjValidateOrderData mergeValidatedOrderData(final List<JnjValidateOrderData> validatedOrderData)
	{

		final JnjValidateOrderData mergedData = new JnjValidateOrderData();
		mergedData.setValidateOrderResponse(Boolean.TRUE);
		mergedData.setSapErrorResponse(Boolean.FALSE);
		final ArrayList<String> sapErrorMessages = new ArrayList<String>();
		final Map<String, StringBuilder> removedProductCodes = new HashMap<>();

		for (final JnjValidateOrderData validatedData : validatedOrderData)
		{
			LOG.info("Validate response value:::::::::::::::::: "+validatedData.getValidateOrderResponse());
			if (!validatedData.getValidateOrderResponse())
			{
				mergedData.setValidateOrderResponse(Boolean.FALSE);
			}

			if (validatedData.getSapErrorResponse())
			{
				mergedData.setSapErrorResponse(Boolean.TRUE);
			}

			if (validatedData.getSapErrorMessages() != null)
			{
				sapErrorMessages.addAll(validatedData.getSapErrorMessages());
			}

			if (validatedData.getRemovedProductCodes() != null)
			{
				removedProductCodes.putAll(validatedData.getRemovedProductCodes());
			}
		}

		mergedData.setSapErrorMessages(sapErrorMessages);
		mergedData.setRemovedProductCodes(removedProductCodes);

		return mergedData;
	}


	@Override
	public List<String> getIndirectCustomer(final String siteDefaultCountry, final String term)
	{
		return jnjCustomerDataService.getIndirectCustomer(siteDefaultCountry, term);
	}

	@Override
	public List<String> getIndirectPayer(final String siteDefaultCountry, final String term)
	{
		return jnjCustomerDataService.getIndirectPayer(siteDefaultCountry, term);
	}


	@Override
	public String getIndirectCustomerName(final String country, final String indirectCustomer)
	{
		final JnjIndirectCustomerModel jnjIndirectCustomerModel = jnjCustomerDataService.getIndirectCustomerModel(country,
			indirectCustomer);
		if (jnjIndirectCustomerModel != null)
		{
			return jnjIndirectCustomerModel.getIndirectCustomerName();
		}
		return Jnjlab2bcoreConstants.EMPTY_STRING;
	}

	@Override
	public String getIndirectPayerName(final String country, final String indirectPayer)
	{
		final JnjIndirectPayerModel jnjIndirectPayerModel = jnjCustomerDataService.getIndirectPayerModel(country, indirectPayer);
		if (jnjIndirectPayerModel != null)
		{
			return jnjIndirectPayerModel.getIndirectPayerName();
		}
		return Jnjlab2bcoreConstants.EMPTY_STRING;
	}


	@Override
	public void updateShippingAddress(final JnJB2BUnitModel currentB2BUnit)
	{
		final AddressModel addressModel = jnjGTB2BUnitService.getShippingAddress(currentB2BUnit);
		if (addressModel != null)
		{
			jnjGTCartService.changeShippingAddress(addressModel.getPk().toString());
		}
	}

	@Override
	public boolean displaySubstitutes() {
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		final String id = cmsSiteModel.getUid();
		boolean countryProductSubstitutesFlag = configurationService
				.getConfiguration()
				.getBoolean(
						(CART_SUBSTITUTE_ENABLED
								+ Jnjb2bFacadesConstants.Logging.DOT_STRING + id),
						false);

		if (countryProductSubstitutesFlag) {
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService
					.getCurrentUser();
			final JnJLaB2BUnitModel currentB2BUnit = (JnJLaB2BUnitModel) currentUser
					.getCurrentB2BUnit();
			if (BooleanUtils.isTrue(currentB2BUnit
					.getEligibleForSubstitutes())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void replaceProducts(final CartModel cart, final List<ReplacementProductData> replacementProductsData)
	{
		if (CollectionUtils.isNotEmpty(cart.getEntries()) && CollectionUtils.isNotEmpty(replacementProductsData)) {
			final long[] updateQuantity = {DEFAULT_PRODUCT_QUANTITY};
			final Map<String, AbstractOrderEntryModel> cartEntriesMap = new HashMap<>();

			for(final ReplacementProductData replacementProductData: replacementProductsData) {
				cartService.getSessionCart().getEntries().stream().forEach(entry -> {
					cartEntriesMap.put(entry.getProduct().getCode(), entry);
					LOG.info("Entry Number : " + entry.getEntryNumber() + "\t" + "Product : " + entry.getProduct().getCode() + "\t" + "isProposed : "
							+ entry.getIsProposed() + "\t" + "Quantity : " + entry.getQuantity());
				});
				populateReplacementProduct(cart, updateQuantity, cartEntriesMap, replacementProductData);
			}
                
            cartService.setSessionCart(cart);
		}
	}

	/**
	 * Chnage shipping address.
	 *
	 * @param shippingAddrId the shipping addr id
	 * @param request
	 * @return the address data
	 */
	@Override
	public AddressData chnageShippingAddress(final String shippingAddrId, final HttpServletRequest request) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllDropShipAccounts()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final AddressModel addressModel = jnjLaCartService.changeShippingAddress(shippingAddrId);

		final HttpSession session = request.getSession();
		JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();
		if (jnJB2bCustomerModel.getDefaultShipToAddressIdMap().containsValue(shippingAddrId)) {
			session.setAttribute("makeThisAddrDefaultChangeChk", Boolean.TRUE);
		} else {
			session.setAttribute("makeThisAddrDefaultChangeChk", Boolean.FALSE);
		}
		session.setAttribute("defaultChekAddid", shippingAddrId);

		final JnjGTAddressData shippingAddressData = new JnjGTAddressData();

		if (addressModel != null) {
			jnjGTAddressPopulator.populate(addressModel, shippingAddressData);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllDropShipAccounts()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return shippingAddressData;
	}

	private void populateReplacementProduct(CartModel cart, long[] updateQuantity, Map<String, AbstractOrderEntryModel> cartEntriesMap, ReplacementProductData replacementProductData) {
        if (MapUtils.isNotEmpty(cartEntriesMap) && (cartEntriesMap.get(replacementProductData.getReplacementProductCode()) != null)) {
            AbstractOrderEntryModel ce = cartEntriesMap.get(replacementProductData.getReplacementProductCode());
            ce.setProduct(jnjLaProductService.getProductForCode(replacementProductData.getReplacementProductCode()));
            if (!ce.getIsProposed()) {
                ce.setQuantity(replacementProductData.getQuantity());
            } else {
                updateQuantity[0] = ce.getQuantity().longValue() + replacementProductData.getQuantity().longValue();
                LOG.info("Quantity is updated in the existing line item after replace the substitute product quantity : " + updateQuantity[0]);
                ce.setQuantity(updateQuantity[0]);
            }
            ce.setOriginalOrderItem(replacementProductData.getOriginalProductCode());
            ce.setIsProposed(true);
            getModelService().save(ce);
            if (cartEntriesMap.get(replacementProductData.getOriginalProductCode()) != null) {
                LOG.info("Removing duplicate entry from the cart after merge the quantity : " + replacementProductData.getOriginalProductCode());
                getModelService().remove(cartEntriesMap.get(replacementProductData.getOriginalProductCode()));
            }
            getModelService().save(cart);
            getModelService().refresh(cart);
        } else if (MapUtils.isNotEmpty(cartEntriesMap) && cartEntriesMap.get(replacementProductData.getOriginalProductCode()) != null && StringUtils.isNotEmpty(replacementProductData.getReplacementProductCode())) {
			AbstractOrderEntryModel ce = null;
			if(cartEntriesMap.get(replacementProductData.getReplacementProductCode()) != null) {
				ce = cartEntriesMap.get(replacementProductData.getOriginalProductCode());
				ce.setQuantity(replacementProductData.getQuantity());
				ce.setOriginalOrderItem(replacementProductData.getOriginalProductCode());
				ce.setIsProposed(true);
			} else {
				ce = cartEntriesMap.get(replacementProductData.getOriginalProductCode());
				ce.setProduct(jnjLaProductService.getProductForCode(replacementProductData.getReplacementProductCode()));
				ce.setQuantity(replacementProductData.getQuantity());
				ce.setOriginalOrderItem(replacementProductData.getOriginalProductCode());
				ce.setIsProposed(true);
			}
            getModelService().save(ce);
            getModelService().save(cart);
            getModelService().refresh(cart);
        }
    }

	@Override
	public boolean updateComplementaryInfo(final String complementaryInfo) {
		return jnjLaCartService.updateComplementaryInfo(complementaryInfo);
	}
	
	@Override
	public void setCustomerFreightType(final String customerFreightType) {
		jnjLaCartService.setCustomerFreightType(customerFreightType);
		
	}
	
	public JnjContractService getJnjContractService() {
		return jnjContractService;
	}

	public void setJnjContractService(final JnjContractService jnjContractService) {
		this.jnjContractService = jnjContractService;
	}

}

