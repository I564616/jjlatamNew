/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.data;

import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.Date;
import java.util.Map;


/**
 * TODO:<Sandeep Kumar-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderHistoryData extends OrderHistoryData
{

	private String clientNumber;
	private String jnJOrderNumber;
	private String contractNumber;
	private Date date;
	private Double totalOrder;
	private String orderStatus;
	private Map testMap;
	private String sapOrderNumber;
	private String customerPONumber;
	private String orderNumber;
	private String deliveryMode;
	private String soldToNumber;
	private String shipToNumber;
	private String payFromNumber;
	private String namedDeliveryDate;
	private String totalNetPrice;
	private String startDate;
	private String forbiddenSales;
	private String completeDelivery;
	private String salesOrganizationCode;
	private String distributionChannel;
	private String division;
	private String salesOrderOverallStatus;
	private String salesOrderDeliveryStatus;
	private String salesOrderRejectionStatus;
	private String SalesOrderCreditStatus;
	private String salesOrderDataCompleteness;
	private String headerDeliveryBlock;
	private String poType;
	private String invoiceStatus;
	private PriceData totalGrossPrice;



	/**
	 * @return the totalGrossPrice
	 */
	public PriceData getTotalGrossPrice()
	{
		return totalGrossPrice;
	}

	/**
	 * @param totalGrossPrice
	 *           the totalGrossPrice to set
	 */
	public void setTotalGrossPrice(final PriceData totalGrossPrice)
	{
		this.totalGrossPrice = totalGrossPrice;
	}

	/**
	 * @return the clientNumber
	 */
	public String getClientNumber()
	{
		return clientNumber;
	}

	/**
	 * @param clientNumber
	 *           the clientNumber to set
	 */
	public void setClientNumber(final String clientNumber)
	{
		this.clientNumber = clientNumber;
	}

	/**
	 * @return the jnJOrderNumber
	 */
	public String getJnJOrderNumber()
	{
		return jnJOrderNumber;
	}

	/**
	 * @param jnJOrderNumber
	 *           the jnJOrderNumber to set
	 */
	public void setJnJOrderNumber(final String jnJOrderNumber)
	{
		this.jnJOrderNumber = jnJOrderNumber;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date
	 *           the date to set
	 */
	public void setDate(final Date date)
	{
		this.date = date;
	}

	/**
	 * @return the totalOrder
	 */
	public Double getTotalOrder()
	{
		return totalOrder;
	}

	/**
	 * @param totalOrder
	 *           the totalOrder to set
	 */
	public void setTotalOrder(final Double totalOrder)
	{
		this.totalOrder = totalOrder;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus()
	{
		return orderStatus;
	}

	/**
	 * @param orderStatus
	 *           the orderStatus to set
	 */
	public void setOrderStatus(final String orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the testMap
	 */
	public Map getTestMap()
	{
		return testMap;
	}

	/**
	 * @param testMap
	 *           the testMap to set
	 */
	public void setTestMap(final Map testMap)
	{
		this.testMap = testMap;
	}

	/**
	 * @return the sapOrderNumber
	 */
	public String getSapOrderNumber()
	{
		return sapOrderNumber;
	}

	/**
	 * @param sapOrderNumber
	 *           the sapOrderNumber to set
	 */
	public void setSapOrderNumber(final String sapOrderNumber)
	{
		this.sapOrderNumber = sapOrderNumber;
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
	 * @return the salesOrderCreditStatus
	 */
	public String getSalesOrderCreditStatus()
	{
		return SalesOrderCreditStatus;
	}

	/**
	 * @param salesOrderCreditStatus
	 *           the salesOrderCreditStatus to set
	 */
	public void setSalesOrderCreditStatus(final String salesOrderCreditStatus)
	{
		SalesOrderCreditStatus = salesOrderCreditStatus;
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



}
