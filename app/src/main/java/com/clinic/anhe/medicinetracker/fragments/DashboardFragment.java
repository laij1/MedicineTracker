package com.clinic.anhe.medicinetracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.DashboardRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.PatientListRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private DashboardRecyclerViewAdapter mAdapter;
    private List<EmployeeCardViewModel> employeeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mRecyclerView = view.findViewById(R.id.dashboard_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 4);
        //TODO:here we need to filter
        prepareEmployeeData();
        mAdapter = new DashboardRecyclerViewAdapter(employeeList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        setRetainInstance(true);
        return view;
    }

    private void prepareEmployeeData() {
        employeeList = new ArrayList<>();
        employeeList.add(new EmployeeCardViewModel("test", 1, "test"));
        employeeList.add(new EmployeeCardViewModel("test2", 2, "test"));
        employeeList.add(new EmployeeCardViewModel("test3", 3, "test"));
        employeeList.add(new EmployeeCardViewModel("test4", 4, "test"));

        employeeList.add(new EmployeeCardViewModel("test5", 5, "test"));
        employeeList.add(new EmployeeCardViewModel("test6", 6, "test"));
        employeeList.add(new EmployeeCardViewModel("test7", 7, "test"));
        employeeList.add(new EmployeeCardViewModel("test8", 8, "test"));



    }


}
