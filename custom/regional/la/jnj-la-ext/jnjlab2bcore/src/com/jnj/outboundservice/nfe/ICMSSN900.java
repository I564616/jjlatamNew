
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMSSN900 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMSSN900">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CSOSN" type="{http://www.portalfiscal.inf.br/nfe}CSOSN6" form="qualified"/>
 *         &lt;element name="modBC" type="{http://www.portalfiscal.inf.br/nfe}modBC8" minOccurs="0" form="qualified"/>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pRedBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="pICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" minOccurs="0" form="qualified"/>
 *         &lt;element name="vICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="modBCST" type="{http://www.portalfiscal.inf.br/nfe}modBCST8" minOccurs="0" form="qualified"/>
 *         &lt;element name="pMVAST" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="pRedBCST" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vBCST" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pICMSST" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" minOccurs="0" form="qualified"/>
 *         &lt;element name="vICMSST" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="vBCFCPST" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pFCPST" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vFCPST" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pCredSN" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" minOccurs="0" form="qualified"/>
 *         &lt;element name="vCredICMSSN" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMSSN900", propOrder = {
    "orig",
    "csosn",
    "modBC",
    "vbc",
    "pRedBC",
    "picms",
    "vicms",
    "modBCST",
    "pmvast",
    "pRedBCST",
    "vbcst",
    "picmsst",
    "vicmsst",
    "vbcfcpst",
    "pfcpst",
    "vfcpst",
    "pCredSN",
    "vCredICMSSN"
})
public class ICMSSN900 {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CSOSN", required = true)
    protected String csosn;
    protected String modBC;
    @XmlElement(name = "vBC")
    protected String vbc;
    protected String pRedBC;
    @XmlElement(name = "pICMS")
    protected String picms;
    @XmlElement(name = "vICMS")
    protected String vicms;
    protected String modBCST;
    @XmlElement(name = "pMVAST")
    protected String pmvast;
    protected String pRedBCST;
    @XmlElement(name = "vBCST")
    protected String vbcst;
    @XmlElement(name = "pICMSST")
    protected String picmsst;
    @XmlElement(name = "vICMSST")
    protected String vicmsst;
    @XmlElement(name = "vBCFCPST")
    protected String vbcfcpst;
    @XmlElement(name = "pFCPST")
    protected String pfcpst;
    @XmlElement(name = "vFCPST")
    protected String vfcpst;
    protected String pCredSN;
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
     * Gets the value of the modBCST property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModBCST() {
        return modBCST;
    }

    /**
     * Sets the value of the modBCST property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModBCST(String value) {
        this.modBCST = value;
    }

    /**
     * Gets the value of the pmvast property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPMVAST() {
        return pmvast;
    }

    /**
     * Sets the value of the pmvast property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPMVAST(String value) {
        this.pmvast = value;
    }

    /**
     * Gets the value of the pRedBCST property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRedBCST() {
        return pRedBCST;
    }

    /**
     * Sets the value of the pRedBCST property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRedBCST(String value) {
        this.pRedBCST = value;
    }

    /**
     * Gets the value of the vbcst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCST() {
        return vbcst;
    }

    /**
     * Sets the value of the vbcst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCST(String value) {
        this.vbcst = value;
    }

    /**
     * Gets the value of the picmsst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPICMSST() {
        return picmsst;
    }

    /**
     * Sets the value of the picmsst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPICMSST(String value) {
        this.picmsst = value;
    }

    /**
     * Gets the value of the vicmsst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSST() {
        return vicmsst;
    }

    /**
     * Sets the value of the vicmsst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSST(String value) {
        this.vicmsst = value;
    }

    /**
     * Gets the value of the vbcfcpst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCFCPST() {
        return vbcfcpst;
    }

    /**
     * Sets the value of the vbcfcpst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCFCPST(String value) {
        this.vbcfcpst = value;
    }

    /**
     * Gets the value of the pfcpst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPFCPST() {
        return pfcpst;
    }

    /**
     * Sets the value of the pfcpst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPFCPST(String value) {
        this.pfcpst = value;
    }

    /**
     * Gets the value of the vfcpst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVFCPST() {
        return vfcpst;
    }

    /**
     * Sets the value of the vfcpst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVFCPST(String value) {
        this.vfcpst = value;
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
