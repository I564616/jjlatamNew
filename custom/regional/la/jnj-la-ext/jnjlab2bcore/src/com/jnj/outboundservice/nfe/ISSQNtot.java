
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ISSQNtot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ISSQNtot">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vServ" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vISS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vPIS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vCOFINS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="dCompet" type="{http://www.portalfiscal.inf.br/nfe}TData" form="qualified"/>
 *         &lt;element name="vDeducao" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vOutro" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vDescIncond" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vDescCond" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vISSRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="cRegTrib" type="{http://www.portalfiscal.inf.br/nfe}cRegTrib" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ISSQNtot", propOrder = {
    "vServ",
    "vbc",
    "viss",
    "vpis",
    "vcofins",
    "dCompet",
    "vDeducao",
    "vOutro",
    "vDescIncond",
    "vDescCond",
    "vissRet",
    "cRegTrib"
})
public class ISSQNtot {

    protected String vServ;
    @XmlElement(name = "vBC")
    protected String vbc;
    @XmlElement(name = "vISS")
    protected String viss;
    @XmlElement(name = "vPIS")
    protected String vpis;
    @XmlElement(name = "vCOFINS")
    protected String vcofins;
    @XmlElement(required = true)
    protected String dCompet;
    protected String vDeducao;
    protected String vOutro;
    protected String vDescIncond;
    protected String vDescCond;
    @XmlElement(name = "vISSRet")
    protected String vissRet;
    protected String cRegTrib;

    /**
     * Gets the value of the vServ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVServ() {
        return vServ;
    }

    /**
     * Sets the value of the vServ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVServ(String value) {
        this.vServ = value;
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
     * Gets the value of the viss property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVISS() {
        return viss;
    }

    /**
     * Sets the value of the viss property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVISS(String value) {
        this.viss = value;
    }

    /**
     * Gets the value of the vpis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVPIS() {
        return vpis;
    }

    /**
     * Sets the value of the vpis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVPIS(String value) {
        this.vpis = value;
    }

    /**
     * Gets the value of the vcofins property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVCOFINS() {
        return vcofins;
    }

    /**
     * Sets the value of the vcofins property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVCOFINS(String value) {
        this.vcofins = value;
    }

    /**
     * Gets the value of the dCompet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDCompet() {
        return dCompet;
    }

    /**
     * Sets the value of the dCompet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDCompet(String value) {
        this.dCompet = value;
    }

    /**
     * Gets the value of the vDeducao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDeducao() {
        return vDeducao;
    }

    /**
     * Sets the value of the vDeducao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDeducao(String value) {
        this.vDeducao = value;
    }

    /**
     * Gets the value of the vOutro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVOutro() {
        return vOutro;
    }

    /**
     * Sets the value of the vOutro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVOutro(String value) {
        this.vOutro = value;
    }

    /**
     * Gets the value of the vDescIncond property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDescIncond() {
        return vDescIncond;
    }

    /**
     * Sets the value of the vDescIncond property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDescIncond(String value) {
        this.vDescIncond = value;
    }

    /**
     * Gets the value of the vDescCond property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDescCond() {
        return vDescCond;
    }

    /**
     * Sets the value of the vDescCond property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDescCond(String value) {
        this.vDescCond = value;
    }

    /**
     * Gets the value of the vissRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVISSRet() {
        return vissRet;
    }

    /**
     * Sets the value of the vissRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVISSRet(String value) {
        this.vissRet = value;
    }

    /**
     * Gets the value of the cRegTrib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCRegTrib() {
        return cRegTrib;
    }

    /**
     * Sets the value of the cRegTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCRegTrib(String value) {
        this.cRegTrib = value;
    }

}
