
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ISSQN complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ISSQN">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vAliq" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302" form="qualified"/>
 *         &lt;element name="vISSQN" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="cMunFG" type="{http://www.portalfiscal.inf.br/nfe}TCodMunIBGE" form="qualified"/>
 *         &lt;element name="cListServ" type="{http://www.portalfiscal.inf.br/nfe}TCListServ" form="qualified"/>
 *         &lt;element name="cSitTrib" type="{http://www.portalfiscal.inf.br/nfe}cSitTrib" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ISSQN", propOrder = {
    "vbc",
    "vAliq",
    "vissqn",
    "cMunFG",
    "cListServ",
    "cSitTrib"
})
public class ISSQN {

    @XmlElement(name = "vBC", required = true)
    protected String vbc;
    @XmlElement(required = true)
    protected String vAliq;
    @XmlElement(name = "vISSQN", required = true)
    protected String vissqn;
    @XmlElement(required = true)
    protected String cMunFG;
    @XmlElement(required = true)
    protected String cListServ;
    @XmlElement(required = true)
    protected CSitTrib cSitTrib;

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
     * Gets the value of the vAliq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVAliq() {
        return vAliq;
    }

    /**
     * Sets the value of the vAliq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVAliq(String value) {
        this.vAliq = value;
    }

    /**
     * Gets the value of the vissqn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVISSQN() {
        return vissqn;
    }

    /**
     * Sets the value of the vissqn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVISSQN(String value) {
        this.vissqn = value;
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

    /**
     * Gets the value of the cListServ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCListServ() {
        return cListServ;
    }

    /**
     * Sets the value of the cListServ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCListServ(String value) {
        this.cListServ = value;
    }

    /**
     * Gets the value of the cSitTrib property.
     * 
     * @return
     *     possible object is
     *     {@link CSitTrib }
     *     
     */
    public CSitTrib getCSitTrib() {
        return cSitTrib;
    }

    /**
     * Sets the value of the cSitTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link CSitTrib }
     *     
     */
    public void setCSitTrib(CSitTrib value) {
        this.cSitTrib = value;
    }

}
