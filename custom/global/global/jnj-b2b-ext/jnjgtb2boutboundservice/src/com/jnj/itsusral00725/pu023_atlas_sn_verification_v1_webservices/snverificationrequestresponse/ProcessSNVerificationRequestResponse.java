
package com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for processSNVerificationRequestResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="processSNVerificationRequestResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SerialNoResponse" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}SerialNoResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processSNVerificationRequestResponse", propOrder = {
    "serialNoResponse"
})
@XmlRootElement(name = "ProcessSNVerificationRequestResponse")
public class ProcessSNVerificationRequestResponse {

    @XmlElement(name = "SerialNoResponse", required = true, nillable = true)
    protected SerialNoResponse serialNoResponse;

    /**
     * Gets the value of the serialNoResponse property.
     * 
     * @return
     *     possible object is
     *     {@link SerialNoResponse }
     *     
     */
    public SerialNoResponse getSerialNoResponse() {
        return serialNoResponse;
    }

    /**
     * Sets the value of the serialNoResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link SerialNoResponse }
     *     
     */
    public void setSerialNoResponse(SerialNoResponse value) {
        this.serialNoResponse = value;
    }

}
