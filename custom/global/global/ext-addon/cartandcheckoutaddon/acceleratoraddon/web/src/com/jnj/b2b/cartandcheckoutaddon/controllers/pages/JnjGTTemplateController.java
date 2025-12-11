package com.jnj.b2b.cartandcheckoutaddon.controllers.pages;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;

import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.facades.template.JnjGTOrderTemplateFacade;


import com.jnj.b2b.cartandcheckoutaddon.controllers.CartandcheckoutaddonControllerConstants;

@Controller
@Scope("tenant")
@RequestMapping(value = "/my-account/template")
public class JnjGTTemplateController extends AbstractPageController
{
	protected static final Logger LOGGER = Logger.getLogger(JnjGTTemplateController.class);

	@Resource(name = "jnjGTOrderTemplateFacade")
	JnjGTOrderTemplateFacade jnjGTOrderTemplateFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	/**
	 * This handler is used to create Order template from session cart or placed order
	 *
	 * @param String
	 *           orderId id of the order which will be used for create template empty/null value will be used session
	 *           cart to create template
	 * @param templateName
	 *           the template name
	 * @param shared
	 *           the shared
	 * @param model
	 *           the model
	 * @return the string
	 */
	@RequestMapping(value = "/save")
	public String createOrderTemplate(@RequestParam(value = "orderId", required = false) final String orderId,
			@RequestParam("templateName") final String templateName, @RequestParam("shared") final boolean shared, final Model model)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Start:- Create Template from Cart/Order");
		}
		boolean templateSaved = false;
		if (StringUtils.isNotEmpty(orderId))
		{
			templateSaved = jnjGTOrderTemplateFacade.createTemplateFromOrder(orderId, templateName, shared);
		}
		else
		{
			templateSaved = jnjGTOrderTemplateFacade.createTemplateFromSessionCart(templateName, shared);
		}
		model.addAttribute("templateSaved", templateSaved);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("End:- Create Template from Cart/Order");
		}
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.SavedTemplatePopup);
	}

	public String getView(String view)
	{
		return CartandcheckoutaddonControllerConstants.ADDON_PREFIX + view;
	}
}