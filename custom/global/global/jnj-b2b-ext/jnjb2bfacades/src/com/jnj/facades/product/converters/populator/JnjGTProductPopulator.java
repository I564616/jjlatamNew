/**
 * 
 */
package com.jnj.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;


public class JnjGTProductPopulator extends ProductPopulator
{

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		super.populate(source, target);
		getProductBasicPopulator().populate(source, target);
		getVariantSelectedPopulator().populate(source, target);
		getProductPrimaryImagePopulator().populate(source, target);

	}
}
