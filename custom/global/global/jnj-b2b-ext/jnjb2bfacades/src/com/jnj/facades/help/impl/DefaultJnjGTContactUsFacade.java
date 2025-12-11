/**
 *
 */
package com.jnj.facades.help.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.help.impl.DefaultJnjContactUsFacade;
import com.jnj.core.event.JnjGTContactUsEvent;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTContactUsData;
import com.jnj.facades.help.JnjGTContactUsFacade;
import com.jnj.core.services.JnjGTContactUsService;
import com.jnj.services.CMSSiteService;
import com.jnj.utils.CommonUtil;
import jakarta.annotation.Resource;

/**
 * @author balinder.singh
 * 
 */
public class DefaultJnjGTContactUsFacade extends DefaultJnjContactUsFacade implements JnjGTContactUsFacade
{
	@Resource(name="GTContactUsService")
	protected JnjGTContactUsService jnjGTContactUsService;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	/** The event service required for the email flow **/
	@Autowired
	protected EventService eventService;

	/** Base Store Service for Email Event Population **/
	@Autowired
	protected BaseStoreService baseStoreService;

	/** Base Site Service for Email Event Population **/
	@Autowired
	protected BaseSiteService baseSiteService;

	/** Internationalization Service for Email Event Population **/
	@Autowired
	protected CommonI18NService commonI18NService;

	/** Session service to save account level attributes **/
	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Autowired
	protected MediaService mediaService;

	@Autowired
	protected CMSSiteService cMSSiteService;

	protected static final String CONTACT_US = "CONTACT US";
	protected static final Logger LOG = Logger.getLogger(DefaultJnjGTContactUsFacade.class);

	/**
	 * This method returns the list of subject with their key to be shown as drop down on contract us page
	 */
	@Override
	public HashMap<String, String> getSubjectDropDown()
	{
		return jnjGTContactUsService.getSubjectDropDown();
	}

	/*
	 * This method returns the email id to which the contact us email will be sent.
	 * 
	 * @see com.jnj.facades.help.JnjGTContactUsFacade#getToEmailID(java.lang.String)
	 */
	@Override
	public String getToEmailID(final String subjectId)
	{
		return jnjGTContactUsService.getEmailForContractUs(subjectId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.help.JnjGTContactUsFacade#sendMail(com.jnj.facades.data.JnjGTContactUsData)
	 */
	@Override
	public void sendMail(final JnjGTContactUsData jnjGTContactUsData)
	{
		final String METHOD_NAME = "sendMail()";
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Publishing event...", LOG);
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		eventService.publishEvent(initializeEvent(jnjGTContactUsData));
	}

	protected JnjGTContactUsEvent initializeEvent(final JnjGTContactUsData jnjGTContactUsData)
	{
		final String METHOD_NAME = "initializeEvent()";
		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		JnjGTContactUsEvent jnjGTContactUsEvent = null;

		CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Populating event", LOG);
		jnjGTContactUsEvent = new JnjGTContactUsEvent();
		jnjGTContactUsEvent.setFromName(jnjGTContactUsData.getFromName());
		jnjGTContactUsEvent.setContactNumber(jnjGTContactUsData.getContactNumber());
		jnjGTContactUsEvent.setFromEmailID(jnjGTContactUsData.getFromEmail());
		jnjGTContactUsEvent.setToEmailID(getToEmailID(jnjGTContactUsData.getSubjectSelected()));
		jnjGTContactUsEvent.setOrderID(jnjGTContactUsData.getOrderID());
		jnjGTContactUsEvent.setDetailIssue(jnjGTContactUsData.getDetailInquiry());
		jnjGTContactUsEvent.setLogoURL(jnjGTContactUsData.getSiteLogoURL() + createMediaLogoURL());
		jnjGTContactUsEvent.setEmailSubject(jnjCommonFacadeUtil.getMessageFromImpex(jnjGTContactUsService
				.getSubjectForContractUs(jnjGTContactUsData.getSubjectSelected())));
		jnjGTContactUsEvent.setProductID(jnjGTContactUsData.getProductID());
		/** populating the event with the basic required values **/
		jnjGTContactUsEvent.setBaseStore(baseStoreService.getCurrentBaseStore());
		jnjGTContactUsEvent.setSite(baseSiteService.getCurrentBaseSite());
		jnjGTContactUsEvent.setCustomer((CustomerModel) userService.getCurrentUser());
		jnjGTContactUsEvent.setLanguage(commonI18NService.getCurrentLanguage());
		jnjGTContactUsEvent.setCurrency(commonI18NService.getCurrentCurrency());
		CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Event populated", LOG);

		CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return jnjGTContactUsEvent;
	}

	/**
	 * This method is responsible for creating the URL for Media (Image)
	 * 
	 * @return mediaURL
	 */
	protected String createMediaLogoURL()
	{
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
				.getActiveCatalogVersion();
		return mediaService.getMedia(currentCatalog, "siteLogoImage").getURL();
	}

}
