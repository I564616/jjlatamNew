package com.jnj.gt.service.product.impl;

import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Collection;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJGTProductSalesOrgModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductPlantModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.gt.dao.product.JnjGTProductFeedDao;
import com.jnj.gt.model.JnJGTIntProductLocalPlantModel;
import com.jnj.gt.model.JnJGTIntProductPlantModel;
import com.jnj.gt.model.JnjGTIntProductDescModel;
import com.jnj.gt.model.JnjGTIntProductKitModel;
import com.jnj.gt.model.JnjGTIntProductModel;
import com.jnj.gt.model.JnjGTIntProductRegModel;
import com.jnj.gt.model.JnjGTIntProductSalesOrgModel;
import com.jnj.gt.model.JnjGTIntProductUnitModel;
import com.jnj.gt.service.product.JnjGTProductFeedService;


/**
 * Service layer class responsible for defining APIs related to Product feed.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTProductFeedService implements JnjGTProductFeedService
{

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTProductFeedService.class);

	/**
	 * Instance of <code>CompanyB2BCommerceService</code>
	 */
	@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;

	/**
	 * Instance of <code>JnJGTProductService</code>
	 */
	@Resource(name = "productService")
	JnJGTProductService jnjGTProductService;


	/**
	 * Instance of <code>ModelService</code>
	 */
	@Autowired
	private ModelService modelService;


	/**
	 * Instance of <code>JnjGTProductFeedDao</code>
	 */
	@Autowired
	private JnjGTProductFeedDao jnjGTProductFeedDao;


	/**
	 * Instance of <code>FlexibleSearchService</code>
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Instance of <code>CommonI18NService</code>
	 */
	@Autowired
	private CommonI18NService commonI18NService;


	private static final String VARIANT_TYPE = "JnjGTVariantProduct";

	@Override
	public void saveItem(final ItemModel item) throws BusinessException
	{
		try
		{
			getModelService().save(item);
		}
		catch (final ModelSavingException e)
		{
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void saveItems(final Collection<? extends ItemModel> items) throws BusinessException
	{
		try
		{
			getModelService().saveAll(items);
		}
		catch (final ModelSavingException e)
		{
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public ItemModel createNewItem(final Class modelClass)
	{
		return getModelService().create(modelClass);
	}

	@Override
	public JnJProductModel getProductByCode(final String code, final CatalogVersionModel catalogVersion)
	{
		try
		{
			final JnJProductModel product = (JnJProductModel) getJnjGTProductService().getProductForCode(catalogVersion, code);
			return product;
		}
		catch (final UnknownIdentifierException exception)
		{
			return null;
		}
	}

	@Override
	public List<JnjGTIntProductDescModel> getIntProductDescRecordsByProductSkuCode(final String typeCode,
			final String productCode, final String sourceSysId)
	{
		return getJnjGTProductFeedDao().getIntProductDescRecords(productCode, sourceSysId);
	}

	@Override
	public List<JnjGTIntProductKitModel> getIntProductKitRecordsByProductSkuCode(final String typeCode, final String productCode,
			final String sourceSysId)
	{
		return (List<JnjGTIntProductKitModel>) getJnjGTProductFeedDao().getIntRecordsByProductSkuCode(typeCode, productCode,
				sourceSysId);
	}

	@Override
	public List<JnjGTIntProductRegModel> getIntProductRegRecordsByProductSkuCode(final String typeCode, final String productCode,
			final String sourceSysId)
	{
		return getJnjGTProductFeedDao().getIntProductRegionRecords(productCode, sourceSysId);
	}

	@Override
	public List<JnjGTIntProductUnitModel> getIntProductUnitsRecordsByProductSkuCode(final String productCode,
			final String regionCode, final boolean isMddProduct, final String sourceSysId)
	{
		if (regionCode != null)
		{
			return (List<JnjGTIntProductUnitModel>) getJnjGTProductFeedDao().getIntProductUnitRecords(productCode, regionCode,
					isMddProduct);
		}
		else
		{
			return (List<JnjGTIntProductUnitModel>) getJnjGTProductFeedDao().getIntRecordsByProductSkuCode(
					JnjGTIntProductUnitModel._TYPECODE, productCode, sourceSysId);
		}
	}

	@Override
	public List<JnJGTIntProductPlantModel> getIntProductPlantRecordsByProductSkuCode(final String typeCode,
			final String productCode, final String sourceSysId)
	{
		return (List<JnJGTIntProductPlantModel>) getJnjGTProductFeedDao().getIntRecordsByProductSkuCode(typeCode, productCode,
				sourceSysId);
	}

	@Override
	public List<JnjGTIntProductSalesOrgModel> getIntProductSalesOrgRecordsByProductSkuCode(final String typeCode,
			final String productCode, final String sourceSysId)
	{
		return (List<JnjGTIntProductSalesOrgModel>) getJnjGTProductFeedDao().getIntRecordsByProductSkuCode(typeCode, productCode,
				sourceSysId);
	}

	@Override
	public CountryModel getCountryByIso(final String iscoCode)
	{
		return commonI18NService.getCountry(iscoCode);
	}

	@Override
	public UnitModel getUnitByCode(final String code) throws BusinessException
	{
		try
		{
			return getJnjGTProductService().getUnitByCode(code);
		}
		catch (final ModelNotFoundException exception)
		{
			return null;
		}
		catch (final AmbiguousIdentifierException exception)
		{
			throw new BusinessException(exception.getMessage());
		}
	}

	@Override
	public void removeItems(final Collection<? extends ItemModel> items) throws BusinessException
	{
		try
		{
			getModelService().removeAll(items);
		}
		catch (final ModelRemovalException e)
		{
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Collection<JnjGTIntProductModel> getIntMaterialBaseProducts(final boolean isBaseProductSearch)
	{
		return getJnjGTProductFeedDao().getIntMaterialBaseProducts(isBaseProductSearch);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public Collection<JnjGTIntProductModel> getIntCanadaProducts()
	{
		return getJnjGTProductFeedDao().getIntCanadaProducts();
	}

	@Override
	public void setJnjGTVariantType(final JnJProductModel JnJProductModel) throws BusinessException
	{
		final VariantTypeModel exampleVariantType = new VariantTypeModel();
		exampleVariantType.setCode(VARIANT_TYPE);

		final VariantTypeModel variantType = getFlexibleSearchService().getModelByExample(exampleVariantType);
		JnJProductModel.setVariantType(variantType);
		saveItem(JnJProductModel);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnJProductModel> getJnJProductModelsUsingMaterialBaseProductPK(final String materialBaseProdPk,
			final CatalogVersionModel catalogVersionModel)
	{
		return getJnjGTProductFeedDao().getJnJProductModelsUsingMaterialBaseProductPK(materialBaseProdPk, catalogVersionModel);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnJProductModel> getProductModelForExclusionFeed(final CatalogVersionModel catalogVersionModel,
			final boolean isCallForPCM)
	{
		return getJnjGTProductFeedDao().getProductModelForExclusionFeed(catalogVersionModel, isCallForPCM);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<String> getProductCodesUsingIntProductModel(final String materialBaseNum)
	{
		return getJnjGTProductFeedDao().getProductCodesUsingIntProductModel(materialBaseNum);
	}

	@Override
	public Collection<JnJGTIntProductLocalPlantModel> getIntProdPlantLocalRecords(final String productCode,
			final String srcSystemId)
	{
		return getJnjGTProductFeedDao().getIntProdPlantLocalRecords(productCode, srcSystemId);
	}


	@Override
	public JnjGTVariantProductModel getDeliveryGtinEnabledVariant(final JnJProductModel product)
	{
		return jnjGTProductService.getDeliveryGTIN(product);
	}

	@Override
	public JnjGTProductPlantModel getExistingProductPlantRecord(final String productCode)
	{
		return jnjGTProductFeedDao.getExistingProductPlantRecord(productCode);
	}

	@Override
	public JnJGTProductSalesOrgModel getExistingProductSalesOrgRecord(final String productSalesOrgCode)
	{
		return jnjGTProductFeedDao.getExistingProductSalesOrgRecord(productSalesOrgCode);
	}

	@Override
	public CategoryModel getcategoryByCustomName(final CatalogVersionModel catalogVersion, final String customName)
			throws UnknownIdentifierException
	{
		final CategoryModel category = getJnjGTProductFeedDao().getcategoryByCustomName(catalogVersion, customName);
		if (category == null)
		{
			throw new UnknownIdentifierException("CATEGORY WITH NAME: " + customName + " DOES NOT EXIST IN CATALOG VERSION: "
					+ catalogVersion);
		}
		return category;
	}

	public CompanyB2BCommerceService getCompanyB2BCommerceService()
	{
		return companyB2BCommerceService;
	}

	public JnJGTProductService getJnjGTProductService()
	{
		return jnjGTProductService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public JnjGTProductFeedDao getJnjGTProductFeedDao()
	{
		return jnjGTProductFeedDao;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

}
