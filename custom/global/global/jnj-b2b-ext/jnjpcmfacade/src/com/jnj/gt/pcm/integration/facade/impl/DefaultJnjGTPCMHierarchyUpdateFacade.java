/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.facade.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jnj.gt.core.model.JnjGTPCMIntegrationCronJobModel;
import com.jnj.gt.pcm.integration.constants.JnjpcmfacadeConstants;
import com.jnj.gt.pcm.integration.data.JnjPCMGTHierarchyDataResponse;
import com.jnj.gt.pcm.integration.data.JnjPCMGTProductDataLocalAttributes;
import com.jnj.gt.pcm.integration.facade.JnjGTPCMHierarchyUpdateFacade;


/**
 * This class contains implementation of pulling Category data from P360 and updating in Hybris DB
 *
 */
public class DefaultJnjGTPCMHierarchyUpdateFacade extends DefaultJnjCCP360IntegrationFacade
		implements JnjGTPCMHierarchyUpdateFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultJnjGTPCMHierarchyUpdateFacade.class);

	private static final String CATEGORY = "category";
	private static final String CATEGORY_NAME = "categoryName";
	private static final String SUB_CATEGORIES = "subCategories";
	private static final String SUPER_CATEGORIES = "superCategories";
	private static final String CATEGORY_CODE = "categoryCode";
	private static final String LOCALE = "locale";
	private static final String ATTR_VALUE = "attrValue";
	private static final int INITIAL_CURRENT_PAGE = 2;
	private static final int COUNT_ZERO = 0;
	private static final String COLON = ":";
	private static final String DEFAULT_LOCALE = "en";
	private static final String CUSTOMERGROUP = "customergroup";

	private static final int FIRST_PAGE_ID = 1;

	private CatalogVersionService catalogVersionService;
	private ModelService modelService;
	private UserService userService;

	@Override
	public boolean updateHierarchy(final JnjGTPCMIntegrationCronJobModel jobModel)
	{

		boolean isUpdateHierarchySuccess = true;
		final String url = getConfigurationService().getConfiguration()
				.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_CATEGORY_COUNTRY_URL);

		final String startDate = getJnjPCMCommonFacadeUtil().getStartDate();

		final JSONParser parser = new JSONParser();
		try
		{

			final String jsonData = getJsonDataForCategory(url, startDate, jobModel, FIRST_PAGE_ID);
			final JSONObject obj = (JSONObject) parser.parse(jsonData);
			final Object object = getProdObject(obj);

			if (null != object)
			{
				retrieveCategoryDataAndPopulate(obj, url, startDate, jobModel);
			}
			else
			{
				LOG.info("No Product Response Found For Date: " + startDate + " & sector: " + jobModel.getSector());
			}

		}
		catch (final Exception e)
		{
			LOG.error(e);
			isUpdateHierarchySuccess = false;
			return isUpdateHierarchySuccess;
		}
		return isUpdateHierarchySuccess;
	}

	protected void retrieveCategoryDataAndPopulate(final JSONObject obj, final String url, final String startDate,
			final JnjGTPCMIntegrationCronJobModel jobModel)
	{

		Map<String, JnjPCMGTHierarchyDataResponse> categoryDataMap = null;
		final Object object = getProdObject(obj);
		final JSONObject paginationData = (JSONObject) obj.get(JnjpcmfacadeConstants.PCMIntegration.PAGINATION_DATA);
		final JSONArray jsonArray = (JSONArray) object;
		final JSONParser parser = new JSONParser();
		final int totalAssetCount = Integer
				.parseInt(paginationData.get(JnjpcmfacadeConstants.PCMIntegration.TOTAL_NUMBER_OF_RESULTS).toString());
		final int numberOfPages = Integer
				.parseInt(paginationData.get(JnjpcmfacadeConstants.PCMIntegration.NUMBER_OF_PAGES).toString());
		LOG.info("Total number of pages" + numberOfPages);
		LOG.info("Total Count" + totalAssetCount);
		categoryDataMap = categoryCodeAndDataMap(jsonArray);
		populateCategoryContent(categoryDataMap, jobModel);

		for (int currentPageNumber = INITIAL_CURRENT_PAGE; currentPageNumber <= numberOfPages; currentPageNumber++)
		{
			final String jsonData = getJsonDataForCategory(url, startDate, jobModel, currentPageNumber);
			LOG.info("Hitting for currentpage " + currentPageNumber);
			try
			{
				final JSONObject categoryObject = (JSONObject) parser.parse(jsonData);
				final Object paginatedObject = getProdObject(categoryObject);
				final JSONArray paginatedProductsArray = (JSONArray) paginatedObject;
				categoryDataMap = categoryCodeAndDataMap(paginatedProductsArray);
				populateCategoryContent(categoryDataMap, jobModel);
			}
			catch (final ParseException e)
			{
				LOG.debug(e);
			}
			catch (final IllegalArgumentException ex)
			{
				LOG.debug(ex);
			}
		}
	}

	protected Object getProdObject(final JSONObject obj)
	{
		return obj.get(JnjpcmfacadeConstants.PCMIntegration.CATEGORIES);
	}

	protected void populateCategoryContent(final Map<String, JnjPCMGTHierarchyDataResponse> categoryDataMap,
			final JnjGTPCMIntegrationCronJobModel jobModel)
	{

		for (final Map.Entry<String, JnjPCMGTHierarchyDataResponse> categoryData : categoryDataMap.entrySet())
		{
			final String categoryCode = categoryData.getKey();
			final JnjPCMGTHierarchyDataResponse response = categoryData.getValue();

			final List<JnjPCMGTProductDataLocalAttributes> categoryNameList = response.getCategoryName();
			final Map<String, String> subCategories = response.getSubCategories();
			final Map<String, String> superCtagories = response.getSuperCategories();

			CategoryModel categoryModel = null;
			String defaultCtagoryName = null;

			if (categoryNameList != null && !categoryNameList.isEmpty())
			{
				defaultCtagoryName = getDefaultcategoryName(categoryNameList);
			}

			categoryModel = getCategoryModel(categoryCode, defaultCtagoryName, jobModel.getCatalogVersion());
			populateCategoryLocaleName(categoryModel, categoryNameList);
			populateSuperCategories(categoryModel, superCtagories, jobModel.getCatalogVersion());
			populateSubcategories(categoryModel, subCategories, jobModel.getCatalogVersion());
			modelService.save(categoryModel);


		}
	}

	protected String getDefaultcategoryName(final List<JnjPCMGTProductDataLocalAttributes> categoryNameList)
	{
		String defaultCtagoryName = null;
		for (final JnjPCMGTProductDataLocalAttributes localeName : categoryNameList)
		{

			if (("en").equalsIgnoreCase(localeName.getLocale()))
			{
				defaultCtagoryName = localeName.getAttrValue();
				break;
			}
		}
		return defaultCtagoryName;
	}

	protected void populateCategoryLocaleName(final CategoryModel categoryModel,
			final List<JnjPCMGTProductDataLocalAttributes> categoryName)
	{

		Locale locale = null;
		if (null != categoryName)
		{
			for (final JnjPCMGTProductDataLocalAttributes attributes : categoryName)
			{
				locale = Locale.of(attributes.getLocale());
				categoryModel.setName(attributes.getAttrValue(), locale);
			}
			LOG.info("Category name updated successfuly for Category : " + categoryModel.getCode());
		}
	}

	protected void populateSuperCategories(final CategoryModel categoryModel, final Map<String, String> superCtagories,
			final CatalogVersionModel catalogVersionModel)
	{

		final Collection<CategoryModel> exitingSuperCategories = categoryModel.getAllSupercategories();

		final List<CategoryModel> newSuperCategories = new ArrayList<>();

		if (superCtagories != null && !superCtagories.isEmpty())
		{
			for (final Map.Entry<String, String> entry : superCtagories.entrySet())
			{
				final CategoryModel superCategoryModel = getCategoryModel(entry.getKey(), entry.getValue(), catalogVersionModel);
				newSuperCategories.add(superCategoryModel);
			}

			if (exitingSuperCategories != null && !exitingSuperCategories.isEmpty() && !newSuperCategories.isEmpty())
			{
				categoryModel.getAllSupercategories().clear();
			}
			if (!newSuperCategories.isEmpty())
			{
				categoryModel.setSupercategories(newSuperCategories);
			}
		}

	}

	protected void populateSubcategories(final CategoryModel categoryModel, final Map<String, String> subCategories,
			final CatalogVersionModel catalogVersionModel)
	{
		final Collection<CategoryModel> exitingSubCategories = categoryModel.getCategories();

		final List<CategoryModel> newSubCategories = new ArrayList<>();

		if (subCategories != null && !subCategories.isEmpty())
		{
			for (final Map.Entry<String, String> entry : subCategories.entrySet())
			{
				final CategoryModel subCategoryModel = getCategoryModel(entry.getKey(), entry.getValue(), catalogVersionModel);
				newSubCategories.add(subCategoryModel);
			}

			if (exitingSubCategories != null && !exitingSubCategories.isEmpty() && !newSubCategories.isEmpty())
			{
				categoryModel.getAllSubcategories().clear();
			}
			if (!newSubCategories.isEmpty())
			{
				categoryModel.setCategories(newSubCategories);
			}
		}

	}

	protected CategoryModel getCategoryModel(final String categoryCode, final String categoryName,
			final CatalogVersionModel catalogVersionModel)
	{
		CategoryModel categoryModel = null;

		categoryModel = getJnjCCP360IntegrationService().getCategoryModel(categoryCode, catalogVersionModel);

		if (categoryModel == null)
		{
			categoryModel = new CategoryModel();
			final List<PrincipalModel> allowedUserGroups = new ArrayList<>();
			final Locale locale = Locale.of(DEFAULT_LOCALE);
			allowedUserGroups.add(userService.getUserGroupForUID(CUSTOMERGROUP));
			categoryModel.setCode(categoryCode);
			categoryModel.setName(categoryName, locale);
			categoryModel.setAllowedPrincipals(allowedUserGroups);
			categoryModel.setDisplayProducts(true);
			categoryModel.setCatalogVersion(catalogVersionModel);
			modelService.save(categoryModel);
		}
		return categoryModel;
	}

	protected Map<String, JnjPCMGTHierarchyDataResponse> categoryCodeAndDataMap(final JSONArray jsonArray)
	{
		final Map<String, JnjPCMGTHierarchyDataResponse> categoryDataMap = new HashMap<>();
		if (jsonArray != null)
		{

			for (int i = COUNT_ZERO; i < jsonArray.size(); i++)
			{
				final JSONObject innerObj = (JSONObject) jsonArray.get(i);
				final JSONObject categoryObject = (JSONObject) innerObj.get(CATEGORY);

				final JnjPCMGTHierarchyDataResponse response = new JnjPCMGTHierarchyDataResponse();
				final JSONArray categoryNameList = getAttributeJsonArray(categoryObject, CATEGORY_NAME);
				final JSONArray subCategoriesList = getAttributeJsonArray(categoryObject, SUB_CATEGORIES);
				final JSONArray superCategoriesList = getAttributeJsonArray(categoryObject, SUPER_CATEGORIES);

				final List<JnjPCMGTProductDataLocalAttributes> categoryName = new ArrayList<>();

				if (categoryObject.get(CATEGORY_CODE) != null)
				{
					response.setCode(categoryObject.get(CATEGORY_CODE).toString().trim());
				}

				if (null != categoryNameList && !categoryNameList.isEmpty())
				{
					response.setCategoryName(setResponseLocalAttributes(categoryName, categoryNameList));
				}

				populateSubCategoryResponse(response, subCategoriesList);
				populateSuperCategoryResponse(response, superCategoriesList);

				categoryDataMap.put(response.getCode(), response);
			}
		}

		return categoryDataMap;
	}

	protected void populateSubCategoryResponse(final JnjPCMGTHierarchyDataResponse response, final JSONArray subCategoriesList)
	{
		final Map<String, String> subCategories = new HashMap<>();
		if (null != subCategoriesList && !subCategoriesList.isEmpty())
		{
			for (int k = COUNT_ZERO; k < subCategoriesList.size(); k++)
			{
				final String categoryDetail = subCategoriesList.get(k).toString();
				if (categoryDetail != null && !categoryDetail.isEmpty())
				{
					final String[] categoryCode = categoryDetail.split(COLON);
					subCategories.put(categoryCode[0], categoryCode[1]);

				}
			}
			response.setSubCategories(subCategories);
		}

	}

	protected void populateSuperCategoryResponse(final JnjPCMGTHierarchyDataResponse response, final JSONArray superCategoriesList)
	{
		final Map<String, String> superCategories = new HashMap<>();
		if (null != superCategoriesList && !superCategoriesList.isEmpty())
		{
			for (int k = COUNT_ZERO; k < superCategoriesList.size(); k++)
			{
				final String categoryDetail = superCategoriesList.get(k).toString();
				if (categoryDetail != null && !categoryDetail.isEmpty())
				{
					final String[] categoryCode = categoryDetail.split(COLON);
					superCategories.put(categoryCode[0], categoryCode[1]);

				}

			}
			response.setSuperCategories(superCategories);
		}

	}



	protected List<JnjPCMGTProductDataLocalAttributes> setResponseLocalAttributes(
			final List<JnjPCMGTProductDataLocalAttributes> productAttribute, final JSONArray producAttributeList)
	{
		for (int k = COUNT_ZERO; k < producAttributeList.size(); k++)
		{
			final JSONObject productJsn = (JSONObject) producAttributeList.get(k);
			final JnjPCMGTProductDataLocalAttributes productNameLoc = new JnjPCMGTProductDataLocalAttributes(
					productJsn.get(LOCALE).toString(), productJsn.get(ATTR_VALUE).toString());
			productAttribute.add(productNameLoc);
		}
		return productAttribute;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}
	
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}



}
