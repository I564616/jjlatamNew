package com.jnj.core.dao.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author  
 *
 */
public class JnjMasterFeedRowMapper implements RowMapper<List<LinkedHashMap<String,String>>>
{

	@Override
	public List<LinkedHashMap<String,String>> mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        List<String> columnsList = getColumns(resultSet);
        List<LinkedHashMap<String,String>> dataList = new ArrayList<LinkedHashMap<String,String>>();
        LinkedHashMap<String,String> dataMap = null;
        do
        {
             // For each record
        	dataMap = new LinkedHashMap<String,String>();
             
             for (String column: columnsList) {
                  // For each column, columnName -> actualValue
                  dataMap.put(column, resultSet.getString(column));
             }
             dataList.add(dataMap);
             
        }
        while (resultSet.next());
        
        return dataList;

	}
	
    private List<String> getColumns (ResultSet rs){
    	List<String> listColumns = new ArrayList<String>();
    	try
    	{
	        ResultSetMetaData metaData = rs.getMetaData();
	        for (int i = 1; i <= metaData.getColumnCount(); i++)
	        {
	             listColumns.add(metaData.getColumnName(i));
	        }
    	}
    	catch(Exception ex)
    	{
    		
    	}
        return listColumns;
  }


}
