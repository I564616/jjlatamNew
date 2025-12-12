package com.jnj.la.jnjlaservicepageaddon.controllers.pages;

import com.jnj.core.dto.*;
import com.jnj.facades.customer.impl.JnjLatamCustomerFacadeImpl;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.core.annotations.AuthorizedUserGroup;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.facades.customer.JnjCustomerFacade;
import com.jnj.facades.data.JnjLaudoData;
import com.jnj.facades.data.JnjLaudoFileStatusData;
import com.jnj.facades.laudo.JnjLaudoFacade;
import com.jnj.facades.services.JnjServicesFacade;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Laudo;
import com.jnj.la.core.services.customer.impl.JnjLatamCustomerServiceImpl;
import com.jnj.utils.CommonUtil;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import com.jnj.la.jnjlaservicepageaddon.constants.JnjlaservicepageaddonWebConstants;
import com.jnj.la.jnjlaservicepageaddon.controllers.JnjlaservicepageaddonControllerConstants;
import com.jnj.la.jnjlaservicepageaddon.forms.JnjAddIndirectCustomerForm;
import com.jnj.la.jnjlaservicepageaddon.forms.JnjAddIndirectPayerForm;
import com.jnj.la.jnjlaservicepageaddon.forms.JnjConsignmentIssueForm;
import com.jnj.la.jnjlaservicepageaddon.forms.JnjDeleteLaudoForm;
import com.jnj.la.jnjlaservicepageaddon.forms.JnjLaudoFileUploadForm;
import com.jnj.la.jnjlaservicepageaddon.forms.JnjLaudoForm;
import com.jnj.facades.account.JnJCrossReferenceFacade;
import com.jnj.la.jnjlaservicepageaddon.forms.CrossReferenceForm;

import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.media.MediaService;

/**
 * This class handles the requests related to services and forms pages.
 *
 * @author asing187
 */

@Controller
@Scope("tenant")
@RequireHardLogIn
@RequestMapping("/services")
public class JnjServicesPageController extends AbstractSearchPageController
{

    private static final String CONSIGNMENT_GROUP = "consingmentGroup";
    private static final String ADD_INDIRECT_PAYER_GROUP = "addIndirectPayer";
    private static final String INDIRECT_CUSTOMER_FORM_GROUP = "indirectCustomerFormGroup";
    private static final String SHOW_CHANGE_ACCOUNT_LINK_VALUE = "showChangeAccountLink value : ";
    private static final String ENTERED_CONDITION_SHOW_CHANGE_ACCOUNT_LINK = "entered condition...........showChangeAccountLink :";
    private static final String ADD_INDIRECT_CUSTOMER_FORM_CMS_PAGE = "addIndirectCustomerFormPage";
    private static final String ADD_INDIRECT_PAYER_FORM_CMS_PAGE = "addIndirectPayerFormPage";
    private static final String DOWNLOAD_LAUDO_PAGE_ID = "jnjDownloadLaudoPage";
    private static final String MANAGE_LAUDO_PAGE_ID = "jnjManageLaudoPage";
	private static final String CONSIGNMENT_ISSUE_CMS_PAGE = "consignmentIssue";
    private static final String SSRF_VALIDATION_FLAG = "SSRFFailed";

    /** The error occured. */
    private boolean errorOccured = false;

    /** The resource breadcrumb builder. */
    @Resource(name = "simpleBreadcrumbBuilder")
    private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

    /** The message facade. */
    @Resource(name = "messageFacade")
    protected MessageFacadeUtill messageFacade;

    @Autowired
    protected JnjCustomerFacade jnjGTCustomerFacade;

    @Autowired
    protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

    @Autowired
    protected UserService userService;

    @Autowired
    protected SessionService sessionService;

    @Autowired
    protected JnjConfigService jnjConfigService;

    @Autowired
    protected JnjServicesFacade jnjServicesFacade;

    @Autowired
    protected JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

    @Autowired
    private JnjLaudoFacade jnjLaudoFacade;

    @Autowired
    protected CompanyB2BCommerceService companyB2BCommerceService;
    
    @Autowired
    protected JnJCrossReferenceFacade jnjLaCrossReferenceFacade;
    
    @Autowired
    protected MediaService mediaService;
    
    @Autowired
    protected CMSSiteService cmsSiteService;
	
	@Autowired
    protected CatalogVersionService catalogVersionService;
	
	@Autowired
	protected JnjLatamCustomerServiceImpl jnjLatamCustomerService;

    @Autowired
    private JnjLatamCustomerFacadeImpl jnjLatamCustomerFacadeImpl;

    /** The error occurred. */
    private static final boolean ERROR_OCCURRED = false;

    protected static final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";

    private static final String LAUDO_FILE_NAME_PATTERN = "/{laudoFileName:.*}";

    private static final Class currentClass = JnjServicesPageController.class;
	
    private enum DOWNLOAD_TYPE
    {
        PDF, EXCEL;
    }

    public static final String RESULT_PDF = "crossReferencePdfView";
    public static final String RESULT_EXCEL = "crossReferenceExcelView";
    public static final String CROSS_REF = "crossReferenceTable";
    

    private String pageSize = "0";
    private int updatedPageSize = 0;
    private String searchTerm = null;
    private SearchPageData<JnjCrossReferenceTableDTO> list = null;

    /**
     * This method handles the request to render the Services Page.
     *
     * @param downloadFlag
     *            the download flag
     * @param model
     *            the model
     * @return pageName
     * @throws CMSItemNotFoundException
     *             the cMS item not found exception
     */
    @GetMapping
    public String getServices(@RequestParam(value = "download", required = false) final String downloadFlag,
            @RequestParam(value = "downloadError", required = false) final String downloadErrorFlag, final Model model,
            final HttpServletRequest request) throws CMSItemNotFoundException
    {
        final String METHOD_NAME = "getServices()";

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        setupModel(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, model, ERROR_OCCURRED, request);
        final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
        breadcrumbs.add(new Breadcrumb(Jnjb2bCoreConstants.PostRedirectionURI.SERVICES_PREFIX,
                jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Forms.BREADCRUMB_KEY_SERVICES), null));
        model.addAttribute(Jnjlab2bcoreConstants.Forms.BREADCRUMBS, breadcrumbs);
        model.addAttribute("downloadFlag", downloadFlag);
        model.addAttribute("downloadErrorFlag", downloadErrorFlag);
        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Misc.JnjServicesPage;
    }

    /**
     * This method is used to fetch the CMS page and store it in the model.
     *
     * @param pageName
     *            the page name
     * @param model
     *            the model
     * @param errorOccured
     *            the error occured
     * @throws CMSItemNotFoundException
     *             the cMS item not found exception
     */
    protected void setupModel(final String pageName, final Model model, final boolean errorOccured,
            final HttpServletRequest request) throws CMSItemNotFoundException
    {
        final String METHOD_NAME = "setupModel()";
        final ContentPageModel pageForRequest = getContentPageForLabelOrId(pageName);
        Boolean error = errorOccured;
        storeCmsPageInModel(model, pageForRequest);
        setUpMetaDataForContentPage(model, pageForRequest);
        try
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.NO_BLANK_ERROR,
                    messageFacade.getMessageTextForCode(Jnjlab2bcoreConstants.Forms.NO_BLANK_ERROR_MESSAGE_KEY));
        }
        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(JnjlaservicepageaddonWebConstants.Services.SERVICES, METHOD_NAME,
                    businessException.getMessage(), businessException, currentClass);
            error = true;
        }
        model.addAttribute("ERROR_OCCURED", error);
        getServiceLandingPageVisibleItems(model, request);
    }

    /**
     * This method gets the visible components from the
     * 'ServicesSideContentSlot' and stores a flag for each of them in the
     * Model.
     *
     * @param model
     * @param request
     */
    private void getServiceLandingPageVisibleItems(final Model model, final HttpServletRequest request)
    {
        final List<String> listOfLinks = jnjGTCustomerFacade
                .getVisibleUrls(Jnjlab2bcoreConstants.ServicesForms.SIDE_SLOT_COMPONENT_ID, request);

        for (final String leftNavLinnk : listOfLinks)
        {
            getServiceLandingPageVisible(model, leftNavLinnk);
        }
    }

    protected void getServiceLandingPageVisible(final Model model, final String link){
        final String methodName = "getServiceLandingPageVisible()";
        final List<String> assignedGroups = getServicesPageUserGroupsForCurrentUSer();

        populateSiteRegions(model);

        if (StringUtils.equalsIgnoreCase(link,
                Jnjlab2bcoreConstants.ServicesForms.CROSS_REFERENCE_TABLE_LINK_COMPONENT_ID) && assignedGroups != null
                && assignedGroups.contains("crossReferenceTable")){
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_CROSS_REFERENCE_TABLE, Boolean.TRUE);
        } else if (StringUtils.equalsIgnoreCase(link, Jnjlab2bcoreConstants.ServicesForms.CONSIGNMENT_LINK_COMPONENT_ID)
                && assignedGroups != null && assignedGroups.contains(CONSIGNMENT_GROUP)){
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_CONSIGNMENT, Boolean.TRUE);
        } else if (StringUtils.equalsIgnoreCase(link,
                Jnjlab2bcoreConstants.ServicesForms.INTEGRAL_SERVICES_LINK_COMPONENT_ID) && assignedGroups != null
                && assignedGroups.contains("integralServicesGroup")){
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_INTEGRAL_SERVICES, Boolean.TRUE);
        } else{
            getSubServiceLandingPageVisible(model, link);
        }
    }

    private void populateSiteRegions(Model model){
        final String methodName = "populateSiteRegions()";
        try{
            final String isoCode = jnjLatamCustomerService.getCountryOfUser().getIsocode();
            final List<RegionData> regionDataList = jnjLatamCustomerFacadeImpl.getRegions(isoCode);

            if (CollectionUtils.isNotEmpty(regionDataList)){
                model.addAttribute(Jnjlab2bcoreConstants.Forms.CURRENT_SITE_COUNTRY_REGION, regionDataList);
            }
        } catch (BusinessException e) {
            JnjGTCoreUtil.logErrorMessage("Populate country regions", methodName,
                    "Error getting country from user", e, currentClass);
        }
    }

    protected void getSubServiceLandingPageVisible(final Model model, final String link)
    {
        final List<String> assignedGroups = getServicesPageUserGroupsForCurrentUSer();
        if (StringUtils.equalsIgnoreCase(link, Jnjlab2bcoreConstants.ServicesForms.SERVICE_FORM_LINK_COMPONENT_ID)
                && assignedGroups != null && assignedGroups.contains("ocdGroup"))
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_SERVICE_FORM, Boolean.TRUE);
        }

        else if (StringUtils.equalsIgnoreCase(link, Jnjlab2bcoreConstants.ServicesForms.SYNTHESIS_LINK_COMPONENT_ID)
                && assignedGroups != null && assignedGroups.contains("synthesGroup"))
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_SYNTHESIS, Boolean.TRUE);
        }

        else if (StringUtils.equalsIgnoreCase(link,
                Jnjlab2bcoreConstants.ServicesForms.DOWNLOAD_PHARMA_PRICE_LIST_LINK_COMPONENT_ID)
                && assignedGroups != null && assignedGroups.contains("downloadPharmaPriceList"))
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_DOWNLOAD_PHARMA_PRICE_LIST, Boolean.TRUE);
        }

        else
        {
            getLaudoServiceLandingPageVisible(model, link);
        }
    }

    protected void getLaudoServiceLandingPageVisible(final Model model, final String link)
    {
        final List<String> assignedGroups = getServicesPageUserGroupsForCurrentUSer();
        if (StringUtils.equalsIgnoreCase(link, Jnjlab2bcoreConstants.ServicesForms.LAUDO_DOWNLOAD_LINK_COMPONENT_ID)
                && assignedGroups != null && assignedGroups.contains("laudoUserGroup"))
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_DOWNLOAD_LAUDO, Boolean.TRUE);
        }

        else if (StringUtils.equalsIgnoreCase(link, Jnjlab2bcoreConstants.ServicesForms.LAUDO_MANAGE_LINK_COMPONENT_ID)
                && assignedGroups != null && assignedGroups.contains("manageLaudo"))
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_MANAGE_LAUDO, Boolean.TRUE);
        }

        else
        {
            getIndirectServiceLandingPageVisible(model, link);
        }
    }

    protected void getIndirectServiceLandingPageVisible(final Model model, final String link)
    {
        final List<String> assignedGroups = getServicesPageUserGroupsForCurrentUSer();
        if (StringUtils.equalsIgnoreCase(link, Jnjlab2bcoreConstants.ServicesForms.ADD_INDIRECT_CUST_LINK_COMPONENT_ID)
                && assignedGroups != null && assignedGroups.contains(INDIRECT_CUSTOMER_FORM_GROUP))
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_ADD_INDIRECT, Boolean.TRUE);
        }

        else if (StringUtils.equalsIgnoreCase(link,
                Jnjlab2bcoreConstants.ServicesForms.ADD_INDIRECT_PAYER_LINK_COMPONENT_ID) && assignedGroups != null
                && assignedGroups.contains(ADD_INDIRECT_PAYER_GROUP))
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SHOW_ADD_INDIRECT_PAYER, Boolean.TRUE);
        }
    }

    public List<String> getServicesPageUserGroupsForCurrentUSer()
    {
        final UserModel currentUser = userService.getCurrentUser();
        final List<String> assignedUserGroups = new ArrayList<>();
        final Set<PrincipalGroupModel> listOfAssignedGroups = currentUser.getAllGroups();
        for (final PrincipalGroupModel pgm : listOfAssignedGroups)
        {
            assignedUserGroups.add(pgm.getUid());
        }
        return assignedUserGroups;
    }

    @AuthorizedUserGroup(value = INDIRECT_CUSTOMER_FORM_GROUP)
    @GetMapping(Jnjlab2bcoreConstants.PostRedirectionURI.SERVICES_INDIRECT_CUSTOMER)
    public String getAddIndirectCustomerForm(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel)
            throws CMSItemNotFoundException, BusinessException
    {
        final String METHOD_NAME = "getAddIndirectCustomerForm()";
        final String PAGE_TITLE_KEY = "cart.review.addIndirectCustomer";

        if (sessionService.getAttribute(SSRF_VALIDATION_FLAG) != null) {
        	model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.FALSE);
			sessionService.removeAttribute(SSRF_VALIDATION_FLAG);
		}

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, METHOD_NAME,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);

        setupModel(ADD_INDIRECT_CUSTOMER_FORM_CMS_PAGE, model, errorOccured, request);

        final String indirectCustomerTypesCount = jnjConfigService.getConfigValueById("indirectCustomerTypesCount");
        if (null != indirectCustomerTypesCount)
        {
            model.addAttribute("customerTypeCount", Integer.parseInt(indirectCustomerTypesCount));
        }

        model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(PAGE_TITLE_KEY));

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, METHOD_NAME,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Misc.JnjAddIndirectCustomerFormPage;
    }

    @AuthorizedUserGroup(value = INDIRECT_CUSTOMER_FORM_GROUP)
    @PostMapping(Jnjlab2bcoreConstants.PostRedirectionURI.SERVICES_INDIRECT_CUSTOMER)
    public String sendAddIndirectCustomerForm(final Model model,
            @ModelAttribute final JnjAddIndirectCustomerForm jnjAddIndirectCustomerForm,
            final HttpServletRequest request) throws CMSItemNotFoundException
    {
        final String METHOD_NAME = "sendAddIndirectCustomerForm()";
        final String PAGE_TITLE_KEY = "cart.review.addIndirectCustomer";

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, METHOD_NAME,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.FORMS_NAME, METHOD_NAME,
                "Name :: " + jnjAddIndirectCustomerForm.getCustomerName() + " ::  Address :: "
                        + jnjAddIndirectCustomerForm.getAddress() + " :: getCnpjOrCpf :: "
                        + jnjAddIndirectCustomerForm.getGovernmentIdType() + " :: getCnpjOrCpfText :: "
                        + jnjAddIndirectCustomerForm.getGovernmentId() + " :: getCpfPatientOrPhysicianName :: "
                        + jnjAddIndirectCustomerForm.getCpfPatientOrPhysicianName() + " :: getCustomerType :: "
                        + jnjAddIndirectCustomerForm.getCustomerType() + " :: getNeighborhood :: "
                        + jnjAddIndirectCustomerForm.getNeighborhood() + " :: getPublicOrPrivate :: "
                        + jnjAddIndirectCustomerForm.getPublicOrPrivate() + " :: getState :: "
                        + jnjAddIndirectCustomerForm.getState() + " :: getZipCode :: "
                        + jnjAddIndirectCustomerForm.getZipCode(), currentClass);

        final JnjLatamAddIndirectCustomerDTO jnjAddIndirectCustomerDTO = new JnjLatamAddIndirectCustomerDTO();
        jnjAddIndirectCustomerDTO.setFormName(Jnjlab2bcoreConstants.Forms.ADD_INDIRECT_CUSTOMER_CMS_PAGE);

        jnjAddIndirectCustomerDTO.setCustomerName(jnjAddIndirectCustomerForm.getCustomerName());
        jnjAddIndirectCustomerDTO.setCnpjOrCpf(jnjAddIndirectCustomerForm.getGovernmentIdType());
        jnjAddIndirectCustomerDTO.setCnpjOrCpfText(jnjAddIndirectCustomerForm.getGovernmentId());
        jnjAddIndirectCustomerDTO
                .setCpfPatientOrPhysicianName(jnjAddIndirectCustomerForm.getCpfPatientOrPhysicianName());
        jnjAddIndirectCustomerDTO.setCustomerType(jnjAddIndirectCustomerForm.getCustomerType());
        jnjAddIndirectCustomerDTO.setPublicOrPrivate(jnjAddIndirectCustomerForm.getPublicOrPrivate());
        jnjAddIndirectCustomerDTO.setState(jnjAddIndirectCustomerForm.getState());
        jnjAddIndirectCustomerDTO.setCity(jnjAddIndirectCustomerForm.getCity());
        jnjAddIndirectCustomerDTO.setAddress(jnjAddIndirectCustomerForm.getAddress());
        jnjAddIndirectCustomerDTO.setNeighborhood(jnjAddIndirectCustomerForm.getNeighborhood());
        jnjAddIndirectCustomerDTO.setZipCode(jnjAddIndirectCustomerForm.getZipCode());
        jnjAddIndirectCustomerDTO.setBid(jnjAddIndirectCustomerForm.getBid());
        jnjAddIndirectCustomerDTO.setCompany(jnjAddIndirectCustomerForm.getCompany());
        jnjAddIndirectCustomerDTO.setServerURL(Config.getParameter(Jnjb2bCoreConstants.UserCreation.HOST_NAME));
        jnjAddIndirectCustomerDTO.setFromEmail(getUser().getEmail());
        jnjAddIndirectCustomerDTO.setFromDisplayName(getUser().getName());

        setupModel(ADD_INDIRECT_CUSTOMER_FORM_CMS_PAGE, model, errorOccured, request);

        final String indirectCustomerTypesCount = jnjConfigService.getConfigValueById("indirectCustomerTypesCount");
        if (null != indirectCustomerTypesCount)
        {
            model.addAttribute("customerTypeCount", Integer.parseInt(indirectCustomerTypesCount));
        }

        try
        {
            jnjServicesFacade.sendEmailToUser(jnjAddIndirectCustomerDTO);
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.TRUE);
            model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(PAGE_TITLE_KEY));
        }
        catch (final Exception exception)
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.FALSE);
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, METHOD_NAME,
                    exception.getMessage(), exception, currentClass);
        }

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, METHOD_NAME,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Misc.JnjAddIndirectCustomerFormPage;
    }

    @AuthorizedUserGroup(value = ADD_INDIRECT_PAYER_GROUP)
    @GetMapping(Jnjlab2bcoreConstants.PostRedirectionURI.SERVICES_INDIRECT_PAYER)
    public String getAddIndirectPayerForm(final Model model, final HttpServletRequest request)
            throws CMSItemNotFoundException, BusinessException
    {

        final String methodName = "getAddIndirectPayerForm()";
        final String pageTitleKey = "cart.review.addIndirectPayer";

        if (sessionService.getAttribute(SSRF_VALIDATION_FLAG) != null) {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.FALSE);
            sessionService.removeAttribute(SSRF_VALIDATION_FLAG);
        }

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);

        setupModel(ADD_INDIRECT_PAYER_FORM_CMS_PAGE, model, errorOccured, request);

        final String indirectPayerTypesCount = jnjConfigService.getConfigValueById("indirectPayerTypesCount");
        if (null != indirectPayerTypesCount)
        {
            model.addAttribute("payerTypeCount", Integer.parseInt(indirectPayerTypesCount));
        }

        model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Misc.JnjAddIndirectPayerFormPage;
    }

    @AuthorizedUserGroup(value = ADD_INDIRECT_PAYER_GROUP)
    @PostMapping(Jnjlab2bcoreConstants.PostRedirectionURI.SERVICES_INDIRECT_PAYER)
    public String sendAddIndirectPayerForm(final Model model,
            @ModelAttribute final JnjAddIndirectPayerForm jnjAddIndirectPayerForm, final HttpServletRequest request)
            throws CMSItemNotFoundException
    {
        final String methodName = "sendAddIndirectPayerForm()";
        final String pageTitleKey = "cart.review.addIndirectPayer";

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.FORMS_NAME, methodName, "Name :: "
                + jnjAddIndirectPayerForm.getPayerName() + " ::  Address :: " + jnjAddIndirectPayerForm.getAddress()
                + " :: getCnpjOrCpf :: " + jnjAddIndirectPayerForm.getGovernmentIdType() + " :: getCnpjOrCpfText :: "
                + jnjAddIndirectPayerForm.getGovernmentId() + " :: getCpfPatientOrPhysicianName :: "
                + jnjAddIndirectPayerForm.getCpfPatientOrPhysicianName() + " :: getPayerType :: "
                + jnjAddIndirectPayerForm.getPayerType() + " :: getNeighborhood :: "
                + jnjAddIndirectPayerForm.getNeighborhood() + " :: getPublicOrPrivate :: "
                + jnjAddIndirectPayerForm.getPublicOrPrivate() + " :: getState :: " + jnjAddIndirectPayerForm.getState()
                + " :: getZipCode :: " + jnjAddIndirectPayerForm.getZipCode(), currentClass);

        final JnjLatamAddIndirectPayerDTO jnjAddIndirectPayerDTO = new JnjLatamAddIndirectPayerDTO();
        jnjAddIndirectPayerDTO.setFormName(Jnjlab2bcoreConstants.Forms.ADD_INDIRECT_PAYER_CMS_PAGE);

        jnjAddIndirectPayerDTO.setPayerName(jnjAddIndirectPayerForm.getPayerName());
        jnjAddIndirectPayerDTO.setCnpjOrCpf(jnjAddIndirectPayerForm.getGovernmentIdType());
        jnjAddIndirectPayerDTO.setCnpjOrCpfText(jnjAddIndirectPayerForm.getGovernmentId());
        jnjAddIndirectPayerDTO.setCpfPatientOrPhysicianName(jnjAddIndirectPayerForm.getCpfPatientOrPhysicianName());
        jnjAddIndirectPayerDTO.setPayerType(jnjAddIndirectPayerForm.getPayerType());
        jnjAddIndirectPayerDTO.setPublicOrPrivate(jnjAddIndirectPayerForm.getPublicOrPrivate());
        jnjAddIndirectPayerDTO.setState(jnjAddIndirectPayerForm.getState());
        jnjAddIndirectPayerDTO.setCity(jnjAddIndirectPayerForm.getCity());
        jnjAddIndirectPayerDTO.setAddress(jnjAddIndirectPayerForm.getAddress());
        jnjAddIndirectPayerDTO.setNeighborhood(jnjAddIndirectPayerForm.getNeighborhood());
        jnjAddIndirectPayerDTO.setZipCode(jnjAddIndirectPayerForm.getZipCode());
        jnjAddIndirectPayerDTO.setBid(jnjAddIndirectPayerForm.getBid());
        jnjAddIndirectPayerDTO.setCompany(jnjAddIndirectPayerForm.getCompany());
        jnjAddIndirectPayerDTO.setServerURL(Config.getParameter(Jnjb2bCoreConstants.UserCreation.HOST_NAME));
        jnjAddIndirectPayerDTO.setFromEmail(getUser().getEmail());
        jnjAddIndirectPayerDTO.setFromDisplayName(getUser().getName());

        setupModel(ADD_INDIRECT_PAYER_FORM_CMS_PAGE, model, errorOccured, request);

        final String indirectPayerTypesCount = jnjConfigService.getConfigValueById("indirectPayerTypesCount");
        if (null != indirectPayerTypesCount)
        {
            model.addAttribute("payerTypeCount", Integer.parseInt(indirectPayerTypesCount));
        }

        try
        {
            jnjServicesFacade.sendEmailToUserForIndirectPayer(jnjAddIndirectPayerDTO);
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.TRUE);
            model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
        }
        catch (final Exception exception)
        {
            model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.FALSE);
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
                    exception.getMessage(), exception, currentClass);
        }

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Misc.JnjAddIndirectPayerFormPage;
    }

    private List<Breadcrumb> getBreadCrumbsForPage(final String pageMessageKey) throws BusinessException
    {
        final List<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new Breadcrumb(Jnjb2bCoreConstants.PostRedirectionURI.SERVICES_PREFIX,
                jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.Forms.BREADCRUMB_KEY_SERVICES), null));
        breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex(pageMessageKey), null));
        return breadcrumbs;
    }

    @GetMapping("/laudo/download")
    private String showDownloadLaudo(@RequestParam(value = "search", required = false) final boolean search,
            final JnjLaudoForm jnjLaudoForm, final Model model, final HttpServletRequest request)
            throws CMSItemNotFoundException
    {
        final String METHOD_SHOW_DOWNLOAD_LAUDO = "showDownloadLaudo";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SHOW_DOWNLOAD_LAUDO,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        try
        {
            final String pageTitleKey = "services.laudo.downloadlaudo";
            model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
        }
        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SHOW_DOWNLOAD_LAUDO,
                    "BusinessException occured while Getting Breadcrumbs. ", businessException,
                    currentClass);
        }
		boolean isBatchRequired = isBatchMandatory();
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
        boolean errorOccured = false;
        String mandatoryErrorMessage = null;
        if (null != Boolean.valueOf(search) && search)
        {
            populateFormDefaultValues(jnjLaudoForm);
            final JnjPageableData jnjPageableData = new JnjPageableData();
            populateJnjPageableData(jnjLaudoForm, jnjPageableData);
            SearchPageData<JnjLaudoData> laudoData = null;
            try
            {
                laudoData = jnjLaudoFacade.getLaudoDetailsWithMedia(jnjPageableData);

            }

            catch (final BusinessException businessException)
            {
                JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SHOW_DOWNLOAD_LAUDO,
                        "BusinessException occured while Getting Laudo Details. ", businessException,
                        currentClass);

                errorOccured = true;
            }
            model.addAttribute("laudoData", laudoData);
            model.addAttribute("search", Boolean.valueOf(search));
        }
		model.addAttribute("isBatchRequired", isBatchRequired);
        model.addAttribute("jnjLaudoForm", jnjLaudoForm);
        setupModel(DOWNLOAD_LAUDO_PAGE_ID, model, errorOccured, request);
        try
        {
            mandatoryErrorMessage = messageFacade.getMessageTextForCode(Laudo.LAUDO_MANDATORY_ERROR_KEY);
            final String countryIsoCode = jnjLatamCustomerService.getCountry();
            model.addAttribute("countryIsoCode", countryIsoCode);
        }

        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SHOW_DOWNLOAD_LAUDO,
                    "BusinessException occured while getting Mandatory Error Message. ", businessException,
                    currentClass);

            errorOccured = true;
        }
        model.addAttribute("mandatoryErrorMessage", mandatoryErrorMessage);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SHOW_DOWNLOAD_LAUDO,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Laudo.DownloadLaudo;

    }

    public boolean isBatchMandatory(){
    	boolean isLotNumberRequired = false;
    	final String batchCountries = jnjConfigService.getConfigValueById(Jnjlab2bcoreConstants.BATCH_COUNTRIES);
        final CountryModel countryModel =cmsSiteService.getCurrentSite().getDefaultCountry();
        final String countryCode = countryModel != null ? countryModel.getIsocode() : Jnjlab2bcoreConstants.Invoice.SearchOption.NONE;
        if(StringUtils.isNotEmpty(batchCountries) && batchCountries.contains(countryCode)){
			isLotNumberRequired = true;
		}
		return isLotNumberRequired;
    } 
	 
    @GetMapping("/laudo/download" + LAUDO_FILE_NAME_PATTERN)
    public void downloadLaudoFile(@PathVariable("laudoFileName") final String laudoFileName,
            final HttpServletResponse httpServletResponse)
    {
        final String METHOD_DOWNLOAD_LAUDO_FILE = "downloadLaudoFile()";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DOWNLOAD_LAUDO_FILE,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        errorOccured = false;

        try
        {

            final Map<File, String> laudoFileMap = jnjLaudoFacade.getLaudoFile(laudoFileName);
            final File laudoFile = (null != laudoFileMap && !laudoFileMap.keySet().isEmpty()
                    && laudoFileMap.keySet().iterator().hasNext()) ? laudoFileMap.keySet().iterator().next() : null;
            final String fileContentType = (null != laudoFileMap && !laudoFileMap.keySet().isEmpty()
                    && laudoFileMap.keySet().iterator().hasNext()) ? laudoFileMap.get(laudoFile) : null;

            if (null != laudoFile && laudoFile.exists())
            {

                JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DOWNLOAD_LAUDO_FILE,
                        "Laudo File with Filename: " + laudoFileName + " found.", currentClass);

                final FileInputStream fileInputStream = new FileInputStream(laudoFile);
                httpServletResponse.setContentType(fileContentType);
                httpServletResponse.setContentLength(Long.valueOf(laudoFile.length()).intValue());
                httpServletResponse.setHeader(WebConstants.InvoiceDetails.HEADER_PARAM,
                        WebConstants.InvoiceDetails.HEADER_PARAM_VALUE + laudoFileName);
                FileCopyUtils.copy(fileInputStream, httpServletResponse.getOutputStream());
                fileInputStream.close();
                httpServletResponse.getOutputStream().close();
                httpServletResponse.getOutputStream().flush();
            }
            else
            {
                JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DOWNLOAD_LAUDO_FILE,
                        "Laudo File with Filename: " + laudoFileName + "  not found..",
                        currentClass);
                errorOccured = true;
            }
        }
        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DOWNLOAD_LAUDO_FILE,
                    "BusinessException occured while Downloading Laudo File with FileName: " + laudoFileName,
                    businessException, currentClass);
            errorOccured = true;
        }
        catch (final FileNotFoundException fileNotFoundException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DOWNLOAD_LAUDO_FILE,
                    "FileNotFoundException occured while Downloading Laudo File with FileName: " + laudoFileName,
                    fileNotFoundException, currentClass);
            errorOccured = true;
        }
        catch (final IOException iOException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DOWNLOAD_LAUDO_FILE,
                    "IOException occured while Downloading Laudo File with FileName: " + laudoFileName, iOException,
                    currentClass);

            errorOccured = true;
        }
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DOWNLOAD_LAUDO_FILE,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
    }

    @GetMapping("/laudo/manage")
    private String showManageLaudo(final JnjLaudoForm jnjLaudoForm, final Model model, final HttpServletRequest request)
            throws CMSItemNotFoundException

    {
        final String METHOD_SHOW_MANAGE_LAUDO = "showManageLaudo()";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SHOW_MANAGE_LAUDO,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
        try
        {
            final String pageTitleKey = "services.laudo.manageLaudo";
            model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
        }
        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SHOW_MANAGE_LAUDO,
                    "BusinessException occured while Getting Breadcrumbs. ", businessException,
                    currentClass);
        }

        populateManageLaudoPageData(jnjLaudoForm, model, request);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SHOW_MANAGE_LAUDO,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Laudo.ManageLaudo;
    }

    @PostMapping("/laudo/delete")
    private String deleteLaudoEntry(final JnjLaudoForm jnjLaudoForm, final JnjDeleteLaudoForm jnjDeleteLaudoForm,
            final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
    {
        final String METHOD_DELETE_LAUDO_ENTRY = "deleteLaudoEntry()";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_LAUDO_ENTRY,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);

        try
        {
            final String pageTitleKey = "services.laudo.manageLaudo";
            model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
        }
        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_LAUDO_ENTRY,
                    "BusinessException occured while Getting Breadcrumbs. ", businessException,
                    currentClass);
        }
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
        final List<String> fileNameList = Arrays.asList(jnjDeleteLaudoForm.getDeleteFileNames().split("\\s*,\\s*"));
        List<JnjLaudoFileStatusData> jnjLaudoFileStatusDataList = new ArrayList<JnjLaudoFileStatusData>();
        try
        {
            jnjLaudoFileStatusDataList = jnjLaudoFacade.deleteLaudos(fileNameList);
        }
        catch (final BusinessException exception)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_LAUDO_ENTRY,
                    "BusinessException Occured while fetching localised value of the message with code ["
                            + Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE + "]. ",
                    exception, currentClass);
        }

        model.addAttribute("jnjLaudoFileDeleteStatusList", jnjLaudoFileStatusDataList);
        final Map<String, String> deleteStatusMap = new HashMap<String, String>();

        popuplateDeleteLaudoStatusMap(jnjLaudoFileStatusDataList, deleteStatusMap);
        model.addAttribute("deleteStatusMap", deleteStatusMap);

        // Populating Manage Laudo Page Data
        populateManageLaudoPageData(jnjLaudoForm, model, request);
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_LAUDO_ENTRY,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Laudo.ManageLaudo;
    }

    private void popuplateDeleteLaudoStatusMap(final List<JnjLaudoFileStatusData> jnjLaudoFileStatusDataList,
            final Map<String, String> deleteStatusMap)
    {
        final String METHOD_POPULATE_DELETION_STATUS_MAP = "popuplateDeleteLaudoStatusMap()";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_POPULATE_DELETION_STATUS_MAP,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        final String TYPE = "type";
        final String STATUS = "status";
        final String TYPE_SINGLE = "single";
        final String TYPE_MULTIPLE = "multiple";
        final String STATUS_SUCCESS = "success";
        final String STATUS_FAILURE = "failure";
        final String STATUS_PARTIAL = "partial";

        int successfulDelteEntires = 0;
        int failedDelteEntires = 0;
        for (final JnjLaudoFileStatusData jnjLaudoFileStatusData : jnjLaudoFileStatusDataList)
        {
            if (jnjLaudoFileStatusData.getProcessed().booleanValue())
            {
                successfulDelteEntires++;
            }
            else if (!jnjLaudoFileStatusData.getProcessed().booleanValue())
            {
                failedDelteEntires++;
            }
        }
        if (successfulDelteEntires == 1)
        {
            deleteStatusMap.put(TYPE, TYPE_SINGLE);
        }
        else
        {
            deleteStatusMap.put(TYPE, TYPE_MULTIPLE);
        }

        if (successfulDelteEntires == 0)
        {
            deleteStatusMap.put(STATUS, STATUS_FAILURE);
        }
        else if (failedDelteEntires == 0)
        {
            deleteStatusMap.put(STATUS, STATUS_SUCCESS);
        }
        else
        {
            deleteStatusMap.put(STATUS, STATUS_PARTIAL);
        }
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_POPULATE_DELETION_STATUS_MAP,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

    }

    @PostMapping("/laudo/uploadSingle")
    public String uploadSingleLaudoFiles(final JnjLaudoFileUploadForm jnjLaudoFileUploadForm,
            final JnjLaudoForm jnjLaudoForm, final Model model, final HttpServletRequest request)
            throws CMSItemNotFoundException
    {
        final String METHOD_NAME = "uploadSingleLaudoFiles()";
        JnjLaudoFileStatusData jnjLaudoFileStatusData = null;
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        try
        {
            final String pageTitleKey = "services.laudo.manageLaudo";
            model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
        }
        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                    "BusinessException occured while Getting Breadcrumbs. ", businessException,
                    currentClass);
        }
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
        final MultipartFile multipartFile = jnjLaudoFileUploadForm.getMultipartFile();
        if (null != multipartFile)
        {
            if (checkFileSize(multipartFile))// File Size Check
            {
                JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                        "File obtained :: " + multipartFile, currentClass);
                JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                        "File obtained :: " + multipartFile + " within Size Limits of 5MB.",
                        currentClass);
                File file = null;
                try
                {
                    file = jnjLaudoFacade.convertMultipartFile(multipartFile);
                    if (file.exists())
                    {
                        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME, "File found to exist",
                                currentClass);
                        final JnjLaudoData jnjLaudoData = new JnjLaudoData();
                        populateLaudoData(jnjLaudoFileUploadForm, jnjLaudoData, file, model);
                        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                                "jnjLaudoData pouplated. Initiating facade call.", currentClass);
                        jnjLaudoFileStatusData = jnjLaudoFacade.processSingleLaudoEntry(jnjLaudoData);
                        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                                "File upload status :: " + jnjLaudoFileStatusData.getProcessed(),
                                currentClass);
                        // Delete Temp Laudo File
                        jnjLaudoFacade.deleteLaudoTempFile(file);

                        // Populating Manage Laudo Page Data
                        populateManageLaudoPageData(jnjLaudoForm, model, request);
                    }
                }
                catch (final BusinessException businessException)
                {
                    JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                            "BusinessException Occured.", businessException, currentClass);
                    jnjLaudoFileStatusData = getFileStatusData(METHOD_NAME, multipartFile, jnjLaudoFileStatusData);
                }
            }
            else
            {
                JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME, "File Size Greater Than 5 MB",
                        currentClass);

                model.addAttribute("fileSizeExceeded", Boolean.TRUE);
                // Populating Manage Laudo Page Data
                populateManageLaudoPageData(jnjLaudoForm, model, request);
            }
        }
        else
        {
            JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                    "File found to be null / No file submitted", currentClass);
            jnjLaudoFileStatusData = getFileStatusData(METHOD_NAME, multipartFile, jnjLaudoFileStatusData);
        }
        model.addAttribute("singleAutoPopUpFlag", Boolean.TRUE);
        model.addAttribute("jnjLaudoFileStatusData", jnjLaudoFileStatusData);
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Laudo.ManageLaudo;
    }

    private JnjLaudoFileStatusData getFileStatusData(final String METHOD_NAME, final MultipartFile multipartFile,
            JnjLaudoFileStatusData jnjLaudoFileStatusData)
    {
        jnjLaudoFileStatusData = new JnjLaudoFileStatusData();
        jnjLaudoFileStatusData.setFileName(multipartFile.getOriginalFilename());
        jnjLaudoFileStatusData.setProcessed(Boolean.FALSE);
        try
        {
            jnjLaudoFileStatusData
                    .setStatusMessage(messageFacade.getMessageTextForCode(Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE));
        }
        catch (final BusinessException exception)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                    "BusinessException Occured while fetching localised value of the message with code ["
                            + Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE + "]. ",
                    exception, currentClass);

        }
        return jnjLaudoFileStatusData;
    }

    private void populateFormDefaultValues(final JnjLaudoForm jnjLaudoForm)
    {
        final String METHOD_POPULATE_FORM_DEFAULT_VALUES = "populateFormDefaultValues()";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_POPULATE_FORM_DEFAULT_VALUES,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);

        if (Integer.valueOf(0) == Integer.valueOf(jnjLaudoForm.getCurrentPageSize()))
        {
            final int pageDefaultSize = 10;
            jnjLaudoForm.setCurrentPageSize(pageDefaultSize);
        }
        else
        {
            final boolean moreResults = jnjLaudoForm.isMoreResults();
            final int loadMoreClickCounter = jnjLaudoForm.getLoadMoreClickCounter();
            final int pageSize = jnjLaudoForm.getCurrentPageSize();
            final int finalPageSize = (moreResults) ? pageSize * loadMoreClickCounter : pageSize;
            jnjLaudoForm.setCurrentPageSize(finalPageSize);
        }
        if (StringUtils.isEmpty(jnjLaudoForm.getToDate()))
        {
            jnjLaudoForm.setToDate(jnjCommonFacadeUtil.getMessageFromImpex("misc.services.selectDate"));
        }
        if (StringUtils.isEmpty(jnjLaudoForm.getFromDate()))
        {
            jnjLaudoForm.setFromDate(jnjCommonFacadeUtil.getMessageFromImpex("misc.services.selectDate"));
        }
        if (Integer.valueOf(0) == Integer.valueOf(jnjLaudoForm.getLoadMoreClickCounter()))
        {
            final int defaultLoadMoreClickCounter = 1;
            jnjLaudoForm.setLoadMoreClickCounter(defaultLoadMoreClickCounter);
        }
        if (null == Boolean.valueOf(jnjLaudoForm.isMoreResults()))
        {
            jnjLaudoForm.setMoreResults(false);
        }

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_POPULATE_FORM_DEFAULT_VALUES,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

    }

    private void populateJnjPageableData(final JnjLaudoForm jnjLaudoForm, final JnjPageableData jnjPageableData)
    {
        final String METHOD_POPULATE_JNJ_PAGEABLE_DATA = "populateJnjPageableData()";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_POPULATE_JNJ_PAGEABLE_DATA,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);

        jnjPageableData.setPageSize(jnjLaudoForm.getCurrentPageSize());
        jnjPageableData.setSearchBy(jnjLaudoForm.getSearchBy());
        jnjPageableData.setCurrentPage(jnjLaudoForm.getCurrentPageNumber());
        jnjPageableData.setAdditionalSearchText(jnjLaudoForm.getSearchTextLoteNumber());
        jnjPageableData.setSearchText(jnjLaudoForm.getSearchTextJnjId());
        jnjPageableData.setToDate(jnjLaudoForm.getToDate());
        jnjPageableData.setFromDate(jnjLaudoForm.getFromDate());
        jnjPageableData.setSort(jnjLaudoForm.getSortCode());

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_POPULATE_JNJ_PAGEABLE_DATA,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

    }

    private void populateManageLaudoPageData(final JnjLaudoForm jnjLaudoForm, final Model model,
            final HttpServletRequest request) throws CMSItemNotFoundException
    {
        final String METHOD_NAME = "populateManageLaudoPageData()";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        errorOccured = false;
        populateFormDefaultValues(jnjLaudoForm);
        final JnjPageableData jnjPageableData = new JnjPageableData();
        populateJnjPageableData(jnjLaudoForm, jnjPageableData);
        boolean isBatchRequired = isBatchMandatory();
        SearchPageData<JnjLaudoData> laudoData = null;
        try
        {
            laudoData = jnjLaudoFacade.getLaudoDetails(jnjPageableData);
        }
        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                    "BusinessException occured while Getting Laudo Details. ", businessException,
                    currentClass);
            errorOccured = true;
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                    "Exception occured while Getting Laudo Details. ", exception, currentClass);
            errorOccured = true;
        }
        model.addAttribute("isBatchRequired", isBatchRequired);
        model.addAttribute("laudoData", laudoData);
        model.addAttribute("jnjLaudoForm", jnjLaudoForm);
        populateModel(model, laudoData, ShowMode.Page);
        // Need to Remove
        final JnjLaudoFileUploadForm jnjLaudoFileUploadForm = new JnjLaudoFileUploadForm();
        model.addAttribute("jnjLaudoFileUploadForm", jnjLaudoFileUploadForm);
        setupModel(MANAGE_LAUDO_PAGE_ID, model, errorOccured, request);
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

    }

    private boolean checkFileSize(final MultipartFile multipartFile)
    {
        final String METHOD_CHECK_FILE_SIZE = "checkFileSize()";

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CHECK_FILE_SIZE,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        boolean fileSizeWithinLmits = false;
        final long FIE_SIZE_LIMIT = 5242880;// 5MB In Bytes
        if (null != multipartFile)
        {
            final long fileSize = multipartFile.getSize();
            if (fileSize <= FIE_SIZE_LIMIT)
            {
                fileSizeWithinLmits = true;
            }
        }
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CHECK_FILE_SIZE,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

        return fileSizeWithinLmits;
    }

    private void populateLaudoData(final JnjLaudoFileUploadForm jnjLaudoFileUploadForm, final JnjLaudoData jnjLaudoData,
            final File file, final Model model)
    {
        final String METHOD_POPULATE_LAUDO_DATA = "populateLaudoData()";

        final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) companyB2BCommerceService.getCurrentUser();

        final String language = currentUser.getSessionLanguage() != null ? currentUser.getSessionLanguage().getIsocode()
                : null;
        model.addAttribute("sessionlanguage", language);
        if (StringUtils.isNotEmpty(jnjLaudoFileUploadForm.getExpirationDate())) {
        	jnjLaudoData.setExpirationDate(CommonUtil.parseStringToDate(jnjLaudoFileUploadForm.getExpirationDate(),Jnjlab2bcoreConstants.Laudo.LAUDO_EN_DATE_FORMAT));
    	} else {
            JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_POPULATE_LAUDO_DATA,
                    "No Expiration Date Recieved. Throwing BUsiness Exception.", currentClass);
        }
		
        jnjLaudoData.setProductCode(jnjLaudoFileUploadForm.getProductCode());
        jnjLaudoData.setLaudoNumber(jnjLaudoFileUploadForm.getLaudoNumber());
        jnjLaudoData.setPdfFile(file);
        jnjLaudoData.setPdfFileName(file.getName());
		jnjLaudoData.setBatchRequired(jnjLaudoFileUploadForm.isDocumentDeleted());
        try {
			final CountryModel countryModel = jnjLatamCustomerService.getCountryOfUser();
			jnjLaudoData.setCountry(
					countryModel != null ? countryModel.getIsocode() : Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL);
		} catch (final BusinessException exception) {
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_POPULATE_LAUDO_DATA,
					"Error getting country from user's default b2b unit to fetch laudo. Defining BR as default", exception,
					currentClass);
			jnjLaudoData.setCountry(Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL);
		}
        
    }

    @PostMapping("/laudo/uploadMultipe")
    public String uploadMultipleLaudoFiles(final JnjLaudoFileUploadForm jnjLaudoFileUploadForm,
            final JnjLaudoForm jnjLaudoForm,
            @RequestParam(value = "fileExtension", required = true) final String fileExtension, final Model model,
            final HttpServletRequest request) throws CMSItemNotFoundException
    {
        final String METHOD_NAME = "uploadMultipleLaudoFiles()";
        List<JnjLaudoFileStatusData> jnjLaudoFileStatusList = null;
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        try
        {
            final String pageTitleKey = "services.laudo.manageLaudo";
            model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
        }
        catch (final BusinessException businessException)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                    "BusinessException occured while Getting Breadcrumbs. ", businessException,
                    currentClass);
        }
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
        final MultipartFile[] files = jnjLaudoFileUploadForm.getFiles();
        if (files != null && files.length != 0)
        {
            if (checkFileSize(files[0]))// Checking File Size of 1st File.
                                        // Currently we do not support MutliFile
                                        // Upload at UI Level.
            {
                JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                        "File obtained :: " + files[0] + " within Size Limits of 5MB.",
                        currentClass);
                jnjLaudoFileStatusList = jnjLaudoFacade.processUploadedFiles(files);
                setMultiUploadPopupStatus(fileExtension, model, jnjLaudoFileStatusList);
            }
            else
            {
                JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME, "File Size Greater Than 5 MB",
                        currentClass);
                model.addAttribute("fileSizeExceeded", Boolean.TRUE); // Setting
                                                                      // Flag
                                                                      // to
                                                                      // For
                                                                      // Exceeded
                                                                      // File
                                                                      // Size
                // Populating Manage Laudo Page Data
                populateManageLaudoPageData(jnjLaudoForm, model, request);
            }
        }
        else
        {
            JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                    "File found to be null / No file submitted", currentClass);
        }

        if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_CSV))
        {
            model.addAttribute("csvAutoPopUpFlag", Boolean.TRUE);
        }
        else if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_ZIP))
        {
            model.addAttribute("multiAutoPopUpFlag", Boolean.TRUE);
        }

        model.addAttribute("jnjLaudoFileStatusList", jnjLaudoFileStatusList);
        // Populating Manage Laudo Page Data
        populateManageLaudoPageData(jnjLaudoForm, model, request);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.Laudo.ManageLaudo;
    }

    private void setMultiUploadPopupStatus(final String fileExtension, final Model model,
            final List<JnjLaudoFileStatusData> jnjLaudoFileStatusList)
    {
        final String METHOD_NAME = "setMultiUploadPopupStatus()";
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
        // Setting Partially Success Flag In Case of CSV Only
        if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_CSV))
        {
            final JnjLaudoFileStatusData jnjLaudoFileStatusData = jnjLaudoFileStatusList.get(0);
            final String successMessage = jnjLaudoFileStatusData.getStatusMessage();
            final String partiallySuccess = StringUtils.substringAfter(successMessage, Laudo.STATUS_HYPHEN);
            if (StringUtils.isNotEmpty(partiallySuccess))
            {
                model.addAttribute("csvStatusPartial", Boolean.TRUE);
            }
        }
        // Setting the Status of Popup for ZIP.
        else if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_ZIP))
        {
            int successfulFiles = 0;
            int failedFiles = 0;
            for (final JnjLaudoFileStatusData jnjLaudoFileStatusData : jnjLaudoFileStatusList)
            {
                if (jnjLaudoFileStatusData.getProcessed().booleanValue())
                {
                    successfulFiles++;
                }
                else if (!jnjLaudoFileStatusData.getProcessed().booleanValue())
                {
                    failedFiles++;
                }
            }
            if (successfulFiles == 0)
            {
                model.addAttribute("zipStatusFailure", Boolean.TRUE);
            }
            else if (failedFiles == 0)
            {
                model.addAttribute("zipStatusSuccess", Boolean.TRUE);
            }
            else
            {
                model.addAttribute("zipStatusPartial", Boolean.TRUE);
            }
        }
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
    }

    @GetMapping(Jnjlab2bcoreConstants.PostRedirectionURI.CROSS_REFERENCE_TABLE)
    public String getCrossReferenceTable(@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
            @ModelAttribute("crossReferenceForm") final CrossReferenceForm form,
            @RequestParam(value = "sort", required = false) final String sortCode,
            @RequestParam(value = "isNewRequest", defaultValue = "true") final String isNewRequest,
            @RequestParam(value = "page", defaultValue = "0") final int page,
            @RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
            @RequestParam(value = "currentPageSize", defaultValue = "0") int currentPageSize, final Model model)
            throws CMSItemNotFoundException,BusinessException
    {
        final String methodName = "getCrossReferenceTable()";
        final String pageTitleKey = "text.account.crossRef";

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);

        storeCmsPageInModel(model, getCmsPage());
        setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());

        final PageableData pageableData = createPageableData(page, currentPageSize, sortCode, showMode);

        try
        {
            if (list == null)
            {
                list = jnjLaCrossReferenceFacade.getCrossReferenceTable(pageableData);
            }
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                    " Error while fetching records for Cross Reference Table -- " + exception.getMessage(), exception,
                    currentClass);

            model.addAttribute(Jnjlab2bcoreConstants.CrossReferenceTable.ERROR_MSG_KEY,
                    Jnjlab2bcoreConstants.CrossReferenceTable.ERROR_MSG);
        }
        model.addAttribute("defaultPageSize", String.valueOf(currentPageSize));
        model.addAttribute(Jnjlab2bcoreConstants.SellOutReports.DATA_LIST, list);
        model.addAttribute(Jnjlab2bcoreConstants.SellOutReports.SEARCH_TERM, searchTerm);
        form.setSearchTerm(searchTerm);
        form.setPageSize(String.valueOf(pageableData.getPageSize()));
        model.addAttribute("crossReferenceForm", form);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                "Returning CrossReferenceTable View", currentClass);

        list = null;
        searchTerm = null;
        if (sessionService.getAttribute("scrollPos") != null
                && !StringUtils.isEmpty(sessionService.getAttribute("scrollPos").toString()))
        {
            model.addAttribute("scrollPos", sessionService.getAttribute("scrollPos").toString());
            sessionService.removeAttribute("scrollPos");
        }

        model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

        return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
                + JnjlaservicepageaddonControllerConstants.Views.Pages.CrossReferenceTable.CrossReferenceTable;
    }

    /**
     *
     * This method invokes pdf or excel view classes, which convert the Cross
     * Reference Table into pdf or excel depending on the user's selection.
     *
     * @param form
     * @param model
     * @param downloadType
     * @return String
     */

    @PostMapping("/crossReferenceTable/downloadCrossReferenceData")
    public String downloadData(@ModelAttribute("crossReferenceForm") final CrossReferenceForm form,
            @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
            @RequestParam(value = "sort", required = false) final String sortCode,
            @RequestParam(value = "page", defaultValue = "0") final int page, final Model model,
            @RequestParam(value = "downloadType", defaultValue = "None") String downloadType)
    {

        final String methodName = "downloadData()";
        final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
        final CatalogVersionModel currentCatalog = cmsSiteService.getCurrentSite().getContentCatalogs().get(0)
                .getActiveCatalogVersion();

        searchTerm = form.getSearchTerm();
        try
        {
            if (form.getPageSize() == null)
            {
                form.setPageSize("10");
            }
            int pageSize = Integer.parseInt(form.getPageSize());
            if (DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType))
            {
                pageSize = 0;
            }
            final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
            
            if (!StringUtils.isEmpty(searchTerm))
            {
                list = jnjLaCrossReferenceFacade.getCrossReferenceSearch(searchTerm.trim(), pageableData);
            }
            else
            {
                list = jnjLaCrossReferenceFacade.getCrossReferenceTable(pageableData);
            }
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                    " Error while getting cross reference table in Controller class -- " + exception.getMessage(),
                    exception, currentClass);
        }
        final List<ContentCatalogModel> catalogList = getCmsSiteService().getCurrentSite().getContentCatalogs();
        if (catalogList != null && catalogList.size() > 0) {
            final MediaModel mediaModel = mediaService.getMedia(
                    catalogVersionService.getCatalogVersion(catalogList.get(0).getId(),
                            Jnjb2bCoreConstants.ONLINE),
                    Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
            
            if (mediaModel != null) {
                model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel));
            }
        }

            
        final JnjCrossReferenceTableDTO jnjCrossReferenceTableDTO = new JnjCrossReferenceTableDTO();
        jnjCrossReferenceTableDTO.setResultList(list);
        model.addAttribute(Jnjlab2bcoreConstants.CrossReferenceTable.CROSS_REFERENCE, jnjCrossReferenceTableDTO);
        // Send site logo
        model.addAttribute("siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
                        + mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
        
        downloadType = form.getDownloadType();

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                "Calling class for " + downloadType, currentClass);

        return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_PDF : RESULT_EXCEL;
    }

    /**
     *
     * This method invokes the facade class to obtain the Cross Reference Table
     * data corresponding to the product id searched by the user on the front
     * end.
     *
     * @param form
     * @param model
     * @return String
     * @throws CMSItemNotFoundException
     */
    @PostMapping("/crossReferenceTable/searchCrossRef")
    public String doSearch(@ModelAttribute("crossReferenceForm") final CrossReferenceForm form,
            @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
            @RequestParam(value = "sort", required = false) final String sortCode,
            @RequestParam(value = "page", defaultValue = "0") final int page,
            @RequestParam(value = "currentPageSize", defaultValue = "0") final int currentPageSize, final Model model)
            throws CMSItemNotFoundException,BusinessException
    {
        final String methodName = "doSearch()";
        final String pageTitleKey = "text.account.crossRef";

        storeCmsPageInModel(model, getCmsPage());
        setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
        searchTerm = form.getSearchTerm();
        pageSize = form.getPageSize();
        
        if (searchTerm.trim().isEmpty())
        {
            model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
            return REDIRECT_PREFIX + "/services/crossReferenceTable";
        }
        final PageableData pageableData = createPageableData(page, currentPageSize, sortCode, showMode);
        try
        {
            list = jnjLaCrossReferenceFacade.getCrossReferenceSearch(searchTerm.trim(), pageableData);
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                    " Error while getting cross reference table in Controller class -- " + exception.getMessage(),
                    exception, currentClass);
        }
        model.addAttribute("defaultPageSize", Integer.valueOf(10));
        model.addAttribute(Jnjlab2bcoreConstants.SellOutReports.DATA_LIST, list);
        model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
        return REDIRECT_PREFIX + "/services/crossReferenceTable";
    }

    protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
    {
        return getContentPageForLabelOrId(CROSS_REF);
    }

    @AuthorizedUserGroup(value=CONSIGNMENT_GROUP)
	@GetMapping(Jnjb2bCoreConstants.PostRedirectionURI.SERVICES_CONSIGNMENT)
	public String showConsignmentIssue(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException {
		final String methodName = "showConsignmentIssue()";
		final String pageTitleKey = "misc.services.consignmentIssue";

        if(sessionService.getAttribute(SSRF_VALIDATION_FLAG) != null) {
             model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.FALSE);
             sessionService.removeAttribute(SSRF_VALIDATION_FLAG);
        }

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
				LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);
		setupModel(CONSIGNMENT_ISSUE_CMS_PAGE, model, errorOccured, request); // Set up model
		final JnjConsignmentIssueForm jnjConsignmentIssueForm = new JnjConsignmentIssueForm();

		jnjConsignmentIssueForm.setDate("");

		autoFillValuesConsignment(jnjConsignmentIssueForm, model); // AUTO FILL VALUES
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
				LoginaddonConstants.Logging.END_OF_METHOD, currentClass);

		try {
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));
		} catch (final BusinessException businessException) {
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
					"BusinessException occured while Getting Breadcrumbs. ", businessException,
					currentClass);
		}
		return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
				+ JnjlaservicepageaddonControllerConstants.Views.Pages.Misc.JnjConsignmentIssuePage;
	}

    @AuthorizedUserGroup(value=CONSIGNMENT_GROUP)
	@PostMapping(Jnjb2bCoreConstants.PostRedirectionURI.SERVICES_CONSIGNMENT)
	public String sendConsignmentIssueForm(final Model model,
			@ModelAttribute final JnjConsignmentIssueForm jnjConsignmentIssueForm, final HttpServletRequest request)
			throws CMSItemNotFoundException, BusinessException {
		final String methodName = "sendConsignmentIssueForm()";
		final String pageTitleKey = "misc.services.consignmentIssue";

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        final JnjLatamConsignmentIssueDTO jnjLatamConsignmentIssueDTO = populateConsignmentIssueDTO(jnjConsignmentIssueForm);
		final ContentPageModel pageForRequest = getContentPageForLabelOrId(CONSIGNMENT_ISSUE_CMS_PAGE);
		storeCmsPageInModel(model, pageForRequest);
		setUpMetaDataForContentPage(model, pageForRequest);

		final JnjConsignmentIssueForm newJnjConsignmentIssueForm = new JnjConsignmentIssueForm();
		jnjConsignmentIssueForm.setDate("");

		try {
			jnjServicesFacade.sendEmailToUser(jnjLatamConsignmentIssueDTO);
			model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.TRUE);
		} catch (final Exception exception) {
			model.addAttribute(Jnjlab2bcoreConstants.Forms.SUCCESS, Jnjlab2bcoreConstants.Forms.FALSE);
		}
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForPage(pageTitleKey));

		autoFillValuesConsignment(newJnjConsignmentIssueForm, model);
        populateSiteRegions(model);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
				LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
		return JnjlaservicepageaddonControllerConstants.ADDON_PREFIX
				+ JnjlaservicepageaddonControllerConstants.Views.Pages.Misc.JnjConsignmentIssuePage;
	}

    private JnjLatamConsignmentIssueDTO populateConsignmentIssueDTO(@ModelAttribute JnjConsignmentIssueForm jnjConsignmentIssueForm) {
        final String methodName = "populateConsignmentIssueDTO()";

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
                LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.FORMS_NAME, methodName, " consignment issue :: "
                + jnjConsignmentIssueForm.getContactEmail() + " :: Contact Phone :: "
                + jnjConsignmentIssueForm.getContactPhone() + " :: Date :: " + jnjConsignmentIssueForm.getDate()
                + " :: Doctor :: " + jnjConsignmentIssueForm.getDoctor() + " :: Order Reason :: "
                + jnjConsignmentIssueForm.getOrderReason() + " :: Patient :: " + jnjConsignmentIssueForm.getPatient()
                + " :: PO Number :: " + jnjConsignmentIssueForm.getPoNumber() + " :: getReplenishmentOrFillUpDoc :: "
                + jnjConsignmentIssueForm.getReplenishmentOrFillUpDoc() + " :: getReplenishmentOrFillUpNFE :: "
                + jnjConsignmentIssueForm.getReplenishmentOrFillUpNFE() + " :: getShipTo :: "
                + jnjConsignmentIssueForm.getShipTo() + " :: getSoldTo :: " + jnjConsignmentIssueForm.getSoldTo()
                + " :: getSpecialStockPartner :: " + jnjConsignmentIssueForm.getSpecialStockPartner()
                + " :: getItem :: " + jnjConsignmentIssueForm.getItem() + " :: getQty :: "
                + jnjConsignmentIssueForm.getQty() + " :: getUom :: " + jnjConsignmentIssueForm.getUom()
                + " :: getBatchNumber :: " + jnjConsignmentIssueForm.getBatchNumber() + " :: getFolio :: "
                + jnjConsignmentIssueForm.getFolio() + " :: getPrice :: " + jnjConsignmentIssueForm.getPrice()
                + " :: getHealthPlan :: " + jnjConsignmentIssueForm.getHealthPlan()
                + " :: getSurgeryDate :: " + jnjConsignmentIssueForm.getSurgeryDate()
                + " :: getObservation :: " + jnjConsignmentIssueForm.getObservation()
                + " :: getResidentialQuarter :: " + jnjConsignmentIssueForm.getResidentialQuarter()
                + " :: getCity :: " + jnjConsignmentIssueForm.getCity()
                + " :: getState :: " + jnjConsignmentIssueForm.getState()
                + " :: getZipCode :: " + jnjConsignmentIssueForm.getZipCode()
                + " :: getBillingOrReplacement :: " + jnjConsignmentIssueForm.getBillingOrReplacement(),currentClass);

        final JnjLatamConsignmentIssueDTO jnjLatamConsignmentIssueDTO = new JnjLatamConsignmentIssueDTO();
        jnjLatamConsignmentIssueDTO.setFormName(Jnjlab2bcoreConstants.Forms.CONSIGNMENT_ISSUE_CMS_PAGE);
        jnjServicesFacade.setInfoForConsignmentForm(jnjLatamConsignmentIssueDTO);
        jnjLatamConsignmentIssueDTO.setBatchNumber(jnjConsignmentIssueForm.getBatchNumber());
        jnjLatamConsignmentIssueDTO.setContactEmail(jnjConsignmentIssueForm.getContactEmail());
        jnjLatamConsignmentIssueDTO.setContactPhone(jnjConsignmentIssueForm.getContactPhone());
        jnjLatamConsignmentIssueDTO.setCurrency(jnjConsignmentIssueForm.getCurrency());
        jnjLatamConsignmentIssueDTO.setDate(jnjConsignmentIssueForm.getDate());
        jnjLatamConsignmentIssueDTO.setDoctor(jnjConsignmentIssueForm.getDoctor());
        jnjLatamConsignmentIssueDTO.setFolio(jnjConsignmentIssueForm.getFolio());
        jnjLatamConsignmentIssueDTO.setItem(jnjConsignmentIssueForm.getItem());
        jnjLatamConsignmentIssueDTO.setOrderReason(jnjConsignmentIssueForm.getOrderReason());
        jnjLatamConsignmentIssueDTO.setPatient(jnjConsignmentIssueForm.getPatient());
        jnjLatamConsignmentIssueDTO.setPoNumber(jnjConsignmentIssueForm.getPoNumber());
        jnjLatamConsignmentIssueDTO.setPrice(jnjConsignmentIssueForm.getPrice());
        jnjLatamConsignmentIssueDTO.setQty(jnjConsignmentIssueForm.getQty());
        jnjLatamConsignmentIssueDTO.setReplenishmentOrFillUpDoc(jnjConsignmentIssueForm.getReplenishmentOrFillUpDoc());
        jnjLatamConsignmentIssueDTO.setReplenishmentOrFillUpNFE(jnjConsignmentIssueForm.getReplenishmentOrFillUpNFE());
        jnjLatamConsignmentIssueDTO.setShipTo(jnjConsignmentIssueForm.getShipTo());
        jnjLatamConsignmentIssueDTO.setSoldTo(jnjConsignmentIssueForm.getSoldTo());
        jnjLatamConsignmentIssueDTO.setSpecialStockPartner(jnjConsignmentIssueForm.getSpecialStockPartner());
        jnjLatamConsignmentIssueDTO.setUom(jnjConsignmentIssueForm.getUom());
        jnjLatamConsignmentIssueDTO.setServerURL(Config.getParameter(Jnjb2bCoreConstants.UserCreation.HOST_NAME));
        jnjLatamConsignmentIssueDTO.setFromEmail(getUser().getEmail());
        jnjLatamConsignmentIssueDTO.setFromDisplayName(getUser().getName());

        jnjLatamConsignmentIssueDTO.setHealthPlan(jnjConsignmentIssueForm.getHealthPlan());
        jnjLatamConsignmentIssueDTO.setSurgeryDate(jnjConsignmentIssueForm.getSurgeryDate());
        jnjLatamConsignmentIssueDTO.setObservation(jnjConsignmentIssueForm.getObservation());
        jnjLatamConsignmentIssueDTO.setResidentialQuarter(jnjConsignmentIssueForm.getResidentialQuarter());
        jnjLatamConsignmentIssueDTO.setCity(jnjConsignmentIssueForm.getCity());
        jnjLatamConsignmentIssueDTO.setState(jnjConsignmentIssueForm.getState());
        jnjLatamConsignmentIssueDTO.setZipCode(jnjConsignmentIssueForm.getZipCode());
        jnjLatamConsignmentIssueDTO.setBillingOrReplacement(jnjConsignmentIssueForm.getBillingOrReplacement());

        /** Start: Sending auto fill values **/
        jnjLatamConsignmentIssueDTO.setCustomerName(jnjConsignmentIssueForm.getCustomerName());
        jnjLatamConsignmentIssueDTO.setHospital(jnjConsignmentIssueForm.getHospital());
        jnjLatamConsignmentIssueDTO.setContactFirstName(jnjConsignmentIssueForm.getContactName().split(" ")[0]);
        if (jnjConsignmentIssueForm.getContactName().split(" ").length > 1) {
            jnjLatamConsignmentIssueDTO.setContactLastName(jnjConsignmentIssueForm.getContactName().split(" ")[1]);
        }
        jnjLatamConsignmentIssueDTO.setSoldTo(jnjConsignmentIssueForm.getSoldTo());
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
                "AUTO FILL DATA SET :: setCustomerName :: " + jnjLatamConsignmentIssueDTO.getCustomerName()
                        + " :: setHospital :: " + jnjLatamConsignmentIssueDTO.getHospital() + " :: setContactFirstName :: "
                        + jnjLatamConsignmentIssueDTO.getContactFirstName() + " :: setContactLastName :: "
                        + jnjLatamConsignmentIssueDTO.getContactLastName() + " :: setCityOrState :: "
                        + jnjLatamConsignmentIssueDTO.getCityOrState() + " :: setSoldTo :: "
                        + jnjLatamConsignmentIssueDTO.getSoldTo(),
                currentClass);
        /** End: Sending auto fill values **/return jnjLatamConsignmentIssueDTO;
    }

    private void autoFillValuesConsignment(final JnjConsignmentIssueForm jnjConsignmentIssueForm, final Model model) {
		final String methodName = "autoFillValuesConsignment()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
				LoginaddonConstants.Logging.BEGIN_OF_METHOD, currentClass);

		JnjConsignmentIssueDTO jnjConsignmentIssueDTO = new JnjConsignmentIssueDTO();

		// Facade call to fetch values
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
				"Facade call to fetch auto fill values", currentClass);

		jnjConsignmentIssueDTO = jnjServicesFacade.setInfoForConsignmentForm(jnjConsignmentIssueDTO);
		jnjConsignmentIssueForm.setCustomerName(jnjConsignmentIssueDTO.getCustomerName());
		jnjConsignmentIssueForm.setHospital(jnjConsignmentIssueDTO.getHospital());
		jnjConsignmentIssueForm.setContactName(
				jnjConsignmentIssueDTO.getContactFirstName() + (StringUtils.isEmpty(jnjConsignmentIssueDTO.getContactLastName()) ? StringUtils.EMPTY :(" " + jnjConsignmentIssueDTO.getContactLastName())));
		jnjConsignmentIssueForm.setSoldTo(jnjConsignmentIssueDTO.getSoldTo());
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
				"Setting form with auto filled values in model", currentClass);
		model.addAttribute("jnjConsignmentIssueForm", jnjConsignmentIssueForm);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.SERVICES_CMS_PAGE, methodName,
				LoginaddonConstants.Logging.END_OF_METHOD, currentClass);
	}

}
