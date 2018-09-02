package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardViewModel extends ViewModel {

//    private MutableLiveData<Map<String, List<String>>> dashboardLiveData;
//    private Map<String, List<String>> dashboardMap;

    private MutableLiveData<List<String>> selectedPatientsLiveData;
    private List<String> patientList;
    private MutableLiveData<String> nurseLiveData;
    private MutableLiveData<List<ShiftRecordModel>> shiftRecordListLiveData;
    private List<ShiftRecordModel> shiftRecordList;


    public DashboardViewModel(){
//        dashboardLiveData = new MutableLiveData<>();
//        dashboardMap = new HashMap<>();

        selectedPatientsLiveData = new MutableLiveData<>();
        patientList = new ArrayList<>();
        selectedPatientsLiveData.setValue(patientList);

        nurseLiveData = new MutableLiveData<>();
        shiftRecordListLiveData = new MutableLiveData<>();
        shiftRecordList = new ArrayList<>();
        shiftRecordListLiveData.setValue(shiftRecordList);
//        dashboardMap.put("test",selectedPatientsLiveData.getValue());
//        dashboardLiveData.setValue(dashboardMap);
    }

//    public Map<String, List<String>> getDashboardMap() {
//        return dashboardLiveData.getValue();

//    }

    public MutableLiveData<String> getNurseLiveData(){ return nurseLiveData; }

    public List<String> getSelectedPatientsList(){
        return selectedPatientsLiveData.getValue();
    }

    public MutableLiveData<List<String>> getSelectedPatientsLiveData()  { return selectedPatientsLiveData; }

    public MutableLiveData<List<ShiftRecordModel>> getShiftRecordListLiveData(){ return shiftRecordListLiveData; }

//    public MutableLiveData<Map<String, List<String>>> getDashboardMapLiveData() {
//        return dashboardLiveData;
//    }
}
