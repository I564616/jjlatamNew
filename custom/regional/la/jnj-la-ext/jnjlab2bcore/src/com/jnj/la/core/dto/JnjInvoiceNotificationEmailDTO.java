/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.la.core.dto;

public class JnjInvoiceNotificationEmailDTO {
    private String sapOrderNumber;
    private String hybrisOrder;
    private String orderType;
    private String billingName;
    private String billingAddress;
    private String shippingAddress;
    private String orderChannel;
    private String notaFiscal;
    private String customerPO;
    private String unit;
    private String quantity;
    private String accountNumber;
    private String invoiceNumber;

    public String getHybrisOrder() {
        return hybrisOrder;
    }

    public void setHybrisOrder(final String hybrisOrder) {
        this.hybrisOrder = hybrisOrder;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(final String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(final String orderType) {
        this.orderType = orderType;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(final String billingName) {
        this.billingName = billingName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(final String orderChannel) {
        this.orderChannel = orderChannel;
    }


    public String getCustomerPO() {
        return customerPO;
    }

    public void setCustomerPO(final String customerPO) {
        this.customerPO = customerPO;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(final String quantity) {
        this.quantity = quantity;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(final String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSapOrderNumber() {
        return sapOrderNumber;
    }

    public void setSapOrderNumber(final String sapOrderNumber) {
        this.sapOrderNumber = sapOrderNumber;
    }

}