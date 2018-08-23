package com.clinic.anhe.medicinetracker.utils;

public enum DayType {

    oddDay("oddday"),
    evenDay("evenday");


    private final String name;

    private DayType(String s) {
        this.name = s;
    }

    public String toString() {
        return this.name;
    }

    public static DayType fromString(String s) {
        for (DayType dayType : DayType.values()) {
            if (dayType.toString().equalsIgnoreCase(s)) {
                return dayType;
            }
        }
        return null;
    }
}