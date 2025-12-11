
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for procRef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="procRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nProc" type="{http://www.portalfiscal.inf.br/nfe}nProc" form="qualified"/>
 *         &lt;element name="indProc" type="{http://www.portalfiscal.inf.br/nfe}indProc" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "procRef", propOrder = {
    "nProc",
    "indProc"
})
public class ProcRef {

    @XmlElement(required = true)
    protected String nProc;
    @XmlElement(required = true)
    protected String indProc;

    /**
     * Gets the value of the nProc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNProc() {
        return nProc;
    }

    /**
     * Sets the value of the nProc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNProc(String value) {
        this.nProc = value;
    }

    /**
     * Gets the value of the indProc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndProc() {
        return indProc;
    }

    /**
     * Sets the value of the indProc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndProc(String value) {
        this.indProc = value;
    }

}
