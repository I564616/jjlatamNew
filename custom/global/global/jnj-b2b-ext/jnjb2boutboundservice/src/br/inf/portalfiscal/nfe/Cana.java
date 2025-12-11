
package br.inf.portalfiscal.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cana complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cana">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="safra" type="{http://www.portalfiscal.inf.br/nfe}safra" form="qualified"/>
 *         &lt;element name="ref" type="{http://www.portalfiscal.inf.br/nfe}ref" form="qualified"/>
 *         &lt;element name="forDia" type="{http://www.portalfiscal.inf.br/nfe}forDia" maxOccurs="unbounded" form="qualified"/>
 *         &lt;element name="qTotMes" type="{http://www.portalfiscal.inf.br/nfe}TDec_1110" form="qualified"/>
 *         &lt;element name="qTotAnt" type="{http://www.portalfiscal.inf.br/nfe}TDec_1110" form="qualified"/>
 *         &lt;element name="qTotGer" type="{http://www.portalfiscal.inf.br/nfe}TDec_1110" form="qualified"/>
 *         &lt;element name="deduc" type="{http://www.portalfiscal.inf.br/nfe}deduc" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="vFor" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vTotDed" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vLiqFor" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cana", propOrder = {
    "safra",
    "ref",
    "forDia",
    "qTotMes",
    "qTotAnt",
    "qTotGer",
    "deduc",
    "vFor",
    "vTotDed",
    "vLiqFor"
})
public class Cana {

    @XmlElement(required = true)
    protected String safra;
    @XmlElement(required = true)
    protected String ref;
    @XmlElement(required = true)
    protected List<ForDia> forDia;
    @XmlElement(required = true)
    protected String qTotMes;
    @XmlElement(required = true)
    protected String qTotAnt;
    @XmlElement(required = true)
    protected String qTotGer;
    protected List<Deduc> deduc;
    @XmlElement(required = true)
    protected String vFor;
    @XmlElement(required = true)
    protected String vTotDed;
    @XmlElement(required = true)
    protected String vLiqFor;

    /**
     * Gets the value of the safra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSafra() {
        return safra;
    }

    /**
     * Sets the value of the safra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSafra(String value) {
        this.safra = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Gets the value of the forDia property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the forDia property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getForDia().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ForDia }
     * 
     * 
     */
    public List<ForDia> getForDia() {
        if (forDia == null) {
            forDia = new ArrayList<ForDia>();
        }
        return this.forDia;
    }

    /**
     * Gets the value of the qTotMes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTotMes() {
        return qTotMes;
    }

    /**
     * Sets the value of the qTotMes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTotMes(String value) {
        this.qTotMes = value;
    }

    /**
     * Gets the value of the qTotAnt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTotAnt() {
        return qTotAnt;
    }

    /**
     * Sets the value of the qTotAnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTotAnt(String value) {
        this.qTotAnt = value;
    }

    /**
     * Gets the value of the qTotGer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTotGer() {
        return qTotGer;
    }

    /**
     * Sets the value of the qTotGer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTotGer(String value) {
        this.qTotGer = value;
    }

    /**
     * Gets the value of the deduc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deduc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeduc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Deduc }
     * 
     * 
     */
    public List<Deduc> getDeduc() {
        if (deduc == null) {
            deduc = new ArrayList<Deduc>();
        }
        return this.deduc;
    }

    /**
     * Gets the value of the vFor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVFor() {
        return vFor;
    }

    /**
     * Sets the value of the vFor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVFor(String value) {
        this.vFor = value;
    }

    /**
     * Gets the value of the vTotDed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVTotDed() {
        return vTotDed;
    }

    /**
     * Sets the value of the vTotDed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVTotDed(String value) {
        this.vTotDed = value;
    }

    /**
     * Gets the value of the vLiqFor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVLiqFor() {
        return vLiqFor;
    }

    /**
     * Sets the value of the vLiqFor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVLiqFor(String value) {
        this.vLiqFor = value;
    }

}
