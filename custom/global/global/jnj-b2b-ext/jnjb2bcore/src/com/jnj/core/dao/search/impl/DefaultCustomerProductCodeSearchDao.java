/**
 * 
 */
package com.jnj.core.dao.search.impl;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jnj.core.dao.search.CustomerProductCodeSearchDao;


/**
 * DAO responsible for fetching the Customer based Product Codes (Cross Reference Translation)
 * 
 * @author akash.rawat
 * 
 */
public class DefaultCustomerProductCodeSearchDao implements CustomerProductCodeSearchDao
{

	//protected final String query = null; 

	@Override
	public Map<String, String> searchCustomerProductCodes(final List<ProductData> productsResult, final CustomerModel customer)
	{
		ServicesUtil.validateParameterNotNull(productsResult, "productsResult must not be null");
		ServicesUtil.validateParameterNotNull(customer, "customer must not be null");

		//		final Map<String, Object> params = new HashMap<String, Object>();
		//		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(query, params);
		//		final SearchResult<EmailPageModel> result = getFlexibleSearchService().search(fsq);

		return populateMokData(productsResult);
	}


	/**
	 * This is a mock data populator for testing purpose until Load Translation design gets finalized.
	 * 
	 * @param productsResult
	 * @return
	 */
	protected Map<String, String> populateMokData(final List<ProductData> productsResult)
	{
		final Map<String, String> customerProductCodes = new HashMap<String, String>();

		for (int i = 0; i < productsResult.size(); i++)
		{
			customerProductCodes.put(productsResult.get(i).getCode(), "000100" + i);
		}

		return customerProductCodes;
	}
}
