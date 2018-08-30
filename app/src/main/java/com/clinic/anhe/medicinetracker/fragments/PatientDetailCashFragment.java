package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.PatientDetailCashRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.PatientListRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.PaymentType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDetailCashFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PatientDetailCashRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MedicineRecordCardViewModel> recordList;
    private VolleyController volleyController;
    private Context mContext;
    private String selectedPatientName;
    private String selectedPatientIC;
    private Integer selectedPatientPID;
    private TextView mPatientName;
    private TextView mPatientIC;

    public static PatientDetailCashFragment newInstance(String selectedPatientName, String selectedPatientIC, Integer PID){
        PatientDetailCashFragment fragment  = new PatientDetailCashFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME, selectedPatientName);
        args.putString(ArgumentVariables.ARG_SELECTED_PATIENT_ID, selectedPatientIC);
        args.putInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID, PID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME, selectedPatientName);
        outState.putString(ArgumentVariables.ARG_SELECTED_PATIENT_ID, selectedPatientIC);
        outState.putInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID, selectedPatientPID);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_detail_cash, container, false);

        if(savedInstanceState != null) {
            selectedPatientName = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME);
            selectedPatientIC = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_PATIENT_ID);
            selectedPatientPID = savedInstanceState.getInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID);
        }
        if(selectedPatientName == null) {
            selectedPatientName = getArguments().getString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME);
            selectedPatientIC = getArguments().getString(ArgumentVariables.ARG_SELECTED_PATIENT_ID);
            selectedPatientPID = getArguments().getInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID);
        }
        mContext = view.getContext();

        recordList = new ArrayList<>();

        mPatientName = view.findViewById(R.id.patient_detail_cash_name);
        mPatientIC = view.findViewById(R.id.patient_detail_cash_ic);
        mPatientName.setText(selectedPatientName);
        mPatientIC.setText(selectedPatientIC);

        mRecyclerView = view.findViewById(R.id.patient_detail_cash_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new PatientDetailCashRecyclerViewAdapter(recordList, mContext, this);


        //TODO: needs to get pid from parent fragment
        String url = "http://192.168.0.4:8080/anhe/record/pid/unpaid?pid="+ selectedPatientPID;
        parseRecordListData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                mAdapter.notifyDataSetChanged();
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void parseRecordListData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);

                                        String createAt = object.getString("createAt");
                                        Integer rid = object.getInt("rid");
                                        Integer pid = object.getInt("pid");
                                        Integer mid = object.getInt("mid");
                                        String name = object.getString("medicineName");
                                        Integer quantity = object.getInt("quantity");
                                        Integer subtotal = object.getInt("subtotal");
                                        String createBy = object.getString("createBy");
                                        String payment = object.getString("payment");
                                        Log.d("medicine record jason object" , name + pid + createAt);

                                        if(payment.equalsIgnoreCase(PaymentType.CASH.toString())){
                                            recordList.add(new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                    subtotal, payment, pid, createBy));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                     Log.d("getting patient data from database", "CHLOE");
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

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }


}
