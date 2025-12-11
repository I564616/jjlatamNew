package com.jnj.la.core.dao.contract.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.dao.contract.JnjContractDao;


/**
 * The Class JnjContractDaoImpl.
 *
 * @author komal.sehgal
 */
public class JnjContractDaoImpl extends AbstractItemDao implements JnjContractDao
{

	private static final String CONTRACT_STATUS = "contractStatus";

	private static final String START_DATE = "startDate";

	private static final String END_DATE = "endDate";

	private static final String GET_ALL_CONTRACT_WITH_STATUS = "getAllContractWithStatus()";

	private static final String CONSTANT_NUMERIC_0 = "0";

	private static final String CONSTANT_NUMERIC_1 = "1";

	private static final String CONTRACT_STATUS_INACTIVE = "Inactive";

	private static final String CONTRACT_STATUS_ACTIVE = "Active";

	/** The Constant CLASS_NAME. */
	private static final String CLASS_NAME = JnjContractDaoImpl.class.getName();

	protected SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;

	private final String LIKE_CHARACTER = "%";

	@Autowired
	private UserService userService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	protected ModelService modelService;


	@Override
	public JnjContractModel getContractDetailsById(final String eCCContractNum)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_DETAILS_BY_ID + "-"
					+ Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		ServicesUtil.validateParameterNotNull(eCCContractNum, "eCCContractNum must not be null");
		try
		{
			final Map queryParams = new HashMap();
			final String query = Jnjb2bCoreConstants.Contract.FIND_CONTRACT_BY_NUMBER;
			queryParams.put(Jnjb2bCoreConstants.Contract.ECCUMBER, eCCContractNum);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			final JnjContractModel result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public JnjContractModel execute()
				{
					return getFlexibleSearchService().searchUnique(fQuery);
				}
			}, userService.getAnonymousUser());
			return result;
		}

		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_DETAILS_BY_ID
					+ "Contract Not Found For Given Id" + modelNotFoundException + JnJCommonUtil.getCurrentDateTime());
			return null;
		}

	}

	@Override
	public SearchPageData<JnjContractModel> getPagedContracts(final PageableData pageableData, final String searchByCriteria,
			final String searchParameter, final String sortByCriteria, final String selectCriteria)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_GET_PAGED_CONTRACTS
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		final SearchPageData<JnjContractModel> contracts = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public SearchPageData<JnjContractModel> execute()
			{
				String subQuery = null;
				String contractOrderReasonQuery = null;

				final Map<String, Object> queryParams = new HashMap<>();
				queryParams.put("selectedB2BUnit", jnjB2BUnitModel.getPk().toString());

				List<SortQueryData> sortQueries = null;

				if (searchParameter != null)
				{
					if (searchParameter.equals(Jnjb2bCoreConstants.Contract.CONTRACTNUMBER))
					{
						subQuery = Jnjb2bCoreConstants.Contract.FIND_BY_CONTRACT_NUMBER;
					}
					else if (searchParameter.equals(Jnjb2bCoreConstants.Contract.TENDERNUMBER))
					{
						subQuery = Jnjb2bCoreConstants.Contract.FIND_BY_TENDER_NUMBER;
					}
					else if (searchParameter.equals(Jnjb2bCoreConstants.Contract.INDIRECTCUSTOMER))
					{
						subQuery = Jnjb2bCoreConstants.Contract.FIND_BY_INDIRECT_NUMBER;
					}

					if (selectCriteria != null && (selectCriteria.equals(Jnjb2bCoreConstants.Contract.BID)
							|| selectCriteria.equals(Jnjb2bCoreConstants.Contract.COMMERCIAL)))
					{

						contractOrderReasonQuery = Jnjb2bCoreConstants.Contract.FIND_BY_CONTRCAT_TYPE;
					}
				}

				//This will execute if the user will neither use  any selection for contract type nor searching.
				if (StringUtils.isEmpty(searchByCriteria) && StringUtils.isEmpty(selectCriteria))
				{
					sortQueries = Arrays.asList(
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_NUMBER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_NUMBER
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CREATION_DATE_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_CREATIONDATE),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CREATION_DATE_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_CREATIONDATE
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_INDIRECT_CUSTOMER_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_INDIRECT_CUSTOMER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_INDIRECT_CUSTOMER_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_INDIRECT_CUSTOMER
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER));
				}
				else if ((StringUtils.isNotEmpty(searchParameter) && StringUtils.isNotEmpty(searchByCriteria)
						&& StringUtils.isEmpty(selectCriteria)))
				{
					queryParams.put("searchByCriteria", LIKE_CHARACTER + searchByCriteria + LIKE_CHARACTER);
					sortQueries = Arrays.asList(
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_NUMBER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_NUMBER
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CREATION_DATE_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_CREATIONDATE),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CREATION_DATE_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_CREATIONDATE
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_INDIRECT_CUSTOMER_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_INDIRECT_CUSTOMER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_INDIRECT_CUSTOMER_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_INDIRECT_CUSTOMER
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER));
				}

				//This will execute if the user will  selection for contract type without searching.
				else if (StringUtils.isNotEmpty(selectCriteria) && StringUtils.isEmpty(searchByCriteria))
				{

					queryParams.put("selectCriteria", selectCriteria);
					sortQueries = Arrays.asList(
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_NUMBER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_NUMBER
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CREATION_DATE_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_CREATIONDATE),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CREATION_DATE_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_CREATIONDATE
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_INDIRECT_CUSTOMER_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_INDIRECT_CUSTOMER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_INDIRECT_CUSTOMER_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_INDIRECT_CUSTOMER
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER));
				}

				//This will execute if the user will use both
				else if (StringUtils.isNotEmpty(searchParameter) && StringUtils.isNotEmpty(selectCriteria)
						&& StringUtils.isNotEmpty(searchByCriteria))
				{
					queryParams.put("searchByCriteria", LIKE_CHARACTER + searchByCriteria + LIKE_CHARACTER);
					queryParams.put("selectCriteria", selectCriteria);
					sortQueries = Arrays.asList(
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_NUMBER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_NUMBER
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CREATION_DATE_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_CREATIONDATE),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_CREATION_DATE_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_CREATIONDATE
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_INDIRECT_CUSTOMER_INC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_INDIRECT_CUSTOMER),
							createSortQueryData(Jnjb2bCoreConstants.Contract.SORT_BY_INDIRECT_CUSTOMER_DESC,
									Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT + subQuery + contractOrderReasonQuery
											+ Jnjb2bCoreConstants.Contract.SORT_CONTRACT_BY_INDIRECT_CUSTOMER
											+ Jnjb2bCoreConstants.Contract.SORT_ORDER));

				}

				return pagedFlexibleSearchService.search(sortQueries, sortByCriteria, queryParams, pageableData);
			}
		}, userService.getAnonymousUser());

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_GET_PAGED_CONTRACTS
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return contracts;
	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.core.dao.contract.JnjContractDao#getRecenlyUsedContracts()
	 */
	@Override
	public List<JnjContractModel> getRecenlyUsedContracts()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + " - " + Jnjb2bCoreConstants.Logging.GET_RECENTLY_USED_CONTRACTS
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final Map queryParams = new HashMap();
		final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		queryParams.put("unit", jnjB2BUnitModel.getUid());
		final String query = "select {pk} from {JnjContract AS cont JOIN Order AS ord on {cont:eCCContractNum}={ord:contractNumber} JOIN JNJB2bUnit as unit on "
				+ "{unit:pk}={ord:unit}} where {unit.uid}=?unit and {cont:active}= '1' AND {ord:contractNumber} is not null order by {ord.creationTime} desc ";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		final SearchResult<JnjContractModel> result = getFlexibleSearchService().search(fQuery);
		final List<JnjContractModel> distinctcontractList = new ArrayList<>();
		final List<JnjContractModel> contractList = result.getResult();
		for (final JnjContractModel jnJContractModel : contractList)
		{
			if (!distinctcontractList.contains(jnJContractModel) && distinctcontractList.size() < 3)
			{
				distinctcontractList.add(jnJContractModel);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + " - " + Jnjb2bCoreConstants.Logging.GET_RECENTLY_USED_CONTRACTS
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return distinctcontractList;
	}


	@Override
	public List<JnjContractModel> getContractsForB2BUnit()
	{
		try
		{
			final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
			List<JnjContractModel> contractModelList = new ArrayList<>();
			final Map<String, Object> queryParams = new HashMap<>();
			queryParams.put("selectedB2BUnit", jnjB2BUnitModel.getPk().toString());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(Jnjb2bCoreConstants.Contract.GET_CONTRACT_BY_B2BUNIT);
			fQuery.addQueryParameters(queryParams);
			final SearchResult<JnjContractModel> result = getFlexibleSearchService().search(fQuery);
			contractModelList = result.getResult();
			return contractModelList;
		}

		catch (final ModelNotFoundException e)
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_DETAILS_BY_ID
					+ "Contract Not Found For Given Id" + e + JnJCommonUtil.getCurrentDateTime());
			return null;
		}


	}



	@Override
	public SearchResult<JnjContractModel> getAllContractWithStatus(final String status, final int startIndex)
			throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + GET_ALL_CONTRACT_WITH_STATUS + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		String query = null;
		final Map queryParams = new HashMap();
		LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + GET_ALL_CONTRACT_WITH_STATUS + Logging.HYPHEN
				+ "Fetching All contracts with status [Active]");
		LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + GET_ALL_CONTRACT_WITH_STATUS + Logging.HYPHEN
				+ "and Start Index is [" + startIndex + "]");

		String contractStatus = null;
		final Date date = new Date();
		if (StringUtils.isEmpty(status))
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + GET_ALL_CONTRACT_WITH_STATUS + Logging.HYPHEN
					+ "No status recieved. Throwing Business Exception");
			throw new BusinessException("No status recieved.");
		}
		else if (StringUtils.equalsIgnoreCase(status, CONTRACT_STATUS_ACTIVE))
		{
			query = "select {pk} from {JnjContract} where {ACTIVE}=?contractStatus and ({endDate} < CONVERT(DATETIME,?endDate) or {startDate} > CONVERT(DATETIME,?startDate))";
			contractStatus = CONSTANT_NUMERIC_1;
		}
		else if (StringUtils.equalsIgnoreCase(status, CONTRACT_STATUS_INACTIVE))
		{
			query = "select {pk} from {JnjContract} where {ACTIVE}= ?contractStatus and {startDate} <= CONVERT(DATETIME,?startDate) and {endDate} >= CONVERT(DATETIME,?endDate) order by {pk}";
			contractStatus = CONSTANT_NUMERIC_0;
		}

		SearchResult<JnjContractModel> result = null;
		try
		{
			queryParams.put(CONTRACT_STATUS, contractStatus);
			queryParams.put(START_DATE, formatter.format(date));
			queryParams.put(END_DATE, formatter.format(date));
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.setDisableSearchRestrictions(true);
			fQuery.setNeedTotal(true);
			fQuery.setStart(startIndex);
			fQuery.addQueryParameters(queryParams);
			result = getFlexibleSearchService().search(fQuery);

			LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + GET_ALL_CONTRACT_WITH_STATUS + Logging.HYPHEN
					+ "Total No. of Contracts with status [Active] are [" + result.getTotalCount() + "]");

			if (result.getTotalCount() == 0)
			{
				LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + GET_ALL_CONTRACT_WITH_STATUS
						+ Logging.HYPHEN + "Contracts with Statis " + status + " not found. Throwing Business Exception.");
				throw new BusinessException("Contracts with Status " + status + " not found. Throwing Business Exception.");
			}

		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + GET_ALL_CONTRACT_WITH_STATUS + Logging.HYPHEN
					+ "Contracts with Statis [Active] not found. Throwing Business Exception." + modelNotFoundException);
			throw new BusinessException("Contracts with Statis " + status + " not found.");

		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + GET_ALL_CONTRACT_WITH_STATUS + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.core.dao.contract.JnjContractDao#getAllContractForEntry(java.lang.String, int, int)
	 */
	@Override
	public SearchResult<JnjContractModel> getAllContractForEntry(final String status, final int startIndex)
			throws BusinessException
	{
		final String METHOD_NAME = "getAllContractForEntry ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		String query = null;
		final Map queryParams = new HashMap();
		LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Fetching All contracts with status [Active]");
		LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ " and Start Index is [" + startIndex + "]");

		String contractStatus = null;
		if (StringUtils.isEmpty(status))
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "No status recieved. Throwing Business Exception");
			throw new BusinessException("No status recieved.");
		}
		else if (StringUtils.equalsIgnoreCase(status, CONTRACT_STATUS_ACTIVE))
		{
			query = "select {pk} from {JnjContract} where {ACTIVE}=?contractStatus";
			contractStatus = CONSTANT_NUMERIC_1;
		}

		SearchResult<JnjContractModel> result = null;
		try
		{
			queryParams.put(CONTRACT_STATUS, contractStatus);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.setDisableSearchRestrictions(true);
			fQuery.setNeedTotal(true);
			fQuery.setStart(startIndex);
			fQuery.addQueryParameters(queryParams);
			result = getFlexibleSearchService().search(fQuery);

			LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Total No. of Contracts with status [Active] are [" + result.getTotalCount() + "]");

			if (result.getTotalCount() == 0)
			{
				LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "Contracts with Statis " + status + " not found. Throwing Business Exception.");
				throw new BusinessException("Contracts with Status " + status + " not found. Throwing Business Exception.");
			}

		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Contracts with Statis [Active] not found. Throwing Business Exception." + modelNotFoundException);
			throw new BusinessException("Contracts with Statis " + status + " not found.");

		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_DAO + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}
}