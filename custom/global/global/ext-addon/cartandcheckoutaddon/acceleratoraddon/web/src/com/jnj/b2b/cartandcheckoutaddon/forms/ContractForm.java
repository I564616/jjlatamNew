/**
 * 
 */
package com.jnj.b2b.cartandcheckoutaddon.forms;

import com.jnj.core.dto.JnjGTSearchSortForm;

/**
 * @author komal.sehgal This is a Pojo Class for Contracts
 * 
 */
public class ContractForm  extends JnjGTSearchSortForm
{

	private String distributorCustomer;
	private String eCCContractNum;
	private String cRMContractNum;
	private String tenderNum;
	private String startDate;
	private String endDate;
	private String contractOrderReason;
	private String status;
	private String active;
	private String documentType;
	private String indirectCustomer;
	private String totalAmount;
	private String balanceAmount;
	private String searchParameter;
	private String searchByCriteria;
	private String sortByCriteria;
	private int noOfRecords;
	private String loadNoOfRecords;
	private String totalNoOfRecords;
	private String selectCriteria;
	private String filterCriteria2;
	private boolean isNonContractProductInCart;
	private boolean isNonContractProductInSelectedList;
	
	private Integer multiContractCount;
	private Integer multiProductCount;


	/**
	 * @return the totalNoOfRecords
	 */
	public String getTotalNoOfRecords()
	{
		return totalNoOfRecords;
	}

	/**
	 * @param totalNoOfRecords
	 *           the totalNoOfRecords to set
	 */
	public void setTotalNoOfRecords(final String totalNoOfRecords)
	{
		this.totalNoOfRecords = totalNoOfRecords;
	}

	

	/**
	 * @return the distributorCustomer
	 */
	public String getDistributorCustomer()
	{
		return distributorCustomer;
	}

	/**
	 * @return the loadNoOfRecords
	 */

	/**
	 * @param distributorCustomer
	 *           the distributorCustomer to set
	 */
	public void setDistributorCustomer(final String distributorCustomer)
	{
		this.distributorCustomer = distributorCustomer;
	}

	/**
	 * @return the loadNoOfRecords
	 */
	public String getLoadNoOfRecords()
	{
		return loadNoOfRecords;
	}

	/**
	 * @param loadNoOfRecords
	 *           the loadNoOfRecords to set
	 */
	public void setLoadNoOfRecords(final String loadNoOfRecords)
	{
		this.loadNoOfRecords = loadNoOfRecords;
	}

	/**
	 * @return the eCCContractNum
	 */
	public String geteCCContractNum()
	{
		return eCCContractNum;
	}

	/**
	 * @param eCCContractNum
	 *           the eCCContractNum to set
	 */
	public void seteCCContractNum(final String eCCContractNum)
	{
		this.eCCContractNum = eCCContractNum;
	}

	/**
	 * @return the cRMContractNum
	 */
	public String getcRMContractNum()
	{
		return cRMContractNum;
	}

	/**
	 * @return the searchParameter
	 */
	public String getSearchParameter()
	{
		return searchParameter;
	}

	/**
	 * @param searchParameter
	 *           the searchParameter to set
	 */
	public void setSearchParameter(final String searchParameter)
	{
		this.searchParameter = searchParameter;
	}

	/**
	 * @return the searchByCriteria
	 */
	public String getSearchByCriteria()
	{
		return searchByCriteria;
	}

	/**
	 * @param searchByCriteria
	 *           the searchByCriteria to set
	 */
	public void setSearchByCriteria(final String searchByCriteria)
	{
		this.searchByCriteria = searchByCriteria;
	}

	/**
	 * @return the sortByCriteria
	 */
	public String getSortByCriteria()
	{
		return sortByCriteria;
	}

	/**
	 * @param sortByCriteria
	 *           the sortByCriteria to set
	 */
	public void setSortByCriteria(final String sortByCriteria)
	{
		this.sortByCriteria = sortByCriteria;
	}

	/**
	 * @return the noOfRecords
	 */
	public int getNoOfRecords()
	{
		return noOfRecords;
	}

	/**
	 * @param noOfRecords
	 *           the noOfRecords to set
	 */
	public void setNoOfRecords(final int noOfRecords)
	{
		this.noOfRecords = noOfRecords;
	}

	/**
	 * @return the selectCriteria
	 */
	public String getSelectCriteria()
	{
		return selectCriteria;
	}

	/**
	 * @param selectCriteria
	 *           the selectCriteria to set
	 */
	public void setSelectCriteria(final String selectCriteria)
	{
		this.selectCriteria = selectCriteria;
	}

	/**
	 * @param cRMContractNum
	 *           the cRMContractNum to set
	 */
	public void setcRMContractNum(final String cRMContractNum)
	{
		this.cRMContractNum = cRMContractNum;
	}

	/**
	 * @return the tenderNum
	 */
	public String getTenderNum()
	{
		return tenderNum;
	}

	/**
	 * @param tenderNum
	 *           the tenderNum to set
	 */
	public void setTenderNum(final String tenderNum)
	{
		this.tenderNum = tenderNum;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *           the startDate to set
	 */
	public void setStartDate(final String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 *           the endDate to set
	 */
	public void setEndDate(final String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return the contractOrderReason
	 */
	public String getContractOrderReason()
	{
		return contractOrderReason;
	}

	/**
	 * @param contractOrderReason
	 *           the contractOrderReason to set
	 */
	public void setContractOrderReason(final String contractOrderReason)
	{
		this.contractOrderReason = contractOrderReason;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the active
	 */
	public String getActive()
	{
		return active;
	}

	/**
	 * @param active
	 *           the active to set
	 */
	public void setActive(final String active)
	{
		this.active = active;
	}

	/**
	 * @return the documentType
	 */
	public String getDocumentType()
	{
		return documentType;
	}

	/**
	 * @param documentType
	 *           the documentType to set
	 */
	public void setDocumentType(final String documentType)
	{
		this.documentType = documentType;
	}

	/**
	 * @return the indirectCustomer
	 */
	public String getIndirectCustomer()
	{
		return indirectCustomer;
	}

	/**
	 * @param indirectCustomer
	 *           the indirectCustomer to set
	 */
	public void setIndirectCustomer(final String indirectCustomer)
	{
		this.indirectCustomer = indirectCustomer;
	}

	/**
	 * @return the totalAmount
	 */
	public String getTotalAmount()
	{
		return totalAmount;
	}

	/**
	 * @param totalAmount
	 *           the totalAmount to set
	 */
	public void setTotalAmount(final String totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the balanceAmount
	 */
	public String getBalanceAmount()
	{
		return balanceAmount;
	}

	/**
	 * @param balanceAmount
	 *           the balanceAmount to set
	 */
	public void setBalanceAmount(final String balanceAmount)
	{
		this.balanceAmount = balanceAmount;
	}

	/**
	 * @return the filterCriteria2
	 */
	public String getFilterCriteria2()
	{
		return filterCriteria2;
	}

	/**
	 * @param filterCriteria2 the filterCriteria2 to set
	 */
	public void setFilterCriteria2(String filterCriteria2)
	{
		this.filterCriteria2 = filterCriteria2;
	}

	/**
	 * @return
	 */
	public boolean isNonContractProductInSelectedList() {
		return isNonContractProductInSelectedList;
	}

	/**
	 * @param isNonContractProductInSelectedList
	 */
	public void setNonContractProductInSelectedList(boolean isNonContractProductInSelectedList) {
		this.isNonContractProductInSelectedList = isNonContractProductInSelectedList;
	}

	/**
	 * @return
	 */
	public boolean isNonContractProductInCart() {
		return isNonContractProductInCart;
	}

	/**
	 * @param isNonContractProductInCart
	 */
	public void setNonContractProductInCart(boolean isNonContractProductInCart) {
		this.isNonContractProductInCart = isNonContractProductInCart;
	}

	public Integer getMultiContractCount() {
		return multiContractCount;
	}

	public void setMultiContractCount(Integer multiContractCount) {
		this.multiContractCount = multiContractCount;
	}

	public Integer getMultiProductCount() {
		return multiProductCount;
	}

	public void setMultiProductCount(Integer multiProductCount) {
		this.multiProductCount = multiProductCount;
	}
	
	


}
