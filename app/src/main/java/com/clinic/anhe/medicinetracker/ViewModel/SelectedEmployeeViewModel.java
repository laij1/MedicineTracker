package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;

public class SelectedEmployeeViewModel extends ViewModel {

    MutableLiveData<EmployeeCardViewModel> selectedEmployee;

    public SelectedEmployeeViewModel() {
        selectedEmployee = new MutableLiveData<>();
        EmployeeCardViewModel e = new EmployeeCardViewModel("", -1,"");
        selectedEmployee.postValue(e);
    }

    public MutableLiveData<EmployeeCardViewModel> getSelectedEmployee() {
        return selectedEmployee;
    }
}
