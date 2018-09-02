package com.clinic.anhe.medicinetracker.model;

public class ShiftRecordModel {

//    "sid": 7,
//            "createAt": "2018-09-02",
//            "nurse": "吳宏基",
//            "patient": "連朱網",
//            "shift": "早班",
//            "day": "二四六"

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


}
