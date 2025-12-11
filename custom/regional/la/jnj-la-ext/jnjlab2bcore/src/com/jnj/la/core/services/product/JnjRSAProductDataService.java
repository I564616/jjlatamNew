package com.jnj.la.core.services.product;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import java.util.Date;
import java.util.List;

import com.jnj.la.core.dto.JnJLaProductDTO;


public interface JnjRSAProductDataService
{
	List<JnJLaProductDTO> pullProductsFromRSA(final Date lowerDate, final Date upperDate, final JnjIntegrationRSACronJobModel cronJob, final  String sector);

    List<String> getUniqueDivisionsFromRSA(final Date lowerDate, final Date upperDate, final String sector);

    Date getLastUpdatedDateForLatestRecord();
}
