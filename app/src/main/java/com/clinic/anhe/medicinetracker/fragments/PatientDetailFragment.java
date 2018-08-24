package com.clinic.anhe.medicinetracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;

public class PatientDetailFragment extends Fragment {
    BottomNavigationView mBottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_detail, container, false);
        mBottomNavigationView = view.findViewById(R.id.patient_detail_bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                switch(item.getItemId()) {
                    case R.id.patient_detail_cash:
                        PatientDetailCashFragment patientDetailCashFragment = PatientDetailCashFragment.newInstance();
                        transaction.replace(R.id.patient_detail_container, patientDetailCashFragment)
                                   .commit();
                        break;
                    case R.id.patient_detail_month:
                        PatientDetailMonthFragment patientDetailMonthFragment = PatientDetailMonthFragment.newInstance();
                        transaction.replace(R.id.patient_detail_container, patientDetailMonthFragment)
                                   .commit();
                        break;
                    case R.id.patient_detail_search:
                        PatientDetailSearchFragment patientDetailSearchFragment = PatientDetailSearchFragment.newInstance();
                        transaction.replace(R.id.patient_detail_container, patientDetailSearchFragment)
                                   .commit();
                        break;
                }
                return false;
            }
        });
        return view;
    }
}
