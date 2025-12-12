/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.converters.populator.customer;

import com.jnj.core.enums.CustomerStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.facades.customer.JnjLatamCustomerFacade;
import com.jnj.facades.data.JnjLaCustomerData;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.strategies.B2BUserGroupsLookUpStrategy;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.jnj.la.core.enums.JnJEmailFrequency;
import com.jnj.facades.data.JnJLaUserAccountPreferenceData;
import java.util.Collections;

public class JnjLatamCustomerReversePopulator extends JnjGTCustomerReversePopulator {

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	@Autowired
	private B2BUserGroupsLookUpStrategy b2bUserGroupsLookUpStrategy;

	@Autowired
	private B2BCommerceUnitService b2BCommerceUnitService;

	@Autowired
	private JnjGTAddressReversePopulator jnjGTAddressReversePopulator;

    @Autowired
    private JnjLatamCustomerFacade jnjLaCustomerFacade;

	public void populate(final JnjLaCustomerData source, final JnJB2bCustomerModel target) throws ConversionException
	{
		target.setCommercialUserFlag(source.getCommercialUserFlag());
		target.setCommercialUserSector(source.getCommercialUserSector());
		// added to set the current country for the PCM user
		if (StringUtils.isNotBlank(source.getCurrentCountry()))
		{
			target.setCurrentCountry(commonI18NService.getCountry(source.getCurrentCountry()));
		}
		target.setName(getCustomerNameStrategy().getName(source.getFirstName(), source.getLastName()));
		if (source.getCustomerCode() != null)
		{
			target.setCustomerCode(source.getCustomerCode());
		}
		if (source.getSoldTo() != null)
		{
			target.setCustomerName(source.getSoldTo());
		}
		if (source.getJnjAccountManager() != null)
		{
			target.setAccountManager(source.getJnjAccountManager());
		}

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

		if (source.getLoginDisabledFlag() != null)
		{
			target.setLoginDisabled((source.getLoginDisabledFlag()).booleanValue());
		}

		/* checking isResetpassword for signup default setting true */
		if (source.getIsResetPassword() != null)
		{
			target.setIsResetPassword(source.getIsResetPassword());
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
		target.setOriginalUid(source.getEmail());
		target.setSessionCurrency(getCommonI18NService().getCurrentCurrency());

		//added for new Req
		if (source.getLanguage() != null)
		{
			final LanguageModel languageModel = getCommonI18NService().getLanguage(source.getLanguage().getIsocode());
			target.setSessionLanguage(languageModel);
		}
		else
		{
			target.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		}

		target.setEmailPreferences(source.getEmailPreferences());

		if (source.getPolicyVersion() != null)
		{
			target.setPrivacyPolicyVersion(source.getPolicyVersion());
		}

		if (source.getNoCharge() != null)
		{
			target.setNoCharge(source.getNoCharge());
		}
		if (StringUtils.isNotEmpty(source.getStatus()))
		{
			target.setStatus(CustomerStatus.valueOf(source.getStatus()));
		}
		if (source.getContactAddress() != null)
		{
			final AddressModel addressModel = getModelService().create(AddressModel.class);
			jnjGTAddressReversePopulator.populate(source.getContactAddress(), addressModel);
			final List customerAddresses = new ArrayList();
			addressModel.setOwner(target);
			customerAddresses.add(addressModel);
			target.setAddresses(customerAddresses);
		}
		if (!CollectionUtils.isEmpty(source.getGroups()))
		{
			final Set<PrincipalGroupModel> userGroups = new HashSet(target.getGroups());
//			final Collection<B2BUnitModel> b2bUnits = CollectionUtils.select(target.getGroups(),
//					PredicateUtils.instanceofPredicate(B2BUnitModel.class));
            Collection<B2BUnitModel> b2bUnits = target.getGroups().stream()
                    .filter(g -> g instanceof B2BUnitModel)
                    .map(g -> (B2BUnitModel) g)
                    .toList();
			userGroups.removeAll(b2bUnits);
			for (final String account : source.getGroups())
			{

				final B2BUnitModel b2BUnitModel = b2BCommerceUnitService.getUnitForUid(account);
				userGroups.add(b2BUnitModel);
			}
			target.setGroups(userGroups);
		}
		if (!CollectionUtils.isEmpty(source.getRoles()))
		{
			b2BCommerceB2BUserGroupService.updateUserGroups(b2bUserGroupsLookUpStrategy.getUserGroups(), source.getRoles(), target);
		}
	}

	public void populateAccountPreferences(JnjLaCustomerData source, JnJB2bCustomerModel target) {
        if (source.getCurrentAccountPreference() == null) {
            return;
        }
        JnJEmailPeriodicity periodicity = source.getCurrentAccountPreference().getPeriodicity();
        JnJB2BUnitModel currentAccount = jnjLaCustomerFacade.getCurrentB2bUnit();

        List<JnJLaUserAccountPreferenceModel> accountPreferences = new ArrayList<>(target.getAccountPreferences()); // new List used to avoid java.lang.UnsupportedOperationException error when adding a new element
        Optional<JnJLaUserAccountPreferenceModel> accountPreference = accountPreferences.stream().filter(item -> Objects.equals(item.getAccount(), currentAccount)).findFirst();
        if (accountPreference.isPresent()) {
            JnJLaUserAccountPreferenceModel jnjLaUserAccountPreferenceModel = accountPreference.get();
            jnjLaUserAccountPreferenceModel.setPeriodicity(periodicity);
			if(JnJEmailPeriodicity.CONSOLIDATED.equals(periodicity)) {
				jnjLaUserAccountPreferenceModel.setOrderTypes(source.getCurrentAccountPreference().getOrderTypes());
				jnjLaUserAccountPreferenceModel.setConsolidatedEmailFrequency(source.getCurrentAccountPreference().getConsolidatedEmailFrequency());
				JnjLatamCustomerReversePopulator.populateFrequency(jnjLaUserAccountPreferenceModel, source);
			} else {
				jnjLaUserAccountPreferenceModel.setConsolidatedEmailFrequency(null);
				jnjLaUserAccountPreferenceModel.setOrderTypes(null);
				jnjLaUserAccountPreferenceModel.setDayOfTheWeek(null);
				jnjLaUserAccountPreferenceModel.setDaysOfTheMonth(Collections.emptyList());
			}
            getModelService().save(jnjLaUserAccountPreferenceModel);
        } else if (currentAccount != null) {
        	accountPreferences.add(createJnJLaUserAccountPreferenceModel(source.getCurrentAccountPreference(), target, currentAccount));
        }

        target.setAccountPreferences(accountPreferences);
    }

	private static void populateFrequency(JnJLaUserAccountPreferenceModel jnjLaUserAccountPreferenceModel, JnjLaCustomerData source){
		if(JnJEmailFrequency.DAILY.equals(source.getCurrentAccountPreference().getConsolidatedEmailFrequency())){
			jnjLaUserAccountPreferenceModel.setDayOfTheWeek(null);
			jnjLaUserAccountPreferenceModel.setDaysOfTheMonth(Collections.emptyList());
		}
		if (source.getCurrentAccountPreference().getDayOfTheWeek() != null && JnJEmailFrequency.WEEKLY.equals(source.getCurrentAccountPreference().getConsolidatedEmailFrequency())) {
			jnjLaUserAccountPreferenceModel.setDaysOfTheMonth(Collections.emptyList());
			jnjLaUserAccountPreferenceModel.setDayOfTheWeek(source.getCurrentAccountPreference().getDayOfTheWeek());
		}
		if (CollectionUtils.isNotEmpty(source.getCurrentAccountPreference().getDaysOfTheMonth()) && JnJEmailFrequency.MONTHLY.equals(source.getCurrentAccountPreference().getConsolidatedEmailFrequency())) {
			jnjLaUserAccountPreferenceModel.setDayOfTheWeek(null);
			jnjLaUserAccountPreferenceModel.setDaysOfTheMonth(source.getCurrentAccountPreference().getDaysOfTheMonth());
		}
	}
    private JnJLaUserAccountPreferenceModel createJnJLaUserAccountPreferenceModel(JnJLaUserAccountPreferenceData jnjLaUserAccountPreference, JnJB2bCustomerModel user, JnJB2BUnitModel account) {
        JnJLaUserAccountPreferenceModel jnjLaUserAccountPreferenceModel = getModelService().create(JnJLaUserAccountPreferenceModel.class);
        jnjLaUserAccountPreferenceModel.setPeriodicity(jnjLaUserAccountPreference.getPeriodicity());
		if(JnJEmailPeriodicity.CONSOLIDATED.equals(jnjLaUserAccountPreference.getPeriodicity())) {
			jnjLaUserAccountPreferenceModel.setOrderTypes(jnjLaUserAccountPreference.getOrderTypes());
			jnjLaUserAccountPreferenceModel.setConsolidatedEmailFrequency(jnjLaUserAccountPreference.getConsolidatedEmailFrequency());
			if(JnJEmailFrequency.WEEKLY.equals(jnjLaUserAccountPreference.getConsolidatedEmailFrequency())) {
				jnjLaUserAccountPreferenceModel.setDayOfTheWeek(jnjLaUserAccountPreference.getDayOfTheWeek());
			}
			if(JnJEmailFrequency.MONTHLY.equals(jnjLaUserAccountPreference.getConsolidatedEmailFrequency())) {
				jnjLaUserAccountPreferenceModel.setDaysOfTheMonth(CollectionUtils.isNotEmpty(jnjLaUserAccountPreference.getDaysOfTheMonth()) ? jnjLaUserAccountPreference.getDaysOfTheMonth() : Collections.emptyList());
			}
		}
        jnjLaUserAccountPreferenceModel.setUser(user);
        jnjLaUserAccountPreferenceModel.setAccount(account);
        return jnjLaUserAccountPreferenceModel;
    }

}
