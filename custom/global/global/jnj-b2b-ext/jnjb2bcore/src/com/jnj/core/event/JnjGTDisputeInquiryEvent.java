/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.user.AddressModel;

import com.jnj.core.dto.JnjGTDisputeItemInquiryDto;
import com.jnj.core.dto.JnjGTDisputeOrderInquiryDto;


/**
 * Event class responsible for carrying data for submitting dispute Order and Item inquiry.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTDisputeInquiryEvent extends AbstractCommerceUserEvent
{
	/**
	 * Data class for Dispute Order Inquiry.
	 */
	private JnjGTDisputeOrderInquiryDto disputeOrderInquiryData;

	/**
	 * Data class for Dispute Item Inquiry.
	 */
	private JnjGTDisputeItemInquiryDto disputeItemInquiryData;

	/**
	 * 
	 */
	private boolean disputeOrder;

	/**
	 * 
	 */
	private AddressModel shipToAddress;

	/**
	 * 
	 */
	private AddressModel correctAddress;

	/**
	 * @return the disputeOrderInquiryData
	 */
	public JnjGTDisputeOrderInquiryDto getDisputeOrderInquiryData()
	{
		return disputeOrderInquiryData;
	}

	/**
	 * @param disputeOrderInquiryData
	 *           the disputeOrderInquiryData to set
	 */
	public void setDisputeOrderInquiryData(final JnjGTDisputeOrderInquiryDto disputeOrderInquiryData)
	{
		this.disputeOrderInquiryData = disputeOrderInquiryData;
	}

	/**
	 * @return the disputeItemInquiryData
	 */
	public JnjGTDisputeItemInquiryDto getDisputeItemInquiryData()
	{
		return disputeItemInquiryData;
	}

	/**
	 * @param disputeItemInquiryData
	 *           the disputeItemInquiryData to set
	 */
	public void setDisputeItemInquiryData(final JnjGTDisputeItemInquiryDto disputeItemInquiryData)
	{
		this.disputeItemInquiryData = disputeItemInquiryData;
	}

	/**
	 * @return the disputeOrder
	 */
	public boolean isDisputeOrder()
	{
		return disputeOrder;
	}

	/**
	 * @param disputeOrder
	 *           the disputeOrder to set
	 */
	public void setDisputeOrder(final boolean disputeOrder)
	{
		this.disputeOrder = disputeOrder;
	}

	/**
	 * @return the shipToAddress
	 */
	public AddressModel getShipToAddress()
	{
		return shipToAddress;
	}

	/**
	 * @param shipToAddress
	 *           the shipToAddress to set
	 */
	public void setShipToAddress(final AddressModel shipToAddress)
	{
		this.shipToAddress = shipToAddress;
	}

	/**
	 * @return the correctAddress
	 */
	public AddressModel getCorrectAddress()
	{
		return correctAddress;
	}

	/**
	 * @param correctAddress
	 *           the correctAddress to set
	 */
	public void setCorrectAddress(final AddressModel correctAddress)
	{
		this.correctAddress = correctAddress;
	}

}
