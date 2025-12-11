/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.territory;

import de.hybris.platform.core.PK;

import java.util.List;
import java.util.Set;

import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;


/**
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTTerritoryDao
{

	/**
	 * Gets the territory divison by uid.
	 *
	 * @param id
	 *           the id
	 * @param sourceId
	 *           the source id
	 * @return the territory divison by uid
	 */
	public JnjGTTerritoryDivisonModel getTerritoryDivisonByUid(final String id, String sourceId);


	/**
	 * Gets the all jnj na territory divison.
	 *
	 * @param sourceSystemId
	 *           the source system id
	 * @return the all jnj na territory divison
	 */
	List<JnjGTTerritoryDivisonModel> getAllJnjGTTerritoryDivison(String sourceSystemId);

	/**
	 * Gets the all jnj na territory div cust rel model.
	 *
	 * @return the all jnj na territory div cust rel model
	 */
	public List<JnjGTTerritoryDivCustRelModel> getAllJnjGTTerritoryDivCustRelModel();

	/**
	 * Gets the territory div cust rel with null target.
	 *
	 * @param territoryDivPk
	 *           the territory div pk
	 * @param wwid
	 *           the wwid
	 * @param ucn
	 *           the ucn
	 * @return the territory div cust rel with null target
	 */
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRelWithNullTarget(PK territoryDivPk, String wwid, String ucn);

	/**
	 * Gets the territory div cust rel with null target.
	 *
	 * @return the territory div cust rel with null target
	 */
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRelWithNullTarget();

	/**
	 * Gets the territory div cust rel with wwid and null target.
	 *
	 * @param wwid
	 *           the wwid
	 * @return the territory div cust rel with wwid and null target
	 */
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRelWithWwidAndNullTarget(final String wwid);

	/**
	 * Gets the territory n customer rels.
	 *
	 * @param terriotyPKs
	 *           the unit
	 * @return the territory n customer rels
	 */
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryNCustomerRels(final Set<String> terriotyPKs);
	
	}
