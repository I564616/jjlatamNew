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
import com.jnj.core.model.JnjGTRejectUserEmailProcessModel;
import com.jnj.utils.CommonUtil;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTRejectUserEmailContext extends CustomerEmailContext
{
	private Map<String, String> rejectUserEmailDetail = new HashMap<String, String>();

	private static final Logger LOG = Logger.getLogger(JnjGTRejectUserEmailContext.class);

	private static final String USER_EMAIL_ADDRESS = "userEmailAddress";
	
	/* Added for Rejected User Email from Hotfix*/
	private static final String USER_FULL_NAME = "userFullName";
	

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME, "Setting the email addresses...",
				LOG);

		if (storeFrontCustomerProcessModel instanceof JnjGTRejectUserEmailProcessModel)
		{

			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME,
					"Process Model is an instance of JnjGTRejectUserEmailProcessModel", LOG);
			setRejectUserEmailDetail(((JnjGTRejectUserEmailProcessModel) storeFrontCustomerProcessModel)
					.getJnjGTRejectUserEmailDetails());
			/** Setting the Approved Email data map in the context **/
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME,
					"Setting the Approved Email data map in the context...", LOG);
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME,
					" Approved Email data map has been set!", LOG);
			/** Setting the To Email address **/
			
			/* Added for Rejected User Email from Hotfix*/
			put(USER_FULL_NAME,
					((JnjGTRejectUserEmailProcessModel) storeFrontCustomerProcessModel).getJnjGTRejectUserEmailDetails().get(
							USER_FULL_NAME));
			put(EMAIL,
					((JnjGTRejectUserEmailProcessModel) storeFrontCustomerProcessModel).getJnjGTRejectUserEmailDetails().get(
							USER_EMAIL_ADDRESS));
			/** Setting the From Email address **/
			put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS)); // TODO
			/** Setting the From User display name **/
			put(FROM_DISPLAY_NAME, "Johnson & Johnson"); // TODO
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME,
					"Email addresses have been set!", LOG);
		}
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	/**
	 * @return the rejectUserEmailDetail
	 */
	public Map<String, String> getRejectUserEmailDetail()
	{
		return rejectUserEmailDetail;
	}

	/**
	 * @param rejectUserEmailDetail
	 *           the rejectUserEmailDetail to set
	 */
	public void setRejectUserEmailDetail(final Map<String, String> rejectUserEmailDetail)
	{
		this.rejectUserEmailDetail = rejectUserEmailDetail;
	}


}
