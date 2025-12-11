
package com.jnj.outboundservice.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for imposto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="imposto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vTotTrib" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS" type="{http://www.portalfiscal.inf.br/nfe}ICMS" minOccurs="0" form="qualified"/>
 *         &lt;element name="IPI" type="{http://www.portalfiscal.inf.br/nfe}TIpi" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="II" type="{http://www.portalfiscal.inf.br/nfe}II" minOccurs="0" form="qualified"/>
 *         &lt;element name="ISSQN" type="{http://www.portalfiscal.inf.br/nfe}ISSQN" minOccurs="0" form="qualified"/>
 *         &lt;element name="PIS" type="{http://www.portalfiscal.inf.br/nfe}PIS" minOccurs="0" form="qualified"/>
 *         &lt;element name="PISST" type="{http://www.portalfiscal.inf.br/nfe}PISST" minOccurs="0" form="qualified"/>
 *         &lt;element name="COFINS" type="{http://www.portalfiscal.inf.br/nfe}COFINS" minOccurs="0" form="qualified"/>
 *         &lt;element name="COFINSST" type="{http://www.portalfiscal.inf.br/nfe}COFINSST" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSUFDest" type="{http://www.portalfiscal.inf.br/nfe}ICMSUFDest" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "imposto", propOrder = {
    "vTotTrib",
    "icms",
    "ipi",
    "ii",
    "issqn",
    "pis",
    "pisst",
    "cofins",
    "cofinsst",
    "icmsufDest"
})
public class Imposto {

    protected String vTotTrib;
    @XmlElement(name = "ICMS")
    protected ICMS icms;
    @XmlElement(name = "IPI")
    protected List<TIpi> ipi;
    @XmlElement(name = "II")
    protected II ii;
    @XmlElement(name = "ISSQN")
    protected ISSQN issqn;
    @XmlElement(name = "PIS")
    protected PIS pis;
    @XmlElement(name = "PISST")
    protected PISST pisst;
    @XmlElement(name = "COFINS")
    protected COFINS cofins;
    @XmlElement(name = "COFINSST")
    protected COFINSST cofinsst;
    @XmlElement(name = "ICMSUFDest")
    protected ICMSUFDest icmsufDest;

    /**
     * Gets the value of the vTotTrib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVTotTrib() {
        return vTotTrib;
    }

    /**
     * Sets the value of the vTotTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVTotTrib(String value) {
        this.vTotTrib = value;
    }

    /**
     * Gets the value of the icms property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS }
     *     
     */
    public ICMS getICMS() {
        return icms;
    }

    /**
     * Sets the value of the icms property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS }
     *     
     */
    public void setICMS(ICMS value) {
        this.icms = value;
    }

    /**
     * Gets the value of the ipi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ipi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIPI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TIpi }
     * 
     * 
     */
    public List<TIpi> getIPI() {
        if (ipi == null) {
            ipi = new ArrayList<TIpi>();
        }
        return this.ipi;
    }

    /**
     * Gets the value of the ii property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getII() {
        return ii;
    }

    /**
     * Sets the value of the ii property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setII(II value) {
        this.ii = value;
    }

    /**
     * Gets the value of the issqn property.
     * 
     * @return
     *     possible object is
     *     {@link ISSQN }
     *     
     */
    public ISSQN getISSQN() {
        return issqn;
    }

    /**
     * Sets the value of the issqn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ISSQN }
     *     
     */
    public void setISSQN(ISSQN value) {
        this.issqn = value;
    }

    /**
     * Gets the value of the pis property.
     * 
     * @return
     *     possible object is
     *     {@link PIS }
     *     
     */
    public PIS getPIS() {
        return pis;
    }

    /**
     * Sets the value of the pis property.
     * 
     * @param value
     *     allowed object is
     *     {@link PIS }
     *     
     */
    public void setPIS(PIS value) {
        this.pis = value;
    }

    /**
     * Gets the value of the pisst property.
     * 
     * @return
     *     possible object is
     *     {@link PISST }
     *     
     */
    public PISST getPISST() {
        return pisst;
    }

    /**
     * Sets the value of the pisst property.
     * 
     * @param value
     *     allowed object is
     *     {@link PISST }
     *     
     */
    public void setPISST(PISST value) {
        this.pisst = value;
    }

    /**
     * Gets the value of the cofins property.
     * 
     * @return
     *     possible object is
     *     {@link COFINS }
     *     
     */
    public COFINS getCOFINS() {
        return cofins;
    }

    /**
     * Sets the value of the cofins property.
     * 
     * @param value
     *     allowed object is
     *     {@link COFINS }
     *     
     */
    public void setCOFINS(COFINS value) {
        this.cofins = value;
    }

    /**
     * Gets the value of the cofinsst property.
     * 
     * @return
     *     possible object is
     *     {@link COFINSST }
     *     
     */
    public COFINSST getCOFINSST() {
        return cofinsst;
    }

    /**
     * Sets the value of the cofinsst property.
     * 
     * @param value
     *     allowed object is
     *     {@link COFINSST }
     *     
     */
    public void setCOFINSST(COFINSST value) {
        this.cofinsst = value;
    }

    /**
     * Gets the value of the icmsufDest property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSUFDest }
     *     
     */
    public ICMSUFDest getICMSUFDest() {
        return icmsufDest;
    }

    /**
     * Sets the value of the icmsufDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSUFDest }
     *     
     */
    public void setICMSUFDest(ICMSUFDest value) {
        this.icmsufDest = value;
    }

}
