
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for encerrante complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="encerrante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nBico" type="{http://www.portalfiscal.inf.br/nfe}nBico" form="qualified"/>
 *         &lt;element name="nBomba" type="{http://www.portalfiscal.inf.br/nfe}nBomba" minOccurs="0" form="qualified"/>
 *         &lt;element name="nTanque" type="{http://www.portalfiscal.inf.br/nfe}nTanque" form="qualified"/>
 *         &lt;element name="vEncIni" type="{http://www.portalfiscal.inf.br/nfe}TDec_1203" form="qualified"/>
 *         &lt;element name="vEncFin" type="{http://www.portalfiscal.inf.br/nfe}TDec_1203" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "encerrante", propOrder = {
    "nBico",
    "nBomba",
    "nTanque",
    "vEncIni",
    "vEncFin"
})
public class Encerrante {

    @XmlElement(required = true)
    protected String nBico;
    protected String nBomba;
    @XmlElement(required = true)
    protected String nTanque;
    @XmlElement(required = true)
    protected String vEncIni;
    @XmlElement(required = true)
    protected String vEncFin;

    /**
     * Gets the value of the nBico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNBico() {
        return nBico;
    }

    /**
     * Sets the value of the nBico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNBico(String value) {
        this.nBico = value;
    }

    /**
     * Gets the value of the nBomba property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNBomba() {
        return nBomba;
    }

    /**
     * Sets the value of the nBomba property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNBomba(String value) {
        this.nBomba = value;
    }

    /**
     * Gets the value of the nTanque property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNTanque() {
        return nTanque;
    }

    /**
     * Sets the value of the nTanque property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNTanque(String value) {
        this.nTanque = value;
    }

    /**
     * Gets the value of the vEncIni property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVEncIni() {
        return vEncIni;
    }

    /**
     * Sets the value of the vEncIni property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVEncIni(String value) {
        this.vEncIni = value;
    }

    /**
     * Gets the value of the vEncFin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVEncFin() {
        return vEncFin;
    }

    /**
     * Sets the value of the vEncFin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVEncFin(String value) {
        this.vEncFin = value;
    }

}
