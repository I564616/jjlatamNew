/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.company.impl;

import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
//import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facades.company.DefaultJnjB2BUnitFacade;
import com.jnj.facades.data.JnjAddressData;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.converters.populator.address.JnjGTAddressPopulator;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.territory.JnjGTTerritoryService;

import org.apache.commons.lang3.StringUtils;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.b2b.services.B2BCartService;
/**
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTB2BUnitFacade extends DefaultJnjB2BUnitFacade implements JnjGTB2BUnitFacade
{

	/*@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;*/

	@Resource(name = "jnjB2BUnitService")
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	public JnjGTAddressPopulator jnjGTAddressPopulator;

	@Resource(name = "GTCustomerService")
	private JnjGTCustomerService jnjGTCustomerService;
	
	 @Autowired
	 private UserService userService;

	@Override
	public String getSourceSystemIdForUnit()
	{
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
		return currentCustomer.getCurrentB2BUnit().getSourceSysId();
	}

	@Override
	public String getAccountType(JnJB2BUnitModel unitModel)
	{
		if (null == unitModel)
		{
			unitModel = jnjGTB2BUnitService.getCurrentB2BUnit();
		}
		return unitModel.getIndicator();
	}

	@Override
	public Set<String> getOrderTypesForAccount()
	{
		return jnjGTB2BUnitService.getOrderTypesForAccount();
	}

	@Override
	public AddressData getContactAddress()
	{
		JnjGTAddressData contactAddData = null;
		final AddressModel contactAddress = jnjGTB2BUnitService.getContactAddress(null);
		if (null != contactAddress)
		{
			contactAddData = new JnjGTAddressData();
			jnjGTAddressPopulator.populate(contactAddress, contactAddData);
		}
		return contactAddData;
	}

	@Override
	public List<AddressData> getShippingAddresses()
	{
		List<AddressData> shippingAddressList = null;
		final Collection<AddressModel> addresses = jnjGTB2BUnitService.getShippingAddresses(null);
		if (CollectionUtils.isNotEmpty(addresses))
		{
			shippingAddressList = new ArrayList<AddressData>();
			for (final AddressModel address : addresses)
			{
				if (address.getShippingAddress().booleanValue() && address.isActive())
				{
					final JnjGTAddressData addressData = new JnjGTAddressData();
					jnjGTAddressPopulator.populate(address, addressData);
					shippingAddressList.add(addressData);
				}
			}
		}
		return shippingAddressList;
	}

	@Override
	public List<AddressData> getBillingAddresses()
	{
		List<AddressData> billingAddressList = null;
		final Collection<AddressModel> addresses = jnjGTB2BUnitService.getBillingAddresses(null);
		if (CollectionUtils.isNotEmpty(addresses))
		{
			billingAddressList = new ArrayList<AddressData>();
			for (final AddressModel address : addresses)
			{
				if (address.getBillingAddress().booleanValue() && address.isActive())
				{
					final JnjGTAddressData addressData = new JnjGTAddressData();
					jnjGTAddressPopulator.populate(address, addressData);
					billingAddressList.add(addressData);
				}
			}
		}
		return billingAddressList;
	}
	@Override
	public Collection<AddressData> getAllDropShipAccounts()
	{
		final List<AddressData> dropShipAddressList = new ArrayList<AddressData>();

		final Collection<AddressModel> dropShipAccounts = jnjGTB2BUnitService.getAllDropShipAccounts();


		for (final AddressModel address : dropShipAccounts)
		{
			if (null != address && address.getShippingAddress().booleanValue())
			{
				final JnjGTAddressData addressData = new JnjGTAddressData();
				jnjGTAddressPopulator.populate(address, addressData);
				dropShipAddressList.add(addressData);
			}
		}
		return dropShipAddressList;
	}

	@Override
	public AddressData getShippingAddress()
	{
		final JnjGTAddressData shippingAddData = new JnjGTAddressData();
		final AddressModel shippingAddress = jnjGTB2BUnitService.getShippingAddress(null);
		jnjGTAddressPopulator.populate(shippingAddress, shippingAddData);
		return shippingAddData;
	}


	@Override
	public boolean isCustomerInternationalAff()
	{
		return jnjGTB2BUnitService.isCustomerInternationalAff();
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	public void setJnjGTB2BUnitService(JnjGTB2BUnitService jnjGTB2BUnitService) {
		this.jnjGTB2BUnitService = jnjGTB2BUnitService;
	}
	
	public UserService getUserService()	{
		return userService;
	}

	@Override
	public Set<String> getAvailableOrderTypes(List<B2BUnitData> b2bUnitList) {
		return jnjGTB2BUnitService.getAvailableOrderTypes(b2bUnitList);
	}
	//3088 start
	public List<AddressData> getSearchShippingAddress(final String searchItem)
	{
		List<AddressData> shippingAddressList = null;
		final Collection<AddressModel> addresses = jnjGTB2BUnitService.getSearchShippingAddress(searchItem);
		if (CollectionUtils.isNotEmpty(addresses))
		{
			shippingAddressList = new ArrayList<AddressData>();
			for (final AddressModel address : addresses)
			{
				if (address.getShippingAddress().booleanValue() && address.isActive())
				{
					final JnjGTAddressData addressData = new JnjGTAddressData();
					jnjGTAddressPopulator.populate(address, addressData);
					shippingAddressList.add(addressData);
				}
			}
		}
		return shippingAddressList;

	}
	
	public List<AddressData> getSearchBillingAddress(final String searchItem)
	{
		List<AddressData> billingAddressList = null;
		final Collection<AddressModel> addresses = jnjGTB2BUnitService.getSearchBillingAddress(searchItem);
		if (CollectionUtils.isNotEmpty(addresses))
		{
			billingAddressList = new ArrayList<AddressData>();
			for (final AddressModel address : addresses)
			{
				if (address.getBillingAddress().booleanValue() && address.isActive())
				{
					final JnjGTAddressData addressData = new JnjGTAddressData();
					jnjGTAddressPopulator.populate(address, addressData);
					billingAddressList.add(addressData);
				}
			}
		}
		return billingAddressList;

	}
	
	//3088-end
	
}
