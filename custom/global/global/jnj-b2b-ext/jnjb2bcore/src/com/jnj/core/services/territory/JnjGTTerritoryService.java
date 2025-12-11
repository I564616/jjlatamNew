/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.services.territory;

import de.hybris.platform.core.PK;

import java.util.List;
import java.util.Set;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivProdMappingModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;


/**
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTTerritoryService
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
	public JnjGTTerritoryDivisonModel getTerritoryDivisonByUid(String id, String sourceId);

	/**
	 * Gets the territory div cust rel.
	 *
	 * @param JnjGTTerritoryDivCustRelModel
	 *           the jnj na territory div cust rel model
	 * @return the territory div cust rel
	 */
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRel(JnjGTTerritoryDivCustRelModel JnjGTTerritoryDivCustRelModel);

	/**
	 * Save territory divison model.
	 *
	 * @param JnjGTTerritoryDivisonModel
	 *           the jnj na territory divison model
	 * @return true, if successful
	 */
	public boolean saveTerritoryDivisonModel(final JnjGTTerritoryDivisonModel JnjGTTerritoryDivisonModel);

	/**
	 * Save territory divison cust model.
	 *
	 * @param JnjGTTerritoryDivCustRelModel
	 *           the jnj na territory div cust rel model
	 * @return true, if successful
	 */
	public boolean saveTerritoryDivisonCustModel(final JnjGTTerritoryDivCustRelModel JnjGTTerritoryDivCustRelModel);

	/**
	 * Gets the all jnj na territory div cust rel model.
	 *
	 * @return the all jnj na territory div cust rel model
	 */
	public List<JnjGTTerritoryDivCustRelModel> getAllJnjGTTerritoryDivCustRelModel();

	/**
	 * Gets the all jnj na territory div model.
	 *
	 * @param sourceSystemId
	 *           the source system id
	 * @return the all jnj na territory div model
	 */
	List<JnjGTTerritoryDivisonModel> getAllJnjGTTerritoryDivModel(String sourceSystemId);


	public boolean removeTerritoryDivisonModel(final JnjGTTerritoryDivisonModel JnjGTTerritoryDivisonModel);

	/**
	 * Removes the territory divison cust rel model.
	 *
	 * @param JnjGTTerritoryDivCustRelModel
	 *           the jnj na territory div cust rel model
	 * @return true, if successful
	 */
	boolean removeTerritoryDivisonCustRelModel(List<JnjGTTerritoryDivCustRelModel> JnjGTTerritoryDivCustRelModel);

	/**
	 * Save territory divison prod model.
	 *
	 * @param JnjGTTerritoryDivProdMappingModel
	 *           the jnj na territory div prod mapping model
	 * @return true, if successful
	 */
	public boolean saveTerritoryDivisonProdModel(final JnjGTTerritoryDivProdMappingModel JnjGTTerritoryDivProdMappingModel);

	/**
	 * Gets the jnj na territory div prod mapping model.
	 *
	 * @param JnjGTTerritoryDivProdMappingModel
	 *           the jnj na territory div prod mapping model
	 * @return the jnj na territory div prod mapping model
	 */
	public JnjGTTerritoryDivProdMappingModel getJnjGTTerritoryDivProdMappingModel(
			JnjGTTerritoryDivProdMappingModel JnjGTTerritoryDivProdMappingModel);

	/**
	 * Removes the jnj na territory div prod mapping model.
	 *
	 * @param JnjGTTerritoryDivProdMappingModel
	 *           the jnj na territory div prod mapping model
	 * @return true, if successful
	 */
	public boolean removeJnjGTTerritoryDivProdMappingModel(JnjGTTerritoryDivProdMappingModel JnjGTTerritoryDivProdMappingModel);

	/**
	 * Removes the invaild territory divison model.
	 */
	void removeInvaildTerritoryDivisonModel();

	/**
	 * Removes the invaild territory divison cust model.
	 */
	void removeInvaildTerritoryDivisonCustModel();

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
	 * Align customer with territory.
	 */
	public void alignCustomerWithTerritory();

	public void alignCustomerWithTerritoryWithWwid(final String wwid, final JnJB2bCustomerModel customer);

	/**
	 * Gets the territory divison model.
	 *
	 * @param JnjGTTerritoryDiv
	 *           the jnj na territory div
	 * @return the territory divison model
	 */
	public JnjGTTerritoryDivisonModel getTerritoryDivisonModel(JnjGTTerritoryDivisonModel JnjGTTerritoryDiv);

	/**
	 * Gets the territory n customer rels.
	 *
	 * @param territoryPKSet
	 *           the unit
	 * @return the territory n customer rels
	 */
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryNCustomerRels(final Set<String> territoryPKSet);
	
}
