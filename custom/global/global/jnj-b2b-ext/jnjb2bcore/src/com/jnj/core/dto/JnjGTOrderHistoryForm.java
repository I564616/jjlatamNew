/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.List;

import com.jnj.core.enums.SelectedAccountType;



/**
 * 'Pojo' for Order History Search And Sort Criteria Form.
 * 
 *  @author Accenture
 * @version 1.0
 */
public class JnjGTOrderHistoryForm extends JnjGTSearchSortForm
{
	/**
	 * Constant for default order status selection.
	 */
	protected static final String ALL_ORDER_STATUS = "All";

	/**
	 * Default Sort Code Value.
	 */
	protected static final String DEFAULT_SORT_CODE = "Order Date - Newest to Oldest";

	/**
	 * Empty Array with default zero size.
	 */
	protected static final String[] EMPTY_ARRAY = new String[0];

	/**
	 * Specifies the Type of Order based on <code>JnjOrderTypesEnum</code>.
	 */
	private String orderType;

	/**
	 * Specifies the Type of Order based on <code>OrderStatus</code>.
	 */
	private String orderStatus;

	/**
	 * The channel through which order has been placed.
	 */
	private String channel;

	/**
	 * Status maintained at the line or Order entry level.
	 */
	private String lineStatus;

	/**
	 * Selected accounts to view Order History for associated orders.
	 */
	private String[] accounts;

	/**
	 * Indicates if the reset option has been seleced or not.
	 */
	private boolean resetSelection;

	/**
	 * Indicates if the request is based on search filter criteria or not.
	 */
	private boolean searchRequest;

	/**
	 * Provides whether MDD, CONS or both types of accounts have been selected.
	 */
	private SelectedAccountType selectedAccountType;
	private List<String> userDivisions;
	
	/**
	 * Indicates if the select All Account checked or not.
	 */
	private boolean selectAllAccount;

	/**
	 * Previously selected accounts to view Order History for associated orders.
	 */
	private String[] previouslySelectedAccounts;
	
	/**
	 * Specifies Franchise of the order entries
	 */
	private String franchise;
	
	private String surgeonId;
	

	/** changes for GTR -1312  **/
	private String totalNumberOfResults;
	@Override
	public String getTotalNumberOfResults() {
		return totalNumberOfResults;
	}
	@Override
	public void setTotalNumberOfResults(String totalNumberOfResults) {
		this.totalNumberOfResults = totalNumberOfResults;
	}

	public boolean isSelectAllAccount() {
		return selectAllAccount;
	}

	public void setSelectAllAccount(boolean selectAllAccount) {
		this.selectAllAccount = selectAllAccount;
	}

	public String[] getPreviouslySelectedAccounts() {
		return previouslySelectedAccounts;
	}

	public void setPreviouslySelectedAccounts(String[] previouslySelectedAccounts) {
		this.previouslySelectedAccounts = previouslySelectedAccounts;
	}

	public String getOrderType()
	{
		return orderType;
	}

	public void setOrderType(final String orderType)
	{
		this.orderType = orderType;
	}

	public String getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(final String orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(final String channel)
	{
		this.channel = channel;
	}

	public String getLineStatus()
	{
		return lineStatus;
	}

	public void setLineStatus(final String lineStatus)
	{

		this.lineStatus = lineStatus;
	}

	public String[] getAccounts()
	{
		return (accounts == null) ? EMPTY_ARRAY : accounts;
	}

	public void setAccounts(final String[] accounts)
	{
		this.accounts = accounts;
	}

	public boolean isResetSelection()
	{
		return resetSelection;
	}

	public void setResetSelection(final boolean resetSelection)
	{
		this.resetSelection = resetSelection;
	}

	public boolean isSearchRequest()
	{
		return searchRequest;
	}

	public void setSearchRequest(final boolean searchRequest)
	{
		this.searchRequest = searchRequest;
	}

	public SelectedAccountType getSelectedAccountType()
	{
		return selectedAccountType;
	}

	public void setSelectedAccountType(final SelectedAccountType selectedAccountType)
	{
		this.selectedAccountType = selectedAccountType;
	}

	@Override
	public String getSortCode()
	{
		return (super.getSortCode() == null) ? DEFAULT_SORT_CODE : super.getSortCode();
	}

	/**
	 * @return the userDivisions
	 */
	public List<String> getUserDivisions()
	{
		return userDivisions;
	}

	/**
	 * @param userDivisions
	 *           the userDivisions to set
	 */
	public void setUserDivisions(final List<String> userDivisions)
	{
		this.userDivisions = userDivisions;
	}
	
	/**
	 * @return the surgeonId
	 */
	public String getSurgeonId() {
		return surgeonId;
	}
	/**
	 * @param surgeonId the surgeonId to set
	 */
	public void setSurgeonId(String surgeonId) {
		this.surgeonId = surgeonId;
	}
	/**
	 * @return the franchise
	 */
	public String getFranchise() {
		return franchise;
	}
	/**
	 * @param franchise the franchise to set
	 */
	public void setFranchise(String franchise) {
		this.franchise = franchise;
	}
}
