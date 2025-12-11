
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for compra complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="compra">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xNEmp" type="{http://www.portalfiscal.inf.br/nfe}xNEmp" minOccurs="0" form="qualified"/>
 *         &lt;element name="xPed" type="{http://www.portalfiscal.inf.br/nfe}xPed2" minOccurs="0" form="qualified"/>
 *         &lt;element name="xCont" type="{http://www.portalfiscal.inf.br/nfe}xCont" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "compra", propOrder = {
    "xnEmp",
    "xPed",
    "xCont"
})
public class Compra {

    @XmlElement(name = "xNEmp")
    protected String xnEmp;
    protected String xPed;
    protected String xCont;

    /**
     * Gets the value of the xnEmp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXNEmp() {
        return xnEmp;
    }

    /**
     * Sets the value of the xnEmp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXNEmp(String value) {
        this.xnEmp = value;
    }

    /**
     * Gets the value of the xPed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXPed() {
        return xPed;
    }

    /**
     * Sets the value of the xPed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXPed(String value) {
        this.xPed = value;
    }

    /**
     * Gets the value of the xCont property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXCont() {
        return xCont;
    }

    /**
     * Sets the value of the xCont property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXCont(String value) {
        this.xCont = value;
    }

}
