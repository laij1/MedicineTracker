package com.clinic.anhe.medicinetracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.clinic.anhe.medicinetracker.R;

public class PatientsFragment  extends Fragment {

    View mPatientsView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPatientsView = inflater.inflate(R.layout.fragment_patients, container, false);
        return mPatientsView;
    }
}
