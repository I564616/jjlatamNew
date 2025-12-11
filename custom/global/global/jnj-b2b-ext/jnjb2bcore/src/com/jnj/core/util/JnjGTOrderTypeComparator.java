/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.util;


import java.util.Comparator;
import java.util.List;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants;


/**
 * @author Accenture
 * @version 1.0 Comparator for sorting order types based on custom logic
 */
public class JnjGTOrderTypeComparator implements Comparator<String>
{

	// When multiple order types are available to the user, the default Order Type priority
	List<String> orderTypesPriority = JnJCommonUtil.getValues(Jnjgtb2bMDDConstants.Cart.ORDER_TYPES_PRIORITY,
			Jnjb2bCoreConstants.SYMBOl_COMMA);

	@Override
	public int compare(final String o1, final String o2)
	{
		// If the list contains both order types, compare them
		if (orderTypesPriority.contains(o1) && orderTypesPriority.contains(o2))
		{
			return (orderTypesPriority.indexOf(o1) - orderTypesPriority.indexOf(o2));
		}
		// If the second order type is not present in list, first order should be given priority
		else if (!orderTypesPriority.contains(o2))
		{
			return Integer.MIN_VALUE;
		}
		// If the first order type is not present in list, second order should be given priority
		else
		{
			return Integer.MAX_VALUE;
		}
	}
}
