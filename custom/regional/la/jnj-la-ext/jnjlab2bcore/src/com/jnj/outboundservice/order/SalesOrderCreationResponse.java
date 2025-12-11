
package com.jnj.outboundservice.order;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for salesOrderCreationResponse complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="salesOrderCreationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SalesOrder_Out" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}SalesOrder_Out"/>
 *         &lt;element name="Response" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}Response"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "salesOrderCreationResponse", propOrder =
{ "salesOrderOut", "response" })
@XmlRootElement(name = "SalesOrderCreationResponse")
public class SalesOrderCreationResponse
{

	@XmlElement(name = "SalesOrder_Out", required = true, nillable = true)
	protected SalesOrderOut salesOrderOut;
	@XmlElement(name = "Response", required = true, nillable = true)
	protected Response response;

	/**
	 * Gets the value of the salesOrderOut property.
	 * 
	 * @return possible object is {@link SalesOrderOut }
	 * 
	 */
	public SalesOrderOut getSalesOrderOut()
	{
		return salesOrderOut;
	}

	/**
	 * Sets the value of the salesOrderOut property.
	 * 
	 * @param value
	 *           allowed object is {@link SalesOrderOut }
	 * 
	 */
	public void setSalesOrderOut(final SalesOrderOut value)
	{
		this.salesOrderOut = value;
	}

	/**
	 * Gets the value of the response property.
	 * 
	 * @return possible object is {@link Response }
	 * 
	 */
	public Response getResponse()
	{
		return response;
	}

	/**
	 * Sets the value of the response property.
	 * 
	 * @param value
	 *           allowed object is {@link Response }
	 * 
	 */
	public void setResponse(final Response value)
	{
		this.response = value;
	}

}
