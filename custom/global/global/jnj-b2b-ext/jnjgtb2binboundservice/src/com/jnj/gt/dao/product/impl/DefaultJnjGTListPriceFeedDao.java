package com.jnj.gt.dao.product.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.gt.dao.product.JnjGTListPriceFeedDao;
import com.jnj.gt.model.JnjGTIntListPriceAmtModel;


/**
 * @author dheeraj.e.sharma
 * 
 */
public class DefaultJnjGTListPriceFeedDao implements JnjGTListPriceFeedDao
{
	@Autowired
	FlexibleSearchService flexibleSearchService;

	//TOD: Add catalogId & catalog version with the query and add these catalogId key in project.properties
	private final String VARIANT_QUERY_FOR_MDD = "SELECT {variant:pk} FROM {JnjGTVariantProduct as variant JOIN JnJProduct as product ON {variant:baseProduct}={product:pk} "
			+ "JOIN Unit as uom on {variant:unit}={uom:pk}} where {uom:code}=?unit and {product:code} = ?code and {product:catalogVersion}=?catalogVersion";
	private final String VARIANT_QUERY_FOR_CONSUMER = "SELECT {variant:pk} FROM {JnjGTVariantProduct as variant JOIN JnJProduct as product ON "
			+ "{variant:baseProduct}={product:pk} JOIN Unit as uom on {uom:pk}={variant:unit} } where {uom:code}=?unit and  ({product:code}=?code"
			+ " OR ( {product:materialBaseProduct} is not null  AND { product:materialBaseProduct} in ( {{ select {baseProduct:pk} from {JnJProduct as baseProduct} where {baseProduct:code}= ?code }} ) ) ) and {product:catalogVersion}=?catalogVersion";
	private final String LIST_PRICE_AMOUNT_QUERY = "SELECT {pk} FROM {JnjGTIntListPriceAmt} where {listPriceID}=?listPriceID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.company.product.JnjGTListPriceFeedDao#getJnjGTVariantProductsByUomAndBaseCode(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Collection<JnjGTVariantProductModel> getProductByUom(final String code, final String unit, final String srcSysId,
			final String stgCatalog)
	{
		StringBuilder searchQuery = new StringBuilder();
		if (srcSysId.equalsIgnoreCase(JnjGTSourceSysId.MDD.toString()))
		{
			searchQuery = new StringBuilder(VARIANT_QUERY_FOR_MDD);
		}
		else if (srcSysId.equalsIgnoreCase(JnjGTSourceSysId.CONSUMER.toString()))
		{
			searchQuery = new StringBuilder(VARIANT_QUERY_FOR_CONSUMER);
		}


		final Map<String, String> queryParams = new HashMap<>();

		queryParams.put(JnjGTVariantProductModel.UNIT, unit);
		queryParams.put(JnJProductModel.CODE, code);
		queryParams.put(JnJProductModel.CATALOGVERSION, stgCatalog);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);

		flexibleSearchQuery.addQueryParameters(queryParams);

		final List<JnjGTVariantProductModel> result = getFlexibleSearchService().<JnjGTVariantProductModel> search(
				flexibleSearchQuery).getResult();

		return result;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.company.product.JnjGTListPriceFeedDao#getListPriceAmountRecordsByListPriceId(java.lang.String)
	 */
	@Override
	public Collection<JnjGTIntListPriceAmtModel> getListPriceAmountRecordsByListPriceId(final String listPriceID)
	{

		StringBuilder searchQuery = new StringBuilder();

		searchQuery = new StringBuilder(LIST_PRICE_AMOUNT_QUERY);

		final Map<String, String> queryParams = new HashMap<>();

		queryParams.put("listPriceID", listPriceID);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);

		final List<JnjGTIntListPriceAmtModel> result = getFlexibleSearchService().<JnjGTIntListPriceAmtModel> search(
				flexibleSearchQuery).getResult();

		return result;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
