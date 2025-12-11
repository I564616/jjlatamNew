/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.reports.converters.populator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.core.model.order.OrderEntryModel;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjLaOpenOrdersReportReponseData;

import de.hybris.platform.converters.Populator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class JnjLaOpenOrdersReportResponsePopulator implements Populator<OrderEntryModel, JnjLaOpenOrdersReportReponseData> {
	final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
	private static final String ORDER_TYPE = "cart.common.orderType.";
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;


  /**
   * This method will be used to populate the data from openOrdersReporttemplate model  to data object
   * @param source
   * @param target
   * @throws ConversionException
   */
    @Override
    public void populate(final OrderEntryModel source, final JnjLaOpenOrdersReportReponseData target) 
 {
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setExtendedPrice(decimalFormat.format(source.getTotalPrice()));
		target.setEstimatedDeliveryDate((null !=source.getExpectedDeliveryDate())? convertDateFormat(source.getExpectedDeliveryDate()):StringUtils.EMPTY);
		target.setAccountName(source.getOrder().getUnit().getName());
		target.setOrderType(null!=source.getOrder().getJnjOrderType()? source.getOrder().getJnjOrderType().getName():jnjCommonUtil.getMessageFromImpex(ORDER_TYPE + source.getOrder().getOrderType()));
	    target.setLineNumber((null != source.getSapOrderlineNumber())? ""+source.getSapOrderlineNumber() : StringUtils.EMPTY);
		target.setRequestedDeliveryDate((null !=source.getExpectedDeliveryDate())? convertDateFormat(source.getExpectedDeliveryDate()):StringUtils.EMPTY);

	}
    
    private String convertDateFormat(final Date orderDate) {
		if(orderDate == null){
			return StringUtils.EMPTY;
		}
		SimpleDateFormat format2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		return format2.format(orderDate);

	}


   
}