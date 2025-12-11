
package com.jnj.outboundservice.order;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for Message_return3 complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Message_return3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NUMBER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Message_return3", propOrder =
{ "id", "message", "number", "type" })
@XmlRootElement(name = "MessageReturn3")
public class MessageReturn3
{

	@XmlElement(name = "ID", required = true)
	protected String id;
	@XmlElement(name = "MESSAGE", required = true)
	protected String message;
	@XmlElement(name = "NUMBER", required = true)
	protected String number;
	@XmlElement(name = "TYPE", required = true)
	protected String type;

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getID()
	{
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setID(final String value)
	{
		this.id = value;
	}

	/**
	 * Gets the value of the message property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMESSAGE()
	{
		return message;
	}

	/**
	 * Sets the value of the message property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setMESSAGE(final String value)
	{
		this.message = value;
	}

	/**
	 * Gets the value of the number property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNUMBER()
	{
		return number;
	}

	/**
	 * Sets the value of the number property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setNUMBER(final String value)
	{
		this.number = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTYPE()
	{
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setTYPE(final String value)
	{
		this.type = value;
	}

}
