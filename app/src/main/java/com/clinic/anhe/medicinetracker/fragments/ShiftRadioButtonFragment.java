package com.clinic.anhe.medicinetracker.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.Shift;

public class ShiftRadioButtonFragment extends Fragment {

    private RadioGroup mRadioGroup;
    private RadioButton mCheckedRadioButton;
    private View view;

    public static ShiftRadioButtonFragment newInstance(){
        ShiftRadioButtonFragment fragment = new ShiftRadioButtonFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shift_radio_button, container, false);
        mRadioGroup = view.findViewById(R.id.shift_radiogroup);

        //set default view for shift_fragment_container
        selectItem(mRadioGroup, R.id.morning_radiobutton);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectItem(group, checkedId);
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
                transaction.replace(R.id.shift_fragment_container,morningFragment).commit();
                break;
            case R.id.afternoon_radiobutton:
                mCheckedRadioButton = view.findViewById(R.id.afternoon_radiobutton);
                mCheckedRadioButton.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)));
                PatientsFragment afternoonFragment = PatientsFragment.newInstance(Shift.afternoon);
                transaction.replace(R.id.shift_fragment_container,afternoonFragment).commit();
                break;
            case R.id.night_radiobutton:
                mCheckedRadioButton = view.findViewById(R.id.night_radiobutton);
                mCheckedRadioButton.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)));
                PatientsFragment nightFragment = PatientsFragment.newInstance(Shift.night);
                transaction.replace(R.id.shift_fragment_container,nightFragment).commit();
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
