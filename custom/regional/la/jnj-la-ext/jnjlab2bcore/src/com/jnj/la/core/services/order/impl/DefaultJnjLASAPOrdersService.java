package com.jnj.la.core.services.order.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.event.JnjOrderStatusNotificationEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.synchronizeOrders.impl.DefaultJnjSAPOrdersService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.order.JnjLaSAPOrdersDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class DefaultJnjLASAPOrdersService extends DefaultJnjSAPOrdersService {

    private static final Class CLASS_NAME = DefaultJnjLASAPOrdersService.class;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private JnjLaSAPOrdersDao jnjLaSAPOrdersDao;
    
    
    @Override
    public void sendStatusChangeNotification(final CustomerModel customer, final String sapOrderNumber, final String clientOrderNumber, final String jnjOrderNumber, final OrderStatus currentStatus, final OrderStatus previousStatus, final String baseUrl, final Boolean isSyncOrder, final String mediaLogoURL, final String toEmail) {

        final String methodName = "sendStatusChangeNotification";

        JnjGTCoreUtil.logDebugMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime(), CLASS_NAME);

        final JnjOrderStatusNotificationEvent event = new JnjOrderStatusNotificationEvent();
        final CMSSiteModel currentSiteForUser = getCurrentSiteForUser(customer, methodName, event);

        if (currentSiteForUser != null) {
            final CatalogVersionModel currentCatalog = currentSiteForUser.getContentCatalogs().get(0).getActiveCatalogVersion();

            event.setLanguage(customer.getSessionLanguage());
            event.setCurrency(customer.getSessionCurrency());
            event.setCustomer(customer);

            event.setCurrentStatus(currentStatus);
            event.setPreviousStatus(previousStatus);
            if (null != clientOrderNumber) {
                event.setJnjOrderNumber(clientOrderNumber);
            } else {
                event.setJnjOrderNumber(jnjOrderNumber);
            }
            event.setSapOrderNumber(sapOrderNumber);
            event.setClientOrderNumber(jnjOrderNumber);
            event.setSyncOrderNotification(isSyncOrder);
            event.setBaseUrl(baseUrl);
            event.setMediaUrl(Config.getParameter(Jnjb2bCoreConstants.EMAIL_LOG_HOSTNAME) + mediaService.getMedia(currentCatalog, "siteLogoImage").getURL());

            JnjGTCoreUtil.logDebugMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, "Sending Order Status Email Notification for the Order with JnJ Order No. [ " + jnjOrderNumber + " ])" + "-" + JnJCommonUtil.getCurrentDateTime(), CLASS_NAME);
            JnjGTCoreUtil.logDebugMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, "Sending Order Status Email Notification for the Order with SAP Order No. [ " + jnjOrderNumber + " ])" + "-" + JnJCommonUtil.getCurrentDateTime(), CLASS_NAME);
            JnjGTCoreUtil.logDebugMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, "Sending Order Status Email Notification for the User with name [ " + customer.getUid() + " ]" + "-" + JnJCommonUtil.getCurrentDateTime(), CLASS_NAME);

            getEventService().publishEvent(event);
        }

        JnjGTCoreUtil.logDebugMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime(), CLASS_NAME);
    }

    private CMSSiteModel getCurrentSiteForUser(CustomerModel customer, String methodName, JnjOrderStatusNotificationEvent event) {
        CMSSiteModel currentSiteForUser = null;
        final Set<PrincipalGroupModel> compositeSubGroups = customer.getGroups();
        for (final PrincipalGroupModel group : compositeSubGroups) {
            if (group instanceof JnJB2BUnitModel) {
                final CountryModel countryModel = ((JnJB2BUnitModel) group).getCountry();
                if (countryModel != null) {
                    final String country = countryModel.getIsocode();
                    final String countryList = Config.getParameter(Jnjb2bCoreConstants.KEY_Valid_COUNTRIES);
                    String baseStore = null;
                    String cmSite = null;
                    if (country.equals(Jnjb2bCoreConstants.COUNTRY_ISO_BRAZIL) || country.equals(Jnjb2bCoreConstants.COUNTRY_ISO_MEXICO)) {
                        baseStore = country.toLowerCase() + Jnjb2bCoreConstants.UserRoles.BASE_STORE;
                        cmSite = country.toLowerCase() + Jnjb2bCoreConstants.UserRoles.CMSSITE;
                    } else if (countryList.contains(country)) {
                        baseStore = Jnjlab2bcoreConstants.COUNTRY_ISO_ARGENTINA.toLowerCase() + Jnjb2bCoreConstants.UserRoles.BASE_STORE;
                        cmSite = Jnjlab2bcoreConstants.COUNTRY_ISO_ARGENTINA.toLowerCase() + Jnjb2bCoreConstants.UserRoles.CMSSITE;
                    }
                    try {
                        event.setBaseStore(getBaseStoreService().getBaseStoreForUid(baseStore));
                        currentSiteForUser = (CMSSiteModel) getBaseSiteService().getBaseSiteForUID(cmSite);
                        event.setSite(currentSiteForUser);

                    } catch (final UnknownIdentifierException e) {

                        JnjGTCoreUtil.logErrorMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, "not found. Error:" + "-" + JnJCommonUtil.getCurrentDateTime(), e, CLASS_NAME);
                    } catch (final IllegalArgumentException e) {

                        JnjGTCoreUtil.logErrorMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, e.getMessage() + "-" + JnJCommonUtil.getCurrentDateTime(), e, CLASS_NAME);
                    } catch (final Exception e) {

                        JnjGTCoreUtil.logErrorMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, "Error definding Base Store or CMSsite. Error:" + "-" + JnJCommonUtil.getCurrentDateTime(), e, CLASS_NAME);
                    }
                }
            }
        }
        return currentSiteForUser;
    }

    @Override
    public void sendStatusChangeNotification(final CustomerModel customer, final String orderCode, final String baseUrl, final String mediaLogoURL, String toEmail) {
        final String methodName = "sendStatusChangeNotification";
        JnjGTCoreUtil.logDebugMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, "bigin of method" + "-" + JnJCommonUtil.getCurrentDateTime(), CLASS_NAME);

        final JnjOrderStatusNotificationEvent event = new JnjOrderStatusNotificationEvent();

        event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
        event.setSite(getBaseSiteService().getCurrentBaseSite());
        event.setLanguage(getCommonI18NService().getCurrentLanguage());
        event.setCurrency(getCommonI18NService().getCurrentCurrency());
        event.setCustomer(customer);
        event.setOrderCode(orderCode);
        event.setBaseUrl(baseUrl);
        event.setMediaUrl(baseUrl + mediaLogoURL);

        try {
            getEventService().publishEvent(event);
        } catch (final Exception e) {
            JnjGTCoreUtil.logErrorMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, "Exception occured while publishing order status notification.." + "-" + JnJCommonUtil.getCurrentDateTime(), e, CLASS_NAME);
        }

        JnjGTCoreUtil.logDebugMessage(Logging.EMAIL_NOTIFICATION_PROCESS, methodName, "Exception occured while publishing order status notification.." + "-" + JnJCommonUtil.getCurrentDateTime(), CLASS_NAME);
    }

    @Override
    public OrderEntryModel getExistingOrderEntry(final String orderEntryNumber, final String orderNumber) {
        return getJnjSAPOrdersDao().getExistingOrderEntryByEntryNumber(orderEntryNumber, orderNumber);
    }

}