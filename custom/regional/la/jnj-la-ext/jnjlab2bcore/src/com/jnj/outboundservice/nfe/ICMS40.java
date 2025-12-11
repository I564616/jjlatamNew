
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMS40 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMS40">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST5" form="qualified"/>
 *         &lt;element name="vICMSDeson" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="motDesICMS" type="{http://www.portalfiscal.inf.br/nfe}motDesICMS3" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMS40", propOrder = {
    "orig",
    "cst",
    "vicmsDeson",
    "motDesICMS"
})
public class ICMS40 {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(name = "vICMSDeson")
    protected String vicmsDeson;
    protected String motDesICMS;

    /**
     * Gets the value of the orig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrig() {
        return orig;
    }

    /**
     * Sets the value of the orig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrig(String value) {
        this.orig = value;
    }

    /**
     * Gets the value of the cst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCST() {
        return cst;
    }

    /**
     * Sets the value of the cst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCST(String value) {
        this.cst = value;
    }

    /**
     * Gets the value of the vicmsDeson property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSDeson() {
        return vicmsDeson;
    }

    /**
     * Sets the value of the vicmsDeson property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSDeson(String value) {
        this.vicmsDeson = value;
    }

    /**
     * Gets the value of the motDesICMS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotDesICMS() {
        return motDesICMS;
    }

    /**
     * Sets the value of the motDesICMS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotDesICMS(String value) {
        this.motDesICMS = value;
    }

}
