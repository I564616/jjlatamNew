/**
 * 
 */
package com.jnj.core.dao.contract;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.model.JnjImtContractModel;
import com.jnj.exceptions.BusinessException;


/**
 * The Interface JnjContractDao.
 * 
 * @author komal.sehgal
 */
public interface JnjContractDao
{

	/**
	 * Gets the contract details by id.
	 * 
	 * @param id
	 *           the id
	 * @return the contract details by id
	 */
	public JnjContractModel getContractDetailsById(String id);

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
	 * @return SearchPageData<JnjContractModel>
	 */
	public SearchPageData<JnjContractModel> getPagedContracts(PageableData pageableData, String searchByCriteria,
			String searchParameter, String sortByCriteria, String selectCriteria, String filterCriteria2);

	/**
	 * Gets the recenly used contracts.
	 * 
	 * @return List<JnjContractModel>
	 */
	public List<JnjContractModel> getRecenlyUsedContracts();

	/**
	 * Gets the contracts for b2 b unit.
	 * 
	 * @return the contracts for b2 b unit
	 */
	public List<JnjContractModel> getContractsForB2BUnit();

	/**
	 * This method is used to fetch the contract model for removal.
	 * 
	 * @param recordStatus
	 *           the record status
	 * @return List<JnjImtContractModel>
	 */
	public List<JnjImtContractModel> getJnjImtContractModelForRemove(RecordStatus recordStatus);

	/**
	 * Method gets all Contract with status active. Status can be 'Active' or 'Inactive'. Batch size is the amount of
	 * records you want to retrieve. Start index is the Index of your first record. It returns a Business exception if
	 * FlexiSearch Fails or no Model is found. It returns an empty collection when the Batch Ends.
	 * 
	 * @param status
	 *           the status
	 * @param batchSize
	 *           the batch size
	 * @param startIndex
	 *           the start index
	 * @return the all contract with status
	 * @throws BusinessException
	 *            the business exception
	 */
	public SearchResult<JnjContractModel> getAllContractWithStatus(final String status, final int batchSize, final int startIndex)
			throws BusinessException;

	/**
	 * Method gets all Contract with status active. Status can be 'Active' or 'Inactive'. Batch size is the amount of
	 * records you want to retrieve. Start index is the Index of your first record. It returns a Business exception if
	 * FlexiSearch Fails or no Model is found. It returns an empty collection when the Batch Ends.
	 * 
	 * @param CONTRACT_STATUS_ACTIVE
	 *           the status
	 * @param batchSize
	 *           the batch size
	 * @param startIndex
	 *           the start index
	 * @return the all contract with status
	 * @throws BusinessException
	 *            the business exception
	 */
	public SearchResult<JnjContractModel> getAllContractForEntry(String CONTRACT_STATUS_ACTIVE, int batchSize, int startIndex)
			throws BusinessException;


}
