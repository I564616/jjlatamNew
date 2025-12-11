package com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import br.inf.portalfiscal.nfe.TNfeProc;


/**
 * <p>
 * Java class for ElectronicNotaFiscalResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ElectronicNotaFiscalResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.portalfiscal.inf.br/nfe}nfeProc"/>
 *         &lt;element name="Response" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}Response" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElectronicNotaFiscalResponse", propOrder =
{ "nfeProc", "response" })
@XmlRootElement(name = "ElectronicNotaFiscalResponse")
public class ElectronicNotaFiscalResponse
{

	@XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe", required = true)
	protected TNfeProc nfeProc;
	@XmlElementRef(name = "Response", type = JAXBElement.class, required = false)
	protected JAXBElement<Response> response;

	/**
	 * Gets the value of the nfeProc property.
	 * 
	 * @return possible object is {@link TNfeProc }
	 * 
	 */
	public TNfeProc getNfeProc()
	{
		return nfeProc;
	}

	/**
	 * Sets the value of the nfeProc property.
	 * 
	 * @param value
	 *           allowed object is {@link TNfeProc }
	 * 
	 */
	public void setNfeProc(final TNfeProc value)
	{
		this.nfeProc = value;
	}

	/**
	 * Gets the value of the response property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link Response }{@code >}
	 * 
	 */
	public JAXBElement<Response> getResponse()
	{
		return response;
	}

	/**
	 * Sets the value of the response property.
	 * 
	 * @param value
	 *           allowed object is {@link JAXBElement }{@code <}{@link Response }{@code >}
	 * 
	 */
	public void setResponse(final JAXBElement<Response> value)
	{
		this.response = value;
	}

}
