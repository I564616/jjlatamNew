/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjScheduledLineData;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

/**
* JnjLatamScheduledLinesPopulator
*/
public class JnjLatamScheduledLinesPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData> {

	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (target instanceof JnjLaOrderEntryData jnjLaOrderEntryData){
			final List<JnjScheduledLineData> scheduledLineData = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(source.getDeliverySchedules())) {
				source.getDeliverySchedules().stream().forEach(scheduleLineModel->
				{ 
				 final JnjScheduledLineData scheduledLine = new JnjScheduledLineData();
				  scheduledLine.setLineNumber(scheduleLineModel.getLineNumber());
				  if(scheduleLineModel.getDeliveryDate() != null) {
					  scheduledLine.setDeliveryDate(dateFormatter.format(scheduleLineModel.getDeliveryDate()));
				  }
				  if(scheduleLineModel.getQty() != null) {
					  scheduledLine.setConfirmedQuantity(scheduleLineModel.getQty().toString());
				  }
				  scheduledLineData.add(scheduledLine);
				}		
			   );
			}
			if(CollectionUtils.isNotEmpty(scheduledLineData)) {
				jnjLaOrderEntryData.setScheduledLines(scheduledLineData);
			}
		}
	}
}
