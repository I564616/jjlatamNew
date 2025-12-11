/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.List;

import com.jnj.exceptions.BusinessException;


/**
 * TODO:<Bhanu-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjProductFacade extends ProductFacade
{

	/**
	 * Gets the details files associated with the products.
	 * 
	 * @param productCode
	 *           the product code
	 * @param fileName
	 *           the file name
	 * @return the details file
	 * @throws BusinessException
	 *            the business exception
	 */

	public MediaModel getDetailsFileForProduct(final String productCode, final String fileName) throws BusinessException;

	List<ProductReferenceData> getProductReferences(String code, List<ProductReferenceTypeEnum> referenceTypes,
													List<ProductOption> options, Integer limit);
}
