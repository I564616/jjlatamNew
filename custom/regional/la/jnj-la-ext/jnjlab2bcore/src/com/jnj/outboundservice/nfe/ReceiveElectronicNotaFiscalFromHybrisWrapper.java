
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for receiveElectronicNotaFiscalFromHybrisWrapper complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="receiveElectronicNotaFiscalFromHybrisWrapper">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="debug" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ElectronicNotaFiscalRequest" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}ElectronicNotaFiscalRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiveElectronicNotaFiscalFromHybrisWrapper", namespace = "http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", propOrder = {
    "debug",
    "electronicNotaFiscalRequest"
})
@XmlRootElement(name = "ReceiveElectronicNotaFiscalFromHybrisWrapper")
public class ReceiveElectronicNotaFiscalFromHybrisWrapper {

    @XmlElement(namespace = "", required = true, nillable = true)
    protected String debug;
    @XmlElement(name = "ElectronicNotaFiscalRequest", namespace = "", required = true, nillable = true)
    protected ElectronicNotaFiscalRequest electronicNotaFiscalRequest;

    /**
     * Gets the value of the debug property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebug() {
        return debug;
    }

    /**
     * Sets the value of the debug property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebug(String value) {
        this.debug = value;
    }

    /**
     * Gets the value of the electronicNotaFiscalRequest property.
     * 
     * @return
     *     possible object is
     *     {@link ElectronicNotaFiscalRequest }
     *     
     */
    public ElectronicNotaFiscalRequest getElectronicNotaFiscalRequest() {
        return electronicNotaFiscalRequest;
    }

    /**
     * Sets the value of the electronicNotaFiscalRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElectronicNotaFiscalRequest }
     *     
     */
    public void setElectronicNotaFiscalRequest(ElectronicNotaFiscalRequest value) {
        this.electronicNotaFiscalRequest = value;
    }

}
