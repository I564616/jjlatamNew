
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMS20 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMS20">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST3" form="qualified"/>
 *         &lt;element name="modBC" type="{http://www.portalfiscal.inf.br/nfe}modBC3" form="qualified"/>
 *         &lt;element name="pRedBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" form="qualified"/>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="pICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" form="qualified"/>
 *         &lt;element name="vICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vBCFCP" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pFCP" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vFCP" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="vICMSDeson" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="motDesICMS" type="{http://www.portalfiscal.inf.br/nfe}motDesICMS" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMS20", propOrder = {
    "orig",
    "cst",
    "modBC",
    "pRedBC",
    "vbc",
    "picms",
    "vicms",
    "vbcfcp",
    "pfcp",
    "vfcp",
    "vicmsDeson",
    "motDesICMS"
})
public class ICMS20 {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(required = true)
    protected String modBC;
    @XmlElement(required = true)
    protected String pRedBC;
    @XmlElement(name = "vBC", required = true)
    protected String vbc;
    @XmlElement(name = "pICMS", required = true)
    protected String picms;
    @XmlElement(name = "vICMS", required = true)
    protected String vicms;
    @XmlElement(name = "vBCFCP")
    protected String vbcfcp;
    @XmlElement(name = "pFCP")
    protected String pfcp;
    @XmlElement(name = "vFCP")
    protected String vfcp;
    @XmlElement(name = "vICMSDeson")
    protected String vicmsDeson;
    protected String motDesICMS;

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
     * Gets the value of the pRedBC property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRedBC() {
        return pRedBC;
    }

    /**
     * Sets the value of the pRedBC property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRedBC(String value) {
        this.pRedBC = value;
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

    /**
     * Gets the value of the vbcfcp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCFCP() {
        return vbcfcp;
    }

    /**
     * Sets the value of the vbcfcp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCFCP(String value) {
        this.vbcfcp = value;
    }

    /**
     * Gets the value of the pfcp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPFCP() {
        return pfcp;
    }

    /**
     * Sets the value of the pfcp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPFCP(String value) {
        this.pfcp = value;
    }

    /**
     * Gets the value of the vfcp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVFCP() {
        return vfcp;
    }

    /**
     * Sets the value of the vfcp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVFCP(String value) {
        this.vfcp = value;
    }

    /**
     * Gets the value of the vicmsDeson property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSDeson() {
        return vicmsDeson;
    }

    /**
     * Sets the value of the vicmsDeson property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSDeson(String value) {
        this.vicmsDeson = value;
    }

    /**
     * Gets the value of the motDesICMS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotDesICMS() {
        return motDesICMS;
    }

    /**
     * Sets the value of the motDesICMS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotDesICMS(String value) {
        this.motDesICMS = value;
    }

}
