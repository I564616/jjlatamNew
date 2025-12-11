
package com.jnj.outboundservice.invoice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for receiveElectronicBillingFromHybrisWrapper complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="receiveElectronicBillingFromHybrisWrapper">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ElectronicBillingRequest" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0504_ElectronicBilling_Hybris_Source_v1.webservices:receiveElectronicBillingWS}ElectronicBillingRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiveElectronicBillingFromHybrisWrapper", propOrder =
{ "electronicBillingRequest" })
@XmlRootElement(name = "ReceiveElectronicBillingFromHybrisWrapper")
public class ReceiveElectronicBillingFromHybrisWrapper
{

	@XmlElement(name = "ElectronicBillingRequest", required = true, nillable = true)
	protected ElectronicBillingRequest electronicBillingRequest;

	/**
	 * Gets the value of the electronicBillingRequest property.
	 *
	 * @return possible object is {@link ElectronicBillingRequest }
	 *
	 */
	public ElectronicBillingRequest getElectronicBillingRequest()
	{
		return electronicBillingRequest;
	}

	/**
	 * Sets the value of the electronicBillingRequest property.
	 *
	 * @param value
	 *           allowed object is {@link ElectronicBillingRequest }
	 *
	 */
	public void setElectronicBillingRequest(final ElectronicBillingRequest value)
	{
		this.electronicBillingRequest = value;
	}

}
