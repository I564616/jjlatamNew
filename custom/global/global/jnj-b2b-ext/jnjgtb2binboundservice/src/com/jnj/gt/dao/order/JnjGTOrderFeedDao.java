/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.dao.order;

import java.util.List;

import com.jnj.gt.model.JnjGTIntOrdLinePriceLocalModel;
import com.jnj.gt.model.JnjGTIntOrderLineModel;
import com.jnj.gt.model.JnjGTIntOrderSchLineModel;



/**
 * The JnjOrderFeedDao interface contains all those methods which are dealing with order related intermediate model and
 * it has declaration of all the methods which are defined in the JnjOrderFeedDaoImpl class.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTOrderFeedDao
{

	/**
	 * Gets JnjGTIntOrdLinePriceLocalModel's on the basis of order number, source system id and price condition type.
	 * 
	 * @param sapOrderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @param sapOrderLineNumber
	 *           the sap order line number
	 * @param priceConditionType
	 *           the price condition type
	 * @return List<JnjGTIntOrdLinePriceLocalModel>
	 */
	public List<JnjGTIntOrdLinePriceLocalModel> getPriceJnjGTIntOrdLineHoldLocal(final String sapOrderNumber,
			String sapOrderLineNumber, final String sourceSystemId, final String[] priceConditionType);

	/**
	 * Gets JnjGTIntOrderLineModel's on the basis of order number, source system id, item category and high Level Item
	 * Number.
	 * 
	 * @param sapOrderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @param itemCategory
	 *           the item categories
	 * @param highLevelItemNumber
	 *           high Level Item Number
	 * @return List<JnjGTIntOrderLineModel>
	 */
	public List<JnjGTIntOrderLineModel> getJnjGTIntOrderLineModel(final String sapOrderNumber, final String sourceSystemId,
			final List<String> itemCategory, String highLevelItemNumber);

	/**
	 * Fetches <code>JnjGTIntOrderSchLineModel</code> based on the sap order number, sap order line number, and source
	 * system id.
	 * 
	 * @param orderNumber
	 * @param sourceSystemId
	 * @param orderLineNumber
	 * @return List<JnjGTIntOrderSchLineModel>
	 */
	public List<JnjGTIntOrderSchLineModel> getJnjGTIntOrderSchLineModel(final String orderNumber, final String sourceSystemId,
			final String orderLineNumber);

	/**
	 * Fetches first non-null currency value from <code>JnjGTIntOrdLinePriceLocalModel</code> based on the sap order
	 * number, and source system id. If not result not found, null is returned.
	 * 
	 * @param sapOrderNumber
	 * @param sourceSysid
	 * @return String
	 */
	public String getCurrencyFromOrdLinePriceLocal(final String sapOrderNumber, final String sourceSysid);
}
