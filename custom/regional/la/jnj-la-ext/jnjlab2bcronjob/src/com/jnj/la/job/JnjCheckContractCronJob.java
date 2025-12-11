/**
 *
 */
package com.jnj.la.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.facades.contract.JnjContractFacade;


/**
 * The Class JnjCheckContractCronJob.
 */
public class JnjCheckContractCronJob extends AbstractJobPerformable<CronJobModel>
{

	/**  */
	private static final String METHOD_PERFORM = "perform()";

	/**  */
	private static final String CHECK_CONTRACT_JOB = "Check Contract Job";

	/** The jnj contract facade. */
	@Autowired
	private JnjContractFacade jnjContractFacade;

	private static final Logger LOGGER = Logger.getLogger(JnjCheckContractCronJob.class);

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(CHECK_CONTRACT_JOB + Logging.HYPHEN + METHOD_PERFORM + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean result = false;

		try
		{

			result = jnjContractFacade.checkAllActiveContracts();
			result = jnjContractFacade.checkActiveContractsForEntry();
			result = jnjContractFacade.checkAllInActiveContracts();
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(CHECK_CONTRACT_JOB + Logging.HYPHEN + METHOD_PERFORM + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ "BusinessException occred. Aborting job." + businessException);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(
					CHECK_CONTRACT_JOB + Logging.HYPHEN + METHOD_PERFORM + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		if (result)
		{
			LOGGER.info(CHECK_CONTRACT_JOB + Logging.HYPHEN + METHOD_PERFORM + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ "Succesfully Updated the Contracts' status");
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			LOGGER.info(CHECK_CONTRACT_JOB + Logging.HYPHEN + METHOD_PERFORM + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ "No Contracts chnaged from status [Active] to [Inactive]");
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
