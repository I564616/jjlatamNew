/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.b2bunit.impl;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jnj.core.constants.Jnjb2bCoreConstants.B2BUnit;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
//import com.jnj.core.constants.Jnjb2bCoreConstants.B2BUnit;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.b2bunit.JnjGTB2BUnitDao;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTSalesOrgCustomerModel;



/**
 * The JnjGTB2BUnitDaoImpl class contains all those methods which are dealing with customer(B2B) related models.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTB2BUnitDao extends AbstractItemDao implements JnjGTB2BUnitDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTB2BUnitDao.class);
	
	@Autowired
	SessionService sessionService;
	@Autowired
	protected UserService userService;

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public JnjGTSalesOrgCustomerModel getSalesOrgCustomerModel(final String salesOrg, final String distribChannel,
			final String division, final String sourceSysId)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgCustomerModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}


		ServicesUtil.validateParameterNotNull(division, " processed Indentfier must not be null");
		ServicesUtil.validateParameterNotNull(sourceSysId, " processed Indentfier must not be null");
		final Map queryParams = new HashMap();
		String query = B2BUnit.SALES_ORG_QUERY;
		if (StringUtils.isNotEmpty(salesOrg) && StringUtils.isNotEmpty(distribChannel))
		{
			query = query.concat(B2BUnit.SALES_ORG_QUERY_WITH);
			queryParams.put("salesOrg", salesOrg);
			queryParams.put("distributionChannel", distribChannel);
		}
		queryParams.put("division", division);
		queryParams.put("sourceSysId", sourceSysId);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgCustomerModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "SalesOrgCustomerModel Query " + fQuery);
		}

		final List<JnjGTSalesOrgCustomerModel> result = getFlexibleSearchService().<JnjGTSalesOrgCustomerModel> search(fQuery)
				.getResult();
		if (LOGGER.isDebugEnabled())
		{
		LOGGER.debug("getSalesOrgCustomerModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (CollectionUtils.isNotEmpty(result))
		{
			return result.get(0);
		}
		else
		{
			return null;
		}
	}


	@Override
	public JnJB2BUnitModel getB2BUnitByUid(final String uid)
	{
		 JnJB2BUnitModel result = null;
		
		 try
			{
			result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public JnJB2BUnitModel execute()
				{
					
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
							"select {pk} from {JnjB2BUnit as b2bUnit}  where {b2bUnit:uid}='" + uid+"'");
					JnJB2BUnitModel	result =  getFlexibleSearchService().searchUnique(fQuery);
					
					return result;
				}
			}, userService.getAdminUser());
			
			}
		 catch (final ModelNotFoundException | AmbiguousIdentifierException exception)
			{
			 
			 result = null;

			}
			
			
		
	
		return result;
	}
	
	/*@Override
	public JnJB2BUnitModel getJnJB2BUnitByUid(final String uid)
	{
		List<JnJB2BUnitModel> result = new ArrayList<JnJB2BUnitModel>();
		JnJB2BUnitModel jnjGTb2UnitModel = null;
		final Map queryParams = new HashMap();
		String query = "select {pk} from {JnJB2BUnit} where {uid} = ?uid";
		queryParams.put("uid", uid);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnJB2BUnitByUid()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnJB2BUnitByUid Query " + fQuery);
		}
		result = getFlexibleSearchService().<JnJB2BUnitModel>search(fQuery).getResult();	
		return result.get(0);
	}*/
	
	@Override
	public JnJB2BUnitModel getJnJB2BUnitByUid(final String uid)
	{
		
		JnJB2BUnitModel jnjb2UnitModel = new JnJB2BUnitModel();
		jnjb2UnitModel.setUid(uid);
		JnJB2BUnitModel fetchedJnJB2BUnitModel = getFlexibleSearchService().getModelByExample(jnjb2UnitModel);	
		return fetchedJnJB2BUnitModel;
	}

}
