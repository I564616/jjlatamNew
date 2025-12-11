
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig.SignatureType;


/**
 * <p>Java class for TNFe complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TNFe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="infNFe" type="{http://www.portalfiscal.inf.br/nfe}infNFe" form="qualified"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TNFe", propOrder = {
    "infNFe",
    "signature"
})
public class TNFe {

    @XmlElement(required = true)
    protected InfNFe infNFe;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;

    /**
     * Gets the value of the infNFe property.
     * 
     * @return
     *     possible object is
     *     {@link InfNFe }
     *     
     */
    public InfNFe getInfNFe() {
        return infNFe;
    }

    /**
     * Sets the value of the infNFe property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfNFe }
     *     
     */
    public void setInfNFe(InfNFe value) {
        this.infNFe = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

}
