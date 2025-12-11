package com.jnj.gt.dao.product;

import java.util.Collection;

import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.gt.model.JnjGTIntListPriceAmtModel;


/**
 * @author dheeraj sharma
 * 
 */
public interface JnjGTListPriceFeedDao
{

	/**
	 * fetch the list of JnjGTVariantProductModel on the basis of uomCode and baseCode of base Product
	 * 
	 * @param productBaseCode
	 * @param lineUomCode
	 * @return
	 */
	Collection<JnjGTVariantProductModel> getProductByUom(String code, String unit, String srcSysId, String stgCatalog);

	/**
	 * @param listPriceID
	 * @return
	 */
	Collection<JnjGTIntListPriceAmtModel> getListPriceAmountRecordsByListPriceId(String listPriceID);

}
