/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.customerEligibility;

import de.hybris.platform.b2b.model.B2BUnitModel;

import java.util.Set;


/**
 * TODO:< class level comments are misssing>.
 * 
 * @author Accenture
 * @version 1.0
 */


public interface JnjCustomerEligiblityDao
{
	/**
	 * Finds restricted categories for a <code>B2BUnitModel</code>.
	 * 
	 * @param b2bUnit
	 * @return Set<String>
	 */
	public Set<String> getRestrictedCategories(B2BUnitModel b2bUnit);

}
