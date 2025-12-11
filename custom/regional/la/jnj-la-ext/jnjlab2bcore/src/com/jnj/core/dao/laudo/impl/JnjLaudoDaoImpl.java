/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.core.dao.laudo.impl;
import com.jnj.core.services.JnjConfigService;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.laudo.JnjLaudoDao;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.mapper.laudo.JnjLaudoMapper;
import com.jnj.core.model.JnjLaudoModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Laudo;
import com.jnj.la.core.services.customer.impl.JnjLatamCustomerServiceImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
* This class is responsible for fetching the data for JNJLaudo
*/
public class JnjLaudoDaoImpl implements JnjLaudoDao
{
	/** The Constant DEFAULT_LAUDO_QUERY. */
	private static final String DEFAULT_LAUDO_QUERY = "select {jd:pk} from {JnjLaudo as jd} WHERE ({jd:batchRequired}='0'  OR ({jd:batchRequired}='1' AND {jd:expirationDate} > ?today )) AND {jd:country} IN ({{ select {cty:pk} from {Country as cty} WHERE {cty:isocode} =?countryIso}}) ";
    private static final String DEFAULT_LAUDO_QUERY_EXPIRE="select {jd:pk} from {JnjLaudo as jd} WHERE ({jd:batchRequired}='1' AND {jd:expirationDate} < ?documentDelete ) AND {jd:country} IN ({{ select {cty:pk} from {Country as cty} WHERE {cty:isocode} =?countryIso}}) ";
	/** The Constant FIND_LAUDO_ENTRIES_BY_USER_QUERY. */
	private static final String FIND_LAUDO_ENTRIES_BY_USER_QUERY = "AND {jd:productCode} = ?productCode AND {jd:laudoNumber} =?laudoNumber ";
	private static final String FIND_LAUDO_WITH_PRODUCTID_QUERY = "AND {jd:productCode} = ?productCode " ;
	/** The Constant FIND_LAUDO_ENTRIES_BY_USER_QUERY_WITH_FROM_DATE. */
	private static final String FIND_LAUDO_ENTRIES_BY_USER_QUERY_WITH_FROM_DATE = "AND {jd:expirationDate} >= CONVERT(DATETIME,?fromDate) ";

	/** The Constant FIND_LAUDO_ENTRIES_BY_USER_QUERY_WITH_TO_DATE. */
	private static final String FIND_LAUDO_ENTRIES_BY_USER_QUERY_WITH_TO_DATE = "AND {jd:expirationDate} < CONVERT(DATETIME,?toDate) ";

	/** The Constant FIND_LAUDO_ENTRIES_WITH_MEDIA_ONLY. */
	private static final String FIND_LAUDO_ENTRIES_WITH_MEDIA_ONLY = "AND {jd:fileMedia} IS NOT NULL ";

	/** The Constant SORT_JOBS_BY_DATE_DESC. */
	private static final String SORT_JOBS_BY_DATE_DESC = "ORDER BY {jd:expirationDate} DESC, {pk}";

	/** The Constant SORT_JOBS_BY_DATE_ASC. */
	private static final String SORT_JOBS_BY_DATE_ASC = "ORDER BY {jd:expirationDate} ASC, {pk}";
	private String mainQuery;
	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;
	protected JnjConfigService jnjConfigService;
	protected JnjLatamCustomerServiceImpl jnjLatamCustomerService;
	private FlexibleSearchService flexibleSearchService;
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
		
	@Override
	public List<JnjLaudoModel> findLaudoDetailsExpiredDocument(final String countryCode) throws BusinessException {
		final String deleteQuery;
		List<JnjLaudoModel> laudo = null;
		final String expired=getJnjConfigService().getConfigValueById(Jnjlab2bcoreConstants.DOCUMENT_EXPIRE_DAYS+countryCode);
        final int expiredDays=StringUtils.isNotEmpty(expired) ? Integer.parseInt(expired):Jnjlab2bcoreConstants.DOCUMENT_EXPIRE_MIN_DAYS ;
        deleteQuery = DEFAULT_LAUDO_QUERY_EXPIRE;
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Jnjlab2bcoreConstants.DOCUMENT_EXPIRE_DATE_FORMAT);
		final LocalDateTime date=LocalDateTime.now().minusDays(expiredDays);
		final String documentDelete=dtf.format(date);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(deleteQuery);
		fQuery.addQueryParameter("documentDelete", documentDelete);
		fQuery.addQueryParameter("countryIso", countryCode);

		final SearchResult<JnjLaudoModel> result = flexibleSearchService.search(fQuery);
		if (result != null && result.getCount() > 0) {
			laudo = result.getResult();
		}
		return laudo;
	}

	@Override
	public SearchPageData<JnjLaudoModel> findPagedLaudoDetails(final JnjPageableData jnjPageableData, final boolean withMedia)
			throws BusinessException
	{
		final String METHOD_FIND_PAGED_LAUDO_DETAILS = "DAO - findPagedLaudoDetails()";

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_FIND_PAGED_LAUDO_DETAILS, Logging.BEGIN_OF_METHOD,
				JnjLaudoDaoImpl.class);

		ServicesUtil.validateParameterNotNull(jnjPageableData, "JnjPageableData cannot be null.");

		final CountryModel countryModel = getJnjLatamCustomerService().getCountryOfUser();
		ServicesUtil.validateParameterNotNull(countryModel, "Country associated with User cannot be null.");


		mainQuery = DEFAULT_LAUDO_QUERY;
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Jnjlab2bcoreConstants.DOCUMENT_EXPIRE_DATE_FORMAT);
		final LocalDateTime now = LocalDateTime.now();
		final String todayDate = dtf.format(now);
		final Map queryParams = new HashMap();
		queryParams.put("today", todayDate);
		queryParams.put("countryIso", countryModel.getIsocode());

		final String searchBy = jnjPageableData.getSearchBy();
		final String searchByJnjProductId = jnjPageableData.getSearchText();
		final String searchByLaudoId = jnjPageableData.getAdditionalSearchText();
		addProductInQueryParam(queryParams, searchBy, searchByJnjProductId, searchByLaudoId);
		/* Case where both From Date and TO Date is Entered */
		if (!StringUtils.equalsIgnoreCase(jnjCommonFacadeUtil.getMessageFromImpex("misc.services.selectDate"),
				jnjPageableData.getToDate())
				&& !StringUtils.equalsIgnoreCase(jnjCommonFacadeUtil.getMessageFromImpex("misc.services.selectDate"),
						jnjPageableData.getFromDate()))
		{
			final String toDate = getDateRange(jnjPageableData.getToDate(), true);
			final String fromDate = getDateRange(jnjPageableData.getFromDate(), false);
			mainQuery = mainQuery + FIND_LAUDO_ENTRIES_BY_USER_QUERY_WITH_FROM_DATE + FIND_LAUDO_ENTRIES_BY_USER_QUERY_WITH_TO_DATE;
			queryParams.put("toDate", toDate);
			queryParams.put("fromDate", fromDate);
		}
		/* Case where only From Date is Entered */
		else if (StringUtils.equalsIgnoreCase(jnjCommonFacadeUtil.getMessageFromImpex("misc.services.selectDate"),
				jnjPageableData.getToDate())
				&& !StringUtils.equalsIgnoreCase(jnjCommonFacadeUtil.getMessageFromImpex("misc.services.selectDate"),
						jnjPageableData.getFromDate()))
		{
			final String fromDate = getDateRange(jnjPageableData.getFromDate(), false);
			mainQuery = mainQuery + FIND_LAUDO_ENTRIES_BY_USER_QUERY_WITH_FROM_DATE;
			queryParams.put("fromDate", fromDate);

		}
		/* Case where only TO Date is Entered */
		else if (!StringUtils.equalsIgnoreCase(jnjCommonFacadeUtil.getMessageFromImpex("misc.services.selectDate"),
				jnjPageableData.getToDate())
				&& StringUtils.equalsIgnoreCase(jnjCommonFacadeUtil.getMessageFromImpex("misc.services.selectDate"),
						jnjPageableData.getFromDate()))
		{
			final String toDate = getDateRange(jnjPageableData.getToDate(), true);
			mainQuery = mainQuery + FIND_LAUDO_ENTRIES_BY_USER_QUERY_WITH_TO_DATE;
			queryParams.put("toDate", toDate);
		}
		if (withMedia)
		{
			mainQuery = mainQuery + FIND_LAUDO_ENTRIES_WITH_MEDIA_ONLY;
		}

		final List<SortQueryData> sortQueries = Arrays.asList(createSortQueryData("byDateDesc", mainQuery + SORT_JOBS_BY_DATE_DESC),
				createSortQueryData("byDateAsc", mainQuery + SORT_JOBS_BY_DATE_ASC));

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_FIND_PAGED_LAUDO_DETAILS, "Final laudo query: "+mainQuery,
				JnjLaudoDaoImpl.class);
		
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_FIND_PAGED_LAUDO_DETAILS, Logging.END_OF_METHOD,
				JnjLaudoDaoImpl.class);

		return pagedFlexibleSearchService.search(sortQueries, "byDateDesc", queryParams, jnjPageableData);

	}

	/**
	 * This method is to add the product code and laudo numer is query parameter
	 * @param queryParams the queryParams is the map for parameters
	 * @param searchBy the searchBy is the search criteria
	 * @param searchByJnjProductId the searchByJnjProductId is the search criteria
	 * @param searchByLaudoId the searchByLaudoId is the search criteria
	 */
	private void addProductInQueryParam(final Map queryParams, final String searchBy, final String searchByJnjProductId,
			final String searchByLaudoId) {
		
		final String METHOD_TO_ADD_QUERY_PARAM = "addProductInQueryParam()";
		
		if (StringUtils.isNotEmpty(searchBy) && StringUtils.equalsIgnoreCase(Laudo.LAUDO_SEARCH_BY_PRODUCT_AND_LAUDO, searchBy))
		{
			if(StringUtils.isEmpty(searchByLaudoId)){
				mainQuery = DEFAULT_LAUDO_QUERY + FIND_LAUDO_WITH_PRODUCTID_QUERY;
			} else {
				mainQuery = DEFAULT_LAUDO_QUERY + FIND_LAUDO_ENTRIES_BY_USER_QUERY;
				queryParams.put("laudoNumber", searchByLaudoId);
			}
			queryParams.put("productCode", searchByJnjProductId);
			
			JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_TO_ADD_QUERY_PARAM, "Main query: "+mainQuery+", Laudo id: "+searchByLaudoId+" and product id: "+searchByJnjProductId,
					JnjLaudoDaoImpl.class);
		}
	}

	private String getDateRange(final String date, final boolean isEndDate) throws BusinessException
	{
		final String METHOD_GET_DATE_RANGE = "DAO - getDateRange()";

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_DATE_RANGE, Logging.BEGIN_OF_METHOD,
				JnjLaudoDaoImpl.class);

		final SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
		final SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String reformattedStr = null;
		try
		{
			if (isEndDate)
			{
				final Calendar cal = Calendar.getInstance();
				cal.setTime(fromUser.parse(date));
				cal.add(Calendar.DATE, 1);
				cal.add(Calendar.SECOND, -1);
				myFormat.setCalendar(cal);
				reformattedStr = myFormat.format(cal.getTime());
			}
			else
			{
				final Calendar cal = Calendar.getInstance();
				cal.setTime(fromUser.parse(date));
				myFormat.setCalendar(cal);
				reformattedStr = myFormat.format(cal.getTime());
			}
		}
		catch (final ParseException parseException)

		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_DATE_RANGE, "Parsing Excpetion Occured.",
					parseException, JnjLaudoDaoImpl.class);
			throw new BusinessException("Parsing Excpetion Occured. ");
		}

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_DATE_RANGE, Logging.END_OF_METHOD,
				JnjLaudoDaoImpl.class);

		return reformattedStr;
	}

	private SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final String METHOD_CREATE_SORTED_QUERY_DATA = "DAO - createSortQueryData()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CREATE_SORTED_QUERY_DATA, Logging.BEGIN_OF_METHOD,
				JnjLaudoDaoImpl.class);

		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CREATE_SORTED_QUERY_DATA, Logging.END_OF_METHOD,
				JnjLaudoDaoImpl.class);
		return result;
	}

	@Override
	public JnjLaudoModel findLaudoByFileName(final String fileName) throws BusinessException
	{
		final String METHOD_FIND_LAUDO_BY_FILE_NAME = "DAO - findLaudoByFileName()";
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_FIND_LAUDO_BY_FILE_NAME, Logging.BEGIN_OF_METHOD,
				JnjLaudoDaoImpl.class);

		ServicesUtil.validateParameterNotNull(fileName, "FileName cannot be null.");
		final CountryModel countryModel = getJnjLatamCustomerService().getCountryOfUser();

		ServicesUtil.validateParameterNotNull(countryModel, "Country associated with User cannot be null.");

		JnjLaudoModel jnjLaudoModel = null;

		try
		{
			jnjLaudoModel = getLaudoByName(fileName, countryModel);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			JnjGTCoreUtil
					.logErrorMessage(
							Logging.LAUDO, METHOD_FIND_LAUDO_BY_FILE_NAME, "No JnjLaudoModel feteched from Hybris with File Name: "
									+ fileName + " for Country: " + countryModel.getName() + ". " + modelNotFoundException,
							JnjLaudoDaoImpl.class);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil
					.logErrorMessage(Logging.LAUDO, METHOD_FIND_LAUDO_BY_FILE_NAME,
							"No JnjLaudoModel feteched from Hybris with File Name: " + fileName + " for Country: "
									+ countryModel.getName() + ". " + " .Throwing Business Exception. " + illegalArgumentException,
							JnjLaudoDaoImpl.class);
			throw new BusinessException("IllegalArgumentException Occured While Fechting JnjLaudoModel from Hybris",
					Laudo.LAUDO_GENERAL_EXCEPTION_CODE, Severity.BUSINESS_EXCEPTION);
		}
		catch (final Throwable throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.LAUDO,
					METHOD_FIND_LAUDO_BY_FILE_NAME, "No JnjLaudoModel feteched from Hybris with File Name: " + fileName
							+ " for Country: " + countryModel.getName() + ". " + " .Throwing Business Exception. " + throwable,
					JnjLaudoDaoImpl.class);
			throw new BusinessException("Exception Occured While Fechting JnjLaudoModel from Hybris",
					Laudo.LAUDO_GENERAL_EXCEPTION_CODE, Severity.BUSINESS_EXCEPTION);
		}
		JnjGTCoreUtil.logInfoMessage(Logging.LAUDO, METHOD_FIND_LAUDO_BY_FILE_NAME,
				"JnjLaudoModel feteched from Hybris with File Name: " + fileName + " for Country: " + countryModel.getName(),
				JnjLaudoDaoImpl.class);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_FIND_LAUDO_BY_FILE_NAME, Logging.END_OF_METHOD,
				JnjLaudoDaoImpl.class);
		return jnjLaudoModel;
	}

	private JnjLaudoModel getLaudoByName(final String fileName, final CountryModel countryModel)
	{

		final String METHOD_GET_LAUDO_BY_NAME = "getLaudoByName()";
		
		JnjLaudoModel laudo = null;
		final String query = "select {pk} from {JnjLaudo} where {fileName}=?fileName and {country}=?countryModel";

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameter("fileName", fileName);
		fQuery.addQueryParameter("countryModel", countryModel);
		
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_BY_NAME, "Final Query for laudo file download: "+fQuery+" and Query Prameters- fileName: "+fileName+", countryModel: "+countryModel, JnjLaudoDaoImpl.class);
		
		final SearchResult<JnjLaudoModel> result = flexibleSearchService.search(fQuery);
		if (result != null && result.getCount() > 0)
		{
			laudo = result.getResult().get(0);
		}

		return laudo;
	}

	@Override
	public JnjLaudoModel getLaudoFromHybris(final String jnjProductCode, final String laudoNumber) throws BusinessException
	{
		final String METHOD_GET_LAUDO_FROM_HYBRIS = "DAO - getLaudoFromHybris()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_FROM_HYBRIS, Logging.BEGIN_OF_METHOD,
				JnjLaudoDaoImpl.class);
		ServicesUtil.validateParameterNotNull(jnjProductCode, "jnjProductCode cannot be null.");
		final CountryModel countryModel = getJnjLatamCustomerService().getCountryOfUser();
		ServicesUtil.validateParameterNotNull(countryModel, "Country associated with User cannot be null.");

		JnjLaudoModel jnjLaudoModel = null;

		try
		{
			jnjLaudoModel = getLaudo(laudoNumber, jnjProductCode, countryModel);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_FROM_HYBRIS,
					"No JnjLaudoModel feteched from Hybris with JnjProductId: " + jnjProductCode + " and Laudo Code: " + laudoNumber
							+ ". ",
					modelNotFoundException, JnjLaudoDaoImpl.class);

		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_FROM_HYBRIS,
					"Exception Occured While Fechting JnjLaudoModel from Hybris with JnjProductId: " + jnjProductCode
							+ " and Laudo Code: " + laudoNumber + " .Throwing Business Exception. ",
					illegalArgumentException, JnjLaudoDaoImpl.class);
			throw new BusinessException("IllegalArgumentException Occured While Fechting JnjLaudoModel from Hybris");
		}
		catch (final Throwable throwable)
		{
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO,
					METHOD_GET_LAUDO_FROM_HYBRIS, "Exception Occured While Fechting JnjLaudoModel from Hybris with JnjProductId: "
							+ jnjProductCode + " and Laudo Code: " + laudoNumber + " .Throwing Business Exception. " + throwable,
					JnjLaudoDaoImpl.class);
			throw new BusinessException("Exception Occured While Fechting JnjLaudoModel from Hybris");
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_FROM_HYBRIS,
				"JnjLaudoModel feteched from Hybris with JnjProductId: " + jnjProductCode + " and Laudo Code: " + laudoNumber + ". ",
				JnjLaudoDaoImpl.class);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_FROM_HYBRIS, Logging.END_OF_METHOD,
				JnjLaudoDaoImpl.class);
		return jnjLaudoModel;
	}

	private JnjLaudoModel getLaudo(final String laudoNumber, final String productCode, final CountryModel countryModel)
	{
        JnjLaudoModel laudo = null;
		String query;
		FlexibleSearchQuery fQuery = null;
		if(StringUtils.isNotEmpty(laudoNumber)){
			query = "select {pk} from {JnjLaudo} where {laudoNumber}=?laudoNumber and {productCode}=?productCode and {country}=?countryModel ";
			fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameter("laudoNumber", laudoNumber);
		} else {
			query = "select {pk} from {JnjLaudo} where {productCode}=?productCode and {country}=?countryModel ";
			fQuery = new FlexibleSearchQuery(query);
		}
		fQuery.addQueryParameter("productCode", productCode);
		fQuery.addQueryParameter("countryModel", countryModel);
        final SearchResult<JnjLaudoModel> result = flexibleSearchService.search(fQuery);
		if (result != null && result.getCount() > 0){
			laudo = result.getResult().get(0);
		}

		return laudo;
	}
	public void setJnjCommonFacadeUtil(JnjCommonFacadeUtil jnjCommonFacadeUtil) {
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}
	public JnjLatamCustomerServiceImpl getJnjLatamCustomerService() {
        return jnjLatamCustomerService;
    }
    public void setJnjLatamCustomerService(JnjLatamCustomerServiceImpl jnjLatamCustomerService) {
        this.jnjLatamCustomerService = jnjLatamCustomerService;
    } 
	public JnjConfigService getJnjConfigService() {
		return this.jnjConfigService;
	}
    public void setJnjConfigService(final JnjConfigService jnjConfigService) {
		this.jnjConfigService = jnjConfigService;
	}
    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
