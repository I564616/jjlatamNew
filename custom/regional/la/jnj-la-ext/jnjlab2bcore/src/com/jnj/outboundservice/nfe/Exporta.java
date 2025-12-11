
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for exporta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="exporta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UFSaidaPais" type="{http://www.portalfiscal.inf.br/nfe}TUfEmi" form="qualified"/>
 *         &lt;element name="xLocExporta" type="{http://www.portalfiscal.inf.br/nfe}xLocExporta" form="qualified"/>
 *         &lt;element name="xLocDespacho" type="{http://www.portalfiscal.inf.br/nfe}xLocDespacho" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exporta", propOrder = {
    "ufSaidaPais",
    "xLocExporta",
    "xLocDespacho"
})
public class Exporta {

    @XmlElement(name = "UFSaidaPais", required = true)
    protected TUfEmi ufSaidaPais;
    @XmlElement(required = true)
    protected String xLocExporta;
    protected String xLocDespacho;

    /**
     * Gets the value of the ufSaidaPais property.
     * 
     * @return
     *     possible object is
     *     {@link TUfEmi }
     *     
     */
    public TUfEmi getUFSaidaPais() {
        return ufSaidaPais;
    }

    /**
     * Sets the value of the ufSaidaPais property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUfEmi }
     *     
     */
    public void setUFSaidaPais(TUfEmi value) {
        this.ufSaidaPais = value;
    }

    /**
     * Gets the value of the xLocExporta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXLocExporta() {
        return xLocExporta;
    }

    /**
     * Sets the value of the xLocExporta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXLocExporta(String value) {
        this.xLocExporta = value;
    }

    /**
     * Gets the value of the xLocDespacho property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXLocDespacho() {
        return xLocDespacho;
    }

    /**
     * Sets the value of the xLocDespacho property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXLocDespacho(String value) {
        this.xLocDespacho = value;
    }

}
