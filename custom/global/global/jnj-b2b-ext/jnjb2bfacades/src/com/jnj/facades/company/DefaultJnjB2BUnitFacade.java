/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.company;



import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.impl.DefaultB2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.b2bcommercefacades.company.util.B2BCompanyUtils;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnJAddressService;
import com.jnj.facades.data.JnjAddressData;
import com.jnj.facades.order.converters.populator.JnjAddressPopulator;


/**
 * TODO:<Neeraj-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjB2BUnitFacade extends DefaultB2BUnitFacade implements JnjB2BUnitFacade
{

	private static final String USERGROUP_PREFIX = "usermanagement.roles.group";
	@Autowired
	private JnjAddressPopulator jnjAddressPopulator;
	@Autowired
	private UserService userService;
	@Autowired
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private Converter<UserGroupModel, B2BUserGroupData> userGroupConverter;
	@Autowired
	@Qualifier(value = "jnjGTAddressService")
	private JnJAddressService jnJAddressService;
	@Autowired
	private Converter<AddressModel, AddressData> addressConverter;

	@Override
	public List<AddressData> getShippingAddresses()
	{
		B2BUnitModel unitModel = null;
		List<AddressData> shippingAddressList = null;

		final UserModel user = getUserService().getCurrentUser();
		if (user instanceof B2BCustomerModel)
		{
			unitModel = ((B2BCustomerModel) user).getDefaultB2BUnit();
		}

		if (null != unitModel)
		{
			final Set<PrincipalModel> branch = unitModel.getMembers();
			final Set<AddressModel> addresses = new HashSet<AddressModel>();
			addresses.addAll(unitModel.getAddresses());
			for (final PrincipalModel unit : branch)
			{
				if (unit instanceof B2BUnitModel || unit instanceof JnJB2BUnitModel)
				{
					final B2BUnitModel b2bUnit = (B2BUnitModel) unit;
					if (CollectionUtils.isNotEmpty(b2bUnit.getAddresses()))
					{
						addresses.addAll(b2bUnit.getAddresses());
					}
				}
			}
			shippingAddressList = new ArrayList<AddressData>();
			for (final AddressModel address : addresses)
			{
				if (address.getShippingAddress().booleanValue())
				{
					final JnjAddressData addressData = new JnjAddressData();
					jnjAddressPopulator.populate(address, addressData);
					shippingAddressList.add(addressData);
				}
			}
		}
		return shippingAddressList;
	}

	@Override
	public List<AddressData> getBillingAddresses()
	{
		B2BUnitModel unitModel = null;
		List<AddressData> billingAddressList = null;

		final UserModel user = getUserService().getCurrentUser();
		if (user instanceof B2BCustomerModel)
		{
			unitModel = ((B2BCustomerModel) user).getDefaultB2BUnit();
		}

		if (null != unitModel)
		{
			final Set<PrincipalModel> branch = unitModel.getMembers();
			final Set<AddressModel> addresses = new HashSet<AddressModel>();
			addresses.addAll(unitModel.getAddresses());
			for (final PrincipalModel unit : branch)
			{
				if (unit instanceof B2BUnitModel || unit instanceof JnJB2BUnitModel)
				{
					final B2BUnitModel b2bUnit = (B2BUnitModel) unit;
					if (CollectionUtils.isNotEmpty(b2bUnit.getAddresses()))
					{
						addresses.addAll(b2bUnit.getAddresses());
					}
				}
			}
			billingAddressList = new ArrayList<AddressData>();
			for (final AddressModel address : addresses)
			{
				if (address.getBillingAddress().booleanValue())
				{
					final JnjAddressData addressData = new JnjAddressData();
					jnjAddressPopulator.populate(address, addressData);
					billingAddressList.add(addressData);
				}
			}
		}
		return billingAddressList;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.company.JnjB2BUnitFacade#getUserGroupsForUnit(java.lang.String) /* (non-Javadoc)
	 * 
	 * @see com.jnj.facades.company.JnjB2BUnitFacade#getUserGroupsForUnit(java.lang.String)
	 */

	//CR 31439 Chnage
	@Override
	public List<B2BUserGroupData> getUserGroupsForUnit(final String editableuser)
	{
		final List<B2BUserGroupData> userGroupDataList = new ArrayList<B2BUserGroupData>();
		final UserModel currentuser = userService.getCurrentUser();

		if (currentuser instanceof JnJB2bCustomerModel)
		{
			final B2BUnitModel unitModel = ((JnJB2bCustomerModel) currentuser).getCreatorB2BUnit();
			if (unitModel instanceof JnJB2BUnitModel)
			{
				final String indicator = ((JnJB2BUnitModel) unitModel).getIndicator().concat(
						configurationService.getConfiguration().getString(USERGROUP_PREFIX));

				final UserGroupModel compositeGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID(indicator,
						UserGroupModel.class);

				final Set<PrincipalGroupModel> compositeSubGroups = compositeGroupModel.getGroups();

				for (final PrincipalGroupModel subGroup : compositeSubGroups)
				{
					if (subGroup instanceof UserGroupModel)
					{
						final B2BUserGroupData userGroupData = new B2BUserGroupData();
						userGroupConverter.convert((UserGroupModel) subGroup, userGroupData);
						if (subGroup.getUid().equals("useradminGroup"))
						{
							if (editableuser != null && currentuser.getUid().equals(editableuser))
							{
								userGroupDataList.add(userGroupData);
							}
						}
						else
						{

							userGroupDataList.add(userGroupData);
						}
					}
				}
			}
		}

		return userGroupDataList;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.company.JnjB2BUnitFacade#getAddressesOfUnit()
	 */
	@Override
	public SearchPageData<AddressData> getPagedAddresses(final PageableData pageableData)
	{
		final SearchPageData<AddressModel> addresses = jnJAddressService.getPagedAddresses(pageableData);

		return B2BCompanyUtils.convertPageData(addresses, addressConverter);

	}
}
