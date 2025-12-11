
package com.jnj.outboundservice.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vol">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qVol" type="{http://www.portalfiscal.inf.br/nfe}qVol" minOccurs="0" form="qualified"/>
 *         &lt;element name="esp" type="{http://www.portalfiscal.inf.br/nfe}esp" minOccurs="0" form="qualified"/>
 *         &lt;element name="marca" type="{http://www.portalfiscal.inf.br/nfe}marca" minOccurs="0" form="qualified"/>
 *         &lt;element name="nVol" type="{http://www.portalfiscal.inf.br/nfe}nVol" minOccurs="0" form="qualified"/>
 *         &lt;element name="pesoL" type="{http://www.portalfiscal.inf.br/nfe}TDec_1203" minOccurs="0" form="qualified"/>
 *         &lt;element name="pesoB" type="{http://www.portalfiscal.inf.br/nfe}TDec_1203" minOccurs="0" form="qualified"/>
 *         &lt;element name="lacres" type="{http://www.portalfiscal.inf.br/nfe}lacres" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vol", propOrder = {
    "qVol",
    "esp",
    "marca",
    "nVol",
    "pesoL",
    "pesoB",
    "lacres"
})
public class Vol {

    protected String qVol;
    protected String esp;
    protected String marca;
    protected String nVol;
    protected String pesoL;
    protected String pesoB;
    protected List<Lacres> lacres;

    /**
     * Gets the value of the qVol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQVol() {
        return qVol;
    }

    /**
     * Sets the value of the qVol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQVol(String value) {
        this.qVol = value;
    }

    /**
     * Gets the value of the esp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsp() {
        return esp;
    }

    /**
     * Sets the value of the esp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsp(String value) {
        this.esp = value;
    }

    /**
     * Gets the value of the marca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Sets the value of the marca property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarca(String value) {
        this.marca = value;
    }

    /**
     * Gets the value of the nVol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNVol() {
        return nVol;
    }

    /**
     * Sets the value of the nVol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNVol(String value) {
        this.nVol = value;
    }

    /**
     * Gets the value of the pesoL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPesoL() {
        return pesoL;
    }

    /**
     * Sets the value of the pesoL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPesoL(String value) {
        this.pesoL = value;
    }

    /**
     * Gets the value of the pesoB property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPesoB() {
        return pesoB;
    }

    /**
     * Sets the value of the pesoB property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPesoB(String value) {
        this.pesoB = value;
    }

    /**
     * Gets the value of the lacres property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lacres property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLacres().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lacres }
     * 
     * 
     */
    public List<Lacres> getLacres() {
        if (lacres == null) {
            lacres = new ArrayList<Lacres>();
        }
        return this.lacres;
    }

}
