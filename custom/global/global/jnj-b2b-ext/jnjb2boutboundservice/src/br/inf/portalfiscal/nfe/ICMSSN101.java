
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMSSN101 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMSSN101">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CSOSN" type="{http://www.portalfiscal.inf.br/nfe}CSOSN" form="qualified"/>
 *         &lt;element name="pCredSN" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302" form="qualified"/>
 *         &lt;element name="vCredICMSSN" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMSSN101", propOrder = {
    "orig",
    "csosn",
    "pCredSN",
    "vCredICMSSN"
})
public class ICMSSN101 {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CSOSN", required = true)
    protected String csosn;
    @XmlElement(required = true)
    protected String pCredSN;
    @XmlElement(required = true)
    protected String vCredICMSSN;

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

    /**
     * Gets the value of the pCredSN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCredSN() {
        return pCredSN;
    }

    /**
     * Sets the value of the pCredSN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCredSN(String value) {
        this.pCredSN = value;
    }

    /**
     * Gets the value of the vCredICMSSN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVCredICMSSN() {
        return vCredICMSSN;
    }

    /**
     * Sets the value of the vCredICMSSN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVCredICMSSN(String value) {
        this.vCredICMSSN = value;
    }

}
