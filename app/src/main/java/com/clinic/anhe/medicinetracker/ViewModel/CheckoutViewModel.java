package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class CheckoutViewModel extends ViewModel {

    private MutableLiveData<List<MedicineRecordCardViewModel>> cashCheckoutLiveData;
    private List<MedicineRecordCardViewModel> cashCheckoutList;

    private MutableLiveData<List<MedicineRecordCardViewModel>> monthCheckoutLiveData;
    private List<MedicineRecordCardViewModel> monthCheckoutList;

    public CheckoutViewModel() {
        cashCheckoutLiveData = new MutableLiveData<>();
        cashCheckoutList = new ArrayList<>();
        cashCheckoutLiveData.setValue(cashCheckoutList);

        monthCheckoutLiveData = new MutableLiveData<>();
        monthCheckoutList = new ArrayList<>();
        monthCheckoutLiveData.setValue(monthCheckoutList);
    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getCashCheckoutLiveData() {
        return cashCheckoutLiveData;
    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getMonthCheckoutLiveData() {
        return monthCheckoutLiveData;
    }
}
