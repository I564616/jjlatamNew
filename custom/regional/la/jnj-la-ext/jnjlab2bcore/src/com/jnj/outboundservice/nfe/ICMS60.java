
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMS60 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMS60">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST7" form="qualified"/>
 *         &lt;element name="vBCSTRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pST" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vICMSSTRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="vBCFCPSTRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pFCPSTRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vFCPSTRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMS60", propOrder = {
    "orig",
    "cst",
    "vbcstRet",
    "pst",
    "vicmsstRet",
    "vbcfcpstRet",
    "pfcpstRet",
    "vfcpstRet"
})
public class ICMS60 {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(name = "vBCSTRet")
    protected String vbcstRet;
    @XmlElement(name = "pST")
    protected String pst;
    @XmlElement(name = "vICMSSTRet")
    protected String vicmsstRet;
    @XmlElement(name = "vBCFCPSTRet")
    protected String vbcfcpstRet;
    @XmlElement(name = "pFCPSTRet")
    protected String pfcpstRet;
    @XmlElement(name = "vFCPSTRet")
    protected String vfcpstRet;

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
     * Gets the value of the pst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPST() {
        return pst;
    }

    /**
     * Sets the value of the pst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPST(String value) {
        this.pst = value;
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
     * Gets the value of the vbcfcpstRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCFCPSTRet() {
        return vbcfcpstRet;
    }

    /**
     * Sets the value of the vbcfcpstRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCFCPSTRet(String value) {
        this.vbcfcpstRet = value;
    }

    /**
     * Gets the value of the pfcpstRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPFCPSTRet() {
        return pfcpstRet;
    }

    /**
     * Sets the value of the pfcpstRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPFCPSTRet(String value) {
        this.pfcpstRet = value;
    }

    /**
     * Gets the value of the vfcpstRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVFCPSTRet() {
        return vfcpstRet;
    }

    /**
     * Sets the value of the vfcpstRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVFCPSTRet(String value) {
        this.vfcpstRet = value;
    }

}
