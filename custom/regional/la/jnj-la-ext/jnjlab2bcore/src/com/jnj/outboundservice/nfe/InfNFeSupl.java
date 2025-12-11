
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for infNFeSupl complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="infNFeSupl">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qrCode" type="{http://www.portalfiscal.inf.br/nfe}qrCode" form="qualified"/>
 *         &lt;element name="urlChave" type="{http://www.portalfiscal.inf.br/nfe}urlChave" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "infNFeSupl", propOrder = {
    "qrCode",
    "urlChave"
})
public class InfNFeSupl {

    @XmlElement(required = true)
    protected String qrCode;
    @XmlElement(required = true)
    protected String urlChave;

    /**
     * Gets the value of the qrCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Sets the value of the qrCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQrCode(String value) {
        this.qrCode = value;
    }

    /**
     * Gets the value of the urlChave property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlChave() {
        return urlChave;
    }

    /**
     * Sets the value of the urlChave property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlChave(String value) {
        this.urlChave = value;
    }

}
