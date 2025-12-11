
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transporta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transporta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CNPJ" type="{http://www.portalfiscal.inf.br/nfe}TCnpj" minOccurs="0" form="qualified"/>
 *         &lt;element name="CPF" type="{http://www.portalfiscal.inf.br/nfe}TCpf" minOccurs="0" form="qualified"/>
 *         &lt;element name="xNome" type="{http://www.portalfiscal.inf.br/nfe}xNome3" minOccurs="0" form="qualified"/>
 *         &lt;element name="IE" type="{http://www.portalfiscal.inf.br/nfe}TIeDest" minOccurs="0" form="qualified"/>
 *         &lt;element name="xEnder" type="{http://www.portalfiscal.inf.br/nfe}xEnder" minOccurs="0" form="qualified"/>
 *         &lt;element name="xMun" type="{http://www.portalfiscal.inf.br/nfe}xMun4" minOccurs="0" form="qualified"/>
 *         &lt;element name="UF" type="{http://www.portalfiscal.inf.br/nfe}TUf" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transporta", propOrder = {
    "cnpj",
    "cpf",
    "xNome",
    "ie",
    "xEnder",
    "xMun",
    "uf"
})
public class Transporta {

    @XmlElement(name = "CNPJ")
    protected String cnpj;
    @XmlElement(name = "CPF")
    protected String cpf;
    protected String xNome;
    @XmlElement(name = "IE")
    protected String ie;
    protected String xEnder;
    protected String xMun;
    @XmlElement(name = "UF")
    protected TUf uf;

    /**
     * Gets the value of the cnpj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNPJ() {
        return cnpj;
    }

    /**
     * Sets the value of the cnpj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNPJ(String value) {
        this.cnpj = value;
    }

    /**
     * Gets the value of the cpf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPF() {
        return cpf;
    }

    /**
     * Sets the value of the cpf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPF(String value) {
        this.cpf = value;
    }

    /**
     * Gets the value of the xNome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXNome() {
        return xNome;
    }

    /**
     * Sets the value of the xNome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXNome(String value) {
        this.xNome = value;
    }

    /**
     * Gets the value of the ie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIE() {
        return ie;
    }

    /**
     * Sets the value of the ie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIE(String value) {
        this.ie = value;
    }

    /**
     * Gets the value of the xEnder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXEnder() {
        return xEnder;
    }

    /**
     * Sets the value of the xEnder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXEnder(String value) {
        this.xEnder = value;
    }

    /**
     * Gets the value of the xMun property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXMun() {
        return xMun;
    }

    /**
     * Sets the value of the xMun property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXMun(String value) {
        this.xMun = value;
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

}
