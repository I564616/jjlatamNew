/**
 * 
 */
package com.jnj.facades.search.impl;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;
import java.util.Map;

import com.jnj.core.services.search.CustomerProductCodeSearchService;
import com.jnj.facades.search.CustomerProductCodeSearchFacade;


/**
 * Facade responsible for interacting with <code>CustomerProductCodeSearchService</code>
 * 
 * @author akash.rawat
 * 
 */
public class DefaultCustomerProductCodeSearchFacade implements CustomerProductCodeSearchFacade
{
	private UserService userService;
	private CustomerProductCodeSearchService customerProductCodeSearchService;

	/**
	 * Fetches the customer product codes using <code>CustomerProductCodeSearchService</code>.
	 */
	@Override
	public Map<String, String> getCustomerProductCodes(final List<ProductData> productsResult)
	{
		final CustomerModel customerModel = getUserService().getUserForUID(userService.getCurrentUser().getUid().toLowerCase(),
				CustomerModel.class);
		return customerProductCodeSearchService.searchCustomerProductCodes(productsResult, customerModel);
	}

	protected UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the customerProductCodeSearchService
	 */
	public CustomerProductCodeSearchService getCustomerProductCodeSearchService()
	{
		return customerProductCodeSearchService;
	}

	/**
	 * @param customerProductCodeSearchService
	 *           the customerProductCodeSearchService to set
	 */
	public void setCustomerProductCodeSearchService(final CustomerProductCodeSearchService customerProductCodeSearchService)
	{
		this.customerProductCodeSearchService = customerProductCodeSearchService;
	}



}
