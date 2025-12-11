package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.core.model.JnjInterfaceStageCleanupCronJobModel;


/**
 * The Class JnjGTFeedJob.
 */
public class JnjGTFeedStageCleanupJob extends AbstractJobPerformable<JnjInterfaceStageCleanupCronJobModel>
{


	private final int REC_STATUS_LOADING = 0;
	private final int REC_STATUS_PENDING = 1;
	private final int REC_STATUS_PROCESSED = 2;

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(JnjGTFeedStageCleanupJob.class);



	/** The int facade map. */
	@Autowired
	Map<String, ArrayList<String>> jnjGTFeedStageCleanupMap;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final JnjInterfaceStageCleanupCronJobModel interfaceCronJob)
	{
		LOG.debug("Strart Inside perform JnjInterfaceStageCleanupCronJobModel Job");

		Date ignoredDate = null;
		Date loadingDate = null;
		final Integer ignoredHours = interfaceCronJob.getPROCESSED_IGNORED_RETENTION();
		final Integer loadingHours = interfaceCronJob.getPROCESSED_LOADING_RETENTION();
		final Boolean preparedStatement = interfaceCronJob.getUSE_PREPARED_STATEMENTS();

		if (ignoredHours == null || loadingHours == null)
		{
			LOG.error("JnjInterfaceStageCleanupCronJobModel is misconfigured, please set retention hours");
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

		final Calendar ignoredCal = Calendar.getInstance();
		ignoredCal.add(Calendar.HOUR_OF_DAY, ignoredHours.intValue() * -1);
		ignoredDate = ignoredCal.getTime();

		final Calendar loadingCal = Calendar.getInstance();
		loadingCal.add(Calendar.HOUR_OF_DAY, loadingHours.intValue() * -1);
		loadingDate = loadingCal.getTime();

		//Look @ 1000, check migrationstatus and insertiontimestamp

		try
		{
			for (final Entry<String, ArrayList<String>> entry : jnjGTFeedStageCleanupMap.entrySet())
			{
				LOG.debug("Cleaning up stage tables for: " + entry.getKey());
				ArrayList<String> cleanupConfig = null;

				cleanupConfig = entry.getValue();

				if (preparedStatement != null && preparedStatement.booleanValue())
				{
					cleanTable(entry.getKey());
				}
				else
				{
					for (final String stageTable : cleanupConfig)
					{
						LOG.debug("Cleaning: " + stageTable);
						cleanTable(stageTable, REC_STATUS_LOADING, loadingDate);
						cleanTable(stageTable, REC_STATUS_PROCESSED, loadingDate);

						cleanTable(stageTable, REC_STATUS_PENDING, ignoredDate);
						cleanTable(stageTable, REC_STATUS_PROCESSED, ignoredDate);
					}
				}
			}
		}
		catch (final ClassCastException e)
		{
			LOG.debug("Failed to cast");
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected boolean cleanTable(final String tableName, final int migrationStatus, final Date retentionDate)
	{
		boolean retVal = true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try
		{
			final String formattedDate = new SimpleDateFormat(jnjCommonUtil.getDBDateFormat()).format(retentionDate);
			Class.forName(Config.getString("inbound.feed.db.driver", ""));
			conn = DriverManager.getConnection(Config.getString("inbound.feed.db.url", ""),
					Config.getString("inbound.feed.db.username", ""), Config.getString("inbound.feed.db.password", ""));

			pstmt.setString(1, Integer.toString(migrationStatus));
			pstmt.setString(2, formattedDate);

			countStmt.setString(1, Integer.toString(migrationStatus));
			countStmt.setString(2, formattedDate);

			if (Config.isOracleUsed()) {
				pstmt = conn.prepareStatement("delete from " + tableName
					+ " O WHERE O.migrationstatus=? and o.INSERTIONTIMESTAMP <= to_timestamp(?,'yyyy-mm-dd') and  rownum<=10000");
			} else if(Config.isHanaUsed()) {
				pstmt = conn.prepareStatement("delete from " + tableName
					+ " O WHERE O.migrationstatus=? and o.INSERTIONTIMESTAMP <= to_timestamp(?,'yyyy-mm-dd') and  ROW_NUMBER()<=10000");
			}

			rs = countStmt.executeQuery();
			rs.next();
			final int count = rs.getInt("total");

			for (int i = 0; i < count; i += 10000)
			{
				rs = pstmt.executeQuery();
			}
		}
		catch (final SQLException e)
		{
			retVal = false;
			LOG.error("Error cleaning table: " + tableName);
		}
		catch (final ClassNotFoundException e)
		{
			retVal = false;
			LOG.error("Error cleaning table: " + tableName);
		}
		finally
		{
			Utilities.tryToCloseJDBC(conn, pstmt, rs);
		}
		return retVal;

	}

	protected boolean cleanTable(final String procedure)
	{
		boolean retVal = true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			Class.forName(Config.getString("inbound.feed.db.driver", ""));
			conn = DriverManager.getConnection(Config.getString("inbound.feed.db.url", ""),
					Config.getString("inbound.feed.db.username", ""), Config.getString("inbound.feed.db.password", ""));

			pstmt = conn.prepareStatement("execute " + procedure);

			rs = pstmt.executeQuery();
		}
		catch (final SQLException e)
		{
			retVal = false;
			LOG.error("Error cleaning table with procedure: " + procedure);
		}
		catch (final ClassNotFoundException e)
		{
			retVal = false;
			LOG.error("Error cleaning table: " + procedure);
		}
		finally
		{
			Utilities.tryToCloseJDBC(conn, pstmt, rs);
		}
		return retVal;

	}

}