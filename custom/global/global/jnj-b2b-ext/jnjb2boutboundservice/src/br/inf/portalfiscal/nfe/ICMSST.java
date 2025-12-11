
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMSST complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMSST">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST11" form="qualified"/>
 *         &lt;element name="vBCSTRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vICMSSTRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vBCSTDest" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vICMSSTDest" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMSST", propOrder = {
    "orig",
    "cst",
    "vbcstRet",
    "vicmsstRet",
    "vbcstDest",
    "vicmsstDest"
})
public class ICMSST {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(name = "vBCSTRet", required = true)
    protected String vbcstRet;
    @XmlElement(name = "vICMSSTRet", required = true)
    protected String vicmsstRet;
    @XmlElement(name = "vBCSTDest", required = true)
    protected String vbcstDest;
    @XmlElement(name = "vICMSSTDest", required = true)
    protected String vicmsstDest;

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
     * Gets the value of the vbcstRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCSTRet() {
        return vbcstRet;
    }

    /**
     * Sets the value of the vbcstRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCSTRet(String value) {
        this.vbcstRet = value;
    }

    /**
     * Gets the value of the vicmsstRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSSTRet() {
        return vicmsstRet;
    }

    /**
     * Sets the value of the vicmsstRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSSTRet(String value) {
        this.vicmsstRet = value;
    }

    /**
     * Gets the value of the vbcstDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCSTDest() {
        return vbcstDest;
    }

    /**
     * Sets the value of the vbcstDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCSTDest(String value) {
        this.vbcstDest = value;
    }

    /**
     * Gets the value of the vicmsstDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSSTDest() {
        return vicmsstDest;
    }

    /**
     * Sets the value of the vicmsstDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSSTDest(String value) {
        this.vicmsstDest = value;
    }

}
