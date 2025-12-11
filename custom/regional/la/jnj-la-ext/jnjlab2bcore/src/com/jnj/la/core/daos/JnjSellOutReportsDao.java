/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.daos;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import com.jnj.la.core.model.JnjSellOutReportModel;



/**
 * This class converts the sell out reports model into list object.
 *
 * @author Accenture
 * @version 1.0
 */
public interface JnjSellOutReportsDao
{
	/**
	 *
	 * This method converts the sell out reports model into a list object.
	 *
	 * @param sortflag
	 * @param pageableData
	 * @return List<SellOutReportData>
	 */
	public SearchPageData<JnjSellOutReportModel> getSellOutReportData(String sortflag, PageableData pageableData);

}
