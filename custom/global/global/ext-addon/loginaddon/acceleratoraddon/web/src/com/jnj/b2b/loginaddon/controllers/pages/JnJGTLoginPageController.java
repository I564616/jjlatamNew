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
import com.jnj.b2b.storefront.controllers.pages.LoginPageController;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 * Login Controller. Handles login account flow.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnJGTLoginPageController extends LoginPageController
{
	/**  */
	protected static final String LOGOUT = "Logout";
	protected static final String ATTEMPTS_EXCEEDED = "attemptsExceeded";
	protected static final String PASSWORD_EXPIRED_USER="passwordExpiredForThisUser";
	@Resource(name = "httpSessionRequestCache")
	protected HttpSessionRequestCache httpSessionRequestCache;

	/** The session service **/
	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;
	/** The user service **/
	@Autowired
	protected UserService userService;

	@Autowired
	protected CMSSiteService cmsSiteService;

	public SessionService getSessionService() {
		return sessionService;
	}

	public B2BCommerceB2BUserGroupService getB2BCommerceB2BUserGroupService() {
		return b2BCommerceB2BUserGroupService;
	}

	public UserService getUserService() {
		return userService;
	}

	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}
	/** The Constant LOGIN. */
	protected static final String LOGIN = "Login";

	/** Getting the LOGGER. */
	protected static final Logger LOG = Logger.getLogger(JnJGTLoginPageController.class);

	//@Autowired
	//protected MessageFacade messageFacade;

	/** Login warning limit **/
	protected static final String LOGIN_WARN_LIMIT = Config.getParameter(LoginaddonConstants.Login.LOGIN_ERROR_LIMIT);

	@Override
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	/**
	 * 
	 * This method is used for fetching the login page by setting essential data in the model
	 * 
	 * @param referer
	 * @param loginError
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@Override
	@GetMapping
	public String doLogin(@RequestHeader(value = "referer", required = false) final String referer,
			@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			@RequestParam(value = "passwordExpireToken", required = false) final String passwordExpireToken,
			@RequestParam(value = "email", required = false) final String email,
			@RequestParam(value = "contactUs", required = false) final String helpFlag, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
			throws CMSItemNotFoundException {
			/*Start AAOL 5074*/
			return doLoginPage(referer, loginError, passwordExpireToken, email, helpFlag, model, request, response, session);
			/*End AAOL 5074*/
		}
			
			public String doLoginPage(final String referer, final boolean loginError, final String passwordExpireToken,
			 final String email, final String helpFlag, final Model model, final HttpServletRequest request, 
			 final HttpServletResponse response, final HttpSession session) throws CMSItemNotFoundException{
		
			logMethodStartOrEnd(LOGIN, "doLogin()", Logging.BEGIN_OF_METHOD);
	
			//** Checking if the attempts exceeded and the account is locked, then setting the message key for the error **//*
			if (null != sessionService.getAttribute(ATTEMPTS_EXCEEDED))
			{
				model.addAttribute(ATTEMPTS_EXCEEDED, Boolean.TRUE);
				sessionService.removeAttribute(ATTEMPTS_EXCEEDED);
			}
			
			/** Checking for expired password parameter in the session **/
			//AAOL-4915
			if (null != sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRED_FOR_USER))
			{
				String message = "passwordExpiredForThisUser";
				model.addAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRED_FOR_USER, Boolean.TRUE);
				model.addAttribute(PASSWORD_EXPIRED_USER, message);
				sessionService.removeAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRED_FOR_USER);
			}
			//End AAOL-4915
			storeReferer(referer, request, response);
			
			//** Checking for sign-out parameter in the request **//*
			if (null != request.getParameter(LoginaddonConstants.Login.LOGOUT_PARAM))
			{
				logDebugMessage(LOGIN, "doLogin()", "Logout Successful!");
				//** Setting the sign-out parameter in the model **//*
				model.addAttribute(LoginaddonConstants.Login.LOGOUT_PARAM, Boolean.TRUE);
			}
	
			//** Checking for session-availability parameter in the request (this is required only in case of PCM) **//*
			if (null != request.getParameter(LoginaddonConstants.Login.SESSION_EXPIRED_PARAM))
			{
				logDebugMessage(LOGIN, "doLogin()", "User's session expired");
				//** Setting the sign-out parameter in the model **//*
				model.addAttribute(LoginaddonConstants.Login.SESSION_EXPIRED_PARAM, Boolean.TRUE);
			}
	
			if (null != request.getParameter(LoginaddonConstants.Login.PASSWORD_EXPIRE_TOKEN))
			{
				model.addAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRE_TOKEN,
						request.getParameter(LoginaddonConstants.Login.PASSWORD_EXPIRE_TOKEN));
				model.addAttribute(LoginaddonConstants.Login.EMAIL, request.getParameter(LoginaddonConstants.Login.EMAIL));
			}
	                
	                if (null != helpFlag)
			{
				model.addAttribute("helpFlag", helpFlag);
			}
	
			//** Check to see which CMSsite is in use, and setting the corresponding value in session **//*
			final String currentSiteType = cmsSiteService.getCurrentSite().getUid();
			final String siteName = currentSiteType.equalsIgnoreCase(LoginaddonConstants.MDD_SITE_ID) ? LoginaddonConstants.MDD
					: LoginaddonConstants.CONS;
			sessionService.setAttribute(LoginaddonConstants.SITE_NAME, siteName);
			LOG.info("doLogin Site = "+siteName);
			logMethodStartOrEnd(LOGIN, "doLogin()", Logging.END_OF_METHOD);
			return getDefaultLoginPage(loginError, session, model);
		}



	@Override
	protected String getDefaultLoginPage(final boolean loginError, final HttpSession session, final Model model)
			throws CMSItemNotFoundException
	{
		logMethodStartOrEnd(LOGIN, "getDefaultLoginPage()", Logging.BEGIN_OF_METHOD);
		String errorMessage = null;
		/** login error found use case **/
		if (loginError)
		{
			/** Fetching session attribute for user-name **/
			final String username = (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);
			logDebugMessage(LOGIN, "getDefaultLoginPage()", "User Name :: " + username);
			Integer userFailedLogins = null;
			if (null != session.getAttribute("JNJ_USER_FAILED_LOGINS"))
			{
				/** Fetching session attribute for user failed login attempts **/
				userFailedLogins = (Integer) session.getAttribute("JNJ_USER_FAILED_LOGINS");
				logDebugMessage(LOGIN, "getDefaultLoginPage()", "User Failed logins :: " + userFailedLogins);
			}
			if (username != null && userService.isUserExisting(StringUtils.lowerCase(username)))
			{
				/** Fetching usermodel **/
				final UserModel userModel = userService.getUserForUID(StringUtils.lowerCase(username));
				//				final JnJNAB2bCustomerModel jnjNACustomer = (JnJNAB2bCustomerModel) userModel;
				/** Disabled account use case **/
				if (userModel != null)
				{

					if (userModel.isLoginDisabled())
					{
						errorMessage = LoginaddonConstants.Login.LOGIN_ERROR_ACCOUNT_DISABLED;
					}

				}
			}
			if (errorMessage == null)
			{
				/** Failed logins limit use case **/
				if (null != userFailedLogins && null != LOGIN_WARN_LIMIT
						&& userFailedLogins.intValue() == Integer.valueOf(LOGIN_WARN_LIMIT).intValue())
				{
					errorMessage = LoginaddonConstants.Login.LOGIN_WARNING_ACCOUNT_DISABLED;
				}
				/** Invalid credentials use case **/
				else
				{
					if (((Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION")) instanceof BadCredentialsException)
					{
						errorMessage = LoginaddonConstants.Login.LOGIN_ERROR_NOT_FOUND;
					}
					else if ((Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null)
					{
						errorMessage = ((Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION")).getMessage();
					}
				}
			}
			logDebugMessage(LOGIN, "getDefaultLoginPage()", "LOGIN ERROR :: message key :: " + errorMessage);
			model.addAttribute(LoginaddonConstants.Login.LOGIN_ERROR, errorMessage);
		}
		logMethodStartOrEnd(LOGIN, "getDefaultLoginPage()", Logging.END_OF_METHOD);
		return super.getDefaultLoginPage(loginError, session, model);
	}


	@Override
	public String getLoginView()
	{
		
		return getView(LoginaddonControllerConstants.Views.Pages.Account.AccountLoginPage);
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		logMethodStartOrEnd(LOGIN, "getSuccessRedirect()", Logging.BEGIN_OF_METHOD);
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}
		logMethodStartOrEnd(LOGIN, "getSuccessRedirect()", Logging.END_OF_METHOD);
		return "/my-account";
	}

	@Override
	protected AbstractPageModel getLoginCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("login");
	}

	@Override
	protected void storeReferer(String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
	
		if(null != request.getSession().getAttribute("originalReferer")){
		referer = (String)request.getSession().getAttribute("originalReferer");
		}
		logMethodStartOrEnd(LOGIN, "storeReferer()", Logging.BEGIN_OF_METHOD);
		/*if (StringUtils.isNotBlank(referer))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}*/
		
		if (StringUtils.isNotBlank(referer) && !request.getRequestURL().toString().contains("login"))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
		logMethodStartOrEnd(LOGIN, "storeReferer()", Logging.END_OF_METHOD);
	}
	/**
	 * This method returns the view for When the user click on the link on Reset Url from the email
	 *
	 * @return expired password view
	 */
	@GetMapping("/passwordExpiredEmail")
	public String getExpiredPasswordPopupViaEmail(
			@RequestParam(value = "passwordExpireToken", required = true) final String passwordExpireToken,
			@RequestParam(value = "email", required = true) final String email) {
	
		return getExpiredPasswordPopupPageViaEmail(passwordExpireToken, email);
	}
	
	protected String getExpiredPasswordPopupPageViaEmail(final String passwordExpireToken, final String email)
	{
		final String METHOD_NAME = "getExpiredPasswordPopupPageViaEmail()";

		final String currentSite = cmsSiteService.getCurrentSite().getUid();
		final String siteName = currentSite.equalsIgnoreCase(LoginaddonConstants.MDD_SITE_ID) ? LoginaddonConstants.MDD
						: LoginaddonConstants.CONS;
		sessionService.setAttribute(LoginaddonConstants.SITE_NAME, siteName);
		if (siteName.equals(LoginaddonConstants.MDD))
		{
			return getView(LoginaddonControllerConstants.Views.Pages.Password.PasswordResetPage);
		}
		return null;
	}
	
	/**
	 * This method returns the view for the expired password light box after re-checking if the password is expired
	 *
	 * @return expired password view
	 *//*
	@RequestMapping(value = "/passwordExpired", method = RequestMethod.GET)
	public String getExpiredPasswordPopup(final Model model)
	{
		final String METHOD_NAME = "getExpiredPasswordPopup()";
		*//** Fool-proofing if the user's password is indeed expired **//*
		*//** SUCCESS **//*
		if (null != (sessionService.getAttribute(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM))
				&& (sessionService.getAttribute(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM).equals(userService
						.getCurrentUser().getUid())))
		{
			*//** Removing the UID from the session **//*
			sessionService.removeAttribute(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM);

			*//** Adding expired password flag in the model **//*
			model.addAttribute(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM, Boolean.TRUE);

			logDebugMessage(LOGIN, "getExpiredPasswordPopup()", "User must now reset the password.");
		}
		*//** FAILURE **//*
		else
		{
			*//** Removing the UID from the session **//*
			sessionService.removeAttribute(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM);
			try
			{
				*//** Adding 'Unauthorized User' Message attribute to the model **//*
				model.addAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRY_ERROR,
						messageFacade.getMessageTextForCode(LoginaddonConstants.Login.PASSWORD_EXPIRY_UNAUTHORIZED_KEY)"");
				logDebugMessage(LOGIN, "getExpiredPasswordPopup()", "User not authorized.");
			}
			catch (final Exception businessException)
			{
				logDebugMessage(LOGIN, "getExpiredPasswordPopup()",
						"Message for unauthorized access not found. Hence using model attribute key!");
				*//** In case message not found **//*
				model.addAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRY_ERROR,
						LoginaddonConstants.Login.PASSWORD_EXPIRY_ERROR);
				LOG.error("Message not found :: " + businessException.getMessage());
			}
		}
		final String currentSite = cmsSiteService.getCurrentSite().getUid();
		final String siteName = currentSite.equalsIgnoreCase(LoginaddonConstants.MDD_SITE_ID) ? LoginaddonConstants.MDD
						: LoginaddonConstants.CONS;
		sessionService.setAttribute(LoginaddonConstants.SITE_NAME, siteName);
		if (siteName.equals(LoginaddonConstants.MDD))
		{
			return getView(LoginaddonControllerConstants.Views.Pages.Password.PasswordResetExpiredPage);
		}
		return null;
	}
*/

	/**
	 * Utility method used for logging entry into / exit from any method.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
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
	 * Utility method used for logging custom messages.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param message
	 *           the message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
 }

	/**
	 * This method returns the view for When the user click on the link on Reset Url from the email
	 *
	 * @return expired password view
	 */
	@PostMapping("/ecoHeartBeat")
	@ResponseBody
	public String ecoHeartBeat() throws CMSItemNotFoundException {
		final String METHOD_NAME = "ecoHeartBeat()";
		logDebugMessage(LOGIN, METHOD_NAME, "invoked.");
		boolean  outMessage = true;
		logDebugMessage(LOGIN, METHOD_NAME, "completed.");
		return outMessage ? "Success" : "Failure";
	}
}
