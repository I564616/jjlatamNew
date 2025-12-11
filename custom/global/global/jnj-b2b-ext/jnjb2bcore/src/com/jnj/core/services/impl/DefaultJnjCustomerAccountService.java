/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.services.impl;

import de.hybris.platform.b2bacceleratorservices.customer.impl.DefaultB2BCustomerAccountService;
import de.hybris.platform.b2bacceleratorservices.dao.B2BAcceleratorCartToOrderCronJobModelDao;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class DefaultJnjCustomerAccountService extends DefaultB2BCustomerAccountService
{
	protected B2BAcceleratorCartToOrderCronJobModelDao b2bAcceleratorCartToOrderCronJobModelDao;

	@Override
	protected B2BAcceleratorCartToOrderCronJobModelDao getB2bAcceleratorCartToOrderCronJobModelDao()
	{
		return b2bAcceleratorCartToOrderCronJobModelDao;
	}

	@Override
	public void setB2bAcceleratorCartToOrderCronJobModelDao(
			final B2BAcceleratorCartToOrderCronJobModelDao b2bAcceleratorCartToOrderCronJobModelDao)
	{
		this.b2bAcceleratorCartToOrderCronJobModelDao = b2bAcceleratorCartToOrderCronJobModelDao;
	}
}
