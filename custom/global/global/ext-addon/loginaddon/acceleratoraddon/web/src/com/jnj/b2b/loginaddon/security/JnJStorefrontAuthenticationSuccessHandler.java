/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.b2b.loginaddon.security;

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.b2b.loginaddon.utill.JnjForgotPasswordErrorCounter;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.security.BruteForceAttackCounter;
import com.jnj.b2b.storefront.security.RequestUtility;


/**
 * Success handler initializing user settings and ensuring the cart is handled correctly
 */
public class JnJStorefrontAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{
	protected CustomerFacade customerFacade;
	protected String defaultTargetUrl;
	protected boolean useReferer;
	protected RequestCache requestCache;
	protected UiExperienceService uiExperienceService;
	protected CartFacade cartFacade;
	protected SessionService sessionService;
	protected BruteForceAttackCounter bruteForceAttackCounter;
	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;
	protected String targetUrlParameter = null;
	/** this is the forgot password error counter which will be reset in case of login success **/
	@Autowired
	protected JnjForgotPasswordErrorCounter jnjForgotPasswordErrorCounter;
	/** The user service **/
	@Autowired
	protected UserService userService;
	@Autowired
	protected ModelService modelService;
	/** Logger - for logging purpose **/
	protected static final Logger LOG = Logger.getLogger(JnJStorefrontAuthenticationSuccessHandler.class);
	protected Map<UiExperienceLevel, Boolean> forceDefaultTargetForUiExperienceLevel;
	@Autowired
	protected RequestUtility requestUtil;
	public RequestUtility getRequestUtil() {
		return requestUtil;
	}

	public void setRequestUtil(RequestUtility requestUtil) {
		this.requestUtil = requestUtil;
	}

	@Autowired
	private BaseSiteService baseSiteService;
	/**
	 * This method is called when authentication is successful
	 *
	 * @author sanchit.a.kumar
	 */
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
										final Authentication authentication) throws IOException, ServletException
	{

		final String METHOD_NAME = "onAuthenticationSuccess";
		logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean passwordexpiryflag = false;

		jnjGTCustomerFacade.loginSuccess();

		final UserModel currentUserModel = userService.getCurrentUser();
		final String userId = currentUserModel.getUid();
		/** Checking if the current user is an instance of JnJNAB2bCustomerModel **/
		if (currentUserModel instanceof JnJB2bCustomerModel)
		{
			/** Setting an attribute to display that the user is  going on the home page for the first time **/

			sessionService.setAttribute(LoginaddonConstants.Login.FIRST_LOGIN_CHECK, Boolean.TRUE);

			/** Fetching the current User and type casting it to JnJNAB2bCustomerModel **/
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) currentUserModel;


			/** Checking the password expiry **/
			passwordexpiryflag = checkPasswordExpiry(currentUser);
			if (passwordexpiryflag)
			{
				LOG.info(LoginaddonConstants.Logging.LOGIN + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "REDIRECTING TO LOGIN :: User's password is expired!");

				/** Setting the UID of the user in the session who's password expired so as to foolproof password reset **/
				sessionService.setAttribute(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM, userId);


				/** Redirecting to the login page passing the parameter for password expiry **/
				getRedirectStrategy().sendRedirect(request, response, LoginaddonConstants.Login.EXPIRED_PASSWORD_LOGIN_URL + userId);
			}


			final Set<PrincipalGroupModel> allGroups = currentUser.getGroups();

			/** Setting Ordering Rights true/false in session **/
			setOrderingRightsInSession(allGroups);

			/** Setting Whether the User is Tier 1 or Tier2 admin in session **/
			setAdminRightsInSession(allGroups);
			setAllowedFranchise(currentUser);

			currentUser.setLastLogin(Calendar.getInstance().getTime());
			modelService.save(currentUser);

		}

		/** Resetting the brute force attack counter **/
		getBruteForceAttackCounter().resetUserCounter(userId);

		/** Added below code to reset the forgot password error counter when the user has logged in successfully **/
		jnjForgotPasswordErrorCounter.getSecretQuestionAttackCounter().remove(userId);

		/** Setting in the session - first time login **/
		sessionService.setAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN, Boolean.TRUE);

		logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD);


		if (!passwordexpiryflag)

		{
			if(!((boolean) sessionService.getAttribute("ssoLogin")))
			{
				savedOnAuthenticationSuccess(request, response, authentication);

				JnJB2BUnitModel currentB2bUnit =jnjGTCustomerFacade.getCurrentB2bUnit();
				if(currentB2bUnit != null)
				{
					List<JnjContractModel> contractList = (List<JnjContractModel>) currentB2bUnit.getContracts();
					if(CollectionUtils.isNotEmpty(contractList))
					{
						sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.TRUE);
					}
					else
					{
						sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.FALSE);
					}
				}
			}
		}

	}

	public JnJStorefrontAuthenticationSuccessHandler() {

		this.requestCache = new HttpSessionRequestCache();
	}

	
	public void savedOnAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		String targetUrl = null;
		final HttpSession session = request.getSession();
		String targetUrlParameter = getTargetUrlParameter();
		if ((isAlwaysUseDefaultTargetUrl())
			|| ((targetUrlParameter != null) && (StringUtils
			.hasText(request.getParameter(targetUrlParameter))))) {
			this.requestCache.removeRequest(request, response);
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}

		clearAuthenticationAttributes(request);
		String savedUrl = requestUtil.getSavedRedirectUrl(request, response);
		if(org.apache.commons.lang3.StringUtils.isNotBlank(savedUrl)){
			targetUrl = savedUrl;
		}
		if(null==savedUrl){
			targetUrl = "/login";
		}
		targetUrl = calculateRelativeRedirectUrl(request.getContextPath(),targetUrl);
		
		if(org.apache.commons.lang3.StringUtils.isNotBlank(targetUrl)){
			String temp = targetUrl.substring(1);
			BaseSiteModel baseSiteModel = baseSiteService.getBaseSiteForUID(temp);
			if(null!=baseSiteModel){
				targetUrl = "/home";
			}
		}

		this.logger.debug("Redirecting to DefaultSavedRequest Url: "
			+ targetUrl);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}


	public SavedRequest getRequest(HttpServletRequest currentRequest,
								   HttpServletResponse response) {
		HttpSession session = currentRequest.getSession(false);

		if (session != null) {
			return ((SavedRequest) session
				.getAttribute("SPRING_SECURITY_SAVED_REQUEST"));
		}

		return null;
	}

	protected String calculateRelativeRedirectUrl(final String contextPath, final String url)
	{
		if (UrlUtils.isAbsoluteUrl(url))
		{
			String relUrl = url.substring(url.indexOf("://") + 3);
			String modifiedContextPath = contextPath;
			final String urlEncodingAttributes = getSessionService().getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
			if (urlEncodingAttributes != null && !url.contains(urlEncodingAttributes)
				&& modifiedContextPath.contains(urlEncodingAttributes))
			{
				modifiedContextPath = org.apache.commons.lang3.StringUtils.remove(modifiedContextPath, urlEncodingAttributes);
			}
			if (ObjectUtils.isEmpty(relUrl) || ObjectUtils.isEmpty(modifiedContextPath))
			{
				relUrl = "/";
			}
			else
			{
				relUrl = relUrl.substring(relUrl.indexOf(modifiedContextPath) + modifiedContextPath.length());
			}
			return relUrl;
		}
		else
		{
			return url;
		}
	}
	/**
	 * This method checks if the user's password has expired or not. If expiry is detected it will return true.
	 *
	 * @param jnJNAB2bCustomerModel
	 * @author sanchit.a.kumar
	 * @return true/false
	 */
	protected boolean checkPasswordExpiry(final JnJB2bCustomerModel jnJB2bCustomerModel)
	{
		final String METHOD_NAME = "checkPasswordExpiry";
		logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		boolean expiry = false;
		/** Fetching last password change date **/
		final Date passwordChangeDate = jnJB2bCustomerModel.getPasswordChangeDate();

		//if flag - forceFul expire is true then return true for  User Management Password Expire Functionality
		if (jnJB2bCustomerModel.getForcefulExpired())
		{
			return true;
		}
		logDebugMessage(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, "User's last password change date :: " + passwordChangeDate);

		/** Checking if last password change date retrieved is not null **/
		if (null != passwordChangeDate)
		{
			/** Specifying date format - MM/dd/yyyy **/
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				LoginaddonConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT);

			/** Fetching current date **/
			final Date currentDate = new Date();

			/** Get calendar instance for Date of Expiry **/
			final Calendar dateOfExpiry = Calendar.getInstance();

			/** Setting current date in the dateOfExpiry instance **/
			dateOfExpiry.setTime(currentDate);

			/** Subtracting the number of days in which password expires from the current date **/
			dateOfExpiry.add(Calendar.DATE, (-1 * Config.getInt(LoginaddonConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_KEY, 90)));

			/** Formatting the date of expiry **/
			simpleDateFormat.setCalendar(dateOfExpiry);

			/** Get calendar instance for Last Password change Date **/
			final Calendar passwordChangeDateCal = Calendar.getInstance();

			/** Setting Last Password change Date in the passwordChangeCal instance **/
			passwordChangeDateCal.setTime(passwordChangeDate);

			/** Formatting the last password change date **/
			simpleDateFormat.setCalendar(passwordChangeDateCal);

			/** Comparing the last password change date with the date of expiry **/
			if (passwordChangeDateCal.before(dateOfExpiry))
			{
				/** EXPIRED! **/
				expiry = true;
				LOG.info(LoginaddonConstants.Logging.LOGIN + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "User's password is expired!");
			}
			else
			{
				/** NOT EXPIRED **/
				LOG.info(LoginaddonConstants.Logging.LOGIN + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "User's password has not yet expired. User fit for login");
			}
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD);
		return expiry;
	}

	/**
	 * This method checks if the user has the place order group assigned and based on that sets in the session true or
	 * false against ordering rights key.
	 *
	 */
	protected void setOrderingRightsInSession(final Set<PrincipalGroupModel> allGroups)
	{
		final String METHOD_NAME = "setOrderingRightsInSession";
		logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Checking if the current user's groups contain ordering rights **/
		if (allGroups
			.contains(userService.getUserGroupForUID(Config.getParameter(LoginaddonConstants.Login.USER_GROUP_PLACE_ORDER)))
			|| allGroups
			.contains(userService.getUserGroupForUID(Config.getParameter(LoginaddonConstants.Login.USER_GROUP_ADMIN)))
			|| allGroups.contains(userService.getUserGroupForUID(Config
			.getParameter(LoginaddonConstants.Register.PLACE_ORDER_BUYER_USER_ROLE)))
			|| allGroups.contains(userService.getUserGroupForUID(Config
			.getParameter(LoginaddonConstants.Register.PLACE_ORDER_SALES_USER_ROLE)))
			|| allGroups.contains(userService.getUserGroupForUID(Config.getParameter(LoginaddonConstants.Login.USER_GROUP_CSR))))
		{
			/** Set user ordering privileges **/
			sessionService.setAttribute(LoginaddonConstants.Login.ORDERING_RIGHTS, Boolean.valueOf(true));
		}
		/** Setting the sales rep user as false by default **/
		sessionService.setAttribute(LoginaddonConstants.Login.SALES_REP_USER, Boolean.FALSE);
		if (allGroups.contains(userService.getUserGroupForUID(Config.getParameter(LoginaddonConstants.Login.USER_GROUP_ADMIN))))
		{
			sessionService.setAttribute(LoginaddonConstants.Login.USER_TYPE, JnjGTUserTypes.PORTAL_ADMIN);
		}
		else if (allGroups.contains(userService.getUserGroupForUID(Config
			.getParameter(LoginaddonConstants.Login.USER_GROUP_PLACED_ORDER_SALES_REP))))
		{
			sessionService.setAttribute(LoginaddonConstants.Login.SALES_REP_USER, Boolean.TRUE);
			sessionService.setAttribute(LoginaddonConstants.Login.SALESREP_ORDERING_RIGHTS, Boolean.TRUE);
			sessionService.setAttribute(LoginaddonConstants.Login.USER_TYPE, JnjGTUserTypes.PLACED_ORDER_SALES_REP);
		}

		else if (allGroups.contains(userService.getUserGroupForUID(Config
			.getParameter(LoginaddonConstants.Login.USER_GROUP_VIEW_ONLY_SALES_REP))))
		{
			sessionService.setAttribute(LoginaddonConstants.Login.SALES_REP_USER, Boolean.TRUE);
			sessionService.setAttribute(LoginaddonConstants.Login.USER_TYPE, JnjGTUserTypes.VIEW_ONLY_SALES_REP);
		}

		else if (allGroups.contains(userService.getUserGroupForUID(Config
			.getParameter(LoginaddonConstants.Login.PLACE_ORDER_BUYER_USER_ROLE))))
		{
			sessionService.setAttribute(LoginaddonConstants.Login.USER_TYPE, JnjGTUserTypes.VIEW_AND_PLACE_ORDER);
		}

		else if (allGroups.contains(userService.getUserGroupForUID(Config
			.getParameter(LoginaddonConstants.Login.USER_GROUP_VIEW_ONLY))))
		{
			sessionService.setAttribute(LoginaddonConstants.Login.USER_TYPE, JnjGTUserTypes.VIEW_ONLY);
		}

		//Changes for Serialization AAOL-5669
		else if (allGroups.contains(userService.getUserGroupForUID(Config
			.getParameter(LoginaddonConstants.Login.USER_GROUP_SERIALIZATION))))
		{
			sessionService.setAttribute(LoginaddonConstants.Login.USER_TYPE, JnjGTUserTypes.SERIAL_USER);
		}

		if (allGroups.contains(userService.getUserGroupForUID(Config
			.getParameter(LoginaddonConstants.Login.USER_GROUP_NO_CHARGE_USERS))))
		{
			sessionService.setAttribute(LoginaddonConstants.Login.NO_CHARGE_INTERNAL_USER, Boolean.TRUE);
		}
		LOG.debug("User Type is :>>>" + sessionService.getAttribute(LoginaddonConstants.Login.USER_TYPE));

		logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method checks if the user is tier1 or Tier2 Admin.
	 *
	 */

	protected void setAdminRightsInSession(final Set<PrincipalGroupModel> allGroups)
	{
		if (allGroups.contains(userService.getUserGroupForUID(Config.getParameter(LoginaddonConstants.Login.USER_GROUP_ADMIN))))
		{
			sessionService.setAttribute(LoginaddonConstants.UserSearch.USER_TIER_TYPE, LoginaddonConstants.UserSearch.USER_TIER2);
		}
		else if (allGroups.contains(userService.getUserGroupForUID(Config.getParameter(LoginaddonConstants.Login.USER_GROUP_CSR))))
		{
			sessionService.setAttribute(LoginaddonConstants.UserSearch.USER_TIER_TYPE, LoginaddonConstants.UserSearch.USER_TIER1);
		}

	}
	/**
	 * Added for Selection of User Franchise for AAOL-4913
	 * @param currentUser
	 */

	protected void setAllowedFranchise(final JnJB2bCustomerModel currentUser)
	{
		StringBuffer allowedFranchiseCommaSeparated = new StringBuffer();
		StringBuffer allowedFranchiseNoQuotes = new StringBuffer();
		List<CategoryModel> categoryModels= currentUser.getAllowedFranchise();
		if (CollectionUtils.isNotEmpty(categoryModels))
		{
			for (CategoryModel categoryModel : categoryModels) {
				if(!ObjectUtils.isEmpty(categoryModel.getCode())){
					allowedFranchiseCommaSeparated.append("'"+categoryModel.getCode()+"'"+LoginaddonConstants.Register.COMMA_SEPARATOR);
					allowedFranchiseNoQuotes.append(categoryModel.getCode()+LoginaddonConstants.Register.COMMA_SEPARATOR);
				}
			}
			allowedFranchiseCommaSeparated.deleteCharAt(allowedFranchiseCommaSeparated.lastIndexOf(LoginaddonConstants.Register.COMMA_SEPARATOR));
			allowedFranchiseNoQuotes.deleteCharAt(allowedFranchiseNoQuotes.lastIndexOf(LoginaddonConstants.Register.COMMA_SEPARATOR));
			logger.info("setting allowedFranchiseCommaSeparated in session value=>"+allowedFranchiseCommaSeparated);
			sessionService.setAttribute(LoginaddonConstants.UserSearch.ALLOWEDFRANCHISE, allowedFranchiseCommaSeparated.toString());
			sessionService.setAttribute(LoginaddonConstants.UserSearch.ALLOWEDFRANCHISECONDITION, allowedFranchiseNoQuotes.toString());
		}
		else{
			logger.info("AllowedFranchise is empty. Setting allowedFranchiseCommaSeparated in session value as null=>"+allowedFranchiseCommaSeparated);
			sessionService.setAttribute(LoginaddonConstants.UserSearch.ALLOWEDFRANCHISE, LoginaddonConstants.Register.EMPTY_STRING);
			sessionService.setAttribute(LoginaddonConstants.UserSearch.ALLOWEDFRANCHISECONDITION, LoginaddonConstants.Register.EMPTY_STRING);
		}
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	/*
	 * @see org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler#
	 * isAlwaysUseDefaultTargetUrl()
	 */
	@Override
	protected boolean isAlwaysUseDefaultTargetUrl()
	{
		final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		if (getForceDefaultTargetForUiExperienceLevel().containsKey(uiExperienceLevel))
		{
			return Boolean.TRUE.equals(getForceDefaultTargetForUiExperienceLevel().get(uiExperienceLevel));
		}
		else
		{
			return false;
		}
	}

	protected Map<UiExperienceLevel, Boolean> getForceDefaultTargetForUiExperienceLevel()
	{
		return forceDefaultTargetForUiExperienceLevel;
	}

	public void setForceDefaultTargetForUiExperienceLevel(
		final Map<UiExperienceLevel, Boolean> forceDefaultTargetForUiExperienceLevel)
	{
		this.forceDefaultTargetForUiExperienceLevel = forceDefaultTargetForUiExperienceLevel;
	}


	protected BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	public void setBruteForceAttackCounter(final BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
	}



	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
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



	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}

	public boolean isUseReferer() {
		return useReferer;
	}

	public void setUseReferer(boolean useReferer) {
		this.useReferer = useReferer;
	}

	public String getTargetUrlParameter() {
		return targetUrlParameter;
	}

	public void setTargetUrlParameter(String targetUrlParameter) {
		this.targetUrlParameter = targetUrlParameter;
	}

	public RequestCache getRequestCache() {
		return requestCache;
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	public UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}
	public JnjForgotPasswordErrorCounter getJnjForgotPasswordErrorCounter() {
		return jnjForgotPasswordErrorCounter;
	}

	public UserService getUserService() {
		return userService;
	}

	public ModelService getModelService() {
		return modelService;
	}
	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}
}
