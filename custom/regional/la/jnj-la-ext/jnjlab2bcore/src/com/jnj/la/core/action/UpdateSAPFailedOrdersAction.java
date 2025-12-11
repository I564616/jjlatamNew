package com.jnj.la.core.action;

import java.util.List;

import com.jnj.la.core.model.JnjLaSAPFailedOrdersReportEmailProcessModel;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;

/**
 * Action to update the sap failed order email report sent flag
 *
 */
public class UpdateSAPFailedOrdersAction extends AbstractProceduralAction<JnjLaSAPFailedOrdersReportEmailProcessModel> {

	private ModelService modelService;

	@Override
	public void executeAction(
			final JnjLaSAPFailedOrdersReportEmailProcessModel jnjLaSAPFailedOrdersReportEmailProcessModel) {

		List<OrderModel> sapFailedOrders = jnjLaSAPFailedOrdersReportEmailProcessModel.getSapFailedOrders();

		sapFailedOrders.stream()
				.forEach(sapFailedOrder -> sapFailedOrder.setSapFailedOrderReportEmailSent(Boolean.TRUE));

		modelService.saveAll(sapFailedOrders);
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

}