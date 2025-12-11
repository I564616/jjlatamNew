package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.facades.services.JnjServicesFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnjIndirectFormProcessModel;


public class JnjIndirectPayerFormEmailContext extends CustomerEmailContext
{

	private Map<String, Object> jnjCustomerFormMap = new HashMap<>();
	private Map<String, List<String>> jnjCustomerDetailsMap = new HashMap<>();

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

		if (storeFrontCustomerProcessModel instanceof JnjIndirectFormProcessModel)
		{
			setJnjCustomerFormMap(((JnjIndirectFormProcessModel) storeFrontCustomerProcessModel).getJnjCustomerFormMap());
			setJnjCustomerDetailsMap(((JnjIndirectFormProcessModel) storeFrontCustomerProcessModel).getJnjCustomerDetails());
		}
		put(FROM_EMAIL, jnjConfigService.getConfigValueById(Jnjlab2bcoreConstants.Forms.FORMS_FROM_EMAIL));
		put(FROM_DISPLAY_NAME, jnjConfigService.getConfigValueById(Jnjlab2bcoreConstants.Forms.FORMS_FROM_DISPLAY_NAME));
		if (("addIndirectPayer").equals(getJnjCustomerFormMap().get("formName")))
		{
			put(EMAIL,
					jnjServicesFacade.fetchRecipientEmails((String) getJnjCustomerFormMap().get("formName"),
							(JnJB2bCustomerModel) ((JnjIndirectFormProcessModel) storeFrontCustomerProcessModel).getCustomer(),
							(String) getJnjCustomerFormMap().get("company"), (String) getJnjCustomerFormMap().get("bid")));
		}
		else
		{
			put(EMAIL, jnjServicesFacade.fetchRecipientEmails((String) getJnjCustomerFormMap().get("formName"),
					(JnJB2bCustomerModel) ((JnjIndirectFormProcessModel) storeFrontCustomerProcessModel).getCustomer(), null, null));
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
