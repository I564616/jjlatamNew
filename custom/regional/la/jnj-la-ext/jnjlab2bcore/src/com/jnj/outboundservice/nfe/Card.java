
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for card complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="card">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tpIntegra" type="{http://www.portalfiscal.inf.br/nfe}tpIntegra" form="qualified"/>
 *         &lt;element name="CNPJ" type="{http://www.portalfiscal.inf.br/nfe}TCnpj" minOccurs="0" form="qualified"/>
 *         &lt;element name="tBand" type="{http://www.portalfiscal.inf.br/nfe}tBand" minOccurs="0" form="qualified"/>
 *         &lt;element name="cAut" type="{http://www.portalfiscal.inf.br/nfe}cAut" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "card", propOrder = {
    "tpIntegra",
    "cnpj",
    "tBand",
    "cAut"
})
public class Card {

    @XmlElement(required = true)
    protected String tpIntegra;
    @XmlElement(name = "CNPJ")
    protected String cnpj;
    protected String tBand;
    protected String cAut;

    /**
     * Gets the value of the tpIntegra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpIntegra() {
        return tpIntegra;
    }

    /**
     * Sets the value of the tpIntegra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpIntegra(String value) {
        this.tpIntegra = value;
    }

    /**
     * Gets the value of the cnpj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNPJ() {
        return cnpj;
    }

    /**
     * Sets the value of the cnpj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNPJ(String value) {
        this.cnpj = value;
    }

    /**
     * Gets the value of the tBand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTBand() {
        return tBand;
    }

    /**
     * Sets the value of the tBand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTBand(String value) {
        this.tBand = value;
    }

    /**
     * Gets the value of the cAut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAut() {
        return cAut;
    }

    /**
     * Sets the value of the cAut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAut(String value) {
        this.cAut = value;
    }

}
