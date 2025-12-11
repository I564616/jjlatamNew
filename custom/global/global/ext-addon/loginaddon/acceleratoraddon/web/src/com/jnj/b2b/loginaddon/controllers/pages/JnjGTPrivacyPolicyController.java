package com.jnj.b2b.loginaddon.controllers.pages;


import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
/*import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;*/

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;


/**
 * @author abhishek.b.arora
 * 
 */
@Controller
public class JnjGTPrivacyPolicyController extends AbstractPageController
{
	protected static final String PRIVACY_POLICY_PAGE = "privacyPolicyPage";
	protected static final String UPDATED_PRIVACY_POLICY_PAGE = "updatedPrivacyPolicyPage";
	protected static final Logger LOG = Logger.getLogger(JnjGTPrivacyPolicyController.class);

	@GetMapping("/privacyPolicy")
	public String getPrivacyPolicy(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getPrivacyPolicy()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		storeCmsPageInModel(model, getContentPageForLabelOrId(PRIVACY_POLICY_PAGE));
		logMethodStartOrEnd(LoginaddonConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.END_OF_METHOD);
		//return getViewForPage(model);
		return getView(LoginaddonControllerConstants.Views.Pages.Account.privacyandPolicyPage);

	}

	/*
	 * @Override protected String getViewForPage(final Model model) { if (model.containsAttribute(CMS_PAGE_MODEL)) {
	 * final AbstractPageModel page = (AbstractPageModel) model.asMap().get(CMS_PAGE_MODEL); if (page != null) { return
	 * getViewForPage(page); } } return null; }
	 */



	/**
	 * This method fetches the updated privacy policy view
	 * 
	 * @author sanchit.a.kumar
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/updatedPrivacyPolicy")
	public String getUpdatedprivacyPolicy(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getUpdatedprivacyPolicy()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATED_PRIVACY_POLICY_PAGE));
		logMethodStartOrEnd(LoginaddonConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.END_OF_METHOD);
		//return getViewForPage(model);

		return getView(LoginaddonControllerConstants.Views.Pages.Account.updatePrivacyPolicyPage);

	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @author sanchit.a.kumar
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
 }
}
