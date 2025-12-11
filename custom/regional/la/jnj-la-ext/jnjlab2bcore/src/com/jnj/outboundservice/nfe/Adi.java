
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for adi complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nAdicao" type="{http://www.portalfiscal.inf.br/nfe}nAdicao" form="qualified"/>
 *         &lt;element name="nSeqAdic" type="{http://www.portalfiscal.inf.br/nfe}nSeqAdic" form="qualified"/>
 *         &lt;element name="cFabricante" type="{http://www.portalfiscal.inf.br/nfe}cFabricante" form="qualified"/>
 *         &lt;element name="vDescDI" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="nDraw" type="{http://www.portalfiscal.inf.br/nfe}nDraw" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adi", propOrder = {
    "nAdicao",
    "nSeqAdic",
    "cFabricante",
    "vDescDI",
    "nDraw"
})
public class Adi {

    @XmlElement(required = true)
    protected String nAdicao;
    @XmlElement(required = true)
    protected String nSeqAdic;
    @XmlElement(required = true)
    protected String cFabricante;
    protected String vDescDI;
    protected String nDraw;

    /**
     * Gets the value of the nAdicao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNAdicao() {
        return nAdicao;
    }

    /**
     * Sets the value of the nAdicao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNAdicao(String value) {
        this.nAdicao = value;
    }

    /**
     * Gets the value of the nSeqAdic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNSeqAdic() {
        return nSeqAdic;
    }

    /**
     * Sets the value of the nSeqAdic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNSeqAdic(String value) {
        this.nSeqAdic = value;
    }

    /**
     * Gets the value of the cFabricante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFabricante() {
        return cFabricante;
    }

    /**
     * Sets the value of the cFabricante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFabricante(String value) {
        this.cFabricante = value;
    }

    /**
     * Gets the value of the vDescDI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDescDI() {
        return vDescDI;
    }

    /**
     * Sets the value of the vDescDI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDescDI(String value) {
        this.vDescDI = value;
    }

    /**
     * Gets the value of the nDraw property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNDraw() {
        return nDraw;
    }

    /**
     * Sets the value of the nDraw property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNDraw(String value) {
        this.nDraw = value;
    }

}
