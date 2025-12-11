/**
 *
 */
package com.jnj.b2b.la.loginaddon.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.servicelayer.session.SessionService;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
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
import com.jnj.b2b.loginaddon.controllers.pages.JnJGTRegistrationPageController;
import com.jnj.b2b.loginaddon.forms.LoginForm;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjCountryData;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.dto.JnjLatamCompanyInfoData;
import com.jnj.la.core.dto.JnjLatamRegistrationData;
import com.jnj.loginaddon.forms.JnjGTRegistrationForm;
import com.jnj.b2b.la.loginaddon.forms.JnjLatamRegistrationForm;
import com.jnj.facades.customer.JnjLatamCustomerFacade;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.b2b.la.loginaddon.controllers.JnjlaloginaddonControllerConstants;


/**
 * @author skant3
 *
 */

@Scope("tenant")
@RequestMapping(value = "/register")
public class JnjLatamRegistrationPageController extends JnJGTRegistrationPageController
{
	private static final String REGISTRATION_FORM = "registrationForm";
	
	private static final String IS_COMMERCIAL_USER = "isCommercialUser";

	private static Class className = JnjLatamRegistrationPageController.class;

	@Autowired
	protected JnjLatamCustomerFacade jnjCustomerFacade;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
    private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

	@Override
	public String getRegister(final Model model, final JnjGTRegistrationForm jnjRegistrationForm) throws CMSItemNotFoundException
	{
		 if(sessionService.getAttribute("SSRFFailed") != null) {
			 model.addAttribute(LoginaddonConstants.Register.REGISTRATION_ERROR, configurationService.getConfiguration()
						.getString(LoginaddonConstants.Register.SSRF_CHECK_FAILED_INPUT));
			 sessionService.removeAttribute("SSRFFailed");
		 }
		JnjLatamRegistrationForm laRegistrationForm = null;
		if (jnjRegistrationForm instanceof JnjLatamRegistrationForm)
		{
			laRegistrationForm = (JnjLatamRegistrationForm) jnjRegistrationForm;
		}
		if (!model.containsAttribute(REGISTRATION_FORM))
		{
			model.addAttribute(REGISTRATION_FORM, new JnjLatamRegistrationForm());
		}
		
		model.addAttribute(IS_COMMERCIAL_USER, jnjLatamCommonFacadeUtil.isCommercialUserEnabledForCurrentSite());

		if (null != laRegistrationForm)
		{
			return getDefaultRegistrationPage(model, false, laRegistrationForm);
		}
		else
		{
			return getDefaultRegistrationPage(model, true, laRegistrationForm);
		}
	}

	@PostMapping("/laprocess")
	public String getRegister(@ModelAttribute(REGISTRATION_FORM) final JnjLatamRegistrationForm registrationForm,
			final Model model, final HttpServletRequest request, final HttpServletResponse response,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{

		final String methodName = "getRegister()";

		final JnjLatamRegistrationData jnjRegistrationData = storeRegistrationData(registrationForm);

		boolean status = false;
		try
		{

			status = jnjCustomerFacade.registerUser(jnjRegistrationData, JnjWebUtil.getUnsecureServerUrl(request));

		}
		catch (final DuplicateUidException exp)
		{
			JnjGTCoreUtil.logWarnMessage(Jnjb2bCoreConstants.Logging.REGISTRAION, methodName, "registration failed: ", exp,
					className);
			model.addAttribute(registrationForm);
			model.addAttribute(new LoginForm());
			model.addAttribute(new JnjLatamRegistrationForm()); //If user is already registered display error message.
			model.addAttribute(LoginaddonConstants.Register.REGISTRATION_ERROR, LoginaddonConstants.Register.REGISTRATION_ERROR_MSG);
			return handleRegistrationError(model);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logWarnMessage(Jnjb2bCoreConstants.Logging.REGISTRAION, methodName, "registration failed: ", exception,
					className);
			model.addAttribute(LoginaddonConstants.Register.REGISTRATION_ERROR,
					LoginaddonConstants.Register.REGISTRATION_GLOBAL_MSG);
			model.addAttribute("registrationForm", new JnjLatamRegistrationForm());
			handleRegistrationError(model);
		}

		if (status)
		{
			return onSuccessfulRegistration(model);
		}

		return null;
	}

	protected JnjLatamRegistrationData storeRegistrationData(final JnjLatamRegistrationForm registrationForm)
	{
		final JnjLatamRegistrationData jnjRegistrationData = new JnjLatamRegistrationData();
		jnjRegistrationData.setCompanyInfoData(storeCompanyInfo(registrationForm));
		jnjRegistrationData.setJnjUserInfoData(storeUserInfo(registrationForm));
		jnjRegistrationData.setCommercialUserFlag(registrationForm.getCommercialUserFlag());
		jnjRegistrationData.setCommercialUserSector(registrationForm.getCommercialUserSector());
		jnjRegistrationData.setPassword(registrationForm.getPassword());
		jnjRegistrationData.setEmailPreferences(registrationForm.getEmailPreferences());
		jnjRegistrationData.setSecretQuestionsAnswers(registrationForm.getSecretQuestionsAnswers());
		return jnjRegistrationData;
	}

	protected JnjLatamCompanyInfoData storeCompanyInfo(final JnjLatamRegistrationForm jnjRegistrationForm)
	{
		final JnjLatamCompanyInfoData companyInfoData = new JnjLatamCompanyInfoData();
		companyInfoData.setCustomerCode(jnjRegistrationForm.getCustomerCode());
		companyInfoData.setSoldTo(jnjRegistrationForm.getSoldTo());
		companyInfoData.setJnjAccountManager(jnjRegistrationForm.getJnjAccountManager());
		companyInfoData.setShipToCountry(jnjRegistrationForm.getShipToCountry());
		companyInfoData.setShipToLine1(jnjRegistrationForm.getShipToLine1());
		companyInfoData.setShipToLine2(jnjRegistrationForm.getShipToLine2());
		companyInfoData.setShipToCity(jnjRegistrationForm.getShipToCity());
		companyInfoData.setShipToState(jnjRegistrationForm.getShipToState());
		companyInfoData.setShipToZipCode(jnjRegistrationForm.getShipToZipCode());
		return companyInfoData;
	}

	@Override
	protected String getRegView(final String pageName)
	{
		if ("activation".equals(pageName))
		{
			return getView(LoginaddonControllerConstants.Views.Pages.Account.UserActivationPage);
		}
		else
		{
			return getLatamView(LoginaddonControllerConstants.Views.Pages.Account.AccountRegisterPage);
		}
	}

	public String getLatamView(final String view)
	{
		return JnjlaloginaddonControllerConstants.ADDON_PREFIX + view;
	}

	protected String getDefaultRegistrationPage(final Model model, final boolean formRefreshFlag,
			final JnjLatamRegistrationForm jnjRegistrationForm) throws CMSItemNotFoundException
	{

		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());

		model.addAttribute(LoginaddonWebConstants.BREADCRUMBS_KEY,
				contentPageBreadcrumbBuilder.getBreadcrumbs((ContentPageModel) getCmsPage()));
		if (formRefreshFlag)
		{
			model.addAttribute(new JnjLatamRegistrationForm());
		}
		else
		{
			model.addAttribute(jnjRegistrationForm);
			model.addAttribute("registerForm", jnjRegistrationForm);
		}
		setCurrentSiteNameInSession();
		setupUpModelForRegistration(model, jnjRegistrationForm);

		return getRegView(LoginaddonConstants.Register.REGISTER);
	}

	private void setCurrentSiteNameInSession() {
		final String currentSiteType = cmsSiteService.getCurrentSite().getJnjWebSiteType().getCode();
		final String siteName = currentSiteType.equalsIgnoreCase(LoginaddonConstants.MDD_SITE_ID) ? LoginaddonConstants.MDD : LoginaddonConstants.CONS;
		sessionService.setAttribute(LoginaddonConstants.SITE_NAME, siteName);
	}

	protected void setupUpModelForRegistration(final Model model, final JnjLatamRegistrationForm jnjRegistrationForm)
	{
		final List<JnjCountryData> countryList = jnjCustomerFacade.getCountries();
		model.addAttribute("countryList", countryList);
		model.addAttribute("emailPreferences", getEmailPreferences());
		model.addAttribute("secretQuestions", getSecurityQuestions());
		model.addAttribute("countryCodes", jnjCustomerFacade.getPhoneCodes("CA"));
		model.addAttribute("stateList", jnjCustomerFacade.getRegions("CA"));
	}

	@Override
	public List<RegionData> getStates(@RequestParam("country") final String country)
	{
		List<RegionData> list = new ArrayList<>();
		list = jnjCustomerFacade.getRegions(country);
		return list;
	}

	@PostMapping("/getCodes")
	public @ResponseBody List<String> getCodes(@RequestParam("country") final String country)
	{
		List<String> list = new ArrayList<>();
		list = jnjCustomerFacade.getPhoneCodes(country);
		return list;
	}

	@GetMapping("/laResetPasswordForNewUser")
	public String resetPasswordForNewUser(
			@RequestParam(value = "passwordExpireToken", required = true) final String passwordExpireToken,
			@RequestParam(value = "email", required = true) final String email, Model model)
	{
		String methodName = "resetPasswordForNewUser()";
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(LoginaddonConstants.Register.FIRST_RESET_PASSWORD));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LoginaddonConstants.Register.FIRST_RESET_PASSWORD));
			model.addAttribute("email", email);
			return JnjlaloginaddonControllerConstants.ADDON_PREFIX + LoginaddonControllerConstants.Views.Pages.Password.FirstTimeResetPassword;
		}
		catch (CMSItemNotFoundException e)
		{
			JnjGTCoreUtil.logWarnMessage(Jnjb2bCoreConstants.Logging.REGISTRAION, methodName, "Reset Password Failed : ", e,
					className);
		}
		return null;
	}
}