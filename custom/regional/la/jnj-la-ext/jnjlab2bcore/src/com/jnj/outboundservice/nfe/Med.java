
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for med complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="med">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cProdANVISA" type="{http://www.portalfiscal.inf.br/nfe}cProdANVISA" form="qualified"/>
 *         &lt;element name="vPMC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "med", propOrder = {
    "cProdANVISA",
    "vpmc"
})
public class Med {

    @XmlElement(required = true)
    protected String cProdANVISA;
    @XmlElement(name = "vPMC", required = true)
    protected String vpmc;

    /**
     * Gets the value of the cProdANVISA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCProdANVISA() {
        return cProdANVISA;
    }

    /**
     * Sets the value of the cProdANVISA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCProdANVISA(String value) {
        this.cProdANVISA = value;
    }

    /**
     * Gets the value of the vpmc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVPMC() {
        return vpmc;
    }

    /**
     * Sets the value of the vpmc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVPMC(String value) {
        this.vpmc = value;
    }

}
