/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.data;

import java.util.List;
import java.util.Map;


public class JnjPCMGTHierarchyDataResponse
{

	protected String code;
	protected List<JnjPCMGTProductDataLocalAttributes> categoryName;
	protected Map<String, String> subCategories;
	protected Map<String, String> superCategories;

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the categoryName
	 */
	public List<JnjPCMGTProductDataLocalAttributes> getCategoryName()
	{
		return categoryName;
	}

	/**
	 * @param categoryName
	 *           the categoryName to set
	 */
	public void setCategoryName(final List<JnjPCMGTProductDataLocalAttributes> categoryName)
	{
		this.categoryName = categoryName;
	}

	/**
	 * @return the subCategories
	 */
	public Map<String, String> getSubCategories()
	{
		return subCategories;
	}

	/**
	 * @param subCategories
	 *           the subCategories to set
	 */
	public void setSubCategories(final Map<String, String> subCategories)
	{
		this.subCategories = subCategories;
	}

	/**
	 * @return the superCategories
	 */
	public Map<String, String> getSuperCategories()
	{
		return superCategories;
	}

	/**
	 * @param superCategories
	 *           the superCategories to set
	 */
	public void setSuperCategories(final Map<String, String> superCategories)
	{
		this.superCategories = superCategories;
	}


}
