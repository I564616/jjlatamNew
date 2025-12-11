
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for detExport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="detExport">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nDraw" type="{http://www.portalfiscal.inf.br/nfe}nDraw2" minOccurs="0" form="qualified"/>
 *         &lt;element name="exportInd" type="{http://www.portalfiscal.inf.br/nfe}exportInd" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detExport", propOrder = {
    "nDraw",
    "exportInd"
})
public class DetExport {

    protected String nDraw;
    protected ExportInd exportInd;

    /**
     * Gets the value of the nDraw property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNDraw() {
        return nDraw;
    }

    /**
     * Sets the value of the nDraw property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNDraw(String value) {
        this.nDraw = value;
    }

    /**
     * Gets the value of the exportInd property.
     * 
     * @return
     *     possible object is
     *     {@link ExportInd }
     *     
     */
    public ExportInd getExportInd() {
        return exportInd;
    }

    /**
     * Sets the value of the exportInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportInd }
     *     
     */
    public void setExportInd(ExportInd value) {
        this.exportInd = value;
    }

}
