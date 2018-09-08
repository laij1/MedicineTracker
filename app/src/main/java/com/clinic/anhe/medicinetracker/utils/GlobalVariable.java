package com.clinic.anhe.medicinetracker.utils;

import android.app.Application;
import android.content.Context;

public class GlobalVariable extends Application {

    private static GlobalVariable globalVariable;

    private String ipaddress;
    private String port;

    public static GlobalVariable setInstance(Context context) {
            if (globalVariable == null) {
                globalVariable = (GlobalVariable) context;
            }
            return globalVariable;
    }

    public static GlobalVariable getInstance() {
        return globalVariable;
    }
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIpaddress() {

        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

}
