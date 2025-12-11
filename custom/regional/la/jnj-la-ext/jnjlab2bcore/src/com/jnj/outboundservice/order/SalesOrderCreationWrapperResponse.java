
package com.jnj.outboundservice.order;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for salesOrderCreationWrapperResponse complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="salesOrderCreationWrapperResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="salesOrderCreationResponse" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}salesOrderCreationResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "salesOrderCreationWrapperResponse", propOrder =
{ "salesOrderCreationResponse" })
@XmlRootElement(name = "SalesOrderCreationWrapperResponse")
public class SalesOrderCreationWrapperResponse
{

	@XmlElement(required = true, nillable = true)
	protected SalesOrderCreationResponse salesOrderCreationResponse;

	/**
	 * Gets the value of the salesOrderCreationResponse property.
	 * 
	 * @return possible object is {@link SalesOrderCreationResponse }
	 * 
	 */
	public SalesOrderCreationResponse getSalesOrderCreationResponse()
	{
		return salesOrderCreationResponse;
	}

	/**
	 * Sets the value of the salesOrderCreationResponse property.
	 * 
	 * @param value
	 *           allowed object is {@link SalesOrderCreationResponse }
	 * 
	 */
	public void setSalesOrderCreationResponse(final SalesOrderCreationResponse value)
	{
		this.salesOrderCreationResponse = value;
	}

}
