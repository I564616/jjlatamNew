
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for refNFP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="refNFP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cUF" type="{http://www.portalfiscal.inf.br/nfe}TCodUfIBGE" form="qualified"/>
 *         &lt;element name="AAMM" type="{http://www.portalfiscal.inf.br/nfe}AAMM2" form="qualified"/>
 *         &lt;element name="CNPJ" type="{http://www.portalfiscal.inf.br/nfe}TCnpj" minOccurs="0" form="qualified"/>
 *         &lt;element name="CPF" type="{http://www.portalfiscal.inf.br/nfe}TCpf" minOccurs="0" form="qualified"/>
 *         &lt;element name="IE" type="{http://www.portalfiscal.inf.br/nfe}TIeDest" form="qualified"/>
 *         &lt;element name="mod" type="{http://www.portalfiscal.inf.br/nfe}mod2" form="qualified"/>
 *         &lt;element name="serie" type="{http://www.portalfiscal.inf.br/nfe}TSerie" form="qualified"/>
 *         &lt;element name="nNF" type="{http://www.portalfiscal.inf.br/nfe}TNF" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refNFP", propOrder = {
    "cuf",
    "aamm",
    "cnpj",
    "cpf",
    "ie",
    "mod",
    "serie",
    "nnf"
})
public class RefNFP {

    @XmlElement(name = "cUF", required = true)
    protected String cuf;
    @XmlElement(name = "AAMM", required = true)
    protected String aamm;
    @XmlElement(name = "CNPJ")
    protected String cnpj;
    @XmlElement(name = "CPF")
    protected String cpf;
    @XmlElement(name = "IE", required = true)
    protected String ie;
    @XmlElement(required = true)
    protected String mod;
    @XmlElement(required = true)
    protected String serie;
    @XmlElement(name = "nNF", required = true)
    protected String nnf;

    /**
     * Gets the value of the cuf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUF() {
        return cuf;
    }

    /**
     * Sets the value of the cuf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUF(String value) {
        this.cuf = value;
    }

    /**
     * Gets the value of the aamm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAAMM() {
        return aamm;
    }

    /**
     * Sets the value of the aamm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAAMM(String value) {
        this.aamm = value;
    }

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
     * Gets the value of the mod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMod() {
        return mod;
    }

    /**
     * Sets the value of the mod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMod(String value) {
        this.mod = value;
    }

    /**
     * Gets the value of the serie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Sets the value of the serie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerie(String value) {
        this.serie = value;
    }

    /**
     * Gets the value of the nnf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNNF() {
        return nnf;
    }

    /**
     * Sets the value of the nnf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNNF(String value) {
        this.nnf = value;
    }

}
