
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for avulsa complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="avulsa">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CNPJ" type="{http://www.portalfiscal.inf.br/nfe}TCnpj" form="qualified"/>
 *         &lt;element name="xOrgao" type="{http://www.portalfiscal.inf.br/nfe}xOrgao" form="qualified"/>
 *         &lt;element name="matr" type="{http://www.portalfiscal.inf.br/nfe}matr" form="qualified"/>
 *         &lt;element name="xAgente" type="{http://www.portalfiscal.inf.br/nfe}xAgente" form="qualified"/>
 *         &lt;element name="fone" type="{http://www.portalfiscal.inf.br/nfe}fone2" minOccurs="0" form="qualified"/>
 *         &lt;element name="UF" type="{http://www.portalfiscal.inf.br/nfe}TUfEmi" form="qualified"/>
 *         &lt;element name="nDAR" type="{http://www.portalfiscal.inf.br/nfe}nDAR" minOccurs="0" form="qualified"/>
 *         &lt;element name="dEmi" type="{http://www.portalfiscal.inf.br/nfe}TData" minOccurs="0" form="qualified"/>
 *         &lt;element name="vDAR" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="repEmi" type="{http://www.portalfiscal.inf.br/nfe}repEmi" form="qualified"/>
 *         &lt;element name="dPag" type="{http://www.portalfiscal.inf.br/nfe}TData" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "avulsa", propOrder = {
    "cnpj",
    "xOrgao",
    "matr",
    "xAgente",
    "fone",
    "uf",
    "ndar",
    "dEmi",
    "vdar",
    "repEmi",
    "dPag"
})
public class Avulsa {

    @XmlElement(name = "CNPJ", required = true)
    protected String cnpj;
    @XmlElement(required = true)
    protected String xOrgao;
    @XmlElement(required = true)
    protected String matr;
    @XmlElement(required = true)
    protected String xAgente;
    protected String fone;
    @XmlElement(name = "UF", required = true)
    protected TUfEmi uf;
    @XmlElement(name = "nDAR")
    protected String ndar;
    protected String dEmi;
    @XmlElement(name = "vDAR")
    protected String vdar;
    @XmlElement(required = true)
    protected String repEmi;
    protected String dPag;

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
     * Gets the value of the xOrgao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXOrgao() {
        return xOrgao;
    }

    /**
     * Sets the value of the xOrgao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXOrgao(String value) {
        this.xOrgao = value;
    }

    /**
     * Gets the value of the matr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatr() {
        return matr;
    }

    /**
     * Sets the value of the matr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatr(String value) {
        this.matr = value;
    }

    /**
     * Gets the value of the xAgente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXAgente() {
        return xAgente;
    }

    /**
     * Sets the value of the xAgente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXAgente(String value) {
        this.xAgente = value;
    }

    /**
     * Gets the value of the fone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFone() {
        return fone;
    }

    /**
     * Sets the value of the fone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFone(String value) {
        this.fone = value;
    }

    /**
     * Gets the value of the uf property.
     * 
     * @return
     *     possible object is
     *     {@link TUfEmi }
     *     
     */
    public TUfEmi getUF() {
        return uf;
    }

    /**
     * Sets the value of the uf property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUfEmi }
     *     
     */
    public void setUF(TUfEmi value) {
        this.uf = value;
    }

    /**
     * Gets the value of the ndar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNDAR() {
        return ndar;
    }

    /**
     * Sets the value of the ndar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNDAR(String value) {
        this.ndar = value;
    }

    /**
     * Gets the value of the dEmi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEmi() {
        return dEmi;
    }

    /**
     * Sets the value of the dEmi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEmi(String value) {
        this.dEmi = value;
    }

    /**
     * Gets the value of the vdar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDAR() {
        return vdar;
    }

    /**
     * Sets the value of the vdar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDAR(String value) {
        this.vdar = value;
    }

    /**
     * Gets the value of the repEmi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepEmi() {
        return repEmi;
    }

    /**
     * Sets the value of the repEmi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepEmi(String value) {
        this.repEmi = value;
    }

    /**
     * Gets the value of the dPag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDPag() {
        return dPag;
    }

    /**
     * Sets the value of the dPag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDPag(String value) {
        this.dPag = value;
    }

}
