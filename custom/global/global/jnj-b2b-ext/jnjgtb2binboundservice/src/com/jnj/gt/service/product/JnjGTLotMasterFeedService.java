/**
 * 
 */
package com.jnj.gt.service.product;

import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductLotModel;
import com.jnj.exceptions.BusinessException;


/**
 * @author t.e.sharma
 * 
 */
public interface JnjGTLotMasterFeedService
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
	 * Gets the product by code.
	 * 
	 * @param code
	 * 
	 */
	public JnJProductModel getProductByCode(final String code);

	/**
	 * REmoves the items being passed as params usings <code>ModelService</code>
	 * 
	 * @param items
	 * @throws BusinessException
	 */
	public void removeItems(final Collection<? extends ItemModel> items) throws BusinessException;

	/**
	 * Gets the productLot Model By example.
	 * 
	 * @param code
	 * 
	 */

	public JnjGTProductLotModel getProductLotByExample(final JnjGTProductLotModel productLot);
}
