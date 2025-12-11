/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.la.core.daos.impl;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.dao.impl.DefaultJnjInvoiceDao;
import com.jnj.core.dao.impl.InvoiceRowMapper;
import com.jnj.core.dto.JnJInvoiceDTO;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.la.core.daos.JnjLaInvoiceDao;
import com.jnj.la.core.util.JnJLALanguageDateFormatUtil;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import de.hybris.platform.util.Config;
import com.jnj.la.core.util.JnJLaCronjobUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SearchOption.INVOICE_NUMBER;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SearchOption.NONE;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SearchOption.ORDER_NUMBER;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SearchOption.PO_NUMBER;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.DEFAULT_SORT_CODE;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.INVOICE_DATE_NEWEST_TO_OLDEST;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.INVOICE_DATE_OLDEST_TO_NEWEST;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.INVOICE_NUMBER_DECREASING;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.INVOICE_NUMBER_INCREASING;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.INVOICE_TOTAL_DECREASING;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.INVOICE_TOTAL_INCREASING;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.ORDER_NUMBER_DECREASING;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption.ORDER_NUMBER_INCREASING;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.servicelayer.config.ConfigurationService;


/**
 * This class converts the sell out reports model into list object.
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjLaInvoiceDao extends DefaultJnjInvoiceDao implements JnjLaInvoiceDao
{
    private static final Logger LOG = Logger.getLogger(DefaultJnjLaInvoiceDao.class);

    //SELECT
    private static final String SELECT_INVOICE = "SELECT {invoice:pk} FROM {" + JnJInvoiceOrderModel._TYPECODE + " as invoice %s %s} %s %s";

    //JOIN
    private static final String JOIN = " JOIN ";
    private static final String ORDER_JOIN = JOIN + OrderModel._TYPECODE + " AS order ON {invoice:" + JnJInvoiceOrderModel.ORDER + "} = {order:pk}";
    private static final String SOLD_TO_JOIN = JOIN + JnJB2BUnitModel._TYPECODE+" AS b2bunit ON {invoice:" +JnJInvoiceOrderModel.SOLDTO +"} = {b2bunit:pk}";

    //WHERE
    private static final String WHERE_ORDER_NUMBER = " WHERE {order:"+ OrderModel.SAPORDERNUMBER +"}=?"+ OrderModel.SAPORDERNUMBER;
    private static final String WHERE_PO_NUMBER = " WHERE {invoice:"+JnJInvoiceOrderModel.PONUMBER + "} LIKE CONCAT( '%',CONCAT( ?" + JnJInvoiceOrderModel.PONUMBER + " , '%' ) )";
    private static final String WHERE_SOLD_TO = " WHERE {b2bunit:"+ JnJB2BUnitModel.UID +"}=?" + JnJB2BUnitModel.UID;

    //AND
    private static final String FROM_DATE = "fromDate";
    private static final String TO_DATE = "toDate";
    private static final String AND_INVOICE = " AND {invoice:";
    private static final String AND_DATE_BETWEEN = AND_INVOICE + JnJInvoiceOrderModel.CREATIONDATE  +"} BETWEEN CONVERT(DATETIME,?fromDate) AND CONVERT(DATETIME,?toDate)";
    private static final String AND_FROM_DATE = AND_INVOICE + JnJInvoiceOrderModel.CREATIONDATE  +"} >= CONVERT(DATETIME,?"+FROM_DATE+")";
    private static final String AND_TO_DATE = AND_INVOICE + JnJInvoiceOrderModel.CREATIONDATE  +"} <= CONVERT(DATETIME,?"+TO_DATE+")";
    private static final String AND_SOLD_TO = " AND {b2bunit:"+ JnJB2BUnitModel.UID +"}=?" + JnJB2BUnitModel.UID;

    //ORDER BY
    private static final String ORDER_BY_ORDER_NUMBER = " ORDER BY {order:" + OrderModel.ORDERNUMBER + "}";
    private static final String ORDER_BY_INVOICE = " ORDER BY {invoice:";
    private static final String ORDER_BY_DATE = ORDER_BY_INVOICE + JnJInvoiceOrderModel.CREATIONDATE +"}";
    private static final String ORDER_BY_TOTAL = ORDER_BY_INVOICE + JnJInvoiceOrderModel.NETVALUE +"}";

    //SORT
    private static final String SORT_ASCENDING = " ASC ";
    private static final String SORT_DESCENDING = " DESC ";

    private static final String HOUR_FROM = " 00:00";
    private static final String HOUR_TO = " 23:59";

    private static final String SELECT_INVOICES_BY_ORDERS = "SELECT {pk} FROM {JnJInvoiceOrder} WHERE {salesOrder} IN ( ?sapOrderNumbers )";

    private JNJRSADBConnector rsaDBConnector;

    @Autowired
    protected SessionService sessionService;
    
	protected JnJLaCronjobUtil jnjLaCronjobUtil;

    @Autowired
    protected UserService userService;

    @Autowired
    private PagedFlexibleSearchService pagedFlexibleSearchService;

    @Autowired
    private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
    
	protected ConfigurationService configurationService;

	
    public JnJLaCronjobUtil getJnjLaCronjobUtil() {
		return jnjLaCronjobUtil;
	}
    
	public void setJnjLaCronjobUtil(final JnJLaCronjobUtil jnjLaCronjobUtil) {
		this.jnjLaCronjobUtil = jnjLaCronjobUtil;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	private String getWhereInvoiceNumber(){
        return " WHERE {invoice:" + getInvoiceNumberField() + "}=?" + getInvoiceNumberField();
    }

    private String getOrderByInvoiceNumber(){
        return ORDER_BY_INVOICE + getInvoiceNumberField() + "}";
    }

    protected String getInvoiceNumberField(){
        return JnJInvoiceOrderModel.INVDOCNO;
    }

    public JNJRSADBConnector getRsaDBConnector()
    {
        return rsaDBConnector;
    }

    public void setRsaDBConnector(final JNJRSADBConnector rsaDBConnector)
    {
        this.rsaDBConnector = rsaDBConnector;
    }

	@Override
	public JnJInvoiceDTO getInvoiceRecordsFormRSA(final String invoiceQuery) {

		JnJInvoiceDTO invoiceRecords = null;
		Map<String, List<?>> resultMap = new HashMap<>();

		int retryAttempt = 0;
		int connectionWaitPeriod = 0;

		try {
			retryAttempt = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.RETRY_ATTEMPT));
			connectionWaitPeriod = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));
		} catch (final NumberFormatException e) {
			LOG.error("Can not convert string to integer: - " + e.getMessage());
		}

		try{
			for (int count = 0; count < retryAttempt; count++) {

				if(count != 0)
				{
					TimeUnit.SECONDS.sleep(connectionWaitPeriod); 
				}
				
				final boolean reconnectDB = jnjLaCronjobUtil.getResultsFromDB(invoiceQuery, resultMap, JnJInvoiceDTO.class);

				LOG.info("ReconnectDB value: "+reconnectDB);
				
				if (!reconnectDB) {
					break;
				}

			}
		}
		catch(InterruptedException e)
		{
			LOG.error(e.getMessage(), e);
		}
		if (resultMap.get(JnJInvoiceDTO.class.getSimpleName()) instanceof List<?>) {
			final List<JnJInvoiceDTO> invoiceResults = (List<JnJInvoiceDTO>) resultMap.get(JnJInvoiceDTO.class.getSimpleName());
			if (!invoiceResults.isEmpty()) {
				invoiceRecords = invoiceResults.get(0);
			}
		}


		return invoiceRecords;
	}

    @Override
    public List<JnJInvoiceOrderModel> getInvoiceOrderData(final String invDocNo) {
        List<JnJInvoiceOrderModel> result = null;
        final Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("invDocNo", invDocNo);

        try {
            String query = "select {pk} from {JnJInvoiceOrder as p} where {p:invDocNo}=?invDocNo";

            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.addQueryParameters(queryParams);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing query for obtaining JnJInvoiceOrderModel records.");
                LOG.debug("Query fired is--> " + query);
            }
            result = getFlexibleSearchService().<JnJInvoiceOrderModel> search(fQuery).getResult();
        }
        catch (final Exception exp) {
            LOG.error("Error while executing query to get JnJInvoiceOrderModel records in JnjInvoiceDaoImpl class - "
                    + exp.getMessage());
            LOG.error(exp);
        }
        return result;
    }

    @Override
    public SearchPageData<JnJInvoiceOrderModel> getInvoiceOrderData(JnjPageableData pageableData) {
        final Map<String, Object> queryParams = new HashMap<>();

        final String selectQuery = createSelectQuery(pageableData, queryParams);
        if (StringUtils.isEmpty(selectQuery)) {
            JnjGTCoreUtil.logErrorMessage("Invoice History","getInvoiceOrderData",
                "Couldn't form query", DefaultJnjLaInvoiceDao.class);
            return null;
        }

        final List<SortQueryData> sortQueries = createSortQueries(selectQuery);
        return sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public SearchPageData<JnJInvoiceOrderModel> execute() {
                return pagedFlexibleSearchService.search(sortQueries, DEFAULT_SORT_CODE, queryParams, pageableData);
            }
        }, userService.getAdminUser());
    }

    @Override
    public List<JnJInvoiceOrderModel> getInvoicesByOrders(List<OrderModel> orders) {
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_INVOICES_BY_ORDERS);
        final List<String> sapOrderNumbers = orders.stream().map(AbstractOrderModel::getSapOrderNumber).collect(Collectors.toList());
        final List<String> fixedSapOrderNumbers = removeBlanks(sapOrderNumbers);
        if (fixedSapOrderNumbers.size() == 0) {
            LOG.info("No sapOrderNumbers found to run query. Returning empty list.");
            return new ArrayList<>();
        }

        fQuery.addQueryParameter("sapOrderNumbers", fixedSapOrderNumbers);

        LOG.info("Querying invoices by orders using sapOrderNumbers: " + fixedSapOrderNumbers);

        return getFlexibleSearchService().<JnJInvoiceOrderModel>search(fQuery).getResult();
    }

    private List<String> removeBlanks(List<String> items) {
        return items.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    private String createSelectQuery(JnjPageableData pageableData, Map<String, Object> queryParams) {
        StringBuilder selectQueryBuilder = new StringBuilder();

        final String defaultB2BUnit = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit().getUid();
        queryParams.put(JnJB2BUnitModel.UID, defaultB2BUnit);
        switch (pageableData.getSearchBy()){
            case INVOICE_NUMBER:
                selectQueryBuilder.append(String.format(SELECT_INVOICE, SOLD_TO_JOIN, StringUtils.EMPTY, getWhereInvoiceNumber(), AND_SOLD_TO));
                queryParams.put(getInvoiceNumberField(), pageableData.getSearchText());
                break;
            case ORDER_NUMBER:
                selectQueryBuilder.append(String.format(SELECT_INVOICE, ORDER_JOIN, SOLD_TO_JOIN, WHERE_ORDER_NUMBER, AND_SOLD_TO));
                queryParams.put(OrderModel.SAPORDERNUMBER, pageableData.getSearchText());
                break;
            case PO_NUMBER:
                selectQueryBuilder.append(String.format(SELECT_INVOICE, SOLD_TO_JOIN, StringUtils.EMPTY, WHERE_PO_NUMBER, AND_SOLD_TO));
                queryParams.put(JnJInvoiceOrderModel.PONUMBER, pageableData.getSearchText());
                break;
            case NONE:
            default:
                selectQueryBuilder.append(String.format(SELECT_INVOICE, SOLD_TO_JOIN, StringUtils.EMPTY, WHERE_SOLD_TO, StringUtils.EMPTY));
        }

        selectQueryBuilder.append(createFromToDateClause(pageableData, queryParams));

        return selectQueryBuilder.toString();
    }

    private String createFromToDateClause(final JnjPageableData pageableData, final Map<String, Object> queryParams) {
        if(StringUtils.isEmpty(pageableData.getToDate()) && StringUtils.isEmpty(pageableData.getFromDate())) {
            return StringUtils.EMPTY;
        }

        String sessionLanguage = sessionService.getCurrentSession().getAttribute("sessionLanguage");
        String fromToDateClause = StringUtils.EMPTY;
        if(StringUtils.isNotEmpty(pageableData.getToDate()) && StringUtils.isNotEmpty(pageableData.getFromDate())) {
            fromToDateClause = AND_DATE_BETWEEN;
            queryParams.put(FROM_DATE, JnJLALanguageDateFormatUtil.convertDateToUSFormat(pageableData.getFromDate(), sessionLanguage) + HOUR_FROM);
            queryParams.put(TO_DATE, JnJLALanguageDateFormatUtil.convertDateToUSFormat(pageableData.getToDate(), sessionLanguage) + HOUR_TO);
        } else if (StringUtils.isNotEmpty(pageableData.getToDate()) && StringUtils.isEmpty(pageableData.getFromDate())) {
            fromToDateClause = AND_TO_DATE;
            queryParams.put(TO_DATE, JnJLALanguageDateFormatUtil.convertDateToUSFormat(pageableData.getToDate(), sessionLanguage) + HOUR_TO);
        } else if(StringUtils.isEmpty(pageableData.getToDate()) && StringUtils.isNotEmpty(pageableData.getFromDate())) {
            fromToDateClause = AND_FROM_DATE;
            queryParams.put(FROM_DATE, JnJLALanguageDateFormatUtil.convertDateToUSFormat(pageableData.getFromDate(), sessionLanguage) + HOUR_FROM);
        }

        return fromToDateClause;
    }

    private List<SortQueryData> createSortQueries(final String selectQuery) {
        return Arrays.asList(
            createSortQueryData(DEFAULT_SORT_CODE, selectQuery + ORDER_BY_DATE + SORT_DESCENDING),
            createSortQueryData(INVOICE_DATE_OLDEST_TO_NEWEST, selectQuery + ORDER_BY_DATE + SORT_ASCENDING),
            createSortQueryData(INVOICE_DATE_NEWEST_TO_OLDEST, selectQuery + ORDER_BY_DATE + SORT_DESCENDING),
            createSortQueryData(INVOICE_NUMBER_INCREASING, selectQuery + getOrderByInvoiceNumber() + SORT_ASCENDING),
            createSortQueryData(INVOICE_NUMBER_DECREASING, selectQuery + getOrderByInvoiceNumber() + SORT_DESCENDING),
            createSortQueryData(ORDER_NUMBER_INCREASING, selectQuery + ORDER_BY_ORDER_NUMBER + SORT_ASCENDING),
            createSortQueryData(ORDER_NUMBER_DECREASING, selectQuery + ORDER_BY_ORDER_NUMBER + SORT_DESCENDING),
            createSortQueryData(INVOICE_TOTAL_INCREASING, selectQuery + ORDER_BY_TOTAL + SORT_ASCENDING),
            createSortQueryData(INVOICE_TOTAL_DECREASING, selectQuery + ORDER_BY_TOTAL + SORT_DESCENDING)
        );
    }

    private SortQueryData createSortQueryData(final String sortCode, final String query) {
        final SortQueryData result = new SortQueryData();
        result.setSortCode(sortCode);
        result.setQuery(query);
        return result;
    }
}