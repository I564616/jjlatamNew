/**
 *
 */
package com.jnj.la.b2b.cartandcheckoutaddon.controllers.pages;

import com.jnj.b2b.cartandcheckoutaddon.controllers.pages.JnjGTCheckoutController;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjSftpFileTransferUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.facades.cart.impl.JnjLatamCartFacadeImpl;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjOutOrderLine;
import com.jnj.facades.data.JnjSAPErrorMessageData;
import com.jnj.facades.order.JnjLatamCheckoutFacade;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.la.b2b.cartandcheckoutaddon.controllers.JnjlacartandcheckoutaddonControllerConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnjLaSftpFileTransferUtil;
import com.jnj.la.email.util.JnjLaEmailUtil;
import com.jnj.utils.CommonUtil;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author nbanerj1
 *
 */
public class JnjLatamCheckoutController extends JnjGTCheckoutController

{
	protected static final Logger LOG = Logger.getLogger(JnjLatamCheckoutController.class);
	public static final String REPLENISH_ORDER_CONFIRMATION_CMS_PAGE = "replenishmentConfirmation";
	public static final String CART_CLEANUP_REQUIRED = "isCartCleanUprequired";
	private static final String DUP_ORDER_OPTIMIZE = "duplicate.order.optimize";
	
	private ConfigurationService configurationService;

	@Autowired
	private JnjLatamCheckoutFacade jnjLatamCheckoutFacade;

	@Autowired
	private JnjLatamOrderFacade jnjlatamCustomOrderFacade;

	@Autowired
	private JnjLaEmailUtil jnjLaEmailUtil;

	private final Class currentClass = JnjLatamCheckoutController.class;
	

	@Override
	public String createOrderByGet(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
                                   final JnjGTSapWsData sapWsData) throws CMSItemNotFoundException, InvalidCartException
	{
		return createOrder(model, request, redirectModel, sapWsData);
	}

	@Override
	public String createOrder(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
			final JnjGTSapWsData sapWsData) throws CMSItemNotFoundException, InvalidCartException
	{
		sapWsData.setTimeOutExtended(true);
		
		//Duplicate order Fix
		
		final String methodName = "createOrder()";

		boolean isOptimize = Boolean.FALSE;
		final HttpSession session = request.getSession();
		boolean isOrderPlaced = Boolean.FALSE;
		
		try {
			isOptimize = configurationService.getConfiguration().getBoolean(DUP_ORDER_OPTIMIZE);
			if (isOptimize) {
				synchronized (session) {
					if (session.getAttribute("_PLACING_ORDER_") != null) {
						JnjGTCoreUtil.logInfoMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
								"Order is processing, redirect to cart page", currentClass);
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
								"cart.required.cartHeader.PONumber");
						return REDIRECT_PREFIX + "/cart";
					} else {
						session.setAttribute("_PLACING_ORDER_", "Order is placed.");
						isOrderPlaced = Boolean.TRUE;
						JnjGTCoreUtil.logInfoMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
								"New session, start placing order process", currentClass);
					}
				}
			}
			return placeOrder(model, request, redirectModel, sapWsData);
		} 
		finally 
		{
			if (isOrderPlaced) {

				session.removeAttribute("_PLACING_ORDER_");

			}

		}

		//duplicate order fix end..
		
	}

	@Override
	public String placeOrder(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
			final JnjGTSapWsData sapWsData) throws CMSItemNotFoundException, InvalidCartException
	{

		final List<String> orderCodeList = createHybrisOrder(Config.getParameter(CART_CLEANUP_REQUIRED).isEmpty() ? true
				: Boolean.valueOf(Config.getParameter(CART_CLEANUP_REQUIRED)));

		final String checkoutConfirmationURL = createERPOrder(model, request, redirectModel, sapWsData, orderCodeList);

		jnjlatamCustomOrderFacade.modifyOrderStatus(orderCodeList);	
		LOG.info("freight type in place order "+ jnjlatamCustomOrderFacade.getCustomerFreightType(orderCodeList.get(0)));
		redirectModel.addFlashAttribute("freightType", jnjlatamCustomOrderFacade.getCustomerFreightType(orderCodeList.get(0)));
		
		for (final String orderCode : orderCodeList)
		{
			sendOrdersStatusEmail(request, orderCode);
		}

		return checkoutConfirmationURL;
	}

	@Override
	protected List<String> createHybrisOrder(final boolean isCartCleanupRequired) throws InvalidCartException
	{
		final String methodName = "createHybrisOrder()";
		String orderCode = null;
		List<String> orderCodeList;
		JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName, "START: Placing order in Hybris Latam", currentClass);


		boolean splitCart = false;
		if (sessionService.getAttribute("populateSpliCart") != null)
		{
			splitCart = sessionService.getAttribute("populateSpliCart");
		}


		if (splitCart)
		{
			orderCodeList = jnjLatamCheckoutFacade.placeSplitOrderInHybris(isCartCleanupRequired);
		}
		else
		{
			orderCode = jnjLatamCheckoutFacade.placeOrderInHybris(isCartCleanupRequired);
			orderCodeList = new ArrayList<>();
			orderCodeList.add(orderCode);
		}
		JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName, "END: Placing order in Hybris | Order Code is: #" + orderCodeList.toString(),
				currentClass);
		return orderCodeList;
	}

	@Override
	protected String createERPOrder(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel,
			final JnjGTSapWsData sapWsData, final List<String> ordeCodeList) throws CMSItemNotFoundException, InvalidCartException
	{
		final String methodName = "createERPOrder()";
		final List<String> empenhoFilesFullPath = getEmpenhoFilesRenamedList();

		SalesOrderCreationResponse outboundCreateOrderData;
		for (final String orderCode : ordeCodeList)
		{
			try
			{
				boolean isRemoteUploadSuccess = false;
				// To fetch information and create order in sap.
				JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName, "START: Placing order in SAP #" + orderCode,
						currentClass);

				outboundCreateOrderData = jnjLatamCheckoutFacade.createOrderInSAP(orderCode, sapWsData);

				if (null != empenhoFilesFullPath && outboundCreateOrderData != null
						&& outboundCreateOrderData.getSalesOrderOut() != null
						&& StringUtils.isNotEmpty(outboundCreateOrderData.getSalesOrderOut().getOutSalesOrderNumber()))
				{
					JnjGTCoreUtil.logInfoMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
							"File Upload to SFTP remote folder transfering: " + empenhoFilesFullPath.size() + "files.", currentClass);
					final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjLaSftpFileTransferUtil();
					isRemoteUploadSuccess = jnjSftpFileTransferUtil.uploadEmpenhoDocsToSftp(empenhoFilesFullPath,
							outboundCreateOrderData.getSalesOrderOut().getOutSalesOrderNumber(), orderCode);
				}

				JnjGTCoreUtil.logInfoMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
						"File Upload to SFTP remote folder status :" + isRemoteUploadSuccess, currentClass);
				JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName,
						"END: Placing order in SAP | SAP create Order Status" + outboundCreateOrderData, currentClass);
			}
			catch (final SystemException systemException)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN, methodName,
						Logging.HYPHEN + "System Exception occurred " + systemException.getMessage(), systemException, currentClass);
			}
			catch (final IntegrationException integrationException)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN, methodName,
						Logging.HYPHEN + "Integration Exception occurred " + integrationException.getMessage(), integrationException,
						currentClass);
			}
			catch (final BusinessException businessException)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN, methodName,
						Logging.HYPHEN + "Business Exception occured " + businessException.getMessage(), businessException,
						currentClass);
				if (StringUtils.isNotEmpty(orderCode))
				{
					b2bOrderService.deleteOrder(orderCode);
				}
				redirectModel.addFlashAttribute("customerExcludedError", "cart.common.excluded.customer.error");
				return REDIRECT_PREFIX + "/cart";
			}
			catch (final Exception throwable)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN, methodName,
						Logging.HYPHEN + "Exception occured other then integration exception " + throwable.getMessage(), throwable,
						currentClass);
			}
		}

		final String allOrderCodes = ordeCodeList.toString().replace("[", StringUtils.EMPTY).replace("]", StringUtils.EMPTY)
				.replace(", ", ",");
		JnjGTCoreUtil.logInfoMessage("Redirect", methodName,
				"Redirecting user to Order confirmation page for order:" + ordeCodeList, currentClass);

		redirectModel.addFlashAttribute("showChangeAccountLink", Boolean.TRUE);
		return REDIRECT_PREFIX + "/checkout/single/orderConfirmation/" + allOrderCodes;
	}

	private List<String> getEmpenhoFilesRenamedList()
	{
		final String methodName = "getEmpenhoFilesRenamedList()";
		final List<String> empenhoFilesFullPath = sessionService.getAttribute(Jnjlab2bcoreConstants.EMPENHO_FILES_FULL_PATH);
		if (empenhoFilesFullPath != null && !empenhoFilesFullPath.isEmpty())
		{
			sessionService.removeAttribute(Jnjlab2bcoreConstants.EMPENHO_FILES_FULL_PATH);
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
					"No empenho files attached to the current order.", currentClass);
		}
		return empenhoFilesFullPath;
	}

	@Override
	protected String prepareOrderConfirmation(final String orderCode, final Model model) throws CMSItemNotFoundException
	{
		final String methodName = "orderConfirmation()";
		CommonUtil.logMethodStartOrEnd("Checkout", methodName, Logging.BEGIN_OF_METHOD, LOG);
		setupSplitConfirmationPage(orderCode, model);
		CommonUtil.logMethodStartOrEnd("Checkout", methodName, Logging.END_OF_METHOD, LOG);
		final String viewPath = jnjGTCartFacade
				.getPathForView(JnjlacartandcheckoutaddonControllerConstants.Views.Pages.Cart.CheckoutConfirmationPage, null);
		model.addAttribute("checkoutoption", Config.getParameter(BYEPASS_CHECKOUT_OPTION));

		if (sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP) != null)
		{
			final Map<String, JnjOutOrderLine> freeGoodsMapFrom = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
			model.addAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP, freeGoodsMapFrom);
			sessionService.removeAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
		}
		if (sessionService.getAttribute("gtsProductMap") != null)
		{
			final Map<String, JnjSAPErrorMessageData> gtsProductMap = sessionService.getAttribute("gtsProductMap");
			model.addAttribute("gtsProductMap", gtsProductMap);
			sessionService.removeAttribute("gtsProductMap");
		}

		final JnjLatamCartFacade jnjLatamCartFacade = new JnjLatamCartFacadeImpl();
		model.addAttribute("sessionLanguagePattern", jnjLatamCartFacade.getLanguageSpecificDatePattern(super.getCurrentLanguage()));

		return getView(viewPath);
	}

	@Override
	public String getView(final String view)
	{
		return JnjlacartandcheckoutaddonControllerConstants.ADDON_PREFIX + view;
	}

	/**
	 *
	 * @param orderCodes
	 * @param model
	 * @throws CMSItemNotFoundException
	 */
	@Override
	protected List<OrderData> setupSplitConfirmationPage(final String orderCodes, final Model model)
			throws CMSItemNotFoundException
	{
		final String methodName = "setupSplitConfirmationPage()";
		Boolean splitOrder = Boolean.FALSE;
		final List<OrderData> orderDataList = new ArrayList<>();
		final String[] orderCodesArray = StringUtils.split(orderCodes, Jnjb2bCoreConstants.CONST_COMMA);
		OrderData orderDetails;

		if (orderCodesArray != null)
		{
			for (final String orderCode : orderCodesArray)
			{
				orderDetails = jnjGTOrderFacade.getOrderDetailsForCode(orderCode);

				orderDataList.add(orderDetails);
			}
		}

		if (orderDataList.size() > 1)
		{
			splitOrder = Boolean.TRUE;
			model.addAttribute("orderDataList", orderDataList);
			model.addAttribute("orderData", orderDataList.get(0));
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage("Order data list with one element", methodName,
					"orderDataList.get(0) :" + orderDataList.get(0), currentClass);
			model.addAttribute("orderData", orderDataList.get(0));
		}

		model.addAttribute("cartData", sessionService.getCurrentSession().getAttribute("jnjCartData"));
		model.addAttribute("splitOrder", splitOrder);
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

	@Override
	public void sendOrdersStatusEmail(final HttpServletRequest request, final String orderCode)
	{
		final String baseUrl = JnjWebUtil.getServerUrl(request);
		jnjLaEmailUtil.sendStatusChangeNotification(getCustomerFacade().getCurrentCustomer(), orderCode, baseUrl,
				jnjCommonFacadeUtil.createMediaLogoURL());
	}
	
    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
	
}
