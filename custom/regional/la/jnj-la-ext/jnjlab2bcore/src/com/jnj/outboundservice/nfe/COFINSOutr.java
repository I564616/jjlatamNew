
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for COFINSOutr complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COFINSOutr">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST21" form="qualified"/>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pCOFINS" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" minOccurs="0" form="qualified"/>
 *         &lt;element name="qBCProd" type="{http://www.portalfiscal.inf.br/nfe}TDec_1204v" minOccurs="0" form="qualified"/>
 *         &lt;element name="vAliqProd" type="{http://www.portalfiscal.inf.br/nfe}TDec_1104v" minOccurs="0" form="qualified"/>
 *         &lt;element name="vCOFINS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COFINSOutr", propOrder = {
    "cst",
    "vbc",
    "pcofins",
    "qbcProd",
    "vAliqProd",
    "vcofins"
})
public class COFINSOutr {

    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(name = "vBC")
    protected String vbc;
    @XmlElement(name = "pCOFINS")
    protected String pcofins;
    @XmlElement(name = "qBCProd")
    protected String qbcProd;
    protected String vAliqProd;
    @XmlElement(name = "vCOFINS", required = true)
    protected String vcofins;

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
     * Gets the value of the pcofins property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCOFINS() {
        return pcofins;
    }

    /**
     * Sets the value of the pcofins property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCOFINS(String value) {
        this.pcofins = value;
    }

    /**
     * Gets the value of the qbcProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQBCProd() {
        return qbcProd;
    }

    /**
     * Sets the value of the qbcProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQBCProd(String value) {
        this.qbcProd = value;
    }

    /**
     * Gets the value of the vAliqProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVAliqProd() {
        return vAliqProd;
    }

    /**
     * Sets the value of the vAliqProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVAliqProd(String value) {
        this.vAliqProd = value;
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

}
