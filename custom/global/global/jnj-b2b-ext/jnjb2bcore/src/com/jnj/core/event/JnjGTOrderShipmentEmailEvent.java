/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * Event class responsible for carrying data for submitting Order Shipment Email Notification.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTOrderShipmentEmailEvent extends AbstractCommerceUserEvent
{
	/**
	 * SAP based Order Number.
	 */
	private String sapOrderNumber;

	/**
	 * Hybris based order code.
	 */
	private String orderCode;

	/**
	 * @return the sapOrderNumber
	 */
	public String getSapOrderNumber()
	{
		return sapOrderNumber;
	}

	/**
	 * @param sapOrderNumber
	 *           the sapOrderNumber to set
	 */
	public void setSapOrderNumber(final String sapOrderNumber)
	{
		this.sapOrderNumber = sapOrderNumber;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

}
