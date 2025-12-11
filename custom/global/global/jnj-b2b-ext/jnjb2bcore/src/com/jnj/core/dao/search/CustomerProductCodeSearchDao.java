/**
 * 
 */
package com.jnj.core.dao.search;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;
import java.util.Map;


/**
 * @author akash.rawat
 * 
 */
public interface CustomerProductCodeSearchDao
{
	public Map<String, String> searchCustomerProductCodes(List<ProductData> productsResult, CustomerModel customer);
}
