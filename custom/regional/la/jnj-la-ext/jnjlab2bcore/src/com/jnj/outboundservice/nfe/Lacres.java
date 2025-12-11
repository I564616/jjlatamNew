
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lacres complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lacres">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nLacre" type="{http://www.portalfiscal.inf.br/nfe}nLacre" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lacres", propOrder = {
    "nLacre"
})
public class Lacres {

    @XmlElement(required = true)
    protected String nLacre;

    /**
     * Gets the value of the nLacre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNLacre() {
        return nLacre;
    }

    /**
     * Sets the value of the nLacre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNLacre(String value) {
        this.nLacre = value;
    }

}
