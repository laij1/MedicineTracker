package com.clinic.anhe.medicinetracker.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Transaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineCategoryPagerAdapter;
import com.clinic.anhe.medicinetracker.adapters.SummaryRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;

import java.util.ArrayList;
import java.util.List;

public class SummaryFragment  extends Fragment {
    private View view;
    private TextView patientName;
    private TextView patientId;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SummaryRecyclerViewAdapter mAdapter;
    private FloatingActionButton summaryFab;
    private ViewGroup mContainer;

    //TODO
    private CartViewModel cartViewModel;
    private List<MedicineCardViewModel> medicineList;
    private List<MedicineCardViewModel> cartList;

    //TODO
    private SelectedPatientViewModel selectedPatientViewModel;

    public static SummaryFragment newInstance(){
       SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_summary, container, false);
        mContainer = container;


        cartViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(CartViewModel.class);

        patientName = view.findViewById(R.id.summary_patientname);
        patientId = view.findViewById(R.id.summary_patientid);
        summaryFab = view.findViewById(R.id.summary_fab);
        summaryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                //getChildFragmentManager().popBackStackImmediate().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                int backStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();
//                for (int i = 0; i < backStackCount; i++) {
//
//                    // Get the back stack fragment id.
//                    int backStackId = getActivity().getSupportFragmentManager().getBackStackEntryAt(i).getId();

//                getActivity().getSupportFragmentManager().popBackStack("selectp", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//                getActivity().getSupportFragmentManager().popBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                //getActivity().getSupportFragmentManager().popBackStack("summary", FragmentManager.POP_BACK_STACK_INCLUSIVE);

//                Log.d("can we get the container? "+ getActivity().findViewById(R.id.main_fragment_container, "");
//                } /* end of for */

//                view = inflater.inflate(R.layout.activity_main, mContainer, false);
                MedicineCategoryFragment fragment = MedicineCategoryFragment.newInstance();
                transaction
                        //.remove(getFragmentManager().findFragmentByTag(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT))
                          // .remove(getFragmentManager().findFragmentByTag("selectp"))
                        //.remove(getFragmentManager().findFragmentByTag("summary"))
//
//                        .replace(R.id.summary_layout,fragment,ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
//                           .addToBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                        .remove(getFragmentManager().findFragmentByTag("summary"))
                        .remove(getParentFragment())
//                        .remove(getParentFragment().getParentFragment())
//                        .add(fragment, "test")
                           //.remove(getFragmentManager().findFragmentByTag("summary"))

                           .commit();

//                getActivity().getSupportFragmentManager().popBackStack("summary", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                view.getRootView();



            }
        });

        patientId.setText(getArguments().getString("patientId"));
        patientName.setText(getArguments().getString("patientName"));

        mRecyclerView = view.findViewById(R.id.summary_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        //TODO: only pass the meds that has been selected
        cartList = new ArrayList<>();
        //all four categories meds
        medicineList = cartViewModel.getDialysisList();
        addToCartList(medicineList);
        medicineList = cartViewModel.getEdibleList();
        addToCartList(medicineList);
        medicineList = cartViewModel.getNeedleList();
        addToCartList(medicineList);
        medicineList = cartViewModel.getBandaidList();
        addToCartList(medicineList);

        mAdapter = new SummaryRecyclerViewAdapter(cartList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    private void addToCartList(List<MedicineCardViewModel> list) {
        for(MedicineCardViewModel m : list) {
            if(m.getIsAddToCart()) {
                cartList.add(m);
            }
        }
    }
}
