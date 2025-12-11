
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for exportInd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="exportInd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nRE" type="{http://www.portalfiscal.inf.br/nfe}nRE" form="qualified"/>
 *         &lt;element name="chNFe" type="{http://www.portalfiscal.inf.br/nfe}TChNFe" form="qualified"/>
 *         &lt;element name="qExport" type="{http://www.portalfiscal.inf.br/nfe}TDec_1104v" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exportInd", propOrder = {
    "nre",
    "chNFe",
    "qExport"
})
public class ExportInd {

    @XmlElement(name = "nRE", required = true)
    protected String nre;
    @XmlElement(required = true)
    protected String chNFe;
    @XmlElement(required = true)
    protected String qExport;

    /**
     * Gets the value of the nre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRE() {
        return nre;
    }

    /**
     * Sets the value of the nre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRE(String value) {
        this.nre = value;
    }

    /**
     * Gets the value of the chNFe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChNFe() {
        return chNFe;
    }

    /**
     * Sets the value of the chNFe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChNFe(String value) {
        this.chNFe = value;
    }

    /**
     * Gets the value of the qExport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQExport() {
        return qExport;
    }

    /**
     * Sets the value of the qExport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQExport(String value) {
        this.qExport = value;
    }

}
