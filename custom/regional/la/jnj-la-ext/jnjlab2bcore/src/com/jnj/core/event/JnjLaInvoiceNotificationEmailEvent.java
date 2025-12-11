/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.core.event;


import java.util.List;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderModel;

import com.jnj.core.model.JnJInvoiceOrderModel;



/**
 * Event class responsible for carrying data for submitting Invoice Email Notification.
 *
 */
public class JnjLaInvoiceNotificationEmailEvent extends AbstractCommerceUserEvent
{		
	private JnJInvoiceOrderModel invoice;	
	private OrderModel order;
	private List<String> b2bCustomerEmails;
	
	public List<String> getB2bCustomerEmails()
	{
		return b2bCustomerEmails;
	}
	
	public void setB2bCustomerEmails(final List<String> b2bCustomerEmails)
	{
		this.b2bCustomerEmails = b2bCustomerEmails;
	}
	
	public JnJInvoiceOrderModel getInvoice()
	{
		return invoice;
	}
	
	public void setInvoice(final JnJInvoiceOrderModel invoice)
	{
		this.invoice = invoice;
	}

	public OrderModel getOrder()
	{
		return order;
	}	
	
	public void setOrder(final OrderModel order)
	{
		this.order = order;
	}

}
