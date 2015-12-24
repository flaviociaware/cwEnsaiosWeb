
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PRPA_MT201302UV02.NonPersonLivingSubject.id.updateMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PRPA_MT201302UV02.NonPersonLivingSubject.id.updateMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PRPA_MT201302UV02.NonPersonLivingSubject.id.updateMode")
@XmlEnum
public enum PRPAMT201302UV02NonPersonLivingSubjectIdUpdateMode {

    A,
    N;

    public String value() {
        return name();
    }

    public static PRPAMT201302UV02NonPersonLivingSubjectIdUpdateMode fromValue(String v) {
        return valueOf(v);
    }

}
