/**
 *
 */
package com.jnj.core.dao.territory.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jnj.core.dao.territory.JnjGTTerritoryDao;
import com.jnj.core.model.JnjGTTerritoryDivAddressesModel;
import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;



public class DefaultJnjGTTerritoryDao extends AbstractItemDao implements JnjGTTerritoryDao
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.territory.JnjGTTerritoryDao#getTerritoryDivisonByUid(java.lang.String)
	 */
	@Override
	public JnjGTTerritoryDivisonModel getTerritoryDivisonByUid(final String id, final String sourceSystemId)
	{
		JnjGTTerritoryDivisonModel jnjGTTerritoryDivison = null;
		try
		{
			final Map queryParams = new HashMap();
			queryParams.put("id", id);
			queryParams.put("sourceSystemId", sourceSystemId);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
					"select {pk} from {JnjGTTerritoryDivison} where {uid}=?id  and {sourceSystemId}=?sourceSystemId");
			fQuery.addQueryParameters(queryParams);
			jnjGTTerritoryDivison = (JnjGTTerritoryDivisonModel) getFlexibleSearchService().searchUnique(fQuery);
		}
		catch (final ModelNotFoundException e)
		{
			jnjGTTerritoryDivison = null;

		}
		return jnjGTTerritoryDivison;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.territory.JnjGTTerritoryDao#getAllJnjGTTerritoryDivison()
	 */
	@Override
	public List<JnjGTTerritoryDivisonModel> getAllJnjGTTerritoryDivison(final String sourceSystemId)
	{
		List<JnjGTTerritoryDivisonModel> territoryDivisonModelList = new ArrayList<JnjGTTerritoryDivisonModel>();
		final Map queryParams = new HashMap();
		queryParams.put("sourceSystem", sourceSystemId);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"select {pk} from {JnjGTTerritoryDivison} where {sourceSystemId}=?sourceSystem");
		fQuery.addQueryParameters(queryParams);
		final SearchResult<JnjGTTerritoryDivisonModel> result = getFlexibleSearchService().search(fQuery);
		territoryDivisonModelList = result.getResult();
		return territoryDivisonModelList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.territory.JnjGTTerritoryDao#getAllJnjGTTerritoryDivCustRelModel()
	 */
	@Override
	public List<JnjGTTerritoryDivCustRelModel> getAllJnjGTTerritoryDivCustRelModel()
	{
		List<JnjGTTerritoryDivCustRelModel> territoryDivCustModelList = new ArrayList<JnjGTTerritoryDivCustRelModel>();
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("select {pk} from {JnjGTTerritoryDivCustRel}");
		final SearchResult<JnjGTTerritoryDivCustRelModel> result = getFlexibleSearchService().search(fQuery);
		territoryDivCustModelList = result.getResult();
		return territoryDivCustModelList;
	}


	@Override
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRelWithNullTarget(final PK territoryDivPk, final String wwid,
			final String ucn)
	{
		List<JnjGTTerritoryDivCustRelModel> territoryDivisonCustModelList = new ArrayList<JnjGTTerritoryDivCustRelModel>();
		final Map queryParams = new HashMap();
		queryParams.put("territoryDivPk", territoryDivPk);
		final StringBuilder query = new StringBuilder(
				"select {pk} from {JnjGTTerritoryDivCustRel} where {source}=?territoryDivPk and  {target} IS NULL");
		final String wwidNull = " and {wwid} is null";
		final String wwidCondition = " and {wwid}=?wwid";
		final String ucnNull = " and {uniqueCustomer} is null";
		final String ucnCondition = " and {uniqueCustomer}=?ucn";

		if (StringUtils.isEmpty(wwid))
		{
			query.append(wwidNull);
		}
		else
		{
			query.append(wwidCondition);
			queryParams.put("wwid", wwid);
		}

		if (StringUtils.isEmpty(ucn))
		{
			query.append(ucnNull);
		}
		else
		{
			query.append(ucnCondition);
			queryParams.put("ucn", ucn);
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);


		fQuery.addQueryParameters(queryParams);
		final SearchResult<JnjGTTerritoryDivCustRelModel> result = getFlexibleSearchService().search(fQuery);
		territoryDivisonCustModelList = result.getResult();
		return territoryDivisonCustModelList;
	}


	@Override
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRelWithNullTarget()
	{
		List<JnjGTTerritoryDivCustRelModel> territoryDivisonCustModelList = new ArrayList<JnjGTTerritoryDivCustRelModel>();
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"select {pk} from {JnjGTTerritoryDivCustRel} where {target} IS NULL");
		final SearchResult<JnjGTTerritoryDivCustRelModel> result = getFlexibleSearchService().search(fQuery);
		territoryDivisonCustModelList = result.getResult();
		return territoryDivisonCustModelList;
	}


	@Override
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRelWithWwidAndNullTarget(final String wwid)
	{
		List<JnjGTTerritoryDivCustRelModel> territoryDivisonCustModelList = new ArrayList<JnjGTTerritoryDivCustRelModel>();
		final Map queryParams = new HashMap();
		queryParams.put("wwid", wwid);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"select {pk} from {JnjGTTerritoryDivCustRel} where {target} IS NULL and {wwid}=?wwid");
		fQuery.addQueryParameters(queryParams);
		final SearchResult<JnjGTTerritoryDivCustRelModel> result = getFlexibleSearchService().search(fQuery);
		territoryDivisonCustModelList = result.getResult();
		return territoryDivisonCustModelList;
	}

	@Override
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryNCustomerRels(final Set<String> terriotyPKs)
	{
		final Map queryParams = new HashMap(1);
		List<JnjGTTerritoryDivCustRelModel> territoryDivCustModelList = new ArrayList<JnjGTTerritoryDivCustRelModel>();
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"select {pk} from {JnjGTTerritoryDivCustRel} where {source} in (?source) and {uniqueCustomer} is not null and {orgTypeCd}='T' Order by {uniqueCustomer}");
		queryParams.put("source", terriotyPKs);
		fQuery.addQueryParameters(queryParams);
		final SearchResult<JnjGTTerritoryDivCustRelModel> result = getFlexibleSearchService().search(fQuery);
		territoryDivCustModelList = result.getResult();
		return territoryDivCustModelList;
	}
	
}
