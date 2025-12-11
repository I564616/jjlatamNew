/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.customer.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.strategies.B2BUserGroupsLookUpStrategy;
//import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceB2BUserGroupService;
//import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
//import de.hybris.platform.b2bacceleratorservices.strategies.B2BUserGroupsLookUpStrategy;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.storesession.converters.populator.LanguagePopulator;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.converters.populator.CustomerPopulator;
import de.hybris.platform.commercefacades.user.converters.populator.CustomerReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercefacades.user.data.UserGroupData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjCountryData;
import com.jnj.core.dto.JnjRegisterData;
import com.jnj.core.event.JnjCreateUserEvent;
import com.jnj.core.event.JnjPasswordExpiryEvent;
import com.jnj.core.event.JnjRegisterEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjParagraphComponentModel;
import com.jnj.core.model.OldPasswordModel;
import com.jnj.core.model.SecretQuestionsAndAnswersModel;
import com.jnj.core.services.JnJCustomerDataService;
import com.jnj.core.services.JnjCustomerService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.facades.company.JnjB2BUnitFacade;
import com.jnj.facades.customer.JnjCustomerFacade;
import com.jnj.facades.data.JnjCustomerData;
import com.jnj.facades.data.SecretQuestionData;
import com.jnj.services.CMSSiteService;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class DefaultJnjCustomerFacade extends DefaultCustomerFacade implements JnjCustomerFacade
{
	protected final String INTERVAL_LIST_FOR_EMAIL = "user.email.password.expire.interval";
	protected final String PASSWORD_EXPIRY_WINDOW = "user.password.expirationPeriod";

	String intervals = null;

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(DefaultJnjCustomerFacade.class);

	/** The Constant SECRET_QUESTION. */
	protected static final String SECRET_QUESTION = "myprofile.secret.question";

	/** The jnj customer populator. */
	@Autowired
	@Resource(name = "customerPopulator")
	protected CustomerPopulator customerPopulator;

	/** The base store service. */
	@Autowired
	protected BaseStoreService baseStoreService;

	/** The lang populator. */
	@Autowired
	protected LanguagePopulator langPopulator;

	/** The customer reverse populator. */
	@Autowired
	protected CustomerReversePopulator customerReversePopulator;

	/** The configuration service. */
	@Autowired
	protected ConfigurationService configurationService;

	/** The cms content slot service. */
	@Autowired
	protected CMSContentSlotService cmsContentSlotService;

	/** The message facade. */
	@Autowired
	protected MessageFacadeUtill messageFacade;

	/** The jn j customer data service. */
	@Autowired
	protected JnJCustomerDataService jnJCustomerDataService;

	/** The company b2 b commerce service. */
	/*@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;*/

	/** The b2 b commerce b2 b user group service. */
	@Autowired
	protected B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	/** The customer account service. */
	protected CustomerAccountService customerAccountService;

	/** The b2b unit service. */
	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

	/** The common i18 n service. */
	protected CommonI18NService commonI18NService;

	/** The model service. */
	@Autowired
	protected ModelService modelService;

	/** The customer name strategy. */
	protected CustomerNameStrategy customerNameStrategy;

	/** The i18n service. */
	protected I18NService i18nService;

	/** The i18 n facade. */
	@Resource(name = "i18NFacade")
	protected I18NFacade i18NFacade;

	/** The secure token service. */
	protected SecureTokenService secureTokenService;

	/** The token validity seconds. */
	protected long tokenValiditySeconds;

	/** The event service. */
	@Autowired
	protected EventService eventService;

	/** The base site service. */
	@Autowired
	protected BaseSiteService baseSiteService;

	/** The password encoder service. */
	@Autowired
	protected PasswordEncoderService passwordEncoderService;

	/** The default user service. */
	@Autowired
	protected DefaultUserService defaultUserService;

	/** The user service. */
	@Autowired
	protected UserService userService;

	/** The jnj customer service. */
	@Autowired
	protected JnjCustomerService jnjCustomerService;

	/** The jnj b2 b unit facade. */
	@Autowired
	protected JnjB2BUnitFacade jnjB2BUnitFacade;

	/** The media service. */
	@Autowired
	protected MediaService mediaService;

	/** The c ms site service. */
	@Autowired
	protected CMSSiteService cMSSiteService;
	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	protected B2BUserGroupsLookUpStrategy b2BUserGroupsLookUpStrategy;
	@Autowired
	protected Converter<UserGroupModel, UserGroupData> userGroupConverter;

	/** The c ms component service. */
	@Autowired
	protected CMSComponentService cMSComponentService;

	 @Autowired
	 protected B2BCommerceUnitService b2BCommerceUnitService;

	/**
	 * @return the i18NFacade
	 */
	public I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	protected B2BUserGroupsLookUpStrategy getB2BUserGroupsLookUpStrategy()
	{
		return b2BUserGroupsLookUpStrategy;
	}

	public void setB2BUserGroupsLookUpStrategy(final B2BUserGroupsLookUpStrategy b2BUserGroupsLookUpStrategy)
	{
		this.b2BUserGroupsLookUpStrategy = b2BUserGroupsLookUpStrategy;
	}

	/**
	 * @param i18nFacade
	 *           the i18NFacade to set
	 */
	public void setI18NFacade(final I18NFacade i18nFacade)
	{
		i18NFacade = i18nFacade;
	}
	

	public B2BCommerceUnitService getB2BCommerceUnitService()
	{
		return b2BCommerceUnitService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean register(final JnjRegisterData registerData, final String siteLogoURL) throws DuplicateUidException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "register()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final JnJB2bCustomerModel newCustomer = getModelService().create(JnJB2bCustomerModel.class);
		final AddressModel addressModel = getModelService().create(AddressModel.class);
		boolean status = false;
		updateSecretQuestionsForRegistration(createSecretQuestionData(registerData), registerData.getEmail().toLowerCase());
		newCustomer.setName(getCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
		newCustomer.setCustomerName(registerData.getCustName());
		newCustomer.setEmail(registerData.getEmail());
		newCustomer.setEmailNotification(Boolean.valueOf(isEmailNotification(registerData)));
		newCustomer.setGroups(getDummyUnitGroup(newCustomer));
		newCustomer.setLoginDisabled(true);
		newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		setUidForRegister(registerData, newCustomer);
		setOldPasswords(newCustomer);
		setAddress(registerData, newCustomer, addressModel);

		//Setting the Versions of Legal Notice and Privacy Policy
		setLegalPrivacyVersions(newCustomer);
		try
		{
			getModelService().saveAll(newCustomer);
			getModelService().refresh(newCustomer);
			getCustomerAccountService().setDefaultAddressEntry(newCustomer, addressModel);
		}
		catch (final ModelSavingException exp)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.error(Logging.REGISTRAION + Logging.HYPHEN + "register()" + Logging.HYPHEN + "Unable to save the new user : "
						+ exp);
			}
			status = false;
		}
		if (status)
		{
			sendRegistrationEmail(registerData, siteLogoURL);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "register()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		//returning status of registration process.
		return status;
	}

	/**
	 * This method sends the registration email.
	 * 
	 * @param registerData
	 * @param siteLogoURL
	 */
	private void sendRegistrationEmail(final JnjRegisterData registerData, final String siteLogoURL)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "sendRegistrationEmail()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final JnjRegisterEvent jnjRegisterEvent = new JnjRegisterEvent(registerData);
		jnjRegisterEvent.setSiteLogoURL(siteLogoURL + jnjCommonFacadeUtil.createMediaLogoURL());
		eventService.publishEvent(initializeEvent(jnjRegisterEvent));
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "sendRegistrationEmail()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	/**
	 * This method sets address and populates the address model
	 * 
	 * @param registerData
	 * @param newCustomer
	 * @param addressModel
	 */
	private void setAddress(final JnjRegisterData registerData, final JnJB2bCustomerModel newCustomer,
			final AddressModel addressModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "setAddress()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		//Creating instance of AddressData 
		final AddressData newAddress = new AddressData();
		newAddress.setTitleCode(registerData.getTitleCode());
		newAddress.setFirstName(registerData.getFirstName());
		newAddress.setLastName(registerData.getLastName());
		newAddress.setLine1(registerData.getAddress());
		newAddress.setTown(registerData.getCity());
		newAddress.setPostalCode(registerData.getZipCode());
		newAddress.setPhone(registerData.getPhoneNo());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		newAddress.setVisibleInAddressBook(true);
		newAddress.setDefaultAddress(true);

		final CountryData country = new CountryData();
		country.setIsocode(registerData.getCountry());
		newAddress.setCountry(country);

		final RegionData region = new RegionData();
		region.setName(registerData.getState());
		region.setIsocode(registerData.getState());
		region.setIsocodeShort(registerData.getState());
		region.setCountryIso(registerData.getCountry());
		newAddress.setRegion(region);

		addressModel.setPhone1(registerData.getPhoneNo());
		addressModel.setPhone2(registerData.getMobileNo());
		//addressModel.setJnJAddressId(registerData.getEmail());

		//Populating the AddressModel
		getAddressReversePopulator().populate(newAddress, addressModel);

		//Saving the user's address details
		final List customerAddresses = new ArrayList();
		addressModel.setOwner(newCustomer);
		getModelService().save(addressModel);
		getModelService().refresh(addressModel);
		customerAddresses.add(addressModel);
		newCustomer.setAddresses(customerAddresses);
		newAddress.setId(addressModel.getPk().toString());
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "setAddress()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	/**
	 * @param newCustomer
	 */
	protected void setOldPasswords(final JnJB2bCustomerModel newCustomer)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "setOldPasswords()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final List<OldPasswordModel> oldPasswordsList = new ArrayList();
		final OldPasswordModel oldPasswordModel = new OldPasswordModel();

		oldPasswordModel.setOldPassword(newCustomer.getEncodedPassword());
		oldPasswordModel.setModifiedDate(Calendar.getInstance().getTime());
		oldPasswordModel.setJnjCustomer(newCustomer);
		oldPasswordsList.add(oldPasswordModel);
		newCustomer.setOldPasswords(oldPasswordsList);
		newCustomer.setPasswordChangeDate(oldPasswordModel.getModifiedDate());
		if (newCustomer.getForcefulExpired())
		{
			newCustomer.setForcefulExpired(Boolean.FALSE);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "setOldPasswords()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	/**
	 * This method adds the dummy unit to a set of principal group model and returns it.
	 * 
	 * @param newCustomer
	 * @return Set<PrincipalGroupModel> groups
	 */
	protected Set<PrincipalGroupModel> getDummyUnitGroup(final JnJB2bCustomerModel newCustomer)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "getDummyUnitGroup()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final B2BUnitModel defaultUnit = b2BCommerceUnitService.getUnitForUid("JnJDummyUnit");
		final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>(newCustomer.getGroups());
		groups.add(defaultUnit);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "getDummyUnitGroup()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return groups;
	}

	/**
	 * This method creates the secret question data list by extracting the values from the register data.
	 * 
	 * @param registerData
	 * @return List<SecretQuestionData> secretQuestionData
	 */
	private List<SecretQuestionData> createSecretQuestionData(final JnjRegisterData registerData)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "createSecretQuestionData()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final List<SecretQuestionData> secretQuestionData = new ArrayList<SecretQuestionData>();
		for (final SecretQuestionData secretQuestion : registerData.getQuestionList())
		{
			if (secretQuestion.getCode() != null && StringUtils.isNotBlank(secretQuestion.getCode())
					&& secretQuestion.getAnswer() != null && StringUtils.isNotBlank(secretQuestion.getAnswer()))
			{
				secretQuestionData.add(secretQuestion);
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "createSecretQuestionData()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return secretQuestionData;
	}

	/**
	 * This method checks if the registration data holds the email notification info as Yes or No, and accordingly
	 * returns true or false
	 * 
	 * @param registerData
	 * @return true/false
	 */
	private boolean isEmailNotification(final JnjRegisterData registerData)
	{
		if (registerData.getEmailNotification().equals(Jnjb2bCoreConstants.Y_STRING))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * This method sets the Current Versions of Legal Policy and Privacy Notice in the Current User.
	 * 
	 * @param newCustomer
	 */
	protected void setLegalPrivacyVersions(final JnJB2bCustomerModel newCustomer)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.LEGACY_POLICY_CHECK + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "setLegalPrivacyVersions()" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		try
		{
			final JnjParagraphComponentModel legalNoticeParagraphComponentModel = cMSComponentService.getAbstractCMSComponent(Config
					.getParameter(Jnjb2bCoreConstants.Login.LEGAL_NOTICE_COMPONENT_UID_KEY));
			final JnjParagraphComponentModel privacyPolicyParagraphComponentModel = cMSComponentService
					.getAbstractCMSComponent(Config.getParameter(Jnjb2bCoreConstants.Login.PRIVACY_POLICY_COMPONENT_UID_KEY));

			newCustomer.setLegalNoticeVersion(legalNoticeParagraphComponentModel.getComponentVersion());
			newCustomer.setPrivacyPolicyVersion(privacyPolicyParagraphComponentModel.getComponentVersion());
		}
		catch (final CMSItemNotFoundException cMSItemNotFoundException)
		{
			LOG.error(Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "checkLegalPrivacyVersions()" + Logging.HYPHEN
					+ "Unable to fetch LegalNoticeComponent or PrivacyPolicyComponent with excweption: " + cMSItemNotFoundException);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.LEGACY_POLICY_CHECK + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "setLegalPrivacyVersions()" + Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}



	private JnjRegisterEvent initializeEvent(final JnjRegisterEvent event)
	{
		LOG.debug("Entered the initializeEvent() method");
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer((CustomerModel) userService.getCurrentUser());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		LOG.debug("Exiting the initializeEvent() method");
		return event;
	}


	protected void setUidForRegister(final JnjRegisterData registerData, final JnJB2bCustomerModel customer)
	{
		customer.setUid(registerData.getEmail().toLowerCase());
		customer.setOriginalUid(registerData.getEmail());
	}




	@Override
	public List<SecretQuestionData> getSecretQuestions()
	{
		final List<SecretQuestionData> secretQuestionsList = new ArrayList<SecretQuestionData>();

		final String questionString = configurationService.getConfiguration().getString(SECRET_QUESTION);
		final String[] questionCodes = questionString.split(";");
		for (final String question : questionCodes)
		{
			final SecretQuestionData secretQuestionData = new SecretQuestionData();
			secretQuestionData.setCode(question);
			try
			{
				secretQuestionData.setQuestion(messageFacade.getMessageTextForCode(question));
			}
			catch (final BusinessException exp)
			{
				LOG.error("Error while getting secret questions list - " + exp.getMessage());
			}
			secretQuestionsList.add(secretQuestionData);

		}

		return secretQuestionsList;

	}

	@Override
	public void updatePwd(final String newPassword, final String email) throws TokenInvalidatedException
	{
		final B2BCustomerModel b2bcustomerModel = getUserService().getUserForUID(email.toLowerCase(), B2BCustomerModel.class);
		final long timeStamp = getTokenValiditySeconds() <= 0L ? 0L : (new Date()).getTime();
		final SecureToken data = new SecureToken(b2bcustomerModel.getUid(), timeStamp);
		final String token = getSecureTokenService().encryptData(data);
		b2bcustomerModel.setToken(token);
		getModelService().save(b2bcustomerModel);
		getCustomerAccountService().updatePassword(token, newPassword);
	}

	@Override
	public void updateFullProfile(final JnjCustomerData customerData) throws DuplicateUidException
	{
		//validateParameterNotNullStandardMessage("customerData", customerData);
		//Assert.hasText(customerData.getTitleCode(), "The field [TitleCode] cannot be empty");
		Assert.hasText(customerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(customerData.getLastName(), "The field [LastName] cannot be empty");
		Assert.hasText(customerData.getUid(), "The field [Uid] cannot be empty");

		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) getCurrentSessionCustomer();

		customerReversePopulator.populate(customerData, customer);

		if (customer.getDefaultPaymentAddress() != null)
		{
			getModelService().save(customer.getDefaultPaymentAddress());
		}

		if (customer.getDefaultShipmentAddress() != null)
		{
			getModelService().save(customer.getDefaultShipmentAddress());
		}
		if (CollectionUtils.isNotEmpty(customer.getAddresses()))
		{
			final List<AddressModel> addresses = (List<AddressModel>) customer.getAddresses();
			getModelService().save(addresses.get(0));
		}

		getModelService().save(customer);
	}

	/**
	 * Populate current user model.
	 * 
	 * @param customerData
	 *           the customer data
	 * @param customerModel
	 *           the customer model
	 */
	private void populateUserModel(final JnjCustomerData customerData, final JnJB2bCustomerModel customerModel)
	{
		final List<AddressModel> addresses = (List<AddressModel>) customerModel.getAddresses();
		customerModel.setEmail(customerData.getEmail());
		customerModel.setUid(customerData.getEmail());
		customerModel.setEmailNotification(customerData.getEmailNotification());
		customerModel.setName(customerData.getFirstName() + " " + customerData.getLastName());
		final B2BUnitModel defaultUnit = b2BCommerceUnitService.getUnitForUid(customerData.getUnit().getUid());
		customerModel.setDefaultB2BUnit(defaultUnit);
		if (CollectionUtils.isNotEmpty(addresses))
		{
			final AddressModel address = addresses.get(0);//As user will be having only one address
			address.setPhone1(customerData.getDefaultAddress().getPhone());
			address.setPhone2(customerData.getDefaultAddress().getPhone2());
			getModelService().save(address);
			customerModel.setAddresses(addresses);

		}

		else
		{
			if (customerData.getDefaultAddress() != null)
			{

				final List<AddressModel> addressesList = new ArrayList<AddressModel>();
				final AddressModel addressModel = getModelService().create(AddressModel.class);
				addressModel.setPhone1(customerData.getDefaultAddress().getPhone());
				addressModel.setPhone2(customerData.getDefaultAddress().getPhone2());
				addressModel.setOwner(customerModel);
				getModelService().save(addressModel);
				addressesList.add(addressModel);
				customerModel.setAddresses(addresses);

			}
		}
	}



	@Override
	public void updateProfile(final JnjCustomerData customerData) throws DuplicateUidException
	{
		validateDataBeforeUpdate(customerData);

		final String name = getCustomerNameStrategy().getName(customerData.getFirstName(), customerData.getLastName());
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) getCurrentSessionCustomer();
		customer.setOriginalUid(customerData.getDisplayUid());

		getModelService().save(customer);
		getCustomerAccountService().updateProfile(customer, customerData.getTitleCode(), name, customerData.getUid());
	}

	@Override
	public void validateDataBeforeUpdate(final JnjCustomerData customerData)
	{
		//validateParameterNotNullStandardMessage("customerData", customerData);
		//Assert.hasText(customerData.getTitleCode(), "The field [TitleCode] cannot be empty");
		Assert.hasText(customerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(customerData.getLastName(), "The field [LastName] cannot be empty");
		Assert.hasText(customerData.getUid(), "The field [Uid] cannot be empty");
	}



	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException
	{
		getCustomerAccountService().updatePassword(token, newPassword);
	}

	@Override
	public Set<LanguageData> getLangList()
	{
		final Set<LanguageData> langDataList = new HashSet<LanguageData>();
		final Set<LanguageModel> langList = baseStoreService.getCurrentBaseStore().getLanguages();
		for (final LanguageModel lang : langList)
		{
			final LanguageData langData = new LanguageData();
			langPopulator.populate(lang, langData);
			langDataList.add(langData);

		}

		return langDataList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.customer.JnJCustomerFacade#getSecretQuestionsForUser()
	 */
	@Override
	public List<SecretQuestionData> getSecretQuestionsForUser()
	{
		final String userId = getUserService().getCurrentUser().getUid();
		final List<SecretQuestionData> secretQuestions = getSecretQuestionsForUser(userId);
		return secretQuestions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.customer.JnJCustomerFacade#updateSecretQuestions()
	 */
	@Override
	public boolean updateSecretQuestions(final List<SecretQuestionData> secretQuestionDataList)
	{
		boolean saved = false;
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) getUserService().getCurrentUser();
		final List<SecretQuestionsAndAnswersModel> secretQuestionModelList = new ArrayList<SecretQuestionsAndAnswersModel>();
		for (final SecretQuestionData secretQuestionData : secretQuestionDataList)
		{
			final SecretQuestionsAndAnswersModel secretQuestionModel = new SecretQuestionsAndAnswersModel();
			if ((StringUtils.isNotBlank(secretQuestionData.getCode())) && (StringUtils.isNotEmpty(secretQuestionData.getCode())))
			{
				secretQuestionModel.setQuestionId(secretQuestionData.getCode());
				secretQuestionModel.setAnswer(secretQuestionData.getAnswer());
				try
				{
					getModelService().save(secretQuestionModel);
				}
				catch (final ModelSavingException modelSavingException)
				{
					LOG.error("Unable to save the secret questions for the user :" + customer.getUid());

				}
			}
			secretQuestionModelList.add(secretQuestionModel);
		}

		try
		{
			customer.setSecretQuestionsAndAnswersList(secretQuestionModelList);
			getModelService().save(customer);
			saved = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOG.error("Unable to save the secret questions for the user :" + customer.getUid());

		}


		return saved;

	}

	/**
	 * Update secret questions for registration.
	 * 
	 * @param secretQuestionDataList
	 *           the secret question data list
	 * @param emailId
	 *           the email id
	 * @return true, if successful
	 */
	public boolean updateSecretQuestionsForRegistration(final List<SecretQuestionData> secretQuestionDataList, final String emailId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "updateSecretQuestionsForRegistration()" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		boolean saved = false;

		for (final SecretQuestionData secretQuestionData : secretQuestionDataList)
		{
			final SecretQuestionsAndAnswersModel secretQuestionModel = new SecretQuestionsAndAnswersModel();
			secretQuestionModel.setQuestionId(secretQuestionData.getCode());
			//			secretQuestionModel.setCustomerUid(emailId);
			secretQuestionModel.setAnswer(secretQuestionData.getAnswer());

			try
			{
				getModelService().save(secretQuestionModel);
				saved = true;
			}
			catch (final ModelSavingException modelSavingException)
			{
				LOG.error("Unable to save the secret questions for the user :" + emailId);
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "updateSecretQuestionsForRegistration()" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return saved;

	}

	@Override
	public List<SecretQuestionData> getSecretQuestionsForRegistration(final String userUid)
	{
		final List<SecretQuestionData> secretQuestionDataList = new ArrayList<SecretQuestionData>();
		final List<SecretQuestionsAndAnswersModel> secretQuestions = jnJCustomerDataService.getSecretQuestionsForUser(userUid);
		for (final SecretQuestionsAndAnswersModel secretQuestion : secretQuestions)
		{
			final SecretQuestionData secretQuestionData = new SecretQuestionData();
			secretQuestionData.setCode(secretQuestion.getQuestionId());
			secretQuestionData.setAnswer(secretQuestion.getAnswer());
			try
			{
				secretQuestionData.setQuestion(messageFacade.getMessageTextForCode(secretQuestion.getQuestionId()));
			}
			catch (final BusinessException exp)
			{

				LOG.error("Error while getting secret questions list for user - " + exp.getMessage());
			}
			secretQuestionDataList.add(secretQuestionData);

		}
		return secretQuestionDataList;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.facades.customer.JnjCustomerFacade#updateCustomer(de.hybris.platform.commercefacades.user.data.CustomerData
	 * )
	 */
	@Override
	public void updateCustomer(final JnjCustomerData customerData) throws DuplicateUidException
	{
		String password = null;
		//validateParameterNotNullStandardMessage("customerData", customerData);
		Assert.hasText(customerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(customerData.getLastName(), "The field [LastName] cannot be empty");
		final JnJB2bCustomerModel customerModel;
		if (StringUtils.isEmpty(customerData.getUid()))
		{
			customerModel = this.getModelService().create(JnJB2bCustomerModel.class);
			password = JnJCommonUtil.createInitialPassword();
		}
		else
		{
			//customerModel = companyB2BCommerceService.getCustomerForUid(customerData.getUid());
			customerModel =  (JnJB2bCustomerModel) userService.getUserForUID(customerData.getUid(), B2BCustomerModel.class);
		}
		customerPopulator.populate(customerModel, customerData);

		if (customerData.getDefaultBillingAddress() == null && !StringUtils.isEmpty(customerData.getUid()))
		{
			customerData.setDefaultBillingAddress(customerData.getDefaultAddress());
		}
		if (customerData.getDefaultShippingAddress() == null && !StringUtils.isEmpty(customerData.getUid()))
		{
			customerData.setDefaultShippingAddress(customerData.getDefaultAddress());
		}
		customerData.setUid(customerData.getEmail());
		customerReversePopulator.populate(customerData, customerModel);

		//b2BCustomerReversePopulator.populate(customerData, customerModel);
		modelService.save(customerModel);
		if (StringUtils.isNotEmpty(password))
		{
			try
			{
				final JnjCustomerData customerDataForEmail = (JnjCustomerData) getCustomerConverter().convert(customerModel);
				customerDataForEmail.setSiteLoginURL(customerData.getSiteLoginURL());
				customerDataForEmail.setLogoURL(customerData.getLogoURL() + jnjCommonFacadeUtil.createMediaLogoURL());
				updateUserPassword(password, customerModel.getUid());
				eventService.publishEvent(initializeEvent(new JnjCreateUserEvent(password), customerModel, customerDataForEmail));
			}
			catch (final TokenInvalidatedException tokenInvalidException)
			{
				LOG.error("Password cannot be updated for user as token has invalidated" + customerModel.getUid());

			}
		}

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<JnjCountryData> getCountries()
	{
		final List<CountryModel> countryList = jnJCustomerDataService.getCountries();
		final List<JnjCountryData> resultList = new ArrayList<JnjCountryData>();

		if (CollectionUtils.isNotEmpty(countryList))
		{
			for (final CountryModel countryModel : countryList)
			{
				final JnjCountryData countryData = new JnjCountryData();
				countryData.setIsocode(countryModel.getIsocode());
				countryData.setName(countryModel.getName());
				countryData.setPhoneCode(countryModel.getPhoneCode());
				
				if(StringUtils.isNotBlank(countryModel.getName()) && StringUtils.isNotBlank(countryModel.getPhoneCode())){
				resultList.add(countryData);
				}
				
			}
		}
		
		Collections.sort(resultList, new Comparator<JnjCountryData>() 
        {
		@Override
		public int compare(JnjCountryData arg0, JnjCountryData arg1) {
			final String sa = arg0.getName();
	          final String sb = arg1.getName();
	          int v = 0;
	          if(sa!=null && sb!=null){
	           v = sa.compareTo(sb);
	          }
	          return v;    
         } 
         }
		);
		 
		return resultList;
	}

	@Override
	public List<JnjCountryData> getCodes(final String country)
	{
		final List<CountryModel> countryList = jnJCustomerDataService.getRegions(country);
		final List<JnjCountryData> resultList = new ArrayList<JnjCountryData>();

		if (CollectionUtils.isNotEmpty(countryList))
		{
			final JnjCountryData countryData = new JnjCountryData();
			countryData.setPhoneCode(countryList.get(0).getPhoneCode());
			resultList.add(countryData);
		}

		return resultList;
	}

	@Override
	public List<RegionData> getRegions(final String countryIso)
	{
		final CountryModel country = getCommonI18NService().getCountry(countryIso);
		final List<RegionData> resultList = new ArrayList<RegionData>();
		for (final RegionModel regionModel : country.getRegions())
		{
			final RegionData regionData = new RegionData();
			regionData.setIsocode(regionModel.getIsocode());
			regionData.setName(regionModel.getName());
			resultList.add(regionData);
		}
		return resultList;
	}

	@Override
	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	@Override
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	@Override
	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Override
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@Override
	protected ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	@Override
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	/**
	 * Gets the b2b unit service.
	 * 
	 * @return the b2b unit service
	 */
	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	/**
	 * Sets the b2b unit service.
	 * 
	 * @param b2bUnitService
	 *           the b2b unit service
	 */
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	/**
	 * Gets the i18n service.
	 * 
	 * @return the i18nService
	 */
	public I18NService getI18nService()
	{
		return i18nService;
	}

	/**
	 * Sets the i18n service.
	 * 
	 * @param i18nService
	 *           the i18nService to set
	 */
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	/**
	 * Gets the secure token service.
	 * 
	 * @return the secure token service
	 */
	protected SecureTokenService getSecureTokenService()
	{
		return secureTokenService;
	}

	/**
	 * Sets the secure token service.
	 * 
	 * @param secureTokenService
	 *           the new secure token service
	 */
	public void setSecureTokenService(final SecureTokenService secureTokenService)
	{
		this.secureTokenService = secureTokenService;
	}

	/**
	 * Gets the token validity seconds.
	 * 
	 * @return the token validity seconds
	 */
	protected long getTokenValiditySeconds()
	{
		return tokenValiditySeconds;
	}

	/**
	 * Sets the token validity seconds.
	 * 
	 * @param tokenValiditySeconds
	 *           the new token validity seconds
	 */
	public void setTokenValiditySeconds(final long tokenValiditySeconds)
	{
		if (tokenValiditySeconds < 0L)
		{
			throw new IllegalArgumentException("tokenValiditySeconds has to be >= 0");
		}
		else
		{
			this.tokenValiditySeconds = tokenValiditySeconds;

			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.customer.JnjCustomerFacade#getListOfUrls(java.lang.String)
	 */
	/*
	 * @Override public List<String> getListOfUrls(final String contentId) { final List<String> listOfURLs = null; if
	 * (cmsContentSlotService.getContentSlotForId(contentId).getCmsComponents() != null) { listOfURLs = new
	 * ArrayList<>(); final List<AbstractCMSComponentModel> list =
	 * cmsContentSlotService.getContentSlotForId(contentId).getCmsComponents(); for (int i = 0; i < list.size(); i++) {
	 * listOfURLs.add(cmsContentSlotService.getContentSlotForId(contentId).getCmsComponents().get(i).getName()); } }
	 * return listOfURLs; }
	 */
	@Override
	public List<String> getVisibleUrls(final String contentId, final HttpServletRequest request)
	{

		if (cmsContentSlotService.getContentSlotForId(contentId).getCmsComponents() != null)
		{
			final List<String> listOfURLs = new ArrayList<>();
			final List<SimpleCMSComponentModel> simpleCMSComponentList = cmsContentSlotService.getSimpleCMSComponents(
					cmsContentSlotService.getContentSlotForId(contentId), false, request);
			for (final SimpleCMSComponentModel component : simpleCMSComponentList)
			{
				listOfURLs.add(component.getUid());
			}

			return listOfURLs;
		}
		return null;
	}


	/**
	 * Initialize event.
	 * 
	 * @param event
	 *           the event
	 * @param customerModel
	 *           the customer model
	 * @param customerData
	 *           the customer data
	 * @return the jnj create user event
	 */
	protected JnjCreateUserEvent initializeEvent(final JnjCreateUserEvent event, final JnJB2bCustomerModel customerModel,
			final JnjCustomerData customerData)
	{
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer(customerModel);
		event.setLanguage(customerModel.getSessionLanguage());
		event.setCurrency(getCommonI18NService().getCurrentCurrency());
		event.setEmailNotification(customerModel.getEmailNotification().booleanValue());
		final List<String> fullName = jnjCommonFacadeUtil.getName(customerData.getFirstName() + " " + customerData.getLastName());
		customerData.setFirstName(fullName.get(0));
		if (fullName.size() > 1)
		{
			customerData.setLastName(fullName.get(1));
		}
		else
		{
			customerData.setLastName("");
		}
		event.setFirstName(customerData.getFirstName());
		event.setPassword(event.getPassword());
		event.setLastName(customerData.getLastName());
		event.setRoles(getRoles(customerData.getRoles()));
		event.setEmailID(customerModel.getEmail());
		event.setAdminUser(getUserService().getCurrentUser().getName());
		event.setPortalName(cMSSiteService.getCurrentSite().getName());
		event.setLoginUrl(customerData.getSiteLoginURL());
		event.setLogoURL(customerData.getLogoURL());

		return event;
	}

	@Override
	public Boolean isPasswordChangeRequired()
	{
		return jnJCustomerDataService.isPasswordChangeRequired(getUserService().getCurrentUser().getUid());

	}

	@Override
	public String changeUserPassword(final String oldPassword, final String newPassword) throws PasswordMismatchException
	{
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) getCurrentSessionCustomer();
		String returnValue = StringUtils.EMPTY;
		try
		{
			returnValue = verifyPassword(newPassword);
			if (returnValue.equalsIgnoreCase(Boolean.TRUE.toString()))
			{
				jnJCustomerDataService.changePassword(currentCustomer, oldPassword, newPassword);

			}
		}
		catch (final de.hybris.platform.commerceservices.customer.PasswordMismatchException passwordMismatchException)
		{
			throw new PasswordMismatchException(passwordMismatchException);
		}
		catch (final BusinessException businessException)
		{
			returnValue = businessException.getMessage();
		}

		return returnValue;
	}

	@Override
	public String verifyPassword(final String newPassword) throws PasswordMismatchException, BusinessException
	{
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) getCurrentSessionCustomer();

		List<OldPasswordModel> oldPassList = null;
		if (currentCustomer.getOldPasswords() != null && !currentCustomer.getOldPasswords().isEmpty())
		{
			oldPassList = (List<OldPasswordModel>) currentCustomer.getOldPasswords();
		}

		if (oldPassList == null || currentCustomer.getPasswordChangeDate() == null)
		{
			return Boolean.TRUE.toString();
		}
		else if (removeTimeFromDate(currentCustomer.getPasswordChangeDate()).compareTo(
				removeTimeFromDate(Calendar.getInstance().getTime())) == 0)
		{
			return messageFacade.getMessageTextForCode("password.forgotPassword.invalid.password.msg3");
		}
		else
		{
			final Calendar currentServerTime = Calendar.getInstance();
			currentServerTime.add(Calendar.DAY_OF_YEAR, -365);
			final Date currentServerDate = removeTimeFromDate(currentServerTime.getTime());
			OldPasswordModel oldPass = null;
			final String newEncodedPass = passwordEncoderService.encode(currentCustomer, newPassword,
					currentCustomer.getPasswordEncoding());
			for (int i = oldPassList.size() - 1; i >= 0; i--)
			{
				oldPass = oldPassList.get(i);
				if (oldPassList.size() - i <= 5 && oldPass.getOldPassword().equals(newEncodedPass))
				{
					return messageFacade.getMessageTextForCode("password.forgotPassword.invalid.password.msg1");
				}

				if ((!removeTimeFromDate(oldPass.getModifiedDate()).before(currentServerDate)) && oldPass.getOldPassword().equals(newPassword))
				{
					return messageFacade.getMessageTextForCode("password.forgotPassword.invalid.password.msg4");
				}
			}

			if (oldPassList.size() < 5)
			{
				return Boolean.TRUE.toString();
			}

		}
		return Boolean.TRUE.toString();
	}

	/**
	 * Removes the time from date.
	 * 
	 * @param date
	 *           the date
	 * @return the date
	 */
	Date removeTimeFromDate(final Date date)
	{
		final Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeInMillis(date.getTime());
		currentDate.set(Calendar.HOUR, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		return currentDate.getTime();
	}

	@Override
	public String verifyPassword(final String newPassword, final String email) throws PasswordMismatchException, BusinessException
	{

		//final JnJB2bCustomerModel currentCustomer = companyB2BCommerceService.getCustomerForUid(email);
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getUserForUID(email, B2BCustomerModel.class);

		List<OldPasswordModel> oldPassList = null;
		if (currentCustomer.getOldPasswords() != null && !currentCustomer.getOldPasswords().isEmpty())
		{
			oldPassList = (List<OldPasswordModel>) currentCustomer.getOldPasswords();
		}

		if (oldPassList == null)
		{
			return "true";

		}
		else if (removeTimeFromDate(currentCustomer.getPasswordChangeDate()).compareTo(
				removeTimeFromDate(Calendar.getInstance().getTime())) == 0)
		{
			return messageFacade.getMessageTextForCode("profile.password.error");
		}
		else
		{
			final Calendar currentServerTime = Calendar.getInstance();
			currentServerTime.add(Calendar.DAY_OF_YEAR, -365);
			final Date currentServerDate = removeTimeFromDate(currentServerTime.getTime());
			OldPasswordModel oldPass = null;
			final String newEncodedPass = passwordEncoderService.encode(currentCustomer, newPassword,
					currentCustomer.getPasswordEncoding());
			for (int i = oldPassList.size() - 1; i >= 0; i--)
			{
				oldPass = oldPassList.get(i);
				if ((oldPassList.size() - i <= 5 && oldPass.getOldPassword().equals(newEncodedPass))
						|| ((!removeTimeFromDate(oldPass.getModifiedDate()).before(currentServerDate)) && oldPass.getOldPassword().equals(newPassword)))
				{
					return messageFacade.getMessageTextForCode("profile.password.error");
				}
			}

			if (oldPassList.size() < 5)
			{
				return "true";
			}

		}

		return "true";
	}

	@Override
	public String getUserName(final String uid)
	{
		final JnJB2bCustomerModel jnjCUstomer = (JnJB2bCustomerModel) userService.getUserForUID(uid);
		return jnjCUstomer.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.customer.JnjCustomerFacade#createCustomer(com.jnj.facades.data.JnjCustomerData)
	 */
	@Override
	public void createCustomer(final JnjCustomerData customerData) throws DuplicateUidException
	{
		String password = null;
		Assert.hasText(customerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(customerData.getLastName(), "The field [LastName] cannot be empty");
		final JnJB2bCustomerModel customerModel;
		if (StringUtils.isEmpty(customerData.getUid()))
		{
			customerData.setUid(customerData.getEmail());
		}
		customerModel = this.getModelService().create(JnJB2bCustomerModel.class);
		password = JnJCommonUtil.createInitialPassword();
		final AddressData addressData = new AddressData();
		//set emtpy default shipping and billing addresses
		customerData.setDefaultBillingAddress(addressData);
		customerData.setDefaultShippingAddress(addressData);

		populateUserModel(customerData, customerModel);
		customerReversePopulator.populate(customerData, customerModel);
		modelService.save(customerModel);
		try
		{
			final JnjCustomerData customerDataForEmail = (JnjCustomerData) getCustomerConverter().convert(customerModel);
			customerDataForEmail.setSiteLoginURL(customerData.getSiteLoginURL());
			customerDataForEmail.setLogoURL(customerData.getLogoURL() + jnjCommonFacadeUtil.createMediaLogoURL());
			updateUserPassword(password, customerModel.getUid());
			eventService.publishEvent(initializeEvent(new JnjCreateUserEvent(password), customerModel, customerDataForEmail));
			customerModel.setRegistrationEmailSent(Boolean.TRUE);
			getModelService().save(customerModel);
		}
		catch (final TokenInvalidatedException tokenInvalidException)
		{
			LOG.error("Password cannot be updated for user as token has invalidated" + customerModel.getUid());

		}
	}

	@Override
	public boolean isAdminUser(final String userId)
	{
		boolean returnVal = false;
		JnJB2bCustomerModel customerModel = null;
		final UserModel userModel = userService.getUserForUID(userId);
		if (userModel instanceof JnJB2bCustomerModel)
		{
			customerModel = (JnJB2bCustomerModel) userModel;
			for (final PrincipalGroupModel userGroup : customerModel.getAllGroups())
			{
				if (userGroup.getUid().equalsIgnoreCase(Jnjb2bCoreConstants.UserRoles.USER_ADMIN_GROUP))
				{
					returnVal = true;
					break;
				}
			}
		}

		return returnVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.customer.JnjCustomerFacade#getSecretQuestionsForUser(java.lang.String)
	 */
	@Override
	public List<SecretQuestionData> getSecretQuestionsForUser(final String email)
	{

		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(email);
		final List<SecretQuestionsAndAnswersModel> secretQuestions = customer.getSecretQuestionsAndAnswersList();
		final List<SecretQuestionData> secretQuestionDataList = new ArrayList<SecretQuestionData>();
		for (final SecretQuestionsAndAnswersModel secretQuestion : secretQuestions)
		{
			final SecretQuestionData secretQuestionData = new SecretQuestionData();
			secretQuestionData.setCode(secretQuestion.getQuestionId());
			secretQuestionData.setAnswer(secretQuestion.getAnswer());
			try
			{
				secretQuestionData.setQuestion(messageFacade.getMessageTextForCode(secretQuestion.getQuestionId()));
			}
			catch (final BusinessException exp)
			{

				LOG.error("Error while getting secret questions list for user - " + exp.getMessage());
			}
			secretQuestionDataList.add(secretQuestionData);

		}
		return secretQuestionDataList;
	}


	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Map<String, String> getAccountsMap(final boolean addCurrentB2BUnit)
	{
		return jnjCustomerService.getAccountsMap(addCurrentB2BUnit);
	}

	/**
	 * Gets the roles.
	 * 
	 * @param roles
	 *           the roles
	 * @return the roles
	 */
	private List<String> getRoles(final Collection<String> roles)
	{
		final ArrayList listOfRoles = new ArrayList<String>();
		for (final String role : roles)
		{
			listOfRoles.add(role);
		}
		return listOfRoles;
	}

	@Override
	public boolean checkLegalPrivacyVersions()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "checkLegalPrivacyVersions()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + System.currentTimeMillis());
		}
		boolean versionCheck = false;
		final JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "checkLegalPrivacyVersions()" + Logging.HYPHEN
						+ "Fetcing LegalNoticeComponent and PrivacyPolicyComponent");
			}
			final JnjParagraphComponentModel legalNoticeParagraphComponentModel = cMSComponentService.getAbstractCMSComponent(Config
					.getParameter(Jnjb2bCoreConstants.Login.LEGAL_NOTICE_COMPONENT_UID_KEY));
			final JnjParagraphComponentModel privacyPolicyParagraphComponentModel = cMSComponentService
					.getAbstractCMSComponent(Config.getParameter(Jnjb2bCoreConstants.Login.PRIVACY_POLICY_COMPONENT_UID_KEY));

			if (null != jnJB2bCustomerModel.getLegalNoticeVersion() && null != jnJB2bCustomerModel.getPrivacyPolicyVersion()
					&& jnJB2bCustomerModel.getLegalNoticeVersion() == legalNoticeParagraphComponentModel.getComponentVersion()
					&& jnJB2bCustomerModel.getPrivacyPolicyVersion() == privacyPolicyParagraphComponentModel.getComponentVersion())
			{
				LOG.info(Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "checkLegalPrivacyVersions()" + Logging.HYPHEN
						+ "LegalNoticeComponent and PrivacyPolicyComponent Versions in Sync with the Logged in User: ["
						+ jnJB2bCustomerModel.getCustomerID() + "]");
				versionCheck = true;
			}
			else
			{
				LOG.info(Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "checkLegalPrivacyVersions()" + Logging.HYPHEN
						+ "LegalNoticeComponent and PrivacyPolicyComponent Versions out of Sync with the Logged in User: ["
						+ jnJB2bCustomerModel.getCustomerID() + "]");
			}

		}
		catch (final CMSItemNotFoundException cMSItemNotFoundException)
		{
			LOG.error(Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "checkLegalPrivacyVersions()" + Logging.HYPHEN
					+ "Unable to fetch LegalNoticeComponent or PrivacyPolicyComponent for the Logged in User: ["
					+ jnJB2bCustomerModel.getCustomerID() + "] with excweption: " + cMSItemNotFoundException);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "checkLegalPrivacyVersions()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + System.currentTimeMillis());
		}
		return versionCheck;
	}





	@Override
	public void sendAccountActiveEmail()
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.SENT_ACCOUNT_ACIVE_EMAIL + Logging.HYPHEN + "sendAccountActiveEmail()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD);
		}
		final List<JnJB2bCustomerModel> usersForEmailNotification = new ArrayList<JnJB2bCustomerModel>();
		final List<JnJB2bCustomerModel> newlyRegisteredUsers = jnjCustomerService.getNewlyRegisteredUsers();
		final JnJB2BUnitModel jnjDummyUnit = (JnJB2BUnitModel) getB2bUnitService().getUnitForUid("JnJDummyUnit");
		for (final JnJB2bCustomerModel user : newlyRegisteredUsers)
		{

			final Set<PrincipalGroupModel> compositeSubGroups = user.getGroups();
			if (!compositeSubGroups.contains(jnjDummyUnit))
			{
				usersForEmailNotification.add(user);
			}
		}

		for (final JnJB2bCustomerModel user : usersForEmailNotification)
		{

			//final JnJB2bCustomerModel customerModel = companyB2BCommerceService.getCustomerForUid(user.getUid());
			final JnJB2bCustomerModel customerModel = (JnJB2bCustomerModel) userService.getUserForUID(user.getUid(), B2BCustomerModel.class);
			eventService.publishEvent(initializeCreateuserEvent(new JnjCreateUserEvent(), customerModel));
			customerModel.setRegistrationEmailSent(Boolean.TRUE);
			getModelService().save(customerModel);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.SENT_ACCOUNT_ACIVE_EMAIL + Logging.HYPHEN + "sendAccountActiveEmail()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD);
		}

	}



	private JnjCreateUserEvent initializeCreateuserEvent(final JnjCreateUserEvent event, final JnJB2bCustomerModel customerModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.INTIALIZE_CREATE_USER_EVENT + Logging.HYPHEN + "sendAccountActiveEmail()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD);
		}
		CMSSiteModel currentSiteForUser = null;
		final Set<PrincipalGroupModel> compositeSubGroups = customerModel.getGroups();
		for (final PrincipalGroupModel group : compositeSubGroups)
		{
			if (group instanceof JnJB2BUnitModel)
			{
				final CountryModel country = ((JnJB2BUnitModel) group).getCountry();
				if (country != null)
				{
					event.setBaseStore(baseStoreService.getBaseStoreForUid(country.getIsocode().toLowerCase()
							+ Jnjb2bCoreConstants.UserRoles.BASE_STORE));
					currentSiteForUser = (CMSSiteModel) baseSiteService.getBaseSiteForUID(country.getIsocode().toLowerCase()
							+ Jnjb2bCoreConstants.UserRoles.CMSSITE);
					event.setSite(currentSiteForUser);
					event.setPortalName(baseSiteService.getBaseSiteForUID(
							country.getIsocode().toLowerCase() + Jnjb2bCoreConstants.UserRoles.CMSSITE).getName());
				}
			}
		}
		event.setCustomer(customerModel);
		event.setLanguage(getCommonI18NService().getCurrentLanguage());
		event.setCurrency(getCommonI18NService().getCurrentCurrency());
		if (customerModel.getEmailNotification() != null)
		{
			event.setEmailNotification(customerModel.getEmailNotification().booleanValue());
		}
		final JnjCustomerData customerData = new JnjCustomerData();
		customerPopulator.populate(customerModel, customerData);
		event.setFirstName(customerData.getFirstName());
		//event.setMobile(customerModel.get);
		event.setPassword(event.getPassword());
		event.setLastName(customerData.getLastName());
		//event.setPhone(phone);
		event.setRoles(getUserRolesForEmail(customerModel));
		event.setEmailID(customerModel.getEmail());
		event.setAdminUser(getUserService().getCurrentUser().getName());
		//event.setRegistrationEmailSent(customerData.getRistrationEmailSent());
		if (currentSiteForUser != null)
		{
			final CatalogVersionModel currentCatalog = currentSiteForUser.getContentCatalogs().get(0).getActiveCatalogVersion();
			event.setLogoURL(Config.getParameter(Jnjb2bCoreConstants.UserCreation.HOST_NAME)
					+ mediaService.getMedia(currentCatalog, "siteLogoImage").getURL());
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.SENT_ACCOUNT_ACIVE_EMAIL + Logging.HYPHEN + "sendAccountActiveEmail()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD);
		}
		return event;

	}






	private List<String> getUserRolesForEmail(final JnJB2bCustomerModel customerModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.GET_USER_ROLES_FOR_APPROVAL_EMAIL + Logging.HYPHEN + "getUserRolesForEmail()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD);
		}
		final Set<PrincipalGroupModel> userGroups = customerModel.getGroups();
		Set<PrincipalGroupModel> compositeSubGroups = new HashSet<PrincipalGroupModel>();

		for (final PrincipalGroupModel userGroup : userGroups)
		{
			if (userGroup instanceof UserGroupModel && !(userGroup instanceof JnJB2BUnitModel)
					&& !userGroup.getUid().equalsIgnoreCase(Jnjb2bCoreConstants.UserCreation.B2B_CUSTOMER_ID))
			{
				final UserGroupModel compositeGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID(userGroup.getUid(),
						UserGroupModel.class);
				compositeSubGroups = compositeGroupModel.getGroups();
				LOG.debug("map.. composite group.. " + compositeGroupModel.getGroups());
				break;
			}
		}
		final ArrayList listOfRoles = new ArrayList<String>();
		for (final PrincipalGroupModel compositeSubGroup : compositeSubGroups)
		{

			listOfRoles.add(compositeSubGroup.getLocName());
			LOG.debug("compositeSubGroup.. loc Name is " + compositeSubGroup.getLocName());
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.GET_USER_ROLES_FOR_APPROVAL_EMAIL + Logging.HYPHEN + "getUserRolesForEmail()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD);
		}
		LOG.debug("list of Role size.." + listOfRoles.size());
		return listOfRoles;
	}

	@Override
	public boolean updateLegalPrivacyPolicyVersion()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.LEGACY_POLICY_CHECK + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "updateLegalPrivacyPolicyVersion()" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		boolean versionUpdate = false;
		final JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();
		setLegalPrivacyVersions(jnJB2bCustomerModel);
		versionUpdate = jnjCustomerService.saveJnjB2bCustomer(jnJB2bCustomerModel);

		if (versionUpdate)
		{
			LOG.info(Jnjb2bCoreConstants.Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "updateLegalPrivacyPolicyVersion()"
					+ Logging.HYPHEN + "Legal Notice AND Privacy Policy Updated Successfully.");
		}
		else
		{
			LOG.error(Jnjb2bCoreConstants.Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "updateLegalPrivacyPolicyVersion()"
					+ Logging.HYPHEN + "Legal Notice AND Privacy Policy Updation FAILED.");
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.LEGACY_POLICY_CHECK + Logging.HYPHEN + "updateLegalPrivacyPolicyVersion()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + System.currentTimeMillis());
		}
		return versionUpdate;
	}

	/**
	 * This method gets list of uid, lastEmailSent For the Interval, last password change date
	 * 
	 * @return
	 */
	@Override
	public Set<JnJB2bCustomerModel> getAllJnjUsers()
	{
		return jnjCustomerService.getAllJnjUsers();
	}

	@Override
	public void sendPasswordExpiryMail()
	{
		Integer lastInterval = Integer.valueOf(0);
		Integer sendMailOnDay = Integer.valueOf(0);
		LOG.debug("START: Iterate.. List all JnjUsers");
		if (jnjCustomerService.getAllJnjUsers().size() > 0)
		{
			for (final JnJB2bCustomerModel emailRecipent : jnjCustomerService.getAllJnjUsers())
			{				
				final String[] interval = Config.getParameter(INTERVAL_LIST_FOR_EMAIL).split(Jnjb2bCoreConstants.CONST_COMMA);
				LOG.debug("List of interval is "+interval);
				LOG.debug("Last interval is " + emailRecipent.getMailSentForInterval());
				final SortedSet<String> intervals = new TreeSet<String>(Arrays.asList(interval));
				if (emailRecipent.getMailSentForInterval() != null && emailRecipent.getMailSentForInterval().intValue() > 0)
				{
					lastInterval = emailRecipent.getMailSentForInterval();					
					sendMailOnDay = Integer.valueOf(intervals.headSet(lastInterval.toString()).last().toString());
				}
				else
				{
					sendMailOnDay = Integer.valueOf(Jnjb2bCoreConstants.PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS
							+ daysBetween(Calendar.getInstance().getTime(), emailRecipent.getPasswordChangeDate()));
				}
				LOG.debug("START : Publishing JnjPasswordExpiryEvent ");
				eventService.publishEvent(initializeEvent(new JnjPasswordExpiryEvent(), emailRecipent, sendMailOnDay));
				LOG.debug("END : Publishing JnjPasswordExpiryEvent ");
			}
		}
		LOG.debug("END: Iterate.. List all JnjUsers");
	}

	/**
	 * 
	 * @param event
	 * @param customerModel
	 * @return
	 */
	protected JnjPasswordExpiryEvent initializeEvent(final JnjPasswordExpiryEvent event, final JnJB2bCustomerModel customerModel,
			final Integer daysBeforePasswordExpiry)
	{
		
		
		
		event.setBaseStore(baseStoreService.getBaseStoreForUid(Jnjb2bCoreConstants.MDD_STORE_ID));
		event.setSite(baseSiteService.getBaseSiteForUID(Jnjb2bCoreConstants.MDD_SITE_ID));
		
		//event.setBaseStore(baseStoreService.getCurrentBaseStore());
		//event.setSite(baseSiteService.getCurrentBaseSite());
		
		/*CMSSiteModel currentSiteForUser = null;
		final Set<PrincipalGroupModel> compositeSubGroups = customerModel.getGroups();
		for (final PrincipalGroupModel group : compositeSubGroups)
		{
			if (group instanceof JnJB2BUnitModel)
			{
				final CountryModel country = ((JnJB2BUnitModel) group).getCountry();
				if (country != null)
				{
					event.setBaseStore(baseStoreService.getBaseStoreForUid(country.getIsocode().toLowerCase()
							+ Jnjb2bCoreConstants.UserRoles.BASE_STORE));
					currentSiteForUser = (CMSSiteModel) baseSiteService.getBaseSiteForUID(country.getIsocode().toLowerCase()
							+ Jnjb2bCoreConstants.UserRoles.CMSSITE);
					event.setSite(currentSiteForUser);
					event.setPortalName(baseSiteService.getBaseSiteForUID(
							country.getIsocode().toLowerCase() + Jnjb2bCoreConstants.UserRoles.CMSSITE).getName());
				}
			}
		}*/
		event.setCustomer(customerModel);
		event.setLanguage(customerModel.getSessionLanguage());
		//event.setSecurityWindow(jnjCommonFacadeUtil.getMessageFromImpex(PASSWORD_EXPIRY_WINDOW));
		event.setDaysBeforePasswordExpiry(daysBeforePasswordExpiry);
		event.setEmailID(customerModel.getEmail());
		event.setUserFirstName(customerModel.getName());
		if (cMSSiteService != null && cMSSiteService.getCurrentSite() != null)
		LOG.debug("Password Expiry.. Customer Name.. " + customerModel.getName());
		event.setUserLastName(customerModel.getName());
		event.setCustomerName(customerModel.getName());
		event.setUserRoles(getUserRolesForEmail(customerModel));
		event.setEmailNotifications(customerModel.getEmailNotification());
		/*if (cMSSiteService != null && cMSSiteService.getCurrentSite() != null)
		{
			event.setPortalName(cMSSiteService.getCurrentSite().getName());			
		}*/
		event.setPortalName(event.getSite().getName());
		LOG.debug("portal nm... " + event.getPortalName());

		return event;
	}

	protected int daysBetween(final Date earlyDate, final Date nextDate)
	{
		return (int) ((nextDate.getTime() - earlyDate.getTime()) / (1000 * 60 * 60 * 24));
	}


	/**
	 * This method will accept the Uid and perform validation on it.
	 * 
	 * @param uid
	 * @return true/false
	 */
	@Override
	public boolean validateUid(final String uid)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "validateUid()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "validateUid()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return userService.isUserExisting(uid);
	}

	/**
	 * This method validates the user entered secret questions and answers with the ones stored in the database.
	 * 
	 * @param uid
	 * @param userEnteredSecretQuestionsData
	 * 
	 * @return validationStatus
	 */
	@Override
	public boolean validateSecretQuestionsAndAnswers(final String uid,
			final List<SecretQuestionData> userEnteredSecretQuestionsData)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "validateUid()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		boolean validationStatus = false;
		final List<SecretQuestionData> secretQuestionsForUserFromDatabase = getSecretQuestionsForUser();
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "validateUid()"
					+ "Questions retrieved from database for the user :: " + uid + ", are :: " + secretQuestionsForUserFromDatabase);
		}
		if (null != secretQuestionsForUserFromDatabase && !secretQuestionsForUserFromDatabase.isEmpty()
				&& userEnteredSecretQuestionsData != null && !userEnteredSecretQuestionsData.isEmpty())
		{
			for (final SecretQuestionData secretQuestionData : secretQuestionsForUserFromDatabase)
			{
				if (userEnteredSecretQuestionsData.isEmpty())
				{
					break;
				}
				for (int i = 0; i < userEnteredSecretQuestionsData.size(); i++)
				{
					if (userEnteredSecretQuestionsData.get(i).getCode().equalsIgnoreCase(secretQuestionData.getCode())
							&& userEnteredSecretQuestionsData.get(i).getAnswer().equals(secretQuestionData.getAnswer()))
					{
						userEnteredSecretQuestionsData.remove(i);
						break;
					}
				}
			}
			if (userEnteredSecretQuestionsData.isEmpty())
			{
				validationStatus = true;
			}
		}
		else
		{
			LOG.warn("Secret Questions null or empty! secretQuestionsForUserFromDatabase :: " + secretQuestionsForUserFromDatabase
					+ "; userEnteredSecretQuestionsData :: " + userEnteredSecretQuestionsData);
		}
		LOG.info("Secret questions validation result :: " + validationStatus);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "validateUid()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return validationStatus;
	}


	/**
	 * This method performs password update
	 * 
	 * @param newPassword
	 * @param email
	 */
	@Override
	public void updateUserPassword(final String newPassword, final String email) throws TokenInvalidatedException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.FORGOT_PASSWORD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + "updateUserPassword()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final JnJB2bCustomerModel b2bCustomerModel = userService.getUserForUID(email.toLowerCase(), JnJB2bCustomerModel.class);
		final long timeStamp = getTokenValiditySeconds() <= 0L ? 0L : (new Date()).getTime();
		LOG.debug(Jnjb2bCoreConstants.Logging.FORGOT_PASSWORD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + "updateUserPassword()"
				+ "Timestamp generated :: " + email);
		final SecureToken data = new SecureToken(b2bCustomerModel.getUid(), timeStamp);
		final String token = secureTokenService.encryptData(data);
		b2bCustomerModel.setToken(token);
		updatePasswordWithToken(newPassword, b2bCustomerModel, token);
		LOG.info("New password saved successfully.");

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.FORGOT_PASSWORD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + "updateUserPassword()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}



	/**
	 * @param newPassword
	 * @param b2bCustomerModel
	 * @param token
	 * @throws TokenInvalidatedException
	 */
	@Override
	public void updatePasswordWithToken(final String newPassword, final JnJB2bCustomerModel b2bCustomerModel, final String token)
			throws TokenInvalidatedException
	{
		b2bCustomerModel.setLoginDisabled(false);
		jnjCustomerService.updatePassword(token, newPassword);
		final List<OldPasswordModel> oldPasswordsList = new ArrayList<OldPasswordModel>();
		oldPasswordsList.addAll(b2bCustomerModel.getOldPasswords());
		final OldPasswordModel oldPasswordModel = new OldPasswordModel();
		oldPasswordModel.setOldPassword(b2bCustomerModel.getEncodedPassword());
		oldPasswordModel.setModifiedDate(Calendar.getInstance().getTime());
		oldPasswordModel.setJnjCustomer(b2bCustomerModel);
		oldPasswordsList.add(oldPasswordModel);
		b2bCustomerModel.setOldPasswords(oldPasswordsList);
		b2bCustomerModel.setPasswordChangeDate(oldPasswordModel.getModifiedDate());
		b2bCustomerModel.setIsResetPassword(true); // make a indication reset password is is done
		if (b2bCustomerModel.getForcefulExpired())
		{
			b2bCustomerModel.setForcefulExpired(Boolean.FALSE);
			//Setting session attribute to validate if user has reset the password and shouldn't navigate to home page - Hotfix
			getSessionService().setAttribute(Jnjb2bCoreConstants.EXPIRED_PWD_RESET, Boolean.TRUE);
		}
		modelService.save(oldPasswordModel);
		modelService.save(b2bCustomerModel);
		modelService.refresh(oldPasswordModel);
		modelService.refresh(b2bCustomerModel);
	}

}
