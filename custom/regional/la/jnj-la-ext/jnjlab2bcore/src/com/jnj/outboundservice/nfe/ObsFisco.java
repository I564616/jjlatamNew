
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obsFisco complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="obsFisco">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xTexto" type="{http://www.portalfiscal.inf.br/nfe}xTexto2" form="qualified"/>
 *       &lt;/sequence>
 *       &lt;attribute name="xCampo" use="required" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}_x0040_xCampo2" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obsFisco", propOrder = {
    "xTexto"
})
public class ObsFisco {

    @XmlElement(required = true)
    protected String xTexto;
    @XmlAttribute(name = "xCampo", required = true)
    protected String xCampo;

    /**
     * Gets the value of the xTexto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXTexto() {
        return xTexto;
    }

    /**
     * Sets the value of the xTexto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXTexto(String value) {
        this.xTexto = value;
    }

    /**
     * Gets the value of the xCampo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXCampo() {
        return xCampo;
    }

    /**
     * Sets the value of the xCampo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXCampo(String value) {
        this.xCampo = value;
    }

}
