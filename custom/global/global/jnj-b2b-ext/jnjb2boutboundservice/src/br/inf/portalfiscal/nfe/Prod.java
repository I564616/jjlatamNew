
package br.inf.portalfiscal.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for prod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="prod">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cProd" type="{http://www.portalfiscal.inf.br/nfe}cProd" form="qualified"/>
 *         &lt;element name="cEAN" type="{http://www.portalfiscal.inf.br/nfe}cEAN" form="qualified"/>
 *         &lt;element name="xProd" type="{http://www.portalfiscal.inf.br/nfe}xProd" form="qualified"/>
 *         &lt;element name="NCM" type="{http://www.portalfiscal.inf.br/nfe}NCM" form="qualified"/>
 *         &lt;element name="EXTIPI" type="{http://www.portalfiscal.inf.br/nfe}EXTIPI" minOccurs="0" form="qualified"/>
 *         &lt;element name="CFOP" type="{http://www.portalfiscal.inf.br/nfe}TCfop" form="qualified"/>
 *         &lt;element name="uCom" type="{http://www.portalfiscal.inf.br/nfe}uCom" form="qualified"/>
 *         &lt;element name="qCom" type="{http://www.portalfiscal.inf.br/nfe}TDec_1104v" form="qualified"/>
 *         &lt;element name="vUnCom" type="{http://www.portalfiscal.inf.br/nfe}TDec_1110" form="qualified"/>
 *         &lt;element name="vProd" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="cEANTrib" type="{http://www.portalfiscal.inf.br/nfe}cEANTrib" form="qualified"/>
 *         &lt;element name="uTrib" type="{http://www.portalfiscal.inf.br/nfe}uTrib" form="qualified"/>
 *         &lt;element name="qTrib" type="{http://www.portalfiscal.inf.br/nfe}TDec_1104v" form="qualified"/>
 *         &lt;element name="vUnTrib" type="{http://www.portalfiscal.inf.br/nfe}TDec_1110" form="qualified"/>
 *         &lt;element name="vFrete" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vSeg" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vDesc" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vOutro" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="indTot" type="{http://www.portalfiscal.inf.br/nfe}indTot" form="qualified"/>
 *         &lt;element name="DI" type="{http://www.portalfiscal.inf.br/nfe}DI" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="xPed" type="{http://www.portalfiscal.inf.br/nfe}xPed" minOccurs="0" form="qualified"/>
 *         &lt;element name="nItemPed" type="{http://www.portalfiscal.inf.br/nfe}nItemPed" minOccurs="0" form="qualified"/>
 *         &lt;element name="nFCI" type="{http://www.portalfiscal.inf.br/nfe}TGuid" minOccurs="0" form="qualified"/>
 *         &lt;element name="veicProd" type="{http://www.portalfiscal.inf.br/nfe}veicProd" minOccurs="0" form="qualified"/>
 *         &lt;element name="med" type="{http://www.portalfiscal.inf.br/nfe}med" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="arma" type="{http://www.portalfiscal.inf.br/nfe}arma" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="comb" type="{http://www.portalfiscal.inf.br/nfe}comb" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "prod", propOrder = {
    "cProd",
    "cean",
    "xProd",
    "ncm",
    "extipi",
    "cfop",
    "uCom",
    "qCom",
    "vUnCom",
    "vProd",
    "ceanTrib",
    "uTrib",
    "qTrib",
    "vUnTrib",
    "vFrete",
    "vSeg",
    "vDesc",
    "vOutro",
    "indTot",
    "di",
    "xPed",
    "nItemPed",
    "nfci",
    "veicProd",
    "med",
    "arma",
    "comb"
})
public class Prod {

    @XmlElement(required = true)
    protected String cProd;
    @XmlElement(name = "cEAN", required = true)
    protected String cean;
    @XmlElement(required = true)
    protected String xProd;
    @XmlElement(name = "NCM", required = true)
    protected String ncm;
    @XmlElement(name = "EXTIPI")
    protected String extipi;
    @XmlElement(name = "CFOP", required = true)
    protected String cfop;
    @XmlElement(required = true)
    protected String uCom;
    @XmlElement(required = true)
    protected String qCom;
    @XmlElement(required = true)
    protected String vUnCom;
    @XmlElement(required = true)
    protected String vProd;
    @XmlElement(name = "cEANTrib", required = true)
    protected String ceanTrib;
    @XmlElement(required = true)
    protected String uTrib;
    @XmlElement(required = true)
    protected String qTrib;
    @XmlElement(required = true)
    protected String vUnTrib;
    protected String vFrete;
    protected String vSeg;
    protected String vDesc;
    protected String vOutro;
    @XmlElement(required = true)
    protected String indTot;
    @XmlElement(name = "DI")
    protected List<DI> di;
    protected String xPed;
    protected String nItemPed;
    @XmlElement(name = "nFCI")
    protected String nfci;
    protected VeicProd veicProd;
    protected List<Med> med;
    protected List<Arma> arma;
    protected Comb comb;

    /**
     * Gets the value of the cProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCProd() {
        return cProd;
    }

    /**
     * Sets the value of the cProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCProd(String value) {
        this.cProd = value;
    }

    /**
     * Gets the value of the cean property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEAN() {
        return cean;
    }

    /**
     * Sets the value of the cean property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEAN(String value) {
        this.cean = value;
    }

    /**
     * Gets the value of the xProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXProd() {
        return xProd;
    }

    /**
     * Sets the value of the xProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXProd(String value) {
        this.xProd = value;
    }

    /**
     * Gets the value of the ncm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNCM() {
        return ncm;
    }

    /**
     * Sets the value of the ncm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNCM(String value) {
        this.ncm = value;
    }

    /**
     * Gets the value of the extipi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXTIPI() {
        return extipi;
    }

    /**
     * Sets the value of the extipi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXTIPI(String value) {
        this.extipi = value;
    }

    /**
     * Gets the value of the cfop property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFOP() {
        return cfop;
    }

    /**
     * Sets the value of the cfop property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFOP(String value) {
        this.cfop = value;
    }

    /**
     * Gets the value of the uCom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUCom() {
        return uCom;
    }

    /**
     * Sets the value of the uCom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUCom(String value) {
        this.uCom = value;
    }

    /**
     * Gets the value of the qCom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQCom() {
        return qCom;
    }

    /**
     * Sets the value of the qCom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQCom(String value) {
        this.qCom = value;
    }

    /**
     * Gets the value of the vUnCom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVUnCom() {
        return vUnCom;
    }

    /**
     * Sets the value of the vUnCom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVUnCom(String value) {
        this.vUnCom = value;
    }

    /**
     * Gets the value of the vProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVProd() {
        return vProd;
    }

    /**
     * Sets the value of the vProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVProd(String value) {
        this.vProd = value;
    }

    /**
     * Gets the value of the ceanTrib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEANTrib() {
        return ceanTrib;
    }

    /**
     * Sets the value of the ceanTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEANTrib(String value) {
        this.ceanTrib = value;
    }

    /**
     * Gets the value of the uTrib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUTrib() {
        return uTrib;
    }

    /**
     * Sets the value of the uTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUTrib(String value) {
        this.uTrib = value;
    }

    /**
     * Gets the value of the qTrib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTrib() {
        return qTrib;
    }

    /**
     * Sets the value of the qTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTrib(String value) {
        this.qTrib = value;
    }

    /**
     * Gets the value of the vUnTrib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVUnTrib() {
        return vUnTrib;
    }

    /**
     * Sets the value of the vUnTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVUnTrib(String value) {
        this.vUnTrib = value;
    }

    /**
     * Gets the value of the vFrete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVFrete() {
        return vFrete;
    }

    /**
     * Sets the value of the vFrete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVFrete(String value) {
        this.vFrete = value;
    }

    /**
     * Gets the value of the vSeg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVSeg() {
        return vSeg;
    }

    /**
     * Sets the value of the vSeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVSeg(String value) {
        this.vSeg = value;
    }

    /**
     * Gets the value of the vDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDesc() {
        return vDesc;
    }

    /**
     * Sets the value of the vDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDesc(String value) {
        this.vDesc = value;
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
     * Gets the value of the indTot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndTot() {
        return indTot;
    }

    /**
     * Sets the value of the indTot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndTot(String value) {
        this.indTot = value;
    }

    /**
     * Gets the value of the di property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the di property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DI }
     * 
     * 
     */
    public List<DI> getDI() {
        if (di == null) {
            di = new ArrayList<DI>();
        }
        return this.di;
    }

    /**
     * Gets the value of the xPed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXPed() {
        return xPed;
    }

    /**
     * Sets the value of the xPed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXPed(String value) {
        this.xPed = value;
    }

    /**
     * Gets the value of the nItemPed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNItemPed() {
        return nItemPed;
    }

    /**
     * Sets the value of the nItemPed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNItemPed(String value) {
        this.nItemPed = value;
    }

    /**
     * Gets the value of the nfci property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNFCI() {
        return nfci;
    }

    /**
     * Sets the value of the nfci property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNFCI(String value) {
        this.nfci = value;
    }

    /**
     * Gets the value of the veicProd property.
     * 
     * @return
     *     possible object is
     *     {@link VeicProd }
     *     
     */
    public VeicProd getVeicProd() {
        return veicProd;
    }

    /**
     * Sets the value of the veicProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link VeicProd }
     *     
     */
    public void setVeicProd(VeicProd value) {
        this.veicProd = value;
    }

    /**
     * Gets the value of the med property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the med property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMed().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Med }
     * 
     * 
     */
    public List<Med> getMed() {
        if (med == null) {
            med = new ArrayList<Med>();
        }
        return this.med;
    }

    /**
     * Gets the value of the arma property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the arma property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArma().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Arma }
     * 
     * 
     */
    public List<Arma> getArma() {
        if (arma == null) {
            arma = new ArrayList<Arma>();
        }
        return this.arma;
    }

    /**
     * Gets the value of the comb property.
     * 
     * @return
     *     possible object is
     *     {@link Comb }
     *     
     */
    public Comb getComb() {
        return comb;
    }

    /**
     * Sets the value of the comb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comb }
     *     
     */
    public void setComb(Comb value) {
        this.comb = value;
    }

}
