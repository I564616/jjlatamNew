/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjOrderTypesEnum;


/**
 * @author Accenture
 * @version 1.0
 */
public class CartViewMapping
{
	public static final Map<String, String> cmsSiteCartDirMap = new HashMap<String, String>();
	public static final Map<String, String> orderTypeCartDirMap = new HashMap<String, String>();
	public static final String CartPageBaseDirPath = "pages/cart";
	static
	{
		cmsSiteCartDirMap.put(Jnjb2bCoreConstants.MDD, "/MDD");
		cmsSiteCartDirMap.put(Jnjb2bCoreConstants.CONS, "/CONS");

		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZOR.getCode(), "/standard");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZORD.getCode(), "/standard");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZINS.getCode(), "/standard");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZIND.getCode(), "/standard");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZDEL.getCode(), "/delivered");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZQT.getCode(), "/quote");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZEX.getCode(), "/international");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZNC.getCode(), "/noCharge");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZRE.getCode(), "/return");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZKB.getCode(), "/replenish");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.KA.getCode(), "/consignmentReturn");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.KB.getCode(), "/consignmentFillUp");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.KE.getCode(), "/consignmentCharge");

		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZIO2.getCode(), "/house");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZTOR.getCode(), "/house");
		orderTypeCartDirMap.put(JnjOrderTypesEnum.ZHOR.getCode(), "/house");
	}
}