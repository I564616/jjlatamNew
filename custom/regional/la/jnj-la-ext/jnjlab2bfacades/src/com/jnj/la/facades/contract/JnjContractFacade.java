/**
 *
 */
package com.jnj.la.facades.contract;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;

import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjContractData;


/**
 * The Interface JnjContractFacade.
 *
 * @author komal.sehgal
 */
public interface JnjContractFacade
{

	/**
	 * Gets the contracts.
	 *
	 * @return the contracts
	 */
	public List<JnjContractData> getContracts();

	/**
	 * Gets the contract details by id.
	 *
	 * @param eCCContractNum
	 *           the e cc contract num
	 * @param entryCount
	 *           the entry count
	 * @return the contract details by id
	 */
	public JnjContractData getContractDetailsById(String eCCContractNum, int entryCount);

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
	 * Gets the paged Contracts.
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
	 * @return the paged order templates
	 */
	public SearchPageData<JnjContractData> getPagedContracts(PageableData pageableData, String searchByCriteria,
			String searchParameter, String sortByCriteria, String selectCriteria);

	/**
	 * Gets the recenly used contracts.
	 *
	 * @return the recenly used contracts
	 */
	public List<JnjContractData> getRecenlyUsedContracts();

	/**
	 * This method is used to Check whether the Contract is of type "Bids & Tenders" or not.
	 *
	 * @param contracrId
	 *           the contracr id
	 * @return true, if is contract bid
	 */
	boolean isContractBid(String contracrId);

	/**
	 * This method checks whether the Products added to Cart have a valid Contract or Not.
	 *
	 * @param productCodes
	 *           the product codes
	 * @return true, if is contractr valid for products
	 */
	boolean isContractrValidForProducts(List<String> productCodes);

	/**
	 * Gets all the active contracts in Hybris.
	 *
	 * @return the all active contracts
	 * @throws BusinessException
	 *            the business exception
	 */
	public boolean checkAllActiveContracts() throws BusinessException;


	/**
	 * Gets all In active contracts in Hybris.
	 *
	 * @return the all active contracts
	 * @throws BusinessException
	 *            the business exception
	 */
	public boolean checkAllInActiveContracts() throws BusinessException;

	/**
	 * Gets All active contracts in Hybris for Entry.
	 *
	 * @return the all active contracts
	 */
	public boolean checkActiveContractsForEntry() throws BusinessException;
}
