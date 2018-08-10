package com.clinic.anhe.medicinetracker.model;

import com.clinic.anhe.medicinetracker.utils.PaymentType;

public class MedicineCardViewModel {

//    private int medicineIcon = -1;
    private String medicinName;
    private String medicineId;
    private String medicineDose;
    private PaymentType cashPayment = PaymentType.UNSELECT;
    private boolean isAddToCart = false;
    private int quantity = 0;
    private float sliderPosition = 0f;


    public MedicineCardViewModel(String medicinName, String medicineId, String medicineDose) {
        this.medicinName = medicinName;
        this.medicineId = medicineId;
        this.medicineDose = medicineDose;
    }

    public String getMedicinName(){
        return medicinName;
    }

    public String getMedicineId(){
        return medicineId;
    }

    public String getMedicineDose() { return medicineDose; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int i) {
        quantity = i;
    }

    public void setCashPayment(boolean cash) {
        if(cash) {
            cashPayment = PaymentType.CASH;
        } else {
            cashPayment = PaymentType.MONTH;
        }
    }

    public float getSliderPosition() { return sliderPosition; }

    public void setSliderPosition(float f) {
        sliderPosition = f;
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
