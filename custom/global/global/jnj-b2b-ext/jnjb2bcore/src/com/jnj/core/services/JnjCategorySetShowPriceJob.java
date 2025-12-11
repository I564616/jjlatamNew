/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Set;

import org.apache.log4j.Logger;

import com.jnj.exceptions.BusinessException;


/**
 * Cron job to set <code>showProductPrice</code> parameter for all sub categories in sync with First level category.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjCategorySetShowPriceJob extends AbstractJobPerformable<CronJobModel>
{
	protected static final Logger LOG = Logger.getLogger(JnjCategorySetShowPriceJob.class);
	protected static final String ROOT_CATEGORY_CODE = "Categories";

	protected CommerceCategoryService commerceCategoryService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			final CategoryModel rootCategory = commerceCategoryService.getCategoryForCode(ROOT_CATEGORY_CODE);
			checkFirstLevelCategoryData(rootCategory);
		}
		catch (final BusinessException exception)
		{
			LOG.error("Customer Eligiblity Cronjob computation caused an exception : " + exception.getMessage());
			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return result;
	}

	/**
	 * Checks if level 1 category has parameter <code>showProductPrice</code> set.
	 * 
	 * @param rootCategory
	 * @throws BusinessException
	 */
	protected void checkFirstLevelCategoryData(final CategoryModel rootCategory) throws BusinessException
	{
		for (final CategoryModel firstLevelCategory : rootCategory.getCategories())
		{
			if (firstLevelCategory.getShowProductPrice() != null)
			{
				setShowPriceForAllSubCategories((Set<CategoryModel>) firstLevelCategory.getAllSubcategories(),
						firstLevelCategory.getShowProductPrice());
			}
		}
	}

	/**
	 * Sets <code>showProductPrice</code> parameter for all sub categories.
	 * 
	 * @param firstLevelCategory
	 * @param showProductPrice
	 * @throws BusinessException
	 */
	protected void setShowPriceForAllSubCategories(final Set<CategoryModel> firstLevelCategory, final boolean showProductPrice)
			throws BusinessException
	{
		for (final CategoryModel subCategory : firstLevelCategory)
		{
			subCategory.setShowProductPrice(Boolean.valueOf(showProductPrice));
		}
	}

	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}



}
