/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.core.dao.company.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJB2BUnitModel;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class JnjLatamPagedB2BCustomerDaoImpl extends DefaultJnjGTPagedB2BCustomerDao {

    public JnjLatamPagedB2BCustomerDaoImpl(final String typeCode) {
        super(typeCode);
    }
    
    private static final Logger LOG = Logger.getLogger(JnjLatamPagedB2BCustomerDaoImpl.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Resource(name = "jnjB2BUnitService")
    private JnjGTB2BUnitService jnjGTB2BUnitService;

    @Override
    public SearchPageData<B2BCustomerModel> findPagedCustomers(final JnjGTPageableData pageableData) {

        final Map<String, Object> queryParams = new HashMap<>();
        final String sortByCriteria = pageableData.getSort();
        final String query = Jnjlab2bcoreConstants.UserSearch.FIND_B2B_ALL_CUSTOMERS;
        
        final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) getUserService().getCurrentUser();
        final JnJB2BUnitModel currentB2BUnit = (JnJB2BUnitModel) currentUser.getCurrentB2BUnit();

        if (sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE).equals(Jnjb2bCoreConstants.UserSearch.USER_TIER1)) {
            queryParams.put(Jnjb2bCoreConstants.UserSearch.USERGROUPS, JnJCommonUtil.getValues(Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER1, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));
        } else {
            queryParams.put(Jnjb2bCoreConstants.UserSearch.USERGROUPS, JnJCommonUtil.getValues(Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER2, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));
        }
        
        if(currentB2BUnit!=null){
        	queryParams.put(Jnjlab2bcoreConstants.CURRENT_B2B_UNIT, currentB2BUnit.getUid());
        }
        final List<SortQueryData> sortQueriesT = Arrays.asList(
            createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_NAME, query + Jnjb2bCoreConstants.UserSearch.ORDER_BY_NAME),
            createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_ROLE, query + Jnjb2bCoreConstants.UserSearch.ORDER_BY_ROLE),
            createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_STATUS, query + Jnjb2bCoreConstants.UserSearch.ORDER_BY_STATUS));
                
        for(SortQueryData sortQuery:sortQueriesT){
        	 LOG.info("Query to find customers #################:" + sortQuery.getQuery());
        	 
        }
        LOG.info("Fainal query parameters: #################:" + queryParams);
        
        return sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public SearchPageData execute() {
                return getPagedFlexibleSearchService().search(sortQueriesT, sortByCriteria, queryParams, pageableData);
            }
        }, userService.getAdminUser());

    }

}
