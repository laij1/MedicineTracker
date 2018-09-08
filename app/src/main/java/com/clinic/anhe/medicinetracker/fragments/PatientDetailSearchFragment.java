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
import com.clinic.anhe.medicinetracker.adapters.PatientDetailSearchRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientDetailSearchFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PatientDetailSearchRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MedicineRecordCardViewModel> recordList;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private Context mContext;
    private String selectedPatientName;
    private String selectedPatientIC;
    private Integer selectedPatientPID;
    private TextView mPatientName;
    private TextView mPatientIC;
    String url = "";

    public static PatientDetailSearchFragment newInstance(String selectedPatientName, String selectedPatientIC, Integer PID) {

        PatientDetailSearchFragment fragment = new PatientDetailSearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_patient_detail_search, container, false);
        if (savedInstanceState != null) {
            selectedPatientName = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME);
            selectedPatientIC = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_PATIENT_ID);
            selectedPatientPID = savedInstanceState.getInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID);
        }
        if (selectedPatientName == null) {
            selectedPatientName = getArguments().getString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME);
            selectedPatientIC = getArguments().getString(ArgumentVariables.ARG_SELECTED_PATIENT_ID);
            selectedPatientPID = getArguments().getInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID);
        }
        mContext = view.getContext();

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        recordList = new ArrayList<>();

        Log.d("selected patient in patient month detail", selectedPatientName + selectedPatientIC + selectedPatientPID);
        mPatientName = view.findViewById(R.id.patient_detail_search_name);
        mPatientIC = view.findViewById(R.id.patient_detail_search_ic);
        mPatientName.setText(selectedPatientName);
        mPatientIC.setText(selectedPatientIC);

        mRecyclerView = view.findViewById(R.id.patient_detail_search_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new PatientDetailSearchRecyclerViewAdapter(recordList);


        //TODO: needs to get pid from parent fragment
        url = "http://" + ip + ":" + port + "/anhe/record/createdate?pid=" + selectedPatientPID
                + "&start=2018-09-01";
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
                                        String chargeAt = object.getString("chargeAt");
                                        String chargeBy = object.getString("chargeBy");
                                        Log.d("medicine record jason object" , name + pid + createAt);
                                        MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                    subtotal, payment, pid, createBy);
                                        item.setChargeAt(chargeAt);
                                        item.setChargeBy(chargeBy);
                                        recordList.add(item);

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

    public void refreshRecyclerView(int index) {
        recordList.remove(index);
        mAdapter.notifyDataSetChanged();


    }
}
