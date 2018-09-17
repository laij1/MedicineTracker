package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import com.clinic.anhe.medicinetracker.ViewModel.CashFlowViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import java.util.HashMap;
import java.util.Map;

public class MedicineDetailFragment extends Fragment {

    BottomNavigationView mBottomNavigationView;
    private VolleyController volleyController;
    private Context mContext;
    private GlobalVariable globalVariable;
    private String medicineName;

    public static MedicineDetailFragment newInstance(String medicineName) {
        MedicineDetailFragment fragment = new MedicineDetailFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME, medicineName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME, medicineName);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_medicine_detail, container, false);
        mBottomNavigationView = view.findViewById(R.id.medicine_detail_bottom_navigation);

        mContext = view.getContext();

        if(savedInstanceState != null) {
            this.medicineName = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME);
        }

        if(this.medicineName == null) {
            this.medicineName = getArguments().getString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME);
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                switch (item.getItemId()) {

                    case R.id.medicine_detail_search:
                        MedicineDetailSearchFragment cashflowSearchFragment = MedicineDetailSearchFragment.newInstance(medicineName);
                        transaction.replace(R.id.medicine_detail_fragment_container, cashflowSearchFragment)
                                .commit();
                        break;

                    case R.id.medicine_detail_inventory:
//
                        break;
                }
                return false;
            }
        });

        if(savedInstanceState == null) {
            mBottomNavigationView.getMenu().performIdentifierAction(R.id.medicine_detail_search, 0);
        }
        return view;
    }
}
