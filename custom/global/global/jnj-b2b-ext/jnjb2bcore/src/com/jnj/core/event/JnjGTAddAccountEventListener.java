package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjCompanyInfoData;
import com.jnj.core.model.JnjGTAddAccountEmailProcessModel;
import org.springframework.util.CollectionUtils;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.model.JnjConfigModel;
import java.util.List;
import com.jnj.services.MessageService;
import de.hybris.platform.servicelayer.i18n.I18NService;



/**
 * @author komal.sehgal
 * 
 */
public class JnjGTAddAccountEventListener extends AbstractSiteEventListener<JnjGTAddAccountEvent>
{

	private static final Logger LOG = Logger.getLogger(JnjGTAddAccountEventListener.class);
	private static final String PURCHASE_PRODUCT = "purchaseProduct";
	private ModelService modelService;
	private BusinessProcessService businessProcessService;
	@Autowired
	private CommonI18NService commonI18NService;
	@Autowired
	private UserService userService;
	@Autowired
	private MessageFacadeUtill messageFacade;
	
	
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public UserService getUserService() {
		return userService;
	}

	public MessageFacadeUtill getMessageFacade() {
		return messageFacade;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
	
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Autowired
	private JnjConfigService jnjConfigService;
	@Autowired
	protected MessageService messageService;

	/** I18NService to retrieve the current locale. */
	@Autowired
	protected I18NService i18nService;

	@Override
	protected void onSiteEvent(final JnjGTAddAccountEvent event)
	{

		logMethodStartOrEnd(Logging.ADD_ACCOUNT_EMAIL, "onSiteEvent()", Logging.BEGIN_OF_METHOD);
		logDebugMessage(Logging.ADD_ACCOUNT_EMAIL, "sendEmailToCSRr()", "Start setting the process Model");
		final JnjGTAddAccountEmailProcessModel jnjGTAddAccountEmailProcessModel = (JnjGTAddAccountEmailProcessModel) getBusinessProcessService()
				.createProcess("" + "-" + System.currentTimeMillis(), "jnjGTAddAccountCSREmailProcess");
		final JnjCompanyInfoData companyData = event.getCompanyData();
		jnjGTAddAccountEmailProcessModel.setAccountName(companyData.getAccountName());
		jnjGTAddAccountEmailProcessModel.setSector(companyData.getSector());
		jnjGTAddAccountEmailProcessModel.setGln(companyData.getgLN());
		jnjGTAddAccountEmailProcessModel.setInitialOpeningOrderAmount(companyData.getInitialOpeningOrderAmount());
		jnjGTAddAccountEmailProcessModel.setSubsidiary(companyData.getSubsidiaryOf());
		jnjGTAddAccountEmailProcessModel.setSalesAndUseTaxFlag(companyData.getSalesAndUseTaxFlag().booleanValue());
		jnjGTAddAccountEmailProcessModel.setEstimatedAmountPerYear(companyData.getEstimatedAmountPerYear());
		jnjGTAddAccountEmailProcessModel.setShipToCountry(commonI18NService.getCountry(companyData.getShipToCountry()).getName());
		jnjGTAddAccountEmailProcessModel.setShipToLine1(companyData.getShipToLine1());
		jnjGTAddAccountEmailProcessModel.setShipToLine2(companyData.getShipToLine2());
		jnjGTAddAccountEmailProcessModel.setShipToRegion(companyData.getShipToState());
		jnjGTAddAccountEmailProcessModel.setShipToTown(companyData.getShipToCity());
		jnjGTAddAccountEmailProcessModel.setShipToPostalCode(companyData.getShipToZipCode());
		jnjGTAddAccountEmailProcessModel.setBillToCountry(commonI18NService.getCountry(companyData.getShipToCountry()).getName());
		jnjGTAddAccountEmailProcessModel.setBillToLine1(companyData.getBillToLine1());
		jnjGTAddAccountEmailProcessModel.setBillToLine2(companyData.getBillToLine2());
		jnjGTAddAccountEmailProcessModel.setBillToRegion(companyData.getBillToState());
		jnjGTAddAccountEmailProcessModel.setBillToTown(companyData.getBillToCity());
		jnjGTAddAccountEmailProcessModel.setBillToPostalCode(companyData.getShipToZipCode());
		jnjGTAddAccountEmailProcessModel.setEmail(event.getCustomer().getUid());
		jnjGTAddAccountEmailProcessModel.setFirstName(event.getCustomer().getName());
		jnjGTAddAccountEmailProcessModel.setFirstName(event.getCustomer().getName());
		final StringBuilder productsPurchase = new StringBuilder();
		
		if(companyData.getMedicalProductsPurchase() != null && !CollectionUtils.isEmpty(companyData.getMedicalProductsPurchase())){
			for (final String medicalProduct : companyData.getMedicalProductsPurchase())
			{
				//LOG.info("medicalProduct="+medicalProduct);
				final String value = fetchValuesFromConfig(PURCHASE_PRODUCT, medicalProduct);
				//LOG.info("valuevalue="+value);
				/** appending and creating a comma separated string of products **/
				productsPurchase.append(value);
				productsPurchase.append(Jnjb2bCoreConstants.Order.SEMI_COLON);
			}			
		}
		
		if(productsPurchase.length() > 0){
			/** removing last comma delimiter **/
			final String finalString = productsPurchase.substring(0, productsPurchase.lastIndexOf(Jnjb2bCoreConstants.Order.SEMI_COLON));
			if (finalString.length() > 0)
			{
				jnjGTAddAccountEmailProcessModel.setProductsPurchased(finalString);
			}
		}
		try
		{
			jnjGTAddAccountEmailProcessModel.setTypeOfBuisness(messageFacade.getMessageTextForCode(companyData.getTypeOfBusiness()));
			jnjGTAddAccountEmailProcessModel.setInitialOpeningOrderAmount(messageFacade.getMessageTextForCode(companyData
					.getInitialOpeningOrderAmount()));
			jnjGTAddAccountEmailProcessModel.setSubject(messageFacade
					.getMessageTextForCode(Jnjb2bCoreConstants.Profile.Add_NEW_ACCOUNT_EMAIL_SUBJECT));
		}
		catch (final BusinessException exp)
		{

			LOG.error("Unable to fetch subject message for ::" + Jnjb2bCoreConstants.Profile.Add_NEW_ACCOUNT_EMAIL_SUBJECT);
		}
		populateProcessModel(event, jnjGTAddAccountEmailProcessModel);

	}
	
	/*Method created to fetch values from properties file.*/
	private String fetchValuesFromConfig(final String id, final String key)
	{

		final String METHOD_NAME = "fetchValuesFromConfig()";
		
		final List<JnjConfigModel> configModel = jnjConfigService.getConfigModelsByIdAndKey(id, key);
		String value = null;
		for (final JnjConfigModel configData : configModel)
		{
			
			
			try {
				value=messageService.getMessageForCode(configData.getKey(), i18nService.getCurrentLocale());
			} 
			catch (BusinessException e) {
			}
			break;
		}
	
		return value;
	}
	private void populateProcessModel(final JnjGTAddAccountEvent event,
			final JnjGTAddAccountEmailProcessModel jnjGTAddAccountEmailProcessModel)
	{
		logMethodStartOrEnd(Logging.ADD_ACCOUNT_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD);
		logDebugMessage(Logging.ADD_ACCOUNT_EMAIL, "sendEmailToUser()", "Populating process model");
		jnjGTAddAccountEmailProcessModel.setSite(event.getSite());
		jnjGTAddAccountEmailProcessModel.setCustomer(event.getCustomer());
		jnjGTAddAccountEmailProcessModel.setLanguage(event.getLanguage());
		jnjGTAddAccountEmailProcessModel.setCurrency(event.getCurrency());
		jnjGTAddAccountEmailProcessModel.setStore(event.getBaseStore());
		getModelService().save(jnjGTAddAccountEmailProcessModel);
		logDebugMessage(Logging.ADD_ACCOUNT_EMAIL, "sendEmailToUser()", "Starting the process");
		getBusinessProcessService().startProcess(jnjGTAddAccountEmailProcessModel);
		logMethodStartOrEnd(Logging.ADD_ACCOUNT_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD);
	}




	@Override
	protected boolean shouldHandleEvent(final JnjGTAddAccountEvent paramT)
	{
		logMethodStartOrEnd(Logging.ADD_ACCOUNT_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Logging.ADD_ACCOUNT_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD);
		return true;
	}

	private void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	private void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}


}