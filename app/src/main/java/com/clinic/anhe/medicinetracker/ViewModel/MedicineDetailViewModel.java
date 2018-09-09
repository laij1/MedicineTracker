package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineDetailViewModel extends ViewModel {

    private MutableLiveData<List<MedicineRecordCardViewModel>> medicineListLiveData;
    private List<MedicineRecordCardViewModel> medicineList;

    private MutableLiveData<Map<Integer, String>> patientMapLiveData;
    private Map<Integer,String> patientMap;

    public MedicineDetailViewModel () {
        medicineListLiveData = new MutableLiveData<>();
        medicineList = new ArrayList<>();
        medicineListLiveData.setValue(medicineList);

        patientMapLiveData = new MutableLiveData<>();
        patientMap = new HashMap<>();
        patientMapLiveData.postValue(patientMap);

    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getMedicineListLiveData() {
        return medicineListLiveData;
    }

    public MutableLiveData<Map<Integer, String>> getPatientMapLiveData() {
        return patientMapLiveData;
    }
}
