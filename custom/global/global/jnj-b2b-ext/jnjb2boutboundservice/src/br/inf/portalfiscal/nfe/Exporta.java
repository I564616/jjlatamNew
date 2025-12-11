
package br.inf.portalfiscal.nfe;

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
 *         &lt;element name="UFEmbarq" type="{http://www.portalfiscal.inf.br/nfe}TUf" form="qualified"/>
 *         &lt;element name="xLocEmbarq" type="{http://www.portalfiscal.inf.br/nfe}xLocEmbarq" form="qualified"/>
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
    "ufEmbarq",
    "xLocEmbarq"
})
public class Exporta {

    @XmlElement(name = "UFEmbarq", required = true)
    protected TUf ufEmbarq;
    @XmlElement(required = true)
    protected String xLocEmbarq;

    /**
     * Gets the value of the ufEmbarq property.
     * 
     * @return
     *     possible object is
     *     {@link TUf }
     *     
     */
    public TUf getUFEmbarq() {
        return ufEmbarq;
    }

    /**
     * Sets the value of the ufEmbarq property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUf }
     *     
     */
    public void setUFEmbarq(TUf value) {
        this.ufEmbarq = value;
    }

    /**
     * Gets the value of the xLocEmbarq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXLocEmbarq() {
        return xLocEmbarq;
    }

    /**
     * Sets the value of the xLocEmbarq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXLocEmbarq(String value) {
        this.xLocEmbarq = value;
    }

}
