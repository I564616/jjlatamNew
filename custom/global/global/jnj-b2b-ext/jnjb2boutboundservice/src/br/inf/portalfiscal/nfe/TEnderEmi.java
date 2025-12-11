
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TEnderEmi complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TEnderEmi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xLgr" type="{http://www.portalfiscal.inf.br/nfe}xLgr" form="qualified"/>
 *         &lt;element name="nro" type="{http://www.portalfiscal.inf.br/nfe}nro" form="qualified"/>
 *         &lt;element name="xCpl" type="{http://www.portalfiscal.inf.br/nfe}xCpl" minOccurs="0" form="qualified"/>
 *         &lt;element name="xBairro" type="{http://www.portalfiscal.inf.br/nfe}xBairro" form="qualified"/>
 *         &lt;element name="cMun" type="{http://www.portalfiscal.inf.br/nfe}TCodMunIBGE" form="qualified"/>
 *         &lt;element name="xMun" type="{http://www.portalfiscal.inf.br/nfe}xMun" form="qualified"/>
 *         &lt;element name="UF" type="{http://www.portalfiscal.inf.br/nfe}TUfEmi" form="qualified"/>
 *         &lt;element name="CEP" type="{http://www.portalfiscal.inf.br/nfe}CEP" form="qualified"/>
 *         &lt;element name="cPais" type="{http://www.portalfiscal.inf.br/nfe}cPais" minOccurs="0" form="qualified"/>
 *         &lt;element name="xPais" type="{http://www.portalfiscal.inf.br/nfe}xPais" minOccurs="0" form="qualified"/>
 *         &lt;element name="fone" type="{http://www.portalfiscal.inf.br/nfe}fone" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEnderEmi", propOrder = {
    "xLgr",
    "nro",
    "xCpl",
    "xBairro",
    "cMun",
    "xMun",
    "uf",
    "cep",
    "cPais",
    "xPais",
    "fone"
})
public class TEnderEmi {

    @XmlElement(required = true)
    protected String xLgr;
    @XmlElement(required = true)
    protected String nro;
    protected String xCpl;
    @XmlElement(required = true)
    protected String xBairro;
    @XmlElement(required = true)
    protected String cMun;
    @XmlElement(required = true)
    protected String xMun;
    @XmlElement(name = "UF", required = true)
    protected TUfEmi uf;
    @XmlElement(name = "CEP", required = true)
    protected String cep;
    protected String cPais;
    protected String xPais;
    protected String fone;

    /**
     * Gets the value of the xLgr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXLgr() {
        return xLgr;
    }

    /**
     * Sets the value of the xLgr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXLgr(String value) {
        this.xLgr = value;
    }

    /**
     * Gets the value of the nro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNro() {
        return nro;
    }

    /**
     * Sets the value of the nro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNro(String value) {
        this.nro = value;
    }

    /**
     * Gets the value of the xCpl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXCpl() {
        return xCpl;
    }

    /**
     * Sets the value of the xCpl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXCpl(String value) {
        this.xCpl = value;
    }

    /**
     * Gets the value of the xBairro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXBairro() {
        return xBairro;
    }

    /**
     * Sets the value of the xBairro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXBairro(String value) {
        this.xBairro = value;
    }

    /**
     * Gets the value of the cMun property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCMun() {
        return cMun;
    }

    /**
     * Sets the value of the cMun property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCMun(String value) {
        this.cMun = value;
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
     * Gets the value of the cep property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEP() {
        return cep;
    }

    /**
     * Sets the value of the cep property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEP(String value) {
        this.cep = value;
    }

    /**
     * Gets the value of the cPais property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPais() {
        return cPais;
    }

    /**
     * Sets the value of the cPais property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPais(String value) {
        this.cPais = value;
    }

    /**
     * Gets the value of the xPais property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXPais() {
        return xPais;
    }

    /**
     * Sets the value of the xPais property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXPais(String value) {
        this.xPais = value;
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

}
