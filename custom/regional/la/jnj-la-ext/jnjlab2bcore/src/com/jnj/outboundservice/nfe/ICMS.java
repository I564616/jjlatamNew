
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ICMS00" type="{http://www.portalfiscal.inf.br/nfe}ICMS00" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS10" type="{http://www.portalfiscal.inf.br/nfe}ICMS10" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS20" type="{http://www.portalfiscal.inf.br/nfe}ICMS20" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS30" type="{http://www.portalfiscal.inf.br/nfe}ICMS30" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS40" type="{http://www.portalfiscal.inf.br/nfe}ICMS40" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS51" type="{http://www.portalfiscal.inf.br/nfe}ICMS51" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS60" type="{http://www.portalfiscal.inf.br/nfe}ICMS60" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS70" type="{http://www.portalfiscal.inf.br/nfe}ICMS70" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMS90" type="{http://www.portalfiscal.inf.br/nfe}ICMS90" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSPart" type="{http://www.portalfiscal.inf.br/nfe}ICMSPart" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSST" type="{http://www.portalfiscal.inf.br/nfe}ICMSST" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSSN101" type="{http://www.portalfiscal.inf.br/nfe}ICMSSN101" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSSN102" type="{http://www.portalfiscal.inf.br/nfe}ICMSSN102" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSSN201" type="{http://www.portalfiscal.inf.br/nfe}ICMSSN201" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSSN202" type="{http://www.portalfiscal.inf.br/nfe}ICMSSN202" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSSN500" type="{http://www.portalfiscal.inf.br/nfe}ICMSSN500" minOccurs="0" form="qualified"/>
 *         &lt;element name="ICMSSN900" type="{http://www.portalfiscal.inf.br/nfe}ICMSSN900" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMS", propOrder = {
    "icms00",
    "icms10",
    "icms20",
    "icms30",
    "icms40",
    "icms51",
    "icms60",
    "icms70",
    "icms90",
    "icmsPart",
    "icmsst",
    "icmssn101",
    "icmssn102",
    "icmssn201",
    "icmssn202",
    "icmssn500",
    "icmssn900"
})
public class ICMS {

    @XmlElement(name = "ICMS00")
    protected ICMS00 icms00;
    @XmlElement(name = "ICMS10")
    protected ICMS10 icms10;
    @XmlElement(name = "ICMS20")
    protected ICMS20 icms20;
    @XmlElement(name = "ICMS30")
    protected ICMS30 icms30;
    @XmlElement(name = "ICMS40")
    protected ICMS40 icms40;
    @XmlElement(name = "ICMS51")
    protected ICMS51 icms51;
    @XmlElement(name = "ICMS60")
    protected ICMS60 icms60;
    @XmlElement(name = "ICMS70")
    protected ICMS70 icms70;
    @XmlElement(name = "ICMS90")
    protected ICMS90 icms90;
    @XmlElement(name = "ICMSPart")
    protected ICMSPart icmsPart;
    @XmlElement(name = "ICMSST")
    protected ICMSST icmsst;
    @XmlElement(name = "ICMSSN101")
    protected ICMSSN101 icmssn101;
    @XmlElement(name = "ICMSSN102")
    protected ICMSSN102 icmssn102;
    @XmlElement(name = "ICMSSN201")
    protected ICMSSN201 icmssn201;
    @XmlElement(name = "ICMSSN202")
    protected ICMSSN202 icmssn202;
    @XmlElement(name = "ICMSSN500")
    protected ICMSSN500 icmssn500;
    @XmlElement(name = "ICMSSN900")
    protected ICMSSN900 icmssn900;

    /**
     * Gets the value of the icms00 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS00 }
     *     
     */
    public ICMS00 getICMS00() {
        return icms00;
    }

    /**
     * Sets the value of the icms00 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS00 }
     *     
     */
    public void setICMS00(ICMS00 value) {
        this.icms00 = value;
    }

    /**
     * Gets the value of the icms10 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS10 }
     *     
     */
    public ICMS10 getICMS10() {
        return icms10;
    }

    /**
     * Sets the value of the icms10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS10 }
     *     
     */
    public void setICMS10(ICMS10 value) {
        this.icms10 = value;
    }

    /**
     * Gets the value of the icms20 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS20 }
     *     
     */
    public ICMS20 getICMS20() {
        return icms20;
    }

    /**
     * Sets the value of the icms20 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS20 }
     *     
     */
    public void setICMS20(ICMS20 value) {
        this.icms20 = value;
    }

    /**
     * Gets the value of the icms30 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS30 }
     *     
     */
    public ICMS30 getICMS30() {
        return icms30;
    }

    /**
     * Sets the value of the icms30 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS30 }
     *     
     */
    public void setICMS30(ICMS30 value) {
        this.icms30 = value;
    }

    /**
     * Gets the value of the icms40 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS40 }
     *     
     */
    public ICMS40 getICMS40() {
        return icms40;
    }

    /**
     * Sets the value of the icms40 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS40 }
     *     
     */
    public void setICMS40(ICMS40 value) {
        this.icms40 = value;
    }

    /**
     * Gets the value of the icms51 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS51 }
     *     
     */
    public ICMS51 getICMS51() {
        return icms51;
    }

    /**
     * Sets the value of the icms51 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS51 }
     *     
     */
    public void setICMS51(ICMS51 value) {
        this.icms51 = value;
    }

    /**
     * Gets the value of the icms60 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS60 }
     *     
     */
    public ICMS60 getICMS60() {
        return icms60;
    }

    /**
     * Sets the value of the icms60 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS60 }
     *     
     */
    public void setICMS60(ICMS60 value) {
        this.icms60 = value;
    }

    /**
     * Gets the value of the icms70 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS70 }
     *     
     */
    public ICMS70 getICMS70() {
        return icms70;
    }

    /**
     * Sets the value of the icms70 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS70 }
     *     
     */
    public void setICMS70(ICMS70 value) {
        this.icms70 = value;
    }

    /**
     * Gets the value of the icms90 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMS90 }
     *     
     */
    public ICMS90 getICMS90() {
        return icms90;
    }

    /**
     * Sets the value of the icms90 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMS90 }
     *     
     */
    public void setICMS90(ICMS90 value) {
        this.icms90 = value;
    }

    /**
     * Gets the value of the icmsPart property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSPart }
     *     
     */
    public ICMSPart getICMSPart() {
        return icmsPart;
    }

    /**
     * Sets the value of the icmsPart property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSPart }
     *     
     */
    public void setICMSPart(ICMSPart value) {
        this.icmsPart = value;
    }

    /**
     * Gets the value of the icmsst property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSST }
     *     
     */
    public ICMSST getICMSST() {
        return icmsst;
    }

    /**
     * Sets the value of the icmsst property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSST }
     *     
     */
    public void setICMSST(ICMSST value) {
        this.icmsst = value;
    }

    /**
     * Gets the value of the icmssn101 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSSN101 }
     *     
     */
    public ICMSSN101 getICMSSN101() {
        return icmssn101;
    }

    /**
     * Sets the value of the icmssn101 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSSN101 }
     *     
     */
    public void setICMSSN101(ICMSSN101 value) {
        this.icmssn101 = value;
    }

    /**
     * Gets the value of the icmssn102 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSSN102 }
     *     
     */
    public ICMSSN102 getICMSSN102() {
        return icmssn102;
    }

    /**
     * Sets the value of the icmssn102 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSSN102 }
     *     
     */
    public void setICMSSN102(ICMSSN102 value) {
        this.icmssn102 = value;
    }

    /**
     * Gets the value of the icmssn201 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSSN201 }
     *     
     */
    public ICMSSN201 getICMSSN201() {
        return icmssn201;
    }

    /**
     * Sets the value of the icmssn201 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSSN201 }
     *     
     */
    public void setICMSSN201(ICMSSN201 value) {
        this.icmssn201 = value;
    }

    /**
     * Gets the value of the icmssn202 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSSN202 }
     *     
     */
    public ICMSSN202 getICMSSN202() {
        return icmssn202;
    }

    /**
     * Sets the value of the icmssn202 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSSN202 }
     *     
     */
    public void setICMSSN202(ICMSSN202 value) {
        this.icmssn202 = value;
    }

    /**
     * Gets the value of the icmssn500 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSSN500 }
     *     
     */
    public ICMSSN500 getICMSSN500() {
        return icmssn500;
    }

    /**
     * Sets the value of the icmssn500 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSSN500 }
     *     
     */
    public void setICMSSN500(ICMSSN500 value) {
        this.icmssn500 = value;
    }

    /**
     * Gets the value of the icmssn900 property.
     * 
     * @return
     *     possible object is
     *     {@link ICMSSN900 }
     *     
     */
    public ICMSSN900 getICMSSN900() {
        return icmssn900;
    }

    /**
     * Sets the value of the icmssn900 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMSSN900 }
     *     
     */
    public void setICMSSN900(ICMSSN900 value) {
        this.icmssn900 = value;
    }

}
