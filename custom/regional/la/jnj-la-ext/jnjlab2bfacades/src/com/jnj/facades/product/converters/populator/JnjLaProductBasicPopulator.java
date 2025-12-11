/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product.converters.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.core.services.JnJProductService;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.la.core.services.JnjLoadTranslationService;


/**
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjLaProductBasicPopulator<SOURCE extends ProductModel, TARGET extends JnjGTProductData>
		extends JnjProductBasicPopulator<SOURCE, TARGET>
{
	private CommerceCategoryService commerceCategoryService;

	@Autowired
	protected JnjLoadTranslationService jnjLoadTranslationService;

	@Autowired
	protected JnJProductService jnjProductService;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		super.populate(productModel, productData);
		if (productData instanceof JnjLaProductData)
		{
			populateJnjProductSpecification(productModel, productData);
		}
		super.populate(productModel, productData);
	}

	private void populateJnjProductSpecification(final SOURCE productModel, final TARGET productData)
	{
		final JnJProductModel source = (JnJProductModel) productModel;
		final JnjLaProductData target = (JnjLaProductData) productData;

		final DecimalFormat decimalFormat = new DecimalFormat("0.0");
		final JnjUomDTO custDelUOM = jnjLoadTranslationService.getCustDelUomMapping((JnJProductModel) productModel);

		if (null != custDelUOM)
		{
			target.setDeliveryUnit(custDelUOM.getUnitDimension());
			target.setQuantity(Integer.valueOf(custDelUOM.getSalesUnitsCount()));
		}
		else
		{
			target.setDeliveryUnit((source.getDeliveryUnitOfMeasure() != null) ? source.getUnit().getName() : null);
			Integer numeratorD = null;
			Integer numeratorS = null;
			Integer quantity = null;
			if (null != source.getNumeratorDUOM())
			{
				numeratorD = Integer.valueOf(source.getNumeratorDUOM());
			}
			if (null != source.getNumeratorSUOM())
			{
				numeratorS = Integer.valueOf(source.getNumeratorSUOM());
			}
			if (null != numeratorD && null != numeratorS)
			{
				quantity = Integer.valueOf(numeratorD.intValue() / numeratorS.intValue());
			}
			if (null != quantity)
			{
				target.setQuantity(quantity);
			}
		}
		target.setSalesUnit((source.getUnit() != null) ? source.getUnit().getName() : null);
		target.setSalesUnitCode((source.getUnit() != null) ? source.getUnit().getCode() : null);
		target.setDeliveryUnitCode(
				(source.getDeliveryUnitOfMeasure() != null) ? source.getDeliveryUnitOfMeasure().getCode() : null);
		target.setBaseUnit((source.getBaseUnitOfMeasure() != null) ? source.getBaseUnitOfMeasure().getName() : null);
		target.setBaseUnitCode((source.getBaseUnitOfMeasure() != null) ? source.getBaseUnitOfMeasure().getCode() : null);
		target.setNumeratorDUOM(source.getNumeratorDUOM());
		target.setNumeratorSUOM(source.getNumeratorSUOM());
		target.setDiscontinue(source.getDisContinue());

		if (StringUtils.isNotEmpty(source.getNumeratorDUOM()) && StringUtils.isNotEmpty(source.getNumeratorSUOM()))
		{
			final Integer multiplicity = Integer
					.valueOf(Integer.parseInt(source.getNumeratorDUOM()) / Integer.parseInt(source.getNumeratorSUOM()));
			target.setMultiplicity(multiplicity);
		}
		target.setEan(source.getEan());
		target.setMedicalSpeciality(source.getMedicalSpecialty());
		target.setStockingtype(source.getStockingType());

		for (final JnjUomConversionModel jnJUomConversionModel : source.getUomDetails())
		{
			final String unit = jnJUomConversionModel.getUOM().getCode();
			if (null != source.getDeliveryUnitOfMeasure())
			{
				if (unit.equalsIgnoreCase(source.getDeliveryUnitOfMeasure().getCode()))
				{
					if (null != jnJUomConversionModel.getHeight()
							&& Double.compare(jnJUomConversionModel.getHeight().doubleValue(), 0) != 0)
					{
						target.setHeight(decimalFormat.format(jnJUomConversionModel.getHeight()));
					}
					if (null != jnJUomConversionModel.getLength()
							&& Double.compare(jnJUomConversionModel.getLength().doubleValue(), 0) != 0)
					{
						target.setLength(decimalFormat.format(jnJUomConversionModel.getLength()));
					}
					if (null != jnJUomConversionModel.getWidth()
							&& Double.compare(jnJUomConversionModel.getWidth().doubleValue(), 0) != 0)
					{
						target.setWidth(decimalFormat.format(jnJUomConversionModel.getWidth()));
					}
					target.setShipWeight(jnJUomConversionModel.getShippingWeight());
					target.setShippingUnit(jnJUomConversionModel.getShippingUnit());
					if (null != jnJUomConversionModel.getVolumeUnit())
					{
						target.setVolumeUnit(jnJUomConversionModel.getVolumeUnit().getCode());
					}
				}
			}
		}
		target.setDisplayPrice(Boolean.valueOf(isDisplayProductPrice(productModel)));
		target.setOriginCountry((source.getOriginCountry() != null) ? source.getOriginCountry().getName() : null);
		target.setSector(source.getSector());
		final Map<String, String> dataSheetNames = new HashMap<>();
		final List<MediaModel> dataSheets = (List<MediaModel>) source.getData_sheet();
		if (!dataSheets.isEmpty())
		{
			for (final MediaModel mediaModel : dataSheets)
			{
				dataSheetNames.put(mediaModel.getCode(), mediaModel.getRealFileName().substring(0,
						mediaModel.getRealFileName().lastIndexOf(Jnjb2bCoreConstants.CONST_DOT)));
			}
			target.setDataSheets(dataSheetNames);
		}
		target.setCatalogId(source.getCatalogId());
		target.setEcommerceFlag(source.getEcommerceFlag());
	}

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


	@Override
	public CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	@Override
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}


}