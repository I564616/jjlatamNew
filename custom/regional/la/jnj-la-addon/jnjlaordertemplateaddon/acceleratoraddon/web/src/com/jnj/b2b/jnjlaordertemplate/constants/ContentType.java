/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.b2b.jnjlaordertemplate.constants;

public enum ContentType {

    EXCEL("application/vnd.ms-excel");

    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
