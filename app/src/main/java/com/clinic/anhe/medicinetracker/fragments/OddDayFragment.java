package com.clinic.anhe.medicinetracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.PatientsRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;

import java.util.ArrayList;
import java.util.List;


public class OddDayFragment extends Fragment {
        //patients cardview
        private List<PatientsCardViewModel> patientList;
        private RecyclerView mRecyclerView;
        private PatientsRecyclerViewAdapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view  = inflater.inflate(R.layout.fragment_odd_day, container, false);

            preparePatientData();
            mRecyclerView = view.findViewById(R.id.odd_day_recyclerview);
           // mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mAdapter = new PatientsRecyclerViewAdapter(patientList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            return view;

        }


        private void preparePatientData() {
            patientList = new ArrayList<>();
            patientList.add(new PatientsCardViewModel("賴蓉瑩", "1234"));
            patientList.add(new PatientsCardViewModel("賴蓉瑩1", "12345"));
            patientList.add(new PatientsCardViewModel("賴蓉瑩2", "12346"));
            patientList.add(new PatientsCardViewModel("賴蓉瑩3", "12347"));
            patientList.add(new PatientsCardViewModel("賴蓉瑩4", "12348"));


        }
    }