package com.clinic.anhe.medicinetracker.model;

public class EmployeeCardViewModel {

    private int eid;
    private String employeeName;
    private String employeePosition;

    public EmployeeCardViewModel(String name, int eid, String position) {
        this.eid = eid;
        this.employeeName = name;
        this.employeePosition = position;
    }


    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

}
