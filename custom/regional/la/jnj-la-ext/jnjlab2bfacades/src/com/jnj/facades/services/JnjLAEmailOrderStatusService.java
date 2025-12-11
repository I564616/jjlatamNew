/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.List;

public interface JnjLAEmailOrderStatusService {

    void processPendingEmails(final JnjIntegrationRSACronJobModel model, final JnJEmailPeriodicity periodicity);

    void processPendingEmails(JnjIntegrationRSACronJobModel model, JnJEmailPeriodicity periodicity, String site);

    void removePendingFromOrderEntries(JnJEmailPeriodicity periodicity, final List<? extends AbstractOrderEntryModel> entries);

    /**
	  * to trigger email daily  when order status change 
	  * @param daily the daily is the periodicity
	  * @param string the string is the String
	  */
	void processPendingEmailsForDaily(final JnJEmailPeriodicity daily, final String string);

    /**
	  * to trigger email Immediate  when order status change 
	  * @param model the model is the JnjIntegrationRSACronJobModel
	  * @param periodicity the periodicity is the JnJEmailPeriodicity
	  */
	void processPendingEmailsForImmediate(final JnjIntegrationRSACronJobModel model, final JnJEmailPeriodicity periodicity);
}