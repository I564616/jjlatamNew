/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;





/**
 * 'Pojo' for Search And Sort Criteria Options.
 *
 *  @author Accenture
 *  @version 1.0
 */
public class JnjGTSearchSortForm
{
	/**
	 * Default page size value.
	 */
	private static final int DEFAULT_PAGE_SIZE = 100;

	/**
	 * Default value for the download Type
	 */
	protected static final String DEFAULT_DOWNLOAD_TYPE = "NONE";

	/**
	 * Specifies the Start Date from the selection.
	 */
	private String startDate;

	/**
	 * Specifies the End Date from the selection.
	 */
	private String endDate;

	/**
	 * Size of items to be displayed. Default = 10
	 */
	private int pageSize;

	/**
	 * Sort Code selected to filter with.
	 */
	private String sortCode;


	private int fullCount;
	/**
	 * Document type selected for download.
	 */
	private String downloadType;

	/**
	 * Search criteria type value.
	 */
	private String searchBy;

	/**
	 * Search argument value.
	 */
	private String searchText;

	/**
	 * Indicator for load more items.
	 */
	private boolean showMore;

	/**
	 * Counter for number of times 'Show More' has been clicked.
	 */
	private int showMoreCounter;

	private String totalNumberOfResults;
	private int pageNumber;
	private int lastPageNumber;
	private String displayResults;

	public int getFullCount() {
		return fullCount;
	}

	public void setFullCount(int fullCount) {
		this.fullCount = fullCount;
	}



	public void setTotalNumberOfResults(String totalNumberOfResults) {
		this.totalNumberOfResults = totalNumberOfResults;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(final String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(final String endDate)
	{
		this.endDate = endDate;
	}

	public int getPageSize(final String currentSite)
	{
		return 50;
	}

	public int getPageSize()
	{
		return (pageSize == 0) ? DEFAULT_PAGE_SIZE : pageSize;
	}

	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}

	public String getSortCode()
	{
		return sortCode;
	}


	public void setSortCode(final String sortCode)
	{
		this.sortCode = sortCode;
	}


	public String getDownloadType()
	{
		return (downloadType == null) ? DEFAULT_DOWNLOAD_TYPE : downloadType;
	}


	public void setDownloadType(final String downloadType)
	{
		this.downloadType = downloadType;
	}


	public String getSearchBy()
	{
		return searchBy;
	}


	public void setSearchBy(final String searchBy)
	{
		this.searchBy = searchBy;
	}


	public String getSearchText()
	{
		return searchText;
	}

	public void setSearchText(final String searchText)
	{
		this.searchText = searchText;
	}

	public boolean isShowMore()
	{
		return showMore;
	}

	public void setShowMore(final boolean showMore)
	{
		this.showMore = showMore;
	}

	public int getShowMoreCounter()
	{
		return (showMoreCounter == 0) ? 1 : showMoreCounter;
	}

	public void setShowMoreCounter(final int showMoreCounter)
	{
		this.showMoreCounter = showMoreCounter;
	}

	public String getTotalNumberOfResults() {
		return totalNumberOfResults;
	}

	public void setTotalNumberOfResults1(String totalNumberOfResults) {
		this.totalNumberOfResults = totalNumberOfResults;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getLastPageNumber() {
		return lastPageNumber;
	}

	public void setLastPageNumber(int lastPageNumber) {
		this.lastPageNumber = lastPageNumber;
	}

	public String getDisplayResults() {
		return displayResults;
	}

	public void setDisplayResults(String displayResults) {
		this.displayResults = displayResults;
	}



}
