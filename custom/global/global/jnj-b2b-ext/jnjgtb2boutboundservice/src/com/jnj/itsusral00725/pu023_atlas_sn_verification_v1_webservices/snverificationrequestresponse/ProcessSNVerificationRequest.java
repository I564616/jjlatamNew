
package com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for processSNVerificationRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="processSNVerificationRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SerialNoRequest" type="{http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse}SerialNoRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processSNVerificationRequest", propOrder = {
    "serialNoRequest"
})
@XmlRootElement(name = "ProcessSNVerificationRequest")
public class ProcessSNVerificationRequest {

    @XmlElement(name = "SerialNoRequest", required = true, nillable = true)
    protected SerialNoRequest serialNoRequest;

    /**
     * Gets the value of the serialNoRequest property.
     * 
     * @return
     *     possible object is
     *     {@link SerialNoRequest }
     *     
     */
    public SerialNoRequest getSerialNoRequest() {
        return serialNoRequest;
    }

    /**
     * Sets the value of the serialNoRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link SerialNoRequest }
     *     
     */
    public void setSerialNoRequest(SerialNoRequest value) {
        this.serialNoRequest = value;
    }

}
