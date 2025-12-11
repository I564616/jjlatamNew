/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.cartandcheckoutaddon.controllers.pages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.ProductOption;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;

import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonWebConstants;
import com.jnj.b2b.cartandcheckoutaddon.controllers.CartandcheckoutaddonControllerConstants;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.checkout.AbstractCheckoutController;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Cart;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.data.JnjGTOrderReturnResponseData;
import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.util.JnjFileUploadToSharedFolderUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOutOrderLine;
import com.jnj.facades.order.JnjGTCheckoutFacade;
import com.jnj.facades.order.JnjGTOrderFacade;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.servicelayer.media.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import de.hybris.platform.catalog.CatalogVersionService;

/**
 * The JnjGTCheckoutController class contains all the methods related to order confirmation page.
 *
 * @author Accenture
 * @version 1.0
 */
@Controller("jnjGTCheckoutController")
@Scope("tenant")
@RequestMapping(value = "/checkout/single")
public class JnjGTCheckoutController extends AbstractCheckoutController
{

	/** The Constant LOG. */
	protected static final Logger LOG = Logger.getLogger(JnjGTCheckoutController.class);

	/** The Constant CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE. */
	protected static final String CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE = "orderConfirmationPage";

	/** The Constant ORDER_CODE_PATH_VARIABLE_PATTERN. */
	protected static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";

	
	@Autowired
	protected CMSSiteService cMSSiteService;
	
	public CMSSiteService getcMSSiteService() {
	    return cMSSiteService;
	}
	public CatalogVersionService getCatalogVersionService() {
	    return catalogVersionService;
	}
	
	@Resource(name = "GTCheckoutFacade")
	protected JnjGTCheckoutFacade jnjCheckoutFacade;

	@Resource(name = "GTOrderFacade")
	protected JnjGTOrderFacade jnjGTOrderFacade;

	@Autowired
	protected JnjFileUploadToSharedFolderUtil jnjFileUploadToSharedFolderUtil;

	@Resource(name = "cartFacade")
	protected CartFacade cartFacade;
	
	@Autowired
	private CatalogVersionService catalogVersionService;
	
	@Autowired
	private MediaService mediaService;
	
	@Resource(name="GTCartFacade")
	protected JnjGTCartFacade jnjGTCartFacade;
	@Autowired
	protected I18NService i18nService;

	@Autowired
	protected SessionService sessionService;
	@Autowired
	protected MessageService messageService;
	
	@Autowired
	protected CartService cartService;

	@Autowired
	UserService userService;

	@Resource(name = "b2bProductFacade")
	protected ProductFacade productFacade;

	@Resource(name = "b2bOrderFacade")
	protected B2BOrderFacade b2bOrderFacade;

	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Autowired
	protected B2BOrderService b2bOrderService;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	JnjOrderUtil orderUtil;
	
	public JnjGTCheckoutFacade getJnjCheckoutFacade() {
		return jnjCheckoutFacade;
	}

	public JnjGTOrderFacade getJnjGTOrderFacade() {
		return jnjGTOrderFacade;
	}

	public JnjFileUploadToSharedFolderUtil getJnjFileUploadToSharedFolderUtil() {
		return jnjFileUploadToSharedFolderUtil;
	}

	public JnjGTCartFacade getJnjGTCartFacade() {
		return jnjGTCartFacade;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public ProductFacade getProductFacade() {
		return productFacade;
	}

	public B2BOrderFacade getB2bOrderFacade() {
		return b2bOrderFacade;
	}

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}

	public B2BOrderService getB2bOrderService() {
		return b2bOrderService;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	protected enum DOWNLOAD_TYPE
	{

		PDF, EXCEL;
	}

	public static final String RETURN_ORDER_RESULT_PDF = "JnjGTReturnOrderConfirmationPdfView";

	public static final String RETURN_ORDER_RESULT_EXCEL = "JnjGTReturnOrderConfirmationExcelView";

	public static final String PRICE_INQUIRY_PDF = "JnjGTPriceInquiryPdfView";

	public static final String SAMPLE_ORDER_RESULT_PDF = "JnjGTSampleOrderPdfView";
	public static final String RESULT_EXCEL = "JnjGTOrderConfirmationExcelView";
	
	public static final String RETURN_CONFIRMATION_PAGE = "returnConfirmationPage";
	protected static final String SAMPLE_CONFIRMATION_PAGE = "checkoutConfirmationPage";
	protected static final String RETURNS_CONFIRMATION_PAGE = "checkoutConfirmationPage";

	public static final String RESULT_PDF = "JnjGTOrderConfirmationPdfView";

	public static final String RESULT_STANDARD_ORDER_PDF = "JnjGTStandardOrderConfirmationPdfView";  
	
	public static final String RESULT_CONSIGNMENT_ORDER_PDF = "JnjGTConsignmentOrderConfirmationPdfView";  
	/**
	 * BYEPASS_CHECKOUT_OPTION
	 */
	public static final String BYEPASS_CHECKOUT_OPTION = "byepass.checkout.option"; //for byepass business logic

	private String jnjOrderNo = "";

	@GetMapping("/placeOrder")
	public String createOrderByGet(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
								   final JnjGTSapWsData sapWsData) throws CMSItemNotFoundException, InvalidCartException
	{
		if(cartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}
		sessionService.setAttribute("confirmationdone", Boolean.TRUE);
		redirectModel.addFlashAttribute("showChangeAccountLink", Boolean.TRUE);
		return createOrder(model, request, redirectModel, sapWsData);
	}

	/**
	 * Creates the order in hybris and also calls the outbound create order sap service to create order in SAP.
	 *
	 * @param model
	 *           the model
	 * @param request
	 *           the request
	 * @param redirectModel
	 *           the redirect model
	 * @param skipSAPWS
	 *           the skip sapws
	 * @return the string
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@PostMapping("/placeOrder")
	public String createOrder(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
			final JnjGTSapWsData sapWsData) throws CMSItemNotFoundException, InvalidCartException
	{
	return placeOrder(model,request,redirectModel,sapWsData);
	}
	/**
	 * 
	 * @param model
	 * @param request
	 * @param redirectModel
	 * @param sapWsData
	 * @return
	 * @throws CMSItemNotFoundException
	 * @throws InvalidCartException
	 */
	public String placeOrder(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
			final JnjGTSapWsData sapWsData) throws CMSItemNotFoundException, InvalidCartException {
		
		boolean orderSplit = orderUtil.isOrderSplit();
		
		if(orderSplit){
			List<String> orderCodeList = createHybrisOrder(false);
			return createERPOrder(model,request,redirectModel,sapWsData,orderCodeList);
		}
		else{
			String hybrisOrderNumber = createHybrisOrder(false,false);
			return createERPOrder(model,request,redirectModel,sapWsData,hybrisOrderNumber);
		}
////		String hybrisOrderNumber = createHybrisOrder(false);
//		List<String> orderCodeList = createHybrisOrder(false);
////		return createERPOrder(model,request,redirectModel,sapWsData,hybrisOrderNumber);
//		return createERPOrder(model,request,redirectModel,sapWsData,orderCodeList);
	}
	
	/**
	 * 
	 * @param isCartCleanupRequired
	 * @return
	 * @throws InvalidCartException
	 */
   protected List<String> createHybrisOrder(boolean isCartCleanupRequired) throws InvalidCartException {
	   
	   //String orderCode = null;
	   	List<String> orderCodeList = new ArrayList<String>();
		// create order in hybris
		LOG.info("START: Placing order in Hybris");
//		orderCode = jnjCheckoutFacade.placeOrderInHybris(isCartCleanupRequired);
		orderCodeList= jnjCheckoutFacade.placeSplitOrderInHybris(isCartCleanupRequired);
		LOG.info("END: Placing order in Hybris | Order Code is:" + orderCodeList);
        return orderCodeList;
}

   protected String createHybrisOrder(boolean isCartCleanupRequired, boolean split) throws InvalidCartException {
	   
	   	String orderCode = null;
//	   	List<String> orderCodeList = new ArrayList<String>();
//      create order in hybris
		LOG.info("START: Placing order in Hybris");
		orderCode = jnjCheckoutFacade.placeOrderInHybris(isCartCleanupRequired);
//		orderCodeList= jnjCheckoutFacade.placeSplitOrderInHybris(isCartCleanupRequired);
		LOG.info("END: Placing order in Hybris | Order Code is:" + orderCode);
        return orderCode;
}

   /**
    * 
    * @param model
    * @param request
    * @param redirectModel
    * @param sapWsData
    * @param orderCode
    * @return
    * @throws CMSItemNotFoundException
    * @throws InvalidCartException
    */
   protected String createERPOrder(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
			final JnjGTSapWsData sapWsData, final List<String> ordeCodeList) throws CMSItemNotFoundException, InvalidCartException {
		

		JnjGTOutboundStatusData outboundCreateOrderData = null;

		final Map<String, JnjGTOrderData> placedOrders = new HashMap<String, JnjGTOrderData>();
		
		for(String orderCode : ordeCodeList)
		{
			try{
					
					// To fetch information and create order in sap.
					LOG.info("START: Placing order in SAP");
//					outboundCreateOrderData = jnjCheckoutFacade.createOrderInSAP(orderCode, sapWsData);
					
					final JnjGTOrderData jnjGTOrderData = jnjCheckoutFacade.createOrderInSAP(orderCode, sapWsData, true); // sap call
					outboundCreateOrderData = sessionService.getAttribute("outboundCreateOrderData");
					
					placedOrders.put(orderCode, jnjGTOrderData);
					if (null != outboundCreateOrderData)
					{
						if (outboundCreateOrderData.isHardStopErrorOcurred())
						{
							if (StringUtils.isNotEmpty(orderCode))
							{
								b2bOrderService.deleteOrder(orderCode);
							}
							redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
							return REDIRECT_PREFIX + "/cart";
						}
						else if (null != outboundCreateOrderData.getRemovedProductCodes())
						{
							for (final String removeProductKey : outboundCreateOrderData.getRemovedProductCodes().keySet())
							{
								final String[] arguments =
								{ outboundCreateOrderData.getRemovedProductCodes().get(removeProductKey).toString() };
								GlobalMessages
										.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, removeProductKey, arguments);
							}
						}
					}
					else {
						if (StringUtils.isNotEmpty(orderCode))
						{
							b2bOrderService.deleteOrder(orderCode);
						}
						redirectModel.addFlashAttribute("sapCreateOrderFail", Boolean.TRUE);
						return REDIRECT_PREFIX + "/cart/checkout";
					}
					
					LOG.info("END: Placing order in SAP | SAP create Order Status" + outboundCreateOrderData);			
					
					//AAOL-2420 changes
					if(null != outboundCreateOrderData && outboundCreateOrderData.getCreditLimitCode()!= null)
							{
							
						
							String message =	jnjGTOrderFacade.getJnjOrderCreditLimitMsg(outboundCreateOrderData.getCreditLimitCode(), i18nService.getCurrentLocale());
							if(null != message){
								GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, message);
							}
							
							}
					//end of AAOL-2420 changes
				}
				catch (final SystemException systemException)
				{
					LOG.error(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
							+ "System Exception occured " + systemException.getMessage(), systemException);
				}
				catch (final IntegrationException integrationException)
				{
					LOG.error(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
							+ "Integration Exception occured " + integrationException.getMessage(), integrationException);
				}
				catch (final BusinessException businessException)
				{
					LOG.error(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
							+ "Business Exception occured " + businessException.getMessage(), businessException);
					if (StringUtils.isNotEmpty(orderCode))
					{
						b2bOrderService.deleteOrder(orderCode);
					}
					redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
					return REDIRECT_PREFIX + "/cart";
				}
				catch (final Throwable throwable)
				{
					LOG.error(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
							+ "Exception occured other then integration exception " + throwable.getMessage(), throwable);
				}
		}
		
		for (final String orderCode : ordeCodeList)
		{
			if (placedOrders.keySet() == null || placedOrders.keySet().isEmpty() || !placedOrders.keySet().contains(orderCode))
			{
				placedOrders.put(orderCode, new JnjGTOrderData());
			}
		}
		sessionService.setAttribute("orderDataMap", placedOrders);
		//if (null != outboundCreateOrderData && outboundCreateOrderData.isSavedSuccessfully())
		//outboundCreateOrderData = new JnjGTOutboundStatusData();
		//outboundCreateOrderData.setSavedSuccessfully(true);
		if (null != outboundCreateOrderData && outboundCreateOrderData.isSavedSuccessfully())
		{
			sendOrdersStatusEmail(request, placedOrders);
			final String allOrderCodes = ordeCodeList.toString().replace("[", StringUtils.EMPTY).replace("]", StringUtils.EMPTY)
					.replace(", ", ",");
			LOG.info("Redirecting user to Order confirmation page for order:" + ordeCodeList);
			return REDIRECT_PREFIX + "/checkout/single/orderConfirmation/" + allOrderCodes;
		}
		/*else
		{
			LOG.info("Not able to create SAP order | Deleting Hybris order:" + orderCode + " | Redirecting user to Checkout page");
			if (StringUtils.isNotEmpty(orderCode))
			{
				b2bOrderService.deleteOrder(orderCode);
			}
			redirectModel.addFlashAttribute("sapCreateOrderFail", Boolean.TRUE);
			return REDIRECT_PREFIX + "/cart/checkout";
		}		*/
		return null;
	}


protected String createERPOrder(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
			final JnjGTSapWsData sapWsData, final String orderCode) throws CMSItemNotFoundException, InvalidCartException {
		

		JnjGTOutboundStatusData outboundCreateOrderData = null;

		try
		{
	     //rama needs to changes
			
			// To fetch information and create order in sap.
			LOG.info("START: Placing order in SAP");
			outboundCreateOrderData = jnjCheckoutFacade.createOrderInSAP(orderCode, sapWsData);
			if (null != outboundCreateOrderData)
			{
				if (outboundCreateOrderData.isHardStopErrorOcurred())
				{
					if (StringUtils.isNotEmpty(orderCode))
					{
						b2bOrderService.deleteOrder(orderCode);
					}
					redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
					return REDIRECT_PREFIX + "/cart";
				}
				else if (null != outboundCreateOrderData.getRemovedProductCodes())
				{
					for (final String removeProductKey : outboundCreateOrderData.getRemovedProductCodes().keySet())
					{
						final String[] arguments =
						{ outboundCreateOrderData.getRemovedProductCodes().get(removeProductKey).toString() };
						GlobalMessages
								.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, removeProductKey, arguments);
					}
				}
				
				//AAOL-2420 changes
				else if(null != outboundCreateOrderData && outboundCreateOrderData.getCreditLimitCode()!= null)
					{
							
						
					String message =	jnjGTOrderFacade.getJnjOrderCreditLimitMsg(outboundCreateOrderData.getCreditLimitCode(), i18nService.getCurrentLocale());
					if(null != message){
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, message);
					}
					
					}
				//end of AAOL-2420 changes
			}
			LOG.info("END: Placing order in SAP | SAP create Order Status" + outboundCreateOrderData);
		}
		catch (final SystemException systemException)
		{
			LOG.error(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
					+ "System Exception occured " + systemException.getMessage(), systemException);
		}
		catch (final IntegrationException integrationException)
		{
			LOG.error(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
					+ "Integration Exception occured " + integrationException.getMessage(), integrationException);
		}
		catch (final BusinessException businessException)
		{
			LOG.error(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
					+ "Business Exception occured " + businessException.getMessage(), businessException);
			if (StringUtils.isNotEmpty(orderCode))
			{
				b2bOrderService.deleteOrder(orderCode);
			}
			redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
			return REDIRECT_PREFIX + "/cart";
		}
		catch (final Throwable throwable)
		{
			LOG.error(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
					+ "Exception occured other then integration exception " + throwable.getMessage(), throwable);
		}

		//rama
		//if (null != outboundCreateOrderData && outboundCreateOrderData.isSavedSuccessfully())
		//outboundCreateOrderData = new JnjGTOutboundStatusData();
		//outboundCreateOrderData.setSavedSuccessfully(true);
		if (null != outboundCreateOrderData && outboundCreateOrderData.isSavedSuccessfully())
		{
			sendOrdersStatusEmail(request, orderCode);
			LOG.info("Redirecting user to Order confirmation page for order:" + orderCode);
			
			
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
			return REDIRECT_PREFIX + "/checkout/single/orderConfirmation/" + orderCode;
		}
		else
		{
			LOG.info("Not able to create SAP order | Deleting Hybris order:" + orderCode + " | Redirecting user to Checkout page");
			if (StringUtils.isNotEmpty(orderCode))
			{
				b2bOrderService.deleteOrder(orderCode);
			}
			redirectModel.addFlashAttribute("sapCreateOrderFail", Boolean.TRUE);
			return REDIRECT_PREFIX + "/cart/checkout";
		}

	}

	/**
	 * This handler create the order in Hybris system only(Batch Mode), SAP Create Order and Change Order WS will be by
	 * passed.
	 *
	 * @return the string
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@PostMapping("/placeOrder/batch")
	public String createBatchOrder(final HttpServletRequest request) throws CMSItemNotFoundException, InvalidCartException
	{
		final String orderCode = jnjCheckoutFacade.placeOrderInHybris(true);
		sendOrdersStatusEmail(request, orderCode);
		return REDIRECT_PREFIX + "/checkout/single/orderConfirmation/batch/" + orderCode;
	}

	/**
	 * Order confirmation.
	 *
	 * @param orderCode
	 *           the order code
	 * @param model
	 *           the model
	 * @return the string
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	@GetMapping("/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN)
	@RequireHardLogIn
	public String orderConfirmation(@PathVariable("orderCode") final String orderCode, final Model model)
			throws CMSItemNotFoundException
	{
        String returnValue = prepareOrderConfirmation(orderCode, model);
        final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return returnValue;
	}
	protected String prepareOrderConfirmation(final String orderCode, final Model model)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "orderConfirmation()";
		JnjGTOrderData orderData = null;
		CommonUtil.logMethodStartOrEnd("Checkout", METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final List<OrderData> orderDetails = setupSplitConfirmationPage(orderCode, model);
		if(CollectionUtils.isNotEmpty(orderDetails)){
			 orderData = (JnjGTOrderData) orderDetails.get(0);
		}
		CommonUtil.logMethodStartOrEnd("Checkout", METHOD_NAME, Logging.END_OF_METHOD, LOG);
		//Changes for issue AAOL-6279 - Reset the flag once the order is placed.
		sessionService.removeAttribute("changeOrderFlag");
		sessionService.removeAttribute("initiateReplenishFlag");
		final String ViewPath = jnjCheckoutFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Checkout.CheckoutConfirmationPage,orderData.getOrderType());
		model.addAttribute("checkoutoption", Config.getParameter(BYEPASS_CHECKOUT_OPTION));
		return getView(ViewPath);
	}
	
	/**
	 * 
	 * @param orderCodes
	 * @param model
	 * @return
	 * @throws CMSItemNotFoundException 
	 */
	protected List<OrderData> setupSplitConfirmationPage(String orderCodes, Model model) throws CMSItemNotFoundException {
		Boolean splitOrder = Boolean.FALSE;
		final List<OrderData> orderDataList = new ArrayList<OrderData>();
		final String orderCodesArray[] = StringUtils.split(orderCodes, Jnjb2bCoreConstants.CONST_COMMA);
		OrderData orderDetails = null;
		String productCode = null;
		ProductData product = null;
		for(String orderCode : orderCodesArray){
	
			orderDetails = jnjGTOrderFacade.getOrderDetailsForCode(orderCode);		

			{
				for (final OrderEntryData entry : orderDetails.getEntries())
				{
					productCode = entry.getProduct().getCode();
//					product = jnjGTProductFacade.getProductForCodeAndOptions(productCode,
//							Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
//					entry.setProduct(product);
					
				}
			}
			orderDataList.add(orderDetails);
		}
		
		if(orderDataList.size()>1){
			splitOrder = Boolean.TRUE;
			model.addAttribute("orderDataList", orderDataList);
			model.addAttribute("orderData", orderDataList.get(0));
		}
		else{
			model.addAttribute("orderData", orderDataList.get(0));
		}
		
		model.addAttribute("splitOrder", splitOrder);
//		model.addAttribute("orderDataList", orderDataList);
		model.addAttribute("divisionData", jnjGTCartFacade.getPopulatedDivisionData());
		
		//Changes for Bonus Item
		final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
		model.addAttribute("freeGoodsMap", freeGoodsMap);		
		sessionService.removeAttribute("freeGoodsMap");

		model.addAttribute("email", getCustomerFacade().getCurrentCustomer().getUid());
		model.addAttribute("pageType", PageType.ORDERCONFIRMATION.name());
		model.addAttribute("metaRobots", "no-index,no-follow");
		final int cartEntriesPageSize = Config.getInt(Jnjb2bCoreConstants.CART_PAGE_ENTRIES_SIZE, 30);
		model.addAttribute("cartEntriesPageSize", Integer.valueOf(cartEntriesPageSize));
		final AbstractPageModel cmsPage = getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE);
		storeCmsPageInModel(model, cmsPage);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				resourceBreadcrumbBuilder.getBreadcrumbs("order.confirmation.breadbreadcrumb"));
		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());
		return orderDataList;
	}

	/**
	 * Order confirmation for Batch Mode.
	 *
	 * @param orderCode  the order code
	 * @param model the model
	 * @return the string
	 * @throws CMSItemNotFoundException the cMS item not found exception
	 */
	@GetMapping("/orderConfirmation/batch/" + ORDER_CODE_PATH_VARIABLE_PATTERN)
	@RequireHardLogIn
	public String batchOrderConfirmation(@PathVariable("orderCode") final String orderCode, final Model model)
			throws CMSItemNotFoundException	{
		
		return batchOrderConfirmationPage(orderCode, model);
	}
	
	/**
	 * batchOrderConfirmationPage methods invokes for Batch Mode.
	 *
	 * @param orderCode the order code
	 * @param model the model
	 * @return the string 
	 * @throws CMSItemNotFoundException the cMS item not found exception
	 */
	public String batchOrderConfirmationPage(final String orderCode, final Model model)
			throws CMSItemNotFoundException	{
		final String METHOD_NAME = "batchOrderConfirmationPage()";
		CommonUtil.logMethodStartOrEnd("Checkout - BATCH", METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final OrderData orderDetails = setupConfirmationPage(orderCode, model);
		CommonUtil.logMethodStartOrEnd("Checkout - BATCH", METHOD_NAME, Logging.END_OF_METHOD, LOG);
		final String ViewPath =jnjGTCartFacade.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Checkout.CheckoutBatchConfirmationPage,((JnjGTOrderData) orderDetails).getOrderType());
		return getView(ViewPath);
	}

	/**
	 * This method is for setting up the confirmation page data
	 *
	 * @param orderCode
	 * @param model
	 * @return orderDetails
	 * @throws CMSItemNotFoundException
	 */
	protected OrderData setupConfirmationPage(final String orderCode, final Model model) throws CMSItemNotFoundException
	{
		final OrderData orderDetails = b2bOrderFacade.getOrderDetailsForCode(orderCode);
		model.addAttribute("orderData", orderDetails);
		model.addAttribute("email", getCustomerFacade().getCurrentCustomer().getUid());
		model.addAttribute("pageType", PageType.ORDERCONFIRMATION.name());
		model.addAttribute("metaRobots", "no-index,no-follow");
		final int cartEntriesPageSize = Config.getInt(Jnjb2bCoreConstants.CART_PAGE_ENTRIES_SIZE, 30);
		model.addAttribute("cartEntriesPageSize", Integer.valueOf(cartEntriesPageSize));
		final AbstractPageModel cmsPage = getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE);
		storeCmsPageInModel(model, cmsPage);
		
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
		.add(new Breadcrumb("/cart/validate", jnjCommonFacadeUtil.getMessageFromImpex("Shopping Cart"), null));
		breadcrumbs
				.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("Confirmation"), null));
		
		
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		
		
		/*model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				resourceBreadcrumbBuilder.getBreadcrumbs("order.confirmation.breadbreadcrumb"));*/
		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());
		return orderDetails;
	}

	/**
	 * Download data.
	 *
	 * @param orderCode
	 *           the order code
	 * @param model
	 *           the model
	 * @param downloadType
	 *           the download type
	 * @return the string
	 */
	@GetMapping("/downloadOrderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN)
	@RequireHardLogIn
	public String downloadData(@PathVariable("orderCode") final String orderCode, final Model model,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "isReturnOrder", defaultValue = "false") final boolean isReturnOrder)
	{
		List<OrderEntryData> orderEntryList = null;
		OrderData orderData = null;
		String orderTypeCode= null;
		try
		{
			orderData = b2bOrderFacade.getOrderDetailsForCode(orderCode);
			orderEntryList = orderData.getEntries();
			String orderType = null;
			 /* Excel image adding Started here */
			final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
			final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
						.getActiveCatalogVersion();
			model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
				+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
				 /* Excel image adding end here */
			
			List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
			 if (catologLst != null && catologLst.size() > 0) {
				 MediaModel mediaModel1 = null;
				
					 mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
							 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
				
				 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
						 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
	       	        if (mediaModel1 != null) {
	       	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
	       	        }
	       	        if (mediaModel2 != null) {
	       	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
	       	        }
			}
			 /* Pdf image adding Started here */
			if (((JnjGTOrderData) orderData).getOrderType() != null)
			{
				orderTypeCode = ((JnjGTOrderData) orderData).getOrderType();
				orderType = messageService.getMessageForCode(Jnjb2bCoreConstants.Cart.ORDER_TYPE_CONSTANT + "."+ ((JnjGTOrderData) orderData).getOrderType().toString(), i18nService.getCurrentLocale());
			}
			((JnjGTOrderData) orderData).setOrderType(orderType);
		}
		catch (final Exception exp)
		{
			LOG.error(CartandcheckoutaddonWebConstants.CheckOut.ORDER_CONFIRMATION_DOWNLOAD + exp);
		}

		model.addAttribute("orderData", orderData);
		model.addAttribute("orderEntryList", orderEntryList);
		if (!isReturnOrder)
		{
			if(null != orderTypeCode   && orderTypeCode.equals(JnjOrderTypesEnum.ZOR.getCode())){
				return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_STANDARD_ORDER_PDF : RESULT_EXCEL;
			}
			//AAOL-6018, AAOL-6019 and AAOL-6020 changes start
			else if(null != orderTypeCode   &&  (orderTypeCode.equals(JnjOrderTypesEnum.KB.getCode()) || orderTypeCode.equals(JnjOrderTypesEnum.KA.getCode()) || orderTypeCode.equals(JnjOrderTypesEnum.KE.getCode()))){
				return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_CONSIGNMENT_ORDER_PDF : RESULT_EXCEL;
			}
			//AAOL-6018, AAOL-6019 and AAOL-6020 changes ends
			else
			return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_PDF : RESULT_EXCEL;
		}
		else
		{
			return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RETURN_ORDER_RESULT_PDF : RETURN_ORDER_RESULT_EXCEL;
		}
	}
	
	@GetMapping("/orderConfirmationDownload/" + ORDER_CODE_PATH_VARIABLE_PATTERN)
	@RequireHardLogIn
	public String downloadPriceInqiryConfirmationData(@PathVariable("orderCode") final String orderCode, final Model model,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "isReturnOrder", defaultValue = "false") final boolean isReturnOrder,final HttpServletRequest request )
	{
		String returnType = null;
		try
		{
			//final OrderData orderData = getB2bOrderFacade().getOrderDetailsForCode(orderCode);
			final OrderData orderData = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public OrderData execute()
				{
					return getB2bOrderFacade().getOrderDetailsForCode(orderCode);
				}
			}, userService.getAdminUser());

			if(null != sessionService.getAttribute("GOGSPdfExcelDownload") && sessionService.getAttribute("GOGSPdfExcelDownload").equals("true")){
				final OrderData goorderDetails = jnjGTOrderFacade.getPriceInquiryOrderDetailsForCode(sessionService.getAttribute("goOrderCode").toString(), sessionService.getAttribute("goJdeOrderCode").toString());
				OrderData orderDetails = jnjGTOrderFacade.getPriceInquiryOrderDetailsForCode(orderCode, jnjOrderNo);
				 List<OrderEntryData> allEntryList = new ArrayList<OrderEntryData>(goorderDetails.getEntries());
				 allEntryList.addAll(orderDetails.getEntries());
				 orderDetails.setEntries(allEntryList);
				 
					model.addAttribute("orderData", orderDetails);
					model.addAttribute("jdegoOrderNum",((JnjGTOrderData) goorderDetails).getSapOrderNumber());
					model.addAttribute("goOrderCode", sessionService.getAttribute("goOrderCode"));
					sessionService.removeAttribute("GOGSPdfExcelDownload");
			}
			else{
			model.addAttribute("orderData", orderData);
			}
			model.addAttribute("confirmationZQT", "orderConfirmation");
			
			List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
			if (catologLst != null && catologLst.size() >0)
			{
				/*String media = getJnjCommonFacadeUtil().getMediaURL( catologLst.get(0).getId(), 
						Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE, JnjemeaCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);*/
				MediaModel mediaModel = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
				if (mediaModel != null){
					model.addAttribute("jnjConnectLogoURL",  mediaService.getStreamFromMedia(mediaModel));
				}
			}
						
			if (!isReturnOrder)
			{
				if(orderData instanceof JnjGTOrderData){
					JnjGTOrderData jnjGTOrderData = (JnjGTOrderData) orderData;
					String orderType = jnjGTOrderData.getOrderType();
					
					if(JnjOrderTypesEnum.ZQT.getCode().equals(orderType)){
						returnType =  (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? PRICE_INQUIRY_PDF : RESULT_EXCEL;
					}
					else if(JnjOrderTypesEnum.SABOX.getCode().equals(orderType) || JnjOrderTypesEnum.SAEACH.getCode().equals(orderType) || JnjOrderTypesEnum.SAMPLEORDERS9BOX.getCode().equals(orderType) || JnjOrderTypesEnum.SAMPLEORDERS9EACH.getCode().equals(orderType)){
						returnType =  (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? SAMPLE_ORDER_RESULT_PDF : RESULT_EXCEL;
					}
					else if(((JnjGTOrderData) orderData).getOrderType() != null && ((JnjGTOrderData) orderData).getOrderType().equals(JnjOrderTypesEnum.ZOR.getCode())){
						 if(DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) 
							 return RESULT_STANDARD_ORDER_PDF;
					}
					//AAOL-6018, AAOL-6019 and AAOL-6020 changes start
					else if(((JnjGTOrderData) orderData).getOrderType() != null &&  (((JnjGTOrderData) orderData).getOrderType().equals(JnjOrderTypesEnum.KB.getCode()) || ((JnjGTOrderData) orderData).getOrderType().equals(JnjOrderTypesEnum.KA.getCode())||((JnjGTOrderData) orderData).getOrderType().equals(JnjOrderTypesEnum.KE.getCode()))){
						if(DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) 
							 return RESULT_CONSIGNMENT_ORDER_PDF ;
					}
					//AAOL-6018, AAOL-6019 and AAOL-6020 changes ends
					else
					{
						returnType =  (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_PDF : RESULT_EXCEL;
					}
				}else{
					returnType =  (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_PDF : RESULT_EXCEL;
				}
			}
			else
			{
				returnType = (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RETURN_ORDER_RESULT_PDF : RETURN_ORDER_RESULT_EXCEL;
			}
			
		}
		catch (final Exception exp)
		{
			LOG.error("Exception occurred inside JnjEmeaCheckoutController.downloadEMEAConfirmationData() due to" + exp);
		}
		return returnType;
		
	}

	/**
	 * Send orders status email.
	 *
	 * @param request
	 *           the request
	 * @param orderCode
	 *           the order code
	 */
	protected void sendOrdersStatusEmail(final HttpServletRequest request, final String orderCode)
	{
		final String baseUrl = JnjWebUtil.getServerUrl(request);
		jnjCheckoutFacade.sendOrderStatusEmail(orderCode, baseUrl, Boolean.FALSE);

	}
	
	/**
	 * 
	 * @param request
	 * @param placedOrders
	 */
	protected void sendOrdersStatusEmail(final HttpServletRequest request, final Map<String, JnjGTOrderData> placedOrders)
	{
		final String baseUrl = JnjWebUtil.getServerUrl(request);
		for (final String orderCode : placedOrders.keySet())
		{
			jnjCheckoutFacade.sendOrderStatusEmail(orderCode, baseUrl, Boolean.FALSE);
		}
	}


	/**
	 * This method is used to fetch the tablet signature page.
	 *
	 * @return view
	 */
	@RequestMapping(value = "/signature")
	public String getTabletSignaturePopup()
	{
		/*return ControllerConstants.Views.Pages.Cart.TabletSignaturePage;*/
		return "";
	}

	/**
	 * This method is used to save the tablet signature in the order.
	 *
	 * @return view
	 */
	@PostMapping("/signature")
	@ResponseBody
	public boolean saveTabletSignature(@RequestParam(value = "imageCode") String imageCode,
			@RequestParam(value = "name") final String name)
	{
		boolean status = false;
		/** Extracting the significant part of the image code **/
		imageCode = imageCode.substring(imageCode.indexOf(",") + 1);
		File image = null;
		try
		{
			/** Creating a temporary file that holds the signature image **/
			image = File.createTempFile("signature", ".png");
			final FileOutputStream fileOutputStream = new FileOutputStream(image);

			/** Decoding the image code and writing the image file **/
			fileOutputStream.write(Base64.getDecoder().decode(imageCode));
			fileOutputStream.close();

			/** Calling facade to create media model in cart **/
			jnjGTCartFacade.createMediaModelForCart(image, name);
			status = true;
		}
		catch (final IOException ioException)
		{
			LOG.error("CART_PAGE_CONTROLLER" + "saveTabletSignature()" + Logging.HYPHEN + "Error in creating image File"
					+ ioException.getMessage(), ioException);
		}
		catch (final IllegalStateException illegalStateException)
		{

			LOG.error("CART_PAGE_CONTROLLER" + "saveTabletSignature()" + Logging.HYPHEN + "Error in uploading image File"
					+ illegalStateException.getMessage(), illegalStateException);
		}
		catch (final BusinessException exception)
		{
			LOG.error("CART_PAGE_CONTROLLER" + "saveTabletSignature()" + Logging.HYPHEN + "Error in converting image File to media"
					+ exception.getMessage(), exception);
		}
		return status;
	}

	/** Handler for return order confirmation call **/
	@PostMapping("/returnConfirmationPage")
	public String returnConfirmationPage(final RedirectAttributes redirectModel, final HttpServletRequest request,
			@RequestParam(value = "skipSAPValidation", defaultValue = "false") final boolean skipSAPValidation)
			throws CMSItemNotFoundException, IOException
	{
		String orderCode = null;
		final String cartPageRedirectURL = REDIRECT_PREFIX + "/cart";
		String errorMessage = null;
		String message = null;
		String message1 = null;
		final CartModel cartModel = cartService.getSessionCart();
        System.out.println(cartModel.getEntries().size());
        String returnString = null;
		//Check LotNum, Invoice Number and Qty and their combination checks
		if (jnjGTCartFacade.preValidateCartForReturnOrder())
		{
			redirectModel.addFlashAttribute("showPreValidationError", Boolean.TRUE);
			return cartPageRedirectURL;
		}
		else if(cartModel.getEntries().size() > Integer.parseInt(Config.getParameter("cart.product.return.configar.limit"))){
			
			message = jnjCommonFacadeUtil.getMessageFromImpex("cart.submit.return.hdrLevel.error");
			if (StringUtils.isNotEmpty(message))
			{
				errorMessage = jnjCommonFacadeUtil.getMessageFromImpex("cart.submit.return.hdrLevel.error");
				redirectModel.addFlashAttribute("excludedProducts", errorMessage);
				return cartPageRedirectURL;
			}
		}
		else
		{

			//Skips SAP validation
			if (!skipSAPValidation)
			{
				JnjGTOrderReturnResponseData jnjGTOrderReturnResponseData = null;
				try
				{
					jnjGTOrderReturnResponseData = jnjGTCartFacade.returnOrderSAPValidation(); // SAP CALL
				}
				catch (final IllegalStateException exception)
				{
					LOG.error(Jnjb2bFacadesConstants.Logging.RETURN_ORDER + Logging.HYPHEN + "requestReturnConfirmationPage()"
							+ Logging.HYPHEN + "Error in Uploading File" + exception.getMessage(), exception);
				}
				catch (final SystemException systemException)
				{
					LOG.error(Jnjb2bFacadesConstants.Logging.RETURN_ORDER + Logging.HYPHEN + "requestReturnConfirmationPage()"
							+ Logging.HYPHEN + "Error SAP validation call" + systemException.getMessage(), systemException);
				}
				catch (final BusinessException businessException)
				{
					LOG.error(Jnjb2bFacadesConstants.Logging.RETURN_ORDER + Logging.HYPHEN + "requestReturnConfirmationPage()"
							+ Logging.HYPHEN + "Business Exception occured " + businessException.getMessage(), businessException);
					// In case of Excluded Products below error message is shown on the front end.
					if (StringUtils.isNotEmpty(businessException.getMessage()))
					{

						if (businessException.getMessage().contains(Jnjb2bCoreConstants.SYMBOl_DASH))
						{
							message = jnjCommonFacadeUtil.getMessageFromImpex("cart.submit.return.sapOrderNumber.success");
							message1 = jnjCommonFacadeUtil.getMessageFromImpex("cart.submit.return.sapOrderNumber.success1");
							errorMessage = jnjCommonFacadeUtil.getMessageFromImpex("cart.submit.return.excluded.product.error");
							if (StringUtils.isNotEmpty(message) && StringUtils.isNotEmpty(errorMessage))
							{
								final String[] productWithSapNumber = businessException.getMessage().split(
										Jnjb2bCoreConstants.SYMBOl_DASH);
								if (null != productWithSapNumber && productWithSapNumber.length > 1)
								{
									message = message.replace(Config.getParameter(Cart.EXCLUDED_PRODUCTS_WITH_SAP_ORDER_NUMBER),
											productWithSapNumber[0]);
									errorMessage = errorMessage.replace(Config.getParameter(Cart.EXCLUDED_PRODUCTS),
											productWithSapNumber[1]);
									redirectModel.addFlashAttribute("excludedProducts", errorMessage);
									redirectModel.addFlashAttribute("successMessage", message);
									redirectModel.addFlashAttribute("successMessage1", message1);
								}
							}
						}
						else
						{
							message = jnjCommonFacadeUtil.getMessageFromImpex("cart.submit.return.excluded.product.error");
							if (StringUtils.isNotEmpty(message))
							{
								errorMessage = message.replace(Config.getParameter(Cart.EXCLUDED_PRODUCTS),
										businessException.getMessage());
								redirectModel.addFlashAttribute("excludedProducts", errorMessage);
								return cartPageRedirectURL;
							}
						}
					}// Excluded Customers
					else
					{
						redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
						return cartPageRedirectURL;
					}
				}
				catch (final IntegrationException integrationException)
				{
					LOG.error(Jnjb2bFacadesConstants.Logging.RETURN_ORDER + Logging.HYPHEN + "requestReturnConfirmationPage()"
							+ Logging.HYPHEN + "Error SAP validation call" + integrationException.getMessage(), integrationException);
				}

				//Redirect User to cart page in case of SAP Return Order Calls
				if ((null == jnjGTOrderReturnResponseData || !jnjGTOrderReturnResponseData.isSapResponseStatus())
						&& StringUtils.isEmpty(errorMessage))
				{
					redirectModel.addFlashAttribute("sapValidationError", Boolean.TRUE);
					return cartPageRedirectURL;
				}
				else if (null != jnjGTOrderReturnResponseData
						&& StringUtils.isNotEmpty(jnjGTOrderReturnResponseData.getSapOrderNumber()))
				{
					message = jnjCommonFacadeUtil.getMessageFromImpex("cart.submit.return.sapOrderNumber.success");
					message1 = jnjCommonFacadeUtil.getMessageFromImpex("cart.submit.return.sapOrderNumber.success1");
					if (StringUtils.isNotEmpty(message))
					{
						message = message.replace(Config.getParameter(Cart.EXCLUDED_PRODUCTS_WITH_SAP_ORDER_NUMBER),
								jnjGTOrderReturnResponseData.getSapOrderNumber());
						redirectModel.addFlashAttribute("successMessage", message);
						redirectModel.addFlashAttribute("successMessage1", message1);
					}
				}
			}
			//Create order in Hybris in case there is success in SAP Return order Call
			try
			{
				orderCode = jnjCheckoutFacade.placeReturnOrder();
				final CustomerModel currentUser = jnjCheckoutFacade.getCurrentUserForCheckoutForEmail();
				if (currentUser instanceof JnJB2bCustomerModel && null != ((JnJB2bCustomerModel) currentUser).getEmailNotification()
						&& ((JnJB2bCustomerModel) currentUser).getEmailNotification().booleanValue())
				{
					//Changes for AAOL - 5163
					sendReturnOrderUserEmail(request, orderCode);
				}
				//Changes for AAOL - 5163
				sendReturnOrderCSREmail(request, orderCode);
			}
			catch (final InvalidCartException exception)
			{
				LOG.error(Jnjb2bFacadesConstants.Logging.RETURN_ORDER + Logging.HYPHEN + "requestReturnConfirmationPage()"
						+ Logging.HYPHEN + "Order Validation not found in current content" + exception.getMessage(), exception);
			}
			if (!skipSAPValidation)
			{
				returnString = REDIRECT_PREFIX + "/checkout/single/orderConfirmation/" + orderCode;
			}
			else
			{
				returnString = REDIRECT_PREFIX + "/checkout/single/orderConfirmation/batch/" + orderCode;
			}
		}
		return returnString;
	}

	@GetMapping("/sendEmail/{orderId}")
	public void sendEmail(final HttpServletRequest request, @PathVariable final String orderId)
	{
		sendOrdersStatusEmail(request, orderId);
	}
	
	public String getView(final String view){
        return CartandcheckoutaddonControllerConstants.ADDON_PREFIX + view;
	}
	
	/**
	 * AAOL-5163
	 * Method added to trigger mail for return order for user with attachments
	 * @param request
	 * @param orderCode
	 */
	private void sendReturnOrderUserEmail(HttpServletRequest request, String orderCode) {
		
		final String baseUrl = JnjWebUtil.getServerUrl(request);
		jnjCheckoutFacade.sendReturnOrderUserEmail(orderCode, baseUrl);

		
	}
	
	/**
	 * AAOL-5163
	 * Method added to trigger mail for return order for CSR with attachments
	 * @param request
	 * @param orderCode
	 */
	private void sendReturnOrderCSREmail(HttpServletRequest request, String orderCode) {
		
		final String baseUrl = JnjWebUtil.getServerUrl(request);
		jnjCheckoutFacade.sendReturnOrderCSREmail(orderCode, baseUrl);
		
	}
}