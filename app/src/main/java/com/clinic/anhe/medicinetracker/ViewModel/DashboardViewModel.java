package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<Map<String, List<String>>> dashboardLiveData;
    private Map<String, List<String>> dashboardMap;

    private MutableLiveData<List<String>> selectedPatientsLiveData;
    private List<String> patientList;


    public DashboardViewModel(){
        dashboardLiveData = new MutableLiveData<>();
        dashboardMap = new HashMap<>();

        selectedPatientsLiveData = new MutableLiveData<>();
        patientList = new ArrayList<>();
        selectedPatientsLiveData.setValue(patientList);
        dashboardMap.put("test",selectedPatientsLiveData.getValue());
        dashboardLiveData.setValue(dashboardMap);
    }

    public Map<String, List<String>> getDashboardMap() {
        return dashboardLiveData.getValue();
    }

    public List<String> getSelectedPatientsList(){
        return selectedPatientsLiveData.getValue();
    }

    public MutableLiveData<List<String>> getSelectedPatientsLiveData()  { return selectedPatientsLiveData; }

    public MutableLiveData<Map<String, List<String>>> getDashboardMapLiveData() {
        return dashboardLiveData;
    }
}
