/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.customer.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jnj.core.util.JnjSftpFileTransferUtil;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import jakarta.annotation.Resource;

import de.hybris.platform.util.CSVConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.b2bunit.JnjGTB2BUnitDao;
import com.jnj.core.dao.customer.JnjGTCustomerDAO;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.data.JnjGTDivisonData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.enums.CustomerStatus;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.event.JnjGTApprovedUserEvent;
import com.jnj.core.event.JnjGTCutNotificationEmailEvent;
import com.jnj.core.event.JnjGTProfilePasswordExpiryEvent;
import com.jnj.core.event.JnjGTUserLoginDisableEmailEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.impl.DefaultJnjCustomerService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
//import com.jnj.pcm.constants.JnjPCMCoreConstants;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
//import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.security.permissions.impl.SLDPermissionManagementStrategy.AclCacheUnit;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;


/**
 * This class represents the service layer implementation of the customer flow
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTCustomerService extends DefaultJnjCustomerService implements JnjGTCustomerService
{

	private static final Logger LOG = Logger.getLogger(DefaultJnjGTCustomerService.class);

	/** The Constant JNJ_NA_CUSTOMER_FACADE. */
	protected static final String JNJ_NA_CUSTOMER_FACADE = "JnjGTCustomerFacade";
	@Autowired
	protected DefaultCustomerAccountService customerAccountService;
	/** Auto-wired flexible search service **/
	@Autowired
	protected FlexibleSearchService flexibleSearchService;
	
	@Autowired
	 protected B2BCommerceUnitService b2BCommerceUnitService; 

	/** Auto-wired company B2B commerce service **/
	/*@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;*/
	
	@Resource(name="b2bCommerceUserService")
	protected JnjGTB2BCommerceUserService jnjGTB2BCommerceUserService;

	/** Auto-wired Model Service **/
	@Autowired
	protected ModelService modelService;

	/** Auto-wired Session Service **/
	@Autowired
	protected SessionService sessionService;

	@Autowired
	CommonI18NService commonI18NService;

	/** The event service. */
	@Autowired
	protected EventService eventService;

	@Resource(name="GTCustomerDao")
	protected JnjGTCustomerDAO jnjGTCustomerDAO;

	public DefaultCustomerAccountService getCustomerAccountService() {
		return customerAccountService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	
	/*public CompanyB2BCommerceService getCompanyB2BCommerceService() {
		return companyB2BCommerceService;
	}*/
	
	public JnjGTB2BCommerceUserService getJnjGTB2BCommerceUserService() {
		return jnjGTB2BCommerceUserService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public EventService getEventService() {
		return eventService;
	}

	public JnjGTCustomerDAO getJnjGTCustomerDAO() {
		return jnjGTCustomerDAO;
	}

	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	public JnjGTB2BUnitDao getJnjGTB2BUnitDao() {
		return jnjGTB2BUnitDao;
	}

	public UserService getUserService() {
		return userService;
	}

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	@Autowired
	protected BaseStoreService baseStoreService;

	@Autowired
	protected BaseSiteService baseSiteService;

	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	@Resource(name = "jnjB2BUnitDao")
	protected JnjGTB2BUnitDao jnjGTB2BUnitDao;

	/** The user service. */
	@Autowired
	protected UserService userService;

	@Autowired
	protected JnjConfigService jnjConfigService;

	
	protected static final String CUT_NOTIFICATION_EMAIL = "Cut Notification Email";
	protected static final String ACCOUNTS_SALESREP_INDICATOR = "accounts.salesrep.indicator";
	private static final String ACTIVE_CUSTOMERS_LIST_FILE_FORMAT = "jjcc.customers.details.file.format";
	private static final String ACTIVE_CUSTOMERS_LIST_FILE_NAME = "jjcc.customers.details.file.name";
	private static final String ACTIVE_CUSTOMERS_LIST_FILE_FIELD_SEPERATOR = "jjcc.customers.details.file.csv.field.seperator";
	private static final String ACTIVE_CUSTOMERS_LIST_FILE_HEADER = "jjcc.customers.details.file.header";

	/**
	 *
	 * This method fetches accounts
	 *
	 * @param addCurrentB2BUnit
	 * @param isSectorSpecific
	 * @param isUCN
	 * @param pageableData
	 * @return JnjGTAccountSelectionData
	 */
	@Override
	public JnjGTAccountSelectionData getAccountsMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific,
			final boolean isUCN, final JnjGTPageableData pageableData)
	{
		final String METHOD_NAME = "fetchAccounts()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		if (isAdminUser())
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
					"User is an Admin User. Calling admin method for generating accounts...", LOG);

			//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, LOG);
			return populateAccountsMap(jnjGTCustomerDAO.getAllAccountsMap(isSectorSpecific, isUCN, pageableData), addCurrentB2BUnit);
		}
		else
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
					"User is not an Admin User. Calling regular method for generating accounts...", LOG);

			//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, LOG);
			return populateAccountsMap(jnjGTCustomerDAO.getAccountsMap(isSectorSpecific, isUCN, pageableData), addCurrentB2BUnit);
		}
	}

	/**
	 * This method populates the map of all accounts and pagination data
	 *
	 * @param accountsSearchPageData
	 * @param addCurrentB2BUnit
	 * @return JnjGTAccountSelectionData
	 */
	protected JnjGTAccountSelectionData populateAccountsMap(final SearchPageData<JnJB2BUnitModel> accountsSearchPageData,
			final boolean addCurrentB2BUnit)
	{
		final String METHOD_NAME = "populateAccountsMap()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final Map<String, String> accountsMap = new HashMap<String, String>();
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Populating accounts map...", LOG);
          int i = accountsSearchPageData.getResults().size();
	    	CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
				"No of accounts :: " + i, LOG);
		for (final B2BUnitModel b2bunits : accountsSearchPageData.getResults())
		{
			if (b2bunits instanceof JnJB2BUnitModel)
			{
				/** Setting against key - Account UID - the Account Name, Account GLN and the city/state in the map **/
				final JnJB2BUnitModel unitModel = (JnJB2BUnitModel) b2bunits;
				if (String.valueOf(unitModel.getSourceSysId()).equalsIgnoreCase(Jnjb2bCoreConstants.MDD))
				{
					AddressModel addressModel = unitModel.getShippingAddress();
				if(null !=addressModel && false == addressModel.getShippingAddress())
				{
					continue;
				}
				}
				else if (String.valueOf(unitModel.getSourceSysId()).equalsIgnoreCase(Jnjb2bCoreConstants.CONSUMER))
				{
					AddressModel addressModel = unitModel.getBillingAddress();
					if(null !=addressModel && false == addressModel.getBillingAddress())
					{
						continue;
					}
				}
				
				accountsMap.put(b2bunits.getUid(),
						unitModel.getGlobalLocNo() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + unitModel.getLocName()
								+ Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + generateAccountAddress(unitModel));
				CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
						"Account added :: " + b2bunits.getUid(), LOG);
			}
		}

		/** If the getcurrentUser method return the object which is an instance of JnJB2bCustomerModel **/
		//final B2BCustomerModel currentCustomer = companyB2BCommerceService.getCurrentUser();
		B2BCustomerModel currentCustomer = null;
		if(userService.getCurrentUser() instanceof CustomerModel) {
			currentCustomer = (B2BCustomerModel) userService.getCurrentUser();
		}
		if (currentCustomer!= null && currentCustomer instanceof JnJB2bCustomerModel)
		{
			/** If true is passed in addCurrentB2BUnit to maintain the current B2B Unit from the map **/
			if (addCurrentB2BUnit)
			{
				/** Adding session attribute to check if change account link has to be displayed or not **/
				if (accountsMap.size() > 1)
				{
					sessionService.setAttribute(Jnjb2bCoreConstants.Login.SHOW_CHANGE_ACCOUNT, Boolean.TRUE);
					logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
							"Session attribute set for showing change account");
				}
			}
			else
			{
				CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
						"Removing the current B2B unit from the accounts map...", LOG);
				accountsMap.remove(((JnJB2bCustomerModel) currentCustomer).getCurrentB2BUnit().getUid());
			}
			
		}
		final JnjGTAccountSelectionData accountSelectionData = new JnjGTAccountSelectionData();
		accountSelectionData.setAccountsMap(accountsMap);
		accountSelectionData.setPaginationData(accountsSearchPageData.getPagination());
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return accountSelectionData;
	}

	/**
	 * This method fetches the map containing information for all accounts.
	 *
	 * @param addCurrentB2BUnit
	 * @return Map<String, String>
	 */
	@Override
	public Map<String, String> getAccountsMap(final boolean addCurrentB2BUnit)
	{
		final String METHOD_NAME = "getAccountsMap()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		/** Calling generateAccountsMap by passing false for adding accounts irrespective of sector **/
		return generateAccountsMap(addCurrentB2BUnit, false, false, false);
	}

	/**
	 * This method fetches the map containing information for sector specific accounts.
	 *
	 * @param addCurrentB2BUnit
	 * @param isUCNFlag
	 * @return Map<String, String>
	 */
	@Override
	public Map<String, String> getSectorSpecificAccountsMap(final boolean addCurrentB2BUnit, final boolean isUCNFlag)
	{
		final String METHOD_NAME = "getAccountsMap()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		/** Calling generateAccountsMap by passing true for adding only sector specific accounts **/
		return generateAccountsMap(addCurrentB2BUnit, true, isUCNFlag, false);
	}

	/**
	 * This method generates the accounts map based on whether the user is administrator or not by calling the
	 * appropriate protected method
	 *
	 * @param addCurrentB2BUnit
	 * @param isSectorSpecific
	 * @param isUCNFlag
	 * @param isAdmin
	 * @return accountsMap
	 */
	protected Map<String, String> generateAccountsMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific,
			final boolean isUCNFlag, final boolean isAdmin)
	{
		final String METHOD_NAME = "generateAccountsMap()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		if (isAdmin)
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
					"User is an Admin User. Calling admin method for generating accounts...", LOG);
			//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, LOG);
			return null;
		}
		else
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
					"User is not an Admin User. Calling regular method for generating accounts...", LOG);
			//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, LOG);
			return generateAccountsMap(addCurrentB2BUnit, isSectorSpecific, isUCNFlag);
		}
	}

	/**
	 * Added for CR 12th January
	 *
	 * This method checks if the user is an administrator
	 *
	 */
	protected boolean isAdminUser()
	{
		final String METHOD_NAME = "generateAccountsMap()";
		/** Fetching user tier type **/
		final Object userTierType = sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE);
		if (null != userTierType
				&& (Jnjb2bCoreConstants.UserSearch.USER_TIER1.equalsIgnoreCase(String.valueOf(userTierType)) || Jnjb2bCoreConstants.UserSearch.USER_TIER2
						.equalsIgnoreCase(String.valueOf(userTierType))))
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "ADMIN privileges :: TRUE", LOG);
			return true;
		}
		else
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "ADMIN privileges :: FALSE", LOG);
			return false;
		}
	}

	/**
	 * This method generates a map of accounts with essential data.
	 *
	 * @param addCurrentB2BUnit
	 * @param isSectorSpecific
	 * @param isUCNFlag
	 * @return associatedUnits
	 */
	protected Map<String, String> generateAccountsMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific,
			final boolean isUCNFlag)
	{
		final String METHOD_NAME = "generateAccountsMap()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final Map<String, String> associatedUnits = new HashMap<String, String>();
		final List<JnjGTTerritoryDivisonModel> JnjGTTerritoryDivisonModelList = new ArrayList<JnjGTTerritoryDivisonModel>();

		/** If the getcurrentUser method return the object which is an instance of JnJB2bCustomerModel **/
		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			/** Fetching the current user **/
			//final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) companyB2BCommerceService.getCurrentUser();
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Current User fetched :: " + currentUser);

			/** Iterating over all groups obtained from the current user **/
			for (final PrincipalModel principalModel : currentUser.getGroups())
			{
				/** Checking if the group is of JnjGTTerritoryDivisonModel type **/
				if (principalModel instanceof JnjGTTerritoryDivisonModel)
				{
					/** Adding the JnjGTTerritoryDivisonModel type casted principalModel into a list **/
					JnjGTTerritoryDivisonModelList.add((JnjGTTerritoryDivisonModel) principalModel);
				}
				/** Else - Checking if the group is of JnJB2BUnitModel type **/
				else if (principalModel instanceof JnJB2BUnitModel)
				{
					/** Updating the associatedUnits map with the directly associated B2B unit found **/
					extractDirectUnits(associatedUnits, principalModel, isSectorSpecific, isUCNFlag);
				}
			}

			/** Checking if the list of the JnjGTTerritoryDivisonModel obtained above is not empty **/
			if (!JnjGTTerritoryDivisonModelList.isEmpty())
			{
				/** Extracting the B2B Units from the List of JnjGTTerritoryDivisonModels **/
				extractUnitsFromTerritoryDivisionModel(associatedUnits, JnjGTTerritoryDivisonModelList, isSectorSpecific, isUCNFlag);
			}

			/** If true is passed in addCurrentB2BUnit to maintain the current B2B Unit from the map **/
			if (addCurrentB2BUnit && associatedUnits.size() > 1)
			{
				/** Adding session attribute to check if change account link has to be displayed or not **/
				sessionService.setAttribute(Jnjb2bCoreConstants.Login.SHOW_CHANGE_ACCOUNT, Boolean.TRUE);
				logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
						"Session attribute set for showing change account");
			}
			/** If false is passed in addCurrentB2BUnit to remove the current B2B Unit from the map **/
			else if (!addCurrentB2BUnit)
			{
				logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Going to remove current B2B Unit");
				/** Fetching the user's current B2B unit **/
				if (null != currentUser.getCurrentB2BUnit())
				{
					/** Removing the current B2B Unit from the associatedUnits Map **/
					associatedUnits.remove(currentUser.getCurrentB2BUnit().getUid());
					logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Current B2B Unit has been removed.");
				}
			}
		}
		else
		{
			/** User is not of the type JnJB2bCustomerModel **/
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "User is not of the type JnJB2bCustomerModel");
		}

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return associatedUnits;
	}

	/**
	 * This method extracts B2B Unit info from the principalModel which is an instance of JnJB2BUnitModel
	 *
	 * @param associatedUnits
	 * @param isSectorSpecific
	 * @param principalModel
	 */
	protected void extractDirectUnits(final Map<String, String> associatedUnits, final PrincipalModel principalModel,
			final boolean isSectorSpecific, final boolean isUCNFlag)
	{
		final String METHOD_NAME = "extractDirectUnits()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnJB2BUnitModel JnJB2BUnitModel = (JnJB2BUnitModel) principalModel;

		/** Komal : Changing the Login For User Management adding A clause for Global Account **/
		if (!JnJB2BUnitModel.getUid().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.GLOBAL_ACCOUNT_ID))
		{
			/** Setting against key - Account UID - the Account Name, Account GLN and the City / State in the map **/
			putGTB2BUnitInMap(associatedUnits, isSectorSpecific, JnJB2BUnitModel, isUCNFlag);
		}
		else
		{
			/** Fetching all children **/
			setB2BUnitChildren(principalModel, associatedUnits, isSectorSpecific, isUCNFlag);
		}
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * Puts Sector specific / non specific B2B Unit data in the map
	 *
	 * @param associatedUnits
	 * @param isSectorSpecific
	 * @param JnJB2BUnitModel
	 * @param isUCNFlag
	 */
	protected void putGTB2BUnitInMap(final Map<String, String> associatedUnits, final boolean isSectorSpecific,
			final JnJB2BUnitModel JnJB2BUnitModel, final boolean isUCNFlag)
	{
		final String METHOD_NAME = "putNAB2BUnitInMap()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Checking if sector specific account needs to be added **/
		if (isSectorSpecific
				&& null != sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME)
				&& null != JnJB2BUnitModel.getSourceSysId()
				&& (JnJB2BUnitModel.getSourceSysId().equalsIgnoreCase(JnjGTSourceSysId.CONSUMER.getCode())
						&& String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME)).equalsIgnoreCase(
								Jnjb2bCoreConstants.CONS) || JnJB2BUnitModel.getSourceSysId().equalsIgnoreCase(
						JnjGTSourceSysId.MDD.getCode())
						&& String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME)).equalsIgnoreCase(
								Jnjb2bCoreConstants.MDD)))
		{
			if (isUCNFlag && null != Config.getParameter(ACCOUNTS_SALESREP_INDICATOR)
					&& Config.getParameter(ACCOUNTS_SALESREP_INDICATOR).equalsIgnoreCase(JnJB2BUnitModel.getIndicator()))
			{
				/** Setting against key - Account UID - the Account Name, Account GLN and the city/state in the map **/
				associatedUnits.put(JnJB2BUnitModel.getUid(), JnJB2BUnitModel.getGlobalLocNo()
						+ Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + JnJB2BUnitModel.getLocName()
						+ Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + generateAccountAddress(JnJB2BUnitModel));
				logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "B2B unit is a SALES REP Unit");
				logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "B2B unit has been added for the sector :: "
						+ String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME)));
			}
			else if (!isUCNFlag)
			{
				/** Setting against key - Account UID - the Account Name, Account GLN and the city/state in the map **/
				associatedUnits.put(JnJB2BUnitModel.getUid(), JnJB2BUnitModel.getGlobalLocNo()
						+ Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + JnJB2BUnitModel.getLocName()
						+ Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + generateAccountAddress(JnJB2BUnitModel));
				logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "B2B unit has been added for the sector :: "
						+ String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME)));
			}
		}
		else if (!isSectorSpecific)
		{
			/** Setting against key - Account UID - the Account Name, Account GLN and the city/state in the map **/
			associatedUnits.put(JnJB2BUnitModel.getUid(), JnJB2BUnitModel.getGlobalLocNo()
					+ Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + JnJB2BUnitModel.getLocName() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL
					+ generateAccountAddress(JnJB2BUnitModel));
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME,
					"B2B unit has been added irrespective of the sector");
		}

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * Komal : Changing the Login For User Management adding A clause for Global Account
	 *
	 * @param isUCNFlag
	 **/
	protected void setB2BUnitChildren(final PrincipalModel principalModel, final Map<String, String> associatedUnits,
			final boolean isSectorSpecific, final boolean isUCNFlag)
	{
		final Set<PrincipalModel> principalMemberSet = ((JnJB2BUnitModel) principalModel).getMembers();
		for (final PrincipalModel principalMember : principalMemberSet)
		{
			if (principalMember instanceof JnJB2BUnitModel)
			{
				/** Setting against key - Account UID - the Account Name, Account GLN and the City / State in the map **/
				putGTB2BUnitInMap(associatedUnits, isSectorSpecific, (JnJB2BUnitModel) principalMember, isUCNFlag);
				if (CollectionUtils.isNotEmpty(((JnJB2BUnitModel) principalMember).getMembers()))
				{
					setB2BUnitChildren(principalMember, associatedUnits, isSectorSpecific, isUCNFlag);
				}
			}
		}
	}

	/**
	 * This method extracts B2B Unit info from the List of principalModels which are instances of
	 * JnjGTTerritoryDivisonModel
	 *
	 * @param associatedUnits
	 * @param isSectorSpecific
	 * @param isUCNFlag
	 * @param jnjGTTerritoryDivisonModelList
	 */
	protected void extractUnitsFromTerritoryDivisionModel(final Map<String, String> associatedUnits,
			final List<JnjGTTerritoryDivisonModel> JnjGTTerritoryDivisonModelList, final boolean isSectorSpecific,
			final boolean isUCNFlag)
	{
		final String METHOD_NAME = "extractUnitsFromTerritoryDivisionModel()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTTerritoryDivCustRelModel JnjGTTerritoryDivCustRelModel = null;

		List<JnjGTTerritoryDivCustRelModel> JnjGTTerritoryDivCustRelModelList = null;

		/** Iterating over the List of JnjGTTerritoryDivisonModel **/
		for (final JnjGTTerritoryDivisonModel JnjGTTerritoryDivisonModel : JnjGTTerritoryDivisonModelList)
		{

			if (!JnjGTTerritoryDivisonModel.getUid().equalsIgnoreCase(Jnjb2bCoreConstants.Login.HASH_CONSUMER_ID))
			{
				JnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();

				/** Setting the source in the Territory Division relation model **/
				JnjGTTerritoryDivCustRelModel.setSource(JnjGTTerritoryDivisonModel);

				/** Querying for all those models that are of type JnjGTTerritoryDivCustRelModel **/
				JnjGTTerritoryDivCustRelModelList = flexibleSearchService.getModelsByExample(JnjGTTerritoryDivCustRelModel);

				logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "jnjGTTerritoryDivCustRelModelList obtained :: "
						+ JnjGTTerritoryDivCustRelModelList);

				/** Checking if the queried jnjGTTerritoryDivCustRelModelList is not null **/
				if (null != JnjGTTerritoryDivCustRelModelList)
				{
					/** Iterating over the queried jnjGTTerritoryDivCustRelModelList **/
					for (final JnjGTTerritoryDivCustRelModel JnjGTTerritoryDivCustRelModelObj : JnjGTTerritoryDivCustRelModelList)
					{
						/** Fetching the target and checking if it is of the type JnJB2BUnitModels **/
						if (JnjGTTerritoryDivCustRelModelObj.getTarget() instanceof JnJB2BUnitModel)
						{
							final JnJB2BUnitModel JnJB2BUnitModel = (JnJB2BUnitModel) JnjGTTerritoryDivCustRelModelObj.getTarget();
							/**
							 * Setting against key - Account UID - the Account Name, Account GLN and the City / State in the
							 * map
							 **/
							putGTB2BUnitInMap(associatedUnits, isSectorSpecific, JnJB2BUnitModel, isUCNFlag);
						}
					}
				}
			}
			else
			{
				/** This method is used for extracting all the Consumer Accounts For Territory "#####" **/
				putAllConsumerAccounts(associatedUnits);
			}
		}
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
	}


	/**
	 * This method is used for extracting all the Consumer Accounts For Territory "#####"
	 */
	protected void putAllConsumerAccounts(final Map<String, String> associatedUnits)
	{

		final JnJB2BUnitModel b2bUnitModel = new JnJB2BUnitModel();
		b2bUnitModel.setSourceSysId(JnjGTSourceSysId.CONSUMER.getCode());
		final List<JnJB2BUnitModel> b2bUnitList = jnjGTB2BUnitService.getB2bUnitWithSourceSystemId(b2bUnitModel);
		for (final JnJB2BUnitModel JnJB2BUnitModel : b2bUnitList)
		{
			associatedUnits.put(JnJB2BUnitModel.getUid(), JnJB2BUnitModel.getGlobalLocNo()
					+ Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + JnJB2BUnitModel.getLocName() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL
					+ generateAccountAddress(JnJB2BUnitModel));
		}
	}

	/**
	 * This method saves the JnJB2bCustomerModel by using model service
	 *
	 * @param currentCustomer
	 */
	@Override
	public boolean saveJnjGTB2bCustomer(final JnJB2bCustomerModel currentCustomer)
	{
		final String METHOD_NAME = "saveJnjGTB2bCustomer()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean status = false;
		try
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Going to save the current customer");
			/** saving the current customer **/
			modelService.save(currentCustomer);
			status = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			/** exception scenario - model saving exception **/
			LOG.error("Unable to save current customer :: " + modelSavingException);
		}
		logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Current customer save status :: " + status);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return status;
	}

	/**
	 * This method generates the account address
	 *
	 * @param JnJB2BUnitModel
	 * @return address
	 */
	protected String generateAccountAddress(final JnJB2BUnitModel JnJB2BUnitModel)
	{
		final String METHOD_NAME = "generateAccountAddress()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final StringBuilder address = new StringBuilder();

		AddressModel addressModel = null;

		/** Checking if MDD Site or Consumer Site **/
		if (String.valueOf(JnJB2BUnitModel.getSourceSysId()).equalsIgnoreCase(Jnjb2bCoreConstants.MDD))
		{
			/** If MDD then use shipping address **/
			addressModel = JnJB2BUnitModel.getShippingAddress();
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "MDD Shipping address obtained!");
		}
		else
		{
			/** If Consumer then use billing address **/
			addressModel = JnJB2BUnitModel.getBillingAddress();
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Consumer Billing address obtained!");
		}
		/** Generating account address **/
		if (null != addressModel)
		{
			/** Setting City **/
			address.append(addressModel.getTown());
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "City retrieved successfully");

			/** Setting state **/
			if (null != addressModel.getRegion() && null != addressModel.getRegion().getName())
			{
				address.append(Jnjb2bCoreConstants.CONST_COMMA);
				address.append(Jnjb2bCoreConstants.SPACE);
				address.append(addressModel.getRegion().getName());
				logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "State retrieved successfully");
			}
		}
		else
		{
			address.append(Jnjb2bCoreConstants.SPACE);
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Billing Address is null!");
		}
		/** Returning the address **/
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return String.valueOf(address);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.user.JnjGTUserService#getJnJB2bCustomerModelByExample()
	 */
	@Override
	public JnJB2bCustomerModel getJnJB2bCustomerModel(final JnJB2bCustomerModel JnJB2bCustomerModel)
	{

		JnJB2bCustomerModel customer = null;
		try
		{
			customer = flexibleSearchService.getModelByExample(JnJB2bCustomerModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOG.info("Customer was not found" + exp.getMessage());
		}
		return customer;
	}




	@Override
	public List<JnJB2bCustomerModel> getJnJB2bCustomerModels(final JnJB2bCustomerModel JnJB2bCustomerModel)
	{

		List<JnJB2bCustomerModel> customerList = null;
		try
		{
			customerList = flexibleSearchService.getModelsByExample(JnJB2bCustomerModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOG.info("Customer was not found" + exp.getMessage());
		}
		return customerList;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.customer.JnjGTCustomerService#validateAccountNumbers(java.lang.String[])
	 */
	@Override
	public String validateAccountNumbers(final String[] accountNumbers)
	{
		return validateAccountNumbers(accountNumbers, true);
	}

	/**
	 * This method validates accounts for the self registration process
	 *
	 * @param accountNumbers
	 * @return String
	 */
	@Override
	public String validateAccountNumbersRegistration(final String[] accountNumbers)
	{
		return validateAccountNumbers(accountNumbers, true);
	}

	/**
	 *
	 * @param accountNumbers
	 * @param isGLN
	 * @return String
	 */
	protected String validateAccountNumbers(final String[] accountNumbers, final boolean isGLN)
	{
		final List<String> invalidAccounts = new ArrayList<String>();

		/** Iterate on account number supplied by user **/
		for (final String account : accountNumbers)
		{
			/** Validate each account by ID **/
			final B2BUnitModel b2BUnitModel = jnjGTB2BUnitDao.getB2BUnitByUid(account.trim());
			if (!(b2BUnitModel instanceof JnJB2BUnitModel))
			{
				/** Create invalid accounts list **/
				invalidAccounts.add(account);
			}
		}
		String finalInvalidAccountsString = "";

		/** To validate on GLN or not **/
		if (isGLN)
		{
			final List<String> finalInvalidAccountsList = new ArrayList<String>();

			/** Iterating over invalid accounts from ID test above **/
			for (final String account : invalidAccounts)
			{
				/** Validating the GLN **/
				final String glnValidationResult = jnjGTCustomerDAO.validateGLN(account);

				/** If glnValidationResult is not empty, then it is invalid **/
				if (StringUtils.isNotEmpty(glnValidationResult))
				{
					/** create final invalid account list **/
					finalInvalidAccountsList.add(jnjGTCustomerDAO.validateGLN(account));
				}
			}

			/** Creating the comma separated string to return **/
			for (final String finalInvalidAccount : finalInvalidAccountsList)
			{
				finalInvalidAccountsString += Jnjb2bCoreConstants.CONST_COMMA + finalInvalidAccount;
			}

		}
		else
		{
			/** Creating the comma separated string to return **/
			for (final String finalInvalidAccount : invalidAccounts)
			{
				finalInvalidAccountsString += Jnjb2bCoreConstants.CONST_COMMA + finalInvalidAccount;
			}
		}

		/** Returning the result **/
		if (StringUtils.isNotEmpty(finalInvalidAccountsString) && finalInvalidAccountsString.length() > 1)
		{
			return finalInvalidAccountsString.substring(1, finalInvalidAccountsString.length());
		}
		else
		{
			return finalInvalidAccountsString;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.customer.JnjGTCustomerService#getB2bUnitsForAccountNumbers(java.lang.String[])
	 */
	@Override
	public Set<JnJB2BUnitModel> getB2bUnitsForAccountNumbers(final String accountNumbers)
	{
		String METHOD_NAME="getB2bUnitsForAccountNumbers()";
		final Set<JnJB2BUnitModel> accountSet = new HashSet<JnJB2BUnitModel>();
		for (final String account : StringUtils.split(accountNumbers, ","))
		{
			B2BUnitModel b2BUnitModel = getJnjGTB2BUnitDao().getB2BUnitByUid(account);
			if (null == b2BUnitModel)
			{
				final JnJB2BUnitModel JnJB2BUnitModel = new JnJB2BUnitModel();
				JnJB2BUnitModel.setGlobalLocNo(account);
	/**APAC UAT 460 **/
			try{
					b2BUnitModel = flexibleSearchService.getModelByExample(JnJB2BUnitModel);
					if (b2BUnitModel instanceof JnJB2BUnitModel)
					{
						accountSet.add((JnJB2BUnitModel) b2BUnitModel);
					}
				}
				catch(final ModelNotFoundException e)
					{
					final B2BUnitModel defaultUnit = b2BCommerceUnitService.getUnitForUid("JnJDummyUnit");
					accountSet.add((JnJB2BUnitModel)defaultUnit);
					LOG.error( METHOD_NAME + Logging.HYPHEN + e.getMessage());
					}
			}else{
				accountSet.add((JnJB2BUnitModel) b2BUnitModel);
			}
		}
		return accountSet;
	}


	@Override
	public List<String> getPhoneCodes()
	{
		final List<String> phoneCodes = new ArrayList<String>();
		final List<CountryModel> countryModels = commonI18NService.getAllCountries();
		for (final CountryModel countryModel : countryModels)
		{
			if (StringUtils.isNotEmpty(countryModel.getPhoneCode()))
			{
				phoneCodes.add(countryModel.getPhoneCode());
			}
		}
		return phoneCodes;
	}

	/**
	 * Utility method used for logging entry into / exit from any method.
	 *
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages.
	 *
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param message
	 *           the message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}

	@Override
	public JnJB2bCustomerModel getCurrentUser()
	{
		return (JnJB2bCustomerModel) userService.getCurrentUser();
	}

	@Override
	public void setUserDiableLogin()
	{
		final List<JnJB2bCustomerModel> userList = getAllJnjGTUsers();
		
		int sixtyDaysInactive = 30;
		int eightyDaysInactive = 10;
		if (userList != null && userList.size() > 0)
		{
			for (final JnJB2bCustomerModel iteratedUser : userList)
			{

				LOG.info("User last login is " + iteratedUser.getLastLogin());
				if (iteratedUser.getLastLogin() != null)
				{
					final int daysSinceLastLogin = JnjGTCoreUtil.getDaysDiff(iteratedUser.getLastLogin(), Calendar.getInstance()
							.getTime());
					LOG.debug("daysSinceLastLogin is " + daysSinceLastLogin);
					if (daysSinceLastLogin == -1)
					{
						LOG.warn("User have never logged In:" + iteratedUser.getUid());
					}
					else
					{
						final int thresholdDaysSinceLastLogin = Integer.parseInt(Config
								.getParameter(Jnjb2bCoreConstants.Login.CUSTOMER_lOGIN_DISABLE_DAYS));
						LOG.debug("CUSTOMER_lOGIN_DISABLE_DAYS.. " + thresholdDaysSinceLastLogin);
						
						if (daysSinceLastLogin >= thresholdDaysSinceLastLogin)
						{
							iteratedUser.setLoginDisabled(true);
							getModelService().save(iteratedUser);
							LOG.debug("Login disabled & saved the model");
						}
						//Changes for AAOL - 4795
						else if (daysSinceLastLogin == (thresholdDaysSinceLastLogin - sixtyDaysInactive))
						{
							LOG.info("User "+iteratedUser.getUid() +" with email-id ## " + iteratedUser.getEmail() );
							LOG.info("User lastLogin before >>"+ sixtyDaysInactive+ "days");
							LOG.debug("START : sendDisableLoginWarningMail");
							sendDisableLoginWarningMail(iteratedUser,sixtyDaysInactive);
							LOG.debug("END : sendDisableLoginWarningMail");
						}
						else if (daysSinceLastLogin == (thresholdDaysSinceLastLogin - eightyDaysInactive))
						{
							LOG.info("User "+iteratedUser.getUid() +" with email-id ## " + iteratedUser.getEmail() );
							LOG.info("User lastLogin before >>"+ eightyDaysInactive+ "days");
							LOG.debug("START : sendDisableLoginWarningMail");
							sendDisableLoginWarningMail(iteratedUser,eightyDaysInactive);
							LOG.debug("END : sendDisableLoginWarningMail");
						}
					}
				}
			}
		}
	}

	@Override
	public boolean sendDisableLoginWarningMail(final JnJB2bCustomerModel user, int daysbeforeDisable)
	{
		boolean sendStatus = false;
		final String METHOD_NAME = "sendDisableLoginWarningMail()";

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		try
		{
			LOG.debug("START: publishing JnjGTUserLoginDisableEmailEvent");
			/** Publishing event responsible for sending warning mail **/
			LOG.info("Before publish event for user >>" + user.getEmail());
			eventService.publishEvent(initializeEmailEvent(new JnjGTUserLoginDisableEmailEvent(user.getName(), user.getEmail(),daysbeforeDisable),
					user));
			sendStatus = true;
			logDebugMessage(Jnjb2bCoreConstants.Logging.WARNING_EMAIL + Logging.HYPHEN, METHOD_NAME,
					"JnjGTUserLoginDisableEmailEvent has been published!");
			LOG.debug("END: publishing JnjGTUserLoginDisableEmailEvent");
		}
		catch (final Exception exception)
		{

			LOG.error(Jnjb2bCoreConstants.Logging.WARNING_EMAIL + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Unable to publish JnjGTUserLoginDisableEmailEvent : " + exception.getMessage(), exception);
		}

		return sendStatus;
	}

	/**
	 * This method populates the JnjGTUserLoginDisableEmailEvent object.
	 *
	 * @return populated event object
	 */
	protected <T extends AbstractCommerceUserEvent> T initializeEmailEvent(final T event, final JnJB2bCustomerModel user)
	{
		final String METHOD_NAME = "initializeEmailEvent()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		logDebugMessage(Jnjb2bCoreConstants.Logging.WARNING_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to populate the JnjGTUserLoginDisableEmailEvent object");

		/** populating the event with the basic required values **/
		/*final CMSSiteModel consumerSite = (CMSSiteModel) baseSiteService.getBaseSiteForUID("consNAEpicCMSite");
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(consumerSite);*/
		event.setBaseStore(baseStoreService
				.getBaseStoreForUid(Jnjb2bCoreConstants.MDD_STORE_ID));
		LOG.info("Store id is " + baseStoreService .getBaseStoreForUid(Jnjb2bCoreConstants.MDD_STORE_ID));
		event.setSite(baseSiteService.getBaseSiteForUID(Jnjb2bCoreConstants.MDD_SITE_ID));
		LOG.info("Store id is " + baseStoreService .getBaseStoreForUid(Jnjb2bCoreConstants.MDD_STORE_ID));
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		event.setCustomer(user);

		logDebugMessage(Jnjb2bCoreConstants.Logging.WARNING_EMAIL + Logging.HYPHEN, METHOD_NAME, "event object is populated");

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return event;
	}


	@Override
	public List<JnJB2bCustomerModel> getAllJnjGTUsers()
	{
		return jnjGTCustomerDAO.getAllJnjGTUsers();
	}

	@Override
	public JnjGTDivisonData getPopulatedDivisionData(final JnJB2bCustomerModel customerModel)
	{
		JnJB2bCustomerModel currentUser = customerModel;
		if (null == customerModel)
		{
			currentUser = getCurrentUser();
		}
		final List<String> assignedDivisions = jnjGTB2BCommerceUserService.getUserDivisions(currentUser);
		final JnjGTDivisonData divisionData = new JnjGTDivisonData();

		if (assignedDivisions.contains(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_MITEK)))
		{
			divisionData.setIsMitek(true);
		}
		if (assignedDivisions.contains(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_SPINE)))
		{
			divisionData.setIsSpine(true);
		}
		if (assignedDivisions.contains(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_CODMAN)))
		{
			divisionData.setIsCodman(true);
		}
		if (assignedDivisions.contains(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_ASP)))
		{
			divisionData.setAsp(true);
		}
		// Defect NO. 22308--------Changes Start
		// Get the cordis house account values from the project properties.
		final List<String> cordisHouseList = JnJCommonUtil.getValues(Jnjb2bCoreConstants.Cart.DIVISION_CORDIS,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		// check it for empty or null.
		if (CollectionUtils.isNotEmpty(cordisHouseList))
		{
			for (final String cordisHouse : cordisHouseList)
			{
				if (assignedDivisions.contains(cordisHouse))
				{
					divisionData.setCordis(true);
				}
			}
		}
		// Defect NO. 22308--------Changes End
		if (assignedDivisions.contains(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_OCD)))
		{
			divisionData.setOcd(true);
		}
		return divisionData;
	}

	/**
	 * This method is used to save the current country for the PCM user.
	 */
	/*@Override
	public boolean setCurrentCountryAndUnitForPCM(final String countryCode)
	{
		final JnJB2bCustomerModel customerModel = getCurrentUser();
		boolean isSuccess = false;
		final CountryModel country = commonI18NService.getCountry(countryCode);
		customerModel.setCurrentCountry(country);
		JnJB2BUnitModel JnJB2BUnitModel = new JnJB2BUnitModel();
		JnJB2BUnitModel.setCountry(country);
		JnJB2BUnitModel.setSourceSysId(JnjPCMCoreConstants.PCM);
		JnJB2BUnitModel = flexibleSearchService.getModelByExample(JnJB2BUnitModel);
		*//** setting the selected unit in the session services **//*
		sessionService.setAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT, JnJB2BUnitModel);
		customerModel.setCurrentB2BUnit(JnJB2BUnitModel);
		try
		{
			modelService.save(customerModel);
			modelService.refresh(customerModel);
			isSuccess = true;
		}
		catch (final ModelSavingException exp)
		{
			LOG.error("Error while saving the current country for the customer", exp);
			isSuccess = false;
		}
		return isSuccess;
	}*/

	/**
	 * this method is used to send the password expiry email to the user, so the user can reset their password using the
	 * link provided.
	 *
	 * @param jnjGTB2bCustomer
	 * @param siteUrl
	 * @param forPCMFlag
	 * @throws DuplicateUidException
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public void sentPasswordExpiryEmail(final JnJB2bCustomerModel jnjGTB2bCustomer, final String siteUrl,
			final boolean forPCMFlag) throws DuplicateUidException, UnsupportedEncodingException
	{
		final String METHOD_NAME = "sentPasswordExpiryEmail";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.USER_PASSWORD_EXPIRY_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnjGTProfilePasswordExpiryEvent event = new JnjGTProfilePasswordExpiryEvent();
		event.setBuisnessEmail(jnjGTB2bCustomer.getEmail());
		event.setPasswordExpiryUrl(siteUrl);
		event.setForPCMFlag(String.valueOf(forPCMFlag));
		String token = generateToken(jnjGTB2bCustomer);
		jnjGTB2bCustomer.setToken(token);
		token = URLEncoder.encode(token, "UTF-8");
		//companyB2BCommerceService.saveModel(jnjGTB2bCustomer);
		modelService.save(jnjGTB2bCustomer);
		//Setting the  passwordResetUrl in the Event
		event.setPasswordExpiryUrl(siteUrl + Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL + token
				+ Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL_QUERY_PARAM + jnjGTB2bCustomer.getUid());
		LOG.info("Email Map Has Been Set With Email '" + jnjGTB2bCustomer.getEmail() + "' Site Url " + siteUrl);
		eventService.publishEvent(initializePasswordExpiryEmailEvent(event, jnjGTB2bCustomer, forPCMFlag));
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.USER_PASSWORD_EXPIRY_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 *
	 */
	public String generateToken(final JnJB2bCustomerModel jnjGTB2bCustomer)
	{
		final long timeStamp = getTokenValiditySeconds() <= 0L ? 0L : (new Date()).getTime();
		final SecureToken data = new SecureToken(jnjGTB2bCustomer.getUid(), timeStamp);
		//customerAccountService.setTokenValiditySeconds(6048000L);

		final String token = getSecureTokenService().encryptData(data);
		return token;
	}

	/**
	 * This method populates the JnjGTSuccessfulRegistrationEmailEvent object.
	 *
	 * @return populated event object
	 */
	protected <T extends AbstractCommerceUserEvent> T initializePasswordExpiryEmailEvent(final T event,
			final JnJB2bCustomerModel JnJB2bCustomerModel, final boolean forPCMFlag)
	{
		final String METHOD_NAME = "initializeEmailEvent()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to populate the JnjGTSuccessfulRegistrationEmailEvent object");

		/** populating the event with the basic required values **/
		if (forPCMFlag)
		{
			/** populating the event with the basic required values **/
			//final CMSSiteModel baseSite = (CMSSiteModel) baseSiteService.getBaseSiteForUID(JnjPCMCoreConstants.PCM_SITE_ID);
			//event.setSite(baseSite);
			//event.setBaseStore(baseSite.getStores().get(Integer.parseInt(JnjPCMCoreConstants.ZERO)));
		}
		else
		{
			event.setBaseStore(baseStoreService.getCurrentBaseStore());
			event.setSite(baseSiteService.getCurrentBaseSite());
			
			/*//event.setBaseStore(baseStoreService.getCurrentBaseStore());
			//event.setSite(baseSiteService.getCurrentBaseSite());
			final String currentSiteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			if (Jnjb2bCoreConstants.MDD.equals(currentSiteName)){
				event.setBaseStore(baseStoreService
						.getBaseStoreForUid(Jnjb2bCoreConstants.MDD_STORE_ID));
				event.setSite(baseSiteService.getBaseSiteForUID(Jnjb2bCoreConstants.MDD_SITE_ID));
			} else{
			event.setBaseStore(baseStoreService
					.getBaseStoreForUid(Jnjb2bCoreConstants.CONSUMER_STORE_ID));
			event.setSite(baseSiteService.getBaseSiteForUID(Jnjb2bCoreConstants.CONSUMER_SITE_ID));
			}*/
		}
		event.setCustomer(JnJB2bCustomerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME, "event object is populated");

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return event;
	}

	/**
	 * This method is used to find all the PCM users who have their requestAccountIndicator set as true,meaning that the
	 * user is newly created and needs a password reset email to be sent for the same.
	 */
	@Override
	public List<JnJB2bCustomerModel> getAllNewPCMUsers()
	{

		return jnjGTCustomerDAO.getAllNewPCMUsers();
	}



	@Override
	public boolean saveJnjGTB2bCustomerFeed(final JnJB2bCustomerModel currentCustomer)
	{
		final String METHOD_NAME = "saveJnjGTB2bCustomer()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean status = false;
		try
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Going to save the current customer");
			/** saving the current customer **/
			modelService.save(currentCustomer);
			status = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			throw modelSavingException;
		}
		logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Current customer save status :: " + status);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return status;
	}

	@Override
	public UserGroupModel getProductManagerGroupForCatalog(final CatalogModel catalogModel) throws BusinessException
	{
		final String METHOD_NAME = "getProductManagerGroupForCatalog()";
		//logMethodStartOrEnd(JNJ_NA_CUSTOMER_FACADE, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		UserGroupModel productManagerGroup = null;
		final String CONS_US_PROD_CATALOG_PRODUCTMANAGER_GROUP = "consNAProdCatalog.productmanagerGroup";
		final String CONS_CA_PROD_CATALOG_PRODUCTMANAGER_GROUP = "consCAProdCatalog.productmanagerGroup";

		if (null == catalogModel)
		{
			LOG.error(JNJ_NA_CUSTOMER_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Catalog recieved is null.");
			throw new BusinessException("Catalog recieved is null.");
		}
		final String catalogId = catalogModel.getId();
		String productManagerGroupUid = null;

		/*
		 * Getting UserGroupModel Base on Catalog ID. Catalog ID and UserGroupUId mapping is specified in
		 * "/jnjGTb2bfacades/project.properties"
		 */
		if (StringUtils.equalsIgnoreCase(catalogId, Jnjb2bCoreConstants.CONSUMER_CA_PRODUCT_CATALOG_ID))
		{
			LOG.info(JNJ_NA_CUSTOMER_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Catalog ID is [" + catalogId + "].");
			productManagerGroupUid = JnJCommonUtil.getValue(CONS_CA_PROD_CATALOG_PRODUCTMANAGER_GROUP);
			if (null == productManagerGroupUid)
			{
				LOG.error(JNJ_NA_CUSTOMER_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "No Catalog - ProductManagerId mapping found.");
				throw new BusinessException("No Catalog - ProductManagerId mapping found.");
			}
			productManagerGroup = userService.getUserGroupForUID(productManagerGroupUid);
			LOG.info(JNJ_NA_CUSTOMER_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Found ProductManagerGroup with UID ["
					+ productManagerGroup.getUid() + "] for Catalog with ID [" + catalogId + "].");
		}
		else if (StringUtils.equalsIgnoreCase(catalogId, Jnjb2bCoreConstants.CONSUMER_US_PRODUCT_CATALOG_ID))
		{
			LOG.info(JNJ_NA_CUSTOMER_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Catalog ID is [" + catalogId + "].");
			productManagerGroupUid = JnJCommonUtil.getValue(CONS_US_PROD_CATALOG_PRODUCTMANAGER_GROUP);
			if (null == productManagerGroupUid)
			{
				LOG.error(JNJ_NA_CUSTOMER_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "No Catalog - ProductManagerId mapping found.");
				throw new BusinessException("No Catalog - ProductManagerId mapping found.");
			}
			productManagerGroup = userService.getUserGroupForUID(productManagerGroupUid);
			LOG.info(JNJ_NA_CUSTOMER_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Found ProductManagerGroup with UID ["
					+ productManagerGroup.getUid() + "] for Catalog with ID [" + catalogId + "].");
		}
		else
		{
			LOG.error(JNJ_NA_CUSTOMER_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Catalog with ID [" + catalogId
					+ "] does not match any Catalogs Identified by the System.");
		}
		//logMethodStartOrEnd(JNJ_NA_CUSTOMER_FACADE, METHOD_NAME, Logging.END_OF_METHOD);
		return productManagerGroup;
	}

	/**
	 * This method checks if the user's registration is complete by checking the secret questions.
	 *
	 * @return true - if registration is complete ELSE false
	 */
	@Override
	public boolean isRegistrationComplete()
	{
		final String METHOD_NAME = "isRegistrationComplete()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final String userId = getUserService().getCurrentUser().getUid();

		//final JnJB2bCustomerModel currentCustomer = companyB2BCommerceService.getCustomerForUid(userId); //getUserForUID(uid, B2BCustomerModel.class)
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getUserForUID(userId, B2BCustomerModel.class);		
			if (CollectionUtils.isEmpty(currentCustomer.getSecretQuestionsAndAnswersList()))
			{
				CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, "Secret Questions not found for user :: "
						+ userId + "! REGISTRATION STATUS :: Incomplete", LOG);
				//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
				return false;
			}
			else
			{
				CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, "Secret Questions found for user :: "
						+ userId + "! REGISTRATION STATUS :: Complete", LOG);
				//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
				return true;
			}		
	}

	/* This method is responsible for generating token and URL for the All the User with Token True */
	@Override
	public void resetPasswordForUserWithGenerateTokenTrue()
	{
		final String METHOD_NAME = "resetPasswordForUserWithGenerateTokenTrue()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_CRONJOB, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				//LOG);
		final JnJB2bCustomerModel customer = new JnJB2bCustomerModel();
		customer.setGenerateToken(Boolean.valueOf(true));
		customer.setStatus(CustomerStatus.ACTIVE);
		List<JnJB2bCustomerModel> customerList = new ArrayList<JnJB2bCustomerModel>();
		customerList = flexibleSearchService.getModelsByExample(customer);
		if (CollectionUtils.isNotEmpty(customerList))
		{
			for (final JnJB2bCustomerModel user : customerList)
			{
				generateResetPasswordDetails(user);
			}
		}
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_CRONJOB, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				//LOG);
	}

	/**
	 * @param customer
	 */
	@Override
	public void generateResetPasswordDetails(final JnJB2bCustomerModel customer)
	{
		final String METHOD_NAME = "generateResetPasswordDetails()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_DETAILS, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				//LOG);
		try
		{
			String token = generateToken(customer);
			customer.setToken(token);
			token = URLEncoder.encode(token, "UTF-8");
			customer.setResetPasswordUrl(Config.getParameter(Jnjb2bCoreConstants.EPIC_SITE_URL)
					+ Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL + token
					+ Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL_QUERY_PARAM + customer.getUid());
			if (customer.getGenerateToken().booleanValue())
			{
				customer.setGenerateToken(Boolean.FALSE);
			}
			saveJnjGTB2bCustomerFeed(customer);


		}
		catch (final UnsupportedEncodingException exception)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_DETAILS + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Eccoding Exception while resetting Token and Url For User Id" + customer.getUid());
		}
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_DETAILS, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				//LOG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.customer.JnjGTCustomerService#sendActivationEmail()
	 */
	@Override
	public void sendActivationEmail()
	{
		final String METHOD_NAME = "sendActivationEmail()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_CRONJOB, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				//LOG);
		final JnJB2bCustomerModel customer = new JnJB2bCustomerModel();
		customer.setSendActivationMail(Boolean.TRUE);
		customer.setStatus(CustomerStatus.ACTIVE);
		List<JnJB2bCustomerModel> customerList = new ArrayList<JnJB2bCustomerModel>();
		customerList = flexibleSearchService.getModelsByExample(customer);
		if (CollectionUtils.isNotEmpty(customerList))
		{
			for (final JnJB2bCustomerModel user : customerList)
			{
				generateCustomerdetails(user);

				if (user.getSendActivationMail().booleanValue())
				{
					user.setSendActivationMail(Boolean.FALSE);
					user.setResetPasswordUrl("");
					saveJnjGTB2bCustomerFeed(user);

				}
			}
		}
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_CRONJOB, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				//LOG);


	}

	/**
	 * @param user
	 */
	protected void generateCustomerdetails(final JnJB2bCustomerModel user)
	{
		final String METHOD_NAME = "generateCustomerdetails()";

		try
		{

			String token = generateToken(user);
			user.setToken(token);
			token = URLEncoder.encode(token, "UTF-8");
			final JnjGTApprovedUserEvent event = new JnjGTApprovedUserEvent();
			//event.setCustomer(companyB2BCommerceService.getCustomerForUid(user.getUid()));
			event.setCustomer(userService.getUserForUID(user.getUid(), B2BCustomerModel.class));
			event.setLanguage(commonI18NService.getCurrentLanguage());
			event.setCurrency(commonI18NService.getCurrentCurrency());
			event.setSite(baseSiteService.getBaseSiteForUID(Jnjb2bCoreConstants.CONSUMER_SITE_ID));
			event.setBuisnessEmail(user.getEmail());
			event.setBaseStore(baseStoreService.getBaseStoreForUid(Jnjb2bCoreConstants.MDD_STORE_ID));
			event.setBaseUrl(Config.getParameter(Jnjb2bCoreConstants.EPIC_SITE_URL));
			event.setLogoURL(Jnjb2bCoreConstants.EMAIL_LOG_HOSTNAME);
			event.setHelpUrl(Config.getParameter(Jnjb2bCoreConstants.EPIC_SITE_URL) + Jnjb2bCoreConstants.UserSearch.HELP_URL);
			event.setCustomerServiceNumber(jnjConfigService
					.getConfigValueById(Jnjb2bCoreConstants.UserSearch.CUSTOMER_SERVICES_NUMBER));
			event.setPasswordResetUrl(Config.getParameter(Jnjb2bCoreConstants.EPIC_SITE_URL)
					+ Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL + token
					+ Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL_QUERY_PARAM + user.getUid());
			saveJnjGTB2bCustomerFeed(user);
			eventService.publishEvent(initializeEmailEvent(event, user));
		}
		catch (final UnsupportedEncodingException exception)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_DETAILS + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Eccoding Exception while resetting Token and Url For User Id" + user.getUid());
		}
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.RESET_PASSWORD_DETAILS, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				//LOG);
	}

	/**
	 * This method sends the cut notification email
	 */
	@Override
	public void sendCutNotificationEmail()
	{
		final String METHOD_NAME = "sendCutNotificationEmail()";
		//CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final JnjGTCutNotificationEmailEvent cutNotificationEmailEvent = new JnjGTCutNotificationEmailEvent();

		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Calling DAO layer to fetch cut lines details.", LOG);
		/** Fetching the cut lines **/
		final List<OrderEntryModel> cutOrderLines = jnjGTCustomerDAO.fetchCutLineDetails();
		LOG.info("Cut Lines obtained :: " + cutOrderLines);

		for (final OrderEntryModel orderEntryModel : cutOrderLines)
		{
			final OrderModel order = orderEntryModel.getOrder();
			final UserModel user = order.getUser();

			if (user instanceof JnJB2bCustomerModel)
			{
				final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) user;
				final List<String> emailPreferences = customer.getEmailPreferences();

				CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Checking email preferences", LOG);
				/** Checking if the user's email preferences ask for cut notifications **/
				if (emailPreferences.contains(Jnjb2bCoreConstants.EmailPreferences.PLACED_ORDER_LINE_CUTS)
						|| emailPreferences.contains(Jnjb2bCoreConstants.EmailPreferences.ACCOUNT_ORDER_LINE_CUTS))
				{
					CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME,
							"Email preferences require cut notifications. Preparing email event...", LOG);

					/** Setting the cut notification email data in event **/
					cutNotificationEmailEvent.setUserEmailAddress(customer.getEmail());
					cutNotificationEmailEvent.setDisplayOrderNumber(order.getOrderNumber());
					cutNotificationEmailEvent.setUserFullName(user.getName());
					LOG.debug("Order Num ::" + order.getOrderNumber());
					cutNotificationEmailEvent.setHybrisOrderNumber(order.getCode());
					cutNotificationEmailEvent.setBaseStore(baseStoreService
							.getBaseStoreForUid(Jnjb2bCoreConstants.CONSUMER_STORE_ID));
					cutNotificationEmailEvent.setSite(baseSiteService.getBaseSiteForUID(Jnjb2bCoreConstants.CONSUMER_SITE_ID));
					//cutNotificationEmailEvent.setCustomer(companyB2BCommerceService.getCustomerForUid(customer.getUid()));
					cutNotificationEmailEvent.setCustomer(userService.getUserForUID(user.getUid(), B2BCustomerModel.class));
					cutNotificationEmailEvent.setLanguage(commonI18NService.getCurrentLanguage());
					cutNotificationEmailEvent.setCurrency(commonI18NService.getCurrentCurrency());

					LOG.info("Publishing emailing event for order :: " + order.getOrderNumber() + ", to address :: "
							+ customer.getEmail());
					/** Publishing the event for the cut notification email **/
					eventService.publishEvent(cutNotificationEmailEvent);

					CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME,
							"Email event triggered. Now saving cut notification email sent flag in entry as TRUE.", LOG);
					/** Setting the email sent flag as true **/
					orderEntryModel.setCutNotificationEmailSent(true);

					/** Saving the model **/
					modelService.save(orderEntryModel);
					CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Entry Saved!", LOG);
				}
				else
				{
					/** Setting the Cut notification preference flag as false **/

					CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME,
							"Email preferences do not request email. Now saving cut notification preference flag in entry as FALSE.",
							LOG);
					orderEntryModel.setCutNotificationPreference(false);

					/** Saving the model **/
					modelService.save(orderEntryModel);
					CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Entry Saved!", LOG);
				}
			}
		}
		//CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	@Override
	public boolean setCurrentCountryAndUnitForPCM(String countryCode) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String validateWwid(final String wwid)
	{
		String result = jnjGTCustomerDAO.validateWwid(wwid);
		return result;
	}

	@Override
	public boolean validateDEfaultAccount(JnjGTPageableData pageableData) {
		return false;
	}
	@Override
	public String generateTemporaryPassword(JnJB2bCustomerModel jnjGTB2bCustomer) {
	
	    String temporaryPasswrod = RandomStringUtils.randomAlphabetic(8);
	    return temporaryPasswrod;
	}

	/* (non-Javadoc)
	 * @see com.jnj.core.services.customer.JnjGTCustomerService#isResetPasswordComplete()
	 */
	@Override
	public boolean isResetPasswordComplete()	{
		final String METHOD_NAME = "isResetPasswordComplete()";
		final String userId = getUserService().getCurrentUser().getUid();

		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getUserForUID(userId, B2BCustomerModel.class);		
			if (currentCustomer.getIsResetPassword() != null && !currentCustomer.getIsResetPassword()) {
				CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, "Reset Password not completed for user :: "
						+ userId + "! REGISTRATION STATUS :: Incomplete", LOG);
				return false;
			} else {
				CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, "Reset Password is completed for user :: "
						+ userId + "! REGISTRATION STATUS :: depends on security Questions too Complete", LOG);
				return true;
			}		
	}

	@Override
	public List<String> getAccountListForAutoSuggest(String searchText) {

		final String METHOD_NAME = "getAccountsListForAutoSuggest()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return populateAutoSuggestList(jnjGTCustomerDAO.getAccountListForAutoSuggest(searchText,isAdminUser()));
		
	
	}
	/**
	 * Returns list of b2bUint id from List of JnJB2BUnitModel
	 * @param accountListForAutoSuggest
	 * @return
	 */
	private List<String> populateAutoSuggestList(List<JnJB2BUnitModel> accountListForAutoSuggest) {
		
		List<String> accountList = new ArrayList<String>();
		JnJB2bCustomerModel currentUser= new JnJB2bCustomerModel();
		JnJB2BUnitModel jnjGTb2bUnit = new JnJB2BUnitModel();
		if(userService.getCurrentUser()!=null){
			currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			jnjGTb2bUnit = currentUser.getCurrentB2BUnit();
		}		
		
		if(accountListForAutoSuggest!=null){
			for(JnJB2BUnitModel jnjB2bUnit:accountListForAutoSuggest){
				String b2bUnitID = jnjB2bUnit.getUid();
				accountList.add(b2bUnitID);
			}
		}
		
		if(jnjGTb2bUnit!=null){
			if(accountList.contains(jnjGTb2bUnit.getUid())){
				accountList.remove(jnjGTb2bUnit.getUid());
			}
		}
		
		return accountList;
	}

	/**
	 * Extract Active User Detail List
	 * @return boolean
	 */
	@Override
	public boolean extractActiveUserDetailList() {

		final List<ArrayList<String>> customersList = getJnjGTCustomerDAO().extractActiveUserDetailList();
		boolean fileTransferStatus = false;
		
		if (CollectionUtils.isNotEmpty(customersList)) {
			final StringBuilder fileName = new StringBuilder(getConfigurationService().getConfiguration().getString(ACTIVE_CUSTOMERS_LIST_FILE_NAME));
			final String fileFormat = new SimpleDateFormat(getConfigurationService().getConfiguration().getString(ACTIVE_CUSTOMERS_LIST_FILE_FORMAT)).format(new Date());
			final File file = new File(fileName.append(fileFormat).toString());
			try (final PrintWriter csvWriter = new PrintWriter(file, CSVConstants.DEFAULT_ENCODING))
			{
				addHeader(csvWriter);
				customersList.forEach(customerEntry -> {
					final String customerFieldsCommaSeparated = customerEntry.stream()
							.map(x -> (x == null) ? StringUtils.EMPTY : x.replaceAll(Jnjb2bCoreConstants.SYMBOl_COMMA, StringUtils.EMPTY))
							.collect(Collectors.joining(getConfigurationService().getConfiguration().getString(ACTIVE_CUSTOMERS_LIST_FILE_FIELD_SEPERATOR)));

					csvWriter.println(customerFieldsCommaSeparated);
				});
			}
			catch (final IOException e)
			{
				LOG.error("Exception occurred when attempting to write file" , e);
				return false;
			}

			final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjSftpFileTransferUtil();
			final String remoteFilePath = Config
					.getParameter(Jnjb2bCoreConstants.DocumenTransfer.NAGS_MBOX_SFTP_DESTDIRECTORY);
			fileTransferStatus = jnjSftpFileTransferUtil.uploadFileToSftp(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION_TYPE_NAGS_FILE_MBOX, false, remoteFilePath, file);
			if(file.delete()) {
				LOG.debug(file.getName() + " deleted");
			}
			else {
				LOG.debug("failed to delete " + file.getName());
			}

			return fileTransferStatus;
		}
		else
			return fileTransferStatus;

	}

	private void addHeader(final PrintWriter csvWriter) {
		csvWriter.println(getConfigurationService().getConfiguration().getString(ACTIVE_CUSTOMERS_LIST_FILE_HEADER));

	}

	public void setJnjConfigService(JnjConfigService jnjConfigService) {
		this.jnjConfigService = jnjConfigService;
	}
	
}