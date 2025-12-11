
package br.inf.portalfiscal.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cobr complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cobr">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fat" type="{http://www.portalfiscal.inf.br/nfe}fat" minOccurs="0" form="qualified"/>
 *         &lt;element name="dup" type="{http://www.portalfiscal.inf.br/nfe}dup" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cobr", propOrder = {
    "fat",
    "dup"
})
public class Cobr {

    protected Fat fat;
    protected List<Dup> dup;

    /**
     * Gets the value of the fat property.
     * 
     * @return
     *     possible object is
     *     {@link Fat }
     *     
     */
    public Fat getFat() {
        return fat;
    }

    /**
     * Sets the value of the fat property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fat }
     *     
     */
    public void setFat(Fat value) {
        this.fat = value;
    }

    /**
     * Gets the value of the dup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Dup }
     * 
     * 
     */
    public List<Dup> getDup() {
        if (dup == null) {
            dup = new ArrayList<Dup>();
        }
        return this.dup;
    }

}
