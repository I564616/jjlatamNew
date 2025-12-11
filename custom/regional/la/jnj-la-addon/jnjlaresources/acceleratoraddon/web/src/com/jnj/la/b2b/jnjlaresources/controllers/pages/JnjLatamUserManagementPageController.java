/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.b2b.jnjlaresources.controllers.pages;

import com.jnj.b2b.jnjglobalresources.controllers.JnjglobalresourcesControllerConstants;
import com.jnj.b2b.jnjglobalresources.controllers.pages.JnjGTUserManagementPageController;
import com.jnj.b2b.jnjglobalresources.form.JnjGTAddressForm;
import com.jnj.b2b.jnjglobalresources.form.JnjGTB2BCustomerForm;
import com.jnj.b2b.jnjglobalresources.form.JnjGTUserSearchForm;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.enums.AccessBy;
import com.jnj.core.enums.CustomerStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.company.impl.JnjLatamB2BCommerceUserFacadeImpl;
import com.jnj.facades.customer.impl.JnjLatamCustomerFacadeImpl;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.b2b.jnjlaresources.controllers.JnjlaresourcesControllerConstants;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JnjLatamUserManagementPageController extends JnjGTUserManagementPageController {

    private static final Logger LOG = Logger.getLogger(JnjLatamUserManagementPageController.class);

    private static final String BREADCRUMBS = "breadcrumbs";
    private static final String STATUS_CHANGE = "statuschange";
    private static final String USER_SEARCH_FORM = "userSearchForm";
    private static final String RESOURCES_USER_MANAGEMENT = "/resources/usermanagement";
    private static final String B2B_CUSTOMER_GROUP = "b2bcustomergroup";
    private static final String SEARCH_USER_FORM = "searchUserForm";
    private static final String FORM_GLOBAL_ERROR = "form.global.error";
    private static final String FORM_NAME = "jnjGTB2BCustomerForm";

    @Autowired
    private JnjLatamCustomerFacadeImpl jnjLatamCustomerFacadeImpl;

    @Autowired
    private JnjLatamB2BCommerceUserFacadeImpl jnjLatamB2BCommerceUserFacadeImpl;

    @Autowired
    private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private CMSSiteService cmsSiteService;

    @Override
    public String createUser(final Model model, final JnjGTUserSearchForm userSearchForm) throws CMSItemNotFoundException, BusinessException {

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        if (userHasNoRights()) {
            return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
        }

        model.addAttribute(SELECTED_LINK, Jnjb2bCoreConstants.Resources.USER_MANAGEMENT_LINK_COMPONENT_ID);
        model.addAttribute("action", "manageUsers");
        sessionService.setAttribute(USER_SEARCH_FORM, userSearchForm);

        final JnjGTB2BCustomerForm jnjGTB2BCustomerForm = new JnjGTB2BCustomerForm();
        model.addAttribute(FORM_NAME, jnjGTB2BCustomerForm);
        model.addAttribute("pageTitle", "Create User Page");
        setRolesAndDivisionsInModel(model);
        storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));

        final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
        breadcrumbs.add(new Breadcrumb(RESOURCES_USER_MANAGEMENT, messageFacade.getMessageTextForCode(USER_MANAGEMENT_LABEL, getI18nService().getCurrentLocale()), null));
        breadcrumbs.add(new Breadcrumb("/resources/usermanagement/create", messageFacade.getMessageTextForCode(CREATE_NEW_PROFILE_LABEL, getI18nService().getCurrentLocale()), null));

        final Map<String, String> loggedUserAccountData = jnjGTCustomerFacade.getAccountsMap(true);
        model.addAttribute("loggedUserAccountList", loggedUserAccountData.keySet());

        populateAllAccountsAndRoles(model);

        final JnJB2BUnitModel currentB2bUnit = sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
        if (null != currentB2bUnit) {
            model.addAttribute("currentB2bUnit", currentB2bUnit.getUid());
        }
        model.addAttribute(BREADCRUMBS, breadcrumbs);
        return getView(JnjlaresourcesControllerConstants.Views.Pages.MyCompany.MyCompanyManageUserAddEditFormPage);

    }

    @Override
    public String editUser(@RequestParam("user") final String user, final Model model, final JnjGTUserSearchForm userSearchForm) throws CMSItemNotFoundException, BusinessException {
        sessionService.setAttribute(USER_SEARCH_FORM, userSearchForm);

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        checkChangeAccountStatus(model);

        populateAllAccountsAndRoles(model);

        if (userHasNoRights()) {
            return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
        }

        model.addAttribute(SELECTED_LINK, Jnjb2bCoreConstants.Resources.USER_MANAGEMENT_LINK_COMPONENT_ID);

        final JnjGTCustomerData customerData = jnjGTCustomerFacade.getCustomerForUid(user);
        final JnjGTB2BCustomerForm b2bCustomerForm = new JnjGTB2BCustomerForm();
        b2bCustomerForm.setUid(customerData.getUid());
        b2bCustomerForm.setFirstName(customerData.getFirstName());
        b2bCustomerForm.setLastName(customerData.getLastName());

        defineSector(customerData, b2bCustomerForm);

        b2bCustomerForm.setEmail(customerData.getEmail().toLowerCase());

        defineRoles(customerData, b2bCustomerForm);
        defineAccountsList(model, customerData, b2bCustomerForm);

        b2bCustomerForm.setLanguage(customerData.getLanguage().getIsocode());

        definePasswordDates(customerData, b2bCustomerForm);
        defineSupervisorData(customerData, b2bCustomerForm);
        defineAddressData(customerData, b2bCustomerForm);
        defineTierInformation(model, customerData);

        b2bCustomerForm.setStatus(customerData.getStatus());
        b2bCustomerForm.setExistingStatus(customerData.getStatus());
        model.addAttribute("existingEmail", b2bCustomerForm.getEmail().toLowerCase());
        model.addAttribute(FORM_NAME, b2bCustomerForm);
        setRolesAndDivisionsInModel(model);
        model.addAttribute("secreteQuestionList", jnjGTCustomerFacade.getSecretQuestionsForUser(user));
        model.addAttribute("statuses", getStatuses());
        model.addAttribute("language", customerData.getLanguage().getIsocode());
        storeCmsPageInModel(model, getContentPageForLabelOrId(MY_COMPANY_EDIT_USER_PROFILE_PAGE));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_COMPANY_EDIT_USER_PROFILE_PAGE));

        final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
        breadcrumbs.add(new Breadcrumb(RESOURCES_USER_MANAGEMENT, messageFacade.getMessageTextForCode(USER_MANAGEMENT_LABEL, getI18nService().getCurrentLocale()), null));
        breadcrumbs.add(new Breadcrumb("/resources/usermanagement/edit", messageFacade.getMessageTextForCode(EDIT_PROFILE_LABEL, getI18nService().getCurrentLocale()), null));
        model.addAttribute(BREADCRUMBS, breadcrumbs);
        return getView(JnjlaresourcesControllerConstants.Views.Pages.MyCompany.MyCompanyManageUserEditPage);
    }

    @Override
    public String editUser(final String user, final JnjGTB2BCustomerForm b2BCustomerForm, final BindingResult bindingResult,
                           final Model model, final RedirectAttributes redirectModel, final HttpServletRequest request)
        throws CMSItemNotFoundException, BusinessException {

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        if (userHasNoRights()) {
            return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
        }

        if (bindingResult.hasErrors()) {
            GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
            model.addAttribute(b2BCustomerForm);
            return editUser(b2BCustomerForm.getUid(), model);
        }

        final JnjGTCustomerData b2bCustomerData = new JnjGTCustomerData();
        final JnjGTAddressForm contactAddresForm = b2BCustomerForm.getContactAddress();
        b2bCustomerData.setUid(b2BCustomerForm.getUid());

        populateBasicCustomerData(b2BCustomerForm, b2bCustomerData);

        b2bCustomerData.setStatus(b2BCustomerForm.getStatus());
        b2bCustomerData.setConsignmentEntryOrder(b2BCustomerForm.isConsignmentEntryOrder());
        b2bCustomerData.setFinancialAnalysisEnable(b2BCustomerForm.isFinancialEnable());

        defineRoles(b2BCustomerForm, b2bCustomerData);
        defineLoginDisableByStatus(b2BCustomerForm, b2bCustomerData);
        defineSector(b2BCustomerForm, b2bCustomerData);
        defineAddressData(b2BCustomerForm, b2bCustomerData, contactAddresForm);

        if (StringUtils.isNotEmpty(b2BCustomerForm.getGroups())){
            b2bCustomerData.setGroups(Arrays.asList(b2BCustomerForm.getGroups().split(Jnjb2bCoreConstants.CONST_COMMA)));
        }

        storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));

        final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserDetailsBreadcrumb(user);
        model.addAttribute(BREADCRUMBS, breadcrumbs);

        try {
            jnjGTCustomerFacade.updateCustomer(b2bCustomerData);
            sentAppropriateMail(b2BCustomerForm, request);
        } catch (final DuplicateUidException e) {
            JnjGTCoreUtil.logErrorMessage("UserManagement", "editUser", "Update Customer : ", e, JnjLatamUserManagementPageController.class);
            bindingResult.rejectValue("email", "text.manageuser.error.email.exists.title");
            GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
            model.addAttribute(FORM_NAME, b2BCustomerForm);
            return editUser(b2BCustomerForm.getUid(), model);
        }

        redirectModel.addFlashAttribute(SUCCESS, Boolean.TRUE);
        return REDIRECT_TO_USER_SEARCH_PAGE;
    }

    @Override
    public String createUser(@ModelAttribute(FORM_NAME) final JnjGTB2BCustomerForm b2BCustomerForm, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectModel, final HttpServletRequest request) throws CMSItemNotFoundException, BusinessException {

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        if (userHasNoRights()) {
            return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
        }

        model.addAttribute("action", "manageUsers");

        if (bindingResult.hasErrors()) {
            GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
            model.addAttribute(b2BCustomerForm);
            return editUser(b2BCustomerForm.getUid(), model);
        }

        final JnjGTCustomerData b2bCustomerData = new JnjGTCustomerData();
        populateBasicCustomerData(b2BCustomerForm, b2bCustomerData);

        if (!StringUtils.isEmpty(b2BCustomerForm.getGroups())) {
            b2bCustomerData.setGroups(Arrays.asList((b2BCustomerForm.getGroups()).split(Jnjb2bCoreConstants.CONST_COMMA)));
        }

        defineRolesForCreate(b2BCustomerForm, b2bCustomerData);

        b2bCustomerData.setAccessBy(AccessBy.NOT_SALES_REP.name());
        b2bCustomerData.setMddSector(b2BCustomerForm.isMdd());
        b2bCustomerData.setConsumerSector(b2BCustomerForm.isConsumer());
        b2bCustomerData.setPharmaSector(b2BCustomerForm.isPharma());
        b2bCustomerData.setStatus(CustomerStatus.ACTIVE.getCode());
        b2bCustomerData.setLoginDisabledFlag(true);
        b2bCustomerData.setActive(true);

        try {
            jnjLatamCustomerFacadeImpl.createCustomer(b2bCustomerData);

            b2BCustomerForm.setUid(b2BCustomerForm.getEmail().toLowerCase());
            sentAppropriateMailWithUserinformation(b2BCustomerForm, request);
            sentTemporaryEmail(b2BCustomerForm, request);
        } catch (final DuplicateUidException e) {
            JnjGTCoreUtil.logErrorMessage("UserManagement", "createUser", "Update Customer : ", e, JnjLatamUserManagementPageController.class);
            bindingResult.rejectValue("email", "text.manageuser.error.email.exists.title");
            GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
            model.addAttribute("b2BCustomerForm", b2BCustomerForm);
            return editUser(b2BCustomerForm.getUid(), model);
        }

        storeCmsPageInModel(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORGANIZATION_MANAGEMENT_CMS_PAGE));
        final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.getBreadcrumbs(null);
        breadcrumbs.add(new Breadcrumb("/my-company/organization-management/", getMessageSource().getMessage("text.company.organizationManagement", null, getI18nService().getCurrentLocale()), null));
        breadcrumbs.add(new Breadcrumb("/my-company/organization-management/manage-user", getMessageSource().getMessage("text.company.manageUsers", null, getI18nService().getCurrentLocale()), null));
        model.addAttribute(BREADCRUMBS, breadcrumbs);
        return REDIRECT_TO_USER_SEARCH_PAGE;
    }

    @Override
    protected String manageUsersCommon(final int page, final ShowMode showMode, String sortCode, final Model model, JnjGTUserSearchForm userSearchForm) throws CMSItemNotFoundException, BusinessException {

        if (userHasNoRights()) {
            return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
        }

        final Object existsUserSearchForm = sessionService.getAttribute(USER_SEARCH_FORM);
        if (existsUserSearchForm != null) {
            userSearchForm = (JnjGTUserSearchForm) existsUserSearchForm;
            sessionService.removeAttribute(USER_SEARCH_FORM);
        }
        if (userSearchForm.getSortBy() != null) {
            sortCode = userSearchForm.getSortBy();
        }

        final int finalPageSize = (userSearchForm.isShowMore()) ? userSearchForm.getPageSize() * userSearchForm.getShowMoreCounter() : userSearchForm.getPageSize();
        final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, sortCode, showMode);
        pageableData.setSearchUserFlag(userSearchForm.isSearchFlagUserSearch());
        pageableData.setSearchDtoList(setSearchDtoList(userSearchForm));
        final SearchPageData<CustomerData> searchPageData = jnjLatamB2BCommerceUserFacadeImpl.searchCustomers(pageableData);
        populateModel(model, searchPageData, showMode);
        storeCmsPageInModel(model, getContentPageForLabelOrId(MY_COMPANY_CMS_PAGE));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_COMPANY_CMS_PAGE));
        model.addAttribute("pageSize", searchPageData.getPagination().getPageSize());
        model.addAttribute("totalResults", searchPageData.getPagination().getTotalNumberOfResults());
        final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);

        breadcrumbs.add(new Breadcrumb(RESOURCES_USER_MANAGEMENT, messageFacade.getMessageTextForCode(USER_MANAGEMENT_LABEL, getI18nService().getCurrentLocale()), null));
        model.addAttribute(SEARCH_USER_FORM, userSearchForm);
        model.addAttribute(BREADCRUMBS, breadcrumbs);
        model.addAttribute("metaRobots", "no-index,no-follow");

        if (userSearchForm.isSearchFlagUserSearch()) {
            model.addAttribute(SEARCH_USER_FORM, userSearchForm);
        } else {
            final JnjGTUserSearchForm newForm = new JnjGTUserSearchForm();
            newForm.setPageSize(userSearchForm.getPageSize());
            newForm.setSortBy(userSearchForm.getSortBy());
            model.addAttribute(SEARCH_USER_FORM, newForm);
        }

        final JnjGTB2BCustomerForm jnjGTB2BCustomerForm = new JnjGTB2BCustomerForm();
        model.addAttribute("createUserForm", jnjGTB2BCustomerForm);
        model.addAttribute(SELECTED_LINK, Jnjb2bCoreConstants.Resources.USER_MANAGEMENT_LINK_COMPONENT_ID);

        if (null != searchPageData.getResults()) {
            for (int i = 0; i < searchPageData.getResults().size(); i++) {
                if (null != searchPageData.getResults().get(i)) {
                    searchPageData.getResults().get(i).setRoles(defineRoleList(searchPageData.getResults().get(i)));
                }
            }
        }
        
        return getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.MyCompanyManageUsersPage);
    }

    private Set<String> defineRoleList(CustomerData customerData) {
        final List<String> list = (List<String>) customerData.getRoles();
        final Set<String> roleList = new HashSet<>();
        for (int j = 0; j < list.size(); j++) {
            UserGroupModel userGroup = new UserGroupModel();
            if (!(list.get(j).endsWith("UserGroup") && list.get(j).length() == 11) && !(list.get(j).equalsIgnoreCase(B2B_CUSTOMER_GROUP))) {
                userGroup = userService.getUserGroupForUID(list.get(j));
            }

            if (jnjLatamCommonFacadeUtil.getEffectiveGroups().contains(userGroup.getUid())) {
                roleList.add(userGroup.getDisplayName(getI18nService().getCurrentLocale()));
            } else if (null != userGroup.getGroups()) {
                for (final PrincipalGroupModel usg : userGroup.getGroups()) {
                    if (jnjLatamCommonFacadeUtil.getEffectiveGroups().contains(usg.getUid())) {
                        roleList.add(usg.getDisplayName(getI18nService().getCurrentLocale()));
                    }
                }
            }
        }

        return roleList;
    }

    @Override
    public String enableOrDisableUser(@RequestParam("status") final boolean status, @RequestParam("emailAddress") final String emailAddress, final Model model) {
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
        sessionService.setAttribute(STATUS_CHANGE, Boolean.TRUE);
        return jnjgtCustomerFacade.enableOrDisableUser(status, emailAddress);
    }

    @Override
    public String getSelectAccountPopup(final Model model, final String[] selectedAccounts, final JnjGTB2BCustomerForm form,
                                        @RequestParam(value = "page", defaultValue = "0") final int page,
                                        @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                        @RequestParam(value = "showMore", defaultValue = "false") final String showMore,
                                        @RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
                                        @RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm) {

        getSelectAccountData(model, selectedAccounts, form, page, showMode, showMore, showMoreCounter, searchTerm);

        return super.getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.SelectsAccountPopups);

    }

    @Override
    public String getSelectAccountEditPopup(final Model model, final String[] selectedAccounts, final JnjGTB2BCustomerForm form,
                                            @RequestParam(value = "page", defaultValue = "0") final int page,
                                            @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                            @RequestParam(value = "showMore", defaultValue = "false") final String showMore,
                                            @RequestParam(value = "user", defaultValue = "") final String user,
                                            @RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
                                            @RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm) {
        getSelectAccountData(model, selectedAccounts, form, page, showMode, showMore, showMoreCounter, searchTerm);

        final JnjGTCustomerData customerData = jnjgtCustomerFacade.getCustomerForUid(user);
        final List<B2BUnitData> b2bUnits = customerData.getB2bUnits();
        model.addAttribute("accountToAdded",b2bUnits);

        return super.getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.SelectsEditAccountPopups);
    }

    @Override
    public String getView(final String view) {
        return JnjlaresourcesControllerConstants.ADDON_PREFIX + view;
    }

    private void getSelectAccountData(Model model, String[] selectedAccounts, JnjGTB2BCustomerForm form, int page, ShowMode showMode, String showMore, String showMoreCounter, String searchTerm) {
        final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);
        final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter) : pageSizeFromConfig;
        final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

        if (StringUtils.isNotEmpty(searchTerm)) {
            pageableData.setSearchBy(searchTerm);
            model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm);
        }

        final JnjGTAccountSelectionData accountSelectionData = jnjLatamCustomerFacadeImpl.getAccountsMap(true, false, false, pageableData);

        model.addAttribute("popUpType","SELECT_ACCOUNTS");
        model.addAttribute("accounts", accountSelectionData.getAccountsMap());
        model.addAttribute("currentAccountId", orderFacade.getCurrentB2bUnitId());
        model.addAttribute("selectedAccounts", selectedAccounts);
        model.addAttribute(FORM_NAME, form);

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData());
        model.addAttribute(CURRENT_PAGE, showMoreCounter);
    }

    private void checkChangeAccountStatus(final Model model) {
        final Object changeaccountstatus = sessionService.getAttribute(STATUS_CHANGE);
        if (null != changeaccountstatus && ((Boolean) changeaccountstatus)) {
            model.addAttribute("statuschanged", Boolean.TRUE);
            sessionService.removeAttribute(STATUS_CHANGE);
        }
    }

    private boolean userHasNoRights() {
        final String userTierType = sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE) !=null ?
                sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE).toString() : StringUtils.EMPTY;
        return !(Jnjb2bCoreConstants.UserSearch.USER_TIER2.equalsIgnoreCase(userTierType) || Jnjb2bCoreConstants.UserSearch.USER_TIER1.equalsIgnoreCase(userTierType));
    }

    private void defineSector(JnjGTB2BCustomerForm b2BCustomerForm, JnjGTCustomerData b2bCustomerData) {
        final String userTierType = getSessionService().getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE);
        if (Jnjb2bCoreConstants.UserSearch.USER_TIER2.equals(userTierType)) {
            b2bCustomerData.setMddSector(b2BCustomerForm.isMdd());
            b2bCustomerData.setConsumerSector(b2BCustomerForm.isConsumer());
            b2bCustomerData.setPharmaSector(b2BCustomerForm.isPharma());
        }
    }

    private void defineSector(JnjGTCustomerData customerData, JnjGTB2BCustomerForm b2bCustomerForm) {
        if (customerData.getMddSector() != null) {
            b2bCustomerForm.setMdd(customerData.getMddSector());
        }
        if (customerData.getConsumerSector() != null) {
            b2bCustomerForm.setConsumer(customerData.getConsumerSector());
        }
        if (customerData.getPharmaSector() != null) {
            b2bCustomerForm.setPharma(customerData.getPharmaSector());
        }
    }

    private void defineRoles(JnjGTB2BCustomerForm b2BCustomerForm, JnjGTCustomerData b2bCustomerData) {
        final List<String> roles = new ArrayList<>();
        if (b2BCustomerForm.getRoles() != null) {
            Collection<String> listOfSelectedRoles = b2BCustomerForm.getRoles();
            for (String selectedRole : listOfSelectedRoles) {
                roles.add(selectedRole);
            }
        }
        roles.add(b2BCustomerForm.getRole());
        roles.add(Jnjb2bCoreConstants.B2BUnit.B2BCUSTOMERGROUP);

        b2bCustomerData.setRoles(roles);
    }

    private void defineRoles(JnjGTCustomerData customerData, JnjGTB2BCustomerForm b2bCustomerForm) {
        final Collection<String> roles = customerData.getRoles();

        List<String> roleList = new ArrayList<>(roles);
        List<String> rolesList = new ArrayList<>();

        for (final String role : roleList) {
            final UserGroupModel users = userService.getUserGroupForUID(role);
            final Set<PrincipalGroupModel> usgAll = users.getAllGroups();
            if (jnjLatamCommonFacadeUtil.getEffectiveGroups().contains(users.getUid())) {
                rolesList.add(users.getUid());
            }

            for (final PrincipalGroupModel pg : usgAll) {
                if (pg instanceof UserGroupModel) {
                    if (jnjLatamCommonFacadeUtil.getEffectiveGroups().contains(pg.getUid())) {
                        rolesList.add(pg.getUid());
                    }
                }
            }
        }

        b2bCustomerForm.setRoles(rolesList);

        for (final String role : roles) {
            if (!Jnjb2bCoreConstants.B2BUnit.B2BCUSTOMERGROUP.equals(role)) {
                b2bCustomerForm.setRole(role);

            }
        }
    }

    private void defineAccountsList(Model model, JnjGTCustomerData customerData, JnjGTB2BCustomerForm b2bCustomerForm) {
        final List<B2BUnitData> b2bUnits = customerData.getB2bUnits();
        if (CollectionUtils.isNotEmpty(b2bUnits)) {
            String hiddenAccountsString = StringUtils.join(b2bUnits.stream().map(B2BUnitData::getUid).collect(Collectors.toList()), ',');
            String selectedAccountsString = StringUtils.join(b2bUnits.stream().map(b -> b.getUid() + "(" + b.getName() + ")").collect(Collectors.toList()), ',');
            String hiddenAccountNamesList = StringUtils.join(b2bUnits.stream().map(b -> b.getUid() + "**" + b.getName()).collect(Collectors.toList()), '|');

            model.addAttribute("selectedAccountsString", selectedAccountsString);
            model.addAttribute("hiddenAccountNamesList", hiddenAccountNamesList);
            b2bCustomerForm.setGroups(hiddenAccountsString);
        }

        final Map<String, String> loggedUserAccountData = jnjGTCustomerFacade.getAccountsMap(true);
        model.addAttribute("loggedUserAccountList", loggedUserAccountData.keySet());

        final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);
        final JnjGTPageableData pageableData = createPageableData(0, pageSizeFromConfig, JnJB2BUnitModel.UID, ShowMode.Page);

        final JnjGTAccountSelectionData allAccountSelectionData = jnjLatamCustomerFacadeImpl.getAccountsMap(true, false, false, pageableData);
        model.addAttribute("allAccountSelectionList", allAccountSelectionData.getAccountsMap());
    }

    private void defineAddressData(JnjGTCustomerData customerData, JnjGTB2BCustomerForm b2bCustomerForm) {
        final AddressData contactAddress = customerData.getContactAddress();
        if (contactAddress != null) {
            final JnjGTAddressForm contactAddressForm = new JnjGTAddressForm();
            if (contactAddress instanceof JnjGTAddressData) {
                final JnjGTAddressData jnjNAContactAddress = (JnjGTAddressData) contactAddress;
                b2bCustomerForm.setPhone(jnjNAContactAddress.getPhone());
                b2bCustomerForm.setPhoneNumberPrefix(jnjNAContactAddress.getPhoneCode());
            }
            b2bCustomerForm.setContactAddress(contactAddressForm);
        }
    }

    private void defineSupervisorData(JnjGTCustomerData customerData, JnjGTB2BCustomerForm b2bCustomerForm) {
        b2bCustomerForm.setSupervisorName(customerData.getSupervisorName());
        if (customerData.getSupervisorPhone() != null) {
            b2bCustomerForm.setSupervisorPhonePrefix(customerData.getSupervisorPhoneCode());
            b2bCustomerForm.setSupervisorPhone(customerData.getSupervisorPhone());
        }
        b2bCustomerForm.setSupervisorEmail(customerData.getSupervisorEmail());
    }

    private void definePasswordDates(JnjGTCustomerData customerData, JnjGTB2BCustomerForm b2bCustomerForm) {
        final Date passwordChangeDate = customerData.getPasswordChangeDate();
        if (passwordChangeDate != null) {
            b2bCustomerForm.setPasswordChangeDate(JnJCommonUtil.formatDate(passwordChangeDate, Jnjb2bCoreConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT));

            try {
                final String pwdExpiryDays = JnJCommonUtil.getValue(Jnjb2bCoreConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_KEY);
                final int pwdExpDays = Integer.parseInt(pwdExpiryDays);
                final String pwdExpiryDate = JnjGTCoreUtil.convertDateFormat(b2bCustomerForm.getPasswordChangeDate(), Jnjb2bCoreConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT, Jnjb2bCoreConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT, pwdExpDays);
                b2bCustomerForm.setPasswordExpiryDate(pwdExpiryDate);
            } catch (final NumberFormatException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    private Map<String, String> getStatuses() {
        final List<CustomerStatus> statusesEnum = enumerationService.getEnumerationValues(CustomerStatus.class);
        final Map<String, String> statuses = new HashMap<>();
        for (final CustomerStatus status : statusesEnum) {
            statuses.put(status.getCode(), enumerationService.getEnumerationName(status));
        }
        return statuses;
    }

    private void defineTierInformation(Model model, JnjGTCustomerData customerData) {
        final String userTierType = getSessionService().getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE);
        model.addAttribute("isTier1User", Jnjb2bCoreConstants.UserSearch.USER_TIER1.equals(userTierType));
        model.addAttribute("isTier2User", Jnjb2bCoreConstants.UserSearch.USER_TIER2.equals(userTierType));
        if (Jnjb2bCoreConstants.UserSearch.USER_TIER1.equals(userTierType)) {
            if ((enumerationService.getEnumerationName(CustomerStatus.ACTIVE).equalsIgnoreCase(customerData.getStatus()) || (enumerationService.getEnumerationName(CustomerStatus.DISABLED).equalsIgnoreCase(customerData.getStatus())))) {
                model.addAttribute("isStatusDisabled", Boolean.FALSE);
            } else {
                model.addAttribute("isStatusDisabled", Boolean.TRUE);
            }
        }
        if (customerData.getRoles().contains(Config.getParameter(Jnjb2bCoreConstants.Login.USER_GROUP_ADMIN))) {
            model.addAttribute("selfTier2", Boolean.TRUE);
        } else {
            model.addAttribute("selfTier2", Boolean.FALSE);
        }
        if (customerData.getRoles().contains(Config.getParameter(Jnjb2bCoreConstants.Login.USER_GROUP_CSR))) {
            model.addAttribute("selfTier1", Boolean.TRUE);
        } else {
            model.addAttribute("selfTier1", Boolean.FALSE);
        }
    }

    private void defineAddressData(JnjGTB2BCustomerForm b2BCustomerForm, JnjGTCustomerData b2bCustomerData, JnjGTAddressForm contactAddresForm) {
        if (contactAddresForm != null) {
            final JnjGTAddressData contactAddressData = new JnjGTAddressData();
            contactAddressData.setPhone(b2BCustomerForm.getPhoneNumberPrefix() + Jnjb2bCoreConstants.SYMBOl_PIPE + JnjGTCoreUtil.getFormattedPhoneNumber(b2BCustomerForm.getPhone()));
            contactAddressData.setDefaultAddress(true);
            b2bCustomerData.setContactAddress(contactAddressData);
        }
    }

    private void defineLoginDisableByStatus(JnjGTB2BCustomerForm b2BCustomerForm, JnjGTCustomerData b2bCustomerData) {
        if (b2BCustomerForm.getStatus() != null) {
            if (b2BCustomerForm.getStatus().equalsIgnoreCase(enumerationService.getEnumerationName(CustomerStatus.DISABLED))) {
                b2bCustomerData.setLoginDisabledFlag(Boolean.TRUE);
            } else if (b2BCustomerForm.getStatus().equalsIgnoreCase(enumerationService.getEnumerationName(CustomerStatus.ACTIVE))) {
                b2bCustomerData.setLoginDisabledFlag(Boolean.FALSE);
            }
        }
    }

    private void populateAllAccountsAndRoles(Model model) {
        final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);
        final JnjGTPageableData pageableData = createPageableData(0, pageSizeFromConfig, JnJB2BUnitModel.UID, ShowMode.Page);
        final JnjGTAccountSelectionData allAccountSelectionData = jnjLatamCustomerFacadeImpl.getAccountsMap(true, false, false, pageableData);
        model.addAttribute("allAccountSelectionList", allAccountSelectionData.getAccountsMap());

        final Set<UserGroupModel> allAccountSelectionData1 = jnjLatamCustomerFacadeImpl.getUserMap(true, false, false, pageableData);

        final List<UserGroupModel> roles = new ArrayList<>(allAccountSelectionData1);

        final List<String> rolesUid = new ArrayList<>();
        final List<String> rolesName = new ArrayList<>();
        final Map<String, String> rolesMap = new HashMap<>();
        for (int i = 0; i < roles.size(); i++) {
            final String role = roles.get(i).getUid();

            if (jnjLatamCommonFacadeUtil.getEffectiveGroups().contains(role) && isRoleOnTierLevel(role)) {
                rolesUid.add(roles.get(i).getUid());
                rolesName.add(roles.get(i).getDisplayName());
                rolesMap.put(roles.get(i).getUid(), roles.get(i).getDisplayName());
            }
        }

        model.addAttribute("rolesUid", rolesUid);
        model.addAttribute("rolesName", rolesName);
        model.addAttribute("rolesMap", rolesMap);
    }

    private boolean isRoleOnTierLevel(final String role) {
        if (sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE).equals(Jnjb2bCoreConstants.UserSearch.USER_TIER1)) {
            return JnJCommonUtil.getValues(Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER1, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR).contains(role);
        } else {
            return JnJCommonUtil.getValues(Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER2, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR).contains(role);
        }
    }

    private void defineRolesForCreate(JnjGTB2BCustomerForm b2BCustomerForm, JnjGTCustomerData b2bCustomerData) {
        final List<String> roles = new ArrayList<>(b2BCustomerForm.getRoles());
        roles.add(B2BCUSTOMERGROUP);
        roles.add(getCountryUserGroup());
        b2bCustomerData.setRoles(roles);
    }

    private String getCountryUserGroup() {
        final String loggedInSite = cmsSiteService.getCurrentSite().getDefaultCountry().getIsocode();
        if (null != loggedInSite) {
            return loggedInSite.toLowerCase() + "UserGroup";
        }
        return null;
    }
}
