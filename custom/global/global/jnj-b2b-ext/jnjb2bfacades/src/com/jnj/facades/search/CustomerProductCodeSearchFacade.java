/**
 * 
 */
package com.jnj.facades.search;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;
import java.util.Map;


/**
 * @author akash.rawat
 * 
 */
public interface CustomerProductCodeSearchFacade
{
	public Map<String, String> getCustomerProductCodes(List<ProductData> productsResult);
}
