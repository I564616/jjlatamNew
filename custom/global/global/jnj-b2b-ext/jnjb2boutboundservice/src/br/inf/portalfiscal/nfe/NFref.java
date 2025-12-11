
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NFref complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NFref">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="refNFe" type="{http://www.portalfiscal.inf.br/nfe}TChNFe" minOccurs="0" form="qualified"/>
 *         &lt;element name="refNF" type="{http://www.portalfiscal.inf.br/nfe}refNF" minOccurs="0" form="qualified"/>
 *         &lt;element name="refNFP" type="{http://www.portalfiscal.inf.br/nfe}refNFP" minOccurs="0" form="qualified"/>
 *         &lt;element name="refCTe" type="{http://www.portalfiscal.inf.br/nfe}TChNFe" minOccurs="0" form="qualified"/>
 *         &lt;element name="refECF" type="{http://www.portalfiscal.inf.br/nfe}refECF" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NFref", propOrder = {
    "refNFe",
    "refNF",
    "refNFP",
    "refCTe",
    "refECF"
})
public class NFref {

    protected String refNFe;
    protected RefNF refNF;
    protected RefNFP refNFP;
    protected String refCTe;
    protected RefECF refECF;

    /**
     * Gets the value of the refNFe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefNFe() {
        return refNFe;
    }

    /**
     * Sets the value of the refNFe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefNFe(String value) {
        this.refNFe = value;
    }

    /**
     * Gets the value of the refNF property.
     * 
     * @return
     *     possible object is
     *     {@link RefNF }
     *     
     */
    public RefNF getRefNF() {
        return refNF;
    }

    /**
     * Sets the value of the refNF property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefNF }
     *     
     */
    public void setRefNF(RefNF value) {
        this.refNF = value;
    }

    /**
     * Gets the value of the refNFP property.
     * 
     * @return
     *     possible object is
     *     {@link RefNFP }
     *     
     */
    public RefNFP getRefNFP() {
        return refNFP;
    }

    /**
     * Sets the value of the refNFP property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefNFP }
     *     
     */
    public void setRefNFP(RefNFP value) {
        this.refNFP = value;
    }

    /**
     * Gets the value of the refCTe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefCTe() {
        return refCTe;
    }

    /**
     * Sets the value of the refCTe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefCTe(String value) {
        this.refCTe = value;
    }

    /**
     * Gets the value of the refECF property.
     * 
     * @return
     *     possible object is
     *     {@link RefECF }
     *     
     */
    public RefECF getRefECF() {
        return refECF;
    }

    /**
     * Sets the value of the refECF property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefECF }
     *     
     */
    public void setRefECF(RefECF value) {
        this.refECF = value;
    }

}
