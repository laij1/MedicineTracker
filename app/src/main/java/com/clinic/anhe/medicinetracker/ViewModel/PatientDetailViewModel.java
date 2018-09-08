package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class PatientDetailViewModel extends ViewModel {

    private MutableLiveData<List<MedicineRecordCardViewModel>> searchListLiveData;

    private List<MedicineRecordCardViewModel> searchList;

    public PatientDetailViewModel() {

        searchListLiveData = new MutableLiveData<>();
        searchList = new ArrayList<>();
        searchListLiveData.setValue(searchList);

    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getSearchListLiveData() {
        return searchListLiveData;
    }

    public List<MedicineRecordCardViewModel> getSearchList(){
        return  searchListLiveData.getValue();
    }
}
