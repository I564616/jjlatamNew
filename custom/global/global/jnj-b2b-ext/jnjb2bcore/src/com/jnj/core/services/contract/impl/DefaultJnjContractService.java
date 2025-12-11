/**
 *
 */
package com.jnj.core.services.contract.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.LoadContract;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.contract.JnjContractDao;
import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.model.JnjImtContractModel;
import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.contract.JnjContractService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;


/**
 * The Class JnjContractServiceImpl.
 * 
 * @author komal.sehgal
 */
public class DefaultJnjContractService implements JnjContractService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.contract.JnjContractService#getContracts()
	 */

	/**  */
	protected static final String GET_ACTIVE_CONTRACTS = "getActiveContracts()";

	/** The Constant LOG. */
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjContractService.class);

	/** The Constant METHOD_SAVE_ITM_CONTRACT_MODEL. */
	protected static final String METHOD_SAVE_ITM_CONTRACT_MODEL = "saveItmContractModel()";

	/** The Constant METHOD_GET_IMT_CONTRACT_MODEL. */
	protected static final String METHOD_GET_IMT_CONTRACT_MODEL = "getJnjImtContractModel()";

	/** The Constant METHOD_GET_IMT_CONTRACTS_MODEL. */
	protected static final String METHOD_GET_IMT_CONTRACTS_MODEL = "getJnjImtContractModels()";

	/** The user service. */
	@Autowired
	protected UserService userService;

	/** The model service. */
	@Autowired
	protected ModelService modelService;

	/** The jnj contract dao. */
	@Resource(name="jnjConTractDao")
	protected JnjContractDao jnjContractDao;

	/** The flexible search service. */
	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	/** The cart service. */
	@Autowired
	protected CartService cartService;

	@Autowired
	protected JnJGTProductService jnjGTProductService;


	@Override
	public Collection<JnjContractModel> getContracts()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - "
					+ Jnjb2bCoreConstants.Logging.CONTRACTS_GET_CONTRACTS_METHOD + "-" + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		Collection<JnjContractModel> contractModelCollection = null;
		try
		{
			contractModelCollection = jnjContractDao.getContractsForB2BUnit();
		}

		catch (final SystemException exception)
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - "
					+ Jnjb2bCoreConstants.Logging.CONTRACTS_GET_CONTRACTS_METHOD + "-" + "No Default B2B Unit for currrent User"
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return contractModelCollection;
	}


	@Override
	public JnjContractModel getContractDetailsById(final String eCCContractNum)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_DETAILS_BY_ID
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjContractDao.getContractDetailsById(eCCContractNum);
	}


	@Override
	public boolean isContractBid(final String contracrId)
	{
		boolean isContractBid = false;
		if (StringUtils.isNotEmpty(contracrId))
		{
			final JnjContractModel jnjContractModel = getContractDetailsById(contracrId);
			if (null != jnjContractModel
					&& StringUtils.equalsIgnoreCase(jnjContractModel.getContractOrderReason(),
							Jnjb2bCoreConstants.LoadContract.LOAD_CONTRACT_ORDER_REASON_1))
			{
				isContractBid = true;
			}
			else
			{
				LOGGER.error("Add to Cart - isContractBid - Contract With ID: " + contracrId + "not Found for in Hybris.");
			}
		}
		return isContractBid;

	}


	/**
	 * The method is used to save the contracts to hybris.
	 * 
	 * @param jnjContractModel
	 *           the jnj contract model
	 * @return true, if successful
	 */
	@Override
	public boolean saveContract(final JnjContractModel jnjContractModel) throws BusinessException
	{
		boolean recordStatus = false;
		String errorMessage = null;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN + Jnjb2bCoreConstants.Logging.SAVECONTRACT
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		if (null != jnjContractModel)
		{
			try
			{
				modelService.save(jnjContractModel);
			}
			catch (final ModelSavingException exception)
			{
				errorMessage = "Error occured while saving Contracts with ECCContractNum:" + jnjContractModel.getECCContractNum()
						+ "Error Message: " + exception.getMessage();
				LOGGER.error(errorMessage + JnJCommonUtil.getCurrentDateTime());
				throw new BusinessException(errorMessage);
			}
			recordStatus = true;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN + Jnjb2bCoreConstants.Logging.SAVECONTRACT
					+ Logging.HYPHEN + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;

	}

	@Override
	public boolean saveItmContractModel(final JnjImtContractModel jnjImtContractModel) throws BusinessException
	{
		boolean recordStatus = false;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_SAVE_ITM_CONTRACT_MODEL
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		if (null != jnjImtContractModel)
		{
			try
			{
				modelService.saveAll(jnjImtContractModel);
				recordStatus = true;
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_SAVE_ITM_CONTRACT_MODEL
							+ Logging.HYPHEN + "Successfully saved Intermediate Contract Model with ECCContractNum: "
							+ jnjImtContractModel.getContractNoEcc());
				}
			}
			catch (final ModelSavingException exception)
			{
				LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_SAVE_ITM_CONTRACT_MODEL
						+ Logging.HYPHEN + "Error occured while saving Intermediate Contract Model with ECCContractNum: "
						+ jnjImtContractModel.getContractNoEcc() + exception);
				throw new BusinessException("Error occured while saving Intermediate Contract Model with ECCContractNum: "
						+ jnjImtContractModel.getContractNoEcc());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_SAVE_ITM_CONTRACT_MODEL
					+ Logging.HYPHEN + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}


	//onchange FilterBy field
	@Override
	public SearchPageData<JnjContractModel> getPagedContracts(final PageableData pageableData, final String searchByCriteria,
			final String searchParameter, String sortByCriteria, final String selectCriteria, String filterCriteria2)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - "
					+ Jnjb2bCoreConstants.Logging.CONTRACT_GET_PAGED_CONTRACTS + "-" + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - "
					+ Jnjb2bCoreConstants.Logging.CONTRACT_GET_PAGED_CONTRACTS + "-" + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (sortByCriteria == null)
		{
			sortByCriteria = Jnjb2bCoreConstants.Contract.SORT_BY_CONTRACT_NUMBER_DESC;
		}
		return jnjContractDao.getPagedContracts(pageableData, searchByCriteria, searchParameter, sortByCriteria, selectCriteria, filterCriteria2);
	}


	@Override
	public List<JnjContractModel> getRecenlyUsedContracts()
	{

		return jnjContractDao.getRecenlyUsedContracts();

	}

	@Override
	public List<JnjImtContractModel> getJnjImtContractModels(final String iDocNum, final RecordStatus recordStatus,
			final String eCccontractNum, final String fileName)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_GET_IMT_CONTRACTS_MODEL + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		List<JnjImtContractModel> jnjImtContractModelList = null;
		final JnjImtContractModel tempJnjImtContractModel = new JnjImtContractModel();

		String logMessage = null;

		if (null != recordStatus)
		{
			tempJnjImtContractModel.setRecordStatus(recordStatus);
			logMessage = " RecordStatus [" + recordStatus.toString() + "] ";
		}
		if (StringUtils.isNotEmpty(iDocNum))
		{
			tempJnjImtContractModel.setIdocNumber(iDocNum);
			logMessage = StringUtils.isEmpty(logMessage) ? " IDOC Number [" + iDocNum + "] " : logMessage.concat(", IDOC Number ["
					+ iDocNum + "] ");

		}
		if (StringUtils.isNotEmpty(eCccontractNum))
		{
			tempJnjImtContractModel.setContractNoEcc(eCccontractNum);
			logMessage = StringUtils.isEmpty(logMessage) ? " Contract Number [" + eCccontractNum + "] " : logMessage
					.concat(", Contract Number [" + eCccontractNum + "] ");
		}
		if (StringUtils.isNotEmpty(fileName))
		{
			tempJnjImtContractModel.setFileName(fileName);
			logMessage = StringUtils.isEmpty(logMessage) ? " File Name[" + fileName + "] " : logMessage.concat(", File Name ["
					+ fileName + "] ");
		}

		try
		{
			jnjImtContractModelList = flexibleSearchService.getModelsByExample(tempJnjImtContractModel);
			if (!jnjImtContractModelList.isEmpty() && LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_GET_IMT_CONTRACT_MODEL + Logging.HYPHEN
						+ "JnjImtContractModels with Properties" + logMessage + " found in Hybris. Returning Existing Models");
			}
			else if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_GET_IMT_CONTRACT_MODEL + Logging.HYPHEN
						+ "JnjImtContractModels with Properties" + logMessage + " not found in Hybris. Returning Empty List.");
			}
		}
		catch (ModelNotFoundException | IllegalArgumentException exception)
		{
			LOGGER.error(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_GET_IMT_CONTRACT_MODEL + Logging.HYPHEN
					+ "JnjImtContractModels with Properties" + logMessage + " not found in Hybris. Returning Null - " + exception);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_GET_IMT_CONTRACTS_MODEL + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return jnjImtContractModelList;

	}

	/* CP008-CH003 Changes Start */

	@Override
	public JnjUomDTO getContractDelUomMapping(final JnJProductModel jnjProductModel, final UnitModel unitModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getContractDelUomMapping()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjUomDTO contractDelUOM = null;
		final int numSUOM = StringUtils.isEmpty(jnjProductModel.getNumeratorSUOM()) ? 1 : Integer.parseInt(jnjProductModel
				.getNumeratorSUOM());


		if (null != unitModel && CollectionUtils.isNotEmpty(jnjProductModel.getUomDetails()))
		{
			for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
			{
				// Compare the Unit Model of the Jnj Uom Conversion Model with Unit Model of Load Translation Model.
				if (jnjUomConvModel.getUOM().equals(unitModel))
				{
					contractDelUOM = new JnjUomDTO();
					contractDelUOM.setUnitDimension(jnjUomConvModel.getUOM().getName());
					contractDelUOM.setSalesUnitsCount(numSUOM / jnjUomConvModel.getNumerator().intValue()
							/ jnjUomConvModel.getDenominator().intValue());
				}
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMapping()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
		return contractDelUOM;
	}

	/* CP008-CH003 Changes Ends */



	@Override
	public void getJnjImtContractModelsWithEntries(final RecordStatus recordStatus)
	{
		final String METHOD_GET_IMT_CONTRACT_MODELS_WITH_ENTRIES = "getJnjImtContractModelsWithEntries()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_GET_IMT_CONTRACT_MODELS_WITH_ENTRIES + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		if (null != recordStatus)
		{
			final List<JnjImtContractModel> jnjImtContractModelList = getJnjImtContractModelForRemove(recordStatus);
			LOGGER.debug("jnJIntProductModelList is being deleted for record status " + recordStatus + "from"
					+ jnjImtContractModelList);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_GET_IMT_CONTRACT_MODELS_WITH_ENTRIES + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	/**
	 * @param recordStatus
	 * @return List<JnjImtContractModel>
	 */
	@Override
	public List<JnjImtContractModel> getJnjImtContractModelForRemove(final RecordStatus recordStatus)
	{

		return jnjContractDao.getJnjImtContractModelForRemove(recordStatus);
	}


	@Override
	public boolean isContractEntryActive(final JnjContractEntryModel jnjContractEntryModel)
	{
		final String METHOD_IS_CONTRACT_ENTRY_ACTIVE = "isContractEntryActive()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_IS_CONTRACT_ENTRY_ACTIVE + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		boolean entryActive = false;
		final String lineStatus = jnjContractEntryModel.getStatus();
		if (null != lineStatus)
		{
			if (StringUtils.equalsIgnoreCase(lineStatus, LoadContract.LOAD_CONTRACT_STATUS_A)
					|| StringUtils.equalsIgnoreCase(lineStatus, LoadContract.LOAD_CONTRACT_STATUS_B))
			{
				entryActive = true;
			}
			else if (StringUtils.equalsIgnoreCase(lineStatus, LoadContract.LOAD_CONTRACT_STATUS_C))
			{
				entryActive = false;
			}
		}
		else
		{
			LOGGER.error(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_IS_CONTRACT_ENTRY_ACTIVE + Logging.HYPHEN
					+ "No Status Revieced at Line level for enrty with Product [" + jnjContractEntryModel.getProduct().getCode() + "]");
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_IS_CONTRACT_ENTRY_ACTIVE + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return entryActive;
	}

	@Override
	public SearchResult<JnjContractModel> getActiveContracts(final String status, final int batchSize, final int startIndex)
			throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + GET_ACTIVE_CONTRACTS + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final SearchResult<JnjContractModel> activeContractListResult = jnjContractDao.getAllContractWithStatus(status, batchSize,
				startIndex);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + GET_ACTIVE_CONTRACTS + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return activeContractListResult;
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
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final String contractId = cartService.getSessionCart().getContractNumber();
		boolean exists = true;
		if (StringUtils.isNotEmpty(contractId))
		{
			final JnjContractModel jnjContractModel = getContractDetailsById(contractId);
			final List<String> productsInContract = new ArrayList<String>();
			for (final JnjContractEntryModel jnjContractEntryModel : jnjContractModel.getJnjContractEntries())
			{
				if (!StringUtils.isEmpty(jnjContractEntryModel.getProduct().getCode()))
				{
					/* Start:: 34930: Catalog ID Improvement. */
					/* Adding Active Product Code to List */
					JnJProductModel jnJProductModel;
					try
					{
						jnJProductModel = jnjGTProductService.getActiveProduct(jnjContractEntryModel.getProduct());
						if (null != jnJProductModel)
						{
							productsInContract.add(jnJProductModel.getCode());
						}
						else
						{
							LOGGER.warn(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN
									+ Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST + Logging.HYPHEN
									+ "Unable to find Active Product for Product with Code ["
									+ jnjContractEntryModel.getProduct().getCode() + "]. ");
						}
					}
					catch (final BusinessException businessException)
					{
						LOGGER.warn(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN
								+ Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST + Logging.HYPHEN
								+ "Unable to find Active Product for Product with Code [" + jnjContractEntryModel.getProduct().getCode()
								+ "]. ", businessException);
					}
				}
				/* End:: 34930: Catalog ID Improvement. */
			}
			exists = productsInContract.containsAll(productCodes);

		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return exists;

	}

	@SuppressWarnings("unused")
	@Override
	public boolean isContractrValidForProducts(final List<String> productCodes)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		boolean validContract = true;
		final String contractId = cartService.getSessionCart().getContractNumber();
		if (StringUtils.isNotEmpty(contractId))
		{
			final JnjContractModel jnjContractModel = getContractDetailsById(contractId);
			final List<JnjContractEntryModel> jnjContractEntryModelList = jnjContractModel.getJnjContractEntries();
			final List<JnJProductModel> activeProductList = new ArrayList<JnJProductModel>();

			/* Fetching Active Products for incoming Product Code List */
			productCodeLoop: for (final String productCode : productCodes)
			{
				if (StringUtils.isNotEmpty(productCode))
				{
					JnJProductModel actuiveProduct;
					try
					{
						actuiveProduct = jnjGTProductService.getActiveProduct(productCode);
						if (null != actuiveProduct)
						{
							activeProductList.add(actuiveProduct);
						}
						else
						{

							LOGGER.warn(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN
									+ Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST + Logging.HYPHEN
									+ "Unable to find Active Product for Product with Code [" + productCode + "]. ");
						}
					}
					catch (final BusinessException businessException)
					{
						LOGGER.warn(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + Logging.HYPHEN
								+ Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST + Logging.HYPHEN
								+ "Unable to find Active Product for Product with Code [" + productCode + "]. ", businessException);

					}
				}
			}//End of productCodeLoop
			/* Iterating Active Products to Check in Contract Entries for any Inactivity */
			activeProductListLoop: for (final JnJProductModel jnJProductModel : activeProductList)
			{
				jnjContractEntryModelListLoop: for (final JnjContractEntryModel jnjContractEntryModel : jnjContractEntryModelList)
				{
					if (StringUtils
							.equalsIgnoreCase(jnjContractEntryModel.getProduct().getCatalogId(), jnJProductModel.getCatalogId()))
					{
						if (!isContractEntryActive(jnjContractEntryModel))
						{
							LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - "
									+ Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST + "-" + "Contract Entry For Prodcut ["
									+ jnjContractEntryModel.getProduct().getCode() + "] is not active in the Contract");
							validContract = false;
							break activeProductListLoop;
						}
						else
						{
							LOGGER.info(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - "
									+ Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST + "-" + "Contract Entry For Prodcut ["
									+ jnjContractEntryModel.getProduct().getCode() + "] is active in the Contract");
						}
					}
				}//End of jnjContractEntryModelListLoop
			}//End of activeProductListLoop
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS_SERVICE + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_PRODUCT_EXIST
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return validContract;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.contract.JnjContractService#getActiveContractsForEntry(java.lang.String, int, int)
	 */
	@Override
	public SearchResult<JnjContractModel> getActiveContractsForEntry(final String CONTRACT_STATUS_ACTIVE, final int batchSize,
			final int startIndex) throws BusinessException
	{
		final String METHOD_NAME = "getActiveContractsForEntry ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		final SearchResult<JnjContractModel> activeContractListResult = jnjContractDao.getAllContractForEntry(
				CONTRACT_STATUS_ACTIVE, batchSize, startIndex);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONTRACTS_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return activeContractListResult;
	}
}
