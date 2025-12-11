/**
 * 
 */
package com.jnj.gt.dao.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;
import java.util.List;

import com.jnj.core.model.JnJGTProductSalesOrgModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductPlantModel;
import com.jnj.gt.model.JnJGTIntProductLocalPlantModel;
import com.jnj.gt.model.JnjGTIntProductDescModel;
import com.jnj.gt.model.JnjGTIntProductModel;
import com.jnj.gt.model.JnjGTIntProductRegModel;
import com.jnj.gt.model.JnjGTIntProductUnitModel;


/**
 * @author akash.rawat
 * 
 */
public interface JnjGTProductFeedDao
{

	/**
	 * @param typeCode
	 * @param productCode
	 * @return
	 */
	public Collection<? extends ItemModel> getIntRecordsByProductSkuCode(String typeCode, String productCode,
			final String sourceSysId);

	/**
	 * Retrieves Product intermediate records based on the Base MAterial Number.
	 * 
	 * @param isBaseProductSearch
	 * @return Collection<JnjGTIntProductModel>
	 */
	public Collection<JnjGTIntProductModel> getIntMaterialBaseProducts(final boolean isBaseProductSearch);

	/**
	 * Retrieves intermediate records based on the region code = 'USA'.
	 * 
	 * @param productCode
	 * @return Collection<JnjGTProductRegModel>
	 */
	public List<JnjGTIntProductRegModel> getIntProductRegionRecords(final String productCode, final String sourceSysId);

	/**
	 * Retrieves intermediate records based on the Language code = 'EN'.
	 * 
	 * @param productCode
	 * @return List<JnjGTIntProductDescModel>
	 */
	public List<JnjGTIntProductDescModel> getIntProductDescRecords(final String productCode, final String sourceSysId);

	/**
	 * Gets the jnj na product models using material base product pk.
	 * 
	 * @param materialBaseProPk
	 *           the material base pro pk
	 * @param catalogVersionModel
	 *           the catalog version model
	 * @return the jnj na product models using material base product pk
	 */
	public List<JnJProductModel> getJnJProductModelsUsingMaterialBaseProductPK(final String materialBaseProPk,
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
	 * Fetches Intermediate Product Unit records based on matching product sku code and region code (only for MDD).
	 * 
	 * @param productCode
	 * @param regionCode
	 * @param isMddProduct
	 * @return Collection<JnjGTIntProductUnitModel>
	 */
	public Collection<JnjGTIntProductUnitModel> getIntProductUnitRecords(final String productCode, final String regionCode,
			final boolean isMddProduct);

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
