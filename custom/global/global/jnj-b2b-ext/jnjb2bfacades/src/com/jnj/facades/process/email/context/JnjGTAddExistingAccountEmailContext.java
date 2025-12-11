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

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnjConfigService;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.core.model.JnjGTAddExistingAccountEmailProcessModel;


/**
 * @author himanshi.batra
 * 
 */
public class JnjGTAddExistingAccountEmailContext extends CustomerEmailContext
{

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	private Map<String, Object> addAccountEmailMap = new HashMap<String, Object>();
	protected static final String ACCOUNT_NUMBERS = "accountnumbers";
	protected static final String FIRST_NAME = "firstName";

	/**
	 * @return the jnjCustomerFormMap
	 */
	public Map<String, Object> getaddAccountEmailMap()
	{
		return addAccountEmailMap;
	}

	/**
	 * @param jnjCustomerFormMap
	 *           the jnjCustomerFormMap to set
	 */
	public void setAddAccountEmailMap(final Map<String, Object> addAccountEmailMap)
	{
		this.addAccountEmailMap = addAccountEmailMap;
	}


	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		if (storeFrontCustomerProcessModel instanceof JnjGTAddExistingAccountEmailProcessModel)
		{
			final JnjGTAddExistingAccountEmailProcessModel jnjGTAddExistingAccountEmailProcessModel = (JnjGTAddExistingAccountEmailProcessModel) storeFrontCustomerProcessModel;
			addAccountEmailMap.put(FIRST_NAME, jnjGTAddExistingAccountEmailProcessModel.getFirstName());
			addAccountEmailMap.put(ACCOUNT_NUMBERS, jnjGTAddExistingAccountEmailProcessModel.getAccountNumbers());
			addAccountEmailMap.put(EMAIL, jnjGTAddExistingAccountEmailProcessModel.getEmail());
			setAddAccountEmailMap(addAccountEmailMap);
			if (Boolean.valueOf(jnjGTAddExistingAccountEmailProcessModel.getEmailFlag()))
			{
				put(EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
				put(DISPLAY_NAME, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
			}
			else
			{
				put(EMAIL, jnjGTAddExistingAccountEmailProcessModel.getEmail());
				put(DISPLAY_NAME, jnjGTAddExistingAccountEmailProcessModel.getFirstName());
			}
		}
		put(FROM_DISPLAY_NAME, "Johnson & Johnson");
		put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
	}




}
