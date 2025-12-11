/**
 * 
 */
package com.jnj.b2b.loginaddon.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
//import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;


/**
 * @author abhishek.b.arora
 * 
 */
@Controller
public class JnjGTTermsAndConditionsController extends AbstractPageController
{
	@GetMapping("/termsAndConditions")
	public String getLegalNotice(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId("termsAndConditionsPage"));

		//return getViewForPage(model);
		return getView(LoginaddonControllerConstants.Views.Pages.Account.termsandconditionsPage);


	}
	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
 }
}
