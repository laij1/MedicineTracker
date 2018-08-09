package com.clinic.anhe.medicinetracker.model;

import com.clinic.anhe.medicinetracker.utils.PaymentType;

public class MedicineCardViewModel {

//    private int medicineIcon = -1;
    private String medicinName;
    private String medicineId;
    private PaymentType cashPayment = PaymentType.UNSELECT;
    private boolean isAddToCart = false;

    public MedicineCardViewModel(String medicinName, String medicineId, int medicineIcon) {
        this.medicinName = medicinName;
        this.medicineId = medicineId;
//        this.medicineIcon = medicineIcon;
    }

    public String getMedicinName(){
        return medicinName;
    }

    public String getMedicineId(){
        return medicineId;
    }

//    public int getMedicineIcon() { return medicineIcon; }

    public void setCashPayment(boolean cash) {
        if(cash) {
            cashPayment = PaymentType.CASH;
        } else {
            cashPayment = PaymentType.MONTH;
        }
    }

    public PaymentType isCashPayment() {

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
