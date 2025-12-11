
package br.inf.portalfiscal.nfe;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cSitTrib.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cSitTrib">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="R"/>
 *     &lt;enumeration value="S"/>
 *     &lt;enumeration value="I"/>
 *     &lt;whiteSpace value="preserve"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cSitTrib")
@XmlEnum
public enum CSitTrib {

    N,
    R,
    S,
    I;

    public String value() {
        return name();
    }

    public static CSitTrib fromValue(String v) {
        return valueOf(v);
    }

}
