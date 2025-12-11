/**
 * 
 */
package com.jnj.gt.dao.common;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjGTIntermediateMasterModel;


/**
 * @author akash.rawat
 * 
 */
public interface JnjGTFeedDao
{
	Collection<? extends JnjGTIntermediateMasterModel> fetchIntRecords(final String intermediateModel,
			final RecordStatus recordStatus);

	Collection<? extends JnjGTIntermediateMasterModel> fetchIntRecords(final String intermediateModel,
			final RecordStatus recordStatus, Date selectionDate);

	/**
	 * To be used ONLY for Sync Order Intermediate records since source system attribute name differs in other
	 * intermediate table and so does the query.
	 * 
	 * @param intermediateModel
	 * @param recordStatus
	 * @param sourceSysId
	 * @return
	 */
	Collection<? extends JnjGTIntermediateMasterModel> fetchIntRecords(final String intermediateModel,
			final RecordStatus recordStatus, List<String> sourceSysId);

	/**
	 * The added date parameter is used by the Feed Cleanup jobs. If null is sent, this date will be ignored.
	 * 
	 * @param intermediateModel
	 * @param recordStatus
	 * @param sourceSysId
	 * @param selectionDate
	 * @return
	 */
	Collection<? extends JnjGTIntermediateMasterModel> fetchIntRecords(final String intermediateModel,
			final RecordStatus recordStatus, List<String> sourceSysId, Date selectionDate);
}
