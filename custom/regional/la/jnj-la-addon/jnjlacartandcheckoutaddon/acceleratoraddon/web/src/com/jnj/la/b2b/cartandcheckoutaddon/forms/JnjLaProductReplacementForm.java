/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.b2b.cartandcheckoutaddon.forms;

public class JnjLaProductReplacementForm {

    private String originalCartEntryNumber;
    private String originalProductCode;
    private String replacementProductQuantity;
    private String replacementProductCode;

    public String getOriginalCartEntryNumber() {
        return originalCartEntryNumber;
    }

    public void setOriginalCartEntryNumber(final String originalCartEntryNumber) {
        this.originalCartEntryNumber = originalCartEntryNumber;
    }

    public String getOriginalProductCode() {
        return originalProductCode;
    }

    public void setOriginalProductCode(final String originalProductCode) {
        this.originalProductCode = originalProductCode;
    }

    public String getReplacementProductQuantity() {
        return replacementProductQuantity;
    }

    public void setReplacementProductQuantity(final String replacementProductQuantity) {
        this.replacementProductQuantity = replacementProductQuantity;
    }

    public String getReplacementProductCode() {
        return replacementProductCode;
    }

    public void setReplacementProductCode(final String replacementProductCode) {
        this.replacementProductCode = replacementProductCode;
    }
}