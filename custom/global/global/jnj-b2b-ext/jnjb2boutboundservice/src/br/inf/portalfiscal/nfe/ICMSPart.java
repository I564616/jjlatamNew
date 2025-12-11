
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMSPart complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMSPart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orig" type="{http://www.portalfiscal.inf.br/nfe}Torig" form="qualified"/>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST10" form="qualified"/>
 *         &lt;element name="modBC" type="{http://www.portalfiscal.inf.br/nfe}modBC7" form="qualified"/>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="pRedBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="pICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302" form="qualified"/>
 *         &lt;element name="vICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="modBCST" type="{http://www.portalfiscal.inf.br/nfe}modBCST5" form="qualified"/>
 *         &lt;element name="pMVAST" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="pRedBCST" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302Opc" minOccurs="0" form="qualified"/>
 *         &lt;element name="vBCST" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="pICMSST" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302" form="qualified"/>
 *         &lt;element name="vICMSST" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="pBCOp" type="{http://www.portalfiscal.inf.br/nfe}TDec_0302Opc" form="qualified"/>
 *         &lt;element name="UFST" type="{http://www.portalfiscal.inf.br/nfe}TUf" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMSPart", propOrder = {
    "orig",
    "cst",
    "modBC",
    "vbc",
    "pRedBC",
    "picms",
    "vicms",
    "modBCST",
    "pmvast",
    "pRedBCST",
    "vbcst",
    "picmsst",
    "vicmsst",
    "pbcOp",
    "ufst"
})
public class ICMSPart {

    @XmlElement(required = true)
    protected String orig;
    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(required = true)
    protected String modBC;
    @XmlElement(name = "vBC", required = true)
    protected String vbc;
    protected String pRedBC;
    @XmlElement(name = "pICMS", required = true)
    protected String picms;
    @XmlElement(name = "vICMS", required = true)
    protected String vicms;
    @XmlElement(required = true)
    protected String modBCST;
    @XmlElement(name = "pMVAST")
    protected String pmvast;
    protected String pRedBCST;
    @XmlElement(name = "vBCST", required = true)
    protected String vbcst;
    @XmlElement(name = "pICMSST", required = true)
    protected String picmsst;
    @XmlElement(name = "vICMSST", required = true)
    protected String vicmsst;
    @XmlElement(name = "pBCOp", required = true)
    protected String pbcOp;
    @XmlElement(name = "UFST", required = true)
    protected TUf ufst;

    /**
     * Gets the value of the orig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrig() {
        return orig;
    }

    /**
     * Sets the value of the orig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrig(String value) {
        this.orig = value;
    }

    /**
     * Gets the value of the cst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCST() {
        return cst;
    }

    /**
     * Sets the value of the cst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCST(String value) {
        this.cst = value;
    }

    /**
     * Gets the value of the modBC property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModBC() {
        return modBC;
    }

    /**
     * Sets the value of the modBC property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModBC(String value) {
        this.modBC = value;
    }

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
     * Gets the value of the pRedBC property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRedBC() {
        return pRedBC;
    }

    /**
     * Sets the value of the pRedBC property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRedBC(String value) {
        this.pRedBC = value;
    }

    /**
     * Gets the value of the picms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPICMS() {
        return picms;
    }

    /**
     * Sets the value of the picms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPICMS(String value) {
        this.picms = value;
    }

    /**
     * Gets the value of the vicms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMS() {
        return vicms;
    }

    /**
     * Sets the value of the vicms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMS(String value) {
        this.vicms = value;
    }

    /**
     * Gets the value of the modBCST property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModBCST() {
        return modBCST;
    }

    /**
     * Sets the value of the modBCST property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModBCST(String value) {
        this.modBCST = value;
    }

    /**
     * Gets the value of the pmvast property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPMVAST() {
        return pmvast;
    }

    /**
     * Sets the value of the pmvast property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPMVAST(String value) {
        this.pmvast = value;
    }

    /**
     * Gets the value of the pRedBCST property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRedBCST() {
        return pRedBCST;
    }

    /**
     * Sets the value of the pRedBCST property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRedBCST(String value) {
        this.pRedBCST = value;
    }

    /**
     * Gets the value of the vbcst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBCST() {
        return vbcst;
    }

    /**
     * Sets the value of the vbcst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBCST(String value) {
        this.vbcst = value;
    }

    /**
     * Gets the value of the picmsst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPICMSST() {
        return picmsst;
    }

    /**
     * Sets the value of the picmsst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPICMSST(String value) {
        this.picmsst = value;
    }

    /**
     * Gets the value of the vicmsst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVICMSST() {
        return vicmsst;
    }

    /**
     * Sets the value of the vicmsst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVICMSST(String value) {
        this.vicmsst = value;
    }

    /**
     * Gets the value of the pbcOp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPBCOp() {
        return pbcOp;
    }

    /**
     * Sets the value of the pbcOp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPBCOp(String value) {
        this.pbcOp = value;
    }

    /**
     * Gets the value of the ufst property.
     * 
     * @return
     *     possible object is
     *     {@link TUf }
     *     
     */
    public TUf getUFST() {
        return ufst;
    }

    /**
     * Sets the value of the ufst property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUf }
     *     
     */
    public void setUFST(TUf value) {
        this.ufst = value;
    }

}
