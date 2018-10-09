package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.adapters.DashboardPatientRecyclerViewAdapter;

import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.DayType;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.Shift;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardPatientsFragment extends Fragment implements ArgumentVariables{

    private List<PatientsCardViewModel> patientList;
    private RecyclerView mRecyclerView;
    private DashboardPatientRecyclerViewAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private Shift shift;
    private DayType dayType;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private Context mContext;
    private DashboardViewModel dashboardViewModel;
    private String nurseName;


    public static DashboardPatientsFragment newInstance(DayType dayType, Shift shift, String nurseName) {
        DashboardPatientsFragment fragment = new DashboardPatientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAY_TYPE, dayType.toString());
        args.putString(ARG_NURSE_NAME, nurseName);
        args.putString(ARG_PATIENT_SHIFT, shift.toString());
//        nurseName = nurseName;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_DAY_TYPE, dayType.toString());
        outState.putString(ARG_NURSE_NAME, nurseName);
        outState.putString(ARG_PATIENT_SHIFT, shift.toString());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dashboardViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(DashboardViewModel.class);


        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_patient_day, container, false);
        mContext = getContext();
        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        if(savedInstanceState != null) {
            dayType = dayType.fromString(savedInstanceState.getString(ARG_DAY_TYPE));
            nurseName= savedInstanceState.getString(ARG_NURSE_NAME);
            shift = Shift.fromString(savedInstanceState.getString(ARG_PATIENT_SHIFT));
        }

        if(dayType == null) {
            dayType= dayType.fromString(getArguments().getString(ARG_DAY_TYPE));
            nurseName = getArguments().getString(ARG_NURSE_NAME);
            shift = Shift.fromString(getArguments().getString(ARG_PATIENT_SHIFT));
        }

        Log.d("shift is from dashboardpatients", shift.toString());
        Log.d("nurseName in dashboardPatientFragment", nurseName);
//
        preparePatientData();

        mRecyclerView = view.findViewById(R.id.patient_day_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        //here we need to filter
        mAdapter = new DashboardPatientRecyclerViewAdapter(patientList, dashboardViewModel, nurseName, shift);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        refreshRecyclerview();

        setRetainInstance(true);
        return view;

    }

    public void refreshRecyclerview() {
        mAdapter.notifyDataSetChanged();
    }

    private void preparePatientData() {
        patientList = new ArrayList<>();
        String url = "";
        if(dayType == DayType.evenDay) {
            url = "http://" + ip + ":" + port + "/anho/patient/day?day=二四六";
            parsePatientList(url, new VolleyCallBack() {
                @Override
                public void onResult(VolleyStatus status) {
                    if(status == VolleyStatus.SUCCESS) {
                        mAdapter.notifyDataSetChanged();
                        }
                }
            });
        } else {
            url = "http://" + ip + ":" + port + "/anho/patient/day?day=一三五";
            parsePatientList(url, new VolleyCallBack() {
                @Override
                public void onResult(VolleyStatus status) {
                    if(status == VolleyStatus.SUCCESS) {
                        mAdapter.notifyDataSetChanged();

                    }
                }
            });
            }
        }



    private void parsePatientList(String url, final VolleyCallBack volleyCallBack) {

        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                boolean addToSecond = false;
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        Integer pid = object.getInt("pid");
                                        String name = object.getString("name");
                                        String shift = object.getString("shift");
                                        String ic = object.getString("ic");
                                        String day = object.getString("day");
                                        PatientsCardViewModel p = new PatientsCardViewModel(pid, name, ic, shift, day);
//                                        Log.d("patient jason object" , name + pid + shift + day + ic);
                                        if(pid == 2) {
                                            patientList.add(0,p);
                                            addToSecond = true;
                                        } else if(shift.equalsIgnoreCase("早班")) {
                                            if( addToSecond ) {
                                                patientList.add(1,p);
                                            } else {
                                                patientList.add(0,p);
                                            }
                                        } else {
                                            patientList.add(p);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // Log.d("getting patient data from database", "CHLOE");
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
