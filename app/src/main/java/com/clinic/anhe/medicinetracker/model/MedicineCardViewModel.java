package com.clinic.anhe.medicinetracker.model;

public class MedicineCardViewModel {

    private int medicineIcon = -1;
    private String medicinName;
    private String medicineId;
    private boolean cashPayment = true;
    private boolean isAddToCart = false;

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

    public void setCashPayment(boolean cash) {
        if(cash) {
            cashPayment = true;
        } else {
            cashPayment = false;
        }
    }

    public boolean IsCashPayment() {
        return cashPayment;
    }

    public void addToCart() {
        isAddToCart = true;
    }

    public void removeFromCart(){
        isAddToCart = false;
    }

    public boolean getIsAddToCart() {
        return isAddToCart;
    }

}
