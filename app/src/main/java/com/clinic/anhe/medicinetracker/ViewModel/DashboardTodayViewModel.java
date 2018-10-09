package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardTodayViewModel extends ViewModel {

    private MutableLiveData<List<MedicineRecordCardViewModel>> medicineRecordListLiveData;
    private List<MedicineRecordCardViewModel> medicineRecordList;

    public DashboardTodayViewModel(){
        medicineRecordListLiveData = new MutableLiveData<>();
        medicineRecordList = new ArrayList<>();
        medicineRecordListLiveData.setValue(medicineRecordList);
    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getMedicineRecordListLiveData() {
        return medicineRecordListLiveData;
    }
}
