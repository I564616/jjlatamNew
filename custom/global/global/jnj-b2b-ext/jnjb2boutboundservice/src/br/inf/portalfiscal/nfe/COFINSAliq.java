
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for COFINSAliq complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COFINSAliq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST18" form="qualified"/>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="pCOFINS" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302" form="qualified"/>
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
@XmlType(name = "COFINSAliq", propOrder = {
    "cst",
    "vbc",
    "pcofins",
    "vcofins"
})
public class COFINSAliq {

    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(name = "vBC", required = true)
    protected String vbc;
    @XmlElement(name = "pCOFINS", required = true)
    protected String pcofins;
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
