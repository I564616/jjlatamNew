
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for indEscala.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="indEscala">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;whiteSpace value="preserve"/>
 *     &lt;enumeration value="S"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "indEscala")
@XmlEnum
public enum IndEscala {

    S,
    N;

    public String value() {
        return name();
    }

    public static IndEscala fromValue(String v) {
        return valueOf(v);
    }

}
