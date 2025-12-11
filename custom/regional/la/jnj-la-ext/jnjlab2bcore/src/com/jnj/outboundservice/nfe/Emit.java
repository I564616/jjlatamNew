
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for emit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="emit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CNPJ" type="{http://www.portalfiscal.inf.br/nfe}TCnpj" minOccurs="0" form="qualified"/>
 *         &lt;element name="CPF" type="{http://www.portalfiscal.inf.br/nfe}TCpf" minOccurs="0" form="qualified"/>
 *         &lt;element name="xNome" type="{http://www.portalfiscal.inf.br/nfe}xNome" form="qualified"/>
 *         &lt;element name="xFant" type="{http://www.portalfiscal.inf.br/nfe}xFant" minOccurs="0" form="qualified"/>
 *         &lt;element name="enderEmit" type="{http://www.portalfiscal.inf.br/nfe}TEnderEmi" form="qualified"/>
 *         &lt;element name="IE" type="{http://www.portalfiscal.inf.br/nfe}TIe" form="qualified"/>
 *         &lt;element name="IEST" type="{http://www.portalfiscal.inf.br/nfe}TIeST" minOccurs="0" form="qualified"/>
 *         &lt;element name="IM" type="{http://www.portalfiscal.inf.br/nfe}IM" minOccurs="0" form="qualified"/>
 *         &lt;element name="CNAE" type="{http://www.portalfiscal.inf.br/nfe}CNAE" minOccurs="0" form="qualified"/>
 *         &lt;element name="CRT" type="{http://www.portalfiscal.inf.br/nfe}CRT" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emit", propOrder = {
    "cnpj",
    "cpf",
    "xNome",
    "xFant",
    "enderEmit",
    "ie",
    "iest",
    "im",
    "cnae",
    "crt"
})
public class Emit {

    @XmlElement(name = "CNPJ")
    protected String cnpj;
    @XmlElement(name = "CPF")
    protected String cpf;
    @XmlElement(required = true)
    protected String xNome;
    protected String xFant;
    @XmlElement(required = true)
    protected TEnderEmi enderEmit;
    @XmlElement(name = "IE", required = true)
    protected String ie;
    @XmlElement(name = "IEST")
    protected String iest;
    @XmlElement(name = "IM")
    protected String im;
    @XmlElement(name = "CNAE")
    protected String cnae;
    @XmlElement(name = "CRT", required = true)
    protected String crt;

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
     * Gets the value of the xFant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXFant() {
        return xFant;
    }

    /**
     * Sets the value of the xFant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXFant(String value) {
        this.xFant = value;
    }

    /**
     * Gets the value of the enderEmit property.
     * 
     * @return
     *     possible object is
     *     {@link TEnderEmi }
     *     
     */
    public TEnderEmi getEnderEmit() {
        return enderEmit;
    }

    /**
     * Sets the value of the enderEmit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEnderEmi }
     *     
     */
    public void setEnderEmit(TEnderEmi value) {
        this.enderEmit = value;
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
     * Gets the value of the iest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIEST() {
        return iest;
    }

    /**
     * Sets the value of the iest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIEST(String value) {
        this.iest = value;
    }

    /**
     * Gets the value of the im property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIM() {
        return im;
    }

    /**
     * Sets the value of the im property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIM(String value) {
        this.im = value;
    }

    /**
     * Gets the value of the cnae property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNAE() {
        return cnae;
    }

    /**
     * Sets the value of the cnae property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNAE(String value) {
        this.cnae = value;
    }

    /**
     * Gets the value of the crt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCRT() {
        return crt;
    }

    /**
     * Sets the value of the crt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCRT(String value) {
        this.crt = value;
    }

}
