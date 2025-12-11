
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for forDia complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="forDia">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qtde" type="{http://www.portalfiscal.inf.br/nfe}TDec_1110v" form="qualified"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dia" use="required" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}_x0040_dia" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "forDia", propOrder = {
    "qtde"
})
public class ForDia {

    @XmlElement(required = true)
    protected String qtde;
    @XmlAttribute(name = "dia", required = true)
    protected String dia;

    /**
     * Gets the value of the qtde property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQtde() {
        return qtde;
    }

    /**
     * Sets the value of the qtde property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQtde(String value) {
        this.qtde = value;
    }

    /**
     * Gets the value of the dia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDia() {
        return dia;
    }

    /**
     * Sets the value of the dia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDia(String value) {
        this.dia = value;
    }

}
