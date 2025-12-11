/**
 *
 */
package com.jnj.gt.service.common.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjGTIntermediateMasterModel;
import com.jnj.core.model.JnjReadOperationDashboardModel;
import com.jnj.core.model.JnjWriteOperationDashboardModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.dao.common.JnjGTFeedDao;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * @author akash.rawat
 * 
 */
public class DefaultJnjGTFeedService implements JnjGTFeedService
{
	/**
	 * Constant LOGGER.
	 */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTFeedService.class);
	/*Start AAOL 5074*/
	@Resource(name = "jnjGTFeedDao")
	/*End AAOL 5074*/
	private JnjGTFeedDao interfaceFeedDao;

	@Autowired
	private ModelService modelService;

	/**
	 * Constant for None Message.
	 */
	private static final String NO_ERROR_MESSAGE = "None";

	@Override
	public void invalidateRecords(final Collection<? extends ItemModel> invalidRecords)
	{
		//		final Collection<? extends ItemModel> intermediatedRecords = interfaceFeedDao.fetchIntRecords(intermediateModel,
		//				RecordStatus.LOADING);
		try
		{
			getModelService().removeAll(invalidRecords);
			LOGGER.info("Invalid Records having status LOAIDNG Removed/Deleted SUCCESSFULLy from Intermediary table ");
		}
		catch (final ModelRemovalException e)
		{
			LOGGER.error("Deletion/Removal of records from Intermediary tables, having Record Status as LOADING has cuased an error: "
					+ e.getMessage());
		}
	}


	@Deprecated
	public Boolean updateIntermediateRecord(final JnjGTIntermediateMasterModel intermediateModel, final RecordStatus recordStatus,
			final boolean incrementWriteAttempt, final String errorMessage)
	{
		//LOG

		final boolean updateStatus = false;

		if (incrementWriteAttempt)
		{

			final Integer updatedWriteAttempt = (intermediateModel.getWriteAttempts() == null) ? Integer.valueOf(1) : Integer
					.valueOf(intermediateModel.getWriteAttempts().intValue() + 1);
			intermediateModel.setWriteAttempts(updatedWriteAttempt);

			if (updatedWriteAttempt.equals(Integer.valueOf(Config.getInt(Jnjb2bCoreConstants.FEED_RETRY_COUNT, 3))))
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("updateIntermediateRecord()"
							+ Logging.HYPHEN
							+ "Record Retry Status has achieved maximum limit, no further retry possible. Setting the Record Staus to 'Error'. ");
				}

				intermediateModel.setRecordStatus(RecordStatus.ERROR);
				//updateIntermediateRecord(jnjGTIntB2BUnitModel, RecordStatus.ERROR, false);

				intermediateModel.setSentToDashboard(Boolean.TRUE);

			}
		}
		else
		{
			intermediateModel.setRecordStatus(recordStatus);
		}
		saveItem(intermediateModel);
		//logMethodStartOrEnd(Logging.UPSERT_CUSTOMER_NAME, METHOD_NAME, Logging.END_OF_METHOD);

		return Boolean.valueOf(updateStatus);
	}

	@Override
	public Boolean updateIntermediateRecord(final JnjGTIntermediateMasterModel intermediateModel, final RecordStatus recordStatus,
			final boolean incrementWriteAttempt, final String errorMessage, final String feedName, final String idocNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("updateIntermediateRecord()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean updateStatus = false;

		if (incrementWriteAttempt)
		{
			// This if block is added to error out the product exclusion records directly without increment the write attempts count and also
			// it updates the entry in write dash board.
			if (RecordStatus.ERROR.equals(recordStatus))
			{
				intermediateModel.setRecordStatus(RecordStatus.ERROR);
				intermediateModel.setSentToDashboard(Boolean.TRUE);
				// make entry in write dash board.
				updateWriteDashboard(feedName, idocNumber, errorMessage);
			}
			else
			{
				final Integer updatedWriteAttempt = (intermediateModel.getWriteAttempts() == null) ? Integer.valueOf(1) : Integer
						.valueOf(intermediateModel.getWriteAttempts().intValue() + 1);
				intermediateModel.setWriteAttempts(updatedWriteAttempt);

				if (updatedWriteAttempt.equals(Integer.valueOf(Config.getInt(Jnjb2bCoreConstants.FEED_RETRY_COUNT, 3))))
				{
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug("updateIntermediateRecord()"
								+ Logging.HYPHEN
								+ "Record Retry Status has achieved maximum limit, no further retry possible. Setting the Record Staus to 'Error'. ");
					}

					intermediateModel.setRecordStatus(RecordStatus.ERROR);
					intermediateModel.setSentToDashboard(Boolean.TRUE);
					// make entry in write dash board.
					updateWriteDashboard(feedName, idocNumber, errorMessage);
				}
			}
		}
		else
		{
			intermediateModel.setRecordStatus(recordStatus);
		}
		updateStatus = saveItem(intermediateModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("updateIntermediateRecord()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return Boolean.valueOf(updateStatus);
	}

	private boolean saveItem(final JnjGTIntermediateMasterModel item)
	{
		try
		{
			getModelService().save(item);
			return true;
		}
		catch (final ModelSavingException e)
		{
			return false;
		}
	}


	@Override
	public Boolean updateIntRecordStatus(final String intermediateModel)
	{
		System.out.println("jnjGTFeedServiceImpl updateIntRecordStatus() : START");
		final Collection<? extends JnjGTIntermediateMasterModel> intermediatedRecords = interfaceFeedDao.fetchIntRecords(
				intermediateModel, RecordStatus.LOADING);

		for (final JnjGTIntermediateMasterModel intermediaryModel : intermediatedRecords)
		{
			intermediaryModel.setRecordStatus(RecordStatus.PENDING);
		}

		try
		{
			getModelService().saveAll(intermediatedRecords);
		}
		catch (final ModelSavingException e)
		{
			//			LOGGER.error(Logging.LOAD_CUSTOMER_ELIGIBILITY_INTERFACE
			//					+ "Resetting Record Sattus from 'LOADING' to 'PENDING' has caused an exception" + e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean updateReadDashboard(final String feedName, final String stageTimeStamp)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateReadDashboard(final String filePath, final String stagePk, final boolean isSuccess,
			final String errorOccured, final String interfaceName)

	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("updateReadDashboard()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final JnjReadOperationDashboardModel newReadDashboardEntry = modelService.create(JnjReadOperationDashboardModel.class);
		newReadDashboardEntry.setInterfaceName(interfaceName);
		newReadDashboardEntry.setFileName(filePath);
		newReadDashboardEntry.setStatus(isSuccess ? RecordStatus.SUCCESS.toString() : RecordStatus.ERROR.toString());
		newReadDashboardEntry.setErrorMessage(isSuccess ? NO_ERROR_MESSAGE : errorOccured);
		newReadDashboardEntry.setDate(new Date());
		newReadDashboardEntry.setEmailNotificationSent(Boolean.FALSE);
		saveDashboard(newReadDashboardEntry);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("updateReadDashboard()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		return null;
	}

	@Override
	public Collection<? extends JnjGTIntermediateMasterModel> getRecordsByStatus(final String intermediateModel,
			final RecordStatus status)
	{
		return interfaceFeedDao.fetchIntRecords(intermediateModel, status);
	}

	@Override
	public Collection<? extends JnjGTIntermediateMasterModel> getRecordsByStatus(final String intermediateModel,
			final RecordStatus status, final Date selectionDate)
	{
		return interfaceFeedDao.fetchIntRecords(intermediateModel, status, selectionDate);
	}

	@Override
	public Collection<? extends JnjGTIntermediateMasterModel> getRecordsByStatusAndSourceSys(final String intermediateModel,
			final RecordStatus status, final List<String> sourceSysIds)
	{
		return interfaceFeedDao.fetchIntRecords(intermediateModel, status, sourceSysIds);
	}

	@Override
	public Collection<? extends JnjGTIntermediateMasterModel> getRecordsByStatusAndSourceSys(final String intermediateModel,
			final RecordStatus status, final List<String> sourceSysIds, final Date selectionDate)
	{
		return interfaceFeedDao.fetchIntRecords(intermediateModel, status, sourceSysIds, selectionDate);
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
		if (LOGGER.isDebugEnabled())
		{
			//			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
			//					+ System.currentTimeMillis());
		}
	}



	public JnjGTFeedDao getInterfaceFeedDao()
	{
		return interfaceFeedDao;
	}

	public void setInterfaceFeedDao(final JnjGTFeedDao interfaceFeedDao)
	{
		this.interfaceFeedDao = interfaceFeedDao;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateWriteDashboard(final String interfaceName, final String idocNumber, final String errorMessage)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("updateWriteDashboard()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final JnjWriteOperationDashboardModel newWriteDashboardEntry = modelService.create(JnjWriteOperationDashboardModel.class);
		newWriteDashboardEntry.setInterfaceName(interfaceName);
		newWriteDashboardEntry.setFileName(interfaceName);
		newWriteDashboardEntry.setIdocNumber(idocNumber);
		newWriteDashboardEntry.setDate(new Date());
		newWriteDashboardEntry.setErrorMessage(errorMessage);
		newWriteDashboardEntry.setEmailNotificationSent(Boolean.FALSE);
		final boolean dashboardUpdated = saveDashboard(newWriteDashboardEntry);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("updateWriteDashboard()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		return dashboardUpdated;
	}


	/**
	 * Save dashboard.
	 * 
	 * @param dashboard
	 *           the dashboard
	 * @return true, if successful
	 */
	private boolean saveDashboard(final ItemModel dashboard)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveDashboard()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			modelService.save(dashboard);
		}
		catch (final ModelSavingException e)
		{
			if (dashboard instanceof JnjReadOperationDashboardModel)
			{
				LOGGER.error("Saving READ OPERATION DASHBOARD failed. Error Message: " + e.getMessage());
			}
			else
			{
				LOGGER.error("Saving WRITE OPERATION DASHBOARD failed. Error Message: " + e.getMessage());
			}
			return true;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveDashboard()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return true;
	}

}
