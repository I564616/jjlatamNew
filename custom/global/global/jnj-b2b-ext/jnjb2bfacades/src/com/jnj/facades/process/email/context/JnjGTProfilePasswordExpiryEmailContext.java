/**
 *
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnjConfigService;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants.Login;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.core.model.JnjGTProfilePasswordExpireEmailProcessModel;
//import com.jnj.pcm.constants.JnjPCMCoreConstants.Login;
import com.jnj.utils.CommonUtil;


/**
 * @author himanshi.batra
 *
 */
public class JnjGTProfilePasswordExpiryEmailContext extends CustomerEmailContext
{
	protected static final String PCM_FROM_DISPLAY_NAME = "Product360";
	protected static final Logger LOG = Logger.getLogger(JnjGTProfilePasswordExpiryEmailContext.class);
	protected Map<String, String> profilePasswordEmailDetails = new HashMap<String, String>();
	protected final String BUISNESS_EMAIL = "buisnessemail";
	protected static final String FOR_PCM = "isFromPCM";

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Resource(name="GTCustomerFacade")
	JnjGTCustomerFacade jnjGTCustomerFacade;

	/**
	 * @param jnjCustomerFormMap
	 *           the jnjCustomerFormMap to set
	 */

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				LOG);
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME,
				"Setting the email addresses...", LOG);
		JnjGTProfilePasswordExpireEmailProcessModel jnjGTProfilePasswordExpireEmailProcessModel = null;
		if (storeFrontCustomerProcessModel instanceof JnjGTProfilePasswordExpireEmailProcessModel)
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME,
					"Process Model is an instance of JnjGTProfilePasswordExpireEmailProcessModel", LOG);
			/** Type-casting the storeFrontCustomerProcessModel to jnjGTRegistrationConfirmationProcessModel **/
			jnjGTProfilePasswordExpireEmailProcessModel = (JnjGTProfilePasswordExpireEmailProcessModel) storeFrontCustomerProcessModel;

			if (null != jnjGTProfilePasswordExpireEmailProcessModel.getCustomer())
			{
				put("userFullName", jnjGTProfilePasswordExpireEmailProcessModel.getCustomer().getName());
			}
			/** Setting the Password Expire Email data map in the context **/
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME,
					"Setting the Password Expire Email data map in the context...", LOG);

			setProfilePasswordEmailDetails(jnjGTProfilePasswordExpireEmailProcessModel.getJnjGTPasswordExpireEmailDetails());

			/** Setting the To Email address **/
			put(EMAIL, jnjGTProfilePasswordExpireEmailProcessModel.getJnjGTPasswordExpireEmailDetails().get(BUISNESS_EMAIL));
			/** Setting the From Email address **/
			if (Boolean.valueOf(jnjGTProfilePasswordExpireEmailProcessModel.getJnjGTPasswordExpireEmailDetails().get(FOR_PCM))
					.booleanValue())
			{
				//put(FROM_EMAIL, Config.getParameter(Login.PCM_ACCOUNT_ACTIVATION_EMAIL_ID_KEY));
				put(FROM_DISPLAY_NAME, PCM_FROM_DISPLAY_NAME);
			}
			else
			{
				put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
				put(FROM_DISPLAY_NAME, "Johnson & Johnson");

			}
			/** Setting the From User display name **/
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME,
					"Email addresses have been set!", LOG);
		}
		CommonUtil
				.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	/**
	 * @return the profilePasswordEmailDetails
	 */
	public Map<String, String> getProfilePasswordEmailDetails()
	{
		return profilePasswordEmailDetails;
	}

	/**
	 * @param profilePasswordEmailDetails
	 *           the profilePasswordEmailDetails to set
	 */
	public void setProfilePasswordEmailDetails(final Map<String, String> profilePasswordEmailDetails)
	{
		this.profilePasswordEmailDetails = profilePasswordEmailDetails;
	}



}
