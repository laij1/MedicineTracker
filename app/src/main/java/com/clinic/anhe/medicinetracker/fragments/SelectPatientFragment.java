package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.Shift;

public class SelectPatientFragment extends Fragment {

    private RadioGroup mRadioGroup;
    private RadioButton mCheckedRadioButton;
    private View view;
    private FloatingActionButton mFloatingActionButton;
    private SelectedPatientViewModel selectedPatientViewModel;

    String patientName = "";
    String paitientId = "";

    public static SelectPatientFragment newInstance(){
        SelectPatientFragment fragment = new SelectPatientFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        ArrayList<String> cartList = getArguments().getStringArrayList(ArgumentVariables.ARG_CARTLIST);

        view = inflater.inflate(R.layout.fragment_select_patients, container, false);
        mRadioGroup = view.findViewById(R.id.shift_radiogroup);
        mFloatingActionButton = view.findViewById(R.id.patients_fab);

        //set default view for shift_fragment_container
        selectItem(mRadioGroup, R.id.morning_radiobutton);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectItem(group, checkedId);
            }
        });


        mFloatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
              //Log.d("I got the patient name", OddDayFragment.selectedPatientViewModel.getPatientLiveData().getValue().getPatientName());
                selectedPatientViewModel = ViewModelProviders.of(getChildFragmentManager().findFragmentByTag(ArgumentVariables.TAG_SELECT_PATIENT_FRAGMENT)).get(SelectedPatientViewModel.class);
                selectedPatientViewModel.getPatientLiveData().observe(getActivity(), new Observer<PatientsCardViewModel>() {
                    @Override
                    public void onChanged(@Nullable PatientsCardViewModel patientsCardViewModel) {
                        patientName = selectedPatientViewModel.getPatientLiveData().getValue().getPatientName();
                        paitientId = selectedPatientViewModel.getPatientLiveData().getValue().getPatientId();
                        Log.d("I have the selected patient in selectpatientfragment", patientsCardViewModel.getPatientName());
                    }
                });
              //TODO:
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                SummaryFragment summaryFragment = SummaryFragment.newInstance();
                Bundle args = new Bundle();
//                args.putStringArrayList(ArgumentVariables.ARG_CARTLIST, getArguments().getStringArrayList(ArgumentVariables.ARG_CARTLIST));
                args.putString("patientName", patientName);
                args.putString("patientId", paitientId);
                summaryFragment.setArguments(args);
                transaction.replace(R.id.select_patient_layout, summaryFragment)
                           .commit();
            }
        });
        //setRetainInstance does not work
        //setRetainInstance(true);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void selectItem(RadioGroup group, int checkedId) {
        //change all radio button to unchecked color
        changeItemBackgroundTint();
        //use child fragment manager here
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch(checkedId){
            case R.id.morning_radiobutton:
                mCheckedRadioButton = view.findViewById(R.id.morning_radiobutton);
                mCheckedRadioButton.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)));
                PatientsFragment morningFragment = PatientsFragment.newInstance(Shift.morning);
                transaction.replace(R.id.shift_fragment_container, morningFragment, ArgumentVariables.TAG_SELECT_PATIENT_FRAGMENT)
                           .addToBackStack("patient")
                           .commit();
                break;
            case R.id.afternoon_radiobutton:
                mCheckedRadioButton = view.findViewById(R.id.afternoon_radiobutton);
                mCheckedRadioButton.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)));
                PatientsFragment afternoonFragment = PatientsFragment.newInstance(Shift.afternoon);
                transaction.replace(R.id.shift_fragment_container, afternoonFragment, ArgumentVariables.TAG_SELECT_PATIENT_FRAGMENT)
                           .addToBackStack("patient")
                           .commit();
                break;
            case R.id.night_radiobutton:
                mCheckedRadioButton = view.findViewById(R.id.night_radiobutton);
                mCheckedRadioButton.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)));
                PatientsFragment nightFragment = PatientsFragment.newInstance(Shift.night);
                transaction.replace(R.id.shift_fragment_container, nightFragment,ArgumentVariables.TAG_SELECT_PATIENT_FRAGMENT)
                           .addToBackStack("patient")
                           .commit();
                break;
        }
    }

    private void changeItemBackgroundTint() {
        int count = mRadioGroup.getChildCount();
        for(int i = 0; i < count; i++) {
            View v = mRadioGroup.getChildAt(i);
            if (v instanceof RadioButton) {
                ((RadioButton)v).setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.menuTextIconColor)));
            }
        }

    }

}
