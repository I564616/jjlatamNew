/**
 *
 */
package com.jnj.facades.cronjobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.category.JnjCategoryFacade;
import com.jnj.la.core.enums.JnjTargetCatalogs;
import com.jnj.la.core.model.JnjCronJobModel;


/**
 * The Class JnjHideCategoryCronJob.
 *
 */
public class JnjHideCategoryCronJob extends AbstractJobPerformable<JnjCronJobModel>
{

	/** The Constant JNJ_HIDE_CATEGORY_CRON_JOB. */
	private static final String JNJ_HIDE_CATEGORY_CRON_JOB = "JnjHideCategoryCronJob";

	/** The jnj category facade. */
	@Autowired
	private JnjCategoryFacade jnjCategoryFacade;


	@Override
	public PerformResult perform(final JnjCronJobModel jnjCronJobModel)
	{
		final String methodName = "perform";
		JnjGTCoreUtil.logDebugMessage(JNJ_HIDE_CATEGORY_CRON_JOB, methodName, Logging.BEGIN_OF_METHOD,
				JnjHideCategoryCronJob.class);
		boolean cronJobStatus = false;
		final JnjTargetCatalogs jnjTargetCatalogs = jnjCronJobModel.getTargetCatalog();
		if (jnjTargetCatalogs != null)
		{
			cronJobStatus = jnjCategoryFacade.hideCategoryWithNoProducts(jnjTargetCatalogs);
		}

		JnjGTCoreUtil.logDebugMessage(JNJ_HIDE_CATEGORY_CRON_JOB, methodName, Logging.END_OF_METHOD, JnjHideCategoryCronJob.class);
		if (cronJobStatus)
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
