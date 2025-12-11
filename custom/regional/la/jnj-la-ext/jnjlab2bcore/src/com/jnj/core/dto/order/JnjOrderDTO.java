package com.jnj.core.dto.order;

import java.util.ArrayList;
import java.util.List;


/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

/**
 * TODO:<class level comments are missing>.
 *
 * @author mpanda3
 * @version 1.0
 */
public class JnjOrderDTO
{

	private String salesOrganizationCode;

	private String payFromNumber;

	private String invoiceStatus;

	private String salesOrderOverallStatus;

	private String deliveryMode;

	private String division;

	private String contractNumber;

	private String orderNumber;

	private String salesOrderDeliveryStatus;

	private String forbiddenSales;

	private String completeDelivery;

	private String startDate;

	private String salesOrderDataCompleteness;

	private String shipToNumber;

	private String customerPONumber;

	private String poType;

	private String distributionChannel;

	private String headerDeliveryBlock;

	private String salesOrderCreditStatus;

	private String SAPOrderNumber;

	private String namedDeliveryDate;

	private String totalNetPrice;

	private String salesOrderRejectionStatus;

	private String soldToNumber;

	private String lastUpdatedDate;

	private List<JnjOrderLineDTO> JnjOrderLines;

	public JnjOrderDTO()
	{
		JnjOrderLines = new ArrayList<JnjOrderLineDTO>();
	}

	/**
	 * @return the salesOrganizationCode
	 */
	public String getSalesOrganizationCode()
	{
		return salesOrganizationCode;
	}

	/**
	 * @param salesOrganizationCode
	 *           the salesOrganizationCode to set
	 */
	public void setSalesOrganizationCode(final String salesOrganizationCode)
	{
		this.salesOrganizationCode = salesOrganizationCode;
	}

	/**
	 * @return the payFromNumber
	 */
	public String getPayFromNumber()
	{
		return payFromNumber;
	}

	/**
	 * @param payFromNumber
	 *           the payFromNumber to set
	 */
	public void setPayFromNumber(final String payFromNumber)
	{
		this.payFromNumber = payFromNumber;
	}

	/**
	 * @return the invoiceStatus
	 */
	public String getInvoiceStatus()
	{
		return invoiceStatus;
	}

	/**
	 * @param invoiceStatus
	 *           the invoiceStatus to set
	 */
	public void setInvoiceStatus(final String invoiceStatus)
	{
		this.invoiceStatus = invoiceStatus;
	}

	/**
	 * @return the salesOrderOverallStatus
	 */
	public String getSalesOrderOverallStatus()
	{
		return salesOrderOverallStatus;
	}

	/**
	 * @param salesOrderOverallStatus
	 *           the salesOrderOverallStatus to set
	 */
	public void setSalesOrderOverallStatus(final String salesOrderOverallStatus)
	{
		this.salesOrderOverallStatus = salesOrderOverallStatus;
	}

	/**
	 * @return the deliveryMode
	 */
	public String getDeliveryMode()
	{
		return deliveryMode;
	}

	/**
	 * @param deliveryMode
	 *           the deliveryMode to set
	 */
	public void setDeliveryMode(final String deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	/**
	 * @return the division
	 */
	public String getDivision()
	{
		return division;
	}

	/**
	 * @param division
	 *           the division to set
	 */
	public void setDivision(final String division)
	{
		this.division = division;
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
	 * @return the orderNumber
	 */
	public String getOrderNumber()
	{
		return orderNumber;
	}

	/**
	 * @param orderNumber
	 *           the orderNumber to set
	 */
	public void setOrderNumber(final String orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the salesOrderDeliveryStatus
	 */
	public String getSalesOrderDeliveryStatus()
	{
		return salesOrderDeliveryStatus;
	}

	/**
	 * @param salesOrderDeliveryStatus
	 *           the salesOrderDeliveryStatus to set
	 */
	public void setSalesOrderDeliveryStatus(final String salesOrderDeliveryStatus)
	{
		this.salesOrderDeliveryStatus = salesOrderDeliveryStatus;
	}

	/**
	 * @return the forbiddenSales
	 */
	public String getForbiddenSales()
	{
		return forbiddenSales;
	}

	/**
	 * @param forbiddenSales
	 *           the forbiddenSales to set
	 */
	public void setForbiddenSales(final String forbiddenSales)
	{
		this.forbiddenSales = forbiddenSales;
	}

	/**
	 * @return the completeDelivery
	 */
	public String getCompleteDelivery()
	{
		return completeDelivery;
	}

	/**
	 * @param completeDelivery
	 *           the completeDelivery to set
	 */
	public void setCompleteDelivery(final String completeDelivery)
	{
		this.completeDelivery = completeDelivery;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *           the startDate to set
	 */
	public void setStartDate(final String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return the salesOrderDataCompleteness
	 */
	public String getSalesOrderDataCompleteness()
	{
		return salesOrderDataCompleteness;
	}

	/**
	 * @param salesOrderDataCompleteness
	 *           the salesOrderDataCompleteness to set
	 */
	public void setSalesOrderDataCompleteness(final String salesOrderDataCompleteness)
	{
		this.salesOrderDataCompleteness = salesOrderDataCompleteness;
	}

	/**
	 * @return the shipToNumber
	 */
	public String getShipToNumber()
	{
		return shipToNumber;
	}

	/**
	 * @param shipToNumber
	 *           the shipToNumber to set
	 */
	public void setShipToNumber(final String shipToNumber)
	{
		this.shipToNumber = shipToNumber;
	}

	/**
	 * @return the customerPONumber
	 */
	public String getCustomerPONumber()
	{
		return customerPONumber;
	}

	/**
	 * @param customerPONumber
	 *           the customerPONumber to set
	 */
	public void setCustomerPONumber(final String customerPONumber)
	{
		this.customerPONumber = customerPONumber;
	}

	/**
	 * @return the poType
	 */
	public String getPoType()
	{
		return poType;
	}

	/**
	 * @param poType
	 *           the poType to set
	 */
	public void setPoType(final String poType)
	{
		this.poType = poType;
	}

	/**
	 * @return the distributionChannel
	 */
	public String getDistributionChannel()
	{
		return distributionChannel;
	}

	/**
	 * @param distributionChannel
	 *           the distributionChannel to set
	 */
	public void setDistributionChannel(final String distributionChannel)
	{
		this.distributionChannel = distributionChannel;
	}

	/**
	 * @return the headerDeliveryBlock
	 */
	public String getHeaderDeliveryBlock()
	{
		return headerDeliveryBlock;
	}

	/**
	 * @param headerDeliveryBlock
	 *           the headerDeliveryBlock to set
	 */
	public void setHeaderDeliveryBlock(final String headerDeliveryBlock)
	{
		this.headerDeliveryBlock = headerDeliveryBlock;
	}

	/**
	 * @return the salesOrderCreditStatus
	 */
	public String getSalesOrderCreditStatus()
	{
		return salesOrderCreditStatus;
	}

	/**
	 * @param salesOrderCreditStatus
	 *           the salesOrderCreditStatus to set
	 */
	public void setSalesOrderCreditStatus(final String salesOrderCreditStatus)
	{
		this.salesOrderCreditStatus = salesOrderCreditStatus;
	}

	/**
	 * @return the sAPOrderNumber
	 */
	public String getSAPOrderNumber()
	{
		return SAPOrderNumber;
	}

	/**
	 * @param sAPOrderNumber
	 *           the sAPOrderNumber to set
	 */
	public void setSAPOrderNumber(final String sAPOrderNumber)
	{
		SAPOrderNumber = sAPOrderNumber;
	}

	/**
	 * @return the namedDeliveryDate
	 */
	public String getNamedDeliveryDate()
	{
		return namedDeliveryDate;
	}

	/**
	 * @param namedDeliveryDate
	 *           the namedDeliveryDate to set
	 */
	public void setNamedDeliveryDate(final String namedDeliveryDate)
	{
		this.namedDeliveryDate = namedDeliveryDate;
	}

	/**
	 * @return the totalNetPrice
	 */
	public String getTotalNetPrice()
	{
		return totalNetPrice;
	}

	/**
	 * @param totalNetPrice
	 *           the totalNetPrice to set
	 */
	public void setTotalNetPrice(final String totalNetPrice)
	{
		this.totalNetPrice = totalNetPrice;
	}

	/**
	 * @return the salesOrderRejectionStatus
	 */
	public String getSalesOrderRejectionStatus()
	{
		return salesOrderRejectionStatus;
	}

	/**
	 * @param salesOrderRejectionStatus
	 *           the salesOrderRejectionStatus to set
	 */
	public void setSalesOrderRejectionStatus(final String salesOrderRejectionStatus)
	{
		this.salesOrderRejectionStatus = salesOrderRejectionStatus;
	}

	/**
	 * @return the soldToNumber
	 */
	public String getSoldToNumber()
	{
		return soldToNumber;
	}

	/**
	 * @param soldToNumber
	 *           the soldToNumber to set
	 */
	public void setSoldToNumber(final String soldToNumber)
	{
		this.soldToNumber = soldToNumber;
	}

	/**
	 * @return the jnjOrderLines
	 */
	public List<JnjOrderLineDTO> getJnjOrderLines()
	{
		return new ArrayList<>(JnjOrderLines);
	}

	/**
	 * @param jnjOrderLines
	 *           the jnjOrderLines to set
	 */
	public void setJnjOrderLines(final List<JnjOrderLineDTO> jnjOrderLines)
	{
		this.JnjOrderLines = new ArrayList<>(jnjOrderLines);
	}

	/**
	 * @return the lastUpdatedDate
	 */
	public String getLastUpdatedDate()
	{
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate
	 *           the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(final String lastUpdatedDate)
	{
		this.lastUpdatedDate = lastUpdatedDate;
	}
}
