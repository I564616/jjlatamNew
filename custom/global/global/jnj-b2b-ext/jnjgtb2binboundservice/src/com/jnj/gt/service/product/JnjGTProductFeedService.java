package com.jnj.gt.service.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.product.UnitModel;

import java.util.Collection;
import java.util.List;

import com.jnj.core.model.JnJGTProductSalesOrgModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductPlantModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.model.JnJGTIntProductLocalPlantModel;
import com.jnj.gt.model.JnJGTIntProductPlantModel;
import com.jnj.gt.model.JnjGTIntProductDescModel;
import com.jnj.gt.model.JnjGTIntProductKitModel;
import com.jnj.gt.model.JnjGTIntProductModel;
import com.jnj.gt.model.JnjGTIntProductRegModel;
import com.jnj.gt.model.JnjGTIntProductSalesOrgModel;
import com.jnj.gt.model.JnjGTIntProductUnitModel;


/**
 * @author akash
 * 
 */
public interface JnjGTProductFeedService
{
	/**
	 * 
	 * @param item
	 */
	public void saveItem(final ItemModel item) throws BusinessException;

	/**
	 * 
	 * @param items
	 */
	public void saveItems(Collection<? extends ItemModel> items) throws BusinessException;

	/**
	 * 
	 * @param modelClass
	 * @return
	 */
	public <T> T createNewItem(Class modelClass);

	/**
	 * 
	 * @param code
	 * @return
	 */
	public JnJProductModel getProductByCode(final String code, final CatalogVersionModel catalogVersion);

	/**
	 * 
	 * @param iscoCode
	 * @return
	 */
	public CountryModel getCountryByIso(final String iscoCode);

	/**
	 * @param baseUom
	 * @return
	 */
	public UnitModel getUnitByCode(String code) throws BusinessException;

	/**
	 * 
	 * @param items
	 * @throws BusinessException
	 */
	public void removeItems(final Collection<? extends ItemModel> items) throws BusinessException;

	public List<JnjGTIntProductDescModel> getIntProductDescRecordsByProductSkuCode(final String typeCode,
			final String productCode, final String sourceSysId);

	public List<JnjGTIntProductKitModel> getIntProductKitRecordsByProductSkuCode(final String typeCode, final String productCode,
			final String sourceSysId);

	public List<JnjGTIntProductRegModel> getIntProductRegRecordsByProductSkuCode(final String typeCode, final String productCode,
			final String sourceSysId);

	public List<JnjGTIntProductUnitModel> getIntProductUnitsRecordsByProductSkuCode(final String productCode,
			final String regionCode, final boolean isMddProduct, final String sourceSysId);

	public List<JnJGTIntProductPlantModel> getIntProductPlantRecordsByProductSkuCode(final String typeCode,
			final String productCode, final String sourceSysId);

	public List<JnjGTIntProductSalesOrgModel> getIntProductSalesOrgRecordsByProductSkuCode(final String typeCode,
			final String productCode, final String sourceSysId);

	public Collection<JnjGTIntProductModel> getIntMaterialBaseProducts(final boolean isBaseProductSearch);

	public void setJnjGTVariantType(JnJProductModel JnJProductModel) throws BusinessException;

	/**
	 * Gets the jnj na product models using material base product pk.
	 * 
	 * @param materialBaseProdPk
	 *           the material base no pk
	 * @param catalogVersionModel
	 *           the catalog version model
	 * @return the jnj na product models using material base product pk
	 */
	public List<JnJProductModel> getJnJProductModelsUsingMaterialBaseProductPK(final String materialBaseProdPk,
			final CatalogVersionModel catalogVersionModel);

	/**
	 * Gets the product model for exclusion feed.
	 * 
	 * @param catalogVersionModel
	 *           the catalog version model
	 * @return the product model for exclusion feed
	 */
	public List<JnJProductModel> getProductModelForExclusionFeed(final CatalogVersionModel catalogVersionModel,
			final boolean isCallForPCM);

	/**
	 * Fetches variant of the product which has delivery GTIN flag set for it.
	 * 
	 * @param product
	 * @return JnjGTVariantProductModel
	 */
	public JnjGTVariantProductModel getDeliveryGtinEnabledVariant(final JnJProductModel product);

	/**
	 * Fetches Intermediate Product Plant local records by product code and source system id.
	 */
	public Collection<JnJGTIntProductLocalPlantModel> getIntProdPlantLocalRecords(final String productCode, String srcSystemId);

	/**
	 * Get all those existing product plant records which are not associated with Products anymore.
	 * 
	 * @param productCode
	 * @return JnjGTProductPlantModel
	 */
	public JnjGTProductPlantModel getExistingProductPlantRecord(final String productCode);

	/**
	 * Get all those existing product Sales Org records which are not associated with Products anymore.
	 * 
	 * @param productSalesOrgCode
	 * @return JnJGTProductSalesOrgModel
	 */
	public JnJGTProductSalesOrgModel getExistingProductSalesOrgRecord(final String productSalesOrgCode);

	/**
	 * Gets the product model for exclusion feed.
	 * 
	 * @param material
	 *           base number String
	 * @return list of String containing product status code
	 */
	public List<String> getProductCodesUsingIntProductModel(final String materialBaseNum);

	/**
	 * Gets the intermediate canada products.
	 * 
	 * @return list of product model.
	 */
	public Collection<JnjGTIntProductModel> getIntCanadaProducts();

	/**
	 * CONSUMER ONLY | Find existing category by name and catalog version.
	 * 
	 * @param catalogVersion
	 * @param customName
	 * @return CategoryModel
	 */
	public CategoryModel getcategoryByCustomName(final CatalogVersionModel catalogVersion, final String customName);
}
