package com.jnj.la.jnjlaservicepageaddon.forms;

/**
 * @author plahiri1
 *
 */
public class JnjLaudoForm {
	private String fromDate;
	private String toDate;
	private String sortCode;
	private String searchBy;
	private String searchTextJnjId;
	private String searchTextLoteNumber;
	private boolean moreResults;
	private int currentPageSize;
	private int currentPageNumber;
	private int loadMoreClickCounter;

	/**
	 * @return the loadMoreClickCounter
	 */
	public int getLoadMoreClickCounter() {
		return loadMoreClickCounter;
	}

	/**
	 * @param loadMoreClickCounter
	 *            the loadMoreClickCounter to set
	 */
	public void setLoadMoreClickCounter(final int loadMoreClickCounter) {
		this.loadMoreClickCounter = loadMoreClickCounter;
	}

	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(final String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * @param toDate
	 *            the toDate to set
	 */
	public void setToDate(final String toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the sortCode
	 */
	public String getSortCode() {
		return sortCode;
	}

	/**
	 * @param sortCode
	 *            the sortCode to set
	 */
	public void setSortCode(final String sortCode) {
		this.sortCode = sortCode;
	}

	/**
	 * @return the searchBy
	 */
	public String getSearchBy() {
		return searchBy;
	}

	/**
	 * @param searchBy
	 *            the searchBy to set
	 */
	public void setSearchBy(final String searchBy) {
		this.searchBy = searchBy;
	}

	/**
	 * @return the currentPageSize
	 */
	public int getCurrentPageSize() {
		return currentPageSize;
	}

	/**
	 * @param currentPageSize
	 *            the currentPageSize to set
	 */
	public void setCurrentPageSize(final int currentPageSize) {
		this.currentPageSize = currentPageSize;
	}

	/**
	 * @return the currentPageNumber
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	/**
	 * @param currentPageNumber
	 *            the currentPageNumber to set
	 */
	public void setCurrentPageNumber(final int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	/**
	 * @return the searchTextJnjId
	 */
	public String getSearchTextJnjId() {
		return searchTextJnjId;
	}

	/**
	 * @param searchTextJnjId
	 *            the searchTextJnjId to set
	 */
	public void setSearchTextJnjId(final String searchTextJnjId) {
		this.searchTextJnjId = searchTextJnjId;
	}

	/**
	 * @return the searchTextLoteNumber
	 */
	public String getSearchTextLoteNumber() {
		return searchTextLoteNumber;
	}

	/**
	 * @param searchTextLoteNumber
	 *            the searchTextLoteNumber to set
	 */
	public void setSearchTextLoteNumber(final String searchTextLoteNumber) {
		this.searchTextLoteNumber = searchTextLoteNumber;
	}

	/**
	 * @return the moreResults
	 */
	public boolean isMoreResults() {
		return moreResults;
	}

	/**
	 * @param moreResults
	 *            the moreResults to set
	 */
	public void setMoreResults(final boolean moreResults) {
		this.moreResults = moreResults;
	}
}
