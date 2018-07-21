package com.clinic.anhe.medicinetracker.model;

public class MedicineCardViewModel {

    private int medicineIcon = -1;
    private String medicinName;
    private String medicineId;

    public MedicineCardViewModel(String medicinName, String medicineId, int medicineIcon) {
        this.medicinName = medicinName;
        this.medicineId = medicineId;
        this.medicineIcon = medicineIcon;
    }

    public String getMedicinName(){
        return medicinName;
    }

    public String getMedicineId(){
        return medicineId;
    }

    public int getMedicineIcon() { return medicineIcon; }

}
