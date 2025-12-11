/**
 *
 */
package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.core.services.customer.impl.JnjLatamCustomerServiceImpl;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.session.SessionService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import  de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import com.jnj.b2b.loginaddon.controllers.pages.JnjGTHelpPageController;
import com.jnj.b2b.loginaddon.forms.JnjGTContactUsForm;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTContactUsData;
import com.jnj.facades.help.JnjGTContactUsFacade;
import com.jnj.b2b.la.loginaddon.controllers.JnjlaloginaddonControllerConstants;
import com.jnj.b2b.la.loginaddon.constants.JnjlaloginaddonWebConstants;
import de.hybris.platform.servicelayer.i18n.I18NService;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author plahiri1
 *
 */
@Controller
@Scope("tenant")
@RequestMapping("/help")
public class JnJLatamHelpPageController extends JnjGTHelpPageController
{

	private static final String ENTERED_CONDITION_SHOW_CHANGE_ACCOUNT_LINK = "entered condition...........showChangeAccountLink :";
	private static final String SHOW_CHANGE_ACCOUNT_LINK_VALUE = "showChangeAccountLink value : ";
	private static final String FAQ_COUNT_NEW_PORTAL = JnjlaloginaddonControllerConstants.Faq.FAQ_COUNT_NEW_PORTAL;
	private static final String FAQ_COUNT_PLACE_ORDER = JnjlaloginaddonControllerConstants.Faq.FAQ_COUNT_PLACE_ORDER;
	private static final String FAQ_COUNT_ORDER_STATUS = JnjlaloginaddonControllerConstants.Faq.FAQ_COUNT_ORDER_STATUS;
	private static final String FAQ_COUNT_BIDS = JnjlaloginaddonControllerConstants.Faq.FAQ_COUNT_BIDS;
	private static final String COUNT_USEFUL_LINKS = JnjlaloginaddonControllerConstants.Faq.COUNT_USEFUL_LINKS;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	/** The session service **/
	@Autowired
	protected SessionService sessionService;

	@Resource(name="ContactUsFacade")
	protected JnjGTContactUsFacade jnjGTContactUsFacade;

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Autowired
	protected JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;
	
	@Autowired
	protected JnjLatamCustomerServiceImpl jnjLatamCustomerService;
	
	@Autowired
	protected I18NService i18nService;



	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTContactUsFacade getJnjGTContactUsFacade() {
		return jnjGTContactUsFacade;
	}

	protected final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";
	
	@Override
	@RequireHardLogIn
	public String getHelpPage(final Model model, final HttpServletRequest request, final HttpServletResponse response,
							  final HttpSession session) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getHelpPage()";

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnJLatamHelpPageController.class);

		populatePageData(model);

		populateFaqAttributes(model);

		model.addAttribute("contactUsForm", new JnjGTContactUsForm());
		final String helpLabel = jnjCommonFacadeUtil.getMessageFromImpex(HELP_LABEL);
		final ContentPageModel pageModel = getContentPageForLabelOrId(HELP_CMS_PAGE);
		storeCmsPageInModel(model, pageModel);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(HELP_CMS_PAGE));
		createBreadCrumbsForHelp(null, model, helpLabel);
		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, Logging.END_OF_METHOD, JnJLatamHelpPageController.class);
		
		final String countryIsoCode = jnjLatamCustomerService.getCountry();
		Locale locale = i18nService.getCurrentLocale();
		model.addAttribute("locale", locale);
		model.addAttribute("countryIsoCode", countryIsoCode);
		
		return getLatamView(JnjlaloginaddonControllerConstants.Views.Pages.Help.HelpPage);
	}

	private void populateFaqAttributes(Model model) {
		final String METHOD_NAME = "populateFaqAttributes()";

		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnJLatamHelpPageController.class);

		String newPortalCount = jnjConfigService.getConfigValueById(FAQ_COUNT_NEW_PORTAL);
		String placeOrderCount = jnjConfigService.getConfigValueById(FAQ_COUNT_PLACE_ORDER);
		String orderStatusCount = jnjConfigService.getConfigValueById(FAQ_COUNT_ORDER_STATUS);
		String bidsCount = jnjConfigService.getConfigValueById(FAQ_COUNT_BIDS);
		String linksCount = jnjConfigService.getConfigValueById(COUNT_USEFUL_LINKS);

		if (null != newPortalCount)
			model.addAttribute(FAQ_COUNT_NEW_PORTAL, Integer.parseInt(newPortalCount));
		if (null != placeOrderCount)
			model.addAttribute(FAQ_COUNT_PLACE_ORDER, Integer.parseInt(placeOrderCount));
		if (null != orderStatusCount)
			model.addAttribute(FAQ_COUNT_ORDER_STATUS, Integer.parseInt(orderStatusCount));
		if (null != bidsCount)
			model.addAttribute(FAQ_COUNT_BIDS, Integer.parseInt(bidsCount));
		if (null != linksCount)
			model.addAttribute(COUNT_USEFUL_LINKS, Integer.parseInt(linksCount));

		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, Logging.END_OF_METHOD, JnJLatamHelpPageController.class);
	}

	@Override
	protected void createBreadCrumbsForHelp(final String formURI, final Model model, final String formSpecificKey)
	{
		final String METHOD_NAME = "createBreadCrumbsForHelp()";
		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnJLatamHelpPageController.class);
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb(formURI, formSpecificKey, null));
		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, Logging.END_OF_METHOD, JnJLatamHelpPageController.class);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
	}

	@Override
	public String getContactUsPage(final Model model, final HttpServletRequest request, final HttpServletResponse response,
								   final HttpSession session) throws CMSItemNotFoundException
	{
		return contactUsPopUp(model);
	}

	@Override
	public String contactUsPopUp(final Model model)throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "contactUsPopUp()";
		model.addAttribute(EMAIL_SENT_SUCCESSUFULLY, Boolean.TRUE);
		if (sessionService.getAttribute("SSRFFailed") != null) {
			sessionService.setAttribute(JnjlaloginaddonWebConstants.Help.EMAIL_SENT_SUCCESSUFULLY, Boolean.FALSE);
			sessionService.removeAttribute("SSRFFailed");
		}

		if (sessionService.getAttribute(JnjlaloginaddonWebConstants.Help.EMAIL_SENT_SUCCESSUFULLY) != null
				&& BooleanUtils.isNotTrue(sessionService.getAttribute(
						JnjlaloginaddonWebConstants.Help.EMAIL_SENT_SUCCESSUFULLY))) {
			model.addAttribute(EMAIL_SENT_SUCCESSUFULLY, Boolean.FALSE);
			sessionService.removeAttribute(JnjlaloginaddonWebConstants.Help.EMAIL_SENT_SUCCESSUFULLY);
		}

		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.CONTACT_US, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnJLatamHelpPageController.class);
		populatePageData(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(JnjlaloginaddonWebConstants.Help.CONTACT_US_PAGE));
		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.CONTACT_US, METHOD_NAME, Logging.END_OF_METHOD, JnJLatamHelpPageController.class);
		model.addAttribute("contactUsForm", new JnjGTContactUsForm());
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		JnjGTCoreUtil.logInfoMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, SHOW_CHANGE_ACCOUNT_LINK_VALUE +showChangeAccountLink, JnJLatamHelpPageController.class);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			JnjGTCoreUtil.logInfoMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, ENTERED_CONDITION_SHOW_CHANGE_ACCOUNT_LINK +showChangeAccountLink, JnJLatamHelpPageController.class);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return getLatamView(JnjlaloginaddonControllerConstants.Views.Pages.Help.ContactUsPopUpPage);
	}
	@Override
	@ResponseBody
	public void sendEmail(final JnjGTContactUsForm JnjGTContactUsForm, final BindingResult bindingResult, final Model model,
						  final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "sendEmail()";
		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.CONTACT_US, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnJLatamHelpPageController.class);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		populatePageData(model);
		try
		{
			final String helpLabel = jnjCommonFacadeUtil.getMessageFromImpex(JnjlaloginaddonWebConstants.Help.HELP_LABEL);
			storeCmsPageInModel(model, getContentPageForLabelOrId(JnjlaloginaddonWebConstants.Help.HELP_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(JnjlaloginaddonWebConstants.Help.HELP_CMS_PAGE));
			createBreadCrumbsForHelp(null, model, helpLabel);
			final JnjGTContactUsData jnjGTContactUsData = setContactUsData(JnjGTContactUsForm);
			jnjGTContactUsData.setSiteLogoURL(JnjWebUtil.getUnsecureServerUrl(request));

			JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.CONTACT_US, METHOD_NAME, "Sending email...", JnJLatamHelpPageController.class);
			jnjGTContactUsFacade.sendMail(jnjGTContactUsData);
			sessionService.setAttribute(JnjlaloginaddonWebConstants.Help.EMAIL_SENT_SUCCESSUFULLY, Boolean.TRUE);
		}
		catch (final BusinessException businessException)
		{
			sessionService.setAttribute(JnjlaloginaddonWebConstants.Help.EMAIL_SENT_SUCCESSUFULLY, Boolean.FALSE);
			JnjGTCoreUtil.logErrorMessage(JnjlaloginaddonWebConstants.Help.HELP, METHOD_NAME, businessException.getMessage(), businessException, JnJLatamHelpPageController.class);
		}
		JnjGTCoreUtil.logDebugMessage(JnjlaloginaddonWebConstants.Help.CONTACT_US, METHOD_NAME, Logging.END_OF_METHOD, JnJLatamHelpPageController.class);
	}

	public String getLatamView(final String view){
		return JnjlaloginaddonControllerConstants.ADDON_PREFIX + view;
	}

}