package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class MedicineManageViewModel extends ViewModel {
    private MutableLiveData<List<MedicineRecordCardViewModel>> medicineListLiveData;
    private List<MedicineRecordCardViewModel> medicineList;

    public MedicineManageViewModel(){
        medicineListLiveData = new MutableLiveData<>();
        medicineList = new ArrayList<>();
        medicineListLiveData.setValue(medicineList);
    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getMedicineListLiveData() {
        return medicineListLiveData;
    }
}
