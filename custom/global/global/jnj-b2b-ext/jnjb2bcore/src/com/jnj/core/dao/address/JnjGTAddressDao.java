/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.address;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.List;


/**
 * The JnjGTAddressDao class contains declaration all those method which are dealing with na address model and their
 * definition in JnjGTAddressDaoImpl.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTAddressDao
{
	/**
	 * Gets the address by idand source sys id.
	 * 
	 * @param jnjId
	 *           the jnj id
	 * @param sourceSysId
	 *           the source sys id
	 * @return the address by idand source sys id
	 */
	public List<AddressModel> getAddressByIdandSourceSysId(final String jnjId, final String sourceSysId);
	//3088
	public List<AddressModel> getSearchShippingAddress(final String searchitem,final String jnjUnitId);
	
	public List<AddressModel> getSearchBillingAddress(final String searchItem,final String jnjUnitId);
}
