package com.jnj.core.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.JnjMasterFeedDao;
import com.jnj.core.dto.JnjCanonicalDTO;
import com.jnj.core.model.JnjIntegrationCronJobModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.services.JnjMasterFeedService;


import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;


import com.jnj.core.util.JnJIntegrationTableUtil;
import com.jnj.core.util.JnjGTConversionUtils;
import org.apache.log4j.Logger;

import org.apache.commons.collections4.CollectionUtils;


/**
 * @author 
 *
 */
public class DefaultJnjMasterFeedService implements JnjMasterFeedService {

	@Autowired
	JnjMasterFeedDao jnjMasterFeedDao;
	
	@Autowired
	ModelService modelService;
	
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjMasterFeedService.class);


	private static final int NEW_STATUS = 2;
	private static final int FAILED_STATUS = 9;
	private static final int INTERMEDIATE_STATUS = 1;
	private static final String COLUMN_NAME_FOR_UPDATING_RSA_CRONJOB = "jnj.rsa.cronjob.date.column.value.provider";
	private static final String EXISTING_STATUS_LIST = "0,-9";



	@Override
	public boolean loadData(JnjIntegrationCronJobModel jobModel) {
		
		LOGGER.info("######### Starting Data Load for " + jobModel.getJobCode());
		boolean dataloaded = false;
		// Step 1 - Query Canonical Mapping Table with job code
		// Step 2 - Create map of source column to DTO (rest of the columns
		// except jobCode and Source columns in Canonical mapping table)
		// Map<String, DTO>
		List<String> compositePrimaryKeyParams = new ArrayList<>();

		Map<String, JnjCanonicalDTO> canMapData = jnjMasterFeedDao.getCanonicalMetaData(jobModel.getJobCode(), compositePrimaryKeyParams);

		// Create List of primary column
		// List<String> compositePrimaryKeyParams = jnjMasterFeedDao.compositePrimaryKeyParams(jobModel.getJobCode());
		// Step 3 - Connect to Staging DB using JDBC Template
		// Step 4 - Query Staging DB with the query configured in Cronjob

		LOGGER.info("######### Before Fetching Data " + new Timestamp(System.currentTimeMillis()));
		final List<LinkedHashMap<String, String>> dataToLoad = jnjMasterFeedDao.getDataToLoad(jobModel);
		LOGGER.info("###### Record size " + dataToLoad.size());
		LOGGER.info("######### After Fetching Data " + new Timestamp(System.currentTimeMillis()));
				
		if (jobModel.getUpdateMigrationStatusRequired()) {
			//Update staging table status to intermediate status 1 - bulk update
			LOGGER.info("######### Before Updating Intermediate Status "+new Timestamp(System.currentTimeMillis()));
			jnjMasterFeedDao.updateBulkStagingRecordStatus(jobModel, INTERMEDIATE_STATUS, EXISTING_STATUS_LIST);
			LOGGER.info("######### After Updating Intermediate Status  "+new Timestamp(System.currentTimeMillis()));
		}

		// Step 5 - Iterator through results
		// Step 5(a) - Get target model instance (create new if does not exist)
		// Step 5(b) - Process each record
		// Step 5(c) - save target model
		// Step 5(d) - update status back in Staging DB (OPTIONAL)

		if (processSourceRecords(dataToLoad, canMapData, jobModel, compositePrimaryKeyParams)) {
			dataloaded = true;
			// Update staging db record status (OPTIONAL)
		}
		return dataloaded;

	}

	private boolean processSourceRecords(List<LinkedHashMap<String, String>> dataToLoad, Map<String, JnjCanonicalDTO> canMapData, JnjIntegrationCronJobModel jobModel, List<String> compositePrimaryParams) {
		boolean recordSaved = false;
		Map<String, String> resultSetMap = null;
		JnjCanonicalDTO jnjCanonicalDTO = null;

		ItemModel itemModel;
		Map<String, List<ItemModel>> collectionItemsMap = new HashMap<>();
		final Set<String> keySet = new HashSet<>();

		boolean successfulUpdate = false;
		// Step 5 - Iterator through results
		if (dataToLoad != null) {
			for (int i = 0; i < dataToLoad.size(); i++) {
				try {
					// Get One Result
					resultSetMap = dataToLoad.get(i);
					// ItemModel to be saved
					itemModel = jnjMasterFeedDao.fetchExistingOrCreateItemModel(jobModel.getTargetModel(), resultSetMap, canMapData, jobModel, compositePrimaryParams);
					// Save other property
					for (Map.Entry<String, String> entry : resultSetMap.entrySet()) {
						jnjCanonicalDTO = canMapData.get(entry.getKey());
						// Check property should not be primaryKey
						if (jnjCanonicalDTO != null && !jnjCanonicalDTO.isPrimaryKey() && jnjCanonicalDTO.getTargetColumn() != null) {
							// Step 5(a) - set the other attribute in model
							// instance by checking transition type (create
							// new if does not exist)
							populateRecordInTargetModel(jnjCanonicalDTO, itemModel, jobModel, entry, compositePrimaryParams, canMapData, resultSetMap);
						}
					}
					modelService.saveAll(itemModel);
					collectionItemsMap.clear();
					itemModel = null;
					recordSaved = true;


					//jnjMasterFeedDao.updateStagingRecordStatus(resultSetMap, compositePrimaryParams, jobModel, newStatus, existingStatus);
					//checking post process step is available for the job if so the collect the key value in the list
					successfulUpdate = true;
					if (jobModel.getKeyToCollect() != null) {
						keySet.add(resultSetMap.get(jobModel.getKeyToCollect()));
					}
				
					
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage());
					recordSaved = false;

					if (jobModel.getUpdateMigrationStatusRequired()) {
						LOGGER.info("######### Before Updating Failed Record Status " + new Timestamp(System.currentTimeMillis()));
						jnjMasterFeedDao.updateStagingRecordStatus(resultSetMap, compositePrimaryParams, jobModel, FAILED_STATUS, INTERMEDIATE_STATUS);
						LOGGER.info("######### After Updating Failed Record Status " + new Timestamp(System.currentTimeMillis()));
					}

					continue;
				}
			}
			
			//here we need to call post processor

			if (jobModel.getPostProcessingClassName() != null && CollectionUtils.isNotEmpty(keySet)) {
				JnJIntegrationTableUtil.getUtilReturnValue(jobModel, null, null, jnjCanonicalDTO, dataToLoad, keySet);
				LOGGER.info("######### Post process calculation completed ");
			} else {
				LOGGER.info("######### There is no post process called for this job becasue not assigned post process for this job");
			}

			if (jobModel instanceof JnjIntegrationRSACronJobModel) {
				jnjMasterFeedDao.setLastSuccesfulStartTimeForJob(resultSetMap.get(Config.getParameter(COLUMN_NAME_FOR_UPDATING_RSA_CRONJOB)), (JnjIntegrationRSACronJobModel) jobModel);

			}
			
			if (jobModel.getUpdateMigrationStatusRequired() && successfulUpdate) {
				LOGGER.info("######### Performing Status Update for Successfull Records " + new Timestamp(System.currentTimeMillis()));
				jnjMasterFeedDao.updateBulkStagingRecordStatus(jobModel, NEW_STATUS, INTERMEDIATE_STATUS);
				LOGGER.info("######### Done Performing Status Update for Successfull Records " + new Timestamp(System.currentTimeMillis()));
			}
		}
		return recordSaved;
	}

	private void populateRecordInTargetModel(JnjCanonicalDTO jnjCanonicalDTO, ItemModel itemModel, JnjIntegrationCronJobModel jobModel, Map.Entry<String, String> entry, List<String> compositePrimaryParams, Map<String, JnjCanonicalDTO> canMapData, Map<String, String> resultSetMap) {

		// case direct:
		// case format:
		// case locale:
		// case collection:
		// case composite:
		// case custom:

		String transitionType = jnjCanonicalDTO.getTransitionType().trim();
		switch (transitionType) {
			case "DIRECT":
				itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), entry.getValue());
				break;
				
			case "FORMAT":
				switch (jnjCanonicalDTO.getTargetColumnFormat()) {
					case "INTEGER":
						itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), JnjGTConversionUtils.convertStringToInt(entry.getValue()));
						break;
						
					case "LONG":
						itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), JnjGTConversionUtils.convertStringToLong(entry.getValue()));
						break;
						
					case "DOUBLE":
						itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), JnjGTConversionUtils.convertStringToDouble(entry.getValue()));
						break;
						
					case "DATE":
						itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), JnjGTConversionUtils.convertStringToDateFormat(entry.getValue()));
						break;
						
					case "FLOAT":
						itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), JnjGTConversionUtils.convertStringToFloat(entry.getValue()));
						break;
				}
				break;
	
			case "LOCALE":
				String fieldWithLocale = entry.getValue();
				if (fieldWithLocale != null && fieldWithLocale.trim().length() > 0) {
					itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), Locale.of(fieldWithLocale.substring(fieldWithLocale.indexOf("|") + 1, fieldWithLocale.length()).toLowerCase()), fieldWithLocale.substring(0, fieldWithLocale.indexOf("|")));
				}
				break;
	
			case "COLLECTION":
				jnjMasterFeedDao.saveCollectionValues(jnjCanonicalDTO, itemModel, entry.getValue(), jobModel);
				break;
	
			case "COMPOSITE":
				jnjMasterFeedDao.saveCompositeValue(entry, canMapData, itemModel, jnjCanonicalDTO.getCompositeType(), jnjCanonicalDTO.getCompositeTypeUid(), entry.getValue(), jobModel);
				break;
	
			case "CUSTOM":
				jnjMasterFeedDao.saveCustomValue(jnjCanonicalDTO, itemModel, resultSetMap);
				// itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(),
				// JnJIntegrationTableUtil.getUtilReturnValue(jnjCanonicalDTO.getUtilClassNamePath(),
				// jnjCanonicalDTO.getUtilMethodName(),
				// jnjCanonicalDTO.getUtilReturnType(),
				// jnjCanonicalDTO.getUtilMethodParam(), jnjCanonicalDTO,
				// resultSetMap));
				break;
	
			case "BOOLEAN":
				jnjMasterFeedDao.saveBooleanValue(jnjCanonicalDTO, itemModel, entry.getValue());
				break;
	
			case "ENUM":
				itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), jnjMasterFeedDao.getEnumValue(jnjCanonicalDTO.getTargetModel(), entry.getValue()));
				break;
	
			default:
				break;

		}
	}
	
	// conversion moved to JnjGTConversionUtils class
	
}
