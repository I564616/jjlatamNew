/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.user.daos.AddressDao;

import java.util.List;



/**
 * TODO:<Komal - class level comments are missing>
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnJAddressDao extends AddressDao
{

	/**
	 * Gets the address by jnjid.
	 * 
	 * @param jnjId
	 *           the id
	 * @return the address by jnjid
	 */
	public AddressModel getAddressById(final String jnjId);

	/**
	 * Finds the addresses for a given unit.
	 * 
	 * @param pageableData
	 *           - the pagableData
	 * @param unitId
	 *           the unit id
	 * @return the list of addressModel SearchPageData
	 */
	SearchPageData<AddressModel> findPagedAddresses(final PageableData pageableData, String unitId);

	/**
	 * Gets the addressModel by pk of the address Model.
	 * 
	 * @param pkString
	 *           the pk string
	 * @return the addressModel
	 */
	public AddressModel getAddressByPK(final String pkString);

	/**
	 * Gets address based on Id and owner's item's type
	 * 
	 * @param jnjId
	 *           the id
	 * 
	 * @return the address based on Id and owner's item's type
	 */
	public List<AddressModel> getAddressByIdandOnwerType(final String jnjId);

	/**
	 * Gets the address by id.
	 * 
	 * @param jnjId
	 *           the jnj id
	 * @param ignoreleadingZeros
	 *           the ignoreleading zeros
	 * @return the address by id
	 */
	public AddressModel getAddressById(final String jnjId, final boolean ignoreleadingZeros);

	/**
	 * Gets the address by id
	 * 
	 * @param jnjId
	 *           the jnjAddressId
	 * @param ignoreleadingZeros
	 *           the ignore leading zeros
	 * @param checkActiveFlag
	 *           true will be passed in case only active addresses are required
	 * @return the address by id
	 */
	public AddressModel getAddressById(final String jnjId, final boolean ignoreleadingZeros, final boolean checkActiveFlag);


	public List<AddressModel> getAddressByIdandOnwerType(final String jnjId, final boolean ignoreleadingZeros,
			final boolean checkActiveFlag);
}
