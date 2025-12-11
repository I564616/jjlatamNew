package com.jnj.core.event;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjLatamAddIndirectCustomerDTO;
import com.jnj.core.dto.JnjLatamConsignmentIssueDTO;
import com.jnj.core.model.JnjFormProcessModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JnjFormEventListener extends AbstractSiteEventListener<JnjFormEmailEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	private static final String CITY = "city";
	private static final String ADDRESS = "address";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String CNPJ_OR_CPF = "cnpjOrCpf";
	private static final String CNPJ_OR_CPF_TEXT = "cnpjOrCpfText";
	private static final String PATIENT_OR_PHYSICIAN_NAME = "cpfPatientOrPhysicianName";
	private static final String CUSTOMER_TYPE = "customerType";
	private static final String PUBLIC_OR_PRIVATE = "publicOrPrivate";
	private static final String STATE = "state";
	private static final String NEIGHBORHOOD = "neighborhood";
	private static final String ZIP_CODE = "zipCode";
	private static final String FORM_NAME = "formName";
	private static final String SUBJECT = "subject";
	private static final String ADD_INDIRECT_KEY = "misc.services.addIndirectCustomer";
	private static final String SITE_LOGO_PATH = "siteLogoPath";
	private static final String FROM_EMAIL = "fromEmail";
	private static final String FROM_DISPLAY_NAME = "fromDisplayName";
	private static final String BID = "bid";
	private static final String COMPANY = "company";
	private static final String CONTACT_EMAIL = "contactEmail";
	private static final String CONTACT_PHONE = "contactPhone";
	private static final String SOLD_TO = "soldTo";
	private static final String SHIP_TO = "shipTo";
	private static final String SPECIAL_STOCK_PARTNER = "specialStockPartner";
	private static final String ORDER_REASON = "orderReason";
	private static final String DATE = "date";
	private static final String PO_NUMBER = "poNumber";
	private static final String REPLISHMENT_OR_FILL_UP_DOC = "replenishmentOrFillUpDoc";
	private static final String REPLISHMENT_OR_FILL_UP_NFE = "replenishmentOrFillUpNFE";
	private static final String PATIENT = "patient";
	private static final String DOCTOR = "doctor";
	private static final String HOSPITAL = "hospital";
	private static final String CONTACT_FIRSTNAME = "contactFirstName";
	private static final String CONTACT_LASTNAME = "contactLastName";
	private static final String CONSIGNMENT_KEY = "misc.services.consignmentIssue";
	private static final String CITY_OR_STATE = "cityOrState";
	private static final String ITEM_LIST = "item";
	private static final String QUANTITY_LIST = "qty";
	private static final String UOM_LIST = "uom";
	private static final String BATCH_NUMBER_LIST = "batchNumber";
	private static final String FOLIO_LIST = "folio";
	private static final String PRICE_LIST = "price";
	private static final String CURRENCY_LIST = "currency";
	private static final String HEALTH_PLAN ="healthPlan";
	private static final String SURGERY_DATE="surgeryDate";
	private static final String OBSERVATION="observation";
	private static final String RESIDENTIAL_QUARTER="residentialQuarter";
	private static final String BILLING_OR_REPLACEMENT="billingOrReplacement";

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
	protected void onSiteEvent(final JnjFormEmailEvent event){
		final String METHOD_NAME = "onSiteEvent()";
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjFormEventListener.class);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "FORM NAME :: " + event.getJnjFormDTO().getFormName(),
				JnjFormEventListener.class);
		if (StringUtils.equals(event.getJnjFormDTO().getFormName(), Jnjlab2bcoreConstants.Forms.ADD_INDIRECT_CUSTOMER_CMS_PAGE)){
			populateIndirectCustomerMap(event, METHOD_NAME);
		}

		if (StringUtils.equals(event.getJnjFormDTO().getFormName(), Jnjlab2bcoreConstants.Forms.CONSIGNMENT_ISSUE_CMS_PAGE)){
			populateConsignmentIssueMap(event, METHOD_NAME);
		}

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjFormEventListener.class);
	}

	private void populateIndirectCustomerMap(JnjFormEmailEvent event, String METHOD_NAME) {
		final JnjFormProcessModel jnjFormProcessModel = (JnjFormProcessModel) getBusinessProcessService().createProcess(
                "jnjAddIndirectFormEmailProcess" + "-" + System.currentTimeMillis(), "jnjAddIndirectFormEmailProcess");
		final JnjLatamAddIndirectCustomerDTO jnjAddIndirectCustomerDTO = (JnjLatamAddIndirectCustomerDTO) event.getJnjFormDTO();
		final Map<String, Object> jnjAddIndirectCustomer = new HashMap<>();
		jnjAddIndirectCustomer.put(ADDRESS, jnjAddIndirectCustomerDTO.getAddress());
		jnjAddIndirectCustomer.put(CUSTOMER_NAME, jnjAddIndirectCustomerDTO.getCustomerName());
		jnjAddIndirectCustomer.put(CNPJ_OR_CPF, jnjAddIndirectCustomerDTO.getCnpjOrCpf());
		jnjAddIndirectCustomer.put(CNPJ_OR_CPF_TEXT, jnjAddIndirectCustomerDTO.getCnpjOrCpfText());
		jnjAddIndirectCustomer.put(PATIENT_OR_PHYSICIAN_NAME, jnjAddIndirectCustomerDTO.getCpfPatientOrPhysicianName());
		jnjAddIndirectCustomer.put(CUSTOMER_TYPE, jnjAddIndirectCustomerDTO.getCustomerType());
		jnjAddIndirectCustomer.put(PUBLIC_OR_PRIVATE, jnjAddIndirectCustomerDTO.getPublicOrPrivate());
		jnjAddIndirectCustomer.put(STATE, jnjAddIndirectCustomerDTO.getState());
		jnjAddIndirectCustomer.put(NEIGHBORHOOD, jnjAddIndirectCustomerDTO.getNeighborhood());
		jnjAddIndirectCustomer.put(ZIP_CODE, jnjAddIndirectCustomerDTO.getZipCode());
		jnjAddIndirectCustomer.put(CITY, jnjAddIndirectCustomerDTO.getCity());
		jnjAddIndirectCustomer.put(FORM_NAME, jnjAddIndirectCustomerDTO.getFormName());
		jnjAddIndirectCustomer.put(SITE_LOGO_PATH, jnjAddIndirectCustomerDTO.getServerURL());
		jnjAddIndirectCustomer.put(FROM_EMAIL, jnjAddIndirectCustomerDTO.getFromEmail());
		jnjAddIndirectCustomer.put(FROM_DISPLAY_NAME, jnjAddIndirectCustomerDTO.getFromDisplayName());
		jnjAddIndirectCustomer.put(BID, jnjAddIndirectCustomerDTO.getBid());
		jnjAddIndirectCustomer.put(COMPANY, jnjAddIndirectCustomerDTO.getCompany());
		try{
            jnjAddIndirectCustomer.put(SUBJECT, messageFacade.getMessageTextForCode(ADD_INDIRECT_KEY));
        }
        catch (final BusinessException businessException){
            JnjGTCoreUtil.logErrorMessage(FUNCTIONALITY_NAME, METHOD_NAME, businessException.getMessage(), businessException,
                    JnjFormEventListener.class);
        }
		jnjFormProcessModel.setJnjCustomerFormMap(jnjAddIndirectCustomer);
		populateProcessModel(event, jnjFormProcessModel);
	}

	private void populateConsignmentIssueMap(JnjFormEmailEvent event, String METHOD_NAME) {
		final JnjFormProcessModel jnjFormProcessModel = (JnjFormProcessModel) getBusinessProcessService().createProcess(
                "jnjConsignmentIssueFormEmailProcess" + "-" + System.currentTimeMillis(), "jnjConsignmentIssueFormEmailProcess");

		final JnjLatamConsignmentIssueDTO jnjLatamConsignmentIssueDTO = (JnjLatamConsignmentIssueDTO) event.getJnjFormDTO();
		final Map<String, List<String>> customerDetailsMap = new HashMap<>();
		final Map<String, Object> jnjConsignmentIssueMap = new HashMap<>();
		jnjConsignmentIssueMap.put(CONTACT_EMAIL, jnjLatamConsignmentIssueDTO.getContactEmail());
		jnjConsignmentIssueMap.put(CONTACT_PHONE, jnjLatamConsignmentIssueDTO.getContactPhone());
		jnjConsignmentIssueMap.put(SOLD_TO, jnjLatamConsignmentIssueDTO.getSoldTo());
		jnjConsignmentIssueMap.put(SHIP_TO, jnjLatamConsignmentIssueDTO.getShipTo());
		jnjConsignmentIssueMap.put(SPECIAL_STOCK_PARTNER, jnjLatamConsignmentIssueDTO.getSpecialStockPartner());
		jnjConsignmentIssueMap.put(ORDER_REASON, jnjLatamConsignmentIssueDTO.getOrderReason());
		jnjConsignmentIssueMap.put(DATE, jnjLatamConsignmentIssueDTO.getDate());
		jnjConsignmentIssueMap.put(PO_NUMBER, jnjLatamConsignmentIssueDTO.getPoNumber());
		jnjConsignmentIssueMap.put(REPLISHMENT_OR_FILL_UP_DOC, jnjLatamConsignmentIssueDTO.getReplenishmentOrFillUpDoc());
		jnjConsignmentIssueMap.put(REPLISHMENT_OR_FILL_UP_NFE, jnjLatamConsignmentIssueDTO.getReplenishmentOrFillUpNFE());
		jnjConsignmentIssueMap.put(PATIENT, jnjLatamConsignmentIssueDTO.getPatient());
		jnjConsignmentIssueMap.put(DOCTOR, jnjLatamConsignmentIssueDTO.getDoctor());
		jnjConsignmentIssueMap.put(CUSTOMER_NAME, jnjLatamConsignmentIssueDTO.getCustomerName());
		jnjConsignmentIssueMap.put(HOSPITAL, jnjLatamConsignmentIssueDTO.getHospital());
		jnjConsignmentIssueMap.put(CONTACT_FIRSTNAME, jnjLatamConsignmentIssueDTO.getContactFirstName());
		jnjConsignmentIssueMap.put(CONTACT_LASTNAME, jnjLatamConsignmentIssueDTO.getContactLastName());
		jnjConsignmentIssueMap.put(CITY_OR_STATE, jnjLatamConsignmentIssueDTO.getCityOrState());
		jnjConsignmentIssueMap.put(FORM_NAME, jnjLatamConsignmentIssueDTO.getFormName());
		jnjConsignmentIssueMap.put(SITE_LOGO_PATH, jnjLatamConsignmentIssueDTO.getServerURL());
		jnjConsignmentIssueMap.put(FROM_EMAIL, jnjLatamConsignmentIssueDTO.getFromEmail());
		jnjConsignmentIssueMap.put(FROM_DISPLAY_NAME, jnjLatamConsignmentIssueDTO.getFromDisplayName());
		jnjConsignmentIssueMap.put(HEALTH_PLAN, jnjLatamConsignmentIssueDTO.getHealthPlan());
		jnjConsignmentIssueMap.put(SURGERY_DATE, jnjLatamConsignmentIssueDTO.getSurgeryDate());
		jnjConsignmentIssueMap.put(OBSERVATION, jnjLatamConsignmentIssueDTO.getObservation());
		jnjConsignmentIssueMap.put(RESIDENTIAL_QUARTER, jnjLatamConsignmentIssueDTO.getResidentialQuarter());
		jnjConsignmentIssueMap.put(BILLING_OR_REPLACEMENT, jnjLatamConsignmentIssueDTO.getBillingOrReplacement());
        jnjConsignmentIssueMap.put(CITY, jnjLatamConsignmentIssueDTO.getCity());
        jnjConsignmentIssueMap.put(STATE, jnjLatamConsignmentIssueDTO.getState());
        jnjConsignmentIssueMap.put(ZIP_CODE, jnjLatamConsignmentIssueDTO.getZipCode());

        try{
            jnjConsignmentIssueMap.put(SUBJECT, messageFacade.getMessageTextForCode(CONSIGNMENT_KEY));
        }
        catch (final BusinessException businessException){
            JnjGTCoreUtil.logErrorMessage(FUNCTIONALITY_NAME, METHOD_NAME,
                    "Unable to fetch subject message for ::" + CONSIGNMENT_KEY, businessException, JnjFormEventListener.class);
        }
		jnjFormProcessModel.setJnjCustomerFormMap(jnjConsignmentIssueMap);
		customerDetailsMap.put(ITEM_LIST, jnjLatamConsignmentIssueDTO.getItem());
		customerDetailsMap.put(BATCH_NUMBER_LIST, jnjLatamConsignmentIssueDTO.getBatchNumber());
		customerDetailsMap.put(CURRENCY_LIST, jnjLatamConsignmentIssueDTO.getCurrency());
		customerDetailsMap.put(FOLIO_LIST, jnjLatamConsignmentIssueDTO.getFolio());
		customerDetailsMap.put(QUANTITY_LIST, jnjLatamConsignmentIssueDTO.getQty());
		customerDetailsMap.put(PRICE_LIST, jnjLatamConsignmentIssueDTO.getPrice());
		customerDetailsMap.put(UOM_LIST, jnjLatamConsignmentIssueDTO.getUom());

		jnjFormProcessModel.setJnjCustomerDetails(customerDetailsMap);
		populateProcessModel(event, jnjFormProcessModel);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjFormEventListener.class);
	}

	@Override
	protected boolean shouldHandleEvent(final JnjFormEmailEvent event)
	{
		final String METHOD_NAME = "shouldHandleEvent()";
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjFormEventListener.class);
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.END_OF_METHOD,
				JnjFormEventListener.class);
		return true;
	}

	private void populateProcessModel(final JnjFormEmailEvent event, final JnjFormProcessModel jnjFormProcessModel)
	{
		final String METHOD_NAME = "populateProcessModel()";
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				JnjFormEventListener.class);

		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "Populating process model", JnjFormEventListener.class);
		jnjFormProcessModel.setSite(event.getSite());
		jnjFormProcessModel.setCustomer(event.getCustomer());
		jnjFormProcessModel.setLanguage(event.getLanguage());
		jnjFormProcessModel.setCurrency(event.getCurrency());
		jnjFormProcessModel.setStore(event.getBaseStore());
		getModelService().save(jnjFormProcessModel);
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, "Starting process model", JnjFormEventListener.class);

		getBusinessProcessService().startProcess(jnjFormProcessModel);
		JnjGTCoreUtil.logDebugMessage(FUNCTIONALITY_NAME, METHOD_NAME, Jnjb2bCoreConstants.Logging.END_OF_METHOD,
				JnjFormEventListener.class);
	}
}
