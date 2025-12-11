
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for det complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="det">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="prod" type="{http://www.portalfiscal.inf.br/nfe}prod" form="qualified"/>
 *         &lt;element name="imposto" type="{http://www.portalfiscal.inf.br/nfe}imposto" form="qualified"/>
 *         &lt;element name="infAdProd" type="{http://www.portalfiscal.inf.br/nfe}infAdProd" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *       &lt;attribute name="nItem" use="required" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}_x0040_nItem" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "det", propOrder = {
    "prod",
    "imposto",
    "infAdProd"
})
public class Det {

    @XmlElement(required = true)
    protected Prod prod;
    @XmlElement(required = true)
    protected Imposto imposto;
    protected String infAdProd;
    @XmlAttribute(name = "nItem", required = true)
    protected String nItem;

    /**
     * Gets the value of the prod property.
     * 
     * @return
     *     possible object is
     *     {@link Prod }
     *     
     */
    public Prod getProd() {
        return prod;
    }

    /**
     * Sets the value of the prod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Prod }
     *     
     */
    public void setProd(Prod value) {
        this.prod = value;
    }

    /**
     * Gets the value of the imposto property.
     * 
     * @return
     *     possible object is
     *     {@link Imposto }
     *     
     */
    public Imposto getImposto() {
        return imposto;
    }

    /**
     * Sets the value of the imposto property.
     * 
     * @param value
     *     allowed object is
     *     {@link Imposto }
     *     
     */
    public void setImposto(Imposto value) {
        this.imposto = value;
    }

    /**
     * Gets the value of the infAdProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfAdProd() {
        return infAdProd;
    }

    /**
     * Sets the value of the infAdProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfAdProd(String value) {
        this.infAdProd = value;
    }

    /**
     * Gets the value of the nItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNItem() {
        return nItem;
    }

    /**
     * Sets the value of the nItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNItem(String value) {
        this.nItem = value;
    }

}
