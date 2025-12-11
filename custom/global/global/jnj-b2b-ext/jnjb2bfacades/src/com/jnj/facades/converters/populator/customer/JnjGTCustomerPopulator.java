/**
 * 
 */
package com.jnj.facades.converters.populator.customer;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.user.converters.populator.CustomerPopulator;
import de.hybris.platform.commercefacades.product.converters.populator.CategoryPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.facades.data.JnjGTCustomerBasicData;
import de.hybris.platform.enumeration.EnumerationService;

/**
 * @author komal.sehgal
 * 
 */
public class JnjGTCustomerPopulator extends CustomerPopulator
{
	//@Resource(name = "enumerationService")
	//private EnumerationService enumerationService;
	
	private static final Logger LOGGER = Logger.getLogger(JnjGTCustomerPopulator.class);
	
	private Converter<AddressModel, AddressData> addressConverter;	

	@Autowired
	private SessionService sessionService;
	@Autowired
	protected EnumerationService enumerationService;

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	@Autowired
	private JnjGTCustomerBasicPopulator jnjGTCustomerBasicPopulator;
	
	@Autowired
	private CategoryPopulator categoryPopulator;

	public JnjGTCustomerBasicPopulator getJnjGTCustomerBasicPopulator() {
		return jnjGTCustomerBasicPopulator;
	}

	public void setJnjGTCustomerBasicPopulator(
			JnjGTCustomerBasicPopulator jnjGTCustomerBasicPopulator) {
		this.jnjGTCustomerBasicPopulator = jnjGTCustomerBasicPopulator;
	}

	@Override
	public void populate(final CustomerModel source, final CustomerData target)
	{
		try
		{
			super.populate(source, target);
		}
		catch (IllegalArgumentException illegalArgumentException) {
			LOGGER.error("IllegalArgumentException Occurred while populating locale###################: "+illegalArgumentException.getMessage());
		}
		catch (Exception exception) {
			LOGGER.error("Exception Occurred while populating locale###################: "+exception.getMessage());
		}
		if (target instanceof JnjGTCustomerData && source instanceof JnJB2bCustomerModel)
		{
			final JnjGTCustomerData jnjGTCustomerData = (JnjGTCustomerData) target;
			final JnJB2bCustomerModel jnjGTCustomerModel = (JnJB2bCustomerModel) source;
			// added to set the current country for the PCM user
			if (jnjGTCustomerModel.getCurrentCountry() != null)
			{
				jnjGTCustomerData.setCurrentCountry(jnjGTCustomerModel.getCurrentCountry().getIsocode());
			}
			jnjGTCustomerData.setWwid(jnjGTCustomerModel.getWwid());
			jnjGTCustomerData.setEmail(jnjGTCustomerModel.getEmail());
			jnjGTCustomerData.setRegistrationDate(jnjGTCustomerModel.getRegistrationDate());
			jnjGTCustomerData.setUserType(jnjGTCustomerModel.getUserType());
			jnjGTCustomerData.setFinancialAnalysisEnable(jnjGTCustomerModel.getFinancialAnalysisEnable());
			if (jnjGTCustomerModel.getStatus() != null)
			{
				jnjGTCustomerData.setStatus(enumerationService.getEnumerationName(jnjGTCustomerModel.getStatus()));
				//jnjGTCustomerData.setStatus(jnjGTCustomerModel.getStatus().toString());
			}
			if (StringUtils.isNotEmpty(jnjGTCustomerModel.getDivison()))
			{
				jnjGTCustomerData.setDivison(jnjGTCustomerModel.getDivison());
			}
			jnjGTCustomerData.setOrgName(jnjGTCustomerModel.getCompanyName());
			jnjGTCustomerData.setSupervisorEmail(jnjGTCustomerModel.getSuperVisorEmail());
			jnjGTCustomerData.setSupervisorName(jnjGTCustomerModel.getSuperVisorName());

			if (StringUtils.isNotEmpty(jnjGTCustomerModel.getSuperVisorNumber())
					&& jnjGTCustomerModel.getSuperVisorNumber().contains(Jnjb2bCoreConstants.SYMBOl_PIPE))
			{
				final String[] phoneNumber = StringUtils.split(jnjGTCustomerModel.getSuperVisorNumber(),
						Jnjb2bCoreConstants.SYMBOl_PIPE);
				if (phoneNumber.length > 1)
				{
					jnjGTCustomerData.setSupervisorPhoneCode(phoneNumber[0]);
					jnjGTCustomerData.setSupervisorPhone(phoneNumber[1]);
				}
			}
			else
			{
				jnjGTCustomerData.setSupervisorPhone(jnjGTCustomerModel.getSuperVisorNumber());
			}

			jnjGTCustomerData.setAuthorizedBuyer(jnjGTCustomerModel.getAuthorizedBuyer());
			jnjGTCustomerData.setSalesOrg(jnjGTCustomerModel.getSalesOrg());
			jnjGTCustomerData.setGateWayAccessCode(jnjGTCustomerModel.getGateWayAccessCode());
			jnjGTCustomerData.setDepartment(jnjGTCustomerModel.getDepartment());
			jnjGTCustomerData.setEmailPreferences(jnjGTCustomerModel.getEmailPreferences());
			jnjGTCustomerData.setPasswordChangeDate(jnjGTCustomerModel.getPasswordChangeDate());
			jnjGTCustomerData.setCsrNotes(jnjGTCustomerModel.getCsrNotes());
			jnjGTCustomerData.setNoCharge(jnjGTCustomerModel.getNoCharge());
			
			/*5508,5509*/
			jnjGTCustomerData.setPreferredMobileNumber(jnjGTCustomerModel.getPreferredMobileNumber());
			jnjGTCustomerData.setSmsPreferences(jnjGTCustomerModel.getSmsPreferences()); 
			/*5508,5509*/
			
			final List<String> territoryDivsionsList = jnjGTCustomerModel.getTerritoryDiv();
			if (CollectionUtils.isNotEmpty(jnjGTCustomerModel.getTerritoryDiv()))
			{
				jnjGTCustomerData.setTerritoryDiv(jnjGTCustomerModel.getTerritoryDiv());
			}
			populateB2BUnits(jnjGTCustomerModel, jnjGTCustomerData);
			jnjGTCustomerData.setMddSector(Boolean.valueOf(jnjGTCustomerModel.isMddSector()));
			jnjGTCustomerData.setConsumerSector(Boolean.valueOf(jnjGTCustomerModel.isConsumerSector()));
			jnjGTCustomerData.setPharmaSector(Boolean.valueOf(jnjGTCustomerModel.isPharmaSector()));
			if (jnjGTCustomerModel.getAccessBy() != null)
			{
				jnjGTCustomerData.setAccessBy(jnjGTCustomerModel.getAccessBy().name());
			}
			// Converting the Contact Address
			if (jnjGTCustomerModel.getAddresses() != null) {
				for (final AddressModel address : jnjGTCustomerModel
						.getAddresses()) {
					if (Boolean.valueOf(address.getContactAddress())) {

						jnjGTCustomerData.setContactAddress(getAddressConverter().convert(address));
					}
				}
			}
			if(null != sessionService.getAttribute(Jnjb2bCoreConstants.Login.SHOW_CHANGE_ACCOUNT)) {
				jnjGTCustomerData.setShowChangeAccount(sessionService.getAttribute(Jnjb2bCoreConstants.Login.SHOW_CHANGE_ACCOUNT).toString());
			}
			jnjGTCustomerData.setUid(jnjGTCustomerModel.getUid());

			/**
			 * Checking getCurrentB2BUnit not null - setting current B2B Unit
			 * related info
			 **/
			if (null != jnjGTCustomerModel.getCurrentB2BUnit()) {
				jnjGTCustomerData.setCurrentB2BUnitID(jnjGTCustomerModel.getCurrentB2BUnit().getUid());
				jnjGTCustomerData.setCurrentB2BUnitName(jnjGTCustomerModel.getCurrentB2BUnit().getLocName());
				jnjGTCustomerData.setCurrentB2BUnitGLN(jnjGTCustomerModel.getCurrentB2BUnit().getGlobalLocNo());
				jnjGTCustomerData.setJnjSiteName(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME));
			}
			//Changes for Default B2bUnit - AAOL-4659
			
			if (null != jnjGTCustomerModel.getDefaultB2BUnit()) {
				jnjGTCustomerData.setDefaultB2BUnitID(jnjGTCustomerModel.getDefaultB2BUnit().getUid());
				jnjGTCustomerData.setDefaultB2BUnitName(jnjGTCustomerModel.getDefaultB2BUnit().getName());
			}
			
			//Changes for Default B2bUnit - AAOL-4660
			if(null!=jnjGTCustomerModel.getDefaultOrderType()){
				jnjGTCustomerData.setDefaultOrderType(jnjGTCustomerModel.getDefaultOrderType());
			}
			/** Checking name not null **/
			if (null != jnjGTCustomerModel.getName()) {
				/** Splitting Name by SPACE to retrieve first name and last name **/
				final String[] customerSplitName = jnjGTCustomerModel.getName().split(Jnjb2bCoreConstants.SPACE);

				/** SET first name **/
				jnjGTCustomerData.setFirstName(customerSplitName[0]);

				/**
				 * If splitting by space gave length 2 then we have both first
				 * and last names : Done to prevent Array Index Out of Bounds
				 * Exception
				 **/
				if (customerSplitName.length == 2) {
					/** SET Last Name **/
					jnjGTCustomerData.setLastName(customerSplitName[1]);
				}
			}
			jnjGTCustomerData.setConsignmentEntryOrder(jnjGTCustomerModel.getConsignmentEntryOrder()); //AAOL_3112
			//Soumitra - Converting category model to categoryData and setting into customerData AAOL-4913
			List<CategoryModel> categoryModels = jnjGTCustomerModel.getAllowedFranchise();
			List<CategoryData> categoryDatas = new ArrayList<>();
			CategoryData categoryData;
			for (CategoryModel categoryModel : categoryModels) {
				categoryData = new CategoryData();
				categoryPopulator.populate(categoryModel,categoryData);
				categoryDatas.add(categoryData);
			}
			jnjGTCustomerData.setAllowedFranchise(categoryDatas);
		}
	}

	protected void populateB2BUnits(final B2BCustomerModel source, final JnjGTCustomerData target)
	{
		final Collection<B2BUnitModel> b2bUnits = CollectionUtils.select(source.getGroups(),
				PredicateUtils.instanceofPredicate(B2BUnitModel.class));
		final List<B2BUnitData> unitDataList = new ArrayList<B2BUnitData>(b2bUnits.size());

		for (final B2BUnitModel unit : b2bUnits)
		{
			if (!Jnjb2bCoreConstants.B2BUnit.JNJDUMMYUNIT.equals(unit.getUid()))
			{
				final B2BUnitData b2BUnitData = new B2BUnitData();
				b2BUnitData.setUid(unit.getUid());
				b2BUnitData.setName(unit.getLocName());
				b2BUnitData.setActive(Boolean.TRUE.equals(unit.getActive()));

				unitDataList.add(b2BUnitData);
			}
		}

		target.setB2bUnits(unitDataList);
	}

	//	@Override
	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	//@Override
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}
}
