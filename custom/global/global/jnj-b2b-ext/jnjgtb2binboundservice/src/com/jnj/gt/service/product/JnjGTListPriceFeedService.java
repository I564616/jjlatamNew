package com.jnj.gt.service.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;

import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.model.JnjGTIntListPriceAmtModel;


/**
 * @author dheeraj
 * 
 */
public interface JnjGTListPriceFeedService
{
	/**
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
	 * @return
	 */
	public Collection<JnjGTIntListPriceAmtModel> getListPriceAmountRecordsByListPriceId(String listPriceID);

	public Collection<JnjGTVariantProductModel> getProductByUom(String productSkuCode, String priceUomCode, String srcSysId);

	public CatalogVersionModel getStgCatalogVersionByProduct(final String srcSystemID);
}
