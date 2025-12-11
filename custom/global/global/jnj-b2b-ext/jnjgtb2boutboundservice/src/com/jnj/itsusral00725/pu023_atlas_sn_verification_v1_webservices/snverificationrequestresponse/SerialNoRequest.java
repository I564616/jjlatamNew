
package com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SerialNoRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SerialNoRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageID" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}messageID"/>
 *         &lt;element name="LPN" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}LPN"/>
 *         &lt;element name="appID" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}appID"/>
 *         &lt;element name="scanTimeStamp" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}scanTimeStamp"/>
 *         &lt;element name="locationLatitude" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationLatitude" minOccurs="0"/>
 *         &lt;element name="locationLongitude" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationLongitude" minOccurs="0"/>
 *         &lt;element name="locationName" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationName" minOccurs="0"/>
 *         &lt;element name="locationStreet" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationStreet" minOccurs="0"/>
 *         &lt;element name="locationCity" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationCity" minOccurs="0"/>
 *         &lt;element name="locationState" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationState" minOccurs="0"/>
 *         &lt;element name="locationProvince" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationProvince" minOccurs="0"/>
 *         &lt;element name="locationCountry" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationCountry" minOccurs="0"/>
 *         &lt;element name="locationZip" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}locationZip" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SerialNoRequest", propOrder = {
    "messageID",
    "lpn",
    "appID",
    "scanTimeStamp",
    "locationLatitude",
    "locationLongitude",
    "locationName",
    "locationStreet",
    "locationCity",
    "locationState",
    "locationProvince",
    "locationCountry",
    "locationZip"
})
public class SerialNoRequest {

    @XmlElement(required = true, nillable = true)
    protected String messageID;
    @XmlElement(name = "LPN", required = true, nillable = true)
    protected String lpn;
    @XmlElement(required = true, nillable = true)
    protected String appID;
    @XmlElement(required = true, nillable = true)
    protected String scanTimeStamp;
    @XmlElementRef(name = "locationLatitude", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationLatitude;
    @XmlElementRef(name = "locationLongitude", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationLongitude;
    @XmlElementRef(name = "locationName", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationName;
    @XmlElementRef(name = "locationStreet", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationStreet;
    @XmlElementRef(name = "locationCity", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationCity;
    @XmlElementRef(name = "locationState", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationState;
    @XmlElementRef(name = "locationProvince", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationProvince;
    @XmlElementRef(name = "locationCountry", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationCountry;
    @XmlElementRef(name = "locationZip", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationZip;

    /**
     * Gets the value of the messageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageID(String value) {
        this.messageID = value;
    }

    /**
     * Gets the value of the lpn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLPN() {
        return lpn;
    }

    /**
     * Sets the value of the lpn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLPN(String value) {
        this.lpn = value;
    }

    /**
     * Gets the value of the appID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppID() {
        return appID;
    }

    /**
     * Sets the value of the appID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppID(String value) {
        this.appID = value;
    }

    /**
     * Gets the value of the scanTimeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScanTimeStamp() {
        return scanTimeStamp;
    }

    /**
     * Sets the value of the scanTimeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScanTimeStamp(String value) {
        this.scanTimeStamp = value;
    }

    /**
     * Gets the value of the locationLatitude property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationLatitude() {
        return locationLatitude;
    }

    /**
     * Sets the value of the locationLatitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationLatitude(JAXBElement<String> value) {
        this.locationLatitude = value;
    }

    /**
     * Gets the value of the locationLongitude property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationLongitude() {
        return locationLongitude;
    }

    /**
     * Sets the value of the locationLongitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationLongitude(JAXBElement<String> value) {
        this.locationLongitude = value;
    }

    /**
     * Gets the value of the locationName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationName() {
        return locationName;
    }

    /**
     * Sets the value of the locationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationName(JAXBElement<String> value) {
        this.locationName = value;
    }

    /**
     * Gets the value of the locationStreet property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationStreet() {
        return locationStreet;
    }

    /**
     * Sets the value of the locationStreet property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationStreet(JAXBElement<String> value) {
        this.locationStreet = value;
    }

    /**
     * Gets the value of the locationCity property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationCity() {
        return locationCity;
    }

    /**
     * Sets the value of the locationCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationCity(JAXBElement<String> value) {
        this.locationCity = value;
    }

    /**
     * Gets the value of the locationState property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationState() {
        return locationState;
    }

    /**
     * Sets the value of the locationState property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationState(JAXBElement<String> value) {
        this.locationState = value;
    }

    /**
     * Gets the value of the locationProvince property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationProvince() {
        return locationProvince;
    }

    /**
     * Sets the value of the locationProvince property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationProvince(JAXBElement<String> value) {
        this.locationProvince = value;
    }

    /**
     * Gets the value of the locationCountry property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationCountry() {
        return locationCountry;
    }

    /**
     * Sets the value of the locationCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationCountry(JAXBElement<String> value) {
        this.locationCountry = value;
    }

    /**
     * Gets the value of the locationZip property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationZip() {
        return locationZip;
    }

    /**
     * Sets the value of the locationZip property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationZip(JAXBElement<String> value) {
        this.locationZip = value;
    }

}
