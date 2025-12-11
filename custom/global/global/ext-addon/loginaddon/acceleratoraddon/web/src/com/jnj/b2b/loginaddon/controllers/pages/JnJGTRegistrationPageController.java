/**
 *
 */
package com.jnj.b2b.loginaddon.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonWebConstants;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.loginaddon.forms.JnjGTUserActivationForm;
import com.jnj.b2b.loginaddon.forms.LoginForm;
import com.jnj.b2b.loginaddon.forms.ProfileValidationData;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.storefront.breadcrumb.impl.ContentPageBreadcrumbBuilder;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.core.dto.JnjCountryData;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.SecretQuestionData;
import com.jnj.loginaddon.forms.JnjGTRegistrationForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjCompanyInfoData;
import com.jnj.core.dto.JnjRegistrationData;
import com.jnj.core.dto.JnjSectorTypeData;
import com.jnj.core.dto.JnjUserInfoData;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCustomerData;


/**
 * @author ujjwal.negi
 * 
 */
@Controller("JnJGTRegistrationPageController")
@Scope("tenant")
@RequestMapping(value = "/register")
public class JnJGTRegistrationPageController extends AbstractPageController
{
	protected static final Logger LOG = Logger.getLogger(JnJGTRegistrationPageController.class);

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjCustomerFacade;

	@Autowired
	protected ConfigurationService configurationService;

	@Autowired
	UserService userService;

	/** the Session service **/
	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	@Autowired
	protected ContentPageBreadcrumbBuilder contentPageBreadcrumbBuilder;


	@Autowired
	protected CMSSiteService cmsSiteService;

	public JnjGTCustomerFacade getJnjCustomerFacade() {
		return jnjCustomerFacade;
	}


	public ConfigurationService getConfigurationService() {
		return configurationService;
	}


	public UserService getUserService() {
		return userService;
	}


	public SessionService getSessionService() {
		return sessionService;
	}


	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}


	public ContentPageBreadcrumbBuilder getContentPageBreadcrumbBuilder() {
		return contentPageBreadcrumbBuilder;
	}

	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}
	
	protected static final String REGISTER_STEP_ONE_INVALID_ACCOUNT_ERROR = "register.stepOne.invalid.account.error";
	protected static final String USER_REGISTRATION_DISABLE_RE_CAPTCHA = "user.registration.disable.reCaptcha";
	protected static final String REGISTER_STEP_ONE_INVALID_WWID_ERROR = "register.stepOne.invalid.wwid.error";

	/**
	 * 
	 * This method displays the Registration page to the user on clicking the register link.
	 * 
	 * @param model
	 * @return string
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping
	public String getRegister(final Model model, final JnjGTRegistrationForm jnjRegistrationForm) throws CMSItemNotFoundException
	{
		//model.addAttribute("emailMismatchErr", jnjCommonFacadeUtil.getMessageFromImpex("text.register.email.match.error"));
		// to display the error msg when the type of account is not selected
		//model.addAttribute("typeSelectErr", jnjCommonFacadeUtil.getMessageFromImpex("text.register.type.select.error"));
		if (!model.containsAttribute("registrationForm"))
		{
			model.addAttribute("registrationForm", new JnjGTRegistrationForm());
			//model.addAttribute("passwordError", jnjCommonFacadeUtil.getMessageFromImpex("text.register.password.not.matched"));
		}

		if (null != jnjRegistrationForm)
		{
			return getDefaultRegistrationPage(model, false, jnjRegistrationForm);
		}
		else
		{
			return getDefaultRegistrationPage(model, true, jnjRegistrationForm);
		}
	}


	/**
	 * This method fetches the form containing the values of all the fields that customer has entered during
	 * registration, and invokes the facade class to register new user.
	 * 
	 * @param registrationForm
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 */

	@PostMapping("/process")
	public String getRegister(@ModelAttribute("registrationForm") final JnjGTRegistrationForm registrationForm, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{

	
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Form validation passed.");
		}
		
		//Populate the data class with the fields returned in the form.
		final JnjRegistrationData jnjRegistrationData = populateRegistrationData(registrationForm);
		//      try
		//      {
		//Calling facade class to register new user.
		boolean status = false;
		try
		{

			status = jnjCustomerFacade.registerUser(jnjRegistrationData, JnjWebUtil.getUnsecureServerUrl(request));
	
		}
		catch (final DuplicateUidException exp)
		{
		
			LOG.warn("registration failed: " + exp);
			model.addAttribute(registrationForm);
			model.addAttribute(new LoginForm());
			model.addAttribute(new JnjGTRegistrationForm()); //If user is already registered display error message.
			model.addAttribute(LoginaddonConstants.Register.REGISTRATION_ERROR, LoginaddonConstants.Register.REGISTRATION_ERROR_MSG);
			return handleRegistrationError(model);
		}
		catch (final Exception exception)
		{
		
			exception.printStackTrace();
			model.addAttribute(LoginaddonConstants.Register.REGISTRATION_ERROR, LoginaddonConstants.Register.REGISTRATION_GLOBAL_MSG);
			model.addAttribute("registrationForm", new JnjGTRegistrationForm());
			handleRegistrationError(model);
		}

		//If registration is successful redirect to successful message page.
		if (status)
		{
		
			return onSuccessfulRegistration(model);

		}

		return null;
	}


	protected String handleRegistrationError(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		setupUpModelForRegistration(model);
		return getRegView(LoginaddonConstants.Register.REGISTER);
	}


	@GetMapping("/activateUser")
	public String activateUser(final Model model, final JnjGTUserActivationForm jnjUserForm) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(LoginaddonConstants.Register.USER_ACTIVATION_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LoginaddonConstants.Register.USER_ACTIVATION_PAGE));
		model.addAttribute("userActivationForm", jnjUserForm);
		setUpModelForUserActivation(model);
		return getRegView(LoginaddonConstants.Register.USER_ACTIVATION);
	}

	/**
	 * @param model
	 */
	protected void setUpModelForUserActivation(final Model model)
	{
		model.addAttribute("stateList", jnjCustomerFacade.getRegions("CA"));
		model.addAttribute("countryList", jnjCustomerFacade.getCountries());
		model.addAttribute("secretQuestions", getSecurityQuestions());
		model.addAttribute("countryCodes", jnjCustomerFacade.getPhoneCodes());
	}

	@PostMapping("/activateUser")
	public String activateUser(@ModelAttribute(value = "userActivationForm") final JnjGTUserActivationForm jnjUserForm,
			final Model model) throws CMSItemNotFoundException
	{

		final JnjGTCustomerData jnjGTCustomerData = (JnjGTCustomerData) jnjCustomerFacade.getCurrentCustomer();
		final JnjGTAddressData addressData = new JnjGTAddressData();
		populateUserData(jnjUserForm, addressData);
		jnjGTCustomerData.setContactAddress(addressData);
		try
		{
			jnjCustomerFacade.updateCustomer(jnjGTCustomerData);
		}
		catch (final DuplicateUidException e)
		{
			LOG.error("Could not update the personal information of the user", e);
		}
		final boolean status = jnjCustomerFacade.updateSecretQuestionsForUser(jnjUserForm.getSecretQuestionsAnswers(),
				jnjGTCustomerData.getUid());
		if (!status)
		{
			model.addAttribute("errorMsg", "There was an error while updating the Secret Questions. Kindly retry.");
			handleActivationError(model);
		}

		/** Setting in the session - first time login **/
		sessionService.setAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN, Boolean.TRUE);
		storeCmsPageInModel(model, getContentPageForLabelOrId(LoginaddonConstants.Register.ACTIVATION_SUCCESS));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LoginaddonConstants.Register.ACTIVATION_SUCCESS));
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		model.addAttribute("userActivationForm", jnjUserForm);

		return getView(LoginaddonControllerConstants.Views.Pages.Account.SuccessfulActivationPage);

	}

	protected String handleActivationError(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(LoginaddonConstants.Register.USER_ACTIVATION_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LoginaddonConstants.Register.USER_ACTIVATION_PAGE));
		setUpModelForUserActivation(model);
		return getRegView(LoginaddonConstants.Register.USER_ACTIVATION);
	}

	/**
	 * 
	 * @param jnjUserForm
	 * @param addressData
	 */
	protected void populateUserData(final JnjGTUserActivationForm jnjUserForm, final JnjGTAddressData addressData)
	{
		addressData.setDefaultAddress(true);
		addressData.setLine1(jnjUserForm.getAddressLine1());
		addressData.setLine2(jnjUserForm.getAddressLine2());
		addressData.setPhone(jnjUserForm.getPhone());
		addressData.setTown(jnjUserForm.getCity());
		addressData.setMobile(jnjUserForm.getMobile());
		addressData.setFax(jnjUserForm.getFax());
		addressData.setTown(jnjUserForm.getCity());
		addressData.setPostalCode(jnjUserForm.getZip());
		final CountryData country = new CountryData();
		country.setIsocode(jnjUserForm.getCountry());
		addressData.setCountry(country);
		if (country.getIsocode().equalsIgnoreCase(LoginaddonConstants.US))
		{
			final RegionData region = new RegionData();
			region.setIsocode(jnjUserForm.getState());
			addressData.setRegion(region);
		}
	}

	/**
	 * @param model
	 * @param formRefreshFlag
	 * @param jnjRegistrationForm
	 * @return String
	 */

	protected String getDefaultRegistrationPage(final Model model, final boolean formRefreshFlag,
			final JnjGTRegistrationForm jnjRegistrationForm) throws CMSItemNotFoundException
	{

		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());

		model.addAttribute(LoginaddonWebConstants.BREADCRUMBS_KEY,
				contentPageBreadcrumbBuilder.getBreadcrumbs((ContentPageModel) getCmsPage()));
		if (formRefreshFlag)
		{
			model.addAttribute(new JnjGTRegistrationForm());
		}
		else
		{
			model.addAttribute(jnjRegistrationForm);
			model.addAttribute("registerForm", jnjRegistrationForm);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Displaying default register page");
		}
		setupUpModelForRegistration(model);

		return getRegView(LoginaddonConstants.Register.REGISTER);
	}

	/**
	 * @param model
	 */
	protected void setupUpModelForRegistration(final Model model)
	{
		final List<JnjCountryData> countryList = jnjCustomerFacade.getCountries();
		model.addAttribute("countryList", countryList);
		model.addAttribute("emailPreferences", getEmailPreferences());
		model.addAttribute("secretQuestions", getSecurityQuestions());
		model.addAttribute("countryCodes", jnjCustomerFacade.getPhoneCodes());
		model.addAttribute("stateList", jnjCustomerFacade.getRegions("CA"));
		final Map<String, Map<String, String>> dropdownMap = getCompanyInfoDropdowns();
		model.addAttribute("businessType", dropdownMap.get("businessType"));
		model.addAttribute("estimatedAmount", dropdownMap.get("estimatedAmount"));
		model.addAttribute("purchaseProduct", dropdownMap.get("purchaseProduct"));		
		model.addAttribute("departments", jnjCustomerFacade.getDepartments());
	}

	/**
	 * To validate the account numbers as submitted by the user
	 * 
	 * @return ProfileValidationData object
	 */
	
	/*  @RequestMapping(value = "/validateProfile", method = RequestMethod.POST) public @ResponseBody
	  ProfileValidationData validateProfile(final HttpServletRequest request) { final boolean validationStatus = true;
	  final String typeOfProfile = request.getParameter("typeOfProfile");
	  
	  
	  
	  return getOutputMap(validationStatus, "success", null, typeOfProfile);
	  
	  
	 
	 }
	 
*/

	@PostMapping("/validateProfile")
	public @ResponseBody
	ProfileValidationData validateProfile(final HttpServletRequest request)
	{
		final boolean validationStatus = true;
		final String typeOfProfile = request.getParameter("typeOfProfile");
		final String isUnknownAccount = request.getParameter("isUnknownAccount");
		final String wwid = request.getParameter("wwid");

		if (typeOfProfile.equals("custAccount"))
		{
			final String accountNumbers = request.getParameter("accounts");
			if (isUnknownAccount.equals("true"))
			{
				return getOutputMap(validationStatus, "success", null, typeOfProfile);
			}
			else
			{
				return validateAccounts(accountNumbers, typeOfProfile);
			}
		}
		else if (typeOfProfile.equals("jjCustomer"))
		{
			return getOutputMap(validationStatus, "success", null, typeOfProfile);
		}
		else
		{
			if (StringUtils.isNotEmpty(request.getParameter("wwid")))
			{
				return validateWwid(request.getParameter("wwid"), typeOfProfile);
			}	
			if (StringUtils.isNotEmpty(request.getParameter("empAccount")))
			{
				return validateAccounts(request.getParameter("empAccount"), typeOfProfile);
			}
			else
			{
				return getOutputMap(validationStatus, "success", null, typeOfProfile);
			}
		}

	}



	/**
	 * 
	 * @param accounts
	 * @param typeOfProfile
	 * @return ProfileValidationData
	 */
	protected ProfileValidationData validateAccounts(final String accounts, final String typeOfProfile)
	{
		boolean validationStatus = true;
		final String[] accountNumbers = StringUtils.split(accounts, ",");
		final String invalidAccounts = jnjCustomerFacade.validateAccountNumbersRegistration(accountNumbers);
		if (StringUtils.isNotEmpty(invalidAccounts))
		{
			validationStatus = false;
			return getOutputMap(validationStatus, "invalidAccount", invalidAccounts, typeOfProfile);
		}
		return getOutputMap(validationStatus, "success", null, typeOfProfile);
	}
	
	/**
	 * 
	 * @param wwid
	 * @return ProfileValidationData
	 */
	protected ProfileValidationData validateWwid(final String wwid, final String typeOfProfile)
	{
		boolean validationStatus = true;
		final String invalidWwid = jnjCustomerFacade.validateWwid(wwid);
		if (StringUtils.isNotEmpty(invalidWwid))
		{
			validationStatus = false;
			return getOutputMap(validationStatus, "invalidWwid", invalidWwid, typeOfProfile);
		}
		return getOutputMap(validationStatus, "success", null, typeOfProfile);
	}

	/**
	 * 
	 * @param validationStatus
	 * @param messageKey
	 * @param valueParameters
	 * @return ProfileValidationData
	 */
	protected ProfileValidationData getOutputMap(final boolean validationStatus, final String messageKey,
			final String valueParameters, final String typeOfProfile)
	{
		//final Map<String, String> outputMap = new HashMap<String, String>();
		final ProfileValidationData profileValidationData = new ProfileValidationData();
		profileValidationData.setTypeOfProfile(typeOfProfile);
		profileValidationData.setStatus(validationStatus ? "success" : "error");
		//outputMap.put("status", (validationStatus ? "success" : "error"));
		if (validationStatus && typeOfProfile.equals("jjCustomer"))
		{
			final Map<String, Map<String, String>> dropdownMap = getCompanyInfoDropdowns();
			profileValidationData.setBusinessTypeDropdown(dropdownMap.get("businessType"));
			profileValidationData.setEstimatedAmountDropdown(dropdownMap.get("estimatedAmount"));
			profileValidationData.setPurchaseProducts(dropdownMap.get("purchaseProduct"));
		}
		else if (validationStatus && !typeOfProfile.equals("jjCustomer"))
		{
			final Map<String, String> departments = getDepartments();
			profileValidationData.setDepartmentsDropdown(departments);
		}
		switch (messageKey)
		{
			case "success":
				profileValidationData.setMessage("");
				//outputMap.put("message", "");
				break;
			case "invalidAccount":
				profileValidationData.setMessage(jnjCommonFacadeUtil.getMessageFromImpex(REGISTER_STEP_ONE_INVALID_ACCOUNT_ERROR));
				//outputMap.put("message", "The following accounts are unvalid" + valueParameters);
				break;
			case "invalidWwid":
				profileValidationData.setMessage(jnjCommonFacadeUtil.getMessageFromPropertiesFile(REGISTER_STEP_ONE_INVALID_WWID_ERROR));
				//outputMap.put("message", "The following accounts are unvalid" + valueParameters);
				break;
			default:
				profileValidationData.setMessage("");
				//outputMap.put("message", "");
				break;
		}
		return profileValidationData;
	}

	/**
	 * @param jnjRegistrationForm
	 * @return JnjSectorTypeData
	 */
	protected JnjSectorTypeData storeSectorInfo(final JnjGTRegistrationForm jnjRegistrationForm)
	{
		final JnjSectorTypeData jnjSectorTypeData = new JnjSectorTypeData();
		jnjSectorTypeData.setWwid(jnjRegistrationForm.getWWID());
		jnjSectorTypeData.setAccountNumbers(jnjRegistrationForm.getAccountNumbers());
		jnjSectorTypeData.setConsumerProductsSector(jnjRegistrationForm.getConsumerProductsSector().booleanValue());
		jnjSectorTypeData.setDivision(jnjRegistrationForm.getDivision());
		jnjSectorTypeData.setMddSector(jnjRegistrationForm.getMddSector().booleanValue());
		jnjSectorTypeData.setPharmaSector(jnjRegistrationForm.getPharmaSector().booleanValue());
		jnjSectorTypeData.setTypeOfProfile(jnjRegistrationForm.getTypeOfProfile());
		jnjSectorTypeData.setUnknownAccount(jnjRegistrationForm.getUnknownAccount().booleanValue());
		jnjSectorTypeData.setGlnOrAccountNumber(jnjRegistrationForm.getGlnOrAccountNumber());
		return jnjSectorTypeData;
	}


	/**
	 * 
	 * @param jnjRegistrationForm
	 * @return JnjCompanyInfoData
	 */

	protected JnjCompanyInfoData storeCompanyInfo(final JnjGTRegistrationForm jnjRegistrationForm)
	{
		final JnjCompanyInfoData companyInfoData = new JnjCompanyInfoData();
		companyInfoData.setAccountName(jnjRegistrationForm.getAccountName());
		companyInfoData.setgLN(jnjRegistrationForm.getgLN());
		companyInfoData.setTypeOfBusiness(jnjRegistrationForm.getTypeOfBusiness());
		companyInfoData.setSubsidiaryOf(jnjRegistrationForm.getSubsidiaryOf());
		companyInfoData.setBillToCountry(jnjRegistrationForm.getBillToCountry());
		companyInfoData.setBillToLine1(jnjRegistrationForm.getBillToLine1());
		companyInfoData.setBillToLine2(jnjRegistrationForm.getBillToLine2());
		companyInfoData.setBillToCity(jnjRegistrationForm.getBillToCity());
		companyInfoData.setBillToState(jnjRegistrationForm.getBillToState());
		companyInfoData.setBillToZipCode(jnjRegistrationForm.getBillToZipCode());
		companyInfoData.setShipToCountry(jnjRegistrationForm.getShipToCountry());
		companyInfoData.setShipToLine1(jnjRegistrationForm.getShipToLine1());
		companyInfoData.setShipToLine2(jnjRegistrationForm.getShipToLine2());
		companyInfoData.setShipToCity(jnjRegistrationForm.getShipToCity());
		companyInfoData.setShipToState(jnjRegistrationForm.getShipToState());
		companyInfoData.setShipToZipCode(jnjRegistrationForm.getShipToZipCode());
		companyInfoData.setSalesAndUseTaxFlag(jnjRegistrationForm.getSalesAndUseTaxFlag());
		companyInfoData.setInitialOpeningOrderAmount(jnjRegistrationForm.getInitialOpeningOrderAmount());
		companyInfoData.setEstimatedAmountPerYear(jnjRegistrationForm.getEstimatedAmountPerYear());
		companyInfoData.setMedicalProductsPurchase(jnjRegistrationForm.getMedicalProductsPurchase());
		return companyInfoData;
	}

	/**
	 * 
	 * @param jnjRegistrationForm
	 * @return JnjUserInfoData object
	 */

	protected JnjUserInfoData storeUserInfo(final JnjGTRegistrationForm jnjRegistrationForm)
	{

		final JnjUserInfoData jnjUserInfoData = new JnjUserInfoData();
		jnjUserInfoData.setPermissionLevel(jnjRegistrationForm.getPermissionLevel());
		jnjUserInfoData.setFirstName(jnjRegistrationForm.getFirstName());
		jnjUserInfoData.setLastName(jnjRegistrationForm.getLastName());
		jnjUserInfoData.setOrgName(jnjRegistrationForm.getOrgName());
		jnjUserInfoData.setDepartment(jnjRegistrationForm.getDepartment());
		jnjUserInfoData.setEmailAddress(jnjRegistrationForm.getEmailAddress());
		jnjUserInfoData.setSupervisorEmail(jnjRegistrationForm.getSupervisorEmail());
		jnjUserInfoData.setSupervisorName(jnjRegistrationForm.getSupervisorName());

		jnjUserInfoData.setSupervisorPhone(preparePhone(jnjRegistrationForm.getSupervisorPhonePrefix(), jnjRegistrationForm.getSupervisorPhone()));
		jnjUserInfoData.setPhone(preparePhone(jnjRegistrationForm.getPhoneNumberPrefix(), jnjRegistrationForm.getPhone()));
		jnjUserInfoData.setMobile(preparePhone(jnjRegistrationForm.getMobileNumberPrefix(), jnjRegistrationForm.getMobileNumberPrefix()));
		jnjUserInfoData.setFax(preparePhone(jnjRegistrationForm.getFaxPrefix(), jnjRegistrationForm.getFax()));
		jnjUserInfoData.setPreferredMobileNumber(preparePhone(jnjRegistrationForm.getPreferredMobileNumberPrefix(), jnjRegistrationForm.getPreferredMobileNumber()));

		jnjUserInfoData.setCountry(jnjRegistrationForm.getCountry());
		jnjUserInfoData.setCity(jnjRegistrationForm.getCity());
		jnjUserInfoData.setAddressLine1(jnjRegistrationForm.getAddressLine1());
		jnjUserInfoData.setAddressLine2(jnjRegistrationForm.getAddressLine2());
		jnjUserInfoData.setState(jnjRegistrationForm.getState());
		jnjUserInfoData.setZip(jnjRegistrationForm.getZip());
		return jnjUserInfoData;
	}

	private String preparePhone(String phonePrefix, String phone) {
		if (StringUtils.isNotBlank(phone)) {
			return JnjGTCoreUtil.getFormattedPhoneNumber(phonePrefix + Jnjb2bCoreConstants.SYMBOl_PIPE + JnjGTCoreUtil.getFormattedPhoneNumber(phone.substring(0, phone.length() - 1)));
		}
		return null;
	}


	/**
	 * 
	 * @param registrationForm
	 * @return JnjRegistrationData
	 */
	protected JnjRegistrationData populateRegistrationData(final JnjGTRegistrationForm registrationForm)
	{
		final JnjRegistrationData jnjRegistrationData = new JnjRegistrationData();
		jnjRegistrationData.setJnjSectorTypeData(storeSectorInfo(registrationForm));
		jnjRegistrationData.setCompanyInfoData(storeCompanyInfo(registrationForm));
		jnjRegistrationData.setJnjUserInfoData(storeUserInfo(registrationForm));
		jnjRegistrationData.setPassword(registrationForm.getPassword());
		jnjRegistrationData.setEmailPreferences(registrationForm.getEmailPreferences());
		/*5506*/
		jnjRegistrationData.setSmsPreferences(registrationForm.getSmsPreferences());
		jnjRegistrationData.setSecretQuestionsAnswers(registrationForm.getSecretQuestionsAnswers());
		jnjRegistrationData.setBackorderEmailType(registrationForm.getBackorderEmailType());
		jnjRegistrationData.setShippedOrderEmailType(registrationForm.getShippedOrderEmailType());
		jnjRegistrationData.setInvoiceEmailPrefType(registrationForm.getInvoiceEmailPrefType());
		jnjRegistrationData.setDeliveryNoteEmailPrefType(registrationForm.getDeliveryNoteEmailPrefType());
		
		
		return jnjRegistrationData;
	}

	protected String getRegView(final String pageName)
	{

		if ("activation".equals(pageName))
		{
			return getView(LoginaddonControllerConstants.Views.Pages.Account.UserActivationPage);
		}else if("emailPreference".equals(pageName)){
			return getView(LoginaddonControllerConstants.Views.Pages.Account.EmailPreferencePage);
		}
		else
		{
			return getView(LoginaddonControllerConstants.Views.Pages.Account.AccountRegisterPage);
		}
	}



	@PostMapping("/getStates")
	public @ResponseBody
	List<RegionData> getStates(@RequestParam("country") final String country)
	{
		final List<RegionData> list = new ArrayList<RegionData>();
		/* list = jnjCustomerFacade.getRegions(country); */
		final RegionData reg = new RegionData();
		reg.setIsocode("US");
		reg.setName("Canada");
		list.add(reg);

		final RegionData reg1 = new RegionData();
		reg1.setIsocode("US");
		reg1.setName("alabma");
		list.add(reg1);

		return list;
	}




	@PostMapping("/divisions")
	public @ResponseBody
	Map<String, String> getDivisions()
	{
		return jnjCustomerFacade.getDivisions();
	}

	@PostMapping("/companyInfo")
	public @ResponseBody
	Map<String, Map<String, String>> getCompanyInfoDropdowns()
	{
		final List<String> list = new ArrayList<String>();
		list.add(LoginaddonConstants.Register.BUSINESS_TYPE);
		list.add(LoginaddonConstants.Register.ESTIMATED_AMOUNT);
		list.add(LoginaddonConstants.Register.PURCHASE_PRODUCT);
		return jnjCustomerFacade.getCompanyInfoDropdowns(list);

	}

	@PostMapping("/departments")
	public @ResponseBody
	Map<String, String> getDepartments()
	{

		return jnjCustomerFacade.getDepartments();
	}

	@PostMapping("/validateUser")
	public @ResponseBody
	Map<String, String> isUserValid(final HttpServletRequest request)
	{
		final Map<String, String> map = new HashMap<String, String>();
		final boolean userExists = jnjCustomerFacade.validateUid(request.getParameter("emailAddress").toLowerCase());

		map.put("isUserValid", (userExists ? "false" : "true"));
		if (userExists)
		{
			map.put("errorMsg", jnjCommonFacadeUtil.getMessageFromImpex("text.register.user.exist.error"));
		}
		return map;

	}

	protected Map<String, String> getEmailPreferences()
	{
		return jnjCustomerFacade.getEmailPreferences();
	}

	/*
	 * @RequestMapping(value = "/getcheckedUser", method = RequestMethod.POST) public @ResponseBody boolean
	 * checkUser(@RequestParam("user") final String user) { boolean userExists = false; userExists =
	 * userService.isUserExisting(user.toLowerCase()); if (userExists) { userExists = true; } return userExists;
	 */


	@PostMapping("/securityQues")
	public @ResponseBody
	List<SecretQuestionData> getSecurityQuestions()
	{
		final List<SecretQuestionData> secretQuestionList = new ArrayList<SecretQuestionData>();
		//final List<String> questionString = jnjNAUserFacade.getValuesBasedOnId("securityQues");

		final Map<String, String> quesMap = jnjCustomerFacade.getSecurityQuestions();
		for (final Entry<String, String> entry : quesMap.entrySet())
		{
			final SecretQuestionData secretQuestionData = new SecretQuestionData();
			secretQuestionData.setCode(entry.getKey());
			secretQuestionData.setQuestion(entry.getValue());
			secretQuestionList.add(secretQuestionData);
		}
		return secretQuestionList;
	}

	

	/**
	 * 
	 * This method redirects the user to a successful message page after the user registers into the portal successfully.
	 * 
	 * @param model
	 * @return string
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/successfulRegistration")
	public String onSuccessfulRegistration(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(LoginaddonConstants.Register.REGISTER_SUCCESS));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LoginaddonConstants.Register.REGISTER_SUCCESS));
		return getView(LoginaddonControllerConstants.Views.Pages.Account.successfulRegistration);

	}



	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId(LoginaddonConstants.Register.REGISTER);
	}
	

	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
	}
	
	/**
	 * This method returns the view for When the user click on the link on Reset Url from the email
	 * @param passwordExpireToken 
	 * @param email 
	 * @param model 
	 * @return expired password view
	 */
	@GetMapping("/resetPasswordForNewUser")
	public String resetPasswordForNewUser(
			@RequestParam(value = "passwordExpireToken", required = true) final String passwordExpireToken,
			@RequestParam(value = "email", required = true) final String email,
			Model model) {
		final String currentSite = cmsSiteService.getCurrentSite().getUid();
		final String siteName = currentSite.equalsIgnoreCase(LoginaddonConstants.MDD_SITE_ID) ? LoginaddonConstants.MDD
						: LoginaddonConstants.CONS;
		sessionService.setAttribute(LoginaddonConstants.SITE_NAME, siteName);
		try {
			storeCmsPageInModel(model, getContentPageForLabelOrId(LoginaddonConstants.Register.USER_ACTIVATION_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LoginaddonConstants.Register.USER_ACTIVATION_PAGE));
			model.addAttribute("email", email);
		
		
		
			storeCmsPageInModel(model, getContentPageForLabelOrId(LoginaddonConstants.Register.FIRST_RESET_PASSWORD));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LoginaddonConstants.Register.FIRST_RESET_PASSWORD));
			return getView(LoginaddonControllerConstants.Views.Pages.Password.FirstTimeResetPassword);
		
		} catch (CMSItemNotFoundException e){
		}
		return null;
	}
	
	
	
	@PostMapping("/emailPreferences")
	public String fetchSpecificEmailPreferences(
			@RequestParam(value = "emailPreference", required = true) final String emailPreference,final Model model) {
		/*Start AAOL 5074*/
		return fetchEmailPreferences(emailPreference, model);
		/*end AAOL 5074*/
	}
	
	public String fetchEmailPreferences(final String emailPreference, final Model model) {
	try {
			
			model.addAttribute("emailPreferences", jnjCustomerFacade.getEmailPreferences(emailPreference));
			/*model.addAttribute("backorderEmailType", "");
			model.addAttribute("shippedOrderEmailType", "");*/
		} catch (Exception e){
		}
	return getRegView(LoginaddonConstants.Register.EMAIL_PREFERENCE);
	}

	/**
	 * @param captchaResponse
	 * @param request
	 * @return
	 */
	@GetMapping("/validateCaptchaResponse")
	@ResponseBody
	public boolean validateSignUpCaptchaResponse(@RequestParam("captchaResponse") final String captchaResponse , final HttpServletRequest request)
	{
		LOG.info("Validating Captcha response");
		if (Config.getParameter(USER_REGISTRATION_DISABLE_RE_CAPTCHA).equalsIgnoreCase("true"))
		{
			return true;
		} 
		
		return validateCaptchaResponse(captchaResponse);
	}

	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}


	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}	
	
	
}
