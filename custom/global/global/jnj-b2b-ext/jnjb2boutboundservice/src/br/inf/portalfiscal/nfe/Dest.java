
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CNPJ" type="{http://www.portalfiscal.inf.br/nfe}TCnpjOpc" minOccurs="0" form="qualified"/>
 *         &lt;element name="CPF" type="{http://www.portalfiscal.inf.br/nfe}TCpf" minOccurs="0" form="qualified"/>
 *         &lt;element name="xNome" type="{http://www.portalfiscal.inf.br/nfe}xNome2" form="qualified"/>
 *         &lt;element name="enderDest" type="{http://www.portalfiscal.inf.br/nfe}TEndereco" form="qualified"/>
 *         &lt;element name="IE" type="{http://www.portalfiscal.inf.br/nfe}TIeDest" form="qualified"/>
 *         &lt;element name="ISUF" type="{http://www.portalfiscal.inf.br/nfe}ISUF" minOccurs="0" form="qualified"/>
 *         &lt;element name="email" type="{http://www.portalfiscal.inf.br/nfe}email" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dest", propOrder = {
    "cnpj",
    "cpf",
    "xNome",
    "enderDest",
    "ie",
    "isuf",
    "email"
})
public class Dest {

    @XmlElement(name = "CNPJ")
    protected String cnpj;
    @XmlElement(name = "CPF")
    protected String cpf;
    @XmlElement(required = true)
    protected String xNome;
    @XmlElement(required = true)
    protected TEndereco enderDest;
    @XmlElement(name = "IE", required = true)
    protected String ie;
    @XmlElement(name = "ISUF")
    protected String isuf;
    protected String email;

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
     * Gets the value of the enderDest property.
     * 
     * @return
     *     possible object is
     *     {@link TEndereco }
     *     
     */
    public TEndereco getEnderDest() {
        return enderDest;
    }

    /**
     * Sets the value of the enderDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEndereco }
     *     
     */
    public void setEnderDest(TEndereco value) {
        this.enderDest = value;
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
     * Gets the value of the isuf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISUF() {
        return isuf;
    }

    /**
     * Sets the value of the isuf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISUF(String value) {
        this.isuf = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

}
