/*
 *
 */
package com.jnj.la.core.dao.customer;

import java.util.Date;
import java.util.List;

public interface JnjCDLExportDao
{

	/**
	 * This Method is user to export type between date range.
	 * 
	 * @param type      type
	 * @param startDate startDate
	 * @param endDate   endDate
	 * @return List<Object> List<Object>
	 */
	public List<Object> getCDLExportForTypeForDate(String type, Date startDate, Date endDate);

	/**
	 * This Method is user to export type based on date.
	 * 
	 * @param type                         type
	 * @param cronjoblastSuccessfulRunDate cronjoblastSuccessfulRunDate
	 * @return List<Object> List<Object>
	 */
	public List<Object> getCDLExportForType(String type, Date cronjoblastSuccessfulRunDate);

}