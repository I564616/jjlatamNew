
package com.jnj.outboundservice.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="modFrete" type="{http://www.portalfiscal.inf.br/nfe}modFrete" form="qualified"/>
 *         &lt;element name="transporta" type="{http://www.portalfiscal.inf.br/nfe}transporta" minOccurs="0" form="qualified"/>
 *         &lt;element name="retTransp" type="{http://www.portalfiscal.inf.br/nfe}retTransp" minOccurs="0" form="qualified"/>
 *         &lt;element name="veicTransp" type="{http://www.portalfiscal.inf.br/nfe}TVeiculo" minOccurs="0" form="qualified"/>
 *         &lt;element name="reboque" type="{http://www.portalfiscal.inf.br/nfe}TVeiculo" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="vagao" type="{http://www.portalfiscal.inf.br/nfe}vagao" minOccurs="0" form="qualified"/>
 *         &lt;element name="balsa" type="{http://www.portalfiscal.inf.br/nfe}balsa" minOccurs="0" form="qualified"/>
 *         &lt;element name="vol" type="{http://www.portalfiscal.inf.br/nfe}vol" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transp", propOrder = {
    "modFrete",
    "transporta",
    "retTransp",
    "veicTransp",
    "reboque",
    "vagao",
    "balsa",
    "vol"
})
public class Transp {

    @XmlElement(required = true)
    protected String modFrete;
    protected Transporta transporta;
    protected RetTransp retTransp;
    protected TVeiculo veicTransp;
    protected List<TVeiculo> reboque;
    protected String vagao;
    protected String balsa;
    protected List<Vol> vol;

    /**
     * Gets the value of the modFrete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModFrete() {
        return modFrete;
    }

    /**
     * Sets the value of the modFrete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModFrete(String value) {
        this.modFrete = value;
    }

    /**
     * Gets the value of the transporta property.
     * 
     * @return
     *     possible object is
     *     {@link Transporta }
     *     
     */
    public Transporta getTransporta() {
        return transporta;
    }

    /**
     * Sets the value of the transporta property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transporta }
     *     
     */
    public void setTransporta(Transporta value) {
        this.transporta = value;
    }

    /**
     * Gets the value of the retTransp property.
     * 
     * @return
     *     possible object is
     *     {@link RetTransp }
     *     
     */
    public RetTransp getRetTransp() {
        return retTransp;
    }

    /**
     * Sets the value of the retTransp property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetTransp }
     *     
     */
    public void setRetTransp(RetTransp value) {
        this.retTransp = value;
    }

    /**
     * Gets the value of the veicTransp property.
     * 
     * @return
     *     possible object is
     *     {@link TVeiculo }
     *     
     */
    public TVeiculo getVeicTransp() {
        return veicTransp;
    }

    /**
     * Sets the value of the veicTransp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TVeiculo }
     *     
     */
    public void setVeicTransp(TVeiculo value) {
        this.veicTransp = value;
    }

    /**
     * Gets the value of the reboque property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reboque property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReboque().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TVeiculo }
     * 
     * 
     */
    public List<TVeiculo> getReboque() {
        if (reboque == null) {
            reboque = new ArrayList<TVeiculo>();
        }
        return this.reboque;
    }

    /**
     * Gets the value of the vagao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVagao() {
        return vagao;
    }

    /**
     * Sets the value of the vagao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVagao(String value) {
        this.vagao = value;
    }

    /**
     * Gets the value of the balsa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBalsa() {
        return balsa;
    }

    /**
     * Sets the value of the balsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalsa(String value) {
        this.balsa = value;
    }

    /**
     * Gets the value of the vol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Vol }
     * 
     * 
     */
    public List<Vol> getVol() {
        if (vol == null) {
            vol = new ArrayList<Vol>();
        }
        return this.vol;
    }

}
