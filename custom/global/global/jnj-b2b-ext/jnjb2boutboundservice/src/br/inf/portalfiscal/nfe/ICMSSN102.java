
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMSSN102 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMSSN102">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CSOSN" type="{http://www.portalfiscal.inf.br/nfe}CSOSN2" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMSSN102", propOrder = {
    "orig",
    "csosn"
})
public class ICMSSN102 {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CSOSN", required = true)
    protected String csosn;

    /**
     * Gets the value of the orig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrig() {
        return orig;
    }

    /**
     * Sets the value of the orig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrig(String value) {
        this.orig = value;
    }

    /**
     * Gets the value of the csosn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSOSN() {
        return csosn;
    }

    /**
     * Sets the value of the csosn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSOSN(String value) {
        this.csosn = value;
    }

}
