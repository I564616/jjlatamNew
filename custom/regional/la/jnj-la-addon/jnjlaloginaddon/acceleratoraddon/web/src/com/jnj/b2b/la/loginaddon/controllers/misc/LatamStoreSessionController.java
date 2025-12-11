package com.jnj.b2b.la.loginaddon.controllers.misc;

import com.jnj.b2b.storefront.controllers.misc.StoreSessionController;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

public class LatamStoreSessionController extends StoreSessionController {

    private static final String LATAM_STORE_SESSION_CONROLLER = "LatamStoreSessionController";
    private static final Class THIS_CLASS = LatamStoreSessionController.class;

    private static final String LANGUAGE = "Language";

    @Override
    public String selectLanguage(@RequestParam("code") String isoCode, HttpServletRequest request) {
        final String METHOD_NAME = "latamSelectLanguage()";
        JnjGTCoreUtil.logDebugMessage(LATAM_STORE_SESSION_CONROLLER, METHOD_NAME,
                Logging.BEGIN_OF_METHOD, THIS_CLASS);

        final String previousLanguage = getStoreSessionFacade().getCurrentLanguage().getIsocode();
        if (isoCode.equalsIgnoreCase(LANGUAGE)){
            isoCode = previousLanguage;
        }

        JnjGTCoreUtil.logDebugMessage(LATAM_STORE_SESSION_CONROLLER, METHOD_NAME,
                Logging.END_OF_METHOD, THIS_CLASS);
        return super.selectLanguage(isoCode, request);
    }
}
