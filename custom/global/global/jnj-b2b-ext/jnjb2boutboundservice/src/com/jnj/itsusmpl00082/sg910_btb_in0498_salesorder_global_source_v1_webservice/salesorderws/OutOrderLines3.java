
package com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for out_orderLines3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="out_orderLines3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LineNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MaterialNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MaterialQuantity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SalesUOM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ItemCategory" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MaterialEntered" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HigherLevelItemNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ScheduledLines" type="{http://itsusmpl00082.jnj.com/SG910_BtB_IN0498_SalesOrder_Global_Source_v1.webService:salesOrderWS}ScheduledLines3" maxOccurs="unbounded"/>
 *         &lt;element name="ReasonForRejection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LineOverallStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FreightFees" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Taxes" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UnitValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GrossPrice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NetValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Discounts" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpeditedFees" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Insurance" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InternaitonalFreight" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HandlingFee" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DropshipFee" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MinimumOrderFee" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TaxesLocal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "out_orderLines3", propOrder = {
    "lineNumber",
    "materialNumber",
    "materialQuantity",
    "salesUOM",
    "itemCategory",
    "materialEntered",
    "higherLevelItemNumber",
    "scheduledLines",
    "reasonForRejection",
    "lineOverallStatus",
    "freightFees",
    "taxes",
    "unitValue",
    "grossPrice",
    "netValue",
    "discounts",
    "expeditedFees",
    "insurance",
    "internaitonalFreight",
    "handlingFee",
    "dropshipFee",
    "minimumOrderFee",
    "taxesLocal"
})
@XmlRootElement(name = "OutOrderLines3")
public class OutOrderLines3 {

    @XmlElement(name = "LineNumber", required = true)
    protected String lineNumber;
    @XmlElement(name = "MaterialNumber", required = true)
    protected String materialNumber;
    @XmlElement(name = "MaterialQuantity", required = true)
    protected String materialQuantity;
    @XmlElement(name = "SalesUOM", required = true)
    protected String salesUOM;
    @XmlElement(name = "ItemCategory", required = true)
    protected String itemCategory;
    @XmlElement(name = "MaterialEntered", required = true)
    protected String materialEntered;
    @XmlElement(name = "HigherLevelItemNumber", required = true)
    protected String higherLevelItemNumber;
    @XmlElement(name = "ScheduledLines", required = true, nillable = true)
    protected List<ScheduledLines3> scheduledLines;
    @XmlElementRef(name = "ReasonForRejection", type = JAXBElement.class, required = false)
    protected JAXBElement<String> reasonForRejection;
    @XmlElementRef(name = "LineOverallStatus", type = JAXBElement.class, required = false)
    protected JAXBElement<String> lineOverallStatus;
    @XmlElement(name = "FreightFees", required = true)
    protected String freightFees;
    @XmlElement(name = "Taxes", required = true)
    protected String taxes;
    @XmlElement(name = "UnitValue", required = true)
    protected String unitValue;
    @XmlElement(name = "GrossPrice", required = true)
    protected String grossPrice;
    @XmlElement(name = "NetValue", required = true)
    protected String netValue;
    @XmlElement(name = "Discounts", required = true)
    protected String discounts;
    @XmlElement(name = "ExpeditedFees", required = true)
    protected String expeditedFees;
    @XmlElement(name = "Insurance", required = true)
    protected String insurance;
    @XmlElement(name = "InternaitonalFreight", required = true)
    protected String internaitonalFreight;
    @XmlElement(name = "HandlingFee", required = true)
    protected String handlingFee;
    @XmlElement(name = "DropshipFee", required = true)
    protected String dropshipFee;
    @XmlElement(name = "MinimumOrderFee", required = true)
    protected String minimumOrderFee;
    @XmlElement(name = "TaxesLocal", required = true)
    protected String taxesLocal;

    /**
     * Gets the value of the lineNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the value of the lineNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineNumber(String value) {
        this.lineNumber = value;
    }

    /**
     * Gets the value of the materialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialNumber() {
        return materialNumber;
    }

    /**
     * Sets the value of the materialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialNumber(String value) {
        this.materialNumber = value;
    }

    /**
     * Gets the value of the materialQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialQuantity() {
        return materialQuantity;
    }

    /**
     * Sets the value of the materialQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialQuantity(String value) {
        this.materialQuantity = value;
    }

    /**
     * Gets the value of the salesUOM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesUOM() {
        return salesUOM;
    }

    /**
     * Sets the value of the salesUOM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesUOM(String value) {
        this.salesUOM = value;
    }

    /**
     * Gets the value of the itemCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemCategory() {
        return itemCategory;
    }

    /**
     * Sets the value of the itemCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemCategory(String value) {
        this.itemCategory = value;
    }

    /**
     * Gets the value of the materialEntered property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialEntered() {
        return materialEntered;
    }

    /**
     * Sets the value of the materialEntered property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialEntered(String value) {
        this.materialEntered = value;
    }

    /**
     * Gets the value of the higherLevelItemNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHigherLevelItemNumber() {
        return higherLevelItemNumber;
    }

    /**
     * Sets the value of the higherLevelItemNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHigherLevelItemNumber(String value) {
        this.higherLevelItemNumber = value;
    }

    /**
     * Gets the value of the scheduledLines property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scheduledLines property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScheduledLines().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScheduledLines3 }
     * 
     * 
     */
    public List<ScheduledLines3> getScheduledLines() {
        if (scheduledLines == null) {
            scheduledLines = new ArrayList<ScheduledLines3>();
        }
        return this.scheduledLines;
    }

    /**
     * Gets the value of the reasonForRejection property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReasonForRejection() {
        return reasonForRejection;
    }

    /**
     * Sets the value of the reasonForRejection property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReasonForRejection(JAXBElement<String> value) {
        this.reasonForRejection = value;
    }

    /**
     * Gets the value of the lineOverallStatus property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLineOverallStatus() {
        return lineOverallStatus;
    }

    /**
     * Sets the value of the lineOverallStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLineOverallStatus(JAXBElement<String> value) {
        this.lineOverallStatus = value;
    }

    /**
     * Gets the value of the freightFees property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreightFees() {
        return freightFees;
    }

    /**
     * Sets the value of the freightFees property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreightFees(String value) {
        this.freightFees = value;
    }

    /**
     * Gets the value of the taxes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxes() {
        return taxes;
    }

    /**
     * Sets the value of the taxes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxes(String value) {
        this.taxes = value;
    }

    /**
     * Gets the value of the unitValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitValue() {
        return unitValue;
    }

    /**
     * Sets the value of the unitValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitValue(String value) {
        this.unitValue = value;
    }

    /**
     * Gets the value of the grossPrice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrossPrice() {
        return grossPrice;
    }

    /**
     * Sets the value of the grossPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrossPrice(String value) {
        this.grossPrice = value;
    }

    /**
     * Gets the value of the netValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetValue() {
        return netValue;
    }

    /**
     * Sets the value of the netValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetValue(String value) {
        this.netValue = value;
    }

    /**
     * Gets the value of the discounts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscounts() {
        return discounts;
    }

    /**
     * Sets the value of the discounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscounts(String value) {
        this.discounts = value;
    }

    /**
     * Gets the value of the expeditedFees property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpeditedFees() {
        return expeditedFees;
    }

    /**
     * Sets the value of the expeditedFees property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpeditedFees(String value) {
        this.expeditedFees = value;
    }

    /**
     * Gets the value of the insurance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsurance() {
        return insurance;
    }

    /**
     * Sets the value of the insurance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsurance(String value) {
        this.insurance = value;
    }

    /**
     * Gets the value of the internaitonalFreight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternaitonalFreight() {
        return internaitonalFreight;
    }

    /**
     * Sets the value of the internaitonalFreight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternaitonalFreight(String value) {
        this.internaitonalFreight = value;
    }

    /**
     * Gets the value of the handlingFee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHandlingFee() {
        return handlingFee;
    }

    /**
     * Sets the value of the handlingFee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHandlingFee(String value) {
        this.handlingFee = value;
    }

    /**
     * Gets the value of the dropshipFee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDropshipFee() {
        return dropshipFee;
    }

    /**
     * Sets the value of the dropshipFee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDropshipFee(String value) {
        this.dropshipFee = value;
    }

    /**
     * Gets the value of the minimumOrderFee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinimumOrderFee() {
        return minimumOrderFee;
    }

    /**
     * Sets the value of the minimumOrderFee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinimumOrderFee(String value) {
        this.minimumOrderFee = value;
    }

    /**
     * Gets the value of the taxesLocal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxesLocal() {
        return taxesLocal;
    }

    /**
     * Sets the value of the taxesLocal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxesLocal(String value) {
        this.taxesLocal = value;
    }

}