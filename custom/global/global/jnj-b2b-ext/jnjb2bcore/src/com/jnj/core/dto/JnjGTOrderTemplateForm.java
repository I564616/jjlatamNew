/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;



/**
 * 'Pojo' for Order History Search And Sort Criteria Form.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOrderTemplateForm extends JnjGTSearchSortForm
{
	/**
	 * Constant for default order status selection.
	 */
	protected static final String ALL_TEMPLATE_STATUS = "All";

	/**
	 * Default Sort Code Value.
	 */
	protected static final String DEFAULT_SORT_CODE = "SortTemplateNameAsc";

	/**
	 * Empty Array with default zero size.
	 */
	protected static final String[] EMPTY_ARRAY = new String[0];

	/**
	 * Specifies the Type of Template based on <code>JnjTemplateTypesEnum</code>.
	 */
	private String templateType;

	/**
	 * Specifies the Type of Template based on <code>TemplateStatus</code>.
	 */
	private String templateStatus;

	/**
	 * The channel through which order has been placed.
	 */
	private String channel;

	/**
	 * Status maintained at the line or Order entry level.
	 */
	private String lineStatus;

	/**
	 * Selected accounts to view Template for associated template.
	 */
	private String[] accounts;

	/**
	 * Indicates if the reset option has been seleced or not.
	 */
	private boolean resetSelection;

	/**
	 * Total Number of templates present for the specific unit.
	 */
	private Long totalTemplates;

	/**
	 * Value of drop down to select for group of (10,25,50) .
	 */
	private String showinGroups;

	/**
	 * @return the showinGroups
	 */
	public String getShowinGroups()
	{
		return showinGroups;
	}

	/**
	 * @param showinGroups
	 *           the showinGroups to set
	 */
	public void setShowinGroups(final String showinGroups)
	{
		this.showinGroups = showinGroups;
	}

	/**
	 * @return the totalTemplatesShown
	 */
	public Long getTotalTemplatesShown()
	{
		return totalTemplatesShown;
	}

	/**
	 * @param totalTemplatesShown
	 *           the totalTemplatesShown to set
	 */
	public void setTotalTemplatesShown(final Long totalTemplatesShown)
	{
		this.totalTemplatesShown = totalTemplatesShown;
	}

	/**
	 * Total Number of templates being shown on current page.
	 */
	private Long totalTemplatesShown;


	/**
	 * @return the totalTemplates
	 */
	public Long getTotalTemplates()
	{
		return totalTemplates;
	}

	/**
	 * @param totalTemplates
	 *           the totalTemplates to set
	 */
	public void setTotalTemplates(final Long totalTemplates)
	{
		this.totalTemplates = totalTemplates;
	}

	/**
	 * search by
	 */
	private String searchby;

	/**
	 * sort by
	 */
	private String sortby;


	/**
	 * @return the searchby
	 */
	public String getSearchby()
	{
		return searchby;
	}

	/**
	 * @param searchby
	 *           the searchby to set
	 */
	public void setSearchby(final String searchby)
	{
		this.searchby = searchby;
	}

	/**
	 * @return the sortby
	 */
	public String getSortby()
	{
		return (sortby == null) ? DEFAULT_SORT_CODE : this.sortby;
	}

	/**
	 * @param sortby
	 *           the sortby to set
	 */
	public void setSortby(final String sortby)
	{
		this.sortby = sortby;
	}

	public String getChannel()
	{
		return channel;
	}

	/**
	 * @return the templateType
	 */
	public String getTemplateType()
	{
		return templateType;
	}

	/**
	 * @param templateType
	 *           the templateType to set
	 */
	public void setTemplateType(final String templateType)
	{
		this.templateType = templateType;
	}

	/**
	 * @return the templateStatus
	 */
	public String getTemplateStatus()
	{
		return templateStatus;
	}

	/**
	 * @param templateStatus
	 *           the templateStatus to set
	 */
	public void setTemplateStatus(final String templateStatus)
	{
		this.templateStatus = templateStatus;
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

	@Override
	public String getSortCode()
	{
		return (super.getSortCode() == null) ? DEFAULT_SORT_CODE : super.getSortCode();
	}
}
