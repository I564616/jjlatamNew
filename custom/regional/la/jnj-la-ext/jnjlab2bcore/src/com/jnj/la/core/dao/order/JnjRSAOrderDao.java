package com.jnj.la.core.dao.order;

import java.util.List;

import com.jnj.core.dto.order.JnjOrderDTO;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

public interface JnjRSAOrderDao {
    List<JnjOrderDTO> getOrders(JnjIntegrationRSACronJobModel cronJob);

}
