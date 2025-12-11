/*
 * 
 */
package com.jnj.gt.dao.product;

import java.util.Collection;
import java.util.List;

import com.jnj.core.enums.JnjProductExclusionClassType;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.gt.model.JnjGTIntProductExclusionModel;


/**
 * The JnjGTProductExclusionFeedDao interface contains all those methods which are dealing with product exclusion
 * related intermediate model and it has declaration of all the methods which are defined in the
 * JnjGTProductExclusionFeedDaoImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTProductExclusionFeedDao
{
	/**
	 * Gets the prod exclusion records by using class type, characteristic and material number.
	 * 
	 * @param classType
	 *           the class type
	 * @param characteristic
	 *           the characteristic
	 * @param materialNumber
	 *           the material number
	 * @return the prod exclusion records
	 */
	public Collection<JnjGTIntProductExclusionModel> getProdExclusionRecords(final JnjProductExclusionClassType classType,
			final String characteristic, final String materialNumber);

	/**
	 * Gets the customers for group by using customer group number.
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
