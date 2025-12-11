/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.core.event;

import java.util.Map;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

public class JnjLaActiveUsersReportEvent extends AbstractCommerceUserEvent {

	private Map<String, String> activeUserReportData;

	public Map<String, String> getActiveUserReportData() {
		return activeUserReportData;
	}

	public void setActiveUserReportData(final Map<String, String> activeUserReportData) {
		this.activeUserReportData = activeUserReportData;
	}

	
	
}
