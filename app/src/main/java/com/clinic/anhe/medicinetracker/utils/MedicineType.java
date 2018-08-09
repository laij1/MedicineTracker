package com.clinic.anhe.medicinetracker.utils;

public enum MedicineType {

    dialysis("dialysis"),
    edible("edible"),
    needle("needle"),
    bandaid("bandaid");


    private final String name;

    private MedicineType(String s){
        this.name = s;
    }

    public String toString() {
        return this.name;
    }

    public static MedicineType fromString(String s) {
        for (MedicineType medicineType : MedicineType.values()) {
            if (medicineType.toString().equalsIgnoreCase(s)) {
                return medicineType;
            }
        }
        return null;
    }
}
