package com.clinic.anhe.medicinetracker.model;

import com.clinic.anhe.medicinetracker.utils.Shift;

public class ShiftRecordModel {

    private Integer sid;
    private String createAt;
    private String nurse;
    private String patient;
    private String shift;
    private String day;


    public ShiftRecordModel(Integer sid, String createAt, String nurse, String patient, String shift, String day) {
        this.sid = sid;
        this.createAt = createAt;
        this.nurse = nurse;
        this.patient = patient;

        this.shift = shift;
        this.day = day;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ShiftRecordModel)) {
            return false;
        }

        ShiftRecordModel s = (ShiftRecordModel) obj;
        return s.getSid()== sid;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + patient.hashCode();
        result = 31 * result + nurse.hashCode();
        result = 31 * result + sid;
        return result;
    }


}
