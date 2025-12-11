package com.jnj.facades.process.email.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTApproveUserEmailProcessModel;
import com.jnj.core.model.JnjGTCreateUserEmailProcessModel;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.util.Config;

public class JnjGTCreateUserEmailContext  extends CustomerEmailContext
{
	private static final Logger LOG = Logger.getLogger(JnjGTCreateUserEmailContext.class);
	private static final String USER_EMAIL_ADDRESS = "userEmailAddress";
	
	private Map<String, String> createdUserEmailDetail = new HashMap<String, String>();
	
	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		CommonUtil
				.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME,
				"Setting the email addresses...", LOG);

		if (storeFrontCustomerProcessModel instanceof JnjGTCreateUserEmailProcessModel)
		{

			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME,
					"Process Model is an instance of JnjGTCreateUserEmailProcessModel", LOG);
			setCreatedUserEmailDetail(((JnjGTCreateUserEmailProcessModel) storeFrontCustomerProcessModel)
					.getJnjGTCreateUserEmailDetails());
			/** Setting the Create User Email data map in the context **/
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME,
					"Setting the Create User Email data map in the context...", LOG);
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME,
					" Create User Email data map has been set!", LOG);
			/** Setting the To Email address **/
			put(EMAIL,
					((JnjGTCreateUserEmailProcessModel) storeFrontCustomerProcessModel).getJnjGTCreateUserEmailDetails().get(
							USER_EMAIL_ADDRESS));
			/** Setting the From Email address **/
			put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS)); // TODO
			/** Setting the From User display name **/
			put(FROM_DISPLAY_NAME, "Johnson & Johnson"); // TODO
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME,
					"Email addresses have been set!", LOG);
		}
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	public Map<String, String> getCreatedUserEmailDetail() {
		return createdUserEmailDetail;
	}

	public void setCreatedUserEmailDetail(Map<String, String> createdUserEmailDetail) {
		this.createdUserEmailDetail = createdUserEmailDetail;
	}


}
