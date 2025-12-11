/*
 * Copyright: Copyright ©  2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.core.dto;

public class JnjLatamConsignmentIssueDTO extends JnjConsignmentIssueDTO{

    private String healthPlan;
    private String surgeryDate;
    private String observation;
    private String residentialQuarter;
    private String city;
    private String state;
    private String zipCode;
    private String billingOrReplacement;

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