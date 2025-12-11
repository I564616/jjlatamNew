
package com.jnj.outboundservice.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pag complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pag">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="detPag" type="{http://www.portalfiscal.inf.br/nfe}detPag" maxOccurs="unbounded" form="qualified"/>
 *         &lt;element name="vTroco" type="{http://www.portalfiscal.inf.br/nfe}TDec_1302" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pag", propOrder = {
    "detPag",
    "vTroco"
})
public class Pag {

    @XmlElement(required = true)
    protected List<DetPag> detPag;
    protected String vTroco;

    /**
     * Gets the value of the detPag property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detPag property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetPag().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DetPag }
     * 
     * 
     */
    public List<DetPag> getDetPag() {
        if (detPag == null) {
            detPag = new ArrayList<DetPag>();
        }
        return this.detPag;
    }

    /**
     * Gets the value of the vTroco property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVTroco() {
        return vTroco;
    }

    /**
     * Sets the value of the vTroco property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVTroco(String value) {
        this.vTroco = value;
    }

}
