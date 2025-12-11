/**
 * 
 */
package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.territory.JnjGTTerritoryService;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTSalesAlignmentRemoval extends AbstractJobPerformable<CronJobModel>
{

	@Autowired
	private JnjGTTerritoryService jnjGTTerritoryService;

	//This Job  will reomve the invalid TerriTory Divison and  TerriTory Divsion  Cust
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{

		//Invalidating all the Territory Divison Model which doesnt fall in valid dates
		jnjGTTerritoryService.removeInvaildTerritoryDivisonModel();
		//Invalidating all the Territory Divison Cust Model which doesnt fall in valid dates
		jnjGTTerritoryService.removeInvaildTerritoryDivisonCustModel();
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

}
