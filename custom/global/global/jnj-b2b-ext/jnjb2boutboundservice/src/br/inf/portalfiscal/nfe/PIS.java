
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PIS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PIS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PISAliq" type="{http://www.portalfiscal.inf.br/nfe}PISAliq" minOccurs="0" form="qualified"/>
 *         &lt;element name="PISQtde" type="{http://www.portalfiscal.inf.br/nfe}PISQtde" minOccurs="0" form="qualified"/>
 *         &lt;element name="PISNT" type="{http://www.portalfiscal.inf.br/nfe}PISNT" minOccurs="0" form="qualified"/>
 *         &lt;element name="PISOutr" type="{http://www.portalfiscal.inf.br/nfe}PISOutr" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PIS", propOrder = {
    "pisAliq",
    "pisQtde",
    "pisnt",
    "pisOutr"
})
public class PIS {

    @XmlElement(name = "PISAliq")
    protected PISAliq pisAliq;
    @XmlElement(name = "PISQtde")
    protected PISQtde pisQtde;
    @XmlElement(name = "PISNT")
    protected PISNT pisnt;
    @XmlElement(name = "PISOutr")
    protected PISOutr pisOutr;

    /**
     * Gets the value of the pisAliq property.
     * 
     * @return
     *     possible object is
     *     {@link PISAliq }
     *     
     */
    public PISAliq getPISAliq() {
        return pisAliq;
    }

    /**
     * Sets the value of the pisAliq property.
     * 
     * @param value
     *     allowed object is
     *     {@link PISAliq }
     *     
     */
    public void setPISAliq(PISAliq value) {
        this.pisAliq = value;
    }

    /**
     * Gets the value of the pisQtde property.
     * 
     * @return
     *     possible object is
     *     {@link PISQtde }
     *     
     */
    public PISQtde getPISQtde() {
        return pisQtde;
    }

    /**
     * Sets the value of the pisQtde property.
     * 
     * @param value
     *     allowed object is
     *     {@link PISQtde }
     *     
     */
    public void setPISQtde(PISQtde value) {
        this.pisQtde = value;
    }

    /**
     * Gets the value of the pisnt property.
     * 
     * @return
     *     possible object is
     *     {@link PISNT }
     *     
     */
    public PISNT getPISNT() {
        return pisnt;
    }

    /**
     * Sets the value of the pisnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link PISNT }
     *     
     */
    public void setPISNT(PISNT value) {
        this.pisnt = value;
    }

    /**
     * Gets the value of the pisOutr property.
     * 
     * @return
     *     possible object is
     *     {@link PISOutr }
     *     
     */
    public PISOutr getPISOutr() {
        return pisOutr;
    }

    /**
     * Sets the value of the pisOutr property.
     * 
     * @param value
     *     allowed object is
     *     {@link PISOutr }
     *     
     */
    public void setPISOutr(PISOutr value) {
        this.pisOutr = value;
    }

}
