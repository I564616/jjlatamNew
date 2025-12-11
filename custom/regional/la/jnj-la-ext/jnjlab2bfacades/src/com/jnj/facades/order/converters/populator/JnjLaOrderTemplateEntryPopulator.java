/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.facades.converters.populator.template.JnjGTOrderTemplateEntryPopulator;
import com.jnj.facades.data.JnjLaOrderTemplateEntryData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.data.JnjTemplateEntryData;
import com.jnj.facades.product.converters.populator.JnjGTProductBasicPopulator;


/**
 *
 */
public class JnjLaOrderTemplateEntryPopulator extends JnjGTOrderTemplateEntryPopulator
{

	@Resource(name = "jnjB2BUnitService")
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserService userService;

	@Resource(name = "jnjGTProductBasicPopulator")
	private JnjGTProductBasicPopulator<ProductModel, ProductData> productPopulator;

	@Autowired
	private Converter<ProductModel, ProductData> productConverter;

	@Resource(name = "productService")
	private JnJGTProductService jnJGTProductService;

	@Override
	public void populate(final JnjTemplateEntryModel source, final JnjTemplateEntryData target)
	{

		if (target instanceof JnjLaOrderTemplateEntryData)
		{
			final JnjTemplateEntryModel templateEntryModSrc = source;
			final JnjLaOrderTemplateEntryData templateEntryDataTar = (JnjLaOrderTemplateEntryData) target;

			final JnjLaProductData productData = new JnjLaProductData();
			final long totalWeight = 0;
			final long totalVolume = 0;

			productConverter.convert(templateEntryModSrc.getProduct(), productData);
			productPopulator.populate(templateEntryModSrc.getProduct(), productData);

			productData.setNumerator(templateEntryModSrc.getProduct().getNumeratorDUOM());
			productData.setDeliveryUnit(templateEntryModSrc.getProduct().getDeliveryUnitOfMeasure().getName());
			productData.setSalesUnit(templateEntryModSrc.getProduct().getUnit().getName());
			productData.setSalesUnitCode(templateEntryModSrc.getProduct().getUnit().getCode());
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

			templateEntryDataTar.setTotalVolume(Long.toString(totalVolume));
			templateEntryDataTar.setTotalWeight(Long.toString(totalWeight));
			boolean isSalesRepDivisionCompatible = true;

			if (StringUtils.equals(currentSite, Jnjb2bCoreConstants.MDD)
					&& sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALES_REP_USER).equals(Boolean.TRUE)
					&& !jnJGTProductService.isProductDivisionSameAsUserDivision(templateEntryModSrc.getProduct()))
			{
				isSalesRepDivisionCompatible = false;
			}
			templateEntryDataTar.setSalesRepDivisionCompatible(Boolean.valueOf(isSalesRepDivisionCompatible));


			if (currentSite.equalsIgnoreCase(Jnjb2bCoreConstants.MDD))
			{
				final JnJProductModel jnjNAproduct = templateEntryModSrc.getProduct();
				final String url = jnJGTProductService.getProductUrl(jnjNAproduct);
				productData.setCode(jnjNAproduct.getCode());
				productData.setUrl(url);
				productData.setCatalogId(jnjNAproduct.getCatalogId());
			}
			templateEntryDataTar.setRefVariant(productData);
			templateEntryDataTar.setCatalogId(productData.getCatalogId());
			templateEntryDataTar.setQty(templateEntryModSrc.getQty());
		}
	}


}
