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
package com.jnj.facades.customer.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjUserInfoData;
import com.jnj.core.enums.CustomerStatus;
import com.jnj.core.event.JnjLatamSuccessfulRegistrationEmailEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.facades.converters.populator.customer.JnjLatamCustomerReversePopulator;
import com.jnj.facades.customer.JnjLatamCustomerFacade;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.facades.data.JnjLaCustomerData;
import com.jnj.la.core.dto.JnjLatamCompanyInfoData;
import com.jnj.la.core.dto.JnjLatamRegistrationData;
import com.jnj.la.core.services.customer.impl.JnjLatamCustomerServiceImpl;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;

/**
 *
 */
public class JnjLatamCustomerFacadeImpl extends DefaultJnjGTCustomerFacade implements JnjLatamCustomerFacade
{

	private static final Class currentClass = JnjLatamCustomerFacadeImpl.class;

	private static final String b2bUnit = "JnJLaDummyUnit";

	@Autowired
	protected JnjLatamCustomerServiceImpl jnjLatamCustomerService;

	@Autowired
	@Qualifier(value = "customerReversePopulator")
	protected JnjLatamCustomerReversePopulator jnjLatamCustomerReversePopulator;

	@Autowired
	private CMSSiteService cmsSiteService;


	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected B2BCommerceUnitService b2BCommerceUnitService;

	@Autowired
	private Converter<UserModel, CustomerData> jnjLaCustomerConverter;


	@Override
	public CustomerData getCurrentCustomer() {
        return jnjLaCustomerConverter.convert(getUserService().getCurrentUser());
	}

	@Override
	public JnjGTAccountSelectionData getAccountsMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific,
			final boolean isUCN, final JnjGTPageableData pageableData)
	{
		final String methodName = "Latam getAccountsMap()";
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, Logging.END_OF_METHOD, currentClass);
		return jnjLatamCustomerService.getAccountsMap(addCurrentB2BUnit, isSectorSpecific, isUCN, pageableData);
	}


	@Override
	public Set<UserGroupModel> getUserMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific, final boolean isUCN,
			final JnjGTPageableData pageableData)
	{
		final String methodName = "Latam getAccountsMap()";
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, Logging.END_OF_METHOD, currentClass);
		return jnjLatamCustomerService.getUsersMap(pageableData);
	}

	/**
	 * This method performs the change of current account for the user.
	 *
	 * @param selectedAccountUid
	 *           the selected account uid
	 * @return success / failure
	 */
	@Override
	public boolean changeAccount(final String selectedAccountUid)
	{
		final String methodName = "Latam changeAccount()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, Logging.BEGIN_OF_METHOD);

		/** Fetching the current customer **/
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
		final Set<PrincipalGroupModel> allGroupsAssignedToUser = currentCustomer.getGroups();
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName,
				"customer no of account :: " + allGroupsAssignedToUser.size(), currentClass);
		logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, "Current customer :: " + currentCustomer);

		/** Fetching the selected b2b unit by id **/
		final JnJB2BUnitModel selectedUnit = (JnJB2BUnitModel) b2BCommerceUnitService.getUnitForUid(selectedAccountUid);
		logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, "Selected unit :: " + selectedUnit);

		/** setting the selected unit in the customer **/
		currentCustomer.setCurrentB2BUnit(selectedUnit);
		currentCustomer.setDefaultB2BUnit(selectedUnit);
		/** setting the selected unit in the session services **/
		sessionService.setAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT, selectedUnit);

		/** saving the customer model **/
		final boolean status = jnjGTCustomerService.saveJnjGTB2bCustomer(currentCustomer);

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, "Current Customer save status :: " + status,
				currentClass);

		/** setting the account specific attributes at session level **/
		sessionService.setAttribute("isInternationAff", Boolean.valueOf(jnjGTB2BUnitService.isCustomerInternationalAff()));

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, Logging.END_OF_METHOD);
		return status;
	}

	@Override
	public boolean registerUser(final JnjLatamRegistrationData jnjRegistrationData, final String siteLogoURL)
			throws DuplicateUidException, ModelSavingException
	{
		final String methodName = "registerUser()";
		boolean status = false;
		final JnJB2bCustomerModel newCustomer = getModelService().create(JnJB2bCustomerModel.class);
		final JnjLaCustomerData customerData = new JnjLaCustomerData();
		customerData.setCustomerCode(jnjRegistrationData.getCompanyInfoData().getCustomerCode());
		customerData.setSoldTo(jnjRegistrationData.getCompanyInfoData().getSoldTo());
		customerData.setJnjAccountManager(jnjRegistrationData.getCompanyInfoData().getJnjAccountManager());
		customerData.setFirstName(jnjRegistrationData.getJnjUserInfoData().getFirstName());
		customerData.setLastName(jnjRegistrationData.getJnjUserInfoData().getLastName());
		customerData.setEmail(jnjRegistrationData.getJnjUserInfoData().getEmailAddress());
		customerData.setUid(jnjRegistrationData.getJnjUserInfoData().getEmailAddress().toLowerCase());
		customerData
				.setContactAddress(setAddress(jnjRegistrationData.getJnjUserInfoData(), jnjRegistrationData.getCompanyInfoData()));
		customerData.setSecretQuestionsAnswers(createSecretQuestionData(jnjRegistrationData));
		customerData.setEmailPreferences(jnjRegistrationData.getEmailPreferences());
		setCustomerStatus(customerData, jnjRegistrationData);
		customerData.setLoginDisabledFlag(Boolean.TRUE);
		customerData.setIsResetPassword(Boolean.TRUE);
		customerData.setCommercialUserFlag(jnjRegistrationData.getCommercialUserFlag());
		customerData.setCommercialUserSector(jnjRegistrationData.getCommercialUserSector());
		//Sets the Company Info for the user.

		jnjLatamCustomerReversePopulator.populate(customerData, newCustomer);
		//set the accounts and the role of the user.
		setGroups(newCustomer, jnjRegistrationData);
		newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		newCustomer.setPasswordEncoding(Config.getParameter(REGISTER_USER_PASSWORD_ENCODING));
		newCustomer.setDefaultB2BUnit((JnJB2BUnitModel) b2BCommerceUnitService.getUnitForUid(b2bUnit));
		newCustomer.setCurrentB2BUnit((JnJB2BUnitModel) b2BCommerceUnitService.getUnitForUid(b2bUnit));
		setUidForRegister(jnjRegistrationData, newCustomer);
		final String newEncodedPass = passwordEncoderService.encode(newCustomer, jnjRegistrationData.getPassword(),
				Config.getParameter(REGISTER_USER_PASSWORD_ENCODING));
		newCustomer.setEncodedPassword(newEncodedPass);
		setOldPasswords(newCustomer);
		//Sets the Company Info for the user.
		try
		{
			getModelService().saveAll(newCustomer);
			getModelService().refresh(newCustomer);
			status = true;
		}
		catch (final ModelSavingException exp)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.REGISTRAION, methodName, "Unable to save the new user : ", exp,
					currentClass);
			status = false;
		}
		if (status)
		{
			/** Sending email to CSR - Successful Registration **/
			final boolean emailStatusCSR = sendSuccessfulRegistrationEmail(jnjRegistrationData, siteLogoURL, newCustomer);

			JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRAION, methodName,
					"Email to CSR sending status :: " + emailStatusCSR, currentClass);
		}

		//returning status of registration process.
		return status;
	}


	public boolean sendSuccessfulRegistrationEmail(final JnjLatamRegistrationData jnjRegistrationData, final String siteLogoURL,
			final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		final String METHOD_NAME = "sendSuccessfulRegistrationEmail()";
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
				"Email to CSR sending status :: " + Logging.BEGIN_OF_METHOD, currentClass);
		boolean sendStatus = false;

		try
		{
			/** Publishing event responsible for successful registration email flow **/
			eventService.publishEvent(initializeEmailEvent(new JnjLatamSuccessfulRegistrationEmailEvent(
					siteLogoURL + jnjCommonFacadeUtil.createMediaLogoURL(), jnjRegistrationData), JnJB2bCustomerModel));
			sendStatus = true;
			JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"JnjGTSuccessfulRegistrationEmailEvent has been published!", currentClass);

		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"Unable to publish JnjGTSuccessfulRegistrationEmailEvent : ", exception, currentClass);

		}

		return sendStatus;
	}

	@Override
	public void setCustomerStatus(final JnjLaCustomerData customerData, final JnjLatamRegistrationData jnjRegistrationData)
	{
		customerData.setStatus(CustomerStatus.PENDING_SUPERVISOR_RESPONSE.getCode());
	}

	@Override
	public AddressData setAddress(final JnjUserInfoData jnjUserInfoData, final JnjLatamCompanyInfoData jnjLatamCompanyInfoData)
	{
		//Creating instance of AddressData
		final JnjGTAddressData newAddress = new JnjGTAddressData();
		newAddress.setFirstName(jnjUserInfoData.getFirstName());
		newAddress.setLastName(jnjUserInfoData.getLastName());
		newAddress.setLine1(jnjLatamCompanyInfoData.getShipToLine1());
		newAddress.setLine2(jnjLatamCompanyInfoData.getShipToLine2());
		newAddress.setEmail(jnjUserInfoData.getEmailAddress());
		newAddress.setTown(jnjLatamCompanyInfoData.getShipToCity());
		newAddress.setState(jnjLatamCompanyInfoData.getShipToState());
		newAddress.setPostalCode(jnjLatamCompanyInfoData.getShipToZipCode());
		newAddress.setPhone(jnjUserInfoData.getPhone());
		newAddress.setMobile(jnjUserInfoData.getMobile());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(false);
		newAddress.setVisibleInAddressBook(true);
		newAddress.setDefaultAddress(true);


		final CountryData country = new CountryData();
		country.setIsocode(jnjLatamCompanyInfoData.getShipToCountry());
		newAddress.setCountry(country);


		final RegionData region = new RegionData();
		region.setName(jnjLatamCompanyInfoData.getShipToState());
		region.setIsocode(jnjLatamCompanyInfoData.getShipToState());
		region.setIsocodeShort(jnjLatamCompanyInfoData.getShipToState());
		region.setCountryIso(jnjLatamCompanyInfoData.getShipToCountry());
		newAddress.setRegion(region);

		return newAddress;
	}

	/**
	 * @param newCustomer
	 * @param jnjRegistrationData
	 */

	protected void setGroups(final JnJB2bCustomerModel newCustomer, final JnjLatamRegistrationData jnjRegistrationData)
	{
		final String methodName = "setGroups()";

		final Set<PrincipalGroupModel> customerGroups = new HashSet<>(newCustomer.getGroups());

		//Added the default b2bCustomerGroup to the User, so that the user can login.
		customerGroups.add(userService.getUserGroupForUID(B2BCUSTOMERGROUP));

		//Added the JnJLaDummyUnit.
		customerGroups.add(userService.getUserGroupForUID(b2bUnit));

		//Added the default userGroup to the User based on the site, he registered, so that the user can login.
		final String loggedInSite = cmsSiteService.getCurrentSite().getDefaultCountry().getIsocode();

		UserGroupModel userGroup = null;
		try
		{
			userGroup = userService.getUserGroupForUID(loggedInSite.toLowerCase() + "UserGroup");
		}
		catch (final UnknownIdentifierException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.REGISTRAION, methodName,
					"User Group not found : " + loggedInSite.toLowerCase() + "UserGroup", exception, currentClass);
		}
		if (null != userGroup)
		{

			customerGroups.add(userGroup);
		}

		newCustomer.setGroups(customerGroups);
	}

	@Override
	public List<RegionData> getRegions(final String countryIso)
	{
		final CountryModel country = getCommonI18NService().getCountry(countryIso);
		final List<RegionData> resultList = new ArrayList<>();
		for (final RegionModel regionModel : country.getRegions())
		{
			if (regionModel.getActive().booleanValue())
			{
				final RegionData regionData = new RegionData();
				regionData.setIsocode(regionModel.getIsocode());
				regionData.setName(regionModel.getName());
				resultList.add(regionData);
			}
		}
		final JnjObjectComparator jnjObjectComparator = new JnjObjectComparator(RegionData.class, "getName", true, true);
		Collections.sort(resultList, jnjObjectComparator);
		return resultList;
	}

	@Override
	public List<String> getPhoneCodes(final String countryIso)
	{
		final List<String> phoneCodes = new ArrayList<>();
		final CountryModel country = getCommonI18NService().getCountry(countryIso);

		if (StringUtils.isNotEmpty(country.getPhoneCode()))
		{
			phoneCodes.add(country.getPhoneCode());
		}

		return phoneCodes;
	}

	@Override
	public void updateCustomer(final JnjGTCustomerData customerData) {
        JnjLaCustomerData laCustomerData = (JnjLaCustomerData) customerData;
        final JnJB2bCustomerModel customerModel = (JnJB2bCustomerModel) userService.getUserForUID(laCustomerData.getUid(), B2BCustomerModel.class);

		jnjGTCustomerReversePopulator.populate(laCustomerData, customerModel);
        jnjLatamCustomerReversePopulator.populateAccountPreferences(laCustomerData, customerModel);

		setPrivacyPolicyVersions(customerModel);
		try {
			getModelService().save(customerModel);
			getModelService().refresh(customerModel);
		}
		catch (final ModelSavingException | AmbiguousIdentifierException e)
		{
			LOG.info(e);
			if (e.getCause() instanceof InterceptorException
					&& ((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class))
			{
				LOG.error("Model not found or ambiguous identifier exception for given Uid: " + customerModel.getUid()+ e.getMessage());
			}
			else
			{
				LOG.error("Exception Message: " + e.getMessage());
			}
		}
	}

    @Override
	public void createCustomer(final JnjGTCustomerData customerData) throws DuplicateUidException
	{
		final JnJB2bCustomerModel customerModel = getModelService().create(JnJB2bCustomerModel.class);
		if (CollectionUtils.isEmpty(customerData.getGroups()))
		{
			customerModel.setGroups(getLaDummyUnitGroup(customerModel));
		}

		customerModel.setDefaultB2BUnit((JnJB2BUnitModel) b2BCommerceUnitService.getUnitForUid(b2bUnit));
		customerModel.setCurrentB2BUnit((JnJB2BUnitModel) b2BCommerceUnitService.getUnitForUid(b2bUnit));
		jnjGTCustomerReversePopulator.populate(customerData, customerModel);
		setPrivacyPolicyVersions(customerModel);

		getModelService().save(customerModel);

		getModelService().refresh(customerModel);
	}

	protected Set<PrincipalGroupModel> getLaDummyUnitGroup(final JnJB2bCustomerModel newCustomer)
	{
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN,
				"getDummyUnitGroup()", Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(), currentClass);
		final B2BUnitModel defaultUnit = b2BCommerceUnitService.getUnitForUid("b2bUnit");
		final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>(newCustomer.getGroups());
		groups.add(defaultUnit);

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN,
				"getDummyUnitGroup()", Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(), currentClass);
		return groups;
	}

	@Override
	public List<CategoryModel> getAllFranchise(){
		final CategoryModel category = commerceCategoryService.getCategoryForCode(Jnjb2bCoreConstants.PLP.ROOT_CATEGORY_CODE);
		final List<CategoryModel> allFranchise = new ArrayList<>();

		//test if no categories are available in the portal.
		if(CollectionUtils.isNotEmpty(category.getCategories())){
			LOG.info("getAllFranchise() Fetching all available Franchises from hybris");
			for (final CategoryModel subCategory : category.getCategories()){
				addLevel2Cat(subCategory, allFranchise);
			}
		}
		return allFranchise;
	}

	/**
	 * Populate franchise using level 2 sub category
	 * @param subCategory
	 * @param allFranchise
	 */
	private static void addLevel2Cat(final CategoryModel subCategory, final List<CategoryModel> allFranchise){
		if(CollectionUtils.isNotEmpty(subCategory.getCategories())){
			for (final CategoryModel subCat : subCategory.getCategories()){
				if(isProductPresentForTheCategory(subCat)) {
					allFranchise.add(subCat);
				}
			}
		}
	}

	/**
	 * method to check if product present for category
	 * @param category
	 * @return
	 */
	private static boolean isProductPresentForTheCategory(CategoryModel category) {
		return (category.getProductPresentForTheCategory() != null && category
				.getProductPresentForTheCategory().booleanValue()) ;
	}


}


