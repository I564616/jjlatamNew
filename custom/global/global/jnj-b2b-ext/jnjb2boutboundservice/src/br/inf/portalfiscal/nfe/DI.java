
package br.inf.portalfiscal.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DI complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DI">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nDI" type="{http://www.portalfiscal.inf.br/nfe}nDI" form="qualified"/>
 *         &lt;element name="dDI" type="{http://www.portalfiscal.inf.br/nfe}TData" form="qualified"/>
 *         &lt;element name="xLocDesemb" type="{http://www.portalfiscal.inf.br/nfe}xLocDesemb" form="qualified"/>
 *         &lt;element name="UFDesemb" type="{http://www.portalfiscal.inf.br/nfe}TUfEmi" form="qualified"/>
 *         &lt;element name="dDesemb" type="{http://www.portalfiscal.inf.br/nfe}TData" form="qualified"/>
 *         &lt;element name="cExportador" type="{http://www.portalfiscal.inf.br/nfe}cExportador" form="qualified"/>
 *         &lt;element name="adi" type="{http://www.portalfiscal.inf.br/nfe}adi" maxOccurs="unbounded" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DI", propOrder = {
    "ndi",
    "ddi",
    "xLocDesemb",
    "ufDesemb",
    "dDesemb",
    "cExportador",
    "adi"
})
public class DI {

    @XmlElement(name = "nDI", required = true)
    protected String ndi;
    @XmlElement(name = "dDI", required = true)
    protected String ddi;
    @XmlElement(required = true)
    protected String xLocDesemb;
    @XmlElement(name = "UFDesemb", required = true)
    protected TUfEmi ufDesemb;
    @XmlElement(required = true)
    protected String dDesemb;
    @XmlElement(required = true)
    protected String cExportador;
    @XmlElement(required = true)
    protected List<Adi> adi;

    /**
     * Gets the value of the ndi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNDI() {
        return ndi;
    }

    /**
     * Sets the value of the ndi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNDI(String value) {
        this.ndi = value;
    }

    /**
     * Gets the value of the ddi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDDI() {
        return ddi;
    }

    /**
     * Sets the value of the ddi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDDI(String value) {
        this.ddi = value;
    }

    /**
     * Gets the value of the xLocDesemb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXLocDesemb() {
        return xLocDesemb;
    }

    /**
     * Sets the value of the xLocDesemb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXLocDesemb(String value) {
        this.xLocDesemb = value;
    }

    /**
     * Gets the value of the ufDesemb property.
     * 
     * @return
     *     possible object is
     *     {@link TUfEmi }
     *     
     */
    public TUfEmi getUFDesemb() {
        return ufDesemb;
    }

    /**
     * Sets the value of the ufDesemb property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUfEmi }
     *     
     */
    public void setUFDesemb(TUfEmi value) {
        this.ufDesemb = value;
    }

    /**
     * Gets the value of the dDesemb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDDesemb() {
        return dDesemb;
    }

    /**
     * Sets the value of the dDesemb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDDesemb(String value) {
        this.dDesemb = value;
    }

    /**
     * Gets the value of the cExportador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCExportador() {
        return cExportador;
    }

    /**
     * Sets the value of the cExportador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCExportador(String value) {
        this.cExportador = value;
    }

    /**
     * Gets the value of the adi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the adi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdi().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Adi }
     * 
     * 
     */
    public List<Adi> getAdi() {
        if (adi == null) {
            adi = new ArrayList<Adi>();
        }
        return this.adi;
    }

}
