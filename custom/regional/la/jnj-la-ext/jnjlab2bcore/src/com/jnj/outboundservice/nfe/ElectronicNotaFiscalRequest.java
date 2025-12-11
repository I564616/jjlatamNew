
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ElectronicNotaFiscalRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ElectronicNotaFiscalRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Header" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}BTBControlArea"/>
 *         &lt;element name="PayLoad" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}NFERequestData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElectronicNotaFiscalRequest", namespace = "http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", propOrder = {
    "header",
    "payLoad"
})
public class ElectronicNotaFiscalRequest {

    @XmlElement(name = "Header", namespace = "", required = true, nillable = true)
    protected BTBControlArea header;
    @XmlElement(name = "PayLoad", namespace = "", required = true, nillable = true)
    protected NFERequestData payLoad;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link BTBControlArea }
     *     
     */
    public BTBControlArea getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link BTBControlArea }
     *     
     */
    public void setHeader(BTBControlArea value) {
        this.header = value;
    }

    /**
     * Gets the value of the payLoad property.
     * 
     * @return
     *     possible object is
     *     {@link NFERequestData }
     *     
     */
    public NFERequestData getPayLoad() {
        return payLoad;
    }

    /**
     * Sets the value of the payLoad property.
     * 
     * @param value
     *     allowed object is
     *     {@link NFERequestData }
     *     
     */
    public void setPayLoad(NFERequestData value) {
        this.payLoad = value;
    }

}
