/**
 * 
 */
package com.jnj.core.services.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.jnj.core.services.JnJGTMDDProductExportService;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.model.JnJGTProductKitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * This class is used to get MDD product info to be used in catalog export
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnJGTMDDProductExportService implements JnJGTMDDProductExportService
{

	@Resource(name = "productService")
	JnJGTProductService jnJGTProductService;

	public JnJGTProductService getJnJGTProductService() {
		return jnJGTProductService;
	}


	@Override
	public String getName(final ProductModel productModel)
	{
		return productModel.getName();
	}


	@Override
	public String getFranchiseName(final ProductModel productModel)
	{
		String franchise = null;

		for (final CategoryModel levelTwoCategory : productModel.getSupercategories())
		{
			for (final CategoryModel levelOneCategory : levelTwoCategory.getSupercategories())
			{
				franchise = levelOneCategory.getName();
				break;
			}
			break;
		}
		return franchise;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnJGTMDDProductExportService#getDivisionName(de.hybris.platform.core.model.product.
	 * ProductModel)
	 */
	@Override
	public String getDivisionName(final ProductModel productModel)
	{
		String division = null;
		for (final CategoryModel levelTwoCategory : productModel.getSupercategories())
		{
			division = levelTwoCategory.getName();
			break;
		}

		// YTODO Auto-generated method stub
		return division;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnJGTMDDProductExportService#getProductCode(de.hybris.platform.core.model.product.
	 * ProductModel)
	 */
	@Override
	public String getProductCode(final ProductModel productModel)
	{
		return productModel.getCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.services.JnJGTMDDProductExportService#getProductDescription(de.hybris.platform.core.model.product
	 * .ProductModel)
	 */
	@Override
	public String getProductDescription(final ProductModel productModel)
	{
		return productModel.getDescription();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.services.JnJGTMDDProductExportService#getKitDetails(de.hybris.platform.core.model.product.ProductModel
	 * )
	 */
	@Override
	public Map<String, List<String>> getKitDetails(final ProductModel productModel)
	{
		final Map<String, List<String>> kitMap = new HashMap<String, List<String>>();

		if (productModel instanceof JnJProductModel)
		{
			final Collection<JnJGTProductKitModel> productKitModels = ((JnJProductModel) productModel).getProductKits();
			if (!CollectionUtils.isEmpty(productKitModels))
			{
				final List<String> kitComponentDescriptions = new ArrayList<>();
				final List<String> kitComponentNames = new ArrayList<>();
				final List<String> kitComponentUnits = new ArrayList<>();

				String componentCode = null;
				for (final JnJGTProductKitModel productKitModel : productKitModels)
				{
					componentCode = productKitModel.getComponentCode();
					if (StringUtils.isNotEmpty(componentCode))
					{

						final ProductModel deliveryVariant = jnJGTProductService.getMDDDeliveryVariantByProdCode(componentCode);
						if (null != deliveryVariant)
						{
							final ProductModel product = ((VariantProductModel) deliveryVariant).getBaseProduct();

							kitComponentUnits.add((deliveryVariant.getUnit() == null) ? null : deliveryVariant.getUnit().getName());
							kitComponentDescriptions.add(product.getDescription());
							kitComponentNames.add(product.getName());
						}

					}
				}
				kitMap.put("kitComponentDesc", kitComponentDescriptions);
				kitMap.put("kitComponentNames", kitComponentNames);
				kitMap.put("kitComponentUnits", kitComponentUnits);
			}
		}
		return kitMap;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnJGTMDDProductExportService#deliveryUOM(com.jnj.core.model.JnjGTVariantProductModel,
	 * com.jnj.core.model.JnjGTVariantProductModel)
	 */
	@Override
	public void setDeliveryUOM(final JnjGTVariantProductModel deliveryVariant, final JnjGTVariantProductModel salesVariant,
			final StringBuilder stringBuilder)
	{
		final UnitModel salesVariantUom = salesVariant.getUnit();

		final int quantity = (deliveryVariant.getPackagingLevelQty() != null && salesVariant.getPackagingLevelQty() != null) ? deliveryVariant
				.getPackagingLevelQty().intValue() / salesVariant.getPackagingLevelQty().intValue()
				: 0;

		final String quantityText = (quantity == 0) ? "" : Integer.toString(quantity);

		final int numeratorFactor = (salesVariant.getNumerator() != null) ? (quantity * salesVariant.getNumerator().intValue()) : 0;
		final String numeratorFactorText = (numeratorFactor == 0) ? "" : Integer.toString(numeratorFactor);

		stringBuilder.append(quantityText);
		if (salesVariantUom.getCode() != null)
		{
			stringBuilder.append(salesVariantUom.getCode());
		}
		stringBuilder.append(" (");
		stringBuilder.append(numeratorFactorText + "");
		if (salesVariant.getLwrPackagingLvlUom() != null)
		{
			stringBuilder.append(salesVariant.getLwrPackagingLvlUom().getName());
		}
		stringBuilder.append(")");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnJGTMDDProductExportService#eachesUOM(com.jnj.core.model.JnjGTVariantProductModel,
	 * com.jnj.core.model.JnjGTVariantProductModel)
	 */
	@Override
	public void setUOMOfEaches(final JnjGTVariantProductModel deliveryVariant, final JnjGTVariantProductModel salesVariant,
			final StringBuilder stringBuilder)
	{
		final UnitModel salesVariantUom = salesVariant.getUnit();

		final int eachesNumeratorFactor = (salesVariant.getNumerator() != null) ? (salesVariant.getNumerator().intValue()) : 0;
		final String eachNumeratorText = (eachesNumeratorFactor == 0) ? "" : Integer.toString(eachesNumeratorFactor);
		if (salesVariantUom.getCode() != null)
		{
			stringBuilder.append(salesVariantUom.getCode());
		}
		stringBuilder.append(" (");
		stringBuilder.append(eachNumeratorText);
		if (salesVariant.getLwrPackagingLvlUom() != null)
		{
			stringBuilder.append(salesVariant.getLwrPackagingLvlUom().getCode());
		}
		stringBuilder.append(")");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.services.JnJGTMDDProductExportService#getGtins(de.hybris.platform.core.model.product.ProductModel)
	 */
	@Override
	public Map<String, String> getGtins(final ProductModel productModel)
	{
		// YTODO Auto-generated method stub
		return null;
	}

}
