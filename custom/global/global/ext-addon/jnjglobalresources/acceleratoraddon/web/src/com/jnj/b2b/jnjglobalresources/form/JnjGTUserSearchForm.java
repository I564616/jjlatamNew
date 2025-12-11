/**
 * 
 */
package com.jnj.b2b.jnjglobalresources.form;

/**
 * @author komal.sehgal
 * 
 */
public class JnjGTUserSearchForm
{

	private static final int DEFAULT_PAGE_SIZE = 100;
	private static final int DEFAULT_SHOW_MORE_COUNTER = 1;
	private String sector;
	private String lastName;
	private String firstName;
	private String accountNumber;
	private String accountName;
	private String status;
	private String role;
	private String phone;
	private String email;
	private String sortBy;
	private boolean showMore;
	private int showMoreCounter;
	private boolean searchFlagUserSearch;
	private String downloadType;


	public String getDownloadType() {
		return downloadType;
	}

	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}

	/**
	 * @return the role
	 */
	public String getRole()
	{
		return role;
	}

	/**
	 * @param role
	 *           the role to set
	 */
	public void setRole(final String role)
	{
		this.role = role;
	}

	/**
	 * Size of items to be displayed. Default = 10
	 */
	private int pageSize;



	/**
	 * @return the searchFlagUserSearch
	 */
	public boolean isSearchFlagUserSearch()
	{
		return searchFlagUserSearch;
	}

	/**
	 * @param searchFlagUserSearch
	 *           the searchFlagUserSearch to set
	 */
	public void setSearchFlagUserSearch(final boolean searchFlagUserSearch)
	{
		this.searchFlagUserSearch = searchFlagUserSearch;
	}

	/**
	 * @return the showMoreCounter
	 */
	public int getShowMoreCounter()
	{
		return (showMoreCounter == 0) ? DEFAULT_SHOW_MORE_COUNTER : showMoreCounter;
	}

	/**
	 * @param showMoreCounter
	 *           the showMoreCounter to set
	 */
	public void setShowMoreCounter(final int showMoreCounter)
	{
		this.showMoreCounter = showMoreCounter;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize()
	{
		return (pageSize == 0) ? DEFAULT_PAGE_SIZE : pageSize;
	}

	/**
	 * @param pageSize
	 *           the pageSize to set
	 */
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}



	/**
	 * @return the showMore
	 */
	public boolean isShowMore()
	{
		return showMore;
	}

	/**
	 * @param showMore
	 *           the showMore to set
	 */
	public void setShowMore(final boolean showMore)
	{
		this.showMore = showMore;
	}

	/**
	 * @return the sector
	 */
	public String getSector()
	{
		return sector;
	}

	/**
	 * @return the sortBy
	 */
	public String getSortBy()
	{
		return sortBy;
	}

	/**
	 * @param sortBy
	 *           the sortBy to set
	 */
	public void setSortBy(final String sortBy)
	{
		this.sortBy = sortBy;
	}

	/**
	 * @param sector
	 *           the sector to set
	 */
	public void setSector(final String sector)
	{
		this.sector = sector;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber()
	{
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *           the accountNumber to set
	 */
	public void setAccountNumber(final String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName()
	{
		return accountName;
	}

	/**
	 * @param accountName
	 *           the accountName to set
	 */
	public void setAccountName(final String accountName)
	{
		this.accountName = accountName;
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
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *           the phone to set
	 */
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}

}
