package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.la.loginaddon.controllers.JnjlaloginaddonControllerConstants;
import com.jnj.b2b.la.loginaddon.filters.JnjLACMSSiteFilter;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.loginaddon.controllers.pages.JnJGTHomePageController;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.dataload.mapper.UserRolesData;
import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjLatamGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.facades.customer.impl.JnjLatamCustomerFacadeImpl;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractFormData;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.loginaddon.forms.JnjGTHomePageForm;
import com.jnj.utils.CommonUtil;
import com.jnj.facades.cart.impl.JnjLatamCartFacadeImpl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import java.util.Iterator;

import java.io.FileInputStream;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JnjLatamHomePageController extends JnJGTHomePageController {

    private static final Class<JnjLatamHomePageController> THIS_CLASS = JnjLatamHomePageController.class;

    private static final String HOME_PAGE = "/home";
    private static final String CHANGE_ACCOUNT = "changeAccount";
    private static final String B2B_SHIPPING_ADDRESS_MAP = "b2bShippingAddressMap";
    private static final String TWO_DOTS = ":";
    private static final String ZERO_AS_STRING = "0";
    private static final String UPLOAD_TEMPLATE_START_FROM = "home.upload.template.startFrom";
    private static final String IMPORT_EXCEL_MAX_COUNT = "cart.importexcel.max.count";    

    @Autowired
    private JnjLatamCartFacade jnJLatamCartFacade;

    @Autowired
    private JnjLatamCustomerFacadeImpl jnjLatamCustomerFacadeImpl;

    @Autowired
    private JnjLatamGetCurrentDefaultB2BUnitUtil jnjLatamGetCurrentDefaultB2BUnitUtil;

    @Resource(name = "GTCustomerService")
    private JnjGTCustomerService jnjGTCustomerService;

    @Autowired
    private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

    @Autowired
    private CommonI18NService commonI18NService;

    @Autowired
    private JnjLatamOrderFacade jnjlatamCustomOrderFacade;

    @Autowired
    protected UserService userService;

    @Override
    public String changeAccountRequest(@RequestParam(value = "uid") final String selectedAccountUid, @RequestParam(value = "accountName") final String selectedAccountName, @RequestParam(value = "accountGLN") final String selectedAccountGLN, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException {
        final String methodName = "changeAccountRequest()";
        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        // Remove free Goods from the session
        sessionService.removeAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);

        changeAccount(selectedAccountUid, selectedAccountName, selectedAccountGLN, model);
        return REDIRECT_PREFIX + HOME_PAGE;
    }

    private void changeAccount(String selectedAccountUid, String selectedAccountName, String selectedAccountGLN, Model model) {
        boolean status = false;

        if (!model.containsAttribute(LoginaddonConstants.Login.SURVEY)) {
            setSurveyFlag(model);
        }

        if (selectedAccountUid != null) {
            status = jnjLatamCustomerFacadeImpl.changeAccount(selectedAccountUid);
            JnjGTCoreUtil.logDebugMessage(Logging.ACCOUNTS, CHANGE_ACCOUNT, "Change account status :: " + status, THIS_CLASS);
            final String currentSiteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
            final String sourceSystemIdForUnit = jnjGTUnitFacade.getSourceSystemIdForUnit();

            switchSite(currentSiteName, sourceSystemIdForUnit);
            JnjGTCoreUtil.logDebugMessage(Logging.ACCOUNTS, CHANGE_ACCOUNT, "currentSiteName : " + currentSiteName, THIS_CLASS);
            JnjGTCoreUtil.logDebugMessage(Logging.ACCOUNTS, CHANGE_ACCOUNT, "sourceSystemIdForUnit : " + sourceSystemIdForUnit, THIS_CLASS);
            jnjGTCartFacade.restoreCartForCurrentUser(sourceSystemIdForUnit);
        }

        if (!status) {
            model.addAttribute(CHANGE_ACCOUNT_ERROR, Boolean.TRUE);
        } else {
            model.addAttribute(LoginaddonConstants.Login.ACCOUNT_UID, selectedAccountUid);
            model.addAttribute(LoginaddonConstants.Login.ACCOUNT_NAME, selectedAccountName);
            model.addAttribute(LoginaddonConstants.Login.ACCOUNT_GLN, selectedAccountGLN);
            setCurrentB2BUnitIdInModel(model);

            JnjGTCoreUtil.logDebugMessage(Logging.ACCOUNTS, CHANGE_ACCOUNT, "Current account info has been set for the header.", THIS_CLASS);
        }

        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, CHANGE_ACCOUNT, Logging.END_OF_METHOD, THIS_CLASS);

        final JnJB2BUnitModel currentB2bUnit = jnjGTCustomerFacade.getCurrentB2bUnit();
        if (currentB2bUnit != null) {
            final Set<JnjContractModel> contractList = currentB2bUnit.getJnjContracts();
            if (CollectionUtils.isNotEmpty(contractList)) {
                sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.TRUE);
                model.addAttribute(SHOW_CONTRACT, LoginaddonConstants.TRUE);
            } else {
                sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.FALSE);
                model.addAttribute(SHOW_CONTRACT, LoginaddonConstants.FALSE);
            }
            // Updates the cart's shipping address to the default shipping address of the chosen b2b unit
            jnJLatamCartFacade.updateShippingAddress(currentB2bUnit);
        }
    }

    @Override
    public String getView(final String view) {
        return JnjlaloginaddonControllerConstants.ADDON_PREFIX + view;
    }

    @Override
    protected String getEPICHomePage(final Model model, final boolean firstTimeLogin, final boolean redirectToTarget, final HttpServletRequest request) throws CMSItemNotFoundException {
        final String methodName = "getEPICHomePage()";
        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        final JnjGTPageableData pageableData = createPageableData(0, Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50), JnJB2BUnitModel.UID, ShowMode.Page);
        final JnjGTAccountSelectionData accountSelectionData = jnjLatamCustomerFacadeImpl.getAccountsMap(true, false, false, pageableData);
        final Map<String, String> accountsMap = accountSelectionData.getAccountsMap();
        final Object firstTimeVisitAfterLogin = sessionService.getAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN);
        boolean redirectToTargetFlag = redirectToTarget;

        if (null != firstTimeVisitAfterLogin || firstTimeLogin) {
            redirectToTargetFlag = false;
            sessionService.removeAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN);
            model.addAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN, Boolean.TRUE);
            JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, "First time login flag set in the model.", THIS_CLASS);

            if (null != accountsMap) {
                if (accountsMap.size() == 1) {
                    JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, "accountsMap.size() == 1", THIS_CLASS);
                    setSurveyFlag(model);
                    changeAccountRequest(accountsMap.keySet().iterator().next(), accountsMap.values().iterator().next().split(LoginaddonConstants.UNDERSCORE_SYMBOL)[1], accountsMap.values().iterator().next().split(LoginaddonConstants.UNDERSCORE_SYMBOL)[0], model, request);
                } else {
                    JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, "accountsMap.size() > 1", THIS_CLASS);
                    model.addAttribute(LoginaddonConstants.Login.ACCOUNT_LIST, accountsMap); // Pass true to get accounts inclusive of current account
                    model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting pagination data
                    model.addAttribute(CURRENT_PAGE, 1);
                    setShippingAddressMap(model);
                    setCurrentB2BUnitIdInModel(model);
                    JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, "Accounts map has been set in the model.", THIS_CLASS);
                }
            } else {
                JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, "Setting the survey flag", THIS_CLASS);
            }
        }

        setSurveyFlag(model);

        if ((null != firstTimeVisitAfterLogin && accountsMap != null && accountsMap.size() == 1) || firstTimeVisitAfterLogin == null) {
            final Object orderingRights = sessionService.getAttribute(LoginaddonConstants.Login.ORDERING_RIGHTS);

            if (null != orderingRights && ((Boolean) orderingRights)) {
                model.addAttribute("isEligibleForNewOrder", Boolean.TRUE);
            }

            final String currentSiteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
            if (null != currentSiteName && LoginaddonConstants.MDD.equals(currentSiteName)) {
                model.addAttribute("isEligibleForPriceQuote", Boolean.TRUE);
            }
            model.addAttribute("isEligibleForOrderReturn", jnjGTCartFacade.isEligibleForOrderReturn());

            model.addAttribute("searchPageData", getRecentOrders(new JnjGTOrderHistoryForm()));
            model.addAttribute("broadCastData", getBroadCastMessages());
            model.addAttribute("orderTemplates", jnjGTOrderTemplateFacade.getOrderTemplatesForHome());
            model.addAttribute(LoginaddonConstants.Login.ACCOUNT_LIST, accountsMap);
            setShippingAddressMap(model);
        }

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        validateSelectedAccount(model);

        final String orderType = jnjGTCartFacade.getOrderType();
        final HttpSession session = request.getSession();
        session.setAttribute("cartorderType", orderType);
        model.addAttribute("homePageForm", new JnjGTHomePageForm());

        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, Logging.END_OF_METHOD, THIS_CLASS);
        try {
            final String isoCode = getCmsSiteService().getCurrentSite().getDefaultCountry().getIsocode();
            request.getSession().setAttribute("isoCode", isoCode.toLowerCase());
        } catch (final Exception e) {
            JnjGTCoreUtil.logErrorMessage(Logging.HOME_PAGE, methodName, "Define a default country for the current site!", e, THIS_CLASS);
        }

        jnjLatamCommonFacadeUtil.addPermissionsFlags(request, model);
        
        String language = "en";
        if (null != commonI18NService.getCurrentLanguage()) {
            language = commonI18NService.getCurrentLanguage().getIsocode().toLowerCase();
        }
        request.getSession().setAttribute("sessionLanguage", language);

        return getTargetView(redirectToTargetFlag);
    }

    private void validateSelectedAccount(final Model model)  {
        final String selectedAccount = getSessionService().getAttribute(JnjLACMSSiteFilter.SET_ACCOUNT_FROM_EXTERNAL_URL);
        if (StringUtils.isNotBlank(selectedAccount) && selectedAccount.contains(JnjLACMSSiteFilter.ACCOUNT_SEPARATOR)) {
            final String[] accountSplit = selectedAccount.split(JnjLACMSSiteFilter.ACCOUNT_SEPARATOR);
            changeAccount(accountSplit[0], accountSplit[1], null, model);
            model.addAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN, Boolean.FALSE);
            getSessionService().removeAttribute(JnjLACMSSiteFilter.SET_ACCOUNT_FROM_EXTERNAL_URL);
        }
    }

    private void setShippingAddressMap(final Model model) {
        model.addAttribute(B2B_SHIPPING_ADDRESS_MAP, sessionService.getAttribute(B2B_SHIPPING_ADDRESS_MAP));
        sessionService.removeAttribute(B2B_SHIPPING_ADDRESS_MAP);
    }

    @Override
    public String getAccounts(@RequestParam(value = "page", defaultValue = ZERO_AS_STRING) final int page, @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode, @RequestParam(value = "showMore", defaultValue = "false") final String showMore, @RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter, @RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm, @RequestParam(value = "addCurrentB2BUnit", defaultValue = "false", required = false) final String addCurrentB2BUnit, final Model model) throws CMSItemNotFoundException {

        final String methodName = "getAccounts()";
        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.ACCOUNTS, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);

        final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter) : pageSizeFromConfig;
        final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

        if (StringUtils.isNotEmpty(searchTerm)) {
            pageableData.setSearchBy(searchTerm);
            model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm); // setting search term
        }

        JnjGTCoreUtil.logDebugMessage(Logging.ACCOUNTS, methodName, "Calling facade to fetch accounts which has the string " + searchTerm, THIS_CLASS);

        final JnjGTAccountSelectionData accountSelectionData = jnjLatamCustomerFacadeImpl.getAccountsMap(Boolean.valueOf(addCurrentB2BUnit), false, false, pageableData);

        model.addAttribute(LoginaddonConstants.Login.ACCOUNT_LIST, accountSelectionData.getAccountsMap());
        model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData());
        model.addAttribute(CURRENT_PAGE, showMoreCounter);
        setShippingAddressMap(model);

        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.ACCOUNTS, methodName, Logging.END_OF_METHOD, THIS_CLASS);
        return getView(LoginaddonControllerConstants.Views.Pages.Account.ChangeAccountPage);
    }

    @Override
    public String multiAddToCart(@RequestParam("prodIds") final String prodIds, final Model model) throws CMSItemNotFoundException {
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        final String methodName = "multiAddToCart()";
        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        final HashMap<String, String> responseMap = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(prodIds)) {
            final String productIdsUpperCase = prodIds.toUpperCase();
            final String[] products = productIdsUpperCase.replaceAll("\n", "").split(LoginaddonConstants.SYMBOl_COMMA);
            for (final String prodCode : products) {
                if (StringUtils.isNotBlank(prodCode.trim())) {
                    if (responseMap.containsKey(prodCode.trim())) {
                        final int count = Integer.parseInt(responseMap.get(prodCode.trim()));
                        responseMap.put(prodCode.trim(), String.valueOf(count + 1));
                    } else {
                        responseMap.put(prodCode.trim(), "1");
                    }
                }
            }
            addMultipleProds(responseMap, model, false);
        }
        model.addAttribute(ADD_STATUS, responseMap);
        final CartData cartData = jnjGTCartFacade.getSessionCart();

        CommonUtil.logMethodStartOrEnd(Logging.HOME_MULTIPLE_CART, methodName, Logging.BEGIN_OF_METHOD, LOG);
        model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));

        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.END_OF_METHOD, THIS_CLASS);
        return super.getView(LoginaddonControllerConstants.Views.Fragments.Cart.AddToCartHomePopup);
    }

    @Override
    public String multiAddToCartWithQty(String prodIds, String prodQty, Model model, boolean forceNewEntry, String currentLineEntryNumber) throws CMSItemNotFoundException {
        super.multiAddToCartWithQty(prodIds, prodQty, model, forceNewEntry, currentLineEntryNumber);
        return super.getView(LoginaddonControllerConstants.Views.Fragments.Cart.AddToCartHomePopup);
    }

    @Override
    public JnjCartModificationData addMultipleProds(final Map<String, String> productCodes, final Model model, final boolean isHomePageFileUpload) {
        return addMultipleProducts(productCodes, model, isHomePageFileUpload, false, 0);
    }

    @Override
    protected JnjCartModificationData addMultipleProducts(Map<String, String> productCodes, Model model, boolean isHomePageFileUpload, final boolean forceNewEntry, int currentEntryNumber) {
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
        return jnJLatamCartFacade.addMultipleProds(productCodes, model, isHomePageFileUpload, forceNewEntry, currentEntryNumber);
    }

    @Override
    protected JnjCartModificationData quickAddToCart(final String prodId_Qtys, final Model model) {

        final String methodName = "quickAddToCart()";
        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        final String[] products = prodId_Qtys.split(LoginaddonConstants.SYMBOl_COMMA);
        JnjCartModificationData cartModificationData = null;
        final List<CartModificationData> cartModificationList = new ArrayList<>();
        boolean removeContract = false;
        int prodNoQuantity = 0;
        for (String product : products) {
            product = product.toUpperCase();
            final String[] productQq = product.split(LoginaddonConstants.Solr.COLON);
            try {
                if (productQq.length > 1) {
                    cartModificationData = jnJLatamCartFacade.addToCartQuick(productQq[0], productQq[1], cartModificationList);
                } else {
                    prodNoQuantity++;
                    cartModificationData = jnJLatamCartFacade.addToCartQuick(productQq[0], _0, cartModificationList);
                }
                if (null != cartModificationData && !cartModificationData.getCartModifications().get(0).isError()) {
                    if (StringUtils.equals(cartModificationData.getCartModifications().get(0).getStatusCode(), LoginaddonConstants.HomePage.BASKET_PAGE_MESSAGE_MIN_QTY_ADDED) || StringUtils.equals(cartModificationData.getCartModifications().get(0).getStatusCode(), LoginaddonConstants.HomePage.BASKET_PAGE_MESSAGE_QTY_ADJUSTED)) {
                        model.addAttribute("showMinQtyMsg", Boolean.TRUE);
                    }

                    if (!CollectionUtils.isEmpty(cartModificationData.getCartModifications()) && null != cartModificationData.getCartModifications().get(0).getEntry()) {
                        model.addAttribute(PRODUCT, cartModificationData.getCartModifications().get(0).getEntry().getProduct());
                        model.addAttribute(CART_DATA, cartModificationData);
                    } else {
                        model.addAttribute(PRODUCT, productFacade.getProductForCodeAndOptions(productQq[0], Collections.singletonList(ProductOption.BASIC)));
                    }
                    removeContract = !jnJLatamCartFacade.isContractProduct(productQq[0]);
                }
            } catch (final CommerceCartModificationException e) {
                JnjGTCoreUtil.logErrorMessage(Logging.HOME_PAGE, methodName, "Not able to add products" + Arrays.toString(products) + " Excption trace" + e, e, THIS_CLASS);
            } catch (final NumberFormatException e) {
                JnjGTCoreUtil.logErrorMessage(Logging.HOME_PAGE, methodName, "Not able to add products as the entered value for the quantity is not valid", e, THIS_CLASS);
                sessionService.setAttribute("productCodeInvalidFlag", Boolean.TRUE);
            }
        }

        if (prodNoQuantity == 0) {
            sessionService.setAttribute("allProdQuantity", Boolean.TRUE);
        }

        if (removeContract) {
            jnJLatamCartFacade.saveCartRemoveContractNo();
        }

        final CartData sessionCart = jnjGTCartFacade.getSessionCart();
        if (sessionCart != null && cartModificationData != null) {
            cartModificationData.setTotalUnitCount(sessionCart.getTotalUnitCount());
        }

        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.END_OF_METHOD, THIS_CLASS);

        return cartModificationData;
    }

    @Override
    protected void uploadOrderFileHomePage(final Model model, final List<MultipartFile> uploadmultifilehomeList, final HttpServletResponse response) throws CMSItemNotFoundException, IOException {
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        final String methodName = "uploadOrderFileHomePage()";
        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        Boolean indirectCustomer = Boolean.FALSE;
        Boolean indirectPayer = Boolean.FALSE;
        final UserRolesData userRolesData = jnjLatamGetCurrentDefaultB2BUnitUtil.getUserRoles();
        final CountryModel country = jnjLatamGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
        int listSize = getUploadedFileTotalCount(uploadmultifilehomeList);      
		int maxCount = configurationService.getConfiguration().getInt(IMPORT_EXCEL_MAX_COUNT, 1000);
		
		LOG.info("import excel listSize " + listSize + " maxcount " + maxCount);
		
		final Map<String, String> responseMap = new LinkedHashMap<>();
		
		if (listSize > maxCount) {
			LOG.info("Excel record count exceeded the limit of "+ maxCount);

			responseMap.put("maxCount", Integer.toString(maxCount));
			response.setContentType(TEXT_HTML);
			final PrintWriter writer = response.getWriter();
			writer.println(new ObjectMapper().writeValueAsString(responseMap));
			writer.close();
		} else {
		
        if (userRolesData.isIndirectCustomer()) {
            indirectCustomer = Boolean.TRUE;

            final List<String> indirectPayerEnabledCountriesList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_INDIRECT_PAYER_VALID_COUNTRIES);

            if (JnjLaCoreUtil.isCountryValidForACountriesList(country.getIsocode(), indirectPayerEnabledCountriesList)) {
                indirectPayer = Boolean.TRUE;
            }
        }

       
        int fileProdNoQuantity = processUploadedFiles(uploadmultifilehomeList, indirectCustomer, indirectPayer, responseMap);

        if (fileProdNoQuantity == 0) {
            sessionService.setAttribute("fileAllProdQuantity", Boolean.TRUE);
        }

        if (!responseMap.isEmpty()) {
            addMultipleProds(responseMap, model, true);
            responseMap.put("totalCartCount", String.valueOf(jnjGTCartFacade.getSessionCart().getTotalUnitCount()));
        } else {
            responseMap.put(EMPTY, EMPTY);
        }

        if (responseMap.size() > 1 && responseMap.containsKey(EMPTY)) {
            responseMap.remove(EMPTY);
        }

        response.setContentType(TEXT_HTML);
        final PrintWriter writer = response.getWriter();
        writer.println(new ObjectMapper().writeValueAsString(responseMap));
        writer.close();

        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.END_OF_METHOD, THIS_CLASS);
		}
    }

    private int processUploadedFiles(List<MultipartFile> uploadmultifilehomeList, Boolean indirectCustomer, Boolean indirectPayer, Map<String, String> responseMap) {
        int fileProdNoQuantity = 0;
        for (final MultipartFile uploadMultiFileHome : uploadmultifilehomeList) {
            final Map<String, String> productQuantityMap = jnJLatamCartFacade.fileConverter(uploadMultiFileHome, indirectCustomer, indirectPayer);
            if (null != productQuantityMap) {
                for (final Map.Entry<String, String> entry : productQuantityMap.entrySet()) {
                    final String trimmedKey = entry.getKey().trim();
                    if (StringUtils.isNotBlank(trimmedKey)) {
                        if (entry.getValue().equals(ZERO_AS_STRING)) {
                            fileProdNoQuantity++;
                        }

                        if (responseMap.containsKey(trimmedKey)) {
                            final String oldValue = responseMap.get(trimmedKey);
                            final String oldQtyString = oldValue.split(TWO_DOTS)[0];
                            final String indirectSuffix = oldValue.replace(oldQtyString, StringUtils.EMPTY);
                            final long oldQty = Long.parseLong(oldQtyString);
                            final long receivedQty = Long.parseLong(entry.getValue().split(TWO_DOTS)[0]);
                            final long total = oldQty + receivedQty;
                            final String totalString = String.valueOf(total);
                            final String value = totalString.concat(indirectSuffix);
                            responseMap.put(trimmedKey, value);
                        } else {
                            responseMap.put(trimmedKey, entry.getValue());
                        }
                    }
                }
            }
        }
        return fileProdNoQuantity;
    }

    @Override
    public JnjGTHomePageForm validateIsNonContractProduct(@RequestParam(value = "uploadmultifilehome", required = false) final List<MultipartFile> uploadmultifilehomeList, final HttpServletResponse response) {
        final String methodName = "validateIsNonContractProduct()";
        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        final JnjGTHomePageForm jnjGTHomePageForm = new JnjGTHomePageForm();
        final Map<String, String> responseMap = new LinkedHashMap<>();

        Boolean indirectCustomer = Boolean.FALSE;
        Boolean indirectPayer = Boolean.FALSE;
        final UserRolesData userRolesData = jnjLatamGetCurrentDefaultB2BUnitUtil.getUserRoles();
        final CountryModel country = jnjLatamGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();

        if (userRolesData.isIndirectCustomer()) {
            indirectCustomer = Boolean.TRUE;
            final List<String> indirectPayerEnabledCountriesList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_INDIRECT_PAYER_VALID_COUNTRIES);

            if (JnjLaCoreUtil.isCountryValidForACountriesList(country.getIsocode(), indirectPayerEnabledCountriesList)) {
                indirectPayer = Boolean.TRUE;
            }
        }

        for (final MultipartFile uploadmultifilehome : uploadmultifilehomeList) {
            processUploadedFile(responseMap, indirectCustomer, indirectPayer, uploadmultifilehome);
        }

        if (!responseMap.isEmpty()) {
            final Set<String> productIds = responseMap.keySet();
            JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, "productIds in controller " + productIds, THIS_CLASS);

            String[] strProductLst = new String[productIds.size()];
            strProductLst = productIds.toArray(strProductLst);
            final JnjContractFormData formData = jnJLatamCartFacade.validateIsNonContract(strProductLst, null);
            jnjGTHomePageForm.setNonContractProductInCart(formData.getIsNonContractProductInCart());
            jnjGTHomePageForm.setNonContractProductInSelectedList(formData.getIsNonContractProductInSelectedList());

            JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, "getIsNonContractProductInCart in controller : " + jnjGTHomePageForm.isNonContractProductInCart(), THIS_CLASS);
            JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, "getIsNonContractProductInSelectedList in controller : " + jnjGTHomePageForm.isNonContractProductInSelectedList(), THIS_CLASS);
        }

        JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);
        return jnjGTHomePageForm;
    }

    private void processUploadedFile(Map<String, String> responseMap, Boolean indirectCustomer, Boolean indirectPayer, MultipartFile uploadmultifilehome) {
        final Map<String, String> productQuantityMap = jnJLatamCartFacade.fileConverter(uploadmultifilehome, indirectCustomer, indirectPayer);
        if (!productQuantityMap.isEmpty()) {
            for (final Map.Entry<String, String> entry : productQuantityMap.entrySet()) {
                final String trimmedKey = entry.getKey().trim();
                if (StringUtils.isNotBlank(trimmedKey)) {
                    if (responseMap.containsKey(trimmedKey)) {
                        final String oldValue = responseMap.get(trimmedKey);
                        final String oldQtyString = oldValue.split(TWO_DOTS)[0];
                        final String indirectSulfix = oldValue.replace(oldQtyString, StringUtils.EMPTY);
                        final long oldQty = Long.parseLong(oldQtyString);
                        final long receivedQty = Long.parseLong(entry.getValue().split(TWO_DOTS)[0]);
                        final long total = oldQty + receivedQty;
                        final String totalString = String.valueOf(total);
                        final String value = totalString.concat(indirectSulfix);
                        responseMap.put(trimmedKey, value);
                    } else {
                        responseMap.put(trimmedKey, entry.getValue());
                    }
                }
            }
        }
    }


    @Override
    public String getHome(final Model model, @RequestParam(value = "firstTimeLogin", defaultValue = "false", required = false) final boolean firstTimeLogin, final HttpServletRequest request) throws CMSItemNotFoundException {
        final String methodName = "getHome()";
        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        // resetPassword start
        if (!isResetPasswordComplete()) {
            final String siteUrl = JnjWebUtil.getServerUrl(request);
            final String userId = userService.getCurrentUser().getUid();
            String passResetUrl = "";
            try {
                passResetUrl = jnjGTCustomerFacade.resetPasswordUrlFirstTimeLogin(userId, siteUrl);
                JnjGTCoreUtil.logDebugMessage(Logging.HOME_PAGE, methodName, "test point : " + "isResetPasswordComplete :: " + passResetUrl, THIS_CLASS);
            } catch (final UnsupportedEncodingException exception) {
                JnjGTCoreUtil.logErrorMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, exception.getMessage(), exception, THIS_CLASS);
            }
            return REDIRECT_PREFIX + "/register/laResetPasswordForNewUser" + passResetUrl; ///passwordExpiredEmail
        }
        //reset password end


        boolean redirectToTarget = true;
        if (!firstTimeLogin) {
            final boolean updatedPrivacyPolicy = jnjGTCustomerFacade.checkPrivacyPolicyVersions();
            model.addAttribute(UPDATED_PRIVACY_POLICY, updatedPrivacyPolicy);
            JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, "Privacy policy updated :: " + updatedPrivacyPolicy, THIS_CLASS);
            if (!updatedPrivacyPolicy) {
                redirectToTarget = false;
            }
        }

        setupModel(model, CMS_HOME_PAGE);
        return getEPICHomePage(model, firstTimeLogin, redirectToTarget, request);
    }

    @RequestMapping(value = "/webEdiOrder")
    @ResponseBody
    public Map<String, String> saveWebEdiOrderFile(final Model model, @RequestParam(value = "submitEdiOrderFile") final CommonsMultipartFile[] submitOrderArrayFile) {
        final String methodName = "saveWebEdiOrderFile()";
        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);
        final Map<String, String> fileStatusMap = new LinkedHashMap<>();

        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, submitOrderArrayFile[0].getOriginalFilename(), THIS_CLASS);
        jnjlatamCustomOrderFacade.saveEdiFilesToFtp(submitOrderArrayFile, fileStatusMap);

        model.addAttribute("fileStatus", fileStatusMap);

        final String redirectView = REDIRECT_PREFIX + HOME_PAGE;
        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, "Edi file upload is done redirecting user to: " + redirectView, THIS_CLASS);

        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, methodName, Logging.END_OF_METHOD, THIS_CLASS);
        return fileStatusMap;
    }

	@Override
	public boolean getFileUploadQuantity(Model model, @RequestParam(value = "uploadmultifilehome", required = false)
		List<MultipartFile> uploadmultifilehomeList, HttpServletResponse response) {
		return false;
	}

	private int getUploadedFileTotalCount(
			List<MultipartFile> uploadmultifilehomeList) {
		int rowCount = 0;		
		for (MultipartFile homePageFile : uploadmultifilehomeList) {

			try {
				final FileInputStream fileInputStream = (FileInputStream) homePageFile
						.getInputStream();
				Iterator<Row> rowIterator;
				if (StringUtils.endsWithIgnoreCase(FilenameUtils
						.getExtension(homePageFile.getOriginalFilename()),
						Jnjlab2bcoreConstants.FILE_EXTENSION_XLSX)) {
					final XSSFWorkbook workbook = new XSSFWorkbook(
							fileInputStream);
					final XSSFSheet sheet = workbook.getSheetAt(0);
					rowIterator = sheet.iterator();

				} else {

					final HSSFWorkbook workbook = new HSSFWorkbook(
							fileInputStream);
					final HSSFSheet sheet = workbook.getSheetAt(0);
					rowIterator = sheet.iterator();

				}
				int rowTotal = 0;
				while (rowIterator.hasNext()) {
					final Row row = rowIterator.next();

					if (!JnjLatamCartFacadeImpl.isCellEmpty(row.getCell(0))) {
						rowTotal++;
					}

				}
				final int startFrom = Config.getInt(UPLOAD_TEMPLATE_START_FROM,
						2);
				rowCount = rowTotal - startFrom;

			} catch (Exception exe) {
				LOG.error("Error while reading the import excel file ", exe);
			}

		}
		return rowCount;
	}
}