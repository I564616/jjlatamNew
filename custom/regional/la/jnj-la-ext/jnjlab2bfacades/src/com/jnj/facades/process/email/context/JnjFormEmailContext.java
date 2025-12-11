package com.jnj.facades.process.email.context;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjFormProcessModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.facades.services.JnjServicesFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JnjFormEmailContext extends CustomerEmailContext {

    private Map<String, Object> jnjCustomerFormMap = new HashMap<String, Object>();
    private Map<String, List<String>> jnjCustomerDetailsMap = new HashMap<String, List<String>>();

    @Autowired
    private JnjServicesFacade jnjServicesFacade;

    @Autowired
    private JnjConfigService jnjConfigService;

    @Autowired
    private CommerceCommonI18NService commerceCommonI18NService;

    @Override
    public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
    {
        storeFrontCustomerProcessModel.setLanguage(commerceCommonI18NService.getCurrentLanguage());

        super.init(storeFrontCustomerProcessModel, emailPageModel);

        if (storeFrontCustomerProcessModel instanceof JnjFormProcessModel)
        {
            setJnjCustomerFormMap(((JnjFormProcessModel) storeFrontCustomerProcessModel).getJnjCustomerFormMap());
            setJnjCustomerDetailsMap(((JnjFormProcessModel) storeFrontCustomerProcessModel).getJnjCustomerDetails());
        }
        put(FROM_EMAIL, jnjConfigService.getConfigValueById(Jnjlab2bcoreConstants.Forms.FORMS_FROM_EMAIL));
        put(FROM_DISPLAY_NAME, jnjConfigService.getConfigValueById(Jnjlab2bcoreConstants.Forms.FORMS_FROM_DISPLAY_NAME));
        if (("addIndirectCustomer").equals(getJnjCustomerFormMap().get("formName")))
        {
            put(EMAIL, jnjServicesFacade.fetchRecipientEmails((String) getJnjCustomerFormMap().get("formName"),
                    (JnJB2bCustomerModel) ((JnjFormProcessModel) storeFrontCustomerProcessModel).getCustomer(),
                    (String) getJnjCustomerFormMap().get("company"), (String) getJnjCustomerFormMap().get("bid")));
        }
        else
        {
            put(EMAIL, jnjServicesFacade.fetchRecipientEmails((String) getJnjCustomerFormMap().get("formName"),
                    (JnJB2bCustomerModel) ((JnjFormProcessModel) storeFrontCustomerProcessModel).getCustomer(), null, null));
        }

    }

    public Map<String, Object> getJnjCustomerFormMap()
    {
        return jnjCustomerFormMap;
    }


    public void setJnjCustomerFormMap(final Map<String, Object> jnjCustomerFormMap)
    {
        this.jnjCustomerFormMap = jnjCustomerFormMap;
    }


    public Map<String, List<String>> getJnjCustomerDetailsMap()
    {
        return jnjCustomerDetailsMap;
    }

    public void setJnjCustomerDetailsMap(final Map<String, List<String>> jnjCustomerDetailsMap)
    {
        this.jnjCustomerDetailsMap = jnjCustomerDetailsMap;
    }
}
