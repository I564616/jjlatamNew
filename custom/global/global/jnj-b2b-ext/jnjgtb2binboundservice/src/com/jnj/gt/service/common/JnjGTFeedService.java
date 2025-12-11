/**
 * 
 */
package com.jnj.gt.service.common;

import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjGTIntermediateMasterModel;


/**
 * @author akash.rawat
 * 
 */
public interface JnjGTFeedService
{
	public void invalidateRecords(final Collection<? extends ItemModel> invalidRecords);

	public Boolean updateIntRecordStatus(final String intermediateModel);

	public Boolean updateIntermediateRecord(final JnjGTIntermediateMasterModel intermediateModel, final RecordStatus status,
			boolean incrementWriteAttempt, final String errorMessage);

	public Boolean updateReadDashboard(final String feedName, final String stageTimeStamp);

	public Boolean updateReadDashboard(final String feedName, final String stagePk, final boolean isSuccess,
			final String errorOccured, final String interfaceName);

	public Collection<? extends JnjGTIntermediateMasterModel> getRecordsByStatus(final String intermediateModel,
			final RecordStatus status);

	public Collection<? extends JnjGTIntermediateMasterModel> getRecordsByStatus(final String intermediateModel,
			final RecordStatus status, Date selectionDate);

	public boolean updateWriteDashboard(final String interfaceName, final String idocNumber, final String errorMessage);

	public Boolean updateIntermediateRecord(final JnjGTIntermediateMasterModel intermediateModel, final RecordStatus recordStatus,
			final boolean incrementWriteAttempt, final String errorMessage, final String feedName, final String idocNumber);

	public Collection<? extends JnjGTIntermediateMasterModel> getRecordsByStatusAndSourceSys(final String intermediateModel,
			final RecordStatus status, List<String> sourceSysIds);

	public Collection<? extends JnjGTIntermediateMasterModel> getRecordsByStatusAndSourceSys(final String intermediateModel,
			final RecordStatus status, List<String> sourceSysIds, Date selectionDate);

}
