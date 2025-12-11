package com.jnj.core.order.job;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjOrderLineItemCancelEmailTiggerCronJobModel;
import com.jnj.core.services.JnjGTOrderService;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class JnjGTCancelOrderLineItemEmailTiggerJobPerformable  extends AbstractJobPerformable<JnjOrderLineItemCancelEmailTiggerCronJobModel>{

	@Autowired 
	JnjGTOrderService jnjGTOrderService;
	
	@Override
	public PerformResult perform(JnjOrderLineItemCancelEmailTiggerCronJobModel jobModel) {
		
		//1. Collect All OrderEntry with status cancel
		//2. Crate Map with key User and value orderEntry
		//3. Tigger cancel email
		//1.
		System.out.println("Start JnjGTCancelOrderLineItemEmailTiggerJobPerformable");
		Map<JnJB2bCustomerModel, List<OrderEntryModel>> map= jnjGTOrderService.getCancelOrderLineItems(jobModel);
		for(Map.Entry<JnJB2bCustomerModel, List<OrderEntryModel>> entry : map.entrySet())
		{
			//3
			sendCancelOrderLineStatusEmail(entry.getKey(),entry.getValue());
		}
		System.out.println("JnjGTCancelOrderLineItemEmailTiggerJobPerformable end");

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	public void sendCancelOrderLineStatusEmail(JnJB2bCustomerModel jnJB2bCustomerModel,List<OrderEntryModel> orderEntries)
	{
		jnjGTOrderService.sendCancelOrderLineStatusEmail(jnJB2bCustomerModel, orderEntries);
	}
}
