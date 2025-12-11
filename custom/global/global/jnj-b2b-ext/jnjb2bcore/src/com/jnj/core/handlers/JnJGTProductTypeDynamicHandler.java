/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.handlers;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.model.JnJProductModel;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;


/**
 * 
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnJGTProductTypeDynamicHandler implements DynamicAttributeHandler <String, JnJProductModel>
{
	
	@Resource(name = "productService")
	private JnJGTProductService jnJGTProductService;
	private static String KIT = "KIT";
	private static String PRODUCT = "Products";
	
	public JnJGTProductService getJnJGTProductService() {
		return jnJGTProductService;
	}

	@Override
	public String get(final JnJProductModel productModel)
	{
		if(Boolean.TRUE.equals(productModel.getKitInd()))
		{
			return KIT;
		}
		return PRODUCT;
	}

	@Override
	public void set(final JnJProductModel paramMODEL, final String paramVALUE)
	{
		//
	}
}
