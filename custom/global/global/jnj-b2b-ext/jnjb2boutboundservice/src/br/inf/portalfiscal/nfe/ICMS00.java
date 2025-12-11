
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMS00 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMS00">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST" form="qualified"/>
 *         &lt;element name="modBC" type="{http://www.portalfiscal.inf.br/nfe}modBC" form="qualified"/>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="pICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302" form="qualified"/>
 *         &lt;element name="vICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMS00", propOrder = {
    "orig",
    "cst",
    "modBC",
    "vbc",
    "picms",
    "vicms"
})
public class ICMS00 {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(required = true)
    protected String modBC;
    @XmlElement(name = "vBC", required = true)
    protected String vbc;
    @XmlElement(name = "pICMS", required = true)
    protected String picms;
    @XmlElement(name = "vICMS", required = true)
    protected String vicms;

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
     * Gets the value of the cst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCST() {
        return cst;
    }

    /**
     * Sets the value of the cst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCST(String value) {
        this.cst = value;
    }

    /**
     * Gets the value of the modBC property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModBC() {
        return modBC;
    }

    /**
     * Sets the value of the modBC property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModBC(String value) {
        this.modBC = value;
    }

    /**
     * Gets the value of the vbc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBC() {
        return vbc;
    }

    /**
     * Sets the value of the vbc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBC(String value) {
        this.vbc = value;
    }

    /**
     * Gets the value of the picms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPICMS() {
        return picms;
    }

    /**
     * Sets the value of the picms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPICMS(String value) {
        this.picms = value;
    }

    /**
     * Gets the value of the vicms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMS() {
        return vicms;
    }

    /**
     * Sets the value of the vicms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMS(String value) {
        this.vicms = value;
    }

}
