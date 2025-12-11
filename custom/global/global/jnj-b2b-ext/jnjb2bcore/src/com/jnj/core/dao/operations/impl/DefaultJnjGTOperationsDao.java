/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.operations.impl;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;






//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.operations.JnjGTOperationsDao;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.BroadcastMessageModel;
import com.jnj.core.model.ClassOfTradeGroupModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnJCommonUtil;


/**
 * This class Handles DatBase level interaction : For Operations
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTOperationsDao extends AbstractItemDao implements JnjGTOperationsDao
{

	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService JnJB2BUnitService;
	/**
	 * Instance of <code>UserService</code>
	 */
	@Autowired
	protected UserService userService;

	/**
	 * Instance of <code>SessionService</code>
	 */
	@Autowired
	protected SessionService sessionService;

	public JnjGTB2BUnitService getJnJB2BUnitService() {
		return JnJB2BUnitService;
	}
	

	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}


	public static final String BROADCAST_QUERY = "select {bm:pk},{bm:priority} from {BroadcastMessage as bm} where {bm:pk} in ("
			+ "{{ select {broadCast:pk} from {BroadcastMessage as broadCast join BcastMsgToUnitRelation as rel on {broadCast:pk} ={rel:source} join JnJB2BUnit as bu on {rel:target} = {bu:pk} } "
			+ "where {bu:pk}=?selectedB2BUnit and {broadCast:startDate}<=CONVERT(DATETIME,?currentDate) and {broadCast:endDate}>=CONVERT(DATETIME,?currentDate)}}";

	public static final String BROADCAST_QUERY_CLASS_OF_TRADE = "UNION ALL {{ select {broadCast:pk} from {BroadcastMessage as broadCast join BcastMsgToCTGrpRelation as rel on {broadCast:pk} ={rel:source} join ClassOfTradeGroup as ct on {rel:target} = {ct:pk}} "
			+ " where {ct:pk}=?selectedClassOfTrade and  {broadCast:startDate}<=CONVERT(DATETIME,?currentDate) and {broadCast:endDate}>=CONVERT(DATETIME,?currentDate) }}";

	public static final String BROADCAST_QUERY_MDD = " UNION ALL {{select {broadCast:pk} from {BroadcastMessage as broadCast} where {broadCast:allMDDAccounts}=1 and {broadCast:startDate}<=CONVERT(DATETIME,?currentDate) and {broadCast:endDate}>=CONVERT(DATETIME,?currentDate) }}";

	public static final String BROADCAST_QUERY_CONSUMER = " UNION ALL {{select {broadCast:pk} from {BroadcastMessage as broadCast} where {broadCast:allConsumerAccounts}=1 and {broadCast:startDate}<=CONVERT(DATETIME,?currentDate) and {broadCast:endDate}>=CONVERT(DATETIME,?currentDate) }}";


	public static final String BROADCAST_QUERY_ORDER_BY = " ) order by {bm:priority}";


	@Override
	public List<BroadcastMessageModel> getBroadCastMessages()
	{
		final Map queryParams = new HashMap();
		ClassOfTradeGroupModel selectedClassOfTrade = null;
		String query = BROADCAST_QUERY;
		List<BroadcastMessageModel> broadCastMessageList = new ArrayList<BroadcastMessageModel>();
		final JnJB2BUnitModel currentB2BUnit = JnJB2BUnitService.getCurrentB2BUnit();
		if (currentB2BUnit !=null)
		{
			queryParams.put("selectedB2BUnit", currentB2BUnit.getPk().toString());
			final Set<PrincipalGroupModel> groups = currentB2BUnit.getGroups();
			for (final PrincipalGroupModel group : groups)
			{
				if (group instanceof ClassOfTradeGroupModel)
				{
					selectedClassOfTrade = (ClassOfTradeGroupModel) group;
				}
			}
			if (selectedClassOfTrade != null)
			{
				query = query + BROADCAST_QUERY_CLASS_OF_TRADE;
				queryParams.put("selectedClassOfTrade", selectedClassOfTrade.getPk().toString());
			}
			if (currentB2BUnit.getSourceSysId() != null)
			{
				if (currentB2BUnit.getSourceSysId().equals(Jnjb2bCoreConstants.SalesAlignment.MDD))
				{
					query = query + BROADCAST_QUERY_MDD;
				}
				if (currentB2BUnit.getSourceSysId().equals(Jnjb2bCoreConstants.SalesAlignment.CONSUMER))
				{
					query = query + BROADCAST_QUERY_CONSUMER;
				}
			}
		query = query + BROADCAST_QUERY_ORDER_BY;
		final Calendar today = Calendar.getInstance();
		final Date currentDate = today.getTime();
		//Date currentDate = new Date("12/15/2016");
		
		 SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		 String curr = sdf.format(currentDate);
		queryParams.put("currentDate", curr);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		
			/*final List<BroadcastMessageModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public List<BroadcastMessageModel> execute()
			{
				final List<BroadcastMessageModel> results = getFlexibleSearchService().<BroadcastMessageModel> search(fQuery).getResult();
				return results;
			}
		}, userService.getAdminUser());
		
			broadCastMessageList = result;*/
			
			final SearchResult<BroadcastMessageModel> result = getFlexibleSearchService().search(fQuery);
			broadCastMessageList = result.getResult();
		}
		
		return broadCastMessageList;
	}
}
