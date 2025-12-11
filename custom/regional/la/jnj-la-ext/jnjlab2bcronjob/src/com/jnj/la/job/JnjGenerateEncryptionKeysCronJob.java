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

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.util.JnjEncryptionUtil;


/**
 * This is the Service Layer for JnjGenerateEncryptionKeysCronJob. The cronJob generates the Private and the Public Keys
 * used in PGP encryption. The parameters required for this cronJob to be success are configurable and are as follows.
 * encryption.keys.path; encryption.privateKey.name;encryption.publicKey.name; encryption.password;
 * encryption.bitsValue; encryption.armored;
 *
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGenerateEncryptionKeysCronJob extends AbstractJobPerformable<CronJobModel>
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjGenerateEncryptionKeysCronJob.class);

	/**
	 * Perform method of the CronJob.
	 *
	 * @param cronJobModel
	 *           the cron job model
	 * @return the perform result
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		final String METHOD_NAME = "perform()";
		boolean jobResult = false;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			jobResult = JnjEncryptionUtil.generatePGPKeyPairs();
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Exception occured while executing JnjGenerateEncryptionKeysCronJob" + Logging.HYPHEN, businessException);
		}
		catch (final Exception exception)
		{
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Exception occured while executing JnjGenerateEncryptionKeysCronJob" + Logging.HYPHEN, exception);
		}
		if (jobResult)
		{
			LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Job Successfully Executed.");
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
						+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Job Failed.");
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
						+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			}
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
