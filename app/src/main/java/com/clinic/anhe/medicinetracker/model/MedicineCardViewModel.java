package com.clinic.anhe.medicinetracker.model;

public class MedicineCardViewModel {

    private String medicinName;
    private String medicineId;

    public MedicineCardViewModel(String medicinName, String medicineId) {
        this.medicinName = medicinName;
        this.medicineId = medicineId;
    }

    public String getMedicinName(){
        return medicinName;
    }

    public String getMedicineId(){
        return medicineId;
    }

}
