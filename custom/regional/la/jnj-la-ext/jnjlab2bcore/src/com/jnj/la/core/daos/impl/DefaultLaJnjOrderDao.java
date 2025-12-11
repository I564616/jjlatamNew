/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.daos.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.model.JnjOrdEntStsMappingModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjLAFlexibleSearchBuilder;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Order;
import com.jnj.la.core.daos.JnjLaOrderDao;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import com.jnj.la.core.model.JnjLAConsolidatedEmailRecipientsModel;
import com.jnj.la.core.model.JnjLADailyEmailRecipientsModel;
import com.jnj.la.core.model.JnjLAImmediateEmailRecipientsModel;
import com.jnj.la.core.model.JnjLaConsolidatedEmailProcessModel;
import com.jnj.la.core.model.JnjUploadOrderModel;
import com.jnj.la.core.model.JnjUploadOrderSHAModel;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultCompanyB2BCommerceService;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.la.core.model.JnjOrderTypeModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The JnjOrderDaoImpl class contains getOrderModel method which hits the Hybris database to retrieve the Order Model.
 */
public class DefaultLaJnjOrderDao extends com.jnj.core.dao.impl.DefaultJnjOrderDao implements JnjLaOrderDao {

    private static final String NONE = "NONE";
	private static final Class<DefaultLaJnjOrderDao> CURRENT_CLASS = DefaultLaJnjOrderDao.class;
    private static final String FETCH_DROPSHIPMENT_DETAILS = "SELECT {pk} FROM {JnjDropShipmentDetails} WHERE {materialId} IN (?selectedMaterialIds)";
    private static final String JOIN = " JOIN ";
    private static final String ON_ORDER_HEADER = " ON {oHeader:";
    private static final String ON_USER_HEADER = " ON {user:";
    private static final String AND = " AND ";
    private static final String WHERE_AFTER_JOIN = "} WHERE ";
    private static final String SELECT_UPDATED_ORDERS = "SELECT {pk} FROM {Order} WHERE {modifiedtime} > ?modifiedtime";
    private static final String SELECT_UPDATED_INVOICE_ORDERS = "SELECT {ord.pk} FROM { Order as ord JOIN JnJInvoiceOrder as inv ON {ord:sapOrderNumber} = {inv:salesOrder} } WHERE {inv:modifiedtime} > ?modifiedtime";
    private static final String PERIODICITY = "periodicity";
    protected static final String DATE_FORMAT = "MM/dd/yyyy";
	protected static final String TEMPLATE = "SELECT {pref.%s} FROM {%s as pref JOIN %s as enum ON {pref.%s}={enum.pk}} WHERE {enum.code} = '%s'";
	private static final String SELECT_CONSOLIDATED_EMAIL_PROCESS = "Select {pk} from {JnjLaConsolidatedEmailProcess}";
	private static final String GET_FAILED_ERP_ORDERS = "SELECT {o.pk} FROM {Order as o JOIN OrderStatus as os ON {o.status} = {os.pk}} WHERE {os.code} in (?status) and ({o.sapOrderNumber} is null or {o.sapOrderNumber} = ' ') ";
	private static final String SELECT_SAP_FAILED_ORDERS = "Select {" + OrderModel.PK + "} FROM {"+OrderModel._TYPECODE+" as o  JOIN "+OrderStatus._TYPECODE+" as os ON {o."+OrderModel.STATUS+"}= {os."+EnumerationValueModel.PK+"} }";
	private static final String WHERE_SAP_FAILED_ORDERS = "where {os.code} IN (?orderStatus) AND {o.retryAttempts} = ?maxRetryAttempt AND {o.sapFailedOrderReportEmailSent} = 0 AND {o.sapOrderNumber} is null";

    @Autowired
    private DefaultCompanyB2BCommerceService companyB2BCommerceService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private JnjGTB2BUnitService jnjGTB2BUnitService;

    @Autowired
    private UserService userService;

    @Autowired
    private JnjLAOrderEntryPermutationMatrixCore orderEntryPermutationMatrixCore;

    @Autowired
    private JnjLAOrderHeaderPermutationMatrixCore orderHeaderPermutationMatrixCore;

    @Override
    public JnJB2BUnitModel fetchAllSoldToNumberForFile(final String validSoldToNumber)
    {
        final String methodName = "fetchAllSoldToNumberForFile ()";
        final String functionName = "Fetch Soldto For File";

        JnjGTCoreUtil.logDebugMessage(functionName, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
        try
        {
            final Map queryParams = new HashMap();
            final String query = Jnjlab2bcoreConstants.UploadOrder.FIND_B2BUNIT_FOR_TXT;
            queryParams.put("uid", validSoldToNumber);
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.addQueryParameters(queryParams);

            JnjGTCoreUtil.logDebugMessage(functionName, methodName,
                "final query to fetch sold to number for given uid : " + validSoldToNumber + ",is :" + fQuery, CURRENT_CLASS);

            final JnJB2BUnitModel result = getSessionService().executeInLocalView(new SessionExecutionBody()
            {
                @Override
                public JnJB2BUnitModel execute()
                {
                    final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
                    fQuery.addQueryParameters(queryParams);
                    return getFlexibleSearchService().<JnJB2BUnitModel> searchUnique(fQuery);
                }
            }, getUserService().getAdminUser());


            JnjGTCoreUtil.logDebugMessage(functionName, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
            return result;
        }
        catch (final ModelNotFoundException modelNotFoundException)
        {
            JnjGTCoreUtil.logErrorMessage("Fetch sold to number", methodName, "Sold to number not found for given  sold to number",
                modelNotFoundException, CURRENT_CLASS);
            return null;
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage("Fetch sold to number", methodName, "Sold to number not found for given  sold to number",
                exception, CURRENT_CLASS);
            return null;
        }
    }

    @Override
    public List<JnjOrdEntStsMappingModel> getOrderEntryStatus(final String overAllStatus, final String rejectionStatus,
                                                              final String deliveryStatus, final String invoiceStatus)
    {
        final String methodName = "getOrderEntryStatus";
        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

        final Map queryParams = new HashMap();
        String query = Order.ORDER_ENTRY_STATUS_QUERY;
        query = fetchStatus(query, overAllStatus, rejectionStatus, null, deliveryStatus, invoiceStatus, queryParams, false);

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameters(queryParams);

        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Order Entry Status Model Query: ", CURRENT_CLASS);

        final List<JnjOrdEntStsMappingModel> jnjOrderEntryStatusModelList = getFlexibleSearchService()
            .<JnjOrdEntStsMappingModel> search(fQuery).getResult();

        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);

        if (!jnjOrderEntryStatusModelList.isEmpty())
        {
            return jnjOrderEntryStatusModelList;
        }
        else
        {
            return Collections.emptyList();
        }
    }

    private String fetchStatus(final String query, final String overAllStatus, final String rejectionStatus,
                               final String creditStatus, final String deliveryStatus, final String invoiceStatus, final Map queryParams,
                               final boolean isHeaderStatus)
    {
        String finalQuery = query;
        if (JnjLaCommonUtil.isStatusValueProvided(overAllStatus))
        {
            finalQuery += Order.OVER_ALL_STATUS_VALUE;
            queryParams.put(Order.OVER_ALL_STATUS, overAllStatus);
        }
        else
        {
            finalQuery += Order.OVER_ALL_STATUS_IS_NULL;
        }
        if (JnjLaCommonUtil.isStatusValueProvided(rejectionStatus))
        {
            finalQuery += Order.REJECTION_STATUS_VALUE;
            queryParams.put(Order.REJECTION_STATUS, rejectionStatus);
        }
        else
        {
            finalQuery += Order.REJECTION_STATUS_IS_NULL;
        }
        if (isHeaderStatus)
        {
            if (JnjLaCommonUtil.isStatusValueProvided(creditStatus))
            {
                finalQuery += Order.CREDIT_STATUS_VALUE;
                queryParams.put(Order.CREDIT_STATUS, creditStatus);
            }
            else
            {
                finalQuery += Order.CREDIT_STATUS_IS_NULL;
            }
        }

        if (JnjLaCommonUtil.isStatusValueProvided(deliveryStatus))
        {
            finalQuery += Order.DELIVERY_STATUS_VALUE;
            queryParams.put(Order.DELIVERY_STATUS, deliveryStatus);
        }
        else
        {
            finalQuery += Order.DELIVERY_STATUS_IS_NULL;
        }
        if (JnjLaCommonUtil.isStatusValueProvided(invoiceStatus))
        {
            finalQuery += Order.INVOICE_STATUS_VALUE;
            queryParams.put(Order.INVOICE_STATUS, invoiceStatus);
        }
        else
        {
            finalQuery += Order.INVOICE_STATUS_IS_NULL;
        }
        return finalQuery;
    }

    /**
     * This method is used to fetch the JnjUploadOrderSHAModel(s) corresponding to a 512 bit Hash
     *
     * @param fileHash
     *           The 512 bit hash representation of the file
     * @return The model(s) corresponding to that hash, if any
     */
    @Override
    public List<JnjUploadOrderSHAModel> getUploadOrderSHADetails(final String fileHash)
    {
        final String methodName = "getUploadOrderSHADetails()";
        List<JnjUploadOrderSHAModel> result = null;
        try
        {
            final String query = "select {pk} from {JnjUploadOrderSHA} where {userFileHashId}=?fileHash";
            final Map<String, Object> params = new HashMap<>();
            params.put("fileHash", fileHash);
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.setNeedTotal(true);
            fQuery.addQueryParameters(params);
            JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
                "Executing query for obtaining JnjUploadOrderSHAModel records. Query fired is--> " + fQuery, CURRENT_CLASS);

            result = getFlexibleSearchService().<JnjUploadOrderSHAModel> search(fQuery).getResult();
        }
        catch (final Exception e)
        {
            JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
                "Error while executing query to get JnjUploadOrderSHAModel records." + e.getMessage(), e, CURRENT_CLASS);
        }
        return result;
    }

    /**
     * This method is used to fetch the conversion model on the basis of sold to number and given ship to number.
     *
     * @param shipToNumber
     *           the ship to number
     * @param jnJB2BUnitModel
     *           the jn j b2 b unit model
     * @return AddressModel addressModel
     */
    @Override
    public AddressModel foundConversion(final String shipToNumber, final JnJB2BUnitModel jnJB2BUnitModel)
    {
        final String methodName = "foundConversion ()";
        JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

        String customerId = null;
        try
        {
            final Map queryParams = new HashMap();
            customerId = jnJB2BUnitModel.getUid();
            final B2BUnitModel unit = companyB2BCommerceService.getUnitForUid(customerId);
            final String query = "select {jnJAddId} from {JnjShipToTranslation} where {b2bUnitId}=?unit and {customerId}=?shipToNumber ";
            queryParams.put(Jnjb2bCoreConstants.OrderHistory.B2BUNIT, unit);
            queryParams.put("shipToNumber", shipToNumber);
            final AddressModel result = sessionService.executeInLocalView(new SessionExecutionBody()
            {
                @Override
                public AddressModel execute()
                {
                    final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
                    fQuery.addQueryParameters(queryParams);
                    final AddressModel result = getFlexibleSearchService().<AddressModel> searchUnique(fQuery);
                    return result;
                }
            }, getUserService().getAdminUser());

            JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
            return result;
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "conversion not found for given combination",
                exception, CURRENT_CLASS);
            return null;
        }
    }

    /**
     * this method is used to fetch the ship to address for given b2b unit id.
     *
     * @param jnJB2BUnitModel
     *           the jn j b2 b unit model
     * @return the list
     */
    @Override
    public List<AddressModel> fetchShipToAddress(final JnJB2BUnitModel jnJB2BUnitModel)
    {
        final String methodName = "fetchShipToAddress ()";

        JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
        String customerId = null;
        try
        {
            final Map queryParams = new HashMap();
            customerId = jnJB2BUnitModel.getUid();

            final B2BUnitModel unit = companyB2BCommerceService.getUnitForUid(customerId);
            final String query = "select {pk} from {Address} where {owner} =?unit and {shippingAddress}='1' ";
            queryParams.put(Jnjb2bCoreConstants.OrderHistory.B2BUNIT, unit);
            final List<AddressModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
            {
                @Override
                public List<AddressModel> execute()
                {
                    final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
                    fQuery.addQueryParameters(queryParams);
                    final List<AddressModel> result = getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
                    return result;
                }
            }, getUserService().getAdminUser());

            JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
            return result;
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "An erro occurred while fetching Address table.",
                exception, CURRENT_CLASS);
            return Collections.emptyList();
        }
    }

    /**
     * this method is used to fetch the JnJ B2B Unit id for given CNPJ number.
     *
     * @param cnpjValue
     *           the cnpj value
     * @return List<JnJB2BUnitModel> jnJB2BUnitModelList
     */
    @Override
    public List<JnJLaB2BUnitModel> fetchB2BUnitForCnpj(final String cnpjValue)
    {
        final String methodName = "fetchB2BUnitForCnpj ()";
        JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

        try
        {
            final Map queryParams = new HashMap();
            final String query = Jnjlab2bcoreConstants.UploadOrder.FIND_B2BUNIT_CNPJ_QUERY;
            queryParams.put("cnpj", cnpjValue);
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.addQueryParameters(queryParams);

            JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, " Config Model Query : " + fQuery, CURRENT_CLASS);
            final List<JnJLaB2BUnitModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
            {
                @Override
                public List<JnJLaB2BUnitModel> execute()
                {
                    final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
                    fQuery.addQueryParameters(queryParams);
                    final List<JnJLaB2BUnitModel> result = getFlexibleSearchService().<JnJLaB2BUnitModel> search(fQuery).getResult();
                    return result;
                }
            }, getUserService().getAdminUser());

            JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
            return result;
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
                "An error occurred while fetching JnJLaB2BUnitModel table.", exception, CURRENT_CLASS);
            return Collections.emptyList();
        }
    }

    @Override
    public SearchPageData<JnjUploadOrderModel> getUploadOrderData(final String sortflag, final String filterFlag,
                                                                  final PageableData pageableData)
    {
        final String methodName = "getUploadOrderData()";
        SearchPageData<JnjUploadOrderModel> result = new SearchPageData<>();
        String customerId = StringUtils.EMPTY;
        String userName = StringUtils.EMPTY;
        try
        {
            final Map queryParams = new HashMap();
            final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
            customerId = jnjB2BUnitModel.getUid();
            final B2BUnitModel unit = jnjGTB2BUnitService.getUnitForUid(customerId);
            final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
            userName = currentCustomer.getName();

            JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.SUBMIT_ORDER_EDI, methodName, "value of unit model" + unit, CURRENT_CLASS);
            String query;
            List<SortQueryData> sortQueries = Collections.emptyList();

            if (sortflag.equalsIgnoreCase(Jnjlab2bcoreConstants.SellOutReports.SORT_ORDER_DESC)
                && filterFlag.equalsIgnoreCase(Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_BY_CUST))
            {
                query = "select {pk} from {JnjUploadOrder} where {b2bUnitId}=?unit ORDER BY {date} desc";
                sortQueries = Arrays.asList(createSortQueryData("byDate", query));
            }
            else if (sortflag.equalsIgnoreCase(Jnjlab2bcoreConstants.SellOutReports.SORT_ORDER_DESC)
                && filterFlag.equalsIgnoreCase(Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_BY_USER))
            {
                query = "select {pk} from {JnjUploadOrder} where {uploadByUser}=?userName ORDER BY {date} desc";
                sortQueries = Arrays.asList(createSortQueryData("byDate", query));
            }
            else if (sortflag.equalsIgnoreCase(Jnjlab2bcoreConstants.SellOutReports.SORT_ORDER_ASC)
                && filterFlag.equalsIgnoreCase(Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_BY_CUST))
            {
                query = "select {pk} from {JnjUploadOrder} where {b2bUnitId}=?unit ORDER BY {date}";
                sortQueries = Arrays.asList(createSortQueryData("byDate", query));
            }
            else if (sortflag.equalsIgnoreCase(Jnjlab2bcoreConstants.SellOutReports.SORT_ORDER_ASC)
                && filterFlag.equalsIgnoreCase(Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_BY_USER))
            {
                query = "select {pk} from {JnjUploadOrder} where {uploadByUser}=?userName ORDER BY {date}";
                sortQueries = Arrays.asList(createSortQueryData("byDate", query));
            }
            else
            {
                query = "select {pk} from {JnjUploadOrder} where {b2bUnitId}=?unit";
            }
            queryParams.put(Jnjlab2bcoreConstants.OrderHistory.B2BUNIT, unit);
            queryParams.put(Jnjlab2bcoreConstants.UploadEdiFiles.USERNAME, userName);
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.addQueryParameters(queryParams);

            JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName,
                "Executing query for obtaining uploded order records.", CURRENT_CLASS);

            result = pagedFlexibleSearchService.<JnjUploadOrderModel> search(sortQueries, "byDate", queryParams, pageableData);
        }
        catch (final Exception exp)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, methodName,
                "Error while executing query to get JnjUploadOrder table records in JnjOrderDataDao class - ", exp, CURRENT_CLASS);

        }
        return result;

    }

    @Override
    public JnjUploadOrderModel getUploadOrderDataDetails(final String fileNameId)
    {
        JnjUploadOrderModel result = null;
        final String methodName = "getUploadOrderDataDetails()";
        try
        {
            final Map queryParams = new HashMap();
            final String query = "select {pk} from {JnjUploadOrder} where {fileNameId}=?fileNameId";
            queryParams.put("fileNameId", fileNameId);
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.addQueryParameters(queryParams);

            JnjGTCoreUtil.logDebugMessage("Executing query for obtaining uploded order details records.", methodName, fileNameId, CURRENT_CLASS);

            JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, "getUploadOrderData()",
                "Executing query for obtaining uploded order details records. " + fileNameId, CURRENT_CLASS);


            result = sessionService.executeInLocalView(new SessionExecutionBody()
            {
                @Override
                public JnjUploadOrderModel execute()
                {
                    final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
                    fQuery.addQueryParameters(queryParams);
                    return getFlexibleSearchService().<JnjUploadOrderModel> searchUnique(fQuery);
                }
            }, userService.getAdminUser());
        }
        catch (final Exception exp)
        {

            JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, "getUploadOrderData()",
                "Error while executing query to get JnjUploadOrder table records in JnjOrderDataDao class - ", exp, CURRENT_CLASS);
        }
        return result;
    }

    /**
     * Method to get the Shipment Details
     *
     * @param materialIds
     * @return List<JnjDropShipmentDetailsModel>
     */
    public List<JnjDropShipmentDetailsModel> getShippingDetails(final List<String> materialIds)
    {
        final String methodName = "getShippingDetails()";

        JnjGTCoreUtil.logDebugMessage("Shipping details", methodName,
            Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
                + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), CURRENT_CLASS);

        List<JnjDropShipmentDetailsModel> results = null;
        try
        {
            final String query = FETCH_DROPSHIPMENT_DETAILS;
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.addQueryParameter("selectedMaterialIds", materialIds);
            results = getFlexibleSearchService().<JnjDropShipmentDetailsModel> search(fQuery).getResult();

        }
        catch (final ModelNotFoundException modelNotFound)
        {
            JnjGTCoreUtil.logErrorMessage("Shipping details", methodName, "Error fetching User", CURRENT_CLASS);
        }

        JnjGTCoreUtil.logDebugMessage("Shipping details", methodName,
            Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
                + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), CURRENT_CLASS);
        return results;
    }

    @Override
    public List<OrderEntryModel> getOrderEntryModelByModifiedTime(final Date lastUpdatedTimeParam) {
        Date lastUpdatedTime = prepareLastUpdatedTime(lastUpdatedTimeParam);

        String queryTemplate = "SELECT {%s} FROM {%s} WHERE {%s} > ?lastUpdatedTime AND {%s} IS NOT NULL ORDER BY {%s}";
        JnjLAFlexibleSearchBuilder builder = new JnjLAFlexibleSearchBuilder(queryTemplate)
            .addVar(OrderEntryModel.PK)
            .addVar(OrderEntryModel._TYPECODE)
            .addVar(OrderEntryModel.MODIFIEDTIME)
            .addVar(OrderEntryModel.PRODUCT)
            .addVar(OrderEntryModel.MODIFIEDTIME)
            .addQueryParam("lastUpdatedTime", lastUpdatedTime);

        return getFlexibleSearchService().<OrderEntryModel> search(builder.getQuery()).getResult();
    }

    @Override
    public List<OrderModel> getOrderModelByModifiedTimeInEntries(Date lastUpdatedTimeParam) {
        Date lastUpdatedTime = prepareLastUpdatedTime(lastUpdatedTimeParam);

        String queryTemplate = "SELECT DISTINCT {ord.%s} FROM {%s as ord JOIN %s as entry ON {entry.%s}={ord.%s}} WHERE {entry.%s} > ?lastUpdatedTime";
        JnjLAFlexibleSearchBuilder builder = new JnjLAFlexibleSearchBuilder(queryTemplate)
            .addVar(OrderModel.PK)
            .addVar(OrderModel._TYPECODE)
            .addVar(OrderEntryModel._TYPECODE)
            .addVar(OrderEntryModel.ORDER)
            .addVar(OrderModel.PK)
            .addVar(OrderEntryModel.MODIFIEDTIME)
            .addVar(OrderEntryModel.MODIFIEDTIME)
            .addQueryParam("lastUpdatedTime", lastUpdatedTime);

        return getFlexibleSearchService().<OrderModel> search(builder.getQuery()).getResult();
    }

    private static Date prepareLastUpdatedTime(Date lastUpdatedTime) {
        if (lastUpdatedTime != null) {
            return lastUpdatedTime;
        }
        return new Date(1); // oldest possible date: 01/01/1970
    }

    @Override
    public OrderEntryStatus getOrderEntryStatusUsingPermutationMatrix(final AbstractOrderEntryModel orderLine) {
        final List<JnjOrdEntStsMappingModel> jnjOrderEntryStatusModelList = orderEntryPermutationMatrixCore.getOrderEntryStatuses(getFlexibleSearchService(), orderLine);
        if (CollectionUtils.isNotEmpty(jnjOrderEntryStatusModelList)) {
            return jnjOrderEntryStatusModelList.get(0).getOrderEntryStatus();
        }
        return null;
    }

    @Override
    public OrderStatus calculateOrderHeaderStatus(OrderModel order) {
        return orderHeaderPermutationMatrixCore.calculateOrderHeaderStatus(getFlexibleSearchService(), order);
    }

    @Override
    public List<OrderModel> getOrdersByPeriodicity(final JnJEmailPeriodicity periodicity) {
        return getOrdersByPeriodicityAndStore(periodicity, null);
    }

    @Override
    public List<OrderEntryModel> getPendingFromOrderEntries(final JnJEmailPeriodicity periodicity) {
        return getPendingFromOrderEntriesByStore(periodicity, null,null);
    }

    @Override
    public List<OrderModel> getOrdersByPeriodicityAndStore(final JnJEmailPeriodicity periodicity, final String site) {
        final StringBuilder query = selectFromOrderEntry();

        joinOnOrderHeader(query);
        joinOnUser(query);
        if (StringUtils.isNotBlank(site)) {
            joinOnSite(query);
        }
        joinOnPreferences(query);

        query.append(WHERE_AFTER_JOIN);

        filterByPendingFlag(periodicity, query);
        filterExcludingUser(query);
        filterBySite(site, query);
        filterByPeriodicity(query);

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        if (StringUtils.isNotBlank(site)) {
            fQuery.addQueryParameter("site", site);
        }
        fQuery.addQueryParameter(PERIODICITY, periodicity);
        fQuery.addQueryParameter("excludedUser", Jnjlab2bcoreConstants.UpsertCustomer.DUMMY_USER);

        LOGGER.info(fQuery);
        List<OrderModel> orders = getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
        addRequiredMissingFieldsInOrders(orders);

        LOGGER.info("Found " + orders.size() + " pending orders");
        return orders;
    }

    @Override
    public List<OrderModel> getOrdersForConsolidatedEmails(final List<String> countries, final List<String> excludeStatuses, final List<String> orderTypes) {
        final StringBuilder query = selectFromOrderEntry();

        joinOnOrderHeader(query);
        joinOnUnit(query);
        joinOnCountry(query);
        joinOnOrderTypes(query);
        
        query.append(WHERE_AFTER_JOIN);

        filterByOrderEntryStatuses(query);
        filterByCountry(query);
        filterByOrderType(query);
        filterByDate(query);


        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("excludeStatuses", excludeStatuses);
        fQuery.addQueryParameter("countries", countries);
        fQuery.addQueryParameter("jnjOrdTypes", orderTypes);

        LOGGER.info("Query for consolidated emails: " + fQuery.getQuery());
        LOGGER.info("Query parameters for consolidated emails: " + fQuery.getQueryParameters());

        return getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
    }

    @Override
    public List<OrderModel> getOrdersForDailyEmails(final JnJEmailPeriodicity periodicity,final List<String> countries, final List<String> orderTypeStatus) {
        final StringBuilder query = selectFromOrderEntry();

        joinOnOrderHeader(query);
        joinOnUserForDailyOrImmediateEmail(query);
        joinOnPreferences(query);
        joinOnUnit(query);
        joinOnCountry(query);

        query.append(WHERE_AFTER_JOIN);

        filterQueryBuilder(periodicity, query);

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("orderTypeStatus", orderTypeStatus);
        fQuery.addQueryParameter(PERIODICITY, periodicity);

        LOGGER.info("Query for Daily emails: " + fQuery.getQuery());
        LOGGER.info("Query parameters: periodicity: " + periodicity+" orderTypeStatus: "+orderTypeStatus);

        return getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
    }

    @Override
    public List<OrderModel> getOrdersForImmediateEmails(final JnJEmailPeriodicity periodicity, final List<String> orderTypeStatus) {
        final StringBuilder query = selectFromOrderEntry();

        joinOnOrderHeader(query);
        joinOnUserForDailyOrImmediateEmail(query);
        joinOnPreferences(query);
        joinOnUnit(query);

        query.append(WHERE_AFTER_JOIN);

        filterQueryBuilder(periodicity, query);

        final FlexibleSearchQuery fQuery = addFqueryParameters(periodicity, orderTypeStatus, query);

        LOGGER.info("Query for Immediate emails: " + fQuery.getQuery());
		
        

        return getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
    }

	/**
	 * adds query param
	 * @param periodicity
	 * @param orderTypeStatus
	 * @param query
	 * @return
	 */
	private static FlexibleSearchQuery addFqueryParameters(final JnJEmailPeriodicity periodicity,
			final List<String> orderTypeStatus, final StringBuilder query) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("orderTypeStatus", orderTypeStatus);
        fQuery.addQueryParameter(PERIODICITY, periodicity);
		return fQuery;
	}

	/**
	 * builds query to filter it by statuses,
	 * periodicity,by date
	 * @param periodicity
	 * @param query
	 */
	private static void filterQueryBuilder(final JnJEmailPeriodicity periodicity, final StringBuilder query) {
		filterByOrderTypeStatuses(query);
        filterByPeriodicity(query);
        filterByPendingFlagForDailyOrImmediate(periodicity, query);
        filterByDate(query);
	}
 
    @Override
    public OrderModel getOrderBySapOrderNumber(String sapOrderNumber) {
        String template = "SELECT {%s} FROM {%s} WHERE {%s} = ?sapOrderNumber";
        final String query = String.format(template, OrderModel.PK, OrderModel._TYPECODE, OrderModel.SAPORDERNUMBER);
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("sapOrderNumber", sapOrderNumber);

        List<OrderModel> result = getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
        if (result.size() == 1) {
            return result.get(0);
        }
        LOGGER.warn("Unexpected order result size found for sapOrderNumber=" + sapOrderNumber + ". Size: " + result.size());
        return null;
    }

    private static StringBuilder selectFromOrderEntry() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT {oHeader:").append(OrderModel.PK).append("} FROM {").append(OrderEntryModel._TYPECODE).append(" AS oEntry");
        return query;
    }

    private static void joinOnPreferences(StringBuilder query) {
        query.append(JOIN).append(JnJLaUserAccountPreferenceModel._TYPECODE).append(" AS prefs");
        query.append(ON_USER_HEADER).append(JnJB2bCustomerModel.PK).append("} = {prefs:").append(JnJLaUserAccountPreferenceModel.USER).append("}");
        query.append(" AND {prefs:").append(JnJLaUserAccountPreferenceModel.ACCOUNT).append("} = {oHeader:").append(OrderModel.UNIT).append("}");
    }

    private static void joinOnOrderHeader(StringBuilder query) {
        query.append(JOIN).append(OrderModel._TYPECODE).append(" AS oHeader");
        query.append(ON_ORDER_HEADER).append(OrderModel.PK).append("} = {oEntry:").append(OrderEntryModel.ORDER).append("}");
    }

    private static void joinOnUser(StringBuilder query) {
        query.append(JOIN).append(JnJB2bCustomerModel._TYPECODE).append(" AS user");
        query.append(ON_ORDER_HEADER).append(OrderModel.USER).append("} = {user:").append(JnJB2bCustomerModel.PK).append("}");
    }

    private static void joinOnUserForDailyOrImmediateEmail(final StringBuilder query) {
        query.append(JOIN).append(JnJB2bCustomerModel._TYPECODE).append(" AS user");
        query.append(ON_USER_HEADER).append(JnJB2bCustomerModel.PK).append("} = {user:").append(JnJB2bCustomerModel.PK).append("}");
    }

    private static void joinOnSite(StringBuilder query) {
        query.append(JOIN).append(BaseSiteModel._TYPECODE).append(" AS site");
        query.append(ON_ORDER_HEADER).append(OrderModel.SITE).append("} = {site:").append(BaseSiteModel.PK).append("}");
    }

    private static void joinOnUnit(StringBuilder query) {
        query.append(JOIN).append(JnJB2BUnitModel._TYPECODE).append(" AS unit");
        query.append(ON_ORDER_HEADER).append(OrderModel.UNIT).append("} = {unit:").append(JnJB2BUnitModel.PK).append("}");
    }

    private static void joinOnCountry(StringBuilder query) {
        query.append(JOIN).append(CountryModel._TYPECODE).append(" AS country");
        query.append(" ON {unit:").append(JnJB2BUnitModel.COUNTRY).append("} = {country:").append(CountryModel.PK).append("}");
    }
    
    private static void joinOnOrderTypes(StringBuilder query) {
        query.append(JOIN).append(JnjOrderTypeModel._TYPECODE).append(" AS ordType");
        query.append(" ON {oHeader:").append("jnjOrderType").append("} = {ordType:").append(JnjOrderTypeModel.PK).append("}");
    }

    private static void filterByPendingFlag(final JnJEmailPeriodicity periodicity, final StringBuilder query) {
        if (periodicity != null) {
            query.append("{oEntry:");

            if (JnJEmailPeriodicity.IMMEDIATE.equals(periodicity)) {
                query.append(OrderEntryModel.PENDINGIMMEDIATEEMAIL);
            } else if (JnJEmailPeriodicity.DAILY.equals(periodicity)) {
                query.append(OrderEntryModel.PENDINGDAILYEMAIL);
            } else if (JnJEmailPeriodicity.CONSOLIDATED.equals(periodicity)) {
                query.append(OrderEntryModel.PENDINGCONSOLIDATEDEMAIL);
            }

            query.append("} = 1");
        }
    }

    private static void filterByPendingFlagForDailyOrImmediate(final JnJEmailPeriodicity periodicity, final StringBuilder query) {
        if (periodicity != null) {
            query.append(AND).append("{oEntry:");

            if (JnJEmailPeriodicity.IMMEDIATE.equals(periodicity)) {
                query.append(OrderEntryModel.PENDINGIMMEDIATEEMAIL);
            } else if (JnJEmailPeriodicity.DAILY.equals(periodicity)) {
                query.append(OrderEntryModel.PENDINGDAILYEMAIL);
            } else if (JnJEmailPeriodicity.CONSOLIDATED.equals(periodicity)) {
                query.append(OrderEntryModel.PENDINGCONSOLIDATEDEMAIL);
            }

            query.append("} = 1");
        }
    }

    private static void filterExcludingUser(final StringBuilder query) {
        query.append(AND).append("{user:").append(JnJB2bCustomerModel.UID).append("} <> ?excludedUser");
    }

    private static void filterBySite(final String site, StringBuilder query) {
        if (StringUtils.isNotBlank(site)) {
            query.append(AND).append("{site:").append(BaseSiteModel.UID).append("} = ?site");
        }
    }

    private static void filterByPeriodicity(StringBuilder query) {
        query.append(AND).append("{prefs:").append(JnJLaUserAccountPreferenceModel.PERIODICITY).append("} = ?periodicity");
    }

    private static void filterByOrderEntryStatuses(StringBuilder query) {
        query.append("({oEntry:").append(OrderEntryModel.STATUS).append("} NOT IN ( ?excludeStatuses )").append(AND);
        query.append("{oEntry:").append(OrderEntryModel.STATUS).append("} IS NOT NULL )");
    }
    
    private static void filterByOrderTypeStatuses(StringBuilder query) {
        query.append("({oHeader:").append(OrderModel.ORDERTYPE).append("} IN ({{SELECT {PK} FROM {JnjOrderTypesEnum} WHERE {CODE} IN (?orderTypeStatus)}})").append(AND);
        query.append("{oHeader:").append(OrderModel.ORDERTYPE).append("} IS NOT NULL )");
    }

    private static void filterByCountry(StringBuilder query) {
        query.append(AND).append("{country:").append(CountryModel.ISOCODE).append("} in ( ?countries )");
    }
    
    private static void filterByOrderType(StringBuilder query) {
        query.append(AND).append("{ordType:").append(JnjOrderTypeModel.CODE).append("} in ( ?jnjOrdTypes )");
    }

    private static void filterByDate(StringBuilder query) {
        query.append(AND).append("{oEntry:").append(OrderEntryModel.MODIFIEDTIME).append("} > CONVERT(DATETIME,'" + filterQueryByModifiedTime() + "')").append(AND).append("{oHeader:").append(OrderModel.CREATIONTIME).append("} > CONVERT(DATETIME,'"+filterQueryByCreationTime()+"')");
    }

    private static String filterQueryByModifiedTime() {
        final Calendar date = Calendar.getInstance();
        final int days = Config.getInt(Jnjlab2bcoreConstants.ORDERS_FROM_DAYS_TO_SEND_CONSOLIDATED_EMAIL, 30);
        date.add(Calendar.DAY_OF_MONTH, -days);

        try {
            return new SimpleDateFormat(DATE_FORMAT).format(date.getTime());
        }
        catch (final Exception exception) {
            final String methodName = "filterQueryByModifiedTime()";
            JnjGTCoreUtil.logErrorMessage("Consolidated email query", methodName, "Error while formatting the order update date format: " + DATE_FORMAT, exception, DefaultLaJnjOrderDao.class);
        }
        return null;
    }
    
    private static String filterQueryByCreationTime() {
        final Calendar date = Calendar.getInstance();
        final int days = Config.getInt(Jnjlab2bcoreConstants.ORDERS_CREATION_DATE_TO_SEND_CONSOLIDATED_EMAIL, 30);
        date.add(Calendar.DAY_OF_MONTH, -days);

        try {
            return new SimpleDateFormat(DATE_FORMAT).format(date.getTime());
        }
        catch (final IllegalArgumentException exception) {
            final String methodName = "filterQueryByModifiedTime()";
            JnjGTCoreUtil.logErrorMessage("Consolidated email query", methodName, "Error while formatting the order creation date format: " + DATE_FORMAT, exception, DefaultLaJnjOrderDao.class);
        }
        return null;
    }
    
    public List<OrderEntryModel> getPendingFromOrderEntriesByStore(final JnJEmailPeriodicity periodicity, final String site, final List<OrderModel> orders ) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT {").append(OrderEntryModel.PK).append("} FROM {").append(OrderEntryModel._TYPECODE).append(" AS oEntry");

        if (StringUtils.isNotBlank(site)) {
            query.append(JOIN).append(OrderModel._TYPECODE).append(" AS oHeader");
            query.append(ON_ORDER_HEADER).append(OrderModel.PK).append("} = {oEntry:").append(OrderEntryModel.ORDER).append("}");
        }

        query.append(WHERE_AFTER_JOIN);

        filterByPendingFlag(periodicity, query);
        
		List<String> sapOrderNos = new ArrayList<>();
        List<String> ordersWithoutSapOrderNumber = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(orders)) {
			for (OrderModel orderModel : orders) {
                if(StringUtils.isNotBlank(orderModel.getSapOrderNumber())){
                    sapOrderNos.add(orderModel.getSapOrderNumber());
                }
				else{
                    ordersWithoutSapOrderNumber.add(orderModel.getOrderNumber());
                }
			}
			if(CollectionUtils.isNotEmpty(sapOrderNos)){
                query.append(AND).append("{oHeader:sapOrderNumber").append("} IN(?sapOrderNumber)");
            }
		}


        LOGGER.info("Order Numbers " + ordersWithoutSapOrderNumber + " Do not have SAP Order number");
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

        if (CollectionUtils.isNotEmpty(orders) && CollectionUtils.isNotEmpty(sapOrderNos)) {
            fQuery.addQueryParameter("sapOrderNumber", sapOrderNos);
        }
        LOGGER.info("Final query to get order entries:  " + fQuery);
        return getFlexibleSearchService().<OrderEntryModel> search(fQuery).getResult();
    }

    @Override
    public Map<B2BUnitModel, List<JnJB2bCustomerModel>> getUsersForConsolidatedEmail() {
        String query = String.format(TEMPLATE,
            JnJLaUserAccountPreferenceModel.PK,
            JnJLaUserAccountPreferenceModel._TYPECODE,
            JnJEmailPeriodicity._TYPECODE,
            JnJLaUserAccountPreferenceModel.PERIODICITY,
            JnJEmailPeriodicity.CONSOLIDATED
        );

        return getAccountPreferenceAndUserMap(query);
    }

	/**
	 * This method is returning the map for account and b2buser
	 * 
	 * @param query the query is the query to run
	 * @return resultMap the resultMap is the map to be returned
	 */
	private Map<B2BUnitModel, List<JnJB2bCustomerModel>> getAccountPreferenceAndUserMap(String query) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        List<JnJLaUserAccountPreferenceModel> items = getFlexibleSearchService().<JnJLaUserAccountPreferenceModel>search(fQuery).getResult();

        Map<B2BUnitModel, List<JnJB2bCustomerModel>> resultMap = new HashMap<>();
        for (JnJLaUserAccountPreferenceModel item : items) {
            JnJB2BUnitModel account = item.getAccount();
            resultMap.computeIfAbsent(account, k -> new ArrayList<>());
            if (item.getUser() != null) {
                resultMap.get(account).add(item.getUser());
            }
        }

        return resultMap;
	}

    @Override
    public Map<B2BUnitModel, List<JnJB2bCustomerModel>> getUsersForDailyEmail() {
        String query = String.format(TEMPLATE,
            JnJLaUserAccountPreferenceModel.PK,
            JnJLaUserAccountPreferenceModel._TYPECODE,
            JnJEmailPeriodicity._TYPECODE,
            JnJLaUserAccountPreferenceModel.PERIODICITY,
            JnJEmailPeriodicity.DAILY
        );

        return getAccountPreferenceAndUserMap(query);
    }

    @Override
    public Map<B2BUnitModel, List<JnJB2bCustomerModel>> getUsersForImmediateEmail() {
        String query = String.format(TEMPLATE,
            JnJLaUserAccountPreferenceModel.PK,
            JnJLaUserAccountPreferenceModel._TYPECODE,
            JnJEmailPeriodicity._TYPECODE,
            JnJLaUserAccountPreferenceModel.PERIODICITY,
            JnJEmailPeriodicity.IMMEDIATE
        );

        return getAccountPreferenceAndUserMap(query);
    }

    @Override
    public Map<B2BUnitModel, List<String>> getDefaultRecipientsForConsolidatedEmail() {
        String template = "SELECT {%s} FROM {%s} WHERE {%s} = 1";
        String query = String.format(template, JnjLAConsolidatedEmailRecipientsModel.PK, JnjLAConsolidatedEmailRecipientsModel._TYPECODE, JnjLAConsolidatedEmailRecipientsModel.ENABLED);

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        List<JnjLAConsolidatedEmailRecipientsModel> items = getFlexibleSearchService().<JnjLAConsolidatedEmailRecipientsModel>search(fQuery).getResult();

        Map<B2BUnitModel, List<String>> resultMap = new HashMap<>();
        for (JnjLAConsolidatedEmailRecipientsModel item : items) {
            resultMap.put(item.getSoldTo(), createRecipientsList(item.getRecipients()));
        }

        return resultMap;
    }

    @Override
    public Map<B2BUnitModel, List<String>> getDefaultRecipientsForImmediateEmail(final B2BUnitModel b2bUnit) {
        final String template = "SELECT {%s} FROM {%s} WHERE {%s} = 1 AND {%s} = '%s'";
        final String query = String.format(template, JnjLAImmediateEmailRecipientsModel.PK, JnjLAImmediateEmailRecipientsModel._TYPECODE, JnjLAImmediateEmailRecipientsModel.ENABLED,JnjLAImmediateEmailRecipientsModel.SOLDTO,b2bUnit.getPk());

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        final List<JnjLAImmediateEmailRecipientsModel> items = getFlexibleSearchService().<JnjLAImmediateEmailRecipientsModel>search(fQuery).getResult();

        final Map<B2BUnitModel, List<String>> resultMap = new HashMap<>();
        for (JnjLAImmediateEmailRecipientsModel item : items) {
                resultMap.put(item.getSoldTo(), createRecipientsList(item.getRecipients()));
        }
        return resultMap;
    }
       

    /**
     * fetch all the user which is having account preferences 
     *  of b2b unit of order and particular periodicity
     *@param periodicity
     *@param B2BUnitModel
     *           
     * @return the list
     */
    public List<JnJB2bCustomerModel> getRecipientsForDailyAndImmediateEmail(final B2BUnitModel b2bUnit,final String periodicity) {
    	final String template="SELECT {user:pk} FROM {JnJLaUserAccountPreference AS prefs JOIN JnJB2bCustomer AS user ON {user:pk} = {prefs:user} join B2BUnit as unit on {prefs:account} = {unit:pk} join JnJEmailPeriodicity as periodicity on {prefs:periodicity}={periodicity:pk}} where {periodicity:code} = '%s'  AND\n" + 
    			"{unit:pk}='%s'";
        
        final String query = String.format(template, periodicity,b2bUnit.getPk());

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		
        return  getFlexibleSearchService().<JnJB2bCustomerModel>search(fQuery).getResult();
    }

    @Override
    public Map<B2BUnitModel, List<String>> getDefaultRecipientsForDailyEmail(final B2BUnitModel b2bUnit) {
        final String template = "SELECT {%s} FROM {%s} WHERE {%s} = 1 AND {%s} = '%s'";
        final String query = String.format(template, JnjLADailyEmailRecipientsModel.PK, JnjLADailyEmailRecipientsModel._TYPECODE, JnjLADailyEmailRecipientsModel.ENABLED,JnjLADailyEmailRecipientsModel.SOLDTO,b2bUnit.getPk());

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        final List<JnjLADailyEmailRecipientsModel> items = getFlexibleSearchService().<JnjLADailyEmailRecipientsModel>search(fQuery).getResult();

        final Map<B2BUnitModel, List<String>> resultMap = new HashMap<>();
        for (JnjLADailyEmailRecipientsModel item : items) {
                resultMap.put(item.getSoldTo(), createRecipientsList(item.getRecipients()));
        }
        return resultMap;
    }

    @Override
    public List<OrderModel> getUpdatedOrders(Date modifiedtime) {
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_UPDATED_ORDERS);
        fQuery.addQueryParameter("modifiedtime", modifiedtime);
        LOGGER.info(fQuery);

        return getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
    }

    @Override
    public List<OrderModel> getUpdatedInvoiceOrders(Date modifiedtime) {
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_UPDATED_INVOICE_ORDERS);
        fQuery.addQueryParameter("modifiedtime", modifiedtime);
        LOGGER.info(fQuery);

        return getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
    }

    private List<String> createRecipientsList(String rawRecipients) {
        if (rawRecipients == null) {
            return new ArrayList<>();
        }
        String trimmedRecipients = rawRecipients.replaceAll("\\s", "");
        List<String> result = Arrays.asList(trimmedRecipients.split(";"));

        result.removeIf(Objects::isNull);
        return result;
    }

    private void addRequiredMissingFieldsInOrders(List<OrderModel> orders) {
        Map<BaseStoreModel, List<String>> storeToCountriesMap = createStoreToCountriesMap();
        orders.forEach(order -> addRequiredMissingFieldInOrder(storeToCountriesMap, order));
    }

    private void addRequiredMissingFieldInOrder(Map<BaseStoreModel, List<String>> storeToCountriesMap, OrderModel order) {
        if (order.getStore() == null || order.getSite() == null || order.getLanguage() == null) {
            BaseStoreModel store = findStoreByOrder(order, storeToCountriesMap);

            if (order.getStore() == null) {
                order.setStore(store);
            }

            if (order.getSite() == null && store.getCmsSites() != null && !store.getCmsSites().isEmpty()) {
                order.setSite(store.getCmsSites().iterator().next());
            }

            if (order.getLanguage() == null) {
                order.setLanguage(store.getDefaultLanguage());
            }
        }
    }

    private static BaseStoreModel findStoreByOrder(OrderModel order, Map<BaseStoreModel, List<String>> storeToCountriesMap) {
        if (order != null && order.getUnit() != null && order.getUnit().getCountry() != null && order.getUnit().getCountry().getIsocode() != null) {
            String isoCode = order.getUnit().getCountry().getIsocode();
            for (BaseStoreModel store : storeToCountriesMap.keySet()) {
                List<String> countries = storeToCountriesMap.get(store);
                if (countries.contains(isoCode)) {
                    return store;
                }
            }
        }
        return null;
    }

    private Map<BaseStoreModel, List<String>> createStoreToCountriesMap() {
        Map<BaseStoreModel, List<String>> result = new HashMap<>();
        List<BaseStoreModel> stores = getAllStores();
        for (BaseStoreModel store : stores) {
            result.put(store, JnjLaCommonUtil.getCountriesBySite(store.getUid()));
        }
        return result;
    }

    private List<BaseStoreModel> getAllStores() {
        String query = String.format("SELECT {%s} FROM { %s }", BaseStoreModel.PK, BaseStoreModel._TYPECODE);
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        return getFlexibleSearchService().<BaseStoreModel>search(fQuery).getResult();
    }
    
    public boolean getUserPreference(final String uid,final B2BUnitModel b2bUnit)
    {
        String query = String.format("SELECT {%s} FROM {%s} where {%s} IN ({{SELECT {%s} FROM {%s}"
                                        +"WHERE {%s} ='%s'}}) AND {%s} IN ({{SELECT {%s} FROM {%s} WHERE {%s} ='%s'}})",
                                        JnJLaUserAccountPreferenceModel.PK,JnJLaUserAccountPreferenceModel._TYPECODE,JnJLaUserAccountPreferenceModel.USER,B2BCustomerModel.PK,
                                        B2BCustomerModel._TYPECODE,B2BCustomerModel.UID,uid,JnJLaUserAccountPreferenceModel.ACCOUNT,B2BUnitModel.PK,
                                        B2BUnitModel._TYPECODE,B2BUnitModel.UID,b2bUnit.getUid());
        
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        final List<JnJLaUserAccountPreferenceModel> jnjAccountPreference = getFlexibleSearchService().<JnJLaUserAccountPreferenceModel>search(fQuery).getResult();
        if(jnjAccountPreference.isEmpty())
        {
            return false;
        }
        else
        {
            final String preference = jnjAccountPreference.get(0).getPeriodicity().getCode();
            if(NONE.equals(preference))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieving all laconsolidated email process models.
     */
    @Override
	public List<JnjLaConsolidatedEmailProcessModel> getJnjLaConsolidatedEmailProcessModels()
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_CONSOLIDATED_EMAIL_PROCESS);
		final List<JnjLaConsolidatedEmailProcessModel> jnjLaConsolidatedEmailProcessModels = getFlexibleSearchService().<JnjLaConsolidatedEmailProcessModel>search(fQuery).getResult();
		return jnjLaConsolidatedEmailProcessModels;
	}
    
    /**
     * Retrieving all orders which failed in ERP
     */
    @Override
    public List<OrderModel> getFailedErpOrdersList(final List<String> orderCodes, final List<JnjOrderTypesEnum> orderTypes, final Date startDate, final Date endDate) {
        final String methodName = "getFailedErpOrdersList()";
    	StringBuilder query = new StringBuilder(GET_FAILED_ERP_ORDERS);
        String orderStatus = Config.getParameter(Jnjlab2bcoreConstants.Order.RETRY_ERP_STATUS) == null ?
                Jnjlab2bcoreConstants.Order.DEFAULT_STATUS_FOR_FAILED_ORDERS
                : Config.getParameter(Jnjlab2bcoreConstants.Order.RETRY_ERP_STATUS);
        List<String> statusList = Stream.of(orderStatus.split(",", -1))
                .collect(Collectors.toList());

        final Map<String, Object> params = new HashMap<>();

        if (CollectionUtils.isNotEmpty(orderCodes)) {
            query = query.append(" and {o.code} in (?orderCodes) ");
            params.put("orderCodes", orderCodes);
        }
       else{
            if (CollectionUtils.isNotEmpty(orderTypes)) {
                query = query.append("and {o.ordertype} in (?orderTypes) ");
                params.put("orderTypes", orderTypes);
            }
            if (Objects.nonNull(startDate)){
                query = query.append("and {o.creationtime} >= ?startDate ");
                params.put("startDate", startDate);
            }
            if (Objects.nonNull(endDate)){
                query = query.append("and {o.creationtime} <= ?endDate ");
                params.put("endDate", endDate);
            }
            
            final Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.HOUR, -Config.getInt(Jnjlab2bcoreConstants.Order.BATCH_ORDER_GRACE_TIME, 6));
            final Date orderCreationMinimumDate = currentDate.getTime();
    		final SimpleDateFormat graceTimeFomrat = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.BATCH_ORDER_DATE_FORMAT);
    		String graceTime = null;
    		try
    		{
    			graceTime = graceTimeFomrat.format(orderCreationMinimumDate);
    			query = query.append("and {o.creationtime} <= ?graceTime ");
                params.put("graceTime", graceTime);
    		}
    		catch (final Exception exception)
    		{
    			LOGGER.error(methodName+"Error while formating the gracetime in to string format:: " + exception +DefaultLaJnjOrderDao.class);
    		}
    		
        }

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString(), params);
        fQuery.addQueryParameter("status", statusList);
        LOGGER.info("getFailedErpOrdersList Query: " + fQuery);

        return getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
    }
    
    /**
	 * To fetch list of sap failed orders after maximum retry attempt.
	 */
	@Override
	public List<OrderModel> findSAPFailedOrders(final Integer maxRetryAttempt, final String[] sapFailedOrderStatus) {
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(SELECT_SAP_FAILED_ORDERS + WHERE_SAP_FAILED_ORDERS);
		
		
		flexibleSearchQuery.addQueryParameter("orderStatus", Arrays.asList(sapFailedOrderStatus));
		flexibleSearchQuery.addQueryParameter("maxRetryAttempt", maxRetryAttempt);
		
		return getFlexibleSearchService().<OrderModel>search(flexibleSearchQuery).getResult();
		
	}
}
