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
import com.jnj.core.model.JnjGTCartOrderEmailProcessModel;


/**
 * @author himanshi.batra
 * 
 */
public class JnjGTCartOrderConfirmationEmailContext extends CustomerEmailContext
{

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Resource(name="jnjGTCustomerFacade")
	JnjGTCustomerFacade jnjGTCustomerFacade;

	protected Map<String, Object> cartOrderEmailMap = new HashMap<String, Object>();
	protected static final String FIRST_NAME = "firstName";
	protected static final String ORDER_STATUS = "orderStatus";

	protected static final String LAST_NAME = "lastName";

	/**
	 * @return the jnjCustomerFormMap
	 */
	public Map<String, Object> getcartOrderEmailMap()
	{
		return cartOrderEmailMap;
	}

	/**
	 * @param jnjCustomerFormMap
	 *           the jnjCustomerFormMap to set
	 */
	public void setCartOrderEmailMap(final Map<String, Object> cartOrderEmailMap)
	{
		this.cartOrderEmailMap = cartOrderEmailMap;
	}


	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		if (storeFrontCustomerProcessModel instanceof JnjGTCartOrderEmailProcessModel)
		{
			final JnjGTCartOrderEmailProcessModel jnjGTCartOrderEmailProcessModel = (JnjGTCartOrderEmailProcessModel) storeFrontCustomerProcessModel;

			cartOrderEmailMap.put(FIRST_NAME, jnjGTCartOrderEmailProcessModel.getFirstName());
			cartOrderEmailMap.put(ORDER_STATUS, jnjGTCartOrderEmailProcessModel.getOrderStatus());
			cartOrderEmailMap.put(LAST_NAME, jnjGTCartOrderEmailProcessModel.getOrderStatus());
			setCartOrderEmailMap(cartOrderEmailMap);
		}
		/** Setting the To Email address **/
		put(EMAIL, "komal.sehgal@accenture.com");
		/** Setting the From Email address **/
		put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS)); // TODO
		/** Setting the From User display name **/
		put(FROM_DISPLAY_NAME, "Johnson & Johnson"); // TODO

	}





}
