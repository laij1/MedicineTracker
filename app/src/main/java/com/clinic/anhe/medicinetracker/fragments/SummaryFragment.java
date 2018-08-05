package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.adapters.SummaryRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class SummaryFragment  extends Fragment {
    private View view;
    private TextView patientName;
    private TextView patientId;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SummaryRecyclerViewAdapter mAdapter;

    //TODO
    private CartViewModel cartViewModel;
    private List<MedicineCardViewModel> medicineList;
    private List<MedicineCardViewModel> cartList;

    public static SummaryFragment newInstance(){
       SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_summary, container, false);

//        cartlist = getArguments().getStringArrayList(ArgumentVariables.ARG_CARTLIST);
        cartViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(CartViewModel.class);
        medicineList = cartViewModel.getMedicineList();

        patientName = view.findViewById(R.id.summary_patientname);
        patientId = view.findViewById(R.id.summary_patientid);

        patientName.setText(getArguments().getString("patientName"));
        patientId.setText(getArguments().getString("patientId"));

        mRecyclerView = view.findViewById(R.id.summary_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        //TODO: only pass the meds that has been selected
        cartList= new ArrayList<>();
        for(MedicineCardViewModel m : medicineList) {
            if(m.getIsAddToCart()) {
                cartList.add(m);
            }
        }
        mAdapter = new SummaryRecyclerViewAdapter(cartList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }
}
