package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.la.loginaddon.constants.JnjlaloginaddonConstants;
import com.jnj.b2b.la.loginaddon.controllers.JnjlaloginaddonControllerConstants;
import com.jnj.b2b.la.loginaddon.filters.JnjLACMSSiteFilter;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.loginaddon.controllers.pages.JnJGTLoginPageController;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.company.impl.JnjLatamB2BCommerceUserFacadeImpl;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import com.jnj.b2b.la.loginaddon.multimode.ResetTokenOptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;

public class JnjLatamLoginPageController extends JnJGTLoginPageController {

    private static final Logger LOG = Logger.getLogger(JnjLatamLoginPageController.class);

    private static final String SAML_SINGLESIGNON_SAML = "samlsinglesignon/saml/";
    private static final String STORE = "/store/";
    private static final String HOME = "/home";
    private static final String PROCESS_LOGIN = "processLogin()";

    @Autowired
    private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

    @Autowired
    private JnjLatamB2BCommerceUserFacadeImpl commerceUserFacade;

    @Autowired
    private CommonI18NService commonI18NService;

    @Autowired
    private BaseSiteService baseSiteService;

    @Override
    public String doLogin(@RequestHeader(value = "referer", required = false) final String referer, @RequestParam(value = "error", defaultValue = "false") final boolean loginError, @RequestParam(value = "passwordExpireToken", required = false) final String passwordExpireToken, @RequestParam(value = "email", required = false) final String email, @RequestParam(value = "contactUs", required = false) final String helpFlag, final Model model, final HttpServletRequest request, final HttpServletResponse response, final HttpSession session) throws CMSItemNotFoundException {
        logMethodStartOrEnd(LOGIN, "LATAM - processLogin()", Logging.BEGIN_OF_METHOD);
        LOG.info("LOGINPAGE dologinmethodstarting = " + new Date());
        if (StringUtils.isNotBlank(passwordExpireToken) && StringUtils.isNotBlank(email)) {
            storeCmsPageInModel(model, getContentPageForLabelOrId(LoginaddonConstants.Register.FIRST_RESET_PASSWORD));
            setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LoginaddonConstants.Register.FIRST_RESET_PASSWORD));
            return getExpiredPasswordPage(passwordExpireToken, email);
        }

        final String countryInfo = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
        model.addAttribute("isoCode", countryInfo.toLowerCase());

        storeReferer(referer, request, response);

        checkSessionExpiration(model, request, response);

        if (null != helpFlag) {
            model.addAttribute("helpFlag", helpFlag);
        }

        final String currentSiteType = cmsSiteService.getCurrentSite().getJnjWebSiteType().getCode();
        final String siteName = currentSiteType.equalsIgnoreCase(LoginaddonConstants.MDD_SITE_ID) ? LoginaddonConstants.MDD : LoginaddonConstants.CONS;
        sessionService.setAttribute(LoginaddonConstants.SITE_NAME, siteName);
        LOG.info("doLogin Site = " + siteName);
        logMethodStartOrEnd(LOGIN, "LATAM - processLogin()", Logging.END_OF_METHOD);

        return getDefaultLoginPage(loginError, session, model);
    }

    private void checkSessionExpiration(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        if (null != sessionService.getAttribute(ATTEMPTS_EXCEEDED)) {
            model.addAttribute(ATTEMPTS_EXCEEDED, Boolean.TRUE);
            sessionService.removeAttribute(ATTEMPTS_EXCEEDED);
        }

        if (null != sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRED_FOR_USER)) {
            String message = "passwordExpiredForThisUser";
            model.addAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRED_FOR_USER, Boolean.TRUE);
            model.addAttribute(PASSWORD_EXPIRED_USER, message);
            sessionService.removeAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRED_FOR_USER);
        }

        if (null != request.getParameter(LoginaddonConstants.Login.LOGOUT_PARAM)) {
            logDebugMessage(LOGIN, PROCESS_LOGIN, "Logout Successful!");
            model.addAttribute(LoginaddonConstants.Login.LOGOUT_PARAM, Boolean.TRUE);
        }

        if (null != request.getParameter(LoginaddonConstants.Login.SESSION_EXPIRED_PARAM)) {
            logDebugMessage(LOGIN, PROCESS_LOGIN, "User's session expired");
            model.addAttribute(LoginaddonConstants.Login.SESSION_EXPIRED_PARAM, Boolean.TRUE);
        }

        if (null != request.getParameter(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM)) {
            logDebugMessage(LOGIN, PROCESS_LOGIN, "User's Password is expired!");
            model.addAttribute(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM, request.getParameter(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM));
        }

        if (null != request.getParameter(LoginaddonConstants.Login.PASSWORD_EXPIRE_TOKEN)) {
            model.addAttribute(LoginaddonConstants.Login.PASSWORD_EXPIRE_TOKEN, request.getParameter(LoginaddonConstants.Login.PASSWORD_EXPIRE_TOKEN));
            model.addAttribute(LoginaddonConstants.Login.EMAIL, request.getParameter(LoginaddonConstants.Login.EMAIL));
        }

        validateLogoutReason(model, request, response);
    }

    private void validateLogoutReason(Model model, HttpServletRequest request, HttpServletResponse response) {
        if (ArrayUtils.isNotEmpty(request.getCookies())) {
            Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase(JnjlaloginaddonConstants.Login.LOGOUT_REASON)).findFirst().ifPresent(c -> {
                model.addAttribute(LoginaddonConstants.Login.LOGIN_ERROR, c.getValue());
                c.setValue("");
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);

            });
        }
    }

    @Override
    protected String getDefaultLoginPage(final boolean loginError, final HttpSession session, final Model model) throws CMSItemNotFoundException {
        LOG.info("LOGINPAGE getDefaultLoginPagemethodstarting = " + new Date());
        prepareSamlURL(model);
        if (null != sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN_VERIFICATION))
        {
            sessionService.removeAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN_VERIFICATION);
            model.addAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_RESET_FLAG, Boolean.TRUE);
            ResetTokenOptions tokenOption= new ResetTokenOptions();
            final boolean tokenVerified =sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN_VERIFIED) ;
            model.addAttribute(LoginaddonConstants.Login.PASSWORD_RESET_EMAIL,sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_RESET_EMAIL));
            tokenOption.setEmail(sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_RESET_EMAIL));
            if(tokenVerified){
                tokenOption.setToken(sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN));
            }else{
                tokenOption.setErrorCode(sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_CODE).toString());
                tokenOption.setErrorMessage(sessionService.getAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_MESSAGE));
            }
            sessionService.removeAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN);
            sessionService.removeAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_CODE);
            sessionService.removeAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_MESSAGE);
            model.addAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN_OPTION,tokenOption);
        }

        final String loginView= super.getDefaultLoginPage(loginError, session, model);
        LOG.info("LOGINPAGE getDefaultLoginPagemethodending = " + new Date());
        return loginView;
    }

    private void prepareSamlURL(final Model model) {
        final BaseSiteModel baseSite = baseSiteService.getCurrentBaseSite();
        final LanguageModel language = commonI18NService.getCurrentLanguage();
        final String country = JnjLaCommonUtil.getCountryOrCenca(baseSite.getDefaultCountry().getIsocode()).toLowerCase();
        final String url = Config.getParameter("sso.server.url");

        final String targetURL = sessionService.getAttribute(JnjLACMSSiteFilter.JNJ_LA_TARGET_URL);
        if(StringUtils.isNotBlank(targetURL)){
            model.addAttribute("samlLoginUrl", url + SAML_SINGLESIGNON_SAML + country + STORE + language.getIsocode() + targetURL);
        } else {
            model.addAttribute("samlLoginUrl", url + SAML_SINGLESIGNON_SAML + country + STORE + language.getIsocode() + HOME);
        }

    }

    @Override
    public String getLoginView() {
        LOG.info("In Latam getLoginView() ");


        String loginView = getView(LoginaddonControllerConstants.Views.Pages.Account.AccountLoginPage);
        LOG.info("loginView :: " + loginView);

        return loginView;
    }

    private String getExpiredPasswordPage(final String passwordExpireToken, final String email) {

        try {
            if (commerceUserFacade.verifyPasswordToken(passwordExpireToken, email)) {
                return getView(LoginaddonControllerConstants.Views.Pages.Password.FirstTimeResetPassword);
            } else {
                LOG.error("Password Token is not valid");
            }
        } catch (DuplicateUidException e) {
            LOG.error("Not able to verify password token", e);
        }

        return REDIRECT_PREFIX + "/login";
    }

    public String getView(final String view) {
        return JnjlaloginaddonControllerConstants.ADDON_PREFIX + view;
    }

}