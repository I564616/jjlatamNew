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
package com.jnj.facades.product.impl;

import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.facades.data.JnjProductCarouselData;
import com.jnj.facades.product.JnjLatamProductFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnJLaProductModel;
import de.hybris.platform.core.model.media.MediaModel;

import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commerceservices.product.data.ReferenceData;
import java.util.stream.Collectors;

/**
 *
 */
public class JnjLatamProductFacadeImpl<REF_TARGET> extends DefaultJnjGTProductFacade implements JnjLatamProductFacade
{

	private static final Logger LOGGER = Logger.getLogger(JnjLatamProductFacadeImpl.class);
	@Resource(name = "b2bProductFacade")
	protected ProductFacade productFacade;

	@Autowired
	protected Converter<ProductModel, ProductData> productConverter;

	@Autowired
	private Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> jnjLatamSubstituteProductReferenceConverter;

	private final Class<JnjLatamProductFacadeImpl> currentClass = JnjLatamProductFacadeImpl.class;

	@Override
	public JnjLaProductData getLatamProductData(final ProductModel productModel)
	{

		final String METHOD_NAME = "getLatamProductData";
		JnjLaProductData laProductData = new JnjLaProductData();
		final DecimalFormat decimalFormat = new DecimalFormat("0.0");
		if (productModel instanceof JnJLaProductModel)
		{
			final JnJLaProductModel laProductModel = (JnJLaProductModel) productModel;
			laProductData = (JnjLaProductData) productFacade.getProductForOptions(laProductModel,
					Collections.singleton(ProductOption.GALLERY));
			JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.PRODUCT_CATALOG, METHOD_NAME,
					"JnjLatamProductPageController before calling convert()", currentClass);
			productConverter.convert(laProductModel, laProductData);
		}
		setupData(productModel, laProductData);
		return laProductData;
	}
	
	@Override
	public MediaModel getDetailsFileForProduct(final String productCode, final String fileName) throws BusinessException
	{
		final String METHOD_GET_DETAILS_FILE = "getDetailsFileForProduct()";
		
		MediaModel fileMedia = null;
		final JnJLaProductModel jnJProductModel = (JnJLaProductModel) jnjGTProductService.getProductForCode(productCode);
		List<MediaModel> fileMediaList = null;
		if (null != jnJProductModel)
		{
			fileMediaList = (List<MediaModel>) jnJProductModel.getData_sheet();
		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN, METHOD_GET_DETAILS_FILE + Logging.HYPHEN,
					"No Product retrieved for ProductCode[" + productCode + "]", currentClass);
			throw new BusinessException("No Product retrieved for ProductCode[" + productCode + "]");
		}
		if (null != fileMediaList)
		{
			for (final MediaModel mediaModel : fileMediaList)
			{
				if (StringUtils.equalsIgnoreCase(mediaModel.getCode(), fileName))
				{
					fileMedia = mediaModel;
					JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN, METHOD_GET_DETAILS_FILE + Logging.HYPHEN,
							"File Name matched for media with code" + mediaModel.getCode() + "]. Returning the same.", currentClass);
					break;
				}
			}
		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN, METHOD_GET_DETAILS_FILE + Logging.HYPHEN,
					"No Medias associated with he Product with ProductCode[" + productCode + "]", currentClass);
			throw new BusinessException("No Medias associated with the Product with ProductCode[" + productCode + "]");
		}
		
		return fileMedia;
	}

	private void setupData(final ProductModel productModel, final JnjLaProductData laProductData) {
		if (productModel instanceof JnJLaProductModel) {
			String productUOM = ((JnJLaProductModel) productModel).getDeliveryUnitOfMeasure().getCode();
			productUOM = productUOM == null ? StringUtils.EMPTY : productUOM;

			final List<JnjUomConversionModel> uomDetails = ((JnJLaProductModel) productModel).getUomDetails();
			if (CollectionUtils.isNotEmpty(uomDetails)) {
				for (JnjUomConversionModel uom : uomDetails) {
					setupUOMData(laProductData, uom, productUOM);
				}
			}
		}
	}

	private void setupUOMData(final JnjLaProductData laProductData, final JnjUomConversionModel uom, final String productUOM) {
		if (productUOM.equalsIgnoreCase(uom.getIsoCode())) {
			if (uom.getWidth() != null) {
				laProductData.setWidth(String.valueOf(uom.getWidth()));
			}

			if (uom.getHeight() != null) {
				laProductData.setHeight(String.valueOf(uom.getHeight()));
			}

			if (uom.getLength() != null) {
				laProductData.setLength(String.valueOf(uom.getLength()));
			}

			if (uom.getShippingUnit() != null) {
				laProductData.setShippingUnit(String.valueOf(uom.getShippingUnit()));
			}

			if (uom.getShippingWeight() != null) {
				laProductData.setShipWeight(String.valueOf(uom.getShippingWeight()));
			}
		}
	}

	@Override
	public List<JnjProductCarouselData> getProductsBoughtTogether(final String productCode) {
		List<JnjProductCarouselData> carouselProductList = new ArrayList<>();
		try {
			carouselProductList = super.getProductsBoughtTogether(productCode, jnjGTProductService.getCurrentProductCatalogVersion());
		} catch (BusinessException ex) {
			LOGGER.error("Business Exception while trying to get current session product catalog: ", ex);
		}
		return carouselProductList;
	}

	/**
	 * Fetch the list of ProductReferenceData objects
	 */
	public List<ProductReferenceData> getSubstituteProductReferences(final String code,
		final List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, final Integer limit)
	{
		final List<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>> references = getCommerceProductReferenceService()
				.getProductReferencesForCode(code, referenceTypes, limit);

		final List<ProductReferenceData> result = new ArrayList<>();
		references.stream().filter(rd -> {
			ProductReferenceData productReferenceData = new ProductReferenceData();
			if (ProductReferenceTypeEnum.SUBSTITUTE_PRODUCTS.equals(rd.getReferenceType())) {
				productReferenceData =  getJnJLatamSubstituteProductReferenceConverter().convert(rd);
				getReferenceProductConfiguredPopulator().populate(rd.getTarget(), productReferenceData.getTarget(), options);
				result.add(productReferenceData);
				return true;
			}
			return false;
		}).collect(Collectors.toList());

		return result;
	}

	public Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> getJnJLatamSubstituteProductReferenceConverter() {
		return jnjLatamSubstituteProductReferenceConverter;
	}
}
