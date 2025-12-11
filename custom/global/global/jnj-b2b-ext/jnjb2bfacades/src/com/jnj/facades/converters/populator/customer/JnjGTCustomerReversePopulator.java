/**
 *
 */
package com.jnj.facades.converters.populator.customer;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.strategies.B2BUserGroupsLookUpStrategy;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.b2bacceleratorfacades.company.CompanyB2BCommerceFacade;
import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.b2bunit.JnjGTB2BUnitDao;
import com.jnj.core.model.SecretQuestionsAndAnswersModel;
import com.jnj.facades.data.SecretQuestionData;
import com.jnj.core.enums.AccessBy;
import com.jnj.core.enums.CustomerStatus;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.core.model.JnJB2bCustomerModel;
import de.hybris.platform.core.model.user.AddressModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;


/**
 * @author ujjwal.negi
 * 
 */
public class JnjGTCustomerReversePopulator implements Populator<JnjGTCustomerData, JnJB2bCustomerModel>
{

	private static final Logger LOG = Logger.getLogger(JnjGTCustomerReversePopulator.class);
	private CustomerNameStrategy customerNameStrategy;

	@Autowired
	protected JnjGTB2BUnitDao jnjGTB2BUnitDao;
	
	@Autowired
	private ModelService modelService;
	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	@Qualifier(value = "addressReversePopulator")
	private AddressReversePopulator addressReversePopulator;
	
	@Autowired
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	@Autowired
	private B2BUserGroupsLookUpStrategy b2bUserGroupsLookUpStrategy;

	/*@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;*/

	@Autowired
	 private B2BCommerceUnitService b2BCommerceUnitService;

	@Autowired
	private EnumerationService enumerationService;

	@Resource(name = "commerceCategoryService")
	protected CommerceCategoryService commerceCategoryService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final JnjGTCustomerData source, final JnJB2bCustomerModel target) throws ConversionException
	{
		// added to set the current country for the PCM user
		if (StringUtils.isNotBlank(source.getCurrentCountry()))
		{
			target.setCurrentCountry(commonI18NService.getCountry(source.getCurrentCountry()));
		}
		target.setName(getCustomerNameStrategy().getName(source.getFirstName(), source.getLastName()));
		target.setCompanyName(source.getOrgName());
		target.setEmail(source.getEmail().toLowerCase());
		if (source.getMddSector() != null)
		{
			target.setMddSector(source.getMddSector().booleanValue());
		}
		if (source.getConsumerSector() != null)
		{
			target.setConsumerSector(source.getConsumerSector().booleanValue());
		}
		if (source.getPharmaSector() != null)
		{
			target.setPharmaSector(source.getPharmaSector().booleanValue());
		}
		if (StringUtils.isNotEmpty(source.getWwid()))
		{
			target.setWwid(source.getWwid());
		}
		target.setDepartment(source.getDepartment());
		if (source.getLoginDisabledFlag() != null)
		{
			target.setLoginDisabled((source.getLoginDisabledFlag()).booleanValue());
		}
		
		/*checking isResetpassword for signup default setting true*/
		if (source.getIsResetPassword() != null)
		{
			target.setIsResetPassword((source.getIsResetPassword()).booleanValue());
		}
		
		if (source.getSecretQuestionsAnswers() != null)
		{
			target.setSecretQuestionsAndAnswersList(getSecretQuestionsList(source.getSecretQuestionsAnswers(), source.getEmail()));
		}

		if (StringUtils.isNotEmpty(source.getUid()))
		{
			target.setUid(source.getUid());
		}
		else if (source.getEmail() != null)
		{
			target.setUid(source.getEmail().toLowerCase());
		}
		target.setConsignmentEntryOrder(source.getConsignmentEntryOrder()); //AAOL-3112
		target.setFinancialAnalysisEnable(source.getFinancialAnalysisEnable()); //AAOL-2422
		target.setOriginalUid(source.getEmail());
		target.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		
		/*target.setSessionLanguage(getCommonI18NService().getCurrentLanguage());*/
		
		//added for new Req
		if(source.getLanguage() != null){
			LanguageModel languageModel = getCommonI18NService().getLanguage(source.getLanguage().getIsocode());
			target.setSessionLanguage(languageModel);
		}else{
			target.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		}
		
		target.setSuperVisorEmail(source.getSupervisorEmail());
		target.setSuperVisorNumber(source.getSupervisorPhone());
		target.setSuperVisorName(source.getSupervisorName());
		 /*4910,5506*/
		target.setEmailPreferences(source.getEmailPreferences());
		if (source.getPreferredMobileNumber() != null)
		{
			target.setPreferredMobileNumber(source.getPreferredMobileNumber());
		} 
		target.setSmsPreferences(source.getSmsPreferences());
		 /*4910,5506*/
		
		target.setBackorderEmailType(source.getBackorderType());
		//AAOL-3716
		target.setShippedOrderEmailType(source.getShippedOrderType());
		target.setInvoiceEmailPrefType(source.getInvoiceEmailPreferenceType());
		target.setDeliveryNoteEmailPrefType(source.getDeliveryNoteEmailPreferenceType());
		if (source.getPolicyVersion() != null)
		{
			target.setPrivacyPolicyVersion(source.getPolicyVersion());
		}
		target.setCsrNotes(source.getCsrNotes());
		if (source.getNoCharge() != null)
		{
			target.setNoCharge(source.getNoCharge());
		}
		if (StringUtils.isNotEmpty(source.getStatus()))
		{
			target.setStatus(CustomerStatus.valueOf(source.getStatus()));
		}
		if (StringUtils.isNotEmpty(source.getAccessBy()))
		{
			target.setAccessBy(AccessBy.valueOf(source.getAccessBy()));
			if (source.getAccessBy().equals(AccessBy.NOT_SALES_REP.name()))
			{
				mapCustomerForAccessByNotSalesRep(source, target);
			}
			else if (source.getAccessBy().equals(AccessBy.WWID.name()))
			{
				mapCustomerForAccessByWwid(source, target);
			}
			else if (source.getAccessBy().equals(AccessBy.TERRITORIES.name()))
			{
				mapCustomerForAccessByTerritory(source, target);
			}
		}
		if (source.getContactAddress() != null)
		{
			final AddressModel addressModel = getModelService().create(AddressModel.class);
			getAddressReversePopulator().populate(source.getContactAddress(), addressModel);
			final List customerAddresses = new ArrayList();
			addressModel.setOwner(target);
			customerAddresses.add(addressModel);
			target.setAddresses(customerAddresses);
		}
		if(CollectionUtils.isNotEmpty(source.getAllowedFranchise()))
		{
			LOG.info("setting allowed franchise at customerreversePopulator @ #209");
			categoryReversePopulator(source,target); //soumitra - allowedFranchise in target -AAOL-4913
		}
		if (!CollectionUtils.isEmpty(source.getGroups()))
		{
			final Set<PrincipalGroupModel> userGroups = new HashSet(target.getGroups());
			final Collection<B2BUnitModel> b2bUnits = CollectionUtils.select(target.getGroups(),
					PredicateUtils.instanceofPredicate(B2BUnitModel.class));
			userGroups.removeAll(b2bUnits);
			for (final String account : source.getGroups())
			{

			//	final B2BUnitModel b2BUnitModel = b2BCommerceUnitService.getUnitForUid(account);
				final B2BUnitModel b2BUnitModel =jnjGTB2BUnitDao.getB2BUnitByUid(account);
				userGroups.add(b2BUnitModel);
			}
			target.setGroups(userGroups);
		}
		if (!CollectionUtils.isEmpty(source.getRoles()))
		{
			b2BCommerceB2BUserGroupService.updateUserGroups(b2bUserGroupsLookUpStrategy.getUserGroups(), source.getRoles(), target);
		}
	}

	/*Soumitra - categoryReversePopulator is not present, hence reverse populating the fields required in a separate function - AAOL-4913*/
	/*categoryReversePopulator converts List<CategoryData> from source to List<CategoryModel> and sets it in target*/
	private void categoryReversePopulator(final JnjGTCustomerData source,JnJB2bCustomerModel target) {
		List<CategoryModel> categoryModels = new ArrayList<>(); 
		CategoryModel categoryModel;
		List<CategoryData> categoryDatas = source.getAllowedFranchise();
		for (CategoryData categoryData : categoryDatas) {
			categoryModel=new CategoryModel();
			categoryModel = commerceCategoryService.getCategoryForCode(categoryData.getCode());
			categoryModels.add(categoryModel);
		}
		target.setAllowedFranchise(categoryModels);
	}

	/**
	 * This method handles the scenario when the user selects Access By Wwwid
	 * 
	 * @param source
	 *           :JnjGTCustomerData
	 * @param target
	 *           : JnJB2bCustomerModel
	 */
	private void mapCustomerForAccessByNotSalesRep(final JnjGTCustomerData source, final JnJB2bCustomerModel target)
	{
		/*
		 * When the user is access By Not SalesRep ,Then remove Division and Its Corresponding Division Group
		 */
		final String divison = target.getDivison();
		final Set<PrincipalGroupModel> userGroups = new HashSet<PrincipalGroupModel>();
		userGroups.addAll(target.getGroups());
		if (StringUtils.isNotEmpty(divison))
		{
			for (final PrincipalGroupModel group : target.getGroups())
			{
				if (group instanceof JnjGTTerritoryDivisonModel && group.getUid().endsWith(divison))
				{
					userGroups.remove(group);
				}
			}
			target.setDivison(null);
		}
		/*
		 * When the user is access By Not SalesRep,Then remove the Territory Division Group and well as Corresponding
		 * Group.
		 */
		final List<String> territoryDivison = target.getTerritoryDiv();
		if (CollectionUtils.isNotEmpty(territoryDivison))
		{
			for (final String territoryDivisonId : territoryDivison)
			{
				for (final PrincipalGroupModel group : target.getGroups())
				{
					if (group instanceof JnjGTTerritoryDivisonModel && group.getUid().endsWith(territoryDivisonId))
					{
						userGroups.remove(group);
					}
				}

			}
			target.setTerritoryDiv(null);
		}

		target.setGroups(userGroups);
	}


	/**
	 * This method handles the scenario when the user selects Access By Not Sales Rep.
	 * 
	 * @param source
	 *           :JnjGTCustomerData
	 * @param target
	 *           : JnJB2bCustomerModel
	 */
	private void mapCustomerForAccessByWwid(final JnjGTCustomerData source, final JnJB2bCustomerModel target)
	{
		/*
		 * When the user is access By WWid, Then remove the Division Group if the new Division Group is not equivalent to
		 * then existing Division.
		 */
		final String divison = target.getDivison();
		final Set<PrincipalGroupModel> userGroups = new HashSet<PrincipalGroupModel>();
		userGroups.addAll(target.getGroups());
		if (StringUtils.isNotEmpty(divison) && !StringUtils.equals(divison, source.getDivison()))
		{
			for (final PrincipalGroupModel group : target.getGroups())
			{
				if (group instanceof JnjGTTerritoryDivisonModel && group.getUid().endsWith(divison))
				{
					userGroups.remove(group);
				}
			}
		}

		//Add the New Division in the Target
		if (StringUtils.isNotEmpty(source.getDivison()))
		{
			target.setDivison(source.getDivison());
		}
		/*
		 * When the user is access By Not SalesRep,Then remove the Territory Division Group and well as Corresponding
		 * Group.
		 */
		final List<String> territoryDivison = target.getTerritoryDiv();
		if (CollectionUtils.isNotEmpty(territoryDivison))
		{
			for (final String territoryDivisonId : territoryDivison)
			{
				for (final PrincipalGroupModel group : target.getGroups())
				{
					if (group instanceof JnjGTTerritoryDivisonModel && group.getUid().endsWith(territoryDivisonId))
					{
						userGroups.remove(group);
					}
				}
			}
			target.setTerritoryDiv(null);
		}
		target.setGroups(userGroups);
	}


	/**
	 * This method handles the scenario when the user selects Access By Territory
	 * 
	 * @param source
	 *           :JnjGTCustomerData
	 * @param target
	 *           : JnJB2bCustomerModel
	 */
	private void mapCustomerForAccessByTerritory(final JnjGTCustomerData source, final JnJB2bCustomerModel target)
	{
		/*
		 * When the user is access By Territory, Then remove the Division Group and set Division as null
		 */
		final String divison = target.getDivison();
		final Set<PrincipalGroupModel> userGroups = new HashSet<PrincipalGroupModel>();
		userGroups.addAll(target.getGroups());
		if (StringUtils.isNotEmpty(divison))
		{
			for (final PrincipalGroupModel group : target.getGroups())
			{
				if (group instanceof JnjGTTerritoryDivisonModel && group.getUid().endsWith(divison))
				{
					userGroups.remove(group);
				}
			}
			target.setDivison(null);
		}
		/*
		 * When the user is access By Territory,Then remove the Territory Division Group and well as Corresponding Group.
		 */
		final List<String> territoryDivison = target.getTerritoryDiv();
		if (CollectionUtils.isNotEmpty(territoryDivison))
		{
			for (final String territoryDivisonId : territoryDivison)
			{
				for (final PrincipalGroupModel group : target.getGroups())
				{
					if (group instanceof JnjGTTerritoryDivisonModel && group.getUid().endsWith(territoryDivisonId))
					{
						userGroups.remove(group);
					}
				}
			}
		}
		/*
		 * When the user is access BTerritory,Then add the Territory Division Group and well as Corresponding Group with
		 * latest Territory Divison
		 */

		final List<String> territoryDiv = source.getTerritoryDiv();
		if (CollectionUtils.isNotEmpty(territoryDiv))
		{
			for (final String territoryDivisonId : territoryDiv)
			{
				final JnjGTTerritoryDivisonModel territoryDivsionModel = b2BCommerceB2BUserGroupService.getUserGroupForUID(
						territoryDivisonId, JnjGTTerritoryDivisonModel.class);
				if (territoryDivsionModel != null)
				{
					userGroups.add(territoryDivsionModel);
				}
			}
			target.setTerritoryDiv(territoryDiv);
		}
		target.setGroups(userGroups);
	}

	/**
	 * @param secretQuestionsAnswers
	 * @return
	 */
	protected List<SecretQuestionsAndAnswersModel> getSecretQuestionsList(final List<SecretQuestionData> secretQuestionDataList,
			final String emailId)
	{
		// YTODO Auto-generated method stub
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "updateSecretQuestionsForRegistration()" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final List<SecretQuestionsAndAnswersModel> questionsAndAnswersList = new ArrayList<SecretQuestionsAndAnswersModel>();
		for (final SecretQuestionData secretQuestionData : secretQuestionDataList)
		{
			final SecretQuestionsAndAnswersModel secretQuestionModel = new SecretQuestionsAndAnswersModel();
			secretQuestionModel.setQuestionId(secretQuestionData.getCode());
			secretQuestionModel.setAnswer(secretQuestionData.getAnswer());

			try
			{
				questionsAndAnswersList.add(secretQuestionModel);
				getModelService().save(secretQuestionModel);
			}
			catch (final ModelSavingException modelSavingException)
			{
				LOG.error("Unable to save the secret questions for the user :" + emailId);
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "updateSecretQuestionsForRegistration()" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return questionsAndAnswersList;
	}

	/**
	 * @return the customerNameStrategy
	 */
	public CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	/**
	 * @param customerNameStrategy
	 *           the customerNameStrategy to set
	 */
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @return the addressReversePopulator
	 */
	public AddressReversePopulator getAddressReversePopulator()
	{
		return addressReversePopulator;
	}

	/**
	 * @param addressReversePopulator
	 *           the addressReversePopulator to set
	 */
	public void setAddressReversePopulator(final AddressReversePopulator addressReversePopulator)
	{
		this.addressReversePopulator = addressReversePopulator;
	}

	public B2BCommerceUnitService getB2BCommerceUnitService()
	{
		return b2BCommerceUnitService;
	}
}
