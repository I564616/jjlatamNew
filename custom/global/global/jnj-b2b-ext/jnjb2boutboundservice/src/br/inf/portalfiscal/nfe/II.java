
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for II complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="II">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vBC" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vDespAdu" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vII" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *         &lt;element name="vIOF" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "II", propOrder = {
    "vbc",
    "vDespAdu",
    "vii",
    "viof"
})
public class II {

    @XmlElement(name = "vBC", required = true)
    protected String vbc;
    @XmlElement(required = true)
    protected String vDespAdu;
    @XmlElement(name = "vII", required = true)
    protected String vii;
    @XmlElement(name = "vIOF", required = true)
    protected String viof;

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
     * Gets the value of the vDespAdu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDespAdu() {
        return vDespAdu;
    }

    /**
     * Sets the value of the vDespAdu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDespAdu(String value) {
        this.vDespAdu = value;
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
     * Gets the value of the viof property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVIOF() {
        return viof;
    }

    /**
     * Sets the value of the viof property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVIOF(String value) {
        this.viof = value;
    }

}
