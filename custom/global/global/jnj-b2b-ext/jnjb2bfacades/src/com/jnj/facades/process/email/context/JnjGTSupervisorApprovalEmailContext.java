/**
 *
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTSupervisorApprovalEmailProcessModel;


/**
 * @author ujjwal.negi
 *
 */
public class JnjGTSupervisorApprovalEmailContext extends CustomerEmailContext
{
	/**
	 *
	 */
	protected static final String SUPERVISOR_EMAIL = "supervisorEmail";

	protected static final Logger LOG = Logger.getLogger(JnjGTSupervisorApprovalEmailContext.class);

	protected final String USER_EMAIL_ADDRESS = "userEmailAddress";

	protected final String USER_FIRST_NAME = "userFirstName";

	protected final String USER_LAST_NAME = "userLastName";

	protected Map<String, String> registrationDataMap = new HashMap<String, String>();

	protected static final String USER_FROM_SUPERVISOR_APPROVAL_EMAIL_KEY = "user.from.supervisor.approval.email";

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.jnj.facades.process.email.context.CustomerEmailContext#init(de.hybris.platform.commerceservices.model.process
	 * .StoreFrontCustomerProcessModel, de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel)
	 */
	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Setting the email addresses...");
		JnjGTSupervisorApprovalEmailProcessModel jnjGTSupervisorApprovalProcessModel = null;
		if (storeFrontCustomerProcessModel instanceof JnjGTSupervisorApprovalEmailProcessModel)
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"Process Model is an instance of jnjGTSuccessfulRegistrationProcessModel");

			/** Type-casting the storeFrontCustomerProcessModel to JnjGTSuccessfulRegistrationProcessModel **/
			jnjGTSupervisorApprovalProcessModel = (JnjGTSupervisorApprovalEmailProcessModel) storeFrontCustomerProcessModel;

			/** Setting the Registration data map in the context **/
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"Setting the Registration data map in the context...");
			setRegistrationDataMap(jnjGTSupervisorApprovalProcessModel.getJnjGTRegistrationDetails());
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Registration data map has been set!");

			/** Setting the From Email Address **/
			put(FROM_EMAIL, Config.getParameter(USER_FROM_SUPERVISOR_APPROVAL_EMAIL_KEY));

			/** Setting the From User display name **/
			put(FROM_DISPLAY_NAME, Config.getParameter(USER_FROM_SUPERVISOR_APPROVAL_EMAIL_KEY));
		}
		/** Setting the To Email address **/
		switch (registrationDataMap.get("division"))
		{
			case "DS":
				put(EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_DEPUY_SPINE_EMAIL_ADDRESS));
				put(DISPLAY_NAME, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_DEPUY_SPINE_EMAIL_ADDRESS));

				break;
			case "DM":
				put(EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_DEPUY_MITEK_EMAIL_ADDRESS));
				put(DISPLAY_NAME, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_DEPUY_MITEK_EMAIL_ADDRESS));

				break;
			default:
				put(EMAIL, jnjGTSupervisorApprovalProcessModel.getJnjGTRegistrationDetails().get(SUPERVISOR_EMAIL));
				put(DISPLAY_NAME, jnjGTSupervisorApprovalProcessModel.getJnjGTRegistrationDetails().get(SUPERVISOR_EMAIL));

				break;
		}

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Email addresses have been set!");
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * @return the registrationDataMap
	 */
	public Map<String, String> getRegistrationDataMap()
	{
		return registrationDataMap;
	}

	/**
	 * @param registrationDataMap
	 *           the registrationDataMap to set
	 */
	public void setRegistrationDataMap(final Map<String, String> registrationDataMap)
	{
		this.registrationDataMap = registrationDataMap;
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
}
