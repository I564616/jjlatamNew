/**
 *
 */
package com.jnj.core.services.contract;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.List;

import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.model.JnjImtContractModel;
import com.jnj.exceptions.BusinessException;


/**
 * The Interface JnjContractService.
 * 
 * @author komal.sehgal
 */
public interface JnjContractService
{

	/**
	 * Gets the contracts.
	 * 
	 * @return the contracts
	 */
	public Collection<JnjContractModel> getContracts();

	/**
	 * Gets the contract details by id.
	 * 
	 * @param eCCContractNum
	 *           the e cc contract num
	 * @return the contract details by id
	 */
	public JnjContractModel getContractDetailsById(String eCCContractNum);

	/**
	 * Save contract.
	 * 
	 * @param jnjContractModel
	 *           the jnj contract model
	 * @return true, if successful
	 * @throws BusinessException
	 *            the business exception
	 */
	public boolean saveContract(JnjContractModel jnjContractModel) throws BusinessException;

	/**
	 * Gets the paged contracts.
	 * 
	 * @param pageableData
	 *           the pageable data
	 * @param searchByCriteria
	 *           the search by criteria
	 * @param searchParameter
	 *           the search parameter
	 * @param sortByCriteria
	 *           the sort by criteria
	 * @param selectCriteria
	 *           the select criteria
	 * @return the paged contracts
	 */
	public SearchPageData<JnjContractModel> getPagedContracts(PageableData pageableData, String searchByCriteria,
			String searchParameter, String sortByCriteria, String selectCriteria, String filterCriteria2);

	/**
	 * Gets the recenly used contracts.
	 * 
	 * @return the recenly used contracts
	 */
	public List<JnjContractModel> getRecenlyUsedContracts();

	/**
	 * Saves the intermediate model for Contract.
	 * 
	 * @param jnjImtContractModel
	 *           the jnj imt contract model
	 * @return true, if successful
	 * @throws BusinessException
	 *            the business exception
	 */
	boolean saveItmContractModel(JnjImtContractModel jnjImtContractModel) throws BusinessException;

	/**
	 * This method created the Temporary JnjImtContractModel with Input Parameters which will be used to Fetch the
	 * JnjImtContractModels from DAO.
	 * 
	 * @param iDocNum
	 *           the i doc num
	 * @param recordStatus
	 *           the record status
	 * @param eCccontractNum
	 *           the e cccontract num
	 * @param fileName
	 *           the file name
	 * @return the jnj imt contract models
	 */
	List<JnjImtContractModel> getJnjImtContractModels(final String iDocNum, final RecordStatus recordStatus,
			final String eCccontractNum, final String fileName);

	/**
	 * Gets the Contract Delivery Uom mapping by using Jnj Product Model.
	 * 
	 * @param jnjProductModel
	 *           the jnj product model
	 * @param unitModel
	 *           the unit model
	 * @return the JnjUomDTO object
	 */

	/* CP008-CH003 Changes Start */

	public JnjUomDTO getContractDelUomMapping(JnJProductModel jnjProductModel, UnitModel unitModel);

	/**
	 * Checks if is contract bid.
	 * 
	 * @param contracrId
	 *           the contracr id
	 * @return true, if is contract bid
	 */
	public boolean isContractBid(final String contracrId);

	/**
	 * This method fetched the Collection of JnjImtContractModel and it's JnjImtContractEntryList based on RecordStatus.
	 * 
	 * @param recordStatus
	 *           the record status
	 * @return the jnj imt contract models with entries
	 * @throws BusinessException
	 *            the business exception
	 */
	void getJnjImtContractModelsWithEntries(RecordStatus recordStatus) throws BusinessException;

	/**
	 * Sets the Activation Flag based on Status for Contract Entry.
	 * 
	 * @param JnjContractEntryModel
	 *           the jnj contract entry model
	 * @return true, if is contract entry active
	 */
	boolean isContractEntryActive(JnjContractEntryModel JnjContractEntryModel);

	/**
	 * This method is used to fetch List of JnjImtContractModel for Remove on RecordStatus.
	 * 
	 * @param recordStatus
	 *           the record status
	 * @return List<JnjImtContractModel>
	 */
	List<JnjImtContractModel> getJnjImtContractModelForRemove(RecordStatus recordStatus);

	/**
	 * Method gets all Contract with status active. Status can be 'Active' or 'Inactive'. Batch size is the amount of
	 * records you want to retrieve. Start index is the Index of your first record.
	 * 
	 * @param status
	 *           the status
	 * @param batchSize
	 *           the batch size
	 * @param startIndex
	 *           the start index
	 * @return the active contracts
	 * @throws BusinessException
	 *            the business exception
	 */
	public SearchResult<JnjContractModel> getActiveContracts(final String status, final int batchSize, final int startIndex)
			throws BusinessException;

	/**
	 * This method checks if given product codes exists in selected contract.
	 * 
	 * 
	 * 
	 * @param productCodes
	 *           the product codes
	 * @return true, if is products exists in contract or any contract not selected
	 */
	boolean isProductsExistsInContract(final List<String> productCodes);


	/**
	 * This method checks whether the Products added to Cart have a valid Contract or Not.
	 * 
	 * @param productCodes
	 *           the product codes
	 * @return true, if is contractr valid for products
	 */
	boolean isContractrValidForProducts(List<String> productCodes);

	/**
	 * Method gets all Contract with status active. Status can be 'Active' or 'Inactive'. Batch size is the amount of
	 * records you want to retrieve. Start index is the Index of your first record.
	 * 
	 * @param CONTRACT_STATUS_ACTIVE
	 *           the status
	 * @param batchSize
	 *           the batch size
	 * @param startIndex
	 *           the start index
	 * @return the active contracts
	 * @throws BusinessException
	 *            the business exception
	 */
	public SearchResult<JnjContractModel> getActiveContractsForEntry(String CONTRACT_STATUS_ACTIVE, int batchSize, int startIndex)
			throws BusinessException;

}
