
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPITrib complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPITrib">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST12" form="qualified"/>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="pIPI" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302" minOccurs="0" form="qualified"/>
 *         &lt;element name="qUnid" type="{http://www.portalfiscal.inf.br/nfe}TDec_1204" minOccurs="0" form="qualified"/>
 *         &lt;element name="vUnid" type="{http://www.portalfiscal.inf.br/nfe}TDec_1104" minOccurs="0" form="qualified"/>
 *         &lt;element name="vIPI" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPITrib", propOrder = {
    "cst",
    "vbc",
    "pipi",
    "qUnid",
    "vUnid",
    "vipi"
})
public class IPITrib {

    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(name = "vBC")
    protected String vbc;
    @XmlElement(name = "pIPI")
    protected String pipi;
    protected String qUnid;
    protected String vUnid;
    @XmlElement(name = "vIPI", required = true)
    protected String vipi;

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
     * Gets the value of the pipi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPIPI() {
        return pipi;
    }

    /**
     * Sets the value of the pipi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPIPI(String value) {
        this.pipi = value;
    }

    /**
     * Gets the value of the qUnid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQUnid() {
        return qUnid;
    }

    /**
     * Sets the value of the qUnid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQUnid(String value) {
        this.qUnid = value;
    }

    /**
     * Gets the value of the vUnid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVUnid() {
        return vUnid;
    }

    /**
     * Sets the value of the vUnid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVUnid(String value) {
        this.vUnid = value;
    }

    /**
     * Gets the value of the vipi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVIPI() {
        return vipi;
    }

    /**
     * Sets the value of the vipi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVIPI(String value) {
        this.vipi = value;
    }

}
