package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.adapters.PatientsRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.Shift;

import java.util.ArrayList;
import java.util.List;


public class OddDayFragment extends Fragment implements ArgumentVariables {
        //patients cardview
        private List<PatientsCardViewModel> patientList;
        private RecyclerView mRecyclerView;
        private PatientsRecyclerViewAdapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        private Shift shift;
        private static SelectedPatientViewModel selectedPatientViewModel;

        public static OddDayFragment newInstance(Shift shift) {
            OddDayFragment fragment = new OddDayFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PATIENT_SHIFT, shift.toString());
            fragment.setArguments(args);
            return fragment;
        }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_PATIENT_SHIFT, shift.toString());
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view  = inflater.inflate(R.layout.fragment_odd_day, container, false);

            if(savedInstanceState != null) {
                shift = shift.fromString(savedInstanceState.getString(ARG_PATIENT_SHIFT));
            }

            if(shift == null) {
                shift = shift.fromString(getArguments().getString(ARG_PATIENT_SHIFT));
            }
            //TODO:
            selectedPatientViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(SelectedPatientViewModel.class);
            selectedPatientViewModel.getPatientLiveData().observe(this, new Observer<PatientsCardViewModel>() {
                @Override
                public void onChanged(@Nullable PatientsCardViewModel patientsCardViewModel) {
                    Log.d("I have the selected patient in OddDayFragment", patientsCardViewModel.getPatientName());
                }
            });
            prepareOddDayPatientData();
            mRecyclerView = view.findViewById(R.id.odd_day_recyclerview);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mAdapter = new PatientsRecyclerViewAdapter(patientList, selectedPatientViewModel);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            setRetainInstance(true);
            return view;

        }


        private void prepareOddDayPatientData() {
            patientList = new ArrayList<>();
            switch(shift) {
                case morning:
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩", "1234"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩1", "12345"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩2", "12346"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩3", "12347"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩4", "12348"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩5", "1234"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩6", "12345"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩7", "12346"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩8", "12347"));
                    patientList.add(new PatientsCardViewModel("M賴蓉瑩9", "12348"));
                    break;
                case afternoon:
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩", "1234"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩1", "12345"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩2", "12346"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩3", "12347"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩4", "12348"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩5", "1234"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩6", "12345"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩7", "12346"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩8", "12347"));
                    patientList.add(new PatientsCardViewModel("A賴蓉瑩9", "12348"));
                    break;
                case night:
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩", "1234"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩1", "12345"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩2", "12346"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩3", "12347"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩4", "12348"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩5", "1234"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩6", "12345"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩7", "12346"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩8", "12347"));
                    patientList.add(new PatientsCardViewModel("N賴蓉瑩9", "12348"));
                    break;

            }
//            patientList.add(new PatientsCardViewModel("賴蓉瑩", "1234"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩1", "12345"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩2", "12346"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩3", "12347"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩4", "12348"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩5", "1234"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩6", "12345"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩7", "12346"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩8", "12347"));
//            patientList.add(new PatientsCardViewModel("賴蓉瑩9", "12348"));

        }
    }