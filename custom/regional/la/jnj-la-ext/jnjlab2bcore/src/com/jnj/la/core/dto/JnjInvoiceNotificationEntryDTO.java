/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.la.core.dto;

public class JnjInvoiceNotificationEntryDTO {
    private String productName;    
    private String productcode;
    private String quantity;
    private String shipUOM;
    private String estimatedDeliveryDate;
    private String status;

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }
  
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(final String quantity) {
        this.quantity = quantity;
    }

    public String getShipUOM() {
        return shipUOM;
    }

    public void setShipUOM(final String shipUOM) {
        this.shipUOM = shipUOM;
    }

    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(final String estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(final String productcode) {
        this.productcode = productcode;
    }


}