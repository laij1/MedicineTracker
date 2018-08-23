package com.clinic.anhe.medicinetracker.model;

public class PatientsCardViewModel {

    private String patientName;
    private String patientId;

    public PatientsCardViewModel(String patientName, String patientId) {
        this.patientName = patientName;
        this.patientId = patientId;
    }

    public String getPatientName(){
        return patientName;
    }

    public String getPatientId(){
        return patientId;
    }

    public void setPatientName(String name) { patientName = name; }

}
