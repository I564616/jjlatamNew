/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.core.dao.order.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.BooleanUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Order.ChannelOption;
import com.jnj.core.dao.JnjGTProductDao;
import com.jnj.core.dao.order.JnjGTOrderDao;
import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order.ChannelOption;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.utils.CommonUtil;
import com.jnj.core.dao.customer.JnjGTCustomerDAO;


/**
 * Dao Layer class responsible to execute Paged Flexible Search Queries based on the various default/custom fields value
 * selection, sort and search criteria from Order History page
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTOrderDao implements JnjGTOrderDao
{
	@Autowired
	protected PagedFlexibleSearchService pagedFlexibleSearchService;

	@Autowired
	FlexibleSearchService flexibleSearchService;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected JnjGTProductDao jnjGTProductDao;

	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	@Autowired
	protected BaseStoreService baseStoreService;
	
	@Resource(name="GTCustomerDao")
	protected JnjGTCustomerDAO jnjGTCustomerDAO;
	
	

	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderDao.class);
	public PagedFlexibleSearchService getPagedFlexibleSearchService() {
		return pagedFlexibleSearchService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public UserService getUserService() {
		return userService;
	}

	public JnjGTProductDao getJnjGTProductDao() {
		return jnjGTProductDao;
	}

	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}
	////Changes for Order History Enhancement AAOL-4732,4796
	protected static final String SELECT_FROM_ORDER = "SELECT DISTINCT {PK}, {ORDERNUMBER}, {PURCHASEORDERNUMBER},  {ORDERTYPE}, {SHIPTOACCOUNT}, {CREATIONTIME}, {DISTRIBUTIONCHANNEL}, {STATUS} ,{TOTALPRICE}    FROM {Order AS order JOIN JnJB2BUnit AS unit ON {order:unit} = {unit:pk}} WHERE {unit:uid} "
			+ "IN (?selectedAccounts) ";
	//Changes for Product with no Franchise
	protected static final String SELECT_FROM_ORDER_FRANCHISE = "SELECT DISTINCT {PK}, {ORDERNUMBER}, {PURCHASEORDERNUMBER},  {ORDERTYPE}, {SHIPTOACCOUNT}, {CREATIONTIME}, {DISTRIBUTIONCHANNEL}, {STATUS} , {TOTALPRICE} FROM {Order AS order JOIN JnJB2BUnit AS unit ON {order:unit} = {unit:pk} JOIN orderEntry AS oe ON {order.pk}={oe.order} JOIN JNJProduct as product on {product:pk}={oe:product} join CategoryProductRelation as cprlevel3 on {cprlevel3:target} = {product:pk} join CategoryCategoryRelation as level2 on {level2:target} = {cprlevel3:source} join Category as cat on {cat:pk} = {level2:source}} WHERE {unit:uid} "
			+ "IN (?selectedAccounts) ";
	protected static final String SALES_REP_ORDER_FRANCHISE = "SELECT DISTINCT {PK}, {ORDERNUMBER}, {CREATIONTIME},  {TOTALPRICE} FROM {Order AS order JOIN JnJB2BUnit AS unit ON {order:unit} = {unit:pk} JOIN orderEntry AS oe ON {order.pk}={oe.order} JOIN JNJProduct as product on {product:pk}={oe:product} join CategoryProductRelation as cprlevel3 on {cprlevel3:target} = {product:pk} join CategoryCategoryRelation as level2 on {level2:target} = {cprlevel3:source} join Category as cat on {cat:pk} = {level2:source}} WHERE {unit:uid} IN (?selectedAccounts) AND {product:SalesOrgCode} IN (?SalesOrgs)";
	protected static final String SALES_REP_ORDER = "SELECT DISTINCT {PK}, {ORDERNUMBER}, {CREATIONTIME},  {TOTALPRICE} FROM {Order AS order JOIN JnJB2BUnit AS unit ON {order:unit} = {unit:pk} JOIN orderEntry AS oe ON {order.pk}={oe.order} JOIN JNJProduct as product on {product:pk}={oe:product}} WHERE {unit:uid} IN (?selectedAccounts) AND {product:SalesOrgCode} IN (?SalesOrgs) ";
	protected static final String AND_CHANNEL_EQUAL_TO = "AND {poType} IN (?poTypes) ";
	protected static final String AND_STATUS_EQUAL_TO = "AND {order:status} = ({{SELECT {PK} FROM {ORDERSTATUS} WHERE {CODE}=?status}}) ";
	protected static final String AND_TYPE_EQUAL_TO = "AND {order:orderType} = ({{SELECT {PK} FROM {JnjOrderTypesEnum} WHERE {CODE}=?orderType}}) ";
	protected static final String AND_LINE_STATUS_EQUAL_TO = "AND {order:PK} IN ({{ SELECT {ENTRY:ORDER} FROM {ORDERENTRY AS ENTRY JOIN ORDER as ORD ON "
						+ "{ENTRY:order}={ORD:PK}} WHERE UPPER({ENTRY:status}) LIKE UPPER(?status)}}) ";
	/*+ "{ENTRY:order}={ORD:PK}} WHERE {ENTRY:status} = ?status}})  ";*/
   protected static final String AND_TYPE_EQUAL_TO_MULTIPLE ="";
   //Modified by Archana for HardCoding Removal
   public static final String DATE_FORMAT ="storefront.date.format";
   public static final String DATE_FORMAT_SQL ="storefront.date.format.sql";
   public static final String DATE_VALUE = Config.getParameter(DATE_FORMAT);
   public static final int DATE_VALUE_SQL = Integer.parseInt(Config.getParameter(DATE_FORMAT_SQL));

   
   	protected static String AND_DATE_BETWEEN = "AND {order:creationtime} BETWEEN convert(datetime,?startDate,?DATE_VALUE) AND  convert(datetime,?endDate,?DATE_VALUE)";
	protected static final String AND_ORDER_CODE_LIKE = "AND ({order:orderNumber} LIKE ?orderNumber OR {order:sapOrderNumber} LIKE ?orderNumber) ";
	protected static final String AND_PRODUCT_CODE_LIKE = "AND {order:PK} IN ({{ SELECT {ENTRY:ORDER} FROM {ORDERENTRY AS ENTRY JOIN ORDER as ORD ON "
			+ " {ENTRY:order}={ORD:PK} JOIN PRODUCT AS PROD ON {ENTRY:PRODUCT} = {PROD:PK}} WHERE {PROD:PK} IN (?PRODUCTPK) }})";
	protected static final String AND_PO_NUMBER_LIKE = "AND UPPER({purchaseOrderNumber}) LIKE UPPER(?purchaseOrderNumber) ";
	protected static final String AND_DEALER_PO_LIKE = " AND {dealerPONum} LIKE ?dealerPONum ";
	protected static final String AND_INVOICE_NUMBER_LIKE = "AND {order:PK} IN ({{ SELECT {INVOICE:ORDER} FROM {JNJGTINVOICE AS INVOICE JOIN ORDER as ORD ON "
			+ "{INVOICE:order}={ORD:PK}} WHERE {INVOICE:invoiceNum} LIKE ?invoiceNumber}}) ";

	protected static final String ORDERBY_DATE = "ORDER BY {order:creationtime} ";
	protected static final String ORDERBY_NUMBER = "ORDER BY {order:orderNumber} ";
	protected static final String ORDERBY_TOTAL = "ORDER BY {order:totalPrice} ";

	//asha
	protected static final String PO_NUMBER = "ORDER BY {order:purchaseOrderNumber} ";
	protected static final String ORDER_TYPE = "ORDER BY {order:orderType} ";
	protected static final String SHIP_TO = "ORDER BY {order:shipToAccount} ";
	protected static final String STATUS = "ORDER BY {order:status} ";
	protected static final String CHANNEL = "ORDER BY {order:distributionChannel} ";
	
	//Changes for Order History Enhancement AAOL-4732,4796
	protected static final String PRODUCT_CATEGORY_LIKE1 = " AND {cat:name} LIKE ?franchise ";
	protected static final String PRODUCT_CATEGORY_ALLOWED = " AND {cat:code} IN (?allowedCatgeory)";
	protected static final String SURGEON_ID_LIKE = "AND {order:surgeon}= ({{SELECT {PK} FROM {JnjGTSurgeon} WHERE {surgeonId}=?surgeonId}})";
	
	protected static final String SORT_DESCENDING = "DESC";
	protected static final String LIKE_WILDCARD_CHARACTER = "%";
	private static final String QUERY_PARAM__FOURTEEN = "fourteenDays";

	protected static final String SELECT_TERRITORY_DIV_BY_SHIP_TO_NUMBER = "SELECT {TER:PK} FROM {JNJGTTERRITORYDIVISON AS TER JOIN JNJGTTERRITORYDIVADDRESSES AS ADDR ON "
			+ "{TER:PK}={ADDR:JNJGTTERRITORYDIVISON}} WHERE {ADDR:ADDRESSCODE} = ?shipToNumber";

	protected static final String SELECT_ORDERS_ELIGIBLE_FOR_SHIP_EMAIL_NOTIFICATION = "SELECT {O:PK} FROM {ORDER AS O JOIN USER AS U ON {O:USER}={U:PK}} WHERE "
			+ "{O:SENDORDERSHIPMENTEMAIL}=1 AND {O:SHIPMENTEMAILPREFERENCE}=1 AND {U:UID} != 'dummy@sapuser.com'";

	protected static final String SELECT_BACKORDERS_FOR_EMAIL_NOTIFICATION = "SELECT {OE:PK} FROM {ORDER AS O JOIN USER AS U ON {O:USER}={U:PK} JOIN ORDERENTRY AS "
			+ "OE ON {OE:ORDER}={O:PK} JOIN JNJPRODUCT AS PROD ON {OE:PRODUCT}={PROD:PK}} WHERE {O:STORE}= ?store AND {U:UID} != 'dummy@sapuser.com' AND {OE:STATUS}"
			+ "='BACKORDERED' AND {PROD:BACKORDEREDDATE} IS NOT NULL ORDER BY {U:UID} ASC";

	private static final String BACKORDER_AND_OPEN_LINE_EMAIL_QUERY = "SELECT Distinct{oe.pk} FROM {order AS o JOIN orderEntry AS oe ON {o.pk}={oe.order} JOIN JnjDeliverySchedule AS ds ON {ds.ownerEntry} = {oe.pk} JOIN JnjOrderTypesEnum AS status ON {o:orderType}={status:pk}} WHERE {status:code} IN (?orderTypes) AND {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} WHERE {uid} IN (?accountNumbers)}}) AND trunc({o.date}) >= dateadd(day, -?days, sysdatetime()) AND {ds.lineStatus} IN (?lineStatus)";

	private static final String CANCELLED_LS_OPEN_LINE_EMAIL_QUERY = "SELECT Distinct{oe.pk} FROM {order AS o JOIN orderEntry AS oe ON {o.pk}={oe.order} JOIN JnjDeliverySchedule AS ds ON {ds.ownerEntry} = {oe.pk} JOIN JnjOrderTypesEnum AS status ON {o:orderType}={status:pk}} WHERE {status:code} IN (?orderTypes) AND {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} WHERE {uid} IN (?accountNumbers)}}) AND trunc({o.date}) >= dateadd(day, -?days, sysdatetime()) AND {o.date} >= convert(datetime,?fourteenDays,103) AND {ds.lineStatus} ='RJ'";

	//Change for AALO-4049
	private static final String SELECT_CANCEL_ORDER_LINE_ITMES= "SELECT {PK} FROM {ORDERENTRY} WHERE {STATUS} ='CANCELED'";
	private static final String SELECT_ORDER_BY_ORDERNUMBER= "SELECT {PK} FROM {ORDER} WHERE {CODE} =?orderCode";
	@Override
	public SearchPageData<OrderModel> getPagedOrderHistoryForStatuses(final PageableData pageableData,
			final JnjGTOrderHistoryForm form)
	{
		final String sortCode = (form.getSortCode() == null) ? Order.SortOption.DEFAULT_SORT_CODE : form.getSortCode();
		List<SortQueryData> sortQueries = null;
		final Map<String, Object> queryParams = new HashMap<String, Object>();

		final String[] selectedAccounts = form.getAccounts();
		
		
		queryParams.put("selectedAccounts", Arrays.asList(selectedAccounts));
		String selectQuery = null;
		if (sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALES_REP_USER).equals(Boolean.TRUE))
		{
			final List<String> userDivs = form.getUserDivisions();
			
			if (CollectionUtils.isNotEmpty(userDivs))
			{
				//This query will be executed when the user filters with Franchise
				if(form.getFranchise() != null && !form.getFranchise().isEmpty()){
					selectQuery = (form.isResetSelection()) ? SALES_REP_ORDER_FRANCHISE : getSelectQuery(form, queryParams, userDivs);
					queryParams.put("SalesOrgs", userDivs);
				}else{
					selectQuery = (form.isResetSelection()) ? SALES_REP_ORDER : getSelectQuery(form, queryParams, userDivs);
					queryParams.put("SalesOrgs", userDivs);
				}
				
			}
		}
		else if (form.getFranchise() != null && !form.getFranchise().isEmpty()){
			//This query will be executed when the user filters with Franchise
			selectQuery = (form.isResetSelection()) ? SELECT_FROM_ORDER_FRANCHISE : getSelectQuery(form, queryParams, null);
		}
		else
		{
			selectQuery = (form.isResetSelection()) ? SELECT_FROM_ORDER : getSelectQuery(form, queryParams, null);
		}
		if (StringUtils.isEmpty(selectQuery))
		{
			final SearchPageData<OrderModel> emptySearchPageData = new SearchPageData<OrderModel>();
			emptySearchPageData.setPagination(new PaginationData());
			emptySearchPageData.setResults(new ArrayList<OrderModel>());
			emptySearchPageData.setSorts(new ArrayList<SortData>());
			return emptySearchPageData;
		}

		sortQueries = Arrays.asList(
				createSortQueryData(Order.SortOption.DEFAULT_SORT_CODE, selectQuery + ORDERBY_DATE + SORT_DESCENDING),
				createSortQueryData(Order.SortOption.ORDER_DATE_OLDEST_TO_NEWEST, selectQuery + ORDERBY_DATE),
				createSortQueryData(Order.SortOption.ORDER_NUMBER_INCREASING, selectQuery + ORDERBY_NUMBER),
				createSortQueryData(Order.SortOption.ORDER_NUMBER_DECREASING, selectQuery + ORDERBY_NUMBER + SORT_DESCENDING),
				createSortQueryData(Order.SortOption.ORDER_TOTAL_INCREASING, selectQuery + ORDERBY_TOTAL),
				createSortQueryData(Order.SortOption.ORDER_TOTAL_DECREASING, selectQuery + ORDERBY_TOTAL + SORT_DESCENDING),		
				createSortQueryData(Order.SortOption.PO_NUMBER_INCREASING, selectQuery + PO_NUMBER ),
				createSortQueryData(Order.SortOption.PO_NUMBER_DECREASING, selectQuery + PO_NUMBER + SORT_DESCENDING),
				createSortQueryData(Order.SortOption.ORDER_TYPE_INCREASING, selectQuery + ORDER_TYPE),
				createSortQueryData(Order.SortOption.ORDER_TYPE_DECREASING, selectQuery + ORDER_TYPE + SORT_DESCENDING),
				createSortQueryData(Order.SortOption.SHIP_TO_INCREASING, selectQuery + SHIP_TO),
				createSortQueryData(Order.SortOption.SHIP_TO_DECREASING, selectQuery + SHIP_TO + SORT_DESCENDING),
				createSortQueryData(Order.SortOption.CHANNEL_INCREASING, selectQuery + CHANNEL),
				createSortQueryData(Order.SortOption.CHANNEL_DECREASING, selectQuery + CHANNEL + SORT_DESCENDING),
				createSortQueryData(Order.SortOption.STATUS_INCREASING, selectQuery + STATUS),
				createSortQueryData(Order.SortOption.STATUS_DECREASING, selectQuery + STATUS + SORT_DESCENDING));

		final List<SortQueryData> finalSortQueries = sortQueries;

		return sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public SearchPageData<OrderModel> execute()
			{
				return pagedFlexibleSearchService.search(finalSortQueries, sortCode, queryParams, pageableData);
			}
		}, userService.getAdminUser());
	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}

	protected String getSelectQuery(final JnjGTOrderHistoryForm form, final Map<String, Object> queryParams,
			final List<String> userDivs)
	{
		final StringBuffer query = new StringBuffer();
		boolean isAdmin = false;
		if (null != userDivs && sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALES_REP_USER).equals(Boolean.TRUE))
		{
			//This query will be executed when the user filters with Franchise
			if(form.getFranchise() != null && !form.getFranchise().isEmpty()){
				query.append(SALES_REP_ORDER_FRANCHISE);
				queryParams.put("SalesOrgs", userDivs);
			}else{
				query.append(SALES_REP_ORDER);
				queryParams.put("SalesOrgs", userDivs);
			}
			
		}//This query will be executed when the user filters with Franchise
		else if (form.getFranchise() != null && !form.getFranchise().isEmpty()){
			query.append(SELECT_FROM_ORDER_FRANCHISE);		}
		else
		{
			query.append(SELECT_FROM_ORDER);
		}
				
		//Changes for Order History Enhancement for Access to Specific Franchise AAOL-5219
				if(sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE)!=null && 
				(sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE).equals(JnjGTUserTypes.PORTAL_ADMIN))) {
			isAdmin = true;
		}
		//No Filter needed for Admin User
		if(!isAdmin){
			final String allowedFranchise = sessionService.getAttribute("allowedFranchise").toString();
			if (form.getFranchise() != null && !form.getFranchise().isEmpty()){
				if(StringUtils.isNotEmpty(allowedFranchise)){
					query.append(" AND {cat:code} IN (" + allowedFranchise + ")");
				}
			}
			
		}
		if (form.getChannel() != null && !form.getChannel().isEmpty())
		{
			query.append(AND_CHANNEL_EQUAL_TO);
			final List<String> selectedChannelValues = new ArrayList<>();

			/*** Fetch selected channel values from the configuration. ***/
			switch (form.getChannel())
			{
				case Order.ChannelOption.OTHER:
					selectedChannelValues.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_OTHER_VALUES,
							Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Order.ChannelOption.FAX:
					selectedChannelValues.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_FAX_VALUES,
							Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Order.ChannelOption.EMAIL:
					selectedChannelValues.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_EMAIL_VALUES,
							Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Order.ChannelOption.VMI:
					selectedChannelValues.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_VMI_VALUES,
							Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Order.ChannelOption.EDI:
					selectedChannelValues.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_EDI_VALUES,
							Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Order.ChannelOption.PHONE:
					selectedChannelValues.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_PHONE_VALUES,
							Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Order.ChannelOption.WEB:
					selectedChannelValues.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_WEB_VALUES,
							Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;
			}
			queryParams.put("poTypes", selectedChannelValues);
		}

		if (form.getOrderStatus() != null && !form.getOrderStatus().isEmpty())
		{
			query.append(AND_STATUS_EQUAL_TO);
			queryParams.put(OrderModel.STATUS, form.getOrderStatus().trim());
		}

		if (form.getOrderType() != null && !form.getOrderType().isEmpty())
		{
			query.append(AND_TYPE_EQUAL_TO);
			queryParams.put(OrderModel.ORDERTYPE, form.getOrderType().trim());
		}

		if (form.getLineStatus() != null && !form.getLineStatus().isEmpty())
		{
			query.append(AND_LINE_STATUS_EQUAL_TO);
			queryParams.put(AbstractOrderEntryModel.STATUS, form.getLineStatus().trim());
		}

		if (form.isSearchRequest() && form.getStartDate() != null && form.getEndDate() != null && form.getStartDate().trim().length()>0 && form.getEndDate().trim().length()>0)
		{
			query.append(AND_DATE_BETWEEN);
			queryParams.put("startDate", form.getStartDate());
			queryParams.put("DATE_VALUE", DATE_FORMAT_SQL);
			queryParams.put("endDate", form.getEndDate());
			
		}
		//Changes for Order History Enhancement AAOL-4732
		if (form.getFranchise() != null && !form.getFranchise().isEmpty())
		{
			query.append(PRODUCT_CATEGORY_LIKE1);
			queryParams.put("franchise", LIKE_WILDCARD_CHARACTER + form.getFranchise().trim() + LIKE_WILDCARD_CHARACTER);
		}
		//Changes for Order History Enhancement AAOL-4732,4796
		/*if (form.getSurgeonId() != null && !form.getSurgeonId().isEmpty())
		{
			query.append(SURGEON_ID_LIKE);
			queryParams.put("surgeonId",form.getSurgeonId());
		}*/
		
		if (form.getSearchBy() != null && (form.getSearchText() != null && !form.getSearchText().trim().isEmpty()))
		{
			switch (form.getSearchBy().trim())
			{
				case Order.SearchOption.ORDER_NUMBER:
					query.append(AND_ORDER_CODE_LIKE);
					queryParams.put(OrderModel.ORDERNUMBER, LIKE_WILDCARD_CHARACTER + form.getSearchText().trim()
							+ LIKE_WILDCARD_CHARACTER);
					break;

				case Order.SearchOption.PRODUCT_NUMBER:
					final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
					final String searchByProdCode = form.getSearchText().trim();

					/*** Pre-check if product exists or not before executing main query. ***/
					/*** CR 107: REGEX LIKE search for Consumer Order, else exact search for MDD. ***/
					/** Modified for JJEPIC 334 to search product without mod code in order history **/
					if (currentSite.equals(Jnjb2bCoreConstants.MDD))
					{
						List<JnJProductModel> products = jnjGTProductDao.getMDDProductByMODValue(searchByProdCode, true);
						ArrayList<String> sbProducts = new ArrayList<String>();
						for (int i = 0; i < products.size(); i++)
						{
							sbProducts.add(products.get(i).getPk().toString());

						}
						if (sbProducts.isEmpty())
						{
							return null;
						}

						query.append(AND_PRODUCT_CODE_LIKE);
						queryParams.put("PRODUCTPK", Arrays.asList(sbProducts.toArray()));
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug("JnjGTOrderDaoImpl-getSelectQuery() : the query is:" + query.toString() + " queryParams "
									+ queryParams.toString());
						}
					}
					else
					{
						ProductModel product = jnjGTProductDao.getProductByPartialValue(searchByProdCode, true, currentSite, true);
						if (product == null)
						{
							return null;
						}

						query.append(AND_PRODUCT_CODE_LIKE);
						queryParams.put("PRODUCTPK", product.getPk().toString());
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug("JnjGTOrderDaoImpl-getSelectQuery() : other than MDD query is:" + query.toString()
									+ " queryParams " + queryParams.toString());
						}
					}
					break;

				case Order.SearchOption.PO_NUMBER:
					query.append(AND_PO_NUMBER_LIKE);
					queryParams.put(OrderModel.PURCHASEORDERNUMBER, LIKE_WILDCARD_CHARACTER + form.getSearchText().trim()
							+ LIKE_WILDCARD_CHARACTER);
					break;

				case Order.SearchOption.DEALER_PO_NUMBER:
					query.append(AND_DEALER_PO_LIKE);
					queryParams.put(AbstractOrderModel.DEALERPONUM, LIKE_WILDCARD_CHARACTER + form.getSearchText().trim()
							+ LIKE_WILDCARD_CHARACTER);
					break;

				case Order.SearchOption.INVOICE_NUMBER:
					query.append(AND_INVOICE_NUMBER_LIKE);
					queryParams.put("invoiceNumber", LIKE_WILDCARD_CHARACTER + form.getSearchText().trim() + LIKE_WILDCARD_CHARACTER);
					break;

				default:
					break;
			}
		}
		return query.toString();
	}

	@Override
	public List<String> getDeliveryBlocksForEntries(final String orderPK)
	{
		final Map params = new HashMap();
		params.put("orderPK", orderPK);

		final String query = "SELECT DISTINCT {billingdeliveryblock} FROM {OrderEntry} WHERE {order}=?orderPK";
		final List resultClassList = new ArrayList();
		resultClassList.add(String.class);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);
		fQuery.setResultClassList(resultClassList);
		return flexibleSearchService.<String> search(fQuery).getResult();
	}

	@Override
	public JnjGTTerritoryDivisonModel getSalesTerritoryByShipToNum(final String shipToNumber)
	{
		final Map params = new HashMap();
		params.put("shipToNumber", shipToNumber);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_TERRITORY_DIV_BY_SHIP_TO_NUMBER);
		fQuery.addQueryParameters(params);
		final List<JnjGTTerritoryDivisonModel> result = flexibleSearchService.<JnjGTTerritoryDivisonModel> search(fQuery)
				.getResult();
		return CollectionUtils.isNotEmpty(result) ? result.get(0) : null;
	}

	@Override
	public Collection<OrderModel> getOrdersEligibleForShipmentNotification()
	{
		final List<OrderModel> result = flexibleSearchService.<OrderModel> search(
				SELECT_ORDERS_ELIGIBLE_FOR_SHIP_EMAIL_NOTIFICATION).getResult();
		return result;
	}

	@Override
	public Collection<OrderEntryModel> getBackordersEligibleForEmailNotification()
	{
		final BaseStoreModel mddBaseStore = baseStoreService.getBaseStoreForUid(Jnjb2bCoreConstants.MDD_STORE_ID);
		final Map params = new HashMap();
		params.put(OrderModel.STORE, mddBaseStore.getPk().toString());
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_BACKORDERS_FOR_EMAIL_NOTIFICATION);
		fQuery.addQueryParameters(params);

		final List<OrderEntryModel> result = flexibleSearchService.<OrderEntryModel> search(fQuery).getResult();
		return result;
	}

	@Override
	public List<OrderModel> getOrdersByTypeAndCreationTime(final String orderType, final Date endDate)
	{
		final String query = "Select {pk} from {order} where {order:orderType} = ({{SELECT {PK} FROM {JnjOrderTypesEnum} WHERE {CODE}=?orderType}}) AND {order:creationtime}<?endDate";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final Map params = new HashMap();
		params.put("orderType", orderType);
		params.put("endDate", endDate);
		fQuery.addQueryParameters(params);
		final List<OrderModel> result = flexibleSearchService.<OrderModel> search(fQuery).getResult();
		return result;
	}

	@Override
	public Collection<OrderModel> getOrdersEligibleForEmailNotification() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.na.core.dao.order.JnjGTOrderDao#getBackordersAndOpenLinesEligibleForEmailNotification(java.util.List,
	 * int)
	 */
	@Override
	public Collection<OrderEntryModel> getBackordersAndOpenLinesEligibleForEmailNotification(List<String> b2bUnitListPerUser,
			int orderHistoryDays)
	{

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(BACKORDER_AND_OPEN_LINE_EMAIL_QUERY);
		List<OrderEntryModel> result = new ArrayList<OrderEntryModel>();
		Map params = new HashMap();
		params.put("orderTypes", createOrderTypesList());
		params.put("accountNumbers", b2bUnitListPerUser);
		params.put("days", Integer.valueOf(orderHistoryDays));
		params.put("lineStatus", createLineStatusList());
		fQuery.addQueryParameters(params);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME,
				"getBackordersAndOpenLinesEligibleForEmailNotification()", "Flex Query: " + fQuery, LOGGER);
		final List<OrderEntryModel> openLineBackorderResult = flexibleSearchService.<OrderEntryModel> search(fQuery).getResult();

		final List<OrderEntryModel> cancelLsResult = getCancelledLinesEligibleForEmailNotification(b2bUnitListPerUser,
				orderHistoryDays);
		if (openLineBackorderResult != null)
		{
			result.addAll(openLineBackorderResult);
		}
		if (cancelLsResult != null)
		{
			result.addAll(cancelLsResult);
		}
		return result;
	}
	
	@Override
	public OrderModel getOrder(final String orderCode, final List<String> salesOrgCodes, final Boolean salesRep)
	{
		OrderModel order = null;
		final Map params = new HashMap();
		final StringBuilder validAccountQuery = jnjGTCustomerDAO.getAccountsMapQuery(true, false, null, params);
		params.put("currentUserUid", userService.getCurrentUser().getUid());
		
		
		FlexibleSearchQuery fQuery = null;
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (BooleanUtils.isTrue(salesRep) && StringUtils.equalsIgnoreCase(currentSite, Jnjb2bCoreConstants.MDD))
		{
			fQuery = new FlexibleSearchQuery(
					"SELECT DISTINCT {PK} FROM {Order AS order JOIN JnJB2BUnit AS unit ON {order:unit} = {unit:pk} JOIN orderEntry AS oe ON {order.pk}={oe.order} JOIN JNJPRoduct as product on {product:pk}={oe:product}} WHERE {unit} IN ({{"
							+ validAccountQuery + "}}) AND {product:SalesOrgCode} IN (?salesOrgs)  and {code}=?orderCode");
			params.put("salesOrgs", salesOrgCodes);
		}
		else
		{
			fQuery = new FlexibleSearchQuery(
					"SELECT DISTINCT {PK} FROM {Order} WHERE {unit} IN ({{" + validAccountQuery + "}}) and {code}=?orderCode");
		}
		params.put("orderCode", orderCode);
		fQuery.addQueryParameters(params);
		final List<OrderModel> result = flexibleSearchService.<OrderModel> search(fQuery).getResult();

		if (CollectionUtils.isNotEmpty(result))
		{
			order = result.get(0);
		}
		return order;
	}
	
	public List<OrderEntryModel> getCancelledLinesEligibleForEmailNotification(List<String> b2bUnitListPerUser,
			int orderHistoryDays)
	{
		//For cancelled line status to stay only for 14 days on the report
		final FlexibleSearchQuery cancelledLsFQuery = new FlexibleSearchQuery(CANCELLED_LS_OPEN_LINE_EMAIL_QUERY);
		Map params = new HashMap();
		params.put("orderTypes", createOrderTypesList());
		params.put("accountNumbers", b2bUnitListPerUser);
		params.put("days", Integer.valueOf(orderHistoryDays));
		//params.put("lineStatus", createLineStatusList());

		DateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -14);
		Date fourteenDaysPrior = cal.getTime();
		String fromdate = dateFormat.format(fourteenDaysPrior);
		/** pick records with line status CANCELLED within fourteen days **/
		params.put(QUERY_PARAM__FOURTEEN, fromdate);
		cancelledLsFQuery.addQueryParameters(params);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, "getCancelledLinesEligibleForEmailNotification()",
				"Flex Query: " + cancelledLsFQuery, LOGGER);
		final List<OrderEntryModel> cancelledLsResult = flexibleSearchService.<OrderEntryModel> search(cancelledLsFQuery)
				.getResult();
		return cancelledLsResult;
	}

	private List<String> createOrderTypesList()
	{
		List<String> orderTypesList = new ArrayList<String>();
		/** Setting order types for back order **/
		orderTypesList.add(JnjOrderTypesEnum.YKB.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZBRO.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZCQ.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZDEL.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZEX.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZKB.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZNC.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZOR.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZREC.getCode());
		orderTypesList.add(JnjOrderTypesEnum.ZVMI.getCode());
		return orderTypesList;
	}

	private List<String> createLineStatusList()
	{
		final List<String> lineStatusList = new ArrayList<String>();
		lineStatusList.add("UC");
		//lineStatusList.add("RJ");
		lineStatusList.add("CS");
		lineStatusList.add("CQ");
		lineStatusList.add("CC");
		return lineStatusList;

	}

	@Override
	public Collection<OrderEntryModel> getShippedOrdersEligibleForEmailNotification(
			List<String> b2bUnitListPerUser, int orderHistoryDays) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Map<JnJB2bCustomerModel, List<OrderEntryModel>> getCancelOrderLineItems() {
		
		FlexibleSearchQuery qFlexibleSearchQuery = new FlexibleSearchQuery(SELECT_CANCEL_ORDER_LINE_ITMES);
		List<OrderEntryModel> listOfOrderEntry = null;
		List<OrderEntryModel> result = flexibleSearchService.<OrderEntryModel> search(qFlexibleSearchQuery).getResult();
		Map<JnJB2bCustomerModel,List<OrderEntryModel>> map = new HashMap<JnJB2bCustomerModel, List<OrderEntryModel>>();
		for(OrderEntryModel orderEntry: result)
		{
			JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) orderEntry.getOrder().getUser();
			if(map.containsKey(jnJB2bCustomerModel))
			{
				listOfOrderEntry.add(orderEntry);
				map.put(jnJB2bCustomerModel, listOfOrderEntry);
			}
			else
			{
				listOfOrderEntry =new ArrayList<OrderEntryModel>();
				listOfOrderEntry.add(orderEntry);
				map.put(jnJB2bCustomerModel, listOfOrderEntry);
			}
		}
		return map;
	}
}
