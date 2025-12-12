/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.cartandcheckoutaddon.controllers.pages;


import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData; 
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import com.jnj.core.constants.GeneratedJnjb2bCoreConstants.Enumerations.JnjGTKitType;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.model.JnJGTProductKitModel;
import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.List;
import java.util.ListIterator;

import jakarta.annotation.Resource;
import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.jnj.b2b.cartandcheckoutaddon.controllers.CartandcheckoutaddonControllerConstants;
import com.jnj.b2b.cartandcheckoutaddon.forms.CreditCardDetailsForm;
import com.jnj.b2b.cartandcheckoutaddon.forms.OneTimeShippingAddForm;
import com.jnj.b2b.cartandcheckoutaddon.forms.ProposedLineItemForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.impl.DefaultMessageFacade;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.b2b.storefront.forms.BatchDetailsForm;
import com.jnj.b2b.storefront.forms.ConsignmentChargeForm;
import com.jnj.b2b.storefront.forms.ConsignmentFillupForm;
import com.jnj.b2b.storefront.forms.UpdateQuantityForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Cart;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
import com.jnj.core.data.JnjGTGetPriceQuoteResponseData;
import com.jnj.core.data.JnjGTUpdatePriceData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.dto.JnjGTSurgeryInfoData;
import com.jnj.core.dto.JnjGTViewSurgeryInfoData;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTOrderTypeComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTCommonFormIOData;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTOutOrderLine;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.facades.order.JnjGTCheckoutFacade;
import com.jnj.facades.order.JnjGTOrderFacade;
import com.jnj.facades.product.JnjGTProductFacade;
import com.jnj.facades.reports.JnjGTReportsFacade;
import com.jnj.gt.outbound.mapper.JnjGTOutOrderLineMapper;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;
import com.jnj.b2b.cartandcheckoutaddon.forms.OneTimeShippingAddForm;
import com.jnj.b2b.cartandcheckoutaddon.forms.ReturnFileUploadForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.services.MessageService;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.collections4.CollectionUtils;

import com.jnj.core.util.JnjGTOrderTypeComparator;

import java.util.Collections;
import java.util.HashSet;


/*GTR-1693 Starts*/
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
//import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import java.util.Base64;

/*GTR-1693 Ends*/

/**
 * @author Accenture
 * @version 1.0
 */
@Controller("JnjGTCartPageController")
@Scope("tenant")
@RequireHardLogIn
@RequestMapping(value = "/cart") 
public class JnjGTCartPageController extends AbstractPageController
{
	protected static final Logger LOGGER = Logger.getLogger(JnjGTCartPageController.class);

	protected static final String CART_VALIDATION_PAGE = "cartValidationPage";
	protected static final String CART_CHECKOUT_PAGE = "cartCheckoutPage";
	protected static final String SHOW_SIGNATURE = "showSignature";
	
	protected static final String SHIPPING_PAGE = "shippingPage";
	protected static final String SHIPPING_PAGE_URL = "shipping";
	@Autowired
	protected JnjGTOutOrderLineMapper jnjGTOutOrderLineMapper;
	@Autowired
	protected DefaultMessageFacade messageFacade;

	

	/** Common constant for PCM and EPIC **/
	protected static final String CART_CMS_PAGE = "cartPage";

	protected static final String PCM_CART_CONFIGURATION_CMS_PAGE = "cartConfigurationPage";
	protected static final String PCM_CART_CONFIRMATION_CMS_PAGE = "cartConfirmationPage";
	protected static final String CONTINUE_URL = "continueUrl";
	protected static final Integer ERROR_COMES = Integer.valueOf(1);
	protected static final Integer ERROR_NOT_COMES = Integer.valueOf(0);
	protected static final String US_COUNTRY_CODE = "US";
	
	
	/*GTR-1693 Starts*/
	
	protected static final String CONFIG_MSG_TYPE = "configMsg";
	protected static final String PO_ORDER_NO_RESTRICTION = "user.po.order.number.restriction.pattern";
	protected static final String PO_ORDER_CART_ORDERTYPE_DELIVERED = "cart.order.type.delivered";
	protected static final String PO_ORDER_CART_ORDERTYPE_REPLINESH = "cart.order.type.replinesh";
	protected static final String PO_ORDER_CART_ORDERTYPE_STANDREPLINESH = "cart.order.type.standreplinesh";
	protected static final String BYEPASS_CHECKOUT_OPTION = "byepass.checkout.option";
	/*GTR-1693 Ends*/
	//AAOL-3769 changes
	protected static final String PRODUCT_CODE = "249095000";
	protected static final String CONFIRM_COUANTITY = "5";
	//End of AAOL-3769
	//UAT-556
	protected static final String ERROR_DETAIL_MAP ="errorDetailMap";

	@Resource(name = "b2bOrderFacade")
	protected B2BOrderFacade b2bOrderFacade;

	/** The reports facade **/
	@Resource(name="jnJGTReportsFacade")
	JnjGTReportsFacade jnjGTReportsFacade;

	@Autowired
	protected JnjConfigServiceImpl jnjConfigServiceImpl;

	@Resource(name = "GTOrderFacade")
	JnjGTOrderFacade orderFacade;

	@Resource(name="GTCartFacade")

	protected JnjGTCartFacade jnjGTCartFacade;


    @Resource(name = "GTOrderFacade")
	JnjGTOrderFacade jnjGTOrderFacade;

    @Resource(name = "GTCheckoutFacade")
	JnjGTCheckoutFacade jnjGTCheckoutFacade;

	@Resource(name="GTCustomerFacade")

	JnjGTCustomerFacade jnjGTCustomerFacade;

	@Resource(name = "cartFacade")
	protected CartFacade cartFacade;

	@Resource(name = "sessionService")
	protected SessionService sessionService;

	@Autowired
	protected MessageService messageService;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	protected I18NFacade i18NFacade;

	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	
	@Autowired
	protected JnjOrderUtil orderUtil;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	// added 
	
	@Autowired
	JnjGTProductFacade jnjGTProductFacade;
	
	public JnjGTProductFacade getJnjGTProductFacade() {
		return jnjGTProductFacade;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}
	
	@Autowired
	JnjGTCartService jnjGTCartService;
	
	@Resource(name = "priceDataFactory")
	protected JnjPriceDataFactory jnjPriceDataFactory;

	public CartService getCartService() {
		return cartService;
	}

	@Resource(name = "GTB2BUnitFacade")
	protected JnjGTB2BUnitFacade jnjGTUnitFacade;


	public JnjGTOrderFacade getOrderFacade() {
		return orderFacade;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}


	public void setJnjGTUnitFacade(JnjGTB2BUnitFacade jnjGTUnitFacade) {
		this.jnjGTUnitFacade = jnjGTUnitFacade;
	}
	
	public B2BOrderFacade getB2bOrderFacade() {
		return b2bOrderFacade;
	}



	public JnjGTReportsFacade getJnjGTReportsFacade() {
		return jnjGTReportsFacade;
	}



	public JnjGTCartFacade getJnjGTCartFacade() {
		return jnjGTCartFacade;
	}


	public JnjGTOrderFacade getJnjGTOrderFacade() {
		return jnjGTOrderFacade;
	}


	public JnjGTCheckoutFacade getJnjGTCheckoutFacade() {
		return jnjGTCheckoutFacade;
	}


	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public CartFacade getCartFacade() {
		return cartFacade;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public JnjGTB2BUnitFacade getJnjGTUnitFacade() {
		return jnjGTUnitFacade;
	}
	@Autowired
	protected UserService userService;
	
	protected static final String SHOPPINGCART = "shopping.cart";
	protected static final String SHIPPING = "shipping.continue";
	
	protected static final String ORDERREVIEW = "order.review";
	
	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String showCart(final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		JnJB2BUnitModel currentB2bUnit = sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
		model.addAttribute("indicator", currentB2bUnit.getIndicator());
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		// to remove cart on clicking back button	
			/*if(sessionService.getAttribute("validatedone")==Boolean.TRUE){
				jnjGTCartFacade.removeSessionCart();
				sessionService.setAttribute("validatedone", Boolean.FALSE);
				sessionService.setAttribute("confirmationdone", Boolean.FALSE);
				}*/
		final ConsignmentFillupForm consignmentFillupForm = new ConsignmentFillupForm();
		model.addAttribute("consignmentFillupForm",consignmentFillupForm);
		//UAT-556
		Map<String, String> errorDetailMap = new HashMap<String, String>();
		errorDetailMap = sessionService.getAttribute(ERROR_DETAIL_MAP);
		if(errorDetailMap!=null){
			model.addAttribute(ERROR_DETAIL_MAP,errorDetailMap);
			sessionService.removeAttribute(ERROR_DETAIL_MAP);
		}
		
		return showCartPage(model, request);
	}
	
	protected String showCartPage(final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{
		LOGGER.info("Start: Cart Page Loading");
		prepareDataForCartPage(model,request);
		prepareDataForPage(model, false);
		LOGGER.info("End>>: Cart Page Loading");
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		
		//Changes for AAOL-5809
		boolean isReturnImageUploaded = false;
		if(null!=sessionService.getAttribute("isReturnImageUploaded")){
			isReturnImageUploaded = sessionService.getAttribute("isReturnImageUploaded");
			sessionService.removeAttribute("isReturnImageUploaded");
		}
		model.addAttribute("isReturnImageUploaded",isReturnImageUploaded);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		
		/** If the User is eligible for Start New Order **/
		final Object orderingRights = sessionService.getAttribute(LoginaddonConstants.Login.ORDERING_RIGHTS);
	
		if (null != orderingRights && ((Boolean) orderingRights).booleanValue())
		{
			model.addAttribute("isEligibleForNewOrder", Boolean.TRUE);
		}
			
		String ViewPath = jnjGTCartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.CartPage, null);
		return getView(ViewPath);
	}

	@RequestMapping(value = "/enterOntimeShippingAddress")
	public String enterOntimeShippingAddress(final Model model)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute("regionCodes", jnjGTCustomerFacade.getRegions(US_COUNTRY_CODE));
		/*return ControllerConstants.Views.Pages.Cart.OneTimeShippingAddressPopUp;*/
		return "";
	}


	@RequestMapping(value = "/removeOneTimeShipToAddress")

	public @ResponseBody JnjGTAddressData removeOneTimeShipToAddress(final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{

		jnjGTCartFacade.removeOneTimeShippingAddress();
		showCart(model,request);
		final CartData cartData = cartFacade.getSessionCart();
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		return (JnjGTAddressData) cartData.getDeliveryAddress();
	}

	@RequestMapping(value = "/validate")
	public String validateCart(final Model model, JnjGTSapWsData wsData, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		if(cartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}
		if(wsData == null){
			wsData = new JnjGTSapWsData();
			wsData.setIsfirstSAPCall(false);
			wsData.setIsSecondSAPCall(false);
			wsData.setIsRefreshCall(false);
			wsData.setSkipSAPValidation(false);
			wsData.setTimeOutExtended(false);
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		return validateCartPage(model,wsData,redirectModel);
	}

	@RequestMapping(value = "/shipping", method = {RequestMethod.GET, RequestMethod.POST})
	public String shipping(final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel, final String freightType)
			throws CMSItemNotFoundException
	{
				
		boolean splitCart = false;
		final CartModel cartModel = cartService.getSessionCart();
						
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CHECKOUT_PAGE));

		}
		catch (final CMSItemNotFoundException exeption)
		{
			LOGGER.error("Order Checkout not found in current content", exeption);
		}
		
		JnJProductModel product = null;
		
		//kit products - start
		List<String> orthoKitProductsList = new ArrayList<>();
		List<String> zeroPriceProductsList = new ArrayList<>();
		List<AbstractOrderEntryModel> cartEntriesList = new ArrayList<>();

		if(CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			cartEntriesList = cartModel.getEntries();
		}
		List<Integer> orderEntryNumberList = new ArrayList<>();
		List<Integer> orderEntryZeroNumberList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(cartEntriesList))
		{
			Iterator<AbstractOrderEntryModel> cartEntryIterator = cartEntriesList.iterator();

			while (cartEntryIterator.hasNext()) 
			{
				AbstractOrderEntryModel abstractOrderEntryModel = cartEntryIterator.next();
				product = (JnJProductModel) abstractOrderEntryModel.getProduct();
				if(null != product.getKitType() && JnjGTKitType.ORTHO.toString().equals(product.getKitType().toString()))
				{
					orthoKitProductsList.add(product.getCode());
					orderEntryNumberList.add(abstractOrderEntryModel.getEntryNumber());
				}
			}
		}

		if(CollectionUtils.isNotEmpty(orderEntryNumberList))
		{
			//reverse iterator
			ListIterator<Integer> entryNumberIterator =  orderEntryNumberList.listIterator(orderEntryNumberList.size());

			while(entryNumberIterator.hasPrevious()) {
				//set the quantity to 0 to remove the cart entry.
				try
				{
					cartFacade.updateCartEntry(entryNumberIterator.previous(),0);
					
				}
				catch (Exception exp)
				{
					LOGGER.debug("Exception in this line : cartFacade.updateCartEntry(iterator1.previous(),0);" + exp);
				}
			}
		}

		/*AAOL-4069*/
		//Changes to enable/disable zero pricing check
		if(orderUtil.checkZeroPricing()){
			final String orderTypes = jnjGTCartFacade.getOrderType();
			if(!orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZKB.getCode())){
				
					if(CollectionUtils.isNotEmpty(cartEntriesList)){	
						
						Iterator<AbstractOrderEntryModel> cartEntryIterator = cartEntriesList.iterator();
	
						while (cartEntryIterator.hasNext()) 
						{
							AbstractOrderEntryModel entry = cartEntryIterator.next();
							if(entry.getBasePrice().intValue() == 0){	
								zeroPriceProductsList.add(entry.getProduct().getCode());
								orderEntryZeroNumberList.add(entry.getEntryNumber());
							}
						}
				
				}
	
				
				if(CollectionUtils.isNotEmpty(orderEntryZeroNumberList))
				{
					//reverse iterator
					ListIterator<Integer> entryNumberIterator =  orderEntryZeroNumberList.listIterator(orderEntryZeroNumberList.size());
	
					while(entryNumberIterator.hasPrevious()) {
						//set the quantity to 0 to remove the cart entry.
						try
						{
							cartFacade.updateCartEntry(entryNumberIterator.previous(),0);
							
						}
						catch (Exception exp)
						{
							LOGGER.debug("Exception in this line : cartFacade.updateCartEntry(iterator1.previous(),0);" + exp);
						}
					}
				}
			}
			
		}
			
				
				/*AAOL-4069*/		

		model.addAttribute("orthoKitProductsList" , orthoKitProductsList);
		//kit products - end
		
		
		
		
		
		
		
		
		
		
		//Changes for Bonus Item
		final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
		model.addAttribute("freeGoodsMap", freeGoodsMap);
		sessionService.setAttribute("shippingFlag", true);
		
		prepareDataForPage(model, true);
		/**Changes for Dropshipment and Order Split */
		if(sessionService.getAttribute("populateSpliCart")!=null){
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		
		
		if(splitCart){
			populateSplitCarts(model);
		}
		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());
		final Collection<AddressData> shippingAddressess = jnjGTUnitFacade.getShippingAddresses();
		model.addAttribute("shippingAddressess", shippingAddressess);


		final String orderTypes = ((JnjGTCartData) jnjGTCartFacade.getSessionCart()).getOrderType();

		/** Checking if the order type is delivered order "ZDEL" **/
		if (orderTypes != null && orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()))
		{
			/** Adding the show signature flag **/
			model.addAttribute(SHOW_SIGNATURE, Boolean.TRUE);
		}
		setFalgForcaribPuertCustomer(model);
		model.addAttribute("timeOutExtended", Boolean.valueOf(wsData.isTimeOutExtended()));
		JnJB2BUnitModel currentB2bUnit = sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
		model.addAttribute("indicator", currentB2bUnit.getIndicator());
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
        model.addAttribute("checkoutoption", Config.getParameter(BYEPASS_CHECKOUT_OPTION));
		//String ViewPath = jnjGTCartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.ShippingPage, null);

		if(jnjGTCartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}
		
		// to remove cart on clicking back button	
		sessionService.setAttribute("validatedone", Boolean.FALSE);
		if(sessionService.getAttribute("validatedone")==Boolean.TRUE)
		{
		jnjGTCartFacade.removeSessionCart();
		
		return REDIRECT_PREFIX + "/cart";
		}
		sessionService.setAttribute("validatedone", Boolean.TRUE);
		if(sessionService.getAttribute("confirmationdone")==Boolean.TRUE)
		{
		jnjGTCartFacade.removeSessionCart();
		sessionService.setAttribute("confirmationdone", Boolean.FALSE);
		return REDIRECT_PREFIX + "/cart";
		}
		
		//model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("Shipping"));
		
		
		
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
		.add(new Breadcrumb("/cart/validate", jnjCommonFacadeUtil.getMessageFromImpex(SHOPPINGCART), null));
		breadcrumbs
				.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex(SHIPPING), null));
		
		
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		
		return getView(jnjGTCartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.ShippingPage, null));
		
		
	
	}
	
	
	@RequestMapping(value = "/paymentContinue")
	public String paymentContinue(final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel ,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		boolean splitCart = false;
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CHECKOUT_PAGE));

		}
		catch (final CMSItemNotFoundException exeption)
		{
			LOGGER.error("Order Checkout not found in current content", exeption);
		}
		
		//Changes for Bonus Item
		final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
		model.addAttribute("freeGoodsMap", freeGoodsMap);
		sessionService.setAttribute("paymentFlag", true);
		
		prepareDataForPage(model, false);

		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());


		final String orderTypes = ((JnjGTCartData) jnjGTCartFacade.getSessionCart()).getOrderType();
		
		final boolean isContractCart = ((JnjGTCartData) jnjGTCartFacade.getSessionCart()).isIsContractCart();
		
		LOGGER.info("isContractCart :: "+isContractCart);

		/** Checking if the order type is delivered order "ZDEL" **/
		if (orderTypes != null && orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()))
		{
			/** Adding the show signature flag **/
			model.addAttribute(SHOW_SIGNATURE, Boolean.TRUE);
		}
		setFalgForcaribPuertCustomer(model);
		
		prepareDataForCartPage(model,request);
		if(sessionService.getAttribute("populateSpliCart")!=null){
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		
		if(splitCart){
			populateSplitCarts(model);
		}
		model.addAttribute("timeOutExtended", Boolean.valueOf(wsData.isTimeOutExtended()));
		model.addAttribute("isContractCart", isContractCart);
		JnJB2BUnitModel currentB2bUnit = sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
		model.addAttribute("indicator", currentB2bUnit.getIndicator());
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		
		
		if(jnjGTCartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}
		
		// to remove cart on clicking back button	
		sessionService.setAttribute("validatedone", Boolean.FALSE);
		if(sessionService.getAttribute("validatedone")==Boolean.TRUE)
		{
		jnjGTCartFacade.removeSessionCart();
		
		return REDIRECT_PREFIX + "/cart";
		}
		sessionService.setAttribute("validatedone", Boolean.TRUE);
		if(sessionService.getAttribute("confirmationdone")==Boolean.TRUE)
		{
		jnjGTCartFacade.removeSessionCart();
		sessionService.setAttribute("confirmationdone", Boolean.FALSE);
		return REDIRECT_PREFIX + "/cart";
		}
		
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
		.add(new Breadcrumb("/cart/validate", jnjCommonFacadeUtil.getMessageFromImpex("Shopping Cart"), null));
		breadcrumbs
				.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("Payment"), null));
		
		setPaymetricField(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
			
		String ViewPath = jnjGTCartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.PaymentContinuePage, null);	
		return getView(ViewPath);
	}
	


	public String validateCartPage(final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		JnjValidateOrderData validateOrderData = null;
		boolean splitCart = false;
		Map<String, String> zeroProductPriceMap = new HashMap<String, String>();
		Map<String, String> kitProducts = new HashMap<String, String>();
		final String orderTypes = jnjGTCartFacade.getOrderType();

		model.addAttribute("orderTypes", orderTypes);
		
		if (orderTypes != null
				&& (orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()) || orderTypes
							.equalsIgnoreCase(JnjOrderTypesEnum.ZKB.getCode()))) {

			final boolean checkDivisionValidation = jnjGTCartFacade.validateProductDivision();
			
			if (checkDivisionValidation)
			{
				redirectModel.addFlashAttribute("validationError", "hetrogenousDivisons");
				return REDIRECT_PREFIX + "/cart";
			}
		}//AAOL-2405 start
		if (orderTypes != null && (orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.KA.getCode()) || orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.KB.getCode())
				|| orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.KE.getCode()))) {
			boolean isFirstcall = wsData.getIsfirstSAPCall();
			//added for AAOL-3769
			if(!orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZOR.getCode()))
			{
				String pOvalidation=sessionService.getAttribute("poValidationResult");
				checkQuantity(model);
				model.addAttribute("pOvalidation", pOvalidation);
			}
			//End for AAOL-3769
			LOGGER.info("isFirstcall :  " + isFirstcall);
			if(isFirstcall){
				 
			      JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = jnjGTCartFacade.simulateOrderFirstSAPCall(wsData);
			      if(CollectionUtils.isNotEmpty(jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList())){
			    	  model.addAttribute("proposedOrderItemList", jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList());
			    	  
			      }
			     
			      return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.ProposedItemPopups);
			 }
		}
		//AAOL-2405 end
		try
		{
			// changes for story 4069 starts
			// Cart Model object of the logged user is retrieved by using
			// getSessionCart method of Cart Service.
			final CartModel cartModel = getCartService().getSessionCart();
			//Map<String, String> zeroProductPriceMap = new HashMap<String, String>();
			// Create Outorder Line List
			
			JnJProductModel product = null;

			for (AbstractOrderEntryModel entry : cartModel.getEntries()) {
				
				product = (JnJProductModel) entry.getProduct();
				System.out.println("Product Id ==" + product.getCode());
				System.out.println("Product Type ==" + product.getKitType());
			//	System.out.println("Is product type kit ==" + product.getKitType());
				if (entry.getBasePrice() == 0 ) {
					if(product.getKitType()!=null){
						if(!(JnjGTKitType.ORTHO.toString().equals(product.getKitType().toString()))){
							zeroProductPriceMap.put(entry.getProduct().getCode(),
							messageFacade.getMessageTextForCode("cart.common.zeroPrice.error"));
						}else{
							kitProducts.put(entry.getProduct().getCode(),messageFacade.getMessageTextForCode("cart.common.Kit.error"));
						}

					}else{
						zeroProductPriceMap.put(entry.getProduct().getCode(),
								messageFacade.getMessageTextForCode("cart.common.zeroPrice.error"));	
					}
					
				}
				
			}
			
			if(zeroProductPriceMap !=null && !zeroProductPriceMap.isEmpty()) {
				String zeropriceProdList = getInvalidProductList(zeroProductPriceMap);
				if (zeroProductPriceMap != null && !zeroProductPriceMap.isEmpty()) {
					model.addAttribute("priceError", "cart.common.zeroPrice.error");
					model.addAttribute("priceValidationErrorMsg", zeropriceProdList);
				
				}

			}
			
			// changes for story 4069 ends
			
			
			if (kitProducts != null && !kitProducts.isEmpty()) {
				String invalidProdList = getInvalidProductList(kitProducts);
				if (kitProducts != null && !kitProducts.isEmpty()) {
					model.addAttribute("KitError", "cart.common.Kit.error");
					model.addAttribute("kitValidationErrorMsg", invalidProdList);
				}

			}
			validateOrderData = jnjGTCartFacade.validateOrder(wsData);
			if (null != validateOrderData)
			{
				if(validateOrderData.getSapErrorResponse()!=null && validateOrderData.getSapErrorResponse() && !orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZOR.getCode()))
				{
					redirectModel.addFlashAttribute("sapErrorResponse", sessionService.getAttribute("sapErrorResponse"));
					return REDIRECT_PREFIX + "/cart";
				}
				final Map<String, String> codesNotFound = sessionService.getAttribute("codesNotFound");
				if (codesNotFound != null)
				{
					if (!codesNotFound.isEmpty())
					{
						String invalidProdList = getInvalidProductList(codesNotFound);
						String errorMsg = "Product(s) " + invalidProdList +" cannot be validated";
						redirectModel.addFlashAttribute("validationError", "dropshipment.error.not.found");
						redirectModel.addFlashAttribute("validationErrorMsg", invalidProdList);
						return REDIRECT_PREFIX + "/cart";
					}
				}
				
				
				final String hardstop = Config.getParameter(Jnjb2bCoreConstants.HomePage.HARD_STOP);
			
				if (hardstop.equalsIgnoreCase("yes") && validateOrderData.isHardStopError())
				{
					
					 cartService.removeSessionCart();
					redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
					return REDIRECT_PREFIX + "/cart";
				}
				else if (null != validateOrderData.getRemovedProductCodes())
				{
					for (final String removeProductKey : validateOrderData.getRemovedProductCodes().keySet())
					{
						final String[] arguments =
						{ validateOrderData.getRemovedProductCodes().get(removeProductKey).toString() };
						GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, removeProductKey, arguments);
					}
				}
				/* Code Fixes For JJEPIC-811 Start * */
				else if (validateOrderData.isCartEmpty())
				{
					redirectModel.addFlashAttribute("cartEmptyError", "cart.common.empty.error");
					return REDIRECT_PREFIX + "/cart";
				}
				/* Code Fixes For JJEPIC-811 End * */
				GlobalMessages.addConfMessage(model, "cart.review.orderSuccessfullyValidated");
			}
		}
		catch (final SystemException systemException)
		{
			LOGGER.error(Logging.VALIDATE_CART + Logging.HYPHEN + "validateCart()" + Logging.HYPHEN + "System Exception occured "
					+ systemException.getMessage(), systemException);

			redirectModel.addFlashAttribute("displayBatchModeAlert", Boolean.TRUE);
			return REDIRECT_PREFIX + "/cart";
		}
		catch (final IntegrationException integrationException)
		{
			LOGGER.error(Logging.VALIDATE_CART + Logging.HYPHEN + "validateCart()" + Logging.HYPHEN
					+ "Integration Exception occured " + integrationException.getMessage(), integrationException);

			redirectModel.addFlashAttribute("displayBatchModeAlert", Boolean.TRUE);
			return REDIRECT_PREFIX + "/cart";
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(Logging.VALIDATE_CART + Logging.HYPHEN + "validateCart()" + Logging.HYPHEN + "Business Exception occured "
					+ businessException.getMessage(), businessException);
			// In case of Excluded Products below error message is shown on the front end.
			if (StringUtils.isNotEmpty(businessException.getMessage()))
			{

				final String[] arguments =
				{ businessException.getMessage() };
				GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, "cart.common.excluded.product.error",
						arguments);
			}// Excluded Customers
			else
			{
				redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
				return REDIRECT_PREFIX + "/cart";
			}
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.VALIDATE_CART + Logging.HYPHEN + "validateCart()" + Logging.HYPHEN
					+ "Exception occured other then integration or system exception " + throwable.getMessage(), throwable);
		}
		// changes for story 4069 starts
		/*Map<String, String> priceMap = sessionService.getAttribute("priceNotThere");
		if (priceMap != null && !priceMap.isEmpty()) {
			String invalidProdList = getInvalidProductList(priceMap);
			if (priceMap != null && !priceMap.isEmpty()) {
				redirectModel.addFlashAttribute("priceError", "cart.common.zeroPrice.error");
				redirectModel.addFlashAttribute("priceValidationErrorMsg", invalidProdList);
				model.addAttribute("priceError", "cart.common.zeroPrice.error");
				model.addAttribute("priceValidationErrorMsg", invalidProdList);
				sessionService.removeAttribute("priceNotThere");
				return REDIRECT_PREFIX + "/cart";
			}

		}*/
		// changes for story 4069 starts
		model.addAttribute("validateOrderData", validateOrderData);
		model.addAttribute("timeOutExtended", Boolean.valueOf(wsData.isTimeOutExtended()));
		model.addAttribute("validationSkipped", Boolean.valueOf(wsData.isSkipSAPValidation()));
		/** Load Validate page in Model */
		
		//Changes for Bonus Item
		final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
		model.addAttribute("freeGoodsMap", freeGoodsMap);
		sessionService.setAttribute("validationFlag", true);
		
		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_VALIDATION_PAGE));
		prepareDataForPage(model, true);
		
		if(sessionService.getAttribute("populateSpliCart")!=null){
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		if(splitCart){
			populateSplitCarts(model);
		}
		
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		checkQuantity(model);
		String ViewPath = jnjGTCartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.CartValidationPage, null);
		sessionService.setAttribute("validatedone", Boolean.TRUE);
		return getView(ViewPath);
	
	}

	protected String getInvalidProductList(Map<String, String> codesNotFound) {
		
		List<String> prodList = new ArrayList<String>();
		for(Map.Entry<String, String> entry : codesNotFound.entrySet()){
			prodList.add(entry.getKey());
		}
		StringBuffer prodIds = new StringBuffer();
		
		if(prodList!=null && !(prodList.isEmpty())){
			
			for(String id :prodList){
				
				if(prodList.size()==1){
					prodIds.append(id);
				}
				else{
					prodIds.append(id + ",");
				}
				
			}
		}
		return prodIds.toString();
	}

	protected String getInvalidPriceProductList(Map<String, Double> priceMap) {

		List<String> prodList = new ArrayList<String>();
		for (Map.Entry<String, Double> entry : priceMap.entrySet()) {
			prodList.add(entry.getKey());
		}
		StringBuffer prodIds = new StringBuffer();

		if (prodList != null && !(prodList.isEmpty())) {

			for (String id : prodList) {

				if (prodList.size() == 1) {
					prodIds.append(id);
				} else {
					prodIds.append(id + ",");
				}

			}
		}
		return prodIds.toString();
	}
	@ResponseBody
	
	@RequestMapping(value = "/checkPO")
	public boolean checkPO(@RequestParam("poNumber") final String poNumber) throws CMSItemNotFoundException
	{

		return jnjGTOrderFacade.isPONumUsed(poNumber);
	}

	
	
	
		
	//GTR-1693 STARTS
	@ResponseBody
	@RequestMapping(value = "/checkPOValid")
	public boolean checkPOValidation(@RequestParam("poNumber") final String poNumber) throws CMSItemNotFoundException
	{
		final String orderType = ((JnjGTCartData) cartFacade.getSessionCart()).getOrderType();
		
		if (orderType.equalsIgnoreCase("ZDEL")
				|| orderType.equalsIgnoreCase("ZKB")
				|| orderType.equalsIgnoreCase(Config.getParameter(PO_ORDER_CART_ORDERTYPE_STANDREPLINESH)))
		{
			return false;
		}
		else
		{
			final Pattern pooderno = Pattern.compile(Config.getParameter(PO_ORDER_NO_RESTRICTION));
			final Matcher ptmtch = pooderno.matcher(poNumber);
			if (!ptmtch.matches())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	//GTR-1693 ENDS
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/updateShippingMethod")
	public boolean updateShippingMethod(@RequestParam("route") final String route,
			@RequestParam("entryNumber") final int entryNumber) throws CMSItemNotFoundException
	{

		return jnjGTCartFacade.updateShippingMethod(route, entryNumber);
	}

	@RequestMapping(value = "/checkout")

	public String checkoutCart(final Model model, final JnjGTSapWsData wsData) throws CMSItemNotFoundException
	{
		boolean splitCart = false;
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CHECKOUT_PAGE));

		}
		catch (final CMSItemNotFoundException exeption)
		{
			LOGGER.error("Order Checkout not found in current content", exeption);
		}
		sessionService.setAttribute("checkOutFlag", true);
		prepareDataForPage(model, false);
		//Changes for Bonus Item
		final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
		model.addAttribute("freeGoodsMap", freeGoodsMap);
		
		/**Changes for Dropshipment and Order Split */
		if(sessionService.getAttribute("populateSpliCart")!=null){
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		if(splitCart){
			populateSplitCarts(model);
		}
		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());
		//for bypass until business logic receive from business team for checkout option
		model.addAttribute("checkoutoption", Config.getParameter(BYEPASS_CHECKOUT_OPTION));


		final String orderTypes = ((JnjGTCartData) cartFacade.getSessionCart()).getOrderType();

		/** Checking if the order type is delivered order "ZDEL" **/
		if (orderTypes != null && orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()))
		{
			/** Adding the show signature flag **/
			model.addAttribute(SHOW_SIGNATURE, Boolean.TRUE);
		}
		setFalgForcaribPuertCustomer(model);
		model.addAttribute("timeOutExtended", Boolean.valueOf(wsData.isTimeOutExtended()));
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		String ViewPath = jnjGTCartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.CartCheckoutPage, null);
		
		if(cartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}
		
		// to remove cart on clicking back button	
		sessionService.setAttribute("validatedone", Boolean.FALSE);
		if(sessionService.getAttribute("validatedone")==Boolean.TRUE)
		{
		jnjGTCartFacade.removeSessionCart();
		
		return REDIRECT_PREFIX + "/cart";
		}
		sessionService.setAttribute("validatedone", Boolean.TRUE);
		if(sessionService.getAttribute("confirmationdone")==Boolean.TRUE)
		{
		jnjGTCartFacade.removeSessionCart();
		sessionService.setAttribute("confirmationdone", Boolean.FALSE);
		return REDIRECT_PREFIX + "/cart";
		}
		
		
		
		return getView(ViewPath);
	}

	/**
	 * This method is used to Split Cart data and validate required details.
	 * @param model
	 */
	protected void populateSplitCarts(Model model) {
		final List<JnjGTCartData> jnjCartDataList = sessionService.getAttribute("jnjCartDataList");

		// Stops split population if there is none or only one item in the cart
		if (jnjCartDataList != null && jnjCartDataList.size() == 1)
		{
			final List<OutOrderLines3> outOrderLinesList = sessionService.getAttribute("outOrderLinesList");
			OutOrderLines3 outOrderLine = null;
			for (final OrderEntryData entry : jnjCartDataList.get(0).getEntries())
			{
				outOrderLine = jnjGTCartFacade.getOutOrderLinesResult(entry.getProduct().getCode(), outOrderLinesList);				

			}

			model.addAttribute("splitCart", false);
			return;
		}

		final JnjGTCartData jnjCartData = sessionService.getAttribute("jnjCartData");
		List<OrderEntryData> orderEntryDataList = null;

		// Loop all split carts
		if(jnjCartDataList != null) {
		for (final JnjGTCartData splitCartData : jnjCartDataList)
		{
			//Finds product from split cart in the original cart and adds the whole entry to the split cart
			orderEntryDataList = new ArrayList<OrderEntryData>();
			for (final OrderEntryData entry : splitCartData.getEntries())
			{
				final String productCode = entry.getProduct().getCode();

				orderEntryDataList.add(getOrderDataEntry(productCode, jnjCartData));
			}
			splitCartData.setContractId(jnjCartData.getContractId());

			splitCartData.setEntries(orderEntryDataList);
			jnjGTCartFacade.calculateAllTotals(splitCartData);
		}
		}
		model.addAttribute("splitCart", true);
		model.addAttribute("jnjCartDataList", jnjCartDataList);
	}
	

	/**
	 * Finds an order entry given a product code
	 *
	 * @param productCode
	 * @param cartData
	 * @return
	 */
	protected OrderEntryData getOrderDataEntry(String productCode,CartData cartData) 
	{
		for (final OrderEntryData orderEntryData : cartData.getEntries())
		{
			if (orderEntryData.getProduct().getCode().equalsIgnoreCase(productCode))
			{
				return ((JnjGTOrderEntryData) orderEntryData);
			}
		}
		return null;
	}


	protected boolean validateCart(final RedirectAttributes redirectModel) throws CommerceCartModificationException
	{
		// Validate the cart
		final List<CartModificationData> modifications = cartFacade.validateCartData();
		if (!modifications.isEmpty())
		{
			redirectModel.addFlashAttribute("validationData", modifications);

			// Invalid cart. Bounce back to the cart page.
			return true;
		}
		return false;
	}



	protected void createProductList(final Model model, final boolean convertShippingmethods) throws CMSItemNotFoundException
	{
		final CartData cartData = jnjGTCartFacade.getSessionCart();
		JnjGTProductData lastbaseProduct = null;
		boolean checkisMitekProduct = false;
		boolean isMitekProduct = false;
		
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				lastbaseProduct = (JnjGTProductData) entry.getProduct();
				checkisMitekProduct = StringUtils.equals(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_MITEK),
						lastbaseProduct.getSalesOrgCode());
				if (checkisMitekProduct)
				{
					
					isMitekProduct = true;
				}
				final UpdateQuantityForm uqf = new UpdateQuantityForm();
				uqf.setQuantity(entry.getQuantity());
				model.addAttribute("updateQuantityForm" + entry.getEntryNumber(), uqf);
			}
		}
		//rama needs to check for thired party
		if (convertShippingmethods)
		{

			jnjGTCartFacade.setShippingMethodOnOrderType((JnjGTCartData) cartData);
		}
		model.addAttribute("cartData", cartData);
		sessionService.setAttribute("jnjCartData", cartData);
		model.addAttribute("isMitekProduct", isMitekProduct);
		model.addAttribute("overridePriceContractFlg",
				Config.getParameter(Jnjb2bCoreConstants.Order.PRICE_OVERRIDE_CONTRACTNUM_FLG));
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		if (cartData instanceof JnjGTCartData && ((JnjGTCartData) cartData).isContainsOCDProduct())
		{
			GlobalMessages.addInfoMessage(model, "ocd.product.alert.message");
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CART_CMS_PAGE));
	}
	
	protected void createProductList(final Model model) throws CMSItemNotFoundException
	{
		final CartData cartData = cartFacade.getSessionCart();
		JnjGTProductData lastbaseProduct = null;
		boolean checkisMitekProduct = false;
		boolean isMitekProduct = false;
		Long updateQuantity = null;

		
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				lastbaseProduct = (JnjGTProductData) entry.getProduct();
				checkisMitekProduct = StringUtils.equals(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_MITEK),
						lastbaseProduct.getSalesOrgCode());
				if (checkisMitekProduct)
				{
					
					isMitekProduct = true;
				}
				final UpdateQuantityForm uqf = new UpdateQuantityForm();
				updateQuantity = jnjGTCartFacade.updateQuantity(entry, entry.getQuantity());
				entry.setQuantity(updateQuantity);
				uqf.setQuantity(updateQuantity);
				model.addAttribute("updateQuantityForm" + entry.getEntryNumber(), uqf);
				model.addAttribute("kittypeee",lastbaseProduct.getKitType());
			}
		}
		
		jnjGTCartFacade.setShippingMethodOnOrderType((JnjGTCartData) cartData);

		model.addAttribute("cartData", cartData);
		sessionService.setAttribute("jnjCartData", cartData);
		model.addAttribute("isMitekProduct", isMitekProduct);
		model.addAttribute("overridePriceContractFlg",
				Config.getParameter(Jnjb2bCoreConstants.Order.PRICE_OVERRIDE_CONTRACTNUM_FLG));
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		if (cartData instanceof JnjGTCartData && ((JnjGTCartData) cartData).isContainsOCDProduct())
		{
			GlobalMessages.addInfoMessage(model, "ocd.product.alert.message");
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CART_CMS_PAGE));
	}

	@RequestMapping(value = "/changeOrderType")
	public String changeOrderType(final Model model, HttpSession session, @RequestParam("orderType") final String orderType, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException {
		 
		return changeOrderTypeTo(model, orderType, redirectModel);
	}
	
	public String changeOrderTypeTo( final Model model, final String orderType, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException	{
		//Update order type in cart model and reload cart page

		jnjGTCartFacade.changeOrderType(orderType);
		sessionService.setAttribute("changeOrderFlag", true);
		//CR-34 Get price call from FE after 'Change Order Type'
		redirectModel.addFlashAttribute("refreshSapPrice", Boolean.TRUE);
		return REDIRECT_PREFIX + "/cart";
	}

	@RequestMapping(value = "/getOrderType")
	public String getOrderType(final Model model, @RequestParam("currentOrderType") final String currentOrderType)
			throws CMSItemNotFoundException
	{

		final Set<String> orderTypes = jnjGTUnitFacade.getOrderTypesForAccount();


		final List<String> orderTypesInList = new ArrayList<String>(orderTypes);
		
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		if (CollectionUtils.isNotEmpty(orderTypes))
		{
			// Create a temporary list out of set so that sorting can be done
			

			// Create instance of comparator
			final JnjGTOrderTypeComparator jnjGTOrderTypeComparator = new JnjGTOrderTypeComparator();

			// Sort the list
			Collections.sort(orderTypesInList, jnjGTOrderTypeComparator);
			final Set<String> sortedOrderTypes = new HashSet<String>(orderTypesInList);
			
			
		}
		if (orderTypesInList.contains(currentOrderType))
		{
			orderTypesInList.remove(currentOrderType);
		}
		model.addAttribute("orderTypes", orderTypesInList);
		
		/*if (orderTypes.contains(currentOrderType))
		{
			orderTypes.remove(currentOrderType);
		}


		model.addAttribute("orderTypes", orderTypes);*/
		
		//String ViewPath = jnjNACartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.ChangeOrderType, null);
	 
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.ChangeOrderType);

	}

	/**
	 * Returns the price override popup
	 *
	 * @param model
	 *           the model
	 * @return the string, the represents the jsp for the override popup
	 */
	@RequestMapping(value = "/getPriceOverridePopUp")
	public String getPriceOverridePopUp(@RequestParam("entryNumber") final int entryNumber, final Model model)
			throws CMSItemNotFoundException
	{

		final OrderEntryData orderEntry = jnjGTCartFacade.getOrderEntryData(entryNumber);

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		// If order entry represents service fees product
		if (JnJCommonUtil.getValues("products.service.fees", Jnjb2bCoreConstants.SYMBOl_COMMA).contains(
				orderEntry.getProduct().getCode()))
		{
			// Populate flag to indicate that product is service fees product
			model.addAttribute("isServiceFees", Boolean.TRUE);

			// If price override reason is blank, populate the default values for override reason and approver


			if (orderEntry instanceof JnjGTOrderEntryData
					&& StringUtils.isBlank(((JnjGTOrderEntryData) orderEntry).getPriceOverrideReason()))
			{

				final JnjGTOrderEntryData jnjGTOrderEntry = (JnjGTOrderEntryData) orderEntry;
				jnjGTOrderEntry
				.setPriceOverrideReason(Jnjgtb2bMDDConstants.Cart.DEFAULT_OVERRIDDEN_REASON);
				jnjGTOrderEntry
				.setPriceOverrideApprover(Jnjgtb2bMDDConstants.Cart.DEFAULT_OVERRIDDEN_APPROVER);
			}
		}
		// Cart Entry and its entry number
		model.addAttribute("entry", orderEntry);
		model.addAttribute("entryNumber", Integer.valueOf(entryNumber));
		model.addAttribute("overridePriceWarning", Config.getParameter(Jnjb2bCoreConstants.Order.PRICE_OVERRIDE_WARNING_MESSAGE));

		// List of approvers and overridden codes to be shown in dropdowns

		model.addAttribute("approvers", jnjGTReportsFacade.getDropdownValuesInMap(Jnjgtb2bMDDConstants.Cart.APPROVERS));
		model.addAttribute("priceOverrideCodes",

				jnjGTReportsFacade.getDropdownValuesInMap(Jnjgtb2bMDDConstants.Cart.PRICE_OVERRIDE_CODES));

		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.PriceOverridePopUp);
	}

	@PostMapping("/updatePriceOverride")
	public String updatePriceOverride(@RequestParam("reasonCode") final String reasonCode,
			@RequestParam("approver") final String approver, @RequestParam("overridePrice") final double overridePrice,
			final Model model, @RequestParam("entryNumber") final int entryNumber,final HttpServletRequest request) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		if (jnjGTCartFacade.updateOverridenPrice(reasonCode, approver, overridePrice, entryNumber))
		{
			// Success in updating the overriding price
			GlobalMessages.addConfMessage(model, "basket.page.message.priceOverride");
		}
		else
		{
			// Error while overriding price
			GlobalMessages.addErrorMessage(model, "basket.page.message.errorInPrOveride");
		}
		return showCart(model,request);
	}


	@ResponseBody
	@RequestMapping(value = "/setDefaultDeliveryDate")
	public String setDefaulDeliveryDate() throws CMSItemNotFoundException
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, Integer.parseInt(JnJCommonUtil.getValue(Jnjb2bCoreConstants.DELIVERY_TIME_CONS)));
		if (calendar.get(Calendar.DAY_OF_WEEK) == 7)
		{
			calendar.add(Calendar.DATE, Integer.parseInt(Jnjb2bCoreConstants.DELIVERY_TIME_TWO));
		}
		else if (calendar.get(Calendar.DAY_OF_WEEK) == 1)
		{
			calendar.add(Calendar.DATE, Integer.parseInt(Jnjb2bCoreConstants.DELIVERY_TIME_ONE));
		}
		final Date defaultDeliveryDate = calendar.getTime();

		jnjGTCartFacade.setDefaultDeliveryDate(defaultDeliveryDate);
		return "";
	}


	protected void prepareDataForPage(final Model model, final boolean convertShippingmethods) throws CMSItemNotFoundException
	{

		final String continueUrl = (String) getSessionService().getAttribute(WebConstants.CONTINUE_URL);
		model.addAttribute(CONTINUE_URL, (continueUrl != null && !continueUrl.isEmpty()) ? continueUrl : ROOT);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//TODO: cartRestoration.tag will be not in use
		//final CartRestorationData restorationData = (CartRestorationData) sessionService
		//	.getAttribute(WebConstants.CART_RESTORATION);
		///model.addAttribute("restorationData", restorationData);

		// Check if the user is not having place order privileges
		if (sessionService.getAttribute(Jnjb2bCoreConstants.Login.ORDERING_RIGHTS) == null)
		{
			model.addAttribute("canCheckout", Boolean.valueOf(true));
		}
		model.addAttribute("creditCardDetailsForm", new CreditCardDetailsForm());
		
		//Changes for Bonus Item Quantity 
		boolean validateFlag = false;
		boolean shippingFlag = false;
		boolean reviseOrderFlag = false;
		boolean paymentFlag = false;
		boolean orderReviewFlag= false;
		
		final Boolean validateStatus = sessionService.getAttribute("validationFlag");
		final Boolean shippingStatus = sessionService.getAttribute("shippingFlag");
		final Boolean reviseOrderStatus = sessionService.getAttribute("reviseOrdderFlag");
		final Boolean paymentStatus = sessionService.getAttribute("paymentFlag");
		final Boolean reviewrStatus = sessionService.getAttribute("orderReviewFlag");
		

		if (null != validateStatus)
		{
			validateFlag = validateStatus;
		}
		if (null != shippingStatus)
		{
			shippingFlag = shippingStatus;
			sessionService.removeAttribute("shippingFlag");
		}
		if (null != reviseOrderStatus)
		{
			reviseOrderFlag = reviseOrderStatus;
			sessionService.removeAttribute("reviseOrderFlag");
		}
		if (null != paymentStatus)
		{
			paymentFlag = paymentStatus;
		}
		if (null != reviewrStatus)
		{
			orderReviewFlag = reviewrStatus;
		}
		
		if (validateFlag || shippingFlag||orderReviewFlag||paymentFlag)
		{
			sessionService.setAttribute("showCart", true);
			sessionService.removeAttribute("validationFlag");
			sessionService.removeAttribute("shippingFlag");
			sessionService.removeAttribute("paymentFlag");
			sessionService.removeAttribute("orderReviewFlag");
		}
		if(reviseOrderFlag){
			sessionService.setAttribute("showCart", false);
			sessionService.removeAttribute("reviseOrderFlag");
		}
		boolean update = false;
		boolean cartFlag = false;
		final Boolean updateStatus = sessionService.getAttribute("quantityUpdateFlag");
		final Boolean cartStatus = sessionService.getAttribute("showCart");


		if (null != updateStatus)
		{
			update = updateStatus;
			sessionService.removeAttribute("quantityUpdateFlag");
		}
		if (null != cartStatus)
		{
			cartFlag = cartStatus;
			sessionService.removeAttribute("showCart");
		}
		if (!update)
		{
			if (cartFlag)
			{
				createProductList(model, convertShippingmethods);
			}
			else
			{
				createProductList(model);
			}
		}
		else
		{
			createProductList(model);
		}

//		createProductList(model, convertShippingmethods);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.cart"));
		model.addAttribute("pageType", PageType.CART.name());

		// Get all the fields which need to be hide on FE for current user in selected Order Type

		model.addAttribute("hiddenFields", jnjGTCartFacade.getRestrictedFieldsForUser());

		// Get all mandatory fields to be shown on FE for current user in selected Order Type

		model.addAttribute("requiredFields", jnjGTCartFacade.getMandatoryFieldsForUser());

	}

	protected void prepareDataForCartPage(final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{

		final Collection<AddressData> dropShipAccounts = jnjGTUnitFacade.getAllDropShipAccounts();
		model.addAttribute("dropShipAccounts", dropShipAccounts);
		final HttpSession session=request.getSession();
		final Collection<AddressData> shippingAddressess = jnjGTUnitFacade.getShippingAddresses(); 
		final Collection<AddressData> billingAddressess = jnjGTUnitFacade.getBillingAddresses(); 
		model.addAttribute("makeThisAddrDefaultChk", Boolean.FALSE);
		model.addAttribute("makeThisAddrDefaultChangeChkForBilling", Boolean.FALSE);
		if(null!=session.getAttribute("makeThisAddrDefaultChangeChk") && session.getAttribute("makeThisAddrDefaultChangeChk")== Boolean.TRUE){
			model.addAttribute("makeThisAddrDefaultChk", Boolean.TRUE);	
		}
		if(null!=session.getAttribute("makeThisAddrDefaultChangeChkForBilling") && session.getAttribute("makeThisAddrDefaultChangeChkForBilling")== Boolean.TRUE){
			model.addAttribute("makeThisAddrDefaultChangeChkForBilling", Boolean.TRUE);	
		}
		if(null!=billingAddressess && billingAddressess.size()>0){ 
			List<AddressData> listBillingAddressess=new ArrayList<AddressData>(billingAddressess);
			if(null==session.getAttribute("updateBillingAddressClicked") || session.getAttribute("updateBillingAddressClicked")== Boolean.TRUE){ 
				model.addAttribute("billingAddress", jnjGTCartFacade.getDefaultDisplayBillingAdd(listBillingAddressess,model,request)); 
			} 

		}	
		if(null!=shippingAddressess && shippingAddressess.size()>0){ 
			List<AddressData> listShippingAddressess=new ArrayList<AddressData>(shippingAddressess);
			if(null==session.getAttribute("updateShippingAddressClicked") || session.getAttribute("updateShippingAddressClicked")== Boolean.TRUE){ 
				model.addAttribute("deliveryAddress", jnjGTCartFacade.getDefaultDisplayShippingAdd(listShippingAddressess,model,request)); 
			} 

		}
		
		model.addAttribute("shippingAddressess", shippingAddressess);
		model.addAttribute("billingAddressess", billingAddressess);
		
		Set<String> availableOrderTypes = jnjGTUnitFacade.getOrderTypesForAccount();
		model.addAttribute("orderTypes", availableOrderTypes);
		
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
		
		model.addAttribute("divisionData", jnjGTCartFacade.getPopulatedDivisionData());
		model.addAttribute("divisionList", JnJCommonUtil.getValue("divisions.show.lotcomment"));
		
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		
		//Changes for Default Order Type AAOL-4660
		boolean changeOrderFlag = false;
		//Changes for issue AAOL-6279 - Validate if linked replenishment order created
		boolean initiateReplenishFlag = false;
		final Boolean changeOrderStatus = sessionService.getAttribute("changeOrderFlag");
		final Boolean initiateReplenishStatus = sessionService.getAttribute("initiateReplenishFlag");
		if(changeOrderStatus!=null){
			changeOrderFlag = changeOrderStatus;
		}
		if(initiateReplenishStatus!=null){
			initiateReplenishFlag = initiateReplenishStatus;
		}
		final String orderType = jnjGTCartFacade.getOrderType();
		
		String defaultOrderType = null;
		if(currentUser.getDefaultOrderType()!=null){
			defaultOrderType = currentUser.getDefaultOrderType();				
		}
		
		if(!availableOrderTypes.isEmpty()){
			if(!changeOrderFlag && !initiateReplenishFlag){
				if(availableOrderTypes.contains(defaultOrderType)){
					session.setAttribute("cartorderType", defaultOrderType);
				}
				else if(availableOrderTypes.contains(JnjOrderTypesEnum.ZOR)){
					session.setAttribute("cartorderType", JnjOrderTypesEnum.ZOR);
				}
				else{
					session.setAttribute("cartorderType", orderType);
				}
			}else{
				session.setAttribute("cartorderType", orderType);
			}
			
		}
		else{
			session.setAttribute("cartorderType", orderType);
		}
		//nm3 Changes made to display Cart/Quote/Return based on the orderType
		
		
		
		
		//AAOL-2429 and AAOL-2433 changes
		if (sessionService.getAttribute(LoginaddonConstants.Login.NO_CHARGE_INTERNAL_USER)== Boolean.TRUE) {
			session.setAttribute("cartorderType", JnjOrderTypesEnum.ZNC.getCode());
		}
		//model.addAttribute("cartorderType", orderType);
		//nm3
		
		Boolean canValidate = Boolean.TRUE;
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		if (!jnjGTCartFacade.canValidateCart())
		{
			canValidate = Boolean.FALSE;
		}
		model.addAttribute("canValidateCart", canValidate);

		if (Jnjb2bCoreConstants.CONS.equals(currentSite))
		{

			model.addAttribute("reasonCode", jnjGTCartFacade.getReasonCode(Jnjb2bCoreConstants.Cart.REASON_CODE));
		}

		if (StringUtils.equals(JnjOrderTypesEnum.ZRE.getCode(), orderType))
		{
			model.addAttribute("reasonCodeReturn",

					jnjGTCartFacade.getReturunReasonCodes(Jnjb2bCoreConstants.Cart.REASON_CODE_RETURNS_EXCEPT_CSC_AND_DIST));
		}
		if (StringUtils.equals(JnjOrderTypesEnum.ZNC.getCode(), orderType))
		{

			model.addAttribute("reasonCodeNoCharge", jnjGTCartFacade.getReasonCode(Jnjb2bCoreConstants.Cart.REASON_CODE_NO_CHARGE));
		}
		if (StringUtils.equals(JnjOrderTypesEnum.ZOR.getCode(), orderType))
		{

			final Collection<CCPaymentInfoData> creditCardsInfos = jnjGTCartFacade.getAllCreditCardsInfo();
			model.addAttribute("creditCardsInfos", creditCardsInfos);
			setPaymetricField(model);
		}
		if (StringUtils.equals(JnjOrderTypesEnum.ZDEL.getCode(), orderType))
		{

			final Collection<CCPaymentInfoData> creditCardsInfos = jnjGTCartFacade.getAllCreditCardsInfo();
			model.addAttribute("creditCardsInfos", creditCardsInfos);
			// If cart contains any entry, get the 1st product and find the sales Rep UCN

			model.addAttribute("salesRepUCN", jnjGTCartFacade.getSpineOrderHeaderUCN());
			setPaymetricField(model);
		}
		/*//Soumitra AAOL-3784 - get values form SAP when the integration is done.
		if (StringUtils.equals(JnjOrderTypesEnum.KE.getCode(), orderType))
		{
			
			model.addAttribute("batchNumbers", jnjGTCartFacade.getBatchNumbersForProduct());
			model.addAttribute("serialNumbers", jnjGTCartFacade.getSerialNumbersForProduct());
		}*/
		//threshold values for price override for zdel orders
		model.addAttribute("overridePriceThreshold", Config.getParameter(Jnjb2bCoreConstants.Order.PRICE_OVERRIDE_THRESHOLD));
		model.addAttribute("maxOverridePriceThreshold",
						Config.getParameter(Jnjb2bCoreConstants.Order.MAX_PRICE_OVERRIDE_THRESHOLD));
		model.addAttribute("overridePriceContractNumMsg",
						Config.getParameter(Jnjb2bCoreConstants.Order.PRICE_OVERRIDE_CONTRACTNUM_MESSAGE));
	}

	/**
	 * Update purchase order number in Session Cart.
	 *
	 * @param model
	 *           the model
	 * @param purchaseOrde
	 *           rNumber the purchase Order Number
	 * @return the string
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePurchaseOrderNumber")
	public boolean updatePurchaseOrderNumber(final Model model,
			@RequestParam("purchaseOrderNumber") final String purchaseOrderNumber)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return jnjGTCartFacade.updatePurchaseOrderNumber(purchaseOrderNumber);
	}

	/**
	 * Update distributed purchase order number in Session Cart.
	 *
	 * @param model
	 *           the model
	 * @param distributorPONumber
	 *           the distributed purchase Order Number
	 * @return the string
	 */
	@ResponseBody
	@RequestMapping(value = "/updateDistributorPONumber")
	public boolean updateDistributorPONumber(final Model model,
			@RequestParam("distributorPONumber") final String distributorPONumber)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		return jnjGTCartFacade.updateDistributorPONumber(distributorPONumber);
	}

	/**
	 * Update distributed purchase order number in Session Cart.
	 *
	 * @param model
	 *           the model
	 * @param attention
	 *           the attention
	 * @return the string
	 */
	@ResponseBody
	@RequestMapping(value = "/updateAttention")
	public boolean updateAttention(final Model model, @RequestParam("attention") final String attention)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return jnjGTCartFacade.updateAttention(attention);
	}
	
	/**
	 * Update special text in Session Cart.
	 *
	 * @param model
	 *           the model
	 * @param attention
	 *           the specialText
	 * @return the string
	 */
	@ResponseBody
	@RequestMapping(value = "/updateSpecialText")
	public boolean updateSpecialText(final Model model, @RequestParam("specialText") final String specialText)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return jnjGTCartFacade.updateSpecialText(specialText);
	}

	/**
	 * Update third party flag.
	 *
	 * @param model
	 *           the model
	 * @param thirdPartyFlag
	 *           the Boolean flag
	 * @return the boolean, true if updated else false
	 */
	@ResponseBody
	@RequestMapping(value = "/updateThirdPartyFlag")
	public AddressData updateThirdPartyFlag(final Model model, @RequestParam("thirdPartyFlag") final Boolean thirdPartyFlag)
	{
		AddressData address = null;

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		jnjGTCartFacade.updateThirdPartyFlag(thirdPartyFlag);

		if (BooleanUtils.isFalse(thirdPartyFlag))
		{

			address = jnjGTCartFacade.resetShippingAddress();
		}
		return address;
	}



	/**
	 * The updateExpectedPrice method updates the expected price for an entry in hybris database.
	 *
	 * @param model
	 *           the model
	 * @param expectedPrice
	 *           the expected price for entry
	 * @param cartEntryId
	 *           the cart entry id
	 * @return the string
	 */
	@ResponseBody
	@RequestMapping(value = "/updateExpectedPrice")
	public String updateExpectedPrice(final Model model, @RequestParam("expectedPrice") final String expectedPrice,
			@RequestParam(value = "cartEntryId", defaultValue = "-1", required = false) final int cartEntryId)
	{
		//return jnjCartFacade.updateExpectedPrice(expectedPrice, cartEntryId);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return null;
	}

	@PostMapping("/saveCreditCardInfo")
	public String saveCreditCardInfo(@ModelAttribute("creditCardDetailsForm") final CreditCardDetailsForm form)
	{

		final CCPaymentInfoData creditCardInfoData = new CCPaymentInfoData();
		creditCardInfoData.setCardNumber(form.getCardNumber());
		creditCardInfoData.setCardType(form.getCardTypeCode());
		creditCardInfoData.setExpiryMonth(form.getExpiryMonth());
		creditCardInfoData.setExpiryYear(form.getExpiryYear());


		jnjGTCartFacade.saveCreditCardInfo(creditCardInfoData, form.isRememberCard());

		return REDIRECT_PREFIX + "/cart";
	}


	/**
	 * The updateLotNumberForEntry method updates the lot comment for an entry in hybris database.
	 *
	 * @param model
	 *           the model
	 * @param entryNumber
	 *           the entry number
	 * @param newLotComment
	 *           the lot comment
	 * @return the boolean, true if lot comment is updated
	 */
	@ResponseBody
	@RequestMapping(value = "/updateLotNumberForEntry")
	public boolean updateLotNumberForEntry(final Model model, @RequestParam("entryNumber") final int entryNumber,
			@RequestParam("newLotComment") final String newLotComment)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return jnjGTCartFacade.updateLotNumberForEntry(entryNumber, newLotComment);
	}

	/**
	 * The updateSalesRepUCN method updates the sales rep UCN at order level in hybris database.
	 *
	 * @param model
	 *           the model
	 * @param specialStockPartner
	 *           the special stock partner
	 * @param salesRepUCN
	 *           the sales rep UCN
	 * @return the boolean, true if sales rep UCN is updated
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/updateSalesRepUCN")
	public String updateSalesRepUCN(final Model model, @RequestParam("salesRepUCN") final String salesRepUCN,
			@RequestParam("specialStockPartner") final String specialStockPartner,final HttpServletRequest request) throws CMSItemNotFoundException
	{
		/*
		 * OS-23 Upon selection of a SalesRepUCN, this method will persist the header level for Spine orders it will also
		 * set the entry level specialStockPartner number if it is not set already
		 */
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		jnjGTCartFacade.updateSalesRepUCN(salesRepUCN, specialStockPartner);
		return showCart(model,request);
	}

	/**
	 * The updateSpecialStockPart method updates the special stock partner for an entry in hybris database.
	 *
	 * @param model
	 *           the model
	 * @param entryNumber
	 *           the entry number
	 * @param specialStockPartner
	 *           the special Stock Partner
	 * @return the boolean, true if special Stock Partner is updated
	 */
	@ResponseBody
	@RequestMapping(value = "/updateSpecialStockPart")
	public boolean updateSpecialStockPart(final Model model, @RequestParam("entryNumber") final int entryNumber,
			@RequestParam("specialStockPartner") final String specialStockPartner)
	{
		/* OS-23 This persists a user entered specialStockPartner */
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return jnjGTCartFacade.updateSpecialStockPartner(specialStockPartner, entryNumber);
	}

	/*
	 * @RequestMapping(value = "/checkValidProduct")
	 *
	 * @ResponseBody public String isProductCodeValid(@RequestParam("productId") final String productId) { String
	 * isProductCodeValid = null; if (StringUtils.isNotEmpty(productId)) { isProductCodeValid =
	 * jnjNACartFacade.validateProductCode(productId); } if (StringUtils.isNotBlank(isProductCodeValid)) { return
	 * isProductCodeValid; } return Jnjb2bCoreConstants.Cart.VALID_PRODUCTCODE; }
	 */

	@ResponseBody
	@RequestMapping(value = "/updatePaymentInfo")
	public boolean updatePaymentInfo(@RequestParam("selectCard") final String paymentInfoId)
	{

		return jnjGTCartFacade.updatePaymentInfo(paymentInfoId);
	}

	@PostMapping("/updateShippingAddress")
	public String updateShippingAddress(final Model model, @RequestParam(value = "shippingAddress") final String shippingAddrId,final HttpServletRequest request)
	{ 
		 
	return updateShippingAddressInCart(model, shippingAddrId,request);
	}
	
    protected String updateShippingAddressInCart(final Model model, final String shippingAddrId,final HttpServletRequest request)
	{
    	final HttpSession session=request.getSession();
    	session.setAttribute("updateShippingAddressClicked", Boolean.FALSE);
		if (null != shippingAddrId && (!shippingAddrId.isEmpty()))
		{

			final AddressData shippingAddData = jnjGTCartFacade.chnageShippingAddress(shippingAddrId,request);
			
			model.addAttribute("deliveryAddress", shippingAddData);
			session.setAttribute("defaultChekAddid", shippingAddrId);
		}
		if (sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME) != null && Jnjb2bCoreConstants.MDD
				.equalsIgnoreCase(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME).toString()))
		{              
			/*return ControllerConstants.Views.Pages.Cart.DeliveryAddressPage;*/
			session.setAttribute("skipping", "billing");
			return REDIRECT_PREFIX + "/cart";
		}
		else
		{
			/*Added the below changes for consumer ECPH-2154*/
			return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.DeliveryAddressPage);
		}
	}
	
    
	@PostMapping("/updateBillingAddress")
	public String updateBillingAddress(final Model model, @RequestParam(value = "billingAddress") final String billingAddrId,final HttpServletRequest request)
	{ 
		 
	return updateBillingAddressInCart(model, billingAddrId,request);
	}
	
    protected String updateBillingAddressInCart(final Model model, final String billingAddrId,final HttpServletRequest request)
	{
    	final HttpSession session=request.getSession();
    	session.setAttribute("updateBillingAddressClicked", Boolean.FALSE);
		if (null != billingAddrId && (!billingAddrId.isEmpty()))
		{

			final AddressData billingAddData = jnjGTCartFacade.changeBillingAddress(billingAddrId,request);
			
			model.addAttribute("billingAddress", billingAddData);
			session.setAttribute("defaultCheckforBillingAddid", billingAddrId);
		}
		if (Jnjb2bCoreConstants.MDD.equalsIgnoreCase(String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME))))
		{              
			/*return ControllerConstants.Views.Pages.Cart.DeliveryAddressPage;*/
			
			session.setAttribute("skipping", "shipping");
			return REDIRECT_PREFIX + "/cart";
		}
		else
		{
			/*Added the below changes for consumer ECPH-2154*/
			return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.DeliveryAddressPage);
		}
	}
	
	

	/**
	 * Update distributed purchase order number in Session Cart.
	 *
	 * @param model
	 *           the model
	 * @param dropShipAccountId
	 *           the attention
	 * @return the string
	 */
	@RequestMapping(value = "/updateDropShipAccount")
	public String updateDropShipAccount(final Model model, @RequestParam("dropShipAccount") final String dropShipAccountId)
	{
		try
		{

			final AddressData dropShipAddData = jnjGTCartFacade.updateDropShipAccount(dropShipAccountId);
			if (dropShipAddData != null)
			{
				model.addAttribute("deliveryAddress", dropShipAddData);
			}
			model.addAttribute("errorFlag", ERROR_NOT_COMES);
		}
		catch (final BusinessException e)
		{
			model.addAttribute("errorFlag", ERROR_COMES);
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.DeliveryAddressPage);
	}


	/**
	 * Updates the shipping address to default and clears data.
	 *
	 * @param model
	 *           the model
	 * @return the string
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/updateDefaultDelAdd")
	public String updateDefaultDelAdd(final Model model) throws CMSItemNotFoundException
	{

		final AddressData deliveryAddress = jnjGTUnitFacade.getShippingAddress();

		model.addAttribute("deliveryAddress", deliveryAddress);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		jnjGTCartFacade.setShippingAddress(deliveryAddress);


		jnjGTCartFacade.clearDropShipPurchaseOrderNum();

		/*return ControllerConstants.Views.Pages.Cart.DeliveryAddressPage;*/
		return "";
	}

	/**
	 * Returns the drop ship popup
	 *
	 * @param model
	 *           the model
	 * @return the string
	 */
	@RequestMapping(value = "/getDropShipAccounts")
	public String getDropShipAccounts(final Model model) throws CMSItemNotFoundException
	{

		final Collection<AddressData> dropShipAccounts = jnjGTUnitFacade.getAllDropShipAccounts();
		model.addAttribute("dropShipAccounts", dropShipAccounts);
		/*return ControllerConstants.Views.Pages.Cart.DropShipAccountPage;*/
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.DropShipAccountPage);
	}

	@RequestMapping(value = "/getSurgeryInfo")

	public String getSurgeryInfo(final JnjGTViewSurgeryInfoData surgeryInfoData, final Model model)
			throws CMSItemNotFoundException
	{
    	surgeryInfoData.setSurgerySpecialty(jnjGTCartFacade.getSurgeryValues("surgerySpecialty", null));
		surgeryInfoData.setOrthobiologics(jnjGTCartFacade.getSurgeryValues("orthobiologics", null));
		surgeryInfoData.setInterbody(jnjGTCartFacade.getSurgeryValues("interbody", null));
		surgeryInfoData.setLevelsInstrumented(jnjGTCartFacade.getSurgeryValues("levelsInstrumented", null));
		surgeryInfoData.setInterbodyFusion(jnjGTCartFacade.getSurgeryValues("interbodyFusion", null));
		surgeryInfoData.setPathology(jnjGTCartFacade.getSurgeryValues("pathology", null));
		surgeryInfoData.setCas(jnjGTCartFacade.getSurgeryValues("cas", null));
		surgeryInfoData.setProcedureType(jnjGTCartFacade.getSurgeryValues("procedureType", null));
		surgeryInfoData.setSurgicalApproach(jnjGTCartFacade.getSurgeryValues("surgicalApproach", null));
		surgeryInfoData.setZone(jnjGTCartFacade.getSurgeryValues("zone", null));
		model.addAttribute("surgeryInfoData", surgeryInfoData);

		model.addAttribute("savedSurgeryInfo", jnjGTCartFacade.getSavedSurgeryInfo());

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.SurgeryInfo);
		//return"";
	}
	

	@PostMapping("/saveSurgeryInfo")

	public String saveSurgeryInfo(final JnjGTSurgeryInfoData infoForm, final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{

		jnjGTCartFacade.saveSurgeryInfo(infoForm);
		return showCart(model,request);
	}

	@PostMapping("/saveOneTimeShipping")
	public String saveOneTimeShipping(final OneTimeShippingAddForm shippingAddForm, final Model model,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Start:- save one time address for cart");
		}

		final JnjGTAddressData deliveryAddress = new JnjGTAddressData();

		deliveryAddress.setFirstName(shippingAddForm.getName());
		deliveryAddress.setLine1(shippingAddForm.getAddress1());
		deliveryAddress.setLine2(shippingAddForm.getAddress2());
		deliveryAddress.setTown(shippingAddForm.getCity());
		//AAOM-6890
		deliveryAddress.setAttnLine(shippingAddForm.getAttnLine());

		final RegionData region = new RegionData();
		region.setIsocode(shippingAddForm.getState());
		region.setCountryIso(US_COUNTRY_CODE);
		deliveryAddress.setRegion(region);
		deliveryAddress.setCountry(i18NFacade.getCountryForIsocode(US_COUNTRY_CODE));
		deliveryAddress.setPostalCode(shippingAddForm.getPostalCode());


		jnjGTCartFacade.setShippingAddress(deliveryAddress);
		jnjGTCartFacade.setCustomShippingAddress(true);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("End:- save one time address for cart");
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return showCart(model,request);
	}


	/**
	 * The updateDeliveryDateForCart method is used to persist the expected delivery date for the all the entry
	 * associated with the session cart .
	 *
	 * @param model
	 *           the model
	 * @param expDeliveryDate
	 *           the exp delivery date
	 * @return the string
	 */

	@ResponseBody
	@RequestMapping(value = "/updateDeliveryDateForCart")
	public boolean updateDeliveryDate(final Model model, @RequestParam("expDeliveryDate") final String expDeliveryDate)
			throws CMSItemNotFoundException
	{
		boolean success = false;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("add expectedDeliveryDate for cart entries starts");
		}
		if (StringUtils.isNotEmpty(expDeliveryDate))
		{

			success = jnjGTCartFacade.updateNamedDeliveryDate(expDeliveryDate);
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return success;
	}

	@RequestMapping(value = "/updateReasonCode")
	public String updateReasonCode(final Model model, @RequestParam("reasonCode") final String reasonCode,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("add reason code in the cart");
		}

		if (StringUtils.isNotEmpty(reasonCode))
		{


			jnjGTCartFacade.updateReasonCode(reasonCode);
		}
		else
		{
			LOGGER.error("Add reason code is Null or empty");
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		return showCart(model,request);

	}

	@RequestMapping(value = "/requestQuote")
	public String requestQuote(final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final CartModel cartModel = cartService.getSessionCart();
		JnjGTGetPriceQuoteResponseData jnjGTGetPriceQuoteResponseData = new JnjGTGetPriceQuoteResponseData();
		String sapOrderNumber = null;
		
	if ((sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE)).equals(JnjGTUserTypes.PORTAL_ADMIN) || sessionService.getAttribute(Jnjb2bCoreConstants.Login.ORDERING_RIGHTS) == null )
		{
			model.addAttribute("canCheckout", Boolean.valueOf(true));
		}
		try
		{
				//kit products - start
				List<String> orthoKitProductsList = new ArrayList<>();
				List<AbstractOrderEntryModel> cartEntriesList = new ArrayList<>();
	
				if(CollectionUtils.isNotEmpty(cartModel.getEntries()))
				{
					cartEntriesList = cartModel.getEntries();
				}
				List<Integer> orderEntryNumberList = new ArrayList<>();
	
				if(CollectionUtils.isNotEmpty(cartEntriesList))
				{
					Iterator<AbstractOrderEntryModel> cartEntryIterator = cartEntriesList.iterator();
	
					JnJProductModel product = null;
					while (cartEntryIterator.hasNext()) 
					{
						AbstractOrderEntryModel abstractOrderEntryModel = cartEntryIterator.next();
						product = (JnJProductModel) abstractOrderEntryModel.getProduct();
						if(null != product.getKitType() && JnjGTKitType.ORTHO.toString().equals(product.getKitType().toString()))
						{
							orthoKitProductsList.add(product.getCode());
							orderEntryNumberList.add(abstractOrderEntryModel.getEntryNumber());
						}
					}
				}
	
				if(CollectionUtils.isNotEmpty(orderEntryNumberList))
				{
					//reverse iterator
					ListIterator<Integer> entryNumberIterator =  orderEntryNumberList.listIterator(orderEntryNumberList.size());
	
					while(entryNumberIterator.hasPrevious()) {
						//set the quantity to 0 to remove the cart entry.
						try
						{
							cartFacade.updateCartEntry(entryNumberIterator.previous(),0);
							
						}
						catch (Exception exp)
						{
							LOGGER.debug("Exception in this line : cartFacade.updateCartEntry(iterator1.previous(),0);" + exp);
						}
					}
				}
	
				model.addAttribute("orthoKitProductsList" , orthoKitProductsList);
				prepareDataForPage(model, false);	
				//kit products - end
			
			
			//SAP call to validate Quote Request
			/*jnjGTGetPriceQuoteResponseData = jnjGTCartFacade.quoteSAPValidation();*/
			
			jnjGTGetPriceQuoteResponseData.setSapResponseStatus(true);
			if (jnjGTGetPriceQuoteResponseData.isSapResponseStatus())
			{

				sapOrderNumber = jnjGTCheckoutFacade.placeQuoteOrder();
				
			}		
			else if (jnjGTGetPriceQuoteResponseData.isCartEmpty())
			{
				redirectModel.addFlashAttribute("cartEmptyError", "cart.common.empty.error");
				return REDIRECT_PREFIX + "/cart";
			}
			/* Code Fixes For JJEPIC-811 End * */
			else
			{
				redirectModel.addFlashAttribute("customerExcludedError", jnjGTGetPriceQuoteResponseData.getErrorMessage());
				return REDIRECT_PREFIX + "/cart";
			}
			
		   if(orthoKitProductsList!=null&&cartModel.getEntries().size()==0){
			   redirectModel.addFlashAttribute("orthoKitProductsList" , orthoKitProductsList);
				return REDIRECT_PREFIX + "/cart";
			}
		}
		catch (final InvalidCartException exception)
		{
			LOGGER.error(Logging.VALIDATE_CART + Logging.HYPHEN + "requestQuote()" + Logging.HYPHEN
					+ "Order Validation not found in current content" + exception.getMessage(), exception);
		}
		/*catch (final BusinessException businessException)
		{
			LOGGER.error(Logging.VALIDATE_CART + Logging.HYPHEN + "requestQuote()" + Logging.HYPHEN + "Business Exception occured "
					+ businessException.getMessage(), businessException);
			// In case of Excluded Products below error message is shown on the front end.
			if (StringUtils.isNotEmpty(businessException.getMessage()))
			{
				final String message = jnjCommonFacadeUtil.getMessageFromImpex("cart.price.quote.excluded.product.error");
				if (StringUtils.isNotEmpty(message))
				{
					final String errorMessage = message.replace(Config.getParameter(Cart.EXCLUDED_PRODUCTS),
							businessException.getMessage());
					redirectModel.addFlashAttribute("excludedProducts", errorMessage);
					return REDIRECT_PREFIX + "/cart";
				}
			}/// Excluded Customers
			else
			{
				redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
				return REDIRECT_PREFIX + "/cart";
			}
		}*/
		/*catch (final IntegrationException | ParseException | SystemException exception)
		{
			LOGGER.error(Logging.VALIDATE_CART + Logging.HYPHEN + "requestQuote()" + Logging.HYPHEN + "SAP Integeration Exception"
					+ exception.getMessage(), exception);
			redirectModel.addFlashAttribute("sapValidationError", Boolean.TRUE);
			return REDIRECT_PREFIX + "/cart";
		}*/
		if (null != jnjGTGetPriceQuoteResponseData&& StringUtils.isNotEmpty(jnjGTGetPriceQuoteResponseData.getExcludedProductCodes()))
		{
			final String message = jnjCommonFacadeUtil.getMessageFromImpex("cart.price.quote.excluded.product.error");
			if (StringUtils.isNotEmpty(message))
			{
				final String errorMessage = message.replace(Config.getParameter(Cart.EXCLUDED_PRODUCTS),
						jnjGTGetPriceQuoteResponseData.getExcludedProductCodes());
				model.addAttribute("excludedProducts", errorMessage);
			}
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute("sapOrderNumber", sapOrderNumber);
		
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.quoteResultPage);
	}

	@ResponseBody
	@RequestMapping(value = "/updateLotNumber")
	public boolean updateLotNumberNumber(@RequestParam("lotNumber") final String lotNumber,
			@RequestParam("pcode") final String pcode,
			@RequestParam("entryNumber") final int entryNumber)
	{

		return jnjGTCartFacade.updateLotNumberForOrderEntry(lotNumber, pcode,entryNumber);
	}

	@ResponseBody
	@RequestMapping(value = "/updatePONumber")
	public boolean updatePONumber(@RequestParam("poNumber") final String poNumber,
			@RequestParam("entryNumber") final int entryNumber)
	{

		return jnjGTCartFacade.updatePONumberForOrderEntry(poNumber, entryNumber);
	}

	@ResponseBody
	@RequestMapping(value = "/updateInvoiceNumber")
	public boolean updateInvoiceNumber(@RequestParam("invoiceNumber") final String invoiceNumber,
			@RequestParam("entryNumber") final int entryNumber)
	{

		return jnjGTCartFacade.updateInvoiceNumberForOrderEntry(invoiceNumber, entryNumber);
	}

	@ResponseBody
	@RequestMapping(value = "/updateCustomerPo")
	public boolean updateCustomerPO(@RequestParam("customerPo") final String customerPO)
	{

		return jnjGTCartFacade.updateCustomerPONumber(customerPO);
	}

	@ResponseBody
	@RequestMapping(value = "/updateCordisHouseAccount")
	public boolean updateCordisHouseAccount(@RequestParam("cordisHouseAccount") final String cordisHouseAccount)
	{

		return jnjGTCartFacade.updateCordisHouseAccount(cordisHouseAccount);
	}

	@RequestMapping(value = "/updateReasonCodeReturn")
	public String updateReasonCodeReturn(final Model model, @RequestParam("reasonCodeReturn") final String reasonCode,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		if (StringUtils.isNotEmpty(reasonCode))
		{


			jnjGTCartFacade.updateReasonCode(reasonCode);
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return showCart(model,request);
	}

	@RequestMapping(value = "/updateReasonCodeNoCharge")
	public String updateReasonCodeNoCharge(final Model model, @RequestParam("reasonCodeNoCharge") final String reasonCode,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		if (StringUtils.isNotEmpty(reasonCode))
		{


			jnjGTCartFacade.updateReasonCode(reasonCode);
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return showCart(model,request);
	}

	@PostMapping("/surgeonData")
	public String getSurgeonData(final Model model,
			@RequestParam(value = "loadMoreCounter", defaultValue = "1") final int loadMoreCounter,
			@RequestParam(value = "searchPattern", required = false) final String searchPattern)
	{
		model.addAttribute("pagedSurgeonData", orderFacade.getSurgeonData(searchPattern, loadMoreCounter));
		model.addAttribute("loadMoreCounter", Integer.valueOf(loadMoreCounter));
		model.addAttribute("searchPattern", searchPattern);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.SurgeonPopUp);
		//return "";
				
	}

	@PostMapping("/updateSurgeonData")
	@ResponseBody
	public String updateSurgeon(final String selectedSurgeonId, final String selectSurgeonName, final String hospitalId)
			throws CMSItemNotFoundException
	{

		return jnjGTCartFacade.updateSurgeon(selectedSurgeonId, selectSurgeonName, hospitalId);
	}
/*Added for JJEPIC 720 Start*/
	
	@PostMapping("/deleteDeliveredOrderFile")
	@ResponseBody
	public String deleteDeliveredOrderFile() throws CMSItemNotFoundException
	{
		final boolean isFileDeleted = jnjGTCartFacade.deleteMediaModelFromCart();

		return isFileDeleted ? "Success" : "Failure";
	}
	
	/*Added for JJEPIC 720 Ends */

	@PostMapping("/deliveredOrderFileUpload")
	public String deliveredOrderFileUpload(
			@RequestParam(value = "deliveredOrderDoc", required = false) final CommonsMultipartFile deliveredOrderDoc,
			final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{

		//final String filePath = null;
		boolean isUploadSucces = false;

		if (deliveredOrderDoc != null && deliveredOrderDoc.getOriginalFilename() != null
				&& !deliveredOrderDoc.getOriginalFilename().isEmpty())
		{
			//filePath = JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.CART_FILE_UPLOAD_PATH);

			//final File file = new File(filePath + deliveredOrderDoc.getOriginalFilename());

			//if (!file.getParentFile().exists())
			//	{
			//	file.getParentFile().mkdirs();
			//	}
			try
			{
				final File file = File.createTempFile(deliveredOrderDoc.getOriginalFilename(), ".tmp");
				//file.createNewFile();
				deliveredOrderDoc.transferTo(file);

      				jnjGTCartFacade.createMediaModelForCart(file, null);
                                 /*FileUtils.deleteQuietly(file);*///delete the file once the media is created.
				isUploadSucces = true;
			}
			catch (IllegalStateException | IOException exception)
			{

				LOGGER.error("CART_PAGE_CONTROLLER" + "deliveredOrderFileUpload()" + Logging.HYPHEN + "Error in Uploading File"
						+ exception.getMessage(), exception);
			}
			catch (final BusinessException exception)
			{
				LOGGER.error("CART_PAGE_CONTROLLER" + "deliveredOrderFileUpload()" + Logging.HYPHEN
						+ "Error in converting File to media" + exception.getMessage(), exception);
			}
		}
		
		/*<!-- Added JJEPIC-720 Starts -->*/
		final String message = isUploadSucces ? messageService
				.getMessageForCode(Cart.DELIVERED_ORDER_FORM_UPLOAD_SUCCESS, null, "") : messageService.getMessageForCode(
				Cart.DELIVERED_ORDER_FORM_UPLOAD_FAILURE, null, "");
		redirectModel.addFlashAttribute("formUploadMessage", message);
		redirectModel.addFlashAttribute("formUploadStatus", Boolean.valueOf(isUploadSucces));
		/*<!--  Added JJEPIC-720 Ends -->*/
		
		
		return REDIRECT_PREFIX + "/cart";
	}
	
	/*Added for AAOL-4937*/
	@SuppressWarnings("rawtypes")
	@PostMapping("/uploadFileforReturn")
	public String uploadFileforReturn(@ModelAttribute("returnUploadForm") ReturnFileUploadForm returnUploadForm)throws CMSItemNotFoundException
	{
		boolean isReturnImageUploaded = false;
		@SuppressWarnings("unchecked")
		List<MultipartFile> returnUploadFiles = returnUploadForm.getFiles();
		//Changes for AAOL-5809
		isReturnImageUploaded = jnjGTCartFacade.saveReturnMedia(returnUploadFiles);
		sessionService.setAttribute("isReturnImageUploaded", isReturnImageUploaded);
		return REDIRECT_PREFIX + "/cart";
	}	
	/*End of AAOL-4937*/
	
	@PostMapping("/updateFreightCost")
	public String updateFreightCost(@RequestParam("freightCost") final double freightCost)
	{

		jnjGTCartFacade.updateFreightCost(freightCost);
		return REDIRECT_PREFIX + "/cart";
	}

	/**
	 * Sets the paymetric field.
	 *
	 * @param model
	 *           the new paymetric field
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	public void setPaymetricField(final Model model) throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.SAVE_CREDIT_CARD_INFO + Logging.HYPHEN + "setPaymetricField()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		// Specify the GUID here.
		final String myGuid = Config.getParameter(Jnjb2bCoreConstants.PayMetrics.MY_GUID);
		final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		final HttpServletRequest request = attr.getRequest();
		final String url = JnjWebUtil.getServerUrl(request);
		// specify the XML payload here
		final String xmlPayload = Config.getParameter(Jnjb2bCoreConstants.PayMetrics.XML_PAYLOAD_BEFORE_URL).concat(url)
				.concat(Config.getParameter(Jnjb2bCoreConstants.PayMetrics.XML_PAYLOAD_AFTER_URL));
		// setup the mac preshared key
		final javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(Config.getParameter(
				Jnjb2bCoreConstants.PayMetrics.SECRET_KEY_SPEC).getBytes(),
				Config.getParameter(Jnjb2bCoreConstants.PayMetrics.SECRET_KEY_SPEC_ALGORITHM));
		// setup the MAC Hashing object
		javax.crypto.Mac mac = null;
		try
		{
			mac = javax.crypto.Mac.getInstance(Config.getParameter(Jnjb2bCoreConstants.PayMetrics.SECRET_KEY_SPEC_ALGORITHM));
			// initialize the mac object with the preshared key.

			mac.init(keySpec);
		}
		catch (final NoSuchAlgorithmException noSuchAlgException)
		{
			LOGGER.error(Logging.SAVE_CREDIT_CARD_INFO + Logging.HYPHEN + "setPaymetricField()" + Logging.HYPHEN
					+ "NoSuchAlgorithmException Exception occured " + noSuchAlgException.getMessage(), noSuchAlgException);
		}
		catch (final InvalidKeyException invalidKeyException)
		{
			LOGGER.error(Logging.SAVE_CREDIT_CARD_INFO + Logging.HYPHEN + "setPaymetricField()" + Logging.HYPHEN
					+ "InvalidKeyException Exception occured " + invalidKeyException.getMessage(), invalidKeyException);
		}
		// setup the base64 encoding object.
		//final sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		// generate the signature
		final String signature = Base64.getEncoder().encodeToString(mac.doFinal(xmlPayload.getBytes()));
		// generate the payload that include the XML and the signature. This will be written to the html later.
		final String SignedPayload = Config.getParameter(Jnjb2bCoreConstants.PayMetrics.SIGN_PAYLOAD_MYGUID).concat(myGuid)
				.concat(Config.getParameter(Jnjb2bCoreConstants.PayMetrics.SIGN_PAYLOAD_SIGNATURE)).concat(signature)
				.concat(Config.getParameter(Jnjb2bCoreConstants.PayMetrics.SIGNATURE_TAG_END)).concat(xmlPayload)
				.concat(Config.getParameter(Jnjb2bCoreConstants.PayMetrics.MERCHANT));
		final String payMetricUrl = Config.getParameter(Jnjb2bCoreConstants.PayMetrics.URL).concat(
				Config.getParameter(Jnjb2bCoreConstants.PayMetrics.REMAINING_URL).concat(myGuid));
		model.addAttribute("XMLPayload", xmlPayload);
		model.addAttribute("SignedPayload", SignedPayload);
		model.addAttribute("MyGUID", myGuid);
		model.addAttribute("URL", payMetricUrl);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.SAVE_CREDIT_CARD_INFO + Logging.HYPHEN + "setPaymetricField()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
	}

	/**
	 * Save value in session.
	 */
	@RequestMapping(value = "/saveValueInSession")
	protected void saveValueInSession()
	{
		sessionService.setAttribute("remember", "true");
	}

	@RequestMapping(value = "/paymentSuceess")
	protected String saveCreditCardInfo(final HttpServletRequest request,final RedirectAttributes redirectModel) throws CMSItemNotFoundException, IOException,
			NoSuchAlgorithmException, InvalidKeyException, XMLStreamException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.SAVE_CREDIT_CARD_INFO + Logging.HYPHEN + "saveCreditCardInfo()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean isRememberSet = false;
		if (null != sessionService.getAttribute("remember"))
		{
			isRememberSet = true;
			sessionService.removeAttribute("remember");
		}


		String response = jnjGTCartFacade.savePayMetricsResponse(request, isRememberSet);
		
		if(response.equalsIgnoreCase("Success")){
			redirectModel.addFlashAttribute("payCreditcard", "SUCCESS");
		}else{
			redirectModel.addFlashAttribute("payCreditcard", "FAILED");
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.SAVE_CREDIT_CARD_INFO + Logging.HYPHEN + "saveCreditCardInfo()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return REDIRECT_PREFIX + "/cart/paymentContinue";
	}

	@PostMapping("/initiateReplenish")
	public String initiateReplenishOrder(final Model model, @RequestParam(value = "orderNum") final String orderNum)
			throws CMSItemNotFoundException
	{
		try
		{

			jnjGTCartFacade.initiateReplenishForDelivered(orderNum);
			sessionService.setAttribute("initiateReplenishFlag", true);
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.INITIATE_REPLENISH_ORDER + Logging.HYPHEN + "initiateReplenishOrder()"
					+ Logging.HYPHEN + "B2bUnit does not exists for spl stock partner while initiating Replenish for Del order"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			return REDIRECT_PREFIX + "/home";
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return REDIRECT_PREFIX + "/cart";
		//prepareDataForCartPage(model);
		//return jnjNACartFacade.getPathForView(ControllerConstants.Views.Pages.Cart.CartPage, null);
	}




	/*@RequestMapping(value = "/pcmCart", method =
	{ RequestMethod.POST, RequestMethod.GET })
	public String getPCMCart(final Model model, final PCMShowMoreForm form) throws CMSItemNotFoundException
	{

		final RemovePCMCartProductForm removePCMCartProductForm = new RemovePCMCartProductForm();
		final String thresholdValue = JnJCommonUtil.getValue(JnjPCMCoreConstants.Cart.THRESHOLD_VALUE_OF_PRODUCTS);

		final PageableData pageableData = createPageableData(0, form.getPageSize(), ShowMode.Page);
		final int finalPageSize = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
		//prepare DATA for CART[START]
		final CartData cartData = jnjNACartFacade.getSessionCart();
		final int numberOfEntries = cartData.getEntries().size();

		if (finalPageSize <= numberOfEntries)
		{
			model.addAttribute("entries", cartData.getEntries().subList(0, finalPageSize));
		}
		else
		{
			model.addAttribute("entries", cartData.getEntries());
		}
		//prepare DATA for CART[END]
		model.addAttribute("groups",
				JnJCommonUtil.getValues(JnjPCMCoreConstants.Cart.SHOW_IN_GROUPS, Jnjb2bCoreConstants.SYMBOl_COMMA));
		model.addAttribute("showMoreCounter", String.valueOf(form.getShowMoreCounter()));
		model.addAttribute("pageableData", pageableData);
		model.addAttribute("pcmShowMoreForm", form);
		model.addAttribute("cartData", cartData);

		model.addAttribute("prodRemovePCMCartForm", removePCMCartProductForm);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.pcm.cart"));

		model.addAttribute("thresholdValue", thresholdValue);

		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CMS_PAGE));

		return PcmControllerConstants.Views.Pages.Cart.CART_PAGE;
	}*/

	@GetMapping("/getConfigPage")
	public String getCustomExportConfig(final Model model) throws CMSItemNotFoundException

	{
		boolean showThresholdMessage = false;
		String localizationString = "";

		
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		/*final SelectedAttributesForm selectedAttributesDTO = new SelectedAttributesForm();*/

		/*final String currentCountryIsocode = getCurrentCountry().getIsocode();

		if (StringUtils.equals(JnjPCMCoreConstants.US, currentCountryIsocode))
		{
			if (StringUtils.contains(Config.getParameter(JnjPCMCoreConstants.Cart.PDP_LOCALE_FLAG_US), "es"))
			{
				localizationString = "US_ES";
			}
			else
			{
				localizationString = "US";
			}
		}
		else if (StringUtils.equals(JnjPCMCoreConstants.CA, currentCountryIsocode))
		{
			if (StringUtils.contains(Config.getParameter(JnjPCMCoreConstants.Cart.PDP_LOCALE_FLAG_CA), "fr"))
			{
				localizationString = "CA_FR";
			}
			else
			{
				localizationString = "CA";
			}
		}*/

		/*final String thresholdValue = JnJCommonUtil.getValue(JnjPCMCoreConstants.Cart.THRESHOLD_VALUE_OF_PRODUCTS);*/

		final int numberOfEntries = jnjGTCartFacade.getNumberOfCartEntriesInSessionCart();

		/*final List fileTypes = JnJCommonUtil.getValues(JnjPCMCoreConstants.Cart.DOWNLOAD_CART_FILE_TYPES,
				Jnjb2bCoreConstants.SYMBOl_SEMI_COLON);
		model.addAttribute("fileTypes", fileTypes);*/

	/*	final List imageTypes = JnJCommonUtil.getValues(JnjPCMCoreConstants.Cart.DOWNLOAD_CART_IMAGE_FORMATS,
				Jnjb2bCoreConstants.SYMBOl_SEMI_COLON);
		model.addAttribute("imageFormats", imageTypes);*/

		/*final List masterProductData = JnJCommonUtil.getValues(JnjPCMCoreConstants.Cart.DOWNLOAD_CART_MASTER_PRODUCT_DATA
				+ localizationString, Jnjb2bCoreConstants.SYMBOl_SEMI_COLON);
		model.addAttribute("masterProductData", masterProductData);*/

	/*	final List labellingData = JnJCommonUtil.getValues(JnjPCMCoreConstants.Cart.DOWNLOAD_CART_LABELLING_DATA
				+ localizationString, Jnjb2bCoreConstants.SYMBOl_SEMI_COLON);
		model.addAttribute("labellingData", labellingData);*/

		/*final List richContent = JnJCommonUtil.getValues(JnjPCMCoreConstants.Cart.DOWNLOAD_CART_RICH_CONTENT + localizationString,
				Jnjb2bCoreConstants.SYMBOl_SEMI_COLON);
		model.addAttribute("richContent", richContent);*/

		/*final List imagery = JnJCommonUtil.getValues(JnjPCMCoreConstants.Cart.DOWNLOAD_CART_IMAGERY + localizationString,
				Jnjb2bCoreConstants.SYMBOl_SEMI_COLON);
		model.addAttribute("imagery", imagery);*/
		/*if (numberOfEntries > Integer.parseInt(thresholdValue))
		{
			showThresholdMessage = true;
		}*/
		model.addAttribute("showThresholdMessage", showThresholdMessage);
		/*model.addAttribute("selectedAttributesForm", selectedAttributesDTO);*/
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.pcm.cart"));

		/*model.addAttribute("thresholdValue", thresholdValue);*/

		storeCmsPageInModel(model, getContentPageForLabelOrId(PCM_CART_CONFIGURATION_CMS_PAGE));
		/*return PcmControllerConstants.Views.Pages.Cart.EXPORT_CONFIG_PAGE;*/
		return"";
	}

	@PostMapping("/getExportConfirmationPage")
	public String getExportConfirmationPage(final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		/*if (null == selectedAttributesForm.getImageFormat())
		{
			final List<String> imageFormat = new ArrayList<String>();
			imageFormat.add(JnjPCMCoreConstants.Cart.CART_IMAGE_FORMAT_NO_IMAGES);
			selectedAttributesForm.setImageFormat(imageFormat);
		}*/
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		String downloadUrl = null;
		boolean isRealTimeDownload = false;

		final int numberOfEntries = jnjGTCartFacade.getNumberOfCartEntriesInSessionCart();
		if (numberOfEntries == 0)
		{
			return REDIRECT_PREFIX + "/home";
		}
		/*final int thresholdValue = Integer.parseInt(JnJCommonUtil.getValue(JnjPCMCoreConstants.Cart.THRESHOLD_VALUE_OF_PRODUCTS));*/
		/*jnjNACartFacade.updateCartModelWithSelectedAttributes(selectedAttributesForm);*/
		String orderCode = null;
		try
		{

			orderCode = jnjGTCheckoutFacade.placeOrderInHybris(true);
		}
		catch (final InvalidCartException exception)
		{
			LOGGER.error(
					"getExportConfirmationPage()" + Logging.HYPHEN + "Can't convert cart model to order model"
							+ exception.getMessage(), exception);
			redirectModel.addFlashAttribute("invalidCartIndicator", Boolean.TRUE);
			return REDIRECT_PREFIX + "/getConfigPage";

		}
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.pcm.cart"));

	/*	if (numberOfEntries <= thresholdValue
				|| selectedAttributesForm.getImageFormat().contains(JnjPCMCoreConstants.Cart.CART_IMAGE_FORMAT_NO_IMAGES))
		{
			isRealTimeDownload = true;
			downloadUrl = jnjNACartFacade.downloadCartInRealTime(orderCode);
			model.addAttribute("downloadUrl", downloadUrl);
			model.addAttribute("isRealTimeDownload", isRealTimeDownload);
		}*/
		 

			jnjGTOrderFacade.updateBatchContentInd(orderCode);
			model.addAttribute("isRealTimeDownload", isRealTimeDownload);
		 
		model.addAttribute("orderData", b2bOrderFacade.getOrderDetailsForCode(orderCode));
		storeCmsPageInModel(model, getContentPageForLabelOrId(PCM_CART_CONFIRMATION_CMS_PAGE));
		/*return PcmControllerConstants.Views.Pages.Cart.EXPORT_CONFIRMATION_PAGE;*/
		return "";
	}

	protected PageableData createPageableData(final int pageNumber, final int pageSize, final ShowMode showMode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(100);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	@PostMapping("/getSAPPrice")
	@ResponseBody

	public JnjGTUpdatePriceData getSAPPrice(@RequestParam(value = "entryNumber") final int entryNumber)
	{

		return jnjGTCartFacade.updatePriceForEntry(entryNumber);
	}

	@PostMapping("/getCartSubTotal")
	@ResponseBody

	public JnjGTUpdatePriceData getCartSubTotal()
	{

		return jnjGTCartFacade.getCartSubTotal();
	}





	@PostMapping("/addToCartReturn")
	public String returnOrderFileUpload(
			@RequestParam(value = "returnOrderDoc", required = false) final CommonsMultipartFile returnOrderDoc,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		String statusKey = "uploadReturnOrder.file.success";
		Map<String, String> productQunatityMap = null;
		final Map<String, String> responseMap = new HashMap<String, String>();
		if (returnOrderDoc != null && returnOrderDoc.getOriginalFilename() != null
				&& !returnOrderDoc.getOriginalFilename().isEmpty())
		{


			productQunatityMap = jnjGTCartFacade.fileConverter(returnOrderDoc);

			if (!productQunatityMap.isEmpty())
			{
				for (final Map.Entry<String, String> entry : productQunatityMap.entrySet())
				{
					try
					{

						final JnjCartModificationData cartModificationData = jnjGTCartFacade
								.addToCart(entry.getKey(), entry.getValue());

						if (cartModificationData.getCartModifications().get(0).isError())
						{
							responseMap.put(entry.getKey(), cartModificationData.getCartModifications().get(0).getStatusCode());

						}
						else
						{
							GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, cartModificationData
									.getCartModifications().get(0).getStatusCode());
						}
					}
					catch (final CommerceCartModificationException exeption)
					{
						LOGGER.error("Not able to add products" + " Excption trace" + exeption);
						responseMap.put(
								entry.getKey(),
								entry.getKey() + Jnjb2bCoreConstants.UserSearch.SPACE
										+ jnjCommonFacadeUtil.getMessageFromImpex(Jnjb2bCoreConstants.HomePage.PRODUCT_NOT_ADDED_ERROR));
					}
					catch (final NumberFormatException exeption)
					{
						LOGGER.error("Not able to add products as the entered value for the quantity is not valid");
						sessionService.setAttribute("productCodeInvalidFlag", Boolean.TRUE);
						responseMap.put(
								entry.getKey(),
								entry.getKey()
										+ Jnjb2bCoreConstants.UserSearch.SPACE
										+ jnjCommonFacadeUtil
												.getMessageFromImpex(Jnjb2bCoreConstants.HomePage.PRODUCT_INVALID_ERROR_QUNATITY));
					}


					if (responseMap.size() > 0)
					{
						statusKey = "uploadReturnOrder.file.error.occcured";
					}

				}

			}

			else
			{
				statusKey = "uploadReturnOrder.file.isEmpty";
			}
			redirectModel.addFlashAttribute("responseMap", responseMap);
			redirectModel.addFlashAttribute("statusKey", statusKey);
		}
		return REDIRECT_PREFIX + "/cart";
	}


	@GetMapping("/pcmCartCancelOrder")
	public String pcmCartCancelOrder(final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final int numberOfEntries = jnjGTCartFacade.getNumberOfCartEntriesInSessionCart();
		if (numberOfEntries > 0)
		{
			//Remove session cart

			jnjGTCartFacade.removeSessionCart();
		}
		return REDIRECT_PREFIX + "/cart/pcmCart";
	}
	
	
	@RequestMapping(value = "/reviseOrder")
	public String reviseCart(final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{	
		sessionService.setAttribute("validatedone", Boolean.FALSE);
		sessionService.setAttribute("checkoutdone", Boolean.FALSE);
		System.out.println("revised order navigated to cart");
		return REDIRECT_PREFIX + "/cart";
	}
	
	public String getView(final String view){
        return CartandcheckoutaddonControllerConstants.ADDON_PREFIX + view;
	}
	
	protected void setFalgForcaribPuertCustomer(final Model model)
	{
		final String customerGroup = jnjGTCustomerFacade.getCurrentB2bUnit().getCustomerGroup();
		
		if(StringUtils.isNotEmpty(customerGroup)){
		if (StringUtils.equals(customerGroup,
				jnjConfigServiceImpl.getConfigValueById(Jnjb2bCoreConstants.CONSUMER_CARIBBEAN_CUSTOMER_GROUP)))
		{
			model.addAttribute("caribbeanPuertoCust", Boolean.TRUE);
			model.addAttribute("termsSalesURL", jnjConfigServiceImpl
					.getConfigValueById(Jnjb2bCoreConstants.CONSUMER_CARIBBEAN_URL).toString());
		}
		else if (StringUtils.equals(customerGroup,
				jnjConfigServiceImpl.getConfigValueById(Jnjb2bCoreConstants.CONSUMER_PUERTO_CUSTOMER_GROUP)))
		{
			model.addAttribute("caribbeanPuertoCust", Boolean.TRUE);
			model.addAttribute("termsSalesURL", jnjConfigServiceImpl.getConfigValueById(Jnjb2bCoreConstants.CONSUMER_PUERTO_URL)
					.toString());
		}
		}
	}
	
	@RequestMapping(value = "/orderReview")
	public String orderReview(final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException {
		
		return orderReviewPage(model,wsData,redirectModel);
	}

	public String orderReviewPage(final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		boolean splitCart = false;
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CHECKOUT_PAGE));

		}
		catch (final CMSItemNotFoundException exeption)
		{
			LOGGER.error("Order Checkout not found in current content", exeption);
		}
		
		//Changes for Bonus Item
		final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
		model.addAttribute("freeGoodsMap", freeGoodsMap);
		sessionService.setAttribute("orderReviewFlag", true);
		prepareDataForPage(model, false);
		
		if(sessionService.getAttribute("populateSpliCart")!=null){
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		if(splitCart){
			populateSplitCarts(model);
		}
		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());
		model.addAttribute("divisionData", jnjGTCartFacade.getPopulatedDivisionData());

		final String orderTypes = ((JnjGTCartData) jnjGTCartFacade.getSessionCart()).getOrderType();

		/** Checking if the order type is delivered order "ZDEL" **/
		if (orderTypes != null && orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()))
		{
			/** Adding the show signature flag **/
			model.addAttribute(SHOW_SIGNATURE, Boolean.TRUE);
		}
		setFalgForcaribPuertCustomer(model);
		model.addAttribute("timeOutExtended", Boolean.valueOf(wsData.isTimeOutExtended()));
		JnJB2BUnitModel currentB2bUnit = sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
		model.addAttribute("indicator", currentB2bUnit.getIndicator());
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		
		
		if(jnjGTCartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}
		
		// to remove cart on clicking back button	
		sessionService.setAttribute("validatedone", Boolean.FALSE);
		if(sessionService.getAttribute("validatedone")==Boolean.TRUE)
		{
		jnjGTCartFacade.removeSessionCart();
		
		return REDIRECT_PREFIX + "/cart";
		}
		sessionService.setAttribute("validatedone", Boolean.TRUE);
		if(sessionService.getAttribute("confirmationdone")==Boolean.TRUE)
		{
		jnjGTCartFacade.removeSessionCart();
		sessionService.setAttribute("confirmationdone", Boolean.FALSE);
		return REDIRECT_PREFIX + "/cart";
		}
		
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
		.add(new Breadcrumb("/cart/validate", jnjCommonFacadeUtil.getMessageFromImpex(SHOPPINGCART), null));
		breadcrumbs
				.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex(ORDERREVIEW), null));
		
		
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
			
			
		return getView(jnjGTCartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.OrderReviewPage, null));
	}
	
	@GetMapping("/clearCart")
	protected String clearCart()
	{
		getCartService().removeSessionCart();
	return REDIRECT_PREFIX + "/cart";
	}
	
		/**
	 * The updateDeliveryDateForCart method is used to persist the expected delivery date for the all the entry
	 * associated with the session cart .
	 *
	 * @param model
	 *           the model
	 * @param expectedShipDate
	 *           the exp delivery date
	 * @param entryNumber
	 *           the exp delivery date   
	 * @throws CMSItemNotFoundException        
	 * @return the string
	 */
	@ResponseBody
	@RequestMapping(value = "/updateShippingDateForCart")
	public boolean updateShippingDate(final Model model, @RequestParam("expectedShipDate") final String expectedShipDate, 
			@RequestParam("entryNumber") final String entryNumber)
			throws CMSItemNotFoundException
	{
		boolean success = false;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("add expectedShipDate for cart entries starts");
		}
		if (StringUtils.isNotEmpty(expectedShipDate))
		{
			success = jnjGTCartFacade.updateNamedShippingDate(expectedShipDate,  Integer.valueOf(entryNumber));
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return success;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateShipToAddressIdMap")
	public boolean updateShipToAddressIdMap(final Model model, @RequestParam(value = "shippingAddressId") final String shippingAddrId,boolean checBoxStatus,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
	 
		boolean success = jnjGTCartFacade.updateShipToAddIdMap(shippingAddrId,checBoxStatus,request); 
		return success;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateBillToAddressIdMap")
	public boolean updateBillToAddressIdMap(final Model model, @RequestParam(value = "billingAddressId") final String billingAddrId,boolean checBoxStatus,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
	 
		boolean success = jnjGTCartFacade.updateBillToAddIdMap(billingAddrId,checBoxStatus,request); 
		return success;
	}
	

	@PostMapping("/simulateOrderFirstSAPCall")
	public String simulateOrderFirstSAPCall(final Model model, @RequestBody(required=false)  final JnjGTSapWsData wsData, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		if(cartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}
		ConsignmentChargeForm consignmentChargeForm = new ConsignmentChargeForm();
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute("consignmentChargeForm", consignmentChargeForm);	
		return validateCartPage(model,wsData,redirectModel);
	}
	
//changes for AAOL-3769	
	@ResponseBody
	@RequestMapping(value = "/checkPOCinsignment")
	public boolean checkPOCinsignment(@RequestParam("poNumber") final String poNumber) throws CMSItemNotFoundException
	{
		
		boolean isValidPo = false ;
	
			 isValidPo= jnjGTOrderFacade.isPONumUsed(poNumber);
			if(isValidPo){
			sessionService.setAttribute("poValidationResult", "duplicate");
			}
			else{
				sessionService.setAttribute("poValidationResult", "notDuplicate");
			}
		
		return isValidPo;
	}
	
	
		private void checkQuantity(final Model model)
	{
		boolean overQuantity = false;
		final JnjGTCartData cartData =(JnjGTCartData) jnjGTCartFacade.getSessionCart();
		for(OrderEntryData entry :cartData.getEntries() )
		{
			if(entry.getProduct().getCode().equals(PRODUCT_CODE) && (entry.getQuantity()>Long.parseLong(CONFIRM_COUANTITY)))
					{
						overQuantity = true;
					}
		}
		
		model.addAttribute("overQuantity",overQuantity);
		
	}
//end of changes AAOL-3769
	
		@RequestMapping(value = "/simulateOrderSecondSAPCall", method = { RequestMethod.POST, RequestMethod.GET })
		public String simulateOrderSecondSAPCall( JnjGTCommonFormIOData jnjGTCommonFormIOData, JnjGTSapWsData wsData,  final Model model, 
				final HttpServletRequest request, final RedirectAttributes redirectModel) throws CMSItemNotFoundException {	
	 
		LOGGER.info("simulateOrderSecondSAPCall method invoked");
			wsData.setIsfirstSAPCall(false);
			wsData.setIsSecondSAPCall(true);
			wsData.setIsRefreshCall(false);
		
			return simulateOrderSecondSAPCallPage(jnjGTCommonFormIOData, model, wsData, redirectModel);
	}

	
	/**
	 * @param model
	 * @param request
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	 public String simulateOrderSecondSAPCallPage(JnjGTCommonFormIOData jnjGTCommonFormIOData, final Model model, JnjGTSapWsData wsData, 
			 final RedirectAttributes redirectModel) throws CMSItemNotFoundException {
		// isBackOrder = false;
	      LOGGER.info( "simulateOrderSecondSAPCallPage invoked");
	      LOGGER.info( "getZzFlag : "+jnjGTCommonFormIOData.getZzFlag());
	      LOGGER.info( "getZzsubstMat : "+jnjGTCommonFormIOData.getZzsubstMat());
	      LOGGER.info( "getZzsubstQty : "+jnjGTCommonFormIOData.getZzsubstQty());
	      
	      return validateCartPage(model,wsData,redirectModel);
	}
	 
	 @RequestMapping(value = "/fetchBatchSerial", method = { RequestMethod.POST, RequestMethod.GET })
		public String fetchBatchSerial(final HttpServletRequest request, final RedirectAttributes redirectModel) throws Exception {	
		 	LOGGER.info( "fetchBatchSerial invoked");
		 	
		 	List<JnjGTConsInventoryData> consInventoryData = jnjGTCartFacade.fetchBatchDetails();
		 	
		 	if(CollectionUtils.isNotEmpty(consInventoryData))
		 	{
		 		LOGGER.info("fetched information in consInventoryData. row count>"+consInventoryData.size());
			 	redirectModel.addFlashAttribute("consInventoryData", consInventoryData);
			 	LOGGER.info("Redirecting to cart with consInventoryData");
			 	return REDIRECT_PREFIX + "/cart";
		 	}
		 	else
		 	{
		 		LOGGER.info("Redirecting to cart without consInventoryData. CAUSE: consInventoryData is null or empty");
		 		return REDIRECT_PREFIX + "/cart";
		 	}
	}
	 
	 //3088 start
	 @PostMapping("/getShippingAjax")
		public String getShippingA(@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm,final Model model) throws CMSItemNotFoundException
		{
			 List<AddressData> shippingAddressess=null ;
	        boolean isShowSearch = false;
			
			if(searchTerm.isEmpty() || searchTerm == null ){
				shippingAddressess = jnjGTUnitFacade.getShippingAddresses();
			}else{
				 shippingAddressess = jnjGTUnitFacade.getSearchShippingAddress(searchTerm);
				 model.addAttribute("shippingSearchTerm", searchTerm);
			}
			
			if(shippingAddressess != null){
				
				model.addAttribute("isShowSearch", isShowSearch);
				model.addAttribute("shippingAddressess", shippingAddressess);
			}
			 
			return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.changeAddressDiv);
		}
	 
	 
	 	@PostMapping("/getBillingAddressSearch")
		public String getBillingAddressSearch(@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm,final Model model) throws CMSItemNotFoundException
		{
			List<AddressData> billingAddresses=null ;
	        boolean isShowSearch = false;
			
			if(searchTerm.isEmpty() || searchTerm == null ){
				billingAddresses = jnjGTUnitFacade.getBillingAddresses();
			}else{
				billingAddresses = jnjGTUnitFacade.getSearchBillingAddress(searchTerm);
				model.addAttribute("BillingSearchTerm", searchTerm);
			}
			
			if(billingAddresses != null){
				
				model.addAttribute("isShowSearch", isShowSearch);
				model.addAttribute("billingAddresses", billingAddresses);
			}
			 
			return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.changeAddressDivForBill);
}
	 	//3088-end
}
