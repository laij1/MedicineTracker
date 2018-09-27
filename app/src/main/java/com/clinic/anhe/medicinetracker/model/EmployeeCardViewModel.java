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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof EmployeeCardViewModel)) {
            return false;
        }

        EmployeeCardViewModel employee = (EmployeeCardViewModel) obj;
        return employee.getEid() == eid;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + employeeName.hashCode();
        result = 31 * result + eid;
        return result;
    }


}
