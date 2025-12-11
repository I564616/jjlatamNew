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
package com.jnj.la.core.services.customer.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.customer.impl.DefaultJnjGTCustomerService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dao.customer.impl.JnjLatamCustomerDAOImpl;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.services.customer.JnjLatamCustomerService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class JnjLatamCustomerServiceImpl extends DefaultJnjGTCustomerService implements JnjLatamCustomerService {

    private static final Class<JnjLatamCustomerServiceImpl> THIS_CLASS = JnjLatamCustomerServiceImpl.class;

    private static final String GET_ALL_ACCOUNTS_FOR_CUSTOMER = "getAllAccountsForCustomer()";
    private static final String TEST_FOR_LOOP_START_TIME = "test for loop start time :: ";
    private static final String TEST_FOR_LOOP_END_TIME = "test for loop end time :: ";
    private static final String GET_COUNTRY_OF_USER = "getCountryOfUser()";
    private static final String DUMMY_B2B_UNIT = "JnJLaDummyUnit";
    

    @Autowired
    private CMSSiteService cmsSiteService;

    @Resource(name = "jnjB2BUnitService")
    protected JnjGTB2BUnitService jnjGTB2BUnitService;

    @Autowired
    private JnjLatamCustomerDAOImpl jnjLatamCustomerDAOImpl;

    @Override
    public JnjGTAccountSelectionData getAccountsMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific, final boolean isUCN, final JnjGTPageableData pageableData) {
        final String METHOD_NAME = "getAccountsMap()";
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        Set<String> additionalB2BUnits = new HashSet<>();
        CustomerModel currentCustomer;
        if (getUserService().getCurrentUser() instanceof CustomerModel) {
            currentCustomer = (CustomerModel) getUserService().getCurrentUser();
            additionalB2BUnits = getNestedAccountsForCustomer(currentCustomer);
        }
        final SearchPageData<JnJB2BUnitModel> directUnitsSearchPageData = jnjLatamCustomerDAOImpl.getAccountsMap(isSectorSpecific, isUCN, pageableData);

        SearchPageData<JnJB2BUnitModel> nestedUnitsSearchPageData = new SearchPageData<>();
        if (pageableData != null && pageableData.getPageSize() > directUnitsSearchPageData.getResults().size() && !additionalB2BUnits.isEmpty()) {
            nestedUnitsSearchPageData = jnjLatamCustomerDAOImpl.getAdditionalAccounts(new ArrayList<>(additionalB2BUnits), pageableData);
        }

        final List<JnJB2BUnitModel> mainAccountsList = directUnitsSearchPageData.getResults();
        final List<JnJB2BUnitModel> nestedAccountsList = nestedUnitsSearchPageData.getResults();

        final Set<JnJB2BUnitModel> allAccountsSet = new HashSet<>();
        if (nestedAccountsList != null && !nestedAccountsList.isEmpty()) {
            allAccountsSet.addAll(nestedAccountsList);
        }

        if (mainAccountsList != null && !mainAccountsList.isEmpty()) {
            allAccountsSet.addAll(mainAccountsList);
        }

        directUnitsSearchPageData.setResults(new ArrayList<>(allAccountsSet));

        JnjGTAccountSelectionData accountSelectionData = populateAccountsMap(directUnitsSearchPageData, addCurrentB2BUnit);
        populateAccountShippingAddress(directUnitsSearchPageData);

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, THIS_CLASS);
        return accountSelectionData;
    }

    private void populateAccountShippingAddress(final SearchPageData<JnJB2BUnitModel> directUnitsSearchPageData) {
        final Map<String, String> b2bShippingAddressMap = new HashMap<>();
        final String methodName = "populateAccountShippingAddress()";
        for (final JnJB2BUnitModel jnjB2BUnitModel : directUnitsSearchPageData.getResults()) {
            if (jnjB2BUnitModel.getAddresses() != null && jnjB2BUnitModel.getAddresses().iterator().hasNext()) {
                final CountryModel shippingAddressCountry = jnjB2BUnitModel.getAddresses().iterator().next().getCountry();
                JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, "Populating accounts map for: " + jnjB2BUnitModel.getUid(), THIS_CLASS);
                if (shippingAddressCountry != null) {
                    if (isPopulateRegion(shippingAddressCountry.getIsocode(),jnjB2BUnitModel)) {
                        b2bShippingAddressMap.put(jnjB2BUnitModel.getUid(), jnjB2BUnitModel.getAddresses().iterator().next().getRegion().getIsocode() + "-" + jnjB2BUnitModel.getAddresses().iterator().next().getStreetname());
                    } else {
                        b2bShippingAddressMap.put(jnjB2BUnitModel.getUid(), jnjB2BUnitModel.getAddresses().iterator().next().getStreetname());
                    }
                }
            }
        }
        sessionService.setAttribute("b2bShippingAddressMap", b2bShippingAddressMap);
    }

    private static Boolean isPopulateRegion(final String addressCountry, final JnJB2BUnitModel jnjB2BUnitModel) {
        Boolean populateRegion = Boolean.FALSE;
        final List<String> shippingAddressList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_SHIPPING_ADDRESS_REGION);
        if (shippingAddressList.contains(addressCountry) && jnjB2BUnitModel.getAddresses().iterator().next().getRegion() != null) {
            populateRegion = Boolean.TRUE;
        }
        return populateRegion;
    }

    @Override
    protected JnjGTAccountSelectionData populateAccountsMap(final SearchPageData<JnJB2BUnitModel> accountsSearchPageData, final boolean addCurrentB2BUnit) {
        final String METHOD_NAME = "populateAccountsMap()";
        final Map<String, String> accountsMap = new TreeMap<>();
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Populating accounts map...", THIS_CLASS);
        final int i = accountsSearchPageData.getResults().size();
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "No of accounts :: " + i, THIS_CLASS);
        for (final B2BUnitModel b2bunits : accountsSearchPageData.getResults()) {
            if (b2bunits instanceof JnJB2BUnitModel) {
                final JnJB2BUnitModel unitModel = (JnJB2BUnitModel) b2bunits;
                if (!(Jnjb2bCoreConstants.MDD.equalsIgnoreCase(unitModel.getSourceSysId()) && isNotShippingAddress(unitModel))
                    || !(Jnjb2bCoreConstants.CONSUMER.equalsIgnoreCase(unitModel.getSourceSysId()) && isNotBillingAddress(unitModel))) {
                    accountsMap.put(b2bunits.getUid(), unitModel.getGlobalLocNo() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + unitModel.getLocName() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + generateAccountAddress(unitModel));
                    JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Account added :: " + b2bunits.getUid(), THIS_CLASS);
                }
            }
        }

        CustomerModel currentCustomer = null;
        if (getUserService().getCurrentUser() instanceof CustomerModel) {
            currentCustomer = (CustomerModel) getUserService().getCurrentUser();
        }
        if (currentCustomer != null && currentCustomer instanceof JnJB2bCustomerModel) {
            if (addCurrentB2BUnit) {
                if (accountsMap.size() > 1) {
                    sessionService.setAttribute(Jnjb2bCoreConstants.Login.SHOW_CHANGE_ACCOUNT, Boolean.TRUE);
                    logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Session attribute set for showing change account");
                }
            } else {
                JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Removing the current B2B unit from the accounts map...", THIS_CLASS);
                accountsMap.remove(((JnJB2bCustomerModel) currentCustomer).getCurrentB2BUnit().getUid());
            }

        }

        final JnjGTAccountSelectionData accountSelectionData = new JnjGTAccountSelectionData();
        accountSelectionData.setAccountsMap(accountsMap);
        accountSelectionData.setPaginationData(accountsSearchPageData.getPagination());
        return accountSelectionData;
    }

    private static boolean isNotBillingAddress(JnJB2BUnitModel unitModel) {
        return unitModel.getBillingAddress() != null && unitModel.getBillingAddress().getBillingAddress().equals(false);
    }

    private static boolean isNotShippingAddress(JnJB2BUnitModel unitModel) {
        return unitModel.getShippingAddress() != null && unitModel.getShippingAddress().getShippingAddress().equals(false);
    }

    public Set<String> getAllAccountsForCustomer(final CustomerModel currentCustomer) {
        final Set<String> b2bUnitsIncludedFromUser = new HashSet<>();

        final Set<PrincipalGroupModel> usg1 = currentCustomer.getGroups();
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, GET_ALL_ACCOUNTS_FOR_CUSTOMER, TEST_FOR_LOOP_START_TIME + JnJCommonUtil.getCurrentDateTime(), THIS_CLASS);
        for (final PrincipalGroupModel pg : usg1) {
            if (StringUtils.isNumeric(pg.getUid()) && pg instanceof JnJLaB2BUnitModel) {
                b2bUnitsIncludedFromUser.add(pg.getUid());
            } else if (pg instanceof JnJLaB2BUnitModel) {
                final B2BUnitModel b2bUnitModel = jnjGTB2BUnitService.getUnitForUid(pg.getUid());
                if (b2bUnitModel != null) {
                    b2bUnitsIncludedFromUser.add(b2bUnitModel.getUid());
                }
            }
        }

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, GET_ALL_ACCOUNTS_FOR_CUSTOMER, TEST_FOR_LOOP_END_TIME + JnJCommonUtil.getCurrentDateTime(), THIS_CLASS);
        return b2bUnitsIncludedFromUser;
    }

    @Override
    public JnJB2BUnitModel getAccountForCustomer(final CustomerModel currentCustomer, final String accountNumber) {
        final Set<String> b2bUnitsFromUser = getAllB2BUnitsFromCustomer(currentCustomer);
        return jnjLatamCustomerDAOImpl.getValidAccount(currentCustomer, b2bUnitsFromUser, accountNumber);
    }

    private Set<String> getAllB2BUnitsFromCustomer(CustomerModel currentCustomer) {
        final Set<String> b2bUnitsFromUser = new HashSet<>();
        if (getUserService().getCurrentUser() instanceof CustomerModel) {
            b2bUnitsFromUser.addAll(getAllAccountsForCustomer(currentCustomer));
            b2bUnitsFromUser.addAll(getNestedAccountsForCustomer(currentCustomer));
        }
        return b2bUnitsFromUser;
    }

    private Set<String> getNestedAccountsForCustomer(final CustomerModel currentCustomer) {
        final Set<String> nestedB2BUnitsUidForUser = new HashSet<>();
        final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
        String loggedInCountryISO = StringUtils.EMPTY;
        if (cmsSiteModel != null && cmsSiteModel.getDefaultCountry() != null) {
            loggedInCountryISO = JnjLaCommonUtil.getIdByCountry(cmsSiteModel.getDefaultCountry().getIsocode()).toUpperCase();
        }

        String mddAccountGroups = StringUtils.EMPTY;
        String phrAccountGroups = StringUtils.EMPTY;
        String masterAccountGroups = StringUtils.EMPTY;
        if (!StringUtils.isEmpty(loggedInCountryISO)) {
            mddAccountGroups = Jnjlab2bcoreConstants.UpsertCustomer.SECTOR_MDD + loggedInCountryISO + Jnjlab2bcoreConstants.UpsertCustomer.GROUP;
            phrAccountGroups = Jnjlab2bcoreConstants.UpsertCustomer.SECTOR_PHR + loggedInCountryISO + Jnjlab2bcoreConstants.UpsertCustomer.GROUP;
            masterAccountGroups = Jnjlab2bcoreConstants.UpsertCustomer.MASTER_B2BUNIT_INITIAL + loggedInCountryISO;
        }

        final Set<PrincipalGroupModel> usg1 = currentCustomer.getGroups();
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, "getNestedAccountsForCustomer", TEST_FOR_LOOP_START_TIME + JnJCommonUtil.getCurrentDateTime(), THIS_CLASS);
        for (final PrincipalGroupModel pg : usg1) {
            if (pg.getUid().equals(mddAccountGroups)) {
                nestedB2BUnitsUidForUser.add(mddAccountGroups);
            } else if (pg.getUid().equals(phrAccountGroups)) {
                nestedB2BUnitsUidForUser.add(phrAccountGroups);
            } else if (pg.getUid().equals(masterAccountGroups)) {
                nestedB2BUnitsUidForUser.add(masterAccountGroups);
            }
        }

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, "getNestedAccountsForCustomer", "nestedB2BUnitsUidForUser :: " + nestedB2BUnitsUidForUser, THIS_CLASS);

        return nestedB2BUnitsUidForUser;
    }

    private static Set<String> getAllUserGroupsForCustomer(final CustomerModel currentCustomer) {
        final String METHOD_NAME = "getAllUserGroupsForCustomer()";
        final Set<String> b2bUnitsIncludedFromUser = new HashSet<>();

        final Set<PrincipalGroupModel> usg1 = currentCustomer.getGroups();
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, TEST_FOR_LOOP_START_TIME + JnJCommonUtil.getCurrentDateTime(), THIS_CLASS);
        for (final PrincipalGroupModel pg : usg1) {
            if (pg instanceof UserGroupModel && !(pg instanceof CompanyModel)) {
                b2bUnitsIncludedFromUser.add(pg.getUid());
            }
        }
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, TEST_FOR_LOOP_END_TIME + JnJCommonUtil.getCurrentDateTime(), THIS_CLASS);
        return b2bUnitsIncludedFromUser;
    }

    private static Set<String> getNestedUserGroupsForCustomer(final CustomerModel currentCustomer) {
        final String METHOD_NAME = GET_ALL_ACCOUNTS_FOR_CUSTOMER;
        final Set<String> nestedUserGroups = new HashSet<>();
        final Set<PrincipalGroupModel> usg1 = currentCustomer.getAllGroups();
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, TEST_FOR_LOOP_START_TIME + JnJCommonUtil.getCurrentDateTime(), THIS_CLASS);

        for (final PrincipalGroupModel pg : usg1) {
            if (pg instanceof UserGroupModel && !(pg instanceof CompanyModel)) {
                nestedUserGroups.add(pg.getUid());
            }
        }

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "nestedB2BUnitsUidForUser :: " + nestedUserGroups, THIS_CLASS);

        return nestedUserGroups;
    }

    public Set<UserGroupModel> getUsersMap(final JnjGTPageableData pageableData) {
        final String METHOD_NAME = "getAccountsMap()";
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        Set<String> allB2BUnitsUidForUser = new HashSet<>();
        Set<String> additionalB2BUnits = new HashSet<>();
        CustomerModel currentCustomer;
        if (getUserService().getCurrentUser() instanceof CustomerModel) {
            currentCustomer = (CustomerModel) getUserService().getCurrentUser();

            allB2BUnitsUidForUser = getAllUserGroupsForCustomer(currentCustomer);

            additionalB2BUnits = getNestedUserGroupsForCustomer(currentCustomer);
        }
        final SearchPageData<UserGroupModel> directUnitsSearchPageData = jnjLatamCustomerDAOImpl.getUserGroupsMap(pageableData, allB2BUnitsUidForUser);

        SearchPageData<UserGroupModel> nestedUnitsSearchPageData = new SearchPageData<>();
        if (!additionalB2BUnits.isEmpty()) {
            nestedUnitsSearchPageData = jnjLatamCustomerDAOImpl.getAdditionalUserGroups(additionalB2BUnits, pageableData);
        }

        final List<UserGroupModel> mainAccountsList = directUnitsSearchPageData.getResults();
        final List<UserGroupModel> nestedAccountsList = nestedUnitsSearchPageData.getResults();

        final Set<UserGroupModel> allAccountsSet = new HashSet<>();

        if (nestedAccountsList != null && !nestedAccountsList.isEmpty()) {
            allAccountsSet.addAll(nestedAccountsList);
        }

        if (mainAccountsList != null && !mainAccountsList.isEmpty()) {
            allAccountsSet.addAll(mainAccountsList);
        }

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, THIS_CLASS);
        return allAccountsSet;
    }


    @Override
    public CountryModel getCountryOfUser() throws BusinessException {
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, GET_COUNTRY_OF_USER, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        final JnJB2bCustomerModel jnjB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();
        JnJB2BUnitModel jnJB2BUnitModel;
        if (null != jnjB2bCustomerModel) {
            jnJB2BUnitModel = (JnJB2BUnitModel) jnjB2bCustomerModel.getDefaultB2BUnit();
        } else {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, GET_COUNTRY_OF_USER, "No User found. Throwing Business Exception. ", THIS_CLASS);
            throw new BusinessException("No User found.");
        }
        CountryModel countryModel;
        if (null != jnJB2BUnitModel) {
            countryModel = jnJB2BUnitModel.getCountry();
        } else {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, GET_COUNTRY_OF_USER, "No B2bUnit found for the Logged User. Throwing Business Exception. ", THIS_CLASS);

            throw new BusinessException("No B2bUnit found for the Logged User. ");
        }
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, GET_COUNTRY_OF_USER, Logging.END_OF_METHOD, THIS_CLASS);
        return countryModel;
    }
    
    @Override
    public String getCountry() {
    	final String METHOD_NAME = "getCountry()";
        String countryIsoCode= StringUtils.EMPTY;
        final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) getUserService().getCurrentUser();
        final JnJB2BUnitModel currentB2BUnit = (JnJB2BUnitModel) currentUser.getCurrentB2BUnit();
        final JnJB2BUnitModel defaultB2BUnit = (JnJB2BUnitModel) currentUser.getDefaultB2BUnit();
        
        if(currentB2BUnit.getUid().equalsIgnoreCase(DUMMY_B2B_UNIT) && !defaultB2BUnit.getUid().equalsIgnoreCase(DUMMY_B2B_UNIT)){
        	
        	countryIsoCode=defaultB2BUnit.getCountry().getIsocode();
        }
        else if(currentB2BUnit.getUid().equalsIgnoreCase(DUMMY_B2B_UNIT) && defaultB2BUnit.getUid().equalsIgnoreCase(DUMMY_B2B_UNIT))
        {
        	countryIsoCode="BR";
        }
        else
        {
        	countryIsoCode=currentB2BUnit.getCountry().getIsocode();
        }
        
        JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME, "countryIsoCode####################: "+countryIsoCode, THIS_CLASS);
        return countryIsoCode;
    }
    
}