
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nDup" type="{http://www.portalfiscal.inf.br/nfe}nDup" minOccurs="0" form="qualified"/>
 *         &lt;element name="dVenc" type="{http://www.portalfiscal.inf.br/nfe}TData" minOccurs="0" form="qualified"/>
 *         &lt;element name="vDup" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dup", propOrder = {
    "nDup",
    "dVenc",
    "vDup"
})
public class Dup {

    protected String nDup;
    protected String dVenc;
    @XmlElement(required = true)
    protected String vDup;

    /**
     * Gets the value of the nDup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNDup() {
        return nDup;
    }

    /**
     * Sets the value of the nDup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNDup(String value) {
        this.nDup = value;
    }

    /**
     * Gets the value of the dVenc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDVenc() {
        return dVenc;
    }

    /**
     * Sets the value of the dVenc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDVenc(String value) {
        this.dVenc = value;
    }

    /**
     * Gets the value of the vDup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDup() {
        return vDup;
    }

    /**
     * Sets the value of the vDup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDup(String value) {
        this.vDup = value;
    }

}
