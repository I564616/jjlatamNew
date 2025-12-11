package com.jnj.b2b.la.loginaddon.controllers;

import com.jnj.b2b.la.loginaddon.constants.JnjlaloginaddonConstants.Logging;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping("/p/la")
public class JnjLatamUtilsController extends AbstractPageController {

    @Autowired
    protected UserService userService;

    @Autowired
    protected MessageFacadeUtill messageFacadeUtil;

    @Autowired
    protected JnjConfigService jnjConfigService;

    @Autowired
    protected JnjGTCustomerFacade jnjGTCustomerFacade;
    
    
 	@Autowired
 	protected SessionService sessionService;
 	
	@Autowired
	protected ConfigurationService configurationService;

    private static final String LATAM_UTILS_CONTROLLER = "UtilsController";
    private static final Class THIS_CLASS = JnjLatamUtilsController.class;
    
    @GetMapping("/permissions")
    @ResponseBody
    @RequireHardLogIn
    private List<String> getPermissions(final HttpServletRequest request){
        final String methodName = "getPermissions()";
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
        final Set<PrincipalGroupModel> groups = currentUser.getAllGroups();

        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.END_OF_METHOD, THIS_CLASS);
        return mapAllGroupAttributes(groups);
    }

    @GetMapping("/isRegistrationComplete")
    @ResponseBody
    @RequireHardLogIn
    private String isRegistrationComplete(final HttpServletRequest request){
        final String methodName = "isRegistrationComplete()";
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.END_OF_METHOD, THIS_CLASS);
        if (userService.isAnonymousUser(userService.getCurrentUser())) {
            return "true";
        }

        if (!jnjGTCustomerFacade.isResetPasswordComplete()){
            return "resetPassword";
        } else if (jnjGTCustomerFacade.isRegistrationComplete()) {
            return "true";
        } else {
            return "false";
        }

    }

    @GetMapping("/message")
    @ResponseBody
    private String getMessage(final HttpServletRequest request, @RequestParam("code") final String messageCode) throws BusinessException {
        final String methodName = "getMessage()";
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.END_OF_METHOD, THIS_CLASS);
        return messageFacadeUtil.getMessageTextForCode(messageCode);
    }

    @GetMapping("/currentCountry")
    @ResponseBody
    private String getCurrentCountry(final HttpServletRequest request){
        final String methodName = "getCurrentCountry()";
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        CountryModel country = getCmsSiteService().getCurrentSite().getDefaultCountry();
        Locale locale = getI18nService().getCurrentLocale();

        if (country != null)
        {
            JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.END_OF_METHOD, THIS_CLASS);
            return country.getName(locale);
        }

        return "";
    }

    @GetMapping("/contactNumber")
    @ResponseBody
    public String getCountryContactNumber(){
        final String methodName = "getCountryContactNumber()";
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        final CountryModel country = getCmsSiteService().getCurrentSite().getDefaultCountry();
        String configCode = StringUtils.EMPTY;
        String brDepuyConfigCode = StringUtils.EMPTY;

        if (country != null && country.getIsocode() != null && country.getIsocode().equalsIgnoreCase("BR")){
        	configCode = country.getIsocode().toUpperCase() + JnjlaloginaddonControllerConstants.CONTACT_NUMBER_SULFIX;
        	brDepuyConfigCode = JnjlaloginaddonControllerConstants.BR_DEPUY_SYNTHES_PREFIX + country.getIsocode().toUpperCase() + JnjlaloginaddonControllerConstants.CONTACT_NUMBER_SULFIX;
        }
        else if (country != null && country.getIsocode() != null){
            configCode = country.getIsocode().toUpperCase() + JnjlaloginaddonControllerConstants.CONTACT_NUMBER_SULFIX;
        }

        StringBuilder contactNumber = new StringBuilder();
        
        if (country != null && country.getIsocode() != null && country.getIsocode().equalsIgnoreCase("BR")){
       	 contactNumber.append(jnjConfigService.getConfigValueById(configCode));
       	 contactNumber.append(" ").append( configurationService.getConfiguration().getString(JnjlaloginaddonControllerConstants.DEPUY_SYNTHES)).append(" ");
       	 contactNumber.append(jnjConfigService.getConfigValueById(brDepuyConfigCode));
       }
       else {
       	 contactNumber.append(jnjConfigService.getConfigValueById(configCode));
       }

        if (contactNumber != null){
            JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.END_OF_METHOD, THIS_CLASS);
            return contactNumber.toString();
        }

        try {
            JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.END_OF_METHOD, THIS_CLASS);
            return messageFacadeUtil.getMessageTextForCode(JnjlaloginaddonControllerConstants.NO_CONTACT_NUMBER);
        }
        catch (BusinessException exception) {
            JnjGTCoreUtil.logErrorMessage(LATAM_UTILS_CONTROLLER, methodName, exception.getMessage(),
                    exception, THIS_CLASS);
            JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.END_OF_METHOD, THIS_CLASS);
            return StringUtils.EMPTY;
        }
    }

    private List<String> mapAllGroupAttributes(final Set<PrincipalGroupModel> groups)
    {
        final String methodName = "mapAllGroupAttributes()";
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

        final List<String> groupsToBeAdded = new ArrayList<>();

        for (final PrincipalGroupModel group : groups)
        {
            String groupUid = group.getUid();
            if (groupUid.equals(JnjlaloginaddonControllerConstants.Groups.GROUP_PLACE_ORDER)
                    || groupUid.equals(JnjlaloginaddonControllerConstants.Groups.GROUP_ORDER_HISTORY)
                    || groupUid.equals(JnjlaloginaddonControllerConstants.Groups.GROUP_CATALOG))
            {
                groupsToBeAdded.add(group.getUid());
            }
        }
        JnjGTCoreUtil.logDebugMessage(LATAM_UTILS_CONTROLLER, methodName, Logging.END_OF_METHOD, THIS_CLASS);
        return groupsToBeAdded;
    }
    
    
    @GetMapping("/comparePOTrackingId")
    @ResponseBody
    private boolean comparePOTrackingId(@RequestParam("valToCompare") final String valToCompare)
    {
       final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
       List<OrderModel> list = new ArrayList();
       boolean retVal=false;
       list.addAll(currentUser.getOrders());
       for(int i=0;i<list.size();i++)
       {
      	 list.get(i).getPurchaseOrderNumber();
      	 if(list.get(i).getPurchaseOrderNumber().equalsIgnoreCase(valToCompare))
      	 {
      		 retVal=true;
      		 sessionService.setAttribute("valToCompare", valToCompare);
      		 return retVal;
      	 }

       }
   	 
      return retVal;
    }
    
}
