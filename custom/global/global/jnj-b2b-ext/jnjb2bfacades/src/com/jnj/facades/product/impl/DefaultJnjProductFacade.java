/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product.impl;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.commerceservices.product.CommerceProductReferenceService;
import de.hybris.platform.commerceservices.product.data.ReferenceData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.product.JnjProductFacade;


/**
 * TODO:<Bhanu-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjProductFacade<REF_TARGET> extends DefaultProductFacade implements JnjProductFacade
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.product.JnjProductFacade#getDetailsFile(java.lang.String)
	 */

	private static final Logger LOG = Logger.getLogger(DefaultJnjProductFacade.class);


	@Autowired
	private ProductService productService;
	private static final String METHOD_GET_DETAILS_FILE = "getDetailsFileForProduct()";

	@Autowired
	protected CommerceProductReferenceService<ProductReferenceTypeEnum, REF_TARGET> commerceProductReferenceService;
	@Autowired
	private Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> referenceDataProductReferenceConverter;


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.product.JnjProductFacade#getDetailsFile(java.lang.String)
	 */
	@Override
	public MediaModel getDetailsFileForProduct(final String productCode, final String fileName) throws BusinessException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN + METHOD_GET_DETAILS_FILE + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		MediaModel fileMedia = null;
		final JnJProductModel jnJProductModel = (JnJProductModel) productService.getProductForCode(productCode);
		List<MediaModel> fileMediaList = null;
		if (null != jnJProductModel)
		{
			fileMediaList = (List<MediaModel>) jnJProductModel.getData_sheet();
		}
		else
		{
			LOG.error(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN + METHOD_GET_DETAILS_FILE + Logging.HYPHEN
					+ "No Product Retireved for ProductCode: " + productCode);
			throw new BusinessException("No Product Retireved for ProductCode: " + productCode);
		}
		for (final MediaModel mediaModel : fileMediaList)
		{
			if (StringUtils.equals(mediaModel.getCode(), fileName))
			{
				fileMedia = mediaModel;
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN + METHOD_GET_DETAILS_FILE + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return fileMedia;
	}

	/**
	 * Fetch Product References for AEKL-1052/952
	 */
	@SuppressWarnings("unchecked")
	public List<ProductReferenceData> getProductReferences(final String code,
														   final List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, final Integer limit)
	{
		final List<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>> references = getCommerceProductReferenceService()
				.getProductReferencesForCode(code, referenceTypes, limit);

		final List<ProductReferenceData> result = new ArrayList<ProductReferenceData>();

		for (final ReferenceData<ProductReferenceTypeEnum, REF_TARGET> reference : references)
		{
			final ProductReferenceData productReferenceData = (ProductReferenceData) getReferenceDataProductReferenceConverter().convert(reference);
			getReferenceProductConfiguredPopulator().populate(reference.getTarget(), productReferenceData.getTarget(), options);
			result.add(productReferenceData);
		}

		return result;
	}
	public CommerceProductReferenceService<ProductReferenceTypeEnum, REF_TARGET> getCommerceProductReferenceService() {
		return commerceProductReferenceService;
	}


	public Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> getReferenceDataProductReferenceConverter() {
		return referenceDataProductReferenceConverter;
	}
}
