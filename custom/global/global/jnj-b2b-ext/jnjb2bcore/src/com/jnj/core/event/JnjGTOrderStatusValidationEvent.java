/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.util.List;
import java.util.Map;


/**
 * Event class responsible for carrying data for submitting Order Status In-bound Files Validation email report.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTOrderStatusValidationEvent extends AbstractCommerceUserEvent
{
	/**
	 * List of all file names validates against the lines count.
	 */
	protected List<String> orderStatusInboundFileNames;

	/**
	 * Maintains key value pair for each file name as key and corresponding counts as value.
	 */
	protected Map<String, String> fileNameAndCounts;

	/**
	 * @return the orderStatusInboundFileNames
	 */
	public List<String> getOrderStatusInboundFileNames()
	{
		return orderStatusInboundFileNames;
	}

	/**
	 * @param orderStatusInboundFileNames
	 *           the orderStatusInboundFileNames to set
	 */
	public void setOrderStatusInboundFileNames(final List<String> orderStatusInboundFileNames)
	{
		this.orderStatusInboundFileNames = orderStatusInboundFileNames;
	}

	/**
	 * @return the fileNameAndCounts
	 */
	public Map<String, String> getFileNameAndCounts()
	{
		return fileNameAndCounts;
	}

	/**
	 * @param fileNameAndCounts
	 *           the fileNameAndCounts to set
	 */
	public void setFileNameAndCounts(final Map<String, String> fileNameAndCounts)
	{
		this.fileNameAndCounts = fileNameAndCounts;
	}

}
