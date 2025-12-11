package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjLatamAddIndirectPayerDTO;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnjIndirectFormProcessModel;


public class JnjIndirectPayerFormEmailEventListener extends AbstractSiteEventListener<JnjIndirectPayerFormEmailEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	private static final String CITY = "city";
	private static final String ADDRESS = "address";
	private static final String CNPJ_OR_CPF = "cnpjOrCpf";
	private static final String CNPJ_OR_CPF_TEXT = "cnpjOrCpfText";
	private static final String PATIENT_OR_PHYSICIAN_NAME = "cpfPatientOrPhysicianName";
	private static final String PUBLIC_OR_PRIVATE = "publicOrPrivate";
	private static final String STATE = "state";
	private static final String NEIGHBORHOOD = "neighborhood";
	private static final String ZIP_CODE = "zipCode";
	private static final String FORM_NAME = "formName";
	private static final String SUBJECT = "subject";
	private static final String ADD_INDIRECT_PAYER_KEY = "misc.services.addIndirectPayer";
	private static final String SITE_LOGO_PATH = "siteLogoPath";
	private static final String FROM_EMAIL = "fromEmail";
	private static final String FROM_DISPLAY_NAME = "fromDisplayName";
	private static final String BID = "bid";
	private static final String COMPANY = "company";
	private static final String PAYER_NAME = "payerName";
	private static final String PAYER_TYPE = "payerType";

	private static final String FUNCTIONALITY_NAME = Jnjlab2bcoreConstants.Logging.FORMS_NAME;

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
	private MessageFacadeUtill messageFacade;


	@Override
	protected void onSiteEvent(final JnjIndirectPayerFormEmailEvent event)
	{
		final String methodName = "onSiteEvent()";
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjIndirectPayerFormEmailEventListener.class);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "FORM NAME :: " + event.getJnjFormDTO().getFormName(),
				JnjIndirectPayerFormEmailEventListener.class);

		if (StringUtils.equals(event.getJnjFormDTO().getFormName(), Jnjlab2bcoreConstants.Forms.ADD_INDIRECT_PAYER_CMS_PAGE))
		{
			final JnjIndirectFormProcessModel jnjFormProcessModel = (JnjIndirectFormProcessModel) businessProcessService
					.createProcess("jnjAddIndirectFormPayerEmailProcess" + "-" + System.currentTimeMillis(),
							"jnjAddIndirectFormPayerEmailProcess");
			final JnjLatamAddIndirectPayerDTO jnjAddIndirectPayerDTO = (JnjLatamAddIndirectPayerDTO) event.getJnjFormDTO();
			final Map<String, Object> jnjAddIndirectPayer = new HashMap<String, Object>();
			jnjAddIndirectPayer.put(ADDRESS, jnjAddIndirectPayerDTO.getAddress());
			jnjAddIndirectPayer.put(PAYER_NAME, jnjAddIndirectPayerDTO.getPayerName());
			jnjAddIndirectPayer.put(CNPJ_OR_CPF, jnjAddIndirectPayerDTO.getCnpjOrCpf());
			jnjAddIndirectPayer.put(CNPJ_OR_CPF_TEXT, jnjAddIndirectPayerDTO.getCnpjOrCpfText());
			jnjAddIndirectPayer.put(PATIENT_OR_PHYSICIAN_NAME, jnjAddIndirectPayerDTO.getCpfPatientOrPhysicianName());
			jnjAddIndirectPayer.put(PAYER_TYPE, jnjAddIndirectPayerDTO.getPayerType());
			jnjAddIndirectPayer.put(PUBLIC_OR_PRIVATE, jnjAddIndirectPayerDTO.getPublicOrPrivate());
			jnjAddIndirectPayer.put(STATE, jnjAddIndirectPayerDTO.getState());
			jnjAddIndirectPayer.put(NEIGHBORHOOD, jnjAddIndirectPayerDTO.getNeighborhood());
			jnjAddIndirectPayer.put(ZIP_CODE, jnjAddIndirectPayerDTO.getZipCode());
			jnjAddIndirectPayer.put(CITY, jnjAddIndirectPayerDTO.getCity());
			jnjAddIndirectPayer.put(FORM_NAME, jnjAddIndirectPayerDTO.getFormName());
			jnjAddIndirectPayer.put(SITE_LOGO_PATH, jnjAddIndirectPayerDTO.getServerURL());
			jnjAddIndirectPayer.put(FROM_EMAIL, jnjAddIndirectPayerDTO.getFromEmail());
			jnjAddIndirectPayer.put(FROM_DISPLAY_NAME, jnjAddIndirectPayerDTO.getFromDisplayName());
			jnjAddIndirectPayer.put(BID, jnjAddIndirectPayerDTO.getBid());
			jnjAddIndirectPayer.put(COMPANY, jnjAddIndirectPayerDTO.getCompany());
			try
			{
				jnjAddIndirectPayer.put(SUBJECT, messageFacade.getMessageTextForCode(ADD_INDIRECT_PAYER_KEY));
			}
			catch (final BusinessException businessException)
			{
				JnjGTCoreUtil.logErrorMessage(FUNCTIONALITY_NAME, methodName, businessException.getMessage(), businessException,
						JnjIndirectPayerFormEmailEventListener.class);
			}
			jnjFormProcessModel.setJnjCustomerFormMap(jnjAddIndirectPayer);
			populateProcessModel(event, jnjFormProcessModel);
		}

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjIndirectPayerFormEmailEventListener.class);
	}

	@Override
	protected boolean shouldHandleEvent(final JnjIndirectPayerFormEmailEvent event)
	{
		final String methodName = "shouldHandleEvent()";
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjIndirectPayerFormEmailEventListener.class);
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD,
				JnjIndirectPayerFormEmailEventListener.class);
		return true;
	}

	private void populateProcessModel(final JnjIndirectPayerFormEmailEvent event,
			final JnjIndirectFormProcessModel jnjFormProcessModel)
	{
		final String methodName = "populateProcessModel()";
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjIndirectPayerFormEmailEventListener.class);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "Populating process model",
				JnjIndirectPayerFormEmailEventListener.class);
		jnjFormProcessModel.setSite(event.getSite());
		jnjFormProcessModel.setCustomer(event.getCustomer());
		jnjFormProcessModel.setLanguage(event.getLanguage());
		jnjFormProcessModel.setCurrency(event.getCurrency());
		jnjFormProcessModel.setStore(event.getBaseStore());
		getModelService().save(jnjFormProcessModel);
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, "Starting process model",
				JnjIndirectPayerFormEmailEventListener.class);

		getBusinessProcessService().startProcess(jnjFormProcessModel);
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD,
				JnjIndirectPayerFormEmailEventListener.class);
	}


}
