/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.interceptors;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.interceptor.JnjGTB2BCustomerValidateInterceptor;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import java.util.function.Predicate;

/**
 *
 */
public class JnjLaB2BCustomerValidateInterceptor implements ValidateInterceptor<JnJB2bCustomerModel>
{
	private static final Logger LOG = Logger.getLogger(JnjGTB2BCustomerValidateInterceptor.class);
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	private UserService userService;
	private L10NService l10NService;

	private final Class currentClass = JnjLaB2BCustomerValidateInterceptor.class;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.interceptor.ValidateInterceptor#onValidate(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onValidate(final JnJB2bCustomerModel model, final InterceptorContext arg1) throws InterceptorException
	{
		final String METHOD_NAME = "Latam onValidate()";
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, currentClass);
		final JnJB2bCustomerModel customer = model;
		final B2BUnitModel parentUnit = this.b2bUnitService.getParent(customer);

		if (parentUnit == null)
		{
			throw new InterceptorException(getL10NService().getLocalizedString("error.b2bcustomer.b2bunit.missing"));
		}

		final Set groups = new HashSet((customer.getGroups() != null) ? customer.getGroups() : Collections.EMPTY_SET);

		groups.removeIf(Predicate.not(PredicateUtils.instanceofPredicate(B2BUnitModel.class)));
		if (customer.getApprovers() != null)
		{
			final Set<B2BCustomerModel> approvers = new HashSet(customer.getApprovers());
			if (CollectionUtils.isNotEmpty(approvers))
			{
				final UserGroupModel b2bApproverGroup = this.userService.getUserGroupForUID("b2bapprovergroup");

				for (final B2BCustomerModel approver : approvers)
				{
					if (this.userService.isMemberOfGroup(approver, b2bApproverGroup))
					{
						continue;
					}
					approvers.remove(approver);
					LOG.warn("Removed approver " + approver.getUid() + " from customer " + customer.getUid()
							+ "due to lack of membership of group b2bapprovergroup ");

				}

				customer.setApprovers(approvers);
			}
		}

		makeSureThatB2BUnitIsInGroups(customer, parentUnit);
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, currentClass);
	}


	protected void makeSureThatB2BUnitIsInGroups(final B2BCustomerModel customer, final B2BUnitModel parentUnit)
	{
		final String METHOD_NAME = "Latam makeSureThatB2BUnitIsInGroups()";
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, currentClass);
		if ((parentUnit == null) || (isB2BUnitInGroupList(customer.getGroups(), parentUnit)))
		{
			return;
		}
		final Set groups = new HashSet(new HashSet());
		groups.add(parentUnit);
		customer.setGroups(groups);
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, currentClass);
	}

	protected boolean isB2BUnitInGroupList(final Set<PrincipalGroupModel> groups, final B2BUnitModel parentUnit)
	{
		final String METHOD_NAME = "Latam isB2BUnitInGroupList()";
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, currentClass);
		if ((groups == null) || (groups.isEmpty()))
		{
			return false;
		}

		for (final PrincipalGroupModel group : groups)
		{

			if ((group instanceof B2BUnitModel) || (group instanceof UserGroupModel) || (group instanceof CompanyModel))
			{
				return true;
			}
		}
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, currentClass);
		return false;
	}

	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return this.b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	public UserService getUserService()
	{
		return this.userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public L10NService getL10NService()
	{
		return this.l10NService;
	}

	public void setL10NService(final L10NService l10NService)
	{
		this.l10NService = l10NService;
	}

}
