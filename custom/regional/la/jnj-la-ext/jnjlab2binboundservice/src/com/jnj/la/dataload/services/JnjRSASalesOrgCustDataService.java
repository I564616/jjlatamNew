package com.jnj.la.dataload.services;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import java.util.List;

import com.jnj.la.core.dto.JnjSalesOrgCustDTO;


public interface JnjRSASalesOrgCustDataService
{
	List<JnjSalesOrgCustDTO> pullDataFromRSA(final JnjIntegrationRSACronJobModel cronJobModel);
}
