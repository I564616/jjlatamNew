package com.jnj.facades.services.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjConsignmentIssueDTO;
import com.jnj.core.dto.JnjFormDTO;
import com.jnj.core.event.JnjFormEmailEvent;
import com.jnj.core.event.JnjIndirectPayerFormEmailEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.customer.JnjLatamCustomerFacade;
import com.jnj.facades.services.JnjServicesFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import org.springframework.beans.factory.annotation.Autowired;


public class JnjServicesFacadeImpl implements JnjServicesFacade {
	@Autowired
	private MediaService mediaService;
	@Autowired
	private CMSSiteService cMSSiteService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CommonI18NService commonI18NService;
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private JnjConfigService jnjConfigService;
	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;
	@Autowired
	private JnjLatamCustomerFacade jnjLatamCustomerFacade;
	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	private static final String FUNCTIONALITY_NAME = "ServicesFacade";
	private static final Class CURRENT_CLASS = JnjServicesFacadeImpl.class;

	@Override
	public void sendEmailToUser(final JnjFormDTO dto) {
		final String METHOD_NAME = "sendEmailToUser()";

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "Going to publish event", CURRENT_CLASS);
		dto.setServerURL(dto.getServerURL() + createMediaLogoURL());
		eventService.publishEvent(initializeEvent(new JnjFormEmailEvent(dto)));
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "Event Published", CURRENT_CLASS);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.END_OF_METHOD, CURRENT_CLASS);
	}

	@Override
	public void sendEmailToUserForIndirectPayer(final JnjFormDTO dto) {
		final String methodName = "sendEmailToUserForIndirectPayer()";

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "Going to publish event", CURRENT_CLASS);
		dto.setServerURL(dto.getServerURL() + createMediaLogoURL());
		eventService.publishEvent(initializeEventForIndirectPayer(new JnjIndirectPayerFormEmailEvent(dto)));
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "Event Published", CURRENT_CLASS);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, CURRENT_CLASS);
	}

	private String createMediaLogoURL() {
		final String METHOD_NAME = "createMediaLogoURL()";

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "currentCatalog URL :: " + currentCatalog, CURRENT_CLASS);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.END_OF_METHOD, CURRENT_CLASS);
		return mediaService.getMedia(currentCatalog, "siteLogoImage").getURL();
	}

	private JnjFormEmailEvent initializeEvent(final JnjFormEmailEvent event) {
		final String METHOD_NAME = "initializeEvent()";

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "Initializing event object", CURRENT_CLASS);
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer((CustomerModel) userService.getCurrentUser());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.END_OF_METHOD, CURRENT_CLASS);
		return event;
	}

	private JnjIndirectPayerFormEmailEvent initializeEventForIndirectPayer(final JnjIndirectPayerFormEmailEvent event) {
		final String methodName = "initializeEventForIndirectPayer()";

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "Initializing event object", CURRENT_CLASS);
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer((CustomerModel) userService.getCurrentUser());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, CURRENT_CLASS);
		return event;
	}

	@Override
	public String fetchRecipientEmails(final String formName, final JnJB2bCustomerModel customer, final String company, final String bid) {
		final String METHOD_NAME = "fetchRecipientEmails()";

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		String toEmailAddress = null;

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "B2B Unit fetched :: " + customer, CURRENT_CLASS);

		final JnJB2BUnitModel jnjB2BUnitModel = (JnJB2BUnitModel) customer.getDefaultB2BUnit();

		final CountryModel country = jnjB2BUnitModel.getCountry();
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "Country fetched :: " + country, CURRENT_CLASS);

		if (null != country) {
			final String countryCode = country.getIsocode();
			final String key = countryCode + Jnjb2bCoreConstants.ContactUs.UNDERSCORE_SYMBOL + formName;
			String finalKey;
			if (null != company && null != bid) {
				if (bid.equalsIgnoreCase(jnjCommonFacadeUtil.getMessageFromImpex("common.label.no"))) {
					finalKey = key + "Bid";
				} else if (company.equalsIgnoreCase(jnjCommonFacadeUtil.getMessageFromImpex("text.uploadOrder.pharma"))) {
					finalKey = key + "Pharma";
				} else {
					finalKey = key + "Medical";
				}
			} else {
				finalKey = key;
			}
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "countryCode fetched :: " + countryCode, CURRENT_CLASS);
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "Key created :: " + finalKey, CURRENT_CLASS);
			JnjGTCoreUtil.logInfoMessage(FUNCTIONALITY_NAME, METHOD_NAME,
					"Calling DAO Layer for fetching Contact Us email address :: key - " + finalKey, CURRENT_CLASS);
			toEmailAddress = jnjConfigService.getConfigValueById(finalKey);
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "toEmailAddress :: " + toEmailAddress, CURRENT_CLASS);
		} else {
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME,
					"Country was found to be null, returning a null email address", CURRENT_CLASS);
		}

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		return toEmailAddress;
	}

	@Override
	public JnjConsignmentIssueDTO setInfoForConsignmentForm(final JnjConsignmentIssueDTO jnjConsignmentIssueDTO)
	{
		final String methodName = "setInfoForConsignmentForm()";

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		/** Setting additional information **/
		if (null != jnjLatamCustomerFacade.getCurrentGTCustomer()) {
			jnjConsignmentIssueDTO.setContactFirstName(jnjLatamCustomerFacade.getCurrentGTCustomer().getFirstName());
			jnjConsignmentIssueDTO.setContactLastName(jnjLatamCustomerFacade.getCurrentGTCustomer().getLastName());
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "Logged in user name set :: "
					+ jnjConsignmentIssueDTO.getContactFirstName() + " " + jnjConsignmentIssueDTO.getContactLastName(), CURRENT_CLASS);
		} else {
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "current customer found to be NULL", CURRENT_CLASS);
		}
		if (null != jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit()) {
			jnjConsignmentIssueDTO.setCustomerName(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit().getLocName());
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName,
					"customer name set :: " + jnjConsignmentIssueDTO.getCustomerName(), CURRENT_CLASS);
			jnjConsignmentIssueDTO.setSoldTo(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit().getUid());
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "sold to set :: " + jnjConsignmentIssueDTO.getSoldTo(),
					CURRENT_CLASS);
			if (null != jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit().getIndicator() && jnjGetCurrentDefaultB2BUnitUtil
					.getDefaultB2BUnit().getIndicator().equalsIgnoreCase(Jnjb2bCoreConstants.HOSPITAL)) {
				jnjConsignmentIssueDTO.setHospital(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit().getLocName());
				JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName,
						"hospital set :: " + jnjConsignmentIssueDTO.getHospital(), CURRENT_CLASS);
			} else {
				jnjConsignmentIssueDTO.setHospital("");
			}
		} else {
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "default B2B Unit found to be NULL", CURRENT_CLASS);
		}
		if (null != (jnjLatamCustomerFacade.getCurrentGTCustomer()).getDefaultShippingAddress()) {
			if (null != (jnjLatamCustomerFacade.getCurrentGTCustomer()).getDefaultShippingAddress().getTown()
					&& null != (jnjLatamCustomerFacade.getCurrentGTCustomer()).getDefaultShippingAddress().getRegion()) {
				jnjConsignmentIssueDTO
						.setCityOrState((jnjLatamCustomerFacade.getCurrentGTCustomer()).getDefaultShippingAddress().getTown()
								+ Jnjlab2bcoreConstants.Forms.SLASH_SYMBOL + (jnjLatamCustomerFacade.getCurrentGTCustomer())
										.getDefaultShippingAddress().getRegion().getName());
				JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName,
						"City or State set :: " + jnjConsignmentIssueDTO.getCityOrState(), CURRENT_CLASS);
			} else if (null == (jnjLatamCustomerFacade.getCurrentGTCustomer()).getDefaultShippingAddress().getTown()
					&& null != (jnjLatamCustomerFacade.getCurrentGTCustomer()).getDefaultShippingAddress().getRegion()) {
				jnjConsignmentIssueDTO
						.setCityOrState(Jnjlab2bcoreConstants.Forms.DASH_SYMBOL + Jnjlab2bcoreConstants.Forms.SLASH_SYMBOL
								+ jnjLatamCustomerFacade.getCurrentGTCustomer().getDefaultBillingAddress().getRegion().getName());
				JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName,
						"City Null, State set :: " + jnjConsignmentIssueDTO.getCityOrState(), CURRENT_CLASS);
			} else if (null != (jnjLatamCustomerFacade.getCurrentGTCustomer()).getDefaultShippingAddress().getTown()
					&& null == (jnjLatamCustomerFacade.getCurrentGTCustomer()).getDefaultShippingAddress().getRegion()) {
				jnjConsignmentIssueDTO
						.setCityOrState(jnjLatamCustomerFacade.getCurrentGTCustomer().getDefaultBillingAddress().getTown());
				JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName,
						"State Null, City set :: " + jnjConsignmentIssueDTO.getCityOrState(), CURRENT_CLASS);
			} else {
				JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "City and state both null.", CURRENT_CLASS);
			}
		} else {
			JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "default billing address found to be NULL", CURRENT_CLASS);
		}
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, CURRENT_CLASS);
		return jnjConsignmentIssueDTO;
	}

}
