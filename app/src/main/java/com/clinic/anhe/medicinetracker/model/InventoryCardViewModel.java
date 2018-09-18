package com.clinic.anhe.medicinetracker.model;

import com.clinic.anhe.medicinetracker.ViewModel.InventoryViewModel;

public class InventoryCardViewModel {
    private String createAt;
    private String medicineName;
    private Integer mid;
    private Integer amount;
    private Integer iid;

    public InventoryCardViewModel(Integer iid, Integer mid, String name, Integer amount, String createAt) {
        this.iid = iid;
        this.medicineName = name;
        this.mid = mid;
        this.amount = amount;
        this.createAt = createAt;
    }

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
