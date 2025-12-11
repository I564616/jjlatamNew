
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deduc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deduc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xDed" type="{http://www.portalfiscal.inf.br/nfe}xDed" form="qualified"/>
 *         &lt;element name="vDed" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deduc", propOrder = {
    "xDed",
    "vDed"
})
public class Deduc {

    @XmlElement(required = true)
    protected String xDed;
    @XmlElement(required = true)
    protected String vDed;

    /**
     * Gets the value of the xDed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXDed() {
        return xDed;
    }

    /**
     * Sets the value of the xDed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXDed(String value) {
        this.xDed = value;
    }

    /**
     * Gets the value of the vDed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDed() {
        return vDed;
    }

    /**
     * Sets the value of the vDed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDed(String value) {
        this.vDed = value;
    }

}
