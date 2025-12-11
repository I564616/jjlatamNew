
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for refECF complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="refECF">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mod" type="{http://www.portalfiscal.inf.br/nfe}mod3" form="qualified"/>
 *         &lt;element name="nECF" type="{http://www.portalfiscal.inf.br/nfe}nECF" form="qualified"/>
 *         &lt;element name="nCOO" type="{http://www.portalfiscal.inf.br/nfe}nCOO" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refECF", propOrder = {
    "mod",
    "necf",
    "ncoo"
})
public class RefECF {

    @XmlElement(required = true)
    protected String mod;
    @XmlElement(name = "nECF", required = true)
    protected String necf;
    @XmlElement(name = "nCOO", required = true)
    protected String ncoo;

    /**
     * Gets the value of the mod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMod() {
        return mod;
    }

    /**
     * Sets the value of the mod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMod(String value) {
        this.mod = value;
    }

    /**
     * Gets the value of the necf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNECF() {
        return necf;
    }

    /**
     * Sets the value of the necf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNECF(String value) {
        this.necf = value;
    }

    /**
     * Gets the value of the ncoo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNCOO() {
        return ncoo;
    }

    /**
     * Sets the value of the ncoo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNCOO(String value) {
        this.ncoo = value;
    }

}
