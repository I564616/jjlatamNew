/**
 *
 */
package com.jnj.core.dao.customer.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants.UpsertCustomer;
import com.jnj.core.dao.customer.JnjGTCustomerDAO;
import com.jnj.core.dao.impl.DefaultJnJCustomerDataDao;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;

/**
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTCustomerDAO implements JnjGTCustomerDAO
{

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	protected PagedFlexibleSearchService pagedFlexibleSearchService;

	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected UserService userService;

	protected ConfigurationService configurationService;
	
	/* added for sorting change account records OS-247 */
	final String ACCOUNT_ORDER_BY = "ORDER BY unitName ";



	/** The Constant LOGGER. */
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCustomerDAO.class);

	/** fetch New PCM Users Query **/
	protected static final String NEW_PCM_USERS_QUERY = "SELECT {pk} from {JnJB2bCustomer as b2bcustomer JOIN PrincipalGroupRelation AS desiredgrouprelations ON {desiredgrouprelations:source} ={b2bcustomer:pk} JOIN UserGroup AS desiredgroups ON {desiredgroups:pk} = {desiredgrouprelations:target}} WHERE {desiredgroups:uid} = 'pcmUsersGroup' AND {b2bcustomer:requestAccountIndicator} = 1";

	protected static final String ORDER_STATUS = "orderStatus";
	protected static final String REGISTRATION = "Registration";
	protected static final String CUT_NOTIFICATION_EMAIL = "Cut Notification Email";

	protected final String SEARCH_TERM = "searchTerm";
	protected final String GLOBAL_UNIT_ID = "JnjGlobalUnit";
	protected final String GLOBAL_B2B_UNIT_KEY = "globalB2bUnit";
	protected final String UNIT_SOURCE_SYS_ID_KEY = "sourcesysid";
	protected final String CURRENT_USER_ID_KEY = "currentUserUid";
	protected final String SALES_REP_INDICATOR_KEY = "salesRepIndicator";
	protected final String SALES_REP_INDICATOR_VALUE = "Sales_Rep_Account";
	protected final String CURRENT_UNIT_SOURCE_SYS_ID_KEY = "currentUnitSourceSysId";
	/** Start :: ACCOUNT SELECTION NON ADMIN QUERIES **/
	protected final String ACCOUNT_SELECT_MAIN = "SELECT DISTINCT {b2bunitrelation:source} from {PrincipalGroupRelation AS b2bunitrelation ";
	protected final String ACCOUNT_SELECT_B2B_JOIN = " JOIN JnJB2BUnit AS b2bunit ON {b2bunitrelation:source}={b2bunit:pk} ";
	protected final String ACCOUNT_SELECT_GLOBAL_WHERE = " } WHERE {b2bunitrelation:target}=({{SELECT {pk} from {JnJB2BUnit} WHERE {uid} = ?globalB2bUnit}}) ";
	protected final String ACCOUNT_SELECT_SECTOR_WHERE = " AND {b2bunit:sourceSysId}=?currentUnitSourceSysId ";
	protected final String ACCOUNT_SELECT_SALES_REP_WHERE = " AND {b2bunit:indicator}=?salesRepIndicator ";
	protected final String LIKE_CHARACTER = "%";

	/*Starts AAOL 5074*/
	protected final String ACCOUNT_SELECT_SEARCH_TERM = " AND (LOWER({b2bunit:globalLocNo}) LIKE LOWER(?searchTerm) OR LOWER({b2bunit:uid}) LIKE LOWER(?searchTerm) OR LOWER({b2bunit:locName}) LIKE LOWER(?searchTerm)  ) ";
	/*End AAOL 5074*/
	protected static final String FETCH_INDIRECT_CUST_WITHOUT_COUNTRYMODEL = "SELECT {PK} FROM {JNJINDIRECTCUSTOMER AS INDIRECT} WHERE (({INDIRECT:INDIRECTCUSTOMER} LIKE ?indirectCustomer) OR ({INDIRECT:INDIRECTCUSTOMERNAME} LIKE ?indirectCustomer))";
 
	/** End :: ACCOUNT SELECTION NON ADMIN QUERIES **/

	/** Start :: ACCOUNT SELECTION ADMIN QUERIES **/
	protected final String ACCOUNT_SELECT_UNION_START = "SELECT uniontable.PK FROM ({{";
	protected final String ACCOUNT_SELECT_UNION_1 = "SELECT {b2bunit:pk} AS PK ,{b2bunit:locName} as unitName, {b2bunitrelation:target} AS Target from {JnJB2bCustomer As customer  JOIN  PrincipalGroupRelation  AS b2bunitrelation ON {b2bunitrelation:source} = {customer:pk} JOIN JnJB2BUnit AS b2bunit ON {b2bunit:pk} = {b2bunitrelation:target}} where {customer:uid}=?currentUserUid ";
	protected final String ACCOUNT_SELECT_UNION_KEYWORD = "}} UNION {{";
	protected final String ACCOUNT_SELECT_UNION_2 = "SELECT {b2bunit:pk} AS PK ,{b2bunit:locName} as unitName, {territoryCustRel:target} AS Target from {JnJB2bCustomer As customer  JOIN   PrincipalGroupRelation AS userRel ON {userRel:source} = {customer:pk} JOIN JnjGTTerritoryDivCustRel AS territoryCustRel ON {territoryCustRel:source} = {userRel:target} JOIN JnJB2BUnit AS b2bunit ON {b2bunit:pk}={territoryCustRel:target}} where {customer:uid}=?currentUserUid AND {territoryCustRel:invalidated}='0' ";
	protected final String ACCOUNT_SELECT_UNION_END = "}}) uniontable";
	/** End :: ACCOUNT SELECTION ADMIN QUERIES **/

	/** Cut notification lines query **/
	protected final String FETCH_CUT_LINES_QUERY = "select {oe:pk} from {orderentry as oe JOIN order as o ON {oe:order}={o:pk} JOIN JnJB2BUnit as unit ON {o:unit}={unit:pk}} WHERE {oe:cutNotificationPreference}=1 AND {oe:cutNotificationEmailSent}=0 AND {unit:sourcesysid}=?sourcesysid AND {oe:reasonForRejection} IS NOT NULL";
	protected final String CANCELLED_ENUM_PK_QUERY = "select {pk} from {orderEntryStatus} where {code}='CANCELLED'";
	protected static final String FETCH_ALL_USERS_FOR_PASSWORD_EXPIRY_EMAIL = "SELECT {pk} "
			+ " FROM {JnJB2bCustomer AS b2bcustomer} " + "where {b2bcustomer:active} = 1";
	
	/* JJEPIC-825: Fetch All MDD Users */
	protected static final String FETCH_ALL_MDD_USERS = "SELECT {pk} FROM {JnjB2BCustomer} WHERE {mddSector} = 1";

	/* JJEPIC-825. Fetch unique list of B2B units */
	protected static final String FETCH_UNIQUE_B2BUNIT_CODES_FOR_REPS = "SELECT distinct uniontable.acno FROM ({{SELECT  {b2bunit:uid} AS acno ,{b2bunitrelation:target} AS Target from {JnjB2BCustomer As customer  JOIN  PrincipalGroupRelation  AS b2bunitrelation ON {b2bunitrelation:source} = {customer:pk} JOIN JnjB2BUnit AS b2bunit ON {b2bunit:pk} = {b2bunitrelation:target}} where {customer:uid}=?currentUserUid }} UNION {{SELECT {b2bunit:uid} AS acno , {territoryCustRel:target} AS Target from {JnjB2BCustomer As customer  JOIN   PrincipalGroupRelation AS userRel ON {userRel:source} = {customer:pk} JOIN JnjGTTerritoryDivCustRel AS territoryCustRel ON {territoryCustRel:source} = {userRel:target} JOIN JnjB2BUnit AS b2bunit ON {b2bunit:pk}={territoryCustRel:target}} where {customer:uid}=?currentUserUid }}) uniontable";

	protected static final String FETCH_INDIRECT_CUSTOMER = "SELECT {PK} FROM {JNJINDIRECTCUSTOMER AS INDIRECT} WHERE (({INDIRECT:INDIRECTCUSTOMER} LIKE ?indirectCustomer) OR ({INDIRECT:INDIRECTCUSTOMERNAME} LIKE ?indirectCustomer)) and {INDIRECT:country} in (?countries)";

	protected static final String QUERY_TO_EXTRACT_ACTIVE_USERS = "select mailid, wwid, groups, group_desc, region from ({{select {pk} as pk , {uid} as mailid ,{wwid} as wwid,?region as region from {jnjb2bcustomer}  where ({status}=({{select {pk} from {CustomerStatus} where {code}='ACTIVE'}}) and {uid} like ?jnjMailDomain) or ({status}=({{select {pk} from {CustomerStatus} where {code}='ACTIVE'}}) and {wwid} is not null)}}) t1 ";

	protected static final String JOIN_USERS_WITH_GROUPS = "JOIN ({{select {source} as source, {usr.uid} as groups, {usr.description} as group_desc from {PrincipalGroupRelation as grp join usergroup as usr on {grp.target}={usr.pk}}";

	protected static final String QUERY_TO_EXCLUDE_UNIT_AND_TERRITORY = " where NOT EXISTS ({{ SELECT {pk} FROM {JnJB2BUnit} WHERE {pk}={grp:target}}}) and NOT EXISTS ({{ SELECT {pk} FROM {JnjGTTerritoryDivison} WHERE {pk}={grp:target}}}) ";

	protected static final String QUERY_TO_EXCLUDE_CUSTOMER_GROUP = " and {usr.uid} NOT IN ('b2bcustomergroup','customergroup')}}) t2 on t1.pk=t2.source order by mailid asc";

	protected static final String REGION = "region";

	protected static final String JNJ_MAIL_DOMAIN = "jnjMailDomain";

	@Override
	public List<JnJB2bCustomerModel> getAllJnjGTUsers()
	{
		final List<JnJB2bCustomerModel> listOfJnjUser = new ArrayList<JnJB2bCustomerModel>();

		final List<JnJB2bCustomerModel> results;
		try
		{
			final String query = FETCH_ALL_USERS_FOR_PASSWORD_EXPIRY_EMAIL;
			LOGGER.debug("qry for getAllJnjGTUsers FOR disable user .. "+query);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			results = flexibleSearchService.<JnJB2bCustomerModel> search(fQuery).getResult();
			if (!results.isEmpty())
			{
				for (final JnJB2bCustomerModel jnjUser : results)
				{
					listOfJnjUser.add(jnjUser);
				}
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("Error fetching User " + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("User list size (getAllJnjGTUsers) for active cust for disable user event is " + JnJCommonUtil.getCurrentDateTime());
		}
		return listOfJnjUser;
	}

	/**
	 * This method validates the GLNs
	 * 
	 * @param invalidCode
	 * @return String
	 */
	@Override
	public String validateGLN(final String invalidCode)
	{
		final String METHOD_NAME = "validateGLN";
		CommonUtil.logMethodStartOrEnd(REGISTRATION, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOGGER);
		List<JnJB2BUnitModel> result;
		String responseString = "";
		/** Populating the query parameters **/

		/** Creating new flexible search query with the query string GLN_VALIDATION_QUERY **/
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} from {JnJB2BUnit} where {globalLocNo} like '%" + invalidCode + "%'");
		try
		{
			/** Calling flexible search service to fetch GLNs **/
			LOGGER.info("Calling flexible search service to fetch GLNs");
			result = flexibleSearchService.<JnJB2BUnitModel> search(fQuery).getResult();

			if (CollectionUtils.isEmpty(result))
			{
				responseString = invalidCode;
			}
			LOGGER.info("Result GLNs obtained :: " + result);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn("Model not found oR ambiguous identifier exception for given ID" + Logging.HYPHEN
					+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
		}

		CommonUtil.logMethodStartOrEnd(REGISTRATION, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
		return responseString;
	}

	/**
	 * This method is used to find all the PCM users who have their requestAccountIndicator set as true,meaning that the
	 * user is newly created and needs a password reset email to be sent for the same.
	 */
	@Override
	public List<JnJB2bCustomerModel> getAllNewPCMUsers()
	{

		final String METHOD_NAME = "validateGLN";
		CommonUtil.logMethodStartOrEnd(REGISTRATION, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOGGER);
		List<JnJB2bCustomerModel> result = new ArrayList<JnJB2bCustomerModel>();
		/** Creating new flexible search query with the query string GLN_VALIDATION_QUERY **/
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(NEW_PCM_USERS_QUERY);
		try
		{
			/** Calling flexible search service to fetch GLNs **/
			LOGGER.info("Calling flexible search service to fetch GLNs");
			result = flexibleSearchService.<JnJB2bCustomerModel> search(fQuery).getResult();
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn("Model not found oR ambiguous identifier exception for given ID" + Logging.HYPHEN
					+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
		}

		CommonUtil.logMethodStartOrEnd(REGISTRATION, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);

		if (result != null)
		{
			return result;
		}
		else
		{
			return null;
		}
	}

	/**
	 * This method fetches the accounts based on the parameters passed for the admin user.
	 * 
	 * @param isSectorSpecific
	 * @param isUCNFlag
	 * @return accountsMap
	 */
	@Override
	public SearchPageData getAllAccountsMap(final boolean isSectorSpecific, final boolean isUCNFlag,
			final JnjGTPageableData pageableData)
	{
		final String METHOD_NAME = "getAllAccountsMap()";

		String query;
		final Map queryParams = new HashMap();

		/** Only those accounts that are from current sector **/
		if (isSectorSpecific)
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
					"Fetching accounts for current sector only...", LOGGER);

			query = generateSectorSpecificAccountSearchQueryForAdmin(isUCNFlag, queryParams);
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "QUERY :: " + query, LOGGER);
		}
		/** Accounts from both sectors MDD and CONS **/
		else
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Fetching accounts for both sectors...",
					LOGGER);
			query = generateAccountSearchQueryForAdmin(isUCNFlag, queryParams);
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "QUERY :: " + query, LOGGER);
		}

		/** If search term is passed, adding to query **/
		if (StringUtils.isNotEmpty(pageableData.getSearchBy()))
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "User wishes to search accounts by :: "
					+ pageableData.getSearchBy(), LOGGER);
		if (Config.getBoolean("accountSearchTermWildcard", true))
			{
			final String searchBy=pageableData.getSearchBy();
				String subquery = null;
				if (StringUtils.isNumeric(searchBy))
				{
					
					if (StringUtils.length(searchBy) <= 10)
					{
						subquery = " AND ({b2bunit:uid} like ?searchTerm)";
						queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
					}
					else
					{
						subquery = " AND (LTRIM({b2bunit:globalLocNo},'0')=?searchTerm)";
						queryParams.put(SEARCH_TERM, StringUtils.stripStart(searchBy, "0"));
					}
				}
				else
				{
					subquery = " AND (LOWER({b2bunit:locName}) LIKE LOWER(?searchTerm))";
					queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy
							+ Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
				}
				
				
				
				query += subquery;
				
			}
		}

		final List<SortQueryData> sortQueries = Arrays.asList(createSortQueryData(pageableData.getSort(), query));
		/** Calling paged flexible search for fetching accounts **/
		UserModel currentUser = userService.getCurrentUser();
		userService.setCurrentUser(userService.getAdminUser());
		SearchPageData<JnJB2BUnitModel> searchResults =	pagedFlexibleSearchService.search(sortQueries, pageableData.getSort(), queryParams, pageableData);
				userService.setCurrentUser(currentUser);
				return searchResults;
	}

	/**
	 * This method fetches accounts for the non-admin user
	 * 
	 * @param isSectorSpecific
	 * @param isUCN
	 * @param pageableData
	 * @return accountsMaps
	 */
	@Override
	public SearchPageData<JnJB2BUnitModel> getAccountsMap(final boolean isSectorSpecific, final boolean isUCN,
			final JnjGTPageableData pageableData)
	{
		final String METHOD_NAME = "getAccountsMap()";

		final StringBuilder query = new StringBuilder();
		final StringBuilder query_part_1 = new StringBuilder();
		final StringBuilder query_part_2 = new StringBuilder();
		final Map<String, String> queryParams = new HashMap<String, String>();

		query_part_1.append(ACCOUNT_SELECT_UNION_START + ACCOUNT_SELECT_UNION_1);
		query_part_2.append(ACCOUNT_SELECT_UNION_KEYWORD + ACCOUNT_SELECT_UNION_2);

		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
				"Fetching accounts for current sector only...", LOGGER);
		final String queryToAppend = generateAccountSearchQuery(isUCN, isSectorSpecific, queryParams);
		query_part_1.append(queryToAppend);
		query_part_2.append(queryToAppend);
		
		/** If search term is passed, adding to query **/
		if (null != pageableData && StringUtils.isNotEmpty(pageableData.getSearchBy()))
		{
			final String searchBy=pageableData.getSearchBy();
			String subquery = null;
			if (StringUtils.isNumeric(searchBy))
			{
				if (StringUtils.length(searchBy) <= 10)
				{
					subquery = " AND ({b2bunit:uid} like ?searchTerm)";
					queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
				}
				else
				{
					subquery = " AND (LTRIM({b2bunit:globalLocNo},'0')=?searchTerm)";
					queryParams.put(SEARCH_TERM, StringUtils.stripStart(searchBy, "0"));
				}
			}
			else
			{
				subquery = " AND (LOWER({b2bunit:locName}) LIKE LOWER(?searchTerm))";
				queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy
						+ Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
			}
			
			
			query_part_1.append(subquery);
			query_part_2.append(subquery);
			
		}
		query.append(String.valueOf(query_part_1));
		query.append(String.valueOf(query_part_2));
		query.append(ACCOUNT_ORDER_BY);
		query.append(ACCOUNT_SELECT_UNION_END);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "QUERY :: " + query, LOGGER);

		queryParams.put(CURRENT_USER_ID_KEY, userService.getCurrentUser().getUid());

		final List<SortQueryData> sortQueries = Arrays.asList(createSortQueryData(pageableData.getSort(), String.valueOf(query)));

		UserModel currentUser = userService.getCurrentUser();
		userService.setCurrentUser(userService.getAdminUser());
		SearchPageData<JnJB2BUnitModel> searchResults = pagedFlexibleSearchService.search(sortQueries, pageableData.getSort(), queryParams, pageableData);
		userService.setCurrentUser(currentUser);
		return searchResults;
	}

	/**
	 * This method generates sector specific accounts search query for non admin user
	 * 
	 * @param isUCN
	 * @param isSectorSpecific
	 * @param queryParams
	 */
	protected String generateAccountSearchQuery(final boolean isUCN, final boolean isSectorSpecific,
			final Map<String, String> queryParams)
	{
		final String METHOD_NAME = "generateAccountSearchQuery()";
		String queryToAppend = "";
		String sourceSysId;

		/** Only those accounts that are from current sector **/
		if (isSectorSpecific)
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
					"Fetching accounts for current sector only...", LOGGER);

			/** Checking if current site is Consumer or MDD **/
			final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			if (Jnjb2bCoreConstants.CONS.equalsIgnoreCase(siteName))
			{
				sourceSysId = JnjGTSourceSysId.CONSUMER.getCode();
			}
			else
			{
				sourceSysId = JnjGTSourceSysId.MDD.getCode();
			}
			queryToAppend += ACCOUNT_SELECT_SECTOR_WHERE;
			queryParams.put(CURRENT_UNIT_SOURCE_SYS_ID_KEY, sourceSysId);
		}

		/** SalesRep accounts only **/
		if (isUCN)
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Fetching only sales rep accounts...",
					LOGGER);
			queryParams.put(SALES_REP_INDICATOR_KEY, SALES_REP_INDICATOR_VALUE);
			queryToAppend += ACCOUNT_SELECT_SALES_REP_WHERE;
		}

		return queryToAppend;
	}

	/**
	 * 
	 * This method generates the query for sector specific account search for administrator
	 * 
	 * @param isUCNFlag
	 * @param queryParams
	 * @return query
	 */
	protected String generateSectorSpecificAccountSearchQueryForAdmin(final boolean isUCNFlag, final Map queryParams)
	{
		final String METHOD_NAME = "generateSectorSpecificAccountSearchQueryForAdmin()";
		String query;
		String sourceSysId;

		/** Checking if current site is Consumer or MDD **/
		final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (Jnjb2bCoreConstants.CONS.equalsIgnoreCase(siteName))
		{
			sourceSysId = JnjGTSourceSysId.CONSUMER.getCode();
		}
		else
		{
			sourceSysId = JnjGTSourceSysId.MDD.getCode();
		}

		query = ACCOUNT_SELECT_MAIN + ACCOUNT_SELECT_B2B_JOIN + ACCOUNT_SELECT_GLOBAL_WHERE + ACCOUNT_SELECT_SECTOR_WHERE;

		queryParams.put(GLOBAL_B2B_UNIT_KEY, GLOBAL_UNIT_ID);
		queryParams.put(CURRENT_UNIT_SOURCE_SYS_ID_KEY, sourceSysId);

		/** SalesRep accounts only **/
		if (isUCNFlag)
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Fetching only sales rep accounts...",
					LOGGER);

			queryParams.put(SALES_REP_INDICATOR_KEY, SALES_REP_INDICATOR_VALUE);
			query += ACCOUNT_SELECT_SALES_REP_WHERE;
		}
		return query;
	}

	/**
	 * 
	 * This method generates the query for account search for administrator
	 * 
	 * @param isUCNFlag
	 * @param queryParams
	 * @return query
	 */
	protected String generateAccountSearchQueryForAdmin(final boolean isUCNFlag, final Map queryParams)
	{
		final String METHOD_NAME = "generateAccountSearchQueryForAdmin()";
		String query;
		queryParams.put(GLOBAL_B2B_UNIT_KEY, GLOBAL_UNIT_ID);
		/** SalesRep accounts only **/
		if (isUCNFlag)
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Fetching only sales rep accounts...",
					LOGGER);

			queryParams.put(SALES_REP_INDICATOR_KEY, SALES_REP_INDICATOR_VALUE);
			query = ACCOUNT_SELECT_MAIN + ACCOUNT_SELECT_B2B_JOIN + ACCOUNT_SELECT_GLOBAL_WHERE + ACCOUNT_SELECT_SALES_REP_WHERE;
		}
		else
		{
			query = ACCOUNT_SELECT_MAIN + ACCOUNT_SELECT_B2B_JOIN + ACCOUNT_SELECT_GLOBAL_WHERE;
		}
		return query;
	}

	/**
	 * This method creates sort query data
	 * 
	 * @param sortCode
	 * @param query
	 * @return SortQueryData
	 */
	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final String METHOD_NAME = "createSortQueryData()";
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}

	/**
	 * This method fetches the cut lines for sending notification email
	 * 
	 * @return List<OrderEntryModel>
	 */
	@Override
	public List<OrderEntryModel> fetchCutLineDetails()
	{
		final String METHOD_NAME = "createSortQueryData()";

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FETCH_CUT_LINES_QUERY);
		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, " Query :: " + FETCH_CUT_LINES_QUERY, LOGGER);

		/** Populating the query parameters **/
		final Map queryParams = new HashMap();
		queryParams.put(UNIT_SOURCE_SYS_ID_KEY, Jnjb2bCoreConstants.CONSUMER);

		fQuery.addQueryParameters(queryParams);
		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, " Params :: " + queryParams, LOGGER);

		List<OrderEntryModel> result = null;
		try
		{
			CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Going to perform flexible search", LOGGER);
			/** Fetching the type-casted result into OrderEntryModel type in order to obtain List<OrderEntryModel> **/
			result = flexibleSearchService.<OrderEntryModel> search(fQuery).getResult();

			LOGGER.info(CUT_NOTIFICATION_EMAIL + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Result obtained :: " + result);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn(CUT_NOTIFICATION_EMAIL + Logging.HYPHEN + METHOD_NAME
					+ "model not found oR ambiguous identifier exception for given ID" + Logging.HYPHEN
					+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
		}

		CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
		return result;
	}
	
	@Override
	public String validateWwid(final String wwid)
	{
		final String METHOD_NAME = "validateWwid";
		CommonUtil.logMethodStartOrEnd(REGISTRATION, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOGGER);
		List<JnJB2bCustomerModel> result;
		String responseString = "";
		/** Populating the query parameters **/

		/** Creating new flexible search query with the query string wwid **/
		final String queryStart = "SELECT {pk} from {JnJB2BCustomer} where {wwid} IN ('";
		final String queryEnd = "')";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryStart + wwid + queryEnd);
		try
		{
			/** Calling flexible search service to fetch GLNs **/
			LOGGER.info("Calling flexible search service to fetch wwid");
			result = flexibleSearchService.<JnJB2bCustomerModel> search(fQuery).getResult();

			if (!CollectionUtils.isEmpty(result))
			{
				responseString = wwid;
			}
			LOGGER.info("Result wwid obtained :: " + result);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn("Model not found oR ambiguous identifier exception for given wwid" + Logging.HYPHEN
					+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
		}

		CommonUtil.logMethodStartOrEnd(REGISTRATION, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
		return responseString;
	}

	@Override
	public List<JnJB2bCustomerModel> getUsersWithAccount(String accountNumber)
	{
		final List<JnJB2bCustomerModel> results;
		final String query = " select {rel:source} from {PrincipalGroupRelation   as rel} where {rel:Target} = ({{ select {pk} from {jnjb2bunit} where {uid} = '"
				+ accountNumber + "' }})";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		results = flexibleSearchService.<JnJB2bCustomerModel> search(fQuery).getResult();

		return results;
	}

	@Override
	public List<JnJB2bCustomerModel> getUsersWithTerritoryWithB2bUnit(String uid)
	{
		final List<JnJB2bCustomerModel> results;
		final String query = "select {u:pk} from {jnjb2bcustomer as u left join JnjGTTerritoryDivCustRel as r on {r:target} = {u:pk} left JOIN AccessBy as a on {u:accessBy} = {a:pk}} where {u:mddSector} = 1 and {a:code} <> 'NOT_SALES_REP' and {r:source} in ({{ select {rel:source} from {JnjGTTerritoryDivCustRel   as rel} where {rel:Target} = ({{ select {pk} from {jnjb2bunit} where {uid} = '"
				+ uid + "' }}) }})";

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		results = flexibleSearchService.<JnJB2bCustomerModel> search(fQuery).getResult();

		return results;
	}

	@Override
	public List<JnJB2bCustomerModel> getAllMDDUsers() {
		List<JnJB2bCustomerModel> results = null;
		try
		{
			final String query = FETCH_ALL_MDD_USERS;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			results = flexibleSearchService.<JnJB2bCustomerModel> search(fQuery).getResult();
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("Error fetching MDD User " + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Error fetching MDD User " + JnJCommonUtil.getCurrentDateTime());
		}
		return results;
	}

	@Override
	public List<String> getUniqueB2BUnitCodesForRepUID(String repUID) {
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getUniqueB2BUnitCodesForRepUID()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FETCH_UNIQUE_B2BUNIT_CODES_FOR_REPS);
		final List resultClassList = new ArrayList();
		resultClassList.add(String.class);
		fQuery.setResultClassList(resultClassList);
		final Map queryParams = new HashMap<>();
		queryParams.put("currentUserUid", repUID);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getUniqueB2BUnitCodesForRepUID()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query " + fQuery);
		}
		final SearchResult result = flexibleSearchService.search(fQuery);
		final List<String> resultList = result.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getUniqueB2BUnitCodesForRepUID()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return resultList;
	}

	@Override
	public JnJB2bCustomerModel getCustomerByUid(String uid) {
		JnJB2bCustomerModel tempCustomerForSearch = new JnJB2bCustomerModel();
		tempCustomerForSearch.setUid(uid);
		JnJB2bCustomerModel modelToReturn = null;
		try
		{
			modelToReturn = flexibleSearchService.getModelByExample(tempCustomerForSearch);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return modelToReturn;
	}
	
	@Override
	public StringBuilder getAccountsMapQuery(final boolean isSectorSpecific, final boolean isUCN,
			final JnjGTPageableData pageableData, final Map<String, String> queryParams)
	{
		final StringBuilder query = new StringBuilder();
		final StringBuilder query_part_1 = new StringBuilder();
		final StringBuilder query_part_2 = new StringBuilder();

		query_part_1.append(ACCOUNT_SELECT_UNION_START + ACCOUNT_SELECT_UNION_1);
		query_part_2.append(ACCOUNT_SELECT_UNION_KEYWORD + ACCOUNT_SELECT_UNION_2);

		final String queryToAppend = generateAccountSearchQuery(isUCN, isSectorSpecific, queryParams);
		query_part_1.append(queryToAppend);
		query_part_2.append(queryToAppend);

		/** If search term is passed, adding to query **/
		if (null != pageableData && StringUtils.isNotEmpty(pageableData.getSearchBy()))
		{
			//FOR JJEPIC-810
			if (Config.getBoolean("local.development.environment", false))
			{
				query_part_1.append(ACCOUNT_SELECT_SEARCH_TERM);
				query_part_2.append(ACCOUNT_SELECT_SEARCH_TERM);
				queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + pageableData.getSearchBy()
						+ Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
			}
			else
			{
				final String extQuery = createQueryForSearch(pageableData.getSearchBy(), queryParams);
				query_part_1.append(extQuery);
				query_part_2.append(extQuery);
			}
		}
		query.append(String.valueOf(query_part_1));
		query.append(String.valueOf(query_part_2));
		query.append(ACCOUNT_ORDER_BY);
		query.append(ACCOUNT_SELECT_UNION_END);
		return query;
	}
	
	/**
	 * FOR JJEPIC-810: This method is used to create the specific search query based on the length of the search text and
	 * the literals in the search text.
	 *
	 * @param searchBy
	 * @return query string to be appended to the query for search
	 */
	protected String createQueryForSearch(final String searchBy, final Map<String, String> queryParams)
	{

		String query = null;
		if (StringUtils.isNumeric(searchBy))
		{
			if (StringUtils.length(searchBy) <= 10)
			{
				query = " AND (LTRIM({b2bunit:uid},'0')=?searchTerm)";
				queryParams.put(SEARCH_TERM, StringUtils.stripStart(searchBy, "0"));
			}
			else
			{
				query = " AND (LTRIM({b2bunit:globalLocNo},'0')=?searchTerm)";
				queryParams.put(SEARCH_TERM, StringUtils.stripStart(searchBy, "0"));
			}
		}
		else
		{
			query = " AND (LOWER({b2bunit:locName}) LIKE LOWER(?searchTerm))";
			queryParams.put(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchBy
					+ Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
		}
		return query;
	}
	
	@Override
	public List<JnjIndirectCustomerModel> fetchIndirectCustomers(final String indirectCustomer, final CountryModel countryModel)
	{
		final String METHOD_NAME = "fetchIndirectCustomerName ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PASSWORD_EXPIRY + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjIndirectCustomerModel> listOfindirectcustomer = new ArrayList<JnjIndirectCustomerModel>();

		final List<JnjIndirectCustomerModel> results;
		try
		{
			final String query = FETCH_INDIRECT_CUSTOMER;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

			final StringTokenizer countryList = new StringTokenizer(Config.getParameter(Jnjb2bCoreConstants.KEY_Valid_COUNTRIES),
					",");
			final List<String> cencaCountryList = new ArrayList<String>();
			DefaultJnJCustomerDataDao.filterCencaCountryList(countryList, cencaCountryList);

			// Cenca countries
			List<String> countries = null;
			if (cencaCountryList.contains(countryModel.getIsocode()))
			{
				countries = new ArrayList<String>();
				for (final String isocode : cencaCountryList)
				{
					countries.add(getCountryModelByIsoOrPk(isocode).getPk().toString());
				}
				fQuery.addQueryParameter(UpsertCustomer.COUNTRIES_STRING, countries);
			}
			else
			{
				fQuery.addQueryParameter(UpsertCustomer.COUNTRIES_STRING, countryModel);
			}

			fQuery.addQueryParameter("indirectCustomer", LIKE_CHARACTER + indirectCustomer.toUpperCase() + LIKE_CHARACTER);
			results = getFlexibleSearchService().<JnjIndirectCustomerModel> search(fQuery).getResult();
			if (!results.isEmpty())
			{
				for (final JnjIndirectCustomerModel jnjIndirectCustomerModel : results)
				{
					listOfindirectcustomer.add(jnjIndirectCustomerModel);
				}
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("Error fetching User " + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PASSWORD_EXPIRY + " - " + METHOD_NAME + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return listOfindirectcustomer;

	}
	
	/**
	 * @param country
	 * @return
	 */
	public CountryModel getCountryModelByIsoOrPk(final String country)
	{
		ServicesUtil.validateParameterNotNull(country, "Country Id/Isocode must not be null");

		final Map queryParams = new HashMap();
		String query = null;
		if (country.length() > 3)
		{
			query = "select {pk} from {Country} where {pk}=?pk";
			queryParams.put(UpsertCustomer.COUNTRY_PK_STRING, country);
		}
		else
		{
			query = "select {pk} from {Country} where {isocode}=?isocode";
			queryParams.put(UpsertCustomer.ISOCODE_STRING, country);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);

		final CountryModel countryModel = (CountryModel) getFlexibleSearchService().searchUnique(fQuery);

		return countryModel;
	}


	@Override
	public JnjIndirectCustomerModel fetchIndirectCustomerName(final String indirectCustomer)
	{
		final String METHOD_NAME = "fetchIndirectCustomerName ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PASSWORD_EXPIRY + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjIndirectCustomerModel> listOfindirectcustomer = new ArrayList<JnjIndirectCustomerModel>();

		final List<JnjIndirectCustomerModel> results;
		try
		{
			final String query = FETCH_INDIRECT_CUST_WITHOUT_COUNTRYMODEL;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameter("indirectCustomer", LIKE_CHARACTER + indirectCustomer.toUpperCase() + LIKE_CHARACTER);
			results = getFlexibleSearchService().<JnjIndirectCustomerModel> search(fQuery).getResult();
			if (!results.isEmpty())
			{
				for (final JnjIndirectCustomerModel jnjIndirectCustomerModel : results)
				{
					listOfindirectcustomer.add(jnjIndirectCustomerModel);
				}
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("Error fetching User " + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PASSWORD_EXPIRY + " - " + METHOD_NAME + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return listOfindirectcustomer.size() > 0 ? listOfindirectcustomer.get(0) : null;//35406 fixed

	} 
	
	@Override
	public JnjIndirectCustomerModel fetchIndirectCustomerName(final String indirectCustomer, final CountryModel countryModel)
	{
		final String METHOD_NAME = "fetchIndirectCustomerName ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PASSWORD_EXPIRY + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjIndirectCustomerModel> listOfindirectcustomer = new ArrayList<JnjIndirectCustomerModel>();

		final List<JnjIndirectCustomerModel> results;
		try
		{
			final String query = FETCH_INDIRECT_CUSTOMER;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);


			// Changes for JCC-38
			final StringTokenizer countryList = new StringTokenizer(Config.getParameter(Jnjb2bCoreConstants.KEY_Valid_COUNTRIES),
					",");
			final List<String> cencaCountryList = new ArrayList<String>();
			DefaultJnJCustomerDataDao.filterCencaCountryList(countryList, cencaCountryList);

			// Cenca countries
			List<String> countries = null;
			if (cencaCountryList.contains(countryModel.getIsocode()))
			{
				countries = new ArrayList<String>();
				for (final String isocode : cencaCountryList)
				{
					countries.add(getCountryModelByIsoOrPk(isocode).getPk().toString());
				}
				fQuery.addQueryParameter(UpsertCustomer.COUNTRIES_STRING, countries);
			}
			else
			{
				fQuery.addQueryParameter(UpsertCustomer.COUNTRIES_STRING, countryModel);
			}

			fQuery.addQueryParameter("indirectCustomer", LIKE_CHARACTER + indirectCustomer.toUpperCase() + LIKE_CHARACTER);
			results = getFlexibleSearchService().<JnjIndirectCustomerModel> search(fQuery).getResult();
			if (!results.isEmpty())
			{
				for (final JnjIndirectCustomerModel jnjIndirectCustomerModel : results)
				{
					listOfindirectcustomer.add(jnjIndirectCustomerModel);
				}
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("Error fetching User " + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PASSWORD_EXPIRY + " - " + METHOD_NAME + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return listOfindirectcustomer.size() > 0 ? listOfindirectcustomer.get(0) : null;//35406 fixed

	}

	@Override
	public List<JnJB2BUnitModel> getAccountListForAutoSuggest(String searchText,boolean isAdminUser) {

		final List<JnJB2BUnitModel> results;
		StringBuilder query = new StringBuilder();
		FlexibleSearchQuery fQuery = null;		
		final String subquery = " AND ({b2bunit:uid} like ?searchTerm)";
		
		if(isAdminUser){
			query.append(ACCOUNT_SELECT_MAIN);
			query.append(ACCOUNT_SELECT_B2B_JOIN);
			query.append(ACCOUNT_SELECT_GLOBAL_WHERE);
			query.append(subquery);
			fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameter(GLOBAL_B2B_UNIT_KEY, GLOBAL_UNIT_ID	);
			
		}
		else{
			final StringBuilder query_part_1 = new StringBuilder();
			final StringBuilder query_part_2 = new StringBuilder();

			query_part_1.append(ACCOUNT_SELECT_UNION_START + ACCOUNT_SELECT_UNION_1);
			query_part_2.append(ACCOUNT_SELECT_UNION_KEYWORD + ACCOUNT_SELECT_UNION_2);
			query_part_1.append(subquery);
			query_part_2.append(subquery);
			query.append(String.valueOf(query_part_1));
			query.append(String.valueOf(query_part_2));
			query.append(ACCOUNT_SELECT_UNION_END);
			fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameter(CURRENT_USER_ID_KEY, userService.getCurrentUser().getUid());
		}
		fQuery.addQueryParameter(SEARCH_TERM, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER + searchText + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);

		results = flexibleSearchService.<JnJB2BUnitModel> search(fQuery).getResult();
		
		return results;
	}

	/**
	 * This method gives the generated user list.
	 * @return
	 */
	@Override
	public List<ArrayList<String>> extractActiveUserDetailList() {

		final Map<String, String> queryParams = new HashMap<>();


		final StringBuilder query = new StringBuilder(QUERY_TO_EXTRACT_ACTIVE_USERS);
		query.append(JOIN_USERS_WITH_GROUPS);
		query.append(QUERY_TO_EXCLUDE_UNIT_AND_TERRITORY);
		query.append(QUERY_TO_EXCLUDE_CUSTOMER_GROUP);


		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
		queryParams.put(REGION, getConfigurationService().getConfiguration().getString(REGION));
		queryParams.put(JNJ_MAIL_DOMAIN, getConfigurationService().getConfiguration().getString(JNJ_MAIL_DOMAIN));
		fQuery.addQueryParameters(queryParams);
		fQuery.setResultClassList(Arrays.asList(String.class,String.class,String.class,String.class,String.class));
		LOGGER.info("Query --: " + fQuery.getQuery());

		return getSessionService().executeInLocalView(new SessionExecutionBody() {
			@Override
			public List<ArrayList<String>> execute() {
				return getFlexibleSearchService().<ArrayList<String>>search(fQuery).getResult();
			}
		}, getUserService().getAdminUser());
	}
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
	public PagedFlexibleSearchService getPagedFlexibleSearchService() {
		return pagedFlexibleSearchService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}
	public void setSessionService(final SessionService sessionService) {
		this.sessionService = sessionService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}
	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}
