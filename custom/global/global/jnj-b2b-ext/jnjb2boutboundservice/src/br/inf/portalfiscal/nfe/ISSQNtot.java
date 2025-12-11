
package br.inf.portalfiscal.nfe;

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
    "vcofins"
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

}
