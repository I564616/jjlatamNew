
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPI complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPI">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vIPIDevol" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPI", propOrder = {
    "vipiDevol"
})
public class IPI {

    @XmlElement(name = "vIPIDevol", required = true)
    protected String vipiDevol;

    /**
     * Gets the value of the vipiDevol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVIPIDevol() {
        return vipiDevol;
    }

    /**
     * Sets the value of the vipiDevol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVIPIDevol(String value) {
        this.vipiDevol = value;
    }

}
