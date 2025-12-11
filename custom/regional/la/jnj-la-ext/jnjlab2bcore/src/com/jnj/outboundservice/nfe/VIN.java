
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VIN.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VIN">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;pattern value="[!-ÿ]{1}[ -ÿ]{0,}[!-ÿ]{1}|[!-ÿ]{1}"/>
 *     &lt;whiteSpace value="preserve"/>
 *     &lt;enumeration value="R"/>
 *     &lt;enumeration value="N"/>
 *     &lt;length value="1"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VIN")
@XmlEnum
public enum VIN {

    R,
    N;

    public String value() {
        return name();
    }

    public static VIN fromValue(String v) {
        return valueOf(v);
    }

}
