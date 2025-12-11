/**
 * 
 */
package com.jnj.core.interceptor;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
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
import java.util.function.Predicate;

/**
 * @author komal.sehgal
 * 
 */
public class JnjGTB2BCustomerValidateInterceptor implements ValidateInterceptor
{
	protected static final Logger LOG = Logger.getLogger(JnjGTB2BCustomerValidateInterceptor.class);
	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	protected UserService userService;
	protected L10NService l10NService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.ValidateInterceptor#onValidate(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onValidate(final Object model, final InterceptorContext arg1) throws InterceptorException
	{
		if (!(model instanceof B2BCustomerModel))
		{
			return;
		}
		final B2BCustomerModel customer = (B2BCustomerModel) model;
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
					LOG.warn(String.format("Removed approver %s from customer %s due to lack of membership of group %s", new Object[]
					{ approver.getUid(), customer.getUid(), "b2bapprovergroup" }));

				}

				customer.setApprovers(approvers);
			}
		}

		makeSureThatB2BUnitIsInGroups(customer, parentUnit);
	}


	protected void makeSureThatB2BUnitIsInGroups(final B2BCustomerModel customer, final B2BUnitModel parentUnit)
	{
		if ((parentUnit == null) || (isB2BUnitInGroupList(customer.getGroups(), parentUnit)))
		{
			return;
		}
		final Set groups = new HashSet(new HashSet());
		groups.add(parentUnit);
		customer.setGroups(groups);
	}


	protected boolean isB2BUnitInGroupList(final Set<PrincipalGroupModel> groups, final B2BUnitModel parentUnit)
	{
		if ((groups == null) || (groups.isEmpty()))
		{
			return false;
		}

		for (final PrincipalGroupModel group : groups)
		{
			if ((group instanceof B2BUnitModel) && (group.getUid().equals(parentUnit.getUid())))
			{
				return true;
			}
		}
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
