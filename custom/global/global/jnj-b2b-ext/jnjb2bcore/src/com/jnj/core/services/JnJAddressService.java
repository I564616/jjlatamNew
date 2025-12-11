/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.user.AddressService;

import java.util.List;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnJAddressService extends AddressService
{


	/**
	 * Gets the address by id.
	 * 
	 * @param jnjid
	 *           the id
	 * @return the address by id
	 */
	public AddressModel getAddressById(String jnjid);

	/**
	 * Gets list of {@link SearchPageData} for pagination given the required pagination parameters with.
	 * 
	 * @param pageableData
	 *           Pagination information
	 * @return List of paginated {@link AddressModel} objects {@link PageableData}
	 */
	SearchPageData<AddressModel> getPagedAddresses(PageableData pageableData);


	/**
	 * Gets the addressModel by pk of the address Model.
	 * 
	 * @param pkString
	 *           the pk string
	 * @return the addressModel
	 */
	AddressModel getAddressByPK(String pkString);

	/**
	 * Gets the address based on Id and owner's item type
	 * 
	 * @param jnjid
	 *           the id
	 * @return the address based on Id and owner's item type
	 */
	public List<AddressModel> getAddressByIdandOnwerType(final String jnjid);

	/**
	 * Gets the address model on the basis of jnj address id.
	 * 
	 * @param unit
	 *           the unit
	 * @param jnjid
	 *           the id
	 * @return the address for jnj address id
	 */
	public AddressModel getAddressForJnjAddressId(final B2BUnitModel unit, final String jnjid);


	/**
	 * Gets the address by id.
	 * 
	 * @param jnjId
	 *           the jnj id
	 * @param ignoreLeadingZeors
	 *           the ignore leading zeors
	 * @return the address by id
	 */
	public AddressModel getAddressById(final String jnjId, final boolean ignoreLeadingZeors);

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

	public List<AddressModel> getAddressByIdandOnwerType(final String jnjid, final boolean ignoreleadingZeros,
			final boolean checkActiveFlag);
}
