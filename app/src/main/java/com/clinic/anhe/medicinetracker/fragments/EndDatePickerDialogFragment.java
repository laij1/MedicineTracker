package com.clinic.anhe.medicinetracker.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EndDatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static Context mContext;
    private static PatientDetailSearchFragment mFrag;

    public static EndDatePickerDialogFragment newInstance(PatientDetailSearchFragment frag, Context c){
        mContext = c;
        mFrag = frag;
        EndDatePickerDialogFragment fragment = new EndDatePickerDialogFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this, year, month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        Date date = cal.getTime();
        String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        mFrag.setEndDateTextView(endDate);
    }
}
