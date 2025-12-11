/**
 * 
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.core.model.JnjGTUserDisableLoginEmailProcessModel;
import org.apache.log4j.Logger;

/**
 * @author labanya.saha
 * 
 */
public class JnjGTDisableUserLoginEmailContext extends CustomerEmailContext
{
	private static final Logger LOG = Logger.getLogger(JnjGTDisableUserLoginEmailContext.class);
	protected static final String FIRST_NAME = "NAME";
	protected static final String DAYS_BEFORE_DISABLE = "daysBeforeDisable";
	@Autowired
	JnjConfigService jnjConfigService;

	@Resource(name="GTCustomerFacade")
	JnjGTCustomerFacade jnjGTCustomerFacade;

	private String emailSecureUrl;

	public String getEmailSecureUrl() {
		return emailSecureUrl;
	}

	public void setEmailSecureUrl(String emailSecureUrl) {
		this.emailSecureUrl = emailSecureUrl;
	}
	
	Map<String, Object> disableUserLoginEmailMap = new HashMap<String, Object>();


	/**
	 * @return the disableUserLoginEmailMap
	 */
	public Map<String, Object> getDisableUserLoginEmailMap()
	{
		return disableUserLoginEmailMap;
	}

	public void setDisableUserLoginEmailMap(final Map<String, Object> disableUserLoginEmailMap)
	{
		this.disableUserLoginEmailMap = disableUserLoginEmailMap;
	}

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		if (storeFrontCustomerProcessModel instanceof JnjGTUserDisableLoginEmailProcessModel)
		{
			final JnjGTUserDisableLoginEmailProcessModel jnjGTUserDisableLoginEmailProcessModel = (JnjGTUserDisableLoginEmailProcessModel) storeFrontCustomerProcessModel;

			disableUserLoginEmailMap.put(EMAIL, jnjGTUserDisableLoginEmailProcessModel.getCustomer().getUid());
			disableUserLoginEmailMap.put(FIRST_NAME, jnjGTUserDisableLoginEmailProcessModel.getCustomer().getName());
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,
					Integer.parseInt(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Login.DAYS_BEFORE_SEND_WARNING_MAIL)));
			disableUserLoginEmailMap.put(Jnjb2bCoreConstants.Login.DATE, cal.getTime().toString());
			setDisableUserLoginEmailMap(disableUserLoginEmailMap);
			put(EMAIL, jnjGTUserDisableLoginEmailProcessModel.getCustomer().getUid());
			put(DISPLAY_NAME, jnjGTUserDisableLoginEmailProcessModel.getCustomer().getName());
			put(DAYS_BEFORE_DISABLE,jnjGTUserDisableLoginEmailProcessModel.getDaysBeforeDisable());

			//String langCode = jnjGTUserDisableLoginEmailProcessModel.getLanguage().getIsocode();
			//setEmailSecureUrl(JnJCommonUtil.getSiteUrlByLanguage(langCode));
			setEmailSecureUrl(JnJCommonUtil.getSecureSiteUrl());
			LOG.debug("setEmailSecureUrl......"+getEmailSecureUrl());
			
		}
		put(FROM_EMAIL, "customerService@jnj.com");
		put(FROM_DISPLAY_NAME, "Johnson &Johnson");
	}


}
