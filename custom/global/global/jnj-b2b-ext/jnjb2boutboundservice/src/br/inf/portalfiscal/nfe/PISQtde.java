
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PISQtde complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PISQtde">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CST" type="{http://www.portalfiscal.inf.br/nfe}CST15" form="qualified"/>
 *         &lt;element name="qBCProd" type="{http://www.portalfiscal.inf.br/nfe}TDec_1204" form="qualified"/>
 *         &lt;element name="vAliqProd" type="{http://www.portalfiscal.inf.br/nfe}TDec_1104v" form="qualified"/>
 *         &lt;element name="vPIS" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PISQtde", propOrder = {
    "cst",
    "qbcProd",
    "vAliqProd",
    "vpis"
})
public class PISQtde {

    @XmlElement(name = "CST", required = true)
    protected String cst;
    @XmlElement(name = "qBCProd", required = true)
    protected String qbcProd;
    @XmlElement(required = true)
    protected String vAliqProd;
    @XmlElement(name = "vPIS", required = true)
    protected String vpis;

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
     * Gets the value of the qbcProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQBCProd() {
        return qbcProd;
    }

    /**
     * Sets the value of the qbcProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQBCProd(String value) {
        this.qbcProd = value;
    }

    /**
     * Gets the value of the vAliqProd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVAliqProd() {
        return vAliqProd;
    }

    /**
     * Sets the value of the vAliqProd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVAliqProd(String value) {
        this.vAliqProd = value;
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

}
