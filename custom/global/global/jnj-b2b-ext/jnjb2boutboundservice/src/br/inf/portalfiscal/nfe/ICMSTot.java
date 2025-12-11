
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMSTot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMSTot">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vICMS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vBCST" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vST" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vProd" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vFrete" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vSeg" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vDesc" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vII" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vIPI" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vPIS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vCOFINS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vOutro" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vNF" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vTotTrib" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMSTot", propOrder = {
    "vbc",
    "vicms",
    "vbcst",
    "vst",
    "vProd",
    "vFrete",
    "vSeg",
    "vDesc",
    "vii",
    "vipi",
    "vpis",
    "vcofins",
    "vOutro",
    "vnf",
    "vTotTrib"
})
public class ICMSTot {

    @XmlElement(name = "vBC", required = true)
    protected String vbc;
    @XmlElement(name = "vICMS", required = true)
    protected String vicms;
    @XmlElement(name = "vBCST", required = true)
    protected String vbcst;
    @XmlElement(name = "vST", required = true)
    protected String vst;
    @XmlElement(required = true)
    protected String vProd;
    @XmlElement(required = true)
    protected String vFrete;
    @XmlElement(required = true)
    protected String vSeg;
    @XmlElement(required = true)
    protected String vDesc;
    @XmlElement(name = "vII", required = true)
    protected String vii;
    @XmlElement(name = "vIPI", required = true)
    protected String vipi;
    @XmlElement(name = "vPIS", required = true)
    protected String vpis;
    @XmlElement(name = "vCOFINS", required = true)
    protected String vcofins;
    @XmlElement(required = true)
    protected String vOutro;
    @XmlElement(name = "vNF", required = true)
    protected String vnf;
    protected String vTotTrib;

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
     * Gets the value of the vst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVST() {
        return vst;
    }

    /**
     * Sets the value of the vst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVST(String value) {
        this.vst = value;
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
     * Gets the value of the vii property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVII() {
        return vii;
    }

    /**
     * Sets the value of the vii property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVII(String value) {
        this.vii = value;
    }

    /**
     * Gets the value of the vipi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVIPI() {
        return vipi;
    }

    /**
     * Sets the value of the vipi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVIPI(String value) {
        this.vipi = value;
    }

    /**
     * Gets the value of the vpis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVPIS() {
        return vpis;
    }

    /**
     * Sets the value of the vpis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVPIS(String value) {
        this.vpis = value;
    }

    /**
     * Gets the value of the vcofins property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVCOFINS() {
        return vcofins;
    }

    /**
     * Sets the value of the vcofins property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVCOFINS(String value) {
        this.vcofins = value;
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
     * Gets the value of the vnf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNF() {
        return vnf;
    }

    /**
     * Sets the value of the vnf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNF(String value) {
        this.vnf = value;
    }

    /**
     * Gets the value of the vTotTrib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVTotTrib() {
        return vTotTrib;
    }

    /**
     * Sets the value of the vTotTrib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVTotTrib(String value) {
        this.vTotTrib = value;
    }

}
