
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TNfeProc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TNfeProc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NFe" type="{http://www.portalfiscal.inf.br/nfe}TNFe" form="qualified"/>
 *         &lt;element name="protNFe" type="{http://www.portalfiscal.inf.br/nfe}TProtNFe" form="qualified"/>
 *       &lt;/sequence>
 *       &lt;attribute name="versao" use="required" type="{http://www.portalfiscal.inf.br/nfe}TVerNFe" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TNfeProc", propOrder = {
    "nFe",
    "protNFe"
})
public class TNfeProc {

    @XmlElement(name = "NFe", required = true)
    protected TNFe nFe;
    @XmlElement(required = true)
    protected TProtNFe protNFe;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;

    /**
     * Gets the value of the nFe property.
     * 
     * @return
     *     possible object is
     *     {@link TNFe }
     *     
     */
    public TNFe getNFe() {
        return nFe;
    }

    /**
     * Sets the value of the nFe property.
     * 
     * @param value
     *     allowed object is
     *     {@link TNFe }
     *     
     */
    public void setNFe(TNFe value) {
        this.nFe = value;
    }

    /**
     * Gets the value of the protNFe property.
     * 
     * @return
     *     possible object is
     *     {@link TProtNFe }
     *     
     */
    public TProtNFe getProtNFe() {
        return protNFe;
    }

    /**
     * Sets the value of the protNFe property.
     * 
     * @param value
     *     allowed object is
     *     {@link TProtNFe }
     *     
     */
    public void setProtNFe(TProtNFe value) {
        this.protNFe = value;
    }

    /**
     * Gets the value of the versao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersao() {
        return versao;
    }

    /**
     * Sets the value of the versao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersao(String value) {
        this.versao = value;
    }

}
