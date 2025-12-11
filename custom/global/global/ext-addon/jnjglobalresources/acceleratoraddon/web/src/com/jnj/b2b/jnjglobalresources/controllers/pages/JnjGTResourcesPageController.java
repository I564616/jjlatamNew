package com.jnj.b2b.jnjglobalresources.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants;
import com.jnj.b2b.jnjglobalreports.controllers.JnjglobalreportsControllerConstants;
import com.jnj.b2b.jnjglobalresources.constants.JnjglobalresourcesConstants;
import com.jnj.b2b.jnjglobalresources.controllers.JnjglobalresourcesControllerConstants;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.facades.converters.populator.customer.JnjGTCustomerReversePopulator;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.help.JnjGTContactUsFacade;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.utils.CommonUtil;
import com.jnj.facades.product.JnjGTProductFacade;


/**
 * @author himanshi.batra
 *
 */
//class for resource component

@Controller("JnjGTResourcesPageController")
@RequestMapping("/resources")
public class JnjGTResourcesPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(JnjGTResourcesPageController.class);

	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	@Autowired
	protected JnjGTProductFacade jnjGTProductFacade;

	@Autowired
	protected SessionService sessionService;
	@Autowired
	protected CMSSiteService cmsSiteService;

	@Resource(name="ContactUsFacade")
	protected JnjGTContactUsFacade jnjGTContactUsFacade;
	
	@Autowired
	@Qualifier(value = "customerReversePopulator")
	protected JnjGTCustomerReversePopulator customerReversePopulator;

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public JnjGTProductFacade getJnjGTProductFacade() {
		return jnjGTProductFacade;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public JnjGTCustomerReversePopulator getCustomerReversePopulator() {
		return customerReversePopulator;
	}

	private final String TRAINING_RESOURCE_LABEL = "resource.trainingresources.text";
	private final String RESOURCE_LABEL = "help.page.label";
	private final String USEFUL_LINKS_LABEL = "resource.usefullinks.text";
	protected final String POLICIES_FEES_LABEL = "resource.policiesandfees.text";
	private final String TERMS_SALE_LABEL = "resource.termsofsale.text";
	private final String PCM_PRIVACY_POLICY_LABEL = "text.register.privacy.policy";
	private final String PCM_TERMS_OF_USE_LABEL = "text.register.termsOfUse";
	protected static final String CPSIA_LABEL = "resource.cpsia.text";
	protected static final String SELECTED_LINK = "selectedLink";
	protected static final String CPSI_DATA = "cpsiData";
	protected static final String CPSIA_FLAG = "cpsiaFlag";
	protected static final String RESOURCES_CPSIA = "Resources - CPSIA";
	protected static final String SORT_BY = "sortBy";
	/*Start AAOL 5074*/
	protected final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";
	/*End AAOL 5074*/
	protected static final String SUBJECT_DROP_DOWN = "subjectDropDown";
	protected static final String CUSTOMER_NAME = "nameOfCustomer";

	protected static final String CUSTOMER_EMAIL = "emailOfCustomer";
	protected static final String IS_SERIAL_USER = "isSerialUser";
	/**
	 * Constant value for CPSIA CMS Page Id.
	 */
	protected static final String CPSIA_CMS_PAGE = "CPSIA";

	/**
	 * Constant value for CPSIA CMS Page Id.
	 */
	protected static final String CPSIA_CAERTIFICATE_PDF_VIEW = "jnjGTCpsiaCertificatePdfView";

	private static final String REDIRECT_PREFIX = "redirect:";


	@GetMapping("/usefullinks")
	public ModelAndView showUsefulLinks(final Model model) throws CMSItemNotFoundException
	{
		//Store the CMS page in the model
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
 
		final ContentPageModel pageModel = getContentPageForLabelOrId("UsefulLinks");

		final PageTemplateModel templateModel = pageModel.getMasterTemplate();

		storeCmsPageInModel(model, pageModel);
		model.addAttribute(SELECTED_LINK, JnjglobalresourcesConstants.Resources.USEFUL_LINK_COMPONENT_ID);
		model.addAttribute(SUBJECT_DROP_DOWN, jnjGTContactUsFacade.getSubjectDropDown());
		model.addAttribute(CUSTOMER_NAME, jnjGTCustomerFacade.getCurrentCustomer().getName());
		model.addAttribute(CUSTOMER_EMAIL, jnjGTCustomerFacade.getCurrentCustomer().getEmail());
		boolean isSerialUser = false;
		if((JnjGTUserTypes.SERIAL_USER.equals(sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE)))){
			isSerialUser = true;
		}
		model.addAttribute(IS_SERIAL_USER, isSerialUser);
		createBreadCrumbsForResources(model,"");
		return new ModelAndView(getView(JnjglobalresourcesControllerConstants.Views.Pages.Resources.ResourcesPage));
	}

	@GetMapping("/trainingresources")
	public ModelAndView showTrainingResources(final Model model) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//1.Store the CMS page in the model
		final ContentPageModel pageModel = getContentPageForLabelOrId("TrainingResource");

		final PageTemplateModel templateModel = pageModel.getMasterTemplate();

		storeCmsPageInModel(model, pageModel);

		model.addAttribute(SELECTED_LINK, JnjglobalresourcesConstants.Resources.TRAINING_RESOURCES_LINK_COMPONENT_ID);

		//3. Set the breadcrumb Key for the breadcrumb navigation
		createBreadCrumbsForResources(model, jnjCommonFacadeUtil.getMessageFromImpex(TRAINING_RESOURCE_LABEL));


		//2. Return the view
		
		//return new ModelAndView(templateModel.getFrontendTemplateName());
		return new ModelAndView(getView(JnjglobalresourcesControllerConstants.Views.Pages.Resources.ResourcesPage));
		
	}

	@GetMapping("/policiesandfees")
	public String showPoliciesAndFees(final Model model) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		/** Checking if not MDD, redirect to root node of Resources **/
		if (!(Jnjb2bCoreConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(Jnjb2bCoreConstants.SITE_NAME))))
		{
			return REDIRECT_PREFIX + "/resources/usefullinks";
		}
		
		final ContentPageModel pageModel = getContentPageForLabelOrId("PoliciesAndFees");

		storeCmsPageInModel(model, pageModel);

		//Dynamicaly rendering the data
		//final String comp_id = "PoliciesAndFeesComponent";

		final String currentSelectedAccountID = this.getcurrentSelectedAccountID();

		model.addAttribute("selectedCompanyComponent", "PoliciesAndFeesComponent_" + currentSelectedAccountID);

		//final String Id = getcomponentid();
		//System.out.println(Id);
		model.addAttribute(SELECTED_LINK, JnjglobalresourcesConstants.Resources.POLICY_FEES_LINK_COMPONENT_ID);

		createBreadCrumbsForResources(model, jnjCommonFacadeUtil.getMessageFromImpex(POLICIES_FEES_LABEL));
		// Return the view
		//return ("policiesandfees");
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.Resources.PolicyFees);

	}

	
	@GetMapping("/termsofsale")
	public String showTermsOfSale(final Model model) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		/** Checking if MDD, redirect to root node of Resources **/
		if ((Jnjb2bCoreConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(Jnjb2bCoreConstants.SITE_NAME))))
		{
			return REDIRECT_PREFIX + "/resources/usefullinks";
		}

		// Store the CMS page in the model
		final ContentPageModel pageModel = getContentPageForLabelOrId("TermsOfSale");

		storeCmsPageInModel(model, pageModel);
		model.addAttribute(SELECTED_LINK, JnjglobalresourcesConstants.Resources.TERMS_SALES_LINK_COMPONENT_ID);

		//Set the breadcrumb Key for the breadcrumb navigation

		createBreadCrumbsForResources(model, jnjCommonFacadeUtil.getMessageFromImpex(TERMS_SALE_LABEL));

		final String currentSelectedAccountID = this.getcurrentSelectedAccountID();

		model.addAttribute("selectedCompanyComponent", "termsofsalecomponent_" + currentSelectedAccountID);


		// Return the view
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.Resources.Termsofsale);

	}

	/**
	 *
	 * This method gets the CPSIA page
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/cpsia")
	public ModelAndView getCpsiaPage(@RequestParam(value = SORT_BY, required = false) final String sortBy, final Model model)
			throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "getCpsiaPage";
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		/** Checking if MDD, redirect to root node of Resources **/
		if ((Jnjb2bCoreConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(Jnjb2bCoreConstants.SITE_NAME))))
		{
			return new ModelAndView(REDIRECT_PREFIX + "/resources/usefullinks");
		}

		/** Setting flag for the CPSIA page **/
		model.addAttribute(CPSIA_FLAG, Boolean.TRUE);

		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Calling facade layer to fetch CPSIA data ...", LOG);
		/** Calling the facade layer to get the CPSIA Data and setting the same in the model **/
		model.addAttribute(CPSI_DATA, jnjGTProductFacade.getConsumerProductsCpsia(sortBy));
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "CPSIA Data fetched and added to the model", LOG);

		/** Store the CMS page in the model **/
		final ContentPageModel contentPageForLabelOrId = getContentPageForLabelOrId(CPSIA_CMS_PAGE);
		final PageTemplateModel templateModel = contentPageForLabelOrId.getMasterTemplate();
		storeCmsPageInModel(model, contentPageForLabelOrId);

		/** Setting the selected link for the side navigation **/
		model.addAttribute(SELECTED_LINK, JnjglobalresourcesConstants.Resources.CPSIA_LINK_COMPONENT_ID);

		/** Generating bread-crumbs **/
		createBreadCrumbsForResources(model, jnjCommonFacadeUtil.getMessageFromImpex(CPSIA_LABEL));

		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		/** Return the view **/
		return new ModelAndView(getView(JnjglobalresourcesControllerConstants.Views.Pages.Resources.ResourcesPage));
	}

	/**
	 *
	 * This method gets the sorted CPSIA results
	 *
	 * @param model
	 * @return view
	 */
	@PostMapping("/cpsia")
	public String getSortedCpsiaResults(@RequestParam(value = SORT_BY, required = false) final String sortBy, final Model model)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "getSortedCpsiaResults()";
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		model.addAttribute(SORT_BY, sortBy);
		/** Calling the facade layer to get the CPSIA Data and setting the same in the model **/
		model.addAttribute(CPSI_DATA, jnjGTProductFacade.getConsumerProductsCpsia(sortBy));
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		/** Returning view **/
		//return ControllerConstants.Views.Pages.CPSIA.CPSIAResultsContainerPage;
		return "";
	}

	/**
	 *
	 * This method generates the PDF for the CPSIA product ID
	 *
	 * @param productCode
	 * @param model
	 * @return PDF
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/cpsia/download")
	public String getCpsiaCertificate(@RequestParam(value = "productCode") final String productCode, final Model model)
			throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "getCpsiaPage";
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Calling facade layer to fetch CPSIA data ...", LOG);
		/** Calling the facade layer to get the CPSIA Data and setting the same in the model **/
		model.addAttribute(CPSI_DATA, jnjGTProductFacade.getCpsiaDataForProduct(productCode));
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "CPSIA Data fetched and added to the model", LOG);

		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		/** Return the view **/
		return CPSIA_CAERTIFICATE_PDF_VIEW;
	}


	@GetMapping("/pcmPrivacyPolicy")
	public ModelAndView showPcmPrivacyPolicy(final Model model) throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//Setting the value of the site when the site is accessed by a non-logged in user.
		//rama
	/*	if (sessionService.getAttribute(Jnjnab2bcoreConstants.SITE_NAME) == null)
		{
			*//** Check to see which CMSsite is in use, and setting the corresponding value in session **//*
			final String currentSite = cmsSiteService.getCurrentSite().getUid();
			final String siteName = currentSite.equalsIgnoreCase(JnjPCMCoreConstants.PCM_SITE_ID) ? JnjPCMCoreConstants.PCM
					: currentSite.equalsIgnoreCase(Jnjnab2bcoreConstants.MDD_SITE_ID) ? Jnjnab2bcoreConstants.MDD
							: Jnjnab2bcoreConstants.CONS;
			sessionService.setAttribute(Jnjnab2bcoreConstants.SITE_NAME, siteName);
		}*/


		//rama
		//model.addAttribute(SELECTED_LINK, JnjPCMCoreConstants.Resources.PCM_PRIVACY_POLICY_COMPONENT_ID);

		// Set the breadcrumb Key for the breadcrumb navigation
		createBreadCrumbsForResources(model, jnjCommonFacadeUtil.getMessageFromImpex(PCM_PRIVACY_POLICY_LABEL));

		// Return the view
		final CustomerModel user = sessionService.getAttribute("user");
		if (user instanceof JnJB2bCustomerModel)
		{
			//Store the CMS page in the model
			final ContentPageModel pageModel = getContentPageForLabelOrId("pcmPrivacyPolicyPage");

			final PageTemplateModel templateModel = pageModel.getMasterTemplate();

			storeCmsPageInModel(model, pageModel);

			return new ModelAndView(templateModel.getFrontendTemplateName());
		}
		else
		{
			//Store the CMS page in the model
			final ContentPageModel pageModel = getContentPageForLabelOrId("pcmAnonymousPrivacyPolicyPage");

			storeCmsPageInModel(model, pageModel);

			//return new ModelAndView(PcmControllerConstants.Views.Pages.Misc.EmailResourcePage);
			return new ModelAndView("");
		}

	}

	@GetMapping("/pcmTermsOfUse")
	public ModelAndView showPcmTermsOfUse(final Model model) throws CMSItemNotFoundException
	{
		//Setting the value of the site when the site is accessed by a non-logged in user.
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		//rama
		/*if (sessionService.getAttribute(Jnjnab2bcoreConstants.SITE_NAME) == null)
		{
			*//** Check to see which CMSsite is in use, and setting the corresponding value in session **//*
			final String currentSite = cmsSiteService.getCurrentSite().getUid();
			final String siteName = currentSite.equalsIgnoreCase(JnjPCMCoreConstants.PCM_SITE_ID) ? JnjPCMCoreConstants.PCM
					: currentSite.equalsIgnoreCase(Jnjnab2bcoreConstants.MDD_SITE_ID) ? Jnjnab2bcoreConstants.MDD
							: Jnjnab2bcoreConstants.CONS;
			sessionService.setAttribute(Jnjnab2bcoreConstants.SITE_NAME, siteName);
		}*/

		//rama
		//model.addAttribute(SELECTED_LINK, JnjglobalresourcesConstants.Resources.PCM_TERMS_OF_USE_LINK_COMPONENT_ID);

		// Set the breadcrumb Key for the breadcrumb navigation
		createBreadCrumbsForResources(model, jnjCommonFacadeUtil.getMessageFromImpex(PCM_TERMS_OF_USE_LABEL));

		// Return the view
		final CustomerModel user = sessionService.getAttribute("user");
		if (user instanceof JnJB2bCustomerModel)
		{
			//Store the CMS page in the model
			final ContentPageModel pageModel = getContentPageForLabelOrId("pcmTermsOfUsePage");

			final PageTemplateModel templateModel = pageModel.getMasterTemplate();

			storeCmsPageInModel(model, pageModel);

			return new ModelAndView(templateModel.getFrontendTemplateName());
		}
		else
		{

			//Store the CMS page in the model
			final ContentPageModel pageModel = getContentPageForLabelOrId("pcmAnonymousTermsOfUsePage");

			storeCmsPageInModel(model, pageModel);

			//return new ModelAndView(PcmControllerConstants.Views.Pages.Misc.EmailResourcePage);
			return new ModelAndView("");
		}
	}

	protected String getcurrentSelectedAccountID() throws CMSItemNotFoundException
	{
		return jnjGTCustomerFacade.getCurrentSelectedAccountClassOfTrade();
	}

	protected void createBreadCrumbsForResources(final Model model, final String formSpecificKey)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		final String usefullLinks = jnjCommonFacadeUtil.getMessageFromImpex(RESOURCE_LABEL);
		//final String homeURL = "/home";
		final String usefulLinksURL = "/resources/usefullinks";
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		//breadcrumbs.add(new Breadcrumb(homeURL, home, null));
		breadcrumbs.add(new Breadcrumb(usefulLinksURL, usefullLinks, null));
		//breadcrumbs.add(new Breadcrumb(null, formSpecificKey, null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
	}

	public String getView(final String view){
        return JnjglobalresourcesControllerConstants.ADDON_PREFIX + view;
 }
}
