/**
 *
 */
package com.jnj.facades.contract.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.apache.log4j.Logger;
import org.assertj.core.util.IterableUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.contract.JnjContractService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.cart.impl.DefaultJnjCartFacade;
import com.jnj.facades.contract.JnjGTContractFacade;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractData;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;


/**
 * The Class JnjContractFacadeImpl.
 *
 * @author komal.sehgal
 */
public class DefaultJnjGTContractFacadeImpl implements JnjGTContractFacade
{

	/**  */
	private static final String GET_ALL_ACTIVE_CONTRACTS = "getAllActiveContracts()";
	protected static final String CART_MODIFICATAION_DATA = "cartModificationData";

	/** The jnj contract service. */
	@Autowired
	JnjContractService jnjContractService;

	/** The cart service. */
	@Autowired
	private CartService cartService;

	@Resource(name="GTCartFacade")
	protected JnjGTCartFacade jnjGTCartFacade;

	/** The jnj contract data converter. */
	@Autowired
	private Converter<JnjContractModel, JnjContractData> jnjContractDataConverter;

	@Autowired
	private JnJGTProductService jnjGTProductService;

	/** The Constant CLASS_NAME. */
	private static final String CLASS_NAME = DefaultJnjCartFacade.class.getName();

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

	/*
	 * (non-Javadoc) This method is used for getting the contracts For B2BUnit
	 *
	 * @see com.jnj.facades.contract.JnjContractFacade#getContracts()
	 */
	@Override
	public List<JnjContractData> getContracts()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - "
					+ Jnjb2bCoreConstants.Logging.CONTRACTS_GET_CONTRACTS_METHOD + "-" + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}

		final Collection<JnjContractModel> jnjContractModelCollection = jnjContractService.getContracts();
		final List<JnjContractData> JnjContractDataList = new ArrayList<JnjContractData>();

		for (final JnjContractModel jnJContractModel : jnjContractModelCollection)
		{
			JnjContractData jnjContractData = new JnjContractData();
			jnjContractData = jnjContractDataConverter.convert(jnJContractModel, jnjContractData);
			JnjContractDataList.add(jnjContractData);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - "
					+ Jnjb2bCoreConstants.Logging.CONTRACTS_GET_CONTRACTS_METHOD + "-" + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}

		return JnjContractDataList;
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.contract.JnjContractFacade#getContractDetailsById(java.lang.String, int)
	 */
	@Override
	public JnjContractData getContractDetailsById(final String eCCContractNum, final int entryCount)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_DETAILS_BY_ID
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjContractModel jnjContractModel = jnjContractService.getContractDetailsById(eCCContractNum);
		JnjContractData jnjContractData = new JnjContractData();
		jnjContractData.setEntryCount(Integer.valueOf(entryCount));
		jnjContractData = jnjContractDataConverter.convert(jnjContractModel, jnjContractData);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_DETAILS_BY_ID
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjContractData;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.contract.JnjContractFacade#isProductsExistsInContract(java.util.List)
	 */
	@Override
	public boolean isProductsExistsInContract(final List<String> productCodes)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final boolean exists = jnjContractService.isProductsExistsInContract(productCodes);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return exists;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.jnj.facades.contract.JnjContractFacade#getPagedOrderTemplates(de.hybris.platform.commerceservices.search.pagedata
	 * .PageableData, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */  //onChange sortBy field
	@Override
	public SearchPageData<JnjContractData> getPagedContracts(final PageableData pageableData, final String searchByCriteria,
			final String searchParameter, final String sortByCriteria, final String selectCriteria, final String filterCriteria2)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - "
					+ Jnjb2bCoreConstants.Logging.CONTRACT_GET_PAGED_CONTRACTS + "-" + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final SearchPageData<JnjContractModel> contractResults = jnjContractService.getPagedContracts(pageableData,
				searchByCriteria, searchParameter, sortByCriteria, selectCriteria, filterCriteria2);
		final Collection<JnjContractModel> contractModelList = contractResults.getResults();
		final List<JnjContractData> jnjContractDataList = new ArrayList<JnjContractData>();
		for (final JnjContractModel jnJContractModel : contractModelList)
		{
			JnjContractData jnjContractData = new JnjContractData();
			jnjContractData = jnjContractDataConverter.convert(jnJContractModel, jnjContractData);
			jnjContractDataList.add(jnjContractData);
		}
		final SearchPageData<JnjContractData> result = new SearchPageData<JnjContractData>();
		result.setResults(jnjContractDataList);
		result.setPagination(contractResults.getPagination());


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - "
					+ Jnjb2bCoreConstants.Logging.CONTRACT_GET_PAGED_CONTRACTS + "-" + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.contract.JnjContractFacade#getRecenlyUsedContracts()
	 */
	@Override
	public List<JnjContractData> getRecenlyUsedContracts()
	{
		final List<JnjContractModel> getRecenlyUsedContracts = jnjContractService.getRecenlyUsedContracts();
		final List<JnjContractData> jnjContractDataList = new ArrayList<JnjContractData>();
		for (final JnjContractModel jnJContractModel : getRecenlyUsedContracts)
		{
			JnjContractData jnjContractData = new JnjContractData();
			jnjContractData = jnjContractDataConverter.convert(jnJContractModel, jnjContractData);
			jnjContractDataList.add(jnjContractData);
		}
		return jnjContractDataList;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 */
	@Override
	public boolean isContractBid(final String contracrId)
	{
		return jnjContractService.isContractBid(contracrId);
	}

	@SuppressWarnings("unused")
	@Override
	public boolean isContractrValidForProducts(final List<String> productCodes)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final boolean validContract = jnjContractService.isContractrValidForProducts(productCodes);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return validContract;
	}

	@Override
	public boolean checkAllActiveContracts() throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		/* Start index of the DB Fetch */
		int startIndex = 0;
		/* Start Index Shift which needs to be adjusted in start index if a contrct is changes from Active to Inactive */
		int startIndexShift = 0;
		/* Batch Size of each DB Fetch */
		final int batchSize = Config.getInt(Jnjb2bCoreConstants.FILE_SIZE_LIMIT, 5);
		/* Total no of Records */
		int totalNoOfRecords = 0;
		final String CONTRACT_STATUS_ACTIVE = "Active";
		boolean endOfBatch = false;
		boolean processResult = false;
		/* Loop 'endOfBatch' flag is 'True' */
		while (!endOfBatch)
		{
			List<JnjContractModel> activeContracts = null;
			SearchResult<JnjContractModel> searchResult = null;
			LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
					+ "##############Start of Contract Batch Update#################");
			try
			{
				searchResult = jnjContractService.getActiveContracts(CONTRACT_STATUS_ACTIVE, batchSize, startIndex);
			}
			catch (final BusinessException businessException)
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
						+ "BusinessException occured. Terminating Batch Update." + businessException);
				endOfBatch = true;
				break;
			}
			/* Exit Loop and Terminate Batch if Search Result is Null */
			if (searchResult == null)
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
						+ "No Contracts with status [Active] fetched from Hybris.");
				endOfBatch = true;
				break;
			}

			/* Fetching total Records from the Result */
			totalNoOfRecords = searchResult.getTotalCount();

			activeContracts = searchResult.getResult();
			if (!(IterableUtil.isNullOrEmpty(activeContracts)))
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
						+ "Contracts with status [Active] fetched from Hybris.");

				for (final JnjContractModel jnjContractModel : activeContracts)
				{
					LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS
							+ Logging.HYPHEN + "Contracts with ID [" + jnjContractModel.getECCContractNum()
							+ "] is expired. Changin status to [Inactive]");
					jnjContractModel.setActive(Boolean.FALSE);
					if (jnjContractService.saveContract(jnjContractModel))
					{
						startIndexShift = startIndexShift + 1;
					}
				}
			}
			else
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
						+ "No Contracts with status [Active] fetched from Hybris.");
				endOfBatch = true;
				break;
			}
			/*
			 * Updating Batch Parameters. StartIndex is added with BatchSize for the next set of Batch. Also we have to
			 * decrease the index with 'startIndexShift' as these records have changed from [Active] to [Inactive], thus
			 * decreasing the total record count as whole.
			 */
			startIndex = startIndex + batchSize - startIndexShift;
			/*
			 * Checking if End of Batch is Reached or Not. If StartIndex is Greater than the Total No. Of Records then
			 * terminate the Batch
			 */
			if (startIndex >= totalNoOfRecords || startIndex < 0)
			{
				endOfBatch = true;
			}

		}
		LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
				+ "################End of  Contract Batch Update#####################");

		if (startIndexShift > 0)
		{
			processResult = true;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return processResult;
	}

	@Override
	public boolean checkAllInActiveContracts() throws BusinessException
	{
		final String METHOD_NAME = "checkAllInActiveContracts ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		/* Start index of the DB Fetch */
		int startIndex = 0;
		/* Start Index Shift which needs to be adjusted in start index if a contrct is changes from Active to Inactive */
		int startIndexShift = 0;
		/* Batch Size of each DB Fetch */
		final int batchSize = Config.getInt(Jnjb2bCoreConstants.FILE_SIZE_LIMIT, 5);
		/* Total no of Records */
		int totalNoOfRecords = 0;
		final String CONTRACT_STATUS_IN_ACTIVE = "Inactive";
		boolean endOfBatch = false;
		boolean processResult = false;
		boolean contractStatus = false;
		/* Loop 'endOfBatch' flag is 'True' */
		while (!endOfBatch)
		{
			List<JnjContractModel> activeContracts = null;
			SearchResult<JnjContractModel> searchResult = null;
			try
			{
				searchResult = jnjContractService.getActiveContracts(CONTRACT_STATUS_IN_ACTIVE, batchSize, startIndex);
			}
			catch (final BusinessException businessException)
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "BusinessException occured. Terminating Batch Update." + businessException);
				endOfBatch = true;
				break;
			}
			/* Exit Loop and Terminate Batch if Search Result is Null */
			if (searchResult == null)
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "No Contracts with status [Inactive] fetched from Hybris.");
				endOfBatch = true;
				break;
			}

			/* Fetching total Records from the Result */
			totalNoOfRecords = searchResult.getTotalCount();

			activeContracts = searchResult.getResult();
			if (!(IterableUtil.isNullOrEmpty(activeContracts)))
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "Contracts with status [Inactive] fetched from Hybris.");

				for (final JnjContractModel jnjContractModel : activeContracts)
				{
					final String entryStatus = jnjContractModel.getStatus();
					if (entryStatus.equalsIgnoreCase("A") || entryStatus.equalsIgnoreCase("B"))
					{
						contractStatus = true;
						LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
								+ "Contracts with ID [" + jnjContractModel.getECCContractNum()
								+ "] is valid. Changing status to [Active]");
					}
					if (contractStatus)
					{
						jnjContractModel.setActive(Boolean.TRUE);
					}
					else
					{
						jnjContractModel.setActive(Boolean.FALSE);
						startIndexShift++;
					}

					jnjContractService.saveContract(jnjContractModel);

					contractStatus = false;
				}
			}
			else
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "No Contracts with status [Inactive] fetched from Hybris.");
				endOfBatch = true;
				break;
			}

			/*
			 * Updating startIndex: if all contracts were Activated, it will be always set to zero. Otherwise, it will add
			 * the count of Inactive contracts which have been looked at to start after them.
			 */
			startIndex += startIndexShift;
			/*
			 * Checking if End of Batch is Reached or Not. If StartIndex is Greater than the Total No. Of Records then
			 * terminate the Batch
			 */
			if (startIndex >= totalNoOfRecords)
			{
				endOfBatch = true;
			}

		}
		if (endOfBatch)
		{
			processResult = true;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return processResult;
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.contract.JnjContractFacade#checkActiveContractsForEntry()
	 */
	@Override
	public boolean checkActiveContractsForEntry() throws BusinessException
	{
		final String METHOD_NAME = "checkActiveContractsForEntry ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		/* Start index of the DB Fetch */
		int startIndex = 0;
		/* Start Index Shift which needs to be adjusted in start index if a contrct is changes from Active to Inactive */
		int startIndexShift = 0;
		/* Batch Size of each DB Fetch */
		final int batchSize = Config.getInt(Jnjb2bCoreConstants.FILE_SIZE_LIMIT, 5);
		/* Total no of Records */
		int totalNoOfRecords = 0;
		final String CONTRACT_STATUS_ACTIVE = "Active";
		boolean endOfBatch = false;
		boolean processResult = false;
		boolean contractEntryStatus = false;
		/* Loop 'endOfBatch' flag is 'True' */
		while (!endOfBatch)
		{
			List<JnjContractModel> activeContracts = null;
			SearchResult<JnjContractModel> searchResult = null;
			LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
					+ "##############Start of Contract Batch Update#################");
			try
			{
				searchResult = jnjContractService.getActiveContractsForEntry(CONTRACT_STATUS_ACTIVE, batchSize, startIndex);
			}
			catch (final BusinessException businessException)
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
						+ "BusinessException occured. Terminating Batch Update." + businessException);
				endOfBatch = true;
				break;
			}
			/* Exit Loop and Terminate Batch if Search Result is Null */
			if (searchResult == null)
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
						+ "No Contracts with status [Active] fetched from Hybris.");
				endOfBatch = true;
				break;
			}

			/* Fetching total Records from the Result */
			totalNoOfRecords = searchResult.getTotalCount();

			activeContracts = searchResult.getResult();
			if (!(IterableUtil.isNullOrEmpty(activeContracts)))
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
						+ "Contracts with status [Active] fetched from Hybris.");

				for (final JnjContractModel jnjContractModel : activeContracts)
				{
					LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS
							+ Logging.HYPHEN + "Contracts with ID [" + jnjContractModel.getECCContractNum()
							+ "] is expired. Changin status to [Inactive]");
					for (final JnjContractEntryModel jnjContractEntryModel : jnjContractModel.getJnjContractEntries())
					{
						final String entryStatus = jnjContractEntryModel.getStatus();
						if (entryStatus.equalsIgnoreCase("A") || entryStatus.equalsIgnoreCase("B"))
						{
							contractEntryStatus = true;
						}
						if (contractEntryStatus)
						{
							break;
						}
					}
					LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
							+ "Contracts with ID [" + jnjContractModel.getECCContractNum() + "] is valid. Changing status to [Active]");
					if (contractEntryStatus)
					{
						jnjContractModel.setActive(Boolean.TRUE);
					}
					else
					{
						jnjContractModel.setActive(Boolean.FALSE);
					}


					if (jnjContractService.saveContract(jnjContractModel))
					{
						startIndexShift = startIndexShift + 1;
					}
				}
			}
			else
			{
				LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
						+ "No Contracts with status [Active] fetched from Hybris.");
				endOfBatch = true;
				break;
			}
			/*
			 * Updating Batch Parameters. StartIndex is added with BatchSize for the next set of Batch. Also we have to
			 * decrease the index with 'startIndexShift' as these records have changed from [Active] to [Inactive], thus
			 * decreasing the total record count as whole.
			 */
			startIndex = startIndex + batchSize - startIndexShift;
			/*
			 * Checking if End of Batch is Reached or Not. If StartIndex is Greater than the Total No. Of Records then
			 * terminate the Batch
			 */
			if (startIndex > totalNoOfRecords || startIndex < 0)
			{
				endOfBatch = true;
			}

		}
		LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
				+ "################End of  Contract Batch Update#####################");

		if (startIndexShift > 0)
		{
			processResult = true;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_FACADE + Logging.HYPHEN + GET_ALL_ACTIVE_CONTRACTS + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return processResult;
	}
	/** ADD TO CART FROM CONTRACT **/
	@Override
	public Map<String, Object> addToCartFromContract(Map<String, Object> responseMap,
			String[] selectedProductCatalogIds,String contractNum) {
		final long qty = Jnjb2bCoreConstants.DEFAULT_ADD_TO_CART_QTY;
		List<String> selectedProductList = Arrays.asList(selectedProductCatalogIds);
		for (final String productTobeAdded : selectedProductList) {
			JnjCartModificationData cartModificationData;
				try {
					// Add To Cart 
					cartModificationData = jnjGTCartFacade.addToCart(productTobeAdded, String.valueOf(qty));
					// Setting Contract number against Product number
					jnjGTCartFacade.updateContractNumInCartEntry(productTobeAdded,contractNum);
					//End 
					if (!cartModificationData.getCartModifications().get(0).isError())
					{
					responseMap.put(CART_MODIFICATAION_DATA, cartModificationData);
						responseMap.put(productTobeAdded, Jnjb2bCoreConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS);
				} else 
					{
						responseMap.put(productTobeAdded, cartModificationData.getCartModifications().get(0).getStatusCode());
					}
				} catch (CommerceCartModificationException e) {
					e.printStackTrace();
				}
		}
		return responseMap;
	}
}
