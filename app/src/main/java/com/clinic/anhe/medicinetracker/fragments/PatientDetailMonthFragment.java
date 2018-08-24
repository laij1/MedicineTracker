package com.clinic.anhe.medicinetracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;

public class PatientDetailMonthFragment extends Fragment {
    public static PatientDetailMonthFragment newInstance() {
        PatientDetailMonthFragment fragment = new PatientDetailMonthFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_detail_month, container, false);
        return view;
    }
}
