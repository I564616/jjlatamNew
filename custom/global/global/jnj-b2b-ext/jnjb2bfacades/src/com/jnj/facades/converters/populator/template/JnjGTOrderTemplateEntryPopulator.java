/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.converters.populator.template;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.facades.data.JnjTemplateEntryData;
import com.jnj.facades.template.converters.populator.JnjTemplateEntryDataPopulator;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.facades.data.JnjGTOrderTemplateEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.product.converters.populator.JnjGTProductBasicPopulator;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;


/**
 * This is used to fetch the data from solr server as per JNJ NA req
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOrderTemplateEntryPopulator extends JnjTemplateEntryDataPopulator
{
	@Resource(name = "jnjB2BUnitService")
	JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	SessionService sessionService;

	@Autowired
	UserService userService;

	@Resource(name = "jnjGTProductBasicPopulator")
	JnjGTProductBasicPopulator<ProductModel, ProductData> productPopulator;

	@Autowired
	Converter<ProductModel, ProductData> productConverter;

	@Resource(name = "productService")
	JnJGTProductService jnJGTProductService;

	@Override
	public void populate(final JnjTemplateEntryModel source, final JnjTemplateEntryData target)
	{
		super.populate(source, target);
		if (source instanceof JnjTemplateEntryModel && target instanceof JnjGTOrderTemplateEntryData)
		{
			final JnjTemplateEntryModel templateEntryModSrc = (JnjTemplateEntryModel) source;
			final JnjGTOrderTemplateEntryData templateEntryDataTar = (JnjGTOrderTemplateEntryData) target;

			final JnjGTProductData productData = new JnjGTProductData();
			long totalWeight = 0;
			long totalVolume = 0;

			productConverter.convert(templateEntryModSrc.getProduct(), productData);
			productPopulator.populate(templateEntryModSrc.getProduct(), productData);
			if (templateEntryModSrc.getReferencedVariant() != null)
			{
				productData.setGtin(templateEntryModSrc.getReferencedVariant().getEan());
				productData.setUpc(templateEntryModSrc.getReferencedVariant().getUpc());
				productData.setNumerator(templateEntryModSrc.getReferencedVariant().getNumerator().toString());
				productData.setDeliveryUnit(templateEntryModSrc.getReferencedVariant().getUnit().getName());
				productData.setSalesUnit(templateEntryModSrc.getReferencedVariant().getLwrPackagingLvlUom().getName());
			}
			if (templateEntryModSrc.getReferencedVariant() != null && templateEntryModSrc.getReferencedVariant().getWeightQty() != null
					&& templateEntryModSrc.getReferencedVariant().getWeightUom() != null)
			{
				final String productWeight = templateEntryModSrc.getReferencedVariant().getWeightQty().toString()
						+ Jnjb2bCoreConstants.SPACE + templateEntryModSrc.getReferencedVariant().getWeightUom().getCode().toString();
				totalWeight = totalWeight + templateEntryModSrc.getReferencedVariant().getWeightQty().longValue()
						* templateEntryModSrc.getQty().longValue();

				productData.setProductWeight(productWeight);
			}
			if (null!=templateEntryModSrc.getReferencedVariant() && templateEntryModSrc.getReferencedVariant().getVolumeQty() != null
					&& templateEntryModSrc.getReferencedVariant().getVolumeUom() != null)
			{

				final String productVolume = templateEntryModSrc.getReferencedVariant().getVolumeQty().toString()
						+ Jnjb2bCoreConstants.SPACE + templateEntryModSrc.getReferencedVariant().getVolumeUom().getCode().toString();

				totalVolume = totalVolume + templateEntryModSrc.getReferencedVariant().getVolumeQty().longValue()
						* templateEntryModSrc.getQty().longValue();

				productData.setProductVolume(productVolume);
			}
			//				entryData.setProductUrl(jnJNAProductService.getProductUrl(JnjTemplateEntryModel.getReferencedVariant()));

			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

			templateEntryDataTar.setTotalVolume(Long.valueOf(totalVolume).toString());
			templateEntryDataTar.setTotalWeight(Long.valueOf(totalWeight).toString());
			boolean isSalesRepDivisionCompatible = true;

			if (StringUtils.equals(currentSite, Jnjb2bCoreConstants.MDD)
					&& sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALES_REP_USER).equals(Boolean.TRUE)
					&& !jnJGTProductService.isProductDivisionSameAsUserDivision((JnJProductModel) templateEntryModSrc.getProduct()))
			{
				isSalesRepDivisionCompatible = false;
			}
			templateEntryDataTar.setSalesRepDivisionCompatible(Boolean.valueOf(isSalesRepDivisionCompatible));


			if (currentSite.equalsIgnoreCase(Jnjb2bCoreConstants.MDD))
			{
				if (((JnJProductModel) templateEntryModSrc.getProduct()).getMaterialBaseProduct() != null)
				{
					final JnJProductModel jnjGTproduct = ((JnJProductModel) templateEntryModSrc.getProduct())
							.getMaterialBaseProduct();
					final List<String> values = jnJGTProductService.getEligibleUrlAndCodeForOrderHistoryAndTemplate(jnjGTproduct,
							templateEntryModSrc.getReferencedVariant());
					final String modProductCode = values.get(0);
					productData.setCode(modProductCode);
					final String modProductUrl = values.get(1);
					productData.setUrl(modProductUrl);
				}
			}
			templateEntryDataTar.setRefVariant(productData);
			templateEntryDataTar.setQty(templateEntryModSrc.getQty());
		}
	}

}
