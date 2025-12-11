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


public class JnjOrderLineDTO
{

	private String orderLineNumber;

	private String internationalFreight;

	private String salesUOM;

	private String discount;

	private String indirectCustomer;

	private String handlingFee;

	private String lineOverallStatus;

	private String taxesLocal;

	private String insurance;

	private String GTSHold;

	private String quantity;

	private String unitPrice;

	private String dropShipFee;

	private String reasonForRejection;

	private String orderNumber;

	private String minimumOrderFee;

	private String expeditedFee;

	private String deliveryStatus;

	private String tax;

	private String freightcharges;

	private String entryNumber;

	private String itemCategory;

	private String netPrice;

	private String lineInvoiceStatus;

	private String higherLevelItemNumber;

	private String materialEntered;

	private String code;

	private String grossPrice;

	private List<JnjOrderSchLineDTO> JnjOrderSchLines;

	public JnjOrderLineDTO()
	{
		JnjOrderSchLines = new ArrayList<JnjOrderSchLineDTO>();
	}

	/**
	 * @return the internationalFreight
	 */
	public String getInternationalFreight()
	{
		return internationalFreight;
	}



	/**
	 * @param internationalFreight
	 *           the internationalFreight to set
	 */
	public void setInternationalFreight(final String internationalFreight)
	{
		this.internationalFreight = internationalFreight;
	}



	/**
	 * @return the salesUOM
	 */
	public String getSalesUOM()
	{
		return salesUOM;
	}



	/**
	 * @param salesUOM
	 *           the salesUOM to set
	 */
	public void setSalesUOM(final String salesUOM)
	{
		this.salesUOM = salesUOM;
	}



	/**
	 * @return the discount
	 */
	public String getDiscount()
	{
		return discount;
	}



	/**
	 * @param discount
	 *           the discount to set
	 */
	public void setDiscount(final String discount)
	{
		this.discount = discount;
	}



	/**
	 * @return the indirectCustomer
	 */
	public String getIndirectCustomer()
	{
		return indirectCustomer;
	}



	/**
	 * @param indirectCustomer
	 *           the indirectCustomer to set
	 */
	public void setIndirectCustomer(final String indirectCustomer)
	{
		this.indirectCustomer = indirectCustomer;
	}



	/**
	 * @return the handlingFee
	 */
	public String getHandlingFee()
	{
		return handlingFee;
	}



	/**
	 * @param handlingFee
	 *           the handlingFee to set
	 */
	public void setHandlingFee(final String handlingFee)
	{
		this.handlingFee = handlingFee;
	}



	/**
	 * @return the lineOverallStatus
	 */
	public String getLineOverallStatus()
	{
		return lineOverallStatus;
	}



	/**
	 * @param lineOverallStatus
	 *           the lineOverallStatus to set
	 */
	public void setLineOverallStatus(final String lineOverallStatus)
	{
		this.lineOverallStatus = lineOverallStatus;
	}

	/**
	 * @return the taxesLocal
	 */
	public String getTaxesLocal()
	{
		return taxesLocal;
	}



	/**
	 * @param taxesLocal
	 *           the taxesLocal to set
	 */
	public void setTaxesLocal(final String taxesLocal)
	{
		this.taxesLocal = taxesLocal;
	}



	/**
	 * @return the insurance
	 */
	public String getInsurance()
	{
		return insurance;
	}



	/**
	 * @param insurance
	 *           the insurance to set
	 */
	public void setInsurance(final String insurance)
	{
		this.insurance = insurance;
	}



	/**
	 * @return the gTSHold
	 */
	public String getGTSHold()
	{
		return GTSHold;
	}



	/**
	 * @param gTSHold
	 *           the gTSHold to set
	 */
	public void setGTSHold(final String gTSHold)
	{
		GTSHold = gTSHold;
	}



	/**
	 * @return the quantity
	 */
	public String getQuantity()
	{
		return quantity;
	}



	/**
	 * @param quantity
	 *           the quantity to set
	 */
	public void setQuantity(final String quantity)
	{
		this.quantity = quantity;
	}



	/**
	 * @return the unitPrice
	 */
	public String getUnitPrice()
	{
		return unitPrice;
	}



	/**
	 * @param unitPrice
	 *           the unitPrice to set
	 */
	public void setUnitPrice(final String unitPrice)
	{
		this.unitPrice = unitPrice;
	}



	/**
	 * @return the dropShipFee
	 */
	public String getDropShipFee()
	{
		return dropShipFee;
	}



	/**
	 * @param dropShipFee
	 *           the dropShipFee to set
	 */
	public void setDropShipFee(final String dropShipFee)
	{
		this.dropShipFee = dropShipFee;
	}



	/**
	 * @return the reasonForRejection
	 */
	public String getReasonForRejection()
	{
		return reasonForRejection;
	}



	/**
	 * @param reasonForRejection
	 *           the reasonForRejection to set
	 */
	public void setReasonForRejection(final String reasonForRejection)
	{
		this.reasonForRejection = reasonForRejection;
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
	 * @return the minimumOrderFee
	 */
	public String getMinimumOrderFee()
	{
		return minimumOrderFee;
	}



	/**
	 * @param minimumOrderFee
	 *           the minimumOrderFee to set
	 */
	public void setMinimumOrderFee(final String minimumOrderFee)
	{
		this.minimumOrderFee = minimumOrderFee;
	}



	/**
	 * @return the expeditedFee
	 */
	public String getExpeditedFee()
	{
		return expeditedFee;
	}



	/**
	 * @param expeditedFee
	 *           the expeditedFee to set
	 */
	public void setExpeditedFee(final String expeditedFee)
	{
		this.expeditedFee = expeditedFee;
	}



	/**
	 * @return the deliveryStatus
	 */
	public String getDeliveryStatus()
	{
		return deliveryStatus;
	}



	/**
	 * @param deliveryStatus
	 *           the deliveryStatus to set
	 */
	public void setDeliveryStatus(final String deliveryStatus)
	{
		this.deliveryStatus = deliveryStatus;
	}



	/**
	 * @return the tax
	 */
	public String getTax()
	{
		return tax;
	}



	/**
	 * @param tax
	 *           the tax to set
	 */
	public void setTax(final String tax)
	{
		this.tax = tax;
	}



	/**
	 * @return the freightcharges
	 */
	public String getFreightcharges()
	{
		return freightcharges;
	}



	/**
	 * @param freightcharges
	 *           the freightcharges to set
	 */
	public void setFreightcharges(final String freightcharges)
	{
		this.freightcharges = freightcharges;
	}



	/**
	 * @return the entryNumber
	 */
	public String getEntryNumber()
	{
		return entryNumber;
	}



	/**
	 * @param entryNumber
	 *           the entryNumber to set
	 */
	public void setEntryNumber(final String entryNumber)
	{
		this.entryNumber = entryNumber;
	}



	/**
	 * @return the itemCategory
	 */
	public String getItemCategory()
	{
		return itemCategory;
	}



	/**
	 * @param itemCategory
	 *           the itemCategory to set
	 */
	public void setItemCategory(final String itemCategory)
	{
		this.itemCategory = itemCategory;
	}



	/**
	 * @return the netPrice
	 */
	public String getNetPrice()
	{
		return netPrice;
	}



	/**
	 * @param netPrice
	 *           the netPrice to set
	 */
	public void setNetPrice(final String netPrice)
	{
		this.netPrice = netPrice;
	}



	/**
	 * @return the lineInvoiceStatus
	 */
	public String getLineInvoiceStatus()
	{
		return lineInvoiceStatus;
	}



	/**
	 * @param lineInvoiceStatus
	 *           the lineInvoiceStatus to set
	 */
	public void setLineInvoiceStatus(final String lineInvoiceStatus)
	{
		this.lineInvoiceStatus = lineInvoiceStatus;
	}



	/**
	 * @return the higherLevelItemNumber
	 */
	public String getHigherLevelItemNumber()
	{
		return higherLevelItemNumber;
	}



	/**
	 * @param higherLevelItemNumber
	 *           the higherLevelItemNumber to set
	 */
	public void setHigherLevelItemNumber(final String higherLevelItemNumber)
	{
		this.higherLevelItemNumber = higherLevelItemNumber;
	}



	/**
	 * @return the materialEntered
	 */
	public String getMaterialEntered()
	{
		return materialEntered;
	}



	/**
	 * @param materialEntered
	 *           the materialEntered to set
	 */
	public void setMaterialEntered(final String materialEntered)
	{
		this.materialEntered = materialEntered;
	}



	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}



	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}



	/**
	 * @return the grossPrice
	 */
	public String getGrossPrice()
	{
		return grossPrice;
	}



	/**
	 * @param grossPrice
	 *           the grossPrice to set
	 */
	public void setGrossPrice(final String grossPrice)
	{
		this.grossPrice = grossPrice;
	}



	/**
	 * @return the jnjOrderSchLines
	 */
	public List<JnjOrderSchLineDTO> getJnjOrderSchLines()
	{
		return new ArrayList<>(JnjOrderSchLines);
	}

	/**
	 * @param jnjOrderSchLines
	 *           the jnjOrderSchLines to set
	 */
	public void setJnjOrderSchLines(final List<JnjOrderSchLineDTO> jnjOrderSchLines)
	{
		this.JnjOrderSchLines = new ArrayList<>(jnjOrderSchLines);
	}

	/**
	 * @return the orderLineNumber
	 */
	public String getOrderLineNumber()
	{
		return orderLineNumber;
	}

	/**
	 * @param orderLineNumber
	 *           the orderLineNumber to set
	 */
	public void setOrderLineNumber(final String orderLineNumber)
	{
		this.orderLineNumber = orderLineNumber;
	}
}
