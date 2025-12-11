/**
 *
 */
package com.jnj.b2b.loginaddon.security;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.security.CoreAuthenticationProvider;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnJCommonUtil;



/**
 * @author rbalasu4
 * 
 */
public class JnjAcceleratorAuthenticationProvider extends CoreAuthenticationProvider
{

	protected UserService userService;
	protected ModelService modelService;
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	@Autowired
	private SessionService sessionService;
	
	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	protected static final Logger LOGGER = Logger
			.getLogger(JnjAcceleratorAuthenticationProvider.class);

	@Override
	protected void additionalAuthenticationChecks(final UserDetails details, final AbstractAuthenticationToken authentication)
			throws AuthenticationException
	{
		

		final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		final UserModel userModel = getUserService().getUserForUID(StringUtils.lowerCase(username));
		final B2BCustomerModel jnjb2bCustomer = (B2BCustomerModel) userModel;
		/** Checking if the user has Dummy unit Associated With It **/
		/*
		 * if (jnjGTCustomerFacade.userWithDummyUnit(jnjb2bCustomer)) { throw new
		 * InsufficientAuthenticationException(Jnjnab2bcoreConstants.Login.ACCOUNT_IN_PROCESS); }
		 */
		
		/*Checking for password expiry ----- START AAOL-4915*/
		if (userModel instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userModel;
			if (checkPasswordExpiry(currentUser))
			{
				sessionService.setAttribute("passwordExpiredForUser", Boolean.TRUE);
				LOGGER.info("Password expired " + currentUser.getUid());
				String NoAccess = null;
				try {
				NoAccess = "passwordExpiredForThisUser";
				LOGGER.error("Password Expired :: "+ NoAccess);
				}
				catch (final Exception exp)
				{
					LOGGER.error("Password Expired ::");
				}
				throw new InsufficientAuthenticationException(NoAccess);
			}
		}
		//End
		/** Check if login is not disabled **/
		if (userModel.isLoginDisabled())
		{
			/** Fetch user groups **/
			final Set<PrincipalGroupModel> userGroups = jnjb2bCustomer.getGroups();
			if (!userGroups.isEmpty())
			{
				// If groups are not empty, then user is not new
				throw new DisabledException(LoginaddonConstants.Login.NEW_USER_DISABLED);
			}
			else
			{
				throw new DisabledException(LoginaddonConstants.Login.NEW_USER_DISABLED);
			}
		}

		super.additionalAuthenticationChecks(details, authentication);
	}
	
	/**AAOL-4915
	 * This method checks if the user's password has expired or not. If expiry is detected it will return true.
	 * 
	 * @param jnJNAB2bCustomerModel
	 * @author surabhi
	 * @return true/false
	 */
	protected boolean checkPasswordExpiry(final JnJB2bCustomerModel jnJB2bCustomerModel)
	{
		final String METHOD_NAME = "checkPasswordExpiry";

		boolean expiry = false;
		/** Fetching last password change date **/
		final Date passwordChangeDate = jnJB2bCustomerModel.getPasswordChangeDate();

		//if flag - forceFul expire is true then return true for  User Management Password Expire Functionality
		if (jnJB2bCustomerModel.getForcefulExpired())
		{
			return true;
		}

		/** Checking if last password change date retrieved is not null **/
		if (null != passwordChangeDate)
		{
			/** Specifying date format - dd/MM/yyyy **/
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

			/** Fetching current date **/
			final Date currentDate = new Date();

			/** Get calendar instance for Date of Expiry **/
			final Calendar dateOfExpiry = Calendar.getInstance();

			/** Setting current date in the dateOfExpiry instance **/
			dateOfExpiry.setTime(currentDate);

			/** Subtracting the number of days in which password expires from the current date **/
			dateOfExpiry.add(Calendar.DATE, (-1 * Config.getInt(LoginaddonConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_KEY,90)));

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
				LOGGER.info(LoginaddonConstants.Logging.LOGIN + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "User's password is expired!");
			}
			else
			{
				/** NOT EXPIRED **/
				LOGGER.info(LoginaddonConstants.Logging.LOGIN + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "User's password has not yet expired. User fit for login");
			}
		}
		return expiry;
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

}
