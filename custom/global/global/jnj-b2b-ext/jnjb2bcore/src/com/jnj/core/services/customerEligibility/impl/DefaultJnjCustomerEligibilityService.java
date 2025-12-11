/**
 * 
 */
package com.jnj.core.services.customerEligibility.impl;

import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.customerEligibility.JnjCustomerEligiblityDao;
import com.jnj.core.services.customerEligibility.JnjCustomerEligibilityService;

/**
 * Customer Eligibility Interface Data Load - Service Layer class, responsible for all method calls from
 * <code>JnjCustomerEligiblityDataLoadMapper</code>
 * 
 * @author akash.rawat
 * 
 */
public class DefaultJnjCustomerEligibilityService implements JnjCustomerEligibilityService
{
	/**
	 * The Constant LOGGER.
	 */
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjCustomerEligibilityService.class);

	/**
	 * The constant for "resetIntermediaryRecordStatus" method name.
	 */
	protected static final String RESET_INTERMEDIARY_RECORD_STATUS = "resetIntermediaryRecordStatus()";

	/**
	 * Instance of <code>ModelService</code>
	 */
	protected ModelService modelService;

	/**
	 * Instance of <code>CompanyB2BCommerceService</code>
	 */
	protected B2BCommerceUnitService companyB2BCommerceService;

	/**
	 * Instance of <code>CommerceCategoryService</code>
	 */
	protected CommerceCategoryService commerceCategoryService;

	/**
	 * Instance of <code>CatalogService</code>
	 */
	protected CatalogService catalogService;

	/**
	 * Instance of <code>CatalogVersionService</code>
	 */
	protected CatalogVersionService catalogVersionService;

	/**
	 * Instance of <code>CategoryService</code>
	 */
	protected CategoryService categoryService;

	/**
	 * Instance of <code>JnjCustomerEligiblityDao</code>
	 */
	protected JnjCustomerEligiblityDao customerEligiblityDao;

	/**
	 * Instance of <code>FlexibleSearchService</code>
	 */
	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveItem(final ItemModel item) throws ModelSavingException
	{
		getModelService().save(item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public B2BUnitModel getB2bUnitAccount(final String uid)
	{
		try
		{
			LOGGER.info("B2b unit uid:"+uid);
			LOGGER.info("B2b unit Name:"+getCompanyB2BCommerceService().getUnitForUid(uid));
			return getCompanyB2BCommerceService().getUnitForUid(uid);
		}
		catch (final UnknownIdentifierException e)
		{
			LOGGER.error("B2bUnit could not be found with the provided uid:" + uid);
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CategoryModel getCategoryByCode(final String categoryCode, final String unitCountryCode)
	{
		CategoryModel category = null;
		try
		{
			final String catalogId = Jnjb2bCoreConstants.UpsertProduct.BRAZIL_COUNTRY_CODE.startsWith(unitCountryCode.toUpperCase()) ? Jnjb2bCoreConstants.UpsertProduct.BRAZIL_PRODUCT_CATALOG
					: Jnjb2bCoreConstants.UpsertProduct.MEXICO_PRODUCT_CATALOG;

			final CatalogModel catalogModel = catalogService.getCatalogForId(catalogId);
			final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogModel.getId(),
					Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE);

			category = getCategoryService().getCategoryForCode(catalogVersion, categoryCode);
		}
		catch (final Exception e)
		{
			//LOG
			return null;
		}
		return category;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getRestrictedCategory(final String b2bUnitUid)
	{
		return getCustomerEligiblityDao().getRestrictedCategories(getB2bUnitAccount(b2bUnitUid));
	}



	/**
	 * Utility method used for logging entry into / exit from any method in debug mode.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the companyB2BCommerceService
	 */
	public B2BCommerceUnitService getCompanyB2BCommerceService()
	{
		return companyB2BCommerceService;
	}

	public void setCompanyB2BCommerceService(final B2BCommerceUnitService companyB2BCommerceService)
	{
		this.companyB2BCommerceService = companyB2BCommerceService;
	}

	/**
	 * @return the commerceCategoryService
	 */
	public CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	/**
	 * @return the catalogService
	 */
	public CatalogService getCatalogService()
	{
		return catalogService;
	}

	public void setCatalogService(final CatalogService catalogService)
	{
		this.catalogService = catalogService;
	}

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	/**
	 * @return the categoryService
	 */
	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public JnjCustomerEligiblityDao getCustomerEligiblityDao()
	{
		return customerEligiblityDao;
	}

	public void setCustomerEligiblityDao(final JnjCustomerEligiblityDao customerEligiblityDao)
	{
		this.customerEligiblityDao = customerEligiblityDao;
	}

}
