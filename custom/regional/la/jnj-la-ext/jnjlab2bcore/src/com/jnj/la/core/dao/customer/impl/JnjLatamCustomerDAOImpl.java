/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.dao.customer.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.customer.impl.DefaultJnjGTCustomerDAO;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JnjLatamCustomerDAOImpl extends DefaultJnjGTCustomerDAO {

    private static final String ACCOUNT_SELECT_UNION_START = "SELECT uniontable.PK FROM ({{";

    private static final String ACCOUNT_SELECT_UNION_1 = "SELECT {b2bunit:pk} AS PK ,{b2bunitrelation:target} AS Target " + "from {JnJB2bCustomer As customer  JOIN  PrincipalGroupRelation  AS b2bunitrelation ON {b2bunitrelation:source} = {customer:pk} " + "JOIN JnJLaB2BUnit AS b2bunit ON {b2bunit:pk} = {b2bunitrelation:target}} where {customer:uid}=?currentUserUid ";

    private static final String ACCOUNT_SELECT_UNION_KEYWORD = "}} UNION {{";

    private static final String ACCOUNT_SELECT_UNION_2 = "SELECT {b2bunit:pk} AS PK , {territoryCustRel:target} AS Target " + "from {JnJB2bCustomer As customer  JOIN   PrincipalGroupRelation AS userRel ON {userRel:source} = {customer:pk} " + "JOIN JnjGTTerritoryDivCustRel AS territoryCustRel ON {territoryCustRel:source} = {userRel:target} " + "JOIN JnJLaB2BUnit AS b2bunit ON {b2bunit:pk}={territoryCustRel:target}} where {customer:uid}=?currentUserUid ";

    private static final String ACCOUNT_SELECT_UNION_END = "}}) uniontable";

    private static final String GET_AND_SEARCH_NESTED_ACCOUNTS = "SELECT {b2bunit:pk} from {JnJLaB2BUnit AS b2bunit} " + "where {b2bunit:pk} in ({{SELECT {groupb2bunitrelation:source} AS Target from {Principal As principal  JOIN  PrincipalGroupRelation " + "AS groupb2bunitrelation ON {groupb2bunitrelation:target} = {principal:pk}} where {principal:uid} in (?additionalGroups) " + "and {groupb2bunitrelation:source} not in ({{select {pk} from {JnJB2bCustomer} where {uid} in (?currentUserUid)}})}})";

    private static final String SEARCH_VALID_ACCOUNT_FOR_CUSTOMER = "select {b2bunit:pk} from {JnJLaB2BUnit as b2bunit} " + " where {b2bunit:pk} in ({{select {groupb2bunitrelation:source} as target from {Principal as principal join PrincipalGroupRelation " + " as groupb2bunitrelation ON {groupb2bunitrelation:target} = {principal:pk}} where {principal:uid} in (?groups) " + " and {groupb2bunitrelation:source} not in ({{select {pk} from {JnJB2bCustomer} where {uid} in (?userUid)}})}})" + " and {b2bunit:uid} = ?accountNumber";

    private static final String GET_AND_SEARCH_NESTED_USERGROUPS = "SELECT {target} from {PrincipalGroupRelation} " + "where {source} in ({{SELECT {desiredgrouprelations:target} from {JnJB2bCustomer as b2bcustomer " + "JOIN PrincipalGroupRelation AS desiredgrouprelations ON {desiredgrouprelations:source} = {b2bcustomer:pk} " + "JOIN UserGroup AS desiredgroups ON {desiredgroups:pk} = {desiredgrouprelations:target}} " + "WHERE {desiredgroups:uid} in (?additionalGroups) AND {b2bcustomer:uid} = ?currentUserUid}})";

    private static final String GET_AND_SEARCH_USERGROUPS = "SELECT {desiredgrouprelations:target} from {JnJB2bCustomer as b2bcustomer " + "JOIN PrincipalGroupRelation AS desiredgrouprelations ON {desiredgrouprelations:source} = {b2bcustomer:pk} " + "JOIN UserGroup AS desiredgroups ON {desiredgroups:pk} = {desiredgrouprelations:target}} " + "WHERE {desiredgroups:uid} in (?additionalGroups) AND {b2bcustomer:uid} = ?currentUserUid ";

    private static final int B2B_UNIT_UID_LENGTH = 10;
    private static final String GENERATE_SEARCH_ACCOUNT_QUERY = "generateSearchAccountQuery";
    private static final String ADDITIONAL_GROUPS = "additionalGroups";
    private static final String CURRENT_USER_UID = "currentUserUid";
    private static final String SIZE = "size :: ";
    private static final String RESULTS = " results :: ";

    @Override
    public SearchPageData<JnJB2BUnitModel> getAccountsMap(final boolean isSectorSpecific, final boolean isUCN, final JnjGTPageableData pageableData) {
        final String METHOD_NAME = "getAccountsMap()";
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLatamCustomerDAOImpl.class);

        final StringBuilder query = new StringBuilder();
        final StringBuilder queryPart1 = new StringBuilder();
        final StringBuilder queryPart2 = new StringBuilder();
        final Map<String, String> queryParams = new HashMap<>();

        queryPart1.append(ACCOUNT_SELECT_UNION_START + ACCOUNT_SELECT_UNION_1);
        queryPart2.append(ACCOUNT_SELECT_UNION_KEYWORD + ACCOUNT_SELECT_UNION_2);

        final String queryToAppend = generateSearchAccountQuery(isUCN, isSectorSpecific, queryParams);
        queryPart1.append(queryToAppend);
        queryPart2.append(queryToAppend);

        if (null != pageableData && StringUtils.isNotEmpty(pageableData.getSearchBy())) {
            final String searchBy = pageableData.getSearchBy();
            String subquery;
            if (StringUtils.isNumeric(searchBy)) {
                if (StringUtils.length(searchBy) <= B2B_UNIT_UID_LENGTH) {
                    subquery = " AND ({b2bunit:uid} like ?searchTerm)";
                    queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
                } else {
                    subquery = " AND (LTRIM({b2bunit:globalLocNo},'0')=?searchTerm)";
                    queryParams.put(SEARCH_TERM, StringUtils.stripStart(searchBy, "0"));
                }
            } else {
                subquery = " AND (LOWER({b2bunit:name}) LIKE LOWER(?searchTerm) OR LOWER({b2bunit:uid}) LIKE LOWER(?searchTerm))";
                queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
            }

            queryPart1.append(subquery);
            queryPart2.append(subquery);
        }
        query.append(String.valueOf(queryPart1));
        query.append(String.valueOf(queryPart2));
        query.append(ACCOUNT_SELECT_UNION_END);
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "QUERY :: " + query, JnjLatamCustomerDAOImpl.class);

        queryParams.put(CURRENT_USER_ID_KEY, userService.getCurrentUser().getUid());

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameters(queryParams);
        setQueryLimit(fQuery, pageableData);

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Final Query :: " + query, JnjLatamCustomerDAOImpl.class);
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "query start time :: " + JnJCommonUtil.getCurrentDateTime(), JnjLatamCustomerDAOImpl.class);

        final SearchPageData<JnJB2BUnitModel> searchResults = sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public SearchPageData<JnJB2BUnitModel> execute() {
                return pagedFlexibleSearchService.search(fQuery, pageableData);
            }
        }, userService.getAdminUser());

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "query end time :: " + JnJCommonUtil.getCurrentDateTime(), JnjLatamCustomerDAOImpl.class);
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "searchResults :: " + searchResults.getResults().size(), JnjLatamCustomerDAOImpl.class);
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, JnjLatamCustomerDAOImpl.class);

        return searchResults;
    }

    private void setQueryLimit(final FlexibleSearchQuery fQuery, final JnjGTPageableData pageableData) {
        if (pageableData != null && pageableData.getPageSize() != 0) {
            fQuery.setCount(pageableData.getPageSize());
        }
    }

    private String generateSearchAccountQuery(final boolean isUCN, final boolean isSectorSpecific, final Map<String, String> queryParams) {
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, GENERATE_SEARCH_ACCOUNT_QUERY, Logging.BEGIN_OF_METHOD, JnjLatamCustomerDAOImpl.class);
        String queryToAppend = "";
        String sourceSysId = "";

        if (isSectorSpecific) {
            final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
            if (null != siteName) {
                if (Jnjb2bCoreConstants.CONS.equalsIgnoreCase(siteName)) {
                    sourceSysId = JnjGTSourceSysId.CONSUMER.getCode();
                } else {
                    sourceSysId = JnjGTSourceSysId.MDD.getCode();
                }
            }
        }
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, GENERATE_SEARCH_ACCOUNT_QUERY, "sourceSysId :: " + sourceSysId, JnjLatamCustomerDAOImpl.class);
        if (isUCN) {
            queryParams.put(SALES_REP_INDICATOR_KEY, SALES_REP_INDICATOR_VALUE);
            queryToAppend += ACCOUNT_SELECT_SALES_REP_WHERE;
        }

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, GENERATE_SEARCH_ACCOUNT_QUERY, Logging.END_OF_METHOD, JnjLatamCustomerDAOImpl.class);
        return queryToAppend;
    }

    public SearchPageData<JnJB2BUnitModel> getAdditionalAccounts(final List<String> additionalGroups, final JnjGTPageableData pageableData) {

        final Map<String, Object> queryParams = new HashMap<>();

        queryParams.put(ADDITIONAL_GROUPS, additionalGroups);
        queryParams.put(CURRENT_USER_UID, userService.getCurrentUser().getUid());

        final StringBuilder mainQuery = new StringBuilder(GET_AND_SEARCH_NESTED_ACCOUNTS);

        if (null != pageableData && StringUtils.isNotEmpty(pageableData.getSearchBy())) {
            final String searchBy = pageableData.getSearchBy();
            String subquery;
            if (StringUtils.isNumeric(searchBy)) {
                if (StringUtils.length(searchBy) <= B2B_UNIT_UID_LENGTH) {
                    subquery = " AND ({b2bunit:uid} like ?searchTerm)";
                    queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
                } else {
                    subquery = " AND (LTRIM({b2bunit:globalLocNo},'0')=?searchTerm)";
                    queryParams.put(SEARCH_TERM, StringUtils.stripStart(searchBy, "0"));
                }
            } else {
                subquery = " AND (LOWER({b2bunit:name}) LIKE LOWER(?searchTerm) OR LOWER({b2bunit:uid}) LIKE LOWER(?searchTerm))";
                queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
            }
            mainQuery.append(subquery);

        }
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(mainQuery);
        fQuery.addQueryParameters(queryParams);
        setQueryLimit(fQuery, pageableData);

        final SearchPageData<JnJB2BUnitModel> searchResults = sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public SearchPageData<JnJB2BUnitModel> execute() {
                return pagedFlexibleSearchService.search(fQuery, pageableData);
            }
        }, userService.getAdminUser());

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, "getAdditionalAccounts", SIZE + searchResults.getResults().size() + RESULTS + searchResults.getResults(), JnjLatamCustomerDAOImpl.class);
        return searchResults;
    }


    public SearchPageData<UserGroupModel> getUserGroupsMap(final JnjGTPageableData pageableData, final Set<String> listOfB2bUnits) {
        final Map<String, Object> queryParams = new HashMap<>();

        queryParams.put(ADDITIONAL_GROUPS, listOfB2bUnits);
        queryParams.put(CURRENT_USER_UID, userService.getCurrentUser().getUid());

        final StringBuilder mainQuery = new StringBuilder(GET_AND_SEARCH_USERGROUPS);

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(mainQuery);
        fQuery.addQueryParameters(queryParams);

        final SearchPageData<UserGroupModel> searchResults = sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public SearchPageData<UserGroupModel> execute() {
                return pagedFlexibleSearchService.search(fQuery, pageableData);
            }
        }, userService.getAdminUser());

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, "getUserGroupsMap", SIZE + searchResults.getResults().size() + RESULTS + searchResults.getResults(), JnjLatamCustomerDAOImpl.class);
        return searchResults;
    }

    public SearchPageData<UserGroupModel> getAdditionalUserGroups(final Set<String> additionalGroups, final JnjGTPageableData pageableData) {
        final Map<String, Object> queryParams = new HashMap<>();

        queryParams.put(ADDITIONAL_GROUPS, additionalGroups);
        queryParams.put(CURRENT_USER_UID, userService.getCurrentUser().getUid());

        final StringBuilder mainQuery = new StringBuilder(GET_AND_SEARCH_NESTED_USERGROUPS);

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(mainQuery);
        fQuery.addQueryParameters(queryParams);

        final SearchPageData<UserGroupModel> searchResults = sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public SearchPageData<UserGroupModel> execute() {
                return pagedFlexibleSearchService.search(fQuery, pageableData);
            }
        }, userService.getAdminUser());

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, "getAdditionalUserGroups", SIZE + searchResults.getResults().size() + RESULTS + searchResults.getResults(), JnjLatamCustomerDAOImpl.class);
        return searchResults;
    }

    public JnJB2BUnitModel getValidAccount(final CustomerModel currentCustomer, final Set<String> b2bUnitsFromUser, final String accountNumber) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(SEARCH_VALID_ACCOUNT_FOR_CUSTOMER);
        query.addQueryParameter("groups", b2bUnitsFromUser);
        query.addQueryParameter("userUid", currentCustomer.getUid());
        query.addQueryParameter("accountNumber", accountNumber);
        return sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public JnJB2BUnitModel execute() {
                final List<JnJB2BUnitModel> result = flexibleSearchService.<JnJB2BUnitModel>search(query).getResult();

                if (CollectionUtils.isNotEmpty(result)) {
                    return result.get(0);
                }

                return null;
            }
        }, userService.getAdminUser());
    }
}