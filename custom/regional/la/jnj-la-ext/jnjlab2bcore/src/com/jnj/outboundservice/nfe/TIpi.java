
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TIpi complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TIpi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CNPJProd" type="{http://www.portalfiscal.inf.br/nfe}TCnpj" minOccurs="0" form="qualified"/>
 *         &lt;element name="cSelo" type="{http://www.portalfiscal.inf.br/nfe}cSelo" minOccurs="0" form="qualified"/>
 *         &lt;element name="qSelo" type="{http://www.portalfiscal.inf.br/nfe}qSelo" minOccurs="0" form="qualified"/>
 *         &lt;element name="cEnq" type="{http://www.portalfiscal.inf.br/nfe}cEnq" form="qualified"/>
 *         &lt;element name="IPITrib" type="{http://www.portalfiscal.inf.br/nfe}IPITrib" minOccurs="0" form="qualified"/>
 *         &lt;element name="IPINT" type="{http://www.portalfiscal.inf.br/nfe}IPINT" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TIpi", propOrder = {
    "cnpjProd",
    "cSelo",
    "qSelo",
    "cEnq",
    "ipiTrib",
    "ipint"
})
public class TIpi {

    @XmlElement(name = "CNPJProd")
    protected String cnpjProd;
    protected String cSelo;
    protected String qSelo;
    @XmlElement(required = true)
    protected String cEnq;
    @XmlElement(name = "IPITrib")
    protected IPITrib ipiTrib;
    @XmlElement(name = "IPINT")
    protected IPINT ipint;

    /**
     * Gets the value of the cnpjProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNPJProd() {
        return cnpjProd;
    }

    /**
     * Sets the value of the cnpjProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNPJProd(String value) {
        this.cnpjProd = value;
    }

    /**
     * Gets the value of the cSelo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSelo() {
        return cSelo;
    }

    /**
     * Sets the value of the cSelo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSelo(String value) {
        this.cSelo = value;
    }

    /**
     * Gets the value of the qSelo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQSelo() {
        return qSelo;
    }

    /**
     * Sets the value of the qSelo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQSelo(String value) {
        this.qSelo = value;
    }

    /**
     * Gets the value of the cEnq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEnq() {
        return cEnq;
    }

    /**
     * Sets the value of the cEnq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEnq(String value) {
        this.cEnq = value;
    }

    /**
     * Gets the value of the ipiTrib property.
     * 
     * @return
     *     possible object is
     *     {@link IPITrib }
     *     
     */
    public IPITrib getIPITrib() {
        return ipiTrib;
    }

    /**
     * Sets the value of the ipiTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPITrib }
     *     
     */
    public void setIPITrib(IPITrib value) {
        this.ipiTrib = value;
    }

    /**
     * Gets the value of the ipint property.
     * 
     * @return
     *     possible object is
     *     {@link IPINT }
     *     
     */
    public IPINT getIPINT() {
        return ipint;
    }

    /**
     * Sets the value of the ipint property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPINT }
     *     
     */
    public void setIPINT(IPINT value) {
        this.ipint = value;
    }

}
