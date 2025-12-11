
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for COFINS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COFINS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COFINSAliq" type="{http://www.portalfiscal.inf.br/nfe}COFINSAliq" minOccurs="0" form="qualified"/>
 *         &lt;element name="COFINSQtde" type="{http://www.portalfiscal.inf.br/nfe}COFINSQtde" minOccurs="0" form="qualified"/>
 *         &lt;element name="COFINSNT" type="{http://www.portalfiscal.inf.br/nfe}COFINSNT" minOccurs="0" form="qualified"/>
 *         &lt;element name="COFINSOutr" type="{http://www.portalfiscal.inf.br/nfe}COFINSOutr" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COFINS", propOrder = {
    "cofinsAliq",
    "cofinsQtde",
    "cofinsnt",
    "cofinsOutr"
})
public class COFINS {

    @XmlElement(name = "COFINSAliq")
    protected COFINSAliq cofinsAliq;
    @XmlElement(name = "COFINSQtde")
    protected COFINSQtde cofinsQtde;
    @XmlElement(name = "COFINSNT")
    protected COFINSNT cofinsnt;
    @XmlElement(name = "COFINSOutr")
    protected COFINSOutr cofinsOutr;

    /**
     * Gets the value of the cofinsAliq property.
     * 
     * @return
     *     possible object is
     *     {@link COFINSAliq }
     *     
     */
    public COFINSAliq getCOFINSAliq() {
        return cofinsAliq;
    }

    /**
     * Sets the value of the cofinsAliq property.
     * 
     * @param value
     *     allowed object is
     *     {@link COFINSAliq }
     *     
     */
    public void setCOFINSAliq(COFINSAliq value) {
        this.cofinsAliq = value;
    }

    /**
     * Gets the value of the cofinsQtde property.
     * 
     * @return
     *     possible object is
     *     {@link COFINSQtde }
     *     
     */
    public COFINSQtde getCOFINSQtde() {
        return cofinsQtde;
    }

    /**
     * Sets the value of the cofinsQtde property.
     * 
     * @param value
     *     allowed object is
     *     {@link COFINSQtde }
     *     
     */
    public void setCOFINSQtde(COFINSQtde value) {
        this.cofinsQtde = value;
    }

    /**
     * Gets the value of the cofinsnt property.
     * 
     * @return
     *     possible object is
     *     {@link COFINSNT }
     *     
     */
    public COFINSNT getCOFINSNT() {
        return cofinsnt;
    }

    /**
     * Sets the value of the cofinsnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link COFINSNT }
     *     
     */
    public void setCOFINSNT(COFINSNT value) {
        this.cofinsnt = value;
    }

    /**
     * Gets the value of the cofinsOutr property.
     * 
     * @return
     *     possible object is
     *     {@link COFINSOutr }
     *     
     */
    public COFINSOutr getCOFINSOutr() {
        return cofinsOutr;
    }

    /**
     * Sets the value of the cofinsOutr property.
     * 
     * @param value
     *     allowed object is
     *     {@link COFINSOutr }
     *     
     */
    public void setCOFINSOutr(COFINSOutr value) {
        this.cofinsOutr = value;
    }

}
