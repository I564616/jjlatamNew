/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.account;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import com.jnj.la.core.dto.JnjSellOutReportData;



/**
 * This class converts the sell out reports model into list object and also invokes the service layer to update this
 * model.
 *
 * @author Accenture
 * @version 1.0
 */
public interface JnjSellOutReportsFacade
{
	/**
	 *
	 * This method converts the sell out reports model into a list object.
	 *
	 * @param sortflag
	 * @param pageableData
	 * @return List<SellOutReportData>
	 */
	public SearchPageData<JnjSellOutReportData> getSellOutReportData(String sortflag, PageableData pageableData);

	/**
	 *
	 * This method invokes the service layer to update the sell out reports model and save it to the database.
	 *
	 * @param sellOutReportData
	 * @return boolean
	 */
	public boolean updateUploadHistory(JnjSellOutReportData sellOutReportData);
}
