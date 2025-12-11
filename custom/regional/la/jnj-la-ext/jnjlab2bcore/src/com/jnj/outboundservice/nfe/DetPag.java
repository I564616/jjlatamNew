
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for detPag complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="detPag">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="indPag" type="{http://www.portalfiscal.inf.br/nfe}indPag" minOccurs="0" form="qualified"/>
 *         &lt;element name="tPag" type="{http://www.portalfiscal.inf.br/nfe}tPag" form="qualified"/>
 *         &lt;element name="vPag" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="card" type="{http://www.portalfiscal.inf.br/nfe}card" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detPag", propOrder = {
    "indPag",
    "tPag",
    "vPag",
    "card"
})
public class DetPag {

    protected String indPag;
    @XmlElement(required = true)
    protected String tPag;
    @XmlElement(required = true)
    protected String vPag;
    protected Card card;

    /**
     * Gets the value of the indPag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndPag() {
        return indPag;
    }

    /**
     * Sets the value of the indPag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndPag(String value) {
        this.indPag = value;
    }

    /**
     * Gets the value of the tPag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTPag() {
        return tPag;
    }

    /**
     * Sets the value of the tPag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTPag(String value) {
        this.tPag = value;
    }

    /**
     * Gets the value of the vPag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVPag() {
        return vPag;
    }

    /**
     * Sets the value of the vPag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVPag(String value) {
        this.vPag = value;
    }

    /**
     * Gets the value of the card property.
     * 
     * @return
     *     possible object is
     *     {@link Card }
     *     
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the value of the card property.
     * 
     * @param value
     *     allowed object is
     *     {@link Card }
     *     
     */
    public void setCard(Card value) {
        this.card = value;
    }

}
