/**
 *
 */
package com.jnj.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductUrlPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionService;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnJGTProductService;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * @author akash.rawat
 * 
 */
public class JnjGTOrderEntryProductPopulator extends ProductUrlPopulator
{
	private Populator<ProductModel, ProductData> productPrimaryImagePopulator;
	private Populator<ProductModel, ProductData> productPricePopulator;

	/**
	 * Private instance of <code>JnJGTProductService</code>
	 */
	@Resource(name = "productService")
	private JnJGTProductService jnJGTProductService;

	/**
	 * Private instance of <code>SessionService</code>
	 */
	@Autowired
	private SessionService sessionService;

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		JnJProductModel JnJProductModel = null;
		
		final JnjGTProductData productData = (JnjGTProductData) target;
		super.populate(source, target);
		if (source instanceof JnjGTVariantProductModel)
		{
			final JnjGTVariantProductModel variant = (JnjGTVariantProductModel) source;
			this.setBasicValuesForVariant(variant, productData);
			getProductPricePopulator().populate(variant, target);
		}
		else
		{
			JnJProductModel = (JnJProductModel) source;
			JnJProductModel baseMaterialProduct = JnJProductModel.getMaterialBaseProduct();
			if (baseMaterialProduct == null)
			{
				baseMaterialProduct = JnJProductModel;
			}
			setBasicValuesForProduct(baseMaterialProduct, productData);

			productData.setName(jnJGTProductService.getProductName(JnJProductModel));

		}

		addEpicDetails(JnJProductModel, productData);
		getProductPrimaryImagePopulator().populate(source, target);

	}

	private void addEpicDetails(final JnJProductModel productModel, final JnjGTProductData target)
	{
		target.setExpiryDate(productModel.getProductExpiryDate());
		target.setGtin(productModel.getEan());
		target.setUpc(productModel.getUpcCode());
		target.setIsProdViewable(jnJGTProductService.isProductBrowsable(productModel));
		target.setHazmatCode(productModel.getHazmatCode());
	}

	/**
	 * To set basic values for Product
	 * 
	 * @param product
	 * @param productData
	 */
	private void setBasicValuesForProduct(final ProductModel product, final JnjGTProductData productData)
	{
		productData.setDescription(product.getDescription());
		productData.setSummary(product.getSummary());
		productData.setBaseMaterialNumber(product.getCode());
	}

	/**
	 * To set basic values for variant
	 * 
	 * @param variant
	 * @param productData
	 */
	private void setBasicValuesForVariant(final JnjGTVariantProductModel variant, final JnjGTProductData productData)
	{
		final ProductModel product = variant.getBaseProduct();
		productData.setCode(variant.getCode());
		productData.setDescription(product.getDescription());
		productData.setSummary(product.getSummary());
		productData.setName(product.getName());
		productData.setBaseMaterialNumber(product.getCode());
		productData.setGtin(variant.getEan());
		productData.setUpc(variant.getUpc());
	}

	protected Populator<ProductModel, ProductData> getProductPrimaryImagePopulator()
	{
		return productPrimaryImagePopulator;
	}

	public void setProductPrimaryImagePopulator(final Populator<ProductModel, ProductData> productPrimaryImagePopulator)
	{
		this.productPrimaryImagePopulator = productPrimaryImagePopulator;
	}

	protected Populator<ProductModel, ProductData> getProductPricePopulator()
	{
		return productPricePopulator;
	}

	public void setProductPricePopulator(final Populator<ProductModel, ProductData> productPricePopulator)
	{
		this.productPricePopulator = productPricePopulator;
	}

}
