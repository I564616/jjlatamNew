
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
 * Java class for SalesOrder_In3 complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SalesOrder_In3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="in_Request_Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_SalesOrganization" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_DistributionChannel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_Division" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_OrderType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_SoldToNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_ShipToNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_CustomerPortalOrdernumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_PONumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_OrderChannel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_OrderReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_ContactName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_ContactPhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_ContactEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_RequestedDeliveryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_CompleteDelivery" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_ForbiddenSales" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_ContractReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in_orderLines" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}in_orderLines3" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SalesOrder_In3", propOrder =
{ "inRequestType", "inSalesOrganization", "inDistributionChannel", "inDivision", "inOrderType", "inSoldToNumber",
		"inShipToNumber", "inCustomerPortalOrdernumber", "inPONumber", "inOrderChannel", "inOrderReason", "inContactName",
		"inContactPhoneNumber", "inContactEmail", "inRequestedDeliveryDate", "inCompleteDelivery", "inForbiddenSales",
		"inContractReferenceNumber", "inOrderLines" })
@XmlRootElement(name = "SalesOrderIn3")
public class SalesOrderIn3
{

	@XmlElement(name = "in_Request_Type", required = true)
	protected String inRequestType;
	@XmlElement(name = "in_SalesOrganization", required = true)
	protected String inSalesOrganization;
	@XmlElement(name = "in_DistributionChannel", required = true)
	protected String inDistributionChannel;
	@XmlElement(name = "in_Division", required = true)
	protected String inDivision;
	@XmlElement(name = "in_OrderType", required = true)
	protected String inOrderType;
	@XmlElement(name = "in_SoldToNumber", required = true)
	protected String inSoldToNumber;
	@XmlElement(name = "in_ShipToNumber", required = true)
	protected String inShipToNumber;
	@XmlElementRef(name = "in_CustomerPortalOrdernumber", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inCustomerPortalOrdernumber;
	@XmlElementRef(name = "in_PONumber", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inPONumber;
	@XmlElementRef(name = "in_OrderChannel", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inOrderChannel;
	@XmlElementRef(name = "in_OrderReason", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inOrderReason;
	@XmlElementRef(name = "in_ContactName", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inContactName;
	@XmlElementRef(name = "in_ContactPhoneNumber", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inContactPhoneNumber;
	@XmlElementRef(name = "in_ContactEmail", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inContactEmail;
	@XmlElementRef(name = "in_RequestedDeliveryDate", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inRequestedDeliveryDate;
	@XmlElementRef(name = "in_CompleteDelivery", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inCompleteDelivery;
	@XmlElementRef(name = "in_ForbiddenSales", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inForbiddenSales;
	@XmlElementRef(name = "in_ContractReferenceNumber", type = JAXBElement.class, required = false)
	protected JAXBElement<String> inContractReferenceNumber;
	@XmlElement(name = "in_orderLines", required = true, nillable = true)
	protected List<InOrderLines3> inOrderLines;

	/**
	 * Gets the value of the inRequestType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInRequestType()
	{
		return inRequestType;
	}

	/**
	 * Sets the value of the inRequestType property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setInRequestType(final String value)
	{
		this.inRequestType = value;
	}

	/**
	 * Gets the value of the inSalesOrganization property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInSalesOrganization()
	{
		return inSalesOrganization;
	}

	/**
	 * Sets the value of the inSalesOrganization property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setInSalesOrganization(final String value)
	{
		this.inSalesOrganization = value;
	}

	/**
	 * Gets the value of the inDistributionChannel property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInDistributionChannel()
	{
		return inDistributionChannel;
	}

	/**
	 * Sets the value of the inDistributionChannel property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setInDistributionChannel(final String value)
	{
		this.inDistributionChannel = value;
	}

	/**
	 * Gets the value of the inDivision property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInDivision()
	{
		return inDivision;
	}

	/**
	 * Sets the value of the inDivision property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setInDivision(final String value)
	{
		this.inDivision = value;
	}

	/**
	 * Gets the value of the inOrderType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInOrderType()
	{
		return inOrderType;
	}

	/**
	 * Sets the value of the inOrderType property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setInOrderType(final String value)
	{
		this.inOrderType = value;
	}

	/**
	 * Gets the value of the inSoldToNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInSoldToNumber()
	{
		return inSoldToNumber;
	}

	/**
	 * Sets the value of the inSoldToNumber property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setInSoldToNumber(final String value)
	{
		this.inSoldToNumber = value;
	}

	/**
	 * Gets the value of the inShipToNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInShipToNumber()
	{
		return inShipToNumber;
	}

	/**
	 * Sets the value of the inShipToNumber property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setInShipToNumber(final String value)
	{
		this.inShipToNumber = value;
	}

	/**
	 * Gets the value of the inCustomerPortalOrdernumber property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInCustomerPortalOrdernumber()
	{
		return inCustomerPortalOrdernumber;
	}

	/**
	 * Sets the value of the inCustomerPortalOrdernumber property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInCustomerPortalOrdernumber(final JAXBElement<String> value)
	{
		this.inCustomerPortalOrdernumber = value;
	}

	/**
	 * Gets the value of the inPONumber property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInPONumber()
	{
		return inPONumber;
	}

	/**
	 * Sets the value of the inPONumber property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInPONumber(final JAXBElement<String> value)
	{
		this.inPONumber = value;
	}

	/**
	 * Gets the value of the inOrderChannel property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInOrderChannel()
	{
		return inOrderChannel;
	}

	/**
	 * Sets the value of the inOrderChannel property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInOrderChannel(final JAXBElement<String> value)
	{
		this.inOrderChannel = value;
	}

	/**
	 * Gets the value of the inOrderReason property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInOrderReason()
	{
		return inOrderReason;
	}

	/**
	 * Sets the value of the inOrderReason property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInOrderReason(final JAXBElement<String> value)
	{
		this.inOrderReason = value;
	}

	/**
	 * Gets the value of the inContactName property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInContactName()
	{
		return inContactName;
	}

	/**
	 * Sets the value of the inContactName property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInContactName(final JAXBElement<String> value)
	{
		this.inContactName = value;
	}

	/**
	 * Gets the value of the inContactPhoneNumber property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInContactPhoneNumber()
	{
		return inContactPhoneNumber;
	}

	/**
	 * Sets the value of the inContactPhoneNumber property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInContactPhoneNumber(final JAXBElement<String> value)
	{
		this.inContactPhoneNumber = value;
	}

	/**
	 * Gets the value of the inContactEmail property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInContactEmail()
	{
		return inContactEmail;
	}

	/**
	 * Sets the value of the inContactEmail property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInContactEmail(final JAXBElement<String> value)
	{
		this.inContactEmail = value;
	}

	/**
	 * Gets the value of the inRequestedDeliveryDate property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInRequestedDeliveryDate()
	{
		return inRequestedDeliveryDate;
	}

	/**
	 * Sets the value of the inRequestedDeliveryDate property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInRequestedDeliveryDate(final JAXBElement<String> value)
	{
		this.inRequestedDeliveryDate = value;
	}

	/**
	 * Gets the value of the inCompleteDelivery property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInCompleteDelivery()
	{
		return inCompleteDelivery;
	}

	/**
	 * Sets the value of the inCompleteDelivery property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInCompleteDelivery(final JAXBElement<String> value)
	{
		this.inCompleteDelivery = value;
	}

	/**
	 * Gets the value of the inForbiddenSales property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInForbiddenSales()
	{
		return inForbiddenSales;
	}

	/**
	 * Sets the value of the inForbiddenSales property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInForbiddenSales(final JAXBElement<String> value)
	{
		this.inForbiddenSales = value;
	}

	/**
	 * Gets the value of the inContractReferenceNumber property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public JAXBElement<String> getInContractReferenceNumber()
	{
		return inContractReferenceNumber;
	}

	/**
	 * Sets the value of the inContractReferenceNumber property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	public void setInContractReferenceNumber(final JAXBElement<String> value)
	{
		this.inContractReferenceNumber = value;
	}

	/**
	 * Gets the value of the inOrderLines property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the inOrderLines property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getInOrderLines().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link InOrderLines3 }
	 * 
	 * 
	 */
	public List<InOrderLines3> getInOrderLines()
	{
		if (inOrderLines == null)
		{
			inOrderLines = new ArrayList<InOrderLines3>();
		}
		return this.inOrderLines;
	}

}
