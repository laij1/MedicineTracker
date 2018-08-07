package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;

public class SelectedPatientViewModel extends ViewModel {
    private MutableLiveData<PatientsCardViewModel> patientLiveData = new MutableLiveData<>();

    public SelectedPatientViewModel(){
        //patientLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PatientsCardViewModel> getPatientLiveData() {
        return patientLiveData;
    }

    public PatientsCardViewModel getPatient(){ return patientLiveData.getValue();}
}
