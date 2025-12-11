
package com.jnj.core.dataload.mapper;

import jakarta.xml.bind.annotation.XmlElement;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;


public class LatamLineItem extends LineItem
{
	private String indirectCustomer = Jnjlab2bcoreConstants.Order.EMPTY_STRING;

	@XmlElement(name = "IndirectCustomerID")
	public String getIndirectCustomer()
	{
		return indirectCustomer;
	}

	public void setIndirectCustomer(final String indirectCustomer)
	{
		this.indirectCustomer = indirectCustomer;
	}

}