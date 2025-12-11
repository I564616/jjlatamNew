/**
 * 
 */
package com.jnj.b2b.jnjglobalreports.forms;



/**
 * @author ujjwal.negi
 * 
 */
public class JnjGTCutReportForm
{
	private String accountIdsList;
	private String oldAccountIds;
	private String poNumber;
	private String postartDate;
	private String poendDate;
	private String productCode;
	private String shipTo;
	private String accountsSelectedValue;
	private String orderBy;
	private boolean allAccountsFlag;

	private String downloadType;
	private String accountIds;
	public String getDownloadType() {
		return downloadType;
	}

	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}

	public String getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(String accountIds) {
		this.accountIds = accountIds;
	}

	/**
	 * @return the oldAccountIds
	 */
	public String getOldAccountIds()
	{
		return oldAccountIds;
	}

	/**
	 * @param oldAccountIds
	 *           the oldAccountIds to set
	 */
	public void setOldAccountIds(final String oldAccountIds)
	{
		this.oldAccountIds = oldAccountIds;
	}
	
	/**
	 * @return the allAccountsFlag
	 */
	public boolean isAllAccountsFlag()
	{
		return allAccountsFlag;
	}

	/**
	 * @param allAccountsFlag
	 *           the allAccountsFlag to set
	 */
	public void setAllAccountsFlag(final boolean allAccountsFlag)
	{
		this.allAccountsFlag = allAccountsFlag;
	}
	
	/**
	 * @return the accountsSelectedValue
	 */
	public String getAccountsSelectedValue()
	{
		return accountsSelectedValue;
	}

	/**
	 * @param accountsSelectedValue
	 *           the accountsSelectedValue to set
	 */
	public void setAccountsSelectedValue(final String accountsSelectedValue)
	{
		this.accountsSelectedValue = accountsSelectedValue;
	}

	/**
	 * @return the poNumber
	 */
	public String getPoNumber()
	{
		return poNumber;
	}

	/**
	 * @param poNumber
	 *           the poNumber to set
	 */
	public void setPoNumber(final String poNumber)
	{
		this.poNumber = poNumber;
	}

	/**
	 * @return the postartDate
	 */
	public String getPostartDate()
	{
		return postartDate;
	}

	/**
	 * @param postartDate
	 *           the postartDate to set
	 */
	public void setPostartDate(final String postartDate)
	{
		this.postartDate = postartDate;
	}

	/**
	 * @return the poendDate
	 */
	public String getPoendDate()
	{
		return poendDate;
	}

	/**
	 * @param poendDate
	 *           the poendDate to set
	 */
	public void setPoendDate(final String poendDate)
	{
		this.poendDate = poendDate;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return productCode;
	}

	/**
	 * @param productCode
	 *           the productCode to set
	 */
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	/**
	 * @return the shipTo
	 */
	public String getShipTo()
	{
		return shipTo;
	}

	/**
	 * @param shipTo
	 *           the shipTo to set
	 */
	public void setShipTo(final String shipTo)
	{
		this.shipTo = shipTo;
	}

	/**
	 * @return the accountIdsList
	 */
	public String getAccountIdsList()
	{
		return accountIdsList;
	}

	/**
	 * @param accountIdsList
	 *           the accountIdsList to set
	 */
	public void setAccountIdsList(final String accountIdsList)
	{
		this.accountIdsList = accountIdsList;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy()
	{
		return orderBy;
	}

	/**
	 * @param orderBy
	 *           the orderBy to set
	 */
	public void setOrderBy(final String orderBy)
	{
		this.orderBy = orderBy;
	}

}
