package com.clinic.anhe.medicinetracker.utils;

public enum Shift {
    morning("morning"),
    afternoon("afternoon"),
    night("night");

    private final String name;

    private Shift(String s){
        this.name = s;
    }

    public String toString() {
        return this.name;
    }

    public static Shift fromString(String s) {
        for (Shift shift : Shift.values()) {
            if (shift.toString().equalsIgnoreCase(s)) {
                return shift;
            }
        }
        return null;
    }
}
