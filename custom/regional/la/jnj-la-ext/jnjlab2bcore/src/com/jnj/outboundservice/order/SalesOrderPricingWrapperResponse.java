
package com.jnj.outboundservice.order;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for salesOrderPricingWrapperResponse complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="salesOrderPricingWrapperResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="salesOrderPricingResponse" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}salesOrderPricingResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "salesOrderPricingWrapperResponse", propOrder =
{ "salesOrderPricingResponse" })
@XmlRootElement(name = "SalesOrderPricingWrapperResponse")
public class SalesOrderPricingWrapperResponse
{

	@XmlElement(required = true, nillable = true)
	protected SalesOrderPricingResponse salesOrderPricingResponse;

	/**
	 * Gets the value of the salesOrderPricingResponse property.
	 * 
	 * @return possible object is {@link SalesOrderPricingResponse }
	 * 
	 */
	public SalesOrderPricingResponse getSalesOrderPricingResponse()
	{
		return salesOrderPricingResponse;
	}

	/**
	 * Sets the value of the salesOrderPricingResponse property.
	 * 
	 * @param value
	 *           allowed object is {@link SalesOrderPricingResponse }
	 * 
	 */
	public void setSalesOrderPricingResponse(final SalesOrderPricingResponse value)
	{
		this.salesOrderPricingResponse = value;
	}

}
