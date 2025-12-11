/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.impl.DefaultAddressService;
import de.hybris.platform.util.Config;


import java.util.Collections;
import java.util.List;

import jakarta.annotation.Resource;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnJAddressDao;
import com.jnj.core.dao.b2bunit.JnjGTB2BUnitDao;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnJAddressService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnJCommonUtil;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnJAddressService extends DefaultAddressService implements JnJAddressService
{
	private static final Logger LOG = Logger.getLogger(DefaultJnJAddressService.class);
	@Autowired
	private UserService userService;
	
	@Autowired
	private JnjGTB2BUnitDao jnjGTB2BUnitDao;
	
	@Resource(name = "jnjB2BUnitService")
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	protected JnJAddressDao jnJAddressDao;

	/*protected JnJAddressDao getJnJAddressDao()
	{
		return jnJAddressDao;
	}

	@Required
	public void setJnJAddressDao(final JnJAddressDao jnJAddressDao)
	{
		this.jnJAddressDao = jnJAddressDao;
	}*/

	/**
	 * This method is used to get Address model on the basis of ID.
	 * 
	 * @param jnjId
	 *           the id
	 * @return the address by id
	 */
	@Override
	public AddressModel getAddressById(final String jnjId)
	{
		return jnJAddressDao.getAddressById(jnjId);
	}

	@Override
	public AddressModel getAddressById(final String jnjId, final boolean ignoreLeadingZeors)
	{
		return jnJAddressDao.getAddressById(jnjId, ignoreLeadingZeors);
	}

	@Override
	public AddressModel getAddressById(final String jnjId, final boolean ignoreLeadingZero, final boolean checkActiveFlag)
	{
		return jnJAddressDao.getAddressById(jnjId, ignoreLeadingZero, checkActiveFlag);
	}

	/**
	 * Gets the address based on Id and owner's item type
	 * 
	 * @param jnjid
	 *           the id
	 * 
	 * @return the address based on Id and owner's item type
	 */
	@Override
	public List<AddressModel> getAddressByIdandOnwerType(final String jnjid)
	{
		return jnJAddressDao.getAddressByIdandOnwerType(jnjid);
	}

	@Override
	public List<AddressModel> getAddressByIdandOnwerType(final String jnjid, final boolean ignoreleadingZeros,
			final boolean checkActiveFlag)
	{
		// this flag is to turn OFF/ON below code when ever needed.
		//NA - Delta changes
		if (Config.getBoolean("jnj.gt.dropship.shipping.address.flag", true)) {
			JnJB2BUnitModel jnjB2BUnitModel = jnjGTB2BUnitDao.getB2BUnitByUid(jnjid);
			if (jnjB2BUnitModel != null) {
				AddressModel deliveryAddress = jnjGTB2BUnitService.getShippingAddress(jnjB2BUnitModel);
				if (deliveryAddress != null & deliveryAddress.getJnJAddressId() != null && deliveryAddress.getJnJAddressId().equals(jnjB2BUnitModel.getUid())) {
					return Collections.singletonList(deliveryAddress);
				}
			}
		} 

		return jnJAddressDao.getAddressByIdandOnwerType(jnjid, ignoreleadingZeros, checkActiveFlag);
	}

	/**
	 * This method is used to get Address model on the basis of Pk.
	 * 
	 * @param pkString
	 *           the id
	 * @return the address by pk
	 */
	@Override
	public AddressModel getAddressByPK(final String pkString)
	{
		return jnJAddressDao.getAddressByPK(pkString);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.services.JnJAddressService#getPagedAddresses(de.hybris.platform.commerceservices.search.pagedata.
	 * PageableData)
	 */
	@Override
	public SearchPageData<AddressModel> getPagedAddresses(final PageableData pageableData)
	{
		final UserModel userModel = userService.getCurrentUser();
		if (userModel instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel jnjCustomer = (JnJB2bCustomerModel) userModel;
			final JnJB2BUnitModel unitModel = (JnJB2BUnitModel) jnjCustomer.getDefaultB2BUnit();
			return jnJAddressDao.findPagedAddresses(pageableData, unitModel.getPk().toString());
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("User is not of JnJB2bCustomerModel " + userModel.getUid());
			}
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AddressModel getAddressForJnjAddressId(final B2BUnitModel b2bUnitModel, final String jnjAddressId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getAddressForJnjAddressId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		for (final AddressModel addressModel : b2bUnitModel.getAddresses())
		{
			if (addressModel.getJnJAddressId().equals(jnjAddressId))
			{
				return addressModel;
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getAddressForJnjAddressId()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return null;
	}
}
