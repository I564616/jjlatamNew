
package com.jnj.outboundservice.invoice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for receiveElectronicBillingFromHybrisWrapperResponse complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="receiveElectronicBillingFromHybrisWrapperResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ElectronicBillingResponse" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0504_ElectronicBilling_Hybris_Source_v1.webservices:receiveElectronicBillingWS}ElectronicBillingResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiveElectronicBillingFromHybrisWrapperResponse", propOrder =
{ "electronicBillingResponse" })
@XmlRootElement(name = "ReceiveElectronicBillingFromHybrisWrapperResponse")
public class ReceiveElectronicBillingFromHybrisWrapperResponse
{

	@XmlElement(name = "ElectronicBillingResponse", required = true, nillable = true)
	protected ElectronicBillingResponse electronicBillingResponse;

	/**
	 * Gets the value of the electronicBillingResponse property.
	 *
	 * @return possible object is {@link ElectronicBillingResponse }
	 *
	 */
	public ElectronicBillingResponse getElectronicBillingResponse()
	{
		return electronicBillingResponse;
	}

	/**
	 * Sets the value of the electronicBillingResponse property.
	 *
	 * @param value
	 *           allowed object is {@link ElectronicBillingResponse }
	 *
	 */
	public void setElectronicBillingResponse(final ElectronicBillingResponse value)
	{
		this.electronicBillingResponse = value;
	}

}
