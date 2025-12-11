
package com.jnj.outboundservice.order;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for salesOrderCreationWrapper complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="salesOrderCreationWrapper">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="salesOrderCreationRequest" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}salesOrderCreationRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "salesOrderCreationWrapper", propOrder =
{ "salesOrderCreationRequest" })
@XmlRootElement(name = "SalesOrderCreationWrapper")
public class SalesOrderCreationWrapper
{

	@XmlElement(required = true, nillable = true)
	protected SalesOrderCreationRequest salesOrderCreationRequest;

	/**
	 * Gets the value of the salesOrderCreationRequest property.
	 * 
	 * @return possible object is {@link SalesOrderCreationRequest }
	 * 
	 */
	public SalesOrderCreationRequest getSalesOrderCreationRequest()
	{
		return salesOrderCreationRequest;
	}

	/**
	 * Sets the value of the salesOrderCreationRequest property.
	 * 
	 * @param value
	 *           allowed object is {@link SalesOrderCreationRequest }
	 * 
	 */
	public void setSalesOrderCreationRequest(final SalesOrderCreationRequest value)
	{
		this.salesOrderCreationRequest = value;
	}

}
