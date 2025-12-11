/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.util;

import java.io.Serializable;
import java.util.List;


/**
 * This class represents a group of Form Tables
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjFormGroup implements Serializable
{
	private String jnjProcedureCode;
	private String hospitalCode;
	private String procedureDescription;
	private String procedureType;
	private String folio;
	private String patient;
	private String doctor;
	private List<String> item;
	private List<String> qty;
	private List<String> uom;
	private List<String> batchNumber;

	/**
	 * @return the jnjProcedureCode
	 */
	public String getJnjProcedureCode()
	{
		return jnjProcedureCode;
	}

	/**
	 * @param jnjProcedureCode
	 *           the jnjProcedureCode to set
	 */
	public void setJnjProcedureCode(final String jnjProcedureCode)
	{
		this.jnjProcedureCode = jnjProcedureCode;
	}

	/**
	 * @return the hospitalCode
	 */
	public String getHospitalCode()
	{
		return hospitalCode;
	}

	/**
	 * @param hospitalCode
	 *           the hospitalCode to set
	 */
	public void setHospitalCode(final String hospitalCode)
	{
		this.hospitalCode = hospitalCode;
	}

	/**
	 * @return the procedureDescription
	 */
	public String getProcedureDescription()
	{
		return procedureDescription;
	}

	/**
	 * @param procedureDescription
	 *           the procedureDescription to set
	 */
	public void setProcedureDescription(final String procedureDescription)
	{
		this.procedureDescription = procedureDescription;
	}

	/**
	 * @return the procedureType
	 */
	public String getProcedureType()
	{
		return procedureType;
	}

	/**
	 * @param procedureType
	 *           the procedureType to set
	 */
	public void setProcedureType(final String procedureType)
	{
		this.procedureType = procedureType;
	}

	/**
	 * @return the folio
	 */
	public String getFolio()
	{
		return folio;
	}

	/**
	 * @param folio
	 *           the folio to set
	 */
	public void setFolio(final String folio)
	{
		this.folio = folio;
	}

	/**
	 * @return the patient
	 */
	public String getPatient()
	{
		return patient;
	}

	/**
	 * @param patient
	 *           the patient to set
	 */
	public void setPatient(final String patient)
	{
		this.patient = patient;
	}

	/**
	 * @return the doctor
	 */
	public String getDoctor()
	{
		return doctor;
	}

	/**
	 * @param doctor
	 *           the doctor to set
	 */
	public void setDoctor(final String doctor)
	{
		this.doctor = doctor;
	}

	/**
	 * @return the item
	 */
	public List<String> getItem()
	{
		return item;
	}

	/**
	 * @param item
	 *           the item to set
	 */
	public void setItem(final List<String> item)
	{
		this.item = item;
	}

	/**
	 * @return the qty
	 */
	public List<String> getQty()
	{
		return qty;
	}

	/**
	 * @param qty
	 *           the qty to set
	 */
	public void setQty(final List<String> qty)
	{
		this.qty = qty;
	}

	/**
	 * @return the uom
	 */
	public List<String> getUom()
	{
		return uom;
	}

	/**
	 * @param uom
	 *           the uom to set
	 */
	public void setUom(final List<String> uom)
	{
		this.uom = uom;
	}

	/**
	 * @return the batchNumber
	 */
	public List<String> getBatchNumber()
	{
		return batchNumber;
	}

	/**
	 * @param batchNumber
	 *           the batchNumber to set
	 */
	public void setBatchNumber(final List<String> batchNumber)
	{
		this.batchNumber = batchNumber;
	}
}
