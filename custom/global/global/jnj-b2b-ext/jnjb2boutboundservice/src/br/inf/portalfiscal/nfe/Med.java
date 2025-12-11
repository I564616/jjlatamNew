
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for med complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="med">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nLote" type="{http://www.portalfiscal.inf.br/nfe}nLote" form="qualified"/>
 *         &lt;element name="qLote" type="{http://www.portalfiscal.inf.br/nfe}TDec_0803" form="qualified"/>
 *         &lt;element name="dFab" type="{http://www.portalfiscal.inf.br/nfe}TData" form="qualified"/>
 *         &lt;element name="dVal" type="{http://www.portalfiscal.inf.br/nfe}TData" form="qualified"/>
 *         &lt;element name="vPMC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "med", propOrder = {
    "nLote",
    "qLote",
    "dFab",
    "dVal",
    "vpmc"
})
public class Med {

    @XmlElement(required = true)
    protected String nLote;
    @XmlElement(required = true)
    protected String qLote;
    @XmlElement(required = true)
    protected String dFab;
    @XmlElement(required = true)
    protected String dVal;
    @XmlElement(name = "vPMC", required = true)
    protected String vpmc;

    /**
     * Gets the value of the nLote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNLote() {
        return nLote;
    }

    /**
     * Sets the value of the nLote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNLote(String value) {
        this.nLote = value;
    }

    /**
     * Gets the value of the qLote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQLote() {
        return qLote;
    }

    /**
     * Sets the value of the qLote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQLote(String value) {
        this.qLote = value;
    }

    /**
     * Gets the value of the dFab property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDFab() {
        return dFab;
    }

    /**
     * Sets the value of the dFab property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDFab(String value) {
        this.dFab = value;
    }

    /**
     * Gets the value of the dVal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDVal() {
        return dVal;
    }

    /**
     * Sets the value of the dVal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDVal(String value) {
        this.dVal = value;
    }

    /**
     * Gets the value of the vpmc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVPMC() {
        return vpmc;
    }

    /**
     * Sets the value of the vpmc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVPMC(String value) {
        this.vpmc = value;
    }

}
