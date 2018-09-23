package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMedicineRecordViewModel extends ViewModel {


    MutableLiveData<EmployeeCardViewModel> selectedEmployee;
    MutableLiveData<List<EmployeeCardViewModel>> employListLiveData;
    List<EmployeeCardViewModel> employeeList;

    private MutableLiveData<Map<String, Integer >> patientMapLiveData;
    private Map<String, Integer> patientMap;


    public AddMedicineRecordViewModel() {

        employListLiveData = new MutableLiveData<>();
        employeeList = new ArrayList<>();
        employListLiveData.setValue(employeeList);

        selectedEmployee = new MutableLiveData<>();
        EmployeeCardViewModel e = new EmployeeCardViewModel("", -1,"");
        selectedEmployee.postValue(e);

        patientMapLiveData = new MutableLiveData<>();
        patientMap = new HashMap<>();
        patientMapLiveData.postValue(patientMap);


    }

    public MutableLiveData<EmployeeCardViewModel> getSelectedEmployee() {
        return selectedEmployee;
    }

    public MutableLiveData<List<EmployeeCardViewModel>> getEmployListLiveData() {
        return employListLiveData;
    }

    public MutableLiveData<Map<String, Integer>> getPatientMapLiveData() {
        return patientMapLiveData;
    }
}
