package com.clinic.anhe.medicinetracker.fragments;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.adapters.PatientsPagerAdapter;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.Shift;

import java.util.ArrayList;
import java.util.List;

public class SelectPatientsDialogFragment extends DialogFragment {
    private ViewPager mPatientsViewPager;
    private TabLayout mPatientsTabLayout;
    private PatientsPagerAdapter mPatientsPagerAdapter;
    private TextView addButton;
    private TextView cancelButton;
    private Context mContext;
    private Shift shift;
    private String nurseName;
    private DashboardViewModel dashboardViewModel;
    private List<String> list = new ArrayList<>();


    public static SelectPatientsDialogFragment newInstance(String name) {
        SelectPatientsDialogFragment fragment = new SelectPatientsDialogFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_NURSE_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
//        Log.d("OnDestoryView", "Chloe");
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ArgumentVariables.ARG_NURSE_NAME, nurseName);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            nurseName = savedInstanceState.getString(ArgumentVariables.ARG_NURSE_NAME);
        }

        if(nurseName == null) {
            nurseName = getArguments().getString(ArgumentVariables.ARG_NURSE_NAME);
        }

        Log.d(nurseName, "chloein sleect");
        View view = inflater.inflate(R.layout.fragment_dashboard_patients, container, false);
        //right here shift does not matter
        shift = Shift.morning;
        //get livedata
        dashboardViewModel = ViewModelProviders.of(getParentFragment()).get(DashboardViewModel.class);


        addButton = view.findViewById(R.id.dashboard_patients_confirmbutton);
        cancelButton = view.findViewById(R.id.dashboard_patients_cancelbutton);
        mPatientsTabLayout = (TabLayout) view.findViewById(R.id.dashboard_patients_tabLayout);

        //set up tab
        TabLayout.Tab OddDays = mPatientsTabLayout.newTab();
        mPatientsTabLayout.addTab(OddDays);

        TabLayout.Tab EvenDays = mPatientsTabLayout.newTab();
        mPatientsTabLayout.addTab(EvenDays);

        mContext = getContext();

        mPatientsTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPatientsViewPager = (ViewPager) view.findViewById(R.id.dashboard_patients_pager);
        mPatientsTabLayout.setupWithViewPager(mPatientsViewPager);
        mPatientsPagerAdapter = new PatientsPagerAdapter
                (getChildFragmentManager(), mPatientsTabLayout.getTabCount(), mContext, shift, ArgumentVariables.KIND_DASHBOARD_PATIENTS, nurseName);
        mPatientsViewPager.setAdapter(mPatientsPagerAdapter);

        highLightCurrentTab(0);


        mPatientsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mPatientsTabLayout) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });
        mPatientsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPatientsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
////                Toast.makeText(getActivity(), "confirm is clicked!", Toast.LENGTH_LONG).show();
//            }
//        });

//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
////                Toast.makeText(getActivity(), "cancel is clicked!", Toast.LENGTH_LONG).show();
//            }
//        });

        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);

        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dashboardViewModel.getSelectedPatientsList().removeAll(dashboardViewModel.getSelectedPatientsList());
//                if(dashboardViewModel.getDashboardMap().containsKey(nurseName)) {
//                    for(String s: dashboardViewModel.getDashboardMap().get(nurseName)){
//                        list.add(s);
//                    }
//                }
//                list.addAll(dashboardViewModel.getSelectedPatientsList());
//                dashboardViewModel.getDashboardMap().put(nurseName,list);
//                dashboardViewModel.getDashboardMapLiveData().setValue(dashboardViewModel.getDashboardMap());
//                //clearup the patient list live data
//                dashboardViewModel.getSelectedPatientsList().removeAll(dashboardViewModel.getSelectedPatientsList());
                dismiss();
//                Toast.makeText(getActivity(), "confirm is clicked", Toast.LENGTH_LONG).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardViewModel.getSelectedPatientsList().removeAll(dashboardViewModel.getSelectedPatientsList());
                dismiss();
//                Toast.makeText(getActivity(), "cancel is clicked", Toast.LENGTH_LONG).show();
            }
        });

        setRetainInstance(true);
        return view;
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < mPatientsPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mPatientsTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mPatientsPagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mPatientsTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mPatientsPagerAdapter.getSelectedTabView(position));
    }


}
