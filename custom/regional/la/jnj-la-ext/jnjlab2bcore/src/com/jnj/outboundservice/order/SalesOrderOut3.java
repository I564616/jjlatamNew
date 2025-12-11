
package com.jnj.outboundservice.order;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for SalesOrder_Out3 complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SalesOrder_Out3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out_SalesOrderNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="out_orderLines" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}out_orderLines3" maxOccurs="unbounded"/>
 *         &lt;element name="out_SalesOrderRejectionStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="out_SalesOrderOverallStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="out_SalesOrderCreditStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="out_SalesOrdertTotalNetValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Message_return" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}Message_return3" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SalesOrder_Out3", propOrder =
{ "outSalesOrderNumber", "outOrderLines", "outSalesOrderRejectionStatus", "outSalesOrderOverallStatus",
		"outSalesOrderCreditStatus", "outSalesOrdertTotalNetValue", "messageReturn" })
@XmlRootElement(name = "SalesOrderOut3")
public class SalesOrderOut3
{

	@XmlElementRef(name = "out_SalesOrderNumber", type = JAXBElement.class, required = false)
	protected JAXBElement<String> outSalesOrderNumber;
	@XmlElement(name = "out_orderLines", required = true, nillable = true)
	protected List<OutOrderLines3> outOrderLines;
	@XmlElementRef(name = "out_SalesOrderRejectionStatus", type = JAXBElement.class, required = false)
	protected JAXBElement<String> outSalesOrderRejectionStatus;
	@XmlElementRef(name = "out_SalesOrderOverallStatus", type = JAXBElement.class, required = false)
	protected JAXBElement<String> outSalesOrderOverallStatus;
	@XmlElementRef(name = "out_SalesOrderCreditStatus", type = JAXBElement.class, required = false)
	protected JAXBElement<String> outSalesOrderCreditStatus;
	@XmlElement(name = "out_SalesOrdertTotalNetValue", required = true)
	protected String outSalesOrdertTotalNetValue;
	@XmlElement(name = "Message_return", required = true, nillable = true)
	protected List<MessageReturn3> messageReturn;

	/**
	 * Gets the value of the outSalesOrderNumber property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getOutSalesOrderNumber()
	{
		return outSalesOrderNumber;
	}

	/**
	 * Sets the value of the outSalesOrderNumber property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setOutSalesOrderNumber(final JAXBElement<String> value)
	{
		this.outSalesOrderNumber = value;
	}

	/**
	 * Gets the value of the outOrderLines property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the outOrderLines property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOutOrderLines().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link OutOrderLines3 }
	 * 
	 * 
	 */
	public List<OutOrderLines3> getOutOrderLines()
	{
		if (outOrderLines == null)
		{
			outOrderLines = new ArrayList<OutOrderLines3>();
		}
		return this.outOrderLines;
	}

	/**
	 * Gets the value of the outSalesOrderRejectionStatus property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getOutSalesOrderRejectionStatus()
	{
		return outSalesOrderRejectionStatus;
	}

	/**
	 * Sets the value of the outSalesOrderRejectionStatus property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setOutSalesOrderRejectionStatus(final JAXBElement<String> value)
	{
		this.outSalesOrderRejectionStatus = value;
	}

	/**
	 * Gets the value of the outSalesOrderOverallStatus property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getOutSalesOrderOverallStatus()
	{
		return outSalesOrderOverallStatus;
	}

	/**
	 * Sets the value of the outSalesOrderOverallStatus property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setOutSalesOrderOverallStatus(final JAXBElement<String> value)
	{
		this.outSalesOrderOverallStatus = value;
	}

	/**
	 * Gets the value of the outSalesOrderCreditStatus property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getOutSalesOrderCreditStatus()
	{
		return outSalesOrderCreditStatus;
	}

	/**
	 * Sets the value of the outSalesOrderCreditStatus property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setOutSalesOrderCreditStatus(final JAXBElement<String> value)
	{
		this.outSalesOrderCreditStatus = value;
	}

	/**
	 * Gets the value of the outSalesOrdertTotalNetValue property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOutSalesOrdertTotalNetValue()
	{
		return outSalesOrdertTotalNetValue;
	}

	/**
	 * Sets the value of the outSalesOrdertTotalNetValue property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setOutSalesOrdertTotalNetValue(final String value)
	{
		this.outSalesOrdertTotalNetValue = value;
	}

	/**
	 * Gets the value of the messageReturn property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the messageReturn property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMessageReturn().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link MessageReturn3 }
	 * 
	 * 
	 */
	public List<MessageReturn3> getMessageReturn()
	{
		if (messageReturn == null)
		{
			messageReturn = new ArrayList<MessageReturn3>();
		}
		return this.messageReturn;
	}

}
