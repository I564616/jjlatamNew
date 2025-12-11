
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CIDE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CIDE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qBCProd" type="{http://www.portalfiscal.inf.br/nfe}TDec_1204v" form="qualified"/>
 *         &lt;element name="vAliqProd" type="{http://www.portalfiscal.inf.br/nfe}TDec_1104" form="qualified"/>
 *         &lt;element name="vCIDE" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CIDE", propOrder = {
    "qbcProd",
    "vAliqProd",
    "vcide"
})
public class CIDE {

    @XmlElement(name = "qBCProd", required = true)
    protected String qbcProd;
    @XmlElement(required = true)
    protected String vAliqProd;
    @XmlElement(name = "vCIDE", required = true)
    protected String vcide;

    /**
     * Gets the value of the qbcProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQBCProd() {
        return qbcProd;
    }

    /**
     * Sets the value of the qbcProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQBCProd(String value) {
        this.qbcProd = value;
    }

    /**
     * Gets the value of the vAliqProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVAliqProd() {
        return vAliqProd;
    }

    /**
     * Sets the value of the vAliqProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVAliqProd(String value) {
        this.vAliqProd = value;
    }

    /**
     * Gets the value of the vcide property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVCIDE() {
        return vcide;
    }

    /**
     * Sets the value of the vcide property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVCIDE(String value) {
        this.vcide = value;
    }

}
