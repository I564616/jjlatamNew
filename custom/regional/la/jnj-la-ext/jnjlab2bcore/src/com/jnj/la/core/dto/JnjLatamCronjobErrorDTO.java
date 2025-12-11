/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.dto;

import java.util.Date;

@SuppressWarnings("unused") // all getters here are used by a template velocity
public class JnjLatamCronjobErrorDTO {

	private String cronJobName;
	private Date startDate;
	private Date endDate;
	private String duration;
	private String result;

    public String getCronJobName() {
		return cronJobName;
	}

	public void setCronJobName(final String cronJobName) {
		this.cronJobName = cronJobName;
	}

	public Date getStartDate() {
		return (Date) startDate.clone();
	}

	public void setStartDate(final Date startDate) {
        if (startDate != null) {
		    this.startDate = (Date) startDate.clone();
        }
	}

	public Date getEndDate() {
		return (Date) endDate.clone();
	}

	public void setEndDate(final Date endDate) {
		if (endDate != null) {
            this.endDate = (Date) endDate.clone();
        }
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(final String duration) {
		this.duration = duration;
	}

	public String getResult() {
		return result;
	}

	public void setResult(final String result) {
		this.result = result;
	}

}
