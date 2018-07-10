package com.clinic.anhe.medicinetracker;
/*
* This model is for navigation menu Group Menu.
*
* */
public class GroupMenuModel {

    private String iconName = "";
    private int iconImg = -1; // menu icon resource id'

    public String getIconName() {
        return iconName;
    }
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    public int getIconImg() {
        return iconImg;
    }
    public void setIconImg(int iconImg) {
        this.iconImg = iconImg;
    }
}
