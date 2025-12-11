/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.versioning.enumeration;

import java.util.Arrays;

public enum JnjLAVersionEnum {
    NONE(0, "NONE"),
    V3_0(1, "3.0"),
    V3_1(2, "3.1"),
    V3_2(3, "3.2"),
    V3_3(4, "3.3"),
    V3_4(5, "3.4");

    private Integer code;
    private String label;

    JnjLAVersionEnum(final Integer code, final String label) {
        this.code = code;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static String[] getVersionsGreaterThan(final JnjLAVersionEnum comparison) {
        return Arrays.stream(JnjLAVersionEnum.values()).
            filter(v -> v.getCode() > comparison.getCode()).
            map(JnjLAVersionEnum::getLabel).toArray(String[]::new);
    }

    public static JnjLAVersionEnum get(final String version) {
        return Arrays.stream(JnjLAVersionEnum.values()).filter(v -> v.getLabel().equals(version)).findFirst().orElse(NONE);
    }

}
