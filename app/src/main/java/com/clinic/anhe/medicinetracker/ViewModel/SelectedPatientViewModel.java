package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;

public class SelectedPatientViewModel extends ViewModel {
    private MutableLiveData<PatientsCardViewModel> patientLiveData = new MutableLiveData<>();

    public SelectedPatientViewModel(){
        PatientsCardViewModel p = new PatientsCardViewModel(-1, "","","", "");
        patientLiveData.postValue(p);
    }

    public MutableLiveData<PatientsCardViewModel> getPatientLiveData() {
        return patientLiveData;
    }

    public PatientsCardViewModel getPatient(){ return patientLiveData.getValue();}
}
