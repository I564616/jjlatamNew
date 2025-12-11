
package br.inf.portalfiscal.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for infAdic complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="infAdic">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="infAdFisco" type="{http://www.portalfiscal.inf.br/nfe}infAdFisco" minOccurs="0" form="qualified"/>
 *         &lt;element name="infCpl" type="{http://www.portalfiscal.inf.br/nfe}infCpl" minOccurs="0" form="qualified"/>
 *         &lt;element name="obsCont" type="{http://www.portalfiscal.inf.br/nfe}obsCont" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="obsFisco" type="{http://www.portalfiscal.inf.br/nfe}obsFisco" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *         &lt;element name="procRef" type="{http://www.portalfiscal.inf.br/nfe}procRef" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "infAdic", propOrder = {
    "infAdFisco",
    "infCpl",
    "obsCont",
    "obsFisco",
    "procRef"
})
public class InfAdic {

    protected String infAdFisco;
    protected String infCpl;
    protected List<ObsCont> obsCont;
    protected List<ObsFisco> obsFisco;
    protected List<ProcRef> procRef;

    /**
     * Gets the value of the infAdFisco property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfAdFisco() {
        return infAdFisco;
    }

    /**
     * Sets the value of the infAdFisco property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfAdFisco(String value) {
        this.infAdFisco = value;
    }

    /**
     * Gets the value of the infCpl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfCpl() {
        return infCpl;
    }

    /**
     * Sets the value of the infCpl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfCpl(String value) {
        this.infCpl = value;
    }

    /**
     * Gets the value of the obsCont property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the obsCont property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObsCont().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObsCont }
     * 
     * 
     */
    public List<ObsCont> getObsCont() {
        if (obsCont == null) {
            obsCont = new ArrayList<ObsCont>();
        }
        return this.obsCont;
    }

    /**
     * Gets the value of the obsFisco property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the obsFisco property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObsFisco().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObsFisco }
     * 
     * 
     */
    public List<ObsFisco> getObsFisco() {
        if (obsFisco == null) {
            obsFisco = new ArrayList<ObsFisco>();
        }
        return this.obsFisco;
    }

    /**
     * Gets the value of the procRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the procRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcRef }
     * 
     * 
     */
    public List<ProcRef> getProcRef() {
        if (procRef == null) {
            procRef = new ArrayList<ProcRef>();
        }
        return this.procRef;
    }

}
