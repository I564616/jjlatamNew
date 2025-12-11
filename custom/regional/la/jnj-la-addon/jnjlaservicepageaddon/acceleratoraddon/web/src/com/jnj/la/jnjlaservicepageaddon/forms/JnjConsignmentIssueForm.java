package com.jnj.la.jnjlaservicepageaddon.forms;

import java.util.List;

/**
 * @author skant3
 *
 */
public class JnjConsignmentIssueForm {

    private String contactEmail;
    private String contactPhone;
    private String soldTo;
    private String shipTo;
    private String specialStockPartner;
    private String orderReason;
    private String date;
    private String poNumber;
    private String replenishmentOrFillUpDoc;
    private String replenishmentOrFillUpNFE;
    private String patient;
    private String doctor;
    private List<String> item;
    private List<String> qty;
    private List<String> uom;
    private List<String> batchNumber;
    private List<String> folio;
    private List<String> price;
    private List<String> currency;
    private String customerName; // Customer linked to logged in user
    private String hospital; // Hospital linked to logged in user
    private String contactName; // Logged in user first name
    private String healthPlan;
    private String surgeryDate;
    private String observation;
    private String residentialQuarter;
    private String city;
    private String state;
    private String zipCode;
    private String billingOrReplacement;

    /**
     * @return the contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @param contactEmail the contactEmail to set
     */
    public void setContactEmail(final String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * @return the contactPhone
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * @param contactPhone the contactPhone to set
     */
    public void setContactPhone(final String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * @return the soldTo
     */
    public String getSoldTo() {
        return soldTo;
    }

    /**
     * @param soldTo the soldTo to set
     */
    public void setSoldTo(final String soldTo) {
        this.soldTo = soldTo;
    }

    /**
     * @return the shipTo
     */
    public String getShipTo() {
        return shipTo;
    }

    /**
     * @param shipTo the shipTo to set
     */
    public void setShipTo(final String shipTo) {
        this.shipTo = shipTo;
    }

    /**
     * @return the specialStockPartner
     */
    public String getSpecialStockPartner() {
        return specialStockPartner;
    }

    /**
     * @param specialStockPartner
     *            the specialStockPartner to set
     */
    public void setSpecialStockPartner(final String specialStockPartner) {
        this.specialStockPartner = specialStockPartner;
    }

    /**
     * @return the orderReason
     */
    public String getOrderReason() {
        return orderReason;
    }

    /**
     * @param orderReason the orderReason to set
     */
    public void setOrderReason(final String orderReason) {
        this.orderReason = orderReason;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(final String date) {
        this.date = date;
    }

    /**
     * @return the poNumber
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * @param poNumber the poNumber to set
     */
    public void setPoNumber(final String poNumber) {
        this.poNumber = poNumber;
    }

    /**
     * @return the replenishmentOrFillUpDoc
     */
    public String getReplenishmentOrFillUpDoc() {
        return replenishmentOrFillUpDoc;
    }

    /**
     * @param replenishmentOrFillUpDoc the replenishmentOrFillUpDoc to set
     */
    public void setReplenishmentOrFillUpDoc(final String replenishmentOrFillUpDoc) {
        this.replenishmentOrFillUpDoc = replenishmentOrFillUpDoc;
    }

    /**
     * @return the replenishmentOrFillUpNFE
     */
    public String getReplenishmentOrFillUpNFE() {
        return replenishmentOrFillUpNFE;
    }

    /**
     * @param replenishmentOrFillUpNFE the replenishmentOrFillUpNFE to set
     */
    public void setReplenishmentOrFillUpNFE(final String replenishmentOrFillUpNFE) {
        this.replenishmentOrFillUpNFE = replenishmentOrFillUpNFE;
    }

    /**
     * @return the patient
     */
    public String getPatient() {
        return patient;
    }

    /**
     * @param patient the patient to set
     */
    public void setPatient(final String patient) {
        this.patient = patient;
    }

    /**
     * @return the doctor
     */
    public String getDoctor() {
        return doctor;
    }

    /**
     * @param doctor the doctor to set
     */
    public void setDoctor(final String doctor) {
        this.doctor = doctor;
    }

    /**
     * @return the item
     */
    public List<String> getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(final List<String> item) {
        this.item = item;
    }

    /**
     * @return the qty
     */
    public List<String> getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(final List<String> qty) {
        this.qty = qty;
    }

    /**
     * @return the uom
     */
    public List<String> getUom() {
        return uom;
    }

    /**
     * @param uom the uom to set
     */
    public void setUom(final List<String> uom) {
        this.uom = uom;
    }

    /**
     * @return the batchNumber
     */
    public List<String> getBatchNumber() {
        return batchNumber;
    }

    /**
     * @param batchNumber
     *            the batchNumber to set
     */
    public void setBatchNumber(final List<String> batchNumber) {
        this.batchNumber = batchNumber;
    }

    /**
     * @return the folio
     */
    public List<String> getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(final List<String> folio) {
        this.folio = folio;
    }

    /**
     * @return the price
     */
    public List<String> getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(final List<String> price) {
        this.price = price;
    }

    /**
     * @return the currency
     */
    public List<String> getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(final List<String> currency) {
        this.currency = currency;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     *            the customerName to set
     */
    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the hospital
     */
    public String getHospital() {
        return hospital;
    }

    /**
     * @param hospital
     *            the hospital to set
     */
    public void setHospital(final String hospital) {
        this.hospital = hospital;
    }

    /**
     * @return the contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName
     *            the contactName to set
     */
    public void setContactName(final String contactName) {
        this.contactName = contactName;
    }

    public String getHealthPlan() {
        return healthPlan;
    }

    public void setHealthPlan(String healthPlan) {
        this.healthPlan = healthPlan;
    }

    public String getSurgeryDate() {
        return surgeryDate;
    }

    public void setSurgeryDate(String surgeryDate) {
        this.surgeryDate = surgeryDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getResidentialQuarter() {
        return residentialQuarter;
    }

    public void setResidentialQuarter(String residentialQuarter) {
        this.residentialQuarter = residentialQuarter;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getBillingOrReplacement() {
        return billingOrReplacement;
    }

    public void setBillingOrReplacement(String billingOrReplacement) {
        this.billingOrReplacement = billingOrReplacement;
    }
}
