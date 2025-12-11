/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.loginaddon.controllers.pages;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.buildorder.JnjBuildOrderFacade;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractFormData;
import com.jnj.facades.data.JnjGTBroadCastData;
import com.jnj.facades.data.JnjGTCustomerBasicData;
import com.jnj.facades.operations.JnjGTOperationsFacade;
import com.jnj.facades.order.JnjGTOrderFacade;
import com.jnj.facades.template.JnjGTOrderTemplateFacade;
import com.jnj.loginaddon.forms.JnjGTHomePageForm;
import com.jnj.utils.CommonUtil;
import de.hybris.platform.acceleratorcms.context.ContextInformationLoader;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//added for showAccountChange by NM3

/**
 * This is the Home Page Controller Class.
 *
 * @author Accenture
 * @version 1.0
 */
@Controller("JnJGTHomePageController")
@RequestMapping("/home")
@RequireHardLogIn
public class JnJGTHomePageController extends AbstractPageController
{
	protected static final String CURRENT_PAGE = "currentPage";
	protected static final String ACCOUNT_SEARCH_TERM = "accountSearchTerm";
	protected static final String ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY = "account.selection.resultsPerPage";
	protected static final String ACCOUNT_PAGINATION_DATA = "accountPaginationData";
	protected static final String EMPTY = "empty";
	protected static final String LINES = "lines";
	protected static final String CART_DATA = "cartData";
	protected static final String TEXT_HTML = "text/html";
	protected static final String PRODUCT = "product";
	protected static final String _1 = "1";
	protected static final String _0 = "0";
	protected static final String CMS_HOME_PAGE = "home";
	protected static final String CMS_SURVEY_PAGE = "surveyPage";
	protected static final int MAX_PAGE_LIMIT = 100;
	protected static final String CART_MODIFICATAION_DATA = "cartModificationData";
	protected static final String ADD_STATUS = "addStatus";
	protected static final String BUILD_ORDER_RESET_PAGE_URL = "/buildorder/resetBuildOrder";
	protected static final String SHOW_CONTRACT ="showContractLink";
	//UAT-556
	protected static final String ERROR_DETAIL_MAP ="errorDetailMap";
	
	//AAOL-5857
	private static final String ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY_OTHERS = "account.selection.resultsPerPageothers";
	

	
	
	/** The JNJ customer facade. */
	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	@Autowired
	protected JnjGTOperationsFacade jnjGTOperationsFacade;

	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected CMSSiteService cmsSiteService;

	@Resource(name = "jnjGTB2BUnitFacade")
	protected JnjGTB2BUnitFacade jnjGTUnitFacade;

	@Autowired
	ContextInformationLoader contextInformationLoader;


	@Resource(name = "GTOrderFacade")
	protected JnjGTOrderFacade orderFacade;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Resource(name="GTCartFacade")
	protected JnjGTCartFacade jnjGTCartFacade;

	@Autowired
	protected ProductFacade productFacade;

	@Resource(name = "jnjGTOrderTemplateFacade")
	protected JnjGTOrderTemplateFacade jnjGTOrderTemplateFacade;
	
	@Resource(name="jnjBuildOrderFacade")
	protected JnjBuildOrderFacade jnjBuildOrderFacade;
	@Autowired
	protected B2BCommerceUnitService b2BCommerceUnitService;

	@Autowired

	protected BaseStoreService baseStoreService;

 
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	/** The model service. */
	@Autowired
	protected ModelService modelService;

	@Autowired
	protected UserService userService;
	
	@Autowired
	protected JnjOrderUtil orderUtil;

	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public JnjGTOperationsFacade getJnjGTOperationsFacade() {
		return jnjGTOperationsFacade;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTB2BUnitFacade getJnjGTUnitFacade() {
		return jnjGTUnitFacade;
	}

	public ContextInformationLoader getContextInformationLoader() {
		return contextInformationLoader;
	}

	public JnjGTOrderFacade getOrderFacade() {
		return orderFacade;
	}

	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}
	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public JnjGTCartFacade getJnjGTCartFacade() {
		return jnjGTCartFacade;
	}

	public ProductFacade getProductFacade() {
		return productFacade;
	}

	public JnjGTOrderTemplateFacade getJnjGTOrderTemplateFacade() {
		return jnjGTOrderTemplateFacade;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}


	public UserService getUserService() {
		return userService;
	}

	public B2BCommerceUnitService getB2BCommerceUnitService()
	{
		return b2BCommerceUnitService;
	}
	protected static final String SUCCESS = "success";
	protected static final Logger LOG = Logger.getLogger(JnJGTHomePageController.class);
	protected static final String UPDATED_PRIVACY_POLICY = "updatedLegalPolicy";
	protected static final String CHANGE_ACCOUNT_ERROR = "changeAccountError";

	/**
	 * This method gets the home page
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping
	public String getHome(final Model model,
			@RequestParam(value = "firstTimeLogin", defaultValue = "false", required = false) final boolean firstTimeLogin,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getHome()";
		boolean redirectToTarget = true;
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		
		//Changes for AAOL-5669
		boolean isSerialUser = false;
		if((JnjGTUserTypes.SERIAL_USER.equals(sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE)))){
			isSerialUser = true;
		}
		if(isSerialUser){
//			sessionService.setAttribute("redirectedToLogin",false);
//			getRedirectStrategy().sendRedirect(request, response, "/serialization");	
			return REDIRECT_PREFIX + "/serialization";
		}
		
		
		// resetPassword start		
		if (!isResetPasswordComplete()) {
			final String siteUrl = JnjWebUtil.getServerUrl(request);
			String userId = userService.getCurrentUser().getUid();
			String passwordResetUrl ="";
			try {
				passwordResetUrl = jnjGTCustomerFacade.resetPasswordUrlFirstTimeLogin(userId, siteUrl);
				logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, "isResetPasswordComplete :: "+passwordResetUrl);
			} catch (UnsupportedEncodingException e) {
				//e.printStackTrace();
			}
			//System.out.println("test point : "+REDIRECT_PREFIX + "/register/resetPasswordForNewUser"+passwordResetUrl);
			return REDIRECT_PREFIX + "/register/resetPasswordForNewUser"+passwordResetUrl;  ///passwordExpiredEmail
		}
		//reset password end
		if (!isRegistrationComplete())
		{
			//return REDIRECT_PREFIX + "/register/activateUser"; // commented to redirect to profile page instead of old activate page
			return REDIRECT_PREFIX + "/my-account/personalInformation";
		}
		/**
		 * Check if firstTimeLogin is false, only then will we check for privacy policy update. This is because the first
		 * time login will come as true only when the updated privacy policy has been accepted
		 *
		 **/
		if (!firstTimeLogin)
		{
			/** Checking the Privacy Policy Versions **/
			final boolean updatedPrivacyPolicy = jnjGTCustomerFacade.checkPrivacyPolicyVersions();
			model.addAttribute(UPDATED_PRIVACY_POLICY, Boolean.valueOf(updatedPrivacyPolicy));
			logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, "Privacy policy updated :: "
					+ updatedPrivacyPolicy);
			if (!updatedPrivacyPolicy)
			{
				redirectToTarget = false;

			}
		}

		/** Setting up the data for the CMS page to render **/
		setupModel(model, CMS_HOME_PAGE);
		/** fetching the page corresponding to the EPIC or PCM site to render **/
	  	return getEPICHomePage(model, firstTimeLogin, redirectToTarget,request);
 
	}

	/**
	 * This method is used to display the EPIC home page.
	 *
	 * @param model
	 * @param firstTimeLogin
	 * @return EPIC home page
	 */
	protected String getEPICHomePage(final Model model, final boolean firstTimeLogin, boolean redirectToTarget,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getEPICHomePage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		
		/*AAOL-5857 Changes for Change Account Pop-up starts*/
//		final JnjGTPageableData pageableData = createPageableData(0, Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50),
//				JnJB2BUnitModel.UID, ShowMode.Page);
		boolean showMore = false;
		String showMoreCounter = "1";
		final int page = 0;
		final JnjGTCustomerBasicData data = (JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer();
		boolean isSuperUSer = false;
		final String role = data.getUserRole();
		JnjGTPageableData pageableData;
		int pageSizeFromConfig;
		if (role != null && (role.trim().equals("CSR") || role.trim().equalsIgnoreCase("PORTAL_ADMIN")))
		{
			isSuperUSer = true;
			pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 4);
			final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
					: pageSizeFromConfig;
			pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, ShowMode.Page);

		}
		else
		{
			isSuperUSer = false;
			pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY_OTHERS, 50);
			final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
					: pageSizeFromConfig;
			pageableData = createPageableData(0, finalPageSize, JnJB2BUnitModel.UID, ShowMode.Page);
		}
		
		model.addAttribute("isSuperUSer", isSuperUSer);
		/*AAOL-5857 Changes for Change Account Pop-up ends*/

		final JnjGTAccountSelectionData accountSelectionData = jnjGTCustomerFacade.getAccountsMap(true, false, false, pageableData);
		final Map<String, String> accountsMap = accountSelectionData.getAccountsMap();
		final Object firstTimeVisitAfterLogin = sessionService.getAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN);
		
			model.addAttribute("currencySymbol", baseStoreService.getCurrentBaseStore().getDefaultCurrency().getSymbol());
			
		/** Checking first time login **/
		if (null != firstTimeVisitAfterLogin || firstTimeLogin)
		{
			redirectToTarget = false;
			/** Removing the session attribute **/
			sessionService.removeAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN);

			/** Set first time login flag **/
			model.addAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN, Boolean.TRUE);
			logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, "First time login flag set in the model.");

			/** Checking if Accounts Map is not null **/
			if (null != accountsMap)
			{
				/**
				 * Checking if accounts Map contains just one account, set survey flag and also set single account in
				 * accountsMap as current B2B Unit
				 **/
				if (accountsMap.size() == 1)
				{
					LOG.info("IF...................accountsMap.size() == 1");
					/** Setting the survey flag **/
					setSurveyFlag(model);

					/** Calling change account to set the single account as the current B2B Unit **/
					changeAccountRequest(accountsMap.keySet().iterator().next(),
							accountsMap.values().iterator().next().split(LoginaddonConstants.UNDERSCORE_SYMBOL)[1], accountsMap.values()
									.iterator().next().split(LoginaddonConstants.UNDERSCORE_SYMBOL)[0], model,request);
				}
				/** If more than one account, set the accounts list in the model **/
				else
				{
					/** Setting all the accounts by calling the facade layer method getAccountsMap **/
					LOG.info("else......................");
					model.addAttribute(LoginaddonConstants.Login.ACCOUNT_LIST, accountsMap); // Pass true to get accounts inclusive of current account
					if(accountsMap.size() > 1)
					{
						final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
						
						@SuppressWarnings("unchecked")
						List keys = new ArrayList(accountsMap.keySet());
						/** Fetching the selected b2b unit by id **/
						final JnJB2BUnitModel selectedUnit = (JnJB2BUnitModel) b2BCommerceUnitService.getUnitForUid(keys.get(0).toString());
						currentCustomer.setCurrentB2BUnit(selectedUnit);
						//currentCustomer.setDefaultB2BUnit(selectedUnit);
						
						/*AAOL-4659*/
						if(currentCustomer.getDefaultB2BUnit()!=null){
							changeAccountRequest(currentCustomer.getDefaultB2BUnit().getUid(), currentCustomer.getDefaultB2BUnit().getName(), null, model,
									request);
							
							model.addAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN, null);	
						}
						/*AAOL-4659*/		
						modelService.save(currentCustomer);
		
					}
					
					model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting pagination data
					model.addAttribute(CURRENT_PAGE, 1);
					setCurrentB2BUnitIdInModel(model);
					logDebugMessage(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, "Accounts map has been set in the model.");
				}
			}
			
			/** If accounts map is null, set only the survey flag **/
			else
			{
				/** Setting the survey flag **/
				LOG.info("/** Setting the survey flag **/////////////////");
				setSurveyFlag(model);
			}
		}

               setSurveyFlag(model);


		if ((null != firstTimeVisitAfterLogin && accountsMap.size() == 1) || firstTimeVisitAfterLogin == null)
		{
			/** If the User is eligible for Start New Order **/
			final Object orderingRights = sessionService.getAttribute(LoginaddonConstants.Login.ORDERING_RIGHTS);

			if (null != orderingRights && ((Boolean) orderingRights).booleanValue())
			{
				model.addAttribute("isEligibleForNewOrder", Boolean.TRUE);
			}

			/** If the User is eligible for Start Price Quote **/
			final String currentSiteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
			if (null != currentSiteName)
			{
				if (LoginaddonConstants.MDD.equals(currentSiteName))
				{
					model.addAttribute("isEligibleForPriceQuote", Boolean.TRUE);
				}
			}
			/* Adding a modal attribute to see whether the user is eligible for Order Return */
			model.addAttribute("isEligibleForOrderReturn", jnjGTCartFacade.isEligibleForOrderReturn());
			/* Adding a modal attribute to for Order History Section */
			model.addAttribute("searchPageData", getRecentOrders(new JnjGTOrderHistoryForm()));
			model.addAttribute("broadCastData", getBroadCastMessages());
			model.addAttribute("orderTemplates", jnjGTOrderTemplateFacade.getOrderTemplatesForHome());
			//model.addAttribute("user", jnjGTCustomerFacade.getCurrentSessionCustomer());
		}
		
		//Nirmala code for showChangeAccount link display
		
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final String orderType = jnjGTCartFacade.getOrderType();
		final HttpSession session=request.getSession();
		session.setAttribute("cartorderType", orderType);
		/* Adding a modal attribute to for Order History Form */
		model.addAttribute("homePageForm", new JnjGTHomePageForm());
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.END_OF_METHOD);

		return getTargetView(redirectToTarget);
	}

	/**
	 * @return
	 */
	protected String getTargetView(final boolean redirectToTarget)
	{
		String targetUrl = LoginaddonControllerConstants.Views.Pages.Home.HomePage;
 
		if (redirectToTarget)
		{
//			final String tmpTargetUrl = sessionService.getAttribute(LoginaddonConstants.JNJ_NA_TARGET_URL);
		    	final String tmpTargetUrl = sessionService.getAttribute(LoginaddonConstants.JNJ_GT_TARGET_URL);
			if (StringUtils.isNotEmpty(tmpTargetUrl))
			{
//				sessionService.removeAttribute(LoginaddonConstants.JNJ_NA_TARGET_URL);
			    	sessionService.removeAttribute(LoginaddonConstants.JNJ_GT_TARGET_URL);
				targetUrl = REDIRECT_PREFIX + tmpTargetUrl;
			}
		}
		LOG.info("targetUrl ----------"+targetUrl);
		return getView(targetUrl);
	}

	
	@RequestMapping(value = "/renderOrderHistory", method = {RequestMethod.POST,RequestMethod.GET})
	public String getSortedRecentOrders(@RequestParam(value = "sortCode", defaultValue = "Order Date - Newest to Oldest") final String sortCode,final Model model) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "getRecentOrders()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_ORDER_STATUS, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final PageableData pageableData = createPageableData(0, 10, sortCode,
				ShowMode.Page);
		JnjGTOrderHistoryForm form = new JnjGTOrderHistoryForm();
		form.setSortCode(sortCode);
		final SearchPageData<OrderHistoryData> searchPageData = orderFacade.getPagedOrderHistoryForStatuses(pageableData, form);
		CommonUtil.logMethodStartOrEnd(Logging.HOME_ORDER_STATUS, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		model.addAttribute("searchPageData",searchPageData);
		return getView(LoginaddonControllerConstants.Views.Pages.Order.orderHistorySection);
	}
	
	/**
	 * This method is called when change account link is clicked in header. This method will return the map containing
	 * the list of accounts.
	 *
	 * @return the map
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	@PostMapping("/getAccounts")
	public String getAccounts(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showMore", defaultValue = "false") final String showMore,
			@RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
			@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm,
			@RequestParam(value = "addCurrentB2BUnit", defaultValue = "false", required = false) final String addCurrentB2BUnit,
			final Model model) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "getAccounts()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/*AAOL-5857 Changes for Change Account Pop-up starts*/
		final JnjGTCustomerBasicData data = (JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer();
		boolean isSuperUSer = false;
		final String role = data.getUserRole();
		JnjGTPageableData pageableData;
		int pageSizeFromConfig;
		if (role != null && (role.trim().equals("CSR") || role.trim().equalsIgnoreCase("PORTAL_ADMIN")))
		{
			isSuperUSer = true;
			pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 4);
			final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
					: pageSizeFromConfig;
			pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

		}
		else
		{
			isSuperUSer = false;
			pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY_OTHERS, 50);
			final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
					: pageSizeFromConfig;
			pageableData = createPageableData(0, finalPageSize, JnJB2BUnitModel.UID, showMode);
		}
		
		model.addAttribute("isSuperUSer", isSuperUSer);
		/*AAOL-5857 Changes for Change Account Pop-up ends*/

		if (StringUtils.isNotEmpty(searchTerm))
		{
			pageableData.setSearchBy(searchTerm);
			model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm); // setting search term
		}

		/** Calling facade layer **/
		logDebugMessage(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, "Calling facade to fetch accounts which has the string "+searchTerm);
		final JnjGTAccountSelectionData accountSelectionData = jnjGTCustomerFacade.getAccountsMap(
				Boolean.valueOf(addCurrentB2BUnit), false, false, pageableData);

		/** Setting all the accounts by calling the facade layer method getAccountsMap **/
		model.addAttribute(LoginaddonConstants.Login.ACCOUNT_LIST, accountSelectionData.getAccountsMap());
		model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting pagination data

		model.addAttribute(CURRENT_PAGE, showMoreCounter);

		logMethodStartOrEnd(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(LoginaddonControllerConstants.Views.Pages.Account.ChangeAccountPage);
		
	}

	/**
	 * This method carries out the change of account for the user.
	 *
	 * @param selectedAccountUid
	 *           the selected account uid
	 * @param model
	 *           the model
	 * @return the string
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	@PostMapping("/changeAccount")
	public String changeAccountRequest(@RequestParam(value = "uid") final String selectedAccountUid,
			@RequestParam(value = "accountName") final String selectedAccountName,
			@RequestParam(value = "accountGLN") final String selectedAccountGLN, final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final String METHOD_NAME = "changeAccountRequest()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean status = false;

		/** Checking if the model contains the survey flag already **/
		if (!model.containsAttribute(LoginaddonConstants.Login.SURVEY))
		{
			/** Setting the survey flag **/
			setSurveyFlag(model);
		}

		/** Checking if account UID parameter is not null **/
		if (selectedAccountUid != null)
		{
			//** Calling facade layer to change account **//*
			status = jnjGTCustomerFacade.changeAccount(selectedAccountUid);
			logDebugMessage(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, "Change account status :: " + status);
			String currentSiteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
			final String sourceSystemIdForUnit = jnjGTUnitFacade.getSourceSystemIdForUnit();
			
			switchSite(currentSiteName, sourceSystemIdForUnit);
			LOG.info("currentSiteName : "+currentSiteName);
			LOG.info("sourceSystemIdForUnit : "+sourceSystemIdForUnit);
			//Need to restore cart accordingly
			jnjGTCartFacade.restoreCartForCurrentUser(sourceSystemIdForUnit);
			
			// Remove free Goods from the session
			sessionService.removeAttribute("freeGoodsMap");

		}
		

		/** Checking status for errors **/
		if (!status)
		{
			model.addAttribute(CHANGE_ACCOUNT_ERROR, Boolean.TRUE);
		}
		else
		{
			/**
			 * Adding selected account info to the model on successful account change : Added to allow current account to
			 * reflect in the header
			 **/
			model.addAttribute(LoginaddonConstants.Login.ACCOUNT_UID, selectedAccountUid);
			model.addAttribute(LoginaddonConstants.Login.ACCOUNT_NAME, selectedAccountName);
			model.addAttribute(LoginaddonConstants.Login.ACCOUNT_GLN, selectedAccountGLN);
			setCurrentB2BUnitIdInModel(model);
			logDebugMessage(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, "Current account info has been set for the header.");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.END_OF_METHOD);
		JnJB2BUnitModel currentB2bUnit =jnjGTCustomerFacade.getCurrentB2bUnit();
		if(currentB2bUnit != null)
		{
			List<JnjContractModel> contractList = (List<JnjContractModel>) currentB2bUnit.getContracts();
			if(CollectionUtils.isNotEmpty(contractList))
			{
				sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.TRUE);
				model.addAttribute(SHOW_CONTRACT, LoginaddonConstants.TRUE);
			}
			else
			{
				sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.FALSE);
				model.addAttribute(SHOW_CONTRACT, LoginaddonConstants.FALSE);
			}
		}
		return getHome(model, false,request);
	}

	/**
	 * This method gets the Survey Pop-up page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/survey")
	public String getSurveyPopup(final Model model) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "getSurveyPopup()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		setupModel(model, CMS_SURVEY_PAGE);
		logMethodStartOrEnd(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(LoginaddonControllerConstants.Views.Pages.Misc.SurveyPage);
		//return "";
	}

	/**
	 * This method disables the survey flag for the current logged in user.
	 *
	 * @param model
	 * @return true / false
	 */
	@GetMapping("/disableSurvey")
	@ResponseBody
	public boolean disableSurveyFlag(final Model model)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "disableSurveyFlag()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean status = false;

		/** Calling facade layer to disable survey flag for the logged in user **/
		status = jnjGTCustomerFacade.disableSurveyFlag();
		logDebugMessage(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, "Survey flag disable status :: " + status);

		logMethodStartOrEnd(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, Logging.END_OF_METHOD);
		return status;
	}

	/**
	 * This method sets the survey flag in the model
	 *
	 * @param model
	 */
	protected void setSurveyFlag(final Model model)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "setSurveyFlag()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting the survey flag **/
		model.addAttribute(LoginaddonConstants.Login.SURVEY, Boolean.valueOf(jnjGTCustomerFacade.checkSurvey()));
		logDebugMessage(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, "Survey flag has been set.");

		logMethodStartOrEnd(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method sets the CMS page association in the model.
	 *
	 * @param model
	 * @throws CMSItemNotFoundException
	 */
	protected void setupModel(final Model model, final String pageLabelOrId) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "setupModel()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final ContentPageModel pageForRequest = getContentPageForLabelOrId(pageLabelOrId);
		storeCmsPageInModel(model, pageForRequest);
		setUpMetaDataForContentPage(model, pageForRequest);
		logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, "CMS Page loaded to the model");
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * Update legal privacy policy.
	 *
	 * @return true, if successful
	 */
	@PostMapping("/updateLegalPrivacyPolicy")
	@ResponseBody
	public boolean updateLegalPrivacyPolicy()
	{

	
		final String METHOD_NAME = "updateLegalPrivacyPolicy()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean versionUpdated = false;
		/** Updating the version of the privacy policy **/
		versionUpdated = jnjGTCustomerFacade.updatePrivacyPolicyVersion();
		logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, "Version updated :: " + versionUpdated);
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.END_OF_METHOD);
		return versionUpdated;
	}


	/**
	 * This method is used to Switch site in case of new account is associated with other. It is part of controller as
	 * bean contextInformationLoader is available in storefront only
	 *
	 * @param currentSiteName
	 *           String the current site name
	 * @param customerDivision
	 *           String source system Id of current Unit
	 * @return true if site is switched, false in case of switching not required
	 */
	protected boolean switchSite(final String currentSiteName, final String customerDivision)
	{
		boolean switched = false;
		if (StringUtils.isNotEmpty(customerDivision))
		{
			if (LoginaddonConstants.MDD.equals(currentSiteName) && customerDivision.equals(JnjGTSourceSysId.CONSUMER.getCode()))
			{
				contextInformationLoader.initializeSiteFromRequest(Config.getParameter(LoginaddonConstants.CONS_SITE_URL_KEY));
				sessionService.setAttribute(LoginaddonConstants.SITE_NAME, LoginaddonConstants.CONS);
				switched = true;
				LOG.info("switching to Cons Site");
			}
			else if (LoginaddonConstants.CONS.equals(currentSiteName) && customerDivision.equals(JnjGTSourceSysId.MDD.getCode()))
			{
				contextInformationLoader.initializeSiteFromRequest(Config.getParameter(LoginaddonConstants.MDD_SITE_URL_KEY));
				sessionService.setAttribute(LoginaddonConstants.SITE_NAME, LoginaddonConstants.MDD);
				switched = true;
				LOG.info("switching to MDD Site");
			}
		}
		return switched;
	}

	/**
	 * Gets the registration status.
	 *
	 * @return the registration status
	 */
	protected boolean isRegistrationComplete()
	{
		final String METHOD_NAME = "isRegistrationComplete()";
		CommonUtil.logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return jnjGTCustomerFacade.isRegistrationComplete();
	}

	/**
	 * This method gets the Last ten recent Orders
	 *
	 * @param form
	 *           JnjGTOrderHistoryForm
	 * @return the SearchPageData
	 */
	public SearchPageData<OrderHistoryData> getRecentOrders(final JnjGTOrderHistoryForm form)
	{
		final String METHOD_NAME = "getRecentOrders()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_ORDER_STATUS, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final PageableData pageableData = createPageableData(0, 10, LoginaddonConstants.Order.SortOption.DEFAULT_SORT_CODE,
				ShowMode.Page);
		final SearchPageData<OrderHistoryData> searchPageData = orderFacade.getPagedOrderHistoryForStatuses(pageableData, form);
		CommonUtil.logMethodStartOrEnd(Logging.HOME_ORDER_STATUS, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return searchPageData;

	}


	/**
	 * This method is used for Quick Add To Cart For Home Page
	 *
	 * @param prodId_Qtys
	 *           and Source
	 * @throws CMSItemNotFoundException
	 * @return String
	 */

	@PostMapping("/addTocart")
	@ResponseBody
	public JnjCartModificationData addQuickTocart(@RequestParam("prodId_Qtys") final String prodId_Qtys, final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
	return quickAddToCart(prodId_Qtys, model);
	}
	
	
	

	protected JnjCartModificationData quickAddToCart(final String prodId_Qtys, final Model model){

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "addQuickTocart()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_QUICK_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final Map<String, String> responseMap = new HashMap<String, String>();
		final String[] products = prodId_Qtys.split(LoginaddonConstants.SYMBOl_COMMA);
		JnjCartModificationData cartModificationData = null;
		final List<CartModificationData> cartModificationList = new ArrayList<CartModificationData>();
		for (final String product : products)
		{
			final String[] product_Qq = product.split(LoginaddonConstants.Solr.COLON);
			try
			{
				

				if (product_Qq.length > 1)
				{
					cartModificationData = jnjGTCartFacade.addToCartQuick(product_Qq[0], product_Qq[1],cartModificationList);
				}
				else
				{
					cartModificationData = jnjGTCartFacade.addToCartQuick(product_Qq[0], _0,cartModificationList);
					System.out.println("comes here-------------------"+cartModificationData);
				}
				if (null != cartModificationData)
				{
					if (!cartModificationData.getCartModifications().get(0).isError())
					{
						// Show min quantity message in case of added quantity is different then the requested one if
						if (StringUtils.equals(cartModificationData.getCartModifications().get(0).getStatusCode(),
								LoginaddonConstants.HomePage.BASKET_PAGE_MESSAGE_MIN_QTY_ADDED)
								|| StringUtils.equals(cartModificationData.getCartModifications().get(0).getStatusCode(),
										LoginaddonConstants.HomePage.BASKET_PAGE_MESSAGE_QTY_ADJUSTED))
						{
							model.addAttribute("showMinQtyMsg", Boolean.TRUE);
						}

						responseMap.put(product_Qq[0], LoginaddonConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS);
						if (!CollectionUtils.isEmpty(cartModificationData.getCartModifications())
								&& null != cartModificationData.getCartModifications().get(0).getEntry())
						{
							model.addAttribute(PRODUCT, cartModificationData.getCartModifications().get(0).getEntry().getProduct());
							model.addAttribute(CART_DATA, cartModificationData);
						}
						else
						{
							model.addAttribute(PRODUCT,
									productFacade.getProductForCodeAndOptions(product_Qq[0], Arrays.asList(ProductOption.BASIC)));
						}
					}
					else
					{
						responseMap.put(product_Qq[0], cartModificationData.getCartModifications().get(0).getStatusCode());
					}
				}
			}
			catch (final CommerceCartModificationException exeption)
			{
				LOG.error("Not able to add products" + products + " Excption trace" + exeption);
				responseMap.put(
						product_Qq[0],
						product_Qq[0] + LoginaddonConstants.UserSearch.SPACE
								+ jnjCommonFacadeUtil.getMessageFromImpex(LoginaddonConstants.HomePage.PRODUCT_NOT_ADDED_ERROR));
			}
			catch (final NumberFormatException exeption)
			{
				LOG.error("Not able to add products as the entered value for the quantity is not valid");
				sessionService.setAttribute("productCodeInvalidFlag", Boolean.TRUE);
				responseMap.put(
						product_Qq[0],
						product_Qq[0] + LoginaddonConstants.UserSearch.SPACE
								+ jnjCommonFacadeUtil.getMessageFromImpex(LoginaddonConstants.HomePage.PRODUCT_INVALID_ERROR_QUNATITY));
			}
		}
		
		
		model.addAttribute(ADD_STATUS, responseMap);
		
		final CartData cartData = jnjGTCartFacade.getSessionCart();
	 
		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));
		CommonUtil.logMethodStartOrEnd(Logging.HOME_QUICK_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		
		// Added to get Updated Count on cart Icon
		 cartModificationData.setTotalUnitCount(jnjGTCartFacade.getSessionCart().getTotalUnitCount());
		 
		return cartModificationData;
	}
	
	/**
	 * 
	 * @param productCodes
	 * @param model
	 * @param isHomePageFileUpload
	 * @return
	 */
	public JnjCartModificationData addMultipleProds(final Map<String, String> productCodes, final Model model, final boolean isHomePageFileUpload){
		final JnjCartModificationData cartModificationData = addMultipleProducts(productCodes,model,isHomePageFileUpload,false,0);
		return cartModificationData;
	}
	
	/**
	 * 
	 * @param productCodes
	 * @param model
	 * @param isHomePageFileUpload
	 * @param forceNewEntry
	 * @param currentEntryNumber
	 * @return
	 */
	public JnjCartModificationData addMultipleProds(final Map<String, String> productCodes, final Model model, final boolean isHomePageFileUpload, final boolean forceNewEntry, int currentEntryNumber)
	{
		final JnjCartModificationData cartModificationData = addMultipleProducts(productCodes,model,isHomePageFileUpload,forceNewEntry,currentEntryNumber);
		return cartModificationData;
	}
	
	/**
	 * Method added to prevent duplication of code because of overloading addMultipleProds method
	 * @param productCodes
	 * @param model
	 * @param isHomePageFileUpload
	 * @param forceNewEntry
	 * @param currentEntryNumber
	 * @return
	 */
	protected JnjCartModificationData addMultipleProducts(Map<String, String> productCodes, Model model,
			boolean isHomePageFileUpload, final boolean forceNewEntry, int currentEntryNumber) {

		boolean isForceNewEntry = orderUtil.isForceNewEntryEnabled();
		
		
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		final JnjCartModificationData cartModificationData;
		//Changes to allow regions to enable/disable forceNew Entry
		if(isForceNewEntry){
			cartModificationData = jnjGTCartFacade.addToCartGT(productCodes, false, isHomePageFileUpload, forceNewEntry,currentEntryNumber);
		}else{
			cartModificationData = jnjGTCartFacade.addToCartGT(productCodes, false, isHomePageFileUpload);
		}
		
		//UAT-556
		Map<String, String> errorDetailMap = new HashMap<String, String>();
		errorDetailMap = sessionService.getAttribute(ERROR_DETAIL_MAP);
		if(errorDetailMap!=null){
		model.addAttribute(ERROR_DETAIL_MAP,errorDetailMap);
		}
		
		if (!CollectionUtils.isEmpty(cartModificationData.getCartModifications())
				&& null != cartModificationData.getCartModifications().get(0).getEntry())
		{
			model.addAttribute(PRODUCT, cartModificationData.getCartModifications().get(0).getEntry().getProduct());
		}

		model.addAttribute(CART_DATA, cartModificationData);
		/*if (isHomePageFileUpload)
		{
			final List<CartModificationData> cartDatas = cartModificationData.getCartModifications();
			final StringBuilder productString = new StringBuilder();
			for (final CartModificationData cartData : cartDatas)
			{
				final JnjGTProductData productData = (JnjGTProductData) cartData.getEntry().getProduct();
				productString.append(productData.getCode()).append(',');
				
				if(StringUtils.isNotBlank(productData.getName())){
					productString.append(productData.getName()).append(',');	
				}
				if(null!=productData.getConsumerSpecification() && StringUtils.isNotBlank(productData.getConsumerSpecification().getBrand())){
					productString.append(productData.getConsumerSpecification().getBrand()).append('|');	
				}
			}
			productCodes.put("gaProducts", productString.toString());
		}*/
		if (isHomePageFileUpload)
		{
			final List<CartModificationData> cartDatas = cartModificationData.getCartModifications();
		}
	
	return cartModificationData;

	}

	


	/**
	 * This method is used for Multi Add To Cart For Home Page
	 *
	 * @param prodIds
	 *
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@PostMapping("/multiAddToCart")
	public String multiAddToCart(@RequestParam("prodIds") final String prodIds, final Model model) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "multiAddToCart()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_MULTIPLE_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final HashMap<String, String> responseMap = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(prodIds))
		{
			// Create product code array
			final String[] products = prodIds.replaceAll("\n", "").split(LoginaddonConstants.SYMBOl_COMMA);

			for (final String prodCode : products)
			{
				if (StringUtils.isNotBlank(prodCode.trim()))
				{
					if(responseMap.containsKey(prodCode.trim())){
						int count = Integer.valueOf(responseMap.get(prodCode.trim()));
						responseMap.put(prodCode.trim(), String.valueOf(count+1));
					}
					else{
					responseMap.put(prodCode.trim(), "0");
					}
				}
			}

			// Add all products to cart
			addMultipleProds(responseMap, model, false);
		}
		model.addAttribute(ADD_STATUS, responseMap);
		// Added to get Updated Count on Minicartcomponent
		final CartData cartData = jnjGTCartFacade.getSessionCart();

		CommonUtil.logMethodStartOrEnd(Logging.HOME_MULTIPLE_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
 
	 
			model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));
			//return ControllerConstants.Views.Fragments.Cart.AddToCartHomePopup;
			return getView(LoginaddonControllerConstants.Views.Fragments.Cart.AddToCartHomePopup);
	 
	}
	
	
	/**
	 * This method is used for Multi Add To Cart For Home Page
	 *
	 * @param prodIds
	 *
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@PostMapping("/multiAddToCartWithQty")
	public String multiAddToCartWithQty(@RequestParam("prodIds") final String prodIds,@RequestParam("prodQty") String prodQty, final Model model, 
			@RequestParam(value = "forceNewEntry", required=false) boolean forceNewEntry, @RequestParam(value = "lineNumber", required=false) final String currentLineEntryNumber) throws CMSItemNotFoundException
	{
		
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "multiAddToCartWithQty()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_MULTIPLE_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		int currentEntryNumber =0;
		if(forceNewEntry && !StringUtils.isEmpty(currentLineEntryNumber) && !StringUtils.isEmpty(prodQty)){
			currentEntryNumber = Integer.parseInt(currentLineEntryNumber);
			jnjGTCartFacade.modifyQuantityForCopyCharge(prodQty, currentLineEntryNumber);
			prodQty = "1";
			
		}

		final HashMap<String, String> responseMap = new HashMap<>();
		if (StringUtils.isNotEmpty(prodIds)) {
			// Create product code array
			final String[] products = prodIds.replaceAll("\n", "").split(LoginaddonConstants.SYMBOl_COMMA);

			for (final String product : products) {
				final String productCode = product.trim().toUpperCase();

				if (StringUtils.isNotBlank(productCode)) {
					if (responseMap.containsKey(productCode)) {
						int count = Integer.valueOf(responseMap.get(productCode));
						responseMap.put(productCode, String.valueOf(count+1));
					} else {
						if(StringUtils.isBlank(prodQty)) {
							responseMap.put(productCode, "0");
						} else {
							responseMap.put(productCode, String.valueOf(prodQty));
						}
					}
				}

			}

			// Add all products to cart
			
			addMultipleProds(responseMap, model, false, forceNewEntry, currentEntryNumber);
		}
		model.addAttribute(ADD_STATUS, responseMap);
		// Added to get Updated Count on Minicartcomponent
		final CartData cartData = jnjGTCartFacade.getSessionCart();

		CommonUtil.logMethodStartOrEnd(Logging.HOME_MULTIPLE_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));
		//return ControllerConstants.Views.Fragments.Cart.AddToCartHomePopup;
		return getView(LoginaddonControllerConstants.Views.Fragments.Cart.AddToCartHomePopup);
	 
	}

	/**
	 * This method is used for getting Order Return Pop Up.
	 *
	 * @throws CMSItemNotFoundException
	 * @return the boolean
	 */
	@ResponseBody
	@GetMapping("/startReturn")
	public boolean startReturn() throws CMSItemNotFoundException	{
		 
		return processReturnOrder();
	}
	
	/**
	 * This method is used for getting Order Return Pop Up.
	 *
	 * @throws CMSItemNotFoundException
	 * @return the boolean
	 */
	public boolean processReturnOrder() throws CMSItemNotFoundException	{
		final String METHOD_NAME = "processReturnOrder()";
		boolean response = true;
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_RETURN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final CartData cartData = jnjGTCartFacade.getSessionCart();
		if (cartData.getEntries().size() == 0)
		{
			jnjGTCartFacade.changeOrderType(JnjOrderTypesEnum.ZRE.getCode());
			response = false;
		}
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_RETURN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return response;
	}

	/**
	 * This method is used when we click On Order Return Pop Up.
	 *
	 * @throws CMSItemNotFoundException
	 * @return the String
	 */

	@PostMapping("/startReturn")
	public String startReturnToCart(@RequestParam("keepItems") final String keepItems,
			@RequestParam("deleteItems") final String deleteItems) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "startReturnToCart()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_RETURN_POST, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		if (Boolean.valueOf(deleteItems))
		{
			jnjGTCartFacade.cleanSavedCart();
		}
		jnjGTCartFacade.changeOrderType(JnjOrderTypesEnum.ZRE.getCode());
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_RETURN_POST, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return REDIRECT_PREFIX + "/cart";
	}

	/**
	 * This method is used for getting New Order Pop Up.
	 *
	 * @throws CMSItemNotFoundException
	 * @return the boolean
	 */
	@ResponseBody
	@GetMapping("/startNewOrder")
	public boolean startNewOrder() throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "startReturn()";
		boolean response = true;
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_NEW_ORDER, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final CartData cartData = jnjGTCartFacade.getSessionCart();
		if (cartData.getEntries().size() == 0)
		{
			jnjGTCartFacade.changeOrderType(jnjGTCartFacade.getDefaultOrderType().toString());
			response = false;
		}
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_NEW_ORDER, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return response;
	}


	/**
	 * This method is used when we click On New Order Pop Up.
	 *
	 * @throws CMSItemNotFoundException
	 * @return the String
	 */

	@PostMapping("/startNewOrder")
	public String startNewOrderToCart(@RequestParam("keepItems") final String keepItems,
			@RequestParam("deleteItems") final boolean deleteItems) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "startReturnToCart()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_NEW_ORDER_POST, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		if (deleteItems)
		{
			jnjGTCartFacade.cleanSavedCart();
		}
		jnjGTCartFacade.changeOrderType(jnjGTCartFacade.getDefaultOrderType().toString());
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_NEW_ORDER_POST, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return REDIRECT_PREFIX + "/cart";
	}




	/**
	 * This method is used for getting Request New Quote
	 *
	 * @throws CMSItemNotFoundException
	 * @return the boolean
	 */
	@ResponseBody
	@GetMapping("/requestPriceQuote")
	public boolean requestNewQuote() throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "startReturn()";
		boolean response = true;
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_NEW_QUOTE, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final CartData cartData = jnjGTCartFacade.getSessionCart();
		if (cartData.getEntries().size() == 0)
		{
			jnjGTCartFacade.changeOrderType(JnjOrderTypesEnum.ZQT.getCode());
			response = false;
		}
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_NEW_QUOTE, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return response;
	}

	/**
	 * This method is used when we click On New Order Pop Up.
	 *
	 * @throws CMSItemNotFoundException
	 * @return the String
	 */

	@PostMapping("/requestPriceQuote")
	public String requestNewQuoteToCart(@RequestParam("keepItems") final String keepItems,
			@RequestParam("deleteItems") final String deleteItems) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "startReturnToCart()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_NEW_QUOTE_POST, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		if (Boolean.valueOf(deleteItems))
		{
			jnjGTCartFacade.cleanSavedCart();
		}
		jnjGTCartFacade.changeOrderType(JnjOrderTypesEnum.ZQT.getCode());
		CommonUtil.logMethodStartOrEnd(Logging.HOME_START_NEW_QUOTE_POST, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return REDIRECT_PREFIX + "/cart";
	}


	/**
	 * This method is used when we click On One Of The Templates
	 *
	 * @throws CMSItemNotFoundException
	 * @return the String
	 */


	@PostMapping("/viewTemplate")
	public String getTemplateDetails(final JnjGTHomePageForm homePageForm) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getTemplateDetails()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_VIEW_TEMPLATE_DETAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(Logging.HOME_VIEW_TEMPLATE_DETAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return REDIRECT_PREFIX + "/templates/templateDetail/" + homePageForm.getTemplate();
	}



	/**
	 * This method is used to add items to the cart from the home page by using Order Templates
	 *
	 * @param templateCode
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/addToCartFromTemplate")
	public String addToCartFromTemplate(@RequestParam("templateCode") final String templateCode, final Model model)
			throws CMSItemNotFoundException
	{
	return addToCartFromTemplateHomePage(templateCode, model);
	}
	protected String addToCartFromTemplateHomePage(final String templateCode, final Model model) throws CMSItemNotFoundException {

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "addToCartFromTemplate()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_QUICK_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap = jnjGTOrderTemplateFacade.addToCartFromHomeTemplate(responseMap, templateCode);

		if (!responseMap.isEmpty() && responseMap.containsKey(CART_MODIFICATAION_DATA))
		{
			final JnjCartModificationData cartData = (JnjCartModificationData) responseMap.get(CART_MODIFICATAION_DATA);
			model.addAttribute(PRODUCT, cartData.getCartModifications().get(0).getEntry().getProduct());
			model.addAttribute(CART_DATA, cartData);
			responseMap.remove(CART_MODIFICATAION_DATA);
		}
		model.addAttribute(ADD_STATUS, responseMap);
		// Added to get Updated Count on Minicartcomponent
		final CartData cartData = jnjGTCartFacade.getSessionCart();
		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));
		CommonUtil.logMethodStartOrEnd(Logging.HOME_QUICK_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return getView(LoginaddonControllerConstants.Views.Fragments.Cart.AddToCartHomePopup);
	}

	@PostMapping("/getBroadCastContent")
	@ResponseBody
	public Map<String, String> getBroadCastContent(@RequestParam("broadCastId") final String broadCastId)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getBroadCastContent()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_GET_BROADCAST_CONTENT, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final JnjGTBroadCastData broadCastData = jnjGTOperationsFacade.getBroadCastDataForId(broadCastId);
		final Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put(broadCastData.getType(), broadCastData.getContent());
		CommonUtil.logMethodStartOrEnd(Logging.HOME_GET_BROADCAST_CONTENT, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return responseMap;

	}
	//bulk product upload from home page
	@PostMapping("/homepageFileUpload")
	@ResponseBody
	public void homepageFileUpload(final Model model,
			@RequestParam(value = "uploadmultifilehome", required = false) final List<MultipartFile> uploadmultifilehomeList,
			final HttpServletResponse response) throws CMSItemNotFoundException, IOException
	{
		uploadOrderFileHomePage(model, uploadmultifilehomeList, response);
	}
	
	/**
	 * 
	 * @param model
	 * @param uploadmultifilehomeList
	 * @param response
	 * @throws CMSItemNotFoundException
	 * @throws IOException
	 */
	protected void uploadOrderFileHomePage(final Model model, final List<MultipartFile> uploadmultifilehomeList,
			final HttpServletResponse response) throws CMSItemNotFoundException, IOException {

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final int initialMapSize;
		Map<String, String> productQuantityMap = null;
		final Map<String, String> responseMap = new LinkedHashMap<String, String>(); //Soumitra - change to correct(reverse) the order in which product is added from excel

		for (final MultipartFile uploadmultifilehome : uploadmultifilehomeList)
		{
			productQuantityMap = jnjGTCartFacade.fileConverter(uploadmultifilehome);
			if (!productQuantityMap.isEmpty())
			{
				for (final Map.Entry<String, String> entry : productQuantityMap.entrySet())
				{
					
					if(StringUtils.isNotBlank(entry.getKey().trim())){
						responseMap.put(entry.getKey().trim(), entry.getValue());
					}
					
				}
			}
		}

		if (!responseMap.isEmpty())
		{
			initialMapSize = responseMap.size();
			final JnjCartModificationData modificaionData = addMultipleProds(responseMap, model, true); // true is from home page

			// Added to get Updated Count on Minicartcomponent
			final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
			if(getSessionService().getAttribute("isProductsValid") != null)
			{
				final Object isProductsValidFlag = getSessionService().getAttribute("isProductsValid");
				LOG.debug("isProductsValidFlag in controller "+isProductsValidFlag);
				if((initialMapSize >= responseMap.size()) && ((Boolean) isProductsValidFlag).booleanValue())
			//if (initialMapSize >= responseMap.size())
			{
		 
			 
			 if(responseMap.containsKey("gaProducts")){
					 responseMap.put(SUCCESS, String.valueOf(jnjGTCartFacade.getSessionCart().getTotalUnitCount()));
					 responseMap.put("showMinQtyMsg", String.valueOf(modificaionData.isShowQtyAdjustment())); 
				 }else{
					 //added for home page bulk upload products for count display in the cart
					 responseMap.put("totalCartCount", String.valueOf(jnjGTCartFacade.getSessionCart().getTotalUnitCount())); 
				 }
			   } 
			}
		}

		else
		{
			//responseMap.put(EMPTY, EMPTY);
			responseMap.put("EmptySheet", LoginaddonConstants.UPLOAD_ERROR);
		}

		if (responseMap.size() > 1 && responseMap.containsKey(EMPTY))
		{
			responseMap.remove(EMPTY);
		}
		/**
		 * Below code is used instead of the @Responsebody annotation and returning the map in order to fix a defect in
		 * which IE8 would attempt to download the JSON response instead of running it normally. (Please see defect 32682)
		 */
		response.setContentType(TEXT_HTML);
		final PrintWriter writer = response.getWriter();
		writer.println(new ObjectMapper().writeValueAsString(responseMap));
		writer.close();
		
	}

	protected JnjGTPageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final JnjGTPageableData pageableData = new JnjGTPageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	/**
	 * This method is used to get BroadCast Messages.
	 *
	 * @return the String
	 */

	public List<JnjGTBroadCastData> getBroadCastMessages()
	{
		return jnjGTOperationsFacade.getBroadCastMessages();
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
	
	/**
	 * This method sets the current b2b unit in the model
	 *
	 * @param model
	 */
	protected void setCurrentB2BUnitIdInModel(final Model model)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		//LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "setCurrentB2BUnitIdInModel";
		//** Fetching current NA customer data **//*
		if (null != jnjGTCustomerFacade.getCurrentGTCustomer()
				&& jnjGTCustomerFacade.getCurrentGTCustomer() instanceof JnjGTCustomerBasicData)
		{
			//** Set current account UID in model **//*
			logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Current NA customer FOUND!");
			model.addAttribute(Jnjb2bCoreConstants.Reports.CURRENT_ACCOUNT_ID,((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).getCurrentB2BUnitID());
			model.addAttribute(LoginaddonControllerConstants.Views.Pages.Home.CURRENT_ACCOUNT_NAME,((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).getCurrentB2BUnitName());
			model.addAttribute(LoginaddonControllerConstants.Views.Pages.Home.SITE_NAME,((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).getJnjSiteName());
			model.addAttribute(LoginaddonControllerConstants.Views.Pages.Home.CURRENT_B2B_UNIT_GLN,((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).getCurrentB2BUnitGLN());
			model.addAttribute(LoginaddonControllerConstants.Views.Pages.Home.ADMIN_USER,((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).isAdminUser());
			model.addAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT,((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).getShowChangeAccount());
			model.addAttribute(LoginaddonControllerConstants.Views.Pages.Home.FIRST_NAME,((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).getFirstName());
			model.addAttribute(LoginaddonControllerConstants.Views.Pages.Home.LAST_NAME,((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).getLastName());
			logDebugMessage(Jnjb2bCoreConstants.Logging.HOME_PAGE, METHOD_NAME, "Current B2B Customer's firstName & lastName set in the model.");
		}
		else
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.HOME_PAGE, METHOD_NAME, "Current NA customer not found!");
		}

	}
	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
 }
	
	
	
	@GetMapping(BUILD_ORDER_RESET_PAGE_URL)
	 @RequireHardLogIn
	 public String buildOrderReset(final Model model, final HttpSession session) throws CMSItemNotFoundException
	 {
	  /*final JnjBuildOrderData buildOrder = jnjBuildOrderFacade.getBuildOrderFromSession(null);

	  session.setAttribute(JnjBuildOrderFacade.BUILD_ORDER_SESSION_ATTRIBUTE, buildOrder);
	  model.addAttribute("orderform", buildOrder);
    LOG.info("Reset order form================================================");
	  
		model.addAttribute("metaRobots", "no-index,no-follow");
		  model.addAttribute("noProductToCart", true);*/
	 
		
		return REDIRECT_PREFIX + "/home";
		
	 
	 }
	
	
	/**
	 * Gets the registration status.
	 *
	 * @return the registration status
	 */
	protected boolean isResetPasswordComplete(){
		final String METHOD_NAME = "isResetPasswordComplete()";
		CommonUtil.logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return jnjGTCustomerFacade.isResetPasswordComplete();
	}
	
	
	@PostMapping("/getFileUploadQuantity")
	@ResponseBody
	public boolean getFileUploadQuantity(final Model model,
			@RequestParam(value = "uploadmultifilehome", required = false) final List<MultipartFile> uploadmultifilehomeList,
			final HttpServletResponse response) throws CMSItemNotFoundException, IOException
	{		
		
		boolean returnValue=false;		
		
		Map<String, String> productQuantityMap = null;
		final Map<String, String> responseMap = new HashMap<String, String>();
		for (final MultipartFile uploadmultifilehome : uploadmultifilehomeList) {
			productQuantityMap = jnjGTCartFacade.fileConverter(uploadmultifilehome);
			if (!productQuantityMap.isEmpty()) {
				for (final Map.Entry<String, String> entry : productQuantityMap.entrySet()) {
					if(StringUtils.isNotBlank(entry.getKey().trim())){
						responseMap.put(entry.getKey().trim(), entry.getValue());
						if(Integer.parseInt(entry.getValue()) > 99){
							returnValue=true;	
						}
						
					}
				}
			}
		}
		
	
		return returnValue;
	}
	
	/**
	 * @param uploadmultifilehomeList
	 * @param response
	 * @return jnjGTHomePageForm
	 * @throws CMSItemNotFoundException
	 * @throws IOException
	 */
	@ResponseBody 
	@PostMapping(value = "/isNonContractProduct",produces = "application/json")
	public JnjGTHomePageForm validateIsNonContractProduct(@RequestParam(value = "uploadmultifilehome", required = false) final List<MultipartFile> uploadmultifilehomeList,
			final HttpServletResponse response) throws CMSItemNotFoundException, IOException	{
		LOG.info("validateIsNonContractProduct method invoked in");
		// start to identify contract product or not
		JnjGTHomePageForm jnjGTHomePageForm = new JnjGTHomePageForm();
		Map<String, String> productQuantityMap = null;
		final Map<String, String> responseMap = new HashMap<String, String>();

		for (final MultipartFile uploadmultifilehome : uploadmultifilehomeList) {
			productQuantityMap = jnjGTCartFacade.fileConverter(uploadmultifilehome);
			if (!productQuantityMap.isEmpty()) {
				for (final Map.Entry<String, String> entry : productQuantityMap.entrySet()) {
					if(StringUtils.isNotBlank(entry.getKey().trim())){
						responseMap.put(entry.getKey().trim(), entry.getValue());
					}
				}
			}
		}
		
		if (!responseMap.isEmpty()&&jnjGTHomePageForm.getisQuantityStatus()== false) {
				// start to identify contract product or not
				Set<String> productIds = responseMap.keySet();
				LOG.info("productIds in controller "+productIds);
				String[] strProductLst = new String[productIds.size()];
				strProductLst = productIds.toArray(strProductLst);
				JnjContractFormData formData = jnjGTCartFacade.validateIsNonContract(strProductLst,null);
				jnjGTHomePageForm.setNonContractProductInCart(formData.getIsNonContractProductInCart());
				jnjGTHomePageForm.setNonContractProductInSelectedList(formData.getIsNonContractProductInSelectedList());
				LOG.info("getIsNonContractProductInCart in controller : "+jnjGTHomePageForm.isNonContractProductInCart());
				LOG.info("getIsNonContractProductInSelectedList in controller : "+jnjGTHomePageForm.isNonContractProductInSelectedList());
				// end to identify contract product or not
		}
		return jnjGTHomePageForm;
	}
	
	/**
	 * This method returns list for suto complete for matching search text.
	 * @param term
	 * @return
	 */
	@ResponseBody
	@GetMapping("/autocompleteAccount")
	public List<String> getAutocompleteSuggestionsSecure(@RequestParam("term") final String term){
		List<String> accAutoSearchList = new ArrayList<String>();
		
		accAutoSearchList = jnjGTCustomerFacade.getAccountsListForAutoSuggest(term);
		
		return accAutoSearchList;
	}
	
	/**
	 * Change account functionality for auto suggest dropdown
	 * @param selectedAccountUid
	 * @param model
	 * @param request
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/changeAccountAutoSuggest")
	public String changeAccountFromAutoSuggest(@RequestParam(value = "uid") final String selectedAccountUid,final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final String METHOD_NAME = "changeAccountFromAutoSuggest()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean status = false;
		
		JnJB2BUnitModel selectedB2bUnit = null;

		/** Checking if the model contains the survey flag already **/
		if (!model.containsAttribute(LoginaddonConstants.Login.SURVEY))
		{
			/** Setting the survey flag **/
			setSurveyFlag(model);
		}

		/** Checking if account UID parameter is not null **/
		if (selectedAccountUid != null)
		{
			//** Calling facade layer to change account **//*
			status = jnjGTCustomerFacade.changeAccount(selectedAccountUid);
			
			selectedB2bUnit = jnjGTCustomerFacade.getB2BUnitForUid(selectedAccountUid);
			logDebugMessage(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, "Change account status :: " + status);
			String currentSiteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
			final String sourceSystemIdForUnit = jnjGTUnitFacade.getSourceSystemIdForUnit();
			
			switchSite(currentSiteName, sourceSystemIdForUnit);
			LOG.info("currentSiteName : "+currentSiteName);
			LOG.info("sourceSystemIdForUnit : "+sourceSystemIdForUnit);
			//Need to restore cart accordingly
			jnjGTCartFacade.restoreCartForCurrentUser(sourceSystemIdForUnit);
			
			// Remove free Goods from the session
			sessionService.removeAttribute("freeGoodsMap");

		}
		

		/** Checking status for errors **/
		if (!status)
		{
			model.addAttribute(CHANGE_ACCOUNT_ERROR, Boolean.TRUE);
		}
		else
		{
			/**
			 * Adding selected account info to the model on successful account change : Added to allow current account to
			 * reflect in the header
			 **/
			model.addAttribute(LoginaddonConstants.Login.ACCOUNT_UID, selectedAccountUid);
			model.addAttribute(LoginaddonConstants.Login.ACCOUNT_NAME, selectedB2bUnit.getName());
			model.addAttribute(LoginaddonConstants.Login.ACCOUNT_GLN, null);
			setCurrentB2BUnitIdInModel(model);
			logDebugMessage(LoginaddonConstants.Logging.ACCOUNTS, METHOD_NAME, "Current account info has been set for the header.");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.END_OF_METHOD);
		JnJB2BUnitModel currentB2bUnit =jnjGTCustomerFacade.getCurrentB2bUnit();
		if(currentB2bUnit != null)
		{
			List<JnjContractModel> contractList = (List<JnjContractModel>) currentB2bUnit.getContracts();
			if(CollectionUtils.isNotEmpty(contractList))
			{
				sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.TRUE);
				model.addAttribute(SHOW_CONTRACT, LoginaddonConstants.TRUE);
			}
			else
			{
				sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.FALSE);
				model.addAttribute(SHOW_CONTRACT, LoginaddonConstants.FALSE);
			}
		}
		return getHome(model, false,request);
	}
	
}