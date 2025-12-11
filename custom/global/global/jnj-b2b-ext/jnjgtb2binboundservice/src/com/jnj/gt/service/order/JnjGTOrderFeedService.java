/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.service.order;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jnj.gt.model.JnjGTIntOrdHdrNoteModel;
import com.jnj.gt.model.JnjGTIntOrdLineHoldLocalModel;
import com.jnj.gt.model.JnjGTIntOrdLinePriceLocalModel;
import com.jnj.gt.model.JnjGTIntOrderLineModel;
import com.jnj.gt.model.JnjGTIntOrderLinePartModel;
import com.jnj.gt.model.JnjGTIntOrderLineTxtModel;
import com.jnj.gt.model.JnjGTIntOrderSchLineModel;
import com.jnj.gt.model.JnjGTIntProductLotMasterModel;


/**
 * This interface contains all the methods which are used to fetch order related intermediate records.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTOrderFeedService
{

	/**
	 * Gets the JnjGTIntOrdHdrNoteModel models on the basis of order number and source system id.
	 * 
	 * @param orderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @return the List<JnjGTIntOrdHdrNoteModel>
	 */
	public List<JnjGTIntOrdHdrNoteModel> getJnjGTIntOrdHdrNoteModel(final String orderNumber, String sourceSystemId);

	/**
	 * Gets the JnjGTIntOrderLineModel models on the basis of order number, source system id, item category and high
	 * Level Item Number.
	 * 
	 * @param orderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @param itemCategory
	 *           the item categories
	 * @param highLevelItemNumber
	 *           high Level Item Number
	 * @return List<JnjGTIntOrderLineModel>
	 */
	public List<JnjGTIntOrderLineModel> getJnjGTIntOrderLineModel(final String orderNumber, String sourceSystemId,
			final List<String> itemCategory, String highLevelItemNumber);

	/**
	 * Gets the JnjGTIntOrderSchLineModel models on the basis of order number, source system id and order line number.
	 * 
	 * @param orderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @param orderLineNumber
	 *           the order line number
	 * @return List<JnjGTIntOrderSchLineModel>
	 */
	public List<JnjGTIntOrderSchLineModel> getJnjGTIntOrderSchLineModel(final String orderNumber, String sourceSystemId,
			String orderLineNumber);

	/**
	 * Gets the JnjGTIntOrderLineTxtModel models on the basis of order number and source system id.
	 * 
	 * @param orderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @return List<JnjGTIntOrderLineTxtModel>
	 */
	public List<JnjGTIntOrderLineTxtModel> getJnjGTIntOrderLineTxtModel(final String orderNumber, String sourceSystemId);

	/**
	 * Gets the JnjGTIntOrderLinePartModel models on the basis of order number and source system id.
	 * 
	 * @param orderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @return List<JnjGTIntOrderLinePartModel>
	 */
	public List<JnjGTIntOrderLinePartModel> getJnjGTIntOrderLinePartModel(final String orderNumber, String sourceSystemId);

	/**
	 * Gets JnjGTIntOrderSchLineModel models on the basis of order number, source system id and order line number.
	 * 
	 * @param orderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @param orderLineNumber
	 *           the order line number
	 * @return List<JnjGTIntOrdLineHoldLocalModel>
	 */
	public List<JnjGTIntOrdLineHoldLocalModel> getJnjGTIntOrdLineHoldLocalModel(final String orderNumber, String sourceSystemId,
			String orderLineNumber,Date recordTimeStamp);

	/**
	 * Gets JnjGTIntOrderSchLineModel models on the basis of order number, source system id and order line number.
	 * 
	 * @param orderNumber
	 *           the order number
	 * @param sourceSystemId
	 *           the source system id
	 * @param orderLineNumber
	 *           the order line number
	 * @return List<JnjGTIntOrdLinePriceLocalModel>
	 */
	public List<JnjGTIntOrdLinePriceLocalModel> getJnjGTIntOrdLinePriceLocalModel(final String orderNumber, String sourceSystemId,
			String orderLineNumber);

	/**
	 * Gets Price on the basis of order number, source system id and price condition type.
	 * 
	 * @param sapOrderNumber
	 *           the order number
	 * @param sapOrderLineNumber
	 *           the sap order line number
	 * @param sourceSystemId
	 *           the source system id
	 * @param priceConditionType
	 *           the price condition type
	 * @return Double
	 */
	public Double getPriceJnjGTIntOrdLineHoldLocal(final String sapOrderNumber, String sapOrderLineNumber,
			final String sourceSystemId, final String[] priceConditionType);

	/**
	 * Gets JnjGTIntProductLotMasterModel model on the basis of lot number, source system id and product Code.
	 * 
	 * @param lotNumber
	 *           the lot number
	 * @param sourceSystemId
	 *           the source system id
	 * @param productSkuCode
	 *           the product code
	 * @return List<JnjGTIntOrdLinePriceLocalModel>
	 */
	public JnjGTIntProductLotMasterModel getJnjGTIntProductLotMasterModel(final String lotNumber, final String sourceSystemId,
			final String productSkuCode);

	/**
	 * Gets the base site model using source system id.
	 * 
	 * @param sourceSysId
	 *           the source sys id
	 * @return the base site model using source sys id
	 */
	public BaseSiteModel getBaseSiteModelUsingSourceSysId(final String sourceSysId);

	/**
	 * Gets the order model using sap ord no and base site.
	 * 
	 * @param sapOrderNumber
	 *           the sap order number
	 * @param baseSiteModel
	 *           the base site model
	 * @return the order model using sap ord no and base site
	 */
	public OrderModel getOrderModelUsingSapOrdNoAndBaseSite(final String sapOrderNumber, final BaseSiteModel baseSiteModel);

	/**
	 * Sends email notification for Order Status Inbound files validation report when data lines present does not match
	 * with the mentioed records count.
	 * 
	 * @param fileNames
	 * @param fileAndLineCounts
	 */
	public void sendOrderStatusFileValidationEmail(List<String> fileNames, final Map<String, String> fileAndLineCounts);

	/**
	 * Gets currency iso code based on the SAP Order number and Source System Id.
	 * 
	 * @param sapOrderNumber
	 * @param sourceSysid
	 * @return String
	 */
	public String getCurrencyFromOrdLinePriceLocal(final String sapOrderNumber, final String sourceSysid);

	/**
	 * Gets the order entry status by using first tan and second tan order line status value for TAN-TAN scenario for
	 * Consumer.
	 * 
	 * @param firstTanOrderLineStatus
	 *           the first tan order line status
	 * @param secondTanOrderLineStatus
	 *           the second tan order line status
	 * @return OrderEntryStatus
	 */
	public OrderEntryStatus getTanOrderLineStatus(final String firstTanOrderLineStatus, final String secondTanOrderLineStatus);
}
