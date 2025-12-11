/**
 *
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.catalog.constants.CatalogConstants.Config;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.core.model.JnjGTContactUsEmailProcessModel;
import com.jnj.utils.CommonUtil;


/**
 * @author balinder.singh
 *
 */
public class JnjGTContactUsEmailContext extends CustomerEmailContext
{

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	@Autowired
	protected CustomerFacade customerFacade;

	protected static final String ORDER_ID = "orderID";
	protected static final String NAME = "name";
	protected static final String PHONE_NUMBER = "phoneNumber";
	protected static final String ENQUIRY_DETAIL = "enquiryDetail";
	protected static final String EMAIL_SUBJECT = "emailSubject";
	protected static final String LOGO_URL = "logoURL";
	protected static final String USER_EMAIL_ID = "userEmailId";
	protected static final String CONTACT_US = "CONTACT US";
	protected static final String PRODUCT_ID = "productNumber";
	protected Map<String, Object> contactUsEmailMap = new HashMap<String, Object>();

	protected static final Logger LOG = Logger.getLogger(JnjGTContactUsEmailContext.class);

	/**
	 * @return the contactUsEmailMap
	 */
	public Map<String, Object> getContactUsEmailMap()
	{
		return contactUsEmailMap;
	}

	/**
	 * @param contactUsEmailMap
	 *           the contactUsEmailMap to set
	 */
	public void setContactUsEmailMap(final Map<String, Object> contactUsEmailMap)
	{
		this.contactUsEmailMap = contactUsEmailMap;
	}



	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Super init complete!", LOG);
		if (storeFrontCustomerProcessModel instanceof JnjGTContactUsEmailProcessModel)
		{
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Process model is JnjGTContactUsEmailProcessModel", LOG);
			final JnjGTContactUsEmailProcessModel jnjGTContactUsEmailProcessModel = (JnjGTContactUsEmailProcessModel) storeFrontCustomerProcessModel;
			/*if (StringUtils.equals(jnjGTContactUsEmailProcessModel.getSite().getUid(), JnjPCMCoreConstants.PCM_SITE_ID))
			{
				put(FROM_EMAIL, JnJCommonUtil.getValue(JnjPCMCoreConstants.PCM_CONTACT_US_FROM_EMAIL_ID));
				put(FROM_DISPLAY_NAME, JnJCommonUtil.getValue(JnjPCMCoreConstants.PCM_CONTACT_US_FROM_EMAIL_NAME));
				contactUsEmailMap.put(USER_EMAIL_ID, jnjGTContactUsEmailProcessModel.getFromEmailID());
			}
			else
			{*/
				CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "EPIC Site email detected.", LOG);
				put(FROM_EMAIL, jnjGTContactUsEmailProcessModel.getFromEmailID());
				put(FROM_DISPLAY_NAME, jnjGTContactUsEmailProcessModel.getUserName());
			//}
			put(EMAIL, jnjGTContactUsEmailProcessModel.getToEmailID());
			put(DISPLAY_NAME, jnjGTContactUsEmailProcessModel.getToEmailID());
			contactUsEmailMap.put(EMAIL, jnjGTContactUsEmailProcessModel.getToEmailID());
			contactUsEmailMap.put(DISPLAY_NAME, jnjGTContactUsEmailProcessModel.getToEmailID());
			contactUsEmailMap.put(NAME, jnjGTContactUsEmailProcessModel.getUserName());
			contactUsEmailMap.put(PHONE_NUMBER, jnjGTContactUsEmailProcessModel.getContactNumber());
			contactUsEmailMap.put(ENQUIRY_DETAIL, jnjGTContactUsEmailProcessModel.getDetailIssue());
			contactUsEmailMap.put(EMAIL_SUBJECT, jnjGTContactUsEmailProcessModel.getEmailSubject());
			if(StringUtils.isNotBlank(jnjGTContactUsEmailProcessModel.getEmailSubject()) && (jnjGTContactUsEmailProcessModel.getEmailSubject().contains("Contract")) || jnjGTContactUsEmailProcessModel.getEmailSubject().equalsIgnoreCase("5") ){
				contactUsEmailMap.remove(EMAIL);
				contactUsEmailMap.remove(DISPLAY_NAME);

				contactUsEmailMap.put(EMAIL, de.hybris.platform.util.Config.getString("mail.contract.issue", jnjGTContactUsEmailProcessModel.getToEmailID()));
				contactUsEmailMap.put(DISPLAY_NAME, de.hybris.platform.util.Config.getString("mail.contract.issue", jnjGTContactUsEmailProcessModel.getToEmailID()));
			
				
			}
			contactUsEmailMap.put(LOGO_URL, jnjGTContactUsEmailProcessModel.getLogoURL());
			if(jnjGTContactUsEmailProcessModel.getOrderID()!=null && jnjGTContactUsEmailProcessModel.getOrderID().trim().length()>0){
				contactUsEmailMap.put(ORDER_ID, jnjGTContactUsEmailProcessModel.getOrderID());
	
			}
			
			if(jnjGTContactUsEmailProcessModel.getProductID()!=null && jnjGTContactUsEmailProcessModel.getProductID().trim().length()>0){
				
				contactUsEmailMap.put(PRODUCT_ID, jnjGTContactUsEmailProcessModel.getProductID());
			}
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Contact us email map generated! :: " + contactUsEmailMap, LOG);
			//contactUsEmailMap.put(USER_EMAIL_ID, jnjGTContactUsEmailProcessModel.getCustomer().getContactEmail());
			put("contactUsEmailMap", contactUsEmailMap);
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Context populated!", LOG);
		}
		else
		{
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME,	"Process model is not JnjGTContactUsEmailProcessModel. No action taken.", LOG);
		}
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}
}
