/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.reports.converters.populator;

import com.jnj.la.core.data.JnjLaOpenOrdersReportTemplateData;
import com.jnj.la.core.model.JnjLaOpenOrdersReportTemplateModel;

import de.hybris.platform.converters.Populator;

import org.springframework.util.Assert;

public class JnjLaOpenOrdersReportTemplatePopulator implements Populator<JnjLaOpenOrdersReportTemplateModel, JnjLaOpenOrdersReportTemplateData> {

  /**
   * This method will be used to populate the data from openOrdersReporttemplate model  to data object
   * @param source
   * @param target
   * @throws ConversionException
   */
    @Override
    public void populate(final JnjLaOpenOrdersReportTemplateModel source, final JnjLaOpenOrdersReportTemplateData target) 
 {
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setTemplateName(source.getTemplateName());
		target.setAccountIds(source.getSelectedAccountIds());
		target.setQuickSelection(source.getQuickSelection());
		target.setToDate(source.getToDate());
		target.setFromDate(source.getFromDate());
		target.setOrderType(source.getOrderType());
		target.setOrderNumber(source.getOrderNumber());
		target.setProductCode(source.getProductCode());
		target.setShipTo(source.getShipTo());
		target.setReportColumns(source.getReportColumns());

	}

   
}