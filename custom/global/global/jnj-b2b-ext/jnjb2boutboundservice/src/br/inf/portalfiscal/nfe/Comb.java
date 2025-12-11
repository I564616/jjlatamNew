
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for comb complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="comb">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cProdANP" type="{http://www.portalfiscal.inf.br/nfe}TcProdANP" form="qualified"/>
 *         &lt;element name="CODIF" type="{http://www.portalfiscal.inf.br/nfe}CODIF" minOccurs="0" form="qualified"/>
 *         &lt;element name="qTemp" type="{http://www.portalfiscal.inf.br/nfe}TDec_1204Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="UFCons" type="{http://www.portalfiscal.inf.br/nfe}TUf" form="qualified"/>
 *         &lt;element name="CIDE" type="{http://www.portalfiscal.inf.br/nfe}CIDE" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comb", propOrder = {
    "cProdANP",
    "codif",
    "qTemp",
    "ufCons",
    "cide"
})
public class Comb {

    @XmlElement(required = true)
    protected String cProdANP;
    @XmlElement(name = "CODIF")
    protected String codif;
    protected String qTemp;
    @XmlElement(name = "UFCons", required = true)
    protected TUf ufCons;
    @XmlElement(name = "CIDE")
    protected CIDE cide;

    /**
     * Gets the value of the cProdANP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCProdANP() {
        return cProdANP;
    }

    /**
     * Sets the value of the cProdANP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCProdANP(String value) {
        this.cProdANP = value;
    }

    /**
     * Gets the value of the codif property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODIF() {
        return codif;
    }

    /**
     * Sets the value of the codif property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODIF(String value) {
        this.codif = value;
    }

    /**
     * Gets the value of the qTemp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTemp() {
        return qTemp;
    }

    /**
     * Sets the value of the qTemp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTemp(String value) {
        this.qTemp = value;
    }

    /**
     * Gets the value of the ufCons property.
     * 
     * @return
     *     possible object is
     *     {@link TUf }
     *     
     */
    public TUf getUFCons() {
        return ufCons;
    }

    /**
     * Sets the value of the ufCons property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUf }
     *     
     */
    public void setUFCons(TUf value) {
        this.ufCons = value;
    }

    /**
     * Gets the value of the cide property.
     * 
     * @return
     *     possible object is
     *     {@link CIDE }
     *     
     */
    public CIDE getCIDE() {
        return cide;
    }

    /**
     * Sets the value of the cide property.
     * 
     * @param value
     *     allowed object is
     *     {@link CIDE }
     *     
     */
    public void setCIDE(CIDE value) {
        this.cide = value;
    }

}
