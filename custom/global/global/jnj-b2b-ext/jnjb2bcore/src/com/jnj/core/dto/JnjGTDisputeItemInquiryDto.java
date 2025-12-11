/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.List;
import java.util.Map;


/**
 * Class for holding Dispute Order Inquiry Form data captured from Dispute Order inquiry page.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTDisputeItemInquiryDto extends JnjGTDisputeInquiryDto
{
	
	/**
	 * Map holding product code and associated quantity as key value pairs.
	 */
	private Map<String, List<String>> productsAndQuantityPrice;
	
	/*Start AAOL 5074*/
	private List<String> productCode;
	public List<String> getProductCode() {
		return productCode;
	}

	public void setProductCode(List<String> productCode) {
		this.productCode = productCode;
	}

	public List<String> getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(List<String> contactNumber) {
		this.contactNumber = contactNumber;
	}

	public List<String> getQuantity() {
		return quantity;
	}

	public void setQuantity(List<String> quantity) {
		this.quantity = quantity;
	}

	private List<String> contactNumber;
	private List<String> quantity;
	/*End AAOL 5074*/
	/**
	 * Map holding product code and associated quantity as key value pairs.
	 */
	private Map<String, List<String>> productsAndQuantityRecieved;
	/**
	 * Map holding product code and associated quantity as key value pairs.
	 */
	private Map<String, List<String>> productsAndQuantityReceivedOver;
	private Map<String, String> productsAndQuantityOrdered;
	private Map<String, String> productsAndReplacement;
	private Map<String, String> ProductsAndInvoiceNumberShort;

	private Map<String, String> productsAndQuantityOrderedOver;
	private Map<String, String> productsAndLotNumbers;
	private Map<String, String> ProductsAndInvoiceNumberOver;
	/**
	 * Map holding product code and associated quantity as key value pairs.
	 */
	private Map<String, String> productsAndDisputedPrice;

	/**
	 * Map holding product code and associated quantity as key value pairs.
	 */
	private Map<String, String> productsAndCorrectPrice;


	/**
	 * Map holding product code and associated quantity as key value pairs.
	 */
	private Map<String, String> productsAndContractNumber;


	/**
	 * Map holding product code and associated quantity as key value pairs.
	 */
	private Map<String, String> productsAndInvoiceNumber;

	/**
	 * Total dispute amount captured.
	 */
	private String totalDisputedAmount;
	
	private String disputeInvoiceNumber;
	
	public Map<String, List<String>> getProductsAndQuantityPrice() {
		return productsAndQuantityPrice;
	}

	public void setProductsAndQuantityPrice(Map<String, List<String>> productsAndQuantityPrice) {
		this.productsAndQuantityPrice = productsAndQuantityPrice;
	}

	public Map<String, List<String>> getProductsAndQuantityRecieved() {
		return productsAndQuantityRecieved;
	}

	public void setProductsAndQuantityRecieved(Map<String, List<String>> productsAndQuantityRecieved) {
		this.productsAndQuantityRecieved = productsAndQuantityRecieved;
	}

	public Map<String, List<String>> getProductsAndQuantityReceivedOver() {
		return productsAndQuantityReceivedOver;
	}

	public void setProductsAndQuantityReceivedOver(Map<String, List<String>> productsAndQuantityReceivedOver) {
		this.productsAndQuantityReceivedOver = productsAndQuantityReceivedOver;
	}

	public Map<String, String> getProductsAndQuantityOrdered() {
		return productsAndQuantityOrdered;
	}

	public void setProductsAndQuantityOrdered(Map<String, String> productsAndQuantityOrdered) {
		this.productsAndQuantityOrdered = productsAndQuantityOrdered;
	}

	public Map<String, String> getProductsAndReplacement() {
		return productsAndReplacement;
	}

	public void setProductsAndReplacement(Map<String, String> productsAndReplacement) {
		this.productsAndReplacement = productsAndReplacement;
	}

	public Map<String, String> getProductsAndInvoiceNumberShort() {
		return ProductsAndInvoiceNumberShort;
	}

	public void setProductsAndInvoiceNumberShort(Map<String, String> productsAndInvoiceNumberShort) {
		ProductsAndInvoiceNumberShort = productsAndInvoiceNumberShort;
	}

	public Map<String, String> getProductsAndQuantityOrderedOver() {
		return productsAndQuantityOrderedOver;
	}

	public void setProductsAndQuantityOrderedOver(Map<String, String> productsAndQuantityOrderedOver) {
		this.productsAndQuantityOrderedOver = productsAndQuantityOrderedOver;
	}

	public Map<String, String> getProductsAndLotNumbers() {
		return productsAndLotNumbers;
	}

	public void setProductsAndLotNumbers(Map<String, String> productsAndLotNumbers) {
		this.productsAndLotNumbers = productsAndLotNumbers;
	}

	public Map<String, String> getProductsAndInvoiceNumberOver() {
		return ProductsAndInvoiceNumberOver;
	}

	public void setProductsAndInvoiceNumberOver(Map<String, String> productsAndInvoiceNumberOver) {
		ProductsAndInvoiceNumberOver = productsAndInvoiceNumberOver;
	}

	public Map<String, String> getProductsAndDisputedPrice() {
		return productsAndDisputedPrice;
	}

	public void setProductsAndDisputedPrice(Map<String, String> productsAndDisputedPrice) {
		this.productsAndDisputedPrice = productsAndDisputedPrice;
	}

	public Map<String, String> getProductsAndCorrectPrice() {
		return productsAndCorrectPrice;
	}

	public void setProductsAndCorrectPrice(Map<String, String> productsAndCorrectPrice) {
		this.productsAndCorrectPrice = productsAndCorrectPrice;
	}

	public Map<String, String> getProductsAndContractNumber() {
		return productsAndContractNumber;
	}

	public void setProductsAndContractNumber(Map<String, String> productsAndContractNumber) {
		this.productsAndContractNumber = productsAndContractNumber;
	}

	public Map<String, String> getProductsAndInvoiceNumber() {
		return productsAndInvoiceNumber;
	}

	public void setProductsAndInvoiceNumber(Map<String, String> productsAndInvoiceNumber) {
		this.productsAndInvoiceNumber = productsAndInvoiceNumber;
	}
	
	public String getDisputeInvoiceNumber() {
		return disputeInvoiceNumber;
	}

	public void setDisputeInvoiceNumber(String disputeInvoiceNumber) {
		this.disputeInvoiceNumber = disputeInvoiceNumber;
	}

	public String getDisputeItemNumber() {
		return disputeItemNumber;
	}

	public void setDisputeItemNumber(String disputeItemNumber) {
		this.disputeItemNumber = disputeItemNumber;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getDisputeType() {
		return disputeType;
	}

	public void setDisputeType(String disputeType) {
		this.disputeType = disputeType;
	}

	public boolean isDisputeItemFlag() {
		return disputeItemFlag;
	}

	public void setDisputeItemFlag(boolean disputeItemFlag) {
		this.disputeItemFlag = disputeItemFlag;
	}

	private String disputeItemNumber;
	private String orderCode;
	private String disputeType;
	private boolean disputeItemFlag;

	/**
	 * Expected price value for the disputed item.
	 */
	private String expectedPrice;

	/**
	 * Contract Number with associated order.
	 */
	private String contractNumber;

	/**
	 * Product code if order is being short shipped.
	 */
	private String shortShippedProductCode;

	/**
	 * Ordered quantity for the short shipped product.
	 */
	private String shortShippedOrderedQuantity;

	/**
	 * Received quantity for the short shipped product.
	 */
	private String shortShippedReceivedQuantity;

	/**
	 * Product code if order is being over shipped.
	 */
	private String overShippedProductCode;

	/**
	 * Ordered quantity for the over shipped product.
	 */
	private String overShippedOrderedQuantity;

	/**
	 * Received quantity for the over shipped product.
	 */
	private String overShippedReceivedQuantity;

	/**
	 * Indicates if product replacement required.
	 */
	private boolean replacementRequired;

	/**
	 * <code>List</code> of Lot Numbers.
	 */
	private List<String> lotNumbers;

	/**
	 * Indicates if shipped products to be retained.
	 * 
	 */
	private boolean keepProductsShipped;

	/**
	 * New purcahse order number for the associated order.
	 */
	private String newPurchaseOrderNumber;

	/**
	 * @return the totalDisputedAmount
	 */
	public String getTotalDisputedAmount()
	{
		return totalDisputedAmount;
	}

	/**
	 * @param totalDisputedAmount
	 *           the totalDisputedAmount to set
	 */
	public void setTotalDisputedAmount(final String totalDisputedAmount)
	{
		this.totalDisputedAmount = totalDisputedAmount;
	}

	/**
	 * @return the expectedPrice
	 */
	public String getExpectedPrice()
	{
		return expectedPrice;
	}

	/**
	 * @param expectedPrice
	 *           the expectedPrice to set
	 */
	public void setExpectedPrice(final String expectedPrice)
	{
		this.expectedPrice = expectedPrice;
	}

	/**
	 * @return the contractNumber
	 */
	public String getContractNumber()
	{
		return contractNumber;
	}

	/**
	 * @param contractNumber
	 *           the contractNumber to set
	 */
	public void setContractNumber(final String contractNumber)
	{
		this.contractNumber = contractNumber;
	}

	/**
	 * @return the shortShippedProductCode
	 */
	public String getShortShippedProductCode()
	{
		return shortShippedProductCode;
	}

	/**
	 * @param shortShippedProductCode
	 *           the shortShippedProductCode to set
	 */
	public void setShortShippedProductCode(final String shortShippedProductCode)
	{
		this.shortShippedProductCode = shortShippedProductCode;
	}

	/**
	 * @return the shortShippedOrderedQuantity
	 */
	public String getShortShippedOrderedQuantity()
	{
		return shortShippedOrderedQuantity;
	}

	/**
	 * @param shortShippedOrderedQuantity
	 *           the shortShippedOrderedQuantity to set
	 */
	public void setShortShippedOrderedQuantity(final String shortShippedOrderedQuantity)
	{
		this.shortShippedOrderedQuantity = shortShippedOrderedQuantity;
	}

	/**
	 * @return the shortShippedReceivedQuantity
	 */
	public String getShortShippedReceivedQuantity()
	{
		return shortShippedReceivedQuantity;
	}

	/**
	 * @param shortShippedReceivedQuantity
	 *           the shortShippedReceivedQuantity to set
	 */
	public void setShortShippedReceivedQuantity(final String shortShippedReceivedQuantity)
	{
		this.shortShippedReceivedQuantity = shortShippedReceivedQuantity;
	}

	/**
	 * @return the overShippedProductCode
	 */
	public String getOverShippedProductCode()
	{
		return overShippedProductCode;
	}

	/**
	 * @param overShippedProductCode
	 *           the overShippedProductCode to set
	 */
	public void setOverShippedProductCode(final String overShippedProductCode)
	{
		this.overShippedProductCode = overShippedProductCode;
	}

	/**
	 * @return the overShippedOrderedQuantity
	 */
	public String getOverShippedOrderedQuantity()
	{
		return overShippedOrderedQuantity;
	}

	/**
	 * @param overShippedOrderedQuantity
	 *           the overShippedOrderedQuantity to set
	 */
	public void setOverShippedOrderedQuantity(final String overShippedOrderedQuantity)
	{
		this.overShippedOrderedQuantity = overShippedOrderedQuantity;
	}

	/**
	 * @return the overShippedReceivedQuantity
	 */
	public String getOverShippedReceivedQuantity()
	{
		return overShippedReceivedQuantity;
	}

	/**
	 * @param overShippedReceivedQuantity
	 *           the overShippedReceivedQuantity to set
	 */
	public void setOverShippedReceivedQuantity(final String overShippedReceivedQuantity)
	{
		this.overShippedReceivedQuantity = overShippedReceivedQuantity;
	}

	/**
	 * @return the replacementRequired
	 */
	public boolean isReplacementRequired()
	{
		return replacementRequired;
	}

	/**
	 * @param replacementRequired
	 *           the replacementRequired to set
	 */
	public void setReplacementRequired(final boolean replacementRequired)
	{
		this.replacementRequired = replacementRequired;
	}

	/**
	 * @return the lotNumbers
	 */
	public List<String> getLotNumbers()
	{
		return lotNumbers;
	}

	/**
	 * @param lotNumbers
	 *           the lotNumbers to set
	 */
	public void setLotNumbers(final List<String> lotNumbers)
	{
		this.lotNumbers = lotNumbers;
	}

	/**
	 * @return the keepProductsShipped
	 */
	public boolean isKeepProductsShipped()
	{
		return keepProductsShipped;
	}

	/**
	 * @param keepProductsShipped
	 *           the keepProductsShipped to set
	 */
	public void setKeepProductsShipped(final boolean keepProductsShipped)
	{
		this.keepProductsShipped = keepProductsShipped;
	}

	/**
	 * @return the newPurchaseOrderNumber
	 */
	public String getNewPurchaseOrderNumber()
	{
		return newPurchaseOrderNumber;
	}

	/**
	 * @param newPurchaseOrderNumber
	 *           the newPurchaseOrderNumber to set
	 */
	public void setNewPurchaseOrderNumber(final String newPurchaseOrderNumber)
	{
		this.newPurchaseOrderNumber = newPurchaseOrderNumber;
	}

}
