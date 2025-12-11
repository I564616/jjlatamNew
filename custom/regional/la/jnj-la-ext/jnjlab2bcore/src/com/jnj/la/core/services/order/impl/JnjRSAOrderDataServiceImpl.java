package com.jnj.la.core.services.order.impl;

import java.util.List;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dto.order.JnjOrderDTO;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dao.order.JnjRSAOrderDao;
import com.jnj.la.core.dao.order.JnjRSAOrderDataService;


public class JnjRSAOrderDataServiceImpl implements JnjRSAOrderDataService
{

	@Autowired
	protected JnjRSAOrderDao jnjRSAOrderDao;

	@Override
	public List<JnjOrderDTO> pullOrdersFromRSA(final JnjIntegrationRSACronJobModel cronJob)
	{

		final String METHOD_NAME = "pullOrdersFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				JnjRSAOrderDataServiceImpl.class);

		final List<JnjOrderDTO> jnjOrders = jnjRSAOrderDao.getOrders(cronJob);

		if (jnjOrders == null || jnjOrders.isEmpty())
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, "No data pulled from RSA order view",
					JnjRSAOrderDataServiceImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.END_OF_METHOD,
				JnjRSAOrderDataServiceImpl.class);
		return jnjOrders;
	}

}
