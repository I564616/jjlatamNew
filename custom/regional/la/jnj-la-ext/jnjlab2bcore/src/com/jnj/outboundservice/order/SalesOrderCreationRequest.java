
package com.jnj.outboundservice.order;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for salesOrderCreationRequest complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="salesOrderCreationRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Header" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}BTBControlArea"/>
 *         &lt;element name="SalesOrder_In" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}SalesOrder_In"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "salesOrderCreationRequest", propOrder =
{ "header", "salesOrderIn" })
@XmlRootElement(name = "SalesOrderCreationRequest")
public class SalesOrderCreationRequest
{

	@XmlElement(name = "Header", required = true, nillable = true)
	protected BTBControlArea header;
	@XmlElement(name = "SalesOrder_In", required = true, nillable = true)
	protected SalesOrderIn salesOrderIn;

	/**
	 * Gets the value of the header property.
	 * 
	 * @return possible object is {@link BTBControlArea }
	 * 
	 */
	public BTBControlArea getHeader()
	{
		return header;
	}

	/**
	 * Sets the value of the header property.
	 * 
	 * @param value
	 *           allowed object is {@link BTBControlArea }
	 * 
	 */
	public void setHeader(final BTBControlArea value)
	{
		this.header = value;
	}

	/**
	 * Gets the value of the salesOrderIn property.
	 * 
	 * @return possible object is {@link SalesOrderIn }
	 * 
	 */
	public SalesOrderIn getSalesOrderIn()
	{
		return salesOrderIn;
	}

	/**
	 * Sets the value of the salesOrderIn property.
	 * 
	 * @param value
	 *           allowed object is {@link SalesOrderIn }
	 * 
	 */
	public void setSalesOrderIn(final SalesOrderIn value)
	{
		this.salesOrderIn = value;
	}

}
