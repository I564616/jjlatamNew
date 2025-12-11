/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.List;


/**
 * DTO class for header level objects of Invoices.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnJInvoiceHeaderDataDTO
{

	private String billType;
	private String netValue;
	private String creationDate;
	private String soldTo;
	private String payer;
	private String poNumber;
	private String region;
	private String nfYear;
	private String nfMonth;
	private String stcd;
	private String model;
	private String series;
	private String nfNumber;
	private String docNumber;
	private String cdv;
	private String billingDoc;
	private String cancelledDocNo;
	private String invoiceDocNumber;
	private String salesOrder;
	private List<JnJInvoiceLineItemDataDTO> lineItemList;
	private String fileName;


	/**
	 * @return the salesOrder
	 */
	public String getSalesOrder()
	{
		return salesOrder;
	}

	/**
	 * @param salesOrder
	 *           the salesOrder to set
	 */
	public void setSalesOrder(final String salesOrder)
	{
		this.salesOrder = salesOrder;
	}

	/**
	 * @return the invoiceDocNumber
	 */
	public String getInvoiceDocNumber()
	{
		return invoiceDocNumber;
	}

	/**
	 * @param invoiceDocNumber
	 *           the invoiceDocNumber to set
	 */
	public void setInvoiceDocNumber(final String invoiceDocNumber)
	{
		this.invoiceDocNumber = invoiceDocNumber;
	}

	/**
	 * @return the billType
	 */
	public String getBillType()
	{
		return billType;
	}

	/**
	 * @param billType
	 *           the billType to set
	 */
	public void setBillType(final String billType)
	{
		this.billType = billType;
	}

	/**
	 * @return the netValue
	 */
	public String getNetValue()
	{
		return netValue;
	}

	/**
	 * @param netValue
	 *           the netValue to set
	 */
	public void setNetValue(final String netValue)
	{
		this.netValue = netValue;
	}

	/**
	 * @return the creationDate
	 */
	public String getCreationDate()
	{
		return creationDate;
	}

	/**
	 * @param creationDate
	 *           the creationDate to set
	 */
	public void setCreationDate(final String creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * @return the soldTo
	 */
	public String getSoldTo()
	{
		return soldTo;
	}

	/**
	 * @param soldTo
	 *           the soldTo to set
	 */
	public void setSoldTo(final String soldTo)
	{
		this.soldTo = soldTo;
	}

	/**
	 * @return the payer
	 */
	public String getPayer()
	{
		return payer;
	}

	/**
	 * @param payer
	 *           the payer to set
	 */
	public void setPayer(final String payer)
	{
		this.payer = payer;
	}

	/**
	 * @return the poNumber
	 */
	public String getPoNumber()
	{
		return poNumber;
	}

	/**
	 * @param poNumber
	 *           the poNumber to set
	 */
	public void setPoNumber(final String poNumber)
	{
		this.poNumber = poNumber;
	}

	/**
	 * @return the region
	 */
	public String getRegion()
	{
		return region;
	}

	/**
	 * @param region
	 *           the region to set
	 */
	public void setRegion(final String region)
	{
		this.region = region;
	}

	/**
	 * @return the nfYear
	 */
	public String getNfYear()
	{
		return nfYear;
	}

	/**
	 * @param nfYear
	 *           the nfYear to set
	 */
	public void setNfYear(final String nfYear)
	{
		this.nfYear = nfYear;
	}

	/**
	 * @return the nfMonth
	 */
	public String getNfMonth()
	{
		return nfMonth;
	}

	/**
	 * @param nfMonth
	 *           the nfMonth to set
	 */
	public void setNfMonth(final String nfMonth)
	{
		this.nfMonth = nfMonth;
	}

	/**
	 * @return the stcd
	 */
	public String getStcd()
	{
		return stcd;
	}

	/**
	 * @param stcd
	 *           the stcd to set
	 */
	public void setStcd(final String stcd)
	{
		this.stcd = stcd;
	}

	/**
	 * @return the model
	 */
	public String getModel()
	{
		return model;
	}

	/**
	 * @param model
	 *           the model to set
	 */
	public void setModel(final String model)
	{
		this.model = model;
	}

	/**
	 * @return the series
	 */
	public String getSeries()
	{
		return series;
	}

	/**
	 * @param series
	 *           the series to set
	 */
	public void setSeries(final String series)
	{
		this.series = series;
	}

	/**
	 * @return the nfNumber
	 */
	public String getNfNumber()
	{
		return nfNumber;
	}

	/**
	 * @param nfNumber
	 *           the nfNumber to set
	 */
	public void setNfNumber(final String nfNumber)
	{
		this.nfNumber = nfNumber;
	}

	/**
	 * @return the docNumber
	 */
	public String getDocNumber()
	{
		return docNumber;
	}

	/**
	 * @param docNumber
	 *           the docNumber to set
	 */
	public void setDocNumber(final String docNumber)
	{
		this.docNumber = docNumber;
	}

	/**
	 * @return the cdv
	 */
	public String getCdv()
	{
		return cdv;
	}

	/**
	 * @param cdv
	 *           the cdv to set
	 */
	public void setCdv(final String cdv)
	{
		this.cdv = cdv;
	}

	/**
	 * @return the billingDoc
	 */
	public String getBillingDoc()
	{
		return billingDoc;
	}

	/**
	 * @param billingDoc
	 *           the billingDoc to set
	 */
	public void setBillingDoc(final String billingDoc)
	{
		this.billingDoc = billingDoc;
	}

	/**
	 * @return the cancelledDocNo
	 */
	public String getCancelledDocNo()
	{
		return cancelledDocNo;
	}

	/**
	 * @param cancelledDocNo
	 *           the cancelledDocNo to set
	 */
	public void setCancelledDocNo(final String cancelledDocNo)
	{
		this.cancelledDocNo = cancelledDocNo;
	}

	/**
	 * @return the lineItemList
	 */
	public List<JnJInvoiceLineItemDataDTO> getLineItemList()
	{
		return lineItemList;
	}

	/**
	 * @param lineItemList
	 *           the lineItemList to set
	 */
	public void setLineItemList(final List<JnJInvoiceLineItemDataDTO> lineItemList)
	{
		this.lineItemList = lineItemList;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *           the fileName to set
	 */
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

}
