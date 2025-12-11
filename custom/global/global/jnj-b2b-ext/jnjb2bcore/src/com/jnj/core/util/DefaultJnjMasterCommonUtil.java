package com.jnj.core.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.jnj.core.connector.JNJDBConnector;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjIntegrationCronJobModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.services.impl.DefaultJnjMasterFeedService;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

public class DefaultJnjMasterCommonUtil {
	
	//protected static final String RSA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	protected static final String RSA_DATE_FORMAT = "jnj.rsa.cronjob.data.format";
	private static final String SEQUENCEID = "SEQ";
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjMasterCommonUtil.class);


	/**
	 * Instance of <code>ModelService</code>
	 */
	@Autowired
	private ModelService modelService;
	@Autowired
	JNJDBConnector dBConnector;
	
	public String getLastSuccesfulStartTimeForJob(final JnjIntegrationRSACronJobModel arg0)
	{
		Date returnDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(Config.getParameter(RSA_DATE_FORMAT));
		String retString= null;
		try{
			if(arg0.getLastSuccessFulRecordProcessTime() == null)
			{
				return retString;
			}
			returnDate = arg0.getLastSuccessFulRecordProcessTime();
			retString = sdf.format(returnDate);
		}catch (Exception e)
		{
			
		}
		return retString;
	}
	
	
	public void setLastSuccesfulStartTimeForJob(final String lastUpdatedate,final JnjIntegrationRSACronJobModel arg0)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Config.getParameter(RSA_DATE_FORMAT));
		
		try{
				arg0.setLastSuccessFulRecordProcessTime(sdf.parse(lastUpdatedate));
				modelService.save(arg0);
				//modelService.saveAll();
				//modelService.setAttributeValue(arg0, "lastSuccessFulRecordProcessTime", sdf.parse(lastUpdatedate));
				//modelService.save(arg0);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void stagingTableStatusUpdate(List <Map<String, String>> resultSetMap,JnjIntegrationCronJobModel jobModel, int newStatus, int existingStatus){
		
		String query = "";
		List<Object[]> inputList = new ArrayList<Object[]>(); 
		try {
			List<String> tableNames = jobModel.getAssociatedStgTables();
			for (final String tableName : tableNames) {
				for(int i =0;i<resultSetMap.size();i++){
					Object[] tmp = {Integer.parseInt(resultSetMap.get(i).get(tableName + "_" + SEQUENCEID))};
					// Adding sequence id's to the List
					inputList.add(tmp);
						//inputList.add(resultSetMap.get(i).get(tableName + "_" + SEQUENCEID));
					 if(newStatus==9){
					 LOGGER.info("#### Processing of record failed bearing Sequence ID : "+inputList.get(i)+" of "+tableName);
					 }
				}
				
			
				
				if (inputList != null) {
					query = "UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE SEQUENCEID=? ";
					int[] result=	dBConnector.getJdbcTemplate().batchUpdate(query,inputList);	
			
					LOGGER.info("#### Update performed for  : "+tableName+" of count "+result.length);
				}
			}
	
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		finally {
			//
		}
	}
}
