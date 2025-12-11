
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nFat" type="{http://www.portalfiscal.inf.br/nfe}nFat" minOccurs="0" form="qualified"/>
 *         &lt;element name="vOrig" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vDesc" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vLiq" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fat", propOrder = {
    "nFat",
    "vOrig",
    "vDesc",
    "vLiq"
})
public class Fat {

    protected String nFat;
    protected String vOrig;
    protected String vDesc;
    protected String vLiq;

    /**
     * Gets the value of the nFat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNFat() {
        return nFat;
    }

    /**
     * Sets the value of the nFat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNFat(String value) {
        this.nFat = value;
    }

    /**
     * Gets the value of the vOrig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVOrig() {
        return vOrig;
    }

    /**
     * Sets the value of the vOrig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVOrig(String value) {
        this.vOrig = value;
    }

    /**
     * Gets the value of the vDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDesc() {
        return vDesc;
    }

    /**
     * Sets the value of the vDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDesc(String value) {
        this.vDesc = value;
    }

    /**
     * Gets the value of the vLiq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVLiq() {
        return vLiq;
    }

    /**
     * Sets the value of the vLiq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVLiq(String value) {
        this.vLiq = value;
    }

}
