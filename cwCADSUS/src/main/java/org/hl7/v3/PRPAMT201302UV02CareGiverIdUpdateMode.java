
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PRPA_MT201302UV02.CareGiver.id.updateMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PRPA_MT201302UV02.CareGiver.id.updateMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PRPA_MT201302UV02.CareGiver.id.updateMode")
@XmlEnum
public enum PRPAMT201302UV02CareGiverIdUpdateMode {

    A,
    N;

    public String value() {
        return name();
    }

    public static PRPAMT201302UV02CareGiverIdUpdateMode fromValue(String v) {
        return valueOf(v);
    }

}
