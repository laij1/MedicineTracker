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
import com.clinic.anhe.medicinetracker.utils.MainActivity;
import com.clinic.anhe.medicinetracker.utils.Shift;

import java.util.ArrayList;
import java.util.List;


public class EvenDayFragment extends Fragment {

    //patients cardview
    private List<PatientsCardViewModel> patientList;
    private RecyclerView mRecyclerView;
    private PatientsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Shift shift;

    public EvenDayFragment(Shift shift) {
        this.shift = shift;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_even_day, container, false);
        prepareEvenDayPatientData();
        mRecyclerView = view.findViewById(R.id.even_day_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new PatientsRecyclerViewAdapter(patientList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;

    }

    private void prepareEvenDayPatientData() {
        patientList = new ArrayList<>();
        switch(shift) {
            case morning:
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩", "1234"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩1", "12345"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩2", "12346"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩3", "12347"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩4", "12348"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩5", "1234"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩6", "12345"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩7", "12346"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩8", "12347"));
                patientList.add(new PatientsCardViewModel("M-E賴蓉瑩9", "12348"));
                break;
            case afternoon:
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩", "1234"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩1", "12345"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩2", "12346"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩3", "12347"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩4", "12348"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩5", "1234"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩6", "12345"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩7", "12346"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩8", "12347"));
                patientList.add(new PatientsCardViewModel("A-E賴蓉瑩9", "12348"));
                break;
            case night:
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩", "1234"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩1", "12345"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩2", "12346"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩3", "12347"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩4", "12348"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩5", "1234"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩6", "12345"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩7", "12346"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩8", "12347"));
                patientList.add(new PatientsCardViewModel("N-E賴蓉瑩9", "12348"));
                break;

        }
//        patientList.add(new PatientsCardViewModel("賴蓉瑩Even", "1234"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩1Even", "12345"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩2Even", "12346"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩3Even", "12347"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩4Even", "12348"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩5Even", "1234"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩6Even", "12345"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩7Even", "12346"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩8Even", "12347"));
//        patientList.add(new PatientsCardViewModel("賴蓉瑩9Even", "12348"));

    }

}