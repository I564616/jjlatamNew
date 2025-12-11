
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for receiveElectronicNotaFiscalFromHybrisWrapperResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="receiveElectronicNotaFiscalFromHybrisWrapperResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ElectronicNotaFiscalResponse" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}ElectronicNotaFiscalResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiveElectronicNotaFiscalFromHybrisWrapperResponse", namespace = "http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", propOrder = {
    "electronicNotaFiscalResponse"
})
@XmlRootElement(name = "ReceiveElectronicNotaFiscalFromHybrisWrapperResponse")
public class ReceiveElectronicNotaFiscalFromHybrisWrapperResponse {

    @XmlElement(name = "ElectronicNotaFiscalResponse", namespace = "", required = true, nillable = true)
    protected ElectronicNotaFiscalResponse electronicNotaFiscalResponse;

    /**
     * Gets the value of the electronicNotaFiscalResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ElectronicNotaFiscalResponse }
     *     
     */
    public ElectronicNotaFiscalResponse getElectronicNotaFiscalResponse() {
        return electronicNotaFiscalResponse;
    }

    /**
     * Sets the value of the electronicNotaFiscalResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElectronicNotaFiscalResponse }
     *     
     */
    public void setElectronicNotaFiscalResponse(ElectronicNotaFiscalResponse value) {
        this.electronicNotaFiscalResponse = value;
    }

}
