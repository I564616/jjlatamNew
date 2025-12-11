
package com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SerialNoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SerialNoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageID" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}messageID2"/>
 *         &lt;element name="LPN" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}LPN2"/>
 *         &lt;element name="serialNum" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}serialNum"/>
 *         &lt;element name="status" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}status"/>
 *         &lt;element name="reasonCode" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}reasonCode"/>
 *         &lt;element name="reason" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}reason"/>
 *         &lt;element name="lastEventName" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}lastEventName"/>
 *         &lt;element name="lastEventDisposition" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}lastEventDisposition"/>
 *         &lt;element name="lastEventTime" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}lastEventTime"/>
 *         &lt;element name="eventLotNumber" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}eventLotNumber"/>
 *         &lt;element name="eventExpirationDate" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}eventExpirationDate"/>
 *         &lt;element name="noPrevVerificationEvents" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}noPrevVerificationEvents"/>
 *         &lt;element name="shipToAdress" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}shipToAdress" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SerialNoResponse", propOrder = {
    "messageID",
    "lpn",
    "serialNum",
    "status",
    "reasonCode",
    "reason",
    "lastEventName",
    "lastEventDisposition",
    "lastEventTime",
    "eventLotNumber",
    "eventExpirationDate",
    "noPrevVerificationEvents",
    "shipToAdress"
})
public class SerialNoResponse {

    @XmlElement(required = true, nillable = true)
    protected String messageID;
    @XmlElement(name = "LPN", required = true, nillable = true)
    protected String lpn;
    @XmlElement(required = true, nillable = true)
    protected String serialNum;
    @XmlElement(required = true, nillable = true)
    protected String status;
    @XmlElement(required = true, nillable = true)
    protected String reasonCode;
    @XmlElement(required = true, nillable = true)
    protected String reason;
    @XmlElement(required = true, nillable = true)
    protected String lastEventName;
    @XmlElement(required = true, nillable = true)
    protected String lastEventDisposition;
    @XmlElement(required = true, nillable = true)
    protected String lastEventTime;
    @XmlElement(required = true, nillable = true)
    protected String eventLotNumber;
    @XmlElement(required = true, nillable = true)
    protected String eventExpirationDate;
    @XmlElement(required = true, nillable = true)
    protected String noPrevVerificationEvents;
    @XmlElementRef(name = "shipToAdress", type = JAXBElement.class, required = false)
    protected JAXBElement<ShipToAdress> shipToAdress;

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
     * Gets the value of the serialNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialNum() {
        return serialNum;
    }

    /**
     * Sets the value of the serialNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialNum(String value) {
        this.serialNum = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode(String value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the lastEventName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastEventName() {
        return lastEventName;
    }

    /**
     * Sets the value of the lastEventName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastEventName(String value) {
        this.lastEventName = value;
    }

    /**
     * Gets the value of the lastEventDisposition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastEventDisposition() {
        return lastEventDisposition;
    }

    /**
     * Sets the value of the lastEventDisposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastEventDisposition(String value) {
        this.lastEventDisposition = value;
    }

    /**
     * Gets the value of the lastEventTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastEventTime() {
        return lastEventTime;
    }

    /**
     * Sets the value of the lastEventTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastEventTime(String value) {
        this.lastEventTime = value;
    }

    /**
     * Gets the value of the eventLotNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventLotNumber() {
        return eventLotNumber;
    }

    /**
     * Sets the value of the eventLotNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventLotNumber(String value) {
        this.eventLotNumber = value;
    }

    /**
     * Gets the value of the eventExpirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventExpirationDate() {
        return eventExpirationDate;
    }

    /**
     * Sets the value of the eventExpirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventExpirationDate(String value) {
        this.eventExpirationDate = value;
    }

    /**
     * Gets the value of the noPrevVerificationEvents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoPrevVerificationEvents() {
        return noPrevVerificationEvents;
    }

    /**
     * Sets the value of the noPrevVerificationEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoPrevVerificationEvents(String value) {
        this.noPrevVerificationEvents = value;
    }

    /**
     * Gets the value of the shipToAdress property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ShipToAdress }{@code >}
     *     
     */
    public JAXBElement<ShipToAdress> getShipToAdress() {
        return shipToAdress;
    }

    /**
     * Sets the value of the shipToAdress property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ShipToAdress }{@code >}
     *     
     */
    public void setShipToAdress(JAXBElement<ShipToAdress> value) {
        this.shipToAdress = value;
    }

}
