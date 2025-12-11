/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.la.core.data.order.JnjLAConsolidatedReportRowData;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;

import java.util.List;
import java.util.Locale;

public interface JnjLaConsolidatedReportService {

    EmailAttachmentModel createConsolidatedReport(List<JnjLAConsolidatedReportRowData> reportRows, JnJB2BUnitModel unit, Locale locale);
}
