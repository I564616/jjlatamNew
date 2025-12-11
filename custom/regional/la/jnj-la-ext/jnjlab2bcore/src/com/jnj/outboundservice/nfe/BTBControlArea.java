
package com.jnj.outboundservice.nfe;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BTBControlArea complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BTBControlArea">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InterfaceNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UniqueTransactionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BusinessObjectName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TransactionDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SourceSystem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TargetSystem" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TotalRecordCount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CustomName01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomName02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomName03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomName04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomName05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomValue01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomValue02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomValue03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomValue04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomValue05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BTBControlArea", namespace = "http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", propOrder = {
    "interfaceNumber",
    "uniqueTransactionID",
    "businessObjectName",
    "transactionDateTime",
    "sourceSystem",
    "targetSystem",
    "totalRecordCount",
    "customName01",
    "customName02",
    "customName03",
    "customName04",
    "customName05",
    "customValue01",
    "customValue02",
    "customValue03",
    "customValue04",
    "customValue05"
})
public class BTBControlArea {

    @XmlElement(name = "InterfaceNumber", namespace = "", required = true, nillable = true)
    protected String interfaceNumber;
    @XmlElement(name = "UniqueTransactionID", namespace = "", required = true, nillable = true)
    protected String uniqueTransactionID;
    @XmlElement(name = "BusinessObjectName", namespace = "", required = true, nillable = true)
    protected String businessObjectName;
    @XmlElement(name = "TransactionDateTime", namespace = "", required = true, nillable = true)
    protected String transactionDateTime;
    @XmlElement(name = "SourceSystem", namespace = "", required = true, nillable = true)
    protected String sourceSystem;
    @XmlElement(name = "TargetSystem", namespace = "", nillable = true)
    protected List<String> targetSystem;
    @XmlElement(name = "TotalRecordCount", namespace = "", required = true, nillable = true)
    protected String totalRecordCount;
    @XmlElementRef(name = "CustomName01", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customName01;
    @XmlElementRef(name = "CustomName02", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customName02;
    @XmlElementRef(name = "CustomName03", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customName03;
    @XmlElementRef(name = "CustomName04", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customName04;
    @XmlElementRef(name = "CustomName05", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customName05;
    @XmlElementRef(name = "CustomValue01", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customValue01;
    @XmlElementRef(name = "CustomValue02", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customValue02;
    @XmlElementRef(name = "CustomValue03", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customValue03;
    @XmlElementRef(name = "CustomValue04", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customValue04;
    @XmlElementRef(name = "CustomValue05", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customValue05;

    /**
     * Gets the value of the interfaceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterfaceNumber() {
        return interfaceNumber;
    }

    /**
     * Sets the value of the interfaceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterfaceNumber(String value) {
        this.interfaceNumber = value;
    }

    /**
     * Gets the value of the uniqueTransactionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueTransactionID() {
        return uniqueTransactionID;
    }

    /**
     * Sets the value of the uniqueTransactionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueTransactionID(String value) {
        this.uniqueTransactionID = value;
    }

    /**
     * Gets the value of the businessObjectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessObjectName() {
        return businessObjectName;
    }

    /**
     * Sets the value of the businessObjectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessObjectName(String value) {
        this.businessObjectName = value;
    }

    /**
     * Gets the value of the transactionDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    /**
     * Sets the value of the transactionDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionDateTime(String value) {
        this.transactionDateTime = value;
    }

    /**
     * Gets the value of the sourceSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceSystem() {
        return sourceSystem;
    }

    /**
     * Sets the value of the sourceSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceSystem(String value) {
        this.sourceSystem = value;
    }

    /**
     * Gets the value of the targetSystem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the targetSystem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTargetSystem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTargetSystem() {
        if (targetSystem == null) {
            targetSystem = new ArrayList<String>();
        }
        return this.targetSystem;
    }

    /**
     * Gets the value of the totalRecordCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalRecordCount() {
        return totalRecordCount;
    }

    /**
     * Sets the value of the totalRecordCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalRecordCount(String value) {
        this.totalRecordCount = value;
    }

    /**
     * Gets the value of the customName01 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomName01() {
        return customName01;
    }

    /**
     * Sets the value of the customName01 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomName01(JAXBElement<String> value) {
        this.customName01 = value;
    }

    /**
     * Gets the value of the customName02 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomName02() {
        return customName02;
    }

    /**
     * Sets the value of the customName02 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomName02(JAXBElement<String> value) {
        this.customName02 = value;
    }

    /**
     * Gets the value of the customName03 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomName03() {
        return customName03;
    }

    /**
     * Sets the value of the customName03 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomName03(JAXBElement<String> value) {
        this.customName03 = value;
    }

    /**
     * Gets the value of the customName04 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomName04() {
        return customName04;
    }

    /**
     * Sets the value of the customName04 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomName04(JAXBElement<String> value) {
        this.customName04 = value;
    }

    /**
     * Gets the value of the customName05 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomName05() {
        return customName05;
    }

    /**
     * Sets the value of the customName05 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomName05(JAXBElement<String> value) {
        this.customName05 = value;
    }

    /**
     * Gets the value of the customValue01 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomValue01() {
        return customValue01;
    }

    /**
     * Sets the value of the customValue01 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomValue01(JAXBElement<String> value) {
        this.customValue01 = value;
    }

    /**
     * Gets the value of the customValue02 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomValue02() {
        return customValue02;
    }

    /**
     * Sets the value of the customValue02 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomValue02(JAXBElement<String> value) {
        this.customValue02 = value;
    }

    /**
     * Gets the value of the customValue03 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomValue03() {
        return customValue03;
    }

    /**
     * Sets the value of the customValue03 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomValue03(JAXBElement<String> value) {
        this.customValue03 = value;
    }

    /**
     * Gets the value of the customValue04 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomValue04() {
        return customValue04;
    }

    /**
     * Sets the value of the customValue04 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomValue04(JAXBElement<String> value) {
        this.customValue04 = value;
    }

    /**
     * Gets the value of the customValue05 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomValue05() {
        return customValue05;
    }

    /**
     * Sets the value of the customValue05 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomValue05(JAXBElement<String> value) {
        this.customValue05 = value;
    }

}
