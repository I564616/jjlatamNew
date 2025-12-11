
package com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for shipToAdress complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="shipToAdress">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShipToName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ShipToStreet" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ShipToCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ShipToState" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ShipToCountry" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ShipToZip" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shipToAdress", propOrder = {
    "shipToName",
    "shipToStreet",
    "shipToCity",
    "shipToState",
    "shipToCountry",
    "shipToZip"
})
public class ShipToAdress {

    @XmlElement(name = "ShipToName", required = true)
    protected String shipToName;
    @XmlElement(name = "ShipToStreet", required = true)
    protected String shipToStreet;
    @XmlElement(name = "ShipToCity", required = true, nillable = true)
    protected String shipToCity;
    @XmlElement(name = "ShipToState", required = true, nillable = true)
    protected String shipToState;
    @XmlElement(name = "ShipToCountry", required = true, nillable = true)
    protected String shipToCountry;
    @XmlElement(name = "ShipToZip", required = true, nillable = true)
    protected String shipToZip;

    /**
     * Gets the value of the shipToName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipToName() {
        return shipToName;
    }

    /**
     * Sets the value of the shipToName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipToName(String value) {
        this.shipToName = value;
    }

    /**
     * Gets the value of the shipToStreet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipToStreet() {
        return shipToStreet;
    }

    /**
     * Sets the value of the shipToStreet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipToStreet(String value) {
        this.shipToStreet = value;
    }

    /**
     * Gets the value of the shipToCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipToCity() {
        return shipToCity;
    }

    /**
     * Sets the value of the shipToCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipToCity(String value) {
        this.shipToCity = value;
    }

    /**
     * Gets the value of the shipToState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipToState() {
        return shipToState;
    }

    /**
     * Sets the value of the shipToState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipToState(String value) {
        this.shipToState = value;
    }

    /**
     * Gets the value of the shipToCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipToCountry() {
        return shipToCountry;
    }

    /**
     * Sets the value of the shipToCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipToCountry(String value) {
        this.shipToCountry = value;
    }

    /**
     * Gets the value of the shipToZip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipToZip() {
        return shipToZip;
    }

    /**
     * Sets the value of the shipToZip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipToZip(String value) {
        this.shipToZip = value;
    }

}
