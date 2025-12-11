package com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for NFERequestData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NFERequestData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NFe_Key" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Language" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NFERequestData", propOrder =
{ "nFeKey", "language" })
@XmlRootElement(name = "NFERequestData")
public class NFERequestData
{

	@XmlElement(name = "NFe_Key", required = true, nillable = true)
	protected String nFeKey;
	@XmlElement(name = "Language", required = true, nillable = true)
	protected String language;

	/**
	 * Gets the value of the nFeKey property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNFeKey()
	{
		return nFeKey;
	}

	/**
	 * Sets the value of the nFeKey property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setNFeKey(final String value)
	{
		this.nFeKey = value;
	}

	/**
	 * Gets the value of the language property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLanguage()
	{
		return language;
	}

	/**
	 * Sets the value of the language property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setLanguage(final String value)
	{
		this.language = value;
	}

}
