/**
 * 
 */
package com.jnj.gt.service.product;

import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;

import com.jnj.core.model.JnJProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.model.JnjGTIntCpscContactDetailModel;
import com.jnj.gt.model.JnjGTIntCpscTestDetailModel;


/**
 * The Interface jnjGTCpscFeedService.
 */
public interface JnjGTCpscFeedService
{

	/**
	 * Save item.
	 * 
	 * @param item
	 *           the item
	 * @throws BusinessException
	 *            the business exception
	 */
	public void saveItem(final ItemModel item) throws BusinessException;

	/**
	 * Creates the new item.
	 * 
	 * @param <T>
	 *           the generic type
	 * @param modelClass
	 *           the model class
	 * @return the t
	 */
	public <T> T createNewItem(Class modelClass);

	/**
	 * Remove item.
	 * 
	 * @param item
	 *           the item
	 * @throws BusinessException
	 *            the business exception
	 */
	public void removeItem(final ItemModel item) throws BusinessException;

	/**
	 * Returns Collection of CPSC Contact Details based on the Product code and lot number.
	 * 
	 * @param productSkuCode
	 *           the product sku code
	 * @param lotNumber
	 *           the lot number
	 * @return the cpsc contact detail by product code
	 */
	public Collection<JnjGTIntCpscContactDetailModel> getCpscContactDetailByProductCodeAndLotNumber(final String productSkuCode,
			final String lotNumber);

	/**
	 * Returns Collection of CPSC Test Details based on the Product code and lot number.
	 * 
	 * @param productSkuCode
	 *           the product sku code
	 * @param lotNumber
	 *           the lot number
	 * @return the cpsc test detail by product code
	 */
	public Collection<JnjGTIntCpscTestDetailModel> getCpscTestDetailByProductCodeAndLotNumber(final String productSkuCode,
			final String lotNumber);

	/**
	 * Clean existing CPSC records.
	 */
	public void cleanExistingCPSCRecords();

	/**
	 * Returns Collection of JNJ NA PRODUCT MODEL on the basis of Product code and Lot Number.
	 * 
	 * @param code
	 *           the code
	 * @return the product by code and lot number
	 */
	public JnJProductModel getProductByCode(final String code);

}
