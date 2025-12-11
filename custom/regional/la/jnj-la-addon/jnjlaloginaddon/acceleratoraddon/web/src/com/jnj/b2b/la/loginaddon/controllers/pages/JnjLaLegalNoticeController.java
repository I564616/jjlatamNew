package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.la.loginaddon.constants.JnjlaloginaddonConstants;
import com.jnj.b2b.la.loginaddon.controllers.JnjlaloginaddonControllerConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.loginaddon.controllers.pages.JnjGTLegalNoticeController;
import com.jnj.b2b.loginaddon.controllers.pages.JnjGTPrivacyPolicyController;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * Created by 588685 on 10/02/2017.
 */
public class JnjLaLegalNoticeController extends JnjGTLegalNoticeController {

    public static final String LEGAL_NOTICE_PAGE = "legalNoticePage";

    @Autowired
    JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;


    protected static final Logger LOG = Logger.getLogger(JnjLaLegalNoticeController.class);

    @Override
    public String getLegalNotice(Model model) throws CMSItemNotFoundException {
        final String METHOD_NAME = "getLegalNotice()";
        logMethodStartOrEnd(JnjlaloginaddonConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, JnjlaloginaddonConstants.Logging.BEGIN_OF_METHOD);

        LOG.info(METHOD_NAME + LoginaddonConstants.Logging.HYPHEN + "Looking for country code");
        final String countryInfo = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
        LOG.info(METHOD_NAME + LoginaddonConstants.Logging.HYPHEN + "Country code found: " + countryInfo.toLowerCase());
        model.addAttribute("isoCode", countryInfo.toLowerCase());

        storeCmsPageInModel(model, getContentPageForLabelOrId(LEGAL_NOTICE_PAGE));
        logMethodStartOrEnd(JnjlaloginaddonConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, JnjlaloginaddonConstants.Logging.END_OF_METHOD);

        return getView(LoginaddonControllerConstants.Views.Pages.Account.legalnoticepage);
    }

    @Override
    public String getView(String view) {
        return JnjlaloginaddonControllerConstants.ADDON_PREFIX + view;
    }

    protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug(functionalityName + LoginaddonConstants.Logging.HYPHEN + methodName + LoginaddonConstants.Logging.HYPHEN + entryOrExit + LoginaddonConstants.Logging.HYPHEN
                    + System.currentTimeMillis());
        }
    }
}