/**
 * 
 */
package com.jnj.gt.service.product;

import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;
import java.util.List;

import com.jnj.core.enums.JnjProductExclusionClassType;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.model.JnjGTIntProductExclusionModel;


/**
 * @author akash
 * 
 */
public interface JnjGTProductExclusionFeedService
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
	 * Gets the prod exclusion records by using class type, characteristic and material number.
	 * 
	 * @param classType
	 *           the class type
	 * @param b2bUnitCode
	 *           the b2b unit code
	 * @param materialNumber
	 *           the material number
	 * @return the prod exclusion records
	 */
	public Collection<JnjGTIntProductExclusionModel> getProdExclusionRecords(final JnjProductExclusionClassType classType,
			final String characteristic, final String materialNumber);

	/**
	 * Removes the all product exclusion records.
	 * 
	 * @throws BusinessException
	 *            the business exception
	 */
	public void removeAllProductExclusionRecords() throws BusinessException;

	/**
	 * Gets the customers for group.
	 * 
	 * @param groupNumber
	 *           the group number
	 * @return the customers for group
	 */
	public Collection<JnJB2BUnitModel> getCustomersForGroup(final String groupNumber);

	/**
	 * Gets the b2b unit codes using the material number.
	 * 
	 * @param materialNumber
	 *           the material number
	 * @return the codes of b2b unit.
	 */
	public List<String> getB2BUnitCodesUsingMaterialNumber(final String materialNumber);
}
