/**
 *
 */
package com.jnj.b2b.la.jnjlaprofileaddon.controllers.pages;

import com.jnj.b2b.jnjglobalprofile.constants.JnjglobalprofileConstants;
import com.jnj.b2b.jnjglobalprofile.controllers.JnjglobalprofileControllerConstants;
import com.jnj.b2b.jnjglobalprofile.controllers.pages.JnjGTAccountPageController;
import com.jnj.b2b.jnjglobalprofile.forms.JnjGTProfileForm;
import com.jnj.b2b.jnjglobalprofile.forms.JnjGTSecretQuestionForm;
import com.jnj.b2b.la.jnjlaprofileaddon.constants.JnjlaprofileaddonConstants;
import com.jnj.b2b.la.jnjlaprofileaddon.forms.JnjLaProfileForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.forms.UpdatePasswordForm;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.customer.impl.JnjLatamCustomerFacadeImpl;
import com.jnj.facades.data.JnJLaUserAccountPreferenceData;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.facades.data.JnjLaCustomerData;
import com.jnj.facades.data.SecretQuestionData;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.la.core.enums.JnJEmailFrequency;
import com.jnj.la.core.enums.JnJDayOfTheWeek;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Collections;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class JnjLatamAccountPageController extends JnjGTAccountPageController
{
	@Autowired
    private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

	@Autowired
	private JnjLatamCustomerFacadeImpl jnjLatamCustomerFacadeImpl;

	@Autowired
    private UserService  userService;
	@Autowired
	private ConfigurationService configurationService;
	
	private static final Class currentClass = JnjLatamAccountPageController.class;

	@Override
	public String getView(final String view)
	{
		return JnjlaprofileaddonConstants.ADDON_PREFIX + view;
	}

	@Override
	public String getprofile(final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException, DuplicateUidException
	{
		final String methodName = "getprofile()";
		JnjGTCoreUtil.logInfoMessage(Logging.PERSONAL_INFORMATION, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		// Personal Information
		setUpDatAForPersonalInformationPage(model);

		// Email Preferences
		final JnjLaCustomerData customerData = getCurrentLaCustomer();
		setUpPageForEmailPrefrences(model, customerData);
		model.addAttribute("customerData", customerData);

		// Change Password
		model.addAttribute("updatePasswordForm", new UpdatePasswordForm());

		// Change Security Questions
		final JnjGTSecretQuestionForm secretQuestionForm = new JnjGTSecretQuestionForm();
		final List<SecretQuestionData> secretQuestionDataList = jnjLatamCustomerFacadeImpl.getSecretQuestionsForUser();
		if (CollectionUtils.isNotEmpty(secretQuestionDataList))
		{
			secretQuestionForm.setQuestionList(secretQuestionDataList);
		}
		model.addAttribute("secretQuestions", getAllSecretQuestions());
		model.addAttribute("secretQuestionForm", secretQuestionForm);
		model.addAttribute("checkPassword", new UpdatePasswordForm());

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.PERSONAL_INFORMATION_LINK_COMPONENT_ID);
		createBreadCrumbsForProfile(null, model, jnjCommonFacadeUtil.getMessageFromImpex(MyProfile_LABEL));

		JnjGTCoreUtil.logInfoMessage(Logging.PERSONAL_INFORMATION, methodName, Logging.END_OF_METHOD, currentClass);

		return getView(JnjglobalprofileControllerConstants.Views.Pages.Account.AccountProfilePage);
	}

    @Override
	protected void setUpPageForEmailPrefrences(final Model model, final JnjGTCustomerData customerData) {
        JnjLaCustomerData laCustomerData = (JnjLaCustomerData) customerData;
		String methodName = "setUpPageForEmailPrefrences()";
		try
		{
			jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
			
			final List<String> userEmailPreferenceList = laCustomerData.getEmailPreferences();
			final Map<String, String> emailPreferencesList = jnjLatamCustomerFacadeImpl.getEmailPreferences();
			final String userEmailType = jnjLatamCustomerFacadeImpl.getEmailType(laCustomerData);
			final String shippedOrderEmailType = jnjLatamCustomerFacadeImpl.getShippedOrderEmailType(laCustomerData);
			model.addAttribute("emailPrefrencesList", emailPreferencesList);
			model.addAttribute("userEmailPrefrenceList", userEmailPreferenceList);
			model.addAttribute("emailPrefrenceForm", new JnjGTProfileForm());
			model.addAttribute("backorderEmailType", userEmailType);
			model.addAttribute("shippedOrderEmailType", shippedOrderEmailType);
			model.addAttribute("currentAccountPreferenceData", laCustomerData.getCurrentAccountPreference());

			model.addAttribute("orderTypes", getJnjOrderTypes());
			model.addAttribute("weekDay", addWeekDaysToModel());
			JnjLatamAccountPageController.addMonthDaysToModel(model, laCustomerData);
			JnjLatamAccountPageController.addConsolidatedEmailFrequency(model, laCustomerData);
			addEmailPeriodicityToModel(model, laCustomerData);
            storeCmsPageInModel(model, getContentPageForLabelOrId(EMAIL_PREFRENCES_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(EMAIL_PREFRENCES_CMS_PAGE));
		}
		catch (DuplicateUidException | CMSItemNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.PERSONAL_INFORMATION, methodName,
					"An error occurred while setting up email preferences data for my profile page.", exception, currentClass);
		}
	}

	private Map<String,String> getJnjOrderTypes() {
		Map<String,String> orderTypes = new HashMap<>();
		if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(
				Jnjlab2bcoreConstants.JNJ_CONSOLIDATED_EMAIL_ORDER_TYPES))) {
			List<String> orderTypesKey = Arrays.asList(configurationService.getConfiguration().getString(
					Jnjlab2bcoreConstants.JNJ_CONSOLIDATED_EMAIL_ORDER_TYPES).split(Jnjlab2bcoreConstants.CONST_COMMA));
			orderTypesKey.stream().forEach(ot -> orderTypes.put(JnjOrderTypesEnum.valueOf(ot).getCode(),
					enumerationService.getEnumerationName(JnjOrderTypesEnum.valueOf(ot))));
		}
		return orderTypes;
	}

	private void addEmailPeriodicityToModel(Model model, JnjLaCustomerData laCustomerData) {
        if (laCustomerData.getCurrentAccountPreference() != null) {
            model.addAttribute("emailPeriodicity", laCustomerData.getCurrentAccountPreference().getPeriodicity());
        } else {
            model.addAttribute("emailPeriodicity", JnJEmailPeriodicity.NONE);
        }
    }

	private Map<String,String> addWeekDaysToModel() {
		Map<String,String> weekDays = new HashMap<>();
		if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(
				Jnjlab2bcoreConstants.JNJ_CONSOLIDATED_EMAIL_WEEK_DAYS))) {
			List<String> weekDaysKey = Arrays.asList(configurationService.getConfiguration().getString(
					Jnjlab2bcoreConstants.JNJ_CONSOLIDATED_EMAIL_WEEK_DAYS).split(Jnjlab2bcoreConstants.CONST_COMMA));
			weekDaysKey.stream().forEach(wd -> weekDays.put(JnJDayOfTheWeek.valueOf(wd).getCode(),
					enumerationService.getEnumerationName(JnJDayOfTheWeek.valueOf(wd))));
		}
		return weekDays;
	}

	private static void addMonthDaysToModel(Model model, final JnjLaCustomerData laCustomerData) {
		if (laCustomerData.getCurrentAccountPreference() != null && JnJEmailFrequency.MONTHLY.equals(laCustomerData.getCurrentAccountPreference().getConsolidatedEmailFrequency())) {
			model.addAttribute("monthDates", laCustomerData.getCurrentAccountPreference().getDaysOfTheMonth());
		}
	}

	private static void addConsolidatedEmailFrequency(Model model, final JnjLaCustomerData laCustomerData) {
		if (laCustomerData.getCurrentAccountPreference() != null && laCustomerData.getCurrentAccountPreference().getConsolidatedEmailFrequency() != null) {
			model.addAttribute("jnJEmailFrequency", laCustomerData.getCurrentAccountPreference().getConsolidatedEmailFrequency());
		}
	}

    /**
	 * This method is responsible for setting up the Data for Personal Information in My Profile Page
	 * 
	 * @param model
	 */
	@Override
	protected void setUpDatAForPersonalInformationPage(final Model model)
	{
		final String methodName = "setUpDatAForPersonalInformationPage()";
		JnjGTCoreUtil.logInfoMessage(Logging.PERSONAL_INFORMATION, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final JnjLaCustomerData customerData = getCurrentLaCustomer();
		mapRoleExclusions(model);
		populateDisplayOrderNotificationPreference(model, customerData);

		model.addAttribute("updateProfileForm", new JnjGTProfileForm());
		model.addAttribute("customerData", customerData);

		JnjGTCoreUtil.logInfoMessage(Logging.PERSONAL_INFORMATION, methodName, Logging.END_OF_METHOD, currentClass);
	}

	private void mapRoleExclusions(Model model) {
		final Map<String, String> roleMap = new HashMap<>();
		final List<String> roles = new ArrayList<>();

		Set<PrincipalGroupModel> groups = userService.getCurrentUser().getAllGroups();
		for (PrincipalGroupModel group : groups){
			roles.add(group.getUid());
		}

		roles.retainAll(jnjLatamCommonFacadeUtil.getEffectiveGroups());

		for (final String role : roles)
		{
			UserGroupModel usergroup = userService.getUserGroupForUID(role);
			roleMap.put(role, usergroup.getDisplayName(getI18nService().getCurrentLocale()));
		}
		model.addAttribute("roleMap", roleMap);
	}

	/**
	 * This method is responsible for saving /personalInformation of My Profile
	 */
	@Override
	public String saveProfile(@Valid final JnjGTProfileForm jnjGTProfileForm, final BindingResult bindingResult, final Model model,
			RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, DuplicateUidException
	{
		final String methodName = "saveProfile()";
		JnjGTCoreUtil.logInfoMessage(Logging.PERSONAL_INFORMATION, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		final JnjLaCustomerData customerData = getCurrentLaCustomer();
		customerData.setUid(jnjLatamCustomerFacadeImpl.getCurrentCustomer().getUid());

		updateAddressData((JnjGTAddressData) customerData.getContactAddress(), jnjGTProfileForm, model);

		updateLanguageData(jnjGTProfileForm, customerData);

        jnjLatamCustomerFacadeImpl.updateCustomer(customerData);
        model.addAttribute(SUCCESS, Boolean.TRUE);
        redirectAttributes.addFlashAttribute(SUCCESS, Boolean.TRUE);

		setUpDatAForPersonalInformationPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("selectedLink", JnjglobalprofileConstants.Profile.PERSONAL_INFORMATION_LINK_COMPONENT_ID);

		JnjGTCoreUtil.logInfoMessage(Logging.PERSONAL_INFORMATION, methodName, Logging.END_OF_METHOD, currentClass);
		return REDIRECT_PREFIX + "/my-account/personalInformation";
	}

    @PostMapping("/laEmailPreferences")
    @RequireHardLogIn
	public String saveEmailPreferences(final Model model, @Valid final JnjLaProfileForm jnjLaProfileForm, RedirectAttributes redirectAttributes) {
		String methodName = "saveEmailPreferences()";
		JnjGTCoreUtil.logInfoMessage(Logging.PERSONAL_INFORMATION, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		final JnjLaCustomerData customerData = getCurrentLaCustomer();
		List<String> monthDates = null;
		List<JnjOrderTypesEnum> orderTypes = new ArrayList<>();
		customerData.setEmailPreferences(jnjLaProfileForm.getEmailPreferences());
        JnJLaUserAccountPreferenceData jnJLaUserAccountPreferenceData = new JnJLaUserAccountPreferenceData();
        jnJLaUserAccountPreferenceData.setPeriodicity(jnjLaProfileForm.getEmailPeriodicity());
		if(JnJEmailPeriodicity.CONSOLIDATED.equals(jnjLaProfileForm.getEmailPeriodicity())) {
			for(JnjOrderTypesEnum jnjOrderType: jnjLaProfileForm.getOrderTypes()) {
				orderTypes.add(jnjOrderType);
			}
			jnJLaUserAccountPreferenceData.setOrderTypes(orderTypes);
			jnJLaUserAccountPreferenceData.setConsolidatedEmailFrequency(jnjLaProfileForm.getJnJEmailFrequency());
			if (JnJEmailFrequency.DAILY.equals(jnjLaProfileForm.getJnJEmailFrequency())){
				jnJLaUserAccountPreferenceData.setDayOfTheWeek(null);
				jnJLaUserAccountPreferenceData.setDaysOfTheMonth(Collections.emptyList());
			}
			if (JnJEmailFrequency.WEEKLY.equals(jnjLaProfileForm.getJnJEmailFrequency())) {
				jnJLaUserAccountPreferenceData.setDaysOfTheMonth(Collections.emptyList());
				jnJLaUserAccountPreferenceData.setDayOfTheWeek(jnjLaProfileForm.getDayOfTheWeek());
			}
			if (JnJEmailFrequency.MONTHLY.equals(jnjLaProfileForm.getJnJEmailFrequency())  && CollectionUtils.isNotEmpty(jnjLaProfileForm.getDaysOfTheMonth())) {
				monthDates = jnjLaProfileForm.getDaysOfTheMonth().stream().filter(str -> null != str && str.trim().length() > 0).collect(Collectors.toList());
				jnJLaUserAccountPreferenceData.setDayOfTheWeek(null);
				jnJLaUserAccountPreferenceData.setDaysOfTheMonth(monthDates);
			}
		} else {
			jnJLaUserAccountPreferenceData.setConsolidatedEmailFrequency(null);
			jnJLaUserAccountPreferenceData.setOrderTypes(null);
			jnJLaUserAccountPreferenceData.setDayOfTheWeek(null);
			jnJLaUserAccountPreferenceData.setDaysOfTheMonth(Collections.emptyList());
		}

        customerData.setCurrentAccountPreference(jnJLaUserAccountPreferenceData);
        jnjLatamCustomerFacadeImpl.updateCustomer(customerData);
        model.addAttribute(SUCCESS, Boolean.TRUE);
        redirectAttributes.addFlashAttribute(SUCCESSEMAIL, Boolean.TRUE);
		setUpPageForEmailPrefrences(model, customerData);
		JnjGTCoreUtil.logInfoMessage(Logging.PERSONAL_INFORMATION, methodName, Logging.END_OF_METHOD, currentClass);
		return REDIRECT_PREFIX + "/my-account/personalInformation";
	}

	public void populateDisplayOrderNotificationPreference(Model model, JnjLaCustomerData customerData) {
		final Boolean displayOrderNotificationPreference = customerData.getCurrentB2BUnitID() == null ? Boolean.FALSE : Boolean.TRUE;
		model.addAttribute("displayOrderNotificationPreference", displayOrderNotificationPreference);
	}

	private void updateLanguageData(JnjGTProfileForm jnjGTProfileForm, JnjGTCustomerData customerData)
	{
		LanguageData languageData = customerData.getLanguage();
		String language = jnjGTProfileForm.getCountry();
		if (languageData != null && languageData.getIsocode().equalsIgnoreCase(language))
		{
			languageData.setIsocode(language);
		}
	}

	private void updateAddressData(JnjGTAddressData addressData, JnjGTProfileForm jnjGTProfileForm, Model model)
	{
		String methodName = "updateAddressData()";
		if (addressData != null)
		{
			addressData.setDefaultAddress(true);

			if (StringUtils.isNotEmpty(jnjGTProfileForm.getPhoneNumberPrefix())
					|| StringUtils.isNotEmpty(jnjGTProfileForm.getPhone()))
			{
				addressData.setPhone(
						jnjGTProfileForm.getPhoneNumberPrefix() + JnjglobalprofileConstants.SYMBOl_PIPE + jnjGTProfileForm.getPhone());
			}

			if (StringUtils.isNotEmpty(jnjGTProfileForm.getMobileNumberPrefix())
					|| StringUtils.isNotEmpty(jnjGTProfileForm.getMobile()))
			{
				addressData.setMobile(jnjGTProfileForm.getMobileNumberPrefix() + JnjglobalprofileConstants.SYMBOl_PIPE
						+ jnjGTProfileForm.getMobile());
			}
		}
		else
		{
			model.addAttribute(SUCCESS, Boolean.TRUE);
			JnjGTCoreUtil.logErrorMessage(Logging.PERSONAL_INFORMATION, methodName,
					"Profile not saved sucessfully. Address is null for the current user.", currentClass);
		}
	}

    private JnjLaCustomerData getCurrentLaCustomer() {
        return (JnjLaCustomerData) jnjLatamCustomerFacadeImpl.getCurrentCustomer();
    }

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
