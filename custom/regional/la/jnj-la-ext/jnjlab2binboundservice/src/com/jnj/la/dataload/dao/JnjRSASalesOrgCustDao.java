package com.jnj.la.dataload.dao;

import com.jnj.core.jalo.JnjIntegrationRSACronJob;

import java.util.List;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.la.core.dto.JnjSalesOrgCustDTO;

public interface JnjRSASalesOrgCustDao
{
	List<JnjSalesOrgCustDTO> getSalesOrgDetails(final JnjIntegrationRSACronJobModel cronJobModel);
}
