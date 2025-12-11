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
package com.jnj.la.core.dao.order.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order.ChannelOption;
import com.jnj.core.dao.order.impl.DefaultJnjGTOrderDao;
import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.order.JnjLatamOrder;
import com.jnj.la.core.services.JnJLaProductService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JnjLatamOrderImpl extends DefaultJnjGTOrderDao implements JnjLatamOrder {

	protected static final Logger LOGGER = Logger.getLogger(JnjLatamOrderImpl.class);

	private static final String SELECT_FROM_ORDER = "SELECT DISTINCT {order:PK}, {order:ORDERNUMBER}, {order:PURCHASEORDERNUMBER},  {order:ORDERTYPE}, {order:SHIPTOACCOUNT}, {order:CREATIONTIME}, {order:DISTRIBUTIONCHANNEL}, {order:STATUS} ,{order:TOTALPRICE}, {order:CONTRACTNUMBER} FROM {Order AS order JOIN JnJLaB2BUnit AS unit ON {order:unit} = {unit:pk}} WHERE {unit:uid} IN (?selectedAccounts) ";
	private static final String SALES_REP_ORDER = "SELECT DISTINCT {PK}, {ORDERNUMBER}, {CREATIONTIME},  {TOTALPRICE} FROM {Order AS order JOIN JnJLaB2BUnit AS unit ON {order:unit} = {unit:pk} JOIN orderEntry AS oe ON {order.pk}={oe.order} JOIN JnJLaProduct as product on {product:pk}={oe:product}} WHERE {unit:uid} IN (?selectedAccounts) AND {product:SalesOrgCode} IN (?SalesOrgs) ";

	private static final String AND_CHANNEL_EQUAL_TO = "AND {poType} IN (?poTypes) ";
	private static final String AND_STATUS_EQUAL_TO = "AND {order:status} IN ({{SELECT {PK} FROM {ORDERSTATUS} WHERE {CODE} LIKE (?status)}}) ";
	private static final String AND_TYPE_EQUAL_TO = "AND {order:orderType} = ({{SELECT {PK} FROM {JnjOrderTypesEnum} WHERE {CODE}=?orderType}}) ";
	private static final String AND_LINE_STATUS_EQUAL_TO = "AND {order:PK} IN ({{ SELECT {ENTRY:ORDER} FROM {ORDERENTRY AS ENTRY JOIN ORDER as ORD ON {ENTRY:order}={ORD:PK}} WHERE UPPER({ENTRY:status}) LIKE UPPER(?status)}}) ";
    private static final String AND_DATE_BETWEEN = "AND {order:creationtime} BETWEEN CONVERT(DATETIME,?startDate) AND  CONVERT(DATETIME,?endDate) ";
	private static final String AND_ORDER_CODE_LIKE = "AND ({order:orderNumber} LIKE ?orderNumber OR {order:sapOrderNumber} LIKE ?orderNumber) ";
	private static final String AND_PRODUCT_CODE_LIKE = "AND {order:PK} IN ({{ SELECT {ENTRY:ORDER} FROM {ORDERENTRY AS ENTRY JOIN ORDER as ORD ON {ENTRY:order}={ORD:PK} JOIN PRODUCT AS PROD ON {ENTRY:PRODUCT} = {PROD:PK}} WHERE {PROD:PK}=?PRODUCTPK }})";
	private static final String AND_PO_NUMBER_LIKE = "AND UPPER({purchaseOrderNumber}) LIKE UPPER(?purchaseOrderNumber) ";
	private static final String AND_DEALER_PO_LIKE = " AND {dealerPONum} LIKE ?dealerPONum ";
    private static final String AND_INVOICE_NUMBER_LIKE = "AND {order:PK} IN ({{ SELECT {INVOICE:ORDER} FROM {JNJINVOICEORDER AS INVOICE JOIN ORDER as ORD ON {INVOICE:order}={ORD:PK}} WHERE {INVOICE:invDocNo} LIKE ?invDocNo}}) ";
    private static final String AND_INVOICE_BR_NUMBER_LIKE = "AND {order:PK} IN ({{ SELECT {INVOICE:ORDER} FROM {JNJINVOICEORDER AS INVOICE JOIN ORDER as ORD ON {INVOICE:order}={ORD:PK}} WHERE {INVOICE:nfNumber} LIKE ?nfNumber}}) ";

	private static final String ORDERBY_DATE = "ORDER BY {order:creationtime} ";
	private static final String ORDERBY_NUMBER = "ORDER BY {order:orderNumber} ";
	private static final String ORDERBY_SAP_NUMBER = "ORDER BY {order:sapOrderNumber}, {order:code} ";
	private static final String ORDERBY_SAP_NUMBER_DESC = "ORDER BY {order:sapOrderNumber} DESC, {order:code} DESC";
	private static final String ORDERBY_TOTAL = "ORDER BY {order:totalPrice} ";
	protected static final String PO_NUMBER = "ORDER BY {order:purchaseOrderNumber} ";
	protected static final String ORDER_TYPE = "ORDER BY {order:orderType} ";
	protected static final String SHIP_TO = "ORDER BY {order:shipToAccount} ";
	protected static final String STATUS = "ORDER BY {order:status} ";
	protected static final String CHANNEL = "ORDER BY {order:distributionChannel} ";

	protected static final String SORT_DESCENDING = "DESC";
	protected static final String LIKE_WILDCARD_CHARACTER = "%";
	protected static final String CONTRACT_NUMBER = "ORDER BY {order:contractNumber} ";

	private static final String START_DATE = "startDate";
	private static final String END_DATE = "endDate";

	private static final String SESSION_LANGUAGE = "sessionLanguage";
    private static final String ENGLISH_DATE_MASK = "MM/dd/yyyy";

    @Autowired
    protected FlexibleSearchService flexibleSearchService;

    @Autowired
    protected JnJLaProductService jnjLaProductService;

    @Autowired
    private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

    @Override
	public SearchPageData<OrderModel> getPagedOrderHistoryForStatuses(final PageableData pageableData, final JnjGTOrderHistoryForm form) {
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("selectedAccounts", Arrays.asList(form.getAccounts()));

        String selectQuery = createSelectQuery(form, queryParams);
		if (StringUtils.isEmpty(selectQuery)) {
            return createEmptySearchPageData();
		}

        final List<SortQueryData> sortQueries = createSortQueries(selectQuery);
        final String defaultSortCode = (form.getSortCode() == null) ? Order.SortOption.DEFAULT_SORT_CODE : form.getSortCode();
        return sessionService.executeInLocalView(new SessionExecutionBody() {
			@Override
			public SearchPageData<OrderModel> execute() {
				return pagedFlexibleSearchService.search(sortQueries, defaultSortCode, queryParams, pageableData);
			}
		}, userService.getAdminUser());
	}

    private String createSelectQuery(JnjGTOrderHistoryForm form, Map<String, Object> queryParams) {
        String selectQuery = null;
        if (sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALES_REP_USER).equals(Boolean.TRUE)) {
            final List<String> userDivs = form.getUserDivisions();
            if (userDivs !=  null) {
				userDivs.removeIf(Objects::isNull);
			}

            if (CollectionUtils.isNotEmpty(userDivs)) {
                selectQuery = form.isResetSelection() ? SALES_REP_ORDER : getSelectQuery(form, queryParams, userDivs);
                queryParams.put("SalesOrgs", userDivs);
            }
        } else {
            selectQuery = form.isResetSelection() ? SELECT_FROM_ORDER : getSelectQuery(form, queryParams, null);
        }
        return selectQuery;
    }

    private SearchPageData<OrderModel> createEmptySearchPageData() {
        final SearchPageData<OrderModel> emptySearchPageData = new SearchPageData<>();
        emptySearchPageData.setPagination(new PaginationData());
        emptySearchPageData.setResults(new ArrayList<>());
        emptySearchPageData.setSorts(new ArrayList<>());
        return emptySearchPageData;
    }

    private List<SortQueryData> createSortQueries(String selectQuery) {
        return Arrays.asList(
            createSortQueryData(Order.SortOption.DEFAULT_SORT_CODE, selectQuery + ORDERBY_DATE + SORT_DESCENDING),
            createSortQueryData(Order.SortOption.ORDER_DATE_OLDEST_TO_NEWEST, selectQuery + ORDERBY_DATE),
            createSortQueryData(Order.SortOption.ORDER_NUMBER_INCREASING, selectQuery + ORDERBY_NUMBER),
            createSortQueryData(Order.SortOption.ORDER_NUMBER_DECREASING, selectQuery + ORDERBY_NUMBER + SORT_DESCENDING),
            createSortQueryData(Order.SortOption.ORDER_TOTAL_INCREASING, selectQuery + ORDERBY_TOTAL),
            createSortQueryData(Order.SortOption.ORDER_TOTAL_DECREASING, selectQuery + ORDERBY_TOTAL + SORT_DESCENDING),
            createSortQueryData(Order.SortOption.PO_NUMBER_INCREASING, selectQuery + PO_NUMBER),
            createSortQueryData(Order.SortOption.PO_NUMBER_DECREASING, selectQuery + PO_NUMBER + SORT_DESCENDING),
            createSortQueryData(Order.SortOption.ORDER_TYPE_INCREASING, selectQuery + ORDER_TYPE),
            createSortQueryData(Order.SortOption.ORDER_TYPE_DECREASING, selectQuery + ORDER_TYPE + SORT_DESCENDING),
            createSortQueryData(Order.SortOption.SHIP_TO_INCREASING, selectQuery + SHIP_TO),
            createSortQueryData(Order.SortOption.SHIP_TO_DECREASING, selectQuery + SHIP_TO + SORT_DESCENDING),
            createSortQueryData(Order.SortOption.CHANNEL_INCREASING, selectQuery + CHANNEL),
            createSortQueryData(Order.SortOption.CHANNEL_DECREASING, selectQuery + CHANNEL + SORT_DESCENDING),
            createSortQueryData(Order.SortOption.STATUS_INCREASING, selectQuery + STATUS),
            createSortQueryData(Order.SortOption.STATUS_DECREASING, selectQuery + STATUS + SORT_DESCENDING),
            createSortQueryData(Jnjlab2bcoreConstants.Order.SortOption.CONTRACT_NUMBER_INCREASING, selectQuery + CONTRACT_NUMBER),
            createSortQueryData(Jnjlab2bcoreConstants.Order.SortOption.CONTRACT_NUMBER_DECREASING, selectQuery + CONTRACT_NUMBER + SORT_DESCENDING),
            createSortQueryData(Jnjlab2bcoreConstants.Order.SortOption.SAP_NUMBER_INCREASING, selectQuery + ORDERBY_SAP_NUMBER),
            createSortQueryData(Jnjlab2bcoreConstants.Order.SortOption.SAP_NUMBER_DECREASING, selectQuery + ORDERBY_SAP_NUMBER_DESC)
        );
    }

    @Override
	protected String getSelectQuery(final JnjGTOrderHistoryForm form, final Map<String, Object> queryParams, final List<String> userDivs) {
		final StringBuilder query = new StringBuilder();
		if (null != userDivs && sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALES_REP_USER).equals(Boolean.TRUE)) {
			query.append(SALES_REP_ORDER);
			queryParams.put("SalesOrgs", userDivs);
		} else {
			query.append(SELECT_FROM_ORDER);
		}

		if (form.getChannel() != null && !form.getChannel().isEmpty()) {
			query.append(AND_CHANNEL_EQUAL_TO);
			final List<String> selectedChannelValues = new ArrayList<>();

			/*** Fetch selected channel values from the configuration. ***/
			switch (form.getChannel()) {
				case Jnjlab2bcoreConstants.Order.OrderChannel.CHANNEL_E008:
					selectedChannelValues
							.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_OTHER_VALUES, Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Jnjlab2bcoreConstants.Order.OrderChannel.CHANNEL_M002:
					selectedChannelValues
							.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_FAX_VALUES, Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Jnjlab2bcoreConstants.Order.OrderChannel.CHANNEL_M004:
					selectedChannelValues
							.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_EMAIL_VALUES, Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Jnjlab2bcoreConstants.Order.OrderChannel.CHANNEL_E001:
					selectedChannelValues
							.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_EDI_VALUES, Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Jnjlab2bcoreConstants.Order.OrderChannel.CHANNEL_DFUE:
					selectedChannelValues
							.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_PHONE_VALUES, Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Jnjlab2bcoreConstants.Order.OrderChannel.CHANNEL_E004:
					selectedChannelValues
							.addAll(JnJCommonUtil.getValues(ChannelOption.CHANNEL_WEB_VALUES, Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;

				case Jnjlab2bcoreConstants.Order.OrderChannel.CHANNEL_E006:
					selectedChannelValues.addAll(JnJCommonUtil.getValues(
							Jnjlab2bcoreConstants.Order.OrderChannel.CHANNEL_SERVICE_VALUES, Jnjb2bCoreConstants.SYMBOl_COMMA));
					break;
			}
			queryParams.put("poTypes", selectedChannelValues);
		}

		if (form.getOrderStatus() != null && !form.getOrderStatus().isEmpty()) {
			final String orderStatus ='%'+form.getOrderStatus().replace(" ", "_");
			query.append(AND_STATUS_EQUAL_TO);
			queryParams.put(OrderModel.STATUS,orderStatus);
		}

		if (form.getOrderType() != null && !form.getOrderType().isEmpty()) {
			query.append(AND_TYPE_EQUAL_TO);
			queryParams.put(OrderModel.ORDERTYPE, form.getOrderType().trim());
		}

		if (form.getLineStatus() != null && !form.getLineStatus().isEmpty()) {
			final String lineStatus ='%'+form.getLineStatus().replace(" ", "_").toUpperCase(Locale.ENGLISH);
			query.append(AND_LINE_STATUS_EQUAL_TO);
			queryParams.put(AbstractOrderEntryModel.STATUS, lineStatus);
		}

        addDateFilter(form, queryParams, query);

		if (form.getSearchBy() != null && (form.getSearchText() != null && !form.getSearchText().trim().isEmpty())) {
			switch (form.getSearchBy().trim())
			{
				case Order.SearchOption.ORDER_NUMBER:
					query.append(AND_ORDER_CODE_LIKE);
					queryParams.put(OrderModel.ORDERNUMBER,
							LIKE_WILDCARD_CHARACTER + form.getSearchText().trim() + LIKE_WILDCARD_CHARACTER);
					break;

				case Order.SearchOption.PRODUCT_NUMBER:
					final String searchByProdCode = form.getSearchText().trim();
					JnJProductModel product = null;
					try
					{
						product = jnjLaProductService.getProductForCatalogId(searchByProdCode);
					}
					catch (final BusinessException ex)
					{
						LOGGER.error(
								Jnjlab2bcoreConstants.Logging.ORDER_HISTORY + Jnjlab2bcoreConstants.Logging.HYPHEN + "createOrder()"
										+ Jnjlab2bcoreConstants.Logging.HYPHEN + "Business Exception occured " + ex.getMessage(),
								ex);
					}
					if (product == null)
					{
						return null;
					}

					query.append(AND_PRODUCT_CODE_LIKE);
					queryParams.put("PRODUCTPK", product.getPk().toString());
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Jnjlab2bcoreConstants.Logging.ORDER_HISTORY + " - " + query.toString() + " queryParams "
								+ queryParams.toString());
					}
					break;

				case Order.SearchOption.PO_NUMBER:
					query.append(AND_PO_NUMBER_LIKE);
					queryParams.put(OrderModel.PURCHASEORDERNUMBER,
							LIKE_WILDCARD_CHARACTER + form.getSearchText().trim() + LIKE_WILDCARD_CHARACTER);
					break;

				case Order.SearchOption.DEALER_PO_NUMBER:
					query.append(AND_DEALER_PO_LIKE);
					queryParams.put(AbstractOrderModel.DEALERPONUM,
							LIKE_WILDCARD_CHARACTER + form.getSearchText().trim() + LIKE_WILDCARD_CHARACTER);
					break;

				case Order.SearchOption.INVOICE_NUMBER:
					appendInvoiceNumberParameter(query, queryParams, form);
					break;

				default:
					break;
			}
		}

		return query.toString();
	}

    private void addDateFilter(JnjGTOrderHistoryForm form, Map<String, Object> queryParams, StringBuilder query) {
        Boolean hasStartDate = hasValidDate(form.getStartDate());
        Boolean hasEndDate = hasValidDate(form.getEndDate());

        if (form.isSearchRequest() && (hasStartDate || hasEndDate)) {
            query.append(AND_DATE_BETWEEN);
            queryParams.put(START_DATE, prepareStartDate(form.getStartDate()));
            queryParams.put(END_DATE, prepareEndDate(form.getEndDate()));
        }
    }

    private String prepareStartDate(String date) {
        if (hasValidDate(date)) {
            if (isEnglishLanguage()) {
                return date;
            }
            final String[] split = date.split("/");
            return split[1] + "/" + split[0] + "/" + split[2];
        }
        return getFormattedDate(new GregorianCalendar(1900, 1, 1));
    }

    private String prepareEndDate(String date) {
        if (hasValidDate(date)) {
            if (isEnglishLanguage()) {
                final String[] split = date.split("/");
                int day = Integer.parseInt(split[1]);
                return split[0] + "/" + ++day + "/" + split[2];
            }
            final String[] split = date.split("/");
            int day = Integer.parseInt(split[0]);
            return split[1] + "/" + ++day + "/" + split[2];
        }

        return getFormattedDate(new GregorianCalendar()); // current time
    }

    private static String getFormattedDate(GregorianCalendar gregorianCalendar) {
        return new SimpleDateFormat(ENGLISH_DATE_MASK).format(gregorianCalendar.getTime());
    }

    private static boolean hasValidDate(String date) {
        return date != null && date.trim().length() > 0 && date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})");
    }

    private Boolean isEnglishLanguage() {
        return getSessionService().getCurrentSession().getAttribute(SESSION_LANGUAGE).equals(Jnjlab2bcoreConstants.LANGUAGE_ENGLISH_ISO);
    }

    private void appendInvoiceNumberParameter(final StringBuilder query, final Map<String, Object> queryParams,
			final JnjGTOrderHistoryForm form)
	{
		final String countryInfo = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
		// For Brazil the field nfNumber is used as the invoice number.
		if (Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL.equals(countryInfo))
		{
			query.append(AND_INVOICE_BR_NUMBER_LIKE);
			queryParams.put("nfNumber", LIKE_WILDCARD_CHARACTER + form.getSearchText().trim() + LIKE_WILDCARD_CHARACTER);
		}
		else
		{
			query.append(AND_INVOICE_NUMBER_LIKE);
			queryParams.put("invDocNo", LIKE_WILDCARD_CHARACTER + form.getSearchText().trim() + LIKE_WILDCARD_CHARACTER);
		}
	}

    @Override
    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

}
