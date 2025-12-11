/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.facade;

import com.jnj.gt.core.model.JnjGTPCMIntegrationCronJobModel;


/**
 * This interface contains implementation of pulling Product rich content data from P360 and updating in Hybris DB
 *
 */
public interface JnjGTProductContentFacade
{

	/**
	 * This method is used to consume ProducData from Global PCM and populate to Hybris DB
	 *
	 * @param jobModel
	 *           its the cronjob model which contains basic job configuration
	 * @return true/false
	 */
	public boolean getProductData(final JnjGTPCMIntegrationCronJobModel jobModel);



}