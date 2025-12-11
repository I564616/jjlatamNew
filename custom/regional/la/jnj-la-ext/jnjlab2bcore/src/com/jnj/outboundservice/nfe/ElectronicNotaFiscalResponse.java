
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ElectronicNotaFiscalResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ElectronicNotaFiscalResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.portalfiscal.inf.br/nfe}nfeProc"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElectronicNotaFiscalResponse", namespace = "http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", propOrder = {
    "nfeProc"
})
public class ElectronicNotaFiscalResponse {

    @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe", required = true)
    protected TNfeProc nfeProc;

    /**
     * Gets the value of the nfeProc property.
     * 
     * @return
     *     possible object is
     *     {@link TNfeProc }
     *     
     */
    public TNfeProc getNfeProc() {
        return nfeProc;
    }

    /**
     * Sets the value of the nfeProc property.
     * 
     * @param value
     *     allowed object is
     *     {@link TNfeProc }
     *     
     */
    public void setNfeProc(TNfeProc value) {
        this.nfeProc = value;
    }

}
