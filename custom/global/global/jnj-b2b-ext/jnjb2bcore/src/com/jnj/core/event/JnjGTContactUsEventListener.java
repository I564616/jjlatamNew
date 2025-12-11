/**
 *
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTContactUsEmailProcessModel;
import com.jnj.utils.CommonUtil;


/**
 * @author balinder.singh
 * 
 */
public class JnjGTContactUsEventListener extends AbstractSiteEventListener<JnjGTContactUsEvent>
{
	@Autowired
	protected CommonI18NService commonI18NService;
	@Autowired
	protected BaseSiteService baseSiteService;
	@Autowired
	protected BaseStoreService baseStoreService;
	@Autowired
	protected UserService userService;

	@Autowired
	protected I18NService i18nService;
	
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}


	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}


	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}


	public UserService getUserService() {
		return userService;
	}
	
	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	protected ModelService modelService;

	protected BusinessProcessService businessProcessService;
	protected static final String CONTACT_US = "CONTACT US";

	protected static final Logger LOG = Logger.getLogger(JnjGTContactUsEventListener.class);

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}


	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final JnjGTContactUsEvent jnjGTContactUsEvent)
	{
		final String METHOD_NAME = "onSiteEvent()";
		//CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		String email;
		final JnjGTContactUsEmailProcessModel jnjGTContactUsEmailProcessModel;
		if (jnjGTContactUsEvent.getCustomer() instanceof JnJB2bCustomerModel)
		{
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Customer is a JnJGTB2bCustomer", LOG);
			final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) jnjGTContactUsEvent.getCustomer();
			email = customer.getEmail();
			jnjGTContactUsEmailProcessModel = (JnjGTContactUsEmailProcessModel) getBusinessProcessService().createProcess(
					"jnjGTContactUsEmailProcess" + "-" + email + "-" + System.currentTimeMillis(), "jnjGTContactUsEmailProcess");

			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Process created", LOG);
			jnjGTContactUsEmailProcessModel.setCustomer(customer);
		}
		else
		{
			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Customer is not a JnJGTB2bCustomer", LOG);
			final CustomerModel customer = jnjGTContactUsEvent.getCustomer();
			email = customer.getContactEmail();
			jnjGTContactUsEmailProcessModel = (JnjGTContactUsEmailProcessModel) getBusinessProcessService().createProcess(
					"jnjGTContactUsEmailProcess" + "-" + email + "-" + System.currentTimeMillis(), "jnjGTContactUsEmailProcess");

			CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Process created", LOG);
			jnjGTContactUsEmailProcessModel.setCustomer(customer);
		}

		//Values from various services
		jnjGTContactUsEmailProcessModel.setCurrency(commonI18NService.getCurrentCurrency());
		jnjGTContactUsEmailProcessModel.setSite(baseSiteService.getCurrentBaseSite());
		jnjGTContactUsEmailProcessModel.setStore(baseStoreService.getCurrentBaseStore());
		jnjGTContactUsEmailProcessModel.setSite(jnjGTContactUsEvent.getSite());
		jnjGTContactUsEmailProcessModel.setLanguage(jnjGTContactUsEvent.getLanguage());
		jnjGTContactUsEmailProcessModel.setStore(jnjGTContactUsEvent.getBaseStore());

		CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Essential data set in the process model", LOG);

		// From email
		jnjGTContactUsEmailProcessModel.setFromEmailID(jnjGTContactUsEvent.getFromEmailID());
		// From name
		jnjGTContactUsEmailProcessModel.setUserName(jnjGTContactUsEvent.getFromName());
		// To email
		jnjGTContactUsEmailProcessModel.setToEmailID(jnjGTContactUsEvent.getToEmailID());
		// Subject of email
		jnjGTContactUsEmailProcessModel.setEmailSubject(jnjGTContactUsEvent.getEmailSubject());
		// Order Id
		jnjGTContactUsEmailProcessModel.setOrderID(jnjGTContactUsEvent.getOrderID());
		
		jnjGTContactUsEmailProcessModel.setProductID(jnjGTContactUsEvent.getProductID());
		// Portal Name
		jnjGTContactUsEmailProcessModel.setPortalName(jnjGTContactUsEvent.getPortalName());
		// Logo URL
		jnjGTContactUsEmailProcessModel.setLogoURL(jnjGTContactUsEvent.getLogoURL());
		// Detail issue
		final String detailIssue = jnjGTContactUsEvent.getDetailIssue();
		LOG.info("detailIssue: " + detailIssue);
		final String detailIssueLineBreak = detailIssue.replace("\n", "<br/>");
		LOG.info("detailIssueLineBreak: " + detailIssueLineBreak);
		final Locale loc = i18nService.getCurrentLocale();
		jnjGTContactUsEmailProcessModel.setDetailIssue(detailIssueLineBreak, loc);
		// Contact Number
		jnjGTContactUsEmailProcessModel.setContactNumber(jnjGTContactUsEvent.getContactNumber());

		CommonUtil.logDebugMessage(
				CONTACT_US,
				METHOD_NAME,
				"Data set in model - From email id :: " + jnjGTContactUsEvent.getFromEmailID() + ", From Name :: "
						+ jnjGTContactUsEvent.getFromName() + ", To Email :: " + jnjGTContactUsEvent.getToEmailID() + ", Subject :: "
						+ jnjGTContactUsEvent.getEmailSubject() + ", Order ID :: " + jnjGTContactUsEvent.getOrderID()
						+ ", Portal Name :: " + jnjGTContactUsEvent.getPortalName() + ", Issue Detail :: "
						+ jnjGTContactUsEvent.getDetailIssue() + ", Contact Number :: " + jnjGTContactUsEvent.getContactNumber(), LOG);

		getModelService().save(jnjGTContactUsEmailProcessModel);

		CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Process Model Saved", LOG);
		getBusinessProcessService().startProcess(jnjGTContactUsEmailProcessModel);

		CommonUtil.logDebugMessage(CONTACT_US, METHOD_NAME, "Process started!", LOG);
		//CommonUtil.logMethodStartOrEnd(CONTACT_US, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjGTContactUsEvent arg0)
	{
		// YTODO Auto-generated method stub
		return true;
	}

}
