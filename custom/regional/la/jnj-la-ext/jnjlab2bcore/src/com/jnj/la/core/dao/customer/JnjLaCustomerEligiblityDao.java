/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.dao.customer;

import java.util.Set;

import com.jnj.core.dao.customerEligibility.JnjCustomerEligiblityDao;
import com.jnj.core.dto.JnjCustomerEligiblityDTO;


/**
 * @author Accenture
 * @version 1.0
 */


public interface JnjLaCustomerEligiblityDao extends JnjCustomerEligiblityDao
{
	public Set<JnjCustomerEligiblityDTO> getCustomerEligibilityRecords(String customerEligibilityQuery);
}
