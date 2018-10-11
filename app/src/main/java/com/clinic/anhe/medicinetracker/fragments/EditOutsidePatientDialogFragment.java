package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clinic.anhe.medicinetracker.R;


public class EditOutsidePatientDialogFragment extends DialogFragment {
    private EditText mName;
    private TextView mConfirmButton;
    private TextView mCancelButton;
    private Context mContext;
    private static SummaryFragment.EditOutsidePatientCallBack editOutsidePatientCallBack;

    public static EditOutsidePatientDialogFragment newInstance(SummaryFragment.EditOutsidePatientCallBack callback ) {
        EditOutsidePatientDialogFragment fragment = new EditOutsidePatientDialogFragment();
        editOutsidePatientCallBack = callback;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_outside_patient, container, false);

        mContext = view.getContext();

        mName = view.findViewById(R.id.edit_outside_patient_name);
        mConfirmButton = view.findViewById(R.id.edit_outside_patient_confirmbutton);
        mCancelButton = view.findViewById(R.id.edit_outside_patient_cancelbutton);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOutsidePatientCallBack.patientName(mName.getText().toString());
                Toast.makeText(mContext, "院外人士姓名設定為" +mName.getText().toString(), Toast.LENGTH_SHORT ).show();
                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);

        }

        setRetainInstance(true);
        return view;

    }

}
