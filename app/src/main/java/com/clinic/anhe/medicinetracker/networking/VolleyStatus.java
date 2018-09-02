package com.clinic.anhe.medicinetracker.networking;


public enum  VolleyStatus {

    SUCCESS("success"),
    FAIL("fail"),
    UNKNOWN("unknown");

    private final String name;

    private VolleyStatus(String s){
        this.name = s;
    }

    public String toString() {
        return this.name;
    }
}
