/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.services.usergroup;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.b2b.company.impl.DefaultB2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 *
 */
public class JnjLaB2BCommerceB2BUserGroupService extends DefaultB2BCommerceB2BUserGroupService
{

	@Autowired
	protected ModelService modelService;

	@Override
	public Set<PrincipalGroupModel> updateUserGroups(final Collection<String> availableUserGroups,
			final Collection<String> selectedUserGroups, final B2BCustomerModel customerModel)
	{

		JnjGTCoreUtil.logInfoMessage("updateUserGroups", "updateUserGroups",
				"Entered into updateUserGroups mwthod ofJnjLaB2BCommerceB2BUserGroupService",
				JnjLaB2BCommerceB2BUserGroupService.class);

		selectedUserGroups.removeIf(Objects::isNull);

		final String effectiveGroups = Config.getParameter(Jnjlab2bcoreConstants.KEY_LATAM_EFFECTIVE_GROUPS);

		final List<String> effectivegroups = Arrays.asList(effectiveGroups.split(Jnjb2bCoreConstants.CONST_COMMA));
		final Set<PrincipalGroupModel> customerGroupsto = customerModel.getGroups();
		final Set<PrincipalGroupModel> customerGroups = new HashSet<PrincipalGroupModel>();

		if (null != customerGroupsto)
		{
			for (final PrincipalGroupModel usg : customerGroupsto)
			{

				final UserGroupModel userGroupModel = getUserService().getUserGroupForUID(usg.getUid());

				if (null != effectivegroups)
				{
					if (!(effectivegroups.contains(userGroupModel.getUid())))
					{
						customerGroups.add(usg);
					}
				}



			}
		}




		// If you pass in NULL then nothing will happen
		if (selectedUserGroups != null)
		{
			for (final String group : selectedUserGroups)
			{
				// add a group
				final UserGroupModel userGroupModel = getUserService().getUserGroupForUID(group);

				customerGroups.add(userGroupModel);

			}

			customerModel.setGroups(customerGroups);
			modelService.save(customerModel);

		}

		return customerGroups;
	}

}
