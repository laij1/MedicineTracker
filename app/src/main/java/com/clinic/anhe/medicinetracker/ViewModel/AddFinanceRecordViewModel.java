package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddFinanceRecordViewModel extends ViewModel {

    MutableLiveData<EmployeeCardViewModel> selectedEmployee;
    MutableLiveData<List<EmployeeCardViewModel>> employListLiveData;
    List<EmployeeCardViewModel> employeeList;

    public AddFinanceRecordViewModel(){
        employListLiveData = new MutableLiveData<>();
        employeeList = new ArrayList<>();
        employListLiveData.setValue(employeeList);

        selectedEmployee = new MutableLiveData<>();
        EmployeeCardViewModel e = new EmployeeCardViewModel("", -1,"");
        selectedEmployee.postValue(e);
    }

    public MutableLiveData<List<EmployeeCardViewModel>> getEmployListLiveData() {
        return employListLiveData;
    }

    public MutableLiveData<EmployeeCardViewModel> getSelectedEmployee() {
        return selectedEmployee;
    }
}
