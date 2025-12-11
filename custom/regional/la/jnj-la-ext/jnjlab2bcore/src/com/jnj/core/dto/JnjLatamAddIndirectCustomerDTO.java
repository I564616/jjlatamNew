package com.jnj.core.dto;

public class JnjLatamAddIndirectCustomerDTO extends JnjAddIndirectCustomerDTO{

    private String bid;
    private String company;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
