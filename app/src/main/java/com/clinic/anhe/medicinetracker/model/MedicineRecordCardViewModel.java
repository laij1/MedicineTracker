package com.clinic.anhe.medicinetracker.model;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;

public class MedicineRecordCardViewModel {

    private String createAt;

    private Integer mid;

    private String chargeAt;

    private String medicineName;

    private String patientName;


    private Integer quantity;

    private String createBy;

    private String chargeBy;

    private Integer subtotal;

    private String payment;

    private Integer pid;

    private Integer rid;

    public  MedicineRecordCardViewModel(Integer rid, String createDate, Integer mid, String name, Integer quantity,
                                        Integer subtotal, String payment, Integer pid, String createBy) {

        this.rid = rid;
        this.createAt = createDate;
        this.medicineName = name;
        this.mid = mid;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.payment = payment;
        this.pid = pid;
        this.createBy = createBy;

    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getChargeAt() {
        return chargeAt;
    }

    public void setChargeAt(String chargeAt) {
        this.chargeAt = chargeAt;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }


    public String getPatientName() { return patientName; }

    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MedicineRecordCardViewModel)) {
            return false;
        }

        MedicineRecordCardViewModel record = (MedicineRecordCardViewModel) obj;

        return record.getRid()==rid;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + medicineName.hashCode();
        result = 31 * result + rid;
        result = 31 * result + patientName.hashCode();
        return result;
    }

    public String getChargeBy() {
        return chargeBy;
    }

    public void setChargeBy(String chargeBy) {
        this.chargeBy = chargeBy;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }



}
