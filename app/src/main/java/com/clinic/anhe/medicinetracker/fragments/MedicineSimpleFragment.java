package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.MedicineSimpleRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.MedicineType;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;

import java.util.List;

public class MedicineSimpleFragment extends Fragment {

    private MedicineType medicineType;
    private RecyclerView mRecyclerView;
    private MedicineSimpleRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static List<MedicineCardViewModel> medicineList;

    public static MedicineSimpleFragment newInstance(MedicineType medicineType, List<MedicineCardViewModel> mList) {
        MedicineSimpleFragment fragment = new MedicineSimpleFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_MEDICINE_TYPE, medicineType.toString());
        fragment.setArguments(args);
        medicineList = mList;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: medicineType could be null....
        outState.putString(ArgumentVariables.ARG_MEDICINE_TYPE, medicineType.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view  = inflater.inflate(R.layout.fragment_medicine_simple, container, false);

        if(savedInstanceState != null) {
            medicineType = medicineType.fromString(savedInstanceState.getString(ArgumentVariables.ARG_MEDICINE_TYPE));
        }

        if(medicineType == null) {
            medicineType = medicineType.fromString(getArguments().getString(ArgumentVariables.ARG_MEDICINE_TYPE));
        }
//

        prepareData();
        mRecyclerView = view.findViewById(R.id.medicine_simple_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(getContext());
         mLayoutManager = new GridLayoutManager(getContext(), 2);

        mAdapter = new MedicineSimpleRecyclerViewAdapter(medicineType, medicineList);

//        // and associate them to the adapter.
//        if(medicineType == MedicineType.dialysis) {
//            cartViewModel.getDialysisLiveData().observe(getParentFragment(), new Observer<List<MedicineCardViewModel>>() {
//                @Override
//                public void onChanged(@Nullable final List<MedicineCardViewModel> dialysisList) {
//                    // Update the cached copy of the words in the adapter.
//                    mAdapter.setList(dialysisList);
//                }
//            });
//        } else if(medicineType == MedicineType.edible) {
//            cartViewModel.getEdibleLiveData().observe(getParentFragment(), new Observer<List<MedicineCardViewModel>>() {
//                @Override
//                public void onChanged(@Nullable List<MedicineCardViewModel> edibleList) {
//                    mAdapter.setList(edibleList);
//                }
//            });
//        } else if(medicineType == MedicineType.needle) {
//            cartViewModel.getNeedleLiveData().observe(getParentFragment(), new Observer<List<MedicineCardViewModel>>() {
//                @Override
//                public void onChanged(@Nullable List<MedicineCardViewModel> needleList) {
//                    mAdapter.setList(needleList);
//                }
//            });
//        } else {
//            cartViewModel.getBandaidLiveData().observe(getParentFragment(), new Observer<List<MedicineCardViewModel>>() {
//                @Override
//                public void onChanged(@Nullable List<MedicineCardViewModel> bandaidList) {
//                    mAdapter.setList(bandaidList);
//                }
//            });
//
//        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



//        mCounterFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                //cannot use getSupportFragmentManger(), it is for calling from activity, use getChildFragmentManager
//                //https://stackoverflow.com/questions/7508044/android-fragment-no-view-found-for-id
//               // FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
//               FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//
//                SelectPatientFragment selectPatientFragment = SelectPatientFragment.newInstance();
////                Bundle args = new Bundle();
////                ArrayList<String> cartlist = new ArrayList<>();
////                for(MedicineCardViewModel item :cartViewModel.getMedicineList()) {
////                    if(item.getIsAddToCart() == true) {
////                        cartlist.add(item.getMedicinName());
////                    }
////                }
////                args.putStringArrayList(ArgumentVariables.ARG_CARTLIST, cartlist);
////                selectPatientFragment.setArguments(args);
//                transaction.replace(R.id.medicine_layout, selectPatientFragment)
//                        .addToBackStack("medicine")
//                        .commit();
//            }
//
//
//        });

//setRetainInstance to true is important so that onSaveInstanceState will work
        setRetainInstance(true);
        return view;
    }

    private void prepareData() {
        Log.d("is list null?", medicineList.size()+ "");
    }
}
