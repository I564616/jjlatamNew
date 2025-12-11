/**
 *
 */
package com.jnj.la.b2b.jnjlaresources.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.session.SessionService;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.jnj.b2b.jnjglobalresources.constants.JnjglobalresourcesConstants;
import com.jnj.b2b.jnjglobalresources.controllers.pages.JnjGTResourcesPageController;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.help.JnjGTContactUsFacade;
import com.jnj.la.b2b.jnjlaresources.controllers.JnjlaresourcesControllerConstants;

/**
 * @author plahiri1
 *
 */
public class JnjLatamResourcesPageController extends JnjGTResourcesPageController {

	@Autowired
	protected SessionService sessionService;

	@Resource(name = "ContactUsFacade")
	protected JnjGTContactUsFacade jnjGTContactUsFacade;

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade customerFacade;

	@Autowired
	protected UserFacade userFacade;

	@Override
	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTCustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	public UserFacade getUserFacade() {
		return userFacade;
	}

	protected static final String CUSTOMER_NAME = "nameOfCustomer";

	protected static final String CUSTOMER_EMAIL = "emailOfCustomer";

	private final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";

	protected static final String SUBJECT_DROP_DOWN = "subjectDropDown";

	protected static final String SELECTED_LINK = "selectedLink";

	@Override
	public ModelAndView showUsefulLinks(final Model model) throws CMSItemNotFoundException {
		// Store the CMS page in the model
		final Object showChangeAccountLink = sessionService.getAttribute(SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		final ContentPageModel pageModel = getContentPageForLabelOrId("UsefulLinks");

		final PageTemplateModel templateModel = pageModel.getMasterTemplate();

		storeCmsPageInModel(model, pageModel);
		if (!userFacade.isAnonymousUser()) {
			model.addAttribute(CUSTOMER_NAME, customerFacade.getCurrentCustomer().getName());
			model.addAttribute(CUSTOMER_EMAIL, customerFacade.getCurrentCustomer().getEmail());
		}
		model.addAttribute(SELECTED_LINK, JnjglobalresourcesConstants.Resources.USEFUL_LINK_COMPONENT_ID);
		model.addAttribute(SUBJECT_DROP_DOWN, jnjGTContactUsFacade.getSubjectDropDown());
		// Set the breadcrumb Key for the breadcrumb navigation
		createBreadCrumbsForResources(model, "");
		// Return the view
		return new ModelAndView(getView(JnjlaresourcesControllerConstants.Views.Pages.Resources.ResourcesPage));
	}

	@Override
	public String getView(final String view) {
		return JnjlaresourcesControllerConstants.ADDON_PREFIX + view;
	}
}
