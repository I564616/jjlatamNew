
package com.jnj.outboundservice.order;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for salesOrderSimulationWrapperResponse complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="salesOrderSimulationWrapperResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="salesOrderSimulationResponse" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}salesOrderSimulationResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "salesOrderSimulationWrapperResponse", propOrder =
{ "salesOrderSimulationResponse" })
@XmlRootElement(name = "SalesOrderSimulationWrapperResponse")
public class SalesOrderSimulationWrapperResponse
{

	@XmlElement(required = true, nillable = true)
	protected SalesOrderSimulationResponse salesOrderSimulationResponse;

	/**
	 * Gets the value of the salesOrderSimulationResponse property.
	 * 
	 * @return possible object is {@link SalesOrderSimulationResponse }
	 * 
	 */
	public SalesOrderSimulationResponse getSalesOrderSimulationResponse()
	{
		return salesOrderSimulationResponse;
	}

	/**
	 * Sets the value of the salesOrderSimulationResponse property.
	 * 
	 * @param value
	 *           allowed object is {@link SalesOrderSimulationResponse }
	 * 
	 */
	public void setSalesOrderSimulationResponse(final SalesOrderSimulationResponse value)
	{
		this.salesOrderSimulationResponse = value;
	}

}
