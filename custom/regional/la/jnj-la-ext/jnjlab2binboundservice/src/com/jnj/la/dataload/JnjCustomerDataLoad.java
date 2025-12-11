package com.jnj.la.dataload;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.dto.JnJLaCustomerDTO;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.mapper.JnjLatamRSACustomerDataLoadMapper;
import com.jnj.la.dataload.mapper.JnjRSACustomerDataLoadMapper;
import com.jnj.la.dataload.services.JnjCustomerDataLoadService;


/**
 * The Class JnjCustomerDataLoad.
 */
public class JnjCustomerDataLoad
{

	/** The Constant METHOD_NAME_LOAD_CUSTOMER_DATA. */
	private static final String METHOD_NAME_LOAD_CUSTOMER_DATA = "loadCustomerData()";

	/** The jnj customer data load mapper. */
	@Autowired
	private JnjRSACustomerDataLoadMapper jnjCustomerDataLoadMapper;

	@Autowired
	private JNJRSADBConnector rsaDBConnector;

	@Autowired
	private JnjCustomerDataLoadService jnjCustomerDataLoadService;

	/**
	 * Instance of <code>JnjInterfaceOperationArchUtility</code>
	 */
	@Autowired
	private JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	private Map<Integer, List<JnJLaCustomerDTO>> listOfAllCustomers = null;

	private static Map<Object, String> listOfErroneousRecords = new HashMap<>();

	/**
	 * Load customer data.
	 * 
	 */
	public void loadCustomerData(final JnjIntegrationRSACronJobModel jobModel)
	{
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, METHOD_NAME_LOAD_CUSTOMER_DATA, Logging.BEGIN_OF_METHOD,
				JnjCustomerDataLoad.class);

		listOfAllCustomers = jnjCustomerDataLoadService.fetchCustomerRecords(jobModel);

		if (null != listOfAllCustomers && listOfAllCustomers.get(0) != null && !listOfAllCustomers.get(0).isEmpty())
		{
			for (final JnJLaCustomerDTO record : listOfAllCustomers.get(0))
			{
				if (jnjCustomerDataLoadMapper.mapCustomerDataForSoldTo(record).containsKey(Boolean.TRUE))
				{
					JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataForSoldTo",
							" Record Processed Successfully, update timestamp in job", JnjCustomerDataLoad.class);
					getInterfaceOperationArchUtility().setLastSuccesfulStartTimeForJob(record.getLastUpdateDate(), jobModel);
				}
				else
				{
					listOfErroneousRecords.put(record, jnjCustomerDataLoadMapper.mapCustomerDataForSoldTo(record).get(Boolean.FALSE));
				}
			}
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataForSoldTo",
					"No jnJSapAccountModel Records with status 'PENDING' exists in Hybris for 'Sold To'. Exiting the write process.",
					JnjCustomerDataLoad.class);
		}

		if (null != listOfAllCustomers && listOfAllCustomers.get(1) != null && !listOfAllCustomers.get(1).isEmpty())
		{
			for (final JnJLaCustomerDTO record : listOfAllCustomers.get(1))
			{
				if (jnjCustomerDataLoadMapper.mapCustomerDataForKeyAccount(record).containsKey(Boolean.TRUE))
				{
					JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataForKeyAccount",
							" Record Processed Successfully, update timestamp in job", JnjCustomerDataLoad.class);
					getInterfaceOperationArchUtility().setLastSuccesfulStartTimeForJob(record.getLastUpdateDate(), jobModel);
				}
				else
				{
					listOfErroneousRecords.put(record,
							jnjCustomerDataLoadMapper.mapCustomerDataForKeyAccount(record).get(Boolean.FALSE));
				}
			}
		}

		if (null != listOfAllCustomers && listOfAllCustomers.get(2) != null && !listOfAllCustomers.get(2).isEmpty())
		{
			for (final JnJLaCustomerDTO record : listOfAllCustomers.get(2))
			{
				if (jnjCustomerDataLoadMapper.mapCustomerDataForShipTo(record).containsKey(Boolean.TRUE))
				{
					JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataForShipTo",
							" Record Processed Succsfully, update timestamp in job", JnjCustomerDataLoad.class);
					getInterfaceOperationArchUtility().setLastSuccesfulStartTimeForJob(record.getLastUpdateDate(), jobModel);
				}
				else
				{
					listOfErroneousRecords.put(record, jnjCustomerDataLoadMapper.mapCustomerDataForShipTo(record).get(Boolean.FALSE));
				}
			}
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataForShipTo",
					"No jnJSapAccountModel Records with status 'PENDING' exists in Hybris for 'Ship To'. Exiting the write process.",
					JnjCustomerDataLoad.class);
		}

		if (null != listOfAllCustomers && listOfAllCustomers.get(3) != null && !listOfAllCustomers.get(3).isEmpty())
		{
			for (final JnJLaCustomerDTO record : listOfAllCustomers.get(3))
			{
				if (jnjCustomerDataLoadMapper.mapCustomerDataforIndirectCustomer(record).containsKey(Boolean.TRUE))
				{
					JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataforIndirectCustomer",
							" Record Processed Succsfully, update timestamp in job", JnjCustomerDataLoad.class);
					getInterfaceOperationArchUtility().setLastSuccesfulStartTimeForJob(record.getLastUpdateDate(), jobModel);
				}
				else
				{
					listOfErroneousRecords.put(record,
							jnjCustomerDataLoadMapper.mapCustomerDataforIndirectCustomer(record).get(Boolean.FALSE));
				}
			}
		}
		else
		{
			JnjGTCoreUtil
					.logDebugMessage(
							Logging.UPSERT_CUSTOMER_NAME,
							"mapCustomerDataforIndirectCustomer",
							"No jnjCustomerDTO Records with status 'PENDING' exists in Hybris for 'Indirect Customer'. Exiting the write process.",
							JnjLatamRSACustomerDataLoadMapper.class);
		}



		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, METHOD_NAME_LOAD_CUSTOMER_DATA, Logging.END_OF_METHOD,
				JnjCustomerDataLoad.class);
	}


	public Map<Integer, List<JnJLaCustomerDTO>> getListOfAllCustomers()
	{
		return listOfAllCustomers;
	}


	/**
	 * @return the interfaceOperationArchUtility
	 */
	public JnjLaInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}


	/**
	 * @param interfaceOperationArchUtility
	 *           the interfaceOperationArchUtility to set
	 */
	public void setInterfaceOperationArchUtility(final JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility)
	{
		this.interfaceOperationArchUtility = interfaceOperationArchUtility;
	}


	/**
	 * @return the listOfErroneousRecords
	 */
	public static Map<Object, String> getListOfErroneousRecords()
	{
		return listOfErroneousRecords;
	}


	/**
	 * @param listOfErroneousRecords
	 *           the listOfErroneousRecords to set
	 */
	public static void setListOfErroneousRecords(final Map<Object, String> listOfErroneousRecords)
	{
		JnjCustomerDataLoad.listOfErroneousRecords = listOfErroneousRecords;
	}

}
