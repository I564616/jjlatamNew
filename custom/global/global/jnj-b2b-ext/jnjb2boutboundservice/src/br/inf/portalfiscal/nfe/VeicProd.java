
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for veicProd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="veicProd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tpOp" type="{http://www.portalfiscal.inf.br/nfe}tpOp" form="qualified"/>
 *         &lt;element name="chassi" type="{http://www.portalfiscal.inf.br/nfe}chassi" form="qualified"/>
 *         &lt;element name="cCor" type="{http://www.portalfiscal.inf.br/nfe}cCor" form="qualified"/>
 *         &lt;element name="xCor" type="{http://www.portalfiscal.inf.br/nfe}xCor" form="qualified"/>
 *         &lt;element name="pot" type="{http://www.portalfiscal.inf.br/nfe}pot" form="qualified"/>
 *         &lt;element name="cilin" type="{http://www.portalfiscal.inf.br/nfe}cilin" form="qualified"/>
 *         &lt;element name="pesoL" type="{http://www.portalfiscal.inf.br/nfe}pesoL" form="qualified"/>
 *         &lt;element name="pesoB" type="{http://www.portalfiscal.inf.br/nfe}pesoB" form="qualified"/>
 *         &lt;element name="nSerie" type="{http://www.portalfiscal.inf.br/nfe}nSerie" form="qualified"/>
 *         &lt;element name="tpComb" type="{http://www.portalfiscal.inf.br/nfe}tpComb" form="qualified"/>
 *         &lt;element name="nMotor" type="{http://www.portalfiscal.inf.br/nfe}nMotor" form="qualified"/>
 *         &lt;element name="CMT" type="{http://www.portalfiscal.inf.br/nfe}CMT" form="qualified"/>
 *         &lt;element name="dist" type="{http://www.portalfiscal.inf.br/nfe}dist" form="qualified"/>
 *         &lt;element name="anoMod" type="{http://www.portalfiscal.inf.br/nfe}anoMod" form="qualified"/>
 *         &lt;element name="anoFab" type="{http://www.portalfiscal.inf.br/nfe}anoFab" form="qualified"/>
 *         &lt;element name="tpPint" type="{http://www.portalfiscal.inf.br/nfe}tpPint" form="qualified"/>
 *         &lt;element name="tpVeic" type="{http://www.portalfiscal.inf.br/nfe}tpVeic" form="qualified"/>
 *         &lt;element name="espVeic" type="{http://www.portalfiscal.inf.br/nfe}espVeic" form="qualified"/>
 *         &lt;element name="VIN" type="{http://www.portalfiscal.inf.br/nfe}VIN" form="qualified"/>
 *         &lt;element name="condVeic" type="{http://www.portalfiscal.inf.br/nfe}condVeic" form="qualified"/>
 *         &lt;element name="cMod" type="{http://www.portalfiscal.inf.br/nfe}cMod" form="qualified"/>
 *         &lt;element name="cCorDENATRAN" type="{http://www.portalfiscal.inf.br/nfe}cCorDENATRAN" form="qualified"/>
 *         &lt;element name="lota" type="{http://www.portalfiscal.inf.br/nfe}lota" form="qualified"/>
 *         &lt;element name="tpRest" type="{http://www.portalfiscal.inf.br/nfe}tpRest" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "veicProd", propOrder = {
    "tpOp",
    "chassi",
    "cCor",
    "xCor",
    "pot",
    "cilin",
    "pesoL",
    "pesoB",
    "nSerie",
    "tpComb",
    "nMotor",
    "cmt",
    "dist",
    "anoMod",
    "anoFab",
    "tpPint",
    "tpVeic",
    "espVeic",
    "vin",
    "condVeic",
    "cMod",
    "cCorDENATRAN",
    "lota",
    "tpRest"
})
public class VeicProd {

    @XmlElement(required = true)
    protected String tpOp;
    @XmlElement(required = true)
    protected String chassi;
    @XmlElement(required = true)
    protected String cCor;
    @XmlElement(required = true)
    protected String xCor;
    @XmlElement(required = true)
    protected String pot;
    @XmlElement(required = true)
    protected String cilin;
    @XmlElement(required = true)
    protected String pesoL;
    @XmlElement(required = true)
    protected String pesoB;
    @XmlElement(required = true)
    protected String nSerie;
    @XmlElement(required = true)
    protected String tpComb;
    @XmlElement(required = true)
    protected String nMotor;
    @XmlElement(name = "CMT", required = true)
    protected String cmt;
    @XmlElement(required = true)
    protected String dist;
    @XmlElement(required = true)
    protected String anoMod;
    @XmlElement(required = true)
    protected String anoFab;
    @XmlElement(required = true)
    protected String tpPint;
    @XmlElement(required = true)
    protected String tpVeic;
    @XmlElement(required = true)
    protected String espVeic;
    @XmlElement(name = "VIN", required = true)
    protected VIN vin;
    @XmlElement(required = true)
    protected String condVeic;
    @XmlElement(required = true)
    protected String cMod;
    @XmlElement(required = true)
    protected String cCorDENATRAN;
    @XmlElement(required = true)
    protected String lota;
    @XmlElement(required = true)
    protected String tpRest;

    /**
     * Gets the value of the tpOp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpOp() {
        return tpOp;
    }

    /**
     * Sets the value of the tpOp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpOp(String value) {
        this.tpOp = value;
    }

    /**
     * Gets the value of the chassi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChassi() {
        return chassi;
    }

    /**
     * Sets the value of the chassi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChassi(String value) {
        this.chassi = value;
    }

    /**
     * Gets the value of the cCor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCCor() {
        return cCor;
    }

    /**
     * Sets the value of the cCor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCCor(String value) {
        this.cCor = value;
    }

    /**
     * Gets the value of the xCor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXCor() {
        return xCor;
    }

    /**
     * Sets the value of the xCor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXCor(String value) {
        this.xCor = value;
    }

    /**
     * Gets the value of the pot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPot() {
        return pot;
    }

    /**
     * Sets the value of the pot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPot(String value) {
        this.pot = value;
    }

    /**
     * Gets the value of the cilin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCilin() {
        return cilin;
    }

    /**
     * Sets the value of the cilin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCilin(String value) {
        this.cilin = value;
    }

    /**
     * Gets the value of the pesoL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPesoL() {
        return pesoL;
    }

    /**
     * Sets the value of the pesoL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPesoL(String value) {
        this.pesoL = value;
    }

    /**
     * Gets the value of the pesoB property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPesoB() {
        return pesoB;
    }

    /**
     * Sets the value of the pesoB property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPesoB(String value) {
        this.pesoB = value;
    }

    /**
     * Gets the value of the nSerie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNSerie() {
        return nSerie;
    }

    /**
     * Sets the value of the nSerie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNSerie(String value) {
        this.nSerie = value;
    }

    /**
     * Gets the value of the tpComb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpComb() {
        return tpComb;
    }

    /**
     * Sets the value of the tpComb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpComb(String value) {
        this.tpComb = value;
    }

    /**
     * Gets the value of the nMotor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNMotor() {
        return nMotor;
    }

    /**
     * Sets the value of the nMotor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNMotor(String value) {
        this.nMotor = value;
    }

    /**
     * Gets the value of the cmt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCMT() {
        return cmt;
    }

    /**
     * Sets the value of the cmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCMT(String value) {
        this.cmt = value;
    }

    /**
     * Gets the value of the dist property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDist() {
        return dist;
    }

    /**
     * Sets the value of the dist property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDist(String value) {
        this.dist = value;
    }

    /**
     * Gets the value of the anoMod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnoMod() {
        return anoMod;
    }

    /**
     * Sets the value of the anoMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnoMod(String value) {
        this.anoMod = value;
    }

    /**
     * Gets the value of the anoFab property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnoFab() {
        return anoFab;
    }

    /**
     * Sets the value of the anoFab property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnoFab(String value) {
        this.anoFab = value;
    }

    /**
     * Gets the value of the tpPint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpPint() {
        return tpPint;
    }

    /**
     * Sets the value of the tpPint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpPint(String value) {
        this.tpPint = value;
    }

    /**
     * Gets the value of the tpVeic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpVeic() {
        return tpVeic;
    }

    /**
     * Sets the value of the tpVeic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpVeic(String value) {
        this.tpVeic = value;
    }

    /**
     * Gets the value of the espVeic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEspVeic() {
        return espVeic;
    }

    /**
     * Sets the value of the espVeic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEspVeic(String value) {
        this.espVeic = value;
    }

    /**
     * Gets the value of the vin property.
     * 
     * @return
     *     possible object is
     *     {@link VIN }
     *     
     */
    public VIN getVIN() {
        return vin;
    }

    /**
     * Sets the value of the vin property.
     * 
     * @param value
     *     allowed object is
     *     {@link VIN }
     *     
     */
    public void setVIN(VIN value) {
        this.vin = value;
    }

    /**
     * Gets the value of the condVeic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCondVeic() {
        return condVeic;
    }

    /**
     * Sets the value of the condVeic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCondVeic(String value) {
        this.condVeic = value;
    }

    /**
     * Gets the value of the cMod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCMod() {
        return cMod;
    }

    /**
     * Sets the value of the cMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCMod(String value) {
        this.cMod = value;
    }

    /**
     * Gets the value of the cCorDENATRAN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCCorDENATRAN() {
        return cCorDENATRAN;
    }

    /**
     * Sets the value of the cCorDENATRAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCCorDENATRAN(String value) {
        this.cCorDENATRAN = value;
    }

    /**
     * Gets the value of the lota property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLota() {
        return lota;
    }

    /**
     * Sets the value of the lota property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLota(String value) {
        this.lota = value;
    }

    /**
     * Gets the value of the tpRest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpRest() {
        return tpRest;
    }

    /**
     * Sets the value of the tpRest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpRest(String value) {
        this.tpRest = value;
    }

}
