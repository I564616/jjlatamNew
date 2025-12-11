/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.cart.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.core.constants.CartViewMapping;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
import com.jnj.core.data.JnjGTDivisonData;
import com.jnj.core.data.JnjGTGetPriceQuoteResponseData;
import com.jnj.core.data.JnjGTOrderReturnResponseData;
import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.JnjGTUpdatePriceData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTSurgeryInfoData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.event.JnjGTCartOrderConfirmationEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjGTCreditCardModel;
import com.jnj.core.model.JnjGTEarlyZipCodesModel;
import com.jnj.core.model.JnjGTShippingMethodModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjModelService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.operations.JnjGTOperationsService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.converters.populator.address.JnjGTAddressPopulator;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractFormData;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTCommonFormIOData;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTOutOrderLine;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTShippingMethodData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.facades.data.JnjGTReturnMessageData;
import com.jnj.facades.data.SurgeryInfoData;
import com.jnj.facades.order.JnjGTSalesOrderMapper;
import com.jnj.facades.order.converters.populator.JnjGTOrderEntryPopulator;
import com.jnj.facades.order.converters.populator.JnjGTSurgeryPopulator;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;
import com.jnj.gt.outbound.mapper.JnjGTCreateConsOrdMapper;
import com.jnj.gt.outbound.mapper.JnjGTCreateDeliveredOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTGetPriceQuoteMapper;
import com.jnj.gt.outbound.mapper.JnjGTOrderReturnMapper;
import com.jnj.gt.outbound.mapper.JnjGTProposedOrderItemMapper;
import com.jnj.gt.outbound.mapper.JnjGTSimulateConsignmentOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTSimulateDeliveredOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTSimulateOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTConsInventoryReportMapper;

import com.jnj.services.MessageService;

import de.hybris.platform.b2b.services.B2BCartService;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import java.util.Base64;


/**
 * This is implementation class of JnjGTCartFacade , is used to perform various opertions on Cart specific to JNJ NA
 * requirement.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTCartFacade extends DefaultJnjCartFacade implements JnjGTCartFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCartFacade.class);
	protected static final String _1 = "1";
	protected static final String UPLOAD_TEMPLATE_START_FROM = "home.upload.template.startFrom";
	@Resource(name = "commerceCartService")
	protected JnjGTCartService jnjGTCartService;

	@Autowired
	protected MessageService messageService;

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected I18NService i18nService;

	@Autowired
	protected EventService eventService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected BaseSiteService baseSiteService;

	@Autowired
	protected CommonI18NService commonI18NService;

	@Autowired
	protected BaseStoreService baseStoreService;

	@Autowired
	protected JnjGTCreateConsOrdMapper jnjGTCreateConsOrdMapper;

	@Autowired
	protected Populator<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoPopulator;

	@Autowired
	protected Populator<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReversePopulator;

	@Autowired
	protected JnjGTAddressPopulator jnjGTAddressPopulator;

	@Autowired
	protected JnjGTSimulateOrderMapper jnjGTSimulateOrderMapper;
	
	@Autowired
	protected JnjGTProposedOrderItemMapper jnjGTProposedOrderItemMapper; 
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	@Autowired
	protected JnjGTSalesOrderMapper jnjGTSalesOrderMapper;

	@Resource(name="GTSimulateDeliveredOrderMapper")
	protected JnjGTSimulateDeliveredOrderMapper jnjGTSimulateDeliveredOrderMapper;
	
	@Resource(name="GTSimulateConsignmentOrderMapper")
	protected JnjGTSimulateConsignmentOrderMapper jnjGTSimulateConsignmentOrderMapper;

	@Resource(name="GTCreateDeliveredOrderMapper")
	protected JnjGTCreateDeliveredOrderMapper jnjGTCreateDeliveredOrderMapper;

	@Autowired
	protected JnjGTGetPriceQuoteMapper jnjGTGetPriceQuoteMapper;

	@Autowired
	protected JnjGTOrderReturnMapper jnjGTOrderReturnMapper;

	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;

	@Autowired
	@Qualifier(value = "addressReversePopulator")
	protected AddressReversePopulator addressReversePopulator;

	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	protected JnjModelService jnjModelService;

	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerSevice;
	
	@Autowired
	protected JnjGTSurgeryPopulator jnjGTSurgeryPopulator;
	
	@Autowired
	protected JnjGTOperationsService jnjGTOperationsService;
	
	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
	/*@Autowired
	JnjAmazonUploadIntFacade jnjAmazonUploadIntFacade;*/

	@Autowired
	protected B2BOrderService b2bOrderService;

	@Resource(name = "b2bProductFacade")
	protected ProductFacade productFacade;

	@Autowired
	protected B2BCartService b2bCartService;

	@Autowired
	protected JnjGTOrderEntryPopulator jnjGTOrderEntryPopulator;

	@Autowired
	protected Populator<JnjGTShippingMethodModel, JnjGTShippingMethodData> jnjGTShippingMethodPopulator;
	
	@Resource(name = "priceDataFactory")
	protected JnjPriceDataFactory jnjPriceDataFactory;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	protected CartService cartService;
	
	@Autowired
	protected JnjOrderUtil orderUtil;
	
	@Resource(name = "GTB2BUnitFacade")
	protected JnjGTB2BUnitFacade jnjGTUnitFacade;
	
	@Autowired
	protected JnjGTConsInventoryReportMapper jnjGTConsInventoryReportMapper;

	final List<String> UNCOMPATIBLE_DIVISION_CODES_ZOR = new ArrayList(
			Arrays.asList(Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_OCD),
					Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_VX),
					Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_C4)));

	final List<String> UNCOMPATIBLE_DIVISION_CODES_ZNC = new ArrayList(Arrays.asList(
			Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_OCD),
			Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_VX),
			Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_C4),
			Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_ASP)));

	final List<String> COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS = new ArrayList(Arrays.asList(
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEFO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEPTO),
		    Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDESTO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDE2D)));



	final List<String> COMPATIBLE_SHIPPING_METHODS = new ArrayList(Arrays.asList(
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEFO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEPTO),
		    Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDESTO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDE2D)));

	final List<String> COMPATIBLE_SHIPPING_METHODS_ZKB = new ArrayList(Arrays.asList(
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEFO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEPO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDESO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDE2D),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_UPSSE)));


	final List<String> COMPATIBLE_SHIPPING_METHODS_ASP = new ArrayList(Arrays.asList(
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEFO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEPO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDESO),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDE2D),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_UPSSE),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_UPSSN),
			Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_UPSSL)));

	@Override
	public boolean updatePurchaseOrderNumber(final String purchaseOrderNumber)
	{
		return jnjGTCartService.updatePurchaseOrderNumber(purchaseOrderNumber);
	}

	@Override
	public boolean updateReasonCode(final String reasonCode)
	{
		return jnjGTCartService.updateReasonCode(reasonCode);
	}


	@Override
	public boolean updateDistributorPONumber(final String distributorPONumber)
	{
		return jnjGTCartService.updateDistributorPONumber(distributorPONumber);
	}

	@Override
	public boolean updateAttention(final String attention)
	{
		return jnjGTCartService.updateAttention(attention);
	}
	
	@Override
	public boolean updateSpecialText(final String specialText)
	{
		return jnjGTCartService.updateSpecialText(specialText);
	}

	@Override
	public AddressData updateDropShipAccount(final String dropShipAccount) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "updateDropShipAccount()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		AddressData dropShipAddData = null;
		final AddressModel dropShipAddressModel = jnjGTCartService.updateDropShipAccount(dropShipAccount);

		if (null != dropShipAddressModel)
		{
			dropShipAddData = new JnjGTAddressData();
			jnjGTAddressPopulator.populate(dropShipAddressModel, dropShipAddData);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "updateDropShipAccount()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return dropShipAddData;
	}

	@Override
	public void setShippingAddress(final AddressData addressData)
	{
		final AddressModel addressModel = new AddressModel();
		addressReversePopulator.populate(addressData, addressModel);


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getContactAddress()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CartModel sessionCart = getCartService().getSessionCart();
		addressModel.setOwner(sessionCart);
		sessionCart.setDeliveryAddress(addressModel);
		jnjGTCartService.saveCartModel(sessionCart, false);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getContactAddress()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}
	@Override
	public void setBillingAddress(final AddressData addressData)
	{
		final AddressModel addressModel = new AddressModel();
		addressReversePopulator.populate(addressData, addressModel);


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getContactAddress()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CartModel sessionCart = getCartService().getSessionCart();
		addressModel.setOwner(sessionCart);
		sessionCart.setPaymentAddress(addressModel);
		jnjGTCartService.saveCartModel(sessionCart, false);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getContactAddress()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	@Override
	public void setCustomShippingAddress(final boolean customoeShippingAddress)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "setCustomShippingAddress()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CartModel sessionCart = getCartService().getSessionCart();
		sessionCart.setCustomShippingAddress(customoeShippingAddress);
		jnjGTCartService.saveCartModel(sessionCart, false);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "setCustomShippingAddress()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	@Override
	public void removeOneTimeShippingAddress()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "removeOneTimeShippingAddress()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CartModel sessionCart = getCartService().getSessionCart();
		sessionCart.setDeliveryAddress(jnjGTB2BUnitService.getShippingAddress(null));
		sessionCart.setCustomShippingAddress(false);
		jnjGTCartService.saveCartModel(sessionCart, false);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "removeOneTimeShippingAddress()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	@Override
	public void setDefaultDeliveryDate(final Date defaulDeliveryDate)
	{
		jnjGTCartService.setDefaultDeliveryDate(defaulDeliveryDate);
	}

	@Override
	public boolean updateNamedDeliveryDate(final String expDeliveryDate)
	{
		Date expDeliveryDateInDateFormat = null;
		final DateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		try
		{
			expDeliveryDateInDateFormat = dateFormat.parse(expDeliveryDate);
		}
		catch (final ParseException parseExp)
		{
			LOGGER.error("Invalid date format for requested delivery date", parseExp);
		}
		final CartModel cartModel = getCartService().getSessionCart();
		cartModel.setRequestedDeliveryDate(expDeliveryDateInDateFormat);

		return jnjGTCartService.saveCartModel(cartModel, false);
	}

	@Override
	public void saveCreditCardInfo(final CCPaymentInfoData ccPaymentInfoData, final boolean remember)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "saveCreditCardInfo()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final JnjGTCreditCardModel ccPaymentInfoModel = jnjGTCartService.createCCPaymentInfoModel();

		// associating credit card info with current user
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) getUserService().getCurrentUser();
		final JnJB2BUnitModel currentB2BUnit = (JnJB2BUnitModel) currentUser.getDefaultB2BUnit();

		ccPaymentInfoData.setAccountHolderName(currentUser.getDisplayName());
		if (remember)
		{
			ccPaymentInfoData.setSaved(true);
		}
		else
		{
			ccPaymentInfoData.setSaved(false);
		}
		cardPaymentInfoReversePopulator.populate(ccPaymentInfoData, ccPaymentInfoModel);
		ccPaymentInfoModel.setAccount(currentB2BUnit);
		ccPaymentInfoModel.setUser(currentUser);
		ccPaymentInfoModel.setCode(ccPaymentInfoData.getId());
		jnjGTCartService.saveCreditCardInfo(ccPaymentInfoModel);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "saveCreditCardInfo()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	@Override
	public Collection<CCPaymentInfoData> getAllCreditCardsInfo()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllCreditCardsInfo()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final UserModel currentUser = getUserService().getCurrentUser();

		final Collection<PaymentInfoModel> paymentInfos = currentUser.getPaymentInfos();

		final List<CCPaymentInfoData> paymentInfoDatas = new ArrayList<>();

		for (final PaymentInfoModel paymentInfoModel : paymentInfos)
		{
			if (paymentInfoModel instanceof JnjGTCreditCardModel)
			{
				final CCPaymentInfoData ccPaymentInfoData = new CCPaymentInfoData();
				final JnjGTCreditCardModel ccPaymentInfoModel = (JnjGTCreditCardModel) paymentInfoModel;
				if (BooleanUtils.isTrue(ccPaymentInfoModel.getIsRemember()))
				{
					creditCardPaymentInfoPopulator.populate(ccPaymentInfoModel, ccPaymentInfoData);
					if (StringUtils.isNotEmpty(ccPaymentInfoData.getCardType()))
					{
						final String crediCardType = StringUtils.substring(ccPaymentInfoData.getCardType(), 2);
						ccPaymentInfoData.setCardType(crediCardType);
					}
					paymentInfoDatas.add(ccPaymentInfoData);
				}
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllCreditCardsInfo()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return paymentInfoDatas;
	}

	@Override
	public Map<String, String> getRestrictedFieldsForUser()
	{

		return jnjGTCartService.getHiddenFields();
	}

	@Override
	public Map<String, String> getMandatoryFieldsForUser()
	{

		return jnjGTCartService.getMandatoryFields();
	}

	@Override
	protected CartData createEmptyCart()
	{
		return getCartConverter().convert(getCartService().getSessionCart());
	}

	@Override
	public boolean updateLotNumberForEntry(final int entryNumber, final String newLotComment)
	{
		return jnjGTCartService.updateLotNumberForEntry(entryNumber, newLotComment);
	}

	@Override
	public boolean updateBatchDetailsForEntry(final int entryNumber, final String batchNumber, final String serialNumber)
	{
		return jnjGTCartService.updateBatchDetailsForEntry(entryNumber, batchNumber,serialNumber);
	}

	@Override
	public AddressData getShippingAddressById(final String shippingAddrId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getShippingAddressById()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final AddressModel shippingAddressModel = jnjGTCartService.getShippingAddressById(shippingAddrId);

		final AddressData shippingAddressData = new AddressData();

		if (shippingAddressModel != null)
		{
			jnjGTAddressPopulator.populate(shippingAddressModel, shippingAddressData);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getShippingAddressById()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return shippingAddressData;
	}


	@Override
	public boolean updatePaymentInfo(final String paymentInfoId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllDropShipAccounts()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		jnjGTCartService.updatePaymentInfo(paymentInfoId);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllDropShipAccounts()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return true;
	}


	@Override
	public AddressData chnageShippingAddress(final String shippingAddrId,final HttpServletRequest request)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllDropShipAccounts()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final AddressModel addressModel = jnjGTCartService.changeShippingAddress(shippingAddrId);
		
		final HttpSession session=request.getSession();
		JnJB2bCustomerModel jnJB2bCustomerModel = jnjGTCustomerSevice.getCurrentUser();
		if(jnJB2bCustomerModel.getDefaultShipToAddressIdMap().containsValue(shippingAddrId)){ 
			session.setAttribute("makeThisAddrDefaultChangeChk", Boolean.TRUE);
		}else{
			session.setAttribute("makeThisAddrDefaultChangeChk", Boolean.FALSE);
		}
		session.setAttribute("defaultChekAddid", shippingAddrId);

		final JnjGTAddressData shippingAddressData = new JnjGTAddressData();

		if (addressModel != null)
		{
			jnjGTAddressPopulator.populate(addressModel, shippingAddressData);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllDropShipAccounts()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return shippingAddressData;
	}
	
	@Override
	public AddressData changeBillingAddress(final String billingAddrId,final HttpServletRequest request)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllDropShipAccounts()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final AddressModel addressModel = jnjGTCartService.changeBillingAddress(billingAddrId);
		
		final HttpSession session=request.getSession();
		JnJB2bCustomerModel jnJB2bCustomerModel = jnjGTCustomerSevice.getCurrentUser();
		if(jnJB2bCustomerModel.getDefaultBillToAddressIdMap().containsValue(billingAddrId)){ 
			session.setAttribute("makeThisAddrDefaultChangeChkForBilling", Boolean.TRUE);
		}else{
			session.setAttribute("makeThisAddrDefaultChangeChkForBilling", Boolean.FALSE);
		}
		session.setAttribute("defaultCheckforBillingAddid", billingAddrId);

		final JnjGTAddressData billingAddressData = new JnjGTAddressData();

		if (addressModel != null)
		{
			jnjGTAddressPopulator.populate(addressModel, billingAddressData);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CART_PAGE + Logging.HYPHEN + "getAllDropShipAccounts()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return billingAddressData;
	}
	

	@Override
	public JnjValidateOrderData validateOrder(final JnjGTSapWsData wsData) throws IntegrationException, SystemException,
			BusinessException, TimeoutException
	{
		// rama needs to validate order once third party ready.
		{
			if (LOGGER.isDebugEnabled())
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "validateOrder()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjValidateOrderData validateOrderData = null;
		JnjGTOutboundStatusData jnjGTOutboundStatusData = null;
		JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData=null;

		// Cart Model object of the logged user is retrieved by using getSessionCart method of Cart Service.
		final CartModel cartModel = getCartService().getSessionCart();
		
		//HGDE - 10:Changes for Bonus Item starts

		if (sessionService != null)
		{
			// setting the actual quantity with which create order needs to be invoked
			final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
			for (final AbstractOrderEntryModel abstOrdEntModel : cartModel.getEntries())
			{
				if (freeGoodsMap != null && abstOrdEntModel != null && freeGoodsMap.containsKey(abstOrdEntModel.getProduct().getCode()))
				{
					final String freeItemsQuantity = freeGoodsMap.get(abstOrdEntModel.getProduct().getCode()).getMaterialQuantity();
					final JnjGTOutOrderLine outOrderLine = freeGoodsMap.get(abstOrdEntModel.getProduct().getCode());
					final long orderedQuantity = Long.parseLong(outOrderLine.getOrderedQuantity());
					final long chargedQuantity = abstOrdEntModel.getQuantity();
					final long totalQuantity = Long.parseLong(freeItemsQuantity) + chargedQuantity;
					if (orderedQuantity == totalQuantity)
					{
						abstOrdEntModel.setQuantity(orderedQuantity);
					}
					else
					{
						abstOrdEntModel.setQuantity(chargedQuantity);
					}

					// Set the final Quantity
				}
			}

			//Clear the free Goods from Session
			if (freeGoodsMap != null)
			{
				sessionService.removeAttribute("freeGoodsMap");
			}
		}
		
		
		boolean split = orderUtil.isOrderSplit();		
		
		if(!split){
			if (LOGGER.isDebugEnabled())
					LOGGER.debug("invoked split false");
			
			if (JnjOrderTypesEnum.ZOR.equals(cartModel.getOrderType()) || JnjOrderTypesEnum.ZEX.equals(cartModel.getOrderType())
					|| JnjOrderTypesEnum.ZNC.equals(cartModel.getOrderType()))
			{
				//jnjGTOutboundStatusData = jnjGTSimulateOrderMapper.mapSimulateOrderRequestResponse(cartModel, false, wsData); Soumitra - Commented to use single mapper for all order types
				jnjGTProposedOrderResponseData = jnjGTProposedOrderItemMapper.mapConsignmentSimulateOrderRequestResponse(cartModel,  wsData);
			}
			else if (JnjOrderTypesEnum.ZHOR.equals(cartModel.getOrderType()) || JnjOrderTypesEnum.ZTOR.equals(cartModel.getOrderType())
					|| JnjOrderTypesEnum.ZIO2.equals(cartModel.getOrderType()))
			{
				jnjGTOutboundStatusData = jnjGTCreateConsOrdMapper.mapSimulateConsOrdRequestResponse(cartModel, wsData);
			}
			else if (JnjOrderTypesEnum.ZDEL.equals(cartModel.getOrderType()) || JnjOrderTypesEnum.ZKB.equals(cartModel.getOrderType()))
			{
				jnjGTOutboundStatusData = jnjGTSimulateDeliveredOrderMapper.mapDelSimulateOrderRequestResponse(cartModel, wsData);
			}
			else if (JnjOrderTypesEnum.KA.equals(cartModel.getOrderType()) || JnjOrderTypesEnum.KB.equals(cartModel.getOrderType())|| JnjOrderTypesEnum.KE.equals(cartModel.getOrderType())) {
				jnjGTProposedOrderResponseData = jnjGTProposedOrderItemMapper.mapConsignmentSimulateOrderRequestResponse(cartModel,  wsData);
			}
		}
		else{
			if (LOGGER.isDebugEnabled())
					LOGGER.debug("invoked split true");
			jnjGTOutboundStatusData = jnjGTSalesOrderMapper.mapSalesOrderSimulationWrapper(cartModel,false);
		}
		
		if(null!=jnjGTProposedOrderResponseData)
		{
			Map<String, List<JnjGTReturnMessageData>> sapErrorMessageMap = new HashMap<>();
			validateOrderData = new JnjValidateOrderData();
			validateOrderData.setValidateOrderResponse(Boolean.valueOf(true));
			List<JnjGTCommonFormIOData> jnjGTCommonFormIODatas = jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList();
			for (Iterator iterator = jnjGTCommonFormIODatas.iterator(); iterator.hasNext();) {
				JnjGTCommonFormIOData jnjGTCommonFormIOData = (JnjGTCommonFormIOData) iterator.next();
				List<JnjGTReturnMessageData> returnMessageDataList = jnjGTCommonFormIOData.getJnjGTReturnMessageDataList();
				List<JnjGTReturnMessageData> returnMessages = new ArrayList<>();
				for (Iterator iterator2 = returnMessageDataList.iterator(); iterator2.hasNext();) {
					JnjGTReturnMessageData jnjGTReturnMessageData = (JnjGTReturnMessageData) iterator2.next();
					if(cartModel.getOrderType().getCode().equalsIgnoreCase(JnjOrderTypesEnum.KB.getCode())){
					if(jnjGTReturnMessageData.getId().equalsIgnoreCase("CBC_QTY_EXCEED")){
							returnMessages.add(jnjGTReturnMessageData);
							break;
						}
						continue;
					}
					if(cartModel.getOrderType().getCode().equalsIgnoreCase(JnjOrderTypesEnum.KA.getCode())){
						if(jnjGTReturnMessageData.getId().equalsIgnoreCase("NOQTY")){
							returnMessages.add(jnjGTReturnMessageData);
						}
					continue;
					}
					returnMessages.add(jnjGTReturnMessageData);
				}
				if(CollectionUtils.isNotEmpty(returnMessages)){
				sapErrorMessageMap.put(jnjGTCommonFormIOData.getProductId(),returnMessages);
				}
			}
			if(!sapErrorMessageMap.isEmpty()) {
				validateOrderData.setSapErrorResponse(true);
			}else{
				validateOrderData.setSapErrorResponse(false);
			}
			sessionService.setAttribute("sapErrorResponse", sapErrorMessageMap);
		}
		if (null != jnjGTOutboundStatusData) {
			validateOrderData = new JnjValidateOrderData();
			//validateOrderData.setValidateOrderResponse(Boolean.valueOf(jnjGTOutboundStatusData.isSavedSuccessfully()));
			validateOrderData.setValidateOrderResponse(Boolean.valueOf(true));
			// To handle the hard stop error, it will enter inside if block.
                       jnjGTOutboundStatusData.setHardStopErrorOcurred(Boolean.valueOf(true));
			if (jnjGTOutboundStatusData.isHardStopErrorOcurred())
			{
				validateOrderData.setHardStopError(jnjGTOutboundStatusData.isHardStopErrorOcurred());
			}
			else if (null != jnjGTOutboundStatusData.getRemovedProductCodes()
					&& !jnjGTOutboundStatusData.getRemovedProductCodes().isEmpty())
			{
				validateOrderData.setRemovedProductCodes(jnjGTOutboundStatusData.getRemovedProductCodes());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "validateOrder()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return validateOrderData;
	}

	@Override
	public CartModificationData addToCart(final String code, final long quantity) throws CommerceCartModificationException
	{
		CartModificationData cartModificationData = null;
		try
		{
			final Date startTime = new Date();
			LOGGER.info("Start: Loading Product from Database" + code);
			ProductModel product = jnJGTProductService.getProductByCodeOrEAN(code);
			LOGGER.info("End>>: Loading Product from Database");
			if (null != product)
			{
				LOGGER.info("Start: Perform Business Validating " + code);
				final String validationError = validateProductCode(product);
				LOGGER.info("End>>: Perform Business Validating");

				if (null == validationError)
				{
					if (product instanceof JnJProductModel)
					{
						product = jnJGTProductService.getDeliveryGTIN(product);
					}

					final CartModel cartModel = getCartService().getSessionCart();
					final CommerceCartModification cartModification;
					final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
					/*if (StringUtils.equals(currentSite, JnjPCMCoreConstants.PCM))
					{

						cartModification = jnjGTCartService.addToCartForPCM(cartModel, product, quantity, product.getUnit(), false);
					}

					else
					{*/
						LOGGER.info("Start: Adding product to cart via cart service");
						cartModification = jnjGTCartService.addToCart(cartModel, product, quantity, product.getUnit(), false);
						LOGGER.info("End>>: Adding product to cart via cart service");
					//}
					LOGGER.info("Start: CartModifiaction Conversion");
					cartModificationData = getCartModificationConverter().convert(cartModification);
					LOGGER.info("End>>: CartModifiaction Conversion");
				}

				else
				{// Given product code is not valid
					cartModificationData = new CartModificationData();
					updateCartModificationData(cartModificationData, code, validationError);

					LOGGER.error("Add to Cart - Product Code-" + code + " Found. But, product validation was failed. Error message: "
							+ validationError);
					LOGGER.error("Will be logged into the database");
					jnjGTOperationsService.logAuditData("User", validationError, "", false, false, new Date(), "");
					
				}
			}
			else
			{// Given product code not found
				cartModificationData = new CartModificationData();
				updateCartModificationData(cartModificationData, code,
						messageService.getMessageForCode(Jnjb2bCoreConstants.Cart.INVALID_PRODUCTCODE, null, code));

				LOGGER.error("Add to Cart - Product Code-" + code + "not Found for current catalog");
				LOGGER.error("Add to Cart - Product Code-" + code + "is invalid");

			}
			final Date endTime = new Date();
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("----->JnjGTCartFacadeImpl - addToCart(" + code
						+ ") -----> Total Time taken for adding a product to Cart " + (endTime.getTime() - startTime.getTime())
						+ "seconds");
			}
		}
		catch (final ModelLoadingException itemNotFoundExp)
		{
			LOGGER.error("Add to Cart - Product Code: " + code + "not Valid for add to session cart");

		}
		return cartModificationData;
	}

	@Override
	public JnjCartModificationData addToCartPCM(final Map<String, String> productCodes, final boolean ignoreUPC,
			final String currentSite, final boolean isHomePageFileUpload)
	{
		final JnjCartModificationData jnjCartModificationData = new JnjCartModificationData();
		/*final Map<String, String> revisedErrorMap = new HashMap<String, String>();
		List<CartModificationData> cartModificationList = new ArrayList<CartModificationData>(1);

		CartModificationData cartModificationData = null;
		List<CommerceCartModification> commCartModificationList = new ArrayList<CommerceCartModification>();
		final Set<ProductModel> productModels = new HashSet<ProductModel>();
		for (final String productCode : productCodes.keySet())
		{

			try
			{
				final ProductModel product = jnJGTProductService.getProductByValue(productCode, ignoreUPC);
				if (null != product)
				{
					productModels.add(product);
					productCodes.put(productCode, Jnjb2bCoreConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS);
				}
				else
				{
					cartModificationData = new CartModificationData();
					final String errorMsg = messageService.getMessageForCode(Jnjb2bCoreConstants.Cart.INVALID_PRODUCTCODE, null,
							productCode);
					updateCartModificationData(cartModificationData, productCode, errorMsg);
					productCodes.put(productCode, errorMsg);
					if (isHomePageFileUpload)
					{
						revisedErrorMap.put(productCode, errorMsg);
					}
					LOGGER.error("Add to Cart - Product Code-" + productCode + "not Found for current catalog");
				}
			}
			catch (final ModelLoadingException itemNotFoundExp)
			{
				LOGGER.error("Add to Cart - Product Code: " + productCode + "not Valid for add to session cart");
			}
		}
		if (isHomePageFileUpload)
		{
			productCodes.clear();
			productCodes.putAll(revisedErrorMap);
		}
		final CartModel cartModel = getCartService().getSessionCart();
		commCartModificationList = jnjGTCartService.addMultiProdsToCartPCM(cartModel, productModels);
		if (ignoreUPC)
		{
			cartModificationData = getCartModificationConverter().convert(
					commCartModificationList.get(commCartModificationList.size() - 1));
			cartModificationList.add(cartModificationData);
		}
		else
		{
			cartModificationList = Converters.convertAll(commCartModificationList, getCartModificationConverter());
		}
		jnjCartModificationData.setTotalUnitCount(calcTotalUnitCount(cartModel));
		jnjCartModificationData.setCartModifications(cartModificationList);*/
		return jnjCartModificationData;
	}
	
	public JnjCartModificationData addToCartGT(final Map<String, String> productCodes, final boolean ignoreUPC,
			final boolean isHomePageFileUpload)
	{
		final JnjCartModificationData jnjCartModificationData = addProductsTocart(productCodes,ignoreUPC,isHomePageFileUpload,false,0);
		return jnjCartModificationData;
	}
	
	@Override
	public JnjCartModificationData addToCartGT(final Map<String, String> productCodes, final boolean ignoreUPC,
			final boolean isHomePageFileUpload, boolean forceNewEntry, int currentEntryNumber)
	{
		final JnjCartModificationData jnjCartModificationData = addProductsTocart(productCodes,ignoreUPC,isHomePageFileUpload,forceNewEntry,currentEntryNumber);
		return jnjCartModificationData;
	}
	
	/**
	 * 
	 * @param productCodes
	 * @param ignoreUPC
	 * @param isHomePageFileUpload
	 * @param b
	 * @param i
	 * @return
	 */
	private JnjCartModificationData addProductsTocart(Map<String, String> productCodes, boolean ignoreUPC,
			boolean isHomePageFileUpload, boolean forceNewEntry, int currentEntryNumber) {

		final JnjCartModificationData jnjCartModificationData = new JnjCartModificationData();
		final Map<String, String> revisedErrorMap = new HashMap<String, String>();
		final List<CartModificationData> cartModificationList = new ArrayList<CartModificationData>(1);
		CartModificationData cartModificationData = null;

		// Set value in session so that product query doesn't include UPC
		/*if (ignoreUPC)
		{
			sessionService.setAttribute(JnjPCMCoreConstants.Cart.IGNORE_UPC, JnjPCMCoreConstants.Cart.IGNORE_UPC_VALUE);
		}*/
		//UAT-556
		final Map<String, String> errorDetailMap = new HashMap<String, String>();
		final Map<ProductModel, Long> productQtyMap = new LinkedHashMap<ProductModel, Long>(productCodes.size());
		boolean isProductsValid=true;
		for (final String productCode : productCodes.keySet())
		{
			
			try
			{
				final ProductModel product = jnJGTProductService.getProductByCodeOrEAN(productCode);
				LOGGER.debug("isProductsValid :"+isProductsValid);
				if (null != product)
				{
					final String validationError = validateProductCode(product);
					if (null == validationError && isProductsValid)
					{
						productQtyMap.put(product, Long.valueOf(productCodes.get(productCode)));
						productCodes.put(productCode, Jnjb2bCoreConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS);
					}
					else
					{ // Given product code is not valid
						isProductsValid=false;
						cartModificationData = new CartModificationData();
						updateCartModificationData(cartModificationData, productCode, validationError);
						productCodes.put(productCode, validationError);
						errorDetailMap.put(productCode, validationError);
						if (isHomePageFileUpload)
						{
							revisedErrorMap.put(productCode, validationError);
						}
						LOGGER.error("Add to Cart - Product Code-" + productCode
								+ " Found. But, product validation was failed. Error message: " + validationError);
					}
					// End of modification
				}
				else
				{
					// Given product code not found
					isProductsValid=false;
					cartModificationData = new CartModificationData();
					final String errorMsg = messageService.getMessageForCode(Jnjb2bCoreConstants.Cart.INVALID_PRODUCTCODE, null,
							productCode);
					updateCartModificationData(cartModificationData, productCode, errorMsg);
					productCodes.put(productCode, errorMsg);
					//UAT-556
					errorDetailMap.put(productCode, errorMsg);
					if (isHomePageFileUpload)
					{
						revisedErrorMap.put(productCode, errorMsg);
					}
					LOGGER.error("Add to Cart - Product Code-" + productCode + "not Found for current catalog");
				}
				isProductsValid=true;
				
			}
			catch (final ModelLoadingException itemNotFoundExp)
			{
				LOGGER.error("Add to Cart - Product Code: " + productCode + "not Valid for add to session cart");
			}
		}
		//fixed to remove the error message while upload a bulk product from home page through xls. so added another condition if revisedErrorMap is not null  
		if (isHomePageFileUpload && revisedErrorMap.size() > 0)
		{
			productCodes.clear();
			productCodes.putAll(revisedErrorMap);
		}

		/*dont know what purpose session setting commented. but facing issue while upload a product from home page through xls. 
		so uncommented to test */
		LOGGER.debug("isProductsValid atlast "+isProductsValid);
		if(! isProductsValid){
			LOGGER.debug("if of isProductsValid");
			sessionService.setAttribute("isProductsValid", Boolean.valueOf(false));
			productQtyMap.clear();
		}
		else{
			LOGGER.debug("else of isProductsValid");
			sessionService.setAttribute("isProductsValid", Boolean.valueOf(true));
		}
		// Reset value in session so that product query includes UPC
		/*if (ignoreUPC)
		{
			sessionService.removeAttribute(JnjPCMCoreConstants.Cart.IGNORE_UPC);
		}*/
		if (!productQtyMap.isEmpty())
		{
			final CartModel cartModel = getCartService().getSessionCart();
			CommerceCartModification cartModification = null;
			if(forceNewEntry){
				cartModification = jnjGTCartService.addToCartGT(cartModel, productQtyMap,forceNewEntry,currentEntryNumber);
			}
			else{
				cartModification = jnjGTCartService.addToCartGT(cartModel, productQtyMap);
			}
			cartModificationData = getCartModificationConverter().convert(cartModification);
			jnjCartModificationData.setTotalUnitCount(calcTotalUnitCount(cartModel));
		}
		cartModificationList.add(cartModificationData);
		if (StringUtils.equals(cartModificationData.getStatusCode(), Jnjb2bCoreConstants.Cart.BASKET_PAGE_MESSAGE_MIN_QTY_ADDED)
				|| StringUtils.equals(cartModificationData.getStatusCode(),
						Jnjb2bCoreConstants.Cart.BASKET_PAGE_MESSAGE_QTY_ADJUSTED))
		{
			jnjCartModificationData.setShowQtyAdjustment(true);
		}
		jnjCartModificationData.setCartModifications(cartModificationList);
		//UAT-556
		sessionService.setAttribute("errorDetailMap",errorDetailMap);
		return jnjCartModificationData;
	
	}

	

	@Override
	public String validateProductCode(final ProductModel productModel)
	{
		ProductModel productModelToValidate = productModel;
		final Date startTime = new Date();
		if (productModelToValidate != null)
		{
			// From session get the current site i.e. MDD/CONS
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

			// If site is MDD, variant needs to be searched upon GTIN and Base Material Number
			if (productModelToValidate instanceof JnjGTVariantProductModel)
			{
				productModelToValidate = ((JnjGTVariantProductModel) productModelToValidate).getBaseProduct();
			}
			final Boolean salesRep = sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS);

			// If product is not saleable return the corresponding error message
			final String status = jnJGTProductService.isProductSaleable((JnJProductModel) productModelToValidate, currentSite);
			if (null != status)
			{
				return status;
			}
			// If user is a sales rep with place order privilege, and If product is not of compatible division return the corresponding error message
			else if (Jnjb2bCoreConstants.MDD.equals(currentSite) && BooleanUtils.isTrue(salesRep)
					&& !jnJGTProductService.isProductDivisionSameAsUserDivision((JnJProductModel) productModelToValidate))
			{
				jnjGTOperationsService.logAuditData("User", productModelToValidate.getCode() + Jnjb2bCoreConstants.UserSearch.SPACE + jnjCommonFacadeUtil.getMessageFromImpex(Jnjb2bCoreConstants.HomePage.PRODUCT_NOT_ADDED_ERROR), "", false, false, new Date(), userService.getCurrentUser().getName());
				return messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.DIVISION_ERROR, null,
						productModelToValidate.getCode());
			}
			else if (JnjOrderTypesEnum.ZHOR.equals(getCartService().getSessionCart().getOrderType())
					&& !jnJGTProductService.validateProductMfgForHouseOrder((JnJProductModel) productModelToValidate))
			{
				return messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.DIVISION_ERROR, null,
						productModelToValidate.getCode());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("----->JnjGTCartFacadeImpl - validateProduct(" + productModelToValidate.getCode()
					+ ") Time taken for Validating ProductModel: " + (new Date().getTime() - startTime.getTime()));
		}
		return null;
	}


	@Override
	public void restoreCartForCurrentUser(final String srcSystemId)
	{
		jnjGTCartService.restoreCartForCurrentUser(srcSystemId);
	}



	/**
	 * This method set the shipping methods depending upon the Order Type.
	 */

	@Override
	public JnjGTCartData setShippingMethodOnOrderType(final JnjGTCartData cartData)
	{

		final List<OrderEntryData> entries = cartData.getEntries();
		//Getting the Order Type.
		final String orderType = cartData.getOrderType();
		if(null != entries){
			for (final OrderEntryData entry : entries)
			{
				if (entry instanceof JnjGTOrderEntryData)
				{
					final List<JnjGTShippingMethodModel> requiredShippingMethods = new ArrayList<JnjGTShippingMethodModel>();
					final JnjGTOrderEntryData jnjOrderEntryData = (JnjGTOrderEntryData) entry;

					final JnjGTProductData product = (JnjGTProductData) entry.getProduct();
					final String defaultRoute = jnjOrderEntryData.getDefaultRoute();
					//Checking whether the Order Is of ZOR Type.
					if (orderType.equalsIgnoreCase(JnjOrderTypesEnum.ZOR.getCode()))
					{
						getShippingMethodForZOR(requiredShippingMethods, product, defaultRoute);
					}
					else if (orderType.equalsIgnoreCase(JnjOrderTypesEnum.ZNC.getCode()))
					{
						getShippingMethodsForZNC(requiredShippingMethods, product, defaultRoute);
					}

					else if (orderType.equalsIgnoreCase(JnjOrderTypesEnum.ZKB.getCode()))
					{
						getShippingMethodsForZKB(requiredShippingMethods, defaultRoute);
					}

					else if (orderType.equalsIgnoreCase(JnjOrderTypesEnum.ZEX.getCode()))
					{
						final JnjGTShippingMethodModel stanadrdshippingModel = jnjGTCartService.getShippingMethodByRoute(Config
								.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
						requiredShippingMethods.add(stanadrdshippingModel);
					}

					String zipCode=null;
					if( cartData.getDeliveryAddress()!=null)
					{
					zipCode = cartData.getDeliveryAddress().getPostalCode();
					}				
					if (zipCode != null)
					{
						final JnjGTEarlyZipCodesModel jnjGTEarlyZipCodesModel = new JnjGTEarlyZipCodesModel();
						jnjGTEarlyZipCodesModel.setZipCode(zipCode);
						jnjGTEarlyZipCodesModel.setCourier(Config.getParameter(Jnjb2bCoreConstants.Cart.EARLY_ZIP_CODE_FEDEX));
						final List<JnjGTEarlyZipCodesModel> jnjGTEarlyZipCodesList = jnjGTCartService
								.getJnjGTEarlyZipCodesModelByZipCode(jnjGTEarlyZipCodesModel);
						if (CollectionUtils.isEmpty(jnjGTEarlyZipCodesList))
						{
							final JnjGTShippingMethodModel stanadrdshippingModelFDEFO = jnjGTCartService.getShippingMethodByRoute(Config
									.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEFO));
							requiredShippingMethods.remove(stanadrdshippingModelFDEFO);
						}
						final JnjGTEarlyZipCodesModel jnjGTEarlyZipCodesModelUPS = new JnjGTEarlyZipCodesModel();
						jnjGTEarlyZipCodesModelUPS.setZipCode(zipCode);
						jnjGTEarlyZipCodesModel.setCourier(Config.getParameter(Jnjb2bCoreConstants.Cart.EARLY_ZIP_CODE_UPS));
						final List<JnjGTEarlyZipCodesModel> jnjGTEarlyZipCodesUpsList = jnjGTCartService
								.getJnjGTEarlyZipCodesModelByZipCode(jnjGTEarlyZipCodesModelUPS);
						if (CollectionUtils.isEmpty(jnjGTEarlyZipCodesUpsList))
						{
							final JnjGTShippingMethodModel stanadrdshippingModelFDEFO = jnjGTCartService.getShippingMethodByRoute(Config
									.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDEFO));
							requiredShippingMethods.remove(stanadrdshippingModelFDEFO);
						}
					}
					final List<JnjGTShippingMethodData> shippingmethods = getShippingMethodData(requiredShippingMethods);
					jnjOrderEntryData.setShippingMethodsList(shippingmethods);
				}
				cartData.setEntries(entries);
			}
		}
		return cartData;
	}

	@Override
	public boolean canValidateCart()
	{
		final int orderValidationRange = Config.getInt(Jnjb2bCoreConstants.ORDER_VALIDATION_LINES_COUNT, 30);
		boolean canValidate = true;
		final CartModel sessionCart = getCartService().getSessionCart();
		if (null != sessionCart.getEntries() && sessionCart.getEntries().size() > orderValidationRange)
		{
			canValidate = false;
		}
		return canValidate;
	}

	/**
	 * This method set the shipping methods depending upon the Order Type ZKB
	 * 
	 * @param requiredShippingMethods
	 * @param defaultRoute
	 */
	protected List<JnjGTShippingMethodModel> getShippingMethodsForZKB(final List<JnjGTShippingMethodModel> requiredShippingMethods,
			final String defaultRoute)
	{
		final Calendar date = Calendar.getInstance();
		if ((date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY))
		{
			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS_ZKB.contains(defaultRoute))
			{
				COMPATIBLE_SHIPPING_METHODS_ZKB.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDESA));
				requiredShippingMethods.addAll(jnjGTCartService.getRequiredShippingMethod(COMPATIBLE_SHIPPING_METHODS_ZKB));
			}
			else
			{
				final List<String> complatibleShippingMethod = new ArrayList<String>();
				complatibleShippingMethod.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				COMPATIBLE_SHIPPING_METHODS_ZKB.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHOD_FDESA));
				complatibleShippingMethod.addAll(COMPATIBLE_SHIPPING_METHODS_ZKB);
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(0, complatibleShippingMethod));
			}
		}
		else
		{
			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS_ZKB.contains(defaultRoute))
			{
				requiredShippingMethods.addAll(jnjGTCartService.getRequiredShippingMethod(COMPATIBLE_SHIPPING_METHODS_ZKB));
			}
			else
			{
				final List<String> complatibleShippingMethod = new ArrayList<String>();
				complatibleShippingMethod.addAll(COMPATIBLE_SHIPPING_METHODS_ZKB);
				complatibleShippingMethod.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(0, complatibleShippingMethod));
			}
		}
		return requiredShippingMethods;

	}

	/**
	 * This method set the shipping methods depending upon the Order Type ZNC
	 * 
	 * @param requiredShippingMethods
	 * @param product
	 * @param defaultRoute
	 */
	protected List<JnjGTShippingMethodModel> getShippingMethodsForZNC(final List<JnjGTShippingMethodModel> requiredShippingMethods,
			final JnjGTProductData product, final String defaultRoute)
	{
		//Manipulating ZNC  except for divisions ‘AS’, ‘CD’, ‘VX’, ‘C4’
		if (!UNCOMPATIBLE_DIVISION_CODES_ZNC.contains(product.getSalesOrgCode()))
		{
			//So if the default route is one of the – FDEFO, FDEPO, FDESO, FDE2D
			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS.contains(defaultRoute))
			{
				final JnjGTShippingMethodModel shippingModel = jnjGTCartService.getShippingMethodByRoute(defaultRoute);
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(shippingModel.getRank(),
						COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS));
			}
			//So if the default route for a product does not match any of the for ZOR shipping methods then show Standard and the 4 options.
			else
			{
				final List<String> complatibleShippingMethod = new ArrayList<String>();
				complatibleShippingMethod.addAll(COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS);
				complatibleShippingMethod.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(0, complatibleShippingMethod));
			}
		}
		//Manipulating ZOR  for divisions ‘CD’, ‘VX’
		else if (product.getSalesOrgCode().equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_OCD))
				|| product.getSalesOrgCode().equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_VX)))
		{
			//Only three options are available – FDEFO, FDEPO, FDESO
			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS.contains(defaultRoute))
			{
				final JnjGTShippingMethodModel shippingModel = jnjGTCartService.getShippingMethodByRoute(defaultRoute);
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(shippingModel.getRank(),
						COMPATIBLE_SHIPPING_METHODS));

			}
			//If the default route for the product does not matches any of these 3, then show Standard and the 3 options.
			else
			{
				final List<String> complatibleShippingMethod = new ArrayList<String>();
				complatibleShippingMethod.addAll(COMPATIBLE_SHIPPING_METHODS);
				complatibleShippingMethod.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(0, complatibleShippingMethod));
			}

		}
		//Manipulating ZOR  for divisions ‘C4'
		//If SAP sends one of the four early delivery routes, then the system displays it as is
		else if (product.getSalesOrgCode().equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_C4)))
		{

			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS.contains(defaultRoute))
			{
				final JnjGTShippingMethodModel shippingModel = jnjGTCartService.getShippingMethodByRoute(defaultRoute);
				requiredShippingMethods.add(shippingModel);
			}
			else
			{
				final JnjGTShippingMethodModel standardshippingModel = jnjGTCartService.getShippingMethodByRoute(Config
						.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.add(standardshippingModel);
			}
		}

		//Manipulating ZNC  for divisions ‘ASP’
		else if (product.getSalesOrgCode().equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_ASP)))
		{
			//Only three options are available – FDEFO, FDEPO, FDESO
			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS_ASP.contains(defaultRoute))
			{
				final JnjGTShippingMethodModel shippingModel = jnjGTCartService.getShippingMethodByRoute(defaultRoute);
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(shippingModel.getRank(),
						COMPATIBLE_SHIPPING_METHODS_ASP));
			}
			//If the default route for the product does not matches any of these 3, then show Standard and the 3 options.
			else
			{
				final List<String> complatibleShippingMethod = new ArrayList<String>();
				complatibleShippingMethod.addAll(COMPATIBLE_SHIPPING_METHODS_ASP);
				complatibleShippingMethod.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(0, complatibleShippingMethod));
			}

		}
		return requiredShippingMethods;
	}

	/**
	 * This method set the shipping methods depending upon the Order Type ZOR
	 * 
	 * @param requiredShippingMethods
	 * @param product
	 * @param defaultRoute
	 * @return
	 */
	protected List<JnjGTShippingMethodModel> getShippingMethodForZOR(final List<JnjGTShippingMethodModel> requiredShippingMethods,
			final JnjGTProductData product, final String defaultRoute)
	{
		//Manipulating ZOR  except for divisions ‘CD’, ‘VX’, ‘C4’
		if (!UNCOMPATIBLE_DIVISION_CODES_ZOR.contains(product.getSalesOrgCode()))
		{

			//So if the default route is one of the – FDEFO, FDEPO, FDESO, FDE2D
			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS.contains(defaultRoute))
			{
				final JnjGTShippingMethodModel shippingModel = jnjGTCartService.getShippingMethodByRoute(defaultRoute);
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(shippingModel.getRank(),
						COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS));
			}
			//So if the default route for a product does not match any of the for ZOR shipping methods then show Standard and the 4 options.
			else
			{

				final List<String> complatibleShippingMethod = new ArrayList<String>();
				complatibleShippingMethod.addAll(COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS);
				complatibleShippingMethod.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(0, complatibleShippingMethod));
			}

		}
		//Manipulating ZOR  for divisions ‘CD’, ‘VX’
		else if (product.getSalesOrgCode().equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_OCD))
				|| product.getSalesOrgCode().equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_VX)))
		{
			//Only three options are available – FDEFO, FDEPO, FDESO
			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS.contains(defaultRoute))
			{
				final JnjGTShippingMethodModel shippingModel = jnjGTCartService.getShippingMethodByRoute(defaultRoute);
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(shippingModel.getRank(),
						COMPATIBLE_SHIPPING_METHODS));

			}
			//If the default route for the product does not matches any of these 3, then show Standard and the 3 options.
			else
			{
				final List<String> complatibleShippingMethod = new ArrayList<String>();
				complatibleShippingMethod.addAll(COMPATIBLE_SHIPPING_METHODS);
				complatibleShippingMethod.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(0, complatibleShippingMethod));
			}

		}
		//Manipulating ZOR  for divisions ‘C4'
		//If SAP sends one of the four early delivery routes, then the system displays it as is
		else if (product.getSalesOrgCode().equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_C4)))
		{

			if (defaultRoute != null && COMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS.contains(defaultRoute))
			{
				final List<String> complatibleShippingMethod = new ArrayList<String>();
				final JnjGTShippingMethodModel shippingModel = jnjGTCartService.getShippingMethodByRoute(defaultRoute);
				//Start JJEPIC-906 
				requiredShippingMethods.add(shippingModel);
				complatibleShippingMethod.addAll(COMPATIBLE_SHIPPING_METHODS);
				//complatibleShippingMethod.add(Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.addAll(jnjGTCartService.getFasterShippingMethods(0, complatibleShippingMethod));
				//End JJEPIC-906 		
			}
			else
			{
				final JnjGTShippingMethodModel standardshippingModel = jnjGTCartService.getShippingMethodByRoute(Config
						.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
				requiredShippingMethods.add(standardshippingModel);
			}
		}

		return requiredShippingMethods;
	}





	protected List<JnjGTShippingMethodData> getShippingMethodData(final List<JnjGTShippingMethodModel> requiredShippingMethods)
	{

		final List<JnjGTShippingMethodData> shippingMethodDataList = new ArrayList<JnjGTShippingMethodData>();
		for (final JnjGTShippingMethodModel jnjGTShippingMethodModel : requiredShippingMethods)
		{
			final JnjGTShippingMethodData JnjGTShippingMethodData = new JnjGTShippingMethodData();
			jnjGTShippingMethodPopulator.populate(jnjGTShippingMethodModel, JnjGTShippingMethodData);
			shippingMethodDataList.add(JnjGTShippingMethodData);
		}
		return shippingMethodDataList;
	}


	@Override
	public Map<String, String> getShippingRouteAndNameMap()
	{
		final Set<JnjGTShippingMethodData> shippingMethods = getAllShippingMethods();
		final Map<String, String> shippingRouteNameMap = new HashMap<String, String>();
		for (final JnjGTShippingMethodData data : shippingMethods)
		{
			shippingRouteNameMap.put(data.getRoute(), data.getDispName());
		}
		return shippingRouteNameMap;
	}


	@Override
	public Set<JnjGTShippingMethodData> getAllShippingMethods()
	{
		final List<JnjGTShippingMethodModel> shippingMethods = jnjGTCartService.getAllShippingMethods();
		final Set<JnjGTShippingMethodData> shippingMethodDatas = new HashSet<JnjGTShippingMethodData>(shippingMethods.size());
		JnjGTShippingMethodData data = null;
		for (final JnjGTShippingMethodModel source : shippingMethods)
		{
			data = new JnjGTShippingMethodData();
			jnjGTShippingMethodPopulator.populate(source, data);
			shippingMethodDatas.add(data);
		}
		return shippingMethodDatas;
	}


	@Override
	public void changeOrderType(final String orderType)
	{
		jnjGTCartService.changeOrderType(orderType);
	}

	@Override
	public boolean saveSurgeryInfo(final JnjGTSurgeryInfoData infoForm)
	{
		return jnjGTCartService.saveSurgeryInfo(infoForm);
	}

	@Override
	public SurgeryInfoData getSavedSurgeryInfo()
	{
		SurgeryInfoData surgeryInfo = null;
		final CartModel sessionCart = getCartService().getSessionCart();
		if (null != sessionCart.getSurgeryInfo())
		{
			surgeryInfo = new SurgeryInfoData();
			jnjGTSurgeryPopulator.populate(sessionCart.getSurgeryInfo(), surgeryInfo);
		}
		else
		{
			surgeryInfo = setDefaultSurgeryInfo();
		}
		return surgeryInfo;
	}

	public SurgeryInfoData setDefaultSurgeryInfo()
	{
		final SurgeryInfoData surgeryInfoData = new SurgeryInfoData();
		surgeryInfoData.setSurgerySpecialty("X");
		surgeryInfoData.setOrthobiologics("X");
		surgeryInfoData.setInterbody("X");
		surgeryInfoData.setInterbodyFusion("X");
		surgeryInfoData.setLevelsInstrumented("X");
		surgeryInfoData.setCas("X");
		surgeryInfoData.setCaseDate(null);
		surgeryInfoData.setCaseNumber("X");
		surgeryInfoData.setPathology("X");
		surgeryInfoData.setSurgicalApproach("X");
		surgeryInfoData.setZone("X");
		return surgeryInfoData;
	}


	@Override
	public List<String> getSurgeryValues(final String id, final String key)
	{
		final List<String> lstConfig = new ArrayList<String>();
		for (final JnjConfigModel jnjConfigModel : jnjConfigService.getConfigModelsByIdAndKey(id, key))
		{
			lstConfig.add(jnjConfigModel.getKey());
		}
		return lstConfig;
	}

	@Override
	public String getOrderType()
	{
		return jnjGTCartService.getOrderType();
	}

	@Override
	public Map<String, String> getReturunReasonCodes(final String returnReasonKey)
	{
		String returnReasonsKey = returnReasonKey;
		if (jnjGTB2BUnitService.isCSCUser() || jnjGTB2BUnitService.isCustomerDistributor())
		{
			returnReasonsKey = Jnjb2bCoreConstants.Cart.REASON_CODE_RETURNS_CSC_AND_DIST;
		}
		return getReasonCode(returnReasonsKey);
	}

	@Override
	public Map<String, String> getReasonCode(final String id)
	{
		final Map<String, String> outputMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> configModels = jnjConfigService.getConfigModelsByIdAndKey(id, null);
		for (final JnjConfigModel jnjConfigModel : configModels)
		{
			try
			{
				outputMap.put(jnjConfigModel.getKey(),
						messageService.getMessageForCode(jnjConfigModel.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{

				LOGGER.error("Unable to render message text for message code : " + jnjConfigModel.getKey(), exception);
			}
		}
		return outputMap;
	}

	protected List<String> getValuesBasedOnId(final String id)
	{
		return jnjConfigService.getConfigValuesById(id, Jnjb2bCoreConstants.SYMBOl_COMMA);
	}

	@Override
	public String getPathForView(final String page, String orderType)
	{
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) getUserService().getCurrentUser();
		
		//Changes for default order type AAOL-4660
		Set<String> availableOrderTypes = jnjGTUnitFacade.getOrderTypesForAccount();
		String defaultOrderType = null;
		boolean changeOrderFlag = false;
		//Changes for issue AAOL-6279 - Validate if linked replenishment order created
		boolean initiateReplenishFlag = false;
		final Boolean changeOrderStatus = sessionService.getAttribute("changeOrderFlag");
		final Boolean initiateReplenishStatus = sessionService.getAttribute("initiateReplenishFlag");
		String sessionOrderType = getOrderType();
		
		if(changeOrderStatus!=null){
			changeOrderFlag = changeOrderStatus;
		}
		if(initiateReplenishStatus!=null){
			initiateReplenishFlag = initiateReplenishStatus;
		}
		
		if(currentUser.getDefaultOrderType()!=null){
			defaultOrderType = currentUser.getDefaultOrderType();
		}
		
		if(!availableOrderTypes.isEmpty()){
			if(!changeOrderFlag && !initiateReplenishFlag){
				if(availableOrderTypes.contains(defaultOrderType)){
					orderType = defaultOrderType;
				}
				else if(availableOrderTypes.contains(JnjOrderTypesEnum.ZOR)){
					orderType =(JnjOrderTypesEnum.ZOR).toString();
				}
				else{
					orderType = sessionOrderType;
				}
			}else{
				orderType = sessionOrderType;
			}
			
		}else{
			orderType = sessionOrderType;
		}
		final String siteDir = CartViewMapping.cmsSiteCartDirMap.get(currentSite);
		final String dirForOrderType = CartViewMapping.orderTypeCartDirMap.get(orderType);
		return new StringBuilder(CartViewMapping.CartPageBaseDirPath).append(siteDir).append(dirForOrderType).append(page)
				.toString();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.cart.JnjGTCartFacade#updatePONumberForOrderEntry(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updatePONumberForOrderEntry(final String poNumber, final int entryNumber)
	{
		return jnjGTCartService.updatePONumForEntry(poNumber, entryNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.cart.JnjGTCartFacade#updateLotNumberForOrderEntry(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateLotNumberForOrderEntry(final String lotNumber, final String pcode, final int entryNumber)
	{
		//FIX GTR-1768
		return jnjGTCartService.updateLotNumForEntry(lotNumber, pcode, entryNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.cart.JnjGTCartFacade#updateInvoiceNumberForOrderEntry(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateInvoiceNumberForOrderEntry(final String invoiceNumber, final int entryNumber)
	{
		return jnjGTCartService.updateInvoiceNumForEntry(invoiceNumber, entryNumber);
	}


	@Override
	public Set<String> getSalesRepPrimaryUCN()
	{
		Set<String> salesRepPrimaryUCN = null;
		final List<AbstractOrderEntryModel> entries = getCartService().getSessionCart().getEntries();
		if (CollectionUtils.isNotEmpty(entries))
		{
			final JnJProductModel firstProductInCart = (JnJProductModel) entries.get(0).getProduct();
			if (StringUtils.isNotEmpty(firstProductInCart.getFrancMjrPrdGrpCd()))
			{
				salesRepPrimaryUCN = jnjGTCartService.getSalesRepUCN(firstProductInCart.getFrancMjrPrdGrpCd(), true);
			}
		}
		return salesRepPrimaryUCN;
	}

	@Override
	public boolean updateSalesRepUCN(final String salesRepUCN, final String specialStockPartner)
	{
		return jnjGTCartService.updateSalesRepUCN(salesRepUCN, specialStockPartner);
	}

	@Override
	public Set<String> getSpineOrderHeaderUCN()
	{
		return jnjGTCartService.getSpineOrderHeaderUCN();
	}

	@Override
	public boolean updateSpecialStockPartner(final String specialStockPartner, final int entryNumber)
	{
		return jnjGTCartService.updateSpecialStockPartner(specialStockPartner, entryNumber);
	}

	@Override
	public boolean updateThirdPartyFlag(final Boolean thirdPartyFlag)
	{
		return jnjGTCartService.updateThirdPartyFlag(thirdPartyFlag);
	}

	@Override
	public JnjGTDivisonData getPopulatedDivisionData()
	{
		return jnjGTCustomerSevice.getPopulatedDivisionData(null);
	}

	@Override
	public boolean updateCustomerPONumber(final String customerPO)
	{
		return jnjGTCartService.updateCustomerPONumber(customerPO);
	}

	@Override
	public boolean updateOverridenPrice(final String reasonCode, final String approver, final double overridePrice,
			final int entryNumber)
	{
		return jnjGTCartService.updateOverridenPrice(reasonCode, approver, overridePrice, entryNumber);
	}


	@Override
	public boolean updateCordisHouseAccount(final String cordisHouseAccount)
	{
		return jnjGTCartService.updateCordisHouseAccount(cordisHouseAccount);
	}

	@Override
	public String updateSurgeon(final String surgeonId, final String selectSurgeonName, final String hospitalId)
	{
		return jnjGTCartService.updateSurgeon(surgeonId,selectSurgeonName,hospitalId);
	}

	@Override
	public boolean updateFreightCost(final double freightCost)
	{
		return jnjGTCartService.updateFreightCost(freightCost);
	}


	@Override
	public boolean isEligibleForOrderReturn()
	{
		return jnjGTCartService.isEligibleForOrderReturn();
	}

	/**
	 * Method to Determine Default Order Type For Use
	 * 
	 * @return JnjOrderTypesEnum
	 */
	@Override
	public JnjOrderTypesEnum getDefaultOrderType()
	{
		return jnjGTCartService.getDefaultOrderType();

	}


	@Override
	public void createMediaModelForCart(final File file, final String name) throws BusinessException
	{
		jnjGTCartService.createMediaModelForCart(file, name);

	}

	@Override
	public String savePayMetricsResponse(final HttpServletRequest request, final boolean isRememberSet)
			throws CMSItemNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeyException, XMLStreamException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bFacadesConstants.Logging.SAVE_CREDIT_CARD_INFO + Logging.HYPHEN + "savePayMetricsResponse()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		String status = null;
		CartModel cartModel = null;
		JnjGTCreditCardModel jnjGTCreditCardModel = null;
		JnJB2bCustomerModel JnJB2bCustomerModel = null;
		// we need to start the decoding
		// assign the response from the query string
		String pmResponse = request.getParameter(Config.getParameter(Jnjb2bCoreConstants.PayMetrics.PM_RESPONSE));
		// assign the signature from the query string
		String Signature = request.getParameter(Config.getParameter(Jnjb2bCoreConstants.PayMetrics.SIGNATURE));
		// Convert from modified base64 to normal base 64
		pmResponse = pmResponse.replaceAll("-", "+");
		pmResponse = pmResponse.replaceAll("_", "/");
		Signature = Signature.replaceAll("-", "+");
		Signature = Signature.replaceAll("_", "/");
		// Base 64 decode the response.
		//final sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		pmResponse = new String(Base64.getDecoder().decode(pmResponse));
		// verify the signature is correct on the xml.
		// setup the mac preshared key
		final javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(Config.getParameter(
				Jnjb2bCoreConstants.PayMetrics.SECRET_KEY_SPEC).getBytes(),
				Config.getParameter(Jnjb2bCoreConstants.PayMetrics.SECRET_KEY_SPEC_ALGORITHM));
		// setup the MAC Hashing object
		final javax.crypto.Mac mac = javax.crypto.Mac.getInstance(Config
				.getParameter(Jnjb2bCoreConstants.PayMetrics.SECRET_KEY_SPEC_ALGORITHM));
		// initialize the mac object with the preshared key.
		mac.init(keySpec);
		// setup the base64 encoding object.
		//final sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		// gerate the signature
		final String ourSig = Base64.getEncoder().encodeToString(mac.doFinal(pmResponse.getBytes()));
		if (0 == Signature.compareTo(ourSig))
		{
			final InputStream inputStream = new ByteArrayInputStream(pmResponse.getBytes());

			String elementValue = null;
			final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

			final XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);
			int eventType = xmlStreamReader.getEventType();
			while (xmlStreamReader.hasNext())
			{
				switch (eventType)
				{
					case XMLStreamConstants.START_ELEMENT:
						if (Jnjb2bFacadesConstants.Paymetric.PAYMETRIC_RESPONSE.equals(xmlStreamReader.getLocalName()))
						{
							cartModel = getCartService().getSessionCart();
							jnjGTCreditCardModel = new JnjGTCreditCardModel();
							JnJB2bCustomerModel = jnjGTCustomerSevice.getCurrentUser();
							jnjGTCreditCardModel.setUser(JnJB2bCustomerModel);
							jnjGTCreditCardModel.setCcOwner(JnJB2bCustomerModel.getUid());
							if (isRememberSet)
							{
								jnjGTCreditCardModel.setIsRemember(Boolean.TRUE);
							}
						}
						break;

					case XMLStreamConstants.CHARACTERS:
						elementValue = xmlStreamReader.getText();
						break;

					case XMLStreamConstants.END_ELEMENT:
						if (Jnjb2bFacadesConstants.Paymetric.CC_TOKEN.equalsIgnoreCase(xmlStreamReader.getLocalName()))
						{
							final String[] cctoken = StringUtils.split(elementValue, '-');
							jnjGTCreditCardModel.setCreditCardtoken(elementValue);
							jnjGTCreditCardModel.setNumber(cctoken[1]);
							jnjGTCreditCardModel.setCode(jnjGTCreditCardModel.getCcOwner().concat(Jnjb2bFacadesConstants.PIPE_STRING)
									.concat(cctoken[1]));
						}
						else if (Jnjb2bFacadesConstants.Paymetric.MONTH.equalsIgnoreCase(xmlStreamReader.getLocalName()))
						{
							jnjGTCreditCardModel.setValidToMonth(elementValue);
						}
						else if (Jnjb2bFacadesConstants.Paymetric.YEAR.equalsIgnoreCase(xmlStreamReader.getLocalName()))
						{
							jnjGTCreditCardModel.setValidToYear(elementValue);
						}
						else if (Jnjb2bFacadesConstants.Paymetric.CC_TYPE.equalsIgnoreCase(xmlStreamReader.getLocalName()))
						{
							final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey("CreditCardType",
									elementValue);
							if (CollectionUtils.isNotEmpty(jnjConfigModels))
							{
								final String creditCardType = jnjConfigModels.get(0).getValue();
								jnjGTCreditCardModel.setType(CreditCardType.valueOf(creditCardType));
							}
							else
							{
								jnjGTCreditCardModel.setType(CreditCardType.CCVISA);
							}
						}
						break;
				}
				eventType = xmlStreamReader.next();
			}
			cartModel.setPaymentInfo(jnjGTCreditCardModel);
			jnjModelService.save(cartModel);
			xmlStreamReader.close();
			inputStream.close();
			status = "Success";
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bFacadesConstants.Logging.SAVE_CREDIT_CARD_INFO + Logging.HYPHEN + "savePayMetricsResponse()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return status;
	}

	@Override
	public boolean validateProductDivision()
	{
		boolean differentDivision = false;
		final CartModel cartModel = getCartService().getSessionCart();
		String divOfFirstProduct = null;
		if (cartModel.getEntries() != null && !cartModel.getEntries().isEmpty())
		{
			divOfFirstProduct = getProduct(cartModel.getEntries().get(0).getProduct()).getSalesOrgCode();

			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				//AAOL-6390 start
				if((getProduct(entry.getProduct()).getSalesOrgCode()) != null){
					if (!divOfFirstProduct.equalsIgnoreCase(getProduct(entry.getProduct()).getSalesOrgCode()))
					{
						differentDivision = true;
					}
				}//AAOL-6390 end
			}
		}
		return differentDivision;
	}

	public JnJProductModel getProduct(final ProductModel productModel)
	{
		JnJProductModel JnJProductModel = null;
		if (productModel instanceof JnjGTVariantProductModel)
		{
			final JnjGTVariantProductModel variantProduct = (JnjGTVariantProductModel) productModel;
			final JnJProductModel modProduct = (JnJProductModel) variantProduct.getBaseProduct();
			JnJProductModel = modProduct.getMaterialBaseProduct();

			// if base product is null
			if (JnJProductModel == null)
			{
				JnJProductModel = modProduct;
			}
		}
		else if (productModel instanceof JnJProductModel)
		{
			JnJProductModel = (JnJProductModel) productModel;
		}
		return JnJProductModel;
	}


	@Override
	public boolean preValidateCartForReturnOrder()
	{
		return jnjGTCartService.preSAPReturnCartValidation();
	}

	@Override
	public boolean cartOrderConfirmationEmail()
	{
		eventService.publishEvent(initializeEvent(new JnjGTCartOrderConfirmationEvent()));
		return true;
	}

	protected JnjGTCartOrderConfirmationEvent initializeEvent(final JnjGTCartOrderConfirmationEvent event)
	{
		//final JnjGTOrderData orderData = null;
		LOGGER.debug("Entered the initializeEvent() method");
		//event.setFirstName(getCurrentNACustomer().getFirstName());
		//event.setLastName(getCurrentNACustomer().getLastName());
		//event.setOrderStatus(orderData.getOrderStatus());
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer((CustomerModel) userService.getCurrentUser());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		LOGGER.debug("Exiting the initializeEvent() method");
		return event;
	}

	@Override
	public JnjGTOrderReturnResponseData returnOrderSAPValidation() throws SystemException, IntegrationException, BusinessException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		return jnjGTOrderReturnMapper.mapOrderReturnRequestResponse(cartModel);
	}

	@Override
	public JnjGTGetPriceQuoteResponseData quoteSAPValidation() throws SystemException, IntegrationException, ParseException,
			BusinessException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		return jnjGTGetPriceQuoteMapper.mapGetPriceQuoteRequestResponse(cartModel);
	}

	@Override
	public void initiateReplenishForDelivered(final String orderNum) throws BusinessException
	{
		jnjGTCartService.initiateReplenishForDelivered(orderNum);
	}

	@Override
	public void clearDropShipPurchaseOrderNum()
	{
		jnjGTCartService.clearDropShipPurchaseOrderNum();
	}

	/*@Override
	public boolean updateCartModelWithSelectedAttributes(final SelectedAttributesForm selectedAttributesForm)
	{
		return jnjGTCartService.updateCartModelWithSelectedAttributes(selectedAttributesForm);
	}*/


	/*@Override
	public String downloadCartInRealTime(final String orderCode)
	{
		return jnjAmazonUploadIntFacade.uploadInstantly(b2bOrderService.getOrderForCode(orderCode));
	}*/

	@Override
	public boolean updateShippingMethod(final String route, final int entryNumber)
	{
		return jnjGTCartService.updateShippingMethod(route, entryNumber);
	}

	@Override
	public AddressData resetShippingAddress()
	{
		final JnjGTAddressData shippingAddData = new JnjGTAddressData();
		final AddressModel shippingAddress = jnjGTB2BUnitService.getShippingAddress(null);
		final CartModel cartModel = getCartService().getSessionCart();
		cartModel.setDeliveryAddress(shippingAddress);
		cartModel.setDropShipAccount(null);
		if (jnjGTCartService.saveCartModel(cartModel, false))
		{
			jnjGTAddressPopulator.populate(shippingAddress, shippingAddData);
		}
		return shippingAddData;
	}

	@Override
	public JnjGTUpdatePriceData updatePriceForEntry(final int entryNumber)
	{
		return jnjGTCartService.updatePriceForEntry(entryNumber);
	}

	@Override
	public JnjGTUpdatePriceData getCartSubTotal()
	{
		return jnjGTCartService.getCartSubTotal();
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
	public void createCartFromOrder(final String orderId)
	{
		createCartFromOrder(orderId, null);
	}

	@Override
	public void createCartFromOrder(final String orderId, final JnjGTProductData productData)
	{
		jnjGTCartService.createCartFromOrder(orderId, productData);
		jnjGTCartService.calculateSessionCart();
	}

	@Override
	public Map<String, String> fileConverter(final MultipartFile homepagefile)
	{
		// Call For Excel file and read its content so that we can save the same in hybris data base.
		final Map<String, String> productQunatityMap = new LinkedHashMap<String, String>();
		if (null != homepagefile)
		{

			try
			{

				final FileInputStream fileInputStream = (FileInputStream) homepagefile.getInputStream();

				//Get the workbook instance for XLS file
				final HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

				//Get first sheet from the workbook
				final HSSFSheet sheet = workbook.getSheetAt(0);
				processSheet(productQunatityMap, sheet);
				fileInputStream.close();
			}
			catch (final FileNotFoundException exception)
			{
				LOGGER.error("File not found", exception);

			}
			catch (final IOException exception)
			{
				LOGGER.error("IO exception", exception);
				return new HashMap<String, String>();
			}
			catch (final ClassCastException exception)
			{
				LOGGER.error("Class Cast exception", exception);
				return new HashMap<String, String>();
			}

		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("File is null , file is :" + homepagefile);
			}
		}
		return productQunatityMap;
	}

	/**
	 * @param productQunatityMap
	 * @param sheet
	 */
	protected void processSheet(final Map<String, String> productQunatityMap, final HSSFSheet sheet)
	{
		//Iterate through each rows from first sheet
		final Iterator<Row> rowIterator = sheet.iterator();
		final int startFrom = Config.getInt(UPLOAD_TEMPLATE_START_FROM, 2);
		int counter = 0;
		while (rowIterator.hasNext())
		{
			final Row row = rowIterator.next();
			if (counter >= startFrom)
			{
				//For each row, iterate through each columns
				final Iterator<Cell> cellIterator = row.cellIterator();
				int cellCounter = 0;
				String key = "";
				int value = 0;
				while (cellIterator.hasNext())
				{
					final Cell cell = cellIterator.next();

					switch (cell.getCellType())
					{
						case BOOLEAN:
							LOGGER.warn("Product code and quantity both should not be in cell with format  : "
									+ cell.getBooleanCellValue());
							break;
						case NUMERIC:
							if (cellCounter == 1)
							{
								value = Double.valueOf(cell.getNumericCellValue()).intValue();
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug("Row no , cell no-" + counter + "," + cellCounter + " Quantity is :" + value);
								}
							}
							else if (cellCounter == 0)
							{
								key = String.valueOf(Double.valueOf(cell.getNumericCellValue()).doubleValue());
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug("Row no , cell no-" + counter + "," + cellCounter + " product Code : " + key);
								}
							}
							else
							{
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug("Row no , cell no-" + counter + "," + cellCounter
											+ " Quantity should be in cell with format " + CellType.NUMERIC);
								}
							}

							break;
						case STRING:
							if (cellCounter == 0)
							{
								key = cell.getStringCellValue();
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug("Row no , cell no-" + counter + "," + cellCounter + " product Code : " + key);
								}
							}
							else
							{
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug("Row no , cell no-" + counter + "," + cellCounter
											+ " product code should be in cell with format " + CellType.STRING);
								}
							}

							break;
					}
					cellCounter++;
				}
				if (StringUtils.isNotEmpty(key))
				{
					if (value != 0)
					{
						
						if(productQunatityMap.containsKey(key)){
							int count = Integer.valueOf(productQunatityMap.get(key));
							int actualQty = count + Integer.valueOf(value);
							productQunatityMap.put(key, String.valueOf(actualQty));
						}
						else if(StringUtils.isNotBlank(key)){
							productQunatityMap.put(key, String.valueOf(value));
						}
						
					}
					else
					{
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug("Quantity is not available hence adding the product with default quantity.");
						}
						productQunatityMap.put(key, "0");
					}
				}
			}
			counter++;
		}
	}

	/**
	 * 
	 * @param cartModificationData
	 * @param code
	 * @param addToCartStatus
	 */
	protected void updateCartModificationData(final CartModificationData cartModificationData, final String code,
			final String addToCartStatus)
	{
		final OrderEntryData entryData = new OrderEntryData();
		final ProductData productData = new JnjGTProductData();
		productData.setCode(code);
		entryData.setProduct(productData);
		cartModificationData.setEntry(entryData);
		cartModificationData.setError(true);
		cartModificationData.setStatusCode(addToCartStatus);
	}

	@Override
	public void removeSessionCart()
	{
		b2bCartService.removeSessionCart();
	}

	@Override
	public JnjCartModificationData multiAddToCartGT(final Map<String, String> productCodes, final boolean ignoreUPC)
	{

		JnjCartModificationData jnjCartModification = null;
		// Set value in session so that product query doesn't include UPC
		/*if (ignoreUPC)
		{
			sessionService.setAttribute(JnjPCMCoreConstants.Cart.IGNORE_UPC, JnjPCMCoreConstants.Cart.IGNORE_UPC_VALUE);
		}*/
		final Map<ProductModel, Long> productQtyMap = new HashMap<ProductModel, Long>(productCodes.size());
		for (final String productCode : productCodes.keySet())
		{
			try
			{
				ProductModel product = jnJGTProductService.getProductByCodeOrEAN(productCode);
				if (null != product)
				{
					if (product instanceof JnJProductModel)
					{
						product = jnJGTProductService.getDeliveryGTIN(product);
					}
					productQtyMap.put(product, Long.valueOf(productCodes.get(productCode)));
				}
				else
				{
					LOGGER.error("Add to Cart - Product Code-" + productCode + "not Found for current catalog");
				}
			}
			catch (final ModelLoadingException itemNotFoundExp)
			{
				LOGGER.error("Add to Cart - Product Code: " + productCode + "not Valid for add to session cart");
			}
		}

		// Reset value in session so that product query includes UPC
		/*if (ignoreUPC)
		{
			sessionService.removeAttribute(JnjPCMCoreConstants.Cart.IGNORE_UPC);
		}*/
		if (!productQtyMap.isEmpty())
		{
			final CartModel cartModel = getCartService().getSessionCart();
			final CommerceCartModification cartModification = jnjGTCartService.addToCartGT(cartModel, productQtyMap);

			final CartModificationData cartModificationData = getCartModificationConverter().convert(cartModification);
			final List<CartModificationData> cartModifications = new ArrayList<CartModificationData>(1);
			cartModifications.add(cartModificationData);
			jnjCartModification = new JnjCartModificationData();
			jnjCartModification.setCartModifications(cartModifications);
			jnjCartModification.setTotalUnitCount(calcTotalUnitCount(cartModel));

		}
		return jnjCartModification;
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
	public OrderEntryData getOrderEntryData(final int entryNumber)
	{
		JnjGTOrderEntryData jnjGTOrderEntryData = null;
		if (entryNumber >= 0)
		{
			// get the current cart model object by using Cart Service
			final CartModel cartModel = getCartService().getSessionCart();
			final AbstractOrderEntryModel abstOrdEntModel = jnjGTCartService.getEntryModelForNumber(cartModel, entryNumber);
			jnjGTOrderEntryData = new JnjGTOrderEntryData();
			jnjGTOrderEntryPopulator.populate(abstOrdEntModel, jnjGTOrderEntryData);
		}
		return jnjGTOrderEntryData;
	}

	@Override
	public boolean addQuoteToCart()
	{
		final CartModel cartModel = getCartService().getSessionCart();
		cartModel.setOrderType(jnjGTCartService.getDefaultOrderType());
		return jnjGTCartService.saveCartModel(cartModel, false);
	}

	@Override
	public void cleanSavedCart()
	{
		final CartModel cartModel = getCartService().getSessionCart();
		if (cartModel != null)
		{
			jnjGTCartService.removeAllEntries(cartModel);
		}
	}

	@Override
	public int getNumberOfCartEntriesInSessionCart()
	{
		if (hasSessionCart())
		{
			final CartModel cartModel = getCartService().getSessionCart();
			return cartModel.getEntries().size();
		}
		else
		{
			return Jnjb2bCoreConstants.NUMBER_ZERO;
		}
	}

	/**
	 * @return the jnjGTAddressPopulator
	 */
	public JnjGTAddressPopulator getJnjGTAddressPopulator() {
		return jnjGTAddressPopulator;
	}

	/**
	 * @return the jnjGTCreateConsOrdMapper
	 */
	public JnjGTCreateConsOrdMapper getJnjGTCreateConsOrdMapper() {
		return jnjGTCreateConsOrdMapper;
	}

	/**
	 * @return the jnjGTSimulateOrderMapper
	 */
	public JnjGTSimulateOrderMapper getJnjGTSimulateOrderMapper() {
		return jnjGTSimulateOrderMapper;
	}

	/**
	 * @return the jnjGTSimulateDeliveredOrderMapper
	 */
	public JnjGTSimulateDeliveredOrderMapper getJnjGTSimulateDeliveredOrderMapper() {
		return jnjGTSimulateDeliveredOrderMapper;
	}
	
	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	/*Added for JJEPIC 720 Start*/
	@Override
	public boolean deleteMediaModelFromCart()
	{
		try
		{
			final CartModel cartModel = getCartService().getSessionCart();
			final MediaModel mediaModel = cartModel.getAttachedDoc();
			if(mediaModel != null) {
				modelService.remove(mediaModel);
				cartModel.setAttachedDoc(null);
			}
			
			getCartService().setSessionCart(cartModel);
			return true;
		}
		catch (final Exception exception)
		{
			LOGGER.error("Error while deleting media model from the session cart", exception);
			return false;
		}
	}
	/*Added for JJEPIC 720 Ends */

	/*
	 * This method is used to fetch the indirect customer name on the basis of given indirectCustomer code and country
	 * 
	 * model
	 */
	@Override
	public JnjIndirectCustomerModel fetchIndirectCustomerName(final String indirectCustomer, final CountryModel countryModel)
	{
		return jnjGTCartService.fetchIndirectCustomerName(indirectCustomer, countryModel);
	}
	
	/*
	 * This method is used to fetch the indirect customer name on the basis of given indirectCustomer code
	 * 
	 * model
	 */
	@Override
	public JnjIndirectCustomerModel fetchIndirectCustomerName(final String indirectCustomer)
	{
		return jnjGTCartService.fetchIndirectCustomerName(indirectCustomer);
	}
	/* (non-Javadoc)
	 * @see com.jnj.facades.cart.JnjGTCartFacade#updateNamedShippingDate(java.lang.String)
	 */
	@Override
	public boolean updateNamedShippingDate(String expShippingDate, Integer entryNumber)
	{
		Date expShippingDateInDateFormat = null;
		final DateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		try
		{
			expShippingDateInDateFormat = dateFormat.parse(expShippingDate);
		}
		catch (final ParseException parseExp)
		{
			LOGGER.error("Invalid date format for requested Shipping date", parseExp);
		}
		final CartModel cartModel = getCartService().getSessionCart();
		List <AbstractOrderEntryModel> abstractOrderEntryModelList = cartModel.getEntries();
		for (AbstractOrderEntryModel abstractOrderEntryModel : abstractOrderEntryModelList) {
			if(abstractOrderEntryModel.getEntryNumber() == entryNumber){
				abstractOrderEntryModel.setShippingDate(expShippingDateInDateFormat);
			}
		}
		cartModel.setEntries(abstractOrderEntryModelList);
		return jnjGTCartService.saveCartModel(cartModel, true); // change to true instead of false
	}
	
	@Override
	public void calculateAllTotals(JnjGTCartData cartData) {
		
		double subtotal = 0.0;
		double totalTax = 0;
		double totalFreightFees = 0;
		double totalExpeditedFee = 0;
		double handlingFee = 0;
		double dropShipFee = 0;
		double minimumOrderFee = 0;
		double totalInsuarance = 0;
		double totalDiscounts = 0;
		double grossPrice = 0;
		double totalFee = 0;
		PriceData subtotalPrice = null;
		final CurrencyModel currency = cartService.getSessionCart().getCurrency();

		final List<OutOrderLines3> outOrderLinesList = sessionService.getAttribute("outOrderLinesList");
		OutOrderLines3 outOrderLine = null;
		for (final OrderEntryData entry : cartData.getEntries())
		{
			if(entry.getTotalPrice() != null) {
				subtotal += entry.getTotalPrice().getValue().doubleValue();
			}
			if (outOrderLinesList != null)
			{
				outOrderLine = getOutOrderLinesResult(entry.getProduct().getCode(), outOrderLinesList);
			}

			if (outOrderLine != null)
			{
				if (StringUtils.isNotEmpty(outOrderLine.getTaxes()))
				{
					totalTax += Double.valueOf(outOrderLine.getTaxes()).doubleValue();
				}
				if (StringUtils.isNotEmpty(outOrderLine.getFreightFees()))
				{
					totalFreightFees += Double.valueOf(outOrderLine.getFreightFees());
				}
				if (StringUtils.isNotEmpty(outOrderLine.getDiscounts()))
				{
					totalDiscounts += Double.valueOf(outOrderLine.getDiscounts()).doubleValue();
				}
				if (StringUtils.isNotEmpty(outOrderLine.getInsurance()))
				{
					totalInsuarance += Double.valueOf(outOrderLine.getInsurance());
				}
				if (StringUtils.isNotEmpty(outOrderLine.getDropshipFee()))
				{
					dropShipFee += Double.valueOf(outOrderLine.getDropshipFee());
				}
				if (StringUtils.isNotEmpty(outOrderLine.getMinimumOrderFee()))
				{
					minimumOrderFee += Double.valueOf(outOrderLine.getMinimumOrderFee());
				}
			}
		}

		totalFee = minimumOrderFee + dropShipFee + totalFreightFees;
		grossPrice = subtotal + totalTax + totalFee;

		if (cartData instanceof JnjGTCartData)
		{
			
			final PriceData subTotalPrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(subtotal), currency);
			((JnjGTCartData) cartData).setSubTotal(subTotalPrice);
			
			final PriceData totalFeePrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(totalFee), currency);
			((JnjGTCartData) cartData).setTotalFees(totalFeePrice);

			final PriceData dropShipFeePrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(dropShipFee), currency);
			((JnjGTCartData) cartData).setTotalDropShipFee(dropShipFeePrice);

			final PriceData totalFreightFeesPrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(totalFreightFees),
					currency);
			((JnjGTCartData) cartData).setTotalFreightFees(totalFreightFeesPrice);

			final PriceData minimumOrderFeePrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(minimumOrderFee),
					currency);
			((JnjGTCartData) cartData).setTotalminimumOrderFee(minimumOrderFeePrice);
			
			final PriceData totalTaxPrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(totalTax), currency);
			((JnjGTCartData) cartData).setTotalTax(totalTaxPrice);

			final PriceData totalDiscountsPrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(totalDiscounts),
					currency);
			((JnjGTCartData) cartData).setTotalDiscounts(totalDiscountsPrice);

			final PriceData grossPricePrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(grossPrice), currency);
			((JnjGTCartData) cartData).setTotalGrossPrice(grossPricePrice);
			
			final PriceData totalPrice = jnjPriceDataFactory.create(PriceDataType.FROM, new BigDecimal(grossPrice), currency);
			((JnjGTCartData) cartData).setTotalPrice(totalPrice);			
			
		}
	}

	@Override
	public OutOrderLines3 getOutOrderLinesResult(String materialCode, List<OutOrderLines3> outOrderLinesList) {
		if(outOrderLinesList!=null)
		{
			for (final OutOrderLines3 outOrder : outOrderLinesList)
			{
				if (outOrder.getMaterialEntered().equalsIgnoreCase(materialCode))
				{
					return outOrder;
				}
			}
		}
		return null;
	}
	
	@Override
	public boolean checkProductInContract(String productCode,boolean basePriceFlag,Map<String, List<JnjContractEntryModel>> contractProductMap,
			Map<String, Set<String>> cartOrSelMap)
	{
		
		return jnjGTCartService.checkProductInContract(productCode,basePriceFlag,contractProductMap, cartOrSelMap);
	}

	@Override
	public boolean updateContractNumInCartEntry(String productCode,String contractNum) {
		
		return jnjGTCartService.updateContractNumInCartEntry(productCode,contractNum);
	}
	@Override
	public boolean removeNonContractProduct()	{
		return jnjGTCartService.removeNonContractProduct();
	}
	
	@Override
	public boolean updateShipToAddIdMap(String shippingAddressId,boolean checBoxStatus, HttpServletRequest request)	{
		final HttpSession session=request.getSession();
		JnJB2bCustomerModel jnJB2bCustomerModel = jnjGTCustomerSevice.getCurrentUser();
		 
		if(null!=jnJB2bCustomerModel){
		Map<String,String> shipToAddressIdMap=new HashMap<String,String>(jnJB2bCustomerModel.getDefaultShipToAddressIdMap());
		String addressKey=jnJB2bCustomerModel.getUid()+"-"+jnjGTCustomerSevice.getCurrentUser().getCurrentB2BUnit().getUid();
		
		if(jnJB2bCustomerModel.getDefaultShipToAddressIdMap().containsKey(addressKey)){
			if(checBoxStatus){
				shipToAddressIdMap.put(addressKey, shippingAddressId);
				session.setAttribute("updateShippingAddressClicked", Boolean.TRUE);
			}else{
				shipToAddressIdMap.remove(addressKey);
				session.setAttribute("updateShippingAddressClicked", Boolean.FALSE);
			}
			jnJB2bCustomerModel.setDefaultShipToAddressIdMap(shipToAddressIdMap);
		}else{
			shipToAddressIdMap.put(addressKey, shippingAddressId);
			jnJB2bCustomerModel.setDefaultShipToAddressIdMap(shipToAddressIdMap);
		} 
 
	    modelService.save(jnJB2bCustomerModel);  
		}
		return true;
	}
	
	@Override
	public boolean updateBillToAddIdMap(String shippingAddressId,boolean checBoxStatus, HttpServletRequest request)	{
		final HttpSession session=request.getSession();
		JnJB2bCustomerModel jnJB2bCustomerModel = jnjGTCustomerSevice.getCurrentUser();
		if(null!=jnJB2bCustomerModel){
		Map<String,String> billToAddressIdMap=new HashMap<String,String>(jnJB2bCustomerModel.getDefaultBillToAddressIdMap());
		String addressKey=jnJB2bCustomerModel.getUid()+"-"+jnjGTCustomerSevice.getCurrentUser().getCurrentB2BUnit().getUid();
		
		if(jnJB2bCustomerModel.getDefaultBillToAddressIdMap().containsKey(addressKey)){
			if(checBoxStatus){
				billToAddressIdMap.put(addressKey, shippingAddressId);
				session.setAttribute("updateBillingAddressClicked", Boolean.TRUE);
			}else{
				billToAddressIdMap.remove(addressKey);
				session.setAttribute("updateBillingAddressClicked", Boolean.FALSE);
			}
			jnJB2bCustomerModel.setDefaultBillToAddressIdMap(billToAddressIdMap);
		}else{
			billToAddressIdMap.put(addressKey, shippingAddressId);
			jnJB2bCustomerModel.setDefaultBillToAddressIdMap(billToAddressIdMap);
		} 
	    modelService.save(jnJB2bCustomerModel);  
		}
		return true;
	}
	@Override
	public AddressData getDefaultDisplayShippingAdd(List<AddressData> listOfAvailableAddress,final Model model,final HttpServletRequest request)
	{ 
		JnJB2bCustomerModel jnJB2bCustomerModel = jnjGTCustomerSevice.getCurrentUser();
		final HttpSession session=request.getSession();
		if(null!=jnJB2bCustomerModel){
	    	if(jnJB2bCustomerModel.getDefaultShipToAddressIdMap().size()>0){ 
					    for (AddressData address : listOfAvailableAddress)
						{ 
					    	if(jnJB2bCustomerModel.getDefaultShipToAddressIdMap().containsValue(address.getId())){ 
					    	    setShippingAddress(address); 
					    	    model.addAttribute("makeThisAddrDefaultChk", Boolean.TRUE);
					    	    session.setAttribute("defaultChekAddid", address.getId());
					    		return address; 
					    	} 
						} 
					 
	    	}
		}
	    return null;
	}
	
	@Override
	public AddressData getDefaultDisplayBillingAdd(List<AddressData> listOfAvailableAddress,final Model model,final HttpServletRequest request)
	{ 
		JnJB2bCustomerModel jnJB2bCustomerModel = jnjGTCustomerSevice.getCurrentUser();
		final HttpSession session=request.getSession();
		if(null!=jnJB2bCustomerModel){
	    	if(jnJB2bCustomerModel.getDefaultBillToAddressIdMap().size()>0){ 
					    for (AddressData address : listOfAvailableAddress)
						{ 
					    	if(jnJB2bCustomerModel.getDefaultBillToAddressIdMap().containsValue(address.getId())){ 
					    	    setBillingAddress(address); 
					    	    model.addAttribute("makeThisAddrDefaultChangeChkForBilling", Boolean.TRUE);
					    	    session.setAttribute("defaultCheckforBillingAddid", address.getId());
					    		return address; 
					    	} 
						} 
					 
	    	}
		}
	    return null;
	}
	
	@Override
	public Long updateQuantity(final OrderEntryData entry, final Long entryQuantity)
	{

		Long quantity = null;

		if (sessionService != null)
		{
			final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
			if (freeGoodsMap != null && entry != null && freeGoodsMap.containsKey(entry.getProduct().getCode()))
			{
				final String freeItemsQuantity = freeGoodsMap.get(entry.getProduct().getCode()).getMaterialQuantity();
				final JnjGTOutOrderLine outOrderLine = freeGoodsMap.get(entry.getProduct().getCode());
				final long orderedQuantity = Long.parseLong(outOrderLine.getOrderedQuantity());
				final long chargedQuantity = entry.getQuantity();
				final long totalQuantity = Long.parseLong(freeItemsQuantity) + chargedQuantity;
				if (chargedQuantity == entryQuantity)
				{
					if (totalQuantity == orderedQuantity)
					{
						quantity = orderedQuantity;
					}
					else if (totalQuantity != orderedQuantity)
					{
						quantity = chargedQuantity;
					}
					else
					{
						quantity = totalQuantity;
					}

				}
				else
				{
					quantity = entryQuantity;
				}
			}
			else
			{
				quantity = entryQuantity;
			}
		}
		else
		{
			quantity = entryQuantity;
		}
		return quantity;
	}
	
	@Override
	public JnjContractFormData validateIsNonContract(String[] selectedProductCatalogIds,String selectedContractNum) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_GET_CONTRACTS_PRODUCT
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		 
		String productCodeInCart = null;
		JnjContractFormData jnjContractFormData = new JnjContractFormData();
		boolean isNonContractInCart = false; // dummy for popup its only for validation
		Map<String, List<JnjContractEntryModel>> contractProductMap = new HashMap<String, List<JnjContractEntryModel>>();
		Map<String, Set<String>> cartMap = new HashMap<String, Set<String>>();
		int cont_Size = 0;
		int nonCont_Size = 0;
		// to remove a non contract product from cart while adding contract product for using the below flag
		boolean nonContractProductInCartFlag = false; 
		boolean nonContractProductInSelectedFlag = false;
		boolean contractProductInSelectedFlag = false;
		//boolean isNonContractProductInCart = false;
		boolean isContractProductInCart = false;
		List<String> cartProductIds = new ArrayList<String>();
		//getting existing product from cart to identify non contract or not
		final CartData cartData = getSessionCart();
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty()) {
			for (final OrderEntryData entry : cartData.getEntries()) {
				productCodeInCart =  entry.getProduct().getCode();
				cartProductIds.add(productCodeInCart);
				// checking false condition for contract product is available until to identify the Non contract product
					isNonContractInCart = checkProductInContract(productCodeInCart, isNonContractInCart, contractProductMap,cartMap);
				//}
			}
		}
		//this is to check selected product is available in contract or not
		boolean isNonContractProductInSelectedList= false;
		if(cartData.getEntries() != null && !cartData.getEntries().isEmpty()) {
			for (String productCode : selectedProductCatalogIds) {
				 // checking false condition for contract product is available until to identify the Non contract product
					isNonContractProductInSelectedList = checkProductInContract(productCode, isNonContractProductInSelectedList, contractProductMap,cartMap);
				//}
			}
		}
		
	    for (Map.Entry<String, Set<String>> productsMap : cartMap.entrySet()) {
		   	 LOGGER.info("cartMap Key : " + productsMap.getKey() + " Value : " + productsMap.getValue());
	   	   	 if(productsMap.getKey().equalsIgnoreCase(Jnjb2bCoreConstants.MyAccount.CONTRACT_KEY)){
	   	   		 cont_Size = productsMap.getValue().size();
	   	   		//in cart contract Product is available
	   	   		 for (String cartProduct : cartProductIds) {
	   	   			 isContractProductInCart = productsMap.getValue().contains(cartProduct);
	  	   		 	    if(isContractProductInCart){
	  	   		 	   	//nonContractProductInCartFlag = true;
	  	   		 	   	break; //break if found atleast one contract product or not
	  	   		 	   }
	   	   		 }
	   	   		 
	   	   		//in selected product list non contract Product is available or not
	   	   		 for (String selProduct : selectedProductCatalogIds) {
	   	   			 contractProductInSelectedFlag = productsMap.getValue().contains(selProduct);
	 	   		 	   if(contractProductInSelectedFlag){
	 	   		 	   	break; //break if found atleast one contract product
	 	   		 	   }
	 	   		    }
	   	   	 }
	   	   	 else if(productsMap.getKey().equalsIgnoreCase(Jnjb2bCoreConstants.MyAccount.NON_CONTRACT_KEY)){
	   	   		 nonCont_Size = productsMap.getValue().size();
	   	   		 //in cart non contract Product is available
	   	   		 for (String cartProduct : cartProductIds) {
	   	   			 nonContractProductInCartFlag = productsMap.getValue().contains(cartProduct);
	  	   		 	   // LOGGER.info("non contract product in cart isCartAvailableflag : " + nonContractProductInCartFlag);
	  	   		 	    if(nonContractProductInCartFlag){
	  	   		 	   	//nonContractProductInCartFlag = true;
	  	   		 	   	break; //break if found atleast one contract product or not
	  	   		 	   }
	   	   		 }
	   	   		//in selected product list non contract Product is available or not
	   	   		 for (String selProduct : selectedProductCatalogIds) {
	   	   			 nonContractProductInSelectedFlag = productsMap.getValue().contains(selProduct);
	 	   		 	  // LOGGER.info("non contract product in selected list nonContractProductInSelectedFlag : " + nonContractProductInSelectedFlag);
	 	   		 	   if(nonContractProductInSelectedFlag){
	 	   		 	   	break; //break if found atleast one contract product
	 	   		 	   }
	 	   		    }
	   	   	 }
			}
	    LOGGER.info("nonContractProductInCartFlag before : " + nonContractProductInCartFlag +" and nonContractProductInSelectedFlag : " + nonContractProductInSelectedFlag);
	    LOGGER.info("isContractProductInCart : " + isContractProductInCart +" and contractProductInSelectedFlag : " + contractProductInSelectedFlag);
	    //set default 0 when first time cart adding for contract page alone using
	   jnjContractFormData.setMultiContractCount(0);
		jnjContractFormData.setMultiProductCount(0);
	   if(cont_Size >0){
	    	isMultipleContractProduct(cartData,selectedProductCatalogIds,selectedContractNum,jnjContractFormData);
		}
	    //if user expected all time popup when adding non contract product against contract product in cart so this contition is adding or not then comment this condition
	    //true  && true  && true
	    if(nonContractProductInCartFlag && isContractProductInCart && nonContractProductInSelectedFlag ){
	   		nonContractProductInCartFlag = false; // to show popup and user cancel then no need to do further just close popup
	   		nonContractProductInSelectedFlag = true;
	    }
	    
	   
	   	 if(nonContractProductInCartFlag && isContractProductInCart && contractProductInSelectedFlag ){
	   		nonContractProductInCartFlag = true; // to show popup and user cancel then need to remove existing non contract product from cart
	   		nonContractProductInSelectedFlag = false;
	   	 }
	   	 
	   	 //true  && false  && true
	   	 if(nonContractProductInCartFlag && !isContractProductInCart && contractProductInSelectedFlag ){
    		nonContractProductInCartFlag = true; // to show popup and user cancel then need to remove existing non contract product from cart
    		nonContractProductInSelectedFlag = false;
    	 }
   	 
	    
	   	LOGGER.info("multiContractCount : " + jnjContractFormData.getMultiContractCount());
		LOGGER.info("multiProductCount  : " + jnjContractFormData.getMultiProductCount());
		
	   	LOGGER.info("nonContractProductInCartFlag after : " + nonContractProductInCartFlag +" and nonContractProductInSelectedFlag : " + nonContractProductInSelectedFlag);
	    LOGGER.info("cont_Size : " + cont_Size + " and nonCont_Size : "+nonCont_Size);
	    if(cont_Size >0 && nonCont_Size > 0){
	   	 jnjContractFormData.setIsNonContractProductInCart(nonContractProductInCartFlag);// generally bypass this condition to show popup because  on cart product / selected product is mixed
	   	 jnjContractFormData.setIsNonContractProductInSelectedList(nonContractProductInSelectedFlag);
	    }else if(cont_Size == 0 && nonCont_Size == 0){
	   	 jnjContractFormData.setIsNonContractProductInCart(true);// generally bypass this condition to add product in cart because of no product in cart but this will not happen
	   	 jnjContractFormData.setIsNonContractProductInSelectedList(true);
	    }else if((cont_Size > 0 && nonCont_Size == 0) || (cont_Size == 0 && nonCont_Size > 0)){
	   	 jnjContractFormData.setIsNonContractProductInCart(true);// generally bypass this condition to add product in cart because of no product in cart
	   	 jnjContractFormData.setIsNonContractProductInSelectedList(true);
	    } 
	    LOGGER.info("NonContractInCart : " + jnjContractFormData.getIsNonContractProductInCart() + " and NonContractInSelectedList : " +  jnjContractFormData.getIsNonContractProductInSelectedList());
		//  the facade method to get the boolean response if Non contract product is available
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_GET_CONTRACTS_PRODUCT
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjContractFormData;
	}
	
	protected void isMultipleContractProduct(CartData cartData, String[] selectedIds,String selectedContractNum,JnjContractFormData jnjContractFormData){
		String cartContractNo = null;
		String productCodeInCart = null;
		int multiContractCount = 0;
		int multiProductCount = 0;
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty()) {
			System.out.println("cartData.getEntries() size : "+cartData.getEntries().size());
			for ( OrderEntryData data : cartData.getEntries()) {
				System.out.println("inside for loop");
				if (data instanceof JnjGTOrderEntryData){
					productCodeInCart =  data.getProduct().getCode(); 
					final JnjGTOrderEntryData jnjOrderEntryData = (JnjGTOrderEntryData) data; 
					cartContractNo = jnjOrderEntryData.getContractNumber();
					System.out.println("getContractNumber : "+cartContractNo);
					for (String selProduct : selectedIds) {
						if(cartContractNo != null && selectedContractNum != null){
							if( cartContractNo.equalsIgnoreCase(selectedContractNum)){
								 LOGGER.info("seleected contract is available in cart : " + selectedContractNum); 
							 }else{
								 LOGGER.info("selected product contract is not available in cart : " + selectedContractNum);
								 multiContractCount = multiContractCount +1;
							 }
						  }
						
						if(productCodeInCart.equalsIgnoreCase(selProduct)){
							 LOGGER.info("selected product is available in cart : " + selProduct);
						 }else{
							 LOGGER.info("selected product is not available in cart : " + selProduct);
							 multiProductCount = multiProductCount +1;
						 }
						
		 	   		 }
					
				}
		}
	 }
		jnjContractFormData.setMultiContractCount(multiContractCount);
		jnjContractFormData.setMultiProductCount(multiProductCount);
	}

	/* (non-Javadoc)
	 * @see com.jnj.facades.cart.JnjGTCartFacade#validateBackOrder()
	 */
	@Override
	public JnjGTProposedOrderResponseData simulateOrderFirstSAPCall(JnjGTSapWsData wsData) {
		final CartModel cartModel = getCartService().getSessionCart();
		return jnjGTProposedOrderItemMapper.mapConsignmentSimulateOrderRequestResponse(cartModel, wsData);
	}

	@Override
	public JnjGTProposedOrderResponseData checkReplacemenItemForProduct(String productId, final JnjGTSapWsData wsData){
		return jnjGTProposedOrderItemMapper.checkReplacemenItemForProduct(productId, wsData);
 	} 


	@Override
	public boolean updateCartModelReturn(JnjGTCartData cart) {
		
		final CartModel currentCart = getCartService().getSessionCart();
		currentCart.setPurchaseOrderNumber(cart.getPurchaseOrderNumber());
		currentCart.setPoDate(cart.getPoDate());
		currentCart.setReturnCreatedDate(cart.getReturnCreatedDate());
		currentCart.setRequestedDeliveryDate(cart.getExpectedShipDate());
		currentCart.setEndUser(cart.getEndUser());
		currentCart.setStockUser(cart.getStockUser());
		currentCart.setShippingInstruction(cart.getShippingInstructions());
		return jnjCartService.saveCartModel(currentCart, true);
	}

	@Override
	public boolean updateProposedAndOriginalItem(JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData) {
		return  jnjGTCartService.updateProposedAndOriginalItem(jnjGTProposedOrderResponseData);
	}

	@Override
	public List<String> getBatchNumbersForProduct(){
		return  jnjGTCartService.getBatchNumbersForProduct();
	}
	
	@Override
	public List<String> getSerialNumbersForProduct(){
		return  jnjGTCartService.getSerialNumbersForProduct();
	}
	
	@Override
	public boolean saveReturnMedia(List<MultipartFile> returnUploadFiles) {
		
		return jnjGTCartService.saveReturnMedia(returnUploadFiles);
	}
	
	@Override
	public List<JnjGTConsInventoryData> fetchBatchDetails() throws IntegrationException, SystemException
	{
		final CartModel currentCart = getCartService().getSessionCart();
		List<AbstractOrderEntryModel> entryModels= currentCart.getEntries();
		List<String> productCodes= new ArrayList<>();
		JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
		String productCode="";
		for (Iterator iterator = entryModels.iterator(); iterator.hasNext();) {
			AbstractOrderEntryModel abstractOrderEntryModel = (AbstractOrderEntryModel) iterator.next();
			productCode=abstractOrderEntryModel.getProduct().getCode();
			if(!productCodes.contains(productCode))
			{
				productCodes.add(productCode);
			}
		}	
		if(productCodes.size()>0)
		{
			jnjGTPageableData.setSearchParamsList(productCodes);
			return jnjGTConsInventoryReportMapper.fetchBatchDetails(jnjGTPageableData);
		}
		else
		{
			return null;
		}
	}
	
	//AAOL-5672
	@Override
	public void modifyQuantityForCopyCharge(String prodQty, String currentLineEntryNumber){
		
		Long entryNumber = Long.parseLong(currentLineEntryNumber);
		Long prodQuantity = Long.parseLong(prodQty) -1;
		try {
			if(prodQuantity>0)
			updateCartEntry(entryNumber, prodQuantity);
			else
				LOGGER.warn("Couldn't update product with the entry number: " + entryNumber + "as product Quantity is zero");	
		} catch (CommerceCartModificationException e) {
			LOGGER.warn("Couldn't update product with the entry number: " + entryNumber + ".", e);
		}
	}
}