
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMSUFDest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMSUFDest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vBCUFDest" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vBCFCPUFDest" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pFCPUFDest" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" minOccurs="0" form="qualified"/>
 *         &lt;element name="pICMSUFDest" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" form="qualified"/>
 *         &lt;element name="pICMSInter" type="{http://www.portalfiscal.inf.br/nfe}pICMSInter" form="qualified"/>
 *         &lt;element name="pICMSInterPart" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" form="qualified"/>
 *         &lt;element name="vFCPUFDest" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="vICMSUFDest" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vICMSUFRemet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMSUFDest", propOrder = {
    "vbcufDest",
    "vbcfcpufDest",
    "pfcpufDest",
    "picmsufDest",
    "picmsInter",
    "picmsInterPart",
    "vfcpufDest",
    "vicmsufDest",
    "vicmsufRemet"
})
public class ICMSUFDest {

    @XmlElement(name = "vBCUFDest", required = true)
    protected String vbcufDest;
    @XmlElement(name = "vBCFCPUFDest")
    protected String vbcfcpufDest;
    @XmlElement(name = "pFCPUFDest")
    protected String pfcpufDest;
    @XmlElement(name = "pICMSUFDest", required = true)
    protected String picmsufDest;
    @XmlElement(name = "pICMSInter", required = true)
    protected String picmsInter;
    @XmlElement(name = "pICMSInterPart", required = true)
    protected String picmsInterPart;
    @XmlElement(name = "vFCPUFDest")
    protected String vfcpufDest;
    @XmlElement(name = "vICMSUFDest", required = true)
    protected String vicmsufDest;
    @XmlElement(name = "vICMSUFRemet", required = true)
    protected String vicmsufRemet;

    /**
     * Gets the value of the vbcufDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCUFDest() {
        return vbcufDest;
    }

    /**
     * Sets the value of the vbcufDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCUFDest(String value) {
        this.vbcufDest = value;
    }

    /**
     * Gets the value of the vbcfcpufDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCFCPUFDest() {
        return vbcfcpufDest;
    }

    /**
     * Sets the value of the vbcfcpufDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCFCPUFDest(String value) {
        this.vbcfcpufDest = value;
    }

    /**
     * Gets the value of the pfcpufDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPFCPUFDest() {
        return pfcpufDest;
    }

    /**
     * Sets the value of the pfcpufDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPFCPUFDest(String value) {
        this.pfcpufDest = value;
    }

    /**
     * Gets the value of the picmsufDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPICMSUFDest() {
        return picmsufDest;
    }

    /**
     * Sets the value of the picmsufDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPICMSUFDest(String value) {
        this.picmsufDest = value;
    }

    /**
     * Gets the value of the picmsInter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPICMSInter() {
        return picmsInter;
    }

    /**
     * Sets the value of the picmsInter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPICMSInter(String value) {
        this.picmsInter = value;
    }

    /**
     * Gets the value of the picmsInterPart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPICMSInterPart() {
        return picmsInterPart;
    }

    /**
     * Sets the value of the picmsInterPart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPICMSInterPart(String value) {
        this.picmsInterPart = value;
    }

    /**
     * Gets the value of the vfcpufDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVFCPUFDest() {
        return vfcpufDest;
    }

    /**
     * Sets the value of the vfcpufDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVFCPUFDest(String value) {
        this.vfcpufDest = value;
    }

    /**
     * Gets the value of the vicmsufDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSUFDest() {
        return vicmsufDest;
    }

    /**
     * Sets the value of the vicmsufDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSUFDest(String value) {
        this.vicmsufDest = value;
    }

    /**
     * Gets the value of the vicmsufRemet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSUFRemet() {
        return vicmsufRemet;
    }

    /**
     * Sets the value of the vicmsufRemet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSUFRemet(String value) {
        this.vicmsufRemet = value;
    }

}
