/**
 *
 */
package com.jnj.facades.category;

import de.hybris.platform.commercefacades.product.data.CategoryData;

import java.util.List;

import com.jnj.la.core.enums.JnjTargetCatalogs;


/**
 * The Interface JnjCategoryFacade.
 * 
 * @author balinder.singh
 */
public interface JnjCategoryFacade
{

	/**
	 * Hide category with no products.
	 * 
	 * @param jnjTargetCatalogs
	 *           the jnj target catalogs
	 * @return true, if successful
	 */
	public boolean hideCategoryWithNoProducts(final JnjTargetCatalogs jnjTargetCatalogs);

}
