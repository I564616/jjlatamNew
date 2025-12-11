/**
 * 
 */
package com.jnj.gt.dao.product;

import java.util.Collection;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.gt.model.JnjGTIntCpscContactDetailModel;
import com.jnj.gt.model.JnjGTIntCpscTestDetailModel;


/**
 * The Interface jnjGTCpscFeedDao.
 * 
 * @author akash.rawat
 */
public interface JnjGTCpscFeedDao
{

	/**
	 * Returns Collection of CPSC Test Details based on the Product code and lot number.
	 * 
	 * @param productSkuCode
	 *           the product sku code
	 * @param lotNumber
	 *           the lot number
	 * @return the cpsc test detail by product code and lot number
	 */
	public Collection<JnjGTIntCpscTestDetailModel> getCpscTestDetailByProductCodeAndLotNumber(final String productSkuCode,
			final String lotNumber);

	/**
	 * Returns Collection of CPSC Contact Details based on the Product code and lot number.
	 * 
	 * @param productSkuCode
	 *           the product sku code
	 * @param lotNumber
	 *           the lot number
	 * @return the cpsc contact detail by product code and lot number
	 */
	public Collection<JnjGTIntCpscContactDetailModel> getCpscContactDetailByProductCodeAndLotNumber(final String productSkuCode,
			final String lotNumber);

	/**
	 * Returns Collection of JNJ NA PRODUCT MODEL on the basis of Product code and Lot Number.
	 * 
	 * @param code
	 *           the code
	 * @return the product by code and lot number
	 */
	public JnJProductModel getProductByCode(String code);

	/**
	 * Gets the existing cpsc details.
	 * 
	 * @return the existing cpsc details
	 */
	public Collection<JnjGTProductCpscDetailModel> getExistingCpscDetails();

}
