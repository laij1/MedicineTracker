package com.clinic.anhe.medicinetracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.view.KeyEvent;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.MedicineRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.Shift;

import java.util.ArrayList;
import java.util.List;



public class MedicineFragment extends Fragment implements View.OnKeyListener {

    private List<MedicineCardViewModel> medicineList;
    private RecyclerView mRecyclerView;
    private MedicineRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CounterFab mCounterFab;
    private ImageView mBottomImageView;
    private static MedicineFragment medicineFragment;

    public static MedicineFragment getInstance(){
        if(medicineFragment == null) {
            Log.d("Instance is first time crated!!!", "CHLOE!!");
            return new MedicineFragment();
        }
        Log.d("Instance is already crated!!!", "CHLOE!!");
        return medicineFragment;
    }




//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        FragmentManager manager = getFragmentManager();
//        if (savedInstanceState != null) {
//            medicineFragment = (MedicineFragment) manager.getFragment(savedInstanceState, "MedicineFragment");
//        }
//        Log.d("on restore instance is called", "CHLOE!!");
//        //restore will be called!
//
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        //super.onSaveInstanceState(outState);
//        FragmentManager manager = getFragmentManager();
//        manager.putFragment(outState, "MedicineFragment", medicineFragment);
//
//        Log.d("on save instance is called", "CHLOE!!");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        onSaveInstanceState(new Bundle());
//        //this will be called
//        Log.d("the view has been destoried!", "CHLOE!!!");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("the fragment has been destoried!", "CHLOE!!!");
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_medicine, container, false);

        prepareMedicineData();
        mRecyclerView = view.findViewById(R.id.medicine_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mCounterFab = view.findViewById(R.id.medicine_fab);
        mBottomImageView = view.findViewById(R.id.medicine_add_button);
        mAdapter = new MedicineRecyclerViewAdapter(medicineList, mCounterFab);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        mCounterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                PatientsFragment morningFragment = new PatientsFragment(Shift.morning);
                transaction.replace(R.id.medicine_layout, morningFragment)
                        .addToBackStack("medicine")
                        .commit();
                mCounterFab.setVisibility(View.GONE);
            }


        });

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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                ((FragmentActivity)getContext()).getSupportFragmentManager().popBackStack();
                mCounterFab.setVisibility(View.VISIBLE);
                return true;
            }
        }

        return false;
    }
}
