
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for arma complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="arma">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tpArma" type="{http://www.portalfiscal.inf.br/nfe}tpArma" form="qualified"/>
 *         &lt;element name="nSerie" type="{http://www.portalfiscal.inf.br/nfe}nSerie2" form="qualified"/>
 *         &lt;element name="nCano" type="{http://www.portalfiscal.inf.br/nfe}nCano" form="qualified"/>
 *         &lt;element name="descr" type="{http://www.portalfiscal.inf.br/nfe}descr" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "arma", propOrder = {
    "tpArma",
    "nSerie",
    "nCano",
    "descr"
})
public class Arma {

    @XmlElement(required = true)
    protected String tpArma;
    @XmlElement(required = true)
    protected String nSerie;
    @XmlElement(required = true)
    protected String nCano;
    @XmlElement(required = true)
    protected String descr;

    /**
     * Gets the value of the tpArma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpArma() {
        return tpArma;
    }

    /**
     * Sets the value of the tpArma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpArma(String value) {
        this.tpArma = value;
    }

    /**
     * Gets the value of the nSerie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNSerie() {
        return nSerie;
    }

    /**
     * Sets the value of the nSerie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNSerie(String value) {
        this.nSerie = value;
    }

    /**
     * Gets the value of the nCano property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNCano() {
        return nCano;
    }

    /**
     * Sets the value of the nCano property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNCano(String value) {
        this.nCano = value;
    }

    /**
     * Gets the value of the descr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescr() {
        return descr;
    }

    /**
     * Sets the value of the descr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescr(String value) {
        this.descr = value;
    }

}
