
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ISSQN complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ISSQN">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vAliq" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302a04" form="qualified"/>
 *         &lt;element name="vISSQN" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="cMunFG" type="{http://www.portalfiscal.inf.br/nfe}TCodMunIBGE" form="qualified"/>
 *         &lt;element name="cListServ" type="{http://www.portalfiscal.inf.br/nfe}TCListServ" form="qualified"/>
 *         &lt;element name="vDeducao" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vOutro" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vDescIncond" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vDescCond" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vISSRet" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="indISS" type="{http://www.portalfiscal.inf.br/nfe}indISS" form="qualified"/>
 *         &lt;element name="cServico" type="{http://www.portalfiscal.inf.br/nfe}cServico" minOccurs="0" form="qualified"/>
 *         &lt;element name="cMun" type="{http://www.portalfiscal.inf.br/nfe}TCodMunIBGE" minOccurs="0" form="qualified"/>
 *         &lt;element name="cPais" type="{http://www.portalfiscal.inf.br/nfe}cPais3" minOccurs="0" form="qualified"/>
 *         &lt;element name="nProcesso" type="{http://www.portalfiscal.inf.br/nfe}nProcesso" minOccurs="0" form="qualified"/>
 *         &lt;element name="indIncentivo" type="{http://www.portalfiscal.inf.br/nfe}indIncentivo" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ISSQN", propOrder = {
    "vbc",
    "vAliq",
    "vissqn",
    "cMunFG",
    "cListServ",
    "vDeducao",
    "vOutro",
    "vDescIncond",
    "vDescCond",
    "vissRet",
    "indISS",
    "cServico",
    "cMun",
    "cPais",
    "nProcesso",
    "indIncentivo"
})
public class ISSQN {

    @XmlElement(name = "vBC", required = true)
    protected String vbc;
    @XmlElement(required = true)
    protected String vAliq;
    @XmlElement(name = "vISSQN", required = true)
    protected String vissqn;
    @XmlElement(required = true)
    protected String cMunFG;
    @XmlElement(required = true)
    protected String cListServ;
    protected String vDeducao;
    protected String vOutro;
    protected String vDescIncond;
    protected String vDescCond;
    @XmlElement(name = "vISSRet")
    protected String vissRet;
    @XmlElement(required = true)
    protected String indISS;
    protected String cServico;
    protected String cMun;
    protected String cPais;
    protected String nProcesso;
    @XmlElement(required = true)
    protected String indIncentivo;

    /**
     * Gets the value of the vbc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBC() {
        return vbc;
    }

    /**
     * Sets the value of the vbc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBC(String value) {
        this.vbc = value;
    }

    /**
     * Gets the value of the vAliq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVAliq() {
        return vAliq;
    }

    /**
     * Sets the value of the vAliq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVAliq(String value) {
        this.vAliq = value;
    }

    /**
     * Gets the value of the vissqn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVISSQN() {
        return vissqn;
    }

    /**
     * Sets the value of the vissqn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVISSQN(String value) {
        this.vissqn = value;
    }

    /**
     * Gets the value of the cMunFG property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCMunFG() {
        return cMunFG;
    }

    /**
     * Sets the value of the cMunFG property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCMunFG(String value) {
        this.cMunFG = value;
    }

    /**
     * Gets the value of the cListServ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCListServ() {
        return cListServ;
    }

    /**
     * Sets the value of the cListServ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCListServ(String value) {
        this.cListServ = value;
    }

    /**
     * Gets the value of the vDeducao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDeducao() {
        return vDeducao;
    }

    /**
     * Sets the value of the vDeducao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDeducao(String value) {
        this.vDeducao = value;
    }

    /**
     * Gets the value of the vOutro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVOutro() {
        return vOutro;
    }

    /**
     * Sets the value of the vOutro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVOutro(String value) {
        this.vOutro = value;
    }

    /**
     * Gets the value of the vDescIncond property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDescIncond() {
        return vDescIncond;
    }

    /**
     * Sets the value of the vDescIncond property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDescIncond(String value) {
        this.vDescIncond = value;
    }

    /**
     * Gets the value of the vDescCond property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDescCond() {
        return vDescCond;
    }

    /**
     * Sets the value of the vDescCond property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDescCond(String value) {
        this.vDescCond = value;
    }

    /**
     * Gets the value of the vissRet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVISSRet() {
        return vissRet;
    }

    /**
     * Sets the value of the vissRet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVISSRet(String value) {
        this.vissRet = value;
    }

    /**
     * Gets the value of the indISS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndISS() {
        return indISS;
    }

    /**
     * Sets the value of the indISS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndISS(String value) {
        this.indISS = value;
    }

    /**
     * Gets the value of the cServico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCServico() {
        return cServico;
    }

    /**
     * Sets the value of the cServico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCServico(String value) {
        this.cServico = value;
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
     * Gets the value of the nProcesso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNProcesso() {
        return nProcesso;
    }

    /**
     * Sets the value of the nProcesso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNProcesso(String value) {
        this.nProcesso = value;
    }

    /**
     * Gets the value of the indIncentivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndIncentivo() {
        return indIncentivo;
    }

    /**
     * Sets the value of the indIncentivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndIncentivo(String value) {
        this.indIncentivo = value;
    }

}
