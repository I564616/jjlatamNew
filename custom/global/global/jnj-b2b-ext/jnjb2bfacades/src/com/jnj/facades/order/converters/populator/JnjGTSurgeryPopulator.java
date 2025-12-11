/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.converters.Populator;

import com.jnj.facades.data.SurgeryInfoData;
import com.jnj.core.model.JnjGTSurgeryInfoModel;


/**
 * @author Accenture
 * @version 1.0
 */
public class JnjGTSurgeryPopulator implements Populator<JnjGTSurgeryInfoModel, SurgeryInfoData>
{

	@Override
	public void populate(final JnjGTSurgeryInfoModel source, final SurgeryInfoData target)
	{
		target.setSurgerySpecialty(source.getSurgerySpecialty());
		target.setOrthobiologics(source.getOrthobiologics());
		target.setInterbody(source.getInterbody());
		target.setInterbodyFusion(source.getInterbodyFusion());
		target.setLevelsInstrumented(source.getLevelsInstrumented());
		target.setCas(source.getCas());
		target.setCaseDate(source.getCaseDate());
		target.setCaseNumber(source.getCaseNumber());
		target.setPathology(source.getPathology());
		target.setProcedureType(source.getProcedureType());
		target.setSurgicalApproach(source.getSurgicalApproach());
		target.setZone(source.getZone());

	}
}