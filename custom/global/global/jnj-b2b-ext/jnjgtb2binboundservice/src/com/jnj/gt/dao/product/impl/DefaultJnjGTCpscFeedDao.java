/**
 * 
 */
package com.jnj.gt.dao.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.gt.dao.product.JnjGTCpscFeedDao;
import com.jnj.gt.model.JnjGTIntCpscContactDetailModel;
import com.jnj.gt.model.JnjGTIntCpscTestDetailModel;


//AR: Provide JAva doc fot this class.
/**
 * The Dao layer class associated with Product CPSIA Feed.
 * 
 * @author t.e.sharma
 */
//AR: Define this bean for application context in jnjnab2binboundservice-spring.xml
public class DefaultJnjGTCpscFeedDao implements JnjGTCpscFeedDao
{
	//AR: Autowire missing?
	/**
	 * The instance of <code>FlexibleSearchService</code>.
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<JnjGTIntCpscTestDetailModel> getCpscTestDetailByProductCodeAndLotNumber(final String productSkuCode,
			final String lotNumber)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		if (null != lotNumber)
		{
			searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntCpscTestDetailModel._TYPECODE)
					.append("} WHERE {").append(JnjGTIntCpscTestDetailModel.PRODUCTSKUCODE).append("} = ?productSkuCode")
					.append(" AND {").append(JnjGTIntCpscTestDetailModel.LOTNUMBER).append("} = ?lotNumber");

			queryParams.put(JnjGTIntCpscTestDetailModel.LOTNUMBER, lotNumber);
		}
		else
		{
			searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntCpscTestDetailModel._TYPECODE)
					.append("} WHERE {").append(JnjGTIntCpscTestDetailModel.PRODUCTSKUCODE).append("} = ?productSkuCode")
					.append(" AND {").append(JnjGTIntCpscTestDetailModel.LOTNUMBER).append("} is null");
		}
		queryParams.put(JnjGTIntCpscTestDetailModel.PRODUCTSKUCODE, productSkuCode);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjGTIntCpscTestDetailModel> result = getFlexibleSearchService().<JnjGTIntCpscTestDetailModel> search(
				flexibleSearchQuery).getResult();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<JnjGTIntCpscContactDetailModel> getCpscContactDetailByProductCodeAndLotNumber(final String productSkuCode,
			final String lotNumber)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		if (null != lotNumber)
		{
			searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntCpscContactDetailModel._TYPECODE)
					.append("} WHERE {").append(JnjGTIntCpscContactDetailModel.PRODUCTSKUCODE).append("} = ?productSkuCode")
					.append(" AND {").append(JnjGTIntCpscContactDetailModel.LOTNUMBER).append("} = ?lotNumber");

			queryParams.put(JnjGTIntCpscContactDetailModel.LOTNUMBER, lotNumber);
		}
		else
		{
			searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntCpscContactDetailModel._TYPECODE)
					.append("} WHERE {").append(JnjGTIntCpscContactDetailModel.PRODUCTSKUCODE).append("} = ?productSkuCode")
					.append(" AND {").append(JnjGTIntCpscContactDetailModel.LOTNUMBER).append("} is null");
		}
		queryParams.put(JnjGTIntCpscContactDetailModel.PRODUCTSKUCODE, productSkuCode);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjGTIntCpscContactDetailModel> result = getFlexibleSearchService().<JnjGTIntCpscContactDetailModel> search(
				flexibleSearchQuery).getResult();

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnJProductModel getProductByCode(final String code)
	{
		validateParameterNotNullStandardMessage("productCode", code);
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnJProductModel._TYPECODE)
				.append("} WHERE {code} = ?code");
		queryParams.put(JnJProductModel.CODE, code);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final JnJProductModel result = (JnJProductModel) getFlexibleSearchService().searchUnique(flexibleSearchQuery);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.company.product.jnjGTCpscFeedDao#getExistingCpscDetails()
	 */
	@Override
	public Collection<JnjGTProductCpscDetailModel> getExistingCpscDetails()
	{
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTProductCpscDetailModel._TYPECODE)
				.append("}");

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		final List<JnjGTProductCpscDetailModel> result = getFlexibleSearchService().<JnjGTProductCpscDetailModel> search(
				flexibleSearchQuery).getResult();

		return result;
	}

	/**
	 * Gets the flexible search service.
	 * 
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
