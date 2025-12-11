
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TVeiculo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TVeiculo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="placa" type="{http://www.portalfiscal.inf.br/nfe}placa" form="qualified"/>
 *         &lt;element name="UF" type="{http://www.portalfiscal.inf.br/nfe}TUf" form="qualified"/>
 *         &lt;element name="RNTC" type="{http://www.portalfiscal.inf.br/nfe}RNTC" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TVeiculo", propOrder = {
    "placa",
    "uf",
    "rntc"
})
public class TVeiculo {

    @XmlElement(required = true)
    protected String placa;
    @XmlElement(name = "UF", required = true)
    protected TUf uf;
    @XmlElement(name = "RNTC")
    protected String rntc;

    /**
     * Gets the value of the placa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * Sets the value of the placa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaca(String value) {
        this.placa = value;
    }

    /**
     * Gets the value of the uf property.
     * 
     * @return
     *     possible object is
     *     {@link TUf }
     *     
     */
    public TUf getUF() {
        return uf;
    }

    /**
     * Sets the value of the uf property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUf }
     *     
     */
    public void setUF(TUf value) {
        this.uf = value;
    }

    /**
     * Gets the value of the rntc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRNTC() {
        return rntc;
    }

    /**
     * Sets the value of the rntc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRNTC(String value) {
        this.rntc = value;
    }

}
