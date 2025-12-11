/**
 * 
 */
package com.jnj.core.services.search.impl;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;
import java.util.Map;

import com.jnj.core.dao.search.CustomerProductCodeSearchDao;
import com.jnj.core.services.search.CustomerProductCodeSearchService;


/**
 * Service responsible for interacting with <code>CustomerProductCodeSearchDao</code>
 * 
 * @author akash.rawat
 * 
 */
public class DefaultCustomerProductCodeSearchService implements CustomerProductCodeSearchService
{
	private CustomerProductCodeSearchDao customerProductCodeSearchDao;

	/**
	 * Fetches the customer product codes using <code>CustomerProductCodeSearchService</code>.
	 * 
	 * @param productsResult
	 * @param customer
	 * 
	 * @return customerProductCodesMap
	 */
	@Override
	public Map<String, String> searchCustomerProductCodes(final List<ProductData> productsResult, final CustomerModel customer)
	{
		return customerProductCodeSearchDao.searchCustomerProductCodes(productsResult, customer);
	}

	/**
	 * @return the customerProductCodeSearchDao
	 */
	public CustomerProductCodeSearchDao getCustomerProductCodeSearchDao()
	{
		return customerProductCodeSearchDao;
	}

	/**
	 * @param customerProductCodeSearchDao
	 *           the customerProductCodeSearchDao to set
	 */
	public void setCustomerProductCodeSearchDao(final CustomerProductCodeSearchDao customerProductCodeSearchDao)
	{
		this.customerProductCodeSearchDao = customerProductCodeSearchDao;
	}


}
