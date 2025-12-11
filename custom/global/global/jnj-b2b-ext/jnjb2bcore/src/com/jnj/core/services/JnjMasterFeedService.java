package com.jnj.core.services;


import com.jnj.core.model.JnjIntegrationCronJobModel;


public interface JnjMasterFeedService {

	public boolean loadData(final JnjIntegrationCronJobModel jobModel);
}
