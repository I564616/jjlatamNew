/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.services.customer;

import java.util.Set;

import com.jnj.core.dto.JnjCustomerEligiblityDTO;
import com.jnj.core.services.customerEligibility.JnjCustomerEligibilityService;

/**
 * @author mpanda3
 * @version 1.0
 */
public interface JnjLaCustomerEligibilityService extends JnjCustomerEligibilityService
{
	public Set<JnjCustomerEligiblityDTO> getCustomerEligibilityRecords(final String customerEligibilityQuery);
}
