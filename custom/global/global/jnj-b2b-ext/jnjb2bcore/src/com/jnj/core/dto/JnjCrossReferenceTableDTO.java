/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15 Oct, 2013 9:21:29 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.core.dto;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


public class JnjCrossReferenceTableDTO implements java.io.Serializable
{

	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.clientProductID</code> property defined at extension
	 * <code>jnjb2bstorefront</code>.
	 */
	private String clientProductID;
	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.jnjProductName</code> property defined at extension
	 * <code>jnjb2bstorefront</code>.
	 */
	private String jnjProductName;
	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.jnjProductID</code> property defined at extension
	 * <code>jnjb2bstorefront</code>.
	 */
	private String jnjProductID;
	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.clientName</code> property defined at extension
	 * <code>jnjb2bstorefront</code>.
	 */
	private String clientName;
	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.ID</code> property defined at extension
	 * <code>jnjb2bstorefront</code>.
	 */
	private String ID;
	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.clientID</code> property defined at extension
	 * <code>jnjb2bstorefront</code>.
	 */
	private String clientID;
	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.ProductName</code> property defined at extension
	 * <code>jnjb2bstorefront</code>.
	 */
	private String ProductName;
	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.defaultProductID</code> property defined at
	 * extension <code>jnjb2bstorefront</code>.
	 */
	private String defaultProductID;
	/**
	 * <i>Generated property</i> for <code>JnjCrossReferenceTableDTO.TestMap</code> property defined at extension
	 * <code>jnjb2bstorefront</code>.
	 */
	private SearchPageData<JnjCrossReferenceTableDTO> resultList;

	/**
	 * @return the resultList
	 */
	public SearchPageData<JnjCrossReferenceTableDTO> getResultList()
	{
		return resultList;
	}


	/**
	 * @param list
	 *           the resultList to set
	 */
	public void setResultList(final SearchPageData<JnjCrossReferenceTableDTO> resultList)
	{
		this.resultList = resultList;
	}


	public void setClientProductID(final String clientProductID)
	{
		this.clientProductID = clientProductID;
	}


	public String getClientProductID()
	{
		return clientProductID;
	}


	public void setJnjProductName(final String jnjProductName)
	{
		this.jnjProductName = jnjProductName;
	}


	public String getJnjProductName()
	{
		return jnjProductName;
	}


	public void setJnjProductID(final String jnjProductID)
	{
		this.jnjProductID = jnjProductID;
	}


	public String getJnjProductID()
	{
		return jnjProductID;
	}


	public void setClientName(final String clientName)
	{
		this.clientName = clientName;
	}


	public String getClientName()
	{
		return clientName;
	}


	public void setID(final String ID)
	{
		this.ID = ID;
	}


	public String getID()
	{
		return ID;
	}


	public void setClientID(final String clientID)
	{
		this.clientID = clientID;
	}


	public String getClientID()
	{
		return clientID;
	}


	public void setProductName(final String ProductName)
	{
		this.ProductName = ProductName;
	}


	public String getProductName()
	{
		return ProductName;
	}


	public void setDefaultProductID(final String defaultProductID)
	{
		this.defaultProductID = defaultProductID;
	}


	public String getDefaultProductID()
	{
		return defaultProductID;
	}





}