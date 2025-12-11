package com.jnj.b2b.jnjglobalresources.controllers.pages;

import de.hybris.platform.b2bcommercefacades.company.B2BUserGroupFacade;
//import de.hybris.platform.b2bacceleratorfacades.company.B2BCommerceBudgetFacade;
//import de.hybris.platform.b2bacceleratorfacades.company.B2BCommerceCostCenterFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BCostCenterFacade;
import de.hybris.platform.b2bapprovalprocessfacades.company.B2BPermissionFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUserFacade;
import de.hybris.platform.b2bacceleratorfacades.company.CompanyB2BCommerceFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BBudgetData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionTypeData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.breadcrumb.impl.MyCompanyBreadcrumbBuilder;
import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.b2b.storefront.forms.B2BBudgetForm;
import com.jnj.b2b.storefront.forms.B2BCostCenterForm;
import com.jnj.b2b.storefront.forms.B2BCustomerForm;
import com.jnj.b2b.jnjglobalresources.controllers.pages.JnjGTMyCompanyPageController.SelectOption;
import com.jnj.b2b.jnjglobalresources.constants.JnjglobalresourcesConstants;
import com.jnj.b2b.jnjglobalresources.controllers.JnjglobalresourcesControllerConstants;
import com.jnj.b2b.jnjglobalresources.form.JnjGTAddressForm;
import com.jnj.b2b.jnjglobalresources.form.JnjGTB2BCustomerForm;
/*import com.jnj.b2b.jnjglobalresources.form.validation.B2BBudgetFormValidator;
import com.jnj.b2b.jnjglobalresources.form.validation.B2BPermissionFormValidator;*/
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.AccessBy;
import com.jnj.core.enums.CustomerStatus;
import com.jnj.facades.company.JnjGTB2BCommerceUserFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.facades.territory.JnjGTTerritoryFacade;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * Controller for organization management.
 */
@Controller("jnjGTMyCompanyPageController")
@Scope("tenant")
public class JnjGTMyCompanyPageController extends AbstractSearchPageController
{
	protected static final String SUCCESS = "success";
	protected static final String MY_COMPANY_CMS_PAGE = "my-company";
	protected static final String MY_USER_MANAGEMENT_CMS_PAGE = "user-management";
	protected static final String MY_COMPANY_EDIT_USER_PROFILE_PAGE = "editUserProfile";
	protected static final String ORGANIZATION_MANAGEMENT_CMS_PAGE = "organizationManagement";
	protected static final String MANAGE_UNITS_CMS_PAGE = "manageUnits";
	protected static final String MANAGE_USERGROUPS_CMS_PAGE = "manageUsergroups";
	protected static final String REDIRECT_TO_UNIT_DETAILS = REDIRECT_PREFIX
			+ "/my-company/organization-management/manage-units/details/?unit=%s";
	protected static final String REDIRECT_TO_COSTCENTER_DETAILS = REDIRECT_PREFIX
			+ "/my-company/organization-management/manage-costcenters/view?costCenterCode=%s";
	protected static final String REDIRECT_TO_USER_DETAILS = REDIRECT_PREFIX
			+ "/my-company/organization-management/manage-users/details?user=%s";
	protected static final String REDIRECT_TO_USER_SEARCH_PAGE = REDIRECT_PREFIX + "/resources/usermanagement";
	protected static final String REDIRECT_TO_BUDGET_DETAILS = REDIRECT_PREFIX
			+ "/my-company/organization-management/manage-budgets/view/?budgetCode=%s";
	protected static final String MANAGE_COST_CENTER_BASE_URL = "/my-company/organization-management/manage-costcenters";
	protected static final String MANAGE_COSTCENTERS_EDIT_URL = "/my-company/organization-management/manage-costcenters/edit/?costCenterCode=%s";
	protected static final String MANAGE_COSTCENTERS_VIEW_URL = "/my-company/organization-management/manage-costcenters/view/?costCenterCode=%s";
	protected static final String MANAGE_COSTCENTERS_SELECTBUDGET_URL = "/my-company/organization-management/manage-costcenters/selectBudget/?costCenterCode=%s";
	protected static final String MANAGE_PERMISSIONS_VIEW_URL = "/my-company/organization-management/manage-permissions/view/?permissionCode=%s";
	protected static final String MANAGE_PERMISSIONS_EDIT_URL = "/my-company/organization-management/manage-permissions/edit/?permissionCode=%s";
	protected static final String REDIRECT_TO_PERMISSION_DETAILS = REDIRECT_PREFIX + MANAGE_PERMISSIONS_VIEW_URL;
	protected static final String REDIRECT_TO_USERGROUP_DETAILS = REDIRECT_PREFIX
			+ "/my-company/organization-management/manage-usergroups/details?usergroup=%s";
	protected static final String REDIRECT_TO_USER_GROUPS_PAGE = REDIRECT_PREFIX
			+ "/my-company/organization-management/manage-usergroups";
	protected static final Logger LOG = Logger.getLogger(JnjGTMyCompanyPageController.class);
	protected static final String SINGLE_WHITEPSACE = " ";
	protected static final String MANAGE_UNITS_BASE_URL = "/my-company/organization-management/manage-units";
	protected static final String MANAGE_USERGROUPS_BASE_URL = "/my-company/organization-management/manage-usergroups";
	protected static final String ADD_COSTCENTER_URL = "/my-company/organization-management/manage-costcenters/add";
	protected static final String EDIT_COSTCENTER_URL = "/my-company/organization-management/manage-costcenters/update";
	protected final String RESOURCE_LABEL = "resource.RESOURCE.text";
	protected final String USER_MANAGEMENT_LABEL = "userSearch.breadCrumb";
	protected final String EDIT_PROFILE_LABEL = "editProfile.breadCrumb";
	protected final String CREATE_NEW_PROFILE_LABEL = "createNewProfile.breadCrumb";
	protected static final String B2BCUSTOMERGROUP = "b2bcustomergroup";
	protected static final String INVALID_TERRITORY = "invalidTerritory";
	protected static final String ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY = "account.selection.resultsPerPage";
	protected static final String USER_FINANCIAL_GROUP ="user.financial.report.group";
	@Resource(name = "checkoutFacade")
	protected CheckoutFacade checkoutFacade;

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	// jnjGTCustomerFacade
	@Resource(name = "b2bCommerceFacade")
	protected CompanyB2BCommerceFacade companyB2BCommerceFacade;

	/*@Resource(name = "b2bCommerceUserFacade")
	protected B2BCommerceUserFacade b2bCommerceUserFacade;*/
	
	@Resource(name = "b2bUserFacade")
	protected B2BUserFacade b2bUserFacade;

	/*@Resource(name = "b2bCommerceUnitFacade")
	protected B2BCommerceUnitFacade b2bCommerceUnitFacade;*/
	
	@Resource(name = "b2bUnitFacade")
	protected B2BUnitFacade b2bUnitFacade;

	/*@Resource(name = "b2bCommercePermissionFacade")
	protected B2BCommercePermissionFacade b2bCommercePermissionFacade;*/
	
	@Resource(name = "b2bPermissionFacade")
	protected B2BPermissionFacade b2bPermissionFacade;

	//Commented by Parthiban for HY 5.7 to 6.0 migration deprecated class
	/*@Resource(name = "b2bCommerceCostCenterFacade")
	protected B2BCommerceCostCenterFacade b2bCommerceCostCenterFacade;*/
	//Added by Parthiban for HY 5.7 to 6.0 migration
	@Resource(name = "costCenterFacade")
	protected B2BCostCenterFacade  b2bCostCenterFacade;
	
	
	/*@Resource(name = "b2bCommerceBudgetFacade")
	protected B2BCommerceBudgetFacade b2bCommerceBudgetFacade;*/

	/*@Resource(name = "b2bCommerceB2BUserGroupFacade")
	protected B2BCommerceB2BUserGroupFacade b2bCommerceB2BUserGroupFacade;*/
	
	@Resource(name = "b2bUserGroupFacade")
	protected B2BUserGroupFacade b2bUserGroupFacade;

	@Resource(name = "myCompanyBreadcrumbBuilder")
	protected MyCompanyBreadcrumbBuilder myCompanyBreadcrumbBuilder;

	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	/*@Resource(name = "b2BPermissionFormValidator")
	protected B2BPermissionFormValidator b2BPermissionFormValidator;*/
/*
	@Resource(name = "b2BBudgetFormValidator")
	protected B2BBudgetFormValidator b2BBudgetFormValidator;*/

	@Resource(name = "formatFactory")
	protected FormatFactory formatFactory;

	@Resource(name = "GTB2BCommerceUserFacade")
	protected JnjGTB2BCommerceUserFacade jnjGTB2BCommerceUserFacade;
	
	@Resource(name = "commerceCategoryService")
	protected CommerceCategoryService commerceCategoryService;

	@Autowired
	protected EnumerationService enumerationService;

	@Autowired
	protected MessageFacadeUtill messageFacade;

	@Autowired
	protected JnjGTTerritoryFacade jnjGTTerritoryFacade;

	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	public CheckoutFacade getCheckoutFacade() {
		return checkoutFacade;
	}

	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public CompanyB2BCommerceFacade getCompanyB2BCommerceFacade() {
		return companyB2BCommerceFacade;
	}

	/*public B2BCommerceUserFacade getB2bCommerceUserFacade() {
		return b2bCommerceUserFacade;
	}*/

	
	/*public B2BCommerceUnitFacade getB2bCommerceUnitFacade() {
		return b2bCommerceUnitFacade;
	}*/

	public B2BUserFacade getB2bUserFacade() {
		return b2bUserFacade;
	}
	
	public B2BUnitFacade getB2bUnitFacade() {
		return b2bUnitFacade;
	}
	
	/*public B2BCommercePermissionFacade getB2bCommercePermissionFacade() {
		return b2bCommercePermissionFacade;
	}*/

	/*public B2BCommerceCostCenterFacade getB2bCommerceCostCenterFacade() {
		return b2bCommerceCostCenterFacade;
	}*/

	
	public B2BPermissionFacade getB2bPermissionFacade() {
		return b2bPermissionFacade;
	}

	/*public B2BCommerceBudgetFacade getB2bCommerceBudgetFacade() {
		return b2bCommerceBudgetFacade;
	}*/

	/*public B2BCommerceB2BUserGroupFacade getB2bCommerceB2BUserGroupFacade() {
		return b2bCommerceB2BUserGroupFacade;
	}*/
	
	

	public B2BCostCenterFacade getB2bCostCenterFacade()
	{
		return b2bCostCenterFacade;
	}

	public MyCompanyBreadcrumbBuilder getMyCompanyBreadcrumbBuilder() {
		return myCompanyBreadcrumbBuilder;
	}

	public B2BUserGroupFacade getB2bUserGroupFacade() {
		return b2bUserGroupFacade;
	}

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}

	public FormatFactory getFormatFactory() {
		return formatFactory;
	}

	public JnjGTB2BCommerceUserFacade getJnjGTB2BCommerceUserFacade() {
		return jnjGTB2BCommerceUserFacade;
	}

	public EnumerationService getEnumerationService() {
		return enumerationService;
	}

	public MessageFacadeUtill getMessageFacade() {
		return messageFacade;
	}

	public JnjGTTerritoryFacade getJnjGTTerritoryFacade() {
		return jnjGTTerritoryFacade;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	@ModelAttribute("b2bUnits")
	public List<SelectOption> getB2BUnits()
	{
		return populateSelectBoxForString(b2bUnitFacade.getAllActiveUnitsOfOrganization());
	}

	@ModelAttribute("b2bCostCenterCurrencies")
	public List<SelectOption> getAllCostCenters()
	{
		return populateSelectBoxForString(companyB2BCommerceFacade.getAllCurrencies());
	}


	@ModelAttribute("b2bPermissionTypes")
	public List<SelectOption> getB2BPermissionTypes()
	{
		final List<SelectOption> b2bPermissionTypeList = new ArrayList<SelectOption>();
		final List<B2BPermissionTypeData> permissionTypeDatalist = (List<B2BPermissionTypeData>) b2bPermissionFacade
				.getB2BPermissionTypes();
		for (final B2BPermissionTypeData b2bPermissionType : permissionTypeDatalist)
		{
			final SelectOption selectOption = new SelectOption(b2bPermissionType.getCode(), b2bPermissionType.getName());
			b2bPermissionTypeList.add(selectOption);
		}

		return b2bPermissionTypeList;
	}

	@InitBinder
	protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder)
	{
		final DateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
		binder.registerCustomEditor(Date.class, editor);
	}

	@ModelAttribute("businessProcesses")
	public List<SelectOption> getBusinessProcesses()
	{
		final List<SelectOption> selectOptions = new ArrayList<SelectOption>();
		final Map<String, String> procecess = this.companyB2BCommerceFacade.getBusinessProcesses();
		for (final Map.Entry<String, String> entry : procecess.entrySet())
		{
			selectOptions.add(new SelectOption(entry.getKey(), entry.getValue()));
		}
		return selectOptions;
	}

	@ModelAttribute("b2bStore")
	public String getCurrentB2BStore()
	{
		return companyB2BCommerceFacade.getCurrentStore();
	}

	@GetMapping("/my-company")
	@RequireHardLogIn
	public String myCompany(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(MY_COMPANY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_COMPANY_CMS_PAGE));
		model.addAttribute("breadcrumbs", myCompanyBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.MyCompanyHomePage);
	}
	
	@GetMapping("/my-company/organization-management")
	@RequireHardLogIn
	public String organizationManagement(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		model.addAttribute("breadcrumbs", myCompanyBreadcrumbBuilder.getBreadcrumbs("text.company.organizationManagement"));
		model.addAttribute("unitUid", companyB2BCommerceFacade.getParentUnit().getUid());
		model.addAttribute("userUid", jnjGTCustomerFacade.getCurrentCustomer().getUid());
		model.addAttribute("metaRobots", "no-index,no-follow");
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.MyCompanyHomePage);
	}

	protected String unitDetails(final String unit, final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(MANAGE_UNITS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MANAGE_UNITS_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUnitsDetailsBreadcrumbs(unit);
		model.addAttribute("breadcrumbs", breadcrumbs);

		B2BUnitData unitData = companyB2BCommerceFacade.getUnitForUid(unit);
		if (unitData == null)
		{
			unitData = new B2BUnitData();
			GlobalMessages.addErrorMessage(model, "b2bunit.notfound");
		}
		else if (!unitData.isActive())
		{
			GlobalMessages.addInfoMessage(model, "b2bunit.disabled.infomsg");
		}

		model.addAttribute("unit", unitData);
		model.addAttribute("user", jnjGTCustomerFacade.getCurrentCustomer());
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MyCompany.MyCompanyManageUnitDetailsPage;
	}

	protected String addCostCenter(final Model model) throws CMSItemNotFoundException
	{
		if (!model.containsAttribute("b2BCostCenterForm"))
		{
			final B2BCostCenterForm b2BCostCenterForm = new B2BCostCenterForm();
			b2BCostCenterForm.setParentB2BUnit(companyB2BCommerceFacade.getParentUnit().getUid());
			model.addAttribute(b2BCostCenterForm);
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageCostCenterBreadCrumbs();
		breadcrumbs.add(new Breadcrumb(ADD_COSTCENTER_URL, getMessageSource().getMessage("text.company.costCenter.addPage", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MyCompany.MyCompanyAddCostCenterPage;
	}

	protected String saveCostCenter(@Valid final B2BCostCenterForm b2BCostCenterForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (bindingResult.hasFieldErrors())
		{
			model.addAttribute(b2BCostCenterForm);
			return addCostCenter(model);
		}
		final B2BCostCenterData b2BCostCenterData = populateB2BCostCenterDataFromForm(b2BCostCenterForm);
		try
		{
			b2bCostCenterFacade.addCostCenter(b2BCostCenterData); //modified by Parthiban for HY 5.7 to 6.0 migration deprecated class
		}
		catch (final Exception e)
		{
			LOG.warn("Exception while saving the cost center details " + e);
			model.addAttribute(b2BCostCenterForm);
			bindingResult.rejectValue("code", "text.company.costCenter.code.exists.error.title");
			GlobalMessages.addErrorMessage(model, "form.global.error");
			return addCostCenter(model);
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageCostCenterBreadCrumbs();
		breadcrumbs.add(new Breadcrumb(ADD_COSTCENTER_URL, getMessageSource().getMessage("text.company.costCenter.addPage", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		GlobalMessages
				.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "text.company.costCenter.create.success");
		return String.format(REDIRECT_TO_COSTCENTER_DETAILS, urlEncode(b2BCostCenterData.getCode()));
	}

	protected String viewCostCenterDetails(final String costCenterCode, final Model model) throws CMSItemNotFoundException
	{ 
		model.addAttribute("b2bCostCenter", b2bCostCenterFacade.getCostCenterDataForCode(costCenterCode));
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageCostCenterBreadCrumbs();
		myCompanyBreadcrumbBuilder.addViewCostCenterBreadCrumbs(breadcrumbs, costCenterCode);
		model.addAttribute("breadcrumbs", breadcrumbs);
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MyCompany.MyCompanyCostCenterViewPage;
	}

	protected String editCostCenterDetails(final String costCenterCode, final Model model) throws CMSItemNotFoundException
	{
		if (!model.containsAttribute("b2BCostCenterForm"))
		{
			final B2BCostCenterData b2BCostCenterData = b2bCostCenterFacade.getCostCenterDataForCode(costCenterCode);
			final B2BCostCenterForm b2BCostCenterform = new B2BCostCenterForm();
			b2BCostCenterform.setOriginalCode(costCenterCode);
			b2BCostCenterform.setCode(b2BCostCenterData.getCode());
			b2BCostCenterform.setCurrency(b2BCostCenterData.getCurrency().getIsocode());
			b2BCostCenterform.setName(b2BCostCenterData.getName());
			b2BCostCenterform.setParentB2BUnit(b2BCostCenterData.getUnit().getUid());
			model.addAttribute(b2BCostCenterform);
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageCostCenterBreadCrumbs();
		myCompanyBreadcrumbBuilder.addViewCostCenterBreadCrumbs(breadcrumbs, costCenterCode);
		breadcrumbs.add(new Breadcrumb(String.format(MANAGE_COSTCENTERS_EDIT_URL, costCenterCode), getMessageSource().getMessage(
				"text.company.costCenter.editPage.breadcrumb", new Object[]
				{ costCenterCode }, getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MyCompany.MyCompanyCostCenterEditPage;
	}

	protected String updateCostCenterDetails(final B2BCostCenterForm b2BCostCenterForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			model.addAttribute(b2BCostCenterForm);
			return editCostCenterDetails(b2BCostCenterForm.getOriginalCode(), model);
		}
		final B2BCostCenterData b2BCostCenterData = populateB2BCostCenterDataFromForm(b2BCostCenterForm);

		try
		{
			//b2bCommerceCostCenterFacade.updateCostCenterDetails(b2BCostCenterData); 
			b2bCostCenterFacade.updateCostCenter(b2BCostCenterData);//modified by Parthiban for HY 5.7 to 6.0 migration deprecated class
			
		}
		catch (final Exception e)
		{
			LOG.warn("Exception while saving the cost center details " + e);
			model.addAttribute(b2BCostCenterForm);
			GlobalMessages.addErrorMessage(model, "form.global.error");
			return editCostCenterDetails(b2BCostCenterData.getOriginalCode(), model);
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		GlobalMessages
				.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "text.company.costCenter.update.success");
		return String.format(REDIRECT_TO_COSTCENTER_DETAILS, urlEncode(b2BCostCenterData.getCode()));
	}

	protected String createUser(final Model model) throws CMSItemNotFoundException, BusinessException
	{
		final JnjGTB2BCustomerForm jnjGTB2BCustomerForm = new JnjGTB2BCustomerForm();
		model.addAttribute("jnjGTB2BCustomerForm", jnjGTB2BCustomerForm);
		model.addAttribute("pageTitle", "Create User Page");
		setRolesAndDivisionsInModel(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		/*breadcrumbs.add(new Breadcrumb("/resources/usefullinks", messageFacade.getMessageTextForCode(RESOURCE_LABEL,
				getI18nService().getCurrentLocale()), null));*/
		breadcrumbs.add(new Breadcrumb("/resources/usermanagement", messageFacade.getMessageTextForCode(USER_MANAGEMENT_LABEL,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("/resources/usermanagement/create", messageFacade.getMessageTextForCode(
				CREATE_NEW_PROFILE_LABEL, getI18nService().getCurrentLocale()), null));
		
		Map<String, String> loggedUserAccountData = jnjGTCustomerFacade.getAccountsMap(true);  
		model.addAttribute("loggedUserAccountList", loggedUserAccountData.keySet()); // login user account group list
		
		/** Calling facade layer **/
		final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);
		final int finalPageSize = (Boolean.valueOf("false")) ? pageSizeFromConfig * Integer.valueOf("1")
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(0, finalPageSize, JnJB2BUnitModel.UID, ShowMode.Page); 

		final JnjGTAccountSelectionData allAccountSelectionData = jnjGTCustomerFacade.getAccountsMap(true, false, false, pageableData);
		model.addAttribute("allAccountSelectionList", allAccountSelectionData.getAccountsMap()); // All account group list from 'JnjGlobalUnit'
		
		//Soumitra - Add attribute allFranchise. This will be used to populate edit Franchise popup. AAOL-4913
		model.addAttribute("allFranchise", jnjGTCustomerFacade.getAllFranchise());
		
		model.addAttribute("breadcrumbs", breadcrumbs);
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.MyCompanyManageUserAddEditFormPage);
	}

	protected String createUser(final JnjGTB2BCustomerForm b2BCustomerForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel,final HttpServletRequest request) throws CMSItemNotFoundException, BusinessException
	{

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			model.addAttribute(b2BCustomerForm);
			return editUser(b2BCustomerForm.getUid(), model);
		}
		final List<String> roles = new ArrayList<String>();
		final JnjGTCustomerData b2bCustomerData = new JnjGTCustomerData();
		populateBasicCustomerData(b2BCustomerForm, b2bCustomerData);
		if (b2BCustomerForm instanceof JnjGTB2BCustomerForm)
		{
			final JnjGTB2BCustomerForm jnjGTB2BCustomerForm = (JnjGTB2BCustomerForm) b2BCustomerForm;
			if (!StringUtils.isEmpty(jnjGTB2BCustomerForm.getGroups()))
			{
				b2bCustomerData.setGroups(Arrays.asList((jnjGTB2BCustomerForm.getGroups()).split(Jnjb2bCoreConstants.CONST_COMMA)));
			}
			roles.add(B2BCUSTOMERGROUP);
			if (!StringUtils.isEmpty(jnjGTB2BCustomerForm.getWwid()))
			{
				if (JnJCommonUtil.getValue(JnjglobalresourcesConstants.Register.VIEW_ORDER_BUYER_USER_ROLE).equals(
						jnjGTB2BCustomerForm.getRole()))
				{
					roles.add(JnJCommonUtil.getValue(JnjglobalresourcesConstants.Register.VIEW_ORDER_SALES_USER_ROLE));
				}
				else if (JnJCommonUtil.getValue(JnjglobalresourcesConstants.Register.PLACE_ORDER_BUYER_USER_ROLE).equals(
						jnjGTB2BCustomerForm.getRole()))
				{
					roles.add(JnJCommonUtil.getValue(JnjglobalresourcesConstants.Register.PLACE_ORDER_SALES_USER_ROLE));
				}
				
				{
					roles.add(JnJCommonUtil.getValue(JnjglobalresourcesConstants.Register.USER_GROUP_NO_CHARGE_USERS));
				}
				
				b2bCustomerData.setAccessBy(AccessBy.WWID.name());
			}
			else
			{
				if (jnjGTB2BCustomerForm.getRoles() != null) {
					Collection<String> listOfSelectedRoles = jnjGTB2BCustomerForm.getRoles();
					for (String selectedRole : listOfSelectedRoles) {
						roles.add(selectedRole);
					}
				} 
				roles.add(jnjGTB2BCustomerForm.getRole());
				b2bCustomerData.setAccessBy(AccessBy.NOT_SALES_REP.name());
			}
			if (StringUtils.isNotEmpty(jnjGTB2BCustomerForm.getDivision()))
			{
				b2bCustomerData.setDivison(jnjGTB2BCustomerForm.getDivision());
			}
		}
		
		if (b2BCustomerForm instanceof JnjGTB2BCustomerForm)
		{
			final JnjGTB2BCustomerForm jnjGTB2BCustomerForm = (JnjGTB2BCustomerForm) b2BCustomerForm;
			b2bCustomerData.setMddSector(Boolean.valueOf(jnjGTB2BCustomerForm.isMdd()));
			b2bCustomerData.setConsumerSector(Boolean.valueOf(jnjGTB2BCustomerForm.isConsumer()));
			b2bCustomerData.setPharmaSector(Boolean.valueOf(jnjGTB2BCustomerForm.isPharma()));
		}
		b2bCustomerData.setStatus(CustomerStatus.PENDING_PROFILE_SETUP.getCode());
		roles.add(Jnjb2bCoreConstants.B2BUnit.B2BCUSTOMERGROUP);
		b2bCustomerData.setRoles(roles);
		b2bCustomerData.setLoginDisabledFlag(Boolean.valueOf(true));
		b2bCustomerData.setActive(true);
		

		try
		{
			jnjGTCustomerFacade.updateCustomer(b2bCustomerData);
			b2BCustomerForm.setUid(b2BCustomerForm.getEmail().toLowerCase());
			sentAppropriateMailWithUserinformation(b2BCustomerForm,request);
			sentTemporaryEmail(b2BCustomerForm, request);
		}
		catch (final DuplicateUidException e)
		{
			bindingResult.rejectValue("email", "text.manageuser.error.email.exists.title");
			GlobalMessages.addErrorMessage(model, "form.global.error");
			model.addAttribute("b2BCustomerForm", b2BCustomerForm);
			return editUser(b2BCustomerForm.getUid(), model);
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-company/organization-management/", getMessageSource().getMessage(
				"text.company.organizationManagement", null, getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("/my-company/organization-management/manage-user", getMessageSource().getMessage(
				"text.company.manageUsers", null, getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		return REDIRECT_TO_USER_SEARCH_PAGE;
	}



	public String editUser(final String user, final Model model) throws CMSItemNotFoundException, BusinessException
	{
		final JnjGTCustomerData customerData = jnjGTCustomerFacade.getCustomerForUid(user);
		//Soumitra - setting allowedFranchise for the existing users. AAOL-4913
		if(CollectionUtils.isNotEmpty(customerData.getAllowedFranchise()))
		{
			//get allowed franchise from current user model.
			model.addAttribute("allowedFranchise", customerData.getAllowedFranchise());
		}
		else
		{
			//if allowedFranchise is null then set allFranchise as allowedFranchise.
			model.addAttribute("allowedFranchise", jnjGTCustomerFacade.getAllFranchise());
		}
		//Soumitra - Set all franchise available in the portal - AAOL-4913
		model.addAttribute("allFranchise", jnjGTCustomerFacade.getAllFranchise());
		
		final JnjGTB2BCustomerForm b2bCustomerForm = new JnjGTB2BCustomerForm();
		b2bCustomerForm.setUid(customerData.getUid());
		b2bCustomerForm.setCsrNotes(customerData.getCsrNotes());
		b2bCustomerForm.setFirstName(customerData.getFirstName());
		b2bCustomerForm.setLastName(customerData.getLastName());
		b2bCustomerForm.setOrgName(customerData.getOrgName());
		b2bCustomerForm.setDepartment(customerData.getDepartment());
		if(null!=customerData.getFinancialAnalysisEnable())
		{
		b2bCustomerForm.setFinancialEnable(customerData.getFinancialAnalysisEnable());
		}
		else
		{
			b2bCustomerForm.setFinancialEnable(false);
		}
		 //AAOL-3112 changed to fix null pointer for existing users where consignmenOrderEntry is na
		if(null!=customerData.getConsignmentEntryOrder())
		{
			b2bCustomerForm.setConsignmentEntryOrder(customerData.getConsignmentEntryOrder());
		}
		else
		{
			b2bCustomerForm.setConsignmentEntryOrder(false);
		}
		 //AAOL-3112

		if (customerData.getNoCharge() != null)
		{
			b2bCustomerForm.setNoCharge(customerData.getNoCharge().booleanValue());
		}
		if (customerData.getMddSector() != null)
		{
			b2bCustomerForm.setMdd(customerData.getMddSector().booleanValue());
		}
		if (customerData.getConsumerSector() != null)
		{
			b2bCustomerForm.setConsumer(customerData.getConsumerSector().booleanValue());
		}
		if (customerData.getPharmaSector() != null)
		{
			b2bCustomerForm.setPharma(customerData.getPharmaSector().booleanValue());
		}
		b2bCustomerForm.setEmail(customerData.getEmail().toLowerCase());
		final Collection<String> roles = customerData.getRoles();
		b2bCustomerForm.setRoles(roles); // multiple roles using to make check default while view/edit
		for (final String role : roles)
		{
			if (!Jnjb2bCoreConstants.B2BUnit.B2BCUSTOMERGROUP.equals(role))
			{
				b2bCustomerForm.setRole(role);
			}
		}
		final List<B2BUnitData> b2bUnits = customerData.getB2bUnits();
		if (CollectionUtils.isNotEmpty(b2bUnits))
		{
			String hiddenAccountsString = "";
			String selectedAccountsString = "";
			String hiddenAccountNamesList = "";
			for (final B2BUnitData b2bUnitData : b2bUnits)
			{
				hiddenAccountsString = hiddenAccountsString + b2bUnitData.getUid() + ",";
				selectedAccountsString = selectedAccountsString + b2bUnitData.getUid() + "(" + b2bUnitData.getName() + "), ";
				hiddenAccountNamesList = hiddenAccountNamesList + b2bUnitData.getUid() + "**" + b2bUnitData.getName() + "|";
			}
			hiddenAccountsString = hiddenAccountsString.substring(0, hiddenAccountsString.lastIndexOf(","));
			selectedAccountsString = selectedAccountsString.substring(0, selectedAccountsString.lastIndexOf(","));
			hiddenAccountNamesList = hiddenAccountNamesList.substring(0, hiddenAccountNamesList.lastIndexOf("|"));

			model.addAttribute("selectedAccountsString", selectedAccountsString);
			model.addAttribute("hiddenAccountNamesList", hiddenAccountNamesList);
			b2bCustomerForm.setGroups(hiddenAccountsString); //editing user group account
		}
		//start for multiple accounts
		/** Calling facade layer **/
		/** If true is passed in getAccountsMap(true) to maintain the current B2B Unit from the map for logged in user**/
		Map<String, String> loggedUserAccountData = jnjGTCustomerFacade.getAccountsMap(true);  
		model.addAttribute("loggedUserAccountList", loggedUserAccountData.keySet()); // login user account group list
		
		/** Calling facade layer **/
		final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);
		final int finalPageSize = (Boolean.valueOf("false")) ? pageSizeFromConfig * Integer.valueOf("1")
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(0, finalPageSize, JnJB2BUnitModel.UID, ShowMode.Page); 

		final JnjGTAccountSelectionData allAccountSelectionData = jnjGTCustomerFacade.getAccountsMap(true, false, false, pageableData);
		model.addAttribute("allAccountSelectionList", allAccountSelectionData.getAccountsMap()); // All account group list from 'JnjGlobalUnit'
		
		//end for multiple accounts
		b2bCustomerForm.setSupervisorName(customerData.getSupervisorName());
		//For new Req for language
		b2bCustomerForm.setLanguage(customerData.getLanguage().getIsocode());
		final Date passwordChangeDate = customerData.getPasswordChangeDate();

		if (passwordChangeDate != null)
		{
			b2bCustomerForm.setPasswordChangeDate(JnJCommonUtil.formatDate(passwordChangeDate,
					Jnjb2bCoreConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT));
			final String pwdExpiryDays = JnJCommonUtil.getValue(Jnjb2bCoreConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_KEY);
			try
			{
				final int pwdExpDays = Integer.parseInt(pwdExpiryDays);
				final String pwdExpiryDate = JnjGTCoreUtil.convertDateFormat(b2bCustomerForm.getPasswordChangeDate(),
						Jnjb2bCoreConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT,
						Jnjb2bCoreConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT, pwdExpDays);
				b2bCustomerForm.setPasswordExpiryDate(pwdExpiryDate);
			}
			catch (final NumberFormatException e)
			{
				LOG.error(e.getMessage());
			}

		}
		if (customerData.getSupervisorPhone() != null)
		{
			b2bCustomerForm.setSupervisorPhonePrefix(customerData.getSupervisorPhoneCode());
			b2bCustomerForm.setSupervisorPhone(customerData.getSupervisorPhone());
		}
		b2bCustomerForm.setSupervisorEmail(customerData.getSupervisorEmail());
		b2bCustomerForm.setWwid(customerData.getWwid());
		b2bCustomerForm.setDivision(customerData.getDivison());
		final AddressData contactAddress = customerData.getContactAddress();
		if (contactAddress != null)
		{
			final JnjGTAddressForm contactAddressForm = new JnjGTAddressForm();
			contactAddressForm.setLine1(contactAddress.getLine1());
			contactAddressForm.setLine2(contactAddress.getLine2());
			if (contactAddress.getCountry() != null)
			{
				contactAddressForm.setCountryIso(contactAddress.getCountry().getIsocode());
				model.addAttribute("states", jnjGTCustomerFacade.getRegions(contactAddressForm.getCountryIso()));
			}
			contactAddressForm.setTownCity(contactAddress.getTown());
			contactAddressForm.setPostcode(contactAddress.getPostalCode());
			contactAddress.setPhone(contactAddress.getPhone());
			if (contactAddress instanceof JnjGTAddressData)
			{
				final JnjGTAddressData jnjGTContactAddress = (JnjGTAddressData) contactAddress;

				contactAddressForm.setFaxNumberPrefix(jnjGTContactAddress.getFaxCode());
				contactAddressForm.setFaxNumber(jnjGTContactAddress.getFax());
				contactAddressForm.setMobileNumberPrefix(jnjGTContactAddress.getMobileCode());
				contactAddressForm.setMobileNumber(jnjGTContactAddress.getMobile());

				b2bCustomerForm.setPhone(jnjGTContactAddress.getPhone());
				b2bCustomerForm.setPhoneNumberPrefix(jnjGTContactAddress.getPhoneCode());
				if (contactAddress.getRegion() != null)
				{
					contactAddressForm.setState(jnjGTContactAddress.getRegion().getIsocode());
				}
				else
				{
					contactAddressForm.setState(jnjGTContactAddress.getState());
				}
			}
			b2bCustomerForm.setContactAddress(contactAddressForm);
		}

		if (StringUtils.isNotEmpty(customerData.getAccessBy()))
		{
			b2bCustomerForm.setRadio1(AccessBy.valueOf(customerData.getAccessBy()).name());
		}
		else
		{
			b2bCustomerForm.setRadio1(AccessBy.NOT_SALES_REP.name());
		}
		List<String> territoryDivList = new ArrayList<String>();
		territoryDivList = sessionService.getAttribute("territoryDivisonList");
		if (CollectionUtils.isNotEmpty(territoryDivList))
		{
			b2bCustomerForm.setTerritorDivsion(territoryDivList);
			sessionService.removeAttribute("territoryDivisonList");
		}
		else
		{
			if (CollectionUtils.isNotEmpty(customerData.getTerritoryDiv()))
			{
				b2bCustomerForm.setTerritorDivsion(customerData.getTerritoryDiv());
			}
		}
		final String userTierType = getSessionService().getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE);
		model.addAttribute("isTier1User", Boolean.valueOf(Jnjb2bCoreConstants.UserSearch.USER_TIER1.equals(userTierType)));
		model.addAttribute("isTier2User", Boolean.valueOf(Jnjb2bCoreConstants.UserSearch.USER_TIER2.equals(userTierType)));
		if (Jnjb2bCoreConstants.UserSearch.USER_TIER1.equals(userTierType))
		{
			if ((enumerationService.getEnumerationName(CustomerStatus.ACTIVE).equalsIgnoreCase(customerData.getStatus()) || (enumerationService
					.getEnumerationName(CustomerStatus.DISABLED).equalsIgnoreCase(customerData.getStatus()))))
			{
				model.addAttribute("isStatusDisabled", Boolean.FALSE);
			}
			else
			{
				model.addAttribute("isStatusDisabled", Boolean.TRUE);
			}
		}
		if (customerData.getRoles().contains(Config.getParameter(Jnjb2bCoreConstants.Login.USER_GROUP_ADMIN)))
		{
			model.addAttribute("selfTier2", Boolean.TRUE);
		}
		else
		{
			model.addAttribute("selfTier2", Boolean.FALSE);
		}
		if (customerData.getRoles().contains(Config.getParameter(Jnjb2bCoreConstants.Login.USER_GROUP_CSR)))
		{
			model.addAttribute("selfTier1", Boolean.TRUE);
		}
		else
		{
			model.addAttribute("selfTier1", Boolean.FALSE);
		}


		final JnjGTCustomerData currentCustomerData = (JnjGTCustomerData) jnjGTCustomerFacade.getCurrentCustomer();
		if (customerData.getRoles().contains(Config.getParameter(Jnjb2bCoreConstants.Login.USER_GROUP_ADMIN))
				&& currentCustomerData.getRoles().contains(Config.getParameter(Jnjb2bCoreConstants.Login.USER_GROUP_ADMIN))
				|| (customerData.getRoles().contains(Config.getParameter(Jnjb2bCoreConstants.Login.USER_GROUP_CSR)) && currentCustomerData
						.getRoles().contains(Config.getParameter(Jnjb2bCoreConstants.Login.USER_GROUP_CSR))))
		{
			model.addAttribute("disableAll", Boolean.TRUE);
		}
		else
		{
			model.addAttribute("disableAll", Boolean.FALSE);
		}

		b2bCustomerForm.setStatus(customerData.getStatus());
		b2bCustomerForm.setExistingStatus(customerData.getStatus());
		model.addAttribute("accessNotSalesRep", AccessBy.NOT_SALES_REP.name());
		model.addAttribute("accessWWID", AccessBy.WWID.name());
		model.addAttribute("accessTerritories", AccessBy.TERRITORIES.name());
		model.addAttribute("existingEmail", b2bCustomerForm.getEmail().toLowerCase());
		model.addAttribute("jnjGTB2BCustomerForm", b2bCustomerForm);
		setRolesAndDivisionsInModel(model);
		model.addAttribute("secreteQuestionList", jnjGTCustomerFacade.getSecretQuestionsForUser(user));
		model.addAttribute("countries", jnjGTCustomerFacade.getCountries());
		final List<CustomerStatus> statusesEnum = enumerationService.getEnumerationValues(CustomerStatus.class);
		final Map<String, String> statuses = new HashMap<String, String>();
		for (final CustomerStatus status : statusesEnum)
		{
			statuses.put(status.getCode(), enumerationService.getEnumerationName(status));
		}
		model.addAttribute("statuses", statuses);
		model.addAttribute("departments", jnjGTCustomerFacade.getDepartments());
		model.addAttribute("language", customerData.getLanguage().getIsocode());
		return setUpTheEditPage(model);
	}

	/**
	 * @param model
	 * @return
	 * @throws CMSItemNotFoundException
	 * @throws BusinessException
	 */
	protected String setUpTheEditPage(final Model model) throws CMSItemNotFoundException, BusinessException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(MY_COMPANY_EDIT_USER_PROFILE_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_COMPANY_EDIT_USER_PROFILE_PAGE));
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		/*breadcrumbs.add(new Breadcrumb("/resources/usefullinks", messageFacade.getMessageTextForCode(RESOURCE_LABEL,
				getI18nService().getCurrentLocale()), null));*/
		breadcrumbs.add(new Breadcrumb("/resources/usermanagement", messageFacade.getMessageTextForCode(USER_MANAGEMENT_LABEL,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("/resources/usermanagement/edit", messageFacade.getMessageTextForCode(EDIT_PROFILE_LABEL,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.MyCompanyManageUserEditPage);
	}


	protected String editUser(final String user, final JnjGTB2BCustomerForm b2BCustomerForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel, final HttpServletRequest request)
			throws CMSItemNotFoundException, BusinessException
	{
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			model.addAttribute(b2BCustomerForm);
			return editUser(b2BCustomerForm.getUid(), model);
		}
		final JnjGTCustomerData b2bCustomerData = new JnjGTCustomerData();
		final JnjGTAddressForm contactAddresForm = b2BCustomerForm.getContactAddress();
		b2bCustomerData.setUid(b2BCustomerForm.getUid());
		populateBasicCustomerData(b2BCustomerForm, b2bCustomerData);
		b2bCustomerData.setCsrNotes(b2BCustomerForm.getCsrNotes());
		b2bCustomerData.setStatus(b2BCustomerForm.getStatus());
		b2bCustomerData.setConsignmentEntryOrder(b2BCustomerForm.isConsignmentEntryOrder()); //AAOL-3112
		//added for AAOL-2422
		b2bCustomerData.setFinancialAnalysisEnable(b2BCustomerForm.isFinancialEnable());
		//ended for AAOL-2422
		final String userTierType = getSessionService().getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE);
		if (Jnjb2bCoreConstants.UserSearch.USER_TIER2.equals(userTierType))
		{
			final List<String> roles = new ArrayList<String>();
			roles.add(b2BCustomerForm.getRole());
			roles.add(B2BCUSTOMERGROUP);
			b2bCustomerData.setRoles(roles);
		}
		//added for AAOL-2422
		if(b2BCustomerForm.isFinancialEnable()){
			final List<String> groups = new ArrayList<String>();
			groups.addAll(b2bCustomerData.getRoles());
			groups.add(Config.getParameter(USER_FINANCIAL_GROUP));
			b2bCustomerData.setRoles(groups);
			}
		//ended for AAOL-2422
		
		if (b2BCustomerForm.getStatus() != null)
		{


			if (b2BCustomerForm.getStatus().equalsIgnoreCase(enumerationService.getEnumerationName(CustomerStatus.DISABLED)))
			{
				b2bCustomerData.setLoginDisabledFlag(Boolean.TRUE);
			}
			else if (b2BCustomerForm.getStatus().equalsIgnoreCase(enumerationService.getEnumerationName(CustomerStatus.ACTIVE)))
			{
				b2bCustomerData.setLoginDisabledFlag(Boolean.FALSE);
			}

		}
		b2bCustomerData.setOrgName(b2BCustomerForm.getOrgName());
		b2bCustomerData.setDepartment(b2BCustomerForm.getDepartment());
		if (Jnjb2bCoreConstants.UserSearch.USER_TIER2.equals(userTierType))
		{
			b2bCustomerData.setNoCharge(Boolean.valueOf(b2BCustomerForm.isNoCharge()));
			b2bCustomerData.setMddSector(Boolean.valueOf(b2BCustomerForm.isMdd()));
			b2bCustomerData.setConsumerSector(Boolean.valueOf(b2BCustomerForm.isConsumer()));
			b2bCustomerData.setPharmaSector(Boolean.valueOf(b2BCustomerForm.isPharma()));
		}


		if (contactAddresForm != null)
		{
			final JnjGTAddressData contactAddressData = new JnjGTAddressData();
			contactAddressData.setLine1(contactAddresForm.getLine1());
			contactAddressData.setLine2(contactAddresForm.getLine2());
			final CountryData countryData = new CountryData();
			countryData.setIsocode(contactAddresForm.getCountryIso());

			if (StringUtils.equalsIgnoreCase("us", contactAddresForm.getCountryIso()))
			{
				final RegionData regionData = new RegionData();
				regionData.setIsocode(contactAddresForm.getState());
				regionData.setCountryIso(contactAddresForm.getCountryIso());
				contactAddressData.setRegion(regionData);
			}
			else
			{
				contactAddressData.setState(contactAddresForm.getState());
			}
			contactAddressData.setCountry(countryData);
			contactAddressData.setTown(contactAddresForm.getTownCity());
			contactAddressData.setPostalCode(contactAddresForm.getPostcode());
			contactAddressData.setPhone(b2BCustomerForm.getPhoneNumberPrefix() + Jnjb2bCoreConstants.SYMBOl_PIPE
					+ JnjGTCoreUtil.getFormattedPhoneNumber(b2BCustomerForm.getPhone()));
			contactAddressData.setMobile(contactAddresForm.getMobileNumberPrefix() + Jnjb2bCoreConstants.SYMBOl_PIPE
					+ JnjGTCoreUtil.getFormattedPhoneNumber(contactAddresForm.getMobileNumber()));
			contactAddressData.setFax(contactAddresForm.getFaxNumberPrefix() + Jnjb2bCoreConstants.SYMBOl_PIPE
					+ JnjGTCoreUtil.getFormattedPhoneNumber(contactAddresForm.getFaxNumber()));
			contactAddressData.setDefaultAddress(true);
			b2bCustomerData.setContactAddress(contactAddressData);
		}
		if (StringUtils.isNotEmpty(b2BCustomerForm.getGroups())){
			b2bCustomerData.setGroups(Arrays.asList(b2BCustomerForm.getGroups().split(Jnjb2bCoreConstants.CONST_COMMA)));
		}
		//Soumitra - Set the franchise to which the customer has access to AAOL-4913
		if(StringUtils.isNotEmpty(b2BCustomerForm.getAllowedFranchise()))
		{
			b2bCustomerData.setAllowedFranchise(getCategoryDataList(Arrays.asList(b2BCustomerForm.getAllowedFranchise().split(Jnjb2bCoreConstants.CONST_COMMA))));
		}
		//b2bCustomerData.setGroups(Arrays.asList(b2BCustomerForm.getGroups().split(Jnjb2bCoreConstants.CONST_COMMA)));
		if (StringUtils.isNotEmpty(b2BCustomerForm.getRadio1()))
		{
			b2bCustomerData.setAccessBy(b2BCustomerForm.getRadio1());
			if (StringUtils.equalsIgnoreCase(b2BCustomerForm.getRadio1(), AccessBy.NOT_SALES_REP.name()))
			{
				if (StringUtils.isNotEmpty(b2BCustomerForm.getGroups()))
				{
					b2bCustomerData.setGroups(Arrays.asList(b2BCustomerForm.getGroups().split(Jnjb2bCoreConstants.CONST_COMMA)));
				}
			}
			else if (StringUtils.equalsIgnoreCase(b2BCustomerForm.getRadio1(), AccessBy.WWID.name()))
			{
				b2bCustomerData.setDivison(b2BCustomerForm.getDivision());
			}
			else if (StringUtils.equalsIgnoreCase(b2BCustomerForm.getRadio1(), AccessBy.TERRITORIES.name()))
			{
				final List<String> territoryDivisonList = new ArrayList<>();
				final List<String> territoryDivisonDataList = new ArrayList<>();

				final String[] franchiseDivisions = b2BCustomerForm.getFranchiseDivisions().split(Jnjb2bCoreConstants.SYMBOl_COMMA);
				final List<String> territories = Arrays.asList(b2BCustomerForm.getTerritories().split(
						Jnjb2bCoreConstants.SYMBOl_COMMA));
				int i = 0;
				for (final String territory : territories)
				{
					if (i < franchiseDivisions.length)
					{
						territoryDivisonList.add(territory + Jnjb2bCoreConstants.SYMBOl_PIPE + franchiseDivisions[i]);
						i++;
					}
					else
					{
						territoryDivisonList.add(territory + Jnjb2bCoreConstants.SYMBOl_PIPE + Jnjb2bCoreConstants.EMPTY_STRING);
					}
				}

				for (final String territoryDiv : territoryDivisonList)
				{
					JnjGTTerritoryDivisonModel territoryModel = new JnjGTTerritoryDivisonModel();
					territoryModel.setUid(territoryDiv);
					territoryModel = jnjGTTerritoryFacade.getTerritoryDivisonModel(territoryModel);
					if (territoryModel == null)
					{
						sessionService.setAttribute("territoryDivisonList", territoryDivisonList);
						model.addAttribute(INVALID_TERRITORY, Boolean.valueOf(true));
						model.addAttribute("b2BCustomerForm", b2BCustomerForm);
						return editUser(b2BCustomerForm.getUid(), model);
					}
					else
					{
						territoryDivisonDataList.add(territoryDiv);
					}
				}
				b2bCustomerData.setTerritoryDiv(territoryDivisonDataList);

			}
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserDetailsBreadcrumb(user);
		model.addAttribute("breadcrumbs", breadcrumbs);
		try
		{
			jnjGTCustomerFacade.updateCustomer(b2bCustomerData);
			sentAppropriateMail(b2BCustomerForm, request);
		}
		catch (final DuplicateUidException e)
		{
			bindingResult.rejectValue("email", "text.manageuser.error.email.exists.title");
			GlobalMessages.addErrorMessage(model, "form.global.error");
			model.addAttribute("b2BCustomerForm", b2BCustomerForm);
			return editUser(b2BCustomerForm.getUid(), model);

		}
		redirectModel.addFlashAttribute(SUCCESS, Boolean.TRUE);
		return REDIRECT_TO_USER_SEARCH_PAGE;
	}

	protected String manageUserDetail(final String user, final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{ 
		final CustomerData customerData = b2bUserFacade.getCustomerForUid(user);
		model.addAttribute("customerData", customerData);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserDetailsBreadcrumb(user);
		model.addAttribute("breadcrumbs", breadcrumbs);

		if (!customerData.getUnit().isActive())
		{
			GlobalMessages.addInfoMessage(model, "text.parentunit.disabled.warning");
		}
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MyCompany.MyCompanyManageUserDetailPage;
	}


	/**
	 * Data class used to hold a drop down select option value. Holds the code identifier as well as the display name.
	 */
	public static class SelectOption
	{
		private final String code;
		private final String name;

		public SelectOption(final String code, final String name)
		{
			this.code = code;
			this.name = name;
		}

		public String getCode()
		{
			return code;
		}

		public String getName()
		{
			return name;
		}
	}

	protected B2BCostCenterData populateB2BCostCenterDataFromForm(final B2BCostCenterForm b2BCostCenterForm)
	{
		final B2BCostCenterData b2BCostCenterData = new B2BCostCenterData();
		b2BCostCenterData.setOriginalCode(b2BCostCenterForm.getOriginalCode());
		b2BCostCenterData.setCode(b2BCostCenterForm.getCode());
		b2BCostCenterData.setName(b2BCostCenterForm.getName());
		final CurrencyData currencyData = new CurrencyData();
		currencyData.setIsocode(b2BCostCenterForm.getCurrency());
		b2BCostCenterData.setCurrency(currencyData);
		b2BCostCenterData.setUnit(companyB2BCommerceFacade.getUnitForUid(b2BCostCenterForm.getParentB2BUnit()));

		return b2BCostCenterData;
	}

	protected B2BBudgetData populateB2BBudgetDataFromForm(final B2BBudgetForm b2BBudgetForm) throws ParseException
	{
		final B2BBudgetData b2BBudgetData = new B2BBudgetData();
		b2BBudgetData.setOriginalCode(b2BBudgetForm.getOriginalCode());
		b2BBudgetData.setCode(b2BBudgetForm.getCode());
		b2BBudgetData.setName(b2BBudgetForm.getName());
		b2BBudgetData.setUnit(companyB2BCommerceFacade.getUnitForUid(b2BBudgetForm.getParentB2BUnit()));
		final CurrencyData currencyData = new CurrencyData();
		currencyData.setIsocode(b2BBudgetForm.getCurrency());
		b2BBudgetData.setCurrency(currencyData);
		b2BBudgetData.setStartDate(b2BBudgetForm.getStartDate());
		b2BBudgetData.setEndDate(b2BBudgetForm.getEndDate());
		b2BBudgetData.setBudget(BigDecimal.valueOf(formatFactory.createNumberFormat().parse(b2BBudgetForm.getBudget())
				.doubleValue()));

		return b2BBudgetData;
	}



	protected boolean checkEndDateIsBeforeStartDateForBudget(final B2BBudgetForm b2BBudgetForm)
	{
		final Date startDate = b2BBudgetForm.getStartDate();
		final Date endDate = b2BBudgetForm.getEndDate();
		return endDate.before(startDate);
	}

	protected List<SelectOption> populateSelectBoxForString(final List<String> listOfDatas)
	{
		final List<SelectOption> selectBoxList = new ArrayList<SelectOption>();
		for (final String data : listOfDatas)
		{
			selectBoxList.add(new SelectOption(data, data));
		}

		return selectBoxList;
	}

	protected List<SelectOption> populateRolesCheckBoxes(final List<String> roles)
	{
		final List<SelectOption> selectBoxList = new ArrayList<SelectOption>();
		for (final String data : roles)
		{
			//			selectBoxList.add(new SelectOption(data, getMessageSource().getMessage(String.format("b2busergroup.%s.name", data),
			//					null, getI18nService().getCurrentLocale())));

			selectBoxList.add(new SelectOption(data, data));
		}

		return selectBoxList;
	}

	protected List<SelectOption> getBranchSelectOptions(final List<B2BUnitNodeData> branchNodes)
	{
		final List<SelectOption> selectOptions = new ArrayList<SelectOption>();

		for (final B2BUnitNodeData b2bUnitNode : branchNodes)
		{
			if (b2bUnitNode.isActive())
			{
				selectOptions.add(new SelectOption(b2bUnitNode.getId(), b2bUnitNode.getName()));
			}
		}

		return selectOptions;
	}

	protected String urlEncode(final String url)
	{
		try
		{
			return URLEncoder.encode(url, "UTF-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			return url;
		}
	}

	protected B2BSelectionData populateDisplayNamesForRoles(final B2BSelectionData b2BSelectionData)
	{
		final List<String> roles = b2BSelectionData.getRoles();
		final List<String> displayRoles = new ArrayList<String>(roles.size());
		for (final String role : roles)
		{
			displayRoles.add(getMessageSource().getMessage("b2busergroup." + role + ".name", null, role,
					getI18nService().getCurrentLocale()));
		}
		b2BSelectionData.setDisplayRoles(displayRoles);
		return b2BSelectionData;
	}

	protected void setRolesAndDivisionsInModel(final Model model)
	{
		model.addAttribute("roles",
				Arrays.asList(Config.getParameter(Jnjb2bCoreConstants.Register.VIEW_ORDER_BUYER_USER_ROLE),
								Config.getParameter(Jnjb2bCoreConstants.Register.PLACE_ORDER_BUYER_USER_ROLE),
								Config.getParameter(Jnjb2bCoreConstants.Register.USER_GROUP_NO_CHARGE_USERS))); //AAOL-2429 and AAOL-2433
								
		model.addAttribute("divisionCodes", jnjGTCustomerFacade.getDivisions());
		model.addAttribute("territoryCodes", jnjGTCustomerFacade.getTerritories());
		model.addAttribute("consumerDivisonCodes", jnjGTCustomerFacade.getConsumerDivisons());
	}

	protected void populateBasicCustomerData(final B2BCustomerForm b2BCustomerForm, final JnjGTCustomerData b2bCustomerData)
	{
		b2bCustomerData.setFirstName(b2BCustomerForm.getFirstName());
		b2bCustomerData.setLastName(b2BCustomerForm.getLastName());
		b2bCustomerData.setEmail(b2BCustomerForm.getEmail().toLowerCase());
		if (b2BCustomerForm instanceof JnjGTB2BCustomerForm)
		{
			final JnjGTB2BCustomerForm jnjGTB2BCustomerForm = (JnjGTB2BCustomerForm) b2BCustomerForm;
			final JnjGTAddressData addressData = new JnjGTAddressData();
			addressData.setPhone(jnjGTB2BCustomerForm.getPhoneNumberPrefix() + Jnjb2bCoreConstants.SYMBOl_PIPE
					+ JnjGTCoreUtil.getFormattedPhoneNumber(jnjGTB2BCustomerForm.getPhone()));
			addressData.setDefaultAddress(true);
			b2bCustomerData.setContactAddress(addressData);
			b2bCustomerData.setSupervisorName(jnjGTB2BCustomerForm.getSupervisorName());
			b2bCustomerData.setSupervisorEmail(jnjGTB2BCustomerForm.getSupervisorEmail());
			b2bCustomerData.setSupervisorPhoneCode(jnjGTB2BCustomerForm.getSupervisorPhonePrefix());
			//5491
			b2bCustomerData.setNoCharge(Boolean.valueOf(jnjGTB2BCustomerForm.isNoCharge()));
			//b2bCustomerData.setSupervisorPhone(JnjGTCoreUtil.getFormattedPhoneNumber(jnjNAB2BCustomerForm.getSupervisorPhone()));
			b2bCustomerData.setSupervisorPhone(JnjGTCoreUtil.getFormattedPhoneNumber(jnjGTB2BCustomerForm.getSupervisorPhonePrefix()
					+ Jnjb2bCoreConstants.SYMBOl_PIPE+JnjGTCoreUtil.getFormattedPhoneNumber(jnjGTB2BCustomerForm.getSupervisorPhone())));
			//added for new Req
			LanguageData languageData = new LanguageData();
			languageData.setIsocode(((JnjGTB2BCustomerForm) b2BCustomerForm).getLanguage());
			b2bCustomerData.setLanguage(languageData);
			if (StringUtils.isNotEmpty(jnjGTB2BCustomerForm.getWwid()))
			{
				b2bCustomerData.setWwid(jnjGTB2BCustomerForm.getWwid());
			}
			b2bCustomerData.setConsignmentEntryOrder(jnjGTB2BCustomerForm.isConsignmentEntryOrder()); //AAOL-3112
			
			//Soumitra - Set the franchise to which the customer has access to AAOL-4913
			if(StringUtils.isNotEmpty(jnjGTB2BCustomerForm.getAllowedFranchise()))
			{
				b2bCustomerData.setAllowedFranchise(getCategoryDataList(Arrays.asList(jnjGTB2BCustomerForm.getAllowedFranchise().split(Jnjb2bCoreConstants.CONST_COMMA))));
			}
		}
	}

	public void sentAppropriateMail(final JnjGTB2BCustomerForm customerForm, final HttpServletRequest request)
	{
		//
		if (StringUtils.isNotEmpty(customerForm.getStatus()))
		{

			final String siteUrl = JnjWebUtil.getServerUrl(request);
			final String logoUrl = JnjWebUtil.getUnsecureServerUrl(request);

			if (customerForm.getStatus().equalsIgnoreCase(CustomerStatus.ACTIVE.getCode()))
			{
				if (enumerationService.getEnumerationName(CustomerStatus.PENDING_ACCOUNT_SETUP).equalsIgnoreCase(
						customerForm.getExistingStatus())
						|| enumerationService.getEnumerationName(CustomerStatus.PENDING_PROFILE_SETUP).equalsIgnoreCase(
								customerForm.getExistingStatus())
						|| enumerationService.getEnumerationName(CustomerStatus.PENDING_SUPERVISOR_RESPONSE).equalsIgnoreCase(
								customerForm.getExistingStatus()))
				{
					try
					{
						jnjGTB2BCommerceUserFacade.sentApprovedProfileEmail(logoUrl, customerForm.getUid(), siteUrl);
					}
					catch (final DuplicateUidException e)
					{
						e.printStackTrace();
					}
					catch (final UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
				}
			}

			else if (CustomerStatus.REJECTED.getCode().equalsIgnoreCase(customerForm.getStatus()))
			{
				if (!customerForm.getExistingStatus()
						.equalsIgnoreCase(enumerationService.getEnumerationName(CustomerStatus.REJECTED)))
				{
					jnjGTB2BCommerceUserFacade.sentRejectionEmail(logoUrl, customerForm.getUid(), siteUrl);
				}
			}
		}
	}
	protected JnjGTPageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final JnjGTPageableData pageableData = new JnjGTPageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);
		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}
	public void sentAppropriateMailWithUserinformation(final JnjGTB2BCustomerForm customerForm, final HttpServletRequest request)
	{
			final String siteUrl = JnjWebUtil.getServerUrl(request);
			final String logoUrl = JnjWebUtil.getUnsecureServerUrl(request);
			try
			{
				jnjGTB2BCommerceUserFacade.sentCreateProfileEmail(logoUrl, customerForm.getUid(), siteUrl);
 }
			catch (final DuplicateUidException e)
			{
				e.printStackTrace();
			}
			catch (final UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
	}
	public void sentTemporaryEmail(final JnjGTB2BCustomerForm customerForm, final HttpServletRequest request)
	{
			final String siteUrl = JnjWebUtil.getServerUrl(request);
			final String logoUrl = JnjWebUtil.getUnsecureServerUrl(request);
			try
		{
				jnjGTB2BCommerceUserFacade.sentTemporaryPasswordEmail(logoUrl, customerForm.getUid(), siteUrl);
		}
			catch (final DuplicateUidException e)
			{
				e.printStackTrace();
			}
			catch (final UnsupportedEncodingException e)
		{
				e.printStackTrace();
		}
	}
	public String getView(final String view){
        return JnjglobalresourcesControllerConstants.ADDON_PREFIX + view;
	}
	
	//Soumitra - Util method to fetch and return list of CategoryModel for a given list of category Codes AAOL-4913
	protected List<CategoryData> getCategoryDataList(List<String> codes) {
		List<CategoryData> categories = new ArrayList<>();
		for (String code : codes) {
			categories.add(jnjGTCustomerFacade.getCategoryForCode(code));
		}
		return categories;
	}
}
