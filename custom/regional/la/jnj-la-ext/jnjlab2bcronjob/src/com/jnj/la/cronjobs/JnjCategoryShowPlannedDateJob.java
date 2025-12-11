/**
 *
 */
package com.jnj.la.cronjobs;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.CategoryPlannedDate;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.CategoryPrice;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.util.JnjLaCoreUtil;


/**
 * @author sandeep.y.kumar
 *
 */
public class JnjCategoryShowPlannedDateJob extends AbstractJobPerformable<CronJobModel>
{
	/** The Constant ROOT_CATEGORY_CODE. */
	private static final String ROOT_CATEGORY_CODE = "Categories";
	public static final String PRODUCT_CATALOG_REMOVE_STRING = "productCatalog";

	/** The category service. */
	@Autowired
	private CategoryService categoryService;

	/** The catalog version service. */
	@Autowired
	private CatalogVersionService catalogVersionService;

	/**
	 *
	 * @param arg0
	 *           the arg0
	 * @return the perform result
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		final String methodName = "perform()";
		JnjGTCoreUtil.logDebugMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, Logging.BEGIN_OF_METHOD,
				JnjCategoryShowPlannedDateJob.class);
		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			/* Fetching the Online CatalogVersionModel based on CatalogUid */
			final List<String> storeIsoList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_VALID_STORES);
			final List<CategoryModel> rootCategories = new ArrayList<>();
			for (final String store : storeIsoList)
			{
				final CatalogVersionModel stagedCatalogVersionModel = catalogVersionService.getCatalogVersion(
				JnjLaCommonUtil.getIdByCountry(store) + Jnjb2bCoreConstants.PRODUCT_CATALOG, Jnjb2bCoreConstants.CATALOG_VERSION_STAGED);
				final CategoryModel rootCategory = categoryService.getCategoryForCode(stagedCatalogVersionModel, ROOT_CATEGORY_CODE);
				rootCategories.add(rootCategory);
			}

			processPlannedDateFlag(rootCategories);
		}
		catch (final BusinessException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, exception.getMessage(), exception,
					JnjCategoryShowPlannedDateJob.class);
			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, Logging.END_OF_METHOD,
				JnjCategoryShowPlannedDateJob.class);
		return result;
	}

	/**
	 * Checks if level 1 category has parameter <code>showPlannedDate</code> set.
	 *
	 * @param rootCategories
	 *           the root categories
	 * @throws BusinessException
	 *            the business exception
	 */
	private void processPlannedDateFlag(final List<CategoryModel> rootCategories) throws BusinessException
	{
		final String methodName = "processPlannedDateFlag()";
		JnjGTCoreUtil.logDebugMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, Logging.BEGIN_OF_METHOD,
				JnjCategoryShowPlannedDateJob.class);

		final List<CategoryModel> updatedCategoriesList = new ArrayList<>();
		for (final CategoryModel rootCategory : rootCategories)
		{
			for (final CategoryModel firstLevelCategory : rootCategory.getCategories())
			{
				setShowPlannedDate(firstLevelCategory);
				/* Adding the Updated First Level Category to the Updated Category List for Saving */
				updatedCategoriesList.add(firstLevelCategory);
				if (firstLevelCategory.getShowProductPrice() != null)
				{
					/* Updating Planned Date Flags for Child Categories */
					final boolean showPlannedDate = (firstLevelCategory.getShowPlannedDate() != null)
							? firstLevelCategory.getShowPlannedDate() : false;

					setShowPlannedDateForAllSubCategories(firstLevelCategory.getAllSubcategories(), showPlannedDate,
							updatedCategoriesList);
				}
			}
		}

		/* Save Updated Categories */
		try
		{
			JnjGTCoreUtil.logDebugMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, "Saving all the categories that are updated",
					JnjCategoryShowPlannedDateJob.class);
			modelService.saveAll(updatedCategoriesList);
		}
		catch (final ModelSavingException modelSavingException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, modelSavingException.getMessage(),
					modelSavingException, JnjCategoryShowPlannedDateJob.class);
			throw new BusinessException("ModelSavingException occurred while saving categories");
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, Logging.END_OF_METHOD,
				JnjCategoryShowPlannedDateJob.class);
	}

	private void setShowPlannedDate(final CategoryModel firstLevelCategory)
	{
		final String methodName = "setShowPlannedDate";
		Boolean value = Boolean.FALSE;

		if (StringUtils.isEmpty(firstLevelCategory.getCode()))
			return;

		String catalogId = firstLevelCategory.getCatalogVersion().getCatalog().getId();

		if(CategoryPrice.CATEGORY_CODE_MDD.equals(firstLevelCategory.getCode())){
			JnjGTCoreUtil
					.logDebugMessage(
							Logging.SET_SHOW_PLANNED_DATE, methodName, "Processing [MDD] Categories for [" + catalogId
									+ Logging.HYPHEN + Jnjb2bCoreConstants.CATALOG_VERSION_STAGED + "] catalog.",
							JnjCategoryShowPlannedDateJob.class);
			value = getMDDValue(catalogId);
		} else if(CategoryPrice.CATEGORY_CODE_PHR.equals(firstLevelCategory.getCode())) {
			JnjGTCoreUtil
					.logDebugMessage(
							Logging.SET_SHOW_PLANNED_DATE, methodName, "Processing [PHR] Categories for [" + catalogId
									+ Logging.HYPHEN + Jnjb2bCoreConstants.CATALOG_VERSION_STAGED + "] catalog.",
							JnjCategoryShowPlannedDateJob.class);
			value = getPHRValue(catalogId);
		}

		firstLevelCategory.setShowPlannedDate(value);
	}

	private Boolean getPHRValue(final String productCatalog)
	{
		final String country = StringUtils.removeEndIgnoreCase(productCatalog, PRODUCT_CATALOG_REMOVE_STRING);
		final String propertyName = country + CategoryPlannedDate.PHR_SHOW_PLANNED_DATE_SUFIX;
		return Config.getBoolean(propertyName, true);
	}

	private Boolean getMDDValue(final String productCatalog)
	{
		final String country = StringUtils.removeEndIgnoreCase(productCatalog, PRODUCT_CATALOG_REMOVE_STRING);
		final String propertyName = country + CategoryPlannedDate.MDD_SHOW_PLANNED_DATE_SUFIX;
		return Config.getBoolean(propertyName, true);
	}

	/**
	 * Sets <code>showPlannedDate</code> parameter for all sub categories.
	 *
	 * @param firstLevelCategory
	 *           the first level category
	 * @param showProductPrice
	 *           the show product price
	 * @param updatedCategoriesList
	 *           the updated categories list
	 * @throws BusinessException
	 *            the business exception
	 */
	private void setShowPlannedDateForAllSubCategories(final Collection<CategoryModel> firstLevelCategory,
			final boolean showProductPrice, final List<CategoryModel> updatedCategoriesList) throws BusinessException
	{
		final String methodName = "setShowPlannedDateForAllSubCategories()";
		JnjGTCoreUtil.logDebugMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, Logging.BEGIN_OF_METHOD,
				JnjCategoryShowPlannedDateJob.class);

		for (final CategoryModel subCategory : firstLevelCategory)
		{
			subCategory.setShowPlannedDate(showProductPrice);
			updatedCategoriesList.add(subCategory);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SET_SHOW_PLANNED_DATE, methodName, Logging.END_OF_METHOD,
				JnjCategoryShowPlannedDateJob.class);
	}

	/**
	 * Checks if is abortable.
	 *
	 * @return true, if is abortable
	 */
	@Override
	public boolean isAbortable()
	{
		return true;
	}

}
