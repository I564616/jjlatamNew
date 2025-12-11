
package com.jnj.outboundservice.order;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for in_orderLines3 complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="in_orderLines3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MaterialNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QuantityRequested" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SalesUOM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LineNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpectedDeliveryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IndirectCustomerAcct" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PriceOverrideRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PriceOverrideReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExpectedPriceRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "in_orderLines3", propOrder =
{ "materialNumber", "quantityRequested", "salesUOM", "lineNumber", "expectedDeliveryDate", "indirectCustomerAcct",
		"priceOverrideRate", "priceOverrideReason", "expectedPriceRate" })
@XmlRootElement(name = "InOrderLines3")
public class InOrderLines3
{

	@XmlElement(name = "MaterialNumber", required = true)
	protected String materialNumber;
	@XmlElement(name = "QuantityRequested", required = true)
	protected String quantityRequested;
	@XmlElement(name = "SalesUOM", required = true)
	protected String salesUOM;
	@XmlElement(name = "LineNumber", required = true)
	protected String lineNumber;
	@XmlElementRef(name = "ExpectedDeliveryDate", type = JAXBElement.class, required = false)
	protected JAXBElement<String> expectedDeliveryDate;
	@XmlElementRef(name = "IndirectCustomerAcct", type = JAXBElement.class, required = false)
	protected JAXBElement<String> indirectCustomerAcct;
	@XmlElementRef(name = "PriceOverrideRate", type = JAXBElement.class, required = false)
	protected JAXBElement<String> priceOverrideRate;
	@XmlElementRef(name = "PriceOverrideReason", type = JAXBElement.class, required = false)
	protected JAXBElement<String> priceOverrideReason;
	@XmlElementRef(name = "ExpectedPriceRate", type = JAXBElement.class, required = false)
	protected JAXBElement<String> expectedPriceRate;

	/**
	 * Gets the value of the materialNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMaterialNumber()
	{
		return materialNumber;
	}

	/**
	 * Sets the value of the materialNumber property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setMaterialNumber(final String value)
	{
		this.materialNumber = value;
	}

	/**
	 * Gets the value of the quantityRequested property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getQuantityRequested()
	{
		return quantityRequested;
	}

	/**
	 * Sets the value of the quantityRequested property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setQuantityRequested(final String value)
	{
		this.quantityRequested = value;
	}

	/**
	 * Gets the value of the salesUOM property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSalesUOM()
	{
		return salesUOM;
	}

	/**
	 * Sets the value of the salesUOM property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setSalesUOM(final String value)
	{
		this.salesUOM = value;
	}

	/**
	 * Gets the value of the lineNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLineNumber()
	{
		return lineNumber;
	}

	/**
	 * Sets the value of the lineNumber property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setLineNumber(final String value)
	{
		this.lineNumber = value;
	}

	/**
	 * Gets the value of the expectedDeliveryDate property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getExpectedDeliveryDate()
	{
		return expectedDeliveryDate;
	}

	/**
	 * Sets the value of the expectedDeliveryDate property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setExpectedDeliveryDate(final JAXBElement<String> value)
	{
		this.expectedDeliveryDate = value;
	}

	/**
	 * Gets the value of the indirectCustomerAcct property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getIndirectCustomerAcct()
	{
		return indirectCustomerAcct;
	}

	/**
	 * Sets the value of the indirectCustomerAcct property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setIndirectCustomerAcct(final JAXBElement<String> value)
	{
		this.indirectCustomerAcct = value;
	}

	/**
	 * Gets the value of the priceOverrideRate property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getPriceOverrideRate()
	{
		return priceOverrideRate;
	}

	/**
	 * Sets the value of the priceOverrideRate property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setPriceOverrideRate(final JAXBElement<String> value)
	{
		this.priceOverrideRate = value;
	}

	/**
	 * Gets the value of the priceOverrideReason property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getPriceOverrideReason()
	{
		return priceOverrideReason;
	}

	/**
	 * Sets the value of the priceOverrideReason property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setPriceOverrideReason(final JAXBElement<String> value)
	{
		this.priceOverrideReason = value;
	}

	/**
	 * Gets the value of the expectedPriceRate property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getExpectedPriceRate()
	{
		return expectedPriceRate;
	}

	/**
	 * Sets the value of the expectedPriceRate property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setExpectedPriceRate(final JAXBElement<String> value)
	{
		this.expectedPriceRate = value;
	}

}
