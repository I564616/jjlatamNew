/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.operations;

import java.util.List;

import com.jnj.core.model.BroadcastMessageModel;


/**
 * This class Handles DatBase level interaction For Operation Module
 *
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTOperationsDao
{

	/**
	 * @return
	 */
	public List<BroadcastMessageModel> getBroadCastMessages();

}
