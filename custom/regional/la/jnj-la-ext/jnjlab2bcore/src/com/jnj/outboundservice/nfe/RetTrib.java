
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for retTrib complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="retTrib">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vRetPIS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vRetCOFINS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vRetCSLL" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vBCIRRF" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vIRRF" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vBCRetPrev" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vRetPrev" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retTrib", propOrder = {
    "vRetPIS",
    "vRetCOFINS",
    "vRetCSLL",
    "vbcirrf",
    "virrf",
    "vbcRetPrev",
    "vRetPrev"
})
public class RetTrib {

    protected String vRetPIS;
    protected String vRetCOFINS;
    protected String vRetCSLL;
    @XmlElement(name = "vBCIRRF")
    protected String vbcirrf;
    @XmlElement(name = "vIRRF")
    protected String virrf;
    @XmlElement(name = "vBCRetPrev")
    protected String vbcRetPrev;
    protected String vRetPrev;

    /**
     * Gets the value of the vRetPIS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVRetPIS() {
        return vRetPIS;
    }

    /**
     * Sets the value of the vRetPIS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVRetPIS(String value) {
        this.vRetPIS = value;
    }

    /**
     * Gets the value of the vRetCOFINS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVRetCOFINS() {
        return vRetCOFINS;
    }

    /**
     * Sets the value of the vRetCOFINS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVRetCOFINS(String value) {
        this.vRetCOFINS = value;
    }

    /**
     * Gets the value of the vRetCSLL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVRetCSLL() {
        return vRetCSLL;
    }

    /**
     * Sets the value of the vRetCSLL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVRetCSLL(String value) {
        this.vRetCSLL = value;
    }

    /**
     * Gets the value of the vbcirrf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCIRRF() {
        return vbcirrf;
    }

    /**
     * Sets the value of the vbcirrf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCIRRF(String value) {
        this.vbcirrf = value;
    }

    /**
     * Gets the value of the virrf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVIRRF() {
        return virrf;
    }

    /**
     * Sets the value of the virrf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVIRRF(String value) {
        this.virrf = value;
    }

    /**
     * Gets the value of the vbcRetPrev property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCRetPrev() {
        return vbcRetPrev;
    }

    /**
     * Sets the value of the vbcRetPrev property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCRetPrev(String value) {
        this.vbcRetPrev = value;
    }

    /**
     * Gets the value of the vRetPrev property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVRetPrev() {
        return vRetPrev;
    }

    /**
     * Sets the value of the vRetPrev property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVRetPrev(String value) {
        this.vRetPrev = value;
    }

}
