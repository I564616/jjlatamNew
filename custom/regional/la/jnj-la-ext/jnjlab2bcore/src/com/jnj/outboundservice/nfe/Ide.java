
package com.jnj.outboundservice.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ide complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ide">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cUF" type="{http://www.portalfiscal.inf.br/nfe}TCodUfIBGE" form="qualified"/>
 *         &lt;element name="cNF" type="{http://www.portalfiscal.inf.br/nfe}cNF" form="qualified"/>
 *         &lt;element name="natOp" type="{http://www.portalfiscal.inf.br/nfe}natOp" form="qualified"/>
 *         &lt;element name="mod" type="{http://www.portalfiscal.inf.br/nfe}TMod" form="qualified"/>
 *         &lt;element name="serie" type="{http://www.portalfiscal.inf.br/nfe}TSerie" form="qualified"/>
 *         &lt;element name="nNF" type="{http://www.portalfiscal.inf.br/nfe}TNF" form="qualified"/>
 *         &lt;element name="dhEmi" type="{http://www.portalfiscal.inf.br/nfe}TDateTimeUTC" form="qualified"/>
 *         &lt;element name="dhSaiEnt" type="{http://www.portalfiscal.inf.br/nfe}TDateTimeUTC" minOccurs="0" form="qualified"/>
 *         &lt;element name="tpNF" type="{http://www.portalfiscal.inf.br/nfe}tpNF" form="qualified"/>
 *         &lt;element name="idDest" type="{http://www.portalfiscal.inf.br/nfe}idDest" form="qualified"/>
 *         &lt;element name="cMunFG" type="{http://www.portalfiscal.inf.br/nfe}TCodMunIBGE" form="qualified"/>
 *         &lt;element name="tpImp" type="{http://www.portalfiscal.inf.br/nfe}tpImp" form="qualified"/>
 *         &lt;element name="tpEmis" type="{http://www.portalfiscal.inf.br/nfe}tpEmis" form="qualified"/>
 *         &lt;element name="cDV" type="{http://www.portalfiscal.inf.br/nfe}cDV" form="qualified"/>
 *         &lt;element name="tpAmb" type="{http://www.portalfiscal.inf.br/nfe}TAmb" form="qualified"/>
 *         &lt;element name="finNFe" type="{http://www.portalfiscal.inf.br/nfe}TFinNFe" form="qualified"/>
 *         &lt;element name="indFinal" type="{http://www.portalfiscal.inf.br/nfe}indFinal" form="qualified"/>
 *         &lt;element name="indPres" type="{http://www.portalfiscal.inf.br/nfe}indPres" form="qualified"/>
 *         &lt;element name="procEmi" type="{http://www.portalfiscal.inf.br/nfe}TProcEmi" form="qualified"/>
 *         &lt;element name="verProc" type="{http://www.portalfiscal.inf.br/nfe}verProc" form="qualified"/>
 *         &lt;element name="dhCont" type="{http://www.portalfiscal.inf.br/nfe}TDateTimeUTC" minOccurs="0" form="qualified"/>
 *         &lt;element name="xJust" type="{http://www.portalfiscal.inf.br/nfe}xJust" minOccurs="0" form="qualified"/>
 *         &lt;element name="NFref" type="{http://www.portalfiscal.inf.br/nfe}NFref" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ide", propOrder = {
    "cuf",
    "cnf",
    "natOp",
    "mod",
    "serie",
    "nnf",
    "dhEmi",
    "dhSaiEnt",
    "tpNF",
    "idDest",
    "cMunFG",
    "tpImp",
    "tpEmis",
    "cdv",
    "tpAmb",
    "finNFe",
    "indFinal",
    "indPres",
    "procEmi",
    "verProc",
    "dhCont",
    "xJust",
    "nFref"
})
public class Ide {

    @XmlElement(name = "cUF", required = true)
    protected String cuf;
    @XmlElement(name = "cNF", required = true)
    protected String cnf;
    @XmlElement(required = true)
    protected String natOp;
    @XmlElement(required = true)
    protected String mod;
    @XmlElement(required = true)
    protected String serie;
    @XmlElement(name = "nNF", required = true)
    protected String nnf;
    @XmlElement(required = true)
    protected String dhEmi;
    protected String dhSaiEnt;
    @XmlElement(required = true)
    protected String tpNF;
    @XmlElement(required = true)
    protected String idDest;
    @XmlElement(required = true)
    protected String cMunFG;
    @XmlElement(required = true)
    protected String tpImp;
    @XmlElement(required = true)
    protected String tpEmis;
    @XmlElement(name = "cDV", required = true)
    protected String cdv;
    @XmlElement(required = true)
    protected String tpAmb;
    @XmlElement(required = true)
    protected String finNFe;
    @XmlElement(required = true)
    protected String indFinal;
    @XmlElement(required = true)
    protected String indPres;
    @XmlElement(required = true)
    protected String procEmi;
    @XmlElement(required = true)
    protected String verProc;
    protected String dhCont;
    protected String xJust;
    @XmlElement(name = "NFref")
    protected List<NFref> nFref;

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
     * Gets the value of the cnf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNF() {
        return cnf;
    }

    /**
     * Sets the value of the cnf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNF(String value) {
        this.cnf = value;
    }

    /**
     * Gets the value of the natOp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatOp() {
        return natOp;
    }

    /**
     * Sets the value of the natOp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatOp(String value) {
        this.natOp = value;
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

    /**
     * Gets the value of the dhEmi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDhEmi() {
        return dhEmi;
    }

    /**
     * Sets the value of the dhEmi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDhEmi(String value) {
        this.dhEmi = value;
    }

    /**
     * Gets the value of the dhSaiEnt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDhSaiEnt() {
        return dhSaiEnt;
    }

    /**
     * Sets the value of the dhSaiEnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDhSaiEnt(String value) {
        this.dhSaiEnt = value;
    }

    /**
     * Gets the value of the tpNF property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpNF() {
        return tpNF;
    }

    /**
     * Sets the value of the tpNF property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpNF(String value) {
        this.tpNF = value;
    }

    /**
     * Gets the value of the idDest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDest() {
        return idDest;
    }

    /**
     * Sets the value of the idDest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDest(String value) {
        this.idDest = value;
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
     * Gets the value of the tpImp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpImp() {
        return tpImp;
    }

    /**
     * Sets the value of the tpImp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpImp(String value) {
        this.tpImp = value;
    }

    /**
     * Gets the value of the tpEmis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpEmis() {
        return tpEmis;
    }

    /**
     * Sets the value of the tpEmis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpEmis(String value) {
        this.tpEmis = value;
    }

    /**
     * Gets the value of the cdv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCDV() {
        return cdv;
    }

    /**
     * Sets the value of the cdv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCDV(String value) {
        this.cdv = value;
    }

    /**
     * Gets the value of the tpAmb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpAmb() {
        return tpAmb;
    }

    /**
     * Sets the value of the tpAmb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpAmb(String value) {
        this.tpAmb = value;
    }

    /**
     * Gets the value of the finNFe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinNFe() {
        return finNFe;
    }

    /**
     * Sets the value of the finNFe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinNFe(String value) {
        this.finNFe = value;
    }

    /**
     * Gets the value of the indFinal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndFinal() {
        return indFinal;
    }

    /**
     * Sets the value of the indFinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndFinal(String value) {
        this.indFinal = value;
    }

    /**
     * Gets the value of the indPres property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndPres() {
        return indPres;
    }

    /**
     * Sets the value of the indPres property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndPres(String value) {
        this.indPres = value;
    }

    /**
     * Gets the value of the procEmi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcEmi() {
        return procEmi;
    }

    /**
     * Sets the value of the procEmi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcEmi(String value) {
        this.procEmi = value;
    }

    /**
     * Gets the value of the verProc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerProc() {
        return verProc;
    }

    /**
     * Sets the value of the verProc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerProc(String value) {
        this.verProc = value;
    }

    /**
     * Gets the value of the dhCont property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDhCont() {
        return dhCont;
    }

    /**
     * Sets the value of the dhCont property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDhCont(String value) {
        this.dhCont = value;
    }

    /**
     * Gets the value of the xJust property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXJust() {
        return xJust;
    }

    /**
     * Sets the value of the xJust property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXJust(String value) {
        this.xJust = value;
    }

    /**
     * Gets the value of the nFref property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nFref property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNFref().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NFref }
     * 
     * 
     */
    public List<NFref> getNFref() {
        if (nFref == null) {
            nFref = new ArrayList<NFref>();
        }
        return this.nFref;
    }

}
