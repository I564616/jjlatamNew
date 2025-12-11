/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dto;

import java.sql.Date;
import java.util.List;


/**
 * This class holds the data for the surgery information form
 * 
 * @author sakshi.kashiva
 * @version 1.0
 */
public class JnjGTViewSurgeryInfoData
{

	private List<String> surgerySpecialty;
	private List<String> orthobiologics;
	private List<String> surgicalApproach;
	private List<String> interbody;
	private List<String> interbodyFusion;
	private Date caseDate;
	private List<String> levelsInstrumented;
	private List<String> pathology;
	private List<String> Zone;
	private String caseNumber;
	private List<String> procedureType;
	private List<String> cas;

	/**
	 * @return the surgerySpecialty
	 */
	public List<String> getSurgerySpecialty()
	{
		return surgerySpecialty;
	}

	/**
	 * @param surgerySpecialty
	 *           the surgerySpecialty to set
	 */
	public void setSurgerySpecialty(final List<String> surgerySpecialty)
	{
		this.surgerySpecialty = surgerySpecialty;
	}

	/**
	 * @return the orthobiologics
	 */
	public List<String> getOrthobiologics()
	{
		return orthobiologics;
	}

	/**
	 * @param orthobiologics
	 *           the orthobiologics to set
	 */
	public void setOrthobiologics(final List<String> orthobiologics)
	{
		this.orthobiologics = orthobiologics;
	}

	/**
	 * @return the surgicalApproach
	 */
	public List<String> getSurgicalApproach()
	{
		return surgicalApproach;
	}

	/**
	 * @param surgicalApproach
	 *           the surgicalApproach to set
	 */
	public void setSurgicalApproach(final List<String> surgicalApproach)
	{
		this.surgicalApproach = surgicalApproach;
	}

	/**
	 * @return the interbody
	 */
	public List<String> getInterbody()
	{
		return interbody;
	}

	/**
	 * @param interbody
	 *           the interbody to set
	 */
	public void setInterbody(final List<String> interbody)
	{
		this.interbody = interbody;
	}

	/**
	 * @return the interbodyFusion
	 */
	public List<String> getInterbodyFusion()
	{
		return interbodyFusion;
	}

	/**
	 * @param interbodyFusion
	 *           the interbodyFusion to set
	 */
	public void setInterbodyFusion(final List<String> interbodyFusion)
	{
		this.interbodyFusion = interbodyFusion;
	}

	/**
	 * @return the caseDate
	 */
	public Date getCaseDate()
	{
		return caseDate;
	}

	/**
	 * @param caseDate
	 *           the caseDate to set
	 */
	public void setCaseDate(final Date caseDate)
	{
		this.caseDate = caseDate;
	}

	/**
	 * @return the levelsInstrumented
	 */
	public List<String> getLevelsInstrumented()
	{
		return levelsInstrumented;
	}

	/**
	 * @param levelsInstrumented
	 *           the levelsInstrumented to set
	 */
	public void setLevelsInstrumented(final List<String> levelsInstrumented)
	{
		this.levelsInstrumented = levelsInstrumented;
	}

	/**
	 * @return the pathology
	 */
	public List<String> getPathology()
	{
		return pathology;
	}

	/**
	 * @param pathology
	 *           the pathology to set
	 */
	public void setPathology(final List<String> pathology)
	{
		this.pathology = pathology;
	}

	/**
	 * @return the zone
	 */
	public List<String> getZone()
	{
		return Zone;
	}

	/**
	 * @param zone
	 *           the zone to set
	 */
	public void setZone(final List<String> zone)
	{
		Zone = zone;
	}

	/**
	 * @return the caseNumber
	 */
	public String getCaseNumber()
	{
		return caseNumber;
	}

	/**
	 * @param caseNumber
	 *           the caseNumber to set
	 */
	public void setCaseNumber(final String caseNumber)
	{
		this.caseNumber = caseNumber;
	}

	/**
	 * @return the procedureType
	 */
	public List<String> getProcedureType()
	{
		return procedureType;
	}

	/**
	 * @param procedureType
	 *           the procedureType to set
	 */
	public void setProcedureType(final List<String> procedureType)
	{
		this.procedureType = procedureType;
	}

	/**
	 * @return the cas
	 */
	public List<String> getCas()
	{
		return cas;
	}

	/**
	 * @param cas
	 *           the cas to set
	 */
	public void setCas(final List<String> cas)
	{
		this.cas = cas;
	}



}
