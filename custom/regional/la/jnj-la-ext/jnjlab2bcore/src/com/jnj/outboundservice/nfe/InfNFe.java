
package com.jnj.outboundservice.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for infNFe complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="infNFe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ide" type="{http://www.portalfiscal.inf.br/nfe}ide" form="qualified"/>
 *         &lt;element name="emit" type="{http://www.portalfiscal.inf.br/nfe}emit" form="qualified"/>
 *         &lt;element name="avulsa" type="{http://www.portalfiscal.inf.br/nfe}avulsa" minOccurs="0" form="qualified"/>
 *         &lt;element name="dest" type="{http://www.portalfiscal.inf.br/nfe}dest" minOccurs="0" form="qualified"/>
 *         &lt;element name="retirada" type="{http://www.portalfiscal.inf.br/nfe}TLocal" minOccurs="0" form="qualified"/>
 *         &lt;element name="entrega" type="{http://www.portalfiscal.inf.br/nfe}TLocal" minOccurs="0" form="qualified"/>
 *         &lt;element name="autXML" type="{http://www.portalfiscal.inf.br/nfe}autXML" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="det" type="{http://www.portalfiscal.inf.br/nfe}det" maxOccurs="unbounded" form="qualified"/>
 *         &lt;element name="total" type="{http://www.portalfiscal.inf.br/nfe}total" form="qualified"/>
 *         &lt;element name="transp" type="{http://www.portalfiscal.inf.br/nfe}transp" form="qualified"/>
 *         &lt;element name="cobr" type="{http://www.portalfiscal.inf.br/nfe}cobr" minOccurs="0" form="qualified"/>
 *         &lt;element name="pag" type="{http://www.portalfiscal.inf.br/nfe}pag" form="qualified"/>
 *         &lt;element name="infAdic" type="{http://www.portalfiscal.inf.br/nfe}infAdic" minOccurs="0" form="qualified"/>
 *         &lt;element name="exporta" type="{http://www.portalfiscal.inf.br/nfe}exporta" minOccurs="0" form="qualified"/>
 *         &lt;element name="compra" type="{http://www.portalfiscal.inf.br/nfe}compra" minOccurs="0" form="qualified"/>
 *         &lt;element name="cana" type="{http://www.portalfiscal.inf.br/nfe}cana" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *       &lt;attribute name="versao" use="required" type="{http://www.portalfiscal.inf.br/nfe}TVerNFe" />
 *       &lt;attribute name="Id" use="required" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS}_x0040_Id" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "infNFe", propOrder = {
    "ide",
    "emit",
    "avulsa",
    "dest",
    "retirada",
    "entrega",
    "autXML",
    "det",
    "total",
    "transp",
    "cobr",
    "pag",
    "infAdic",
    "exporta",
    "compra",
    "cana"
})
public class InfNFe {

    @XmlElement(required = true)
    protected Ide ide;
    @XmlElement(required = true)
    protected Emit emit;
    protected Avulsa avulsa;
    protected Dest dest;
    protected TLocal retirada;
    protected TLocal entrega;
    protected List<AutXML> autXML;
    @XmlElement(required = true)
    protected List<Det> det;
    @XmlElement(required = true)
    protected Total total;
    @XmlElement(required = true)
    protected Transp transp;
    protected Cobr cobr;
    @XmlElement(required = true)
    protected Pag pag;
    protected InfAdic infAdic;
    protected Exporta exporta;
    protected Compra compra;
    protected Cana cana;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    @XmlAttribute(name = "Id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    /**
     * Gets the value of the ide property.
     * 
     * @return
     *     possible object is
     *     {@link Ide }
     *     
     */
    public Ide getIde() {
        return ide;
    }

    /**
     * Sets the value of the ide property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ide }
     *     
     */
    public void setIde(Ide value) {
        this.ide = value;
    }

    /**
     * Gets the value of the emit property.
     * 
     * @return
     *     possible object is
     *     {@link Emit }
     *     
     */
    public Emit getEmit() {
        return emit;
    }

    /**
     * Sets the value of the emit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Emit }
     *     
     */
    public void setEmit(Emit value) {
        this.emit = value;
    }

    /**
     * Gets the value of the avulsa property.
     * 
     * @return
     *     possible object is
     *     {@link Avulsa }
     *     
     */
    public Avulsa getAvulsa() {
        return avulsa;
    }

    /**
     * Sets the value of the avulsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link Avulsa }
     *     
     */
    public void setAvulsa(Avulsa value) {
        this.avulsa = value;
    }

    /**
     * Gets the value of the dest property.
     * 
     * @return
     *     possible object is
     *     {@link Dest }
     *     
     */
    public Dest getDest() {
        return dest;
    }

    /**
     * Sets the value of the dest property.
     * 
     * @param value
     *     allowed object is
     *     {@link Dest }
     *     
     */
    public void setDest(Dest value) {
        this.dest = value;
    }

    /**
     * Gets the value of the retirada property.
     * 
     * @return
     *     possible object is
     *     {@link TLocal }
     *     
     */
    public TLocal getRetirada() {
        return retirada;
    }

    /**
     * Sets the value of the retirada property.
     * 
     * @param value
     *     allowed object is
     *     {@link TLocal }
     *     
     */
    public void setRetirada(TLocal value) {
        this.retirada = value;
    }

    /**
     * Gets the value of the entrega property.
     * 
     * @return
     *     possible object is
     *     {@link TLocal }
     *     
     */
    public TLocal getEntrega() {
        return entrega;
    }

    /**
     * Sets the value of the entrega property.
     * 
     * @param value
     *     allowed object is
     *     {@link TLocal }
     *     
     */
    public void setEntrega(TLocal value) {
        this.entrega = value;
    }

    /**
     * Gets the value of the autXML property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the autXML property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAutXML().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AutXML }
     * 
     * 
     */
    public List<AutXML> getAutXML() {
        if (autXML == null) {
            autXML = new ArrayList<AutXML>();
        }
        return this.autXML;
    }

    /**
     * Gets the value of the det property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the det property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Det }
     * 
     * 
     */
    public List<Det> getDet() {
        if (det == null) {
            det = new ArrayList<Det>();
        }
        return this.det;
    }

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link Total }
     *     
     */
    public Total getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link Total }
     *     
     */
    public void setTotal(Total value) {
        this.total = value;
    }

    /**
     * Gets the value of the transp property.
     * 
     * @return
     *     possible object is
     *     {@link Transp }
     *     
     */
    public Transp getTransp() {
        return transp;
    }

    /**
     * Sets the value of the transp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transp }
     *     
     */
    public void setTransp(Transp value) {
        this.transp = value;
    }

    /**
     * Gets the value of the cobr property.
     * 
     * @return
     *     possible object is
     *     {@link Cobr }
     *     
     */
    public Cobr getCobr() {
        return cobr;
    }

    /**
     * Sets the value of the cobr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cobr }
     *     
     */
    public void setCobr(Cobr value) {
        this.cobr = value;
    }

    /**
     * Gets the value of the pag property.
     * 
     * @return
     *     possible object is
     *     {@link Pag }
     *     
     */
    public Pag getPag() {
        return pag;
    }

    /**
     * Sets the value of the pag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pag }
     *     
     */
    public void setPag(Pag value) {
        this.pag = value;
    }

    /**
     * Gets the value of the infAdic property.
     * 
     * @return
     *     possible object is
     *     {@link InfAdic }
     *     
     */
    public InfAdic getInfAdic() {
        return infAdic;
    }

    /**
     * Sets the value of the infAdic property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfAdic }
     *     
     */
    public void setInfAdic(InfAdic value) {
        this.infAdic = value;
    }

    /**
     * Gets the value of the exporta property.
     * 
     * @return
     *     possible object is
     *     {@link Exporta }
     *     
     */
    public Exporta getExporta() {
        return exporta;
    }

    /**
     * Sets the value of the exporta property.
     * 
     * @param value
     *     allowed object is
     *     {@link Exporta }
     *     
     */
    public void setExporta(Exporta value) {
        this.exporta = value;
    }

    /**
     * Gets the value of the compra property.
     * 
     * @return
     *     possible object is
     *     {@link Compra }
     *     
     */
    public Compra getCompra() {
        return compra;
    }

    /**
     * Sets the value of the compra property.
     * 
     * @param value
     *     allowed object is
     *     {@link Compra }
     *     
     */
    public void setCompra(Compra value) {
        this.compra = value;
    }

    /**
     * Gets the value of the cana property.
     * 
     * @return
     *     possible object is
     *     {@link Cana }
     *     
     */
    public Cana getCana() {
        return cana;
    }

    /**
     * Sets the value of the cana property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cana }
     *     
     */
    public void setCana(Cana value) {
        this.cana = value;
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

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
