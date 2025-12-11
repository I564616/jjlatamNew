package com.jnj.la.dataload.mapper;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjCustomerEligiblityDTO;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.dto.JnjLaCustomerEligiblityDTO;
import com.jnj.la.core.services.customer.JnjLaCustomerEligibilityService;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class responsible for mapping <code>JnjCustomerEligiblityDTO</code> objects to the Customer Eligibility relationship
 * between B2B Unit and categories.
 *
 * @author Manoj.K.Panda
 *
 */
public class JnjCustomerEligiblityDataLoadMapper extends AbstractDataloadMapper<JnjCustomerEligiblityDTO>
{
	public static final Class currentClass = JnjCustomerEligiblityDataLoadMapper.class;

	/**
	 * Instance of <code>JnjCustomerEligibilityService</code>
	 */
	private JnjLaCustomerEligibilityService customerEligibilityService;

	/**
	 * Instance of <code>JnjInterfaceOperationArchUtility</code>
	 */
	private JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	/**
	 * Constant to store MAXIMUM WRITE ATTEMPTS count.
	 */

	private String errorMessage;

	/**
	 * Builds category code based on the hierarchy of category codes sent.
	 *
	 * @param categoryCodes
	 * @return restrictedCategoryCode String
	 */
	private String buildRestrictedCategoryCode(final List<String> categoryCodes)
	{
		final StringBuilder restrictedCategoryCode = new StringBuilder();
		for (final String code : categoryCodes)
		{
			if (code != null)
			{
				restrictedCategoryCode.append(code);
			}
		}
		return restrictedCategoryCode.toString();
	}

	/**
	 * Retrieves restricted category code based on code and country code associated with its catalog.
	 *
	 * @param restrictedCategoryCode
	 * @param countryCode
	 * @return CategoryModel
	 */
	private CategoryModel getRestrictedCategory(final String restrictedCategoryCode, final String countryCode)
	{
		if (restrictedCategoryCode != null)
		{
			return getCustomerEligibilityService().getCategoryByCode(restrictedCategoryCode, countryCode);
		}
		return null;
	}

	/**
	 * @return the customerEligibilityService
	 */
	public JnjLaCustomerEligibilityService getCustomerEligibilityService()
	{
		return customerEligibilityService;
	}

	public void setCustomerEligibilityService(final JnjLaCustomerEligibilityService customerEligibilityService)
	{
		this.customerEligibilityService = customerEligibilityService;
	}

	public JnjLaInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}

	public void setInterfaceOperationArchUtility(final JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility)
	{
		this.interfaceOperationArchUtility = interfaceOperationArchUtility;
	}

	@Override
	public String populateCustomerEligibilityRecords(final String customerEligibilityQuery, final JnjIntegrationRSACronJobModel jobModel)
	{
		errorMessage = null;
		final String methodName = "populateCustomerEligibilityRecords()";
		final Set<JnjCustomerEligiblityDTO> records = getCustomerEligibilityService()
				.getCustomerEligibilityRecords(customerEligibilityQuery);
		for (final JnjCustomerEligiblityDTO record : records)
		{
			if (processCustomerEligibilityRecords(record))
			{
				JnjGTCoreUtil.logInfoMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN,
						methodName, " Record Processed Successfully, update timestamp in job", currentClass);

				getInterfaceOperationArchUtility()
						.setLastSuccesfulStartTimeForJob(((JnjLaCustomerEligiblityDTO) record).getLastUpdateDate(), jobModel);
			}
		}
		return errorMessage;
	}

	@Override
	public boolean processCustomerEligibilityRecords(final JnjCustomerEligiblityDTO record)
	{
		JnJB2BUnitModel jnjB2bUnitAccount;
		final String methodName = "processCustomerEligibilityRecords()";

		boolean processSuccessful = true;
		final Set<CategoryModel> updatedRestrictedCategories = new HashSet<>();

		final List<String> categoryCodes = Arrays.asList(record.getFirstLevelCategoryId(), record.getSecondLevelCategoryId(),
				record.getThirdLevelCategoryId());
		try
		{
			jnjB2bUnitAccount = (JnJB2BUnitModel) getCustomerEligibilityService().getB2bUnitAccount(record.getB2bAccountUnitId());
			if (jnjB2bUnitAccount == null)
			{
				errorMessage = "Customer " + record.getB2bAccountUnitId() + " is not in the platform.";
				throw new BusinessException(errorMessage);
			}
			else
			{
				final Set<CategoryModel> existingRestrictedCategories = jnjB2bUnitAccount.getRestrictedCategories();
				if (existingRestrictedCategories != null && !existingRestrictedCategories.isEmpty())
				{
					updatedRestrictedCategories.addAll(existingRestrictedCategories);
				}

				final CategoryModel restrictedCategory = getRestrictedCategory(buildRestrictedCategoryCode(categoryCodes),
						(jnjB2bUnitAccount.getCountry() == null) ? null : jnjB2bUnitAccount.getCountry().getIsocode());

				if (restrictedCategory == null)
				{
					errorMessage = "Error occurred while processing record with B2B Id: " + record.getB2bAccountUnitId()
							+ ". Could not find Category from the catalog as per the given hierarchy of restricted category codes";

					throw new BusinessException(errorMessage);
				}
				else
				{
					switch (record.getStatus())
					{
						case "I":
						case "U":
							if (!updatedRestrictedCategories.contains(restrictedCategory)){
								updatedRestrictedCategories.add(restrictedCategory);
							}
							JnjGTCoreUtil.logInfoMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN,
									methodName, "restriction (" + record.getFirstLevelCategoryId()+ " - "
											+ record.getSecondLevelCategoryId() + " - "+ record.getThirdLevelCategoryId() +
											") added for B2BUnit " + record.getB2bAccountUnitId(), currentClass);
							break;
						case "D":
							JnjGTCoreUtil.logInfoMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN,
									methodName, "Removing the restriction (" +
							record.getFirstLevelCategoryId()+ " - " + record.getSecondLevelCategoryId() + " - "+
							record.getThirdLevelCategoryId() +") for B2BUnit " + record.getB2bAccountUnitId(), currentClass);
							updatedRestrictedCategories.remove(restrictedCategory);
							break;
					}
				}
				jnjB2bUnitAccount.setRestrictedCategories(updatedRestrictedCategories);
				getCustomerEligibilityService().saveItem(jnjB2bUnitAccount);
				return processSuccessful;
			}
		}
		catch (final ModelSavingException | BusinessException e)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN + "",
					methodName," Error occurred while mapping Customer Eligibility DTO: " + Logging.HYPHEN + e, e,currentClass);
			processSuccessful = false;
			return processSuccessful;
		}

	}

}
