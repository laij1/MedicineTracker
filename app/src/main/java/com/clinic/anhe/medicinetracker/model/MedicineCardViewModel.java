package com.clinic.anhe.medicinetracker.model;

import com.clinic.anhe.medicinetracker.utils.PaymentType;

public class MedicineCardViewModel {

//    private int medicineIcon = -1;
    private String medicinName;
    private String medicinePrice;
    private String medicineDose;
    private Integer medicineId;
    private Integer medicineStock;
    private String medicineCategory;
    private PaymentType cashPayment = PaymentType.UNSELECT;
    private boolean isAddToCart = false;
    private int quantity = 0;
    private float sliderPosition = 0f;


    public MedicineCardViewModel(
            Integer medicineId, String medicineName, String medicinePrice, String medicineDose, Integer medicineStock, String medicineCategory) {
        this.medicineId = medicineId;
        this.medicinName = medicineName;
        this.medicinePrice = medicinePrice;
        this.medicineDose = medicineDose;
        this.medicineStock = medicineStock;
        this.medicineCategory = medicineCategory;
    }

    public String getMedicinName(){
        return medicinName;
    }

    public String getMedicinePrice(){
        return medicinePrice;
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

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getMedicineStock() {
        return medicineStock;
    }

    public void setMedicineStock(Integer medicineStock) {
        this.medicineStock = medicineStock;
    }

    public void setMedicineCategory(String medicineCategory) { this.medicineCategory = medicineCategory; }

    public String getMedicineCategory() { return medicineCategory; }
}
