/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.jnj.b2b.storefront.security;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.jnj.core.constants.Jnjb2bCoreConstants;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;

import com.jnj.core.model.JnJB2bCustomerModel;

import com.jnj.core.services.operations.JnjGTOperationsService;

import com.jnj.core.util.JnjGTCoreUtil;



/**
 * Handler for login authentication, see {@link SimpleUrlAuthenticationFailureHandler}.
 */
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{
	private static final String ATTEMPTS_EXCEEDED = "attemptsExceeded";
	private BruteForceAttackCounter bruteForceAttackCounter;
	@Autowired
	private UserService userService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private JnjGTOperationsService jnjGTOperationsService;

	private static final Logger LOG = Logger.getLogger(LoginAuthenticationFailureHandler.class);

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException
	{

		/** Fetching the user name entered **/
		final String userUid = request.getParameter("j_username");
		/** Check if user name is not empty **/
		if (StringUtils.isNotEmpty(userUid) && (userService.isUserExisting(StringUtils.lowerCase(userUid))))
		{
			try
			{
				final UserModel userModel = userService.getUserForUID(StringUtils.lowerCase(userUid));
				/** Check if login is not disabled **/
				if (!userModel.isLoginDisabled())
				{
					// Register brute attacks
					bruteForceAttackCounter.registerLoginFailure(userUid);
					this.logAuditData("login", "User Login Event. unsucessful login. Bad credentials.",
							JnjGTCoreUtil.getIpAddress(request), true, false, new Date(), userUid);

				}
				else
				{
					/** Fetch user groups **/
					final JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) userModel;
					final Set<PrincipalGroupModel> userGroups = jnJB2bCustomerModel.getGroups();
					if (!userGroups.isEmpty())
					{
						// If groups are not empty, then user is not new
						request.getSession().setAttribute(Jnjb2bCoreConstants.Login.NEW_USER_DISABLED, Boolean.FALSE);
						this.logAuditData("login", "User Login Event. unsucessful login as user is disabled. ",
								JnjGTCoreUtil.getIpAddress(request), true, false, new Date(), userUid);

					}
					else
					{
						// If groups are empty, then user is a new user
						request.getSession().setAttribute(Jnjb2bCoreConstants.Login.NEW_USER_DISABLED, Boolean.TRUE);
						this.logAuditData("login", "User Login Event. unsucessful login.No User group assigned",
								JnjGTCoreUtil.getIpAddress(request), true, false, new Date(), userUid);

					}
				}
				/** Check if attack threshold is reached **/
				if (getBruteForceAttackCounter().isAttack(userUid))
				{
					try
					{
						/** Disable Login **/
						userModel.setLoginDisabled(true);
						sessionService.setAttribute(ATTEMPTS_EXCEEDED, Boolean.TRUE);
						modelService.save(userModel);
						/** Brute Force Attack Counter Reset **/
						bruteForceAttackCounter.resetUserCounter(userModel.getUid());
					}
					catch (final UnknownIdentifierException unknownIdentifierException)
					{
						LOG.warn("Brute force attack attempt for non existing user name " + userUid);
					}
				}
			}
			catch (final UnknownIdentifierException unknownIdentifierException)
			{
				LOG.warn("Exception in Login Failure Handling process ::" + unknownIdentifierException.getMessage());
			}
		}
		else
		{
			/** User doesn't exist in the database **/
			LOG.warn("User with UID :: " + userUid + " doesn't exist!");
			this.logAuditData("login", "User Login Event.user does'nt exist.", JnjGTCoreUtil.getIpAddress(request), false, false,
					new Date(), null);

		}
		/** Fetch Failed Login Attempts **/
		final int userFailedLogins = bruteForceAttackCounter.getUserFailedLogins(userUid);
		/** Store the j_username in the session **/
		request.getSession().setAttribute("SPRING_SECURITY_LAST_USERNAME", userUid);
		/** Store the Failed Attempts in the session **/
		super.onAuthenticationFailure(request, response, exception);
	}


	protected BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	public void setBruteForceAttackCounter(final BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
	}

	private void logAuditData(final String systemOrProcess, final String descriptionOfEvent, final String ipAddress,
			final boolean isAuthorised, final boolean isSuccess, final Date timeOfEvent, final String userId)
	{

		jnjGTOperationsService.logAuditData(systemOrProcess, descriptionOfEvent, ipAddress, isAuthorised, isSuccess, timeOfEvent,
				userId);
	}



	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	private void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
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
	private void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
}
