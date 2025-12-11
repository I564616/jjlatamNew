/**
 *
 */
package com.jnj.facades.category.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.category.JnjCategoryFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.enums.JnjTargetCatalogs;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.services.CMSSiteService;


/**
 * The Class JnjCategoryFacadeImpl.
 *
 * @author balinder.singh
 */
public class JnjCategoryFacadeImpl implements JnjCategoryFacade
{

	private static final String PARENT_CATEGORY_SET_TO_TRUE = "parentCategorySetToTrue";

	private static final String PARENT_CATEGORY_UPDATED = "parentCategoryUpdated";

	private static final String HIDE_CATEGORY_CRON_JOB = "hide Category Cron Job";

	/** The category service. */
	@Autowired
	private CategoryService categoryService;

	/** The catalog version service. */
	@Autowired
	private CatalogVersionService catalogVersionService;

	/** The catalog service. */
	@Autowired
	private CatalogService catalogService;

	/** The model service. */
	@Autowired
	private ModelService modelService;

	/** The commerce category service. */
	@Autowired
	private CommerceCategoryService commerceCategoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private CMSSiteService cmsSiteService;

	@Resource(name = "categoryConverter")
	protected Converter<CategoryModel, CategoryData> categoryConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.category.JnjCategoryFacade#hideCategoryWithNoProducts()
	 */
	@Override
	public boolean hideCategoryWithNoProducts(final JnjTargetCatalogs jnjTargetCatalogs)
	{
		final String METHOD_NAME = "hideCategoryWithNoProducts";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);
		boolean hideCategoryProcessStatus = true;
		try
		{
			if (JnjTargetCatalogs.ALLCATALOGS.equals(jnjTargetCatalogs))
			{
				hideCategoryProcessStatus = processAllCatalogsHideCategory();
			}
			else
			{
				processHideCategory(
						JnjLaCommonUtil.getIdByCountry(jnjTargetCatalogs.toString()) + Jnjb2bCoreConstants.PRODUCT_CATALOG);
			}
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, "Exception occurred", exception,
					JnjCategoryFacadeImpl.class);
			hideCategoryProcessStatus = false;
		}

		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
		return hideCategoryProcessStatus;
	}

	/**
	 *
	 */
	private boolean processAllCatalogsHideCategory()
	{
		final String methodName = "processAllCatalogsHideCategory()";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);
		boolean hideCategoryProcessStatus = false;

		final List<String> myList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_VALID_STORES);
		for (final String countryIso : myList)
		{
			final String productCatalog = JnjLaCommonUtil.getIdByCountry(countryIso) + Jnjb2bCoreConstants.PRODUCT_CATALOG;
			try
			{
				processHideCategory(productCatalog);
				hideCategoryProcessStatus = true;

				JnjGTCoreUtil.logInfoMessage(HIDE_CATEGORY_CRON_JOB, methodName,
						"Product catalog ::: " + productCatalog + " updated sucessfully.", JnjCategoryFacadeImpl.class);
			}
			catch (final Exception exception)
			{
				JnjGTCoreUtil.logErrorMessage(HIDE_CATEGORY_CRON_JOB, methodName,
						"Exception occurred while updating the product catalog :: " + productCatalog, exception,
						JnjCategoryFacadeImpl.class);
				hideCategoryProcessStatus = false;
			}
		}
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
		return hideCategoryProcessStatus;
	}

	/**
	 * Hide Category Method - Toggles [productPresentForTheCategory] for individual category. This method is Deprecated.
	 * The new version is processHideCategory()
	 *
	 * @param catalogName
	 *           the catalog name
	 * @throws BusinessException
	 *            the business exception
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void hideCategory(final String catalogName) throws BusinessException
	{
		final String methodName = "hideCategory";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);
		JnjGTCoreUtil.logInfoMessage(HIDE_CATEGORY_CRON_JOB, methodName,
				"########### Start of Hide Category Process For: " + catalogName + "- Staged ###############",
				JnjCategoryFacadeImpl.class);
		try
		{
			final CategoryModel category = categoryService.getCategoryForCode(
					catalogVersionService.getCatalogVersion(catalogName, Jnjb2bCoreConstants.CATALOG_VERSION_STAGED),
					Jnjlab2bcoreConstants.SUPER_PARENT_CATEGORY);

			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName,
					"Setting [productPresentForTheCategory] to [false] for all the Categories.", JnjCategoryFacadeImpl.class);

			for (final CategoryModel categoryModel : category.getAllSubcategories())
			{
				categoryModel.setProductPresentForTheCategory(Boolean.FALSE);
			}

			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName,
					"Start of Toggling [productPresentForTheCategory] to for the Categories.", JnjCategoryFacadeImpl.class);
			final Calendar calendar = Calendar.getInstance();
			for (final CategoryModel categoryModel : category.getAllSubcategories())
			{
				final List<ProductModel> products = categoryModel.getProducts();
				if (categoryModel.getProducts() != null && products.isEmpty())
				{
					for (final ProductModel productModel : products)
					{
						final Date productOfflineDate = productModel.getOfflineDate();
						if (null == productOfflineDate || calendar.before(productOfflineDate))
						{
							JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName,
									"First Online Product [" + productModel.getCode() + "] found for the Category ["
											+ categoryModel.getCode() + "] Setting "
											+ "[productPresentForTheCategory] to [true] for the Category [" + categoryModel.getCode() + "]",
									JnjCategoryFacadeImpl.class);
							categoryModel.setProductPresentForTheCategory(Boolean.TRUE);
							break;
						}
					}
				}
			}
			saveChanges(category);
		}
		catch (final ModelSavingException modelSavingException)
		{
			JnjGTCoreUtil.logErrorMessage("CategoryFacadeImplException", methodName, "Message.", modelSavingException,
					JnjCategoryFacadeImpl.class);
			JnjGTCoreUtil.logWarnMessage(HIDE_CATEGORY_CRON_JOB, methodName,
					"Category Model not saved. Throwing Business Exception.", JnjCategoryFacadeImpl.class);
			throw new BusinessException("ModelSavingException occurred while saving categories for catalog [" + catalogName + "]");
		}
		JnjGTCoreUtil.logInfoMessage(HIDE_CATEGORY_CRON_JOB, methodName,
				"########### End of Hide Category Process For: " + catalogName + "- Staged ###############",
				JnjCategoryFacadeImpl.class);
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
	}

	private void saveChanges(final CategoryModel category)
	{
		final String methodName = "saveChanges";
		try
		{
			modelService.saveAll(category.getAllSubcategories());
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage("CategoryFacadeImplException", methodName, "Message.", e, JnjCategoryFacadeImpl.class);

		}
	}


	/**
	 * Process hide category. Toggles [productPresentForTheCategory] for individual category.
	 *
	 * @param catalogName
	 *           the catalog name
	 * @throws BusinessException
	 *            the business exception
	 */
	@SuppressWarnings("unused")
	private void processHideCategory(final String catalogName) throws BusinessException
	{
		final String METHOD_NAME = "processHideCategory";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);

		final CatalogModel currentCatalog = catalogService.getCatalogForId(catalogName);

		JnjGTCoreUtil
				.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME,
						"########### Start of Hide Category Process For [" + catalogName + "]["
								+ currentCatalog.getActiveCatalogVersion().getVersion() + "] ###############",
						JnjCategoryFacadeImpl.class);

		/* Get the root category for Active Version of the provided Catalog */
		final CategoryModel rootCategory = categoryService.getCategoryForCode(currentCatalog.getActiveCatalogVersion(),
				Jnjlab2bcoreConstants.SUPER_PARENT_CATEGORY);

		/* This collection holds all the Categories that were updated during the Process and need a save. */
		final List<CategoryModel> updatedCategoriesList = new ArrayList<>();

		final List<CategoryModel> firstLevelCategoriesList = rootCategory.getCategories();
		final Set<CategoryModel> firstLevelCategories = new HashSet<>();
		firstLevelCategories.addAll(firstLevelCategoriesList);

		firstLevelCategoriesLoop: for (final CategoryModel firstLevelCategory : firstLevelCategories)
		{
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, "Inside First Level Category Loop",
					JnjCategoryFacadeImpl.class);
			/*
			 * [firstLevelCategorySetToTrue] Flag is used to track 1st [TRUE] set to [ProductPresentForTheCategory] of the
			 * current 1st level category. It helps to avoid redundant processing over 2nd Level child Category. If one
			 * child category(2nd Level) has [ProductPresentForTheCategory] set to [TRUE] the parent category flag must
			 * also be [TRUE].
			 */
			boolean firstLevelCategorySetToTrue = false;
			boolean firstLevelCategoryUpdated = false;
			final Map<String, Boolean> categoryUpdateFlags = new HashMap<>();
			categoryUpdateFlags.put(PARENT_CATEGORY_SET_TO_TRUE, Boolean.valueOf(firstLevelCategorySetToTrue));
			categoryUpdateFlags.put(PARENT_CATEGORY_UPDATED, Boolean.valueOf(firstLevelCategoryUpdated));

			/* Fetch all the Child Categories */
			final List<CategoryModel> secondLevelCategoriesList = firstLevelCategory.getCategories();
			final Set<CategoryModel> secondLevelCategories = new HashSet<>();
			secondLevelCategories.addAll(secondLevelCategoriesList);

			secondLevelCategoriesLoop: for (final CategoryModel secondLevelCategory : secondLevelCategories)
			{
				JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, "Inside Second Level Category Loop",
						JnjCategoryFacadeImpl.class);
				boolean productPresentForSecondLevelCategory = false;
				secondLevelCategoryCheck(updatedCategoriesList, secondLevelCategory);
				productPresentForSecondLevelCategory = secondLevelCategory.getProductPresentForTheCategory().booleanValue();
				updateCategoryProductFlag(productPresentForSecondLevelCategory, categoryUpdateFlags, firstLevelCategory);
				firstLevelCategorySetToTrue = categoryUpdateFlags.get(PARENT_CATEGORY_SET_TO_TRUE).booleanValue();
				firstLevelCategoryUpdated = categoryUpdateFlags.get(PARENT_CATEGORY_UPDATED).booleanValue();
			} // End of secondLevelCategoriesLoop

			/*
			 * After all the 2nd Level Categories of this current 1st Level Category are processed and still If
			 * [firstLevelCategorySetToTrue] flag is not [TRUE] then all the child categories of this 1st Level Categories
			 * are without products. Thus set [ProductPresentForTheCategory] flag to [FALSE] for current category(1st
			 * Level).
			 */
			if (!firstLevelCategorySetToTrue)
			{
				firstLevelCategoryUpdated = setCatProductFlagToFalse(firstLevelCategory, firstLevelCategoryUpdated);
			}
			/* Add current 4th Level Category to 'To Update' Collection */
			if (firstLevelCategoryUpdated)
			{
				updatedCategoriesList.add(firstLevelCategory);
			}

		} // End of firstLevelCategoriesLoop

		/* Saving all the Categories that were Updated */
		try
		{
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, "Saving all the categories that are updated",
					JnjCategoryFacadeImpl.class);
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME,
					"Total Count of Categories to be Saved = [" + updatedCategoriesList.size() + "]", JnjCategoryFacadeImpl.class);
			modelService.saveAll(updatedCategoriesList);
		}
		catch (final ModelSavingException modelSavingException)
		{
			JnjGTCoreUtil.logErrorMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, modelSavingException.getMessage(),
					modelSavingException, JnjCategoryFacadeImpl.class);
			throw new BusinessException("ModelSavingException occurred while saving categories [" + catalogName + "]["
					+ currentCatalog.getActiveCatalogVersion().getVersion() + "]");
		}

		JnjGTCoreUtil
				.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME,
						"########### End of Hide Category Process For [" + catalogName + "]["
								+ currentCatalog.getActiveCatalogVersion().getVersion() + "] ###############",
						JnjCategoryFacadeImpl.class);
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
	}

	/**
	 * This methods processes the child categories at 2nd Level.
	 *
	 * @param updatedCategoriesList
	 *           the updated categories list
	 * @param secondLevelCategory
	 *           the second level category
	 */
	@SuppressWarnings("unused")
	private void secondLevelCategoryCheck(final List<CategoryModel> updatedCategoriesList, final CategoryModel secondLevelCategory)
	{
		final String METHOD_NAME = "secondLevelCategoryCheck";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);
		/*
		 * [secondLevelCategorySetToTrue] Flag is used to track 1st [TRUE] set to [ProductPresentForTheCategory] of the
		 * current 2nd level category. It helps to avoid redundant processing over 3rd Level child Category. If one child
		 * category(3rd Level) has [ProductPresentForTheCategory] set to [TRUE] the parent category flag must also be
		 * [TRUE].
		 */
		boolean secondLevelCategorySetToTrue = false;
		boolean secondLevelCategoryUpdated = false;
		final Map<String, Boolean> categoryUpdateFlags = new HashMap<>();
		categoryUpdateFlags.put(PARENT_CATEGORY_SET_TO_TRUE, Boolean.valueOf(secondLevelCategorySetToTrue));
		categoryUpdateFlags.put(PARENT_CATEGORY_UPDATED, Boolean.valueOf(secondLevelCategoryUpdated));

		/* Fetch all the Child Categories */
		final List<CategoryModel> thirdLevelCategoriesList = secondLevelCategory.getCategories();
		final Set<CategoryModel> thirdLevelCategories = new HashSet<>();
		thirdLevelCategories.addAll(thirdLevelCategoriesList);


		thirdLevelCategoriesLoop: for (final CategoryModel thirdLevelCategory : thirdLevelCategories)
		{
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, "Inside Third Level Category Loop",
					JnjCategoryFacadeImpl.class);
			boolean productPresentForThirdLevelCategory = false;
			thirdLevelCategoryCheck(updatedCategoriesList, thirdLevelCategory);
			productPresentForThirdLevelCategory = thirdLevelCategory.getProductPresentForTheCategory().booleanValue();

			updateCategoryProductFlag(productPresentForThirdLevelCategory, categoryUpdateFlags, secondLevelCategory);
			secondLevelCategorySetToTrue = categoryUpdateFlags.get(PARENT_CATEGORY_SET_TO_TRUE).booleanValue();
			secondLevelCategoryUpdated = categoryUpdateFlags.get(PARENT_CATEGORY_UPDATED).booleanValue();

		} // End of thirdLevelCategoriesLoop

		/*
		 * After all the 3rd Level Categories of this current 2nd Level Category are processed and still If
		 * [secondLevelCategorySetToTrue] flag is not [TRUE] then all the child categories of this 2nd Level Categories
		 * are without products. Thus set [ProductPresentForTheCategory] flag to [FALSE] for current category(2nd Level).
		 */
		if (!secondLevelCategorySetToTrue)
		{
			secondLevelCategoryUpdated = setCatProductFlagToFalse(secondLevelCategory, secondLevelCategoryUpdated);
		}

		/* Add current 4th Level Category to 'To Update' Collection */
		if (secondLevelCategoryUpdated)
		{
			updatedCategoriesList.add(secondLevelCategory);
		}

		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
	}

	/**
	 * This methods processes the child categories at 3rd Level.
	 *
	 * @param updatedCategoriesList
	 *           the updated categories list
	 * @param thirdLevelCategory
	 *           the third level category
	 */
	@SuppressWarnings("unused")
	private void thirdLevelCategoryCheck(final List<CategoryModel> updatedCategoriesList, final CategoryModel thirdLevelCategory)
	{
		final String METHOD_NAME = "thirdLevelCategoryCheck";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);

		/*
		 * [thirdLevelCategorySetToTrue] Flag is used to track 1st [TRUE] set to [ProductPresentForTheCategory] of the
		 * current 3rd level category. It helps to avoid redundant processing over 4th Level child Category. If one child
		 * category(4th Level) has [ProductPresentForTheCategory] set to [TRUE] the parent category flag must also be
		 * [TRUE].
		 */
		boolean thirdLevelCategorySetToTrue = false;
		boolean thirdLevelCategoryUpdated = false;
		final Map<String, Boolean> categoryUpdateFlags = new HashMap<>();
		categoryUpdateFlags.put(PARENT_CATEGORY_SET_TO_TRUE, Boolean.valueOf(thirdLevelCategorySetToTrue));
		categoryUpdateFlags.put(PARENT_CATEGORY_UPDATED, Boolean.valueOf(thirdLevelCategoryUpdated));

		/* Fetch all the Child Categories */
		final List<CategoryModel> fourthLevelCategoriesList = thirdLevelCategory.getCategories();
		final Set<CategoryModel> fourthLevelCategories = new HashSet<>();
		fourthLevelCategories.addAll(fourthLevelCategoriesList);

		fourthLevelCategoriesLoop: for (final CategoryModel fourthLevelCategory : fourthLevelCategories)
		{
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, "Inside Fourth Level Category Loop",
					JnjCategoryFacadeImpl.class);
			boolean productPresentForFourthLevelCategory = false;
			fourthLevelCategoryCheck(updatedCategoriesList, fourthLevelCategory);
			productPresentForFourthLevelCategory = fourthLevelCategory.getProductPresentForTheCategory().booleanValue();
			updateCategoryProductFlag(productPresentForFourthLevelCategory, categoryUpdateFlags, thirdLevelCategory);
			thirdLevelCategorySetToTrue = categoryUpdateFlags.get(PARENT_CATEGORY_SET_TO_TRUE).booleanValue();
			thirdLevelCategoryUpdated = categoryUpdateFlags.get(PARENT_CATEGORY_UPDATED).booleanValue();
		} // End of fourthLevelCategoriesLoop

		/*
		 * After all the 4th Level Categories of this current 3rd Level Category are processed and still If
		 * [thirdLevelCategorySetToTrue] flag is not [TRUE] then all the child categories of this 3rd Level Categories are
		 * without products. Thus set [ProductPresentForTheCategory] flag to [FALSE] for current category(3rd Level).
		 */
		if (!thirdLevelCategorySetToTrue)
		{
			thirdLevelCategoryUpdated = setCatProductFlagToFalse(thirdLevelCategory, thirdLevelCategoryUpdated);
		}

		/* Add current 4th Level Category to 'To Update' Collection */
		if (thirdLevelCategoryUpdated)
		{
			updatedCategoriesList.add(thirdLevelCategory);
		}

		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);
	}



	/**
	 * This methods processes the child categories at 4th Level.
	 *
	 * @param updatedCategoriesList
	 *           the updated categories list
	 * @param fourthLevelCategory
	 *           the fourth level category
	 */
	@SuppressWarnings("unused")
	private void fourthLevelCategoryCheck(final List<CategoryModel> updatedCategoriesList, final CategoryModel fourthLevelCategory)
	{
		final String METHOD_NAME = "fourthLevelCategoryCheck";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);

		/*
		 * [fourthLevelCategorySetToTrue] Flag is used to track 1st [TRUE] set to [ProductPresentForTheCategory] of the
		 * current 4th level category. It helps to avoid redundant processing over current 4th Level category, as If one
		 * child category(5th Level) has [ProductPresentForTheCategory] set to [TRUE] the parent category flag must also
		 * be [TRUE].
		 */
		boolean fourthLevelCategorySetToTrue = false;
		boolean fourthLevelCategoryUpdated = false;
		final Map<String, Boolean> categoryUpdateFlags = new HashMap<>();
		categoryUpdateFlags.put(PARENT_CATEGORY_SET_TO_TRUE, Boolean.valueOf(fourthLevelCategorySetToTrue));
		categoryUpdateFlags.put(PARENT_CATEGORY_UPDATED, Boolean.valueOf(fourthLevelCategoryUpdated));

		/* Fetch all the Child Categories */
		final List<CategoryModel> fifthLevelCategoriesList = fourthLevelCategory.getCategories();
		final Set<CategoryModel> fifthLevelCategories = new HashSet<>();
		fifthLevelCategories.addAll(fifthLevelCategoriesList);

		fifthLevelCategoriesLoop: for (final CategoryModel fifthLevelCategory : fifthLevelCategories)
		{
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, "Inside Fifth Level Category Loop",
					JnjCategoryFacadeImpl.class);

			boolean productPresentForFifthLevelCategory = false;
			if (null != fifthLevelCategory.getDisplayProducts() && fifthLevelCategory.getDisplayProducts().booleanValue())
			{
				/* This category level has the products. We will perform a product check here */
				categoryProductCheck(fifthLevelCategory, updatedCategoriesList);
				productPresentForFifthLevelCategory = fifthLevelCategory.getProductPresentForTheCategory().booleanValue();
			}
			updateCategoryProductFlag(productPresentForFifthLevelCategory, categoryUpdateFlags, fourthLevelCategory);

			fourthLevelCategorySetToTrue = categoryUpdateFlags.get(PARENT_CATEGORY_SET_TO_TRUE).booleanValue();
			fourthLevelCategoryUpdated = categoryUpdateFlags.get(PARENT_CATEGORY_UPDATED).booleanValue();
		} //End of fifthLevelCategoriesLoop

		/*
		 * After all the 5th Level Categories of this current 4th Level Category are processed and still If
		 * [fourthLevelCategorySetToTrue] flag is not [TRUE] then all the child categories of this 4th Level Categories
		 * are without products. Thus set [ProductPresentForTheCategory] flag to [FALSE] for current category(4th Level).
		 */
		if (!fourthLevelCategorySetToTrue)
		{
			fourthLevelCategoryUpdated = setCatProductFlagToFalse(fourthLevelCategory, fourthLevelCategoryUpdated);
		}
		/* Add current 4th Level Category to 'To Update' Collection */
		if (fourthLevelCategoryUpdated)
		{
			updatedCategoriesList.add(fourthLevelCategory);
		}

		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
	}

	/**
	 * This method checks the Products for the Last Level Categories. If Online products are present then sets the
	 * [ProductPresentForTheCategory] flag to [true] otherwise [false]
	 *
	 * @param fifthLevelCategory
	 *           the fifth level category
	 * @param updatedCategoriesList
	 *           the updated categories list
	 */
	private void categoryProductCheck(final CategoryModel fifthLevelCategory, final List<CategoryModel> updatedCategoriesList)
	{
		final String methodName = "categoryProductCheck";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);

		final List<ProductModel> products = fifthLevelCategory.getProducts();
		boolean categorySaveRequired = false;
		if (null != products && !products.isEmpty())
		{
			/* Products Exists in Category. Check whether the Product is Online */
			final Calendar calendar = Calendar.getInstance();

			/* [onlineProductFound] is an indicator of first Online Product in the Current Operating Category */

			boolean onlineProductFound = false;
			productLoop: for (final ProductModel productModel : products)
			{
				final Date productOfflineDate = productModel.getOfflineDate();
				final Date productOnlineDate = productModel.getOnlineDate();
				if ((null != ((JnJProductModel) productModel).getEcommerceFlag()
						&& ((JnJProductModel) productModel).getEcommerceFlag().booleanValue())
						&& (null == productOnlineDate || calendar.after(productOnlineDate))
						&& (null == productOfflineDate || calendar.before(productOfflineDate)))
				{
					JnjGTCoreUtil.logDebugMessage(
							HIDE_CATEGORY_CRON_JOB, methodName, "Product [" + productModel.getCode()
									+ "] is found Online for Category with Code [" + fifthLevelCategory.getCode() + "] .",
							JnjCategoryFacadeImpl.class);

					JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName,
							"Setting [ProductPresentForTheCategory] flag for Category with Code [" + fifthLevelCategory.getCode()
									+ "] to [true]",
							JnjCategoryFacadeImpl.class);

					/*
					 * Check if the [ProductPresentForTheCategory] flag is already [TRUE] or not, to avoid redundant saving.
					 */
					if (null != fifthLevelCategory.getProductPresentForTheCategory()
							&& !fifthLevelCategory.getProductPresentForTheCategory().booleanValue())
					{
						fifthLevelCategory.setProductPresentForTheCategory(Boolean.TRUE);
						categorySaveRequired = true;
					}
					onlineProductFound = true;
					break productLoop;
				}
			}
			if (!onlineProductFound)
			{
				JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName,
						"No Product is the found Online for Category with Code [" + fifthLevelCategory.getCode() + "] .",
						JnjCategoryFacadeImpl.class);
				JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName,
						"Setting [ProductPresentForTheCategory] flag for Category with Code [" + fifthLevelCategory.getCode()
								+ "] to [false]",
						JnjCategoryFacadeImpl.class);

				/* Check if the [ProductPresentForTheCategory] flag is already [FALSE] or not, to avoid redundant saving. */
				if (null != fifthLevelCategory.getProductPresentForTheCategory()
						&& fifthLevelCategory.getProductPresentForTheCategory().booleanValue())
				{
					fifthLevelCategory.setProductPresentForTheCategory(Boolean.FALSE);
					categorySaveRequired = true;
				}
			}
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName,
					"No Product is found for Category with Code [" + fifthLevelCategory.getCode() + "] .",
					JnjCategoryFacadeImpl.class);
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName,
					"Setting [ProductPresentForTheCategory] flag for Category with Code [" + fifthLevelCategory.getCode()
							+ "] to [false]",
					JnjCategoryFacadeImpl.class);

			if (null != fifthLevelCategory.getProductPresentForTheCategory()
					&& fifthLevelCategory.getProductPresentForTheCategory().booleanValue())
			{
				/* No Product Exist in Category */
				fifthLevelCategory.setProductPresentForTheCategory(Boolean.FALSE);
				categorySaveRequired = true;
			}
		}

		/* Add current 5th Level Category to 'To Update' Collection */
		if (categorySaveRequired)
		{
			updatedCategoriesList.add(fifthLevelCategory);
		}

		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, methodName, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
	}

	/**
	 * This method checks the [currentCategorySetToTrue] flag. If [TRUE], then nothing is changed in the
	 * [parentCategory]. If [FALSE] then set [ProductPresentForTheCategory] for current [parentCategory].
	 *
	 * @param childCategoryFlag
	 *           the child category flag
	 * @param parentCategory
	 *           the parent category
	 * @param categoryUpdateFlags
	 *
	 */
	private void updateCategoryProductFlag(final boolean childCategoryFlag, final Map<String, Boolean> categoryUpdateFlags,
			final CategoryModel parentCategory)
	{
		final String METHOD_NAME = "updateCategoryProductFlag";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);

		boolean currentCategorySetToTrue = categoryUpdateFlags.get(PARENT_CATEGORY_SET_TO_TRUE).booleanValue();
		boolean currentCategoryUpdated = categoryUpdateFlags.get(PARENT_CATEGORY_UPDATED).booleanValue();

		categoryUpdateFlags.put(PARENT_CATEGORY_SET_TO_TRUE, Boolean.FALSE);
		if (!currentCategorySetToTrue && childCategoryFlag)
		{
			/* Check if the [ProductPresentForTheCategory] flag is already [TRUE] or not, to avoid redundant saving. */
			if (null != parentCategory.getProductPresentForTheCategory()
					&& !parentCategory.getProductPresentForTheCategory().booleanValue())
			{
				JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB,
						"Setting [ProductPresentForTheCategory] flag for Category with Code [" + parentCategory.getCode()
								+ "] to [true]",
						Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);
				parentCategory.setProductPresentForTheCategory(Boolean.TRUE);
				currentCategoryUpdated = true;
			}
			currentCategorySetToTrue = true;
		}

		if (currentCategoryUpdated)
		{
			categoryUpdateFlags.put(PARENT_CATEGORY_UPDATED, Boolean.TRUE);
		}
		else
		{
			categoryUpdateFlags.put(PARENT_CATEGORY_UPDATED, Boolean.FALSE);
		}

		if (currentCategorySetToTrue)
		{
			categoryUpdateFlags.put(PARENT_CATEGORY_SET_TO_TRUE, Boolean.TRUE);

		}
		else
		{
			categoryUpdateFlags.put(PARENT_CATEGORY_SET_TO_TRUE, Boolean.FALSE);

		}

		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
	}

	/**
	 * This method sets [ProductPresentForTheCategory] to [FALSE].
	 *
	 * @param currentCategory
	 *           the current category
	 * @param currentCategoryUpdated
	 *           the third level category updated
	 * @return currentCategoryUpdated
	 */
	private boolean setCatProductFlagToFalse(final CategoryModel currentCategory, final boolean currentCategoryUpdated)
	{
		final String METHOD_NAME = "setCatProductFlagToFalse";
		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjCategoryFacadeImpl.class);

		/* Check if the [ProductPresentForTheCategory] flag is already [FALSE] or not, to avoid redundant saving. */
		if (null != currentCategory.getProductPresentForTheCategory()
				&& currentCategory.getProductPresentForTheCategory().booleanValue())
		{
			JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME,
					"Setting [ProductPresentForTheCategory] flag for Category with Code [" + currentCategory.getCode()
							+ "] to [false]",
					JnjCategoryFacadeImpl.class);
			currentCategory.setProductPresentForTheCategory(Boolean.FALSE);

		}

		JnjGTCoreUtil.logDebugMessage(HIDE_CATEGORY_CRON_JOB, METHOD_NAME, Logging.END_OF_METHOD, JnjCategoryFacadeImpl.class);
		return currentCategoryUpdated;
	}
}