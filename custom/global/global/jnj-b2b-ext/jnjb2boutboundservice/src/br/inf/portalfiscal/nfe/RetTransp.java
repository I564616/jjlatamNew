
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for retTransp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="retTransp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vServ" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vBCRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="pICMSRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302" form="qualified"/>
 *         &lt;element name="vICMSRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="CFOP" type="{http://www.portalfiscal.inf.br/nfe}TCfopTransp" form="qualified"/>
 *         &lt;element name="cMunFG" type="{http://www.portalfiscal.inf.br/nfe}TCodMunIBGE" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retTransp", propOrder = {
    "vServ",
    "vbcRet",
    "picmsRet",
    "vicmsRet",
    "cfop",
    "cMunFG"
})
public class RetTransp {

    @XmlElement(required = true)
    protected String vServ;
    @XmlElement(name = "vBCRet", required = true)
    protected String vbcRet;
    @XmlElement(name = "pICMSRet", required = true)
    protected String picmsRet;
    @XmlElement(name = "vICMSRet", required = true)
    protected String vicmsRet;
    @XmlElement(name = "CFOP", required = true)
    protected String cfop;
    @XmlElement(required = true)
    protected String cMunFG;

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
     * Gets the value of the vbcRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCRet() {
        return vbcRet;
    }

    /**
     * Sets the value of the vbcRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCRet(String value) {
        this.vbcRet = value;
    }

    /**
     * Gets the value of the picmsRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPICMSRet() {
        return picmsRet;
    }

    /**
     * Sets the value of the picmsRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPICMSRet(String value) {
        this.picmsRet = value;
    }

    /**
     * Gets the value of the vicmsRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSRet() {
        return vicmsRet;
    }

    /**
     * Sets the value of the vicmsRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSRet(String value) {
        this.vicmsRet = value;
    }

    /**
     * Gets the value of the cfop property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFOP() {
        return cfop;
    }

    /**
     * Sets the value of the cfop property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFOP(String value) {
        this.cfop = value;
    }

    /**
     * Gets the value of the cMunFG property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCMunFG() {
        return cMunFG;
    }

    /**
     * Sets the value of the cMunFG property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCMunFG(String value) {
        this.cMunFG = value;
    }

}
