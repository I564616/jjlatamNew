/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import de.hybris.platform.commercefacades.user.data.AddressData;

import java.util.List;
import java.util.Map;


/**
 * Class for holding Dispute Order Inquiry Form data captured from Dispute Order inquiry page.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTDisputeOrderInquiryDto extends JnjGTDisputeInquiryDto
{
	/**
	 * Map holding product code and <code>List</code> of lot numbers as key value pairs.
	 */
	private Map<String, List<String>> productsAndlotInfo;

	/**
	 * Map holding product code and associated quantity as key value pairs.
	 */
	private Map<String, String> productsAndQuantity;

	/**
	 * <code>list</code> of Disputed fees reasons.
	 */
	private List<String> disputedFees;

	/**
	 * Correct Purchase Order Number entered.
	 */
	private String correctPurchaseOrderNumber;

	/**
	 * Ship to address data.
	 */
	private AddressData shipToAddress;

	/**
	 * Correct to address data.
	 */
	private AddressData correctAddress;

	/**
	 * Indicates if certificate is attached.
	 */
	private boolean certificateAttached;

	/**
	 * contains created file name.
	 */
	private String createdFileName;

	/**
	 * Indicates if shipped products would be retained.
	 */
	private boolean keepProductsShipped;

	/**
	 * New Purchase Order Number entered.
	 */
	private String newPONumber;

	/**
	 * Indicates if incorrect address dispute is checked.
	 */
	private boolean inCorrectAddressDispute;

	/**
	 * @return the productsAndlotInfo
	 */
	public Map<String, List<String>> getProductsAndlotInfo()
	{
		return productsAndlotInfo;
	}

	/**
	 * @param productsAndlotInfo
	 *           the productsAndlotInfo to set
	 */
	public void setProductsAndlotInfo(final Map<String, List<String>> productsAndlotInfo)
	{
		this.productsAndlotInfo = productsAndlotInfo;
	}

	/**
	 * @return the productsAndQuantity
	 */
	public Map<String, String> getProductsAndQuantity()
	{
		return productsAndQuantity;
	}

	/**
	 * @param productsAndQuantity
	 *           the productsAndQuantity to set
	 */
	public void setProductsAndQuantity(final Map<String, String> productsAndQuantity)
	{
		this.productsAndQuantity = productsAndQuantity;
	}

	/**
	 * @return the disputedFees
	 */
	public List<String> getDisputedFees()
	{
		return disputedFees;
	}

	/**
	 * @param disputedFees
	 *           the disputedFees to set
	 */
	public void setDisputedFees(final List<String> disputedFees)
	{
		this.disputedFees = disputedFees;
	}

	/**
	 * @return the correctPurchaseOrderNumber
	 */
	public String getCorrectPurchaseOrderNumber()
	{
		return correctPurchaseOrderNumber;
	}

	/**
	 * @param correctPurchaseOrderNumber
	 *           the correctPurchaseOrderNumber to set
	 */
	public void setCorrectPurchaseOrderNumber(final String correctPurchaseOrderNumber)
	{
		this.correctPurchaseOrderNumber = correctPurchaseOrderNumber;
	}

	/**
	 * @return the shipToAddress
	 */
	public AddressData getShipToAddress()
	{
		return shipToAddress;
	}

	/**
	 * @param shipToAddress
	 *           the shipToAddress to set
	 */
	public void setShipToAddress(final AddressData shipToAddress)
	{
		this.shipToAddress = shipToAddress;
	}

	/**
	 * @return the correctAddress
	 */
	public AddressData getCorrectAddress()
	{
		return correctAddress;
	}

	/**
	 * @param correctAddress
	 *           the correctAddress to set
	 */
	public void setCorrectAddress(final AddressData correctAddress)
	{
		this.correctAddress = correctAddress;
	}

	/**
	 * @return the certificateAttached
	 */
	public boolean isCertificateAttached()
	{
		return certificateAttached;
	}

	/**
	 * @param certificateAttached
	 *           the certificateAttached to set
	 */
	public void setCertificateAttached(final boolean certificateAttached)
	{
		this.certificateAttached = certificateAttached;
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
	 * @return the inCorrectAddressDispute
	 */
	public boolean isInCorrectAddressDispute()
	{
		return inCorrectAddressDispute;
	}

	/**
	 * @param inCorrectAddressDispute
	 *           the inCorrectAddressDispute to set
	 */
	public void setInCorrectAddressDispute(final boolean inCorrectAddressDispute)
	{
		this.inCorrectAddressDispute = inCorrectAddressDispute;
	}

	/**
	 * @return the newPONumber
	 */
	public String getNewPONumber()
	{
		return newPONumber;
	}

	/**
	 * @param newPONumber
	 *           the newPONumber to set
	 */
	public void setNewPONumber(final String newPONumber)
	{
		this.newPONumber = newPONumber;
	}

	/**
	 * @return the createdFileName
	 */
	public String getCreatedFileName()
	{
		return createdFileName;
	}

	/**
	 * @param createdFileName
	 *           the createdFileName to set
	 */
	public void setCreatedFileName(final String createdFileName)
	{
		this.createdFileName = createdFileName;
	}

}
