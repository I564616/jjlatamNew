/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalprofile.controllers.pages;


import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Collections;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants;
import com.jnj.b2b.jnjglobalprofile.controllers.JnjglobalprofileControllerConstants;
import com.jnj.b2b.storefront.controllers.pages.AccountPageController;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.SecretQuestionData;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.b2b.storefront.forms.UpdatePasswordForm;
import com.jnj.b2b.jnjglobalprofile.constants.JnjglobalprofileConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjCompanyInfoData;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.converters.populator.customer.JnjGTCustomerReversePopulator;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.b2b.jnjglobalprofile.forms.JnjGTAddAccountForm;
import com.jnj.b2b.jnjglobalprofile.forms.JnjGTDefaultAccAndOrderForm;
import com.jnj.b2b.jnjglobalprofile.forms.JnjGTProfileForm;
import com.jnj.b2b.jnjglobalprofile.forms.JnjGTSecretQuestionForm;


/**
 * Controller for home page.
 */
public class JnjGTAccountPageController extends AccountPageController
{

	/**  */
	protected static final String STATUS = "status";

	/**  */
	protected static final String SUCCESS = "success";
	
	protected static final String SUCCESSPWD = "successPwd";
	
	protected static final String SUCCESSQUE = "successQue";
	
	protected static final String SUCCESSACC = "successAcc";
	
	protected static final String SUCCESSCHK = "successChk";
	
	protected static final String SUCCESSEMAIL = "successEmail";
	
	protected static final String DUPLICATE_VALUES= "duplicateValues";
	/**  */
	protected static final String PASS_MISMATCH = "passMismatch";

	/**  */
	protected static final String OLD_PASSWORD = "oldPassword";

	/**
	 *
	 */
	protected static final String STRING_HYPEN = "-";
	protected static final String VIEWONLY = "viewOnly";
	protected static final String PLACEORDER = "placeOrder";
	protected static final String NOCHARGE = "noCharge";
	
	
	protected static final String PLACEORDER_SALES_GROUP = "placeOrderSalesGroup";
	protected static final String PLACEORDER_BUYER_GROUP = "placeOrderBuyerGroup";
	protected static final String KEY_EMAIL_PREFERENCE_15 = "emailPreference15";
	protected static final String KEY_EMAIL_PREFERENCE_16  = "emailPreference16";
	
	//Added constants for AAOL-4660 Default Order Type
	protected static final String ORDER_TYPE_LIST = "orderTypeList";
	protected static final String SUCCESSDFLT = "successdflt";

	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	// CMS Pages
	//private static final String ACCOUNT_CMS_PAGE = "account";
	protected static final String PROFILE_CMS_PAGE = "personalInformation";
	protected static final String EMAIL_PREFRENCES_CMS_PAGE = "emailPreferences";
	protected static final String ADD_ACCOUNT_CMS_PAGE = "addAccount";
	protected static final String CHANGE_PASSWORD_CMS_PAGE = "changePassword";
	protected static final String CHANGE_SECURITY_QUESTION_CMS_PAGE = "changeSecurityQuestion";
	//Changes for AAOL-4659,4660
	protected static final String DEFAULT_ACCOUNT_ORDER ="defaultAccAndOrder";
	protected final String MyProfile_LABEL = "Myprofile.myprofile.text";
	protected final String Addaccount_LABEL = "Myprofile.addaccount.text";
	protected final String EMAIL_PREFERENCES_LABEL = "Myprofile.emailprefernces.text";
	protected final String PERSONAL_INFORMATION_LABEL = "Myprofile.personalinformation.text";
	protected final String SECURITY_QUESTIONS_LABEL = "Myprofile.securityquestions.text";
	protected final String CHANGE_PASSWORD_LABEL = "Myprofile.changepassword.text";
	protected final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";

	protected static final Logger LOG = Logger.getLogger(JnjGTAccountPageController.class);

	@Resource(name = "b2bOrderFacade")
	protected B2BOrderFacade orderFacade;
	
	@Resource(name = "checkoutFacade")
	protected CheckoutFacade checkoutFacade;

	@Resource(name = "userFacade")
	protected UserFacade userFacade;

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade customerFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	@Autowired
	@Qualifier(value = "customerReversePopulator")
	protected JnjGTCustomerReversePopulator customerReversePopulator;

	@Autowired
	protected UserService userService;
	
	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	/** The session service **/
	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected ModelService modelService;
	
	@Resource(name = "GTB2BUnitFacade")
	protected JnjGTB2BUnitFacade jnjGTUnitFacade;
	
	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}

	public B2BOrderFacade getOrderFacade() {
		return orderFacade;
	}

	public CheckoutFacade getCheckoutFacade() {
		return checkoutFacade;
	}

	public UserFacade getUserFacade() {
		return userFacade;
	}

	public JnjGTCustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	public ResourceBreadcrumbBuilder getAccountBreadcrumbBuilder() {
		return accountBreadcrumbBuilder;
	}

	public EnumerationService getEnumerationService() {
		return enumerationService;
	}

	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public JnjGTCustomerReversePopulator getCustomerReversePopulator() {
		return customerReversePopulator;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public SessionService getSessionService() {
		return sessionService;
	}
	/**
	 * This method is responsible for bringing the My Profile Page
	 * 
	 * @param Model
	 * @throws DuplicateUidException
	 * 
	 */
	@GetMapping("/personalInformation")
	@RequireHardLogIn
	public String getprofile(final Model model, final HttpServletRequest request) throws CMSItemNotFoundException,
			DuplicateUidException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "getprofile()";
		logMethodStartOrEnd(Logging.PERSONAL_INFORMATION, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		setUpDatAForPersonalInformationPage(model);
		
		//Chnages for Serialization.
		boolean isSerialUser = false;
		
		if((JnjGTUserTypes.SERIAL_USER.equals(sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE)))){
			isSerialUser = true;
		}
		model.addAttribute("isSerialUser",isSerialUser);
		
		logMethodStartOrEnd(Logging.EMAIL_PREFERENCES, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnjGTCustomerData customerData = (JnjGTCustomerData) customerFacade.getCurrentCustomer();
		setUpPageForEmailPrefrences(model, customerData);
		
		//Changes for AAOL-4660 - Default Order Type
		List<String> orderTypeList = getOrderTypeListForUser(customerData.getB2bUnits());
		model.addAttribute(ORDER_TYPE_LIST,orderTypeList);
		
		model.addAttribute("customerData", customerData);
		model.addAttribute("updatePasswordForm", new UpdatePasswordForm());
		
		
		
		logMethodStartOrEnd(Logging.Get_SECURITY_Question, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnjGTSecretQuestionForm secretQuestionForm = new JnjGTSecretQuestionForm();
		final List<SecretQuestionData> secretQuestionDataList = customerFacade.getSecretQuestionsForUser();
		if (CollectionUtils.isNotEmpty(secretQuestionDataList))
		{
			secretQuestionForm.setQuestionList(secretQuestionDataList);
		}
		model.addAttribute("secretQuestions", getAllSecretQuestions());
		model.addAttribute("secretQuestionForm", secretQuestionForm);
		
		
		
		model.addAttribute("checkPassword", new UpdatePasswordForm());
		model.addAttribute("defaultAccAndOrderForm", new JnjGTDefaultAccAndOrderForm());
		
		logMethodStartOrEnd(Logging.ADD_ACCOUNT, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		setUpPageForAddNewAccount(model);
		final JnjGTAddAccountForm addAccountForm = new JnjGTAddAccountForm();
		addAccountForm.setSector(JnjglobalprofileConstants.SalesAlignment.MDD);
		model.addAttribute("addaccountForm", addAccountForm);
		
		
		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.PERSONAL_INFORMATION_LINK_COMPONENT_ID);
		logMethodStartOrEnd(Logging.PERSONAL_INFORMATION, METHOD_NAME, Logging.END_OF_METHOD);
		createBreadCrumbsForProfile(null, model, jnjCommonFacadeUtil.getMessageFromImpex(MyProfile_LABEL));
		return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountProfilePage);
	}

	protected List<String> getOrderTypeListForUser(List<B2BUnitData> b2bUnitList) {
		
		Set<String> avilableOrderTypes = new HashSet<String>();
		if(b2bUnitList!=null && !(b2bUnitList.isEmpty())){
			avilableOrderTypes = jnjGTUnitFacade.getAvailableOrderTypes(b2bUnitList);		}
		
		List<String> orderTypeList  = new ArrayList<>(avilableOrderTypes);
		
		return orderTypeList;
	}

	/**
	 * This method is responsible for setting up the Data for My Profile
	 * 
	 * @param model
	 */
	protected void setUpDatAForPersonalInformationPage(final Model model)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "setUpDatAForPersonalInformationPage()";
		logMethodStartOrEnd(Logging.PERSONAL_INFORMATION, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnjGTCustomerData customerData = (JnjGTCustomerData) customerFacade.getCurrentCustomer();
		final Map<String, String> regionList = new HashMap();
		final Map<String, String> roleMap = new HashMap<String, String>();
		AddressData contactAddress = customerData.getContactAddress();
		if(contactAddress != null) {
			if(contactAddress.getCountry() != null){ // checking null pointer exception from coming first time login by parthiban
   			CountryData country = contactAddress.getCountry();
   			String countryIsoCode = country.getIsocode();
   			final List<RegionData> regionDataList = customerFacade.getRegions(customerData.getContactAddress().getCountry().getIsocode());
   				
      		for (final RegionData region : regionDataList) {
      			regionList.put(region.getIsocode(), region.getName());
      		}
			}
		}
		final List<String> roles = (List<String>) customerData.getRoles();
		for (final String role : roles)
		{
			if (!role.equalsIgnoreCase("b2bcustomergroup"))
			{
				roleMap.put(role, jnjCommonFacadeUtil.getMessageFromImpex("role." + role));
			}
		}
		model.addAttribute("countryCodes", jnjGTCustomerFacade.getPhoneCodes());
		model.addAttribute("regionList", regionList);
		model.addAttribute("roleMap", roleMap);
		model.addAttribute("updateProfileForm", new JnjGTProfileForm());
		model.addAttribute("departments", customerFacade.getDepartments());
		model.addAttribute("customerData", customerData);
		model.addAttribute("countryList", customerFacade.getCountries());
		logMethodStartOrEnd(Logging.PERSONAL_INFORMATION, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method is responsible for saving the My Profile Updates Done By The User
	 * 
	 * @param model
	 */

	@PostMapping("/personalInformation")
	@RequireHardLogIn
	public String saveProfile(@Valid final JnjGTProfileForm jnjGTProfileForm, final BindingResult bindingResult, final Model model,RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, DuplicateUidException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "saveProfile()";
		logMethodStartOrEnd(Logging.PERSONAL_INFORMATION, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnjGTCustomerData customerData = (JnjGTCustomerData) customerFacade.getCurrentCustomer();
		
		if(customerData!=null){
			customerData.setUid(customerFacade.getCurrentCustomer().getUid());
			final JnjGTAddressData addressData = (JnjGTAddressData) customerData.getContactAddress();
			
			if(addressData!=null){
				addressData.setDefaultAddress(true);
				addressData.setLine1(jnjGTProfileForm.getAddressLine1());
				addressData.setLine2(jnjGTProfileForm.getAddressLine2());
				if (StringUtils.isNotEmpty(jnjGTProfileForm.getPhoneNumberPrefix()) && StringUtils.isNotEmpty(jnjGTProfileForm.getPhone()))
				{
					addressData.setPhone(jnjGTProfileForm.getPhoneNumberPrefix() + JnjglobalprofileConstants.SYMBOl_PIPE
							+ jnjGTProfileForm.getPhone());
				}
				else
				{
					addressData.setPhone(jnjGTProfileForm.getPhone());
				}

			//	addressData.setPhone(jnjGTProfileForm.getPhoneNumberPrefix() + JnjglobalprofileConstants.SYMBOl_PIPE + jnjGTProfileForm.getPhone());
				addressData.setTown(jnjGTProfileForm.getCity());
				addressData.setPostalCode(jnjGTProfileForm.getZip());
				if (StringUtils.isNotEmpty(jnjGTProfileForm.getMobileNumberPrefix())
						|| StringUtils.isNotEmpty(jnjGTProfileForm.getMobile()))
				{
					addressData.setMobile(jnjGTProfileForm.getMobileNumberPrefix() + JnjglobalprofileConstants.SYMBOl_PIPE
							+ jnjGTProfileForm.getMobile());
				}
				if (StringUtils.isNotEmpty(jnjGTProfileForm.getFaxNumberPrefix()) || StringUtils.isNotEmpty(jnjGTProfileForm.getFax()))
				{
					addressData
							.setFax(jnjGTProfileForm.getFaxNumberPrefix() + JnjglobalprofileConstants.SYMBOl_PIPE + jnjGTProfileForm.getFax());
				}
				/*if (jnjGTProfileForm.getCountry().equalsIgnoreCase(JnjglobalprofileConstants.US))
				{
					final RegionData region = new RegionData();
					region.setIsocode(jnjGTProfileForm.getState());
					addressData.setRegion(region);
				}else{
					addressData.setRegion(null);
				}*/
				String state = null;
				if(jnjGTProfileForm.getState() != null && !jnjGTProfileForm.getState().isEmpty()){
				if(jnjGTProfileForm.getState().charAt(0) == ','){
					state = jnjGTProfileForm.getState().substring(1);
					addressData.setState(state);
				}else{
					addressData.setState(jnjGTProfileForm.getState());
				}
				}
				
				CountryData country = new CountryData();
				country.setIsocode(jnjGTProfileForm.getCountry());
				addressData.setCountry(country);
				customerData.setContactAddress(addressData);
			}
			
			customerData.setSupervisorName(jnjGTProfileForm.getSupervisorName());
			customerData.setSupervisorEmail(jnjGTProfileForm.getSupervisorEmail());
			
			if (StringUtils.isNotEmpty(jnjGTProfileForm.getSupervisorPhoneCode()) && StringUtils.isNotEmpty(jnjGTProfileForm.getSupervisorPhone()))
			{
				customerData.setSupervisorPhone(jnjGTProfileForm.getSupervisorPhoneCode() + JnjglobalprofileConstants.SYMBOl_PIPE
						+ jnjGTProfileForm.getSupervisorPhone());
			}
			else
			{
				customerData.setSupervisorPhone(jnjGTProfileForm.getSupervisorPhone());
			}
			
			customerData.setSupervisorPhoneCode(jnjGTProfileForm.getSupervisorPhoneCode());
			customerData.setOrgName(jnjGTProfileForm.getOrgName());
			customerData.setDepartment(jnjGTProfileForm.getDepartment());
			customerData.setFirstName(jnjGTProfileForm.getFirstName());
			customerData.setLastName(jnjGTProfileForm.getLastName());
			try
			{
				customerFacade.updateCustomer(customerData);
				model.addAttribute(SUCCESS, Boolean.valueOf(true));
				redirectAttributes.addFlashAttribute(SUCCESS,  Boolean.valueOf(true));

			}
			catch (final DuplicateUidException exception)
			{
				model.addAttribute(SUCCESS, Boolean.valueOf(true));
				LOG.error("Profile not saved sucessfully :: " + exception.getMessage());
			}
		}
		
		setUpDatAForPersonalInformationPage(model);
		model.addAttribute("countryCodes", jnjGTCustomerFacade.getPhoneCodes());
		redirectAttributes.addFlashAttribute("countryCodes",  jnjGTCustomerFacade.getPhoneCodes());
		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.PERSONAL_INFORMATION_LINK_COMPONENT_ID);
		logMethodStartOrEnd(Logging.PERSONAL_INFORMATION, METHOD_NAME, Logging.END_OF_METHOD);
//		return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountProfilePage);
		return REDIRECT_PREFIX + "/my-account/personalInformation";
	}


	@GetMapping("/changeSecurityQuestion")
	@RequireHardLogIn
	public String verifyPassword(final Model model) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "changesecurityquestion()";
		logMethodStartOrEnd(Logging.CHANGE_SECURITY_Question, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		model.addAttribute("checkPassword", new UpdatePasswordForm());
		storeCmsPageInModel(model, getContentPageForLabelOrId(CHANGE_SECURITY_QUESTION_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHANGE_SECURITY_QUESTION_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.CHANGE_SECURITY_QUESTION_LINK_COMPONENT_ID);
		createBreadCrumbsForProfile(null, model, jnjCommonFacadeUtil.getMessageFromImpex(SECURITY_QUESTIONS_LABEL));
		logMethodStartOrEnd(Logging.CHANGE_SECURITY_Question, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.ChangeSecurityquestion);
	 
	}

	@PostMapping("/verifyCurrentPassword")
	@RequireHardLogIn
	public String verifyPassword(final UpdatePasswordForm form, final Model model,RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "verifycurrentpassword()";
		logMethodStartOrEnd(Logging.UPDATE_PASSWORD, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		String response = null;
		 
		//response = getView(JnjglobalprofileControllerConstants.Views.Pages.Account.ChangeSecurityquestion);
		//response =  getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountProfilePage);

		try
		{
			/** Verify eligibility to change Password **/
			final String email = customerFacade.getCurrentGTCustomer().getUid();
			if (customerFacade.verifyExistingPassword(form.getCurrentPassword(), email))
			{
				response = String.format(REDIRECT_PREFIX + "/my-account/getSecurityQuestions");
				model.addAttribute(SUCCESSCHK, Boolean.valueOf(true));
				redirectAttributes.addFlashAttribute(SUCCESSCHK,  Boolean.valueOf(true));
			}
			else
			{
				model.addAttribute(SUCCESSCHK, Boolean.valueOf(false));
				redirectAttributes.addFlashAttribute(SUCCESSCHK,  Boolean.valueOf(false));
			}
		}
		catch (final PasswordMismatchException exception)
		{
			model.addAttribute(SUCCESS, Boolean.valueOf(false));
			redirectAttributes.addFlashAttribute(SUCCESS,  Boolean.valueOf(false));
			LOG.error("Exception in password verification process :: ", exception);
		}

		catch (final BusinessException exception)
		{
			model.addAttribute(SUCCESS, Boolean.valueOf(false));
			redirectAttributes.addFlashAttribute(SUCCESS,  Boolean.valueOf(false));
			LOG.error("Exception in password verification process :: ", exception);
		}
		
		final JnjGTSecretQuestionForm secretQuestionForm = new JnjGTSecretQuestionForm();
		model.addAttribute("secretQuestionForm", secretQuestionForm);
		
		model.addAttribute("updateProfileForm", new JnjGTProfileForm());
		model.addAttribute("checkPassword", new UpdatePasswordForm());
		storeCmsPageInModel(model, getContentPageForLabelOrId(CHANGE_SECURITY_QUESTION_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHANGE_SECURITY_QUESTION_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.CHANGE_SECURITY_QUESTION_LINK_COMPONENT_ID);
		logMethodStartOrEnd(Logging.UPDATE_PASSWORD, METHOD_NAME, Logging.END_OF_METHOD);
		return REDIRECT_PREFIX + "/my-account/personalInformation";

	}

	
	@GetMapping("/getSecurityQuestions")
	@RequireHardLogIn
	public String changeSecurityQuestion(final Model model,RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "getsecurityquestion()";
		logMethodStartOrEnd(Logging.Get_SECURITY_Question, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnjGTSecretQuestionForm secretQuestionForm = new JnjGTSecretQuestionForm();
		final List<SecretQuestionData> secretQuestionDataList = customerFacade.getSecretQuestionsForUser();
		if (CollectionUtils.isNotEmpty(secretQuestionDataList))
		{
			secretQuestionForm.setQuestionList(secretQuestionDataList);
		}
		redirectAttributes.addFlashAttribute("secretQuestions", getAllSecretQuestions());
		
		model.addAttribute("secretQuestionForm", secretQuestionForm);
		redirectAttributes.addFlashAttribute("secretQuestionForm", secretQuestionForm);
		storeCmsPageInModel(model, getContentPageForLabelOrId(CHANGE_SECURITY_QUESTION_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHANGE_SECURITY_QUESTION_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.CHANGE_SECURITY_QUESTION_LINK_COMPONENT_ID);
		logMethodStartOrEnd(Logging.Get_SECURITY_Question, METHOD_NAME, Logging.END_OF_METHOD);
		createBreadCrumbsForProfile(null, model, jnjCommonFacadeUtil.getMessageFromImpex(SECURITY_QUESTIONS_LABEL));
		//return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountChangeSecurityQuestionPage);
		return REDIRECT_PREFIX + "/my-account/personalInformation";
	 
	}
	

	protected List<SecretQuestionData> getAllSecretQuestions()
	{

		final List<SecretQuestionData> secretQuestionList = new ArrayList<SecretQuestionData>();
		final Map<String, String> quesMap = customerFacade.getSecurityQuestions();
		for (final Entry<String, String> entry : quesMap.entrySet())
		{
			final SecretQuestionData secretQuestionData = new SecretQuestionData();
			secretQuestionData.setCode(entry.getKey());
			secretQuestionData.setQuestion(entry.getValue());
			secretQuestionList.add(secretQuestionData);
		}
		return secretQuestionList;
	}

	@PostMapping("/secret-questions")
	public String updateSecretQuestions(@Valid final JnjGTSecretQuestionForm secretQuestionForm,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "secretquestion()";
		logMethodStartOrEnd(Logging.SECRET_QUESTION, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final List<SecretQuestionData> secretQuestionData = new ArrayList<SecretQuestionData>();
		for (final SecretQuestionData secretQuestion : secretQuestionForm.getQuestionList())
		{
			if (secretQuestion.getCode() != null && secretQuestion.getAnswer() != null
					&& StringUtils.isNotEmpty(secretQuestion.getCode()) && StringUtils.isNotEmpty(secretQuestion.getAnswer()))
			{
				secretQuestionData.add(secretQuestion);
			}
		}
		final boolean secretQuestionUpdated = customerFacade.updateSecretQuestions(secretQuestionData);
		redirectAttributes.addFlashAttribute(SUCCESSQUE, Boolean.valueOf(secretQuestionUpdated));
		redirectAttributes.addFlashAttribute("secretQuestionUpdated", Boolean.valueOf(secretQuestionUpdated));
		redirectAttributes.addFlashAttribute("secretQuestions", getAllSecretQuestions());
		redirectAttributes.addFlashAttribute("secretQuestionForm", secretQuestionForm);
		storeCmsPageInModel(model, getContentPageForLabelOrId(CHANGE_SECURITY_QUESTION_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHANGE_SECURITY_QUESTION_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.CHANGE_SECURITY_QUESTION_LINK_COMPONENT_ID);
		logMethodStartOrEnd(Logging.SECRET_QUESTION, METHOD_NAME, Logging.END_OF_METHOD);
		
		return REDIRECT_PREFIX + "/my-account/personalInformation";
		//return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountChangeSecurityQuestionPage);
	 
		 
	}



	



	@PostMapping("/updatePassword")
	public String updateCustomerPassword(@Valid final UpdatePasswordForm updatePasswordForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "updateCustomerPassword()";
		logMethodStartOrEnd(Logging.UPDATE_PASSWORD, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final String TRUE = String.valueOf(Boolean.TRUE);
		final String EXCEPTION = "Exception";
		String error = "";
		try
		{
			/** Verify eligibility to change Password **/
			final String email = customerFacade.getCurrentGTCustomer().getUid();
			if (jnjGTCustomerFacade.verifyExistingPassword(updatePasswordForm.getCurrentPassword(), email))
			{
				error = jnjGTCustomerFacade.verifyPassword(updatePasswordForm.getNewPassword(), email);
				if (TRUE.equalsIgnoreCase(error.trim()))
				{
					/** Change password for the user **/
					try
					{
						jnjGTCustomerFacade.updateUserPassword(updatePasswordForm.getNewPassword(), email);
						redirectAttributes.addFlashAttribute(SUCCESSPWD, Boolean.valueOf(true));
						/** SUCCESS SCENARIO **/
						logDebugMessage(Logging.UPDATE_PASSWORD, METHOD_NAME, "Password has been updated successfully");
					}
					catch (final TokenInvalidatedException tokenInvalidatedException)
					{
						/** FAILURE SCENARIO **/
						logDebugMessage(Logging.UPDATE_PASSWORD, METHOD_NAME, "Password update failed!");
						error = String.valueOf(Boolean.FALSE);
						LOG.error("Password update error :: " + tokenInvalidatedException);
					}
				}
				else
				{
					redirectAttributes.addFlashAttribute(SUCCESSPWD, Boolean.valueOf(false));
					redirectAttributes.addFlashAttribute(STATUS, OLD_PASSWORD);
					logDebugMessage(Logging.UPDATE_PASSWORD, METHOD_NAME, "Password update failed!");
				}
			}
			else
			{
				redirectAttributes.addFlashAttribute(SUCCESSPWD, Boolean.valueOf(false));
				redirectAttributes.addFlashAttribute(STATUS, PASS_MISMATCH);
				logDebugMessage(Logging.UPDATE_PASSWORD, METHOD_NAME, "Password update failed!");
			}
		}
		catch (final PasswordMismatchException exception)
		{
			error = EXCEPTION;
			redirectAttributes.addFlashAttribute(SUCCESSPWD, Boolean.valueOf(false));
			LOG.error("Exception in password verification process :: " + exception.getMessage());
		}

		catch (final BusinessException exception)
		{
			error = EXCEPTION;
			redirectAttributes.addFlashAttribute(SUCCESSPWD, Boolean.valueOf(false));
			LOG.error("Exception in password verification process :: " + exception.getMessage());
		}
		logMethodStartOrEnd(Logging.UPDATE_PASSWORD, METHOD_NAME, Logging.END_OF_METHOD);
		storeCmsPageInModel(model, getContentPageForLabelOrId(CHANGE_PASSWORD_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHANGE_PASSWORD_CMS_PAGE));
//	    return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountChangePasswordPage);
		return REDIRECT_PREFIX + "/my-account/personalInformation";
		 
	}


	/**
	 * @param model
	 * @param customerData
	 * @throws CMSItemNotFoundException
	 */
	protected void setUpPageForEmailPrefrences(final Model model, final JnjGTCustomerData customerData)
			throws CMSItemNotFoundException,DuplicateUidException
	{
		try{
		 Map<String, String> emailPrefrencesList = null;
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final List<String> userEmailPrefrenceList = customerData.getEmailPreferences();
		/*5509*/
		final List<String> userSmsPrefrenceList = customerData.getSmsPreferences();
		 Map<String, String> roleMap  = (Map<String, String>) model.asMap().get("roleMap");
		 if(roleMap!=null){

	for(String key: roleMap.keySet()){
		if(StringUtils.isNotEmpty(key)){
			if(key.contains(PLACEORDER)){
				emailPrefrencesList = customerFacade.getEmailPreferences(PLACEORDER);
			
			}else if(key.contains(VIEWONLY)){
				emailPrefrencesList = customerFacade.getEmailPreferences(VIEWONLY);
				
			}else if(key.toLowerCase().contains(NOCHARGE.toLowerCase())){
				emailPrefrencesList = customerFacade.getEmailPreferences(NOCHARGE);
			}
			/*Start of AAOL 4911*/
			if(key.contains(PLACEORDER_SALES_GROUP)){
				emailPrefrencesList.remove(KEY_EMAIL_PREFERENCE_16);
			}else if(key.contains(PLACEORDER_BUYER_GROUP)) {
				emailPrefrencesList.remove(KEY_EMAIL_PREFERENCE_15);
			}
			/*End of AAOL 4911*/
		}
	}}else{
		List<String> customerGroups =customerData.getGroups();
		if(customerGroups!=null && customerGroups.size()>0){
			if(customerGroups.contains(PLACEORDER)){
				emailPrefrencesList = customerFacade.getEmailPreferences(PLACEORDER);
			}else if(customerGroups.contains(VIEWONLY)){
				emailPrefrencesList = customerFacade.getEmailPreferences(VIEWONLY);
			}else if(customerGroups.contains(NOCHARGE)){
				emailPrefrencesList = customerFacade.getEmailPreferences(NOCHARGE);
			}
		}
		
	}
		
		final String userEmailType = customerFacade.getEmailType(customerData);
		final String shippedOrderEmailType = customerFacade.getShippedOrderEmailType(customerData);
		final String invoiceEmailType = customerFacade.getInoviceEmailType(customerData);
		final String deliveryNoteEmailType = customerFacade.getDeliveryNoteEmailType(customerData);
		model.addAttribute("emailPrefrencesList", emailPrefrencesList);
		model.addAttribute("userEmailPrefrenceList", userEmailPrefrenceList);
		/*5509*/
		model.addAttribute("userSmsPrefrenceList", userSmsPrefrenceList);
		model.addAttribute("emailPrefrenceForm", new JnjGTProfileForm());
		model.addAttribute("backorderEmailType", userEmailType);
		model.addAttribute("shippedOrderEmailType", shippedOrderEmailType);
		model.addAttribute("invoiceEmailPreferenceType", invoiceEmailType);
		model.addAttribute("deliveryNoteEmailPreferenceType", deliveryNoteEmailType);
		storeCmsPageInModel(model, getContentPageForLabelOrId(EMAIL_PREFRENCES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(EMAIL_PREFRENCES_CMS_PAGE));
		//model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.EMAIL_PREFRENCES_LINK_COMPONENT_ID);
		}catch (final DuplicateUidException exception)
		{

		}
	}



	@PostMapping("/emailPreferences")
	@RequireHardLogIn
	public String saveEmailPrefrences(final Model model, @Valid final JnjGTProfileForm jnjGTProfileForm,RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException,DuplicateUidException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "saveEmailPrefrences()";
		logMethodStartOrEnd(Logging.UPDATE_EMAIL_PREFRENCES, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnjGTCustomerData customerData = (JnjGTCustomerData) customerFacade.getCurrentCustomer();
		customerData.setEmailPreferences(jnjGTProfileForm.getEmailPreferences());
		/*5509*/
		customerData.setSmsPreferences(jnjGTProfileForm.getSmsPreferences());
		customerData.setPreferredMobileNumber(jnjGTProfileForm.getPreferredMobileNumberPrefix()+JnjglobalprofileConstants.SYMBOl_PIPE+jnjGTProfileForm.getPreferredMobileNumber().substring(0, jnjGTProfileForm.getPreferredMobileNumber().length() - 1));
		
		//added email type for JJEPIC-825 backorder email notification
		customerData.setBackorderType(jnjGTProfileForm.getBackorderEmailType());
		customerData.setShippedOrderType(jnjGTProfileForm.getShippedOrderEmailType());
		
		customerData.setInvoiceEmailPreferenceType(jnjGTProfileForm.getInvoiceEmailPrefType());
		customerData.setDeliveryNoteEmailPreferenceType(jnjGTProfileForm.getDeliveryNoteEmailPrefType());
		try
		{
			customerFacade.updateCustomer(customerData);
			model.addAttribute(SUCCESS, Boolean.valueOf(true));
			redirectAttributes.addFlashAttribute(SUCCESSEMAIL, Boolean.valueOf(true));
		}
		catch (final DuplicateUidException exception)
		{
			model.addAttribute(SUCCESS, Boolean.valueOf(false));
			redirectAttributes.addFlashAttribute(SUCCESSEMAIL, Boolean.valueOf(false));
			LOG.error("Exception in saving  Email Exception :: " + exception.getMessage());
		}
		setUpPageForEmailPrefrences(model, customerData);
		logMethodStartOrEnd(Logging.UPDATE_EMAIL_PREFRENCES, METHOD_NAME, Logging.END_OF_METHOD);
		return REDIRECT_PREFIX + "/my-account/personalInformation";
		//return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountEmailPrefrences);
 
	}


	

	/**
	 * @param model
	 */
	protected void setUpPageForAddNewAccount(final Model model)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		model.addAttribute("customerData", customerData);

		model.addAttribute("countryList", customerFacade.getCountries());
		final Map<String, Map<String, String>> companyInfoDropDownValues = getCompanyInfoDropdowns();
		for (final Entry<String, Map<String, String>> entry : companyInfoDropDownValues.entrySet())
		{
			model.addAttribute(entry.getKey(), entry.getValue());
		}
	}


	@PostMapping("/addNewAccount")
	public String addNewAccount(@Valid final JnjGTAddAccountForm addaccountForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		boolean isSuccess = false;
		final String METHOD_NAME = "addnewaccount()";
		logMethodStartOrEnd(Logging.ADD_NEW_ACCOUNT, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final JnjCompanyInfoData companyData = convertJnjAddAccountForm(addaccountForm);
		try
		{
			isSuccess = customerFacade.addNewAccount(companyData);
		}
		catch (final Exception exception)
		{

			LOG.error("Account Not Able To Added " + exception.getMessage());
		}
		model.addAttribute("isSuccess", Boolean.valueOf(isSuccess));
		redirectAttributes.addFlashAttribute("isSuccess", Boolean.valueOf(isSuccess));
		setUpPageForAddNewAccount(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_ACCOUNT_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.ADD_ACCOUNT_LINK_COMPONENT_ID);
		logMethodStartOrEnd(Logging.ADD_NEW_ACCOUNT, METHOD_NAME, Logging.END_OF_METHOD);
		return REDIRECT_PREFIX + "/my-account/personalInformation";

	}

	@PostMapping("/addExistingAccount")
	public String addExistingAccount(@Valid final JnjGTAddAccountForm addaccountForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "addexistingaccount()";
		logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final String accountNames = addaccountForm.getNameOrNumberOfExistingAccount();
		final String[] accountNumbers = StringUtils.split(accountNames, ",");
		final String invalidAccounts = jnjGTCustomerFacade.validateAccountNumbers(accountNumbers);
		final StringBuilder duplicateValues = new StringBuilder();
		/* AAOL-4662 My Profile: User is able to add the existing account again to the profile. Check needed or not. */
		List duplicateAccList =  new ArrayList() ;
		List newAccList =  new ArrayList() ;
		List<String> accountList = new ArrayList<String>();
		Collections.addAll(accountList, accountNumbers);
		CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		Set<PrincipalGroupModel> groups = customerModel.getGroups();
		List<String> invalidAccountsList = new ArrayList<String>();
		
		if(!invalidAccounts.isEmpty()){
		Collections.addAll(invalidAccountsList, invalidAccounts.split(","));
		}
		
		for (String  account : accountList) {
			boolean duplacteFlage=false;
			for (PrincipalGroupModel principalGroupModel : groups) {
				if(principalGroupModel.getUid().equalsIgnoreCase(account.trim())){
					duplicateAccList.add(principalGroupModel.getUid());
					duplacteFlage=true;
				}
			}
			
			if(duplacteFlage==false && !invalidAccountsList.contains(account.trim()) ){
				newAccList.add(account.trim());
			}
		}
		
		if(!newAccList.isEmpty()){
			final boolean status = jnjGTCustomerFacade.addExistingAccount((String[])newAccList.toArray(new String[newAccList.size()]));		
			model.addAttribute(SUCCESSACC, newAccList);
			redirectAttributes.addFlashAttribute(SUCCESSACC,newAccList);
			}
			model.addAttribute(DUPLICATE_VALUES, duplicateAccList);	
			redirectAttributes.addFlashAttribute(DUPLICATE_VALUES, duplicateAccList);
			model.addAttribute("invalidAccountNumbers", invalidAccountsList);
			redirectAttributes.addFlashAttribute("invalidAccountNumbers", invalidAccountsList);
	/*	 AAOL-4662 My Profile: User is able to add the existing account again to the profile. Check needed or not. */
			
			
			
		setUpPageForAddNewAccount(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_ACCOUNT_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.ADD_ACCOUNT_LINK_COMPONENT_ID);
		logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT, METHOD_NAME, Logging.END_OF_METHOD);
//		return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountAddAccountPage);
		return REDIRECT_PREFIX + "/my-account/personalInformation";


	}

	protected JnjCompanyInfoData convertJnjAddAccountForm(final JnjGTAddAccountForm jnjAddAccountForm)
	{
		//TODO: STEP 2
		final JnjCompanyInfoData companyInfoData = new JnjCompanyInfoData();
		companyInfoData.setSector(jnjAddAccountForm.getSector());
		companyInfoData.setAccountName(jnjAddAccountForm.getAccountName());
		companyInfoData.setgLN(jnjAddAccountForm.getgLN());
		companyInfoData.setTypeOfBusiness(jnjAddAccountForm.getTypeOfBusiness());
		companyInfoData.setSubsidiaryOf(jnjAddAccountForm.getSubsidiaryOf());
		companyInfoData.setBillToCountry(jnjAddAccountForm.getBillToCountry());
		companyInfoData.setBillToLine1(jnjAddAccountForm.getBillToLine1());
		companyInfoData.setBillToLine2(jnjAddAccountForm.getBillToLine2());
		companyInfoData.setBillToCity(jnjAddAccountForm.getBillToCity());
		companyInfoData.setBillToState(jnjAddAccountForm.getBillToState());
		companyInfoData.setBillToZipCode(jnjAddAccountForm.getBillToZipCode());
		companyInfoData.setShipToCountry(jnjAddAccountForm.getShipToCountry());
		companyInfoData.setShipToLine1(jnjAddAccountForm.getShipToLine1());
		companyInfoData.setShipToLine2(jnjAddAccountForm.getShipToLine2());
		companyInfoData.setShipToCity(jnjAddAccountForm.getShipToCity());
		companyInfoData.setShipToState(jnjAddAccountForm.getShipToState());
		companyInfoData.setShipToZipCode(jnjAddAccountForm.getShipToZipCode());
		companyInfoData.setSalesAndUseTaxFlag(jnjAddAccountForm.getSalesAndUseTaxFlag());
		companyInfoData.setInitialOpeningOrderAmount(jnjAddAccountForm.getInitialOpeningOrderAmount());
		companyInfoData.setEstimatedAmountPerYear(jnjAddAccountForm.getEstimatedAmountPerYear());
		companyInfoData.setMedicalProductsPurchase(jnjAddAccountForm.getMedicalProductsPurchase());
		return companyInfoData;
	}


	public Map<String, Map<String, String>> getCompanyInfoDropdowns()
	{
		final List<String> list = new ArrayList<String>();
		list.add(LoginaddonConstants.Register.BUSINESS_TYPE);
		list.add(LoginaddonConstants.Register.ESTIMATED_AMOUNT);
		list.add(LoginaddonConstants.Register.PURCHASE_PRODUCT);
		return jnjGTCustomerFacade.getCompanyInfoDropdowns(list);

	}


	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
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
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}

	protected void createBreadCrumbsForProfile(final String formURI, final Model model, final String formSpecificKey)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "createbreadcrumbs()";
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final String personalInformation = jnjCommonFacadeUtil.getMessageFromImpex(MyProfile_LABEL);
 
		String personalinformationURL = null;
	 
		 
			personalinformationURL = "/my-account/personalInformation";
	 
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		//breadcrumbs.add(new Breadcrumb(personalinformationURL, personalInformation, null));
		breadcrumbs.add(new Breadcrumb(formURI, formSpecificKey, null));
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.END_OF_METHOD);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
	}
	public String getView(final String view){
        return JnjglobalprofileControllerConstants.ADDON_PREFIX + view;
 }

	/* 4659 */
	@PostMapping("/updateDefaultAccAndOrder")
	public String updateDefaultB2BUnit(@Valid final JnjGTDefaultAccAndOrderForm defaultAccAndOrderForm, final BindingResult bindingResult, 
			final Model model,RedirectAttributes redirectAttributes) throws CMSItemNotFoundException,DuplicateUidException
	{
		final String METHOD_NAME = "updateDefaultB2BUnit()";
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		String defaultAccount = null;
		String defaultOrder = null;
		
		if(defaultAccAndOrderForm.getDefaultAccount()!=null){
			defaultAccount = defaultAccAndOrderForm.getDefaultAccount();
		}
		if(defaultAccAndOrderForm.getDefaultOrder()!=null){
			defaultOrder = defaultAccAndOrderForm.getDefaultOrder();
		}
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		try{
			jnjGTCustomerFacade.updateDefaultAccAndOrder(defaultAccount,defaultOrder);
			model.addAttribute(SUCCESS, Boolean.valueOf(true));
			redirectAttributes.addFlashAttribute(SUCCESS,  Boolean.valueOf(true));
		}
		catch (ModelSavingException exception){
			model.addAttribute(SUCCESS, Boolean.valueOf(false));
			LOG.error("Default choices not saved sucessfully :: " + exception.getMessage());
		}
		boolean accAndOrderUpdated=jnjGTCustomerFacade.updateDefaultAccAndOrder(defaultAccount,defaultOrder);
		
		JnJB2bCustomerModel customerModel = (JnJB2bCustomerModel)userService.getCurrentUser();
		
		storeCmsPageInModel(model, getContentPageForLabelOrId(DEFAULT_ACCOUNT_ORDER));
		redirectAttributes.addFlashAttribute(SUCCESSDFLT, Boolean.valueOf(accAndOrderUpdated));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(DEFAULT_ACCOUNT_ORDER));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.DEFAULT_ACCOUNT_ORDER_LINK_COMPONENT_ID);
		logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT, METHOD_NAME, Logging.END_OF_METHOD);
//		return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountAddAccountPage);
		return REDIRECT_PREFIX + "/my-account/personalInformation";
	}
	/* 4659 */	
	
	
}