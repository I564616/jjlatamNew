
package com.jnj.outboundservice.nfe;

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
 *         &lt;element name="cProdANP" type="{http://www.portalfiscal.inf.br/nfe}cProdANP" form="qualified"/>
 *         &lt;element name="descANP" type="{http://www.portalfiscal.inf.br/nfe}descANP" form="qualified"/>
 *         &lt;element name="pGLP" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Max100" minOccurs="0" form="qualified"/>
 *         &lt;element name="pGNn" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Max100" minOccurs="0" form="qualified"/>
 *         &lt;element name="pGNi" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04Max100" minOccurs="0" form="qualified"/>
 *         &lt;element name="vPart" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *         &lt;element name="CODIF" type="{http://www.portalfiscal.inf.br/nfe}CODIF" minOccurs="0" form="qualified"/>
 *         &lt;element name="qTemp" type="{http://www.portalfiscal.inf.br/nfe}TDec_1204temperatura" minOccurs="0" form="qualified"/>
 *         &lt;element name="UFCons" type="{http://www.portalfiscal.inf.br/nfe}TUf" form="qualified"/>
 *         &lt;element name="CIDE" type="{http://www.portalfiscal.inf.br/nfe}CIDE" minOccurs="0" form="qualified"/>
 *         &lt;element name="encerrante" type="{http://www.portalfiscal.inf.br/nfe}encerrante" minOccurs="0" form="qualified"/>
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
    "descANP",
    "pglp",
    "pgNn",
    "pgNi",
    "vPart",
    "codif",
    "qTemp",
    "ufCons",
    "cide",
    "encerrante"
})
public class Comb {

    @XmlElement(required = true)
    protected String cProdANP;
    @XmlElement(required = true)
    protected String descANP;
    @XmlElement(name = "pGLP")
    protected String pglp;
    @XmlElement(name = "pGNn")
    protected String pgNn;
    @XmlElement(name = "pGNi")
    protected String pgNi;
    protected String vPart;
    @XmlElement(name = "CODIF")
    protected String codif;
    protected String qTemp;
    @XmlElement(name = "UFCons", required = true)
    protected TUf ufCons;
    @XmlElement(name = "CIDE")
    protected CIDE cide;
    protected Encerrante encerrante;

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
     * Gets the value of the descANP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescANP() {
        return descANP;
    }

    /**
     * Sets the value of the descANP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescANP(String value) {
        this.descANP = value;
    }

    /**
     * Gets the value of the pglp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPGLP() {
        return pglp;
    }

    /**
     * Sets the value of the pglp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPGLP(String value) {
        this.pglp = value;
    }

    /**
     * Gets the value of the pgNn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPGNn() {
        return pgNn;
    }

    /**
     * Sets the value of the pgNn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPGNn(String value) {
        this.pgNn = value;
    }

    /**
     * Gets the value of the pgNi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPGNi() {
        return pgNi;
    }

    /**
     * Sets the value of the pgNi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPGNi(String value) {
        this.pgNi = value;
    }

    /**
     * Gets the value of the vPart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVPart() {
        return vPart;
    }

    /**
     * Sets the value of the vPart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVPart(String value) {
        this.vPart = value;
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

    /**
     * Gets the value of the encerrante property.
     * 
     * @return
     *     possible object is
     *     {@link Encerrante }
     *     
     */
    public Encerrante getEncerrante() {
        return encerrante;
    }

    /**
     * Sets the value of the encerrante property.
     * 
     * @param value
     *     allowed object is
     *     {@link Encerrante }
     *     
     */
    public void setEncerrante(Encerrante value) {
        this.encerrante = value;
    }

}
