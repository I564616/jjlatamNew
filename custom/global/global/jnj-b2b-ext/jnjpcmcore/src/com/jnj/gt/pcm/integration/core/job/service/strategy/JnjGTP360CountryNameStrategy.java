/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.core.job.service.strategy;

import com.jnj.core.model.JnJProductModel;

public interface JnjGTP360CountryNameStrategy {
	
	/**
	 * This method is used to fetch the country of the product for the push job.
	 * 
	 * @param productModel Used for fetching the country
	 * @return StringBuilder Country ISO code is returned
	 */
	public StringBuilder getCountriesName(final JnJProductModel productModel);

}
