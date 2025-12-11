/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;
import java.util.Map;

import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * This class is used to get MDD product info to be used in catalog export
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnJGTMDDProductExportService
{


	public String getName(ProductModel productModel);

	public String getFranchiseName(ProductModel productModel);

	public String getDivisionName(ProductModel productModel);

	public String getProductCode(ProductModel productModel);

	public String getProductDescription(ProductModel productModel);

	public void setDeliveryUOM(JnjGTVariantProductModel deliveryVariant, JnjGTVariantProductModel salesVariant,
			StringBuilder stringBuilder);

	public void setUOMOfEaches(JnjGTVariantProductModel deliveryVariant, JnjGTVariantProductModel salesVariant,
			StringBuilder stringBuilder);

	public Map<String, String> getGtins(ProductModel productModel);

	public Map<String, List<String>> getKitDetails(ProductModel productModel);
}
