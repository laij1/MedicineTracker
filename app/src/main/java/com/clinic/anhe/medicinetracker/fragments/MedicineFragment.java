package com.clinic.anhe.medicinetracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.MedicineRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;

import java.util.ArrayList;
import java.util.List;


public class MedicineFragment extends Fragment {

    private List<MedicineCardViewModel> medicineList;
    private RecyclerView mRecyclerView;
    private MedicineRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_medicine, container, false);

        prepareMedicineData();
        mRecyclerView = view.findViewById(R.id.medicine_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new MedicineRecyclerViewAdapter(medicineList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    private void prepareMedicineData(){
        medicineList = new ArrayList<>();

        medicineList.add(new MedicineCardViewModel("HDF", "", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("NESP", "20ug", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("NESP", "40ug", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("EPO", "2000", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("Carnitine", "原廠", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("Carnitine", "台廠", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("Provi", "ta", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("循利寧", "", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("Nephrosteril", "", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("IDPN", "", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("Methionin-B", "", R.drawable.ic_cart));
        medicineList.add(new MedicineCardViewModel("Anol", "", R.drawable.ic_cart));




    }
}
