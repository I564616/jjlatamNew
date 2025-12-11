/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.la.pac.aera.dao;

import java.util.List;
import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.pac.aera.job.dao.JnjPacAeraDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

public interface JnjLaPacAeraDao extends JnjPacAeraDao {

	/**
	 * This method is used to get OrphanPacHiveEntries By OrderEntryData.
	 * 
	 * @param orderEntryModel
	 * @return List of type JnjPacHiveEntryModel
	 */
	List<JnjPacHiveEntryModel> getOrphanPacHiveEntriesByOrderEntryData(final AbstractOrderEntryModel orderEntryModel);
	
}
