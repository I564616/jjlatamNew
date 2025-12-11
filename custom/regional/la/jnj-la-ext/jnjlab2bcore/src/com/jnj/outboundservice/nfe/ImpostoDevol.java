
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for impostoDevol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="impostoDevol">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pDevol" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302Max100" form="qualified"/>
 *         &lt;element name="IPI" type="{http://www.portalfiscal.inf.br/nfe}IPI" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "impostoDevol", propOrder = {
    "pDevol",
    "ipi"
})
public class ImpostoDevol {

    @XmlElement(required = true)
    protected String pDevol;
    @XmlElement(name = "IPI", required = true)
    protected IPI ipi;

    /**
     * Gets the value of the pDevol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDevol() {
        return pDevol;
    }

    /**
     * Sets the value of the pDevol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDevol(String value) {
        this.pDevol = value;
    }

    /**
     * Gets the value of the ipi property.
     * 
     * @return
     *     possible object is
     *     {@link IPI }
     *     
     */
    public IPI getIPI() {
        return ipi;
    }

    /**
     * Sets the value of the ipi property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPI }
     *     
     */
    public void setIPI(IPI value) {
        this.ipi = value;
    }

}
