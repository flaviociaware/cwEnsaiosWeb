
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PRPA_MT201302UV02.Patient.patientNonPersonLivingSubject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PRPA_MT201302UV02.Patient.patientNonPersonLivingSubject">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}PRPA_MT201302UV02.NonPersonLivingSubject">
 *       &lt;attribute name="updateMode" type="{urn:hl7-org:v3}PRPA_MT201302UV02.Patient.patientNonPersonLivingSubject.updateMode" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_MT201302UV02.Patient.patientNonPersonLivingSubject")
public class PRPAMT201302UV02PatientPatientNonPersonLivingSubject
    extends PRPAMT201302UV02NonPersonLivingSubject
{

    @XmlAttribute(name = "updateMode")
    protected PRPAMT201302UV02PatientPatientNonPersonLivingSubjectUpdateMode updateMode;

    /**
     * Gets the value of the updateMode property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAMT201302UV02PatientPatientNonPersonLivingSubjectUpdateMode }
     *     
     */
    public PRPAMT201302UV02PatientPatientNonPersonLivingSubjectUpdateMode getUpdateMode() {
        return updateMode;
    }

    /**
     * Sets the value of the updateMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAMT201302UV02PatientPatientNonPersonLivingSubjectUpdateMode }
     *     
     */
    public void setUpdateMode(PRPAMT201302UV02PatientPatientNonPersonLivingSubjectUpdateMode value) {
        this.updateMode = value;
    }

}
