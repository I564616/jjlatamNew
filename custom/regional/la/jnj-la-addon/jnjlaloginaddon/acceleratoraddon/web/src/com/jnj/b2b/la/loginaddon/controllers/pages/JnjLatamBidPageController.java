package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.b2b.la.loginaddon.controllers.JnjlaloginaddonControllerConstants;
import com.jnj.b2b.la.loginaddon.security.LaB2BUserGroupProvider;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.constants.JnjutilConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.util.impl.JnjLatamCommonFacadeUtilImpl;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 588685 on 25/11/2016.
 */

@Controller
@Scope("tenant")
@RequireHardLogIn
@RequestMapping("/bidPage")
public class JnjLatamBidPageController extends AbstractPageController{

    private static final String LAST_NAME = "LastName";
    private static final String FIRST_NAME = "firstName";
    private static final String EMAIL_ID = "emailId";
    private static final String HOST_NAME = "hostName";
    private static final String DISTRIBUTER_ID = "distributerId";
    private static final String LANGUAGE = "language";
    private static final String BID_CMS_PAGE = "bidPage";
    private static final String BIDS_BREADCRUMB_KEY = "myBid.breadCrumb";
    private static final String HOSTNAME_KEY = "bidPage.sap.crm.url.hostName";

    @Autowired
    private UserService userService;

    @Autowired
    private JnjLatamCommonFacadeUtilImpl jnjLatamCommonFacadeUtil;

    @Autowired
    private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

    @Resource(name = "laB2bUserGroupProvider")
    private LaB2BUserGroupProvider laB2bUserGroupProvider;

    @Autowired
    private JnjCommonFacadeUtil jnjCommonFacadeUtil;

    /**
     * This method is responsible for displaying the My Bid Page.
     *
     * @param model
     *           the model
     * @return JSP Path
     * @throws CMSItemNotFoundException
     *            the cMS item not found exception
     * @throws BusinessException
     *            the business exception
     */
    @GetMapping
    public String getMyBid(final Model model) throws CMSItemNotFoundException, BusinessException
    {
        final String METHOD_NAME = "getMyBid()";

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        if (!laB2bUserGroupProvider.isCurrentUserAuthorizedToBid())
        {
            return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
        }
        JnjGTCoreUtil.logDebugMessage(BID_CMS_PAGE, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLatamBidPageController.class);

        final ContentPageModel pageForRequest = getContentPageForLabelOrId(BID_CMS_PAGE);
        storeCmsPageInModel(model, pageForRequest);
        setUpMetaDataForContentPage(model, pageForRequest);

        setUpBidPageIViewUrl(model);
        model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadCrumbsForBids());

        JnjGTCoreUtil.logDebugMessage(BID_CMS_PAGE, METHOD_NAME, Logging.END_OF_METHOD, JnjLatamBidPageController.class);

        return JnjlaloginaddonControllerConstants.ADDON_PREFIX + JnjlaloginaddonControllerConstants.Views.Pages.MyBid.myBidPage;
    }


    /**
     * Gets the bread crumbs for bids.
     *
     * @return the bread crumbs for bids
     * @throws BusinessException
     *            the business exception
     */
    private List<Breadcrumb> getBreadCrumbsForBids() throws BusinessException
    {
        final List<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new Breadcrumb("/bidPage", jnjCommonFacadeUtil.getMessageFromImpex(BIDS_BREADCRUMB_KEY), null));
        return breadcrumbs;
    }


    /**
     * To set iview url.
     *
     * @param model
     *           the new up bid page iview url
     */
    private void setUpBidPageIViewUrl(final Model model)
    {

        final String hostName = JnJCommonUtil.getValue(HOSTNAME_KEY);
        String distributorId = "";
        if (null != jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit())
            distributorId = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit().getUid();

        if (StringUtils.isNotEmpty(distributorId))
        {
            model.addAttribute(DISTRIBUTER_ID, distributorId);
        }
        if (StringUtils.isNotEmpty(hostName))
        {
            model.addAttribute(HOST_NAME, hostName);
        }

        addUserDetails(model);

    }

    /**
     * To add user details.
     *
     * @param model
     *           the model
     */
    private void addUserDetails(final Model model)
    {

        final UserModel userModel = userService.getCurrentUser();
        final String name = userModel.getName();
        if (userModel instanceof B2BCustomerModel)
        {
            final String email = ((B2BCustomerModel) userModel).getEmail();
            if (StringUtils.isNotEmpty(email))
            {
                model.addAttribute(EMAIL_ID, email);
            }
            if (null != userModel.getSessionLanguage())
            {
                final String language = userModel.getSessionLanguage().getIsocode();
                model.addAttribute(LANGUAGE, language.toUpperCase());
            }


        }
        if (StringUtils.isNotEmpty(name))
        {
            final String[] names = name.split(" ");
            model.addAttribute(FIRST_NAME, names[0]);
            if (names.length >= 2)
            {
                StringBuilder lastName = new StringBuilder();
                for (int i = 1; i < names.length; i++)
                {
                    lastName.append(names[i]);
                }
                model.addAttribute(LAST_NAME, lastName);
            }
        }
    }


}