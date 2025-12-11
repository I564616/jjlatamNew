package com.jnj.core.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.jnj.core.connector.JNJDBConnector;
import com.jnj.core.constants.GeneratedJnjb2bCoreConstants.Enumerations.JnjIntJobTransType;
import com.jnj.core.dao.JnjMasterFeedDao;
import com.jnj.core.dto.JnjCanonicalDTO;
import com.jnj.core.model.IntegrationJobDetailsModel;
import com.jnj.core.model.JnjIntegrationCronJobModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.DefaultJnjMasterCommonUtil;
import com.jnj.core.util.JnJIntegrationTableUtil;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

public class DefaultJnjMasterFeedDao implements JnjMasterFeedDao {
	
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjMasterFeedDao.class);

	private static final String FETCH_SOURCE_TARGET_COLUMNS = "SELECT {PK} FROM {IntegrationJobDetails} WHERE {jobCode}=?jobCode";
	private static final String JOB_CODE = "jobCode";
	private static final String STR_AND = " AND ";
	private static final String STR_WHERE = " WHERE ";
	private static final String SEQUENCEID = "SEQ";
	private static final String COLUMN_NAME_FOR_RSA="jnj.rsa.cronjob.data.filter.column";
	private static final String GREATER_THAN_SYMBOL=" > ";
	private static final String CRONJOB_NAMES = "jnj.cronjob.names";


	@Autowired
	CatalogVersionService catalogVersionService;

	@Autowired
	JNJDBConnector dBConnector;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	ModelService modelService;

	@Autowired
	DefaultJnjMasterCommonUtil defaultJnjMasterCommonUtil;
	@Autowired
	private EnumerationService enumerationService;

	@Override
	public List<LinkedHashMap<String, String>> getDataToLoad(JnjIntegrationCronJobModel jobModel) {

		List<LinkedHashMap<String, String>> returnSet;
		try{
   		if (jobModel instanceof JnjIntegrationRSACronJobModel) {
   
   			final String lastLogDateTime = defaultJnjMasterCommonUtil
   					.getLastSuccesfulStartTimeForJob((JnjIntegrationRSACronJobModel) jobModel);
   			String query = jobModel.getCronJobQuery()+STR_AND + Config.getParameter(COLUMN_NAME_FOR_RSA)+GREATER_THAN_SYMBOL+" '" + lastLogDateTime + "'";
   
   			returnSet = dBConnector.getJdbcTemplate().queryForObject(query, new JnjMasterFeedRowMapper());
   
   		} else {
   			LOGGER.info("########################## CronJob Query " + jobModel.getCronJobQuery());
   			returnSet = dBConnector.getJdbcTemplate().queryForObject(jobModel.getCronJobQuery(),
   					new JnjMasterFeedRowMapper());
   		}
   
   		return returnSet;
		
   	} catch (EmptyResultDataAccessException ex) {
   		LOGGER.error(ex.getMessage());
   		return null;
   	}
	}

	@Override
	public Map<String, JnjCanonicalDTO> getCanonicalMetaData(String jobCode, List<String> compositePrimaryKeyParams) {
		Map<String, JnjCanonicalDTO> sourceAndTargetfields = new HashMap<>();
		//LinkedHashMap<String, JnjCanonicalDTO> primarySourceAtTop = new LinkedHashMap<>();
		final List<IntegrationJobDetailsModel> results;
		JnjCanonicalDTO jnjCanonicalDTO = null;
		try {

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FETCH_SOURCE_TARGET_COLUMNS);

			fQuery.addQueryParameter(JOB_CODE, jobCode);
			results = getFlexibleSearchService().<IntegrationJobDetailsModel>search(fQuery).getResult();

			if (!results.isEmpty()) {
				for (final IntegrationJobDetailsModel jnjcanonicalModel : results) {

					jnjCanonicalDTO = new JnjCanonicalDTO();
					jnjCanonicalDTO.setTargetColumn(jnjcanonicalModel.getTargetColumn());
					jnjCanonicalDTO.setTargetModel(jnjcanonicalModel.getTargetModel());
					jnjCanonicalDTO.setJobCode(jnjcanonicalModel.getJobCode());
					jnjCanonicalDTO.setPrimaryKey(jnjcanonicalModel.getPartOfPrimary());
					jnjCanonicalDTO.setTransitionType(jnjcanonicalModel.getTransType().getCode());
					jnjCanonicalDTO.setSourceColumn(jnjcanonicalModel.getSourceColumn());
					if (jnjcanonicalModel.getPartOfPrimary()) {
						if(jnjcanonicalModel.getTransType().getCode().equalsIgnoreCase(JnjIntJobTransType.CUSTOM)){
							jnjCanonicalDTO.setPrimaryKeyOfCustomType(Boolean.TRUE);	
						}
						compositePrimaryKeyParams.add(jnjcanonicalModel.getSourceColumn());
					}
					jnjCanonicalDTO.setCompositeType(jnjcanonicalModel.getCompositeType());
					jnjCanonicalDTO.setCompositeTypeUid(jnjcanonicalModel.getCompositeTypeUID());
					if (jnjcanonicalModel.getTargetColumnFormat() != null) {
						jnjCanonicalDTO.setTargetColumnFormat(jnjcanonicalModel.getTargetColumnFormat().getCode());
					}
					sourceAndTargetfields.put(jnjcanonicalModel.getSourceColumn(), jnjCanonicalDTO);
					// Added for Util Method Creation Start
					if (null != jnjcanonicalModel.getUtilClassNamePath()) {
						jnjCanonicalDTO.setUtilClassNamePath(jnjcanonicalModel.getUtilClassNamePath());
					}
					if (null != jnjcanonicalModel.getUtilMethodName()) {
						jnjCanonicalDTO.setUtilMethodName(jnjcanonicalModel.getUtilMethodName());
					}
					if (null != jnjcanonicalModel.getUtilReturnType()) {
						jnjCanonicalDTO.setUtilReturnType(jnjcanonicalModel.getUtilReturnType());
					}
					if (null != jnjcanonicalModel.getUtilMethodParam()) {
						jnjCanonicalDTO.setUtilMethodParam(jnjcanonicalModel.getUtilMethodParam());
					}
					jnjCanonicalDTO.setSourceColumn(jnjcanonicalModel.getSourceColumn());
					// Added for Util Method Creation End
					jnjCanonicalDTO.setWritable(jnjcanonicalModel.getIsWritable());
				}
			}
			//primarySourceAtTop.putAll(sourceAndTargetfields);

		} catch (final ModelNotFoundException modelNotFound) {

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}

		return sourceAndTargetfields;

	}

	@Override
	public ItemModel fetchCompositeItemModel(final String itemModelName, final String attributeName,
			final String attributeValue, final JnjIntegrationCronJobModel cronJobModel) {

		ItemModel fetchItemModel = null;
		Map<String, String> queryParams = new HashMap<>();

		queryParams.put(attributeName, attributeValue);
		FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				buildQuery(itemModelName, null, queryParams, cronJobModel.getWhereCondition()), queryParams);
		final List<ItemModel> listResult = flexibleSearchService.<ItemModel> search(fQuery).getResult();

		try {
			if (listResult != null && !listResult.isEmpty()) {
				fetchItemModel = listResult.get(0);
			}
		} catch (ModelNotFoundException exception) {

		}

		return fetchItemModel;
	}

	private String buildQuery(String itemModelName, Map<String, JnjCanonicalDTO> canMapData,
			Map<String, String> queryParams, final String strWhereCondition) {
		String query = "SELECT {PK} FROM {" + itemModelName + "} " + STR_WHERE + getParams(canMapData, queryParams);
		if (strWhereCondition != null && !"".equals(strWhereCondition.trim())) {
			query = query + STR_AND + strWhereCondition.trim();
		}
		query = query.endsWith(STR_WHERE) ? query.substring(0, query.length() - STR_WHERE.length()) : query;

		return query;
	}

	public String getParams(final Map<String, JnjCanonicalDTO> canMapData, final Map<String, String> queryParams)
	{
		String strParams = "";
		if (queryParams != null && queryParams.size() > 0)
		{
			final StringBuilder sbParams = new StringBuilder();
			final Set<String> paramKeys = queryParams.keySet();
			if (paramKeys != null)
			{
				for (final String strParam : paramKeys)
				{
					sbParams.append(getParam(canMapData, strParam));
				}
				strParams = sbParams.toString();
				// to remove " AND " at the end
				strParams = strParams.endsWith(STR_AND) ? strParams.substring(0, strParams.length() - STR_AND.length()) : strParams;
			}
		}
		return strParams;
	}
	public String getParam(final Map<String, JnjCanonicalDTO> canMapData, final String strParam)
	{
		final StringBuilder sbParam = new StringBuilder();

		// Composite type
		if (canMapData != null)
		{
			final JnjCanonicalDTO canMapDTO = canMapData.get(strParam);
			if (canMapDTO != null)
			{
				if (canMapDTO.getTransitionType() != null
						&& (canMapDTO.getTransitionType().equalsIgnoreCase(JnjIntJobTransType.COMPOSITE)
								|| canMapDTO.getTransitionType().equalsIgnoreCase(JnjIntJobTransType.ENUM)
								|| canMapDTO.getTransitionType().equalsIgnoreCase(JnjIntJobTransType.CUSTOM)))
				{
					// Ex: select * from {OrderEntry} where {order} = ({{select
					// {PK} from {Order} where {code}=?param}})
					sbParam.append("{").append(canMapDTO.getTargetColumn()).append("} = ({{SELECT {PK} FROM {")
							.append(canMapDTO.getCompositeType()).append("} WHERE {").append(canMapDTO.getCompositeTypeUid())
							.append("} = ?").append(strParam).append("}})").append(STR_AND);
				}
				else
				{
					sbParam.append("{").append(canMapDTO.getTargetColumn()).append("} = ?").append(strParam).append(STR_AND);
				}
			}
		}
		else
		{
			// Non composite type
			// {strParam} = ?strParam AND .......
			sbParam.append("{").append(strParam).append("} = ?").append(strParam).append(STR_AND);
		}

		return sbParam.toString();
	}


	@Override
	public ItemModel fetchExistingOrCreateItemModel(final String itemModelName, final Map<String, String> resultSetMap,
			final Map<String, JnjCanonicalDTO> canMapData, final JnjIntegrationCronJobModel jobModel,
			final List<String> compositePrimaryParams)
	{
		final Map<String, String> queryParams = new HashMap<>();
		ItemModel customItemModel = null;
		HybrisEnumValue primaryKeyOFEnumType = null;
		ItemModel fetchItemModel;
		for (final String primaryKey : compositePrimaryParams)
		{
			//added to handle inner queries ex: OrderEntry
			if (canMapData.get(primaryKey).isPrimaryKeyOfCustomType())
			{
				final JnjCanonicalDTO jnjCanonicalDTO = canMapData.get(primaryKey);
				final Object object = JnJIntegrationTableUtil.getUtilReturnValue(jnjCanonicalDTO.getUtilClassNamePath(),
						jnjCanonicalDTO.getUtilMethodName(), jnjCanonicalDTO.getUtilReturnType(), jnjCanonicalDTO.getUtilMethodParam(),
						jnjCanonicalDTO, resultSetMap);

				if (object instanceof ItemModel)
				{
					customItemModel = (ItemModel) object;
					queryParams.put(primaryKey, customItemModel.getPk().getLongValueAsString());
				}
				else if (object instanceof HybrisEnumValue)
				{
					primaryKeyOFEnumType = (HybrisEnumValue) object;
					queryParams.put(primaryKey, primaryKeyOFEnumType.toString());
				}
			}
			else
			{
				queryParams.put(primaryKey, resultSetMap.get(primaryKey));
			}
			//queryParams.put(primaryKey, resultSetMap.get(primaryKey));
		}
		if (jobModel.getCatalogVersion() != null && Config.getParameter(CRONJOB_NAMES) !=null && Config.getParameter(CRONJOB_NAMES).contains(jobModel.getJobCode()))
		{
			queryParams.put("catalogVersion", jobModel.getCatalogVersion().getPk().getLongValueAsString());
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				buildQuery(itemModelName, canMapData, queryParams, jobModel.getWhereCondition(), jobModel), queryParams);
		if (jobModel.getCatalogVersion() != null)
		{
			fQuery.setCatalogVersions(jobModel.getCatalogVersion());

			//fQuery.append(" AND {CATALOGVERSION}= ?catalogVersion");

		}
		final List<Object> listResult = flexibleSearchService.search(fQuery).getResult();
		if (listResult != null && !listResult.isEmpty())
		{
			// fetch existing model
			fetchItemModel = (ItemModel) listResult.get(0);
		}
		else
		{
			//otherwise create model
			fetchItemModel = modelService.create(itemModelName);
			if (Config.getParameter(CRONJOB_NAMES) !=null && Config.getParameter(CRONJOB_NAMES).contains(jobModel.getJobCode()))
			{
				queryParams.remove("catalogVersion");
			}
			// setting the value
			for (final Map.Entry<String, String> entry : queryParams.entrySet())
			{
				// if Transitiontype value equal to enum
				if (canMapData.get(entry.getKey()).getTransitionType().equalsIgnoreCase(JnjIntJobTransType.ENUM))
				{
					fetchItemModel.setProperty(canMapData.get(entry.getKey()).getTargetColumn(),
							getEnumValue(canMapData.get(entry.getKey()).getCompositeType(), entry.getValue()));
				}
				else if (canMapData.get(entry.getKey()).getTransitionType().equalsIgnoreCase(JnjIntJobTransType.COMPOSITE))
				{
					// if TransitionType value equal to Composite
					saveCompositeValue(entry, canMapData, fetchItemModel, canMapData.get(entry.getKey()).getCompositeType(),
							canMapData.get(entry.getKey()).getCompositeTypeUid(), entry.getValue(), jobModel);
				}
				else if (canMapData.get(entry.getKey()).getTransitionType().equalsIgnoreCase(JnjIntJobTransType.CUSTOM))
				{
					if (customItemModel != null)
					{
						fetchItemModel.setProperty(canMapData.get(entry.getKey()).getTargetColumn(), customItemModel);
					}
					else if (primaryKeyOFEnumType != null)
					{
						fetchItemModel.setProperty(canMapData.get(entry.getKey()).getTargetColumn(), primaryKeyOFEnumType);
					}
				}
				else if (canMapData.get(entry.getKey()).getTransitionType().equalsIgnoreCase(JnjIntJobTransType.BOOLEAN))
				{
					saveBooleanValue(canMapData.get(entry.getKey()), fetchItemModel, entry.getValue());
				}
				else if (canMapData.get(entry.getKey()).getTransitionType().equalsIgnoreCase(JnjIntJobTransType.COLLECTION))
				{
					saveCollectionValues(canMapData.get(entry.getKey()), fetchItemModel, entry.getValue(), jobModel);
				}
				else
				{
					fetchItemModel.setProperty(canMapData.get(entry.getKey()).getTargetColumn(), entry.getValue());
				}
			}
			if (fetchItemModel instanceof ProductModel && jobModel.getCatalogVersion() != null)
			{
				((ProductModel) fetchItemModel).setCatalogVersion(jobModel.getCatalogVersion());
			}
			if (fetchItemModel instanceof CategoryModel && jobModel.getCatalogVersion() != null)
			{
				((CategoryModel) fetchItemModel).setCatalogVersion(jobModel.getCatalogVersion());
			}
		}
		return fetchItemModel;
	}

	@Override
	public HybrisEnumValue getEnumValue(String enumModel, String attributeValue) {
		List<HybrisEnumValue> listEnums = enumerationService.getEnumerationValues(enumModel);
		HybrisEnumValue hybrisEnumValue = null;
		for (HybrisEnumValue val : listEnums) {
			if (val.getCode().equalsIgnoreCase(attributeValue)) {
				hybrisEnumValue = val;
				break;
			}
		}
		return hybrisEnumValue;
	}

	@Override
	public void saveCompositeValue(Map.Entry<String, String> entry, Map<String, JnjCanonicalDTO> canMapData,
			ItemModel ItemModel, String itemModelName, String attributeName, String attributeValue,
			JnjIntegrationCronJobModel jobModel) {
		ItemModel compositeModel = null;
		if (attributeValue != null) {
			compositeModel = fetchCompositeItemModel(itemModelName, attributeName, attributeValue, jobModel);
		}
		if (ItemModel.getPk() != null) {
			if (canMapData.get(entry.getKey()).isWritable()) {
				ItemModel.setProperty(canMapData.get(entry.getKey()).getTargetColumn(), compositeModel);
			}
		} else {
			ItemModel.setProperty(canMapData.get(entry.getKey()).getTargetColumn(), compositeModel);
		}
	}

	@Override
	public void saveCollectionValues(JnjCanonicalDTO jnjCanonicalDTO, ItemModel itemModel, String attributeValue,
			JnjIntegrationCronJobModel jobModel) {

		List<ItemModel> listOfItems = new ArrayList<>();
		List<Object> updatedList = new ArrayList<Object>();
		// If saving collection of compsiteType
		if (jnjCanonicalDTO.getCompositeType() != null) {
			ItemModel collectionItem = fetchCompositeItemModel(jnjCanonicalDTO.getCompositeType(),
					jnjCanonicalDTO.getCompositeTypeUid(), attributeValue, jobModel);
			if (collectionItem != null) {
				listOfItems.add(collectionItem);
				if (CollectionUtils.isNotEmpty(itemModel.getProperty(jnjCanonicalDTO.getTargetColumn()))) {
					// Getting all existing list and updating the UpdateList.
					updatedList.addAll(itemModel.getProperty(jnjCanonicalDTO.getTargetColumn()));
					if (!updatedList.contains(collectionItem)) {
						updatedList.add(collectionItem);
					}
				} else {
					updatedList.addAll(listOfItems);
				}
			}else{
				updatedList.addAll(itemModel.getProperty(jnjCanonicalDTO.getTargetColumn()));
			}
		} else {
			if (CollectionUtils.isNotEmpty(itemModel.getProperty(jnjCanonicalDTO.getTargetColumn()))) {
				updatedList.addAll(itemModel.getProperty(jnjCanonicalDTO.getTargetColumn()));
				if (!updatedList.contains(attributeValue)) {
					// Saving collection of values.
					updatedList.add(attributeValue);
				}
			}
		}
		itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), updatedList);
	}
	
	@Override
	public void saveBooleanValue(JnjCanonicalDTO jnjCanonicalDTO, ItemModel itemModel, String attributeValue) {
		attributeValue = StringUtils.trim(attributeValue);
		if (StringUtils.isNotBlank(attributeValue)) {			
			if (attributeValue.equals("0") || attributeValue.equals("N")) {
				itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), Boolean.FALSE);
			} else {
				itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(), Boolean.TRUE);
			}
		}
	}
	
	@Override
	public Boolean updateStagingRecordStatus(Map<String, String> resultSetMap, List<String> compositePrimaryParams,
			JnjIntegrationCronJobModel jobModel, int newStatus, int existingStatus) {
		boolean recordUpdated = false;
		String query = "";
		try {
			List<String> tableNames = jobModel.getAssociatedStgTables();
			for (final String tableName : tableNames) {
				if (resultSetMap.get(tableName + "_" + SEQUENCEID) != null) {
					query = "UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE SEQUENCEID="
							+ resultSetMap.get(tableName + "_" + SEQUENCEID);
					dBConnector.getJdbcTemplate().update(query);
				}
			}
			recordUpdated = true;
		} finally {
			//
		}
		return recordUpdated;
	}
	
	@Override
	public Boolean updateBulkStagingRecordStatus(JnjIntegrationCronJobModel jobModel, int newStatus, int existingStatus) {
		boolean recordUpdated = false;
		String query = "";
		try {
			List<String> tableNames = jobModel.getAssociatedStgTables();
			for (final String tableName : tableNames) {
					query = "UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE MigrationStatus="
							+ existingStatus;
					dBConnector.getJdbcTemplate().update(query);
			}
			recordUpdated = true;
		} finally {
			//
		}
		return recordUpdated;
	}
	
	@Override
	public Boolean updateBulkStagingRecordStatus(JnjIntegrationCronJobModel jobModel, int newStatus, String existingStatusList) {
		boolean recordUpdated = false;
		String query = "";
		try {
			List<String> tableNames = jobModel.getAssociatedStgTables();
			for (final String tableName : tableNames) {
					query = "UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE MigrationStatus IN (" + existingStatusList + ")";
					dBConnector.getJdbcTemplate().update(query);
			}
			recordUpdated = true;
		} finally {
			//
		}
		return recordUpdated;
	}

	 
	/*public void removeStagingRecord(String orderNumber, JnjIntegrationCronJobModel jobModel)	{
		//boolean recordUpdated = false;
		String query = "";
		try {
				List<String> tableNames = jobModel.getAssociatedStgTables();
				for (final String tableName : tableNames) {
   				query = "DELETE FROM "+ tableName + " WHERE ORDER_NUM ='"+ orderNumber +"'";
   				System.out.println("test point in updateStagingRecordStatus query : "+ query);
   				dBConnector.getJdbcTemplate().update(query);
				}
			} finally {
		}
	}*/

	/**
	 * @return
	 */
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 */
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public void setLastSuccesfulStartTimeForJob(String lastUpdatedate, JnjIntegrationRSACronJobModel arg0) {
		defaultJnjMasterCommonUtil.setLastSuccesfulStartTimeForJob(lastUpdatedate, arg0);

	}

	@Override
	public void saveCustomValue(final JnjCanonicalDTO jnjCanonicalDTO, final ItemModel itemModel,
			final Map<String, String> resultSetMap)
	{
		if (itemModel.getPk() != null)
		{
			if (jnjCanonicalDTO.isWritable())
			{
				itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(),
						JnJIntegrationTableUtil.getUtilReturnValue(jnjCanonicalDTO.getUtilClassNamePath(),
								jnjCanonicalDTO.getUtilMethodName(), jnjCanonicalDTO.getUtilReturnType(),
								jnjCanonicalDTO.getUtilMethodParam(), jnjCanonicalDTO, itemModel, resultSetMap));

			}
		} else {
			itemModel.setProperty(jnjCanonicalDTO.getTargetColumn(),
					JnJIntegrationTableUtil.getUtilReturnValue(jnjCanonicalDTO.getUtilClassNamePath(),
							jnjCanonicalDTO.getUtilMethodName(), jnjCanonicalDTO.getUtilReturnType(),
							jnjCanonicalDTO.getUtilMethodParam(), jnjCanonicalDTO, itemModel, resultSetMap));

		}

	}
	
	public String buildQuery(final String itemModelName, final Map<String, JnjCanonicalDTO> canMapData,
			final Map<String, String> queryParams, final String strWhereCondition, final JnjIntegrationCronJobModel jobModel)
	{
		String query = "SELECT {PK} FROM {" + itemModelName + "} " + STR_WHERE + getParams(canMapData, queryParams);
		if (strWhereCondition != null && !"".equals(strWhereCondition.trim()))
		{
			query = query + STR_AND + strWhereCondition.trim();
		}
		query = query.endsWith(STR_WHERE) ? query.substring(0, query.length() - STR_WHERE.length()) : query;
		if (canMapData != null && canMapData.size() > 0 && Config.getParameter(CRONJOB_NAMES) !=null &&  Config.getParameter(CRONJOB_NAMES).contains(jobModel.getJobCode()))
		{
			query = query + STR_AND + "{CATALOGVERSION}= ?catalogVersion";
		}
		return query;
	}


	


}
