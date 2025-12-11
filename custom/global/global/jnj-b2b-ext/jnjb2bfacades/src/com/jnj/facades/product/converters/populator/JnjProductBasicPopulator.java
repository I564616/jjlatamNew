/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product.converters.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import com.jnj.core.model.JnJProductModel;
import com.jnj.facades.data.JnjProductData;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjProductBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductBasicPopulator<SOURCE, TARGET>
{
	private CommerceCategoryService commerceCategoryService;



	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		super.populate(productModel, productData);
		if (productData instanceof JnjProductData)
		{
			populateJnjProductSpecification(productModel, productData);
		}
		super.populate(productModel, productData);
	}

	private void populateJnjProductSpecification(final SOURCE productModel, final TARGET productData)
	{
		final JnJProductModel source = (JnJProductModel) productModel;
		final JnjProductData target = (JnjProductData) productData;
		target.setSalesUnit((source.getUnit() != null) ? source.getUnit().getName() : null);
		target.setBaseUnit((source.getBaseUnitOfMeasure() != null) ? source.getBaseUnitOfMeasure().getName() : null);
		target.setNumeratorDUOM(source.getNumeratorDUOM());
		target.setNumeratorSUOM(source.getNumeratorSUOM());

		target.setDiscontinue(source.getDisContinue());
		target.setEan(source.getEan());
		target.setMedicalSpeciality(source.getMedicalSpecialty());
		target.setShipWeight(source.getShippingWeight());
		target.setStockingtype(source.getStockingType());
		target.setOriginCountry((source.getOriginCountry() != null) ? source.getOriginCountry().getName() : null);
		target.setDisplayPrice(Boolean.valueOf(isDisplayProductPrice(productModel)));
		target.setSector(source.getSector());
		final List<String> dataSheetNames = new ArrayList<String>();
		final List<MediaModel> dataSheets = (List<MediaModel>) source.getData_sheet();
		if (!dataSheets.isEmpty())
		{
			for (final MediaModel mediaModel : dataSheets)
			{
				dataSheetNames.add(mediaModel.getCode());
			}
			target.setDataSheets(dataSheetNames);
		}
	}

	/*
	 * private int getSaleItems(final JnJProductModel productModel) { try { final int numeratorDUOM =
	 * Integer.parseInt(productModel.getNumeratorDUOM()); final int numeratorSUOM =
	 * Integer.parseInt(productModel.getNumeratorSUOM()); return numeratorDUOM / numeratorSUOM; } catch (final
	 * NumberFormatException exception) { return 1; } }
	 */

	/**
	 * Evaluates if the product price has to be displayed or not depending on the level 1 category parameter.
	 * 
	 * @param product
	 * @return boolean
	 */
	private boolean isDisplayProductPrice(final ProductModel product)
	{
		for (final CategoryModel superCategory : product.getSupercategories())
		{
			if (superCategory.getShowProductPrice() != null && !superCategory.getShowProductPrice())
			{
				return false;
			}
		}

		return true;
	}


	public CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}


}
