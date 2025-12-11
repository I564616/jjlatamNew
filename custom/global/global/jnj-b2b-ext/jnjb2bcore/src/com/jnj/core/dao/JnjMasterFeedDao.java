package com.jnj.core.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jnj.core.dto.JnjCanonicalDTO;
import com.jnj.core.model.JnjIntegrationCronJobModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;

/**
 * @author 
 *
 */
public interface JnjMasterFeedDao {

	/**
	 * @param jnjIntegrationRSACronJobModel
	 * @return
	 */
	public List<LinkedHashMap<String, String>> getDataToLoad(JnjIntegrationCronJobModel jnjIntegrationRSACronJobModel);

	/**
	 * @param jobCode
	 * @param compositePrimaryKeyParams
	 * @return
	 */
	public Map<String, JnjCanonicalDTO> getCanonicalMetaData(String jobCode, List<String> compositePrimaryKeyParams);

	/**
	 * @param itemModel
	 * @param attributeName
	 * @param attributeValue
	 * @param jnjIntegrationCronJobModel
	 * @return
	 */
	public ItemModel fetchCompositeItemModel(String itemModel, String attributeName, String attributeValue,
			JnjIntegrationCronJobModel jnjIntegrationCronJobModel);

	/**
	 * @param itemModelName
	 * @param resultSetMap
	 * @param canMapData
	 * @param jobModel
	 * @param compositePrimaryParams
	 * @return
	 */
	public ItemModel fetchExistingOrCreateItemModel(String itemModelName, Map<String, String> resultSetMap,
			Map<String, JnjCanonicalDTO> canMapData, JnjIntegrationCronJobModel jobModel,
			List<String> compositePrimaryParams);

	/**
	 * @param resultSetMap
	 * @param compositePrimaryParams
	 * @param jobModel
	 * @param newStatus
	 * @param existingStatus
	 * @return
	 */
	public Boolean updateStagingRecordStatus(Map<String, String> resultSetMap, List<String> compositePrimaryParams,
			JnjIntegrationCronJobModel jobModel, int newStatus, int existingStatus);
	
	/**
	 * @param resultSetMap
	 * @param compositePrimaryParams
	 * @param jobModel
	 * @param newStatus
	 * @param existingStatus
	 * @return
	 */
	public Boolean updateBulkStagingRecordStatus(JnjIntegrationCronJobModel jobModel, int newStatus, int existingStatus);
	
	/**
	 * @param resultSetMap
	 * @param compositePrimaryParams
	 * @param jobModel
	 * @param newStatus
	 * @param existingStatus
	 * @return
	 */
	public Boolean updateBulkStagingRecordStatus(JnjIntegrationCronJobModel jobModel, int newStatus, String existingStatusList);

	/**
	 * @param lastUpdatedate
	 * @param integrationRSACronJobModel
	 */
	public void setLastSuccesfulStartTimeForJob(final String lastUpdatedate,
			final JnjIntegrationRSACronJobModel integrationRSACronJobModel);

	/**
	 * @param enumModel
	 * @param entry
	 * @return
	 */
	public HybrisEnumValue getEnumValue(String enumModel, String entry);

	/**
	 * @param entry
	 * @param canMapData
	 * @param ItemModel
	 * @param itemModelName
	 * @param attributeName
	 * @param attributeValue
	 * @param jobModel
	 */
	public void saveCompositeValue(Map.Entry<String, String> entry, Map<String, JnjCanonicalDTO> canMapData,
			ItemModel ItemModel, String itemModelName, String attributeName, String attributeValue,
			JnjIntegrationCronJobModel jobModel);

	/**
	 * @param jnjCanonicalDTO
	 * @param itemModel
	 * @param attributeValue
	 * @param jobModel
	 */
	public void saveCollectionValues(JnjCanonicalDTO jnjCanonicalDTO, ItemModel itemModel, String attributeValue,
			JnjIntegrationCronJobModel jobModel);
	/**
	 * @param jnjCanonicalDTO
	 * @param itemModel
	 * @param attributeValue
	 */
	public void saveBooleanValue(JnjCanonicalDTO jnjCanonicalDTO,ItemModel itemModel,String attributeValue);
	public void saveCustomValue(JnjCanonicalDTO jnjCanonicalDTO,ItemModel itemModel, Map<String, String> resultSetMap);

 
	/**
	 * @param orderNumber
	 * @param jobModel 
	 */
	//public void removeStagingRecord(String orderNumber, JnjIntegrationCronJobModel jobModel);
	
	
}
