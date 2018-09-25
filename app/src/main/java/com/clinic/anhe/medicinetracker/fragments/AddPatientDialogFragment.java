package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.utils.DayType;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.Shift;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;

public class AddPatientDialogFragment extends DialogFragment {

    private RadioGroup mDayRadioGroup;
    private RadioGroup mShiftGroup;

    private EditText mPatientName;
    private EditText mPatientIC;

    private TextView mConfirmButton;
    private TextView mCancelButton;

    private static PatientListDayFragment parent;
    private Context mContext;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String day="";
    private String shift="";


    public static AddPatientDialogFragment newInstance(PatientListDayFragment parentFrag) {
        AddPatientDialogFragment fragment = new AddPatientDialogFragment();
        parent= parentFrag;
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
        View view = inflater.inflate(R.layout.dialog_add_patient, container, false);

        mContext = view.getContext();

        mDayRadioGroup = view.findViewById(R.id.add_patient_day_radiogroup);
        mShiftGroup = view.findViewById(R.id.add_patient_shift_radiogroup);
        mPatientName = view.findViewById(R.id.add_patient_name);
        mPatientIC = view.findViewById(R.id.add_patient_ic);

        mConfirmButton = view.findViewById(R.id.add_patient_confirmbutton);
        mCancelButton = view.findViewById(R.id.add_patient_cancelbutton);

        mDayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.add_patient_oddday:
                        day = "一三五";
                        break;
                    case R.id.add_patient_evenday:
                        day = "二四六";
                        break;
                }
            }
        });

        mShiftGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.add_patient_morning:
                        shift = Shift.morning.toString();
                        break;
                    case R.id.add_patient_afternoon:
                        shift = Shift.afternoon.toString();
                        break;
                    case R.id.add_patient_night:
                        shift = Shift.night.toString();
                        break;
                }
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: first check if day and shift is clicked
                if(day.equals("")) {
                    Toast.makeText(mContext, "請選擇週時", Toast.LENGTH_SHORT).show();
                } else if(shift.equals("")) {
                    Toast.makeText(mContext, "請選擇班次", Toast.LENGTH_SHORT).show();
                } else {
                    addPatientToDatabase(new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                Toast.makeText(mContext,"加入病患成功" , Toast.LENGTH_SHORT).show();
                                parent.refreshrecyclerView();
                                dismiss();
                            }
                        }
                    });

                }

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


    public void addPatientToDatabase(final VolleyCallBack volleyCallBack) {

        String url = "http://" + globalVariable.getInstance().getIpaddress() +
                ":" + globalVariable.getInstance().getPort() + "/anhe/patient/add?name=" + mPatientName.getText().toString()
                + "&ic=" + mPatientIC.getText().toString() + "&day=" + day + "&shift=" + shift;
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("saved")) {
                                    volleyCallBack.onResult(VolleyStatus.SUCCESS);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance(mContext).addToRequestQueue(stringRequest);
    }
}
