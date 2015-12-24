
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OropharyngealRoute.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OropharyngealRoute">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="OROPHARTA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OropharyngealRoute")
@XmlEnum
public enum OropharyngealRoute {

    OROPHARTA;

    public String value() {
        return name();
    }

    public static OropharyngealRoute fromValue(String v) {
        return valueOf(v);
    }

}
