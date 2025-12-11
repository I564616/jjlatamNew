/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnjCustomerDao;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjCustomerService;
import com.jnj.core.util.JnJCommonUtil;

//import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.event.RegisterEvent;
//import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
//import de.hybris.platform.b2bacceleratorservices.dao.PagedB2BCustomerDao;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCustomerService extends DefaultCustomerAccountService implements JnjCustomerService
{
	private static final Logger LOG = Logger.getLogger(DefaultJnjCustomerService.class);
	protected static final String USERGROUP_PREFIX = "usermanagement.roles.group";
	protected static final String INTERVAL_LIST_FOR_EMAIL = "user.email.password.expire.interval";

	@Autowired
	protected ConfigurationService configurationService;

	@Autowired
	JnjCustomerDao jnjCustomerDao;

	@Autowired
	protected UserService userService;

	protected PagedGenericDao<B2BCustomerModel> pagedB2BCustomerDao;

	@Autowired
	protected B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	/*@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;*/
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public JnjCustomerDao getJnjCustomerDao() {
		return jnjCustomerDao;
	}

	public UserService getUserService() {
		return userService;
	}

	public B2BCommerceB2BUserGroupService getB2BCommerceB2BUserGroupService() {
		return b2BCommerceB2BUserGroupService;
	}

	/*public CompanyB2BCommerceService getCompanyB2BCommerceService() {
		return companyB2BCommerceService;
	}*/
	

	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	/**
	 * @return the pagedB2BCustomerDao
	 */
	public PagedGenericDao<B2BCustomerModel> getPagedB2BCustomerDao() {
		return pagedB2BCustomerDao;
	}

	/**
	 * @param pagedB2BCustomerDao
	 *           the pagedB2BCustomerDao to set
	 */
	public void setPagedB2BCustomerDao(final PagedGenericDao<B2BCustomerModel> pagedB2BCustomerDao) {
		this.pagedB2BCustomerDao = pagedB2BCustomerDao;
	}

	@Override
	public void registerCustomer(final JnJB2bCustomerModel customerModel, final String password) throws DuplicateUidException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("customerModel", customerModel);

		generateCustomerId(customerModel);
		if (password != null)
		{
			getUserService().setPassword(customerModel, password, getPasswordEncoding());
		}
		internalSaveCustomer(customerModel);
	}

	@Override
	public void register(final JnJB2bCustomerModel customerModel, final String password) throws DuplicateUidException
	{
		registerCustomer(customerModel, password);
		getEventService().publishEvent(initializeEvent(new RegisterEvent(), customerModel));
	}

	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException
	{
		Assert.hasText(token, "The field [token] cannot be empty");
		Assert.hasText(newPassword, "The field [newPassword] cannot be empty");

		final SecureToken data = getSecureTokenService().decryptData(token);
		if (getTokenValiditySeconds() > 0L)
		{
			final long delta = new Date().getTime() - data.getTimeStamp();
			if (delta / 1000L > getTokenValiditySeconds())
			{
				throw new IllegalArgumentException("token expired");
			}
		}
		final CustomerModel customer = getUserService().getUserForUID(data.getData(), CustomerModel.class);
		if (customer == null)
		{
			throw new IllegalArgumentException("user for token not found");
		}
		if (!(token.equals(customer.getToken())))
		{
			throw new TokenInvalidatedException();
		}
		customer.setToken(null);
		getModelService().save(customer);
		getUserService().setPassword(data.getData(), newPassword, getPasswordEncoding());
	}

	@Override
	public SearchPageData<B2BCustomerModel> getPagedCustomersCaseInsenitiveByGroupMembership(final PageableData pageableData,
			final String... userGroupUid)
	{
		return jnjCustomerDao.findPagedCustomersCaseInsenitiveByGroupMembership("byName", pageableData, userGroupUid);
	}


	@Override
	public Map<String, String> getAccountsMap(final boolean addCurrentB2BUnit)
	{
		final Map<String, String> associatedUnits = new HashMap<String, String>();

		// If the getcurrentUser method return the object which is an instance of JnJB2bCustomerModel then enters in the if block.
		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)		 
		{
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();

			final UserGroupModel otherOrderGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID(
					Config.getParameter(Jnjb2bCoreConstants.OTHER_GROUP_USER_ROLES), UserGroupModel.class);
			// if the current user have list of all groups and it contains the other order groups then enter in the if block.

			if (currentUser.getAllGroups().contains(otherOrderGroup))
			{

				for (final PrincipalModel principalModel : currentUser.getGroups())
				{
					if (principalModel instanceof JnJB2BUnitModel)
					{
						associatedUnits.put(principalModel.getUid(), principalModel.getName());
						//final Set<PrincipalModel> principalMemberSet = ((JnJB2BUnitModel) principalModel).getMembers();
						setB2BUnitChilds(principalModel, associatedUnits);
					}

				}
				if (!associatedUnits.isEmpty())
				{
					if (!addCurrentB2BUnit)
					{
						associatedUnits.remove(currentUser.getDefaultB2BUnit().getUid());
					}
					associatedUnits.remove(Config.getParameter(Jnjb2bCoreConstants.BRAZIL_MASTER_B2BUNIT));
					associatedUnits.remove(Config.getParameter(Jnjb2bCoreConstants.MEXICO_MASTER_B2BUNIT));

				}
			}

		}


		return associatedUnits;
	}


	protected void setB2BUnitChilds(final PrincipalModel principalModel, final Map<String, String> associatedUnits)
	{
		final Set<PrincipalModel> principalMemberSet = ((JnJB2BUnitModel) principalModel).getMembers();
		for (final PrincipalModel principalMember : principalMemberSet)
		{
			if (principalMember instanceof JnJB2BUnitModel)
			{
				associatedUnits.put(principalMember.getUid(), principalMember.getName());
				if (CollectionUtils.isNotEmpty(((JnJB2BUnitModel) principalMember).getMembers()))
				{
					setB2BUnitChilds(principalMember, associatedUnits);
				}
			}

		}
	}

	@Override
	public boolean saveJnjB2bCustomer(final JnJB2bCustomerModel jnJB2bCustomerModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("JnjCustomerService" + Logging.HYPHEN + "saveJnjB2bCustomer()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		boolean modelSaved = false;
		if (null != jnJB2bCustomerModel)
		{
			try
			{
				getModelService().saveAll(jnJB2bCustomerModel);
				modelSaved = true;
			}
			catch (final ModelSavingException modelSavingException)
			{
				LOG.error("JnjCustomerService" + Logging.HYPHEN + "saveJnjB2bCustomer()" + Logging.HYPHEN
						+ "ModelSavingException occured while saving JnJB2bCustomerModel." + modelSavingException);
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("JnjCustomerService" + Logging.HYPHEN + "saveJnjB2bCustomer()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return modelSaved;
	}


	//CR 31439 Changes Ends

	@Override
	public Set<JnJB2bCustomerModel> getAllJnjUsers()
	{
		final Set<JnJB2bCustomerModel> returnValue = new HashSet<JnJB2bCustomerModel>();
		final List<JnJB2bCustomerModel> userList = jnjCustomerDao.getAllJnjUsers();
		final Calendar comparisionDate = Calendar.getInstance();
		String intervals = null;
		comparisionDate.add(Calendar.DATE, -(Jnjb2bCoreConstants.PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS)); // subtracting 90 days from the current date
		LOG.debug("PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS.." + Jnjb2bCoreConstants.PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS);
		if (userList != null && userList.size() > 0)
		{
			for (final JnJB2bCustomerModel iteratedUser : userList)
			{
				LOG.debug("PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS is " + Jnjb2bCoreConstants.PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS);
				LOG.debug("password chage date is " + iteratedUser.getPasswordChangeDate());
				LOG.debug("compare");
				if ((comparisionDate.getTime().compareTo(iteratedUser.getPasswordChangeDate()) <= 0))
				{

					//intervals = jnjCommonUtil.getMessageFromImpex(INTERVAL_LIST_FOR_EMAIL);
					intervals = Config.getParameter(INTERVAL_LIST_FOR_EMAIL);
					LOG.debug("intervals.."+intervals);
					if (StringUtils.isNotEmpty(intervals))
					{
						final SortedSet<Integer> set = new TreeSet<Integer>();
						final String emailIntervalInArray[] = intervals.split(Jnjb2bCoreConstants.CONST_COMMA);
						for (final String interval : emailIntervalInArray)
						{
							LOG.debug("Each Interval is " + interval);
							if (StringUtils.isNumeric(interval))
							{
								//jnjB2bCustomerFacade.getPagedCustomers(pageableData);
								try
								{
									final int intervalInt = Integer.valueOf(interval);
									if (intervalInt < Jnjb2bCoreConstants.PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS)
									{
										set.add(Integer.valueOf(interval));
									}
									else
									{
										throw new NumberFormatException();
									}
								}
								catch (final NumberFormatException numberFormatException)
								{
									LOG.error("JnjCustomerService" + Logging.HYPHEN + "saveJnjB2bCustomer()" + Logging.HYPHEN
											+ "Error in email interval " + interval + ". It must be a number which is less than "
											+ Jnjb2bCoreConstants.PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS);
									continue;
								}

								if (sendEmailtoUser(iteratedUser.getUid(), set, iteratedUser.getMailSentForInterval(),
										iteratedUser.getPasswordChangeDate()))
								{
									LOG.debug("sendEmailtoUser is true");
									returnValue.add(iteratedUser); // add to the list of user to be sent email.
								}
							}
						}
					}
				}
			}
		}

		return returnValue;
	}

	protected boolean sendEmailtoUser(final String id, final SortedSet<Integer> interval, Integer lastInterval,
			final Date passwordChangeDate)
	{
		final int noOfDaysToPasswordExpiry = Jnjb2bCoreConstants.PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS
				+ JnJCommonUtil.daysBetween(Calendar.getInstance().getTime(), passwordChangeDate);
		LOG.debug("sendEmailtoUser.. noOfDaysToPasswordExpiry.. " + noOfDaysToPasswordExpiry);
		LOG.debug("PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS.. " + Jnjb2bCoreConstants.PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS);
		if (lastInterval == null)
		{
			lastInterval = Integer.valueOf(noOfDaysToPasswordExpiry);

			if (interval.contains(lastInterval))
			{
				lastInterval = Integer.valueOf(noOfDaysToPasswordExpiry + 1); // so that the actual last interval gets added in the headSet
			}
		}
		if (interval.contains(Integer.valueOf(noOfDaysToPasswordExpiry))) // if the number of days to password expiry is equal to the last entry in heasSet then send mail
		{
			return true; //Send Email
		}
		LOG.debug("sendEmailtoUser.. false");
		return false;

	}

	/**
	 * @return
	 */
	public List<JnJB2bCustomerModel> getNewlyRegisteredUsers()
	{
		return jnjCustomerDao.getNewlyRegisteredUsers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<PrincipalGroupModel> getRolesForPortalUser()
	{
		final UserModel currentuser = userService.getCurrentUser();
		final Set<PrincipalGroupModel> roles = new HashSet<PrincipalGroupModel>();
		if (currentuser instanceof JnJB2bCustomerModel)
		{

			final JnJB2bCustomerModel jnjUser = (JnJB2bCustomerModel) currentuser;
			final B2BUnitModel unitModel = jnjUser.getDefaultB2BUnit();
			if (unitModel instanceof JnJB2BUnitModel)
			{
				final JnJB2BUnitModel jnJB2BUnitModel = (JnJB2BUnitModel) unitModel;
				final String indicator = jnJB2BUnitModel.getIndicator();
				if (StringUtils.isNotEmpty(indicator))
				{
					final String compositeGroupName = indicator + configurationService.getConfiguration().getString(USERGROUP_PREFIX);
					final UserGroupModel compositeGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID(compositeGroupName,
							UserGroupModel.class);
					final Set<PrincipalGroupModel> compositeSubGroups = compositeGroupModel.getGroups();
					final Set<PrincipalGroupModel> userGroups = currentuser.getAllGroups();
					roles.addAll(userGroups);
					roles.retainAll(compositeSubGroups);
					setRolesforCompositeGroupModel(roles, userGroups);
				}

			}

		}

		return roles;
	}
	
	/**
	 * Sets the rolesfor composite group model.
	 *
	 * @param roles
	 *           the roles
	 * @param userGroups
	 *           the user groups
	 */
	protected void setRolesforCompositeGroupModel(final Set<PrincipalGroupModel> roles, final Set<PrincipalGroupModel> userGroups)
	{
		final UserGroupModel salesThirdPartyGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID("salesThirdPartyGroup",
				UserGroupModel.class);
		final UserGroupModel salesRepGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID("salesRepGroup",
				UserGroupModel.class);
		final UserGroupModel expectedPriceGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID("expectedPriceGroup",
				UserGroupModel.class);
		final UserGroupModel otherOrderGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID("otherOrderGroup",
				UserGroupModel.class);
		final UserGroupModel priceChangeGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID("priceChangeGroup",
				UserGroupModel.class);
		final UserGroupModel accountManagerGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID("accountManagerGroup",
				UserGroupModel.class);
		final UserGroupModel KeyAccountManagerGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID("keyAccountManagerGroup",
				UserGroupModel.class);
		final UserGroupModel keyDistributorGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID("keyDistributorGroup",
				UserGroupModel.class);
		final UserGroupModel keyHospitalGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID("keyHospitalGroup",
				UserGroupModel.class);
		final UserGroupModel mxDistributorGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID("mxDistributorGroup",
				UserGroupModel.class);
		final UserGroupModel mxAccountManagerGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID("mxAccountManagerGroup",
				UserGroupModel.class);
		final UserGroupModel mxKeyAccountManagerGroup = b2BCommerceB2BUserGroupService
				.getUserGroupForUID("mxKeyAccountManagerGroup", UserGroupModel.class);
		final UserGroupModel mxSalesRepGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID("mxSalesRepGroup",
				UserGroupModel.class);

		if (userGroups.contains(salesRepGroupModel) || userGroups.contains(accountManagerGroupModel)
				|| userGroups.contains(KeyAccountManagerGroup) || userGroups.contains(mxAccountManagerGroup)
				|| userGroups.contains(mxKeyAccountManagerGroup) || userGroups.contains(mxSalesRepGroup))
		{
			roles.add(priceChangeGroupModel);
			roles.add(otherOrderGroupModel);
		}
		else if (userGroups.contains(salesThirdPartyGroupModel))
		{
			roles.add(expectedPriceGroupModel);
			roles.add(otherOrderGroupModel);
		}

		else if (userGroups.contains(keyDistributorGroup) || userGroups.contains(keyHospitalGroup)
				|| userGroups.contains(mxDistributorGroup))
		{
			roles.add(otherOrderGroupModel);
		}
	}
	
	@Override
	public String getPasswordEncoding()
	{
		return "bcrypt";
		
	}

}
