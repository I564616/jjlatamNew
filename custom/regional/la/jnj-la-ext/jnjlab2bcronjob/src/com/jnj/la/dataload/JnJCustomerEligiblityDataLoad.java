package com.jnj.la.dataload;


import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.mapper.JnjCustomerEligiblityDataLoadMapper;
import de.hybris.platform.cronjob.model.CronJobModel;


/**
 * Version 1.0 - Parses the Customer Eligibility Records <code>JnjCustomerEligiblityDTO</code> Version 1.1 -
 *
 * @author Manoj.K.Panda .
 */
public class JnJCustomerEligiblityDataLoad
{
	public static final Class currentClass = JnJCustomerEligiblityDataLoad.class;

	/** The Constant loadCustomerEligiblityData method name. */
	private static final String LOAD_CUSTOMER_ELIGIBILITY_DATA_METHOD = "loadCustomerEligiblityData()";

	/**
	 * The constant for "readRSAView" method name.
	 */
	private static final String READ_RSA_VIEW_METHOD = "readRSAView()";

	/**
	 * Message for NO Files found.
	 */
	private static final String NO_VIEW_FOUND_MESSAGE = "NO CUSTOMER ELIGIBLITY DATA LOAD VIEW FOUND AT THE RSA, CLOSING & EXITING THE READING OPERATION";

	/**
	 * SELECT PART.
	 */
	private static final String SQL_QUERY_SELECT_PART = "SELECT MATCUST_NUM, MATCUST_IND, PRODUCT_HIERARCHY_1, PRODUCT_HIERARCHY_2, PRODUCT_HIERARCHY_3, LAST_UPDATED_DATE FROM ";


	private static final String SQL_QUERY_ORDERBY_PART = " ORDER BY LAST_UPDATED_DATE";

	/**
	 * Mapper to perform READ & WRITE operations.
	 */
	private JnjCustomerEligiblityDataLoadMapper customerEligiblityDataLoadMapper;

	/**
	 * Interface Operation Architecture Utility, used for:
	 * <ul>
	 * <li>Read Operation</li>
	 * <li>Write Operation</li>
	 * <li>Remove Invalid Records</li>
	 * </ul>
	 */
	private JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	/**
	 * Indicates if the XMl file has been moved to the respective location successfully or not.
	 */

	/**
	 * Entry point for the Customer Eligibility Data Load Interface. Performs:
	 * <ul>
	 * <li>Read Operation: Sorts all XML files and then parse valid file to the intermediate tables.</li>
	 * <li>Write Operation: Process records form Intermediary tables to persist them in Hybris system.</li>
	 * </ul>
	 */
	public void loadCustomerEligiblityData(final JnjIntegrationRSACronJobModel jobModel)
	{
		logMethodStartOrEnd(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE, LOAD_CUSTOMER_ELIGIBILITY_DATA_METHOD,
				Logging.BEGIN_OF_METHOD);

		readCustomerEligibilityData(Jnjlab2bcoreConstants.CustomerEligiblity.CUSTOMER_ELIGIBILITY_DATA_LOAD_RSA_SCHEMA + "."
				+ Jnjlab2bcoreConstants.CustomerEligiblity.CUSTOMER_ELIGIBILITY_DATA_LOAD_RSA_VIEW, jobModel);

		logMethodStartOrEnd(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE, LOAD_CUSTOMER_ELIGIBILITY_DATA_METHOD,
				Logging.END_OF_METHOD);
	}


	/**
	 * Performs parsing of the valid XML files.
	 *
	 * @param customerEligibilityView
	 */
	private void readCustomerEligibilityData(final String customerEligibilityView, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String methodName = "readCustomerEligibilityData()";
		if (customerEligibilityView == null)
		{
			JnjGTCoreUtil.logInfoMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN, methodName,
					Logging.HYPHEN + NO_VIEW_FOUND_MESSAGE, currentClass);
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN, methodName,
					Logging.HYPHEN + " VIEW SELECTED : " + customerEligibilityView, currentClass);
			if (readCustomerEligibilityDataFromRSA(customerEligibilityView, jobModel))
			{
				JnjGTCoreUtil.logInfoMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN, methodName,
						Logging.HYPHEN + customerEligibilityView + " VIEW has been READ SUCCESSFULLY.", currentClass);
			}
			else
			{
				JnjGTCoreUtil.logErrorMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN, methodName,
						Logging.HYPHEN + customerEligibilityView + " VIEW COULD NOT be READ. Check the errors above.", currentClass);
			}

		}

	}


	/**
	 * Returns <code>Boolean.TRUE</code>, if successfully reads a RSA VIEW and SET it to DTO object.
	 *
	 * @param customerEligibilityView
	 * @param jobModel
	 * @return
	 */
	private boolean readCustomerEligibilityDataFromRSA(final String customerEligibilityView, final JnjIntegrationRSACronJobModel jobModel)
	{
		logMethodStartOrEnd(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE, READ_RSA_VIEW_METHOD, Logging.BEGIN_OF_METHOD);
		String readErrorMessage;
		boolean isReadSuccessful;
		try
		{
			readErrorMessage = getCustomerEligiblityDataLoadMapper()
					.populateCustomerEligibilityRecords(buildRSASQLQuery(customerEligibilityView, jobModel), jobModel);
			isReadSuccessful = (readErrorMessage != null && !readErrorMessage.isEmpty()) ? Boolean.FALSE.booleanValue()
					: Boolean.TRUE.booleanValue();
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE, READ_RSA_VIEW_METHOD,"Exception: " + e, e,currentClass);
			isReadSuccessful = Boolean.FALSE.booleanValue();
			updateReadDashboard(customerEligibilityView, Boolean.FALSE, e.getMessage());
			return isReadSuccessful;
		}

		logMethodStartOrEnd(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE, READ_RSA_VIEW_METHOD, Logging.END_OF_METHOD);
		return isReadSuccessful;
	}

	private String buildRSASQLQuery(final String customerEligibilityView, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String lastLogDateTime = interfaceOperationArchUtility.getLastSuccesfulStartTimeForJob(jobModel);
		final String methodName = "buildRSASQLQuery()";

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD,
				methodName, Logging.HYPHEN + "Last Successful record process time  ::: " + lastLogDateTime, currentClass);

		String query = SQL_QUERY_SELECT_PART + customerEligibilityView + SQL_QUERY_ORDERBY_PART;

		if (lastLogDateTime != null)
		{
			query = SQL_QUERY_SELECT_PART + customerEligibilityView + " WHERE LAST_UPDATED_DATE > '" + lastLogDateTime + "'"
					+ SQL_QUERY_ORDERBY_PART;
		}

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.Logging.END_OF_METHOD,
				methodName, Logging.HYPHEN + "Final Query formed ::: " + query, currentClass);

		return query;
	}


	/**
	 * Updates READ dashboard with the status values.
	 *
	 * @param viewName
	 * @param isSuccess
	 * @param errorMessage
	 */
	private void updateReadDashboard(final String viewName, final Boolean isSuccess, final String errorMessage)
	{
		final String methodName = "updateReadDashboard()";

		JnjGTCoreUtil.logInfoMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN,
				methodName, Logging.HYPHEN + "Updating READ Dashboard for view: " + viewName, currentClass);

		interfaceOperationArchUtility.updateReadDashboard(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE, viewName,
				(isSuccess.booleanValue()) ? RecordStatus.SUCCESS.toString() : RecordStatus.ERROR.toString(),
				isSuccess.booleanValue(), errorMessage);

	}

	/**
	 * Utility method used for logging entry into / exit from any method in debug mode.
	 *
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
	 */
	private void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE + Logging.HYPHEN,
				methodName, Logging.HYPHEN + entryOrExit + Logging.HYPHEN + System.currentTimeMillis(), currentClass);
	}

	public void setInterfaceOperationArchUtility(final JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility)
	{
		this.interfaceOperationArchUtility = interfaceOperationArchUtility;
	}

	/**
	 * @return the customerEligiblityDataLoadMapper
	 */
	public JnjCustomerEligiblityDataLoadMapper getCustomerEligiblityDataLoadMapper()
	{
		return customerEligiblityDataLoadMapper;
	}

	public void setCustomerEligiblityDataLoadMapper(final JnjCustomerEligiblityDataLoadMapper customerEligiblityDataLoadMapper)
	{
		this.customerEligiblityDataLoadMapper = customerEligiblityDataLoadMapper;
	}

}
