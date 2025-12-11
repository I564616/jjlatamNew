/**
 *
 */
package com.jnj.b2b.loginaddon.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTContactUsData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.facades.help.JnjGTContactUsFacade;
import com.jnj.b2b.loginaddon.forms.JnjGTContactUsForm;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.loginaddon.recaptcha.ReCaptchaImpl;
import com.jnj.b2b.loginaddon.recaptcha.ReCaptchaResponse;
import com.jnj.utils.CommonUtil;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;


/**
 * @author balinder.singh
 * 
 */
@Controller("JnJGTHelpPageController")
@RequestMapping("/help")
public class JnjGTHelpPageController extends AbstractPageController
{
	@Resource(name = "httpSessionRequestCache")
	protected HttpSessionRequestCache httpSessionRequestCache;

	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Autowired
	protected CMSSiteService cmsSiteService;

	/** The session service **/
	@Autowired
	protected SessionService sessionService;

	@Resource(name="ContactUsFacade")
	protected JnjGTContactUsFacade jnjGTContactUsFacade;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade customerFacade;

	@Autowired
	protected UserFacade userFacade;

	public HttpSessionRequestCache getHttpSessionRequestCache() {
		return httpSessionRequestCache;
	}

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTContactUsFacade getJnjGTContactUsFacade() {
		return jnjGTContactUsFacade;
	}

	public JnjGTCustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	public UserFacade getUserFacade() {
		return userFacade;
	}

	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}
	/** The Constant HELP. */
	protected static final String HELP = "Help";

	protected static final String HELP_LABEL = "help.page.label";

	protected static final String HELP_CMS_PAGE = "helpPage";

	protected static final String CONTACT_US_PAGE = "contactUs";

	protected static final String SUBJECT_DROP_DOWN = "subjectDropDown";

	protected static final String EMAIL_SENT_SUCCESSUFULLY = "mailSent";

	protected static final String CUSTOMER_NAME = "nameOfCustomer";

	protected static final String CUSTOMER_EMAIL = "emailOfCustomer";

	protected static final String ANONYMOUS = "Anonymous";

	protected static final String CONTACT_US = "CONTACT US";
	
	protected static final String HELP_CONTACTUS_DISABLE_RE_CAPTCHA = "help.contactus.disable.reCaptcha";

	/** Getting the LOGGER. */
	public static final Logger LOG = Logger.getLogger(JnjGTHelpPageController.class);
	protected final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";

	
	
	
	
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	/**
	 * 
	 * This method is used for fetching the login page by setting essential data in the model
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping
	public String getHelpPage(final Model model, final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		logMethodStartOrEnd(HELP, "getHelpPage()", Logging.BEGIN_OF_METHOD);
		populatePageData(model);
		model.addAttribute("contactUsForm", new JnjGTContactUsForm());
		final String helpLabel = jnjCommonFacadeUtil.getMessageFromImpex(HELP_LABEL);
		final ContentPageModel pageModel = getContentPageForLabelOrId(HELP_CMS_PAGE);
		final PageTemplateModel templateModel = pageModel.getMasterTemplate();
		storeCmsPageInModel(model, pageModel);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(HELP_CMS_PAGE));
		createBreadCrumbsForHelp(null, model, helpLabel);
		logMethodStartOrEnd(HELP, "getHelpPage()", Logging.END_OF_METHOD);

		//return new ModelAndView(templateModel.getFrontendTemplateName());
		return getView(templateModel.getFrontendTemplateName());
	}

	/*


	*//**
	 * This will populate the contact us popup page
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return view
	 * @throws CMSItemNotFoundException
	 */

	@GetMapping("/contactUs")
	public String getContactUsPage(final Model model, final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session) throws CMSItemNotFoundException
	{
	System.out.println("inside getContactUsPage");
		return contactUsPopUp(model);
		
	}
	
	public String contactUsPopUp(final Model model)throws CMSItemNotFoundException
	{
		System.out.println("inside contactUsPopUp");
		logMethodStartOrEnd(HELP, "getContactUsPage()", Logging.BEGIN_OF_METHOD);
		populatePageData(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(CONTACT_US_PAGE));
		logMethodStartOrEnd(HELP, "getContactUsPage()", Logging.END_OF_METHOD);
		model.addAttribute("contactUsForm", new JnjGTContactUsForm());
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
	
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return getView(LoginaddonControllerConstants.Views.Pages.Help.ContactUsPopUpPage);
		
	}

	/**
	 * This method will send the email to the respective user on the basis of subject selected
	 * 
	 * @param model
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/contactUs")
	@ResponseBody
	public void sendEmail(final JnjGTContactUsForm JnjGTContactUsForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "sendEmail()";
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		populatePageData(model);
		try
		{
			final String helpLabel = jnjCommonFacadeUtil.getMessageFromImpex(HELP_LABEL);
			storeCmsPageInModel(model, getContentPageForLabelOrId(HELP_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(HELP_CMS_PAGE));
			createBreadCrumbsForHelp(null, model, helpLabel);
			final JnjGTContactUsData jnjGTContactUsData = setContactUsData(JnjGTContactUsForm);
			jnjGTContactUsData.setSiteLogoURL(JnjWebUtil.getUnsecureServerUrl(request));

			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Sending email...", LOG);
			jnjGTContactUsFacade.sendMail(jnjGTContactUsData);
			sessionService.setAttribute(EMAIL_SENT_SUCCESSUFULLY, Boolean.TRUE);
		}
		catch (final BusinessException businessException)
		{
			LOG.error(HELP + Logging.HYPHEN + "sendEmail()" + Logging.HYPHEN + businessException.getMessage());
			sessionService.setAttribute(EMAIL_SENT_SUCCESSUFULLY, Boolean.FALSE);
		}
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.END_OF_METHOD, LOG);
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
	 * To create breadcrumb for the help page
	 * 
	 * @param formURI
	 * @param model
	 * @param formSpecificKey
	 */
	protected void createBreadCrumbsForHelp(final String formURI, final Model model, final String formSpecificKey)
	{
		final String METHOD_NAME = "createBreadCrumbsForHelp()";
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(formURI, formSpecificKey, null));
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.END_OF_METHOD);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
	}

	//data being set from page form to data class
	protected JnjGTContactUsData setContactUsData(final JnjGTContactUsForm JnjGTContactUsForm)
	{
		final JnjGTContactUsData jnjGTContactUsData = new JnjGTContactUsData();
		final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
		if (!ANONYMOUS.equalsIgnoreCase(currentCustomerData.getName()))
		{
			jnjGTContactUsData.setFromName(currentCustomerData.getName());
			jnjGTContactUsData.setFromEmail(currentCustomerData.getEmail());
			if (currentCustomerData instanceof JnjGTCustomerData)
			{
				if (null != ((JnjGTCustomerData) currentCustomerData).getContactAddress())
				{
					jnjGTContactUsData.setContactNumber(((JnjGTCustomerData) currentCustomerData).getContactAddress().getPhone());
				}
			}

		}
		else
		{
			if (StringUtils.isNotEmpty(JnjGTContactUsForm.getContactUsFromName()))
			{
				jnjGTContactUsData.setFromName(JnjGTContactUsForm.getContactUsFromName());
			}
			else
			{
				jnjGTContactUsData.setFromName(JnjGTContactUsForm.getContactUsFromNameLb());
			}
			if (StringUtils.isNotEmpty(JnjGTContactUsForm.getContactUsEmail()))
			{
				jnjGTContactUsData.setFromEmail(JnjGTContactUsForm.getContactUsEmail());
			}
			else
			{
				jnjGTContactUsData.setFromEmail(JnjGTContactUsForm.getContactUsEmailLb());
			}
		}
		if (StringUtils.isNotEmpty(JnjGTContactUsForm.getContactUsMessage()))
		{
			jnjGTContactUsData.setDetailInquiry(JnjGTContactUsForm.getContactUsMessage());
		}
		else
		{
			jnjGTContactUsData.setDetailInquiry(JnjGTContactUsForm.getContactUsMessage());
		}
		if (StringUtils.isNotEmpty(JnjGTContactUsForm.getContactUsSubject()))
		{
			jnjGTContactUsData.setSubjectSelected(JnjGTContactUsForm.getContactUsSubject());
		}
		else
		{
			jnjGTContactUsData.setSubjectSelected(JnjGTContactUsForm.getContactUsSubjectLb());
		}
		if (StringUtils.isNotEmpty(JnjGTContactUsForm.getContactUsOrderNumber()))
		{
			jnjGTContactUsData.setOrderID(JnjGTContactUsForm.getContactUsOrderNumber());
		}
		else
		{
			jnjGTContactUsData.setOrderID(JnjGTContactUsForm.getContactUsOrderNumberLb());
		}
		jnjGTContactUsData.setProductID(JnjGTContactUsForm.getContactUsProductNumber());
		return jnjGTContactUsData;
	}

	//default page data being set
	protected void populatePageData(final Model model)
	{
		final String METHOD_NAME = "populatePageData()";
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		if (!userFacade.isAnonymousUser())
		{
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "User is not anonymous", LOG);
			model.addAttribute(CUSTOMER_NAME, customerFacade.getCurrentCustomer().getName());
			model.addAttribute(CUSTOMER_EMAIL, customerFacade.getCurrentCustomer().getEmail());
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "User name and email set", LOG);
		}
		else
		{
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "User is anonymous", LOG);
		}
		model.addAttribute(SUBJECT_DROP_DOWN, jnjGTContactUsFacade.getSubjectDropDown());
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}
	
	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
	}
	
	
	
	@GetMapping("/validateCaptchaResponse")
	@ResponseBody
	public boolean validateContactUsCaptchaResponse(@RequestParam("captchaResponse") final String captchaResponse , final HttpServletRequest request)
	{
		LOG.info("Validating Captcha response");
		if (Config.getParameter(HELP_CONTACTUS_DISABLE_RE_CAPTCHA).equalsIgnoreCase("true"))
		{
			return true;
		}
		return validateCaptchaResponse(captchaResponse);
	}

	
}
