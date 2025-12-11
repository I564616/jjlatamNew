
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for total complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="total">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ICMSTot" type="{http://www.portalfiscal.inf.br/nfe}ICMSTot" form="qualified"/>
 *         &lt;element name="ISSQNtot" type="{http://www.portalfiscal.inf.br/nfe}ISSQNtot" minOccurs="0" form="qualified"/>
 *         &lt;element name="retTrib" type="{http://www.portalfiscal.inf.br/nfe}retTrib" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "total", propOrder = {
    "icmsTot",
    "issqNtot",
    "retTrib"
})
public class Total {

    @XmlElement(name = "ICMSTot", required = true)
    protected ICMSTot icmsTot;
    @XmlElement(name = "ISSQNtot")
    protected ISSQNtot issqNtot;
    protected RetTrib retTrib;

    /**
     * Gets the value of the icmsTot property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSTot }
     *     
     */
    public ICMSTot getICMSTot() {
        return icmsTot;
    }

    /**
     * Sets the value of the icmsTot property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSTot }
     *     
     */
    public void setICMSTot(ICMSTot value) {
        this.icmsTot = value;
    }

    /**
     * Gets the value of the issqNtot property.
     * 
     * @return
     *     possible object is
     *     {@link ISSQNtot }
     *     
     */
    public ISSQNtot getISSQNtot() {
        return issqNtot;
    }

    /**
     * Sets the value of the issqNtot property.
     * 
     * @param value
     *     allowed object is
     *     {@link ISSQNtot }
     *     
     */
    public void setISSQNtot(ISSQNtot value) {
        this.issqNtot = value;
    }

    /**
     * Gets the value of the retTrib property.
     * 
     * @return
     *     possible object is
     *     {@link RetTrib }
     *     
     */
    public RetTrib getRetTrib() {
        return retTrib;
    }

    /**
     * Sets the value of the retTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetTrib }
     *     
     */
    public void setRetTrib(RetTrib value) {
        this.retTrib = value;
    }

}
